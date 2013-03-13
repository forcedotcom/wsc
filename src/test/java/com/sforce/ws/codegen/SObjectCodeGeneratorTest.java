package com.sforce.ws.codegen;

import junit.framework.TestCase;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import com.sforce.ws.codegen.metadata.ClassMetadata;
import com.sforce.ws.tools.wsdlc;

public class SObjectCodeGeneratorTest extends TestCase {

    public void testGenerateSObjectSource() throws Exception {
        String expectedSource = CodeGeneratorTestUtil.fileToString("SObject.java");

        STGroupDir templates = new STGroupDir(wsdlc.TEMPLATE_DIR, '$', '$');
        ST template = templates.getInstanceOf(Generator.SOBJECT);
        assertNotNull(template);
        template.add("gen", new ClassMetadata("com.sforce.soap.partner.sobject", null));
        assertEquals(expectedSource, template.render());
    }
}