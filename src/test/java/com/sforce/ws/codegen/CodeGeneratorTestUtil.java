package com.sforce.ws.codegen;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import junit.framework.Assert;

public class CodeGeneratorTestUtil {
    private static final String NL = System.getProperty("line.separator");
    
    public static String getSourceAsStr(final File src) throws IOException {
        FileInputStream stream = new FileInputStream(src);
        try {
          FileChannel fc = stream.getChannel();
          MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
          /* Instead of using default, pass in a decoder. */
          return Charset.defaultCharset().decode(bb).toString();
        }
        finally {
          stream.close();
        }
    }

    public static File getFileFromResource(final String resource) throws URISyntaxException {
            File file;
            try {
                file = new File(CodeGeneratorTestUtil.class.getClassLoader().getResource(resource).toURI());
            } catch (URISyntaxException x) {
                throw x;
            }
            return file;
    }
    
    public static String getNewLine() {
        return NL;
    }

    public static String fileToString(final String fileName) {
        String str = null;
        
        try {
            File src = new File(CodeGeneratorTestUtil.getFileFromResource("codegeneration"), fileName);
            str = CodeGeneratorTestUtil.getSourceAsStr(src);
        }
        catch (Exception x) {
            Assert.fail("Failed to load source file = " + fileName + " because " + x.getMessage());
        }
        
        return str;
    }
}
