/*
 * Copyright (c) 2013, salesforce.com, inc.
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

import java.util.*;

import javax.xml.namespace.QName;

import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.util.CollectionUtil;

/**
 * This class represents WSDL->Definitions->types->schema->complexType
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 9, 2005
 */
public class ComplexType implements Constants {
    private String name;
    private Collection content;
    private QName base;
    private Schema schema;
    private boolean isHeader;
    private List<Attribute> attributes = new ArrayList<Attribute>();
    private List<AttributeGroup> attributeGroups = new ArrayList<AttributeGroup>();

    public ComplexType(Schema schema) {
        this.schema = schema;
    }

    public ComplexType(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    public Collection getContent() {
        return content;
    }
    
    public Element getElement(String name) {
    	return content.getElement(name);
    }
    
    public boolean hasElement(String name) {
    	return content.hasElement(name);
    }

    public QName getBase() {
        return base;
    }

    public boolean hasBaseClass() {
        return isHeader || base != null;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public Iterator<Attribute> getAttributes() {
        return attributes.iterator();
    }

    public Attribute getAttribute(String name) {
    	return CollectionUtil.findByName(getAttributes(), name);
    }

    @Override
    public String toString() {
        return "ComplexType{" +
                "name='" + name + '\'' +
                ", content=" + content +
                ", base=" + base +
                ", attributes=" + attributes +
                '}';
    }

    public void read(WsdlParser parser, String elementName) throws WsdlParseException {

        name = parser.getAttributeValue(null, NAME);

        if (name == null) {
            name = elementName;
        }

        if (name == null) {
            throw new WsdlParseException("complexType->elementName can not be null. " + parser.getPositionDescription());
        }

        int eventType = parser.getEventType();
        
        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if ((SEQUENCE.equals(name) || ALL.equals(name) || CHOICE.equals(name)) && SCHEMA_NS.equals(namespace)) {
                    content = new Collection(schema, name);
                    content.read(parser);
                } else if (EXTENSION.equals(name) && SCHEMA_NS.equals(namespace)) {
                    name = parser.getAttributeValue(null, BASE);

                    if (name != null) {
                        base = ParserUtil.toQName(name, parser);
                    }
                } else if (ATTRIBUTE.equals(name) && SCHEMA_NS.equals(namespace)) {
                    Attribute attribute = new Attribute(schema);
                    attribute.read(parser);
                    attributes.add(attribute);
                } else if (ATTRIBUTE_GROUP.equals(name) && SCHEMA_NS.equals(namespace)) {
                    AttributeGroup attributeGroup = new AttributeGroup(schema);
                    attributeGroup.read(parser);
                    attributeGroups.add(attributeGroup);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (COMPLEX_TYPE.equals(name) && SCHEMA_NS.equals(namespace)) {
                    break;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'complexType'");
            }

            eventType = parser.next();
        }
        
        if (attributeGroups.size() > 0) {
            parser.addPostParseProcessor(new WsdlParser.PostParseProcessor() {
                @Override
                public void postParse() throws WsdlParseException {
                    for (AttributeGroup group : attributeGroups) {
                    	attributes.addAll(group.getAttributes());
                    }
                }
            });
        }
    }

    public String getName() {
        return name;
    }
}
