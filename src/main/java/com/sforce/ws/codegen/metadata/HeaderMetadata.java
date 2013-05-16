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
package com.sforce.ws.codegen.metadata;

import java.util.List;

/**
 * @author btoal 
 * @author hhildebrand
 * @since 184
 */
public class HeaderMetadata {
    private final String type;
    private final String name;
    private final String args;
    private final String element;
    private final List<HeaderElementMetadata> elements;

    public HeaderMetadata(String type, String name, String args, String element, List<HeaderElementMetadata> elements) {
        this.type = type;
        this.name = name;
        this.args = args;
        this.element = element;
        this.elements = elements;
    }

    public static HeaderMetadata newInstance(final String type, final String name, final String args, final String element, final List<HeaderElementMetadata> elements) {
        return new HeaderMetadata(type, name, args, element, elements);
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getArgs() {
        return this.args;
    }

    public List<HeaderElementMetadata> getElements() {
        return this.elements;
    }
    
    public static class HeaderElementMetadata {
        private final String setMethod;
        private final String name;

        public static HeaderElementMetadata newInstance(final String setMethod, final String name) {
            return new HeaderElementMetadata(setMethod, name);
        }
        
        private HeaderElementMetadata(String setMethod, String name) {
            this.setMethod = setMethod;
            this.name = name;
        }

        public String getSetMethod() {
            return this.setMethod;
        }

        public String getName() {
            return this.name;
        }
    }

    public String getElement() {
        return element;
    }
}
