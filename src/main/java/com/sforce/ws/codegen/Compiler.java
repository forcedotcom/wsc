package com.sforce.ws.codegen;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

import com.sforce.ws.tools.ToolsException;
import com.sforce.ws.util.Verbose;

/**
 * 
 * @author hhildebrand
 * @since 184
 */
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

    public void compile(String[] files, File dir) throws ToolsException {
        String target = System.getProperty("compileTarget");
        if (target == null) {
            target = "1.6";
        }

        Verbose.log("Compiling to target " + target + "... ");

        String[] args = { "-g", "-d", dir.getAbsolutePath(), "-sourcepath", dir.getAbsolutePath(),
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