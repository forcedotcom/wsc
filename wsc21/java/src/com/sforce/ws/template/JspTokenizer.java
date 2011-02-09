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


import java.io.IOException;
import java.io.InputStream;

/**
 * This class breaks input stream in to JSP tokens. It only supports
 * a subset of JSP tokens.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 12, 2005
 */
class JspTokenizer {

    private InputStream in;

    public JspTokenizer(InputStream in) {
        this.in = in;
    }

    public Token nextToken() throws IOException {
        in.mark(5);
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        in.reset();

        if (ch1 == -1) return Token.EOF;

        if (ch1 == '<') {
            if (ch2 == '%') {
                if (ch3 == '-' && ch4 == '-') {      // <%--
                    consume(4);
                    return Token.START_COMMENT;
                }
                if (ch3 == '=') {                    // <%=
                    consume(3);
                    return Token.START_EXPRESSION;
                }
                if (ch3 == '@') {                    // <%@
                    consume(3);
                    return Token.START_DIRECTIVES;
                }
                if (ch3 == '!') {                    // <%!
                    consume(3);
                    return Token.START_DECLARATION;
                }
                consume(2);                          // <%
                return Token.START_SCRIPTLET;
            }
        }

        if (ch1 == '-' && ch2 == '-' && ch3 == '%' && ch4 == '>') {
            consume(4);
            return Token.END_COMMENT;
        }

        if (ch1 == '%' && ch2 == '>') {
            consume(2);
            return Token.END_TAG;
        }

        return Token.TEXT;
    }

    private void consume(int i) throws IOException {
        while (i > 0) {
            in.read();
            i--;
        }
    }

    public String getText() throws IOException {
        StringBuilder sb = new StringBuilder();

        while (true) {
            in.mark(3);
            int ch1 = in.read();
            int ch2 = in.read();
            in.reset();

            if ((ch1 == -1) ||
                    (ch1 == '<' && ch2 == '%') ||
                    (ch1 == '%' && ch2 == '>')) {
                break;
            }

            sb.append((char) in.read());
        }

        return sb.toString();
    }
}
