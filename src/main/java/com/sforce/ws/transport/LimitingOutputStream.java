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

import java.io.IOException;
import java.io.OutputStream;

public class LimitingOutputStream extends OutputStream {
	@Deprecated
	private int size = 0;
	private long sizeLong = 0L;
    @Deprecated
    private int maxSize;
    private long maxSizeLong;
    private OutputStream out;
    private static final String MAX_SIZE_EXCEEDED_ERROR =  "Exceeded max size limit of %d%n with request size %d%n";

    /**
     * Please use {@link com.sforce.ws.transport.LimitingOutputStream#LimitingOutputStream(long, OutputStream) instead.
     */
    @Deprecated
    public LimitingOutputStream(int maxSize, OutputStream out) {
        this.maxSize = maxSize;
        this.maxSizeLong = maxSize;
        this.out = out;
    }
    
    public LimitingOutputStream(long maxSize, OutputStream out) {
        this.maxSizeLong = maxSize;
        this.out = out;
    } 

    /**
     * Please use @see {@link com.sforce.ws.transport.LimitingOutputStream#getSizeLong() instead.
     */
    @Deprecated
    public int getSize() {
        return size;
    }
    
    public long getSizeLong() {
        return sizeLong;
    }

    @Override
    public void write(int b) throws IOException {
        size++;
        sizeLong++;
        checkSizeLimit();
        out.write(b);
    }

    private void checkSizeLimit() throws IOException {
    	if (sizeLong > maxSizeLong) {
            throw new IOException(String.format(MAX_SIZE_EXCEEDED_ERROR, maxSizeLong, sizeLong));
    	}
    }

    @Override
    public void write(byte b[]) throws IOException {
    	int length = b.length;
        size += length;
        sizeLong += length;
        checkSizeLimit();
        out.write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        size += len;
        sizeLong += len;
        checkSizeLimit();
        out.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
