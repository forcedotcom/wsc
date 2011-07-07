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
