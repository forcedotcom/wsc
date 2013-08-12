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
