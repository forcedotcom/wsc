package com.sforce.soap.apex;

/**
 * Generated by ComplexTypeCodeGenerator.java. Please do not edit.
 */
public class NamespacePackagePair implements com.sforce.ws.bind.XMLizable {

    /**
     * Constructor
     */
    public NamespacePackagePair() {}

    /**
     * element : namespace of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo namespace__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","namespace","http://www.w3.org/2001/XMLSchema","string",1,1,true);

    private boolean namespace__is_set = false;

    private java.lang.String namespace;

    public java.lang.String getNamespace() {
      return namespace;
    }

    public void setNamespace(java.lang.String namespace) {
      this.namespace = namespace;
      namespace__is_set = true;
    }

    /**
     * element : packageName of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo packageName__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","packageName","http://www.w3.org/2001/XMLSchema","string",1,1,true);

    private boolean packageName__is_set = false;

    private java.lang.String packageName;

    public java.lang.String getPackageName() {
      return packageName;
    }

    public void setPackageName(java.lang.String packageName) {
      this.packageName = packageName;
      packageName__is_set = true;
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
         com.sforce.ws.bind.TypeMapper __typeMapper)
         throws java.io.IOException {
       __typeMapper.writeString(__out, namespace__typeInfo, namespace, namespace__is_set);
       __typeMapper.writeString(__out, packageName__typeInfo, packageName, packageName__is_set);
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
        if (__typeMapper.verifyElement(__in, namespace__typeInfo)) {
            setNamespace(__typeMapper.readString(__in, namespace__typeInfo, java.lang.String.class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, packageName__typeInfo)) {
            setPackageName(__typeMapper.readString(__in, packageName__typeInfo, java.lang.String.class));
        }
    }

    @Override
    public String toString() {
      java.lang.StringBuilder sb = new java.lang.StringBuilder();
      sb.append("[NamespacePackagePair ");
      sb.append(" namespace=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(namespace)+"'\n");
      sb.append(" packageName=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(packageName)+"'\n");
      sb.append("]\n");
      return sb.toString();
    }
}