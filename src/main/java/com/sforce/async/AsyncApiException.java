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

/**
 * AsyncApiException --
 *
 * @author mcheenath
 * @since 160
 */

public class AsyncApiException extends Exception implements com.sforce.ws.bind.XMLizable {

  /**
   * Constructor
   */
  public AsyncApiException() {
  }

  public AsyncApiException(String message, AsyncExceptionCode code, Throwable th) {
      super(code + " : " + message, th);
      exceptionCode = code;
      exceptionMessage = message;
  }

  public AsyncApiException(String message, AsyncExceptionCode code) {
      super(code + " : " + message);
      exceptionCode = code;
      exceptionMessage = message;
  }

  @Override
  public String getMessage() {
      return exceptionCode + " : "  + exceptionMessage;
  }


  /**
   * element  : exceptionCode of type {http://www.force.com/2009/06/asyncapi/dataload}AsyncExceptionCode
   * java type: com.sforce.soap.dataload.AsyncExceptionCode
   */
  private static final com.sforce.ws.bind.TypeInfo exceptionCode__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload",
            "exceptionCode","http://www.force.com/2009/06/asyncapi/dataload","AsyncExceptionCode",1,1,true);

  private boolean exceptionCode__is_set = false;

  private AsyncExceptionCode exceptionCode;

  public AsyncExceptionCode getExceptionCode() {
    return exceptionCode;
  }



  public void setExceptionCode(AsyncExceptionCode exceptionCode) {
    this.exceptionCode = exceptionCode;
    exceptionCode__is_set = true;
  }

  /**
   * element  : exceptionMessage of type {http://www.w3.org/2001/XMLSchema}string
   * java type: java.lang.String
   */
  private static final com.sforce.ws.bind.TypeInfo exceptionMessage__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload",
            "exceptionMessage","http://www.w3.org/2001/XMLSchema","string",1,1,true);

  private boolean exceptionMessage__is_set = false;

  private java.lang.String exceptionMessage;

  public java.lang.String getExceptionMessage() {
    return exceptionMessage;
  }



  public void setExceptionMessage(java.lang.String exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
    exceptionMessage__is_set = true;
  }


  /**
   */
  @Override
  public void write(javax.xml.namespace.QName __element,
      com.sforce.ws.parser.XmlOutputStream __out, com.sforce.ws.bind.TypeMapper __typeMapper)
      throws java.io.IOException {
    __out.writeStartTag(__element.getNamespaceURI(), __element.getLocalPart());

    writeFields(__out, __typeMapper);
    __out.writeEndTag(__element.getNamespaceURI(), __element.getLocalPart());
  }

  protected void writeFields(com.sforce.ws.parser.XmlOutputStream __out,
      com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException {

    __typeMapper.writeObject(__out, exceptionCode__typeInfo, exceptionCode, exceptionCode__is_set);
    __typeMapper.writeString(__out, exceptionMessage__typeInfo, exceptionMessage, exceptionMessage__is_set);
  }


  @Override
  public void load(com.sforce.ws.parser.XmlInputStream __in,
      com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {
    __typeMapper.consumeStartTag(__in);
    loadFields(__in, __typeMapper);
    __typeMapper.consumeEndTag(__in);
  }

  protected void loadFields(com.sforce.ws.parser.XmlInputStream __in,
      com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {

    __in.peekTag();
    if (__typeMapper.isElement(__in, exceptionCode__typeInfo)) {
      setExceptionCode((AsyncExceptionCode)__typeMapper.readObject(__in, exceptionCode__typeInfo, AsyncExceptionCode.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, exceptionMessage__typeInfo)) {
      setExceptionMessage(__typeMapper.readString(__in, exceptionMessage__typeInfo, java.lang.String.class));
    }
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
