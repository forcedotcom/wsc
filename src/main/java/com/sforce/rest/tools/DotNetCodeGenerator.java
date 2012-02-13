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

import java.io.*;

import com.sforce.rest.DescribeLayout;
import com.sforce.rest.DescribeSobject;
import com.sforce.rest.DescribeLayout.Field;

/**
 * Generates C# objects from api describe calls
 * 
 * @author gwester
 * @since 172
 */
public class DotNetCodeGenerator extends CodeGenerator {

	@Override
    boolean generateCode(DescribeSobject.SobjectDescribe sobject, DescribeLayout describe, String namespace) throws IOException {
        if(describe == null || sobject == null) {
            return false;
        }
        if(namespace == null || namespace.isEmpty()) {
            namespace = "Sforce.Sobjects";
        }
        
        // make sure folder exists
        final String baseFolder = namespace.replace(".", "/");
        File outputFolder = new File(baseFolder);
        if(!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
	    
        // establish file
        String className = describe.getName();
        File file = new File(baseFolder + "/" + className + ".cs");
        if(file.exists()) {
            System.out.println("Could not generate code for " + className + "! File exists. Please move or delete.");
            return false;
        } else {
            file.createNewFile();
            System.out.println("Generating C# .NET code for " + className);
        }
        FileWriter writer = new FileWriter(file);
        
        // import
        writer.append("using System;" + NEWLINE + NEWLINE);

        // package begin
        writer.append("namespace " + namespace + NEWLINE);
        writer.append("{" + NEWLINE);

        // class comment block
        writer.append(TAB + "/// <summary>" + NEWLINE);
        writer.append(TAB + "/// Generated from information gathered from /services/data/v" + VERSION_EXTERNAL);
        writer.append(".0/sobjects/" + className + "/describe by " + System.getProperty("user.name") + NEWLINE);
        writer.append(TAB + "/// </summary>" + NEWLINE);
        
        // class begin
        writer.append(TAB + "public class " + className + NEWLINE);
        writer.append(TAB + "{" + NEWLINE);


        // constants
        writer.append(TAB + TAB + "const bool createable = "); 
        writer.append(sobject.isCreateable() ? "true;" : "false;");
        writer.append(NEWLINE);

        writer.append(TAB + TAB + "const bool deletable = ");
        writer.append(sobject.isDeletable() ? "true;" : "false;");
        writer.append(NEWLINE);
        
        writer.append(TAB + TAB + "const bool updateable = ");
        writer.append(sobject.isUpdateable() ? "true;" : "false;");
        writer.append(NEWLINE);
        writer.append(NEWLINE);

        // add all private member variables
        for (Field field : describe.getAllFields()) {
            String fieldNameLower = 
                field.getName().substring(0, 1).toLowerCase()
                    + field.getName().substring(1, field.getName().length());

            // write member with get, set
            writer.append(TAB + TAB + "public " + getDotNetType(field) + " " + fieldNameLower);
            if(field.isUpdateable() || field.isCreateable()) {
                writer.append(" {get; set;}");
                
                if(describe.getRequiredFieldsForCreateUpdate().contains(field)) {
                    writer.append(" //required");
                } else if(describe.getRequiredFieldsForCreateUpdate().contains(field)) {
                    writer.append(" //optional");
                }
                writer.append(NEWLINE);    
            } 
            else {
                writer.append(" {get;}" + NEWLINE);
            }
        }
        writer.append(NEWLINE);
        
        // no arg constructor
        writer.append(TAB + TAB + "public " + className + "()" + NEWLINE);
        writer.append(TAB + TAB + "{" + NEWLINE);
        writer.append(NEWLINE);
        writer.append(TAB + TAB + "}" + NEWLINE);
        writer.append(NEWLINE);
        
        // constructor with required fields
        int count = 0;
        int max = describe.getRequiredFieldsForCreateUpdate().size();
        if(max > count) {
            writer.append(TAB + TAB + "public " + className + "(");
           
            for (Field field : describe.getRequiredFieldsForCreateUpdate()) {
                String fieldNameLower = field.getName().substring(0, 1).toLowerCase()
                        + field.getName().substring(1, field.getName().length());
   
                writer.append(getDotNetType(field) + " " + fieldNameLower);
                if (++count != max) { writer.append(", "); }
            }
            writer.append(")" + NEWLINE);
            writer.append(TAB + TAB + "{" + NEWLINE);
            
            //constructor body
            for (Field field : describe.getRequiredFieldsForCreateUpdate()) {
                String fieldNameLower = field.getName().substring(0, 1).toLowerCase()
                        + field.getName().substring(1, field.getName().length());
    
                writer.append(TAB + TAB + TAB + "this." + fieldNameLower + " = " + fieldNameLower + ";" + NEWLINE);
            }
            writer.append(TAB + TAB + "}" + NEWLINE);
        }

        // class end
        writer.append(TAB + "}" + NEWLINE);
        
        // package end
        writer.append("}" + NEWLINE);
        
        writer.close();
        
        return true;
    }

    /**
     * This method attempts to match the salesforce sobject field type to Dot Net. If it doesn't know, it returns String.
     * 
     * @param salesforceFieldType
     * @return
     */
    private String getDotNetType(Field field) {
        if (field.getType().equals("int")) {
            return "int";
        } 
        else if (field.getType().equals("boolean")) {
            return "bool";
        } 
        else if (field.getType().equals("double")) {
            return "double";
        } 
        else {
            return "string";
        }
    }
}
