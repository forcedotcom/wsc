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

package com.sforce.ws.wsdl;


public enum SfdcApiType {
    Enterprise(true, Constants.ENTERPRISE_NS, Constants.ENTERPRISE_SOBJECT_NS, "verifyEnterpriseEndpoint"),
    Partner(true, Constants.PARTNER_NS, Constants.PARTNER_SOBJECT_NS, "verifyPartnerEndpoint"),
    Metadata(false, Constants.META_SFORCE_NS),
    CrossInstance(false, Constants.CROSS_INSTANCE_SFORCE_NS, Constants.CROSS_INSTANCE_SFORCE_NS, null),
    Internal(false, Constants.INTERNAL_SFORCE_NS, Constants.INTERNAL_SFORCE_NS, null),
    ClientSync(false, Constants.CLIENT_SYNC_SFORCE_NS, Constants.CLIENT_SYNC_SFORCE_NS, null),
    SyncApi(false, Constants.SYNC_API_SFORCE_NS, Constants.SYNC_API_SFORCE_NS, null),
    Tooling(true, Constants.TOOLING_NS, Constants.TOOLING_NS, "verifyToolingEndpoint");


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
