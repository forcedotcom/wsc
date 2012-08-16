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
import com.sforce.ws.util.Verbose;
import com.sforce.ws.ConnectionException;

import javax.xml.namespace.QName;

/**
 * This class represents WSDL->definitions->message
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0   Nov 5, 2005
 */
public class Part extends WsdlNode {
    private QName element;
    private String name;

    public String getName() {
        return name;
    }

    public QName getElement() throws ConnectionException {
        if (element == null) {
            throw new ConnectionException("Element not defined for part '" + name + "'");
        }
        return element;
    }

    public void read(WsdlParser parser) throws WsdlParseException {

        name = parser.getAttributeValue(null, NAME);
        if (name == null) throw new WsdlParseException("Unable to find name attribute in part");

        String e = parser.getAttributeValue(null, ELEMENT);
        if (e == null) {
            Verbose.log("********* FIXME: document literal WSDL using part->type it must be part->element");
        } else {
            element = ParserUtil.toQName(e, parser);
        }

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (PART.equals(name) && WSDL_NS.equals(namespace)) {
                    return;
                }
            }

            eventType = parser.next();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Part part = (Part) o;

        if (element != null ? !element.equals(part.element) : part.element != null) return false;
        if (name != null ? !name.equals(part.name) : part.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (element != null ? element.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Part{" +
                "element=" + element +
                ", name='" + name + '\'' +
                '}';
    }
}
