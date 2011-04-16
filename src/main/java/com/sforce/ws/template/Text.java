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

import com.sforce.ws.util.FileUtil;

import java.io.IOException;

/**
 * This class represents
 *
 * @author http://cheenath
 * @version 1.0
 * @since 1.0  Nov 21, 2005
 */
class Text implements JspNode {
    private String[] text;

    public Text(JspTokenizer tokeniser) throws IOException {
        String t = tokeniser.getText();
        text = t.split(FileUtil.EOL);
    }

    private String clean(String t) {
        if (t == null) return null;

        t = t.replace("\n", "");
        t = t.replace("\r", "");

        String[] split = t.split("\"");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < split.length - 1; i++) {
            sb.append(split[i]);
            sb.append("\\\"");
        }

        sb.append(split[split.length - 1]);

        if (t.endsWith("\"")) {
            sb.append("\\\"");
        }

        return sb.toString();
    }

    @Override
    public void toJavaScript(StringBuilder sb) {
        for (int i = 0; i < text.length; i++) {
            sb.append(FileUtil.EOL);

            if (i < text.length - 1) {
                sb.append("out.println(");
            } else {
                sb.append("out.print(");
            }

            sb.append("\"");
            sb.append(clean(text[i]));
            sb.append("\"");
            sb.append(");");
        }
    }
}
