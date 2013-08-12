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
import com.sforce.ws.ConnectionException;

import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;

/**
 * This class represents WSDL->Definitions->Binding
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0   Nov 5, 2005
 */
public class Binding extends WsdlNode {
    private Definitions definitions;
    private HashMap<QName, BindingOperation> operations = new HashMap<QName, BindingOperation>();
    private String name;

    public Binding(Definitions definitions) {
        this.definitions = definitions;
    }

    public String getName() {
        return name;
    }

    public Iterator<Part> getAllHeaders() throws ConnectionException {
        HashSet<Part> headers = new HashSet<Part>();

        for (BindingOperation operation : operations.values()) {
            addHeaders(operation.getInput().getHeaders(), headers);
            addHeaders(operation.getOutput().getHeaders(), headers);
        }
        return headers.iterator();
    }

    private void addHeaders(Iterator<SoapHeader> hs, HashSet<Part> headers) throws ConnectionException {
        while(hs.hasNext()) {
            SoapHeader h = hs.next();
            Message message = definitions.getMessage(h.getMessage());
            Part part = message.getPart(h.getPart());
            if (!headers.contains(part)) {
                headers.add(part);
            }
        }
    }

    public BindingOperation getOperation(QName name) throws ConnectionException {
        BindingOperation op = operations.get(name);
        if (op == null) {
            throw new ConnectionException("Unable to find binding operation for " + name);
        }
        return op;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        name = parser.getAttributeValue(null, NAME);

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (name != null && namespace != null) {
                    parse(name, namespace, parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (BINDING.equals(name) && WSDL_NS.equals(namespace)) {
                    return;
                }
            }

            eventType = parser.next();
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {
        if (WSDL_NS.equals(namespace)) {
            if (OPERATION.equals(name)) {
                BindingOperation operation = new BindingOperation(definitions);
                operation.read(parser);
                operations.put(operation.getQName(), operation);
           }
       } else if (WSDL_SOAP_NS.equals(namespace)) {
            if (BINDING.equals(name)) {
                String style = parser.getAttributeValue(null, STYLE);
                String transport = parser.getAttributeValue(null, TRANSPORT);

                if (style != null && !"document".equals(style)) {
                    throw new WsdlParseException("Unsupported WSDL style '" + style +
                            "'. Only supports Dcoument/literal/wrapped services. " +
                            parser.getPositionDescription());
                }
                if (transport != null && !"http://schemas.xmlsoap.org/soap/http".equals(transport)) {
                    throw new WsdlParseException("Unsupported transport " + transport + " " +
                        parser.getPositionDescription());
                }
            }
        }
    }
}
