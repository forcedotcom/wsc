/*
 * Copyright (c) 2011, salesforce.com, inc.
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
package com.sforce.rest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.google.gson.Gson;
import com.sforce.rest.pojo.SObject;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.transport.JdkHttpTransport;

/**
 * RestConnection
 * 
 * @author gwester
 * @since 172
 */
public class RestConnectionImpl implements RestConnection {
	
    private final ConnectorConfig config;
    private final String baseEndpoint;
    private final Gson parser;
    private HashMap<String, String> headers;
    
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_VALUE_PREFIX = "OAuth ";
    private static final String CHARSET_HEADER = "Accept-Charset";
    private static final String CHARSET_VALUE = "UTF-8";
    private static final String CONTENT_HEADER = "Content-Type";
    private static final String PRETTY_HEADER = "X-Pretty-Print";
    
    private static final String SEPARATOR = "/";
    
    private static final String SOBJECTS_ENDPOINT = "sobjects" + SEPARATOR;
    private static final String QUERY_ENDPOINT = "query?q=";
    private static final String SEARCH_ENDPOINT = "search?q=";
    private static final String RECENT_ENDPOINT = "recent" + SEPARATOR;
    
    private static final String DESCRIBE_SUBENDPOINT = "describe" + SEPARATOR;
    
    private static final String PATCH_PARAMETER = "?_HttpMethod=PATCH";
    
    /**
     * Constructor.
     * @param config
     * @throws RestApiException
     */
    public RestConnectionImpl(ConnectorConfig config) throws RestApiException {
        if (config == null) { 
        	throw new RestApiException("config can not be null", RestExceptionCode.ClientInputError);
        }
        if (config.getRestEndpoint() == null) { 
        	throw new RestApiException("rest endpoint cannot be null", RestExceptionCode.ClientInputError);
        }
        if (config.getSessionId() == null) { 
        	throw new RestApiException("session ID not found", RestExceptionCode.ClientInputError); 
        }
        
        this.config = config;
        this.baseEndpoint = config.getRestEndpoint();
        this.parser = new Gson();
        
        headers = new HashMap<String, String>();
        headers.put(AUTH_HEADER, AUTH_VALUE_PREFIX + config.getSessionId());
        headers.put(CHARSET_HEADER, CHARSET_VALUE);
        headers.put(CONTENT_HEADER, ContentType.JSON.toString());
        headers.put(PRETTY_HEADER, "1");
    }

    public ConnectorConfig getConfig() {
        return config;
    }

    @Override
    public DescribeGlobal describeGlobal() throws IOException, RestApiException {
		String endpoint = baseEndpoint + SOBJECTS_ENDPOINT;
		
		URL url = new URL(endpoint);
		String json = doHttpGet(url);
		
		return parser.fromJson(json, DescribeGlobal.class);
	}

    @Override
    public DescribeSobject describeSobject(String sobjectName) throws IOException, RestApiException {
		String endpoint = baseEndpoint + SOBJECTS_ENDPOINT +
			sobjectName + SEPARATOR;
		
		URL url = new URL(endpoint);
		String json = doHttpGet(url);
		
		return parser.fromJson(json, DescribeSobject.class);
	}

    @Override
    public DescribeLayout describeLayout(String sobjectName) throws IOException, RestApiException {
		String endpoint = baseEndpoint + SOBJECTS_ENDPOINT +
			sobjectName + SEPARATOR +
			DESCRIBE_SUBENDPOINT;
		
		URL url = new URL(endpoint);
		String json = doHttpGet(url);
		
		return parser.fromJson(json, DescribeLayout.class);
	}

    @Override
	public <T extends SObject> T get(Class<T> clazz, String id) throws IOException, RestApiException {    
		String endpoint = baseEndpoint + 
			SOBJECTS_ENDPOINT + 
			clazz.getSimpleName() + SEPARATOR + 
			id.toString() + SEPARATOR;
		
		URL url = new URL(endpoint);
		String json = doHttpGet(url);
		
		return parser.fromJson(json, clazz);
	}

    @Override
	public SObjectResult create(SObject sobject) throws IOException, RestApiException {
		String endpoint = baseEndpoint + SOBJECTS_ENDPOINT + 
			sobject.getClass().getSimpleName() + SEPARATOR;
        
		URL url = new URL(endpoint);
		String json = doHttpPost(url, parser.toJson(sobject));
		
		return parser.fromJson(json, SObjectResult.class);
	}

    @Override
	public SObjectResult update(SObject sobject, String id) throws IOException {
		String endpoint = baseEndpoint + SOBJECTS_ENDPOINT + 
			sobject.getClass().getSimpleName() + SEPARATOR +
			id + SEPARATOR;
		
		URL url = new URL(endpoint);
		String json = doHttpPatch(url, sobject.toString());
		
		return parser.fromJson(json, SObjectResult.class);
	}

