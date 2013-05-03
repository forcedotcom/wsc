/*
 * Copyright, 1999-2008, SALESFORCE.com All Rights Reserved Company Confidential
 */
package com.sforce.ws.wsdl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Assert;

import junit.framework.TestCase;

import com.sforce.ws.parser.PullParserException;
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