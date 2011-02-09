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
