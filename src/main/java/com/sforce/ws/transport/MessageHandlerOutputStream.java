package com.sforce.ws.transport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;

import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.MessageHandler;

public class MessageHandlerOutputStream extends OutputStream {
    private final ByteArrayOutputStream bout = new ByteArrayOutputStream();
    private final OutputStream output;
	private final URL url;
	private final ConnectorConfig config;

    public MessageHandlerOutputStream(ConnectorConfig config, URL url, OutputStream output) {
    	this.url = url;
        this.output = output;
        this.config = config;
    }

    @Override
    public void write(int b) throws IOException {
        bout.write((char) b);
        output.write(b);
    }

    @Override
    public void write(byte b[]) throws IOException {
        bout.write(b);
        output.write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        bout.write(b, off, len);
        output.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        bout.close();
        output.close();

        Iterator<MessageHandler> it = config.getMessagerHandlers();

        while(it.hasNext()) {
            MessageHandler handler = it.next();
            handler.handleRequest(url, bout.toByteArray());
        }
    }
}