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
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents a WSDL->definitions
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0   Nov 5, 2005
 */
public class Definitions extends WsdlNode {

    // private static boolean LOG = Verbose.isVerbose(Verbose.WSDL);
    private Types types;
    private HashMap<QName, Message> messages = new HashMap<QName, Message>();
    private String targetNamespace;
    private SfdcApiType apiType;
    private PortType portType;
    private Service service;
    private Binding binding;

    public Types getTypes() {
        return types;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public SfdcApiType getApiType() {
    	return apiType;
    }

    public PortType getPortType() {
        return portType;
    }

    public Binding getBinding() throws ConnectionException {
        QName name = service.getPort().getBinding();
        if (binding.getName().equals(name.getLocalPart()) && targetNamespace.equals(name.getNamespaceURI())) {
            return binding;
        } else {
            throw new ConnectionException("Unable to find binding " + name + ". Found "
                    + binding.getName() + " instead.");
        }
    }

    public Service getService() {
        return service;
    }

    public Message getMessage(QName name) throws ConnectionException {
        Message message = messages.get(name);
        if (message == null) {
            throw new ConnectionException("No message found for:" + name);
        }
        return message;
    }

    void read(WsdlParser parser) throws WsdlParseException {

        int eventType = parser.getEventType();

        while (eventType != XmlInputStream.END_DOCUMENT) {
            if (eventType == XmlInputStream.START_DOCUMENT) {
            //} else if (eventType == XmlInputStream.END_DOCUMENT) {

            } else if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (name != null && namespace != null) {
                    parse(name, namespace, parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
            } else if (eventType == XmlInputStream.TEXT) {
            }

            eventType = parser.next();
        }

        if (targetNamespace == null) {
            throw new WsdlParseException("targetNamespace not specified in wsdl:definitions ");
        }

        if (binding == null) {
            throw new WsdlParseException("Unable to find wsdl:binding in the specified wsdl");
        }

        if (portType == null) {
            throw new WsdlParseException("Unable to find wsdl:portType in the specified wsdl");
        }

        if (service == null) {
            throw new WsdlParseException("Unable to find wsdl:service in the specified wsdl");
        }

        try {
            updateHeaderTypes();
        } catch (ConnectionException e) {
            throw new WsdlParseException("Failed to parse WSDL: " + e.getMessage(), e);
        }
    }

    private void updateHeaderTypes() throws ConnectionException {
        Iterator<Part> headers = getBinding().getAllHeaders();

        while (headers.hasNext()) {
            Part part = headers.next();
            QName el = part.getElement();
            if (getTypes() != null) {
                Element element = getTypes().getElement(el);
                if (element.isComplexType()) {
                    ComplexType ct = getTypes().getComplexType(element.getType());
                    ct.setHeader(true);
                } else {
                    //no need to set header type for simple types
                }
            }
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_NS.equals(namespace)) {
            if (DEFINITIONS.equals(name)) {
                targetNamespace = parser.getAttributeValue(null, TARGET_NAME_SPACE);
                apiType = SfdcApiType.getFromNamespace(targetNamespace);
            } else if (TYPES.equals(name)) {
                types = new Types();
                types.read(parser);
            } else if (MESSAGE.equals(name)) {
                Message message = new Message(targetNamespace);
                message.read(parser);
                messages.put(message.getName(), message);
            } else if (PORT_TYPE.equals(name)) {
                 if (portType != null) {
                    throw new WsdlParseException("Found more than one wsdl:portType. " +
                            "WSDL with multiple portType not supported");
                }
                portType = new PortType(this);
                portType.read(parser);
            } else if (BINDING.equals(name)) {
                if (binding != null) {
                    throw new WsdlParseException("Found more than one wsdl:binding. " +
                            "WSDL with multiple binding not supported");
                }
                binding = new Binding(this);
                binding.read(parser);
            } else if (SERVICE.equals(name)) {
                if (service != null) {
                    throw new WsdlParseException("Found more than one wsdl:service. " +
                            "WSDL with multiple service not supported");
                }
                service = new Service();
                service.read(parser);
            } else if (DOCUMENTATION.equals(name)) {
                new Documentation().read(parser);
            } else {
                throw new WsdlParseException("Unknown element: " + name);
            }
        }
    }

    @Override
    public String toString() {
        return "Definitions{" +
                "types=" + types +
                ", messages=" + messages +
                ", targetNamespace='" + targetNamespace + '\'' +
                ", portType=" + portType +
                ", service=" + service +
                '}';
    }
}
