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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.Test;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.MessageHandler;
import com.sforce.ws.transport.Transport;
import com.sforce.ws.transport.TransportFactory;

public class JwtConnectionTest {

	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String INSTANCE_URL = "https://bulgari--env.sandbox.my.salesforce.com";
	private static final String ERROR = "ERROR_TYPE";
	private static final String ERROR_DESC = "ERROR_DESC";
	private static final String RESPONSE_JSON = "{" + // 
	        "\"access_token\": \"" + ACCESS_TOKEN + "\"," + //
	        "\"scope\": \"id api\"," + //
	        "\"instance_url\": \"" + INSTANCE_URL + "\"," + //
	        "\"id\": \"https://test.salesforce.com/id/SESSION_ID\"," + //
	        "\"token_type\": \"Bearer\"" + //
	        "}";
	private static final String RESPONSE_JSON_ERROR = "{" + // 
	        "\"error\": \"" + ERROR + "\"," + //
	        "\"error_description\": \"" + ERROR_DESC + "\"" + //
	        "}";

	@Test
	public void testInvalidLoginURL() {
		ConnectorConfig config = new ConnectorConfig();
		config.setClientId("CLIENT_ID");
		config.setUsername("USERNAME");
		config.setJwtEndPointUrl("JWT_ENDPOINT");
		String invalidLoginUrl = "JWT_INVALID_LOGIN";
		config.setJwtLoginUrl(invalidLoginUrl);
		config.setJwtPrivateKey(obtainPrivateKey());
		Throwable exception = assertThrows(ConnectionException.class, () -> {
			new JwtConnection(config).send();
		});
		assertEquals("Failed to send request to " + invalidLoginUrl, exception.getMessage());
	}

	@Test
	public void testSend() {
		ConnectorConfig config = new ConnectorConfig();
		config.setClientId("CLIENT_ID");
		config.setUsername("USERNAME");
		config.setAuthEndpoint("https://DOMAIN" + ConnectorConfig.ENTERPRISE_ENDPOINT_PATH + "50.0/");
		config.setJwtEndPointUrl("JWT_ENDPOINT");
		config.setJwtLoginUrl("JWT_LOGIN");
		config.setJwtPrivateKey(obtainPrivateKey());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(RESPONSE_JSON.getBytes());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		config.setTransportFactory(new MockTransportFactory(stream, inputStream, true, config));
		JwtConnectionMock connectionMock = new JwtConnectionMock(config);
		JwtResult result = null;
		try {
			result = connectionMock.send();
		} catch (JwtConnectionException e) {
			fail(e.getMessage());
		}
		assertNotNull(result);
		assertNotNull(result.getJwtResponse());
		assertNotNull(result.getJwtResponse().getAccess_token());
		assertNotNull(result.getServerUrl());
		assertTrue(result.getServerUrl().contains(ConnectorConfig.ENTERPRISE_ENDPOINT_PATH));
	}

