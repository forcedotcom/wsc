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
