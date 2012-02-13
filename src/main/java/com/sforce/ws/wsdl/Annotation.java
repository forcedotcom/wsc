/*
 * Copyright, 1999-2008, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.ws.wsdl;

import com.sforce.ws.parser.XmlInputStream;

/**
 * Annotation
 *
 * @author mcheenath
 * @since 158
 */
public class Annotation extends WsdlNode {

    public void read(WsdlParser parser) throws WsdlParseException {

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                //skip annotation
           } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ANNOTATION.equals(name) && SCHEMA_NS.equals(namespace)) {
                    return;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'annotation'");
            }

            eventType = parser.next();
        }
    }
}
