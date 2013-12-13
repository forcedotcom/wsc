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

import java.io.IOException;
import java.io.OutputStream;

import com.sforce.ws.transport.Transport;

/**
 * 
 * Transformation specification request.
 * 
 * @author drobertson
 * @since 188
 *
 */
public class TransformationSpecRequest {

	private final static String SALESFORCE_FIELD = "Salesforce Field";
	private final static String CSV_HEADER = "Csv Header";
	private final static String VALUE = "Value";
	private final static String HINT = "Hint";
	
    private final OutputStream csvStream;
    private final Transport transport;
    
    private final String UTF8 = "UTF-8";
	
    public TransformationSpecRequest(Transport transport, OutputStream out) throws IOException, AsyncApiException {
        this.transport = transport;
        this.csvStream = out;
        
        addRow(new String[] { SALESFORCE_FIELD, CSV_HEADER, VALUE, HINT });
    }

    /**
     * Adds a transformation spec row.
     * 
     * @param sfdcField the Salesforce field that should receive the value
     * @param csvHeader the header from the CSV file that identifies which column, when non-null, contains the value to provide
     * @param value a default or fallback value to use if either no CSV header was specified or the value in the column was null
     * @param hint an optional hint, whose content depends on the type of the Salesforce field
     * @throws AsyncApiException
     */
    public void addSpecRow(String sfdcField, String csvHeader, String value, String hint) throws AsyncApiException {
        addRow(new String[] { sfdcField, csvHeader, value, hint });
    }

    public void completeRequest() throws AsyncApiException {
        try {
            csvStream.close();

            if (!transport.isSuccessful()) {
                BulkConnection.parseAndThrowException(transport.getContent());
            }
        } catch(IOException e) {
            throw new AsyncApiException("Failed to complete request", AsyncExceptionCode.ClientInputError, e);
		}
    }
    
    
    private void addRow(String[] columns) throws AsyncApiException {
        try {
        	for (int i = 0; i < columns.length; ++i) {
                if (i == 0) {
                	addFirstColumn(columns[i]);
                }
                else {
                	addColumn(columns[i]);
                }
        	}
        } catch (IOException e) {
            throw new AsyncApiException("Failed to add row", AsyncExceptionCode.ClientInputError, e);
        }
    }

    
    private void addFirstColumn(String value) throws IOException {
    	csvStream.write(",".getBytes(UTF8));
    	addColumn(value);
    }
    
    private void addColumn(String value) throws IOException {
    	if (value != null) {
        	csvStream.write("\"".getBytes(UTF8));
        	csvStream.write(value.replace("\"", "\"\"").getBytes(UTF8));
        	csvStream.write("\"".getBytes(UTF8));
    	}
    }
}
