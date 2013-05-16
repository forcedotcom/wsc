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

package com.sforce.ws.bind;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;
import com.sforce.ws.util.Verbose;

import java.io.IOException;

/**
 * SoapHeaderObject
 *
 * @author cheenath
 * @version 1.0
 * @since 146  Dec 19, 2006
 */
public class SoapHeaderObject implements com.sforce.ws.bind.XMLizable {

    /**
     * element  : actor of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final TypeInfo actor__typeInfo =
            new TypeInfo("http://schemas.xmlsoap.org/soap/envelope/",
                    "actor", "http://www.w3.org/2001/XMLSchema", "string", 0, 1, true);

    private boolean actor__is_set = false;

    private String actor;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
        actor__is_set = true;
    }

    /**
     * element  : actor of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final TypeInfo mustUnderstand__typeInfo =
            new TypeInfo("http://schemas.xmlsoap.org/soap/envelope/",
                    "mustUnderstand", "http://www.w3.org/2001/XMLSchema", "string", 0, 1, true);

    private boolean mustUnderstand__is_set = false;

    private boolean mustUnderstand;

    public boolean getMustUnderstand() {
        return mustUnderstand;
    }

    public void setMustUnderstand(boolean mustUnderstand) {
        this.mustUnderstand = mustUnderstand;
        mustUnderstand__is_set = true;
    }

    /**
     */
    @Override
    public void write(javax.xml.namespace.QName __element, XmlOutputStream __out, TypeMapper __typeMapper)
            throws IOException {
        __out.writeStartTag(__element.getNamespaceURI(), __element.getLocalPart());

        writeFields(__out, __typeMapper);
        __out.writeEndTag(__element.getNamespaceURI(), __element.getLocalPart());
    }

    protected void writeFields(XmlOutputStream __out, TypeMapper __typeMapper) throws java.io.IOException {
        if (actor__is_set) {
            __out.writeAttribute(actor__typeInfo.getNamespace(), "actor", actor);
        }

        if (mustUnderstand__is_set) {
            String val = mustUnderstand ? "1" : "0";
            __out.writeAttribute(mustUnderstand__typeInfo.getNamespace(), "mustUnderstand", val);
        }
    }

    @Override
    public void load(XmlInputStream __in, TypeMapper __typeMapper) throws IOException, ConnectionException {
        __typeMapper.consumeStartTag(__in);
        loadFields(__in, __typeMapper);
        __typeMapper.consumeEndTag(__in);
    }

    protected void loadFields(XmlInputStream __in, TypeMapper __typeMapper) throws IOException, ConnectionException {
        actor = __in.getAttributeValue(actor__typeInfo.getNamespace(), "actor");
        String val = __in.getAttributeValue(mustUnderstand__typeInfo.getNamespace(), "mustUnderstand");

        if (val != null) {
            if ("0".equals(val)) {
                setMustUnderstand(false);
            } else if ("1".equals(val)) {
                setMustUnderstand(true);
            } else {
                throw new ConnectionException("mustUndrestand must be 1 or 0. but found " + val);
            }
        }
    }

    @Override
    public String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("[SoapHeaderObject ");

        sb.append(" actor=");
        sb.append("'").append(Verbose.toString(actor)).append("'\n");
        sb.append(" mustUnderstand=");
        sb.append("'").append(Verbose.toString(mustUnderstand)).append("'\n");
        sb.append("]\n");
        return sb.toString();
    }
}
