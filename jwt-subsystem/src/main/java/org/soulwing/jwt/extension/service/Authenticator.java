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
package org.soulwing.jwt.extension.service;

/**
 * A stateless component that validates a JWT token.
 * <p>
 * An instance of this class may be used to validate an arbitrary number of
 * authentication requests and may be invoked currently by multiple threads.
 *
 * @author Carl Harris
 */
public interface Authenticator {

  /**
   * Validates the given token and returns the corresponding {@link Credential}
   * object.
   * @param token the bearer token
   * @return credential object representing a valid authentication token
   * @throws AuthenticationException if the token is not valid; e.g. expired,
   *    corrupt, unrecognized issuer, etc.
   */
  Credential validate(String token) throws AuthenticationException;

}
