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
package com.sforce.ws.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import com.sforce.ws.wsdl.Constants;

/**
 * This is a minimal XML output stream, that can be used to write XML.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 30, 2005
 */
public class XmlOutputStream {
    private MXSerializer serializer = new MXSerializer();
    private OutputStream out;

    public XmlOutputStream(OutputStream out, boolean prettyPrint) throws IOException {
        this.out = out;
        serializer.setOutput(out, "UTF-8");

        if (prettyPrint) {
            serializer.setProperty(serializer.PROPERTY_SERIALIZER_INDENTATION, " ");
        }
    }

    public String getPrefix(String namespace) {
        return serializer.getPrefix(namespace, false);
    }

    public void startDocument() throws IOException {
        serializer.startDocument("UTF-8", null);
    }

    public void endDocument() throws IOException {
        serializer.endDocument();
    }

    public void setPrefix(String prefix, String namespace) throws IOException {
        serializer.setPrefix(prefix, namespace);
    }

    public void writeStartTag(String namespace, String name) throws IOException {
		serializer.startTag(namespace, name);
	}

    public void writeEndTag(String namespace, String name) throws IOException {
		serializer.endTag(namespace, name);
	}

	// writes <elem>content</elem>
	public void writeStringElement(String namespace, String name, String content) throws IOException {
        writeStartTag(namespace, name);

        if (content == null) {
            writeAttribute(Constants.SCHEMA_INSTANCE_NS, "nil", "true");
        } else {
            writeText(content);
        }
        
		writeEndTag(namespace, name);
	}
		
    public void writeAttribute(String namespace, String name, String value) throws IOException {
        serializer.attribute(namespace, name, value);
    }

    public void writeText(String text) throws IOException {
        serializer.text(text);
    }

    public void writeComment(String text) throws IOException {
        serializer.comment(text);
    }

    // access the internal writer used by the serializer, use with care!
    public Writer getWriter() {
    	return serializer.getWriter();
    }
    
    public void flush() throws IOException {
        serializer.flush();
    }

    public void close() throws IOException {
        flush();
        out.close();
    }
}
