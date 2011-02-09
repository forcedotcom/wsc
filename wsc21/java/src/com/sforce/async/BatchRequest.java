/*
 * Copyright, 1999-2008, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.async;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.PullParserException;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;
import com.sforce.ws.transport.JdkHttpTransport;
import com.sforce.ws.wsdl.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * BatchRequest
 *
 * @author mcheenath
 * @since 160
 */
public class BatchRequest {

    private XmlOutputStream xmlStream;
    private JdkHttpTransport transport;

    public BatchRequest(JdkHttpTransport transport, OutputStream out) throws IOException {
        this.transport = transport;
        xmlStream = new AsyncXmlOutputStream(out, false);
        xmlStream.setPrefix("xsi", Constants.SCHEMA_INSTANCE_NS);
        xmlStream.writeStartTag(RestConnection.NAMESPACE, "sObjects");
    }

    public void addSObject(SObject object) throws AsyncApiException {
        try {
            object.write(xmlStream);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to add SObject", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public void addSObjects(SObject[] objects) throws AsyncApiException {
        for (SObject object : objects) {
            addSObject(object);
        }
    }

    public BatchInfo completeRequest() throws AsyncApiException {
        try {
            xmlStream.writeEndTag(RestConnection.NAMESPACE, "sObjects");
            xmlStream.endDocument();
            xmlStream.close();
            InputStream in = transport.getContent();

            if (transport.isSuccessful()) {
                return loadBatchInfo(in);
            } else {
                RestConnection.parseAndThrowException(in);
            }
        } catch(IOException e) {
            throw new AsyncApiException("Failed to complete request", AsyncExceptionCode.ClientInputError, e);
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to complete request", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to complete request", AsyncExceptionCode.ClientInputError, e);
        }
        return null;
    }

    static BatchInfo loadBatchInfo(InputStream in) throws PullParserException, IOException, ConnectionException {
        BatchInfo info = new BatchInfo();
        XmlInputStream xin = new XmlInputStream();
        xin.setInput(in, "UTF-8");
        info.load(xin, RestConnection.typeMapper);
        return info;
    }
}
