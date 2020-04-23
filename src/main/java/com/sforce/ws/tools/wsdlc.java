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

package com.sforce.ws.tools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.sforce.ws.codegen.FactoryMetadataConstructor;
import com.sforce.ws.codegen.metadata.ConnectionWrapperClassMetadata;
import com.sforce.ws.codegen.metadata.FactoryClassMetadata;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import com.sforce.ws.codegen.ConnectionMetadataConstructor;
import com.sforce.ws.codegen.Generator;
import com.sforce.ws.codegen.metadata.ConnectionClassMetadata;
import com.sforce.ws.codegen.metadata.ConnectorMetadata;
import com.sforce.ws.util.FileUtil;
import com.sforce.ws.wsdl.*;

/**
 * wsdlc is a tool that can generate java stubs from WSDL.
 * 
 * @author btoal
 * @author hhildebrand
 */
public class wsdlc extends Generator {
    private static final String CONNECTOR_JAVA = "Connector.java";

    public static final String TEMPLATE_DIR = String.format("%s/templates", Generator.class.getPackage().getName()
            .replace('.', '/'));

    static final String STANDALONE_JAR = "standalone-jar";
    static final String PACKAGE_PREFIX = "package-prefix";
    static final String JAVA_TIME = "java-time";

    public final static String CONNECTION = "connection";
    public final static String CONNECTOR = "connector";
    public final static String CONNECTION_WRAPPER = "connectionWrapper";
    public final static String INTERFACE_CONNECTION_WRAPPER = "iconnectionWrapper";
    public final static String FACTORY = "factory";
    public final static String IFACTORY = "ifactory";

