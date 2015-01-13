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

package com.sforce.ws.codegen;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.*;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.codegen.metadata.*;
import com.sforce.ws.tools.ToolsException;
import com.sforce.ws.util.FileUtil;
import com.sforce.ws.util.Verbose;
import com.sforce.ws.wsdl.*;

/**
 * @author hhildebrand
 * @since 184
 */
abstract public class Generator {
    private static final int BUFFER_SIZE_16_KIB = 16 * 1024;

    private static final String AGGREGATE_RESULT_JAVA = "AggregateResult.java";
    private static final String SOBJECT_JAVA = "SObject.java";
    private static final String ISOBJECT_JAVA = "ISObject.java";

    /**
     * Template names
     */
    public final static String AGGREGATE_RESULT = "aggregateResult";
    public final static String SIMPLE_TYPE = "simpleType";
    public final static String SOBJECT = "sobject";
    public final static String ISOBJECT = "isobject";

    public final static String TYPE = "type";
    public final static String TYPE_INTERFACE = "typeinterface";

    protected final TypeMapper typeMapper = new TypeMapper();
    protected final ArrayList<File> javaFiles = new ArrayList<File>();
    protected final String packagePrefix;
    protected final String interfacePackagePrefix;

    protected final STGroupDir templates;
    protected boolean generateInterfaces;


    public Generator(String packagePrefix, STGroupDir templates, String interfacePackagePrefix) throws Exception {
        this(packagePrefix, templates, interfacePackagePrefix, '$', '$');
    }

    public Generator(String packagePrefix, STGroupDir templates, String interfacePackagePrefix, char startDelim, char endDelim) throws Exception {
        this.templates = templates;
        this.packagePrefix = packagePrefix;
        this.interfacePackagePrefix = interfacePackagePrefix;
        typeMapper.setPackagePrefix(packagePrefix);
        typeMapper.setInterfacePackagePrefix(interfacePackagePrefix);
    }

    public void generate(URL wsdl, File dest) throws WsdlParseException, ToolsException, IOException {
        Definitions definitions = WsdlFactory.create(wsdl);
        SfdcApiType sfdcApiType = definitions.getApiType();
        generateInterfaces = sfdcApiType == SfdcApiType.Partner;
        typeMapper.setGenerateInterfaces(generateInterfaces);
        Types types = definitions.getTypes();
        generate(definitions, sfdcApiType, types, dest);
    }

    protected void addFileToJar(String className, String classFile, JarOutputStream jar) throws IOException {
        className = className.replace('\\', '/');
        FileInputStream fio = new FileInputStream(classFile);
        jar.putNextEntry(new JarEntry(className));

        int cb;
        byte[] buffer = new byte[8192];
        while ((cb = fio.read(buffer)) != -1) {
            jar.write(buffer, 0, cb);
        }

        jar.closeEntry();
        fio.close();
    }

