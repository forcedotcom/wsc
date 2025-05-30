/*
 * Copyright (c) 2017, salesforce.com, inc.
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

package com.sforce.ws.codegen;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.sforce.ws.tools.wsdlc;
import com.sforce.ws.util.FileUtil;
import junit.framework.Assert;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import static junit.framework.TestCase.assertNotNull;

public class CodeGeneratorTestUtil {
    private static final String NL = System.getProperty("line.separator");
    
    public static String getSourceAsStr(final File src) throws IOException {
        return FileUtil.toString(src);
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

    public static void cleanupDirectory(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static String getRenderedStringWithReplacements(ST template){
        String rendered = template.render();
        rendered = rendered.replace("\r\n", "\n");
        return rendered;
    }

    public static ST getTemplateDefinitions(String generatorName) {
        STGroupDir templates = new STGroupDir(wsdlc.TEMPLATE_DIR, '$', '$');
        ST template = templates.getInstanceOf(generatorName);
        assertNotNull(template);
        return template;
    }
}
