/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the
 * following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
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

package com.sforce.oauth.flow;

import com.sforce.oauth.exception.OAuthException;
import com.sforce.oauth.model.OAuthCallbackResult;
import com.sforce.oauth.model.OAuthTokenResponse;
import com.sforce.oauth.server.OAuthCallbackServer;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Desktop.*;

public class WebServerFlow extends AbstractOAuthFlow {

    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE = "code";

    private String state;
    private String codeChallenge;
    private String codeVerifier;
    private OAuthCallbackResult oAuthCallbackResult;

    @Override
    public OAuthTokenResponse getToken(ConnectorConfig config) throws OAuthException, ConnectionException {
        if (!isConfigValid(config)) {
            throw new OAuthException("Invalid OAuth configuration: missing required parameters (client_id, token_endpoint, authorization_code, or redirect_uri)");
        }

        try {
            final URI authorizationURI = buildAuthorizationUri(config);

            OAuthCallbackServer oAuthCallbackServer = new OAuthCallbackServer(new URI(config.getRedirectUri()), this.state);
            oAuthCallbackServer.start();

            launchBrowser(authorizationURI);

            this.oAuthCallbackResult = oAuthCallbackServer.waitForCallback(60000L);
            if (!oAuthCallbackResult.isSuccess()) {
                throw new OAuthException("Authorization failed: " + oAuthCallbackResult.getErrorText());
            }

            final Map<String, String> requestHeaders = createHeaders(config);
            final String requestBody = createRequestBody(config);
            final String tokenEndpoint = config.getTokenEndpoint();

            return executeTokenRequest(config, tokenEndpoint, requestHeaders, requestBody);
        } catch (IOException ex) {
            throw new ConnectionException("Error establishing token request connection.", ex);
        } catch (Exception ex) {
            if (ex instanceof OAuthException) throw (OAuthException) ex;
            throw new ConnectionException("An unexpected error occurred during the OAuth flow.", ex);
        }
    }

    private URI buildAuthorizationUri(ConnectorConfig config) throws NoSuchAlgorithmException, URISyntaxException {
        StringBuilder authorizationURLBuilder =
                new StringBuilder(config.getAuthEndpoint());
        String encodedRedirectURI =
                URLEncoder.encode(config.getRedirectUri(), StandardCharsets.UTF_8);
        this.state = generateState();

        authorizationURLBuilder
                .append("?response_type=").append(RESPONSE_TYPE)
                .append("&client_id=").append(config.getClientId())
                .append("&redirect_uri=").append(encodedRedirectURI)
                .append("&state=").append(state);

        if (config.isEnablePKCE()) {
            this.codeVerifier = generateCodeVerifier();
            this.codeChallenge = generateCodeChallenge(codeVerifier);

            authorizationURLBuilder
                    .append("&code_challenge=").append(codeChallenge)
                    .append("&code_challenge_method=S256");
        }
        return new URI(authorizationURLBuilder.toString());
    }

    @Override
    protected boolean isConfigValid(ConnectorConfig config) {

        if (config == null) {
            return false;
        }

        String clientId = config.getClientId();
        String redirectURI = config.getRedirectUri();
        String tokenEndpoint = config.getTokenEndpoint();
        String authorizationEndpoint = config.getAuthorizationEndpoint();

        return clientId != null && !clientId.trim().isEmpty() &&
                redirectURI != null && !redirectURI.trim().isEmpty() &&
                tokenEndpoint != null && !tokenEndpoint.trim().isEmpty() &&
                authorizationEndpoint != null && !authorizationEndpoint.trim().isEmpty();
    }

    @Override
    protected String createRequestBody(ConnectorConfig config) {
        StringBuilder body = new StringBuilder();

        body.append("grant_type=").append(GRANT_TYPE);
        body.append("&code=").append(oAuthCallbackResult.getCode());
        body.append("&client_id=").append(config.getClientId());
        body.append("&redirect_uri=").append(config.getRedirectUri());

        String clientSecret = config.getClientSecret();
        if (clientSecret != null && !clientSecret.trim().isEmpty()) {
            body.append("&client_secret=").append(clientSecret);
        }

        if (config.isEnablePKCE()) {
            body.append("&code_verifier=").append(codeVerifier);
        }
        return body.toString();
    }

    @Override
    protected Map<String, String> createHeaders(ConnectorConfig config) {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put(CONTENT_TYPE_HEADER, "application/x-www-form-urlencoded");
        requestHeaders.put(ACCEPT_HEADER, "application/json");
        return requestHeaders;
    }


    private String generateState() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String generateCodeVerifier() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String generateCodeChallenge(String verifier) throws NoSuchAlgorithmException {
        byte[] bytes = verifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    private void launchBrowser(URI uri) throws IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
            Desktop.getDesktop().browse(uri);
        }
    }
}
