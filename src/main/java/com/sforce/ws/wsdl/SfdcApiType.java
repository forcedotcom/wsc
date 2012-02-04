package com.sforce.ws.wsdl;


public enum SfdcApiType {
    Enterprise(true, Constants.ENTERPRISE_NS, Constants.ENTERPRISE_SOBJECT_NS, "verifyEnterpriseEndpoint"),
    Partner(true, Constants.PARTNER_NS, Constants.PARTNER_SOBJECT_NS, "verifyPartnerEndpoint"),
    Metadata(false, Constants.META_SFORCE_NS),
    CrossInstance(false, Constants.CROSS_INSTANCE_SFORCE_NS, Constants.CROSS_INSTANCE_SFORCE_NS, null),
    Internal(false, Constants.INTERNAL_SFORCE_NS, Constants.INTERNAL_SFORCE_NS, null),
    ClientSync(false, Constants.CLIENT_SYNC_SFORCE_NS, Constants.CLIENT_SYNC_SFORCE_NS, null),
    SyncApi(false, Constants.SYNC_API_SFORCE_NS, Constants.SYNC_API_SFORCE_NS, null);


    private SfdcApiType(boolean hasLoginCall, String namespace) {
        this(hasLoginCall, namespace, null, null);
    }
    private SfdcApiType(boolean hasLoginCall, String namespace, String sobjectNamespace, String verifyEndpoint) {
        this.hasLoginCall = hasLoginCall;
        this.namespace = namespace;
        this.sobjectNamespace = sobjectNamespace;
        this.verifyEndpoint = verifyEndpoint;
    }
    boolean hasLoginCall;
    String namespace;
    String sobjectNamespace;
    String verifyEndpoint;

    public boolean hasLoginCall() {return this.hasLoginCall;}
    public String getNamespace() {return this.namespace;}
    public String getSobjectNamespace() {return this.sobjectNamespace;}
    public String getVerifyEndpoint() {return this.verifyEndpoint;}

    public static SfdcApiType getFromNamespace(String namespace) {
        for (SfdcApiType type: values()) {
            if ( type.getNamespace().equals(namespace)) return type;
        }
        return null;
    }

    public static SfdcApiType getFromSobjectNamespace(String namespace) {
        for (SfdcApiType type: values()) {
            if ( type.getSobjectNamespace() != null && type.getSobjectNamespace().equals(namespace)) return type;
        }
        return null;
    }

}
