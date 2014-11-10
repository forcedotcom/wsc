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
import java.io.InputStream;
import java.io.OutputStream;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.PullParserException;
import com.sforce.ws.transport.Transport;

/**
 * CSV batch request.
 * 
 * @author drobertson
 * @since 188
 *
 */
public class CsvBatchRequest {

    private final OutputStream csvStream;
    private final Transport transport;
    private boolean isNew;
    
    private final String UTF8 = "UTF-8";

    public CsvBatchRequest(Transport transport, OutputStream out) throws IOException {
        this.transport = transport;
        this.csvStream = out;
        
        this.isNew = true;
    }

    public void addHeader(String[] columns) throws AsyncApiException {
    	if (isNew) {
    		addRow(columns);
    	}
    	else {
            throw new AsyncApiException("Can't add header to populated CSV", AsyncExceptionCode.ClientInputError);
    	}
    }
    
    
    public void addRow(String[] columns) throws AsyncApiException {
        try {
        	for (int i = 0; i < columns.length; ++i) {
                if (i == 0) {
                	addFirstColumn(columns[i]);
                }
                else {
                	addColumn(columns[i]);
                }
        	}
        	isNew = false;
        } catch (IOException e) {
            throw new AsyncApiException("Failed to add row", AsyncExceptionCode.ClientInputError, e);
        }
    }

    public BatchInfo completeRequest() throws AsyncApiException {
        try {
            csvStream.close();
            InputStream in = transport.getContent();

            if (transport.isSuccessful()) {
                return BatchRequest.loadBatchInfo(in);
            } else {
                BulkConnection.parseAndThrowException(in);
            }
        } catch(IOException e) {
            throw new AsyncApiException("Failed to complete request", AsyncExceptionCode.ClientInputError, e);
        } catch (PullParserException e) {
            throw new AsyncApiException("Failed to complete request", AsyncExceptionCode.ClientInputError, e);
		} catch (ConnectionException e) {
            throw new AsyncApiException("Failed to complete request", AsyncExceptionCode.ClientInputError, e);
		}
        return null;
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
