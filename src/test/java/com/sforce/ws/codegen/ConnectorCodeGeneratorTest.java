package com.sforce.ws.codegen;

import org.stringtemplate.v4.*;

import com.sforce.ws.codegen.metadata.ConnectorMetadata;
import com.sforce.ws.tools.wsdlc;

import junit.framework.TestCase;

public class ConnectorCodeGeneratorTest extends TestCase {

    public void testGenerateConnectorSource() throws Exception {
        String expectedSource = CodeGeneratorTestUtil.fileToString("Connector.java");

        String className = "EnterpriseConnection";
        String packageName = "com.sforce.soap.enterprise";
        String endpoint = "https://login.salesforce.com/services/Soap/c/16.0";

        STGroupDir templates = new STGroupDir(wsdlc.TEMPLATE_DIR, '$', '$');
        ST template = templates.getInstanceOf(wsdlc.CONNECTOR);
        assertNotNull(template);
        template.add("gen", new ConnectorMetadata(packageName, className, endpoint));
        assertEquals(expectedSource, template.render());
    }
}