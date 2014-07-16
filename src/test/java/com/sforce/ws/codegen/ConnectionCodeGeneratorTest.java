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

import java.util.ArrayList;
import java.util.List;

import org.stringtemplate.v4.*;

import junit.framework.TestCase;

import com.sforce.ws.codegen.metadata.*;
import com.sforce.ws.codegen.metadata.HeaderMetadata.HeaderElementMetadata;
import com.sforce.ws.tools.wsdlc;

public class ConnectionCodeGeneratorTest extends TestCase {

    public void testGenerateConnectionSource() throws Exception {
        final String expectedSource = CodeGeneratorTestUtil.fileToString("PartnerConnection.java");

        List<HeaderMetadata> headers = new ArrayList<HeaderMetadata>();

        List<HeaderElementMetadata> headerElements = null;

        HeaderMetadata header = null;

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setLanguage", "language"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.LocaleOptions_element", "LocaleOptions",
                "java.lang.String language", "LocaleOptions_qname", headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements
                .add(HeaderElementMetadata.newInstance("setTriggerAutoResponseEmail", "triggerAutoResponseEmail"));
        headerElements.add(HeaderElementMetadata.newInstance("setTriggerOtherEmail", "triggerOtherEmail"));
        headerElements.add(HeaderElementMetadata.newInstance("setTriggerUserEmail", "triggerUserEmail"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.EmailHeader_element", "EmailHeader",
                "boolean triggerAutoResponseEmail,boolean triggerOtherEmail,boolean triggerUserEmail",
                "EmailHeader_qname", headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setDebugLog", "debugLog"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.DebuggingInfo_element", "DebuggingInfo",
                "java.lang.String debugLog", "DebuggingInfo_qname", headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setUpdateMru", "updateMru"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.MruHeader_element", "MruHeader",
                "boolean updateMru", "MruHeader_qname", headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setClient", "client"));
        headerElements.add(HeaderElementMetadata.newInstance("setDefaultNamespace", "defaultNamespace"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.CallOptions_element", "CallOptions",
                "java.lang.String client,java.lang.String defaultNamespace", "CallOptions_qname", headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setSessionId", "sessionId"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.SessionHeader_element", "SessionHeader",
                "java.lang.String sessionId", "SessionHeader_qname", headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setDebugLevel", "debugLevel"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.DebuggingHeader_element",
                "DebuggingHeader", "com.sforce.soap.partner.wsc130.DebugLevel debugLevel", "DebuggingHeader_qname",
                headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setOrganizationId", "organizationId"));
        headerElements.add(HeaderElementMetadata.newInstance("setPortalId", "portalId"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.LoginScopeHeader_element",
                "LoginScopeHeader", "java.lang.String organizationId,java.lang.String portalId",
                "LoginScopeHeader_qname", headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setTransferToUserId", "transferToUserId"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.UserTerritoryDeleteHeader_element",
                "UserTerritoryDeleteHeader", "java.lang.String transferToUserId", "UserTerritoryDeleteHeader_qname",
                headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setBatchSize", "batchSize"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.QueryOptions_element", "QueryOptions",
                "int batchSize", "QueryOptions_qname", headerElements);
        headers.add(header);

        headerElements = new ArrayList<HeaderElementMetadata>();
        headerElements.add(HeaderElementMetadata.newInstance("setAssignmentRuleId", "assignmentRuleId"));
        headerElements.add(HeaderElementMetadata.newInstance("setUseDefaultRule", "useDefaultRule"));
        header = HeaderMetadata.newInstance("com.sforce.soap.partner.wsc130.AssignmentRuleHeader_element",
                "AssignmentRuleHeader", "java.lang.String assignmentRuleId,java.lang.Boolean useDefaultRule",
                "AssignmentRuleHeader_qname", headerElements);
        headers.add(header);

        List<OperationMetadata> operations = new ArrayList<OperationMetadata>();
        List<ElementMetadata> elements = new ArrayList<ElementMetadata>();
        elements.add(ElementMetadata.newInstance("setFieldList", "fieldList"));
        elements.add(ElementMetadata.newInstance("setSObjectType", "sObjectType"));
        elements.add(ElementMetadata.newInstance("setIds", "ids"));
        List<HeaderMetadata> operationHeaders = new ArrayList<HeaderMetadata>();
        operationHeaders.add(HeaderMetadata.newInstance("", "SessionHeader", "", "SessionHeader_qname", null));
        operationHeaders.add(HeaderMetadata.newInstance("", "CallOptions", "", "CallOptions_qname", null));
        operationHeaders.add(HeaderMetadata.newInstance("", "QueryOptions", "", "QueryOptions_qname", null));
        operationHeaders.add(HeaderMetadata.newInstance("", "MruHeader", "", "MruHeader_qname", null));
        operations.add(OperationMetadata.newInstance("com.sforce.soap.partner.sobject.wsc130.SObject[]", "retrieve",
                "com.sforce.soap.partner.wsc130.Retrieve_element",
                "com.sforce.soap.partner.wsc130.RetrieveResponse_element",
                "java.lang.String fieldList,java.lang.String sObjectType,java.lang.String[] ids", "ids",
                "java.lang.String fieldList,java.lang.String sObjectType,java.lang.String[] ids",
                "ids", "\"\"",
                "retrieve_qname", "retrieveResponse_qname", "return __response.getResult();", elements,
                operationHeaders, true, "com.sforce.soap.partner.sobject.wsc130.ISObject[]"));

        final String qNames = "    private static final javax.xml.namespace.QName convertLead_qname = new javax.xml.namespace.QName(\"urn:partner.soap.sforce.com\", \"convertLead\");\n"
                + "    private static final javax.xml.namespace.QName convertLeadResponse_qname = new javax.xml.namespace.QName(\"urn:partner.soap.sforce.com\", \"convertLeadResponse\");\n"
                + "    private static final javax.xml.namespace.QName sendEmail_qname = new javax.xml.namespace.QName(\"urn:partner.soap.sforce.com\", \"sendEmail\");\n";

        final String knownHeaders = "  knownHeaders.put(LocaleOptions_qname,com.sforce.soap.partner.wsc130.LocaleOptions_element.class);\n"
                + "  knownHeaders.put(EmailHeader_qname,com.sforce.soap.partner.wsc130.EmailHeader_element.class);\n";

        final ConnectionClassMetadata connectionClassMetadata = ConnectionClassMetadata.newInstance("\"wsc130\"",
                "com.sforce.soap.partner.wsc130", "PartnerConnection", true,
                "com.sforce.soap.partner.wsc130.LoginResult", "verifyPartnerEndpoint", true,
                "\"urn:sobject.partner.soap.sforce.com\"", qNames, knownHeaders, headers, operations);
        STGroupDir templates = new STGroupDir(wsdlc.TEMPLATE_DIR, '$', '$');
        ST template = templates.getInstanceOf(wsdlc.CONNECTION);
        assertNotNull(template);
        template.add("gen", connectionClassMetadata);
        assertEquals(expectedSource, template.render());
    }
}
