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

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.wsdl.*;

/**
 * @author hhildebrand
 * @since 184
 */
public class SimpleClassMetadata extends ClassMetadata {
	
	public static class EnumAndValue {
		public String e;
		public String v;
		public EnumAndValue(String e, String v) {
			this.e = e;
			this.v = v;
		}
	}
	
    private static final Pattern DASH_PATTERN = Pattern.compile("-");
    
    public static Collection<EnumAndValue> getEnumsAndValues(SimpleType simpleType, TypeMapper typeMapper) {
    	Collection<EnumAndValue> enumsAndValues = new ArrayList<EnumAndValue>();
    	for (Enumeration e : simpleType.getRestriction()) {
    		enumsAndValues.add(new EnumAndValue(javaName(e, typeMapper), e.getValue()));
    	}
    	return enumsAndValues;
    }

    public static String javaName(Enumeration enumeration, TypeMapper typeMapper) {
        String name = enumeration.getValue();
        int index = name.indexOf(":");
        String subname = index == -1 ? name : name.substring(index + 1);
        if (typeMapper.isKeyWord(subname)) {
            subname = "_" + subname;
        }
        if (subname.indexOf("-") > 0) {
            subname = DASH_PATTERN.matcher(subname).replaceAll("_");
        }
        return subname;
    }

    private final Collection<EnumAndValue> enumsAndValues;

    public SimpleClassMetadata(Schema schema, SimpleType simpleType, TypeMapper typeMapper) {
        this(NameMapper.getPackageName(schema.getTargetNamespace(), typeMapper.getPackagePrefix()), NameMapper
                .getClassName(simpleType.getName()), getEnumsAndValues(simpleType, typeMapper));
    }

    public SimpleClassMetadata(String packageName, String className, Collection<EnumAndValue> enumsAndValues) {
        super(packageName, className);
        this.enumsAndValues = enumsAndValues;
    }
    
    public Collection<EnumAndValue> getEnumsAndValues() {
    	return enumsAndValues;
    }
}
