package com.sforce.ws.transport;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.parser.PullParserException;
import com.sforce.ws.parser.XmlInputStream;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * This class validates parts of the Soap Connection.
 *
 * @author rcornel
 */
public class SoapConnectionTest {
    // If we cannot identify a header throw an exception.
    @Test(expected = ConnectionException.class)
    public void validateParsingHeader() throws PullParserException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ConnectionException {
        SoapConnection connection = new SoapConnection("http://www.salesforce.com", "sobject", new TypeMapper(), ConnectorConfig.DEFAULT);
        Map<QName, Class> knownHeaders = new HashMap<>();
        connection.setKnownHeaders(knownHeaders);

        String inputString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>   \n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "  xmlns:urn=\"urn:enterprise.soap.sforce.com\"\n" +
                "  xmlns:urn1=\"urn:sobject.enterprise.soap.sforce.com\"\n" +
                "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <soapenv:Header>\n" +
                "     <urn:SessionHeader>\n" +
                "        <urn:sessionId>QwWsHJyTPW.1pd0_jXlNKOSU</urn:sessionId>\n" +
                "     </urn:SessionHeader>" +
                "  </soapenv:Header>\n" +
                "  <soapenv:Body>\n" +
                "     <urn:create>\n" +
                "        <urn:sObjects> <!--Zero or more repetitions:-->\n" +
                "           <urn1:type>Contact</urn1:type>\n" +
                "           <!--You may enter ANY elements at this point-->\n" +
                "           <AccountId>001D000000HRzKD</AccountId>\n" +
                "           <FirstName>Jane</FirstName>\n" +
                "           <LastName>Doe</LastName>\n" +
                "        </urn:sObjects>\n" +
                "        <urn:sObjects>\n" +
                "           <urn1:type>Account</urn1:type>\n" +
                "           <Name>Acme Rockets, Inc.</Name>\n" +
                "        </urn:sObjects>\n" +
                "     </urn:create>\n" +
                "  </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        ByteArrayInputStream stream = new ByteArrayInputStream(inputString.getBytes());
        XmlInputStream xin = new XmlInputStream();
        xin.setInput(stream, "UTF-8");
        xin.nextTag();
        xin.nextTag();

        Method isHeaderMethod = SoapConnection.class.getDeclaredMethod("isHeader", XmlInputStream.class);
        isHeaderMethod.setAccessible(true);

        Method readSoapHeaderMethod = SoapConnection.class.getDeclaredMethod("readSoapHeader", XmlInputStream.class);
        readSoapHeaderMethod.setAccessible(true);
        try {
            assertTrue((Boolean) isHeaderMethod.invoke(connection, xin));
            xin.nextTag();
            readSoapHeaderMethod.invoke(connection, xin);
        } catch (InvocationTargetException e) {
            throw (ConnectionException)e.getCause();
        }
    }
}
