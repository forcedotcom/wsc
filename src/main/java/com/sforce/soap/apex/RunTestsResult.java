package com.sforce.soap.apex;

/**
 * Generated by ComplexTypeCodeGenerator.java. Please do not edit.
 */
public class RunTestsResult implements com.sforce.ws.bind.XMLizable {

    /**
     * Constructor
     */
    public RunTestsResult() {}

    /**
     * element : codeCoverage of type {http://soap.sforce.com/2006/08/apex}CodeCoverageResult
     * java type: com.sforce.soap.apex.CodeCoverageResult[]
     */
    private static final com.sforce.ws.bind.TypeInfo codeCoverage__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","codeCoverage","http://soap.sforce.com/2006/08/apex","CodeCoverageResult",0,-1,true);

    private boolean codeCoverage__is_set = false;

    private com.sforce.soap.apex.CodeCoverageResult[] codeCoverage = new com.sforce.soap.apex.CodeCoverageResult[0];

    public com.sforce.soap.apex.CodeCoverageResult[] getCodeCoverage() {
      return codeCoverage;
    }

    public void setCodeCoverage(com.sforce.soap.apex.CodeCoverageResult[] codeCoverage) {
      this.codeCoverage = codeCoverage;
      codeCoverage__is_set = true;
    }

    /**
     * element : codeCoverageWarnings of type {http://soap.sforce.com/2006/08/apex}CodeCoverageWarning
     * java type: com.sforce.soap.apex.CodeCoverageWarning[]
     */
    private static final com.sforce.ws.bind.TypeInfo codeCoverageWarnings__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","codeCoverageWarnings","http://soap.sforce.com/2006/08/apex","CodeCoverageWarning",0,-1,true);

    private boolean codeCoverageWarnings__is_set = false;

    private com.sforce.soap.apex.CodeCoverageWarning[] codeCoverageWarnings = new com.sforce.soap.apex.CodeCoverageWarning[0];

    public com.sforce.soap.apex.CodeCoverageWarning[] getCodeCoverageWarnings() {
      return codeCoverageWarnings;
    }

    public void setCodeCoverageWarnings(com.sforce.soap.apex.CodeCoverageWarning[] codeCoverageWarnings) {
      this.codeCoverageWarnings = codeCoverageWarnings;
      codeCoverageWarnings__is_set = true;
    }

    /**
     * element : failures of type {http://soap.sforce.com/2006/08/apex}RunTestFailure
     * java type: com.sforce.soap.apex.RunTestFailure[]
     */
    private static final com.sforce.ws.bind.TypeInfo failures__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","failures","http://soap.sforce.com/2006/08/apex","RunTestFailure",0,-1,true);

    private boolean failures__is_set = false;

    private com.sforce.soap.apex.RunTestFailure[] failures = new com.sforce.soap.apex.RunTestFailure[0];

    public com.sforce.soap.apex.RunTestFailure[] getFailures() {
      return failures;
    }

    public void setFailures(com.sforce.soap.apex.RunTestFailure[] failures) {
      this.failures = failures;
      failures__is_set = true;
    }

    /**
     * element : numFailures of type {http://www.w3.org/2001/XMLSchema}int
     * java type: int
     */
    private static final com.sforce.ws.bind.TypeInfo numFailures__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","numFailures","http://www.w3.org/2001/XMLSchema","int",1,1,true);

    private boolean numFailures__is_set = false;

    private int numFailures;

    public int getNumFailures() {
      return numFailures;
    }

    public void setNumFailures(int numFailures) {
      this.numFailures = numFailures;
      numFailures__is_set = true;
    }

    /**
     * element : numTestsRun of type {http://www.w3.org/2001/XMLSchema}int
     * java type: int
     */
    private static final com.sforce.ws.bind.TypeInfo numTestsRun__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","numTestsRun","http://www.w3.org/2001/XMLSchema","int",1,1,true);

    private boolean numTestsRun__is_set = false;

    private int numTestsRun;

    public int getNumTestsRun() {
      return numTestsRun;
    }

    public void setNumTestsRun(int numTestsRun) {
      this.numTestsRun = numTestsRun;
      numTestsRun__is_set = true;
    }

