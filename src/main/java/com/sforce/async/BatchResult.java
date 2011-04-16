package com.sforce.async;

import com.sforce.ws.bind.TypeMapper;

/**
 * BatchResult --
 *
 * @author mcheenath
 * @since 160
 */

public class BatchResult implements com.sforce.ws.bind.XMLizable {

    private boolean partialResult = false;

  /**
   * Constructor
   */
  public BatchResult() {
  }

    public boolean isPartialResult() {
        return partialResult;
    }


  /**
   * element  : result of type {urn:partner.soap.sforce.com}SaveResult
   * java type: com.sforce.soap.partner.wsc.SaveResult[]
   */
  private static final com.sforce.ws.bind.TypeInfo result__typeInfo =
    new com.sforce.ws.bind.TypeInfo(RestConnection.NAMESPACE,"result",RestConnection.NAMESPACE,"SaveResult",0,-1,true);

  private boolean result__is_set = false;

  private Result[] result = new Result[0];

  public Result[] getResult() {
    return result;
  }

  public void setResult(Result[] result) {
    this.result = result;
    result__is_set = true;
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

    __typeMapper.writeObject(__out, result__typeInfo, result, result__is_set);
  }


  @Override
  public void load(com.sforce.ws.parser.XmlInputStream __in,
      com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {
      try {
          __typeMapper.consumeStartTag(__in);
          loadFields(__in, __typeMapper);
          __typeMapper.consumeEndTag(__in);
      } catch(TypeMapper.PartialArrayException e) {
          partialResult = true;
          setResult((Result[]) e.getArrayResult());
      }
  }

  protected void loadFields(com.sforce.ws.parser.XmlInputStream __in,
      com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {

    __in.peekTag();
    if (__typeMapper.isElement(__in, result__typeInfo)) {
        setResult((Result[])__typeMapper.readPartialArray(__in, result__typeInfo, Result[].class));
    }
  }

  @Override
  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append("[BatchResult ");

    sb.append(" result=");
    sb.append("'").append(com.sforce.ws.util.Verbose.toString(result)).append("'\n");
    sb.append("]\n");
    return sb.toString();
  }
}
