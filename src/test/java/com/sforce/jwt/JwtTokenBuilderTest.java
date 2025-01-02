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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;

import org.junit.Test;

import com.sforce.ws.ConnectorConfig;

public class JwtTokenBuilderTest {

	@Test
	public void testInvalidPrivateKey() {
		Throwable exception = assertThrows(JwtConnectionException.class, () -> {
			new JwtTokenBuilder().build(new ConnectorConfig());
		});
		assertEquals("A private key is required for JWT authentication token building", exception.getMessage());
	}

	@Test
	public void testTokenBuilding() throws Exception {
		ConnectorConfig config = new ConnectorConfig();
		String inputClientId = "CLIENT_ID";
		config.setClientId(inputClientId);
		config.setUsername("USERNAME");
		config.setJwtEndPointUrl("JWT_ENDPOINT");
		config.setJwtPrivateKey(obtainPrivateKey());
		String token = new JwtTokenBuilder().build(config);
		assertTrue(token != null && !token.trim().isEmpty());
		String clientId = Base64.getUrlEncoder().encodeToString(inputClientId.getBytes(StandardCharsets.UTF_8.toString()));
		assertTrue(token.contains(clientId));
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
}
