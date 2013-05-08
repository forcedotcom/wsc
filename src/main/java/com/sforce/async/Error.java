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
 * Error --
 *
 * @author mcheenath
 * @since 160
 */

public class Error implements com.sforce.ws.bind.XMLizable {

  /**
   * Constructor
   */
  public Error() {
  }

  
  /**
   * element  : fields of type {http://www.w3.org/2001/XMLSchema}string
   * java type: java.lang.String[]
   */
  private static final com.sforce.ws.bind.TypeInfo fields__typeInfo =
    new com.sforce.ws.bind.TypeInfo(BulkConnection.NAMESPACE,"fields","http://www.w3.org/2001/XMLSchema","string",0,-1,true);

  private boolean fields__is_set = false;

  private java.lang.String[] fields = new java.lang.String[0];

  public java.lang.String[] getFields() {
    return fields;
  }



  public void setFields(java.lang.String[] fields) {
    this.fields = fields;
    fields__is_set = true;
  }

  /**
   * element  : message of type {http://www.w3.org/2001/XMLSchema}string
   * java type: java.lang.String
   */
  private static final com.sforce.ws.bind.TypeInfo message__typeInfo =
    new com.sforce.ws.bind.TypeInfo(BulkConnection.NAMESPACE,"message","http://www.w3.org/2001/XMLSchema","string",1,1,true);

  private boolean message__is_set = false;

  private java.lang.String message;

  public java.lang.String getMessage() {
    return message;
  }



  public void setMessage(java.lang.String message) {
    this.message = message;
    message__is_set = true;
  }

  /**
   * element  : statusCode of type {urn:partner.soap.sforce.com}StatusCode
   * java type: com.sforce.soap.partner.wsc.StatusCode
   */
  private static final com.sforce.ws.bind.TypeInfo statusCode__typeInfo =
    new com.sforce.ws.bind.TypeInfo(BulkConnection.NAMESPACE,"statusCode", BulkConnection.NAMESPACE,"StatusCode",1,1,true);

  private boolean statusCode__is_set = false;

  private StatusCode statusCode;

  public StatusCode getStatusCode() {
    return statusCode;
  }



  public void setStatusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
    statusCode__is_set = true;
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

    __typeMapper.writeObject(__out, fields__typeInfo, fields, fields__is_set);
    __typeMapper.writeString(__out, message__typeInfo, message, message__is_set);
    __typeMapper.writeObject(__out, statusCode__typeInfo, statusCode, statusCode__is_set);
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
    if (__typeMapper.isElement(__in, fields__typeInfo)) {
      setFields((java.lang.String[])__typeMapper.readObject(__in, fields__typeInfo, java.lang.String[].class));
    }
    __in.peekTag();
    if (__typeMapper.verifyElement(__in, message__typeInfo)) {
      setMessage(__typeMapper.readString(__in, message__typeInfo, java.lang.String.class));
    }
    __in.peekTag();
    if (__typeMapper.verifyElement(__in, statusCode__typeInfo)) {
      setStatusCode((StatusCode)__typeMapper.readObject(__in, statusCode__typeInfo, StatusCode.class));
    }
  }

  @Override
  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append("[Error ");

    sb.append(" fields=");
    sb.append("'").append(com.sforce.ws.util.Verbose.toString(fields)).append("'\n");
    sb.append(" message=");
    sb.append("'").append(com.sforce.ws.util.Verbose.toString(message)).append("'\n");
    sb.append(" statusCode=");
    sb.append("'").append(com.sforce.ws.util.Verbose.toString(statusCode)).append("'\n");
    sb.append("]\n");
    return sb.toString();
  }
}
