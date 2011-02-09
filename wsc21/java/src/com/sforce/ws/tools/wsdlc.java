/*
 * Copyright (c) 2005, salesforce.com, inc.
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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.jar.*;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.template.Template;
import com.sforce.ws.template.TemplateException;
import com.sforce.ws.util.FileUtil;
import com.sforce.ws.util.Verbose;
import com.sforce.ws.wsdl.*;

/**
 * wsdlc is a tool that can generate java stubs from WSDL.
 *
 * @author http://cheenath
 * @version 1.0
 * @since 1.0  Nov 22, 2005
 */
public class wsdlc {

    private File tempDir;
    private TypeMapper typeMapper = new TypeMapper();
    private ArrayList<String> javaFiles = new ArrayList<String>();
    private String packagePrefix = null;
    private boolean laxMinOccursMode;
    private static final String LAX_MINOCCURS = "lax-minoccurs-checking";
    private static final String PACKAGE_PREFIX = "package-prefix";
    private static final String SOBJECT_TEMPLATE = "com/sforce/ws/tools/sobject.template";
    private static final String AGG_RESULT_TEMPLATE = "com/sforce/ws/tools/aggregateResult.template";


    public wsdlc(String wsdlFile, String jarFile, String temp)
            throws ToolsException, WsdlParseException, IOException, TemplateException {

        checkTargetFile(jarFile);
        createTempDir(temp);
        Verbose.log("Created temp dir: " + tempDir.getAbsolutePath());

        packagePrefix = System.getProperty(PACKAGE_PREFIX);
        laxMinOccursMode = System.getProperty(LAX_MINOCCURS) != null;
        typeMapper.setPackagePrefix(packagePrefix);

        Definitions definitions = WsdlFactory.create(wsdlFile);
        SfdcApiType type = definitions.getApiType();
        Types types = definitions.getTypes();

        generateTypes(types);
        generateConnection(definitions);
        generateConnector(definitions);

        if (type != null && type.getSobjectNamespace() != null) {
            generateSObject(definitions);
            generateAggregateResult(definitions);
        }

        compileTypes();
        generateJarFile(jarFile);

        if (Boolean.parseBoolean(System.getProperty("del-temp-dir", "true"))) {
            Verbose.log("Delete temp dir: " + tempDir.getAbsolutePath());
            Verbose.log("Set system property del-temp-dir=false to not delete temp dir.");
            FileUtil.deleteDir(tempDir);
        }
    }

    private void createTempDir(String temp) throws IOException {
        if (temp == null) {
            tempDir = File.createTempFile("wsdlc-temp-", "-dir", null);
            tempDir.delete();
            tempDir.mkdir();
        } else {
            tempDir = new File(temp);
        }
    }

