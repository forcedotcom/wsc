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

import java.io.IOException;
import java.io.InputStream;

public class LimitingInputStream extends InputStream {

    private int maxSize;
    private int size;
    private InputStream in;

    public LimitingInputStream(int maxSize, InputStream in) {
        this.in = in;
        this.maxSize = maxSize;
    }

    private void checkSizeLimit() throws IOException {
        if (size > maxSize) {
            throw new IOException("Exceeded max size limit of " +
                    maxSize + " with response size " + size);
        }
    }

    @Override
    public int read() throws IOException {
        int result = in.read();
        size++;
        checkSizeLimit();
        return result;
    }

    @Override
    public int read(byte b[]) throws IOException {
        int len = in.read(b);
        size += len;
        checkSizeLimit();
        return len;
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        int length = in.read(b, off, len);
        size += length;
        checkSizeLimit();
        return length;
     }

    @Override
    public long skip(long n) throws IOException {
        long len = in.skip(n);
        size += len;
        checkSizeLimit();
        return len;
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();

    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }
}
