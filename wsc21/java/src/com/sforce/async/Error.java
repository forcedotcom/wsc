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
    new com.sforce.ws.bind.TypeInfo(RestConnection.NAMESPACE,"fields","http://www.w3.org/2001/XMLSchema","string",0,-1,true);

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
    new com.sforce.ws.bind.TypeInfo(RestConnection.NAMESPACE,"message","http://www.w3.org/2001/XMLSchema","string",1,1,true);

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
    new com.sforce.ws.bind.TypeInfo(RestConnection.NAMESPACE,"statusCode",RestConnection.NAMESPACE,"StatusCode",1,1,true);

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
