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

import javax.xml.namespace.QName;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.util.Named;

/**
 * This class represents WSDL->Definitions->types->schema->element
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 9, 2005
 */
public class Element implements Constants, Named {
	public static final int UNBOUNDED = -1;

	private String name;
	private QName type;
	private QName ref;
	private String nillable;
	private String minOccurs;
	private String maxOccurs;
	private Schema schema;
	private boolean isComplexType = true;

	public Element(Schema schema) {
		this.schema = schema;
	}

	public boolean isComplexType() {
		return isComplexType;
	}

	public QName getType() {
		return type;
	}
	
	public ComplexType findComplexType() throws ConnectionException {
		if (isComplexType)
			return schema.getTypes().getComplexType(getType());
		return null;
	}

	public QName getRef() {
		return ref;
	}

	public Schema getSchema() {
		return schema;
	}

	public int getMinOccurs() {
		return toRange(minOccurs);
	}

	public int getMaxOccurs() {
		return toRange(maxOccurs);
	}

	public boolean isNillable() {
		return "true".equals(nillable);
	}

	private int toRange(String occurs) {
		if (occurs == null || "".equals(occurs)) {
			return 1;
		} else if ("unbounded".equals(occurs)) {
			return UNBOUNDED;
		} else {
			return Integer.parseInt(occurs);
		}
	}

	@Override
	public String toString() {
		return "Element{" + "name='" + name + '\'' + ", type='" + type + '\''
				+ ", ref='" + ref + '\'' + ", nillable='" + nillable + '\''
				+ ", minOccurs='" + minOccurs + '\'' + ", maxOccurs='"
				+ maxOccurs + '\'' + '}';
	}

	public void read(WsdlParser parser) throws WsdlParseException {
		name = parser.getAttributeValue(null, NAME);
		nillable = parser.getAttributeValue(null, NILLABLE);
		minOccurs = parser.getAttributeValue(null, MIN_OCCURS);
		maxOccurs = parser.getAttributeValue(null, MAX_OCCURS);
		parseType(parser);
		ref = parser.parseRef(schema);

		if (ref != null) {
			if (name != null) {
				throw new WsdlParseException(
						"Both name and ref can not be specified for element: "
								+ name);
			}
			if (type != null) {
				throw new WsdlParseException(
						"Both type and ref can not be specified for element with ref: "
								+ ref);
			}
            final String positionDescription = parser.getPositionDescription();
			parser.addPostParseProcessor(new WsdlParser.PostParseProcessor() {
				@Override
				public void postParse() throws ConnectionException {
					Element referencedElement = schema.getTypes().getElement(
							getRef());

					if (referencedElement != null) {
					    Element.this.schema = referencedElement.schema;
					    Element.this.name = referencedElement.name;
					    Element.this.type = referencedElement.type;
					} else {
						throw new ConnectionException("attribute ref '"
								+ ref + "' could not be resolved at: "
								+ positionDescription);
					}
				}
			});
		}

		int eventType = parser.getEventType();

		while (true) {
			if (eventType == XmlInputStream.START_TAG) {
				String n = parser.getName();
				String ns = parser.getNamespace();

				if (COMPLEX_TYPE.equals(n) && SCHEMA_NS.equals(ns)) {
					ComplexType complexType = new ComplexType(schema);
					String ctn = schema.generateUniqueNameForAnonymousType(name);;
					type = new QName(schema.getTargetNamespace(), ctn);
					complexType.read(parser, ctn);
					schema.addComplexType(complexType);
					isComplexType = true;
				} else if (SIMPLE_TYPE.equals(n) && SCHEMA_NS.equals(ns)) {
					SimpleType simpleType = new SimpleType(schema);
					String stn = schema.generateUniqueNameForAnonymousType(name);;
					type = new QName(schema.getTargetNamespace(), stn);
					simpleType.read(parser, stn);
					schema.addSimpleType(simpleType);
					isComplexType = false;
				}
			} else if (eventType == XmlInputStream.END_TAG) {
				String name = parser.getName();
				String namespace = parser.getNamespace();

				if (ELEMENT.equals(name) && SCHEMA_NS.equals(namespace)) {
					if (type == null) {
						// Verbose.log("ERROR: no type found for element " +
						// name);
						// addEmptyComplexType();
					}
					return;
				}
			} else if (eventType == XmlInputStream.END_DOCUMENT) {
				throw new WsdlParseException(
						"Failed to find end tag for 'complexType'");
			}

			eventType = parser.next();
		}
	}

	/*
	 * private void addEmptyComplexType() { String ctn = name + "_element";
	 * ComplexType complexType = new ComplexType(schema, ctn); type = new
	 * QName(schema.getTargetNamespace(), ctn);
	 * schema.addComplexType(complexType); isComplexType = true; }
	 */

	private void parseType(WsdlParser parser) {
		String t = parser.getAttributeValue(null, TYPE);

		if (t != null) {
			type = ParserUtil.toQName(t, parser);
			isComplexType = !SCHEMA_NS.equals(type.getNamespaceURI());
		}
	}

	@Override
    public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	void setType(QName type) {
		this.type = type;
	}
}
