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

package com.sforce.ws.codegen;

import java.io.File;
import java.util.*;

import javax.xml.namespace.QName;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.codegen.metadata.ComplexClassMetadata;
import com.sforce.ws.codegen.metadata.MemberMetadata;
import com.sforce.ws.wsdl.*;
import com.sforce.ws.wsdl.Collection;

/**
 * @author hhildebrand
 * @since 184
 */
public class TypeMetadataConstructor {
    private static class JavaType {
        String loadMethod;
        String writeMethod;

        public JavaType(String loadMethod, String writeMethod) {
            this.loadMethod = loadMethod;
            this.writeMethod = writeMethod;
        }
    }

    private static class JavaTypeMap {
        private HashMap<String, TypeMetadataConstructor.JavaType> map = new HashMap<String, TypeMetadataConstructor.JavaType>();
        private TypeMetadataConstructor.JavaType defaultType = new JavaType("readObject", "writeObject");

        public TypeMetadataConstructor.JavaType get(String key) {
            TypeMetadataConstructor.JavaType o = map.get(key);
            if (o == null) {
                o = defaultType;
            }
            return o;
        }

        public void put(String s, TypeMetadataConstructor.JavaType javaType) {
            map.put(s, javaType);
        }
    }

    private static final String LAX_MINOCCURS = "lax-minoccurs-checking";
    private static final TypeMetadataConstructor.JavaTypeMap javaTypeMap = javaTypeMap();

    private static TypeMetadataConstructor.JavaTypeMap javaTypeMap() {
        TypeMetadataConstructor.JavaTypeMap map = new JavaTypeMap();
        map.put("java.lang.String", new JavaType("readString", "writeString"));
        map.put("int", new JavaType("readInt", "writeInt"));
        map.put("boolean", new JavaType("readBoolean", "writeBoolean"));
        map.put("double", new JavaType("readDouble", "writeDouble"));
        map.put("long", new JavaType("readLong", "writeLong"));
        map.put("float", new JavaType("readFloat", "writeFloat"));
        return map;
    }

    protected final ComplexType complexType;
    private final boolean laxMinOccursMode;
    protected final String className;

    private final Types types;

    protected final TypeMapper mapper;

    private final String packageName;

    public TypeMetadataConstructor(Types types, Schema schema, ComplexType complexType, File tempDir,
            TypeMapper typeMapper) {
    	this(types, schema, complexType, tempDir, typeMapper, System.getProperty(LAX_MINOCCURS) != null);
    }

    public TypeMetadataConstructor(Types types, Schema schema, ComplexType complexType, File tempDir,
            TypeMapper typeMapper, boolean laxMinOccursMode) {
        this.packageName = NameMapper.getPackageName(schema.getTargetNamespace(), typeMapper.getPackagePrefix());
        this.types = types;
        this.mapper = typeMapper;
        this.complexType = complexType;
        this.className = NameMapper.getClassName(complexType.getName());
        this.laxMinOccursMode = laxMinOccursMode;
    }

    public String baseClass() {
        StringBuilder sb = new StringBuilder();

        if (complexType.getBase() == null) {
            if (complexType.isHeader()) {
                sb.append("extends com.sforce.ws.bind.SoapHeaderObject ");
            } else if (className.endsWith("Fault")) {
                sb.append("extends com.sforce.ws.SoapFaultException ");
            }
            sb.append("implements com.sforce.ws.bind.XMLizable");
        } else {
            sb.append("extends ").append(localJavaType(complexType.getBase(), 1, false));
        }
        return sb.toString();
    }

    public String booleanGetMethod(Element element) {
        return "is" + NameMapper.getMethodName(element.getName());
    }

    public String elementDoc(Element element) {
        return element.getName() + " of type " + element.getType();
    }

    public String fieldName(Element element) {
        return NameMapper.getFieldName(element.getName());
    }

