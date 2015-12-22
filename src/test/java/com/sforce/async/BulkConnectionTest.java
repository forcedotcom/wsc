/*
 * Copyright, 2015, salesforce.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.async;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * This is the beginning of a unit test class for {@link BulkConnection}.
 *
 * @author btajuddin-sfdc
 */
public class BulkConnectionTest {

    @Test
    public void testParseAndThrowXml() {
        InputStream inputStream = new ByteArrayInputStream(("<error xmlns=\"http://www.force.com/2009/06/asyncapi/dataload\">\n" +
                "<exceptionCode>InvalidBatch</exceptionCode>\n" +
                "<exceptionMessage>Records not processed</exceptionMessage>\n" +
                "</error>").getBytes());
        try {
            BulkConnection.parseAndThrowException(inputStream, ContentType.XML);
        } catch (AsyncApiException aae) {
            Assert.assertEquals("Records not processed", aae.getExceptionMessage());
            Assert.assertEquals(AsyncExceptionCode.InvalidBatch, aae.getExceptionCode());
        }
    }

    @Test
    public void testParseAndThrowCsv() {
        InputStream inputStream = new ByteArrayInputStream(("<error xmlns=\"http://www.force.com/2009/06/asyncapi/dataload\">\n" +
                "<exceptionCode>InvalidBatch</exceptionCode>\n" +
                "<exceptionMessage>Records not processed</exceptionMessage>\n" +
                "</error>").getBytes());
        try {
            BulkConnection.parseAndThrowException(inputStream, ContentType.CSV);
        } catch (AsyncApiException aae) {
            Assert.assertEquals("Records not processed", aae.getExceptionMessage());
            Assert.assertEquals(AsyncExceptionCode.InvalidBatch, aae.getExceptionCode());
        }
    }

    @Test
    public void testParseAndThrowJson() {
        InputStream inputStream = new ByteArrayInputStream(("{" +
                "\"exceptionCode\":\"InvalidBatch\"," +
                "\"exceptionMessage\":\"Records not processed\"" +
                "}").getBytes());
        try {
            BulkConnection.parseAndThrowException(inputStream, ContentType.JSON);
        } catch (AsyncApiException aae) {
            Assert.assertEquals("Records not processed", aae.getExceptionMessage());
            Assert.assertEquals(AsyncExceptionCode.InvalidBatch, aae.getExceptionCode());
        }
    }

    @Test
    public void testParseAndThrowZipXml() {
        InputStream inputStream = new ByteArrayInputStream(("<error xmlns=\"http://www.force.com/2009/06/asyncapi/dataload\">\n" +
                "<exceptionCode>InvalidBatch</exceptionCode>\n" +
                "<exceptionMessage>Records not processed</exceptionMessage>\n" +
                "</error>").getBytes());
        try {
            BulkConnection.parseAndThrowException(inputStream, ContentType.ZIP_XML);
        } catch (AsyncApiException aae) {
            Assert.assertEquals("Records not processed", aae.getExceptionMessage());
            Assert.assertEquals(AsyncExceptionCode.InvalidBatch, aae.getExceptionCode());
        }
    }

    @Test
    public void testParseAndThrowZipCsv() {
        InputStream inputStream = new ByteArrayInputStream(("<error xmlns=\"http://www.force.com/2009/06/asyncapi/dataload\">\n" +
                "<exceptionCode>InvalidBatch</exceptionCode>\n" +
                "<exceptionMessage>Records not processed</exceptionMessage>\n" +
                "</error>").getBytes());
        try {
            BulkConnection.parseAndThrowException(inputStream, ContentType.ZIP_CSV);
        } catch (AsyncApiException aae) {
            Assert.assertEquals("Records not processed", aae.getExceptionMessage());
            Assert.assertEquals(AsyncExceptionCode.InvalidBatch, aae.getExceptionCode());
        }
    }

    @Test
    public void testParseAndThrowZipJson() {
        InputStream inputStream = new ByteArrayInputStream(("{" +
                "\"exceptionCode\":\"InvalidBatch\"," +
                "\"exceptionMessage\":\"Records not processed\"" +
                "}").getBytes());
        try {
            BulkConnection.parseAndThrowException(inputStream, ContentType.ZIP_JSON);
        } catch (AsyncApiException aae) {
            Assert.assertEquals("Records not processed", aae.getExceptionMessage());
            Assert.assertEquals(AsyncExceptionCode.InvalidBatch, aae.getExceptionCode());
        }
    }

    @Test
    public void testParseAndThrowNull() {
        try {
            BulkConnection.parseAndThrowException(new ByteArrayInputStream(new byte[0]), null);
        } catch (AsyncApiException aae) {
            Assert.assertEquals(AsyncExceptionCode.ClientInputError, aae.getExceptionCode());
        }
    }
}
