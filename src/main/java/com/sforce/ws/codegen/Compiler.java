/*
 * Copyright (c) 2017, salesforce.com, inc.
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

package com.sforce.ws.codegen;

import java.io.File;
import com.sforce.ws.tools.ToolsException;
import com.sforce.ws.util.Verbose;
import javax.tools.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author hhildebrand
 * @since 184
 */
class Compiler {
    JavaCompiler compiler;
    DiagnosticCollector< JavaFileObject > ds;
    StandardJavaFileManager mgr;

    public Compiler() {
        compiler = ToolProvider.getSystemJavaCompiler();
        ds = new DiagnosticCollector<>();
        mgr = compiler.getStandardFileManager( ds, null, null );

    }

    public void compile(String[] files) throws ToolsException {
        List<File> generatedFiles = new ArrayList<>();
        for (int itr = 0; itr< files.length ; itr++){
            generatedFiles.add(new File(files[itr]));
        }
        Iterable<? extends JavaFileObject> sourceFiles =
                mgr.getJavaFileObjectsFromFiles( generatedFiles );
        JavaCompiler.CompilationTask task =
                compiler.getTask( null, mgr, ds, null,
                        null, sourceFiles );
        task.call();

        int diagnosticWithError = 0;
        for( Diagnostic < ? extends JavaFileObject >
                d: ds.getDiagnostics() ) {
            if(d.getKind() == Diagnostic.Kind.ERROR){
                System.out.format("Error at Line: %d, %s in %s \n",
                        d.getLineNumber(), d.getMessage( null ),
                        d.getSource().getName());
                diagnosticWithError++;
            } else {
                System.out.println(d);
            }
        }

        if(diagnosticWithError > 0){
            throw new ToolsException("Failed to compile");
        }
        Verbose.log("Compiled " + files.length + " java files.");
    }
}
