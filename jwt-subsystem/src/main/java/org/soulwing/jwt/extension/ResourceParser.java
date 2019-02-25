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
package org.soulwing.jwt.extension;

import java.util.List;
import java.util.Set;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.dmr.ModelNode;

/**
 * A parser for a system resource configuration.
 *
 * @author Carl Harris
 */
public interface ResourceParser extends XMLStreamConstants {

  /**
   * Parse an element and all of its nested content.
   *
   * @param reader the stream reader
   * @param ops empty list of operations that will be appended as the
   *    parsing operation discovers management elements
   * @throws XMLStreamException if an error occurs
   */
  void readElement(XMLStreamReader reader, List<ModelNode> ops)
      throws XMLStreamException;

  /**
   * Pushes a reader onto to the top of the parser stack.
   * @param reader
   */
  void push(ResourceReader reader);
  
  /**
   * Pops a reader from the top of the parser stack.
   */
  void pop();
  
  /**
   * Throws an exception for an unexpected element.
   * @param reader the reader being parsed
   * @throws XMLStreamException
   */
  void unexpectedElement(XMLStreamReader reader) throws XMLStreamException;
  
  /**
   * Throws an exception for an unexpected end element.
   * @param reader the reader being parsed
   * @throws XMLStreamException
   */
  void unexpectedEndElement(XMLStreamReader reader) throws XMLStreamException;
  
  /**
   * Throws an exception for an unexpected attribute.
   * @param reader the reader being parsed
   * @param index index of the unexpected attribute
   * @throws XMLStreamException
   */
  void unexpectedAttribute(XMLStreamReader reader, int index) 
      throws XMLStreamException;
  
  /**
   * Throws an exception for one or more missing attributes.
   * @param reader the reader being parsed
   * @param names names of the missing elements
   * @throws XMLStreamException
   */
  void missingAttributes(XMLStreamReader reader, Set<String> names)
      throws XMLStreamException;
  
  /**
   * Throws an exception for a duplicate attribute
   * @param reader the reader being parsed
   * @param name name of the duplicated attribute
   * @throws XMLStreamException
   */
  void duplicateAttribute(XMLStreamReader reader,
      String name) throws XMLStreamException;
  
  /**
   * Adds an operation to the list of operations accumulated during the
   * parse.
   * @param op the operation to add
   * @param type type for the resource path
   * @param name name for the resource path
   */
  void addOperation(ModelNode op, String type, String name);
  
  /**
   * Retrieves the last operation added.
   * @return operation
   */
  ModelNode lastOperation();
  
  <T> T getAttachment(AttachmentKey<T> key);
  
  <T> T putAttachment(AttachmentKey<T> key, T value);

  <T> T removeAttachment(AttachmentKey<T> key);

}
