package com.sforce.ws.codegen;

import java.util.ArrayList;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import junit.framework.TestCase;

import com.sforce.ws.codegen.metadata.ComplexClassMetadata;
import com.sforce.ws.codegen.metadata.MemberMetadata;
import com.sforce.ws.tools.wsdlc;

public class ComplexTypeCodeGeneratorTest extends TestCase {

    public void testGenerateComplexTypeSource() throws Exception {
        String expectedSource = CodeGeneratorTestUtil.fileToString("EmailSyncEntity.java");

        final String packageName = "com.sforce.soap.partner.wsc";
        final String className = "EmailSyncEntity";
        final String typeExtention = "implements com.sforce.ws.bind.XMLizable";
        final String xsiType = "";
        final String superWrite = "";
        final String superLoad = "";
        final String superToString = "";

        List<MemberMetadata> memberMetadataList = new ArrayList<MemberMetadata>();

        memberMetadataList
                .add(MemberMetadata
                        .newInstance(
                                "conflictResolution of type {urn:partner.soap.sforce.com}EmailSyncConflictResolution",
                                "com.sforce.soap.partner.wsc.EmailSyncConflictResolution",
                                "conflictResolution",
                                "\"urn:partner.soap.sforce.com\",\"conflictResolution\",\"urn:partner.soap.sforce.com\",\"EmailSyncConflictResolution\",1,1,true",
                                "", "getConflictResolution", "", "", "setConflictResolution", "writeObject",
                                "verifyElement", "readObject"));

        memberMetadataList
                .add(MemberMetadata
                        .newInstance(
                                "dataSetFilter of type {http://www.w3.org/2001/XMLSchema}string",
                                "java.lang.String",
                                "dataSetFilter",
                                "\"urn:partner.soap.sforce.com\",\"dataSetFilter\",\"http://www.w3.org/2001/XMLSchema\",\"string\",0,1,true",
                                "", "getDataSetFilter", "", "", "setDataSetFilter", "writeString", "isElement",
                                "readString"));

        memberMetadataList
                .add(MemberMetadata
                        .newInstance(
                                "fieldMapping of type {urn:partner.soap.sforce.com}EmailSyncFieldMapping",
                                "com.sforce.soap.partner.wsc.EmailSyncFieldMapping[]",
                                "fieldMapping",
                                "\"urn:partner.soap.sforce.com\",\"fieldMapping\",\"urn:partner.soap.sforce.com\",\"EmailSyncFieldMapping\",0,-1,true",
                                " = new com.sforce.soap.partner.wsc.EmailSyncFieldMapping[0]", "getFieldMapping", "",
                                "", "setFieldMapping", "writeObject", "isElement", "readObject"));

        memberMetadataList
                .add(MemberMetadata
                        .newInstance(
                                "matchPreference of type {urn:partner.soap.sforce.com}EmailSyncMatchPreference",
                                "com.sforce.soap.partner.wsc.EmailSyncMatchPreference",
                                "matchPreference",
                                "\"urn:partner.soap.sforce.com\",\"matchPreference\",\"urn:partner.soap.sforce.com\",\"EmailSyncMatchPreference\",1,1,true",
                                "", "getMatchPreference", "", "", "setMatchPreference", "writeObject", "verifyElement",
                                "readObject"));

        memberMetadataList.add(MemberMetadata.newInstance("name of type {http://www.w3.org/2001/XMLSchema}string",
                "java.lang.String", "name",
                "\"urn:partner.soap.sforce.com\",\"name\",\"http://www.w3.org/2001/XMLSchema\",\"string\",1,1,true",
                "", "getName", "", "", "setName", "writeString", "verifyElement", "readString"));

        memberMetadataList.add(MemberMetadata.newInstance("recordTypeId of type {urn:partner.soap.sforce.com}ID",
                "java.lang.String", "recordTypeId",
                "\"urn:partner.soap.sforce.com\",\"recordTypeId\",\"urn:partner.soap.sforce.com\",\"ID\",1,1,true", "",
                "getRecordTypeId", "", "", "setRecordTypeId", "writeString", "verifyElement", "readString"));

        memberMetadataList
                .add(MemberMetadata
                        .newInstance(
                                "syncDirection of type {urn:partner.soap.sforce.com}EmailSyncDirection",
                                "com.sforce.soap.partner.wsc.EmailSyncDirection",
                                "syncDirection",
                                "\"urn:partner.soap.sforce.com\",\"syncDirection\",\"urn:partner.soap.sforce.com\",\"EmailSyncDirection\",1,1,true",
                                "", "getSyncDirection", "", "", "setSyncDirection", "writeObject", "verifyElement",
                                "readObject"));

        memberMetadataList
                .add(MemberMetadata
                        .newInstance(
                                "syncFollowed of type {http://www.w3.org/2001/XMLSchema}boolean",
                                "boolean",
                                "syncFollowed",
                                "\"urn:partner.soap.sforce.com\",\"syncFollowed\",\"http://www.w3.org/2001/XMLSchema\",\"boolean\",1,1,true",
                                "", "getSyncFollowed", "boolean", "isSyncFollowed", "setSyncFollowed", "writeBoolean",
                                "verifyElement", "readBoolean"));

        final ComplexClassMetadata classMetadata = new ComplexClassMetadata(packageName, className, typeExtention,
                xsiType, superWrite, superLoad, superToString, memberMetadataList);

        STGroupDir templates = new STGroupDir(wsdlc.TEMPLATE_DIR, '$', '$');
        ST template = templates.getInstanceOf(Generator.TYPE);
        assertNotNull(template);
        template.add("gen", classMetadata);
        assertEquals(expectedSource, template.render()); 
    }
}
