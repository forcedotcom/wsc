package com.sforce.ws.bind;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.PullParserException;
import com.sforce.ws.parser.XmlInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * This test validates type mapper functionality
 *
 * @author rcornel
 */
public class TypeMapperTests {
    // test to validate we can read primitive int array types.
    @Test
    public void testPrimitiveArrayRead() throws PullParserException, ConnectionException, IOException {
        String arrayStr = "<b><a>0</a><a>1</a><a>2</a></b>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arrayStr.getBytes());

        XmlInputStream xmlInputStream = new XmlInputStream();
        xmlInputStream.setInput(inputStream, "UTF-8");
        xmlInputStream.nextTag();

        TypeInfo info = new TypeInfo("","a","","int", 0, 5, true);

        TypeMapper mapper = new TypeMapper();
        int[] returnArray = (int[]) mapper.readPartialArray(xmlInputStream, info, int[].class);
        for (int i = 0 ; i < returnArray.length; i++) {
            assertEquals(i, returnArray[i]);
        }
    }


    // test to validate we can read a String Array type
    @Test
    public void testStringArrayRead() throws PullParserException, ConnectionException, IOException {
        String arrayStr = "<b><a>Test</a></b>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arrayStr.getBytes());

        XmlInputStream xmlInputStream = new XmlInputStream();
        xmlInputStream.setInput(inputStream, "UTF-8");
        xmlInputStream.nextTag();

        TypeInfo info = new TypeInfo("","a","","int", 0, 5, true);

        TypeMapper mapper = new TypeMapper();
        String[] returnArray = (String[]) mapper.readPartialArray(xmlInputStream, info, String[].class);
        assertEquals("Test", returnArray[0]);

    }
}
