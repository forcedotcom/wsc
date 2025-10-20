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

package com.sforce.oauth.server;

import com.sforce.oauth.exception.OAuthException;
import com.sforce.oauth.model.OAuthCallbackResult;
import com.sforce.oauth.util.OAuthUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class OAuthCallbackHandler implements HttpHandler {

    private final CompletableFuture<OAuthCallbackResult> authCodeFuture;
    private final String expectedState;

    public OAuthCallbackHandler() {
        this(new CompletableFuture<>(), null);
    }

    public OAuthCallbackHandler(CompletableFuture<OAuthCallbackResult> authCodeFuture, String expectedState) {
        this.authCodeFuture = authCodeFuture;
        this.expectedState = expectedState;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        OAuthCallbackResult oAuthCallbackResult = buildOauthCallbackResult(httpExchange);
        if (oAuthCallbackResult.isSuccess()) {
            sendSuccessResponse(httpExchange);
            authCodeFuture.complete(oAuthCallbackResult);
        } else {
            sendErrorResponse(httpExchange, oAuthCallbackResult.getErrorText());
            authCodeFuture.completeExceptionally(new OAuthException(oAuthCallbackResult.getError()));
        }
    }

    private OAuthCallbackResult buildOauthCallbackResult(HttpExchange httpExchange) {

        String queryString = httpExchange.getRequestURI().getQuery();

        if (queryString == null || queryString.trim().isEmpty()) {
            throw new RuntimeException("No query parameters found");
        }
        Map<String, String> queryParams = OAuthUtil.getQueryParams(queryString);

        if (!hasValidStateParam(queryParams.get("state"))) {
            return OAuthCallbackResult.createError("invalid_state", "State parameter mismatch");
        }
        
        
        if (queryParams.containsKey("code")) {
            return OAuthCallbackResult.createSuccess(queryParams.get("code"), queryParams.get("state"));
        } else if (queryParams.containsKey("error") || queryParams.containsKey("error_description")) {
            return OAuthCallbackResult.createError(queryParams.get("error"), queryParams.get("error_description"));
        }
        return OAuthCallbackResult.createError("invalid_query_string", "Could not parse request query string: " + queryString);
    }

    private boolean hasValidStateParam(String actualState) {
        if (expectedState == null) {
            return true; // bypass state check
        }
        return Objects.equals(expectedState, actualState);
    }

    private void sendSuccessResponse(HttpExchange exchange) throws IOException {
        String response = getSuccessPage();
        sendResponse(exchange, 200, response);
    }

    private void sendErrorResponse(HttpExchange exchange, String errorMessage) throws IOException {
        String response = getErrorPage(errorMessage);
        sendResponse(exchange, 400, response);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String content) throws IOException {
        byte[] responseBytes = content.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
    
    private String getSuccessPage() {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Authorization Successful</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }" +
                "h1 { color: #28a745; }" +
                "p { color: #666; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1> Authorization Successful!</h1>" +
                "<p>You have successfully authorized the application.</p>" +
                "<p>You can close this window and return to the application.</p>" +
                "</body>" +
                "</html>";
    }

    private String getErrorPage(String errorMessage) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Authorization Error</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }" +
                "h1 { color: #dc3545; }" +
                "p { color: #666; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>Authorization Error</h1>" +
                "<p>An error occurred during authorization: " + errorMessage + "</p>" +
                "</body>" +
                "</html>";
    }
}