    public static void main(String[] args) throws Exception {
        try {
            run(args);
        } catch (ToolsException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public static void run(
            String wsdlUrl,
            String destJarFilename,
            String packagePrefix,
            boolean javaTime,
            boolean standAlone,
            STGroupDir templates,
            String destDir,
            boolean compile
    ) throws ToolsException, WsdlParseException, IOException {

        wsdlc wsc = new wsdlc(packagePrefix, templates, javaTime);
        File destJar = new File(destJarFilename);
        if (destJar.exists()) {
            if (!destJar.delete()) {
                throw new ToolsException(String.format(
                        "Output Jar file exists and cannot be deleted: %s", destJar.getAbsolutePath()));
            }
        }


        if (destJar.getParentFile() != null && !destJar.getParentFile().exists()) {
            if (!destJar.getParentFile().mkdirs() && !destJar.getParentFile().exists()) {
                // only throw exception if mkdirs returns false and directory does not exist to 
                // prevent build failures when multiple instances of wsdlc are invoked in parallel
                throw new ToolsException(String.format(
                        "Cannot create jar file directory: %s", destJar.getParentFile().getAbsolutePath()));
            }
        }
        URL wsdl;
        try {
            wsdl = new URL(wsdlUrl);
        } catch (MalformedURLException e) {
            try {
                wsdl = new URL(String.format("file:%s", wsdlUrl));
            } catch (MalformedURLException e2) {
                throw e;
            }
        }
        File destDirectory;
        if (destDir == null) {
            destDirectory = File.createTempFile("wsc-scratch", "tmp");
            FileUtil.deleteDir(destDirectory);
        } else {
            destDirectory = new File(destDir);
        }
        destDirectory.mkdirs();
        try {
            wsc.generate(wsdl, destDirectory);
            if (compile) {
                wsc.compileTypes();
                wsc.generateJarFile(destJar, standAlone, destDirectory);
            }
        } finally {
            if (destDir == null) {
                FileUtil.deleteDir(destDirectory);
            }
        }
    }

    static void run(String[] args) throws Exception {
        if (args.length < 2 || args.length > 4) { throw new ToolsException(
                " usage: java com.sforce.ws.tools.wsdlc -nc <wsdl-file> <jar-file> <dest-dir>"); }
        boolean compile = true;
        String destJarFilename = null;
        String wsdlUrl = null;
        String destDir = null;
        for (String arg : args) {
            if (arg.equals("-nc")) {
                compile = false;
            } else if (wsdlUrl == null) {
                wsdlUrl = arg;
            } else if (destJarFilename == null) {
                destJarFilename = arg;
            } else if (destDir == null) {
                destDir = arg;
            } else {
                throw new ToolsException(" usage: java com.sforce.ws.tools.wsdlc -nc <wsdl-file> <jar-file> <dest-dir>");
            }
        }
        String packagePrefix = System.getProperty(PACKAGE_PREFIX);
        boolean standAlone = Boolean.parseBoolean(System.getProperty(STANDALONE_JAR, "false"));
        boolean javaTime = Boolean.parseBoolean(System.getProperty(wsdlc.JAVA_TIME, "false"));
        STGroupDir stGroupDir = new STGroupDir(TEMPLATE_DIR, '$', '$');
        run(wsdlUrl, destJarFilename, packagePrefix, javaTime, standAlone, stGroupDir, destDir, compile);
    }

    public wsdlc(String packagePrefix, STGroupDir templates, boolean javaTime) {
        super(packagePrefix, templates, packagePrefix, javaTime);
    }

    public wsdlc(String packagePrefix, STGroupDir templates) {
        super(packagePrefix, templates, packagePrefix, false);
    }

    private void generateConnectionClasses(Definitions definitions, File dir) throws IOException {
        ConnectionClassMetadata gen = new ConnectionMetadataConstructor(definitions, typeMapper, packagePrefix)
                .getConnectionClassMetadata();
        ST template = templates.getInstanceOf(CONNECTION);
        javaFiles.add(generate(gen.getPackageName(), gen.getClassName() + ".java", gen, template, dir));
    }

    private void generateConnectionWrapperClasses(Definitions definitions, File dir)
            throws IOException {
        ConnectionClassMetadata connectionMetadata = new ConnectionMetadataConstructor(
                definitions, typeMapper, packagePrefix)
                .getConnectionClassMetadata();
        ConnectionWrapperClassMetadata gen = new ConnectionWrapperClassMetadata(connectionMetadata.getPackageName(), connectionMetadata.getClassName() + "Wrapper", null, connectionMetadata);
        ST template = templates.getInstanceOf(CONNECTION_WRAPPER);
        File wrapperFile = generate(gen.getPackageName(), gen.getClassName()
                + ".java", gen, template, dir);
        javaFiles.add(wrapperFile);

        template = templates.getInstanceOf(INTERFACE_CONNECTION_WRAPPER);
        javaFiles.add(generate(gen.getPackageName(), gen.getInterfaceName()
                + ".java", gen, template, dir));
    }

    private void generateFactoryClasses(Definitions definitions, File dir) throws IOException {
        FactoryClassMetadata gen = new FactoryMetadataConstructor(definitions, typeMapper, packagePrefix)
                .getFactoryClassMetadata();
        ST template = templates.getInstanceOf(FACTORY);
        javaFiles.add(generate(gen.getPackageName(), gen.getClassName() + ".java", gen, template, dir));

        template = templates.getInstanceOf(IFACTORY);
        javaFiles.add(generate(gen.getPackageName(), gen.getInterfaceName() + ".java", gen, template, dir));
    }

    private void generateConnectorClasses(Definitions definitions, File dir) throws IOException {
        ConnectorMetadata gen = new ConnectorMetadata(definitions, packagePrefix);
        ST template = templates.getInstanceOf(CONNECTOR);
        javaFiles.add(generate(gen.getPackageName(), CONNECTOR_JAVA, gen, template, dir));
    }

    @Override
    protected void generate(Definitions definitions, SfdcApiType sfdcApiType, Types types, File dir) throws IOException {
        super.generate(definitions, sfdcApiType, types, dir);
        generateConnectionClasses(definitions, dir);
        generateConnectorClasses(definitions, dir);

        if (generateInterfaces) {
            generateConnectionWrapperClasses(definitions, dir);
            generateFactoryClasses(definitions, dir);
        }
    }
}
