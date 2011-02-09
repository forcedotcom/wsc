package com.sforce.ws.wsdl;

import com.sforce.ws.parser.XmlInputStream;

import javax.xml.namespace.QName;

/**
 * SoapHeader
 *
 * @author cheenath
 * @version 1.0
 * @since 146  Dec 13, 2006
 */
public class SoapHeader extends SoapNode {
    private String use;
    private QName message;
    private String part;

    public String getUse() {
        return use;
    }

    public QName getMessage() {
        return message;
    }

    public String getPart() {
        return part;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        use = parseUse(parser);
        message = parseMessage(parser);
        part = parser.getAttributeValue(null, "part");

        int eventType = parser.getEventType();
        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                //nothing to do
           } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();
                if (HEADER.equals(name) && WSDL_SOAP_NS.equals(namespace)) {
                    return;
                }
            }
            eventType = parser.next();
        }
    }

    @Override
    public String toString() {
        return "SoapHeader{" +
                "use='" + use + '\'' +
                ", message=" + message +
                ", part='" + part + '\'' +
                '}';
    }
}
