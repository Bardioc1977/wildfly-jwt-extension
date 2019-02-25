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

import java.util.Arrays;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.soulwing.jwt.extension.Names;

/**
 * An abstract base for {@link DescriptorReader} implementations.
 *
 * @author Carl Harris
 */
abstract class AbstractDescriptorReader implements DescriptorReader {

  private final String namespaceUri;
  private final String localName;
  private final List<AbstractDescriptorReader> children;
  
  protected DescriptorParser parser;
  
  AbstractDescriptorReader(String localName,
      AbstractDescriptorReader... children) {
    this(Names.NAMESPACE, localName, children);
  }

  private AbstractDescriptorReader(String namespaceUri, String localName,
      AbstractDescriptorReader... children) {
    this.namespaceUri = namespaceUri;
    this.localName = localName;
    this.children = Arrays.asList(children);
  }

  @Override
  public void init(DescriptorParser parser) {
    this.parser = parser;
  }

  /**
   * Tests whether this reader supports a given element.
   * @param namespaceUri namespace of the element
   * @param localName local name of the element
   * @return {@code true} if this reader can handle the specified element
   */
  boolean supports(String namespaceUri, String localName) {
    return this.namespaceUri.equals(namespaceUri)
        && this.localName.equals(localName);
  }

  @Override
  public void startElement(XMLStreamReader reader, String namespaceUri,
      String localName, AppConfiguration config) throws XMLStreamException {
    for (AbstractDescriptorReader child : children) {
      if (child.supports(namespaceUri, localName)) {
        parser.push(child);
        child.attributes(reader, config);
        return;
      }
    }
  }

  @Override
  public void endElement(XMLStreamReader reader, String namespaceUri,
      String localName, AppConfiguration config) throws XMLStreamException {
    if (!this.namespaceUri.equals(namespaceUri)
        && this.localName.equals(localName)) {
      throw new XMLStreamException("unexpected end element", 
          reader.getLocation());
    }
    
    parser.pop();
  }

  /**
   * Notifies the recipient of attributes associated with the start element.
   * @param reader XML stream being parsed
   * @param config application configuration
   * @throws XMLStreamException
   */
  protected void attributes(XMLStreamReader reader, AppConfiguration config)
      throws XMLStreamException {
    if (reader.getAttributeCount() > 0) {
      throw new XMLStreamException("unexpected attributes(s)", 
          reader.getLocation());
    }
  }

  @Override
  public void characters(XMLStreamReader reader,
      AppConfiguration config) throws XMLStreamException {
    if (!reader.getText().trim().isEmpty()) {
      throw new XMLStreamException("unexpected text", reader.getLocation());
    }
  }

}
