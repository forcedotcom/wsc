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
