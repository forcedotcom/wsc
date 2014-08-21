/*
 * Copyright (c) 2013, salesforce.com, inc. All rights reserved. Redistribution and use in source and binary forms, with
 * or without modification, are permitted provided that the following conditions are met: Redistributions of source code
 * must retain the above copyright notice, this list of conditions and the following disclaimer. Redistributions in
 * binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution. Neither the name of salesforce.com, inc. nor the
 * names of its contributors may be used to endorse or promote products derived from this software without specific
 * prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.sforce.ws.codegen.metadata;

/**
 * @author btoal
 * @author hhildebrand
 * @since 184
 */
public class MemberMetadata {
    private final String elementDoc;
    private final String javaType;
    private final String fieldName;
    private final String typeInfo;
    private final String arraySource;
    private final String getMethod;
    private final String boolMemberType;
    private final String boolMethod;
    private final String setMethod;
    private final String writeMethod;
    private final String loadType;
    private final String loadMethod;
    private final boolean isArray;

    // Either the interface corresponding to the javaType field if both interfaces are generated for the WSDL and
    // the javaType corresponds to a complex class
    private final String javaTypeInterface;

    public MemberMetadata(String elementDoc, String javaType, String fieldName, String typeInfo, String arraySource,
                          String getMethod, String boolMemberType, String boolMethod, String setMethod, String writeMethod,
                          String loadType, String loadMethod, String javaTypeInterface, boolean isArray) {
        this.elementDoc = elementDoc;
        this.javaType = javaType;
        this.fieldName = fieldName;
        this.typeInfo = typeInfo;
        this.arraySource = arraySource;
        this.getMethod = getMethod;
        this.boolMemberType = boolMemberType;
        this.boolMethod = boolMethod;
        this.setMethod = setMethod;
        this.writeMethod = writeMethod;
        this.loadType = loadType;
        this.loadMethod = loadMethod;
        this.javaTypeInterface = javaTypeInterface;
        this.isArray = isArray;
    }

    public static MemberMetadata newInstance(String elementDoc, String javaType, String fieldName, String typeInfo,
                                             String arraySource, String getMethodName, String boolMemberType, String boolMethodName,
                                             String setMethodName, String writeMethod, String loadType, String loadMethod, boolean isComplexType,
                                             String javaTypeInterface, boolean isArray) {
        return new MemberMetadata(elementDoc, javaType, fieldName, typeInfo, arraySource, getMethodName,
                boolMemberType, boolMethodName, setMethodName, writeMethod, loadType, loadMethod, javaTypeInterface, isArray);
    }

    public String getElementDoc() {
        return this.elementDoc;
    }

    public String getJavaType() {
        return this.javaType;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getTypeInfo() {
        return this.typeInfo;
    }

    public String getArraySource() {
        return this.arraySource;
    }

    public String getGetMethodName() {
        return this.getMethod;
    }

    public String getBoolMemberType() {
        return this.boolMemberType;
    }

    public String getBoolMethodName() {
        return this.boolMethod;
    }

    public String getSetMethodName() {
        return this.setMethod;
    }

    public String getWriteMethod() {
        return this.writeMethod;
    }

    public String getLoadMethod() {
        return loadMethod;
    }

    public String getLoadType() {
        return this.loadType;
    }

    public boolean getBooleanJavaType() {
        return "boolean".equals(javaType);
    }

    public String getCast() {
        if ("boolean".equals(javaType) || "java.lang.String".equals(javaType)) { return ""; }
        return String.format("(%s)", javaType);
    }

    public String getCastFromInterface() {
        if (javaType.equals(javaTypeInterface)) {
            return "";
        } else {
            return getCast();
        }
    }

    public String getJavaTypeInterface() {
        return javaTypeInterface;
    }
    
    public boolean getIsArray() {
    	return isArray;
    }
    
    public String getArrayCast() {
    	return String.format("castArray(%s.class, %s)", javaType.replace("[","").replace("]",""), this.fieldName);
    }
}