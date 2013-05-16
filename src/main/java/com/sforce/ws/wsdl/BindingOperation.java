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
 */
public class BindingOperation extends WsdlNode {
    private Definitions definitions;
    private QName name;
    private BindingMessage input;
    private BindingMessage output;
    private String soapAction = "";

    public BindingOperation(Definitions definitions) {
        this.definitions = definitions;
    }

    public QName getName() {
        return name;
    }

    public BindingMessage getInput() {
        return input;
    }

    public BindingMessage getOutput() {
        return output;
    }

    public Operation getOperation() {
        return definitions.getPortType().getOperation(name);
    }

    public String getSoapAction() {
        return soapAction;
    }

    public QName getQName() {
        return name;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        name = new QName(definitions.getTargetNamespace(), parser.getAttributeValue(null, NAME));
        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String n = parser.getName();
                String ns = parser.getNamespace();

                if (n != null && ns != null) {
                    parse(n, ns, parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (OPERATION.equals(name) && WSDL_NS.equals(namespace)) {
                    break;
                }
            }

            eventType = parser.next();
        }

        if (input == null) {
            throw new WsdlParseException("input not defined in binding operation '" + name + "'");
        }
        if (output == null) {
            throw new WsdlParseException("output not defined in binding operation '" + name + "'");
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_NS.equals(namespace)) {
            if (INPUT.equals(name)) {
                input = new BindingMessage(definitions, INPUT);
                input.read(parser);
            } else if (OUTPUT.equals(name)) {
                output = new BindingMessage(definitions, OUTPUT);
                output.read(parser);
            } else if (FAULT.equals(name)) {
                //todo: parse binding fault
           }
        } else if (WSDL_SOAP_NS.equals(namespace)) {
            if (OPERATION.equals(name)) {
                String sa = parser.getAttributeValue(null, "soapAction");
                if (sa != null) {
                    soapAction = sa;
                }
                String style = parser.getAttributeValue(null, "style");
                if (style != null) {
                    if (!"document".equals(style)) {
                        throw new WsdlParseException("Unsupported style " + style);
                    }
                }
            }
        }
    }

}
