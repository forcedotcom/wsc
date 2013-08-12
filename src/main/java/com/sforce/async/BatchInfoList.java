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
 * BatchInfoList --
 *
 * @author mcheenath
 * @since 160
 */

public class BatchInfoList implements com.sforce.ws.bind.XMLizable {

    /**
     * Constructor
     */
    public BatchInfoList() {
    }

    private static final String NAMESPACE = BulkConnection.NAMESPACE;

    /**
     * element  : batchInfo of type {urn:partner.soap.sforce.com}Error
     * java type: com.sforce.soap.partner.wsc.Error[]
     */
    private static final TypeInfo batchInfo__typeInfo =
            new com.sforce.ws.bind.TypeInfo(NAMESPACE, "batchInfo", NAMESPACE, "BatchInfo", 0, -1, true);

    private boolean batchInfo__is_set = false;

    private BatchInfo[] batchInfo = new BatchInfo[0];

    public BatchInfo[] getBatchInfo() {
        return batchInfo;
    }


    public void setBatchInfo(BatchInfo[] batchInfo) {
        this.batchInfo = batchInfo;
        batchInfo__is_set = true;
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

        __typeMapper.writeObject(__out, batchInfo__typeInfo, batchInfo, batchInfo__is_set);
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
        if (__typeMapper.isElement(__in, batchInfo__typeInfo)) {
            setBatchInfo((BatchInfo[]) __typeMapper.readObject(__in, batchInfo__typeInfo, BatchInfo[].class));
        }
    }

    @Override
    public String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("[BatchInfoList ");
        sb.append(" batchInfo=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(batchInfo)).append("'\n");
        sb.append("]\n");
        return sb.toString();
    }
}