    protected void addRuntimeClasses(JarOutputStream jar) throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = getClass().getClassLoader();
        }
        ArrayList<String> runtimeClasses = getRuntimeClasses(cl);
        for (String c : runtimeClasses) {
            jar.putNextEntry(new JarEntry(c));
            InputStream in = cl.getResourceAsStream(c);
            int ch;
            while ((ch = in.read()) != -1) {
                jar.write((char)ch);
            }
            jar.closeEntry();
            in.close();
        }
    }

    protected void compileTypes(File dir) throws ToolsException {
        Compiler compiler = new Compiler();
        List<String> fileNames = new ArrayList<String>();
        for (File f : javaFiles) {
            fileNames.add(f.getPath());
        }
        compiler.compile(fileNames.toArray(new String[fileNames.size()]), dir);
    }

    protected void generate(Definitions definitions, SfdcApiType sfdcApiType, Types types, File dir) throws IOException {
        generateTypeClasses(types, dir);
        if (sfdcApiType != null && sfdcApiType.getSobjectNamespace() != null) {
            if (requiresSObjectClass(definitions)) {
                generateSObjectClass(definitions, dir);
            }
            if (requiresSObjectInterface(definitions)) {
                generateSObjectInterface(definitions, dir);
            }
            if (requiresAggregateResultClass(definitions)) {
                generateAggregateResultClasses(definitions, dir);
            }
        }
    }

    protected File generate(String packageName, String fileName, Object gen, ST template, File dir) throws IOException {
        template.add("gen", gen);
        File source = new File(FileUtil.mkdirs(packageName, dir), fileName);
        PrintWriter out = null;
        out = new PrintWriter(new BufferedWriter(newSourceWriter(source), BUFFER_SIZE_16_KIB));
        try {
            out.print(template.render());
        } finally {
            out.close();
        }

        return source;
    }

    /**
     * Extension point for embedding applications, like Maven plugins, to provide custom I/O primitives.
     */
    protected Writer newSourceWriter(File source) throws IOException {
        return new FileWriter(source);
    }

    protected void generateAggregateResultClasses(Definitions definitions, File dir) throws IOException {
        generateAggregateResultClasses(getPackageName(definitions), dir);
    }

    protected void generateAggregateResultClasses(String packageName, File dir) throws IOException {
        ClassMetadata gen = new ClassMetadata(packageName, null);
        ST template = templates.getInstanceOf(AGGREGATE_RESULT);
        javaFiles.add(generate(packageName, AGGREGATE_RESULT_JAVA, gen, template, dir));
    }

    protected void generateClassFromComplexType(Types types, Schema schema, ComplexType complexType, File dir)
            throws IOException {
        ComplexClassMetadata gen = newTypeMetadataConstructor(types, schema, complexType, dir)
                .generateMetadata();
        ST template = templates.getInstanceOf(TYPE);
        javaFiles.add(generate(gen.getPackageName(), gen.getClassName() + ".java", gen, template, dir));
        if (generateInterfaces) {
            ST interfc = templates.getInstanceOf(TYPE_INTERFACE);
            javaFiles.add(generate(gen.getPackageName(), gen.getInterfaceName() + ".java", gen, interfc, dir));
        }
    }

    /**
     * Extension point for embedding applications, like Maven plugins, to customize TypeMetadataConstructor without
     * changing system properties.
     */
    protected TypeMetadataConstructor newTypeMetadataConstructor(Types types, Schema schema, ComplexType complexType,
            File dir) {
        return new TypeMetadataConstructor(types, schema, complexType, dir, typeMapper);
    }

    protected void generateClassFromSimpleType(Schema schema, SimpleType simpleType, File dir) throws IOException {
        SimpleClassMetadata gen = new SimpleClassMetadata(schema, simpleType, typeMapper);
        ST template = templates.getInstanceOf(SIMPLE_TYPE);
        javaFiles.add(generate(gen.getPackageName(), gen.getClassName() + ".java", gen, template, dir));
    }

    protected void generateComplexTypeClass(Types types, Schema schema, File dir) throws IOException {
        for (ComplexType complexType : schema.getComplexTypes()) {

            if (!typeMapper.isWellKnownType(complexType.getSchema().getTargetNamespace(), complexType.getName())) {
                generateClassFromComplexType(types, schema, complexType, dir);
            }
        }
    }

    protected void generateJarFile(File jarFile, boolean standAlone, File dir) throws IOException {
        Verbose.log("Generating jar file ... " + jarFile);
        FileOutputStream out = new FileOutputStream(jarFile);
        InputStream manifestIo = getManifest();
        Manifest manifest = new Manifest(manifestIo);
        JarOutputStream jar = new JarOutputStream(out, manifest);

        int rootLen = dir.getAbsolutePath().length();

        int len = "java".length();
        for (File javaFile : javaFiles) {
            String fileName = javaFile.getPath();
            String classFile = fileName.substring(0, fileName.length() - len) + "class";
            String className = classFile.substring(rootLen + 1);
            addFileToJar(className, classFile, jar);

            String javaName = fileName.substring(rootLen + 1);
            addFileToJar(javaName, fileName, jar);
        }

        if (standAlone) {
            Verbose.log("Adding runtime classes to the jar");
            addRuntimeClasses(jar);
        }

        jar.close();
        out.close();
        Verbose.log("Generated jar file " + jarFile);
    }

    protected void generateSimpleTypeClass(Schema schema, File dir) throws IOException {
        for (SimpleType simpleType : schema.getSimpleTypes()) {
            if (!typeMapper.isWellKnownType(simpleType.getSchema().getTargetNamespace(), simpleType.getName())) {
                generateClassFromSimpleType(schema, simpleType, dir);
            }
        }
    }

    protected void generateSObjectClass(Definitions definitions, File dir) throws IOException {
        String packageName = getPackageName(definitions);
        ClassMetadata gen = new ClassMetadata(packageName, null, getInterfacePackageName(packageName));
        ST template = templates.getInstanceOf(SOBJECT);
        javaFiles.add(generate(packageName, SOBJECT_JAVA, gen, template, dir));
    }

    protected String getInterfacePackageName(String packageName) {
        return packageName;
    }

    protected void generateSObjectInterface(Definitions definitions, File dir) throws IOException {
        String packageName = getPackageName(definitions);
        ClassMetadata gen = new ClassMetadata(packageName, null, getInterfacePackageName(packageName));
        ST template = templates.getInstanceOf(ISOBJECT);
        javaFiles.add(generate(packageName, ISOBJECT_JAVA, gen, template, dir));
    }


    protected void generateTypeClass(Types types, Schema schema, File dir) throws IOException {
        generateComplexTypeClass(types, schema, dir);
        generateSimpleTypeClass(schema, dir);
    }

    protected void generateTypeClasses(Types types, File dir) throws IOException {
        Verbose.log("Generating Java files from schema ...");
        for (Schema s : types.getSchemas()) {
            generateTypeClass(types, s, dir);
        }

        Verbose.log("Generated " + javaFiles.size() + " java files.");
    }

    protected InputStream getManifest() {
        String m = "Manifest-Version: 1.0\n" + "Created-By: 1.4.2_05-b04 (Sun Microsystems Inc.)\n";

        return new ByteArrayInputStream(m.getBytes());
    }

    protected String getPackageName(Definitions definitions) {
        return NameMapper.getPackageName(definitions.getApiType().getSobjectNamespace(), packagePrefix);
    }

    protected ArrayList<String> getRuntimeClasses(ClassLoader cl) throws IOException {

        ArrayList<String> classes = new ArrayList<String>();
        InputStream in = cl.getResourceAsStream("com/sforce/ws/runtime-classes.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            classes.add(line);
        }
        reader.close();

        return classes;
    }

    protected boolean requiresAggregateResultClass(Definitions definitions) {
        return definitions.getApiType() == SfdcApiType.Enterprise ||
        	   definitions.getApiType() == SfdcApiType.Tooling;
    }

    protected boolean requiresSObjectClass(Definitions definitions) {
        return definitions.getApiType() == SfdcApiType.Partner || definitions.getApiType() == SfdcApiType.CrossInstance
                || definitions.getApiType() == SfdcApiType.Internal
                || definitions.getApiType() == SfdcApiType.ClientSync
                || definitions.getApiType() == SfdcApiType.SyncApi;
    }

    protected boolean requiresSObjectInterface(Definitions definitions) {
        return requiresSObjectClass(definitions);
    }
}
