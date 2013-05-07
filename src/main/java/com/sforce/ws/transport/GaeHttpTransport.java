/*
 * Copyright (c) 2009, salesforce.com, inc.
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
import java.net.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.tools.VersionInfo;

import com.google.appengine.api.urlfetch.*;

/**
 * This class is an implementation of Transport using the
 * Google AppEngine urlFetch
 *
 * @author Ron Hess
 * @version 1.0
 * @since 1.0  Jan 29 2009
 */
public class GaeHttpTransport implements Transport {

    private static Logger log = Logger.getLogger("soapconnection send");
    private URLFetchService connection;
    private boolean successful;
   
    private ConnectorConfig config; // not much is used from here
    private URL url;
    private HTTPRequest request;
       
        /* soapConnection writes into this stream where it is collected
         * until getContent() is called, then we take the bytes and put
         * them into the request using setPayload()
         */
    private ByteArrayOutputStream output;
       
    public GaeHttpTransport() {
        this.connection = URLFetchServiceFactory.getURLFetchService();
    }

    public void setConfig(ConnectorConfig config) {
        this.config = config;
    }

    @Override
    public OutputStream connect(String uri, String soapAction) throws IOException {
        if (soapAction == null) {
            soapAction = "";
        }

        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put("Content-Type", "text/xml; charset=UTF-8");
        headers.put("Accept", "text/xml");
        headers.put("User-Agent", VersionInfo.info());
        headers.put("SOAPAction", "\"" + soapAction + "\"");
        
        return connect(uri, headers, true);
    }


    @Override
    public OutputStream connect(String endpoint, HashMap<String, String> headers) throws IOException {
        return connect(endpoint, headers, true);
    }

	@Override
	public OutputStream connect(String endpoint, HashMap<String, String> httpHeaders, boolean enableCompression) throws IOException {
        url = new URL(endpoint);
        
        if (config.getReadTimeout() > 0) {
        	FetchOptions fetchOptions = FetchOptions.Builder.withDeadline(config.getReadTimeout()/1000);
            request = new HTTPRequest(url, HTTPMethod.POST, fetchOptions);
        } else {
            request = new HTTPRequest(url, HTTPMethod.POST);
        }
        
        for (Map.Entry<String, String> hdr : httpHeaders.entrySet()) {
            request.addHeader( new HTTPHeader(hdr.getKey(), hdr.getValue()) );
        }

        output = new ByteArrayOutputStream();
        return output;
    }

    public InputStream getContent() throws IOException {
 
        InputStream in;        
        try {
            successful = true;
            if (config.isTraceMessage()) {
                log.info( output.toString() );   // educational
            }
           
            // payload comes from bytes written to the output stream
            this.request.setPayload( output.toByteArray() );
           
            // go get that soap, read/write to Force.com API
            HTTPResponse resp = connection.fetch( this.request );
            if (resp.getResponseCode() != 200) {
                successful = false;
            }
           
            byte[] bytes = resp.getContent();
           
            // dump out the response
            if (config.isTraceMessage()) {
                log.info( new String(bytes) );
            }
            in = new ByteArrayInputStream(bytes);
           
        } catch (IOException e) {
            successful = false;
            in = new ByteArrayInputStream( e.getMessage().getBytes() );
            log.warning("getContent: "+e.getMessage());
        }

        return in;
    }

    public boolean isSuccessful() {   
        return successful;    
    }
}