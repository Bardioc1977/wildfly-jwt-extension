/*
 * File created on Apr 3, 2019
 *
 * Copyright (c) 2019 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.jwt.extension.jaas;

import static org.soulwing.jwt.extension.jaas.JaasLogger.LOGGER;

import java.security.AccessController;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.as.core.security.RealmRole;
import org.jboss.as.core.security.RealmUser;
import org.jboss.as.core.security.SubjectUserInfo;
import org.jboss.as.domain.management.AuthMechanism;
import org.jboss.as.domain.management.AuthorizingCallbackHandler;
import org.jboss.as.domain.management.SecurityRealm;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;

/**
 * A JAAS {@code LoginModule} that extends the base 
 * {@link JwtLoginModule} by delegating authorization to
 * a configured security realm.
 * <p>
 * The realm delegate is specified using the {@link #REALM} module option.
 * The default realm is {@link #DEFAULT_REALM}.
 *
 * @author Carl Harris
 */
public class DelegatingJwtLoginModule
    extends JwtLoginModule {

  public static final String REALM = "realm";
  
  public static final String DEFAULT_REALM = "ApplicationRealm";
  
  private String realmName;
  
  private SecurityRealm realm;
  
  private AuthorizingCallbackHandler authorizingCallbackHandler;
  
  @Override
  public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String, ?> sharedState, Map<String, ?> options) {
    super.initialize(subject, callbackHandler, sharedState, options);
    
    realmName = (String) options.get(REALM);
    if (realmName == null) {
      realmName = DEFAULT_REALM;
    }
    
    final ServiceController<?> controller = serviceContainer().getService(
        SecurityRealm.ServiceUtil.createServiceName(realmName));
    if (controller != null) {
      realm = (SecurityRealm) controller.getValue();
    }    
    if (realm == null) {
      throw new IllegalArgumentException("realm '" + realmName + "' not found");
    }
    
    final Set<AuthMechanism> mechs = realm.getSupportedAuthenticationMechanisms();
    if (mechs.isEmpty()) {
      throw new IllegalArgumentException("realm '" + realmName
          + "' does not support any authentication mechanisms");
    }
    
    AuthMechanism mech = AuthMechanism.PLAIN;
    if (!mechs.contains(mech)) {
      mech = mechs.iterator().next();
    }

    authorizingCallbackHandler = realm.getAuthorizingCallbackHandler(mech);
    if (authorizingCallbackHandler == null) {
      throw new IllegalArgumentException("realm '" + realmName
          + "' does not provide authorization");
    }  
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("attached to realm '" + realmName + "'");
    }
  }

  @Override
  protected Set<Principal> getRoles() throws LoginException {
    final Set<Principal> roles = super.getRoles();
    try {
      RealmUser user = new RealmUser(credential.getPrincipal().getName());
      SubjectUserInfo subjectUserInfo = authorizingCallbackHandler
          .createSubjectUserInfo(Collections.<Principal>singleton(user));
      Set<RealmRole> realmRoles = subjectUserInfo.getSubject()
          .getPrincipals(RealmRole.class);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("user '" + user + '@' + realmName + "' has roles " 
            + realmRoles);
      }
      for (RealmRole role : realmRoles) {
        roles.add(createRole(role.getName()));
      }
    }
    catch (Exception ex) {
      LOGGER.error("error getting realm roles: " + ex, ex);
      throw new LoginException("error getting realm roles: " + ex);
    }
    return roles;
  }
 
  private static ServiceContainer serviceContainer() {
    if (System.getSecurityManager() == null) {
      return CurrentServiceContainer.getServiceContainer();
    }
    return AccessController.doPrivileged(CurrentServiceContainer.GET_ACTION);
  }
  
}
