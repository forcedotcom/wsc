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

import com.sforce.ws.codegen.ConnectionMetadataConstructor;

import java.util.List;

/**
 * @author btoal 
 * @author hhildebrand
 * @author sweiss
 * @since 184
 */
public class OperationMetadata {
    private final String returnType;
    private final String operationName;
    private final String requestType;
    private final String responseType;
    private final String soapAction;
    private final String requestName;
    private final String responseName;
    private final String resultCall;
    private final List<ElementMetadata> elements;
    private final List<HeaderMetadata> headers;
    private final String returnTypeInterface;

    // SObject sObject, String s
    private final String argsWithClasses;
    // sObject, s
    private final String argsWithoutTypes;
    // ISObject sObject, String s
    private final String argsWithInterfaces;
    // (SObject)sObject, s
    private final String argsWithCasts;


    public OperationMetadata(final String returnType, final String operationName, final String requestType,
                             final String responseType, final String argsWithClasses, String argsWithoutTypes, String argsWithInterfaces, String argsWithCasts, final String soapAction, final String requestName, final String responseName, final String resultCall, final List<ElementMetadata> elements, final List<HeaderMetadata> headers, boolean isReturnTypeComplexType, String returnTypeInterface) {
        this.returnType = returnType;
        this.operationName = operationName;
        this.requestType = requestType;
        this.responseType = responseType;
        this.argsWithClasses = argsWithClasses;
        this.argsWithoutTypes = argsWithoutTypes;
        this.argsWithInterfaces = argsWithInterfaces;
        this.argsWithCasts = argsWithCasts;
        this.soapAction = soapAction;
        this.requestName = requestName;
        this.responseName = responseName;
        this.resultCall = resultCall;
        this.elements = elements;
        this.headers = headers;
        this.returnTypeInterface = returnTypeInterface;
    }

    public static OperationMetadata newInstance(final String returnType, final String operationName, final String requestType,
                                                final String responseType, final String argsWithClasses, String argsWithoutTypes, String argsWithInterfaces, String argsWithCasts, final String soapAction, final String requestName, final String responseName, final String resultCall, final List<ElementMetadata> elements, final List<HeaderMetadata> headers, boolean isReturnTypeComplexType, String returnTypeInterface) {
        return new OperationMetadata(returnType, operationName, requestType, responseType, argsWithClasses, argsWithoutTypes, argsWithInterfaces, argsWithCasts, soapAction, requestName, responseName, resultCall, elements, headers, isReturnTypeComplexType, returnTypeInterface);
    }

    public String getReturnType() {
        return returnType;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getArgsWithClasses() {
        return argsWithClasses;
    }

    public String getArgsWithInterfaces() {
        return argsWithInterfaces;
    }

    public String getArgsWithCasts() {
        return argsWithCasts;
    }

    public List<ElementMetadata> getElements() {
        return elements;
    }
    
    public List<HeaderMetadata> getHeaders() {
        return headers;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public String getRequestName() {
        return requestName;
    }

    public String getResponseName() {
        return responseName;
    }

    public String getResultCall() {
        return resultCall;
    }

    public String getArgsWithoutTypes() {
        return argsWithoutTypes;
    }

    public boolean getHasReturnType() {
        return !getReturnType().equals("void");
    }

    public String getReturnTypeInterface() {
        return returnTypeInterface;
    }
}