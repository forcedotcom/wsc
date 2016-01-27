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
