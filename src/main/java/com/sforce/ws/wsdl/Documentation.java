package com.sforce.ws.wsdl;

import com.sforce.ws.parser.XmlInputStream;

/**
 * Documentation
 * 
 */
public class Documentation extends WsdlNode {

        public void read(WsdlParser parser) throws WsdlParseException {

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                //read doc
           } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (DOCUMENTATION.equals(name) && WSDL_NS.equals(namespace)) {
                    return;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'types'");
            }

            eventType = parser.next();
        }
    }

}
