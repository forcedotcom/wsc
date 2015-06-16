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

package com.sforce.ws.transport;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import com.sforce.ws.*;
import com.sforce.ws.bind.*;
import com.sforce.ws.parser.*;
import com.sforce.ws.util.Verbose;
import com.sforce.ws.wsdl.Constants;

/**
 * SoapConnection can be used to send and receive SOAP messages over the
 * specified Transport. This class can be used to send any XML data and it
 * returns the result as XML. This class is ideal to use with doc-literal
 * web service.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 30, 2005
 */
public class SoapConnection {
    private String url;
    private TypeMapper typeMapper;
    private String objectNamespace;
    private HashMap<QName, Object> headers = new HashMap<QName, Object>();
    private ConnectorConfig config;
    private Object connection;
    
    private Map<QName, Class> knownHeaders;

    public SoapConnection(String url, String objectNamespace, TypeMapper typeMapper, ConnectorConfig config) {
        this.url = url;
        this.objectNamespace = objectNamespace;
        this.typeMapper = typeMapper;
        this.config = config;
    }

    public void setConnection(Object connection) {
        this.connection = connection;
    }

    public void setKnownHeaders(Map<QName, Class> knownHeaders) {
        this.knownHeaders = knownHeaders;
    }

    //TODO: delete this method
    public XMLizable send(QName requestElement, XMLizable request, QName responseElement, Class responseType)
            throws ConnectionException {
        return send(null, requestElement, request, responseElement, responseType);
    }

    public XMLizable send(String soapAction, QName requestElement, XMLizable request, QName responseElement, Class responseType)
            throws ConnectionException {

        long startTime = System.currentTimeMillis();

        try {
            boolean firstTime = true;
            while(true) {
                try {
                    Transport transport = newTransport(config);
                    OutputStream out = transport.connect(url, soapAction);
                    sendRequest(out, request, requestElement);
                    InputStream in = transport.getContent();
                    XMLizable result;
                    result = receive(transport, responseElement, responseType, in);
                    return result;
                } catch (SessionTimedOutException se) {
                    if (config.getSessionRenewer() == null || !firstTime) {
                        throw (ConnectionException) se.getCause();
                    } else {
                    	SessionRenewer.SessionRenewalHeader sessionHeader = config.getSessionRenewer().renewSession(config);
                    	if (sessionHeader != null) {
                            addHeader(sessionHeader.name, sessionHeader.headerElement);
                    	}
                    }
                }
                firstTime = false;
            }
        } catch (SocketTimeoutException e) {
            long timeTaken = System.currentTimeMillis() - startTime;
            throw new ConnectionException("Request to " + url + " timed out. TimeTaken=" + timeTaken +
                    " ConnectionTimeout=" + config.getConnectionTimeout() + " ReadTimeout=" + 
                    config.getReadTimeout(), e);


        } catch (IOException e) {
            throw new ConnectionException("Failed to send request to " + url, e);
        }
    }

    private Transport newTransport(ConnectorConfig config) throws ConnectionException {
    	if(config.getTransportFactory() != null) {
    		return config.getTransportFactory().createTransport();
    	}
    	
        try {
            Transport t = (Transport) config.getTransport().newInstance();
            t.setConfig(config);
            return t;
        } catch (InstantiationException e) {
            throw new ConnectionException("Failed to create new Transport " + config.getTransport());
        } catch (IllegalAccessException e) {
            throw new ConnectionException("Failed to create new Transport " + config.getTransport());
        }
    }

    private XMLizable receive(Transport transport, QName responseElement,
                              Class responseType, InputStream in) throws IOException, ConnectionException {

        XMLizable result;

        try {
           
            XmlInputStream xin = new XmlInputStream();
            xin.setInput(in, "UTF-8");

            if (transport.isSuccessful()) {
                result = bind(xin, responseElement, responseType);
            } else {
                throw createException(xin);
            }
        } catch (PullParserException e) {
            throw new ConnectionException("Failed to create/parse xml input stream ", e);
        } finally {
            in.close();
        }

        return result;
    }

