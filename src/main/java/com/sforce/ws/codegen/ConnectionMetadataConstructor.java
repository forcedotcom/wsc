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

import java.util.*;

import javax.xml.namespace.QName;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.codegen.metadata.*;
import com.sforce.ws.codegen.metadata.HeaderMetadata.HeaderElementMetadata;
import com.sforce.ws.util.FileUtil;
import com.sforce.ws.wsdl.*;
import com.sforce.ws.wsdl.Collection;

/**
 * @author hhildebrand
 * @since 184
 */
public class ConnectionMetadataConstructor {
    private final String className;
    private final String packageName;
    private final Definitions definitions;
    private final TypeMapper typeMapper;

    public ConnectionMetadataConstructor(Definitions definitions, TypeMapper typeMapper, String packagePrefix) {
        this.definitions = definitions;
        this.typeMapper = typeMapper;
        this.className = (definitions.getApiType() != null ? definitions.getApiType().name() : "Soap") + "Connection";
        this.packageName = NameMapper.getPackageName(definitions.getTargetNamespace(), packagePrefix);
    }

    public ConnectionClassMetadata getConnectionClassMetadata() {
        try {
            List<HeaderMetadata> headers = new ArrayList<HeaderMetadata>();
            List<OperationMetadata> operations = new ArrayList<OperationMetadata>();

            for (Iterator<Part> i = headers(); i.hasNext();) {
                Part header = i.next();
                List<HeaderElementMetadata> elements = new ArrayList<HeaderMetadata.HeaderElementMetadata>();
                for (Iterator<Element> j = headerElements(header); j.hasNext();) {
                    Element element = j.next();
                    elements.add(HeaderElementMetadata.newInstance(argSetMethod(element), argName(element)));
                }
                headers.add(HeaderMetadata.newInstance(headerType(header), headerName(header), headerArgs(header),
                        headerElement(header), elements));
            }

            for (Iterator<Operation> i = getOperations(); i.hasNext();) {
                Operation operation = i.next();

                List<ElementMetadata> elements = new ArrayList<ElementMetadata>();
                for (Iterator<Element> j = argElements(operation); j.hasNext();) {
                    Element element = j.next();
                    elements.add(ElementMetadata.newInstance(argSetMethod(element), argName(element)));
                }

                List<HeaderMetadata> operationHeaders = new ArrayList<HeaderMetadata>();
                for (Iterator<Part> j = headersFor(operation); j.hasNext();) {
                    Part part = j.next();
                    operationHeaders.add(HeaderMetadata.newInstance(null, headerName(part), null, headerElement(part),
                            null));
                }

                operations.add(OperationMetadata.newInstance(returnType(operation), getOperationName(operation),
                        requestType(operation), responseType(operation), getArgs(operation), soapAction(operation),
                        requestName(operation), responseName(operation), getResultCall(operation), elements,
                        operationHeaders));
            }

            ConnectionClassMetadata connectionClassMetadata = ConnectionClassMetadata.newInstance(getPackagePrefix(),
                    packageName, className, hasLoginCall(), hasLoginCall() == true ? loginResult() : null,
                    verifyEndpoint(), hasSessionHeader(), sobjectNamespace(), dumpQNames(), dumpKnownHeaders(),
                    headers, operations);
            return connectionClassMetadata;
        } catch (ConnectionException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public String endpoint() {
        return definitions.getService().getPort().getSoapAddress().getLocation();
    }

    public String verifyEndpoint() {
        return definitions.getApiType() == null ? null : definitions.getApiType().getVerifyEndpoint();
    }

    public String getPackagePrefix() {
        String prefix = typeMapper.getPackagePrefix();
        return (prefix == null) ? "null" : "\"" + prefix + "\"";
    }

    public String getTargetNamespace() {
        return definitions.getTargetNamespace();
    }

    public String sobjectNamespace() {
        return definitions.getApiType() == null ? "null" : "\"" + definitions.getApiType().getSobjectNamespace() + "\"";
    }

    public String dumpQNames() throws ConnectionException {
        StringBuilder sb = new StringBuilder();
        Iterator<Operation> oit = definitions.getPortType().getOperations();
        while (oit.hasNext()) {
            Operation operation = oit.next();
            Part in = operation.getInput().getParts().next();
            addQName(sb, in.getElement());
            Part out = operation.getOutput().getParts().next();
            addQName(sb, out.getElement());
        }

        Iterator<Part> parts = headers();
        while (parts.hasNext()) {
            Part hp = parts.next();
            addQName(sb, hp.getElement());
        }

        return sb.toString();
    }

    public String dumpKnownHeaders() throws ConnectionException {
        StringBuilder sb = new StringBuilder();
        Iterator<Part> parts = headers();
        while (parts.hasNext()) {
            Part hp = parts.next();
            String varName = qname(hp.getElement().getLocalPart());
            sb.append("  knownHeaders.put(");
            sb.append(varName).append(",");
            sb.append(headerType(hp)).append(".class");
            sb.append(");").append(FileUtil.EOL);
        }
        return sb.toString();
    }

    private boolean isSimpleType(Part header) throws ConnectionException {
        Types types = definitions.getTypes();
        Element element = types.getElement(header.getElement());
        if (!element.isComplexType()) { return true; }
        return typeMapper.isWellKnownType(element.getType().getNamespaceURI(), element.getType().getLocalPart());
    }

    public String headerName(Part header) {
        return header.getName();
    }

    public String headerElement(Part header) throws ConnectionException {
        return qname(header.getElement().getLocalPart());
    }

    private void addQName(StringBuilder sb, QName el) {
        sb.append("    private static final javax.xml.namespace.QName ");
        sb.append(qname(el.getLocalPart()));
        sb.append(" = new javax.xml.namespace.QName(\"");
        sb.append(el.getNamespaceURI());
        sb.append("\", \"");
        sb.append(el.getLocalPart());
        sb.append("\");");
        sb.append(FileUtil.EOL);
    }

    public boolean hasLoginCall() {
        return definitions.getApiType() != null && definitions.getApiType().hasLoginCall();
    }

    private String qname(String str) {
        return str + "_qname";
    }

    public boolean hasSessionHeader() throws ConnectionException {
        Iterator<Part> it = headers();
        while (it.hasNext()) {
            Part part = it.next();
            if (part.getName().equals("SessionHeader")) { return true; }
        }
        return false;
    }

    public Iterator<Part> headersFor(Operation operation) throws ConnectionException {
        BindingOperation bop = definitions.getBinding().getOperation(operation.getName());
        ArrayList<Part> parts = new ArrayList<Part>();
        Iterator<SoapHeader> hit = bop.getInput().getHeaders();
        while (hit.hasNext()) {
            SoapHeader sh = hit.next();
            parts.add(definitions.getMessage(sh.getMessage()).getPart(sh.getPart()));
        }
        return parts.iterator();
    }

    public Iterator<Part> headers() throws ConnectionException {
        return definitions.getBinding().getAllHeaders();
    }

    public String headerType(Part header) throws ConnectionException {
        if (isSimpleType(header)) {
            return "java.lang.String";
        } else {
            ComplexType type = getType(header);
            QName qn = new QName(type.getSchema().getTargetNamespace(), type.getName());
            return typeMapper.getJavaClassName(qn, definitions.getTypes(), false);
        }
    }

    public String getResultCall(Operation operation) throws ConnectionException {
        // return "void".equals(returnType(operation)) ? "" : "return __response.getResult();";
        Element el = getResponseElement(operation);
        if (el == null) { return ""; }

        return "return __response.get" + NameMapper.getMethodName(el.getName()) + "();";
    }

    public String soapAction(Operation operation) throws ConnectionException {
        BindingOperation bop = definitions.getBinding().getOperation(operation.getName());
        String soapAction = bop.getSoapAction();
        return "\"" + soapAction + "\"";
    }

    public String returnType(Operation operation) throws ConnectionException {
        Element el = getResponseElement(operation);
        if (el == null) { return "void"; }
        return getJavaClassName(el);
    }

    public Element getResponseElement(Operation operation) throws ConnectionException {
        ComplexType ct = getType(operation.getOutput());
        Collection sequence = ct.getContent();
        if (sequence == null) { // <complexType/>
            return null;
        }
        Iterator<Element> eit = sequence.getElements();

        Element result;
        if (eit.hasNext()) {
            result = eit.next();
            if (eit.hasNext()) { throw new IllegalArgumentException("Operation.output got more than one element:"
                    + operation); }
        } else {
            result = null; // <complexType><sequence/><complexType>
        }

        return result;
    }

    public String headerArgs(Part header) throws ConnectionException {
        Iterator<Element> eit = headerElements(header);
        return toArgs(eit);
    }

    public Iterator<Element> headerElements(Part header) throws ConnectionException {
        if (isSimpleType(header)) {
            ArrayList<Element> list = new ArrayList<Element>();
            return list.iterator();
        } else {
            ComplexType ct = getType(header);
            Collection sequence = ct.getContent();
            return sequence.getElements();
        }
    }

    public String getArgs(Operation operation) throws ConnectionException {
        Iterator<Element> eit = argElements(operation);
        return toArgs(eit);
    }

    private String toArgs(Iterator<Element> eit) {
        StringBuilder sb = new StringBuilder();
        while (eit.hasNext()) {
            Element el = eit.next();
            String clazz = getJavaClassName(el);
            sb.append(clazz).append(" ").append(argName(el));
            if (eit.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public String argSetMethod(Element element) {
        return "set" + NameMapper.getMethodName(element.getName());
    }

    public String argName(Element element) {
        return element.getName();
    }

    public Iterator<Element> argElements(Operation operation) throws ConnectionException {
        ComplexType ct = getType(operation.getInput());
        Collection sequence = ct.getContent();
        return (sequence == null) ? new ArrayList<Element>().iterator() : sequence.getElements();
    }

    public String getJavaClassName(Element el) {
        String clazz = typeMapper.getJavaClassName(el.getType(), definitions.getTypes(), el.isNillable());
        if (el.getMaxOccurs() != 1) {
            clazz += "[]";
        }
        return clazz;
    }

    public String loginResult() throws ConnectionException {
        QName ln = new QName(definitions.getTargetNamespace(), "login");
        Operation op = definitions.getPortType().getOperation(ln);
        return returnType(op);
    }

    public String responseName(Operation operation) throws ConnectionException {
        return qname(operation.getOutput().getParts().next().getElement().getLocalPart());
    }

    public String requestName(Operation operation) throws ConnectionException {
        return qname(operation.getInput().getParts().next().getElement().getLocalPart());
    }

    public String responseType(Operation operation) throws ConnectionException {
        ComplexType ct = getType(operation.getOutput());
        QName type = new QName(ct.getSchema().getTargetNamespace(), ct.getName());
        return typeMapper.getJavaClassName(type, definitions.getTypes(), false);
    }

    public String requestType(Operation operation) throws ConnectionException {
        ComplexType ct = getType(operation.getInput());
        QName type = new QName(ct.getSchema().getTargetNamespace(), ct.getName());
        return typeMapper.getJavaClassName(type, definitions.getTypes(), false);
    }

    private ComplexType getType(Message message) throws ConnectionException {
        Iterator<Part> it = message.getParts();
        if (!it.hasNext()) { throw new IllegalArgumentException("Input for operation " + message
                + " does not have a part"); }
        Part part = it.next();
        if (it.hasNext()) throw new IllegalArgumentException("Found more than one part for operation " + message);
        return getType(part);
    }

    private ComplexType getType(Part part) throws ConnectionException {
        Types types = definitions.getTypes();
        Element element = types.getElement(part.getElement());
        QName type = element.getType();
        return types.getComplexType(type);
    }

    public Iterator<Operation> getOperations() {
        return definitions.getPortType().getOperations();
    }

    public String getOperationName(Operation operation) {
        return operation.getName().getLocalPart();
    }
}
