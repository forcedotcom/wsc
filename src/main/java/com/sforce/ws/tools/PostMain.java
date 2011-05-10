/*
 * Copyright (c) 2005, salesforce.com, inc.
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
package com.sforce.ws.tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

/**
 * This is a util class that can be used to post a message
 * to the specified endpoint.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 30, 2005
 */
public class PostMain {

    private PostMain(String url, String file, String tempDir) throws IOException {
        FileInputStream fio = new FileInputStream(file);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.addRequestProperty("Content-Type", "text/xml");
        connection.addRequestProperty("SOAPAction", "\"\"");

        OutputStream out = connection.getOutputStream();

        int ch;
        try {
            while ((ch = fio.read()) != -1) {
                out.write((char) ch);
            }
        } finally {
            fio.close();
            out.close();
        }

        InputStream io;

        boolean failed = false;
        try {
            io = connection.getInputStream();
            System.out.println("SUCCESS:");
        } catch (IOException e) {
            System.out.println("FAILED:");
            io = connection.getErrorStream();
            failed = true;
        }

        OutputStream fout;
        if (tempDir != null) {
            File f = File.createTempFile("result", "" + failed, new File("temp"));
            fout = new FileOutputStream(f);
        } else {
            fout = System.out;
        }

        try {
            StringBuffer sb = new StringBuffer();
            while ((ch = io.read()) != -1) {
                sb.append((char)ch);
            }
            boolean dumpResponse = false;
            if (failed || dumpResponse) {
                fout.write(sb.toString().getBytes());
            }
        } finally {
            fout.close();
            io.close();
        }
    }

    /**
     * Usage: post <endpoint> <request-file>
     *
     * @param args <endpoint> <request-file>
     * @throws IOException failed to post
     */
    public static void main(final String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("usage: post <endpoint> <input-file> [no-of-threads] [temp-dir]");
            System.exit(1);
        }
        final String endpoint = args[0];
        final String input = args[1];

        final String tempDir =  (args.length == 4) ? args[3] : null;

        if (args.length >= 3) {
            for (int i=0; i<Integer.parseInt(args[2]); i++) {
                new Thread("Post Main test thread " + i) {
                    @Override
                    public void run() {
                        try {
                            new PostMain(endpoint, input, tempDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } else {
          new PostMain(endpoint, input, tempDir);
        }
    }
}
