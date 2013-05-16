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

package com.sforce.ws.wsdl;

import java.io.*;
import java.net.URL;

import com.sforce.ws.parser.XmlInputStream;
/**
 * @author cheenath.com
 * @version 1.0
 * @since 1.0   Nov 5, 2005
 */
public class WsdlFactory {

    /**
     * @param url url of the wsdl
     * @return parsed definitions
     * @throws WsdlParseException failed to parse wsdl
     * @throws  
     */
    public static Definitions create(URL url) throws WsdlParseException, IOException {
        InputStream in = url.openStream();
        try {
            return createFromInputStream(in);
        } finally {
            closeQuietly(in);
        }
    }

    public static Definitions createFromString(String wsdl) throws WsdlParseException {
        ByteArrayInputStream in = new ByteArrayInputStream(wsdl.getBytes());
        try {
            return createFromInputStream(in);
        } finally {
            closeQuietly(in);
        }
    }

    private static Definitions createFromInputStream(InputStream in) throws WsdlParseException {
        XmlInputStream parser = new XmlInputStream();
        WsdlParser wsdlParser = new WsdlParser(parser);
        Definitions definitions = new Definitions();
        wsdlParser.setInput(in, "UTF-8");
        definitions.read(wsdlParser);
        return definitions;
    }

    private static void closeQuietly(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignored) {
                // ignore IOException while closing stream
            }
        }
    }
}
