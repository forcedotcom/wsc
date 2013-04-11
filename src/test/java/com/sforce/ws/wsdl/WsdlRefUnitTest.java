/*
 * Copyright, 1999-2008, SALESFORCE.com All Rights Reserved Company Confidential
 */
package com.sforce.ws.wsdl;

import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.TestCase;

import com.sforce.ws.parser.PullParserException;

/**
 * WsdlRefUnitTest
 * 
 * @author mcheenath
 * @since 156
 */
public class WsdlRefUnitTest extends TestCase {

    public void testWsdlElement() throws PullParserException, WsdlParseException, MalformedURLException, IOException {
        WsdlFactory.create(getClass().getClassLoader().getResource("WsdlRefUnitTest.wsdl"));
    }

    public void testAttribute() throws WsdlParseException, MalformedURLException, IOException {
        WsdlFactory.create(getClass().getClassLoader().getResource("AttributeUnitTest.wsdl"));
    }

}