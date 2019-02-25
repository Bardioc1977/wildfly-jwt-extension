/*
 * File created on Feb 19, 2019
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
package org.soulwing.jwt.deployment;

/**
 * An object that provides the configuration obtained from an application's
 * JWT descriptor.
 *
 * @author Carl Harris
 */
class AppConfiguration {

  public static final String DEFAULT_PROFILE = "default";

  private String profileId = DEFAULT_PROFILE;
  private boolean addDependencies;
  
  /**
   * Gets the identifier of the configuration profile.
   * @return configuration profile specified in the deployment descriptor
   */
  public String getProfileId() {
    return profileId;
  }

  /**
   * Sets the identifier of the configuration profile.
   * @param profileId the value to set
   */
  public void setProfileId(String profileId) {
    if (profileId == null || profileId.isEmpty()) {
      profileId = DEFAULT_PROFILE;
    }
    this.profileId = profileId;
  }

  /**
   * Gets the {@code addDependencies} property.
   * @return property value
   */
  public boolean isAddDependencies() {
    return addDependencies;
  }

  /**
   * Sets the {@code addDependencies} property.
   * @param addDependencies the value to set
   */
  public void setAddDependencies(boolean addDependencies) {
    this.addDependencies = addDependencies;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format("{ profileId=%s addDependencies=%s }", 
        profileId, addDependencies);
  }

}
