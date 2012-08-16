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