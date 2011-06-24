/*
 * Copyright (c) 2011, salesforce.com, inc.
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
package com.sforce.rest;

/**
 * RestApiException
 *
 * @author gwester, mcheenath
 * @since 172
 */

public class RestApiException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String exceptionMessage;
	private RestExceptionCode exceptionCode;

	/**
	 * Constructor
	 */
	public RestApiException() {
	}

	public RestApiException(String message, RestExceptionCode code, Throwable th) {
		super(code + " : " + message, th);
		exceptionCode = code;
		exceptionMessage = message;
	}

	public RestApiException(String message, RestExceptionCode code) {
		super(code + " : " + message);
		exceptionCode = code;
		exceptionMessage = message;
	}

	@Override
	public String getMessage() {
		return exceptionCode + " : "  + exceptionMessage;
	}

	public RestExceptionCode getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(RestExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(java.lang.String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	 @Override
	 public String toString() {
		 java.lang.StringBuilder sb = new java.lang.StringBuilder();
		 sb.append("[AsyncApiException ");

		 sb.append(" exceptionCode=");
		 sb.append("'").append(com.sforce.ws.util.Verbose.toString(exceptionCode)).append("'\n");
		 sb.append(" exceptionMessage=");
		 sb.append("'").append(com.sforce.ws.util.Verbose.toString(exceptionMessage)).append("'\n");
		 sb.append("]\n");
		 return sb.toString();
	 }
}
