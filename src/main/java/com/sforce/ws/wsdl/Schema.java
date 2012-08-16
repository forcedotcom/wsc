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

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents WSDL->definitions->types->schema
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 9, 2005
 */
public class Schema implements Constants {

    private String targetNamespace;
    private String elementFormDefault;
    private String attributeFormDefault;
    private HashMap<String, ComplexType> complexTypes = new HashMap<String, ComplexType>();
    private HashMap<String, SimpleType> simpleTypes = new HashMap<String, SimpleType>();
    private HashMap<String, Element> elements = new HashMap<String, Element>();

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public boolean isElementFormQualified() {
        return "qualified".equals(elementFormDefault);
    }

    public boolean isAttributeFormQualified() {
        return "qualified".equals(attributeFormDefault);
    }

    public void addComplexType(ComplexType type) {
        complexTypes.put(type.getName(), type);
    }

    public void addSimpleType(SimpleType type) {
        simpleTypes.put(type.getName(), type);
    }

    public Iterator<ComplexType> getComplexTypes() {
        return complexTypes.values().iterator();
    }

    public Iterator<SimpleType> getSimpleTypes() {
        return simpleTypes.values().iterator();
    }

    public ComplexType getComplexType(String type) {
        return complexTypes.get(type);
    }

    public SimpleType getSimpleType(String type) {
        return simpleTypes.get(type);
    }

    public Element getGlobalElement(String name) {
        return elements.get(name);
    }

    public Iterator<Element> getGlobalElements() {
        return elements.values().iterator();
    }

    @Override
    public String toString() {
        return "Schema{" +
                "targetNamespace='" + targetNamespace + '\'' +
                ", elementFormDefault='" + elementFormDefault + '\'' +
                ", attributeFormDefault='" + attributeFormDefault + '\'' +
                ", complexTypes=" + complexTypes +
                '}';
    }

    public void read(WsdlParser parser) throws WsdlParseException {
        targetNamespace = parser.getAttributeValue(null, TARGET_NAME_SPACE);
        elementFormDefault = parser.getAttributeValue(null, ELEMENT_FORM_DEFAULT);
        attributeFormDefault = parser.getAttributeValue(null, ATTRIBUTE_FORM_DEFAULT);

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String n = parser.getName();
                String ns = parser.getNamespace();

                if (COMPLEX_TYPE.equals(n) && SCHEMA_NS.equals(ns)) {
                    ComplexType complexType = new ComplexType(this);
                    complexType.read(parser, null);
                    complexTypes.put(complexType.getName(), complexType);
                } else  if (ELEMENT.equals(n) && SCHEMA_NS.equals(ns)) {
                    Element element = new Element(this);
                    element.read(parser);
                    elements.put(element.getName(), element);
                } else  if (SIMPLE_TYPE.equals(n) && SCHEMA_NS.equals(ns)) {
                    SimpleType simpleType = new SimpleType(this);
                    simpleType.read(parser, null);
                    simpleTypes.put(simpleType.getName(), simpleType);
                } else  if (SCHEMA.equals(n) && SCHEMA_NS.equals(ns)) {
                    //skip header
                } else  if ("import".equals(n) && SCHEMA_NS.equals(ns)) {
                    String location = parser.getAttributeValue(null, "schemaLocation");
                    if (location != null) {
                        throw new WsdlParseException("Found schema import from location " + location +
                                ". External schema import not supported");
                    }
                } else if (ANNOTATION.equals(n) && SCHEMA_NS.equals(ns)) {
                    Annotation annotation = new Annotation();
                    annotation.read(parser);
                } else {
                    throw new WsdlParseException("Unsupported Schema element found "
                            + ns + ":" + n + ". At: " + parser.getPositionDescription());
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (SCHEMA.equals(name) && SCHEMA_NS.equals(namespace)) {
                    break;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'schema'");
            }

            eventType = parser.next();
        }

        if (targetNamespace == null) {
            throw new WsdlParseException("schema:targetNamespace can not be null");
        }
    }
}
