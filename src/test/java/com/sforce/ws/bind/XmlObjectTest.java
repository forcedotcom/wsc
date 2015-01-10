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

package com.sforce.ws.bind;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;

/**
 * XmlObjectTest -- Validates that subclasses of basic types can be serialized
 *
 * @author fhossain
 * @since 170
 */
public class XmlObjectTest extends TestCase {

    private static class MyDate extends Date {
    }

    public void testSubclassWrite() throws IOException {
        QName qname = new QName("type", "urn:sobject.partner.soap.sforce.com");
        XmlOutputStream xout = new XmlOutputStream(System.out, true);
        TypeMapper typeMapper = new TypeMapper();

        // First try with Date type
        XmlObject obj = new XmlObject(qname);
        obj.setValue(new Date());
        obj.write(qname, xout, typeMapper);

        // Then try subclass of Date
        obj = new XmlObject(qname);
        obj.setValue(new MyDate());
        obj.write(qname, xout, typeMapper);

        // Then try some non-mappable subclass
        obj = new XmlObject(qname);
        obj.setValue(new AtomicLong(10L));
        try {
            obj.write(qname, xout, typeMapper);
            fail("We should have received an exception trying to write object: " + obj.getValue());
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Unable to find xml type for"));
        }
    }

    private static class Foo { int x=0; public int getX() { return x; }
    public String toString() { return ""+x; };
    public boolean equals( Object a ) { return a instanceof Foo && ((Foo)a).getX() == this.getX(); } }
    public void testStringArray() throws Exception {
    	QName qname = new QName( "type", "sobject.partner.soap.sforce.com" );
    	TypeMapper typeMapper = new TypeMapper();

//    	String[][] data = new String[][] { {"a","b"} };
//    			{ "a","b" }, {}, { "<&>!" }
//    	};
    	String[] xx = { "a","b" };
    	Object[] data = new Object[] { xx };

    	for( Object d: data ) {
    		// Serialize
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	XmlOutputStream xout = new XmlOutputStream( baos, true );
        	xout.startDocument();
        	xout.writeStartTag("", "start");
    		XmlObject obj = new XmlObject( qname );
    		obj.setValue( d );
    		obj.write( qname,  xout,  typeMapper );
    		xout.writeEndTag("", "start");
    		xout.close();

    		System.out.println( new String(baos.toByteArray(),"UTF-8"));
    		// Parse
    		ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
    		XmlInputStream xin = new XmlInputStream();
    		xin.setInput( bais, "UTF-8" );
    		xin.nextTag();
    		XmlObject parsed = new XmlObject( qname );
    		parsed.load( xin, typeMapper );
    		parsed = parsed.getChildren().next().getChildren().next();
    		System.out.println( parsed.toString() );

    		assertEquals( d, parsed.getValue() );
    	}
    }
}
