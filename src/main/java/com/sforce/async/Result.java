package com.sforce.async;

/**
 * @author mcheenath,vmehra
 * @since 164
 */
public class Result implements com.sforce.ws.bind.XMLizable {

    private static final String NAMESPACE = RestConnection.NAMESPACE;

    /**
     * element  : errors of type {urn:partner.soap.sforce.com}Error
     * java type: com.sforce.soap.partner.wsc.Error[]
     */
    private static final com.sforce.ws.bind.TypeInfo errors__typeInfo =
            new com.sforce.ws.bind.TypeInfo(NAMESPACE, "errors", NAMESPACE, "Error", 0, -1, true);

    private boolean errors__is_set = false;
    private Error[] errors = new Error[0];

    public Result() {
    }

    public Error[] getErrors() {
        return errors;
    }

    public void setErrors(Error[] errors) {
        this.errors = errors;
        errors__is_set = true;
    }

    /**
     * element  : id of type {urn:partner.soap.sforce.com}ID
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo id__typeInfo =
            new com.sforce.ws.bind.TypeInfo(NAMESPACE, "id", NAMESPACE, "ID", 1, 1, true);

    private boolean id__is_set = false;

    private java.lang.String id;

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
        id__is_set = true;
    }

    /**
     * element  : success of type {http://www.w3.org/2001/XMLSchema}boolean
     * java type: boolean
     */
    private static final com.sforce.ws.bind.TypeInfo success__typeInfo =
            new com.sforce.ws.bind.TypeInfo(NAMESPACE, "success", "http://www.w3.org/2001/XMLSchema", "boolean", 1, 1, true);

    private boolean success__is_set = false;

    private boolean success;

    public boolean getSuccess() {
        return success;
    }

    public boolean isSuccess() {
        return success;
    }


    public void setSuccess(boolean success) {
        this.success = success;
        success__is_set = true;
    }

    /**
     * element  : created of type {http://www.w3.org/2001/XMLSchema}boolean
     * java type: boolean
     */
    private static final com.sforce.ws.bind.TypeInfo created__typeInfo =
            new com.sforce.ws.bind.TypeInfo(NAMESPACE, "created", "http://www.w3.org/2001/XMLSchema", "boolean", 1, 1, true);

    private boolean created__is_set = false;

    private boolean created;

    public boolean getCreated() {
        return created;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
        created__is_set = true;
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

        __typeMapper.writeObject(__out, errors__typeInfo, errors, errors__is_set);
        __typeMapper.writeString(__out, id__typeInfo, id, id__is_set);
        __typeMapper.writeBoolean(__out, success__typeInfo, success, success__is_set);
        __typeMapper.writeBoolean(__out, created__typeInfo, created, created__is_set);
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
        if (__typeMapper.isElement(__in, errors__typeInfo)) {
            setErrors((Error[]) __typeMapper.readObject(__in, errors__typeInfo, Error[].class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, id__typeInfo)) {
            setId(__typeMapper.readString(__in, id__typeInfo, String.class));
        }
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, success__typeInfo)) {
            setSuccess(__typeMapper.readBoolean(__in, success__typeInfo, boolean.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, created__typeInfo)) {
            setCreated(__typeMapper.readBoolean(__in, created__typeInfo, boolean.class));
        }
    }

    @Override
    public String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("[SaveResult ");

        sb.append(" errors=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(errors)).append("'\n");
        sb.append(" id=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(id)).append("'\n");
        sb.append(" success=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(success)).append("'\n");
        sb.append(" created=");
        sb.append("'").append(com.sforce.ws.util.Verbose.toString(created)).append("'\n");
        sb.append("]\n");
        return sb.toString();
    }
}
