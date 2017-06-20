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

package com.sforce.async;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.namespace.QName;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.MessageHandler;
import com.sforce.ws.MessageHandlerWithHeaders;
import com.sforce.ws.bind.CalendarCodec;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.parser.PullParserException;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;
import com.sforce.ws.transport.Transport;
import com.sforce.ws.util.FileUtil;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;


/**
 * BulkConnection
 * 
 * @author mcheenath
 * @since 160
 */

public class BulkConnection {

    public static final String NAMESPACE = "http://www.force.com/2009/06/asyncapi/dataload";
    public static final String SESSION_ID = "X-SFDC-Session";
    public static final String XML_CONTENT_TYPE = "application/xml";
    public static final String CSV_CONTENT_TYPE = "text/csv";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String ZIP_XML_CONTENT_TYPE = "zip/xml";
    public static final String ZIP_CSV_CONTENT_TYPE = "zip/csv";
    public static final String ZIP_JSON_CONTENT_TYPE = "zip/json";

    public static final QName JOB_QNAME = new QName(NAMESPACE, "jobInfo");
    public static final QName BATCH_QNAME = new QName(NAMESPACE, "batchInfo");
    public static final QName BATCH_LIST_QNAME = new QName(NAMESPACE, "batchInfoList");
    public static final QName ERROR_QNAME = new QName(NAMESPACE, "error");

    private ConnectorConfig config;
    private HashMap<String, String> headers = new HashMap<String, String>();
    public static final TypeMapper typeMapper = new TypeMapper();
    private static final JsonFactory factory = new JsonFactory(new ObjectMapper());


    public BulkConnection(ConnectorConfig config) throws AsyncApiException {
        if (config == null) {
            throw new AsyncApiException("config can not be null", AsyncExceptionCode.ClientInputError);
        }

        if (config.getRestEndpoint() == null) {
            throw new AsyncApiException("rest endpoint cannot be null",
                AsyncExceptionCode.ClientInputError);
        }

        this.config = config;

        if (config.getSessionId() == null) {
            throw new AsyncApiException("session ID not found",
                AsyncExceptionCode.ClientInputError);
        }
    }

    public JobInfo createJob(String object, String operation) throws AsyncApiException {
        JobInfo job = new JobInfo();
        job.setObject(object);
        job.setOperation(OperationEnum.valueOf(operation));
        return createJob(job);
    }

    public JobInfo createJob(JobInfo job) throws AsyncApiException {
        String endpoint = getRestEndpoint();
        endpoint = endpoint + "job/";
        return createOrUpdateJob(job, endpoint);
    }

    public JobInfo createJob(JobInfo job, ContentType contentType) throws AsyncApiException {
        String endpoint = getRestEndpoint();
        endpoint = endpoint + "job/";
        return createOrUpdateJob(job, endpoint, contentType);
    }

    private JobInfo createOrUpdateJob(JobInfo job, String endpoint) throws AsyncApiException {
        return createOrUpdateJob(job, endpoint, job.getContentType() == null ? ContentType.XML
            : job.getContentType());
    }

    private JobInfo createOrUpdateJob(JobInfo job, String endpoint, ContentType contentType) throws AsyncApiException {
        try {
            Transport transport = config.createTransport();
            OutputStream out;
            if (contentType == ContentType.ZIP_JSON || contentType == ContentType.JSON) {
                out = transport.connect(endpoint, getHeaders(JSON_CONTENT_TYPE));
                serializeToJson (out, job);
                out.close();
            } else {
                out = transport.connect(endpoint, getHeaders(XML_CONTENT_TYPE));
                XmlOutputStream xout = new AsyncXmlOutputStream(out, true);
                job.write(JOB_QNAME, xout, typeMapper);
                xout.close();
            }

            InputStream in = transport.getContent();

            if (transport.isSuccessful()) {
                if (contentType == ContentType.ZIP_JSON || contentType == ContentType.JSON) {
                    return deserializeJsonToObject(in, JobInfo.class);
                } else {
                    XmlInputStream xin = new XmlInputStream();
                    xin.setInput(in, "UTF-8");
                    JobInfo result = new JobInfo();
                    result.load(xin, typeMapper);
                    return result;
                }
            } else {
                parseAndThrowException(in, contentType);
            }
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to create job ", AsyncExceptionCode.ClientInputError, e);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to create job ", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to create job ", AsyncExceptionCode.ClientInputError, e);
        }
        return null;
    }

