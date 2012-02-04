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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * This is a parser for JSP style templates.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 9, 2005
 */
class JspParser {

  private ArrayList<JspNode> nodes = new ArrayList<JspNode>();

  public JspParser(InputStream in) throws IOException, TemplateException {
    BufferedInputStream bio = new BufferedInputStream(in);
    JspTokenizer tokenizer = new JspTokenizer(bio);

    boolean done = false;

    while (!done) {
      Token token = tokenizer.nextToken();

      switch (token) {
        case TEXT:
          Text text = new Text(tokenizer);
          nodes.add(text);
          break;
        case START_SCRIPTLET:
          Scriptlet scriptlet = new Scriptlet(tokenizer);
          nodes.add(scriptlet);
          break;
        case START_EXPRESSION:
          Expression expression = new Expression(tokenizer);
          nodes.add(expression);
          break;
        case EOF:
          done = true;
          break;
        case END_COMMENT:
          break;
        case START_COMMENT:
          break;
        case START_DECLARATION:
          break;
        case START_DIRECTIVES:
          break;
        case END_TAG:
          throw new TemplateException("Found unexpected token:" + token);
        default:
          throw new InternalError("unknown token: " + token);
      } //switch(token)
    } //while(!done)
  }

  public String toJavaScript() {
    StringBuilder sb = new StringBuilder();

    for (JspNode node : nodes) {
      node.toJavaScript(sb);
    }
    
    return sb.toString();
  }
}
