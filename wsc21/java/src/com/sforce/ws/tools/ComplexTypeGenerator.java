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
package com.sforce.ws.tools;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.wsdl.Collection;
import com.sforce.ws.wsdl.*;

/**
 * This class contains util methods used by type.template.
 *
 * @author http://cheenath
 * @version 1.0
 * @since 1.0  Nov 22, 2005
 */
public class ComplexTypeGenerator extends TypeGenerator {
    private final ComplexType complexType;
    private final boolean laxMinOccursMode;
    private static final JavaTypeMap javaTypeMap = javaTypeMap();
    private static final String TEMPLATE = "com/sforce/ws/tools/type.template";

    public ComplexTypeGenerator(Types types, Schema schema, ComplexType complexType, File tempDir, TypeMapper typeMapper, boolean laxMinOccursMode) {
        super(types, schema, complexType.getName(), tempDir, typeMapper);
        this.complexType = complexType;
        this.laxMinOccursMode = laxMinOccursMode;
    }

    @Override
    public String getTemplate() {
        return TEMPLATE;
    }

    public String baseClass() {
        StringBuilder sb = new StringBuilder();

        if (complexType.getBase() == null) {
            if (complexType.isHeader()) {
                sb.append("extends com.sforce.ws.bind.SoapHeaderObject ");
            } else if (getClassName().endsWith("Fault")) {
                sb.append("extends com.sforce.ws.SoapFaultException ");
            }
            sb.append("implements com.sforce.ws.bind.XMLizable");
        } else {
            sb.append("extends ").append(localJavaType(complexType.getBase(), 1, false));
        }
        return sb.toString();
    }

    public String superWrite() {
        if (!complexType.hasBaseClass()) {
            return "";
        }

        return "super.writeFields(__out, __typeMapper);";
    }

    public String superToString() {
        if (!complexType.hasBaseClass()) {
            return "";
        }

        return "sb.append(super.toString());";
    }

    public String superLoad() {
        if (!complexType.hasBaseClass()) {
            return "";
        }

        return "super.loadFields(__in, __typeMapper);";
    }

    public String xsiType() {
        if (complexType.getBase() == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("__typeMapper.writeXsiType(__out, \"");
        sb.append(complexType.getSchema().getTargetNamespace());
        sb.append("\", \"");
        sb.append(complexType.getName());
        sb.append("\");");
        return sb.toString();
    }

    public Iterator<Element> getElements() {
        Collection sequence = complexType.getContent();
        Iterator<Element> it;

        if (sequence == null) {
            it = Collections.<Element>emptyList().iterator();
        } else {
            it = sequence.getElements();
        }

        return it;
    }

    public String getMethod(Element element) {
        return "get" + NameMapper.getMethodName(element.getName());
    }

    public String booleanGetMethod(Element element) {
        return "is" + NameMapper.getMethodName(element.getName());
    }

    public String setMethod(Element element) {
        return "set" + NameMapper.getMethodName(element.getName());
    }

    public String elementDoc(Element element) {
        return element.getName() + " of type " + element.getType();
    }

    public String javaType(Element element) {
        QName type = element.getType();
        return localJavaType(type, element.getMaxOccurs(), element.isNillable());
    }

    private String localJavaType(QName type, int maxOccurs, boolean nillable) {
        String name = mapper.getJavaClassName(type, types, nillable);

        if (isArray(maxOccurs)) {
            name = name + "[]";
        }

        return name;
    }

    private boolean isArray(int maxOccurs) {
        return maxOccurs == Element.UNBOUNDED || maxOccurs > 1;
    }

    public String initArray(Element element) {
        StringBuilder sb = new StringBuilder();
        if (isArray(element.getMaxOccurs())) {
            sb.append(" = new ");
            sb.append(mapper.getJavaClassName(element.getType(), types, element.isNillable()));
            sb.append("[0]");
        }
        return sb.toString();
    }

    public String fieldName(Element element) {
        return NameMapper.getFieldName(element.getName());
    }

    public String typeInfo(Element element) {
        String tns = element.getSchema().getTargetNamespace();
        String name = element.getName();
        String typeNs = element.getType().getNamespaceURI();
        String type = element.getType().getLocalPart();
        int minOcc = element.getMinOccurs();
        int maxOcc = element.getMaxOccurs();
        boolean formQualified = element.getSchema().isElementFormQualified();
        return "\"" + tns + "\",\"" + name + "\",\"" + typeNs + "\",\"" + type + "\","
                + minOcc + "," + maxOcc + "," + formQualified;
    }

    public String writeMethod(Element element) {
        String type = javaType(element);
        JavaType javaType = javaTypeMap.get(type);
        return javaType.writeMethod;
    }

    public String loadType(Element element) {
        if (!laxMinOccursMode && element.getMinOccurs() == 1) {
            return "verifyElement";
        } else {
            return "isElement";
        }
    }

    public String loadMethod(Element element) {
        String type = javaType(element);
        JavaType javaType = javaTypeMap.get(type);
        return javaType.loadMethod;
    }

    private static JavaTypeMap javaTypeMap() {
        JavaTypeMap map = new JavaTypeMap();
        map.put("java.lang.String", new JavaType("readString", "writeString"));
        map.put("int", new JavaType("readInt", "writeInt"));
        map.put("boolean", new JavaType("readBoolean", "writeBoolean"));
        map.put("double", new JavaType("readDouble", "writeDouble"));
        map.put("long", new JavaType("readLong", "writeLong"));
        map.put("float", new JavaType("readFloat", "writeFloat"));
        return map;
    }

    private static class JavaTypeMap {
        private HashMap<String, JavaType> map = new HashMap<String, JavaType>();
        private JavaType defaultType = new JavaType("readObject", "writeObject");

        public JavaType get(String key) {
        	JavaType o = map.get(key);
            if (o == null) {
                o = defaultType;
            }
            return o;
        }

        public void put(String s, JavaType javaType) {
            map.put(s, javaType);
        }
    }

    private static class JavaType {
        public JavaType(String loadMethod, String writeMethod) {
            this.loadMethod = loadMethod;
            this.writeMethod = writeMethod;
        }

        String loadMethod;
        String writeMethod;
    }
}
