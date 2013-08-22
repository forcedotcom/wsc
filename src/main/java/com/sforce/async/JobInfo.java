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
 * Async Api JobInfo
 */
public class JobInfo implements com.sforce.ws.bind.XMLizable {

    /**
     * Constructor
     */
    public JobInfo() {
    }

    public JobInfo(Builder builder) {
        if (builder.object != null) setObject(builder.object);
        if (builder.operation != null) setOperation(builder.operation);
        if (builder.assignmentRuleId != null) setAssignmentRuleId(builder.assignmentRuleId);
        if (builder.concurrencyMode != null) setConcurrencyMode(builder.concurrencyMode);
        if (builder.contentType != null) setContentType(builder.contentType);
        if (builder.externalIdFieldName != null) setExternalIdFieldName(builder.externalIdFieldName);
        if (builder.state != null) setState(builder.state);
        if (builder.id != null) setId(builder.id);
        if (builder.fastPathEnabled != null) setFastPathEnabled(builder.fastPathEnabled);
    }

    /**
     * element  : id of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo id__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "id",
                    "http://www.w3.org/2001/XMLSchema", "string", 0, 1, true);

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
     * element  : operation of type {http://www.force.com/2009/06/asyncapi/dataload}OperationEnum
     * java type: com.sforce.async.OperationEnum
     */
    private static final com.sforce.ws.bind.TypeInfo operation__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "operation",
                    "http://www.force.com/2009/06/asyncapi/dataload", "OperationEnum", 0, 1, true);

    private boolean operation__is_set = false;

    private com.sforce.async.OperationEnum operation;

    public com.sforce.async.OperationEnum getOperation() {
        return operation;
    }


    public void setOperation(com.sforce.async.OperationEnum operation) {
        this.operation = operation;
        operation__is_set = true;
    }

    /**
     * element  : object of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo object__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "object",
                    "http://www.w3.org/2001/XMLSchema", "string", 0, 1, true);

    private boolean object__is_set = false;

    private java.lang.String object;

    public java.lang.String getObject() {
        return object;
    }


    public void setObject(java.lang.String object) {
        this.object = object;
        object__is_set = true;
    }

    /**
     * element  : createdById of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo createdById__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "createdById",
                    "http://www.w3.org/2001/XMLSchema", "string", 0, 1, true);

    private boolean createdById__is_set = false;

    private java.lang.String createdById;

    public java.lang.String getCreatedById() {
        return createdById;
    }


    public void setCreatedById(java.lang.String createdById) {
        this.createdById = createdById;
        createdById__is_set = true;
    }

    /**
     * element  : createdDate of type {http://www.w3.org/2001/XMLSchema}dateTime
     * java type: java.util.Calendar
     */
    private static final com.sforce.ws.bind.TypeInfo createdDate__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "createdDate",
                    "http://www.w3.org/2001/XMLSchema", "dateTime", 0, 1, true);

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
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "systemModstamp",
                    "http://www.w3.org/2001/XMLSchema", "dateTime", 0, 1, true);

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
     * element  : state of type {http://www.force.com/2009/06/asyncapi/dataload}JobStateEnum
     * java type: com.sforce.async.JobStateEnum
     */
    private static final com.sforce.ws.bind.TypeInfo state__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "state",
                    "http://www.force.com/2009/06/asyncapi/dataload", "JobStateEnum", 0, 1, true);

    private boolean state__is_set = false;

    private com.sforce.async.JobStateEnum state;

    public com.sforce.async.JobStateEnum getState() {
        return state;
    }


    public void setState(com.sforce.async.JobStateEnum state) {
        this.state = state;
        state__is_set = true;
    }
    
    /**
     * element  : externalIdFieldName of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo externalIdFieldName__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "externalIdFieldName",
                    "http://www.w3.org/2001/XMLSchema", "string", 0, 1, true);

    private boolean externalIdFieldName__is_set = false;

    private java.lang.String externalIdFieldName;

    public java.lang.String getExternalIdFieldName() {
        return externalIdFieldName;
    }


    public void setExternalIdFieldName(java.lang.String externalIdFieldName) {
        this.externalIdFieldName = externalIdFieldName;
        externalIdFieldName__is_set = true;
    }


    /**
     * element  : operation of type {http://www.force.com/2009/06/asyncapi/dataload}ConcurrencyMode
     * java type: String
     */
    private static final com.sforce.ws.bind.TypeInfo concurrencyMode__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "concurrencyMode",
                    "http://www.force.com/2009/06/asyncapi/dataload", "ConcurrencyMode", 0, 1, true);

    private boolean concurrencyMode__is_set = false;

    private ConcurrencyMode concurrencyMode;

    public ConcurrencyMode getConcurrencyMode() {
        return concurrencyMode;
    }

    public void setConcurrencyMode(ConcurrencyMode concurrencyMode) {
        this.concurrencyMode = concurrencyMode;
        concurrencyMode__is_set = true;
    }

    /**
     * element : fastPathEnabled of type {http://www.w3.org/2001/XMLSchema}boolean java type: boolean
     */
    private static final com.sforce.ws.bind.TypeInfo fastPathEnabled__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "fastPathEnabled",
                    "http://www.w3.org/2001/XMLSchema", "boolean", 0, 1, true);

    /**
     * fastPath is not currently supported 
     */
    private boolean fastPathEnabled__is_set = false;

    private boolean fastPathEnabled;

    public boolean getFastPathEnabled() {
        return fastPathEnabled;
    }

    public void setFastPathEnabled(boolean fastPathEnabled) {
        this.fastPathEnabled = fastPathEnabled;
        fastPathEnabled__is_set = true;
    }
    
    /**
     * element : numberBatchesQueued of type {http://www.w3.org/2001/XMLSchema}int java type: int
     */
    private static final com.sforce.ws.bind.TypeInfo numberBatchesQueued__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "numberBatchesQueued",
                    "http://www.w3.org/2001/XMLSchema", "int", 0, 1, true);

    private boolean numberBatchesQueued__is_set = false;

    private int numberBatchesQueued;

    public int getNumberBatchesQueued() {
        return numberBatchesQueued;
    }

    public void setNumberBatchesQueued(int numberBatchesQueued) {
        this.numberBatchesQueued = numberBatchesQueued;
        numberBatchesQueued__is_set = true;
    }

    /**
     * element : numberBatchesInProgress of type {http://www.w3.org/2001/XMLSchema}int java type: int
     */
    private static final com.sforce.ws.bind.TypeInfo numberBatchesInProgress__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "numberBatchesInProgress",
                    "http://www.w3.org/2001/XMLSchema", "int", 0, 1, true);

    private boolean numberBatchesInProgress__is_set = false;

    private int numberBatchesInProgress;

    public int getNumberBatchesInProgress() {
        return numberBatchesInProgress;
    }

    public void setNumberBatchesInProgress(int numberBatchesInProgress) {
        this.numberBatchesInProgress = numberBatchesInProgress;
        numberBatchesInProgress__is_set = true;
    }

    /**
     * element : numberBatchesCompleted of type {http://www.w3.org/2001/XMLSchema}int java type: int
     */
    private static final com.sforce.ws.bind.TypeInfo numberBatchesCompleted__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "numberBatchesCompleted",
                    "http://www.w3.org/2001/XMLSchema", "int", 0, 1, true);

    private boolean numberBatchesCompleted__is_set = false;

    private int numberBatchesCompleted;

    public int getNumberBatchesCompleted() {
        return numberBatchesCompleted;
    }

    public void setNumberBatchesCompleted(int numberBatchesCompleted) {
        this.numberBatchesCompleted = numberBatchesCompleted;
        numberBatchesCompleted__is_set = true;
    }

    /**
     * element : numberBatchesFailed of type {http://www.w3.org/2001/XMLSchema}int java type: int
     */
    private static final com.sforce.ws.bind.TypeInfo numberBatchesFailed__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "numberBatchesFailed",
                    "http://www.w3.org/2001/XMLSchema", "int", 0, 1, true);

    private boolean numberBatchesFailed__is_set = false;

    private int numberBatchesFailed;

    public int getNumberBatchesFailed() {
        return numberBatchesFailed;
    }

    public void setNumberBatchesFailed(int numberBatchesFailed) {
        this.numberBatchesFailed = numberBatchesFailed;
        numberBatchesFailed__is_set = true;
    }

    /**
     * element : numberBatchesTotal of type {http://www.w3.org/2001/XMLSchema}int java type: int
     */
    private static final com.sforce.ws.bind.TypeInfo numberBatchesTotal__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "numberBatchesTotal",
                    "http://www.w3.org/2001/XMLSchema", "int", 0, 1, true);

    private boolean numberBatchesTotal__is_set = false;

    private int numberBatchesTotal;

    public int getNumberBatchesTotal() {
        return numberBatchesTotal;
    }

    public void setNumberBatchesTotal(int numberBatchesTotal) {
        this.numberBatchesTotal = numberBatchesTotal;
        numberBatchesTotal__is_set = true;
    }

    /**
     * element : numberRecordsProcessed of type {http://www.w3.org/2001/XMLSchema}int java type: int
     */
    private static final com.sforce.ws.bind.TypeInfo numberRecordsProcessed__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "numberRecordsProcessed",
                    "http://www.w3.org/2001/XMLSchema", "int", 0, 1, true);

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
     * element : numberRetries of type {http://www.w3.org/2001/XMLSchema}int java type: int
     */
    private static final com.sforce.ws.bind.TypeInfo numberRetries__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "numberRetries",
                    "http://www.w3.org/2001/XMLSchema", "int", 0, 1, true);

    private boolean numberRetries__is_set = false;

    private int numberRetries;

    public int getNumberRetries() {
        return numberRetries;
    }

    public void setNumberRetries(int numberRetries) {
        this.numberRetries = numberRetries;
        numberRetries__is_set = true;
    }

    /**
     * element  : operation of type {http://www.force.com/2009/06/asyncapi/dataload}ContentType
     * java type: String
     */
    private static final com.sforce.ws.bind.TypeInfo contentType__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "contentType",
                    "http://www.force.com/2009/06/asyncapi/dataload", "ContentType", 0, 1, true);

    private boolean contentType__is_set = false;

    private ContentType contentType;

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
        contentType__is_set = true;
    }

    /**
     * element  : operation of type {http://www.force.com/2009/06/asyncapi/dataload}ApiVersion
     * java type: double
     */
    private static final com.sforce.ws.bind.TypeInfo apiVersion__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "apiVersion",
                    "http://www.force.com/2009/06/asyncapi/dataload", "apiVersion", 0, 1, true);

    private boolean apiVersion__is_set = false;

    private double apiVersion;

    public double getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(double apiVersion) {
        this.apiVersion = apiVersion;
        apiVersion__is_set = true;
    }


    /**
     * element  : assignmentRuleId of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo assignmentRuleId__typeInfo =
            new com.sforce.ws.bind.TypeInfo("http://www.force.com/2009/06/asyncapi/dataload", "assignmentRuleId",
                    "http://www.w3.org/2001/XMLSchema", "string", 0, 1, true);

    private boolean assignmentRuleId__is_set = false;

    private java.lang.String assignmentRuleId;

    public java.lang.String getAssignmentRuleId() {
        return assignmentRuleId;
    }

    public void setAssignmentRuleId(java.lang.String assignmentRuleId) {
        this.assignmentRuleId = assignmentRuleId;
        assignmentRuleId__is_set = true;
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
    public void write(javax.xml.namespace.QName __element, com.sforce.ws.parser.XmlOutputStream __out,
            com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException {
        __out.writeStartTag(__element.getNamespaceURI(), __element.getLocalPart());

        writeFields(__out, __typeMapper);
        __out.writeEndTag(__element.getNamespaceURI(), __element.getLocalPart());
    }

    protected void writeFields(com.sforce.ws.parser.XmlOutputStream __out, com.sforce.ws.bind.TypeMapper __typeMapper)
            throws java.io.IOException {

        __typeMapper.writeString(__out, id__typeInfo, id, id__is_set);
        __typeMapper.writeObject(__out, operation__typeInfo, operation, operation__is_set);
        __typeMapper.writeString(__out, object__typeInfo, object, object__is_set);
        __typeMapper.writeString(__out, createdById__typeInfo, createdById, createdById__is_set);
        __typeMapper.writeObject(__out, createdDate__typeInfo, createdDate, createdDate__is_set);
        __typeMapper.writeObject(__out, systemModstamp__typeInfo, systemModstamp, systemModstamp__is_set);
        __typeMapper.writeObject(__out, state__typeInfo, state, state__is_set);
        __typeMapper
                .writeObject(__out, externalIdFieldName__typeInfo, externalIdFieldName, externalIdFieldName__is_set);
        __typeMapper.writeObject(__out, concurrencyMode__typeInfo, concurrencyMode, concurrencyMode__is_set);
        __typeMapper.writeObject(__out, contentType__typeInfo, contentType, contentType__is_set);
        __typeMapper.writeInt(__out, numberBatchesQueued__typeInfo, numberBatchesQueued, numberBatchesQueued__is_set);
        __typeMapper.writeInt(__out, numberBatchesInProgress__typeInfo, numberBatchesInProgress,
                numberBatchesInProgress__is_set);
        __typeMapper.writeInt(__out, numberBatchesCompleted__typeInfo, numberBatchesCompleted,
                numberBatchesCompleted__is_set);
        __typeMapper.writeInt(__out, numberBatchesFailed__typeInfo, numberBatchesFailed, numberBatchesFailed__is_set);
        __typeMapper.writeInt(__out, numberBatchesTotal__typeInfo, numberBatchesTotal, numberBatchesTotal__is_set);
        __typeMapper.writeInt(__out, numberRecordsProcessed__typeInfo, numberRecordsProcessed,
                numberRecordsProcessed__is_set);
        __typeMapper.writeInt(__out, numberRetries__typeInfo, numberRetries, numberRetries__is_set);
        __typeMapper.writeDouble(__out, apiVersion__typeInfo, apiVersion, apiVersion__is_set);
        __typeMapper.writeString(__out, assignmentRuleId__typeInfo, assignmentRuleId, assignmentRuleId__is_set);
        __typeMapper.writeInt(__out, numberRecordsFailed__typeInfo, numberRecordsFailed, numberRecordsFailed__is_set);
        __typeMapper.writeLong(__out, totalProcessingTime__typeInfo, totalProcessingTime, totalProcessingTime__is_set);
        __typeMapper.writeLong(__out, apiActiveProcessingTime__typeInfo, apiActiveProcessingTime,
                apiActiveProcessingTime__is_set);
        __typeMapper.writeLong(__out, apexProcessingTime__typeInfo, apexProcessingTime, apexProcessingTime__is_set);
        __typeMapper.writeBoolean(__out, fastPathEnabled__typeInfo, fastPathEnabled, fastPathEnabled__is_set);
    }


    @Override
    public void load(com.sforce.ws.parser.XmlInputStream __in, com.sforce.ws.bind.TypeMapper __typeMapper)
            throws java.io.IOException, com.sforce.ws.ConnectionException {
        __typeMapper.consumeStartTag(__in);
        loadFields(__in, __typeMapper);
        __typeMapper.consumeEndTag(__in);
    }

    protected void loadFields(com.sforce.ws.parser.XmlInputStream __in, com.sforce.ws.bind.TypeMapper __typeMapper)
            throws java.io.IOException, com.sforce.ws.ConnectionException {

        __in.peekTag();
        if (__typeMapper.isElement(__in, id__typeInfo)) {
            setId(__typeMapper.readString(__in, id__typeInfo, String.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, operation__typeInfo)) {
            setOperation((com.sforce.async.OperationEnum) __typeMapper
                    .readObject(__in, operation__typeInfo, com.sforce.async.OperationEnum.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, object__typeInfo)) {
            setObject(__typeMapper.readString(__in, object__typeInfo, String.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, createdById__typeInfo)) {
            setCreatedById(__typeMapper.readString(__in, createdById__typeInfo, String.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, createdDate__typeInfo)) {
            setCreatedDate((java.util.Calendar) __typeMapper
                    .readObject(__in, createdDate__typeInfo, java.util.Calendar.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, systemModstamp__typeInfo)) {
            setSystemModstamp((java.util.Calendar) __typeMapper
                    .readObject(__in, systemModstamp__typeInfo, java.util.Calendar.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, state__typeInfo)) {
            setState((com.sforce.async.JobStateEnum) __typeMapper
                    .readObject(__in, state__typeInfo, com.sforce.async.JobStateEnum.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, externalIdFieldName__typeInfo)) {
            setExternalIdFieldName(__typeMapper.readString(__in, externalIdFieldName__typeInfo, String.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, concurrencyMode__typeInfo)) {
            setConcurrencyMode(
                    (ConcurrencyMode) __typeMapper.readObject(__in, concurrencyMode__typeInfo, ConcurrencyMode.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, contentType__typeInfo)) {
            setContentType((ContentType) __typeMapper.readObject(__in, contentType__typeInfo, ContentType.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, numberBatchesQueued__typeInfo)) {
            setNumberBatchesQueued(__typeMapper.readInt(__in, numberBatchesQueued__typeInfo, int.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, numberBatchesInProgress__typeInfo)) {
            setNumberBatchesInProgress(__typeMapper.readInt(__in, numberBatchesInProgress__typeInfo, int.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, numberBatchesCompleted__typeInfo)) {
            setNumberBatchesCompleted(__typeMapper.readInt(__in, numberBatchesCompleted__typeInfo, int.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, numberBatchesFailed__typeInfo)) {
            setNumberBatchesFailed(__typeMapper.readInt(__in, numberBatchesFailed__typeInfo, int.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, numberBatchesTotal__typeInfo)) {
            setNumberBatchesTotal(__typeMapper.readInt(__in, numberBatchesTotal__typeInfo, int.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, numberRecordsProcessed__typeInfo)) {
            setNumberRecordsProcessed(__typeMapper.readInt(__in, numberRecordsProcessed__typeInfo, int.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, numberRetries__typeInfo)) {
            setNumberRetries(__typeMapper.readInt(__in, numberRetries__typeInfo, int.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, apiVersion__typeInfo)) {
            setApiVersion(__typeMapper.readDouble(__in, apiVersion__typeInfo, double.class));
        }

        __in.peekTag();
        if (__typeMapper.isElement(__in, assignmentRuleId__typeInfo)) {
            setAssignmentRuleId(__typeMapper.readString(__in, assignmentRuleId__typeInfo, String.class));
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
        
        __in.peekTag();
        if (__typeMapper.isElement(__in, fastPathEnabled__typeInfo)) {
            setFastPathEnabled(__typeMapper.readBoolean(__in, fastPathEnabled__typeInfo, boolean.class));
        }
    }

    @Override
    public String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("[JobInfo ");

        sb.append(" id=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(id)).append("'\n");
        sb.append(" operation=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(operation)).append("'\n");
        sb.append(" object=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(object)).append("'\n");
        sb.append(" createdById=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(createdById)).append("'\n");
        sb.append(" createdDate=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(createdDate)).append("'\n");
        sb.append(" systemModstamp=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(systemModstamp)).append("'\n");
        sb.append(" state=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(state)).append("'\n");
        sb.append(" externalIdFieldName=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(externalIdFieldName)).append("'\n");
        sb.append(" concurrencyMode=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(concurrencyMode)).append("'\n");
        sb.append(" contentType=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(contentType)).append("'\n");
        sb.append(" numberBatchesQueued=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberBatchesQueued)).append("'\n");
        sb.append(" numberBatchesInProgress=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberBatchesInProgress)).append("'\n");
        sb.append(" numberBatchesCompleted=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberBatchesCompleted)).append("'\n");
        sb.append(" numberBatchesFailed=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberBatchesFailed)).append("'\n");
        sb.append(" numberBatchesTotal=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberBatchesTotal)).append("'\n");
        sb.append(" numberRecordsProcessed=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberRecordsProcessed)).append("'\n");
        sb.append(" numberRetries=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberRetries)).append("'\n");
        sb.append(" apiVersion=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(apiVersion)).append("'\n");
        sb.append(" assignmentRuleId=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(assignmentRuleId)).append("'\n");
        sb.append(" numberRecordsFailed=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(numberRecordsFailed)).append("'\n");
        sb.append(" totalProcessingTime=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(totalProcessingTime)).append("'\n");
        sb.append(" apiActiveProcessingTime=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(apiActiveProcessingTime)).append("'\n");
        sb.append(" apexProcessingTime=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(apexProcessingTime)).append("'\n");
        sb.append(" fastPathEnabled=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(fastPathEnabled)).append("'\n");
        sb.append("]\n");
        return sb.toString();
    }

    public static class Builder {

        // optional fields
        private String object;
        private OperationEnum operation;
        private String assignmentRuleId;
        private ConcurrencyMode concurrencyMode;
        private ContentType contentType;
        private String externalIdFieldName;
        private JobStateEnum state;
        private String id;
        private Boolean fastPathEnabled;

        public Builder object(String object) {
            this.object = object;
            return this;
        }

        public Builder operation(OperationEnum operation) {
            this.operation = operation;
            return this;
        }

        public Builder assignmentRuleId(String assignmentRuleId) {
            this.assignmentRuleId = assignmentRuleId;
            return this;
        }

        public Builder concurrencyMode(ConcurrencyMode concurrencyMode) {
            this.concurrencyMode = concurrencyMode;
            return this;
        }

        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder externalIdFieldName(String externalIdFieldName) {
            this.externalIdFieldName = externalIdFieldName;
            return this;
        }

        public Builder state(JobStateEnum jobState) {
            this.state = jobState;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder fastPathEnabled(boolean fastPathEnabled) {
            this.fastPathEnabled = fastPathEnabled;
            return this;
        }

    }

}
