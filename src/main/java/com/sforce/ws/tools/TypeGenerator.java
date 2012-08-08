/*
 * Copyright (c) 2005, salesforce.com, inc.
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
package com.sforce.ws.tools;

import java.io.File;
import java.io.IOException;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.template.Template;
import com.sforce.ws.template.TemplateException;
import com.sforce.ws.util.FileUtil;
import com.sforce.ws.wsdl.Schema;
import com.sforce.ws.wsdl.Types;

/**
 * TypeGenerator
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Mar 7, 2006
 */
public abstract class TypeGenerator {
    protected String packageName;
    protected String className;
    protected File tempDir;
    protected TypeMapper mapper;
    protected Types types;

    public TypeGenerator(Types types, Schema schema, String name, File tempDir, TypeMapper typeMapper) {
        packageName = NameMapper.getPackageName(schema.getTargetNamespace(), typeMapper.getPackagePrefix());
        className = NameMapper.getClassName(name);
        this.types = types;
        this.mapper = typeMapper;
        this.tempDir = tempDir;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    protected abstract String getTemplate();

    public String generate() throws IOException, TemplateException {
        File dir = FileUtil.mkdirs(packageName, tempDir);
        Template template = new Template();
        template.setProperty("gen", this);
        File javaFile = new File(dir, className + ".java");
        template.exec(getTemplate(), javaFile.getAbsolutePath());
        return javaFile.getAbsolutePath();
    }
}
