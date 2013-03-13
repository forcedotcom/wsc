/*
 * Copyright, 1999-2008, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.ws.wsdl;

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
        Definitions definitions = WsdlFactory.create(getClass().getClassLoader().getResource("WsdlRefUnitTest.wsdl"));
        Assert.assertNotNull(definitions);
    }

    public void testAttribute() throws Exception {
        Definitions definitions = WsdlFactory.create(getClass().getClassLoader().getResource("AttributeUnitTest.wsdl"));
        Assert.assertNotNull(definitions);
    }

}