package com.sforce.ws.codegen;

import junit.framework.TestCase;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import com.sforce.ws.codegen.metadata.ClassMetadata;
import com.sforce.ws.tools.wsdlc;

public class AggregateCodeGeneratorTest extends TestCase {

    public void testGenerateSObjectSource() throws Exception {
        String expectedSource = CodeGeneratorTestUtil.fileToString("AggregateResult.java");
        STGroupDir templates = new STGroupDir(wsdlc.TEMPLATE_DIR, '$', '$');
        ST template = templates.getInstanceOf(Generator.AGGREGATE_RESULT);
        assertNotNull(template);
        template.add("gen", new ClassMetadata("com.sforce.soap.enterprise.sobject", null));
        assertEquals(expectedSource, template.render());
    }
}