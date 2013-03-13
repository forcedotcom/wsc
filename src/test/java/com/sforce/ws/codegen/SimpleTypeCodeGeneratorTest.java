package com.sforce.ws.codegen;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import com.sforce.ws.codegen.metadata.SimpleClassMetadata;
import com.sforce.ws.tools.wsdlc;

public class SimpleTypeCodeGeneratorTest extends TestCase {

    public void testGenerateSimpleTypeSource() throws Exception {
        String expectedSource = CodeGeneratorTestUtil.fileToString("EmailSyncMatchPreference.java");

        List<String> enumEntries = new ArrayList<String>();
        enumEntries.add("LastModified");
        enumEntries.add("LastActivity");
        enumEntries.add("Oldest");

        STGroupDir templates = new STGroupDir(wsdlc.TEMPLATE_DIR, '$', '$');
        ST template = templates.getInstanceOf(Generator.SIMPLE_TYPE);
        assertNotNull(template);
        template.add("gen", new SimpleClassMetadata("com.sforce.soap.partner", "EmailSyncMatchPreference", enumEntries));
        assertEquals(expectedSource, template.render());
    }
}