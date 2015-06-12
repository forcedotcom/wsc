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

package com.sforce.ws;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

import com.sforce.ws.tools.VersionInfo;
import com.sforce.ws.transport.JdkHttpTransport;
import com.sforce.ws.transport.Transport;
import com.sforce.ws.util.Base64;
import com.sforce.ws.util.Verbose;

/**
 * This class contains a set of configuration properties
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Dec 19, 2005
 */
public class ConnectorConfig {
    public class TeeInputStream {
        private int level = 0;

        private TeeInputStream(byte[] bytes) {
            getTraceStream().println("------------ Response start ----------");

            if (isPrettyPrintXml()) {
                prettyPrint(bytes);
            } else {
                getTraceStream().print(new String(bytes));
            }

            getTraceStream().println();
            getTraceStream().println("------------ Response end   ----------");
        }

        private void prettyPrint(byte[] bytes) {
            boolean newLine = true;
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] == '<') {
                    if (i + 1 < bytes.length) {
                        if (bytes[i + 1] == '/') {
                            level--;
                        } else {
                            level++;
                        }
                    }
                    for (int j = 0; newLine && j < level; j++) {
                        getTraceStream().print("  ");
                    }
                }

                getTraceStream().write(bytes[i]);

                if (bytes[i] == '>') {
                    if (i + 1 < bytes.length && bytes[i + 1] == '<') {
                        getTraceStream().println();
                        newLine = true;
                    } else {
                        newLine = false;
                    }
                }
            }
        }
    }


    public class TeeOutputStream extends OutputStream {
        private OutputStream out;

        private TeeOutputStream(OutputStream out) {
            getTraceStream().println("------------ Request start   ----------");
            this.out = out;
        }

        @Override
        public void write(int b) throws IOException {
            getTraceStream().write((char) b);
            out.write(b);
        }

        @Override
        public void write(byte b[]) throws IOException {
            getTraceStream().write(b);
            out.write(b);
        }

        @Override
        public void write(byte b[], int off, int len) throws IOException {
            getTraceStream().write(b, off, len);
            out.write(b, off, len);
        }

        @Override
        public void close() throws IOException {
            getTraceStream().println();
            getTraceStream().flush();
            out.close();
            getTraceStream().println("------------ Request end   ----------");
        }
    }
    
    private int readTimeout;
    private int connectionTimeout;
    private boolean traceMessage;
    private boolean compression = true;
    private boolean prettyPrintXml;
    private boolean manualLogin;
    private boolean useChunkedPost;
    private String username;
    private String password;
    private String sessionId;
    private String authEndpoint;
    private String serviceEndpoint;
    private String restEndpoint;
    private String traceFile;
    private PrintStream traceStream;
    private String proxyUsername;
    private String proxyPassword;
    private HashMap<String, String> headers;
    private Proxy proxy = null;
    private ArrayList<MessageHandler> handlers = new ArrayList<MessageHandler>();
    private int maxRequestSize;
    private int maxResponseSize;
    private boolean validateSchema = true;
    private Class transport = JdkHttpTransport.class;
    private SessionRenewer sessionRenewer;
    private String ntlmDomain;

    public static final ConnectorConfig DEFAULT = new ConnectorConfig();

    public Class getTransport() {
        return transport;
    }

    public void setTransport(Class transport) {
        this.transport = transport;
    }

    public void setNtlmDomain(String domain) {
    	this.ntlmDomain = domain;
        if (System.getProperty("http.auth.ntlm.domain") == null) {
            System.setProperty("http.auth.ntlm.domain", domain);
        } else {
            Verbose.log("http.auth.ntlm.domain already set");
        }
    }
    
    public String getNtlmDomain() {
    	return ntlmDomain;
    }

    public boolean isValidateSchema() {
        return validateSchema;
    }

    public void setValidateSchema(boolean validateSchema) {
        this.validateSchema = validateSchema;
    }

    public void setProxy(String host, int port) {
        SocketAddress addr = new InetSocketAddress(host, port);
        proxy = new Proxy(Proxy.Type.HTTP, addr);
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public Proxy getProxy() {
        return proxy == null ? Proxy.NO_PROXY : proxy;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getRequestHeader(String key) {
        String ret = null;
        if (this.headers != null) {
            ret = this.headers.get(key);
        }
        return ret;
    }

    public void setRequestHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<String, String>();
        }
        this.headers.put(key, value);
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public boolean isPrettyPrintXml() {
        return prettyPrintXml;
    }

    public void setPrettyPrintXml(boolean prettyPrintXml) {
        this.prettyPrintXml = prettyPrintXml;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        if (serviceEndpoint == null || serviceEndpoint.equals("")) {
            throw new IllegalArgumentException("illegal service endpoint " + serviceEndpoint);
        }
        this.serviceEndpoint = serviceEndpoint;
    }

    public boolean isCompression() {
        return compression;
    }

    public void setCompression(boolean compress) {
        this.compression = compress;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * sets read timeout
     * @param readTimeout timeout in ms
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * sets connection timeout
     * @param connectionTimeout timout in ms
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public boolean isTraceMessage() {
        return traceMessage;
    }

    /**
     * prints request and response xml message on console
     * @param traceMessage true to print message
     */
    public void setTraceMessage(boolean traceMessage) {
        this.traceMessage = traceMessage;
    }

    public String getTraceFile() {
        return traceFile;
    }

    public void setTraceFile(String traceFile) throws FileNotFoundException {
        this.traceFile = traceFile;
        File file = new File(traceFile);

        if (file.exists()) {
            Verbose.log("Log file already exists, appending to " + file);
        }

        traceStream = new PrintStream(new FileOutputStream(file, true), true);
    }

    public PrintStream getTraceStream() {
        return traceStream == null ? System.out : traceStream;
    }

    public String getAuthEndpoint() {
        return authEndpoint;
    }

    public void setAuthEndpoint(String authEndpoint) {
        if (authEndpoint == null || authEndpoint.equals("")) {
            throw new IllegalArgumentException("Illegal auth endpoint " + authEndpoint);
        }
        this.authEndpoint = authEndpoint;
    }

    public void setManualLogin(boolean manualLogin) {
        this.manualLogin = manualLogin;
    }

    public boolean isManualLogin() {
        return manualLogin;
    }

	public void setUseChunkedPost(boolean chunk) {
		this.useChunkedPost = chunk;
	}
	
	public boolean useChunkedPost() {
		return this.useChunkedPost;
	}
	
    public void verifyPartnerEndpoint() throws ConnectionException {
        verifyEndpoint("/services/Soap/u/");
    }

    public void verifyEnterpriseEndpoint() throws ConnectionException {
        verifyEndpoint("/services/Soap/c/");
    }

    public void verifyToolingEndpoint() throws ConnectionException {
        verifyEndpoint("/services/Soap/T/");
    }

    public Iterator<MessageHandler> getMessagerHandlers() {
        return handlers.iterator();
    }

    public boolean hasMessageHandlers() {
        return handlers.size() != 0;
    }

    public void addMessageHandler(MessageHandler handler) {
        handlers.add(handler);
    }

    public void clearMessageHandlers() {
        handlers.clear();
    }

    public int getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(int maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public int getMaxResponseSize() {
        return maxResponseSize;
    }

    public void setMaxResponseSize(int maxResponseSize) {
        this.maxResponseSize = maxResponseSize;
    }

    private void verifyEndpoint(String contains) throws ConnectionException {
        if (authEndpoint != null && !authEndpoint.contains(contains)) {
            throw new ConnectionException("Check authEndpoint. It must contain '" + contains + "'. " +
                                          "authEndpoint specified " + authEndpoint);
        }
        if (serviceEndpoint != null && !serviceEndpoint.contains(contains)) {
            throw new ConnectionException("Check serviceEndpoint. It must contain '" + contains + "'. " +
                                          "serviceEndpoint specified " + serviceEndpoint);
        }
    }

    public String getRestEndpoint() {
        return restEndpoint;
    }

    public void setRestEndpoint(String restEndpoint) {
        this.restEndpoint = restEndpoint;
    }
    
    public SessionRenewer getSessionRenewer() {
        return sessionRenewer;
    }
    
    public void setSessionRenewer(SessionRenewer sessionRenewer) {
        this.sessionRenewer = sessionRenewer;
    }

    public Transport createTransport() throws ConnectionException {
        try {
            Transport t = (Transport)getTransport().newInstance();
            t.setConfig(this);
            return t;
        } catch (InstantiationException e) {
            throw new ConnectionException("Failed to create new Transport " + getTransport());
        } catch (IllegalAccessException e) {
            throw new ConnectionException("Failed to create new Transport " + getTransport());
        }
    }

    public HttpURLConnection createConnection(URL url,
            HashMap<String, String> httpHeaders) throws IOException {
        return createConnection(url, httpHeaders, true);
    }
    
    public void teeInputStream(byte[] bytes) {
        new TeeInputStream(bytes);
    }
    
    public OutputStream teeOutputStream(OutputStream os) {
        return new TeeOutputStream(os);
    }

    public HttpURLConnection createConnection(URL url,
            HashMap<String, String> httpHeaders, boolean enableCompression) throws IOException {

        if (isTraceMessage()) {
            getTraceStream().println( "WSC: Creating a new connection to " + url + " Proxy = " +
                    getProxy() + " username " + getProxyUsername());
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection(getProxy());
        connection.addRequestProperty("User-Agent", VersionInfo.info());

        /*
         * Add all the client specific headers here
         */
        if (getHeaders() != null) {
            for (Entry<String, String> ent : getHeaders().entrySet()) {
                connection.setRequestProperty(ent.getKey(), ent.getValue());
            }
        }

        if (enableCompression && isCompression()) {
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.addRequestProperty("Accept-Encoding", "gzip");
        }

        if (getProxyUsername() != null) {
            String token = getProxyUsername() + ":" + getProxyPassword();
            String auth = "Basic " + new String(Base64.encode(token.getBytes()));
            connection.addRequestProperty("Proxy-Authorization", auth);
            connection.addRequestProperty("Https-Proxy-Authorization", auth);
        }

        if (httpHeaders != null) {
            for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        if (getReadTimeout() != 0) {
            connection.setReadTimeout(getReadTimeout());
        }

        if (getConnectionTimeout() != 0) {
            connection.setConnectTimeout(getConnectionTimeout());
        }

        return connection;
    }
}
