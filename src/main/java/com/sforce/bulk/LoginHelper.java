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

package com.sforce.bulk;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.transport.Transport;
import com.sforce.ws.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class is a helper to do login using partner wsdl.
 * 
 * <p/>
 * User: mcheenath
 * Date: Dec 10, 2010
 */
public class LoginHelper {

    private StreamHandler handler;

    LoginHelper(StreamHandler handler) {
        this.handler = handler;
    }

    void doLogin() throws IOException, StreamException {
        handler.info("Calling login on: " + handler.getConfig().getAuthEndpoint());

        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<env:Body><m:login xmlns:m=\"urn:partner.soap.sforce.com\" " +
                "xmlns:sobj=\"urn:sobject.partner.soap.sforce.com\">" +
                "<m:username>" +
                handler.getConfig().getUsername() +
                "</m:username>" +
                "<m:password>" +
                handler.getConfig().getPassword() +
                "</m:password>" +
                "</m:login>" +
                "</env:Body>" +
                "</env:Envelope>";

        Transport transport;
        try {
            transport = handler.getConfig().createTransport();
        } catch (ConnectionException x) {
            throw new IOException(String.format("Cannot create transport %s", handler.getConfig().getTransport()), x);
        }
        OutputStream out = transport.connect(handler.getConfig().getAuthEndpoint(), "");
        out.write(request.getBytes());
        out.close();

        InputStream input = transport.getContent();
        String response = new String(FileUtil.toBytes(input));

        String sessionId = getValueForTag("sessionId", response);
        handler.getConfig().setSessionId(sessionId);
        handler.info("Session Id: " + sessionId);

        String serverUrl = getValueForTag("serverUrl", response);

        if (sessionId == null || serverUrl == null) {
            throw new StreamException("Failed to login " + response);
        }

        setBulkUrl(response, serverUrl);
    }

    private void setBulkUrl(String response, String serverUrl) throws StreamException {
        String partnerTag = "/services/Soap/u/";

        int index = serverUrl.indexOf(partnerTag);

        if (index == -1) {
            throw new StreamException("Unknown serverUrl " + serverUrl + "in response " + response);
        }

        String bulkUrl = serverUrl.substring(0, index);

        int verIndex = index +  partnerTag.length();
        String version = serverUrl.substring(verIndex, verIndex+4);

        bulkUrl = bulkUrl + "/services/async/" + version + "/";

        handler.getConfig().setRestEndpoint(bulkUrl);
        handler.info("Bulk API Server Url :" + bulkUrl);
    }

    private String getValueForTag(String tag, String response) {
        String value = null;
        int index = response.indexOf("<" + tag + ">");

        if (index != -1) {
            int end = response.indexOf("</" + tag + ">");

            if (end != -1) {
                value = response.substring(index + tag.length() + 2, end);
            }
        }

        return value;
    }
}
