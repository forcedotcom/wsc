/*
 * Copyright (c) 2005, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *    Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 *    promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.sforce.ws.wsdl;

import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.ConnectionException;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents Definitions->types.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0   Nov 5, 2005
 */
public class Types extends WsdlNode {

    private HashMap<String, Schema> schemas = new HashMap<String, Schema>();

    @Override
    public String toString() {
        return "Types{" +
                "schemas=" + schemas +
                '}';
    }

    public void read(WsdlParser parser) throws WsdlParseException {

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (SCHEMA.equals(name)) {

                    if (!SCHEMA_NS.equals(namespace)) {
                        throw new WsdlParseException("Unsupport schema version: " + namespace +
                            ". It must be: " + SCHEMA_NS);
                    }

                    Schema schema = new Schema();
                    schema.read(parser);
                    schemas.put(schema.getTargetNamespace(), schema);
                }

            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (TYPES.equals(name) && WSDL_NS.equals(namespace)) {
                    break;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'types'");
            }

            eventType = parser.next();
        }

        try {
            updateElementRef();
        } catch(ConnectionException e) {
            throw new WsdlParseException(e.getMessage(), e);
        }
    }

    private void updateElementRef() throws WsdlParseException, ConnectionException {
        Iterator<Schema> schemaIt = getSchemas();

        while(schemaIt.hasNext()) {
            Schema s = schemaIt.next();
            checkGlobalElements(s);

            Iterator<ComplexType> ctIt = s.getComplexTypes();
            while(ctIt.hasNext()) {
                ComplexType ctype = ctIt.next();
                if (ctype.getContent() != null) {
                    Iterator<Element> elementIt = ctype.getContent().getElements();

                    while(elementIt.hasNext()) {
                        Element element = elementIt.next();
                        if (element.getRef() != null) {
                            Element targetElement = getElement(element.getRef());
                            element.setName(targetElement.getName());
                            element.setType(targetElement.getType());
                        }
                    }
                }
            }
        }
    }

    private void checkGlobalElements(Schema s) throws WsdlParseException {
        Iterator<Element> elementIt = s.getGlobalElements();
        while(elementIt.hasNext()) {
            Element e = elementIt.next();
            if (e.getRef() != null) {
                throw new WsdlParseException("Global element can not use ref: " + e.getRef());
            }
        }
    }

    public Iterator<Schema> getSchemas() {
        return schemas.values().iterator();
    }

    public Element getElement(QName element) throws ConnectionException {
        Schema schema = getSchema(element);
        Element el = schema.getGlobalElement(element.getLocalPart());
        if (el == null) throw new ConnectionException("Unable to find element for " + element);
        return el;
    }

    private Schema getSchema(QName element) throws ConnectionException {
        Schema schema = schemas.get(element.getNamespaceURI());
        if (schema == null) throw new ConnectionException("Unable to find schema for element; " + element);
        return schema;
    }

    public SimpleType getSimpleTypeAllowNull(QName type) {
        Schema schema = schemas.get(type.getNamespaceURI());
        if (schema == null) return null;
        return schema.getSimpleType(type.getLocalPart());
    }

    public ComplexType getComplexType(QName type) throws ConnectionException {
        Schema schema = getSchema(type);
        ComplexType ct = schema.getComplexType(type.getLocalPart());
        if (ct == null) throw new ConnectionException("Unable to find complexType for " + type);
        return ct;
    }
}
