package com.sforce.soap.apex;

/**
 * Generated by ComplexTypeCodeGenerator.java. Please do not edit.
 */
public class CompileAndTestResult implements com.sforce.ws.bind.XMLizable {

    /**
     * Constructor
     */
    public CompileAndTestResult() {}

    /**
     * element : classes of type {http://soap.sforce.com/2006/08/apex}CompileClassResult
     * java type: com.sforce.soap.apex.CompileClassResult[]
     */
    private static final com.sforce.ws.bind.TypeInfo classes__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","classes","http://soap.sforce.com/2006/08/apex","CompileClassResult",0,-1,true);

    private boolean classes__is_set = false;

    private com.sforce.soap.apex.CompileClassResult[] classes = new com.sforce.soap.apex.CompileClassResult[0];

    public com.sforce.soap.apex.CompileClassResult[] getClasses() {
      return classes;
    }

    public void setClasses(com.sforce.soap.apex.CompileClassResult[] classes) {
      this.classes = classes;
      classes__is_set = true;
    }

    /**
     * element : deleteClasses of type {http://soap.sforce.com/2006/08/apex}DeleteApexResult
     * java type: com.sforce.soap.apex.DeleteApexResult[]
     */
    private static final com.sforce.ws.bind.TypeInfo deleteClasses__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","deleteClasses","http://soap.sforce.com/2006/08/apex","DeleteApexResult",0,-1,true);

    private boolean deleteClasses__is_set = false;

    private com.sforce.soap.apex.DeleteApexResult[] deleteClasses = new com.sforce.soap.apex.DeleteApexResult[0];

    public com.sforce.soap.apex.DeleteApexResult[] getDeleteClasses() {
      return deleteClasses;
    }

    public void setDeleteClasses(com.sforce.soap.apex.DeleteApexResult[] deleteClasses) {
      this.deleteClasses = deleteClasses;
      deleteClasses__is_set = true;
    }

    /**
     * element : deleteTriggers of type {http://soap.sforce.com/2006/08/apex}DeleteApexResult
     * java type: com.sforce.soap.apex.DeleteApexResult[]
     */
    private static final com.sforce.ws.bind.TypeInfo deleteTriggers__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","deleteTriggers","http://soap.sforce.com/2006/08/apex","DeleteApexResult",0,-1,true);

    private boolean deleteTriggers__is_set = false;

    private com.sforce.soap.apex.DeleteApexResult[] deleteTriggers = new com.sforce.soap.apex.DeleteApexResult[0];

    public com.sforce.soap.apex.DeleteApexResult[] getDeleteTriggers() {
      return deleteTriggers;
    }

    public void setDeleteTriggers(com.sforce.soap.apex.DeleteApexResult[] deleteTriggers) {
      this.deleteTriggers = deleteTriggers;
      deleteTriggers__is_set = true;
    }

    /**
     * element : runTestsResult of type {http://soap.sforce.com/2006/08/apex}RunTestsResult
     * java type: com.sforce.soap.apex.RunTestsResult
     */
    private static final com.sforce.ws.bind.TypeInfo runTestsResult__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","runTestsResult","http://soap.sforce.com/2006/08/apex","RunTestsResult",1,1,true);

    private boolean runTestsResult__is_set = false;

    private com.sforce.soap.apex.RunTestsResult runTestsResult;

    public com.sforce.soap.apex.RunTestsResult getRunTestsResult() {
      return runTestsResult;
    }

    public void setRunTestsResult(com.sforce.soap.apex.RunTestsResult runTestsResult) {
      this.runTestsResult = runTestsResult;
      runTestsResult__is_set = true;
    }

    /**
     * element : success of type {http://www.w3.org/2001/XMLSchema}boolean
     * java type: boolean
     */
    private static final com.sforce.ws.bind.TypeInfo success__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","success","http://www.w3.org/2001/XMLSchema","boolean",1,1,true);

    private boolean success__is_set = false;

    private boolean success;

    public boolean getSuccess() {
      return success;
    }

    public boolean isSuccess() {
      return success;
    }

    public void setSuccess(boolean success) {
      this.success = success;
      success__is_set = true;
    }

    /**
     * element : triggers of type {http://soap.sforce.com/2006/08/apex}CompileTriggerResult
     * java type: com.sforce.soap.apex.CompileTriggerResult[]
     */
    private static final com.sforce.ws.bind.TypeInfo triggers__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","triggers","http://soap.sforce.com/2006/08/apex","CompileTriggerResult",0,-1,true);

    private boolean triggers__is_set = false;

    private com.sforce.soap.apex.CompileTriggerResult[] triggers = new com.sforce.soap.apex.CompileTriggerResult[0];

    public com.sforce.soap.apex.CompileTriggerResult[] getTriggers() {
      return triggers;
    }

    public void setTriggers(com.sforce.soap.apex.CompileTriggerResult[] triggers) {
      this.triggers = triggers;
      triggers__is_set = true;
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
       __typeMapper.writeObject(__out, classes__typeInfo, classes, classes__is_set);
       __typeMapper.writeObject(__out, deleteClasses__typeInfo, deleteClasses, deleteClasses__is_set);
       __typeMapper.writeObject(__out, deleteTriggers__typeInfo, deleteTriggers, deleteTriggers__is_set);
       __typeMapper.writeObject(__out, runTestsResult__typeInfo, runTestsResult, runTestsResult__is_set);
       __typeMapper.writeBoolean(__out, success__typeInfo, success, success__is_set);
       __typeMapper.writeObject(__out, triggers__typeInfo, triggers, triggers__is_set);
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
        if (__typeMapper.isElement(__in, classes__typeInfo)) {
            setClasses((com.sforce.soap.apex.CompileClassResult[])__typeMapper.readObject(__in, classes__typeInfo, com.sforce.soap.apex.CompileClassResult[].class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, deleteClasses__typeInfo)) {
            setDeleteClasses((com.sforce.soap.apex.DeleteApexResult[])__typeMapper.readObject(__in, deleteClasses__typeInfo, com.sforce.soap.apex.DeleteApexResult[].class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, deleteTriggers__typeInfo)) {
            setDeleteTriggers((com.sforce.soap.apex.DeleteApexResult[])__typeMapper.readObject(__in, deleteTriggers__typeInfo, com.sforce.soap.apex.DeleteApexResult[].class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, runTestsResult__typeInfo)) {
            setRunTestsResult((com.sforce.soap.apex.RunTestsResult)__typeMapper.readObject(__in, runTestsResult__typeInfo, com.sforce.soap.apex.RunTestsResult.class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, success__typeInfo)) {
            setSuccess(__typeMapper.readBoolean(__in, success__typeInfo, boolean.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, triggers__typeInfo)) {
            setTriggers((com.sforce.soap.apex.CompileTriggerResult[])__typeMapper.readObject(__in, triggers__typeInfo, com.sforce.soap.apex.CompileTriggerResult[].class));
        }
    }

    @Override
    public String toString() {
      java.lang.StringBuilder sb = new java.lang.StringBuilder();
      sb.append("[CompileAndTestResult ");
      sb.append(" classes=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(classes)+"'\n");
      sb.append(" deleteClasses=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(deleteClasses)+"'\n");
      sb.append(" deleteTriggers=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(deleteTriggers)+"'\n");
      sb.append(" runTestsResult=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(runTestsResult)+"'\n");
      sb.append(" success=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(success)+"'\n");
      sb.append(" triggers=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(triggers)+"'\n");
      sb.append("]\n");
      return sb.toString();
    }
}
