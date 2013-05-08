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

import java.io.PrintWriter;
import java.io.Writer;

/**
 * CSV writer
 *
 * <p/>
 * User: mcheenath
 * Date: Nov 1, 2010
 */
public class CsvWriter {
    private PrintWriter writer;

    public CsvWriter(String[] headers, Writer w) {
        assert headers != null;
        assert headers.length != 0;

        writer = new PrintWriter(w, true);
        writeRecord(headers);
    }

    public void writeRecord(String[] values) {
        assert values != null;

        writeFirstField(values[0]);

        for (int i=1; i<values.length; i++) {
            writeField(values[i]);
        }

        endRecord();
    }

    public void endDocument() {
        writer.close();
    }

    public void endRecord() {
        writer.println();
    }

    public void writeField(String value) {
        writer.print(",");
        writeFirstField(value);
    }

    public void writeFirstField(String value) {
        if (value == null) {
            return;
        }

        writer.print("\"");
        value = value.replaceAll("\"", "\"\"");
        writer.print(value);
        writer.print("\"");
    }
}
