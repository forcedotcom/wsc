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
 * Make sure we handle attributes and attributeGroups correctly.
 * 
 * @author lmcalpin
 */
public class AttributeTest extends TestCase {
	Definitions attrGroupTestDefs, attrUnitTestDefs, attrRefTestDefs;

	@Override
    public void setUp() throws Exception {
		attrGroupTestDefs = WsdlFactory.create(getClass().getClassLoader()
				.getResource("AttributeGroupTest.wsdl"));
		attrUnitTestDefs = WsdlFactory.create(getClass().getClassLoader()
				.getResource("AttributeUnitTest.wsdl"));
		attrRefTestDefs = WsdlFactory.create(getClass().getClassLoader()
				.getResource("AttributeRefTest.wsdl"));
	}

	/**
	 * Ensure attributes are properly parsed when they appear on Schema
	 * elements.
	 * 
	 * @throws Exception
	 */
	public void testAttributeOnSchema() throws Exception {
		Assert.assertNotNull(attrUnitTestDefs);
		Schema schema = attrUnitTestDefs.getTypes().getSchemas().iterator()
				.next();
		Attribute version = schema.getGlobalAttribute("version");
		Assert.assertNotNull(version);
		Assert.assertEquals("1.0", version.getFixed());
	}

	/**
	 * Ensure attributes are properly parsed when they appear on ComplexType
	 * elements.
	 * 
	 * @throws Exception
	 */
	public void testAttributeOnComplexType() throws Exception {
		Assert.assertNotNull(attrUnitTestDefs);
		Schema schema = attrUnitTestDefs.getTypes().getSchemas().iterator()
				.next();
		Element purchaseOrder = attrUnitTestDefs.getTypes()
				.getElement(
						ParserUtil.toQName("purchaseOrder",
								schema.getTargetNamespace()));
		ComplexType purchaseOrderType = attrUnitTestDefs.getTypes()
				.getComplexType(purchaseOrder.getType());
		Attribute orderDate = purchaseOrderType.getAttribute("orderDate");
		Assert.assertNotNull(orderDate);
	}

	public void testAttributeGroups() throws Exception {
		Assert.assertNotNull(attrGroupTestDefs);
		ComplexType purchaseOrderType = attrGroupTestDefs.getTypes()
				.getComplexType(
						ParserUtil.toQName("BaseFault",
								"https://lmcalpin.com/testAttributes"));
		List<Attribute> attributes = CollectionUtil.asList(purchaseOrderType
				.getAttributes());
		Assert.assertEquals(3, attributes.size());
		Attribute code = CollectionUtil.findByName(attributes, "code");
		Assert.assertNotNull(code);
		Assert.assertNull(code.getRef());
		Assert.assertNotNull(CollectionUtil.findByName(attributes, "severity"));
		Assert.assertNotNull(CollectionUtil.findByName(attributes,
				"businessImpact"));
	}

	public void testAttributeRefs() throws Exception {
		Assert.assertNotNull(attrRefTestDefs);
		ComplexType purchaseOrderType = attrRefTestDefs.getTypes()
				.getComplexType(
						ParserUtil.toQName("BaseFault",
								"https://lmcalpin.com/testAttributes"));
		List<Attribute> attributes = CollectionUtil.asList(purchaseOrderType
				.getAttributes());
		Assert.assertEquals(3, attributes.size());
		Attribute code = CollectionUtil.findByName(attributes, "code");
		Assert.assertNotNull(code);
		Assert.assertTrue(code.getRef().getLocalPart().equals("code"));
		Assert.assertNotNull(CollectionUtil.findByName(attributes, "severity"));
		Assert.assertNotNull(CollectionUtil.findByName(attributes,
				"businessImpact"));
	}

}