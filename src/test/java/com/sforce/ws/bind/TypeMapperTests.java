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

package com.sforce.ws.bind;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.PullParserException;
import com.sforce.ws.parser.XmlInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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

        TypeInfo info = new TypeInfo("","a","","int", 0, 5, true);

        TypeMapper mapper = new TypeMapper();
        int[] returnArray = (int[]) mapper.readPartialArray(xmlInputStream, info, int[].class);
        for (int i = 0 ; i < returnArray.length; i++) {
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

        TypeInfo info = new TypeInfo("","a","","int", 0, 5, true);

        TypeMapper mapper = new TypeMapper();
        String[] returnArray = (String[]) mapper.readPartialArray(xmlInputStream, info, String[].class);
        assertEquals("Test", returnArray[0]);

    }
}
