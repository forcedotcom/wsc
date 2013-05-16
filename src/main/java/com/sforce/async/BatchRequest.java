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

package com.sforce.async;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.PullParserException;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;
import com.sforce.ws.transport.Transport;
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
    private Transport transport;

    public BatchRequest(Transport transport, OutputStream out) throws IOException {
        this.transport = transport;
        xmlStream = new AsyncXmlOutputStream(out, false);
        xmlStream.setPrefix("xsi", Constants.SCHEMA_INSTANCE_NS);
        xmlStream.writeStartTag(BulkConnection.NAMESPACE, "sObjects");
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
            xmlStream.writeEndTag(BulkConnection.NAMESPACE, "sObjects");
            xmlStream.endDocument();
            xmlStream.close();
            InputStream in = transport.getContent();

            if (transport.isSuccessful()) {
                return loadBatchInfo(in);
            } else {
                BulkConnection.parseAndThrowException(in);
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
        info.load(xin, BulkConnection.typeMapper);
        return info;
    }
}
