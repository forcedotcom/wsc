package com.sforce.bulk;

import com.sforce.ws.ConnectorConfig;

import java.io.PrintStream;

/**
 * This class represents a Handler for Stream status
 *
 * <p/>
 * User: mcheenath
 * Date: Dec 14, 2010
 */
public class StreamHandler {

    private static final String BULK_TAG = "BULK-STREAM:";

    private final ConnectorConfig config;

    private boolean shutdown = false;
    private int errorCount;

    public StreamHandler() {
        config = new ConnectorConfig();
    }

    public ConnectorConfig getConfig() {
        return config;
    }

    public PrintStream getLogStream() {
        return System.out;
    }

    public void info(String message) {
        getLogStream().println(BULK_TAG + "INFO:" + message);
    }

    public void error(String message, Throwable e) throws StreamException {
        errorCount++;

        getLogStream().println(BULK_TAG + "ERROR:" + message);
        e.printStackTrace(getLogStream());

        if (errorCount > getMaxErrorCount()) {
            String str = "Tried more than " + getMaxErrorCount() + "... gaving up ...";
            info(str);
            shutdown = true;
        } else {
            long waitTime = getWaitTime();
            info("Error count " + errorCount + ". Trying again after " + waitTime);

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e1) {
                error("Failed to sleep", e1);
            }
        }
    }

    public long getWaitTime() {
        return (long) (Math.pow(2, errorCount) * 1000);
    }

    public void shutdown() {
        shutdown = true;
    }

    public boolean shouldContinue() {
        return !shutdown;
    }

    public int getMaxErrorCount() {
        return 10;
    }

    public int getMaxRecordsInBatch() {
        return 5000;
    }

    public long getMaxWaitTime() {
        return 1000 * 60 * 10; //10 min
    }
}
