/*
 * Copyright, 1999-2008, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.ws.wsdl;

import com.sforce.ws.parser.PullParserException;

import junit.framework.TestCase;

/**
 * WsdlRefUnitTest
 *
 * @author mcheenath
 * @since 156
 */
public class WsdlRefUnitTest extends TestCase {

    public void testWsdlElement() throws PullParserException, WsdlParseException {
        WsdlFactory.create(getSourcePath()+"src/com/sforce/ws/wsdl/WsdlRefUnitTest.wsdl");
    }

    public void testAttribute() throws WsdlParseException {
        WsdlFactory.create(getSourcePath() + "src/com/sforce/ws/wsdl/AttributeUnitTest.wsdl");
    }

    private String getSourcePath() {
        ClassLoader loader = getClass().getClassLoader();
        String url = loader.getResource(WsdlRefUnitTest.class.getName().replace(".", "/") + ".class").toString();
        int index = url.lastIndexOf("classes");

        if (index == -1) {
            throw new RuntimeException("Failed to find source path");
        } else {
            url = url.substring(0, index);
        }

        return url;
    }
}