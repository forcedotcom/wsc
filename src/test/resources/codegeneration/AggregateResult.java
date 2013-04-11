/*
 * Copyright (c) 2005-2013 salesforce.com, inc.
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
package com.sforce.soap.enterprise.sobject;

import com.sforce.ws.bind.XmlObject;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.wsdl.Constants;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;
import com.sforce.ws.ConnectionException;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;

/**
 */
public class AggregateResult extends SObject {

    private XmlObject xmlObject = new XmlObject();

    /**
     * Constructor
     */
    public AggregateResult() {
    }

    public Object getField(String name) {
        return xmlObject.getField(name);
    }

    @Override
    public String getId() {
        return (String)xmlObject.getField("Id");
    }

    @Override
    public void setId(String id) {
        xmlObject.setField("Id", id);
    }

    @Override
    public void write(QName element, XmlOutputStream out, TypeMapper typeMapper) throws IOException {
        xmlObject.write(element, out, typeMapper);
    }

    @Override
    public void load(XmlInputStream in, TypeMapper typeMapper) throws IOException, ConnectionException {
        xmlObject.load(in, typeMapper);
    }
}