    private XMLizable bind(XmlInputStream xin, QName responseElement, Class responseType)
            throws IOException, ConnectionException {

        readSoapEnvelopeStart(xin);

        xin.peekTag();
        typeMapper.verifyTag(responseElement.getNamespaceURI(),
                             responseElement.getLocalPart(), xin.getNamespace(), xin.getName());

        //todo: change responseElement to typeInfo.
        TypeInfo info = new TypeInfo(responseElement.getNamespaceURI(),
                                     responseElement.getLocalPart(), null, null, 1, 1, true);

        XMLizable result = (XMLizable) typeMapper.readObject(xin, info, responseType);
        readSoapEnvelopeEnd(xin);
        return result;
    }

    private ConnectionException createException(XmlInputStream xin) throws IOException, ConnectionException {
        readSoapEnvelopeStart(xin);

        xin.nextTag();
        typeMapper.verifyTag(Constants.SOAP_ENVELOPE_NS, "Fault", xin.getNamespace(), xin.getName());

        xin.nextTag();
        if (!"faultcode".equals(xin.getName())) {
            throw new ConnectionException("Unable to find 'faultcode' in SOAP:Fault");
        }
        String faultCodeStr = xin.nextText();
        String prefix = TypeMapper.getPrefix(faultCodeStr);
        String name = TypeMapper.getType(faultCodeStr);
        String namespace = xin.getNamespace(prefix);
        QName faultCode = new QName(namespace, name);
        
        xin.nextTag();
        if (!"faultstring".equals(xin.getName())) {
            throw new ConnectionException("Unable to find 'faultstring' in SOAP:Fault");
        }
        String faultstring = xin.nextText();

        ConnectionException e;
        xin.peekTag();
        if ("detail".equals(xin.getName())) {
            e = parseDetail(xin, faultCode, faultstring);
        } else {
        	e = new SoapFaultException(faultCode, faultstring);
        }

        xin.nextTag();
        typeMapper.verifyTag(Constants.SOAP_ENVELOPE_NS, "Fault", xin.getNamespace(), xin.getName());

        readSoapEnvelopeEnd(xin);
        return e;
    }

    private ConnectionException parseDetail(XmlInputStream xin, QName faultCode, 
			String faultstring) throws IOException, ConnectionException {

        ConnectionException e;
        xin.nextTag(); //consume <detail>
        xin.peekTag(); //move to the body of <detail>

        if (xin.getEventType() == XmlInputStream.END_TAG) { //check for empty detail element
            throw new SoapFaultException(faultCode, faultstring);
        }

        TypeInfo info = new TypeInfo(null, null, null, null, 1, 1, true);

        try {
            e = (ConnectionException) typeMapper.readObject(xin, info, ConnectionException.class);
            if (e instanceof SoapFaultException) {
                ((SoapFaultException)e).setFaultCode(faultCode);
                if (faultstring != null &&
                        (faultstring.contains("Session timed out") || faultstring.contains("Session not found") || faultstring.contains("Illegal Session")) &&
                        "INVALID_SESSION_ID".equals(faultCode.getLocalPart())) {
                    e = new SessionTimedOutException(faultstring, e);
                }
            }
        } catch (ConnectionException ce) {
            throw new ConnectionException("Failed to parse detail: " + xin + " due to: " + ce, ce.getCause());
        }

        xin.nextTag(); //consume </detail>
        if (!"detail".equals(xin.getName())) {
            throw new ConnectionException("Failed to find </detail>");
        }

        return e;
    }

    private void readSoapEnvelopeStart(XmlInputStream xin) throws IOException, ConnectionException {
        xin.nextTag();
        typeMapper.verifyTag(Constants.SOAP_ENVELOPE_NS, "Envelope", xin.getNamespace(), xin.getName());
        xin.nextTag();
        if (isHeader(xin)) {
            readSoapHeader(xin);
            xin.nextTag();
        }
        typeMapper.verifyTag(Constants.SOAP_ENVELOPE_NS, "Body", xin.getNamespace(), xin.getName());
    }

    private boolean isHeader(XmlInputStream xin) {
        return Constants.SOAP_ENVELOPE_NS.equals(xin.getNamespace()) &&
               "Header".equals(xin.getName());
    }

