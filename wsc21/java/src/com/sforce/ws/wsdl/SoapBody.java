package com.sforce.ws.wsdl;

import com.sforce.ws.parser.XmlInputStream;

/**
 * SoapBody
 *
 * @author cheenath
 * @version 1.0
 * @since 146  Dec 13, 2006
 */
public class SoapBody extends SoapNode {
    private String use;
    private String parts;

    void read(WsdlParser parser) throws WsdlParseException {
        parts = parser.getAttributeValue(null, "parts");
        use = parseUse(parser);

        int eventType = parser.getEventType();
        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                //nothing to do
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();
                if (BODY.equals(name) && WSDL_SOAP_NS.equals(namespace)) {
                    return;
                }
            }
            eventType = parser.next();
        }
    }

    @Override
    public String toString() {
        return "SoapBody{" +
                "use='" + use + '\'' +
                ", parts='" + parts + '\'' +
                '}';
    }
}
