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