	@Test
	public void testReceiveMapping() {
		ConnectorConfig config = new ConnectorConfig();
		config.setClientId("CLIENT_ID");
		config.setUsername("USERNAME");
		config.setJwtEndPointUrl("JWT_ENDPOINT");
		config.setJwtLoginUrl("JWT_LOGIN");
		config.setJwtPrivateKey(obtainPrivateKey());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(RESPONSE_JSON.getBytes());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		config.setTransportFactory(new MockTransportFactory(stream, inputStream, true, config));
		JwtConnection connection = new JwtConnection(config);

		Method receiveMethod = null;
		Method newTransportMethod = null;
		try {
			receiveMethod = JwtConnection.class.getDeclaredMethod("receive", Transport.class, InputStream.class);
			receiveMethod.setAccessible(true);
			newTransportMethod = JwtConnection.class.getDeclaredMethod("newTransport", ConnectorConfig.class);
			newTransportMethod.setAccessible(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			Object transport = newTransportMethod.invoke(connection, config);
			assertTrue(transport instanceof Transport);
			Object jwtResponse = receiveMethod.invoke(connection, (Transport) transport, inputStream);
			assertTrue(jwtResponse instanceof JwtResponse);
			assertEquals(ACCESS_TOKEN, ((JwtResponse) jwtResponse).getAccess_token());
			assertEquals(INSTANCE_URL, ((JwtResponse) jwtResponse).getInstance_url());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testReceiveErrorMapping() {
		ConnectorConfig config = new ConnectorConfig();
		config.setClientId("CLIENT_ID");
		config.setUsername("USERNAME");
		config.setJwtEndPointUrl("JWT_ENDPOINT");
		config.setJwtLoginUrl("JWT_LOGIN");
		config.setJwtPrivateKey(obtainPrivateKey());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(RESPONSE_JSON_ERROR.getBytes());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		config.setTransportFactory(new MockTransportFactory(stream, inputStream, false, config));
		JwtConnection connection = new JwtConnection(config);

		Method newTransportMethod = null;
		Method exceptionMessageFormatMethod = null;
		try {
			newTransportMethod = JwtConnection.class.getDeclaredMethod("newTransport", ConnectorConfig.class);
			newTransportMethod.setAccessible(true);
			exceptionMessageFormatMethod = JwtConnection.class.getDeclaredMethod("exceptionMessageFormat", String.class, String.class);
			exceptionMessageFormatMethod.setAccessible(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			Object transport = newTransportMethod.invoke(connection, config);
			assertTrue(transport instanceof Transport);
			Throwable exception = assertThrows(InvocationTargetException.class, () -> {
				Method receiveMethod = JwtConnection.class.getDeclaredMethod("receive", Transport.class, InputStream.class);
				receiveMethod.setAccessible(true);
				receiveMethod.invoke(connection, (Transport) transport, inputStream);
			});
			assertTrue(exception.getCause() instanceof JwtConnectionException);
			JwtConnectionException jwtException = (JwtConnectionException) exception.getCause();
			Object expectedMessage = ERROR + " - " + ERROR_DESC;
			Object expectedMessageBuilt = exceptionMessageFormatMethod.invoke(connection, ERROR, ERROR_DESC);
			assertTrue(expectedMessage instanceof String);
			assertEquals((String) expectedMessage, expectedMessageBuilt);
			assertEquals((String) expectedMessage, jwtException.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	private PrivateKey obtainPrivateKey() {
		try {
			KeyStore keystore = KeyStore.getInstance("JKS");
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("selfSignedTest.jks");
			keystore.load(inputStream, "selfSignedTest".toCharArray());
			return (PrivateKey) keystore.getKey("selfSignedTest", "selfSignedTest".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
			e.printStackTrace();
			return null;
		}
	}

	class JwtConnectionMock extends JwtConnection {

		public JwtConnectionMock(ConnectorConfig config) {
			super(config);
		}

		@Override
		protected OutputStream jwtConnect(Transport transport, String uri, HashMap<String, String> httpHeaders, boolean enableCompression)
		        throws IOException {
			return new ByteArrayOutputStream();
		}
	}

	public static class MockTransportFactory implements TransportFactory {

		private final ByteArrayOutputStream outputStream;
		private final ByteArrayInputStream responseStream;
		private final boolean isSuccess;
		private final ConnectorConfig config;

		public MockTransportFactory(ByteArrayOutputStream outputStream, ByteArrayInputStream responseStream, boolean isSuccess,
		        ConnectorConfig config) {
			this.outputStream = outputStream;
			this.responseStream = responseStream;
			this.isSuccess = isSuccess;
			this.config = config;
		}

		@Override
		public Transport createTransport() {
			return new MockTransport(outputStream, responseStream, isSuccess, config);
		}
	}

	public static class MockTransport implements Transport {

		private final ByteArrayInputStream responseStream;
		private final ByteArrayOutputStream outputStream;
		private final boolean isSuccess;
		private final ConnectorConfig config;

		public MockTransport(ByteArrayOutputStream outputStream, ByteArrayInputStream responseStream, boolean isSuccess, ConnectorConfig config) {
			this.outputStream = outputStream;
			this.responseStream = responseStream;
			this.isSuccess = isSuccess;
			this.config = config;
		}

		@Override
		public void setConfig(ConnectorConfig config) {
		}

		@Override
		public OutputStream connect(String url, String soapAction) throws IOException {
			return outputStream;
		}

		@Override
		public InputStream getContent() throws IOException {
			byte[] bytes = new byte[1024];
			responseStream.read(bytes);
			Iterator<MessageHandler> messagerHandlers = config.getMessagerHandlers();
			while (messagerHandlers.hasNext()) {
				MessageHandler next = messagerHandlers.next();
				next.handleResponse(new URL("http://www.salesforce.com"), bytes);
			}
			responseStream.reset();
			return responseStream;
		}

		@Override
		public boolean isSuccessful() {
			return isSuccess;
		}

		@Override
		public OutputStream connect(String endpoint, HashMap<String, String> headers) throws IOException {
			return outputStream;
		}

		@Override
		public OutputStream connect(String endpoint, HashMap<String, String> httpHeaders, boolean b) throws IOException {
			return outputStream;
		}
	}
}