    static void parseAndThrowException(InputStream in, ContentType type) throws AsyncApiException {
        try {
            AsyncApiException exception;
            if (type == ContentType.XML || type == ContentType.ZIP_XML || type == ContentType.CSV || type == ContentType.ZIP_CSV) {
                exception = new AsyncApiException();

                XmlInputStream xin = new XmlInputStream();
                xin.setInput(in, "UTF-8");

                exception.load(xin, typeMapper);
            } else if (type == ContentType.JSON || type == ContentType.ZIP_JSON) {
                JsonParser parser = factory.createJsonParser(in);
                exception = parser.readValueAs(AsyncApiException.class);
            } else {
                throw new AsyncApiException("Server error returned in unknown format", AsyncExceptionCode.ClientInputError);
            }
            throw exception;

        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to parse exception ", AsyncExceptionCode.ClientInputError, e);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to parse exception", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to parse exception ", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    private String getRestEndpoint() {
        String endpoint = config.getRestEndpoint();
        endpoint = endpoint.endsWith("/") ? endpoint : endpoint + "/";
        return endpoint;
    }

    public BatchInfo createBatchFromStream(JobInfo jobInfo, InputStream input) throws AsyncApiException {
        return createBatchFromStreamImpl(jobInfo, input, false);
    }

    public BatchInfo createBatchFromZipStream(JobInfo jobInfo, InputStream zipInput) throws AsyncApiException {
        return createBatchFromStreamImpl(jobInfo, zipInput, true);
    }

    private BatchInfo createBatchFromStreamImpl(JobInfo jobInfo, InputStream input, boolean isZip)
            throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint();
            Transport transport = config.createTransport();
            endpoint = endpoint + "job/" + jobInfo.getId() + "/batch";
            String contentType = getContentTypeString(jobInfo.getContentType(), isZip);
            HashMap<String, String> httpHeaders = getHeaders(contentType);
            // TODO do we want to allow the zip content to be gzipped
            boolean allowZipToBeGzipped = false;
            OutputStream out = transport.connect(endpoint, httpHeaders, allowZipToBeGzipped || !isZip);

            FileUtil.copy(input, out);

            InputStream result = transport.getContent();
            if (!transport.isSuccessful()) parseAndThrowException(result, jobInfo.getContentType());
            //xml/json content type
            if (jobInfo.getContentType() == ContentType.JSON || jobInfo.getContentType() == ContentType.ZIP_JSON)
                return deserializeJsonToObject(result, BatchInfo.class);

            return BatchRequest.loadBatchInfo(result);

        } catch (IOException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public BatchInfo createBatchFromDir(JobInfo job, InputStream batchContent, File attachmentDir)
            throws AsyncApiException {
        final List<File> files = FileUtil.listFilesRecursive(attachmentDir, false);
        final Map<String, File> fileMap = new HashMap<String, File>(files.size());
        final String rootPath = attachmentDir.getAbsolutePath() + "/";
        for (File f : files) {
            String name = f.getAbsolutePath().replace(rootPath, "");
            fileMap.put(name, f);
        }
        return createBatchWithFileAttachments(job, batchContent, fileMap);
    }

    public BatchInfo createBatchWithFileAttachments(JobInfo jobInfo, InputStream batchContent, File rootDirectory,
            String... files) throws AsyncApiException {
        Map<String, File> fileMap = new HashMap<String, File>(files.length);
        for (String fileName : files) {
            File f = new File(rootDirectory, fileName);
            fileMap.put(fileName, f);
        }
        return createBatchWithFileAttachments(jobInfo, batchContent, fileMap);
    }

    public BatchInfo createBatchWithFileAttachments(JobInfo jobInfo, InputStream batchContent,
            Map<String, File> attachedFiles) throws AsyncApiException {

        Map<String, InputStream> inputStreamMap = new HashMap<String, InputStream>(attachedFiles.size());
        for (Map.Entry<String, File> entry : attachedFiles.entrySet()) {
            final File file = entry.getValue();
            try {
                inputStreamMap.put(entry.getKey(), new FileInputStream(file));
            } catch (IOException e) {
                throw new AsyncApiException("Failed to create batch. Could not read file : " + file,
                        AsyncExceptionCode.ClientInputError, e);
            }
        }
        return createBatchWithInputStreamAttachments(jobInfo, batchContent, inputStreamMap);
    }

    /**
     * @param jobInfo
     *            Parent job for new batch.
     * @param batchContent
     *            InputStream containing the xml or csv content of the batch, or null only if request.txt is contained
     *            in attachments map
     * @param attachments
     *            Map of attachments where the key is the filename to be used in the zip file and the value is the
     *            InputStream representing that file.
     * @return BatchInfo of uploaded batch.
     */
    public BatchInfo createBatchWithInputStreamAttachments(JobInfo jobInfo, InputStream batchContent,
            Map<String, InputStream> attachments) throws AsyncApiException {

        if (batchContent != null && attachments.get("request.txt") != null)
            throw new AsyncApiException("Request content cannot be included as both input stream and attachment",
                    AsyncExceptionCode.ClientInputError);
        try {
            String endpoint = getRestEndpoint();
            endpoint = endpoint + "job/" + jobInfo.getId() + "/batch";
            Transport transport = config.createTransport();
            ZipOutputStream zipOut = new ZipOutputStream(transport.connect(endpoint, getHeaders(getContentTypeString(
                    jobInfo.getContentType(), true)), false));

            try {
                if (batchContent != null) {
                    zipOut.putNextEntry(new ZipEntry("request.txt"));
                    FileUtil.copy(batchContent, zipOut, false);
                }
                for (Map.Entry<String, InputStream> entry : attachments.entrySet()) {
                    zipOut.putNextEntry(new ZipEntry(entry.getKey()));
                    FileUtil.copy(entry.getValue(), zipOut, false);
                }
            } finally {
                zipOut.close();
            }

            InputStream result = transport.getContent();
            if (jobInfo.getContentType() == ContentType.JSON || jobInfo.getContentType() == ContentType.ZIP_JSON) {
                return deserializeJsonToObject(result, BatchInfo.class);
            }
            return BatchRequest.loadBatchInfo(result);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        }
    }

    /*
     * Creates a compliant Async Api batch from a stream containing an arbitrary CSV source (eg. Outlook contacts). 
     * The stream does not have to be UTF-8, and it's contents are transformed into a compliant
     * batch using the previously created transformation specification (a mapping of columns to fields). 
     * The stream is still limited according to the same limit rules as apply to normal batches.
     */
    public BatchInfo createBatchFromForeignCsvStream(JobInfo jobInfo, InputStream input, String charSet) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint();
            Transport transport = config.createTransport();
            endpoint = endpoint + "job/" + jobInfo.getId() + "/batch";
            String contentType = getContentTypeString(ContentType.CSV, false);
            if (charSet != null) contentType = contentType + ";charset=" + charSet;
            HashMap<String, String> httpHeaders = getHeaders(contentType);
            final boolean allowZipToBeGzipped = false;
            OutputStream out = transport.connect(endpoint, httpHeaders, allowZipToBeGzipped);

            FileUtil.copy(input, out);

            InputStream result = transport.getContent();
            if (!transport.isSuccessful()) parseAndThrowException(result, jobInfo.getContentType());
            return BatchRequest.loadBatchInfo(result);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        }
    }

    /*
     * Creates a transformation specification for this job. Any subsequent batches that are submitted will
     * be assumed to be non-compliant batches and will be transformed into compliant batches using this specification.
     * An example spec for a Contact job might look like ...
     * <code>
     * Salesforce Field,Csv Header,Value,Hint
     * LastName,Surname,#N/A,
     * Birthdate,Birthday,,MM-dd-YYYY
     * </code> 
     */
    public void createTransformationSpecFromStream(JobInfo jobInfo, InputStream input) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint();
            Transport transport = config.createTransport();
            endpoint = endpoint + "job/" + jobInfo.getId() + "/spec";
            String contentType = getContentTypeString(ContentType.CSV, false);
            HashMap<String, String> httpHeaders = getHeaders(contentType);
            final boolean allowZipToBeGzipped = false;
            OutputStream out = transport.connect(endpoint, httpHeaders, allowZipToBeGzipped);

            FileUtil.copy(input, out);

            InputStream result = transport.getContent();
            if (!transport.isSuccessful()) parseAndThrowException(result, jobInfo.getContentType());
        } catch (IOException e) {
            throw new AsyncApiException("Failed to create transformation specification", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to create transformation specification", AsyncExceptionCode.ClientInputError, e);
        }
    }

    private String getContentTypeString(ContentType contentType, boolean isZip) throws AsyncApiException {
        ContentType ct = contentType == null ? ContentType.XML : contentType;
        if (isZip) {
            switch (ct) {
            case ZIP_CSV:
                return ZIP_CSV_CONTENT_TYPE;
            case ZIP_XML:
                return ZIP_XML_CONTENT_TYPE;
            case ZIP_JSON:
            	return ZIP_JSON_CONTENT_TYPE;
            default:
                // non zip content type
                throw new AsyncApiException("Invalid zip content type: " + contentType,
                        AsyncExceptionCode.ClientInputError);
            }
        } else {
            switch (ct) {
            case XML:
                return XML_CONTENT_TYPE;
            case CSV:
                return CSV_CONTENT_TYPE;
            case JSON:
            	return JSON_CONTENT_TYPE;
            default:
                // zip content type
                throw new AsyncApiException("Not expecting zip content type: " + contentType,
                        AsyncExceptionCode.ClientInputError);
            }
        }
    }

    private HashMap<String, String> getHeaders(String contentType) {
        HashMap<String, String> newMap = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue());
        }

        /* 05-Apr-2012, tsaloranta: This is incorrect, as the content type for request
         *   should indicate type of request payload and NOT the expected response
         *   payload type (which would be indicated with "Accept" header).
         *   See [https://gus.salesforce.com/a07B0000000Lp8A] for more details.
         *   So ideally we would change header to use here; alas, it is bit of risky
         *   change right, so leaving code as is
         *   with just a note saying "that ain't working -- that's the way to do it!"
         */
        newMap.put("Content-Type", contentType);
        newMap.put(SESSION_ID, config.getSessionId());
        return newMap;
    }

    public BatchRequest createBatch(JobInfo job) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint();
            Transport transport = config.createTransport();
            endpoint = endpoint + "job/" + job.getId() + "/batch";
            ContentType ct = job.getContentType();
            if (ct != null && ct != ContentType.XML && ct != ContentType.JSON) { throw new AsyncApiException(
                    "This method can only be used with xml or JSON content type", AsyncExceptionCode.ClientInputError); }

            String jobContentType = "";
            if (ct == null) {
                jobContentType = XML_CONTENT_TYPE;
            } else {
                switch (ct) {
                    case JSON:
                        jobContentType = JSON_CONTENT_TYPE;
                        break;
                    case XML:
                    default:
                        jobContentType = XML_CONTENT_TYPE;
                        break;
                }
            }
            OutputStream out = transport.connect(endpoint, getHeaders(jobContentType));
            return new BatchRequest(transport, out);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException x) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, x);
        }
    }

    public CsvBatchRequest createCsvBatch(JobInfo job) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint();
            Transport transport = config.createTransport();
            endpoint = endpoint + "job/" + job.getId() + "/batch";
            ContentType ct = job.getContentType();
            if (ct != null && ct != ContentType.CSV) { throw new AsyncApiException(
                    "This method can only be used with csv content type", AsyncExceptionCode.ClientInputError); }

            OutputStream out = transport.connect(endpoint, getHeaders(CSV_CONTENT_TYPE));
            return new CsvBatchRequest(transport, out);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to create batch", AsyncExceptionCode.ClientInputError, e);
		}
    }

    public TransformationSpecRequest createTransformationSpec(JobInfo job) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint();
            Transport transport = config.createTransport();
            endpoint = endpoint + "job/" + job.getId() + "/spec";
            ContentType ct = job.getContentType();
            if (ct != null && ct != ContentType.CSV) { throw new AsyncApiException(
                    "This method can only be used with csv content type", AsyncExceptionCode.ClientInputError); }
            final boolean allowZipToBeGzipped = false;
            OutputStream out = transport.connect(endpoint, getHeaders(CSV_CONTENT_TYPE), allowZipToBeGzipped);
            return new TransformationSpecRequest(transport, out);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to create transformation spec", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to create transformation spec", AsyncExceptionCode.ClientInputError, e);
		}
    }

    public BatchInfoList getBatchInfoList(String jobId) throws AsyncApiException {
        return getBatchInfoList(jobId, ContentType.XML);
    }
    public BatchInfoList getBatchInfoList(String jobId, ContentType contentType) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint() + "job/" + jobId + "/batch/";
            URL url = new URL(endpoint);
            InputStream stream = doHttpGet(url);

            if (contentType == ContentType.JSON || contentType == ContentType.ZIP_JSON) {
                return deserializeJsonToObject(stream, BatchInfoList.class);
            } else {
                XmlInputStream xin = new XmlInputStream();
                xin.setInput(stream, "UTF-8");
                BatchInfoList result = new BatchInfoList();
                result.load(xin, typeMapper);
                return result;
            }
        } catch (IOException e) {
            throw new AsyncApiException("Failed to get batch info list ", AsyncExceptionCode.ClientInputError, e);
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to get batch info list ", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to get batch info list ", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public BatchInfo getBatchInfo(String jobId, String batchId) throws AsyncApiException {
        return getBatchInfo(jobId, batchId, ContentType.XML);
    }

    public BatchInfo getBatchInfo(String jobId, String batchId, ContentType contentType) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint() + "job/" + jobId + "/batch/" + batchId;
            URL url = new URL(endpoint);
            InputStream stream = doHttpGet(url);

            if (contentType == ContentType.JSON || contentType == ContentType.ZIP_JSON) {
                return deserializeJsonToObject(stream, BatchInfo.class);
            } else {
                XmlInputStream xin = new XmlInputStream();
                xin.setInput(stream, "UTF-8");
                BatchInfo result = new BatchInfo();
                result.load(xin, typeMapper);
                return result;
            }
        } catch (IOException e) {
            throw new AsyncApiException("Failed to parse batch info ", AsyncExceptionCode.ClientInputError, e);
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to parse batch info ", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to parse batch info ", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public BatchResult getBatchResult(String jobId, String batchId) throws AsyncApiException {
        return getBatchResult(jobId, batchId, ContentType.XML);
    }
    public BatchResult getBatchResult(String jobId, String batchId, ContentType contentType) throws AsyncApiException {
        try {
            InputStream stream = doHttpGet(buildBatchResultURL(jobId, batchId));

            if (contentType == ContentType.JSON || contentType == ContentType.ZIP_JSON) {
                BatchResult batchResult = new BatchResult();
                Result[] results = deserializeJsonToObject(stream, Result[].class);
                batchResult.setResult(results);
                return batchResult;
            } else if (contentType == ContentType.CSV || contentType == ContentType.ZIP_CSV) {
                BatchResult batchResult = new BatchResult();
                Result[] results = deserializeCsvResults(stream);
                batchResult.setResult(results);
                return batchResult;
            } else {
                XmlInputStream xin = new XmlInputStream();
                xin.setInput(stream, "UTF-8");
                BatchResult result = new BatchResult();
                result.load(xin, typeMapper);
                return result;
            }
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to parse result ", AsyncExceptionCode.ClientInputError, e);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to get result ", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to get result ", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public InputStream getBatchResultStream(String jobId, String batchId) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint() + "job/" + jobId + "/batch/" + batchId + "/result";
            URL url = new URL(endpoint);
            return doHttpGet(url);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to get result ", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public URL buildBatchResultURL(String jobId, String batchId) throws AsyncApiException {
        try {
            return new URL(getRestEndpoint() + "job/" + jobId + "/batch/" + batchId + "/result");
        } catch (MalformedURLException e) {
            throw new AsyncApiException("Failed to construct URL for getting batch results: "+e.getMessage(),
                    AsyncExceptionCode.ClientInputError, e);
        }
    }
    
    public InputStream getBatchRequestInputStream(String jobId, String batchId) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint() + "job/" + jobId + "/batch/" + batchId + "/request";
            URL url = new URL(endpoint);
            return doHttpGet(url);
        } catch(IOException e) {
            throw new AsyncApiException("Failed to get request ", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public QueryResultList getQueryResultList(String jobId, String batchId) throws AsyncApiException {
        return getQueryResultList(jobId, batchId, ContentType.XML);
    }

    public QueryResultList getQueryResultList(String jobId, String batchId, ContentType contentType) throws AsyncApiException {
        InputStream stream = getBatchResultStream(jobId, batchId);

        try {
            if (contentType == ContentType.JSON || contentType == ContentType.ZIP_JSON) {
                String[] results = deserializeJsonToObject(stream, String[].class);
                QueryResultList list = new QueryResultList();
                list.setResult(results);
                return list;
            } else {
                XmlInputStream xin = new XmlInputStream();
                xin.setInput(stream, "UTF-8");
                QueryResultList result = new QueryResultList();
                result.load(xin, typeMapper);
                return result;
            }
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to parse query result list ", AsyncExceptionCode.ClientInputError, e);
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to parse query result list ", AsyncExceptionCode.ClientInputError, e);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to parse query result list ", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public InputStream getQueryResultStream(String jobId, String batchId, String resultId) throws AsyncApiException {
        try {
            return doHttpGet(buildQueryResultURL(jobId, batchId, resultId));
        } catch (IOException e) {
            throw new AsyncApiException("Failed to get result ", AsyncExceptionCode.ClientInputError, e);
        }
    }
    
    public URL buildQueryResultURL(String jobId, String batchId, String resultId) throws AsyncApiException {
        try {
            return new URL(getRestEndpoint() + "job/" + jobId + "/batch/" + batchId + "/result" + "/" + resultId);
        } catch (MalformedURLException e) {
            throw new AsyncApiException("Failed to construct URL for getting query result: "+e.getMessage(),
                    AsyncExceptionCode.ClientInputError, e);
        }
    }

    private InputStream doHttpGet(URL url) throws IOException, AsyncApiException {
        HttpURLConnection connection = config.createConnection(url, null);
        connection.setRequestProperty(SESSION_ID, config.getSessionId());

        boolean success = true;
        InputStream in;
        try {
            in = connection.getInputStream();
        } catch (IOException e) {
            success = false;
            in = connection.getErrorStream();
        }

        String encoding = connection.getHeaderField("Content-Encoding");
        if ("gzip".equals(encoding)) {
            in = new GZIPInputStream(in);
        }

        if (config.isTraceMessage() || config.hasMessageHandlers()) {
            byte[] bytes = FileUtil.toBytes(in);
            in = new ByteArrayInputStream(bytes);

            if (config.hasMessageHandlers()) {
                Iterator<MessageHandler> it = config.getMessagerHandlers();
                while (it.hasNext()) {
                    MessageHandler handler = it.next();
                    if (handler instanceof MessageHandlerWithHeaders) {
                        ((MessageHandlerWithHeaders)handler).handleRequest(url, new byte[0], null);
                        ((MessageHandlerWithHeaders)handler).handleResponse(url, bytes, connection.getHeaderFields());
                    } else {
                        handler.handleRequest(url, new byte[0]);
                        handler.handleResponse(url, bytes);
                    }
                }
            }

            if (config.isTraceMessage()) {
                config.getTraceStream().println(url.toExternalForm());

                Map<String, List<String>> headers = connection.getHeaderFields();
                for (Map.Entry<String, List<String>>entry : headers.entrySet()) {
                    StringBuffer sb = new StringBuffer();
                    List<String> values = entry.getValue();

                    if (values != null) {
                        for (String v : values) {
                            sb.append(v);
                        }
                    }

                    config.getTraceStream().println(entry.getKey() + ": " + sb.toString());
                }

                config.teeInputStream(bytes);
            }
        }

        if (!success) {
            ContentType type = null;
            String contentTypeHeader = connection.getContentType();
            if (contentTypeHeader != null) {
                if (contentTypeHeader.contains(XML_CONTENT_TYPE)) {
                    type = ContentType.XML;
                } else if (contentTypeHeader.contains(JSON_CONTENT_TYPE)) {
                    type = ContentType.JSON;
                }
            }
            parseAndThrowException(in, type);
        }

        return in;
    }

    public JobInfo getJobStatus(String jobId) throws AsyncApiException {
        return getJobStatus(jobId, ContentType.XML);
    }

    public JobInfo getJobStatus(String jobId, ContentType contentType) throws AsyncApiException {
        try {
            String endpoint = getRestEndpoint();
            endpoint += "job/" + jobId;
            URL url = new URL(endpoint);

            InputStream in = doHttpGet(url);

            if (contentType == ContentType.JSON  || contentType == ContentType.ZIP_JSON) {
                return deserializeJsonToObject(in, JobInfo.class);
            } else {
                JobInfo result = new JobInfo();
                XmlInputStream xin = new XmlInputStream();
                xin.setInput(in, "UTF-8");
                result.load(xin, typeMapper);
                return result;
            }
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to get job status ", AsyncExceptionCode.ClientInputError, e);
        } catch (IOException e) {
            throw new AsyncApiException("Failed to get job status ", AsyncExceptionCode.ClientInputError, e);
        } catch (ConnectionException e) {
            throw new AsyncApiException("Failed to get job status ", AsyncExceptionCode.ClientInputError, e);
        }
    }

    /**
     * Serialize to json
     * @param out
     * @param value
     * @throws IOException
     */
    static void serializeToJson(OutputStream out, Object value) throws IOException{
        JsonGenerator generator = factory.createJsonGenerator(out);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(CalendarCodec.getDateFormat());
        mapper.writeValue(generator, value);
    }

    /**
     * Deserialize JSON input
     * @param in
     * @param tmpClass
     * @param <T>
     * @return
     * @throws IOException
     * @throws ConnectionException
     */
    static <T> T deserializeJsonToObject (InputStream in, Class<T> tmpClass) throws IOException, ConnectionException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(in, tmpClass);
    }

    static Result[] deserializeCsvResults (InputStream in) throws IOException, ConnectionException {
        CSVReader reader = new CSVReader(in, "UTF-8");
        int idIx, successIx, createdIx, errorIx;
        idIx = successIx = createdIx = errorIx = -1;
        ArrayList<String> record = reader.nextRecord();
        for (int i = 0; i < record.size(); i++) {
            switch (record.get(i)) {
                case "Id":
                    idIx = i;
                    break;
                case "Success":
                    successIx = i;
                    break;
                case "Created":
                    createdIx = i;
                    break;
                case "Error":
                    errorIx = i;
                    break;
            }
        }
        ArrayList<Result> results = new ArrayList<Result>();
        record = reader.nextRecord();
        while (record != null) {
            Result result = new Result();
            result.setId(record.get(idIx));
            result.setSuccess(Boolean.parseBoolean(record.get(successIx)));
            result.setCreated(Boolean.parseBoolean(record.get(createdIx)));
            String error = record.get(errorIx);
            if (error != null && !error.isEmpty()) {
                String[] parts = error.split(":", 2);
                Error error_obj = new Error();
                if (parts.length == 2 && !parts[0].isEmpty()) {
                    error_obj.setStatusCode(StatusCode.valueOf(parts[0]));
                    error_obj.setMessage(parts[1]);
                } else {
                    error_obj.setMessage(error);
                }
                result.setErrors(new Error[] { error_obj });
            }
            results.add(result);
            record = reader.nextRecord();
        }

        return results.toArray(new Result[0]);
    }

    public JobInfo abortJob(String jobId) throws AsyncApiException {
        JobInfo job = new JobInfo();
        job.setId(jobId);
        job.setState(JobStateEnum.Aborted);
        return updateJob(job);
    }

    public JobInfo closeJob(String jobId) throws AsyncApiException {
        JobInfo job = new JobInfo();
        job.setId(jobId);
        job.setState(JobStateEnum.Closed);
        return updateJob(job);
    }

    public JobInfo updateJob(JobInfo job) throws AsyncApiException {
        return updateJob(job, ContentType.XML);
    }

    public JobInfo updateJob(JobInfo job, ContentType contentType) throws AsyncApiException {
        String endpoint = getRestEndpoint();
        endpoint += "job/" + job.getId();
        return createOrUpdateJob(job, endpoint, contentType);
    }

    public ConnectorConfig getConfig() {
        return config;
    }
}
