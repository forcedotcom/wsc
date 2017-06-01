package com.sforce.soap.apex;

/**
 * Generated by ComplexTypeCodeGenerator.java. Please do not edit.
 */
public class RunTestSuccess implements com.sforce.ws.bind.XMLizable {

    /**
     * Constructor
     */
    public RunTestSuccess() {}

    /**
     * element : id of type {http://soap.sforce.com/2006/08/apex}ID
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo id__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","id","http://soap.sforce.com/2006/08/apex","ID",1,1,true);

    private boolean id__is_set = false;

    private java.lang.String id;

    public java.lang.String getId() {
      return id;
    }

    public void setId(java.lang.String id) {
      this.id = id;
      id__is_set = true;
    }

    /**
     * element : methodName of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo methodName__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","methodName","http://www.w3.org/2001/XMLSchema","string",1,1,true);

    private boolean methodName__is_set = false;

    private java.lang.String methodName;

    public java.lang.String getMethodName() {
      return methodName;
    }

    public void setMethodName(java.lang.String methodName) {
      this.methodName = methodName;
      methodName__is_set = true;
    }

    /**
     * element : name of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo name__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","name","http://www.w3.org/2001/XMLSchema","string",1,1,true);

    private boolean name__is_set = false;

    private java.lang.String name;

    public java.lang.String getName() {
      return name;
    }

    public void setName(java.lang.String name) {
      this.name = name;
      name__is_set = true;
    }

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
     * element : time of type {http://www.w3.org/2001/XMLSchema}double
     * java type: double
     */
    private static final com.sforce.ws.bind.TypeInfo time__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","time","http://www.w3.org/2001/XMLSchema","double",1,1,true);

    private boolean time__is_set = false;

    private double time;

    public double getTime() {
      return time;
    }

    public void setTime(double time) {
      this.time = time;
      time__is_set = true;
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
       __typeMapper.writeString(__out, id__typeInfo, id, id__is_set);
       __typeMapper.writeString(__out, methodName__typeInfo, methodName, methodName__is_set);
       __typeMapper.writeString(__out, name__typeInfo, name, name__is_set);
       __typeMapper.writeString(__out, namespace__typeInfo, namespace, namespace__is_set);
       __typeMapper.writeDouble(__out, time__typeInfo, time, time__is_set);
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
        if (__typeMapper.verifyElement(__in, id__typeInfo)) {
            setId(__typeMapper.readString(__in, id__typeInfo, java.lang.String.class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, methodName__typeInfo)) {
            setMethodName(__typeMapper.readString(__in, methodName__typeInfo, java.lang.String.class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, name__typeInfo)) {
            setName(__typeMapper.readString(__in, name__typeInfo, java.lang.String.class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, namespace__typeInfo)) {
            setNamespace(__typeMapper.readString(__in, namespace__typeInfo, java.lang.String.class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, time__typeInfo)) {
            setTime((double)__typeMapper.readDouble(__in, time__typeInfo, double.class));
        }
    }

    @Override
    public String toString() {
      java.lang.StringBuilder sb = new java.lang.StringBuilder();
      sb.append("[RunTestSuccess ");
      sb.append(" id=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(id)+"'\n");
      sb.append(" methodName=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(methodName)+"'\n");
      sb.append(" name=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(name)+"'\n");
      sb.append(" namespace=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(namespace)+"'\n");
      sb.append(" time=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(time)+"'\n");
      sb.append("]\n");
      return sb.toString();
    }
}