    private void checkTargetFile(String jarFile) throws ToolsException {
        if (!jarFile.endsWith(".jar")) {
            throw new ToolsException("<jar-file> must have a .jar extension");
        }

        File jf = new File(jarFile);

        if (jf.exists()) {
            throw new ToolsException("<jar-file> already exists");
        }

        File parentDir = jf.getParentFile();

        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new ToolsException("<jar-file> path does not exist");
        }
    }


    private void generateConnector(Definitions definitions) throws IOException, TemplateException {
        ConnectionGenerator connectionGenerator =
                new ConnectionGenerator(definitions, tempDir, typeMapper, packagePrefix);
        String file = connectionGenerator.generateConnector();
        javaFiles.add(file);
    }

    private void generateConnection(Definitions definitions) throws IOException, TemplateException {
        ConnectionGenerator connectionGenerator = new ConnectionGenerator(definitions, tempDir, typeMapper, packagePrefix);
        String file = connectionGenerator.generateConnection();
        javaFiles.add(file);
    }

    private void generateAggregateResult(Definitions definitions) throws IOException, TemplateException {
        if (definitions.getApiType() == SfdcApiType.Enterprise) {

            String packageName = NameMapper.getPackageName(
                    definitions.getApiType().getSobjectNamespace(), packagePrefix);

            File dir = FileUtil.mkdirs(packageName, tempDir);
            Template template = new Template();
            template.setProperty("packageName", packageName);
            String className = "AggregateResult";
            File javaFile = new File(dir, className + ".java");
            template.exec(AGG_RESULT_TEMPLATE, javaFile.getAbsolutePath());
            javaFiles.add(javaFile.getAbsolutePath());
        }
    }




    private void generateSObject(Definitions definitions) throws IOException, TemplateException {
        if (definitions.getApiType() == SfdcApiType.Partner || definitions.getApiType() == SfdcApiType.CrossInstance
                || definitions.getApiType() == SfdcApiType.Internal || definitions.getApiType() == SfdcApiType.ClientSync
                || definitions.getApiType() == SfdcApiType.SyncApi) {
            String packageName = NameMapper.getPackageName(definitions.getApiType().getSobjectNamespace(), packagePrefix);
            File dir = FileUtil.mkdirs(packageName, tempDir);
            Template template = new Template();
            template.setProperty("packageName", packageName);
            String className = "SObject";
            File javaFile = new File(dir, className + ".java");
            template.exec(SOBJECT_TEMPLATE, javaFile.getAbsolutePath());
            javaFiles.add(javaFile.getAbsolutePath());
        }
    }

    private void generateJarFile(String jarFile) throws IOException {
        Verbose.log("Generating jar file ... " + jarFile);
        FileOutputStream out = new FileOutputStream(jarFile);
        InputStream manifestIo = getManifest();
        Manifest manifest = new Manifest(manifestIo);
        JarOutputStream jar = new JarOutputStream(out, manifest);

        int rootLen = tempDir.getAbsolutePath().length();

        int len = "java".length();
        for (String javaFile : javaFiles) {
            String classFile = javaFile.substring(0, javaFile.length() - len) + "class";
            String className = classFile.substring(rootLen + 1);
            addFileToJar(className, classFile, jar);

            String javaName = javaFile.substring(rootLen + 1);
            addFileToJar(javaName, javaFile, jar);
        }

        if (Boolean.parseBoolean(System.getProperty("standalone-jar", "false"))) {
            Verbose.log("Adding runtime classes to the jar");
            addRuntimeClasses(jar);
        } else {
            Verbose.log("To include runtime classes in the generated jar " +
                        "please set system property standalone-jar=true");
        }

        jar.close();
        out.close();
        Verbose.log("Generated jar file " + jarFile);
    }

    private void addFileToJar(String className, String classFile, JarOutputStream jar) throws IOException {
        className = className.replace('\\', '/');
        FileInputStream fio = new FileInputStream(classFile);
        jar.putNextEntry(new JarEntry(className));

        int cb;
        byte [] buffer = new byte[8192];
        while ((cb = fio.read(buffer)) != -1) {
            jar.write(buffer, 0, cb);
        }

        jar.closeEntry();
        fio.close();
    }

    private void addRuntimeClasses(JarOutputStream jar) throws IOException {
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
                jar.write((char) ch);
            }
            jar.closeEntry();
            in.close();
        }
    }

    private ArrayList<String> getRuntimeClasses(ClassLoader cl) throws IOException {

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

    private InputStream getManifest() {
        String m = "Manifest-Version: 1.0\n" +
                   "Created-By: 1.4.2_05-b04 (Sun Microsystems Inc.)\n";

        return new ByteArrayInputStream(m.getBytes());
    }

    private void compileTypes() throws ToolsException {
        Compiler compiler = new Compiler();
        compiler.compile(javaFiles.toArray(new String[javaFiles.size()]));
    }

    private void generateTypes(Types types) throws IOException, TemplateException {
        Verbose.log("Generating Java files from schema ...");
        Iterator<Schema> schemas = types.getSchemas();

        while (schemas.hasNext()) {
            generate(types, schemas.next());
        }

        Verbose.log("Generated " + javaFiles.size() + " java files.");
    }

    private void generate(Types types, Schema schema) throws IOException, TemplateException {
        Iterator<ComplexType> complexTypes = schema.getComplexTypes();
        while (complexTypes.hasNext()) {
            ComplexType complexType = complexTypes.next();
            if (!typeMapper.isWellKnownType(complexType.getSchema().getTargetNamespace(), complexType.getName())) {
                ComplexTypeGenerator typeGenerator =
                        new ComplexTypeGenerator(types, schema, complexType, tempDir, typeMapper, laxMinOccursMode);
                String file = typeGenerator.generate();
                javaFiles.add(file);
            }
        }

        Iterator<SimpleType> simpleTypes = schema.getSimpleTypes();
        while (simpleTypes.hasNext()) {
            SimpleType simpleType = simpleTypes.next();
            if (!typeMapper.isWellKnownType(simpleType.getSchema().getTargetNamespace(), simpleType.getName())) {
                SimpleTypeGenerator typeGenerator =
                        new SimpleTypeGenerator(types, schema, simpleType, tempDir, typeMapper);
                String file = typeGenerator.generate();
                javaFiles.add(file);
            }
        }
    }

    public static void main(String[] args)
            throws WsdlParseException, IOException, TemplateException {
        try {
            run(args);
        } catch (ToolsException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    static void run(String[] args) throws ToolsException, WsdlParseException, IOException, TemplateException {
        if (args.length == 2) {
            new wsdlc(args[0], args[1], null);
        } else if (args.length == 3) {
            new wsdlc(args[0], args[1], args[2]);
        } else {
            throw new ToolsException(" usage: java com.sforce.ws.tools.wsdlc <wsdl-file> <jar-file> [temp-dir]");
        }
    }

    class Compiler {
        private Object main;
        private Method method;

        public Compiler() throws ToolsException {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();

            if (loader == null) {
                loader = getClass().getClassLoader();
            }

            try {
                findCompiler(loader);
            } catch (ClassNotFoundException e) {
                findCompilerInToolsJar(loader);
            } catch (NoSuchMethodException e) {
                throwToolsexception(e);
            } catch (IllegalAccessException e) {
                throwToolsexception(e);
            } catch (InstantiationException e) {
                throwToolsexception(e);
            }
        }

        private void findCompilerInToolsJar(ClassLoader loader) throws ToolsException {
            try {
                ToolsJarClassLoader tloader = new ToolsJarClassLoader(loader);
                findCompiler(tloader);
           } catch (MalformedURLException e) {
                throwToolsexception(e);
            } catch (NoSuchMethodException e) {
                throwToolsexception(e);
            } catch (IllegalAccessException e) {
                throwToolsexception(e);
            } catch (InstantiationException e) {
                throwToolsexception(e);
            } catch (ClassNotFoundException e) {
                throwToolsexception(e);
            } catch (IOException e) {
                throwToolsexception(e);
            }
        }

        @SuppressWarnings("unchecked")
        private void findCompiler(ClassLoader loader)
                throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {

            Class c = loader.loadClass("com.sun.tools.javac.Main");
            Class arg = (new String[0]).getClass();
            main = c.newInstance();
            method = c.getMethod("compile", arg);
        }

        private void throwToolsexception(Exception e) throws ToolsException {
            e.printStackTrace();
            throw new ToolsException("Unable to find compiler. Make sure that tools.jar is in your classpath: " + e);
        }

        public void compile(String[] files) throws ToolsException {
            String target = System.getProperty("compileTarget");
            if (target == null) {
                target = "1.6";
            }

            Verbose.log("Compiling to target " + target + "... ");

            String[] args = { "-g", "-d", tempDir.getAbsolutePath(), "-sourcepath", tempDir.getAbsolutePath(),
                    "-target", target };

            String[] call = new String[args.length + files.length];

            System.arraycopy(args, 0, call, 0, args.length);
            System.arraycopy(files, 0, call, args.length, files.length);


            try {
                Integer result = (Integer) method.invoke(main, new Object[]{call});
                if (result != 0) {
                    throw new ToolsException("Failed to compile");
                }
            } catch (IllegalAccessException e) {
                throw new ToolsException("Failed to compile: " + e);
            } catch (InvocationTargetException e) {
                throw new ToolsException("Failed to compile: " + e);
            }

            Verbose.log("Compiled " + files.length + " java files.");
        }
    }

    public static class ToolsJarClassLoader extends ClassLoader {

        private JarFile toolsJar;

        ToolsJarClassLoader(ClassLoader parent) throws IOException {
            super(parent);
            String javaHome = System.getProperty("java.home");
            if (javaHome.endsWith("jre")) {
                javaHome = javaHome.substring(0, javaHome.length() - 3);
            }
            if (!javaHome.endsWith("/")) {
                javaHome = javaHome + "/";
            }
            String tj = javaHome + "lib/tools.jar";
            File tjf = new File(tj);
            toolsJar = new JarFile(tjf);
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] b;
            try {
                b = getBytes(name);
            } catch (IOException e) {
                throw new ClassNotFoundException(name);
            }
            return defineClass(null, b, 0, b.length);
        }

        private byte[] getBytes(String name) throws IOException, ClassNotFoundException {
            name = name.replace(".", "/");
            name += ".class";
            JarEntry entry = toolsJar.getJarEntry(name);

            if (entry == null) {
                throw new ClassNotFoundException(name);
            }

            InputStream io = toolsJar.getInputStream(entry);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int ch;
            while((ch=io.read()) != -1) {
                bout.write((char)ch);
            }
            io.close();

            return bout.toByteArray();
        }
    }
}
