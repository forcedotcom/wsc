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

package com.sforce.ws.codegen;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.codegen.metadata.ClassMetadata;
import com.sforce.ws.codegen.metadata.FactoryClassMetadata;
import com.sforce.ws.wsdl.ComplexType;
import com.sforce.ws.wsdl.Definitions;
import com.sforce.ws.wsdl.Schema;
import com.sforce.ws.wsdl.Types;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sweiss
 * @since 192
 */
public class FactoryMetadataConstructor {

    protected final TypeMapper typeMapper;
    protected final String packageName;
    private final Definitions definitions;
    private final String packagePrefix;

    public FactoryMetadataConstructor(Definitions definitions, TypeMapper typeMapper, String packagePrefix) {
        this.definitions = definitions;
        this.typeMapper = typeMapper;
        this.packagePrefix = packagePrefix;
        this.packageName = NameMapper.getPackageName(definitions.getTargetNamespace(), packagePrefix);
    }

    public FactoryClassMetadata getFactoryClassMetadata() {
        List<ClassMetadata> classes = new ArrayList<ClassMetadata>();
        ClassMetadata sObjectClass = null;

        Types types = this.definitions.getTypes();

        for (Schema schema : types.getSchemas()) {
            String schemaPackageName = NameMapper.getPackageName(schema.getTargetNamespace(), packagePrefix);
            for (ComplexType ct : schema.getComplexTypes()) {
                ClassMetadata cm = new ClassMetadata(schemaPackageName, NameMapper.getClassName(ct.getName()), getInterfacePackageName(schemaPackageName));
                if (typeMapper.isSObject(ct.getSchema().getTargetNamespace(), ct.getName())) {
                    assert sObjectClass == null;
                    sObjectClass = cm;
                } else {
                    classes.add(cm);
                }
            }

        }

        String className = (definitions.getApiType() != null ? definitions.getApiType().name() : "Soap") + "ConnectionFactory";
        return new FactoryClassMetadata(packageName, className, classes, sObjectClass, getInterfacePackageName());
    }

    protected String getInterfacePackageName(String packageName) {
        return packageName;
    }

    private String getInterfacePackageName() {
        return getInterfacePackageName(packageName);
    }
}
