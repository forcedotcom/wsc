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

import com.sforce.async.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This class represents
 * <p/>
 * User: mcheenath
 * Date: Dec 15, 2010
 */
public class UpdateResultStream {
    private JobInfo job;
    private BulkConnection bulkConnection;
    private StreamHandler handler;
    private BatchInfo[] batchList;
    private int batchIndex = -1;
    private CSVReader resultReader;

    public UpdateResultStream(StreamHandler handler, BulkConnection bulkConnection, JobInfo job)
            throws StreamException {

        this.job = job;
        this.bulkConnection = bulkConnection;
        this.handler = handler;

        loadBatchInfoList(handler, bulkConnection, job);
    }

    private void loadBatchInfoList(StreamHandler handler, BulkConnection bulkConnection, JobInfo job)
            throws StreamException {

        while(handler.shouldContinue()) {
            try {
                BatchInfoList bList = bulkConnection.getBatchInfoList(job.getId());
                batchList = bList.getBatchInfo();
                break;
            } catch (Throwable e) {
                handler.error("Failed to get batch list", e);
            }
        }
    }

    public UpdateResult next() throws StreamException {
        try {
            ArrayList<String> record;

            if (resultReader == null || (record = resultReader.nextRecord()) == null) {
                batchIndex++;

                if (batchIndex >= batchList.length) {
                    //we reached the end of the list
                    return null;
                }

                loadNextBatch();
                record = resultReader.nextRecord();
            }

            if (record ==  null) {
                return null;
            }

            return new UpdateResult(valueAt(record,0), booleanAt(record, 1), booleanAt(record, 2), valueAt(record, 3));
        } catch (IOException e) {
            throw new StreamException("Failed to read next record", e);
        }
    }

    private String valueAt(ArrayList<String> record, int index) {
        if (index < record.size()) {
            return record.get(index);
        } else {
            return null;
        }
    }

    private boolean booleanAt(ArrayList<String> record, int index) {
        String val = valueAt(record, index);
        return Boolean.parseBoolean(val);
    }

    private void waitForNextBatch() throws StreamException {
        int count = 0;

        while(handler.shouldContinue()) {
            try {
                BatchInfo bInfo = bulkConnection.getBatchInfo(job.getId(), batchList[batchIndex].getId());
                handler.info("Batch " + bInfo.getId() + " -- state -- " + bInfo.getState() + " -- try: " + count);

                if (bInfo.getState() == BatchStateEnum.Completed || bInfo.getState() == BatchStateEnum.Failed) {
                    break;
                }

                long waitTime = (long) (Math.pow(count, 2) * 1000);
                waitTime = waitTime > handler.getMaxWaitTime() ? handler.getMaxWaitTime() : waitTime;

                Thread.sleep(waitTime);
                count++;
            } catch(Throwable e) {
                handler.error("Failed to read result for batch " + batchList[batchIndex].getId(), e);
            }
        }
    }




    private void loadNextBatch() throws StreamException {
        waitForNextBatch();

        while(handler.shouldContinue()) {
            try {
                InputStream resultStream =
                        bulkConnection.getBatchResultStream(job.getId(), batchList[batchIndex].getId());

                resultReader = new CSVReader(resultStream);
                resultReader.nextRecord(); //comsume header
                break;
            } catch(Throwable e) {
                handler.error("Failed to read result for batch " + batchList[batchIndex].getId(), e);
            }
        }
    }
}
