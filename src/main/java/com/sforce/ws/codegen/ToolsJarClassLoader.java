package com.sforce.ws.codegen;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author hhildebrand
 * @since 184
 */
class ToolsJarClassLoader extends ClassLoader {

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

        if (entry == null) { throw new ClassNotFoundException(name); }

        InputStream io = toolsJar.getInputStream(entry);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int ch;
        while ((ch = io.read()) != -1) {
            bout.write((char)ch);
        }
        io.close();

        return bout.toByteArray();
    }
}