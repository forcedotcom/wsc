package com.sforce.ws.transport;

import java.io.IOException;
import java.io.OutputStream;

public class LimitingOutputStream extends OutputStream {
    private int size = 0;
    private int maxSize;
    private OutputStream out;

    public LimitingOutputStream(int maxSize, OutputStream out) {
        this.maxSize = maxSize;
        this.out = out;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void write(int b) throws IOException {
        size++;
        checkSizeLimit();
        out.write(b);
    }

    private void checkSizeLimit() throws IOException {
        if (size > maxSize) {
            throw new IOException("Exceeded max size limit of " +
                    maxSize + " with request size " + size);
        }
    }

    @Override
    public void write(byte b[]) throws IOException {
        size += b.length;
        checkSizeLimit();
        out.write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        size += len;
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