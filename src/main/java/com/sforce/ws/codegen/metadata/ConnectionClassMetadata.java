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
package com.sforce.ws.codegen.metadata;

import java.util.List;

/**
 * @author btoal 
 * @author hhildebrand
 * @since 184
 */
public class ConnectionClassMetadata extends ClassMetadata {
    private final String packagePrefix;
    private final boolean hasLoginCall;
    private final String verifyEndpoint;
    private final String loginResult;
    private final boolean hasSessionHeader;
    private final String sobjectNamespace;
    private final String qNames;
    private final String knownHeaders;
    private final List<HeaderMetadata> headersMetadata;
    private final List<OperationMetadata> operations;

    public static ConnectionClassMetadata newInstance(final String packagePrefix, final String packageName,
            final String className, final boolean hasLoginCall, final String loginResult, final String verifyEndpoint,
            final boolean hasSessionHeader, final String sobjectNamespace, final String qNames, final String knownHeaders, final List<HeaderMetadata> headersMetadata,
            final List<OperationMetadata> operations) {
        return new ConnectionClassMetadata(packagePrefix, packageName, className, hasLoginCall, verifyEndpoint,
                loginResult, hasSessionHeader, sobjectNamespace, qNames, knownHeaders, headersMetadata, operations);
    }

    private ConnectionClassMetadata(final String packagePrefix, final String packageName, final String className,
            final boolean hasLoginCall, final String verifyEndpoint, final String loginResult, final boolean hasSessionHeader,
            final String sobjectNamespace, final String qNames, final String knownHeaders, final List<HeaderMetadata> headersMetadata, final List<OperationMetadata> operations) {
        super(packageName, className);
        this.packagePrefix = packagePrefix;
        this.hasLoginCall = hasLoginCall;
        this.verifyEndpoint = verifyEndpoint;
        this.loginResult = loginResult;
        this.hasSessionHeader = hasSessionHeader;
        this.sobjectNamespace = sobjectNamespace;
        this.qNames = qNames;
        this.knownHeaders = knownHeaders;
        this.headersMetadata = headersMetadata;
        this.operations = operations;
    }

    public List<HeaderMetadata> getHeadersMetadata() {
        return this.headersMetadata;
    }

    public String getPackagePrefix() {
        return packagePrefix;
    }

    public boolean getHasLoginCall() {
        return this.hasLoginCall;
    }

    public String getVerifyEndpoint() {
        return this.verifyEndpoint;
    }

    public String getLoginResult() {
        return loginResult;
    }

    public boolean getHasSessionHeader() {
        return hasSessionHeader;
    }

    public String getSObjectNamespace() {
        return sobjectNamespace;
    }

    public List<OperationMetadata> getOperations() {
        return operations;
    }

    public String getQNames() {
        return qNames;
    }

    public String getKnownHeaders() {
        return knownHeaders;
    }
}
