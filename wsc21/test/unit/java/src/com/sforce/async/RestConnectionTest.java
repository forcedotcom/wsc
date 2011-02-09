/*
 * Copyright, 1999-2008, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.async;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * RestConnectionTest
 *
 * @author mcheenath
 * @since 160
 */
public class RestConnectionTest extends TestCase {


    public void testCreate() {
        try {
            queryAccounts();

            //createCsvAccounts();
            //createAccounts();

            //ignore all errors from unit test, for now.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String login(String endpoint, String username, String passwd) throws IOException, AsyncApiException {
        URL restUrl = new URL(endpoint);

        String loginEndpoint = restUrl.getProtocol() + "://" + restUrl.getHost();

        if (restUrl.getPort() != -1) {
            loginEndpoint += ":" + restUrl.getPort();
        }

        loginEndpoint += "/login.jsp?un=" + username + "&pw=" + passwd;

        HttpURLConnection connection = (HttpURLConnection) new URL(loginEndpoint).openConnection();

        Map<String, List<String>> map = connection.getHeaderFields();
        List<String> cookies = map.get("Set-Cookie");

        if (cookies == null) {
            throw new AsyncApiException("Failed to login to endpoint " + loginEndpoint, AsyncExceptionCode.ClientInputError);
        }

        for (String cookie : cookies) {
            if (cookie.startsWith("sid=")) {
                int index = cookie.indexOf(';');
                cookie = cookie.substring(0, index);
                cookie = cookie.substring("sid=".length());
                return cookie;
            }
        }

        throw new IOException("Failed to login");
    }

    public void queryUsingSoap() {

        ConnectorConfig config = new ConnectorConfig();
        config.setAuthEndpoint("http://localhost:7001/services/Soap/u/19.0/");
        config.setUsername("ee@cheenath.com");
        config.setPassword("123456");

    }

    public void queryAccounts() throws AsyncApiException, IOException, InterruptedException {
        String endpoint = "http://localhost:7001/services/async/21.0/";
        String sessionId = login(endpoint, "ee@cheenath.com", "123456");
        System.out.println(sessionId);

        ConnectorConfig config = new ConnectorConfig();
        config.setTraceMessage(true);
        config.setCompression(false);
        config.setRestEndpoint(endpoint);
        config.setSessionId(sessionId);

        RestConnection rc = new RestConnection(config);

        JobInfo job = new JobInfo();
        job.setObject("Account");

        job.setOperation(OperationEnum.query);
        job.setConcurrencyMode(ConcurrencyMode.Parallel);
        job.setContentType(ContentType.CSV);

        job = rc.createJob(job);
        assert job.getId() != null;

        job = rc.getJobStatus(job.getId());

        String query = "select name,id,phone,createddate from account";

        //query = "SELECT min(AnnualRevenue) FROM Account";

        /*
        query = "SELECT CALENDAR_YEAR(CreatedDate), SUM(Amount)\n" +
                "FROM Opportunity\n" +
                "GROUP BY CALENDAR_YEAR(CreatedDate)";
                */

        long start = System.currentTimeMillis();

        BatchInfo info = null;
        ByteArrayInputStream bout = new ByteArrayInputStream(query.getBytes());
        info = rc.createBatchFromStream(job, bout);

        if (info == null) {
            return;
        }

        String[] queryResults = null;

        for(int i=0; i<10000; i++) {
             Thread.sleep(i==0 ? 30 * 1000 : 30 * 1000); //30 sec
             info = rc.getBatchInfo(job.getId(), info.getId());

             if (info.getState() == BatchStateEnum.Completed) {
                 QueryResultList list = rc.getQueryResultList(job.getId(), info.getId());
                 queryResults = list.getResult();
                 break;
             } else if (info.getState() == BatchStateEnum.Failed) {
                 System.out.println("-------------- failed ----------" + info);
                 break;
             } else {
                 //System.out.println("-------------- waiting ----------" + info);
             }
        }

        if (queryResults != null) {
            for (String resultId : queryResults) {
                rc.getQueryResultStream(job.getId(), info.getId(), resultId);
            }
        }

        System.out.println("Time taken " + (System.currentTimeMillis() - start));
    }


    public void createCsvAccounts() throws AsyncApiException, IOException, InterruptedException {



        String endpoint = "http://localhost:7001/services/async/21.0/";
        String sessionId = login(endpoint, "ee@cheenath.com", "123456");

        //sessionId = "00Dx0000000902e!AQ4AQC9tVMA7rE1dUhy9YEasOL24F6_f.qWWfOR9Y0.9cfIe2RXwR2pZlLNxV8x3HdACxrEUYW7lO1CYNGAeRVhoZsWPD.Cb";

        System.out.println(sessionId);

        //String endpoint = "https://cs2-blitz04.soma.salesforce.com/services/async/17.0/";
        //String sessionId = login(endpoint, "mcheenath@salesforce.com", "123456");



        ConnectorConfig config = new ConnectorConfig();
        config.setTraceMessage(true);
        config.setCompression(false);
        config.setRestEndpoint(endpoint);
        config.setSessionId(sessionId);

        RestConnection rc = new RestConnection(config);

        JobInfo job = new JobInfo();
        job.setObject("Account");

        job.setOperation(OperationEnum.query);
        job.setConcurrencyMode(ConcurrencyMode.Parallel);
        job.setContentType(ContentType.CSV);
        //job.setAssignmentRuleId("001x000xxx3LAaC");

        job = rc.createJob(job);
        assert job.getId() != null;

        //rc.getConfig().setRestEndpoint(endpoint.replace("17.0", "16.0"));

        job = rc.getJobStatus(job.getId());

        String csvRequest = "Name,AnnualRevenue,NumberOfEmployees,billingcity,billingstate,Description\n";

        for (int i=0; i<5000; i++) {
            csvRequest += "name" + i + "--" + System.currentTimeMillis() +
                    ",123.4,#N/A,ca,,\"this is fun" + new Date() + "\"\n";
        }

        csvRequest = "select name,id,phone,createddate from account limit 100";

        //csvRequest = "select name,parent.parent.parent.parent.name from account limit 100";

        //csvRequest = "ParentId,Bodyfoo\n001x000xxx3LAaC,from csv";


        //csvRequest += "name2,123.4,#N/A,,,#N/A,";


        //csvRequest = "Subject,Contact.Email\n";
        //csvRequest += "my subject,mcheenath@salesforce.com";

        long start = System.currentTimeMillis();

        BatchInfo info = null;

        for (int i=0; i<1; i++) {
            ByteArrayInputStream bout = new ByteArrayInputStream(csvRequest.getBytes());
            info = rc.createBatchFromStream(job, bout);
        }

        if (info == null) {
            return;
        }

        String[] queryResults = null;

        for(int i=0; i<10000; i++) {
             Thread.sleep(i==0 ? 30 * 1000 : 30 * 1000); //one min
             info = rc.getBatchInfo(job.getId(), info.getId());
             if (info.getState() == BatchStateEnum.Completed) {
                 QueryResultList list = rc.getQueryResultList(job.getId(), info.getId());
                 queryResults = list.getResult();
                 break;
             } else if (info.getState() == BatchStateEnum.Failed) {
                 System.out.println("-------------- failed ----------" + info);
                 break;
             } else {
                 //System.out.println("-------------- waiting ----------" + info);
             }
        }

        for (String resultId : queryResults) {
            rc.getQueryResultStream(job.getId(), info.getId(), resultId);
        }

        System.out.println("Time taken " + (System.currentTimeMillis() - start));
    }

    public void createAccounts() throws ConnectionException, IOException, InterruptedException, AsyncApiException {
        String endpoint = "http://localhost:7001/services/async/20.0/";
        String sessionId = login(endpoint, "ee@cheenath.com", "123456");

        ConnectorConfig config = new ConnectorConfig();
        config.setTraceMessage(true);
        config.setCompression(true);
        config.setSessionId(sessionId);
        config.setRestEndpoint(endpoint);

        RestConnection rc = new RestConnection(config);

        JobInfo job = new JobInfo();
        //job.setObject("mycustom__c");
        job.setObject("account");
        //job.setObject("AccountTeamMember");
        job.setOperation(OperationEnum.insert);
        job.setConcurrencyMode(ConcurrencyMode.Parallel);
        job.setContentType(ContentType.XML);
        //job.setAssignmentRuleId("001x000xxx3JKFg");
        JobInfo jobResult = rc.createJob(job);
        assert jobResult != null;
        assert jobResult.getId() != null;

        rc.getJobStatus(jobResult.getId());


        String batchId1 = createBatch("Batch 1: ", rc, jobResult);

        //rc.getJobStatus(batchId1);
        rc.getBatchInfo(jobResult.getId(), batchId1);

        //rc.abortJob(jobResult.getId());

        for(int i=0; i<10; i++) {
            try {
                Thread.sleep(1000 * 10);
                BatchInfo info = rc.getBatchInfo(jobResult.getId(), batchId1);
                if (info.getState() == BatchStateEnum.Completed || info.getState() == BatchStateEnum.Failed) {
                    BatchResult result = rc.getBatchResult(jobResult.getId(), batchId1);
                    System.out.println(result.getResult().length);
                    int count = 0;
                    for (Result r : result.getResult()) {
                        System.out.println(r.getSuccess() + " " + count);
                        count++;
                    }
                    break;
                } else if (info.getState() == BatchStateEnum.Failed) {
                    System.out.println("-------------- failed ----------" + info);
                    break;
                } else {
                    System.out.println("-------------- waiting ----------" + info);
                }
            } catch(AsyncApiException e) {
                e.printStackTrace();
            }
        }

        BatchInfoList list = rc.getBatchInfoList(jobResult.getId());
        System.out.println(list.getBatchInfo()[0].getNumberRecordsProcessed());
    }

    private String createBatch(String name, RestConnection rc, JobInfo jobResult) throws AsyncApiException {
        BatchRequest batchRequest = rc.createBatch(jobResult);

        for (int i=0; i<11; i++) {
            SObject sob = new SObject();

            //sob.setField("AccountId", "001x000xxx3JKFg");
            //sob.setField("UserId", "005x0000000wOvb");
            //sob.setField("TeamMemberRole", "Sales Manager");


            //sob.setField("customfield__c", "aaa" + name + i);
            //sob.setField("id", "a00x00000003pPSAAY");
            sob.setField("Name", name + i);
            //sob.setField("ownerid", "005x0000000zzyLAAQ");
            //sob.setField("AnnualRevenue", "123.4");
            //sob.setField("NumberOfEmployees", "12");
            //sob.setField("Description", "hi there this is some description");
            //sob.setField("booleanValue__c", "true");
            //sob.setField("currencyValue__c", "5678.23");
            //sob.setField("dateValue__c", "2009-10-10");
            //sob.setField("type", "Account");
            //sob.setField("foo", "bar");
            //if (i%7 ==0) {
                //sob.setField("OwnerId", "751x000000006AAI");
            //}
            batchRequest.addSObject(sob);
        }

        BatchInfo bresult = batchRequest.completeRequest();
        assert bresult != null;
        assert bresult.getId() != null;
        return bresult.getId();
    }
}
