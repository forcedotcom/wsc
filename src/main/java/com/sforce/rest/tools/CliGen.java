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

import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.tools.ToolsException;

/**
 * restc is a tool that can generate plain old java objects from api describe calls
 * 
 * @author gregoryw
 * @version 1.0
 * @since 1.0  Feb 26, 2011
 */
public class CliGen {
	
    public static void main(String[] args) throws Exception {
		try {
		    run(args);
		} catch (ToolsException e) {
		    System.out.println(e);
		    System.exit(1);
		}
	}

	static void run(String[] args) throws Exception {
		if (args.length != 4) {
		    throw new ToolsException(" usage: java com.sforce.rest.tools.CliGen" + 
		            "<java | dotnet> <my.package | my.namespace> <instance> <session ID>");
		}
		
	    String codeGenLanguage = args[0];
        String namespace = args[1];
	    String instance = args[2];
	    String sid =args[3];
	    
	    if(!namespace.contains(".")) {
	        throw new ToolsException("Second argument must be a Java package like com.sforce.pojo or C# namespace like Sforce.Sobject");
	    }
	    if(instance.length() < 3 || instance.contains("salesforce")) {
	        throw new ToolsException("Third argument must be an instance like na1, cs0, ap1, na9, eu1");
	    }
	    if(sid.length() < 24) {
	        throw new ToolsException("Fourth argument must be a session ID");
	    }
	    
		ConnectorConfig config = new ConnectorConfig();
        config.setRestEndpoint("https://" + instance + ".salesforce.com/services/data/v21.0/");
        config.setSessionId(sid);
        
        if(codeGenLanguage.equals("java")) {
            PojoCodeGenerator codeGen = new PojoCodeGenerator();
            codeGen.describeMyOrg(config, namespace);
        }
        else if(codeGenLanguage.equals("dotnet")) {
            DotNetCodeGenerator codeGen = new DotNetCodeGenerator();
            codeGen.describeMyOrg(config, namespace);
        } 
        else {
            throw new ToolsException("Must choose java or dotnet for first argument.");
        }
	}
}
