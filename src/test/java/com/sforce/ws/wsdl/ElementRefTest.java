/*
 * Copyright (c) 2013, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *    Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 *    promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.sforce.ws.wsdl;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.sforce.ws.util.CollectionUtil;

/**
 * WsdlFactoryTest
 *
 * @author mcheenath
 * @since 156
 */
public class ElementRefTest extends TestCase {
    Definitions wsdlRefUnitTestDefs, duplicateAnonymousTypeTestDefs;
    
	@Override
    public void setUp() throws Exception {
		wsdlRefUnitTestDefs = WsdlFactory.create(getClass().getClassLoader().getResource("WsdlRefUnitTest.wsdl"));
		duplicateAnonymousTypeTestDefs = WsdlFactory.create(getClass().getClassLoader().getResource("DuplicateElementNameTest.wsdl"));
	}
	
    public void testComplexElement() throws Exception {
        Assert.assertNotNull(wsdlRefUnitTestDefs);
        Element complexElement = wsdlRefUnitTestDefs.getTypes().getElement(ParserUtil.toQName("input", "http://doc.sample.com/docSample"));
        Assert.assertNotNull(complexElement);
        Assert.assertTrue(complexElement.isComplexType());
    }

    /**
     * Ensure elements that "ref" a definition elsewhere in the schema are resolved properly in
     * the second pass parse.
     * @throws Exception
     */
    public void testElementRef() throws Exception {
        Assert.assertNotNull(wsdlRefUnitTestDefs);
        Element complexElement = wsdlRefUnitTestDefs.getTypes().getElement(ParserUtil.toQName("input", "http://doc.sample.com/docSample"));
        ComplexType complexType = wsdlRefUnitTestDefs.getTypes().getComplexType(complexElement.getType());
        Assert.assertNotNull(complexElement);
        Assert.assertNotNull(complexType);
        List<Element> elements = CollectionUtil.asList(complexType.getContent().getElements());
        Assert.assertEquals(4, elements.size());
        Element outputElement = CollectionUtil.findByName(elements, "output");
        Assert.assertNotNull(outputElement);
    } 

    /**
     * Ensure that elements that declare anonymous types don't overwrite each other.  (I.e., we have to use more than just the element
     * name to disambiguate them.)
     * @throws Exception
     */
    public void testAnonymousTypes() throws Exception {
        Assert.assertNotNull(duplicateAnonymousTypeTestDefs);
        Element complexElement = duplicateAnonymousTypeTestDefs.getTypes().getElement(ParserUtil.toQName("body", "https://lmcalpin.com/testDupes"));
        ComplexType complexType = complexElement.findComplexType();
        Element test1Elem = complexType.getElement("test1");
        Element test2Elem = complexType.getElement("test2");
        ComplexType test1Type = test1Elem.findComplexType();
        ComplexType anonType1 = test1Type.getContent().getElement("bodyOut").findComplexType();
        Assert.assertTrue(anonType1.hasElement("test1"));
        Assert.assertTrue(anonType1.hasElement("test2"));
        ComplexType test2Type = test2Elem.findComplexType();
        ComplexType anonType2 = test2Type.getElement("bodyOut").findComplexType();
        Assert.assertTrue(anonType2.hasElement("test3"));
        Assert.assertTrue(anonType2.hasElement("test4"));
    }
}