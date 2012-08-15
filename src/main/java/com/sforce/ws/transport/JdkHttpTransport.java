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
package com.sforce.ws.transport;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.tools.VersionInfo;
import com.sforce.ws.util.Base64;

/**
 * This class is an implementation of Transport using the build in
 * JDK URLConnection.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 30, 2005
 */
public class JdkHttpTransport extends AbstractTransport  {
    public JdkHttpTransport() {
    	super();
    }
		
    public JdkHttpTransport(ConnectorConfig config) {
        super(config);
    } 

    public static HttpURLConnection createConnection(ConnectorConfig config, URL url,
            HashMap<String, String> httpHeaders) throws IOException {
        return createConnection(config, url, httpHeaders, true);
    }

    public static HttpURLConnection createConnection(ConnectorConfig config, URL url,
            HashMap<String, String> httpHeaders, boolean enableCompression) throws IOException {

        if (config.isTraceMessage()) {
            config.getTraceStream().println( "WSC: Creating a new connection to " + url + " Proxy = " +
                    config.getProxy() + " username " + config.getProxyUsername());
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection(config.getProxy());
        connection.addRequestProperty("User-Agent", VersionInfo.info());

        /*
         * Add all the client specific headers here
         */
        if (config.getHeaders() != null) {
            for (Entry<String, String> ent : config.getHeaders().entrySet()) {
                connection.setRequestProperty(ent.getKey(), ent.getValue());
            }
        }

        if (enableCompression && config.isCompression()) {
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.addRequestProperty("Accept-Encoding", "gzip");
        }

        if (config.getProxyUsername() != null) {
            String token = config.getProxyUsername() + ":" + config.getProxyPassword();
            String auth = "Basic " + new String(Base64.encode(token.getBytes()));
            connection.addRequestProperty("Proxy-Authorization", auth);
            connection.addRequestProperty("Https-Proxy-Authorization", auth);
        }

        if (httpHeaders != null) {
            for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        if (config.getReadTimeout() != 0) {
            connection.setReadTimeout(config.getReadTimeout());
        }

        if (config.getConnectionTimeout() != 0) {
            connection.setConnectTimeout(config.getConnectionTimeout());
        }

        return connection;
    }

	@Override
	protected HttpURLConnection createHttpURLConnection(
			ConnectorConfig config2, URL url2,
			HashMap<String, String> httpHeaders, boolean enableCompression) throws IOException {
		return createConnection(config, url, httpHeaders, enableCompression);
	}

}
