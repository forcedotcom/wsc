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
package com.sforce.ws.wsdl;

import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.PullParserException;
import com.sforce.ws.ConnectionException;

import java.io.InputStream;
import java.io.IOException;

/**
 * WsdlParser
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Jan 20, 2006
 */
public class WsdlParser {

     private XmlInputStream in;

    public WsdlParser(XmlInputStream in) {
        this.in = in;
    }


    public void setInput(InputStream inputStream, String inputEncoding) throws WsdlParseException {
        try {
            in.setInput(inputStream, inputEncoding);
        } catch (PullParserException e) {
            throw new WsdlParseException("Failed to set input", e);
        }
    }

    public String getNamespace(String prefix) {
        return in.getNamespace(prefix);
    }

    public String getPositionDescription() {
        return in.getLineNumber() + ":" + in.getColumnNumber();
    }

    public String getNamespace() {
        return in.getNamespace();
    }

    public String getName() {
        return in.getName();
    }

    public String getAttributeValue(String namespace, String name) {
        return in.getAttributeValue(namespace, name);
    }

    public int getEventType() throws WsdlParseException {
        try {
            return in.getEventType();
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        }
    }

    public int next() throws WsdlParseException {
        try {
            return in.next();
        } catch (IOException e) {
            throw new WsdlParseException(e);
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        }
    }

    @Override
    public String toString() {
        return "WsdlParser: " + in.toString();
    }

    public String nextText() throws WsdlParseException {
        try {
            return in.nextText();
        } catch (IOException e) {
            throw new WsdlParseException(e);
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        }
    }

    public int nextTag() throws WsdlParseException {
        try {
            return in.nextTag();
        } catch (IOException e) {
            throw new WsdlParseException(e);
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        }
    }

    public int peekTag() throws WsdlParseException {
        try {
            return in.peekTag();
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        } catch (IOException e) {
            throw new WsdlParseException(e);
        }
    }
}
