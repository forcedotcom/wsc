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
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.MessageHandler;
import com.sforce.ws.tools.VersionInfo;
import com.sforce.ws.util.Base64;

import com.google.appengine.api.urlfetch.*;

/**
 * This class is an implementation of Transport using the Google AppEngine
 * urlFetch
 * 
 * @author Ron Hess
 * @version 1.0
 * @since 1.0 Jan 29 2009
 */
public class GaeHttpTransport implements Transport {

  private static Logger log = Logger.getLogger("soapconnection send");
  private URLFetchService connection;
  private HTTPRequest request;

  private boolean successful;

  private ConnectorConfig config; // not much is used from here
  private URL url;

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
  public OutputStream connect(String uri, HashMap<String, String> httpHeaders, boolean enableCompression) throws IOException {
    return wrapOutput(connectRaw(uri, httpHeaders, enableCompression),enableCompression);
  }

  private OutputStream wrapOutput(OutputStream output, boolean enableCompression) throws IOException {
    if (config.getMaxRequestSize() > 0) {
      output = new LimitingOutputStream(config.getMaxRequestSize(), output);
    }

    // when we are writing a zip file we don't bother with compression
    if (enableCompression && config.isCompression()) {
      output = new GZIPOutputStream(output);
    }

    return output;
  }

  private OutputStream connectRaw(String uri, Map<String, String> httpHeaders,
      boolean enableCompression) throws IOException {

    log.info(uri);

    url = new URL(uri);

    request = createRequest(config, url, httpHeaders, enableCompression);

    output = new ByteArrayOutputStream();
    return output;

  }

  public static HTTPRequest createRequest(ConnectorConfig config, URL url,
      Map<String, String> httpHeaders) throws IOException {
    return createRequest(config, url, httpHeaders, true);
  }

  public static HTTPRequest createRequest(ConnectorConfig config, URL url,
      Map<String, String> httpHeaders, boolean enableCompression)
      throws IOException {

    if (config.isTraceMessage()) {
      config.getTraceStream().println(
          "WSC: Creating a new connection to " + url + " Proxy = "
              + config.getProxy() + " username " + config.getProxyUsername());
    }

    FetchOptions fetchOptions = FetchOptions.Builder.withDeadline(config
        .getReadTimeout() / 1000);
    HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST, fetchOptions);

    if (httpHeaders == null
        || (httpHeaders.get("User-Agent") == null && httpHeaders
            .get("user-agent") == null)) {
      request.addHeader(new HTTPHeader("User-Agent", VersionInfo.info()));
    }

    if (config.getHeaders() != null) {
      for (Entry<String, String> ent : config.getHeaders().entrySet()) {
        request.addHeader(new HTTPHeader(ent.getKey(), ent.getValue()));
      }
    }

    if (enableCompression && config.isCompression()) {
      request.addHeader(new HTTPHeader("Content-Encoding", "gzip"));
      request.addHeader(new HTTPHeader("Accept-Encoding", "gzip"));
    }

    if (config.getProxyUsername() != null) {
      String token = config.getProxyUsername() + ":"
          + config.getProxyPassword();
      String auth = "Basic " + new String(Base64.encode(token.getBytes()));
      request.addHeader(new HTTPHeader("Proxy-Authorization", auth));
      request.addHeader(new HTTPHeader("Https-Proxy-Authorization", auth));
    }

    if (httpHeaders != null) {
      for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
        request.addHeader(new HTTPHeader(entry.getKey(), entry.getValue()));
      }
    }

    return request;
  }

  /** Gets the values of all headers with the name {@code headerName}. */
  List<String> getHeaders(HTTPResponse resp, String headerName) {
    List<String> b = new ArrayList<String>();
    for (HTTPHeader h : resp.getHeadersUncombined()) {
      if (headerName.equalsIgnoreCase(h.getName())) {
        b.add(h.getValue());
      }
    }
    return b;
  }

  String getHeader(HTTPResponse resp, String headerName) {
    for (HTTPHeader h : resp.getHeadersUncombined()) {
      if (headerName.equalsIgnoreCase(h.getName())) {
        return h.getValue();
      }
    }
    return null;
  }

  @Override
  public InputStream getContent() throws IOException {
    InputStream in;
    HTTPResponse resp;

    try {
      successful = true;
      if (config.isTraceMessage()) {
        log.info(output.toString()); // educational
      }

      // payload comes from bytes written to the output stream
      this.request.setPayload(output.toByteArray());

      // go get that soap, read/write to Force.com API
      resp = connection.fetch(this.request);

      byte[] bytes = resp.getContent();

      final String contentid = "Content-ID: <root.message@cxf.apache.org>";
      String str = new String(bytes);
      if (str.contains(contentid)) {
        str = str.split(contentid)[1].trim();
      }
      bytes = str.getBytes();

      // dump out the response
      if (config.isTraceMessage()) {
        log.info(new String(bytes));
      }
      in = new ByteArrayInputStream(bytes);

      this.successful = resp.getResponseCode() < 400;
      String encoding = getHeader(resp, "Content-Encoding");

      if (config.getMaxResponseSize() > 0) {
        in = new LimitingInputStream(config.getMaxResponseSize(), in);
      }

      if ("gzip".equals(encoding)) {
        in = new GZIPInputStream(in);
      }

      // dump out the response
      if (config.isTraceMessage()) {
        log.info(new String(bytes));
      }
    } catch (IOException e) {
      successful = false;
      in = new ByteArrayInputStream(e.getMessage().getBytes());
      log.warning("getContent: " + e.getMessage());
    }
    return in;

  }

  public boolean isSuccessful() {
    return successful;
  }

}
