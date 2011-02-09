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

/**
 * Operation
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Jan 18, 2006
 */
public class Operation extends WsdlNode {

    private MessageRef input;
    private MessageRef output;
    private HashMap<String, MessageRef> faults = new HashMap<String, MessageRef>();
    private Definitions definitions;
    private QName name;

    public Operation(Definitions definitions) {
        this.definitions = definitions;
    }

    public QName getName() {
        return name;
    }

    public Message getInput() throws ConnectionException {
        return input == null ? null : definitions.getMessage(input.getMessage());
    }

    public Message getOutput() throws ConnectionException {
        return output == null ? null : definitions.getMessage(output.getMessage());
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
                    return;
                }
            }

            eventType = parser.next();
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_NS.equals(namespace)) {

            if (INPUT.equals(name)) {
                String m = parser.getAttributeValue(null, MESSAGE);
                if (m == null) throw new WsdlParseException("message cant be null for operation: " + name);
                input = new MessageRef(ParserUtil.toQName(m, parser));
            } else if (OUTPUT.equals(name)) {
                String m = parser.getAttributeValue(null, MESSAGE);
                output = new MessageRef(ParserUtil.toQName(m, parser));
            } else if (FAULT.equals(name)) {
                String m = parser.getAttributeValue(null, MESSAGE);
                MessageRef fault = new MessageRef(ParserUtil.toQName(m, parser));
                String fn = parser.getAttributeValue(null, NAME);
                if (fn == null) throw new WsdlParseException("Found a fault without name");
                faults.put(fn, fault);
            }
        }
    }

    static class MessageRef extends WsdlNode {
        private QName message;

        public MessageRef(QName message) {
            this.message = message;
        }

        public QName getMessage() {
            return message;
        }
    }
}
