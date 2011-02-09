package com.sforce.ws.bind;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.namespace.QName;

import com.sforce.ws.parser.XmlOutputStream;

import junit.framework.Assert;
import junit.framework.TestCase;

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
}
