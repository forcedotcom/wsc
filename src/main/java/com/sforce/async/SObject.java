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

    public Map<String, SObject> getFieldReferences() {
        return Collections.unmodifiableMap(fkRefs);
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
        
        out.writeStartTag(BulkConnection.NAMESPACE, "sObject");
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            out.writeStringElement(BulkConnection.NAMESPACE, name, value);
        }
        for (Map.Entry<String, SObject> entry : fkRefs.entrySet()) {
            String relationshipName = entry.getKey();
            SObject ref = entry.getValue();
            out.writeStartTag(BulkConnection.NAMESPACE, relationshipName);
            ref.write(out, depth++);
            out.writeEndTag(BulkConnection.NAMESPACE, relationshipName);
        }
        out.writeEndTag(BulkConnection.NAMESPACE, "sObject");
    }
}
