/*
 * Copyright (c) 2011, salesforce.com, inc.
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
package com.sforce.rest.tools;

import java.io.IOException;

import com.sforce.rest.*;
import com.sforce.ws.ConnectorConfig;

/**
 * This class accesses the discovery resources of the REST API and learns about
 *  every Standard object and custom object in your organziation.
 *  It then generates code.  Extend this 
 * @author gregoryw
 * @since 172
 */	
public abstract class CodeGenerator {
    static final String VERSION_EXTERNAL = "21";
    static final String VERSION_INTERNAL = "172";
    
    static final String NEWLINE = "\r\n";
    static final String TAB = "\t";

    public void describeMyOrg(ConnectorConfig config, String namespace) throws RestApiException, IOException {
        assert config != null;
        assert config.getSessionId() != null;
        assert config.getRestEndpoint() != null;
        
        assert namespace != null;
        assert !namespace.isEmpty();
        
        final RestConnectionImpl connection = new RestConnectionImpl(config);

        // parse through every Sobject known to your Org
        int count = 0;
        DescribeGlobal response = connection.describeGlobal();
        for (DescribeSobject.SobjectDescribe sobject : response.getSobjects().toArray(
                new DescribeSobject.SobjectDescribe[response.getSobjects().size()])) {

            //skip deprecated / hidden
            if (!sobject.isDeprecatedAndHidden()) {
                DescribeLayout describe = connection.describeLayout(sobject.getName());

                // create a class for retrieving (GET) sobjects from salesforce
                boolean created = generateCode(sobject, describe, namespace);
                if(created) count++;
            }
        }
        if(count > 0) {
            System.out.println("Code generation complete. Count: " + count);
        }
        else {
            System.out.println("No code generated.  Check to make sure the files don't already exist");
        }
    }
    
    /**
     * Generate code, write to files.
     * @param sobject
     * @param describe
     * @param folder
     * @param namespace
     * @param parentClass
     * @return
     */
    abstract boolean generateCode(DescribeSobject.SobjectDescribe sobject, DescribeLayout describe, String namespace) throws IOException;

}
