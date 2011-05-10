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

    private static final String NAMESPACE = RestConnection.NAMESPACE;

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
