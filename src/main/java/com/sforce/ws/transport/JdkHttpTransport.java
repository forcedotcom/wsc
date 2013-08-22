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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.sforce.ws.*;
import com.sforce.ws.tools.VersionInfo;
import com.sforce.ws.util.Base64;
import com.sforce.ws.util.FileUtil;

/**
 * This class is an implementation of Transport using the build in
 * JDK URLConnection.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 30, 2005
 */
public class JdkHttpTransport implements Transport {
    private HttpURLConnection connection;
    private boolean successful;
    private ConnectorConfig config;
	private URL url;

    public JdkHttpTransport() {
    }
		
    public JdkHttpTransport(ConnectorConfig config) {
        setConfig(config);
    }

    @Override
    public void setConfig(ConnectorConfig config) {
        this.config = config;
    }

    @Override
    public OutputStream connect(String uri, HashMap<String, String> httpHeaders) throws IOException {
        return connectLocal(uri, httpHeaders, true);
    }

    @Override
    public OutputStream connect(String uri, HashMap<String, String> httpHeaders, boolean enableCompression)
            throws IOException {
        return connectLocal(uri, httpHeaders, enableCompression);
    }

    @Override
    public OutputStream connect(String uri, String soapAction) throws IOException {
        if (soapAction == null) {
            soapAction = "";
        }

        HashMap<String, String> header = new HashMap<String, String>();

        header.put("SOAPAction", "\"" + soapAction + "\"");
        header.put("Content-Type", "text/xml; charset=UTF-8");
        header.put("Accept", "text/xml");

        return connectLocal(uri, header);
    }

    private OutputStream connectLocal(String uri, HashMap<String, String> httpHeaders) throws IOException {
        return connectLocal(uri, httpHeaders, true);
    }

    private OutputStream connectLocal(String uri, HashMap<String, String> httpHeaders, boolean enableCompression)
            throws IOException {
        return wrapOutput(connectRaw(uri, httpHeaders, enableCompression), enableCompression);
    }

    private OutputStream wrapOutput(OutputStream output, boolean enableCompression) throws IOException {
        if (config.getMaxRequestSize() > 0) {
            output = new LimitingOutputStream(config.getMaxRequestSize(), output);
        }

        // when we are writing a zip file we don't bother with compression
        if (enableCompression && config.isCompression()) {
            output = new GZIPOutputStream(output);
        }

        if (config.isTraceMessage()) {
            output = config.teeOutputStream(output);
        }

        if (config.hasMessageHandlers()) {
            output = new MessageHandlerOutputStream(config, url, output);
        }

        return output;
    }

    private OutputStream connectRaw(String uri, HashMap<String, String> httpHeaders, boolean enableCompression)
            throws IOException {
        url = new URL(uri);

        connection = createConnection(config, url, httpHeaders, enableCompression);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (config.useChunkedPost()) {
            connection.setChunkedStreamingMode(4096);
        }

        return connection.getOutputStream();
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
        if (httpHeaders == null || (httpHeaders.get("User-Agent") == null && httpHeaders.get("user-agent") == null)) {
            connection.addRequestProperty("User-Agent", VersionInfo.info());
        }

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
    public InputStream getContent() throws IOException {
        InputStream in;

        try {
            in = connection.getInputStream();
        } catch (IOException e) {
            in = connection.getErrorStream();
            if (in == null) {
                throw e;
            }
        }

        successful = connection.getResponseCode() < 400;

        String encoding = connection.getHeaderField("Content-Encoding");

        if (config.getMaxResponseSize() > 0) {
            in = new LimitingInputStream(config.getMaxResponseSize(), in);
        }

        if ("gzip".equals(encoding)) {
            in = new GZIPInputStream(in);
        }

        if (config.hasMessageHandlers() || config.isTraceMessage()) {
            byte[] bytes = FileUtil.toBytes(in);
            in = new ByteArrayInputStream(bytes);

            if (config.hasMessageHandlers()) {
                Iterator<MessageHandler> it = config.getMessagerHandlers();
                while(it.hasNext()) {
                    MessageHandler handler = it.next();
                    if (handler instanceof MessageHandlerWithHeaders) {
                        ((MessageHandlerWithHeaders) handler).handleResponse(url, bytes, connection.getHeaderFields());
                    } else {
                        handler.handleResponse(url, bytes);
                    }
                }
            }

            if (config.isTraceMessage()) {
                Map<String, List<String>> headers = connection.getHeaderFields();
                for (Map.Entry header : headers.entrySet()) {
                    config.getTraceStream().print(header.getKey());
                    config.getTraceStream().print("=");
                    config.getTraceStream().println(header.getValue());
                }
                
                config.teeInputStream(bytes);
            }
        }

        return in;
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }

}
