package com.sforce.bulk;

import com.sforce.async.ConcurrencyMode;
import com.sforce.async.OperationEnum;
import junit.framework.TestCase;

/**
 * This class represents
 * <p/>
 * User: mcheenath
 * Date: Dec 14, 2010
 */
public class UpdateStreamTest extends TestCase {

    public void testUpdateStream() throws StreamException {
        //doBasicStream();
    }

    public void doBasicStream() throws StreamException {
        StreamHandler handler = new StreamHandler();
        handler.getConfig().setAuthEndpoint("http://localhost:7001/services/Soap/u/21.0/");
        handler.getConfig().setUsername("ee@cheenath.com");
        handler.getConfig().setPassword("123456");

        UpdateStream stream = UpdateStream.create(handler);

        String[] fieldNames = {"Name", "AnnualRevenue", "NumberOfEmployees", "Description"};
        stream.start("Account", OperationEnum.insert, ConcurrencyMode.Parallel, fieldNames);

        for (int i=0;i<100; i++) {
            stream.write("name" + i + "--" + System.currentTimeMillis(), "123.4", "#N/A", "this is fun");
        }

        UpdateResultStream resultStream = stream.close();

        UpdateResult result;

        while((result = resultStream.next()) != null) {
            System.out.println(result);
        }
    }
}
