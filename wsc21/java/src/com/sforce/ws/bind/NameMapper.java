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
package com.sforce.ws.bind;

import java.util.HashSet;
import java.util.Arrays;


/**
 * This class maps between xml and java names.
 *
 * @author http://cheenath
 * @version 1.0
 * @since 1.0  Nov 22, 2005
 */
public class NameMapper {

    public static final HashSet<String> keywords = getKeywords();

    private static HashSet<String> getKeywords() {

        String[] keyWordArray = new String[]{
            "abstract",
            "continue",
            "for",
            "new",
            "switch",
            "assert",
            "default",
            "goto",
            "package",
            "synchronized",
            "boolean",
            "do",
            "if",
            "private",
            "this",
            "break",
            "double",
            "implements",
            "protected",
            "throw",
            "byte",
            "else",
            "import",
            "public",
            "throws",
            "case",
            "enum",
            "instanceof",
            "return",
            "transient",
            "catch",
            "extends",
            "int",
            "short",
            "try",
            "char",
            "final",
            "interface",
            "static",
            "void",
            "class",
            "finally",
            "long",
            "strictfp",
            "volatile",
            "const",
            "float",
            "native",
            "super",
            "while"};

        HashSet<String> keywords = new HashSet<String>();
        keywords.addAll(Arrays.asList(keyWordArray));
        return keywords;
    }


    /**
     * returns the java package name for the specified schema target namespace
     *
     * @param targetNamespace scheam target namespace
     * @param packagePrefix package prefix
     * @return java package name
     */
    public static String getPackageName(String targetNamespace, String packagePrefix) {
        StringBuilder packageName;
        if (targetNamespace.startsWith("urn:")) {
            packageName = new StringBuilder(targetNamespace.substring(4));
            packageName = reverse(packageName);
        } else if (targetNamespace.startsWith("http://") || targetNamespace.startsWith("https://") ){
           packageName = new StringBuilder(24).append("com.sforce.soap.").append(
                    targetNamespace.substring(targetNamespace.lastIndexOf('/')+1));
            /*
            try {
                URI uri = new URI(targetNamespace);
                String host = hostToPackage(uri.getHost());
                uri.getPath();
                packageName = new StringBuilder().append(host).append(uri.getPath());
            } catch (URISyntaxException e) {
            }
            */
        } else {
        	packageName = new StringBuilder(targetNamespace);
        }

        if (packagePrefix != null) {
            packageName.append(".").append(packagePrefix);
        }

        String pkg = packageName.toString().replace("/", ".");
        if (pkg.endsWith(".")) {
            pkg = pkg.substring(0, pkg.length()-1);
        }
        return pkg;
    }

    public static String getFieldName(String name) {
        if (keywords.contains(name)) {
            name = "_" + name;
        }
        return name;
    }

    private static StringBuilder reverse(CharSequence packageName) {
        String[] names = packageName.toString().split("\\.");
        StringBuilder sb = new StringBuilder();

        for (int i = names.length - 1; i > 0; i--) {
            sb.append(names[i]);
            sb.append(".");
        }
        sb.append(names[0]);
        return sb;
    }

    /**
     * return the method name for the specified field name.
     *
     * @param name field name
     * @return method name. First letter of the method name will be upper case.
     */
    public static String getMethodName(String name) {
        return upperCaseFirstLetterName(name);
    }

    /**
     * return the class name for the specified xml type.
     *
     * @param name xml complex type
     * @return class name. First letter of the class name will be upper case.
     */
    public static String getClassName(String name) {
        return upperCaseFirstLetterName(name);
    }

    private static String upperCaseFirstLetterName(String name) {
        String className;

        char ch = name.charAt(0);

        if (Character.isUpperCase(ch)) {
            className = name;
        } else {
            className = Character.toUpperCase(ch) + name.substring(1, name.length());
        }

        return className;
    }
}
