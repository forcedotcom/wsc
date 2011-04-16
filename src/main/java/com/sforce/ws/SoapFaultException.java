/*
 * Copyright, 2006, salesforce.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.ws;

import javax.xml.namespace.QName;

/**
 * Base class for all exceptions that are related to soap fault messages
 * 
 * @author sfell
 * @since  146
 */
public class SoapFaultException extends ConnectionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6727192150757263283L;
	private QName faultCode;
	
	public SoapFaultException() {
	}
	
	public SoapFaultException(QName faultCode, String faultString) {
		super(faultString);
		this.faultCode = faultCode;
	}
	
	public SoapFaultException(QName faultCode, String faultString, Throwable cause) {
		super(faultString, cause);
		this.faultCode = faultCode;
	}
	
	public QName getFaultCode() {
		return faultCode;
	}
	
	public void setFaultCode(QName fc) {
		this.faultCode = fc;
	}
}
