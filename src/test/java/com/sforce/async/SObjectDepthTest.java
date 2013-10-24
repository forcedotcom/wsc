package com.sforce.async;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.sforce.ws.parser.XmlOutputStream;

public class SObjectDepthTest {
	
	@Test
	public void testDefaultDepthOk() throws IOException {
		XmlOutputStream mockedOutputStream = Mockito.mock(XmlOutputStream.class);
		
		SObject sObject = createObjectsWithDepth(5, 5);
		
		try {
			sObject.write(mockedOutputStream);
		} catch (IllegalStateException e) {
			Assert.fail("Default MAX DEPTH test fail");
		}
		Assert.assertTrue(true);
	}
	
	@Test(expected = IllegalStateException.class)	
	public void testDefaultDeepExceeded() throws IOException {
		XmlOutputStream mockedOutputStream = Mockito.mock(XmlOutputStream.class);		
		SObject sObject = createObjectsWithDepth(6, 5);
		sObject.write(mockedOutputStream);
	}
	
	@Test
	public void testIncreasedDeepWithSetter() throws IOException {
		XmlOutputStream mockedOutputStream = Mockito.mock(XmlOutputStream.class);
		
		SObject sObject = createObjectsWithDepth(7, 7);
		sObject.setMaxDepth(7);
		
		try {
			sObject.write(mockedOutputStream);
		} catch (IllegalStateException e) {
			Assert.fail("Default MAX DEPTH test fail");
		}
		Assert.assertTrue(true);
	}
	
	private SObject createObjectsWithDepth(int depth, int maxDepth) {
		SObject parent = null;
		SObject lastChild = null;
		
		for (int x = 0 ; x <= depth ; x++) {
			SObject object = new SObject(maxDepth);
			
			if (parent == null) {
				parent = object;
			}
			
			if (lastChild == null) {
				lastChild = object;
			} else {
				lastChild.setFieldReference("childObject", object);
				lastChild = object;
			}
			
			object.setField("currentDepth", String.valueOf(x));
		}
		
		return parent;
	}
}
