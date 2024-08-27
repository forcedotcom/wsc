/*
 * Copyright (c) 2017, salesforce.com, inc.
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
package com.sforce.jwt;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.transport.Transport;

public class JwtConnection {

	private JwtTokenBuilder jwtTokenBuilder;
	private ConnectorConfig config;
	private String jwtUrl;

	public JwtConnection(ConnectorConfig config) {
		this.config = config;
		this.jwtUrl = config.getJwtLoginUrl();
		this.jwtTokenBuilder = new JwtTokenBuilder();
	}

	public JwtResult send() throws JwtConnectionException {
		long startTime = System.currentTimeMillis();
		try {

			Transport transport = newTransport(config);

			HashMap<String, String> httpHeaders = new HashMap<>();
			httpHeaders.put("Content-Type", "application/x-www-form-urlencoded");
			OutputStream out = jwtConnect(transport, jwtUrl, httpHeaders, false);
			Map<String, Object> parameters = buildJwtUrlParameters();
			sendRequest(out, buildParamData(parameters));
			InputStream in = transport.getContent();
			JwtResponse jwtResponse = receive(transport, in);

			JwtResult jwtResult = new JwtResult();
			jwtResult.setJwtResponse(jwtResponse);
			jwtResult.setServerUrl(buildServiceEndpoint(jwtResponse));

			return jwtResult;

		} catch (SocketTimeoutException e) {
			long timeTaken = System.currentTimeMillis() - startTime;
			throw new JwtConnectionException("Request to " + jwtUrl + " timed out. TimeTaken=" + timeTaken + " ConnectionTimeout="
			        + config.getConnectionTimeout() + " ReadTimeout=" + config.getReadTimeout(), e);
		} catch (IOException e) {
			throw new JwtConnectionException("Failed to send request to " + jwtUrl, e);
		}
	}

	protected Map<String, Object> buildJwtUrlParameters() throws JwtConnectionException {
		String token = jwtTokenBuilder.build(config);
		Map<String, Object> parameters = new LinkedHashMap<>();
		parameters.put("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
		parameters.put("assertion", token);
		return parameters;
	}

	protected OutputStream jwtConnect(Transport transport, String uri, HashMap<String, String> httpHeaders, boolean enableCompression)
	        throws IOException {
		return transport.connect(uri, httpHeaders, enableCompression);
	}

	protected byte[] buildParamData(Map<String, Object> parameters) throws UnsupportedEncodingException {
		StringBuilder data = new StringBuilder();
		for (Map.Entry<String, Object> p : parameters.entrySet()) {
			if (data.length() != 0) {
				data.append('&');
			}
			data.append(URLEncoder.encode(p.getKey(), StandardCharsets.UTF_8.toString()));
			data.append('=');
			data.append(URLEncoder.encode(String.valueOf(p.getValue()), StandardCharsets.UTF_8.toString()));
		}
		return data.toString().getBytes(StandardCharsets.UTF_8.toString());
	}

	protected String buildServiceEndpoint(JwtResponse jwtResponse) {
		String authUrl = config.getAuthEndpoint();
		String path = findCurrentEndPointPath(authUrl);
		String tail = authUrl.substring(authUrl.indexOf(path) + path.length());
		String version = tail.contains("/") ? tail.substring(0, tail.indexOf("/")) : tail;
		return jwtResponse.getInstance_url() + path + version + "/";
	}

	private String findCurrentEndPointPath(String inputUrl) {
		if (inputUrl.contains(ConnectorConfig.PARTNER_ENDPOINT_PATH)) {
			return ConnectorConfig.PARTNER_ENDPOINT_PATH;
		} else if (inputUrl.contains(ConnectorConfig.TOOLING_ENDPOINT_PATH)) {
			return ConnectorConfig.TOOLING_ENDPOINT_PATH;
		} else {
			return ConnectorConfig.ENTERPRISE_ENDPOINT_PATH;
		}
	}

	private void sendRequest(OutputStream out, byte[] dataBytes) throws IOException {
		try (DataOutputStream outputStream = new DataOutputStream(out)) {
			outputStream.write(dataBytes);
		}
	}

	private JwtResponse receive(Transport transport, InputStream in) throws IOException, JwtConnectionException {
		Reader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.toString()));
		StringBuilder stringBuilder = new StringBuilder();
		for (int chr; (chr = bufferedReader.read()) >= 0;) {
			stringBuilder.append((char) chr);
		}
		String jwtResponseJson = stringBuilder.toString();
		JwtResponse jwtResponse = new ObjectMapper().readValue(jwtResponseJson, JwtResponse.class);
		if (transport.isSuccessful()) {
			return jwtResponse;
		} else {
			throw createException(jwtResponse);
		}
	}

	private JwtConnectionException createException(JwtResponse jwtResponse) {
		if (jwtResponse == null || jwtResponse.getError() == null || jwtResponse.getError().trim().isEmpty()) {
			return new JwtConnectionException("Unknown JWT connection exception");
		}
		String error = jwtResponse.getError();
		String desc = jwtResponse.getError_description() != null && !jwtResponse.getError_description().trim().isEmpty() //
		        ? jwtResponse.getError_description() //
		        : "unknown"; //
		String exceptionMessage = exceptionMessageFormat(error, desc);
		return new JwtConnectionException(exceptionMessage);
	}

	private String exceptionMessageFormat(String error, String description) {
		return new MessageFormat("{0} - {1}").format(new String[] { error, description });
	}

	private Transport newTransport(ConnectorConfig config) throws JwtConnectionException {
		if (config.getTransportFactory() != null) {
			Transport t = config.getTransportFactory().createTransport();
			t.setConfig(config);
			return t;
		}
		try {
			Transport t = (Transport) config.getTransport().newInstance();
			t.setConfig(config);
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JwtConnectionException("Failed to create new Transport " + config.getTransport());
		}
	}

}
