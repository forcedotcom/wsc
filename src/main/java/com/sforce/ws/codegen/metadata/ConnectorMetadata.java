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

package com.sforce.ws.codegen.metadata;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.wsdl.Definitions;

/**
 * @author hhildebrand
 * @since 184
 */
public class ConnectorMetadata extends ClassMetadata {

    private final String endpoint;

    public ConnectorMetadata(Definitions definitions, String packagePrefix) {
        this(NameMapper.getPackageName(definitions.getTargetNamespace(), packagePrefix),
                (definitions.getApiType() != null ? definitions.getApiType().name() : "Soap") + "Connection",
                definitions.getService().getPort().getSoapAddress().getLocation());
    }

    public ConnectorMetadata(Definitions definitions, String packagePrefix, boolean addDeprecatedAnnotation) {
        this(NameMapper.getPackageName(definitions.getTargetNamespace(), packagePrefix),
                (definitions.getApiType() != null ? definitions.getApiType().name() : "Soap") + "Connection",
                definitions.getService().getPort().getSoapAddress().getLocation(), addDeprecatedAnnotation);
    }

    public ConnectorMetadata(String packageName, String className, String endpoint) {
        super(packageName, className);
        this.endpoint = endpoint;
    }

    public ConnectorMetadata(String packageName, String className, String endpoint, boolean addDeprecatedAnnotation) {
        super(packageName, className, null, addDeprecatedAnnotation);
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return this.endpoint;
    }
}
