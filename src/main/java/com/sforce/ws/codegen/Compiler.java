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
                "-target", target, "-source", target };

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
