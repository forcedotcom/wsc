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

package com.sforce.ws.bind;

import com.sforce.ws.parser.XmlOutputStream;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.ConnectionException;

import javax.xml.namespace.QName;
import java.io.IOException;

/**
 * This interface is used to mark a class as de/serializable to XML.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 30, 2005
 */
public interface XMLizable {

    /**
     * write this instace as xml.
     *
     * @param element    xml element name
     * @param out        xml output stream
     * @param typeMapper type mapper to be used
     * @throws IOException failed to write xml
     */
    void write(QName element, XmlOutputStream out, TypeMapper typeMapper) throws IOException;

    /**
     * load the fileds/children from the specified xml stream
     *
     * @param in         xml input stream from which the data is read
     * @param typeMapper type mapper to be used
     * @throws IOException         failed to read xml
     * @throws ConnectionException failed to read/parser/bind xml
     */
    void load(XmlInputStream in, TypeMapper typeMapper) throws IOException, ConnectionException;
}
