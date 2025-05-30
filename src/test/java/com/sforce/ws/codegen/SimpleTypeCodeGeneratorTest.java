/*
 * Copyright (c) 2017, salesforce.com, inc.
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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import com.sforce.ws.codegen.metadata.SimpleClassMetadata;
import com.sforce.ws.codegen.metadata.SimpleClassMetadata.EnumAndValue;
import com.sforce.ws.tools.wsdlc;

public class SimpleTypeCodeGeneratorTest extends TestCase {

    public void testGenerateSimpleTypeSource() throws Exception {
        String expectedSource = CodeGeneratorTestUtil.fileToString("SimpleTypeEmailSyncMatchPreference.java");

        ST template = CodeGeneratorTestUtil.getTemplateDefinitions(Generator.SIMPLE_TYPE);
        template.add("gen", new SimpleClassMetadata("com.sforce.soap.partner", "SimpleTypeEmailSyncMatchPreference", getExpectedEnums(), false));

        assertEquals(expectedSource, CodeGeneratorTestUtil.getRenderedStringWithReplacements(template));
    }

    public void testGenerateSimpleTypeWithDeprecatedAnnotation() throws Exception {
        String expectedSource = CodeGeneratorTestUtil.fileToString("SimpleTypeEmailSyncMatchPreferenceDep.java");

        ST template = CodeGeneratorTestUtil.getTemplateDefinitions(Generator.SIMPLE_TYPE);
        template.add("gen", new SimpleClassMetadata("com.sforce.soap.partner", "SimpleTypeEmailSyncMatchPreferenceDep", getExpectedEnums(), true));

        assertEquals(expectedSource, CodeGeneratorTestUtil.getRenderedStringWithReplacements(template));
    }

    private List<EnumAndValue> getExpectedEnums() {
        List<EnumAndValue> enumEntries = new ArrayList<EnumAndValue>();
        enumEntries.add(new EnumAndValue("LastModified", "LastModified"));
        enumEntries.add(new EnumAndValue("LastActivity", "LastActivity"));
        enumEntries.add(new EnumAndValue("Oldest", "Oldest"));
        enumEntries.add(new EnumAndValue("UTF_8", "UTF-8"));
        return enumEntries;
    }
}
