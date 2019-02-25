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
package org.soulwing.jwt.transformer;

import java.util.Properties;
import javax.xml.transform.Transformer;

import org.soulwing.jwt.api.ClaimTransformer;

/**
 * An {@link AtttributeTransformer} that flattens the character case of a
 * string.
 *
 * @author Carl Harris
 */
public class FlattenCaseTransformer
    extends ClaimTransformer<String, String> {

  public static final String USE_UPPER_CASE = "use-upper-case";
  
  private boolean useUpperCase;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(Properties properties) {
    super.initialize(properties);
    useUpperCase = Boolean.parseBoolean(properties.getProperty(USE_UPPER_CASE,
        Boolean.FALSE.toString()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String transform(String value) {
    if (useUpperCase) {
      return value.toUpperCase();
    }
    return value.toLowerCase();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format("%s(%s=%s)",
        getClass().getSimpleName().replaceFirst(
            Transformer.class.getSimpleName() + "$", ""),
            USE_UPPER_CASE, useUpperCase);
  }


}
