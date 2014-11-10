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

import java.util.List;

/**
 * @author btoal
 * @author hhildebrand
 * @since 184
 */
public class ComplexClassMetadata extends ClassMetadata {
    private final String xsiType;
    private final String superWrite;
    private final String superLoad;
    private final String superToString;
    private final List<MemberMetadata> memberMetadataList;
    private final boolean generateInterfaces;
    private final String typeExtension;
    private final String baseComplexClass;

    public ComplexClassMetadata(String packageName, String className, String typeExtension, String xsiType,
                                String superWrite, String superLoad, String superToString, List<MemberMetadata> memberMetadataList,
                                boolean generateInterfaces, String interfacePackageName, String baseComplexClass) {
        super(packageName, className, interfacePackageName);
        this.typeExtension = typeExtension;
        this.xsiType = xsiType;
        this.superWrite = superWrite;
        this.superLoad = superLoad;
        this.superToString = superToString;
        this.memberMetadataList = memberMetadataList;
        this.generateInterfaces = generateInterfaces;
        this.baseComplexClass = baseComplexClass;
    }

    public String getXsiType() {
        return this.xsiType;
    }

    public String getSuperWrite() {
        return this.superWrite;
    }

    public String getSuperLoad() {
        return this.superLoad;
    }

    public String getSuperToString() {
        return this.superToString;
    }

    public String getTypeExtension() {
        return typeExtension;
    }

    public List<MemberMetadata> getMemberMetadataList() {
        return this.memberMetadataList;
    }

    public boolean getGenerateInterfaces() {
        return generateInterfaces;
    }

    public String getInterfaceExtension() {
        if (!generateInterfaces) {
            return "";
        }
        String typeExt = getTypeExtension().toLowerCase();
        if (typeExt.contains("implements")) {
            return ", " + getInterfaceName();
        } else {
            return "implements " + getInterfaceName();
        }
    }
    
    public boolean getHasBaseComplexClass() {
    	return baseComplexClass != null;
    }
    
    public String getBaseComplexClass() {
    	return baseComplexClass;
    }
    
    public String getBaseComplexClassInterface() {
    	if (baseComplexClass == null) {
    		return null;
    	}
    	int position = baseComplexClass.lastIndexOf(".");
    	return baseComplexClass.substring(0, position + 1) + "I" + baseComplexClass.substring(position + 1);
    }
    
    public boolean getHasArrayField() {
    	for (MemberMetadata m : this.memberMetadataList) {
    		if (m.getIsArray()) {
    			return true;
    		}
    	}
    	return false;
    }
}