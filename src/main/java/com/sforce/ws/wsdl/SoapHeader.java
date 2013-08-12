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

import com.sforce.ws.parser.XmlInputStream;

import javax.xml.namespace.QName;

/**
 * SoapHeader
 *
 * @author cheenath
 * @version 1.0
 * @since 146  Dec 13, 2006
 */
public class SoapHeader extends SoapNode {
    private String use;
    private QName message;
    private String part;

    public String getUse() {
        return use;
    }

    public QName getMessage() {
        return message;
    }

    public String getPart() {
        return part;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        use = parseUse(parser);
        message = parseMessage(parser);
        part = parser.getAttributeValue(null, "part");

        int eventType = parser.getEventType();
        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                //nothing to do
           } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();
                if (HEADER.equals(name) && WSDL_SOAP_NS.equals(namespace)) {
                    return;
                }
            }
            eventType = parser.next();
        }
    }

    @Override
    public String toString() {
        return "SoapHeader{" +
                "use='" + use + '\'' +
                ", message=" + message +
                ", part='" + part + '\'' +
                '}';
    }
}
