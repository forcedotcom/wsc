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
import java.util.Iterator;

import javax.xml.namespace.QName;

import com.sforce.ws.parser.XmlInputStream;

/**
 * This class represents WSDL->definitions->types->schema->complexType->sequence
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 9, 2005
 */
public class Restriction implements Constants, Iterable<Enumeration> {

    private ArrayList<Enumeration> enumerations = new ArrayList<Enumeration>();
    private QName base;
    private Schema schema;

    public Restriction(Schema schema) {
        this.schema = schema;
    }

    public Iterator<Enumeration> getElements() {
        return enumerations.iterator();
    }

    @Override
    public Iterator<Enumeration> iterator() {
        return enumerations.iterator();
    }

    public int getNumEnumerations() {
    	return enumerations.size();
    }

    @Override
    public String toString() {
        return "Restriction{" +
                "enumerations=" + enumerations +
                '}';
    }

    public QName getBase() {
    	return this.base;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        String baseLocal = parser.getAttributeValue(null, BASE);
        base = baseLocal != null ? ParserUtil.toQName(baseLocal, parser) : null;

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ENUMERATION.equals(name) && SCHEMA_NS.equals(namespace)) {
                    Enumeration enumeration = new Enumeration(schema);
                    enumeration.read(parser);
                    enumerations.add(enumeration);
                }

            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (RESTRICTION.equals(name) && SCHEMA_NS.equals(namespace)) {
                    return;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'restriction'");
            }

            eventType = parser.next();
        }
    }
}
