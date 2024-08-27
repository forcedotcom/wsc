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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.UUID;

import com.sforce.ws.ConnectorConfig;

public class JwtTokenBuilder {

	public String build(ConnectorConfig config) throws JwtConnectionException {

		String header = "{\"alg\":\"RS256\"}";
		String claimTemplate = "'{'\"iss\": \"{0}\", \"sub\": \"{1}\", \"aud\": \"{2}\", \"exp\": \"{3}\", \"jti\": \"{4}\"'}'";

		try {
			
			if(config.getJwtPrivateKey() == null) {
				throw new JwtConnectionException("A private key is required for JWT authentication token building");
			}
			
			StringBuffer token = new StringBuffer();

			//Encode the JWT Header and add it to our string to sign
			token.append(Base64.getUrlEncoder().encodeToString(header.getBytes(StandardCharsets.UTF_8.toString())));

			//Separate with a period
			token.append(".");

			//Create the JWT Claims Object
			String[] claimArray = new String[5];
			claimArray[0] = config.getClientId();
			claimArray[1] = config.getUsername();
			claimArray[2] = config.getJwtEndPointUrl();
			claimArray[3] = Long.toString((System.currentTimeMillis() / 1000) + 300);
			claimArray[4] = UUID.randomUUID().toString();
			MessageFormat claims;
			claims = new MessageFormat(claimTemplate);
			String payload = claims.format(claimArray);

			//Add the encoded claims object
			token.append(Base64.getUrlEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8.toString())));

			//Load the private key
			PrivateKey privateKey = config.getJwtPrivateKey();

			//Sign the JWT Header + "." + JWT Claims Object
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(privateKey);
			signature.update(token.toString().getBytes(StandardCharsets.UTF_8.toString()));
			String signedPayload = Base64.getUrlEncoder().encodeToString(signature.sign());

			//Separate with a period
			token.append(".");

			//Add the encoded signature
			token.append(signedPayload);

			return token.toString();

		} catch (NoSuchAlgorithmException | IOException | InvalidKeyException | SignatureException e) {
			throw new JwtConnectionException(e.getMessage(), e);
		}
	}
}
