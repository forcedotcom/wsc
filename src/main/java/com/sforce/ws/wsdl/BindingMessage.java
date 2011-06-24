package com.sforce.ws.wsdl;

import com.sforce.ws.parser.XmlInputStream;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * BindingMessage
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Jan 18, 2006
 */
public class BindingMessage extends WsdlNode {

    private QName name;
    private Definitions definitions;
    private String type;
    private ArrayList<SoapHeader> headers = new ArrayList<SoapHeader>();
    private SoapBody body;

    public BindingMessage(Definitions definitions, String type) {
        this.definitions = definitions;
        this.type = type;
    }

    public QName getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Iterator<SoapHeader> getHeaders() {
        return headers.iterator();
    }

    public SoapBody getBody() {
        return body;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        String na = parser.getAttributeValue(null, NAME);
        if (na != null) {
          name = new QName(definitions.getTargetNamespace(), na);
        }

        int eventType = parser.getEventType();
        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String n = parser.getName();
                String ns = parser.getNamespace();
                if (n != null && ns != null) {
                    parse(n, ns, parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();
                if (type.equals(name) && WSDL_NS.equals(namespace)) {
                    return;
                }
            }
            eventType = parser.next();
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_SOAP_NS.equals(namespace)) {

            if (HEADER.equals(name)) {
                SoapHeader header = new SoapHeader();
                header.read(parser);
                headers.add(header);
            } else if (BODY.equals(name)) {
                if (body != null) throw new WsdlParseException("can not have more than one soap:body");
                body = new SoapBody();
                body.read(parser);
            } else if (FAULT.equals(name)) {
                //todo:
            }
        }
    }
}
