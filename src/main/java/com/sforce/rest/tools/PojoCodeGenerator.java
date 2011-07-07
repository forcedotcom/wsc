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
 * Generates plain old java objects from api describe calls
 * 
 * @author gwester
 * @since 172
 */
public class PojoCodeGenerator extends CodeGenerator {
    
    private static final String fileExtension = ".java";

	@Override
    boolean generateCode(DescribeSobject.SobjectDescribe sobject, DescribeLayout describe, String packageName) throws IOException {
        if(describe == null || sobject == null) {
            return false;
        }
        if(packageName == null || packageName.isEmpty()) {
            packageName = "com.sforce.rest.pojo";
        }
        
        // make sure folder exists
        final String baseFolder = packageName.replace(".", "/");
        File outputFolder = new File(baseFolder);
        if(!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
	    
	    // parent class
        final String parentClass = "SObject";
        final String parentClassFQName = packageName + "." + parentClass;
        File parentFile = new File(parentClassFQName.replace(".","/") + fileExtension);
        if(!parentFile.exists()) {
            generateParentClass(parentFile, parentClass, packageName);
        }
	    
        // establish file
        String className = describe.getName();
        File file = new File(baseFolder + "/" + className + fileExtension);
        if(file.exists()) {
            System.out.println("Could not generate code for " + className + "! File exists. Please move or delete.");
            return false;
        } else {
            if(!file.createNewFile()) return false;
            System.out.println("Generating Java code for " + className);
        }
        FileWriter writer = new FileWriter(file);

        // package
        writer.append("package " + packageName + ";" + NEWLINE + NEWLINE);

        // class comment block
        writer.append("/**" + NEWLINE);
        writer.append(" * Generated from information gathered from /services/data/v" + VERSION_EXTERNAL + 
                ".0/sobjects/" + className + "/describe" + NEWLINE);
        writer.append(" * @author " + System.getProperty("user.name") + NEWLINE);
        writer.append(" * @since " + VERSION_INTERNAL + NEWLINE);
        writer.append(" */" + NEWLINE);
        
        // class begin
        writer.append("public class " + className + " extends " + parentClass + " {" + NEWLINE);

        // constants
        writer.append(TAB + "public static boolean CREATEABLE = "); 
        writer.append(sobject.isCreateable() ? "true;" : "false;");
        writer.append(NEWLINE);

        writer.append(TAB + "public static boolean DELETABLE = ");
        writer.append(sobject.isDeletable() ? "true;" : "false;");
        writer.append(NEWLINE);

        writer.append(TAB + "public static boolean UPDATEABLE = ");
        writer.append(sobject.isUpdateable() ? "true;" : "false;");
        writer.append(NEWLINE);
        
        writer.append(NEWLINE);

        // add all private member variables
        for (Field field : describe.getAllFields()) {
            String fieldNameLower = 
                field.getName().substring(0, 1).toLowerCase()
                    + field.getName().substring(1, field.getName().length());

            // write private member
            writer.append(TAB + "private " + getJavaType(field) + " " + fieldNameLower + ";" + NEWLINE);
            
        }
        
        // no arg constructor
        writer.append(NEWLINE + TAB + "/**" + NEWLINE);
        writer.append(TAB + " * Constructor." + NEWLINE);
        writer.append(TAB + " */" + NEWLINE);
        writer.append(TAB + "public " + className + "() { }" + NEWLINE);
        
        // constructor with required fields
        int count = 0;
        int max = describe.getRequiredFieldsForCreateUpdate().size();
        if(max > count) {
            
            writer.append(NEWLINE + TAB + "/**" + NEWLINE);
            writer.append(TAB + " * Constructor with required fields." + NEWLINE);
            writer.append(TAB + " */" + NEWLINE);
            writer.append(TAB + "public " + className + "(");
           
            for (Field field : describe.getRequiredFieldsForCreateUpdate()) {
                String fieldNameLower = field.getName().substring(0, 1).toLowerCase()
                        + field.getName().substring(1, field.getName().length());
   
                writer.append(getJavaType(field) + " " + fieldNameLower);
                if (++count != max) { writer.append(", "); }
            }
            writer.append(") {" + NEWLINE);
            
            //constructor body
            for (Field field : describe.getRequiredFieldsForCreateUpdate()) {
                String fieldNameLower = field.getName().substring(0, 1).toLowerCase()
                        + field.getName().substring(1, field.getName().length());
    
                writer.append(TAB + TAB + "this." + fieldNameLower + " = " + fieldNameLower + ";" + NEWLINE);
            }
            writer.append(TAB + "}" + NEWLINE + NEWLINE);
        }

        // add getters for every private member variable (no need to hide anything)
        for (Field field : describe.getAllFields()) {
            String fieldNameLower = field.getName().substring(0, 1).toLowerCase()
                    + field.getName().substring(1, field.getName().length());

            writer.append(TAB + "public " + getJavaType(field) + " get" + field.getName() + "() {"
                    + NEWLINE);
            writer.append(TAB + TAB + "return " + fieldNameLower + ";" + NEWLINE);
            writer.append(TAB + "}" + NEWLINE);
        }

        // add setters for all required fields, and name them as such to communicate this to the programmer
        for (Field field : describe.getRequiredFieldsForCreateUpdate()) {
            String fieldNameUpper = field.getName();
            String fieldNameLower = field.getName().substring(0, 1).toLowerCase()
                    + field.getName().substring(1, field.getName().length());

            // special naming: setRequiredXxxxxxxx

            writer.append(TAB+ "/**" + NEWLINE);
            writer.append(TAB + " * " + fieldNameUpper + " is a required field." + NEWLINE);
            writer.append(TAB+ " */" + NEWLINE);
            writer.append(TAB + "public void setRequired" + fieldNameUpper + "(" + getJavaType(field)
                    + " " + fieldNameLower + ") {" + NEWLINE);
            writer.append(TAB + TAB + "this." + fieldNameLower + " = " + fieldNameLower + ";" + NEWLINE);
            writer.append(TAB + "}" + NEWLINE);
        }

        // add setters for all optional fields, and name them as such to communicate this to the programmer
        for (Field field : describe.getOptionalFieldsForCreateUpdate()) {
            String fieldNameUpper = field.getName();
            String fieldNameLower = field.getName().substring(0, 1).toLowerCase()
                    + field.getName().substring(1, field.getName().length());

            // special naming: setOptionalXxxxxxxx
            writer.append(TAB+ "/**" + NEWLINE);
            writer.append(TAB + " * " + fieldNameUpper + " is an optional field." + NEWLINE);
            writer.append(TAB+ " */" + NEWLINE);
            writer.append(TAB + "public void setOptional" + fieldNameUpper + "(" + getJavaType(field)
                    + " " + fieldNameLower + ") {" + NEWLINE);
            writer.append(TAB + TAB + "this." + fieldNameLower + " = " + fieldNameLower + ";" + NEWLINE);
            writer.append(TAB + "}" + NEWLINE + NEWLINE);
        }

        // Note: there are no setters for things that can never be set, like Ids and system fields

        // class end
        writer.append("}" + NEWLINE);
        writer.close();
        
        //file created
        return true;
    }
	
	private boolean generateParentClass(File file, final String className, final String packageName) throws IOException {
	    if(!file.createNewFile()) return false;
	
        FileWriter writer = new FileWriter(file);

        // package
        writer.append("package " + packageName + ";" + NEWLINE + NEWLINE);

        // class comment block
        writer.append("/**" + NEWLINE);
        writer.append(" * Parent class." + NEWLINE);
        writer.append(" * @author " + System.getProperty("user.name") + NEWLINE);
        writer.append(" * @since " + VERSION_INTERNAL + NEWLINE);
        writer.append(" */" + NEWLINE);
        
        // class begin
        writer.append("public abstract class " + className + " {" + NEWLINE + NEWLINE + "}" + NEWLINE);
        writer.close();
        System.out.println("Wrote parent class " + className + fileExtension);
        return true;
	}

    /**
     * This method attempts to match the salesforce sobject field type to Java. If it doesn't know, it returns String.
     * 
     * @param salesforceFieldType
     * @return
     */
    private String getJavaType(Field field) {
        if (field.getType().equals("int")) {
            return "Integer";
        } 
        else if (field.getType().equals("boolean")) {
            return "Boolean";
        } 
        else if (field.getType().equals("double")) {
            return "Double";
        } 
        else {
            return "String";
        }
    }
}
