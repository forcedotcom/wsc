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

package com.sforce.ws.bind;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.PullParserException;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.types.OffsetDate;
import com.sforce.ws.wsdl.Constants;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.*;

import static org.junit.Assert.assertEquals;

/**
 * This test validates type mapper functionality
 *
 * @author rcornel
 */
public class TypeMapperTests {
    // test to validate we can read primitive int array types.
    @Test
    public void testPrimitiveArrayRead() throws PullParserException, ConnectionException, IOException {
        String arrayStr = "<b><a>0</a><a>1</a><a>2</a></b>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arrayStr.getBytes());

        XmlInputStream xmlInputStream = new XmlInputStream();
        xmlInputStream.setInput(inputStream, "UTF-8");
        xmlInputStream.nextTag();

        TypeInfo info = new TypeInfo("", "a", "", "int", 0, 5, true);

        TypeMapper mapper = new TypeMapper(null, null, false);
        int[] returnArray = (int[]) mapper.readPartialArray(xmlInputStream, info, int[].class);
        for (int i = 0; i < returnArray.length; i++) {
            assertEquals(i, returnArray[i]);
        }
    }


    // test to validate we can read a String Array type
    @Test
    public void testStringArrayRead() throws PullParserException, ConnectionException, IOException {
        String arrayStr = "<b><a>Test</a></b>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arrayStr.getBytes());

        XmlInputStream xmlInputStream = new XmlInputStream();
        xmlInputStream.setInput(inputStream, "UTF-8");
        xmlInputStream.nextTag();

        TypeInfo info = new TypeInfo("", "a", "", "int", 0, 5, true);

        TypeMapper mapper = new TypeMapper(null, null, false);
        String[] returnArray = (String[]) mapper.readPartialArray(xmlInputStream, info, String[].class);
        assertEquals("Test", returnArray[0]);

    }

    @Test
    public void testDateRead() throws PullParserException, IOException, ConnectionException {
        String withoutTimeZone = "<xml xmlns=\"http://schemas.xmlsoap.org/wsdl\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><xsd:date>2019-12-02</xsd:date></xml>";
        String withTimeZone = "<xml xmlns=\"http://schemas.xmlsoap.org/wsdl\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><xsd:date>2019-12-02+05:00</xsd:date></xml>";

        XmlInputStream xmlWithoutTimeZone = new XmlInputStream();
        xmlWithoutTimeZone.setInput(new ByteArrayInputStream(withoutTimeZone.getBytes()), "UTF-8");
        xmlWithoutTimeZone.nextTag();

        XmlInputStream xmlWithTimeZone = new XmlInputStream();
        xmlWithTimeZone.setInput(new ByteArrayInputStream(withTimeZone.getBytes()), "UTF-8");
        xmlWithTimeZone.nextTag();

        TypeInfo info = new TypeInfo("", "a", Constants.SCHEMA_NS, "date", 0, 1, true);
        TypeMapper mapper = new TypeMapper(null, null, true);

        String javaClassName = mapper.getJavaClassName(new QName(Constants.SCHEMA_NS, "date"), null, false);
        assertEquals(OffsetDate.class.getName(), javaClassName);

        OffsetDate result1 = (OffsetDate) mapper.readObject(xmlWithoutTimeZone, info, OffsetDate.class);
        assertEquals(ZoneOffset.UTC, result1.getOffset());
        assertEquals(LocalDate.of(2019, 12, 2), result1.getDate());

        OffsetDate result2 = (OffsetDate) mapper.readObject(xmlWithTimeZone, info, OffsetDate.class);
        assertEquals(ZoneOffset.of("+05:00"), result2.getOffset());
        assertEquals(LocalDate.of(2019, 12, 2), result2.getDate());
    }

