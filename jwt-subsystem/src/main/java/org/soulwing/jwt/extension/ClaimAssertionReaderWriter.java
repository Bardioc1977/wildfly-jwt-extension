/*
 * File created on Feb 21, 2019
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
package org.soulwing.jwt.extension;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * A reader/writer for {@code claim-assertion} resource.
 *
 * @author Carl Harris
 */
class ClaimAssertionReaderWriter extends AbstractResourceReaderWriter {

  private static final PredicateReaderWriter PREDICATE_RW =
      new PredicateReaderWriter();

  ClaimAssertionReaderWriter() {
    super(Names.CLAIM_ASSERTION, PREDICATE_RW);
  }

  @Override
  protected void handleAttributes(XMLStreamReader reader)
      throws XMLStreamException {
    super.handleAttributes(reader);
  }

}