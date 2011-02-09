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
package com.sforce.ws.template;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to execute a template. The common usage for this
 * class is shown below:
 * <p/>
 * <code>
 * Template template = new Template();
 * <p/>
 * //set up implicit variables
 * template.setProperty("name", value);
 * <p/>
 * String templateFileName = ... //name of the template file
 * String outputFile = ... //name of the file to the generated
 * <p/>
 * //exec the template
 * template.exec(templateFileName, outputFile);
 * <p/>
 * </code>
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 22, 2005
 */
public class Template {

    private HashMap<String,Object> properties = new HashMap<String,Object>();

    /**
     * sets up implicit variable for the script. You can use this variable from the script.
     * You can also invoke methods associated with the variable.
     *
     * @param name  name of the variable
     * @param value value of the variable.
     */
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    /**
     * execute the specified template and generate the specified output file.
     *
     * @param in  name of the template file.
     * @param out name of the output file. If null, the output will be redirected to System.out
     * @throws IOException       failed to read/write file
     * @throws TemplateException failed to exec template
     */
    public void exec(String in, String out) throws IOException, TemplateException {
        InputStream fio = createInputStream(in);

        PrintStream os = null;

        try {
            if (out != null) {
                File o = new File(out);

                if (o.exists()) {
                    throw new TemplateException("Output file already exists: " + out);
                }

                os = new PrintStream(new BufferedOutputStream(new FileOutputStream(o)));
            } else {
                os = System.out;
            }

            generate(fio, os);
        } finally {
            if (out != null && os != null) {
                os.close();
            }
            if (fio != null) {
                fio.close();
            }
        }
    }

    private InputStream createInputStream(String in) throws FileNotFoundException {
        InputStream is;

        File ifile = new File(in);
        if (ifile.exists()) {
            is = new FileInputStream(in);
        } else {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = getClass().getClassLoader();
            }
            is = cl.getResourceAsStream(in);
            if (is == null) {
                throw new FileNotFoundException("Unable to find file: " + in);
            }
        }

        return is;
    }

    private void generate(InputStream in, PrintStream out) throws IOException, TemplateException {
        JspParser parser = new JspParser(in);
        String js = parser.toJavaScript();

        try {
            Context context = Context.enter();
            Scriptable scope = context.initStandardObjects();
            scope.put("out", scope, out);
            addProperties(scope);
            context.evaluateString(scope, js, "<template>", 1, null);
        } catch (RuntimeException th) {
            report(js, th);
            throw th;
        } finally {
            Context.exit();
        }
    }

    private void addProperties(Scriptable scope) {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            scope.put(entry.getKey(), scope, entry.getValue());
        }
    }

    private void report(String js, RuntimeException th) {
        System.out.println("JavaScript evaluation failed.");
        System.out.println("---------------- Java Script Source ---------------");
        print(js);
        System.out.println("---------------------------------------------------");
        System.out.println("Due to:" + th);
    }

    private void print(String js) {
        String[] jsArray = js.split("\r\n");

        for (int i = 0; i < jsArray.length; i++) {
            System.out.print(lineNo(i + 1));
            System.out.println(jsArray[i]);
        }
    }

    private String lineNo(int i) {
        if (i < 10) {
            return "   " + i + ":  ";
        }
        if (i < 100) {
            return "  " + i + ":  ";
        }
        if (i < 1000) {
            return " " + i + ":  ";
        }
        return i + ":  ";
    }
}