    @Test
    public void testDateTimeRead() throws PullParserException, IOException, ConnectionException {
        String withoutTimeZone = "<xml xmlns=\"http://schemas.xmlsoap.org/wsdl\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><xsd:datetime>2019-12-02T11:22:33.444</xsd:datetime></xml>";
        String withTimeZone = "<xml xmlns=\"http://schemas.xmlsoap.org/wsdl\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><xsd:datetime>2019-12-02T11:22:33.444-05:00</xsd:datetime></xml>";

        XmlInputStream xmlWithoutTimeZone = new XmlInputStream();
        xmlWithoutTimeZone.setInput(new ByteArrayInputStream(withoutTimeZone.getBytes()), "UTF-8");
        xmlWithoutTimeZone.nextTag();

        XmlInputStream xmlWithTimeZone = new XmlInputStream();
        xmlWithTimeZone.setInput(new ByteArrayInputStream(withTimeZone.getBytes()), "UTF-8");
        xmlWithTimeZone.nextTag();

        TypeInfo info = new TypeInfo("", "a", Constants.SCHEMA_NS, "dateTime", 0, 1, true);
        TypeMapper mapper = new TypeMapper(null, null, true);

        String javaClassName = mapper.getJavaClassName(new QName(Constants.SCHEMA_NS, "dateTime"), null, false);
        assertEquals(OffsetDateTime.class.getName(), javaClassName);

        OffsetDateTime result1 = (OffsetDateTime) mapper.readObject(xmlWithoutTimeZone, info, OffsetDateTime.class);
        assertEquals(ZoneOffset.UTC, result1.getOffset());
        assertEquals(LocalDateTime.of(2019, 12, 2, 11, 22, 33, 444000000), result1.toLocalDateTime());

        OffsetDateTime result2 = (OffsetDateTime) mapper.readObject(xmlWithTimeZone, info, OffsetDateTime.class);
        assertEquals(ZoneOffset.of("-05:00"), result2.getOffset());
        assertEquals(LocalDateTime.of(2019, 12, 2, 11, 22, 33, 444000000), result2.toLocalDateTime());
    }

    @Test
    public void testTimeRead() throws PullParserException, IOException, ConnectionException {
        String withoutTimeZone = "<xml xmlns=\"http://schemas.xmlsoap.org/wsdl\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><xsd:time>11:22:33.444</xsd:time></xml>";
        String withTimeZone = "<xml xmlns=\"http://schemas.xmlsoap.org/wsdl\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><xsd:time>11:22:33.444+05:00</xsd:time></xml>";

        XmlInputStream xmlWithoutTimeZone = new XmlInputStream();
        xmlWithoutTimeZone.setInput(new ByteArrayInputStream(withoutTimeZone.getBytes()), "UTF-8");
        xmlWithoutTimeZone.nextTag();

        XmlInputStream xmlWithTimeZone = new XmlInputStream();
        xmlWithTimeZone.setInput(new ByteArrayInputStream(withTimeZone.getBytes()), "UTF-8");
        xmlWithTimeZone.nextTag();

        TypeInfo info = new TypeInfo("", "a", Constants.SCHEMA_NS, "time", 0, 1, true);
        TypeMapper mapper = new TypeMapper(null, null, true);

        String javaClassName = mapper.getJavaClassName(new QName(Constants.SCHEMA_NS, "time"), null, false);
        assertEquals(OffsetTime.class.getName(), javaClassName);

        OffsetTime result1 = (OffsetTime) mapper.readObject(xmlWithoutTimeZone, info, OffsetTime.class);
        assertEquals(ZoneOffset.UTC, result1.getOffset());
        assertEquals(LocalTime.of(11, 22, 33, 444000000), result1.toLocalTime());

        OffsetTime result2 = (OffsetTime) mapper.readObject(xmlWithTimeZone, info, OffsetTime.class);
        assertEquals(ZoneOffset.of("+05:00"), result2.getOffset());
        assertEquals(LocalTime.of(11, 22, 33, 444000000), result2.toLocalTime());
    }
}
