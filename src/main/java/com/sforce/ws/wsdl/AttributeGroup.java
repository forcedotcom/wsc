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

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.util.Named;

/**
 * This class represents WSDL->definitions->types->schema->complexType->attributeGroup
 * 
 * @author lmcalpin
 * @version 186
 */
public class AttributeGroup implements Constants, Named {

    private Schema schema;
    private String name;
    private QName ref;
    private List<Attribute> attributes = new ArrayList<Attribute>();

    public AttributeGroup(Schema schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "AttributeGroup{" + "name=" + name + '}';
    }

    @Override
    public String getName() {
        return name;
    }
    
    public String getNamespace() {
        return schema.getTargetNamespace();
    }
    
    public List<Attribute> getAttributes() {
        return attributes;
    }

    void read(final WsdlParser parser) throws WsdlParseException {
        name = parser.getAttributeValue(null, NAME);

        ref = parser.parseRef(schema);

        if (ref != null) {
            final String positionDescription = parser.getPositionDescription();
            final AttributeGroup thisAttribute = this;
            parser.addPostParseProcessor(new WsdlParser.PostParseProcessor() {
                @Override
                public void postParse() throws WsdlParseException {
                	AttributeGroup referencedGroup = schema.getTypes().getAttributeGroup(ref);
                    if (referencedGroup != null) {
                        thisAttribute.schema = referencedGroup.schema;
                        thisAttribute.name = referencedGroup.name;
                        thisAttribute.attributes = referencedGroup.attributes;
                    } else {
                        throw new WsdlParseException("attributeGroup ref '" + ref
                                + "' could not be resolved at: " + positionDescription);
                    }
                }
            });
        } else {
	        if (name == null) { throw new WsdlParseException("attributeGroup name can not be null at: "
	                + parser.getPositionDescription()); }
	
	        if ("".equals(name)) { throw new WsdlParseException("attributeGroup name can not be empty at: "
	                + parser.getPositionDescription()); }
        }

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ATTRIBUTE.equals(name) && SCHEMA_NS.equals(namespace)) {
                    Attribute attribute = new Attribute(this.schema);
                    attribute.read(parser);
                    attributes.add(attribute);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ATTRIBUTE_GROUP.equals(name) && SCHEMA_NS.equals(namespace)) {
                    break;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) { throw new WsdlParseException(
                    "Failed to find end tag for 'attribute'"); }

            eventType = parser.next();
        }
    }
}
