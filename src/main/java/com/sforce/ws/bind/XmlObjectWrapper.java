/*
 * Copyright (c) 2013, salesforce.com, inc. All rights reserved. Redistribution and use in source and binary forms, with
 * or without modification, are permitted provided that the following conditions are met: Redistributions of source code
 * must retain the above copyright notice, this list of conditions and the following disclaimer. Redistributions in
 * binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution. Neither the name of salesforce.com, inc. nor the
 * names of its contributors may be used to endorse or promote products derived from this software without specific
 * prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.sforce.ws.bind;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.*;

/**
 * @author georges.nguyen
 */
public class XmlObjectWrapper extends XmlObject {

    private TypeMapper typeMapper;
    private XMLizable xmlizable;
    private QName xsiType;

    public XmlObjectWrapper() {}

    @Override
    public XMLizable asTyped() {
        if (this.xmlizable != null) { return this.xmlizable; }

        try {
            Class<?> childTypeClass = typeMapper.getJavaType(xsiType);
            if (childTypeClass == null) {
                this.xmlizable = this;
                return this.xmlizable;
            }
            this.xmlizable = (XMLizable)childTypeClass.newInstance();
            this.xmlizable.load(createInputStream(), typeMapper);
        } catch (PullParserException e) {
            this.xmlizable = this;
        } catch (IOException e) {
            this.xmlizable = this;
        } catch (ConnectionException e) {
            this.xmlizable = this;
        } catch (InstantiationException e) {
            this.xmlizable = this;
        } catch (IllegalAccessException e) {
            this.xmlizable = this;
        }
        return this.xmlizable;
    }

    private XmlInputStream createInputStream() throws IOException, PullParserException, ConnectionException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XmlOutputStream xmlOutputStream = new XmlOutputStream(baos, false);
        super.write(getName(), xmlOutputStream, typeMapper);
        xmlOutputStream.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        XmlInputStream in = new XmlInputStream();
        in.setInput(bais, "UTF-8");
        in.peekTag();
        return in;
    }

    @Override
    public void load(XmlInputStream in, TypeMapper typeMapper) throws IOException, ConnectionException {
        this.typeMapper = typeMapper;
        this.xsiType = typeMapper.getXsiType(in);
        if (this.xsiType == null) {
            this.xmlizable = this;
        }
        super.load(in, typeMapper);
    }

}
