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

import com.sforce.oauth.model.OAuthCallbackResult;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class OAuthCallbackServer {

    private final CompletableFuture<OAuthCallbackResult> callbackFuture;
    private final ExecutorService executor;
    private final HttpServer server;
    
    public OAuthCallbackServer(int port, String callbackPath, String expectedState) throws IOException {
        
        this.callbackFuture = new CompletableFuture<>();
        OAuthCallbackHandler defaultHandler = new OAuthCallbackHandler(this.callbackFuture, expectedState);
        
        this.executor = Executors.newSingleThreadExecutor();
        
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.setExecutor(executor);
        this.server.createContext(callbackPath, defaultHandler);
    }

    public OAuthCallbackServer(URI uri, String expectedState) throws IOException {
        this(uri.getPort(), uri.getPath(), expectedState);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);

            executor.shutdown();
            try {
                // Wait for existing tasks to terminate
                if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executor.shutdownNow();
            }
        }
    }

    public OAuthCallbackResult waitForCallback(long timeoutMillis) throws ExecutionException, InterruptedException, TimeoutException {
        return callbackFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
    }
}
