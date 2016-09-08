/**
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

package com.sforce.ws.codegen;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.sforce.ws.bind.XMLizable;
import com.sforce.ws.tools.wsdlc;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.stringtemplate.v4.STGroupDir;

import static com.sforce.ws.tools.wsdlc.TEMPLATE_DIR;

@SuppressWarnings("unchecked")
public class ToStringTest {
    private static ClassLoader classLoader;
    private static Path tempPath;

    @BeforeClass
    public static void init() throws Exception {
        File wsdl = CodeGeneratorTestUtil.getFileFromResource("ToStringTest.wsdl");

        tempPath = Files.createTempDirectory("ToStringTest");
        wsdlc.run(wsdl.getAbsolutePath(), "test.jar", null, false, new STGroupDir(TEMPLATE_DIR, '$', '$'),
                  tempPath.toAbsolutePath().toString(), true);
        tempPath.resolve("test.jar");
        classLoader = new URLClassLoader(new URL[]{tempPath.toUri().toURL()}, ToStringTest.class.getClassLoader());
    }

    @AfterClass
    public static void cleanup() throws IOException {
        Files.walkFileTree(tempPath, new SimpleFileVisitor<Path>() {

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

    @Test
    public void testMultipleAttributes()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException,
                   NoSuchMethodException {
        Class<? extends XMLizable> type =
                (Class<? extends XMLizable>) classLoader.loadClass("com.sforce.soap.purchaseOrder.USAddress");
        XMLizable object = type.newInstance();
        PropertyUtils.setProperty(object, "name", "foo");
        PropertyUtils.setProperty(object, "street", "bar");
        PropertyUtils.setProperty(object, "city", "baz");
        PropertyUtils.setProperty(object, "state", "foo");
        PropertyUtils.setProperty(object, "zip", new BigDecimal(12345));
        Assert.assertEquals("[USAddress  name='foo'\n street='bar'\n city='baz'\n state='foo'\n zip='12345'\n]\n",
                            object.toString());
    }

    @Test
    public void testBigType() throws ClassNotFoundException {
        classLoader.loadClass("com.sforce.soap.purchaseOrder.BigType");
    }

}
