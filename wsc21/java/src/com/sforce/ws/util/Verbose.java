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
package com.sforce.ws.util;

import java.io.PrintStream;
import java.lang.reflect.Array;


/**
 * Util class to log debug messages.
 *
 * @author  http://cheenath.com
 * @version 1.0
 * @since   1.0   Nov 5, 2005
 */
public class Verbose {

  public static final String WSDL = "wsdl";
  public static final String XML = "xml";

  private static final PrintStream out = System.out;

  public static boolean isVerbose(String type) {
    return true;
  }

  public static String toString(Object o) {
    if (o == null) {
      return "null";
    }

    if (o.getClass().isArray()) {
      return toStringArray(o);
    }

    return o.toString();
  }

  private static String toStringArray(Object o) {
    StringBuilder sb = new StringBuilder();
    int length = Array.getLength(o);
    sb.append("{[").append(length).append("]");
    for (int i=0; i<length; i++) {
      sb.append(Array.get(o, i));
      sb.append(",");
    }
    sb.append("}");
    return sb.toString();
  }

  public static void log(Object message) {
    String m = toString(message);

    StackTraceElement[] stack = Thread.currentThread().getStackTrace();
    StackTraceElement element = stack[3];
    out.print("[WSC]");
    String className = element.getClassName();
    int index = className.lastIndexOf(".");
    className = className.substring(index+1);
    out.print("[");
    out.print(className);
    out.print(".");
    out.print(element.getMethodName());
    out.print(":");
    out.print(element.getLineNumber());
    out.print("]");
    out.println(m);
  }
}
