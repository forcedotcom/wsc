/*
 * Copyright (c) 2017, salesforce.com, inc.
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.QName;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;
import com.sforce.ws.wsdl.Constants;

/**
 * This is a generic XML element -- same a DOM element. In the common case this
 * class must be able to hold child elements. We do not have a usecase for it yet.
 * So child elements are not implemented.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Dec 12, 2005
 */
public class XmlObject implements XMLizable {
    private QName name;
    private QName xmlType;
    private Object value;
    private String defaultNamespace;
    private ArrayList<XmlObject> children = new ArrayList<XmlObject>();

    public XmlObject() {
        this(null, null);
    }

    public XmlObject(QName name) {
        this(name, null);
    }

    public XmlObject(QName name, Object value) {
        this.name = name;
        this.value = value;
    }

    public QName getName() {
        return name;
    }

    public void setName(QName name) {
        this.name = name;
    }

    public void setDefaultNamespace(String namespace) {
        this.defaultNamespace = namespace;
    }

    public QName getXmlType() {
        return xmlType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean hasChildren() {
        return children.size() != 0;
    }

    public XmlObject addField(String name, Object value) {
        return addOrSetField(name, value, false);
    }

    public XmlObject setField(String name, Object value) {
        return addOrSetField(name, value, true);
    }

    /**
     * Because we create complex types as subclasses of XMLizable instead of
     * XmlObject (and we don't want to change interfaces), this method will
     * return this as the complex type subclass of XMLizable, if this represents
     * a complex type. If it does not, it will simply return this.
     * @return
     */
    public XMLizable asTyped() {
        return this;
    }

    /**
     * evaluate the given xpath like expression.
     * eg xpath: "OpportunityContactRoles/records/Contact/LastName" this will list
     * all LastName
     *
     * @param xpath
     * @return
     */

    public Iterator<XmlObject> evaluate(String xpath) {
        if (xpath == null) throw new NullPointerException("xpath can not be null");

        ArrayList<XmlObject> parents = new ArrayList<XmlObject>();
        ArrayList<XmlObject> nodes = new ArrayList<XmlObject>();
        parents.add(this);

        String[] paths = xpath.split("/");
        for (String path : paths) {
            nodes = new ArrayList<XmlObject>();
            for (XmlObject parent : parents) {
                Iterator<XmlObject> it = parent.getChildren(path);
                while (it.hasNext()) nodes.add(it.next());
            }
            if (nodes.size() == 0) {
                break;
            } else {
                parents = nodes;
            }
        }

        return nodes.iterator();
    }

    private XmlObject addOrSetField(String n, Object value, boolean set) {
        XmlObject child = null;
        if (value instanceof XmlObject) {
            child = (XmlObject) value;
            child.name = getQNameFor(n);
            children.add(child);
        } else {
            if (set) {
                child = findField(n);
            }
            if (child == null) {
                child = new XmlObject(getQNameFor(n), value);
                children.add(child);
            } else {
                child.setValue(value);
            }
        }
        return child;
    }

    private QName getQNameFor(String n) {
        String namespace = defaultNamespace == null ? Constants.PARTNER_SOBJECT_NS : defaultNamespace;
        return name == null ? new QName(namespace, n) :  new QName(name.getNamespaceURI(), n);
    }

    public boolean removeField(String name) {
        XmlObject item = findField(name);
        return item != null && children.remove(item);
    }

    public Object getField(String name) {
        //TODO: optimize
        XmlObject item = findField(name);
        Object result = null;
        if (item != null) {
            if (item.hasChildren()) {
                result = item;
            } else {
                result = item.getValue();
            }
        }
        return result;
    }

    public XmlObject getChild(String name) {
        return findField(name);
    }

    private XmlObject findField(String name) {
        XmlObject item = null;

        for (XmlObject child : children) {
            if (child.getName().getLocalPart().equals(name)) {
                item = child;
                break;
            }
        }

        return item;
    }

    public Iterator<XmlObject> getChildren() {
        return children.iterator();
    }

    public Iterator<XmlObject> getChildren(String name) {
        ArrayList<XmlObject> result = new ArrayList<XmlObject>();
        for (XmlObject child : children) {
            if (child.getName().getLocalPart().equals(name)) {
                result.add(child);
            }
        }
        return result.iterator();
    }

    /**
     * Similar to {@link #asTyped()}, this will return this object's children
     * as the proper complex type subclass of XMLizable, if they are complex
     * types. If a child is not a complex type, it will be returned as-is,
     * as an XmlObject.
     * @return
     */
    public Iterator<XMLizable> getTypedChildren() {
        ArrayList<XMLizable> result = new ArrayList<XMLizable>(children.size());
        for (XmlObject child : children) {
            result.add(child.asTyped());
        }
        return result.iterator();
    }


    @Override
    public String toString() {
        return "XmlObject{" +
                "name=" + name +
                ", value=" + value +
                ", children=" + children +
                '}';
    }

    @Override
    public void write(QName element, XmlOutputStream out, TypeMapper typeMapper) throws IOException {
        typeMapper.writeFieldXsiType(true);
        if (hasChildren()) {
            out.writeStartTag(element.getNamespaceURI(), element.getLocalPart());
            for (XmlObject child : children) {
                child.write(child.getName(), out, typeMapper);
            }
            out.writeEndTag(element.getNamespaceURI(), element.getLocalPart());
        } else {
            if (value != null) {
            	TypeInfo info = null;
            	if (value instanceof XmlTypeInfoProvider) {
            		info = ((XmlTypeInfoProvider)value).getTypeInfo(name.getNamespaceURI(), name.getLocalPart(), typeMapper);
            	}
            	if (info == null) {
                    QName xmlType = typeMapper.getXmlType(value.getClass().getName());
                    if( xmlType == null && value.getClass().isArray()) {
                    	xmlType = typeMapper.getXmlType( value.getClass().getComponentType().getName() );
                    }
                    for (Class<?> classForType = value.getClass(); classForType != Object.class && xmlType == null;
                        classForType = classForType.getSuperclass()) {
                        xmlType = typeMapper.getXmlType(classForType.getName());
                    }
            		if (xmlType == null) {
            			//todo: throw right exception
            			throw new IOException("Unable to find xml type for :" + value.getClass().getName());
            		}
            		int max = value.getClass().isArray() ? -1 : 1;
            		if ( value.getClass().equals( byte[].class )) {
            			//special case for arrays we wish to treat as a single value
            			max = 1;
            		}
            		info = new TypeInfo(name.getNamespaceURI(), name.getLocalPart(),
            				xmlType.getNamespaceURI(), xmlType.getLocalPart(), 0, max, true);
            	}
                typeMapper.writeObject(out, info, value, true);
            }
        }
        typeMapper.writeFieldXsiType(false);
    }

    @Override
    public void load(XmlInputStream in, TypeMapper typeMapper) throws IOException, ConnectionException {
        loadStartTag(in, typeMapper);
        loadAfterStartTag(in, typeMapper);
    }

    protected void loadStartTag(XmlInputStream in, TypeMapper typeMapper) {
        in.consumePeeked();
        name = new QName(in.getNamespace(), in.getName());
        xmlType = typeMapper.getXsiType(in);
    }

    protected void loadAfterStartTag(XmlInputStream in, TypeMapper typeMapper) throws IOException, ConnectionException {
        StringBuilder text = new StringBuilder();
        boolean textFound = false;

        while (true) {
            int type = in.peek();

            if (type == XmlInputStream.START_TAG) {
                XmlObject child = extractChildElement(in, typeMapper);
                child.load(in, typeMapper);
                children.add(child);
            } else if (type == XmlInputStream.TEXT) {
                in.consumePeeked();
                text.append(in.getText());
                textFound = true;
            } else if (type == XmlInputStream.END_TAG) {
                in.consumePeeked();
                String ns = in.getNamespace();
                String n = in.getName();
                if (name.getNamespaceURI().equals(ns) && name.getLocalPart().equals(n)) {
                    break;
                }
            } else {
                throw new ConnectionException("unknown tag:" + type + " at " + in);
            }
        }

        if (textFound) value = typeMapper.deserialize(text.toString(), xmlType);
    }

    private XmlObject extractChildElement(XmlInputStream in, TypeMapper typeMapper) throws ConnectionException {
        XmlObject child;
        QName xsiType = typeMapper.getXsiType(in);
        if (xsiType == null) {
            child = new XmlObject();
        } else if (xsiType.getLocalPart().equals("QueryResult") && !in.getName().equals("QueryResult")) {
            // In older API versions, some objects had their xsitype erroneously set to "QueryResult"
            // even when they were not QueryResult objects. Since we have no information about their type
            // we will default back to XmlObject.
            child = new XmlObject();
        } else {
            Class<?> childClass = typeMapper.getJavaType(xsiType);
            if (childClass == null || !XMLizable.class.isAssignableFrom(childClass)) {
                child = new XmlObject();
            }
            else {
                XMLizable xmlizable;
                try {
                    xmlizable = (XMLizable)childClass.getConstructor().newInstance();
                    if (xmlizable instanceof XmlObject) {
                        child = (XmlObject) xmlizable;
                    } else {
                        child = new XmlObjectWrapper(xmlizable);
                    }
                } catch (InstantiationException | NoSuchMethodException e) {
                    throw new ConnectionException("Failed to create object", e);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ConnectionException("Failed to create object", e);
                }
            }
        }
        return child;
    }

    protected void cloneFrom(XmlObject source) {
        this.name = source.name;
        this.xmlType = source.xmlType;
        this.value = source.value;
        this.defaultNamespace = source.defaultNamespace;
        this.children = source.children;
    }
}
