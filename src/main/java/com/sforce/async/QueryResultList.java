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

import com.sforce.ws.bind.TypeInfo;

/**
 * QueryResultList --
 *
 * @author mcheenath
 * @since 170
 */

public class QueryResultList implements com.sforce.ws.bind.XMLizable {

    /**
     * Constructor
     */
    public QueryResultList() {
    }

    private static final String NAMESPACE = BulkConnection.NAMESPACE;

    private static final TypeInfo result__typeInfo =
            new TypeInfo(NAMESPACE, "result", "http://www.w3.org/2001/XMLSchema","string", 0, -1, true);

    private boolean result__is_set = false;

    private String[] result = new String[0];

    public String[] getResult() {
        return result;
    }


    public void setResult(String[] result) {
        this.result = result;
        result__is_set = true;
    }


    /**
     */
    @Override
    public void write(javax.xml.namespace.QName __element,
                      com.sforce.ws.parser.XmlOutputStream __out, com.sforce.ws.bind.TypeMapper __typeMapper)
            throws java.io.IOException {
        __out.writeStartTag(__element.getNamespaceURI(), __element.getLocalPart());

        writeFields(__out, __typeMapper);
        __out.writeEndTag(__element.getNamespaceURI(), __element.getLocalPart());
    }

    protected void writeFields(com.sforce.ws.parser.XmlOutputStream __out,
                               com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException {

        __typeMapper.writeObject(__out, result__typeInfo, result, result__is_set);
    }


    @Override
    public void load(com.sforce.ws.parser.XmlInputStream __in,
                     com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {
        __typeMapper.consumeStartTag(__in);
        loadFields(__in, __typeMapper);
        __typeMapper.consumeEndTag(__in);
    }

    protected void loadFields(com.sforce.ws.parser.XmlInputStream __in,
                              com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {

        __in.peekTag();
        if (__typeMapper.isElement(__in, result__typeInfo)) {
            setResult((String[]) __typeMapper.readObject(__in, result__typeInfo, String[].class));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ResultList ");
        sb.append(" result=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(result)).append("'\n");
        sb.append("]\n");
        return sb.toString();
    }
}