    public ComplexClassMetadata generateMetadata() {

        List<MemberMetadata> memberMetadataList = new ArrayList<MemberMetadata>();
        Iterator<Element> itr = getElements();
        while (itr.hasNext()) {
            Element e = itr.next();
            // TODO Get rid of the second javaType, it will always be boolean
            memberMetadataList.add(MemberMetadata.newInstance(elementDoc(e), javaType(e), fieldName(e), typeInfo(e),
                    initArray(e), getMethod(e), javaType(e), booleanGetMethod(e), setMethod(e), writeMethod(e),
                    loadType(e), loadMethod(e), isComplexType(e), javaTypeInterface(e), isArray(e)));
        }

        return new ComplexClassMetadata(packageName, className, baseClass(), xsiType(), superWrite(), superLoad(),
                superToString(), memberMetadataList, mapper.generateInterfaces(), packageName,
                complexType.getBase() == null ? null : localJavaType(complexType.getBase(), 1, false));
    }

	public Iterator<Element> getElements() {
        Collection sequence = complexType.getContent();
        Iterator<Element> it;

        if (sequence == null) {
            it = Collections.<Element> emptyList().iterator();
        } else {
            it = sequence.getElements();
        }

        return it;
    }

    public String getMethod(Element element) {
        return "get" + NameMapper.getMethodName(element.getName());
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

    public String javaType(Element element) {
        QName type = element.getType();
        return localJavaType(type, element.getMaxOccurs(), element.isNillable());
    }

    protected String javaTypeInterface(Element element) {
        return ConnectionMetadataConstructor.convertJavaClassToInterface(javaType(element), isComplexType(element));
    }

    public String loadMethod(Element element) {
        String type = javaType(element);
        TypeMetadataConstructor.JavaType javaType = javaTypeMap.get(type);
        return javaType.loadMethod;
    }

    protected boolean isComplexType(Element e) {
        return types.getComplexTypeAllowNull(e.getType()) != null;
    }

    public String loadType(Element element) {
        if (!laxMinOccursMode && element.getMinOccurs() == 1) {
            return "verifyElement";
        } else {
            return "isElement";
        }
    }

    public String setMethod(Element element) {
        return "set" + NameMapper.getMethodName(element.getName());
    }

    public String superLoad() {
        if (!complexType.hasBaseClass()) { return ""; }

        return "super.loadFields(__in, __typeMapper);";
    }

    public String superToString() {
        if (!complexType.hasBaseClass()) { return ""; }

        return "sb.append(super.toString());";
    }

    public String superWrite() {
        if (!complexType.hasBaseClass()) { return ""; }

        return "super.writeFields(__out, __typeMapper);";
    }

    public String typeInfo(Element element) {
        String tns = element.getSchema().getTargetNamespace();
        String name = element.getName();
        String typeNs = element.getType().getNamespaceURI();
        String type = element.getType().getLocalPart();
        int minOcc = element.getMinOccurs();
        int maxOcc = element.getMaxOccurs();
        boolean formQualified = element.getSchema().isElementFormQualified();
        return "\"" + tns + "\",\"" + name + "\",\"" + typeNs + "\",\"" + type + "\"," + minOcc + "," + maxOcc + ","
                + formQualified;
    }

    public String writeMethod(Element element) {
        String type = javaType(element);
        TypeMetadataConstructor.JavaType javaType = javaTypeMap.get(type);
        return javaType.writeMethod;
    }

    public String xsiType() {
        if (complexType.getBase() == null) { return ""; }

        StringBuilder sb = new StringBuilder();
        sb.append("__typeMapper.writeXsiType(__out, \"");
        sb.append(complexType.getSchema().getTargetNamespace());
        sb.append("\", \"");
        sb.append(complexType.getName());
        sb.append("\");");
        return sb.toString();
    }

    private boolean isArray(int maxOccurs) {
        return maxOccurs == Element.UNBOUNDED || maxOccurs > 1;
    }

    protected boolean isArray(Element e) {
		return isArray(e.getMaxOccurs());
	}

    protected String localJavaType(QName type, int maxOccurs, boolean nillable) {
        String name = mapper.getJavaClassName(type, types, nillable);

        if (isArray(maxOccurs)) {
            name = name + "[]";
        }

        return name;
    }
}