    private void readSoapHeader(XmlInputStream xin) throws IOException, ConnectionException {
        while (true) {
            xin.peekTag();

            if (xin.getEventType() == XmlInputStream.START_TAG) {
                QName tag = new QName(xin.getNamespace(), xin.getName());

                Class headerType = (knownHeaders != null) ? knownHeaders.get(tag) : null;

                if (headerType != null) {
                    TypeInfo info = new TypeInfo(xin.getNamespace(), xin.getName(), null, null, 1, 1, true);
                    XMLizable result = (XMLizable) typeMapper.readObject(xin, info, headerType);
                    if (connection != null) {
                        setHeader(tag, headerType, result);
                    }
                }
            }

            if (xin.getEventType() == XmlInputStream.END_TAG && isHeader(xin)) {
                xin.next();
                break;
            }
        }
    }

    private void setHeader(QName tag, Class headerType, XMLizable result) {
        try {
            Method m = connection.getClass().getMethod("__set" + tag.getLocalPart(), new Class[]{headerType});
            m.invoke(connection, result);
        } catch (NoSuchMethodException e) {
            Verbose.log("Failed to set response header " + e);
        } catch (IllegalAccessException e) {
            Verbose.log("Failed to set response header " + e);
        } catch (InvocationTargetException e) {
            Verbose.log("Failed to set response header " + e);
        }
    }


    private void readSoapEnvelopeEnd(XmlInputStream xin) throws IOException, ConnectionException {
        xin.nextTag();
        typeMapper.verifyTag(Constants.SOAP_ENVELOPE_NS, "Body", xin.getNamespace(), xin.getName());

        xin.nextTag();
        typeMapper.verifyTag(Constants.SOAP_ENVELOPE_NS, "Envelope", xin.getNamespace(), xin.getName());
    }

    private void sendRequest(OutputStream out, XMLizable request, QName requestElement) throws IOException {
        XmlOutputStream xout = new XmlOutputStream(out, config.isPrettyPrintXml());
        xout.startDocument();

        xout.setPrefix("env", Constants.SOAP_ENVELOPE_NS);
        xout.setPrefix("xsd", Constants.SCHEMA_NS);
        xout.setPrefix("xsi", Constants.SCHEMA_INSTANCE_NS);

        xout.writeStartTag(Constants.SOAP_ENVELOPE_NS, "Envelope");

        if (headers.size() > 0) {
            writeHeaders(xout);
        }

        writeBody(xout, requestElement, request);

        xout.writeEndTag(Constants.SOAP_ENVELOPE_NS, "Envelope");

        xout.endDocument();
        xout.close();
    }

    private void writeHeaders(XmlOutputStream xout) throws IOException {
        xout.writeStartTag(Constants.SOAP_ENVELOPE_NS, "Header");

        for (Map.Entry<QName, Object> entry : headers.entrySet()) {
            xout.setPrefix(null, entry.getKey().getNamespaceURI());
            Object value = entry.getValue();

            if (value instanceof XMLizable) {
                ((XMLizable) value).write(entry.getKey(), xout, typeMapper);
            } else {
                //todo: add simple type
            }
        }

        xout.writeEndTag(Constants.SOAP_ENVELOPE_NS, "Header");
    }

    private void writeBody(XmlOutputStream xout, QName requestElement, XMLizable request) throws IOException {
        xout.writeStartTag(Constants.SOAP_ENVELOPE_NS, "Body");
        xout.setPrefix("m", requestElement.getNamespaceURI());
        if (objectNamespace != null) xout.setPrefix("sobj", objectNamespace);
        request.write(requestElement, xout, typeMapper);
        xout.writeEndTag(Constants.SOAP_ENVELOPE_NS, "Body");
    }

    public void addHeader(QName sessionHeader, Object header) {
        headers.put(sessionHeader, header);
    }

    public void clearHeaders() {
        headers.clear();
    }
    
    private static class SessionTimedOutException extends ConnectionException {
        private SessionTimedOutException(String faultString, Exception e) {
            super(faultString, e);
        }
    }
}
