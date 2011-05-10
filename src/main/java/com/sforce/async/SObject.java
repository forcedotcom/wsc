/*
 * Copyright, 1999-2008, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.async;

import java.util.*;
import java.io.IOException;

import com.sforce.ws.parser.XmlOutputStream;

/**
 * SObject
 *
 * @author mcheenath
 * @since 160
 */
public final class SObject {

    private static final int MAX_DEPTH = 5;

    private final HashMap<String, String> fields = new HashMap<String, String>();
    private final HashMap<String, SObject> fkRefs = new HashMap<String, SObject>();

    public Set<String> getFieldNames() {
        return Collections.unmodifiableSet(fields.keySet());
    }

    public String getField(String name) {
        return fields.get(name);
    }

    public void setField(String name, String value) {
        fields.put(name, value);
    }

    public void setFieldReference(String name, SObject ref) {
        if (ref == this) throw new IllegalStateException(
                "Foreign Key SObject Reference is pointing to the same SObject");

        fkRefs.put(name, ref);
    }

    /**
     * Example:
     * <?xml version="1.0" encoding="UTF-8"?>
     * <sObjects xmlns="http://www.force.com/2009/06/asyncapi/dataload">
     *    <sObject>
     *       <Name>XYZ</Name>
     *       <Description>BLAH</Description>
     *       <AccountNumber>123456</AccountNumber>
     *       <ReportTo>
     *         <sObject>
     *           <!-- type is optional. It is needed only for polymorphic foreign keys --> 
     *           <type>ParentEntityType</type>
     *           <email>foo@bar.com</email>
     *           <!-- we don't support more than one field here -->
     *         </sObject>
     *       </ReportTo>
     *    </sObject>
     * </sObjects>
     * 
     * @param out
     * @throws IOException
     */
    public void write(XmlOutputStream out) throws IOException {
        write(out, 0);
    }

    public void write(XmlOutputStream out, int depth) throws IOException {
        if (depth > MAX_DEPTH) throw new IllegalStateException(
                "foreign key reference exceeded the maximum allowed depth of " + MAX_DEPTH);
        
        out.writeStartTag(RestConnection.NAMESPACE, "sObject");
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            out.writeStringElement(RestConnection.NAMESPACE, name, value);
        }
        for (Map.Entry<String, SObject> entry : fkRefs.entrySet()) {
            String relationshipName = entry.getKey();
            SObject ref = entry.getValue();
            out.writeStartTag(RestConnection.NAMESPACE, relationshipName);
            ref.write(out, depth++);
            out.writeEndTag(RestConnection.NAMESPACE, relationshipName);
        }
        out.writeEndTag(RestConnection.NAMESPACE, "sObject");
    }
}
