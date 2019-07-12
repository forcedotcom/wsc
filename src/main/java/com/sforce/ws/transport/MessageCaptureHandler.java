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
package com.sforce.ws.transport;

import com.sforce.ws.MessageHandler;

import java.net.URL;

public class MessageCaptureHandler implements MessageHandler {
    private static final ThreadLocal<ByteBuffer> requestBuffer = new ThreadLocal<>();
    private static final ThreadLocal<ByteBuffer> responseBuffer = new ThreadLocal<>();

    @Override
    public void handleRequest(URL endpoint, byte[] request) {
        requestBuffer.set(new ByteBuffer(request));
    }

    @Override
    public void handleResponse(URL endpoint, byte[] response) {
        responseBuffer.set(new ByteBuffer(response));
    }

    protected String getRequestString() {
        ByteBuffer buffer = this.requestBuffer.get();
        return getString(buffer);
    }

    private String getString(ByteBuffer byteBuffer) {
        if (byteBuffer == null || byteBuffer.array == null) {
            return "No Content";
        } else {
            return new String(byteBuffer.array);
        }
    }

    protected String getResponseString() {
        ByteBuffer buffer = this.responseBuffer.get();
        return getString(buffer);
    }

    private static class ByteBuffer {
        public final byte[] array;

        ByteBuffer(byte[] array) {
            this.array = array;
        }
    }
}
