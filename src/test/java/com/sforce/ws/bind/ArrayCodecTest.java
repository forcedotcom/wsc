package com.sforce.ws.bind;

import java.util.Arrays;

import junit.framework.TestCase;

public class ArrayCodecTest extends TestCase {

    public void testStringArray() throws Exception {
        String[][] data = new String[][] {
                { "a","b" }, {}, { ",<&>!\"" }, { null, "", null }, {"\u269d","\26ab" }
        };

        ArrayCodec codec = new ArrayCodec();

        for( String[] d: data ) {
            String source = codec.getValueAsString( String.class, d );
            Object result = codec.deserialize(String.class, source);
            assertTrue( result.getClass().isArray() );
            assertTrue( Arrays.equals(d, (Object[])result));
        }
    }
}
