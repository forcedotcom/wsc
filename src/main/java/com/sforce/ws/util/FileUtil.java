/*
 * Copyright (c) 2013, salesforce.com, inc.
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

package com.sforce.ws.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains util method related to File handeling.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0  Nov 11, 2005
 */
public class FileUtil {

    /**
     * end of line
     */
    public static final String EOL = System.getProperty("line.separator");

    /**
     * @param file
     *
     * @return
     */
    public static String toString(File file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        return toString(fin);
    }

    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        int ch;
        while ((ch = in.read()) != -1) {
            bout.write(ch);
        }

        in.close();
        return bout.toByteArray();
    }

    public static void copy(InputStream from, OutputStream to) throws IOException {
        copy(from, to, true);
    }

    public static void copy(InputStream from, OutputStream to, boolean closeOutput) throws IOException {
        copy(from, true, to, closeOutput);
    }

    public static void copy(InputStream from, boolean closeInput, OutputStream to, boolean closeOutput)
            throws IOException {
        try {
            byte[] buf = new byte[1024];

            int count;
            while ((count = from.read(buf, 0, buf.length)) != -1) {
                to.write(buf, 0, count);
            }
        } finally {
            try {
                if (closeOutput) to.close();
            } finally {
                if (closeInput) from.close();
            }
        }
    }

    private static String toString(InputStream in) throws IOException {
        return new String(toBytes(in));
    }

    public static File mkdirs(String packageName, File root) {
        String[] dirs = packageName.split("\\.");

        for (String dir : dirs) {
            File f = new File(root, dir);

            if (!f.exists()) {
                f.mkdir();
            }

            root = f;
        }

        return root;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static List<File> listFilesRecursive(File dir, boolean includeDirs) {
        List<File> result = null;
        if (dir.isDirectory()) {
            result = new ArrayList<File>();
            listFilesRecursive(dir, result, includeDirs);
        }
        return result;
    }

    private static void listFilesRecursive(File dir, List<File> result, boolean includeDirs) {
        for (String child : dir.list()) {
            File f = new File(dir, child);
            if (f.isDirectory()) {
                if (includeDirs) result.add(f);
                listFilesRecursive(f, result, includeDirs);
            } else {
                result.add(f);
            }
        }
    }

    public static File makeTempDirectory(String dirName, boolean deleteIfExists) throws IOException {
        return makeTempDirectory(null, dirName, deleteIfExists);
    }

    public static File makeTempDirectory(File parentTempDir, String dirName, boolean deleteIfExists) throws IOException {
        File dir = new File(getSystemTempDirectory(parentTempDir), dirName);
        if (dir.exists()) {
            if (deleteIfExists)
                deleteDir(dir);
            else
                throw new IOException("Cannot create directory that already exists: " + dir);
        }
        dir.mkdirs();
        dir.deleteOnExit();
        return dir;
    }

    public static File getSystemTempDirectory(File parentTempDir) throws IOException {
        if (parentTempDir != null && parentTempDir.exists() && parentTempDir.isDirectory()) return parentTempDir;

        final File t = File.createTempFile("java", ".tmp", parentTempDir);
        try {
            return t.getParentFile();
        } finally {
            t.delete();
        }
    }
}
