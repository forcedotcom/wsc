package com.sforce.bulk;

import com.sforce.async.*;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * This class represents
 * <p/>
 * User: mcheenath
 * Date: Dec 10, 2010
 */
public class UpdateStream {

    private StreamHandler handler;
    private JobInfo job;
    private BulkConnection bulkConnection;
    private String[] fieldNames;
    private StringWriter writer;
    private CsvWriter csvWriter;
    private int recordCount;

    public static UpdateStream create(StreamHandler handler) throws StreamException {
        return new UpdateStream(handler);
    }

    private UpdateStream(StreamHandler handler) throws StreamException {
        this.handler = handler;

        if (handler.getConfig().getAuthEndpoint() == null) {
            throw new StreamException("AuthEndpoint not set in config");
        }

        if (!handler.getConfig().getAuthEndpoint().contains("/services/Soap/u/")) {
            throw new StreamException("Not a valid partner AuthEndpoint " + handler.getConfig().getAuthEndpoint() +
                    " This URL should contain /services/Soap/u/");
        }

        LoginHelper loginHelper = new LoginHelper(handler);


        while(handler.shouldContinue()) {
            try {
                loginHelper.doLogin();
                break;
            } catch (Throwable e) {
                handler.error("Failed to login ", e);
            }
        }


    }

    public void start(String object, OperationEnum operation, ConcurrencyMode concurrencyMode, String[] fieldNames)
            throws StreamException {

        if (fieldNames == null || fieldNames.length == 0) {
            throw new StreamException("field names can not be null/empty");
        }

        this.fieldNames = fieldNames;

        while(handler.shouldContinue()) {
            try {
                bulkConnection = new BulkConnection(handler.getConfig());

                job = new JobInfo();
                job.setObject(object);

                job.setOperation(operation);
                job.setConcurrencyMode(concurrencyMode);
                job.setContentType(ContentType.CSV);

                handler.info("Creating bulk api job");
                job = bulkConnection.createJob(job);

                handler.info("Bulk api job created with ID : " + job.getId());
                break;
            } catch (Throwable e) {
                handler.error("Failed to create job ", e);
            }
        }
    }

    public UpdateResultStream close() throws StreamException {
        if (writer != null) {
            createBatch();
        }

        while(handler.shouldContinue()) {
            try {
                handler.info("Closing job");
                job = bulkConnection.closeJob(job.getId());
                handler.info("Job closed");
                break;
            } catch (Throwable e) {
                handler.error("Failed to close job ", e);
            }
        }

        return new UpdateResultStream(handler, bulkConnection, job);
    }

    public void write(String ... values) throws StreamException {
        if (job == null) {
            throw new StreamException("start() not called");
        }

        if (writer == null) {
            writer = new StringWriter();
            csvWriter = new CsvWriter(fieldNames, writer);
        }

        csvWriter.writeRecord(values);
        recordCount++;

        if (recordCount > handler.getMaxRecordsInBatch()) {
            csvWriter.endDocument();
            createBatch();
        }
    }

    private void createBatch() throws StreamException {
        while(handler.shouldContinue()) {
            try {
                handler.info("Creating Batch");

                BatchInfo batch = bulkConnection.createBatchFromStream(job,
                        new ByteArrayInputStream(writer.getBuffer().toString().getBytes()));

                handler.info("Batch created with ID: " + batch.getId());
                writer = null;
                csvWriter = null;
                recordCount = 0;

                break;
            } catch (Throwable e) {
                handler.error("Failed to close job ", e);
            }
        }
    }
}
