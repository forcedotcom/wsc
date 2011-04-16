package com.sforce.async;

import com.sforce.ws.parser.XmlOutputStream;

import java.io.OutputStream;
import java.io.IOException;

/**
 * AsyncXmlOutputStream --
 *
 * @author mcheenath
 * @since 160
 */
public class AsyncXmlOutputStream extends XmlOutputStream {

    public AsyncXmlOutputStream(OutputStream out, boolean prettyPrint) throws IOException {
        super(out, prettyPrint);
        startDocument();
        setPrefix("", RestConnection.NAMESPACE);
    }
}
