/*
 * Copyright, 1999-2008, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.ws.wsdl;

import java.net.URL;

import junit.framework.TestCase;
import org.junit.Assert;

/**
 * WsdlFactoryTest
 *
 * @author mcheenath
 * @since 156
 */
public class WsdlFactoryTest extends TestCase {

    public void testWsdlElement() throws Exception {
        Definitions definitions = WsdlFactory.create(new URL(getSourcePath() + "src/com/sforce/ws/wsdl/WsdlRefUnitTest.wsdl"));
        Assert.assertNotNull(definitions);
    }

    public void testAttribute() throws Exception {
        Definitions definitions = WsdlFactory.create(new URL(getSourcePath() + "src/com/sforce/ws/wsdl/AttributeUnitTest.wsdl"));
        Assert.assertNotNull(definitions);
    }

    private String getSourcePath() {
        ClassLoader loader = getClass().getClassLoader();
        String url = loader.getResource(WsdlFactoryTest.class.getName().replace(".", "/") + ".class").toString();
        int index = url.lastIndexOf("classes");

        if (index == -1) {
            throw new RuntimeException("Failed to find source path");
        } else {
            url = url.substring(0, index);
        }

        return url;
    }
}