    /**
     * element : successes of type {http://soap.sforce.com/2006/08/apex}RunTestSuccess
     * java type: com.sforce.soap.apex.RunTestSuccess[]
     */
    private static final com.sforce.ws.bind.TypeInfo successes__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","successes","http://soap.sforce.com/2006/08/apex","RunTestSuccess",0,-1,true);

    private boolean successes__is_set = false;

    private com.sforce.soap.apex.RunTestSuccess[] successes = new com.sforce.soap.apex.RunTestSuccess[0];

    public com.sforce.soap.apex.RunTestSuccess[] getSuccesses() {
      return successes;
    }

    public void setSuccesses(com.sforce.soap.apex.RunTestSuccess[] successes) {
      this.successes = successes;
      successes__is_set = true;
    }

    /**
     * element : totalTime of type {http://www.w3.org/2001/XMLSchema}double
     * java type: double
     */
    private static final com.sforce.ws.bind.TypeInfo totalTime__typeInfo =
      new com.sforce.ws.bind.TypeInfo("http://soap.sforce.com/2006/08/apex","totalTime","http://www.w3.org/2001/XMLSchema","double",1,1,true);

    private boolean totalTime__is_set = false;

    private double totalTime;

    public double getTotalTime() {
      return totalTime;
    }

    public void setTotalTime(double totalTime) {
      this.totalTime = totalTime;
      totalTime__is_set = true;
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
       __typeMapper.writeObject(__out, codeCoverage__typeInfo, codeCoverage, codeCoverage__is_set);
       __typeMapper.writeObject(__out, codeCoverageWarnings__typeInfo, codeCoverageWarnings, codeCoverageWarnings__is_set);
       __typeMapper.writeObject(__out, failures__typeInfo, failures, failures__is_set);
       __typeMapper.writeInt(__out, numFailures__typeInfo, numFailures, numFailures__is_set);
       __typeMapper.writeInt(__out, numTestsRun__typeInfo, numTestsRun, numTestsRun__is_set);
       __typeMapper.writeObject(__out, successes__typeInfo, successes, successes__is_set);
       __typeMapper.writeDouble(__out, totalTime__typeInfo, totalTime, totalTime__is_set);
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
        if (__typeMapper.isElement(__in, codeCoverage__typeInfo)) {
            setCodeCoverage((com.sforce.soap.apex.CodeCoverageResult[])__typeMapper.readObject(__in, codeCoverage__typeInfo, com.sforce.soap.apex.CodeCoverageResult[].class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, codeCoverageWarnings__typeInfo)) {
            setCodeCoverageWarnings((com.sforce.soap.apex.CodeCoverageWarning[])__typeMapper.readObject(__in, codeCoverageWarnings__typeInfo, com.sforce.soap.apex.CodeCoverageWarning[].class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, failures__typeInfo)) {
            setFailures((com.sforce.soap.apex.RunTestFailure[])__typeMapper.readObject(__in, failures__typeInfo, com.sforce.soap.apex.RunTestFailure[].class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, numFailures__typeInfo)) {
            setNumFailures((int)__typeMapper.readInt(__in, numFailures__typeInfo, int.class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, numTestsRun__typeInfo)) {
            setNumTestsRun((int)__typeMapper.readInt(__in, numTestsRun__typeInfo, int.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, successes__typeInfo)) {
            setSuccesses((com.sforce.soap.apex.RunTestSuccess[])__typeMapper.readObject(__in, successes__typeInfo, com.sforce.soap.apex.RunTestSuccess[].class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, totalTime__typeInfo)) {
            setTotalTime((double)__typeMapper.readDouble(__in, totalTime__typeInfo, double.class));
        }
    }

    @Override
    public String toString() {
      java.lang.StringBuilder sb = new java.lang.StringBuilder();
      sb.append("[RunTestsResult ");
      sb.append(" codeCoverage=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(codeCoverage)+"'\n");
      sb.append(" codeCoverageWarnings=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(codeCoverageWarnings)+"'\n");
      sb.append(" failures=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(failures)+"'\n");
      sb.append(" numFailures=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(numFailures)+"'\n");
      sb.append(" numTestsRun=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(numTestsRun)+"'\n");
      sb.append(" successes=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(successes)+"'\n");
      sb.append(" totalTime=");
      sb.append("'"+com.sforce.ws.util.Verbose.toString(totalTime)+"'\n");
      sb.append("]\n");
      return sb.toString();
    }
}
