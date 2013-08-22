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

import junit.framework.TestCase;

import java.io.StringReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * CSVReaderTest --
 *
 * @author mcheenath
 * @since 162
 */
public class CSVReaderTest extends TestCase {


    public void testHeader() throws IOException {
        String[][] result = {{"a","b","c","d"}};

        doTest("a,b,c,d", result);
        doTest("a,\"b\",c,d", result);
        doTest("\"a\",\"b\",\"c\",\"d\"", result);
        doTest("a,b,c,d\n", result);

        //tab is not a separator
        String[][] result2 = {{"a\tb\tc\td"}};
        doTest("a\tb\tc\td\n", result2);
    }

    public void testBasic() throws IOException {
        String[][] result = {{"a","b","c","d"}, {"sdf"," sdf","sadfddf","sdsdfdf"}};

        doTest("a,b,c,d\nsdf, sdf,sadfddf,sdsdfdf", result);
        doTest("a,b,c,d\nsdf, sdf,sadfddf,sdsdfdf\n", result);

        //test blank line
        doTest("a,b,c,d\n\nsdf, sdf,sadfddf,sdsdfdf\n", result);
    }

    public void testQuote() throws IOException {
        String[][] result = {{"a","b","c","d!@#$%^&*()_+~`1234567890/\\}]{{'?/"}, {"sdf"," sdf","sad\nfddf","sdsd,fdf"}};

        doTest("a,b,c,\"d!@#$%^&*()_+~`1234567890/\\}]{{'?/\"\nsdf,\" sdf\",\"sad\nfddf\",\"sdsd,fdf\"", result);


        // " sdf" is same as <space>"sdf"
        //doTest("a,b,c,d\nsdf,\"sdf\",\"sad\nfddf\",sdsdfdf", result);
    }

    public void testQuoteInsideField() throws IOException {
        doTest("\"one\"\"two\"", new String[][]{{"one\"two"}});
        doTest("\"\"\"one\"\"two\"", new String[][]{{"\"one\"two"}});
    }

    public void testNotClosedQuote() throws IOException {
        String[][] result = {{"a","b","c","d"}, {"sdf"," sdf","sadfddf","sdsdfdf"}};
        String message = "EOF reached before closing an opened quote";
        String csv =  "a,b,c,d\nsdf, sdf,sadfddf,\"sdsdfdf";

        doFailingTest(result, message, csv);

        doFailingTest(result, "Found unescaped quote. A value with quote should be within a quote",
                "a,b,c,d\nsdf, \"sfd, sadfddf, \"sdsdfdf\"");

        doFailingTest(result, "Not expecting more text after end quote",
                "a,b,c,d\nsdf,\"sfd\" , sadfddf, \"sdsdfdf\"");
    }

    private void doFailingTest(String[][] result, String message, String csv) {
        try {
            doTest(csv, result);
            fail("should not get to this point!");
        } catch(IOException e) {
            assertEquals(message, e.getMessage());
        }
    }

    private void doTest(String csv, String[][] result) throws IOException {
        CSVReader reader = new CSVReader(new StringReader(csv));

        for (String[] line : result) {
            ArrayList<String> record = reader.nextRecord();

            if (line == null) {
                assertNull(record);
                continue;
            }

            assertNotNull(record);
            assertEquals("Number of columns not matching " + Arrays.toString(line) + " record: " + record,
                    line.length,  record.size());

            for (int i=0; i<line.length; i++) {
                assertEquals("Value not same at: " + i, line[i], record.get(i));
            }
        }

        assertNull("More records than expected", reader.nextRecord());
    }

    public void testRowLimits() throws Exception {
        // 10,001 rows should be fine
        doTest(buildCsvWithXRows(10001), buildResultForCsvWithXRows(10001));
        // 10,002 rows is too many!
        doFailingTest(buildResultForCsvWithXRows(10002),
                      "Exceeded number of records : 10002. Number of records should be less than or equal to 10001",
                      buildCsvWithXRows(10002));
        // 10,001 real rows plus a bunch of empty lines should be fine
        doTest(buildCsvWithXRows(10001) + "\n\n\n\n\n\n", buildResultForCsvWithXRows(10001));
    }

    private String buildCsvWithXRows(int numRows) {
        return buildCsvWithXRowsAndYCharsPerRow(numRows, 1);
    }

    private String buildCsvWithXRowsAndYCharsPerRow(int numRows, int numCharsPerRow) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<numRows; i++) {
            for(int j=0; j<numCharsPerRow; j++) {
                sb.append("a");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String[][] buildResultForCsvWithXRows(int numRows) {
        return buildResultForCsvWithXRows(numRows, 1);
    }

    private String[][] buildResultForCsvWithXRows(int numRows, int numCharsPerRow) {
        String[][] result = new String[numRows][1];
        for (int i = 0; i < result.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < numCharsPerRow; j++) {
                sb.append("a");
            }
            result[i][0] = sb.toString();
        }
        return result;
    }

    public void testRowSizeLimits() throws Exception {
        // 400,000 characters ok
        String csv = buildCsvWithXCharacters(400000);
        doTest(csv, buildResultForCsvWithOneRow(csv));
        // 400,001 characters not ok
        csv = buildCsvWithXCharacters(400001);
        doFailingTest(buildResultForCsvWithOneRow(csv),
                      "Exceeded max length for one record: 400001. Max length for one record should be less than or equal to 400000",
                      csv);
    }

    public void testFileSizeLimits() throws Exception {
        // 10,000,000 characters per file ok (100 100,000 char rows)
        String csv = buildCsvWithXRowsAndYCharsPerRow(100, 100000);
        final String[][] result = buildResultForCsvWithXRows(100, 100000);
        doTest(csv, result);
        // 10,000,001 characters per file not ok
        csv += "a\n";
        doFailingTest(result,
                      "Exceeded max file size: 10000001. Max file size in characters should be less than or equal to 10000000",
                      csv);
    }

    private String buildCsvWithXCharacters(int numChars) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < numChars; i++) {
            sb.append("a");
        }
        return sb.toString();
    }

    private String[][] buildResultForCsvWithOneRow(String row) {
        String[][] result = new String[1][1];
        result[0][0] = row;
        return result;
    }

}
