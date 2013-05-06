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
 * Async Api BatchInfo
 */
public class BatchInfo implements com.sforce.ws.bind.XMLizable {

  /**
   * Constructor
   */
  public BatchInfo() {
  }
    
  
  /**
   * element  : id of type {http://www.w3.org/2001/XMLSchema}string
   * java type: java.lang.String
   */
  private static final com.sforce.ws.bind.TypeInfo id__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload","id","http://www.w3.org/2001/XMLSchema","string",1,1,true);

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
   * element  : jobId of type {http://www.w3.org/2001/XMLSchema}string
   * java type: java.lang.String
   */
  private static final com.sforce.ws.bind.TypeInfo jobId__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload","jobId","http://www.w3.org/2001/XMLSchema","string",1,1,true);

  private boolean jobId__is_set = false;

  private java.lang.String jobId;

  public java.lang.String getJobId() {
    return jobId;
  }

  

  public void setJobId(java.lang.String jobId) {
    this.jobId = jobId;
    jobId__is_set = true;
  }
  
  /**
   * element  : state of type {http://www.force.com/2009/06/asyncapi/dataload}BatchStateEnum
   * java type: com.sforce.async.BatchStateEnum
   */
  private static final com.sforce.ws.bind.TypeInfo state__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload","state","http://www.force.com/2009/06/asyncapi/dataload","BatchStateEnum",1,1,true);

  private boolean state__is_set = false;

  private com.sforce.async.BatchStateEnum state;

  public com.sforce.async.BatchStateEnum getState() {
    return state;
  }

  

  public void setState(com.sforce.async.BatchStateEnum state) {
    this.state = state;
    state__is_set = true;
  }
  
  /**
   * element  : stateMessage of type {http://www.w3.org/2001/XMLSchema}string
   * java type: java.lang.String
   */
  private static final com.sforce.ws.bind.TypeInfo stateMessage__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload","stateMessage","http://www.w3.org/2001/XMLSchema","string",0,1,true);

  private boolean stateMessage__is_set = false;

  private java.lang.String stateMessage;

  public java.lang.String getStateMessage() {
    return stateMessage;
  }

  

  public void setStateMessage(java.lang.String stateMessage) {
    this.stateMessage = stateMessage;
    stateMessage__is_set = true;
  }
  
  /**
   * element  : createdDate of type {http://www.w3.org/2001/XMLSchema}dateTime
   * java type: java.util.Calendar
   */
  private static final com.sforce.ws.bind.TypeInfo createdDate__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload","createdDate","http://www.w3.org/2001/XMLSchema","dateTime",1,1,true);

  private boolean createdDate__is_set = false;

  private java.util.Calendar createdDate;

  public java.util.Calendar getCreatedDate() {
    return createdDate;
  }

  

  public void setCreatedDate(java.util.Calendar createdDate) {
    this.createdDate = createdDate;
    createdDate__is_set = true;
  }
  
  /**
   * element  : systemModstamp of type {http://www.w3.org/2001/XMLSchema}dateTime
   * java type: java.util.Calendar
   */
  private static final com.sforce.ws.bind.TypeInfo systemModstamp__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload","systemModstamp","http://www.w3.org/2001/XMLSchema","dateTime",0,1,true);

  private boolean systemModstamp__is_set = false;

  private java.util.Calendar systemModstamp;

  public java.util.Calendar getSystemModstamp() {
    return systemModstamp;
  }

  

  public void setSystemModstamp(java.util.Calendar systemModstamp) {
    this.systemModstamp = systemModstamp;
    systemModstamp__is_set = true;
  }
  
  /**
   * element  : numberRecordsProcessed of type {http://www.w3.org/2001/XMLSchema}int
   * java type: int
   */
  private static final com.sforce.ws.bind.TypeInfo numberRecordsProcessed__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload","numberRecordsProcessed","http://www.w3.org/2001/XMLSchema","int",1,1,true);

  private boolean numberRecordsProcessed__is_set = false;

  private int numberRecordsProcessed;

  public int getNumberRecordsProcessed() {
    return numberRecordsProcessed;
  }

  

  public void setNumberRecordsProcessed(int numberRecordsProcessed) {
    this.numberRecordsProcessed = numberRecordsProcessed;
    numberRecordsProcessed__is_set = true;
  }

  /**
   * element : numberRecordsFailed of type {http://www.w3.org/2001/XMLSchema}int java type: int
   */
  private static final com.sforce.ws.bind.TypeInfo numberRecordsFailed__typeInfo = new com.sforce.ws.bind.TypeInfo(
          "http://www.force.com/2009/06/asyncapi/dataload", "numberRecordsFailed",
            "http://www.w3.org/2001/XMLSchema", "int", 0, 1, true);

  private boolean numberRecordsFailed__is_set = false;

  private int numberRecordsFailed;

  public int getNumberRecordsFailed() {
      return numberRecordsFailed;
  }

  public void setNumberRecordsFailed(int numberRecordsFailed) {
      this.numberRecordsFailed = numberRecordsFailed;
      numberRecordsFailed__is_set = true;
  }

  /**
   * element : totalProcessingTime of type {http://www.w3.org/2001/XMLSchema}long java type: long
   */
  private static final com.sforce.ws.bind.TypeInfo totalProcessingTime__typeInfo = new com.sforce.ws.bind.TypeInfo(
          "http://www.force.com/2009/06/asyncapi/dataload", "totalProcessingTime",
            "http://www.w3.org/2001/XMLSchema", "long", 0, 1, true);

  private boolean totalProcessingTime__is_set = false;

  private long totalProcessingTime;

  public long getTotalProcessingTime() {
      return totalProcessingTime;
  }

  public void setTotalProcessingTime(long totalProcessingTime) {
      this.totalProcessingTime = totalProcessingTime;
      totalProcessingTime__is_set = true;
  }

  /**
   * element : apiActiveProcessingTime of type {http://www.w3.org/2001/XMLSchema}long java type: long
   */
  private static final com.sforce.ws.bind.TypeInfo apiActiveProcessingTime__typeInfo = new com.sforce.ws.bind.TypeInfo(
          "http://www.force.com/2009/06/asyncapi/dataload", "apiActiveProcessingTime",
          "http://www.w3.org/2001/XMLSchema",
 "long", 0, 1, true);

  private boolean apiActiveProcessingTime__is_set = false;

  private long apiActiveProcessingTime;

  public long getApiActiveProcessingTime() {
      return apiActiveProcessingTime;
  }

  public void setApiActiveProcessingTime(long apiActiveProcessingTime) {
      this.apiActiveProcessingTime = apiActiveProcessingTime;
      apiActiveProcessingTime__is_set = true;
  }

  /**
   * element : apexProcessingTime of type {http://www.w3.org/2001/XMLSchema}long java type: long
   */
  private static final com.sforce.ws.bind.TypeInfo apexProcessingTime__typeInfo = new com.sforce.ws.bind.TypeInfo(
          "http://www.force.com/2009/06/asyncapi/dataload", "apexProcessingTime", "http://www.w3.org/2001/XMLSchema",
            "long", 0, 1, true);

  private boolean apexProcessingTime__is_set = false;

  private long apexProcessingTime;

  public long getApexProcessingTime() {
      return apexProcessingTime;
  }

  public void setApexProcessingTime(long apexProcessingTime) {
      this.apexProcessingTime = apexProcessingTime;
      apexProcessingTime__is_set = true;
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
   
    __typeMapper.writeString(__out, id__typeInfo, id, id__is_set);
    __typeMapper.writeString(__out, jobId__typeInfo, jobId, jobId__is_set);
    __typeMapper.writeObject(__out, state__typeInfo, state, state__is_set);
    __typeMapper.writeString(__out, stateMessage__typeInfo, stateMessage, stateMessage__is_set);
    __typeMapper.writeObject(__out, createdDate__typeInfo, createdDate, createdDate__is_set);
    __typeMapper.writeObject(__out, systemModstamp__typeInfo, systemModstamp, systemModstamp__is_set);
    __typeMapper.writeInt(__out, numberRecordsProcessed__typeInfo, numberRecordsProcessed, numberRecordsProcessed__is_set);
    __typeMapper.writeInt(__out, numberRecordsFailed__typeInfo, numberRecordsFailed, numberRecordsFailed__is_set);
    __typeMapper.writeLong(__out, totalProcessingTime__typeInfo, totalProcessingTime, totalProcessingTime__is_set);
    __typeMapper.writeLong(__out, apiActiveProcessingTime__typeInfo, apiActiveProcessingTime, apiActiveProcessingTime__is_set);
    __typeMapper.writeLong(__out, apexProcessingTime__typeInfo, apexProcessingTime, apexProcessingTime__is_set);
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
    if (__typeMapper.isElement(__in, id__typeInfo)) {
      setId(__typeMapper.readString(__in, id__typeInfo, java.lang.String.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, jobId__typeInfo)) {
      setJobId(__typeMapper.readString(__in, jobId__typeInfo, java.lang.String.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, state__typeInfo)) {
      setState((com.sforce.async.BatchStateEnum)__typeMapper.readObject(__in, state__typeInfo, com.sforce.async.BatchStateEnum.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, stateMessage__typeInfo)) {
      setStateMessage(__typeMapper.readString(__in, stateMessage__typeInfo, java.lang.String.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, createdDate__typeInfo)) {
      setCreatedDate((java.util.Calendar)__typeMapper.readObject(__in, createdDate__typeInfo, java.util.Calendar.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, systemModstamp__typeInfo)) {
      setSystemModstamp((java.util.Calendar)__typeMapper.readObject(__in, systemModstamp__typeInfo, java.util.Calendar.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, numberRecordsProcessed__typeInfo)) {
        setNumberRecordsProcessed(__typeMapper.readInt(__in, numberRecordsProcessed__typeInfo, int.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, numberRecordsFailed__typeInfo)) {
        setNumberRecordsFailed(__typeMapper.readInt(__in, numberRecordsFailed__typeInfo, int.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, totalProcessingTime__typeInfo)) {
        setTotalProcessingTime(__typeMapper.readLong(__in, totalProcessingTime__typeInfo, long.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, apiActiveProcessingTime__typeInfo)) {
        setApiActiveProcessingTime(__typeMapper.readLong(__in, apiActiveProcessingTime__typeInfo, long.class));
    }
    __in.peekTag();
    if (__typeMapper.isElement(__in, apexProcessingTime__typeInfo)) {
        setApexProcessingTime(__typeMapper.readLong(__in, apexProcessingTime__typeInfo, long.class));
    }
  }

  @Override
  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append("[BatchInfo ");
    
    sb.append(" id=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(id)).append("'\n");
    sb.append(" jobId=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(jobId)).append("'\n");
    sb.append(" state=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(state)).append("'\n");
    sb.append(" stateMessage=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(stateMessage)).append("'\n");
    sb.append(" createdDate=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(createdDate)).append("'\n");
    sb.append(" systemModstamp=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(systemModstamp)).append("'\n");
    sb.append(" numberRecordsProcessed=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberRecordsProcessed)).append("'\n");
    sb.append(" numberRecordsFailed=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberRecordsFailed)).append("'\n");
    sb.append(" totalProcessingTime=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(totalProcessingTime)).append("'\n");
    sb.append(" apiActiveProcessingTime=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(apiActiveProcessingTime)).append("'\n");
    sb.append(" apexProcessingTime=");
      sb.append("'").append(com.sforce.ws.util.Verbose.toString(apexProcessingTime)).append("'\n");
    sb.append("]\n");
    return sb.toString();
  }
}
