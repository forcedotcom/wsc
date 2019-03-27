/*
 * Copyright (c) 2017, salesforce.com, inc. All rights reserved. Redistribution and use in source and binary forms, with
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

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.xml.namespace.QName;

import org.apache.commons.beanutils.PropertyUtils;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;
import com.sforce.ws.wsdl.Constants;
import org.apache.commons.beanutils.SuppressPropertiesBeanIntrospector;

/**
 * This class wraps an XMLizable and presents it as an XmlObject so that we
 * can later convert this object to the complex type this represents.
 * 
 * @author georges.nguyen
 */
public class XmlObjectWrapper extends XmlObject {
    private String defaultNamespace;
    private QName name;
    private QName xmlType;

    private XMLizable xmlizable;
	
    static {
        PropertyUtils.addBeanIntrospector(SuppressPropertiesBeanIntrospector.SUPPRESS_CLASS);
    }

    public XmlObjectWrapper(XMLizable xmlizable) {
        this.xmlizable = xmlizable;
    }

    @Override
    public XmlObject addField(String name, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public XMLizable asTyped() {
        return this.xmlizable;
    }

    @Override
    public XmlObject getChild(String name) {
        PropertyDescriptor descriptor = getPropertyDescriptor(name);
        if (descriptor != null) {
            try {
                Object value = descriptor.getReadMethod().invoke(xmlizable);
                if (value instanceof XmlObject) {
                    return (XmlObject)value;
                } if (value instanceof XMLizable) {
                    return new XmlObjectWrapper((XMLizable)value);
                } else {
                    return new XmlObject(getQNameFor(descriptor.getName()), value);
                }
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return null;
    }

    @Override
    public Iterator<XmlObject> getChildren() {
        ArrayList<XmlObject> result = new ArrayList<XmlObject>();
        for (PropertyDescriptor descriptor : getPropertyDescriptors()) {
            addProperty(descriptor, result);
        }
        return result.iterator();
    }

    @Override
    public Iterator<XmlObject> getChildren(String name) {
        ArrayList<XmlObject> result = new ArrayList<XmlObject>();
        for (PropertyDescriptor descriptor : getPropertyDescriptors()) {
            if (descriptor.getName().equals(name)) {
                addProperty(descriptor, result);
            }
        }
        return result.iterator();
    }

    @Override
    public Object getField(String name) {
        PropertyDescriptor descriptor = getPropertyDescriptor(name);
        if (descriptor != null) {
            try {
                return descriptor.getReadMethod().invoke(xmlizable);
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return null;
    }

    @Override
    public QName getName() {
        return this.name;
    }

    @Override
    public QName getXmlType() {
        return this.xmlType;
    }

    @Override
    public boolean hasChildren() {
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(xmlizable);
        return descriptors.length != 0;
    }

    @Override
    public void load(XmlInputStream in, TypeMapper typeMapper) throws IOException, ConnectionException {
        name = new QName(in.getNamespace(), in.getName());
        xmlType = typeMapper.getXsiType(in);
        xmlizable.load(in, typeMapper);
    }

    @Override
    public boolean removeField(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultNamespace(String namespace) {
        this.defaultNamespace = namespace;
    }

    @Override
    public XmlObject setField(String name, Object value) {
        PropertyDescriptor descriptor = getPropertyDescriptor(name);
        if (descriptor != null) {
            try {
                descriptor.getWriteMethod().invoke(xmlizable, value);
                return new XmlObjectWrapper((XMLizable)descriptor.getReadMethod().invoke(xmlizable));
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return null;
    }

    @Override
    public void setName(QName name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "XmlObjectWrapper{" +
                "name=" + name +
                ", xmlizable=" + xmlizable +
                '}';
    }

    @Override
    public void write(QName element, XmlOutputStream out, TypeMapper typeMapper) throws IOException {
        xmlizable.write(element, out, typeMapper);
    }

    private void addProperty(PropertyDescriptor descriptor, ArrayList<XmlObject> result) {
        Object value;
        try {
            value = descriptor.getReadMethod().invoke(xmlizable);
            if (value instanceof XmlObject) {
                result.add((XmlObject)value);
            } else if (value instanceof XMLizable) {
                result.add(new XmlObjectWrapper((XMLizable)value));
            } else if (value instanceof Object[]) {
                for (Object subValue : (Object[])value) {
                    result.add(new XmlObject(getQNameFor(descriptor.getName()), subValue));
                }
            } else {
                result.add(new XmlObject(getQNameFor(descriptor.getName()), value));
            }
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
        }
    }

    private QName getQNameFor(String n) {
        String namespace = defaultNamespace == null ? Constants.PARTNER_SOBJECT_NS : defaultNamespace;
        return name == null ? new QName(namespace, n) :  new QName(name.getNamespaceURI(), n);
    }

    private PropertyDescriptor getPropertyDescriptor(String name) {
        for (PropertyDescriptor descriptor : PropertyUtils.getPropertyDescriptors(xmlizable)) {
            if (descriptor.getName().equals(name) && descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null) {
                return descriptor;
            }
        }
        return null;
    }

    private List<PropertyDescriptor> getPropertyDescriptors() {
        List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        for (PropertyDescriptor descriptor : PropertyUtils.getPropertyDescriptors(xmlizable)) {
            if (descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null) {
                descriptors.add(descriptor);
            }
        }
        return descriptors;
    }
}