    @Override
	public SObjectResult delete(Class<? extends SObject> clazz, String id) throws IOException, RestApiException {
		String endpoint = baseEndpoint + SOBJECTS_ENDPOINT + 
		    clazz.getSimpleName() + SEPARATOR +
			id + SEPARATOR;
		
		URL url = new URL(endpoint);
		String json = doHttpGet(url);
		
		return parser.fromJson(json, SObjectResult.class);
	}

    @Override
    public QueryResult query(String query) throws IOException, RestApiException {
		assert query != null;
		if(!query.contains("SELECT") ||
				!query.contains("FROM")) {
			throw new IllegalStateException("Query must be in the form: SELECT+id+FROM+sobject+WHERE+something=else");
		}
		
		String endpoint = baseEndpoint + QUERY_ENDPOINT + query;
		URL url = new URL(endpoint);
		String json = doHttpGet(url);
		
		return parser.fromJson(json, QueryResult.class);
	}

    @Override
	public SearchResult search(String search) throws IOException, RestApiException {
		assert search != null;
		if(!search.contains("{") ||
				!search.contains("}") || 
				!search.startsWith("FIND")) {
			throw new IllegalStateException("Search must be in the form: FIND+{myTerm}");
		}
		
		String endpoint = baseEndpoint + SEARCH_ENDPOINT + search;
		URL url = new URL(endpoint);
		String json = doHttpGet(url);
		
		return parser.fromJson(json, SearchResult.class);
	}

    @Override
	public SearchResult recent() throws IOException, RestApiException {
		String endpoint = baseEndpoint + RECENT_ENDPOINT;
		URL url = new URL(endpoint);
		String json = doHttpGet(url);
		
		return parser.fromJson(json, SearchResult.class);
	}
	
	/**
	 * HTTP GET
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws RestApiException
	 */
    private String doHttpGet(URL url) throws IOException, RestApiException {
        System.out.println("GET " + url.toString());
        HttpURLConnection connection = JdkHttpTransport.createConnection(config, url, headers, false);
    	connection.setInstanceFollowRedirects(true);

    	InputStream in;
    	System.out.println("HTTP " + connection.getResponseCode());
    	if(connection.getResponseCode() >= 400) {
    	    in = connection.getErrorStream();
    	} else {
    	    in = connection.getInputStream();
    	}

        String encoding = connection.getHeaderField(CONTENT_HEADER);
        StringBuilder responseBuilder = new StringBuilder();
        if (encoding.startsWith(ContentType.JSON.toString())) {
            BufferedInputStream bin = new BufferedInputStream(in);

            //read the server response body
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = bin.read(buffer)) != -1) {
            	responseBuilder.append(new String(buffer, 0, bytesRead));
            }
            
        } else {
        	throw new IllegalStateException("Unknown content type on HTTP GET.");
        }
        in.close();
        
        //send back the server response
        return responseBuilder.toString();
    }
    
    /**
     * HTTP POST
     * @param url
     * @param body
     * @return
     * @throws IOException
     */
    private String doHttpPost(URL url, String body) throws IOException {		
        System.out.println("POST " + url.toString() + " " + body);
        HttpURLConnection connection = JdkHttpTransport.createConnection(config, url, headers, false);
        connection.setInstanceFollowRedirects(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        
        OutputStream out = connection.getOutputStream();
        InputStream in;
        out.write(body.getBytes());
        out.flush();
        out.close();     
        

        System.out.print("HTTP " + connection.getResponseCode());
        if(connection.getResponseCode() >= 400) {
            in = connection.getErrorStream();
        } else {
            in = connection.getInputStream();
        }
        
        BufferedInputStream bin = new BufferedInputStream(in);

        StringBuilder responseBuilder = new StringBuilder();

        //read the server response body
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = bin.read(buffer)) != -1) {
            responseBuilder.append(new String(buffer, 0, bytesRead));
        }
        String serverResponse = responseBuilder.toString();
        System.out.print(serverResponse + "\r\n");
        
        if(connection.getResponseCode() >= 400) {
            throw new IllegalStateException(serverResponse);
        } else {
            return serverResponse;
        }
    }
    
    /**
     * HTTP PATCH
     * @param url
     * @param body
     * @return
     * @throws IOException
     */
    private String doHttpPatch(URL url, String body) throws IOException {
    	URL patchUrl = new URL(url.toString() + PATCH_PARAMETER);
    	return doHttpPost(patchUrl, body);
    }
}
