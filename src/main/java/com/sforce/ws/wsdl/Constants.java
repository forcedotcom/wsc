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
package com.sforce.ws.wsdl;

/**
 * This class contains a list of string constants used while parsing WSDL.
 *
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0   Nov 5, 2005
 */
public interface Constants {
    String WSDL_NS = "http://schemas.xmlsoap.org/wsdl/";
    String WSDL_SOAP_NS = "http://schemas.xmlsoap.org/wsdl/soap/";
    String SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";
    String SCHEMA_INSTANCE_NS = "http://www.w3.org/2001/XMLSchema-instance";
    String SOAP_ENVELOPE_NS = "http://schemas.xmlsoap.org/soap/envelope/";
    String ENTERPRISE_NS = "urn:enterprise.soap.sforce.com";
    String ENTERPRISE_SOBJECT_NS = "urn:sobject.enterprise.soap.sforce.com";
    String PARTNER_NS = "urn:partner.soap.sforce.com";
    String PARTNER_SOBJECT_NS = "urn:sobject.partner.soap.sforce.com";

    String META_SFORCE_NS = "http://soap.sforce.com/2006/04/metadata";
    String CROSS_INSTANCE_SFORCE_NS = "http://soap.sforce.com/2006/05/crossinstance";
    String INTERNAL_SFORCE_NS = "http://soap.sforce.com/2007/07/internal";
    String CLIENT_SYNC_SFORCE_NS = "http://soap.sforce.com/2009/10/clientsync";
    String SYNC_API_SFORCE_NS = "http://soap.sforce.com/schemas/class/syncapi/Command";

    String SCHEMA = "schema";
    String TYPES = "types";
    String DOCUMENTATION = "documentation";
    String DEFINITIONS = "definitions";
    String MESSAGE = "message";
    String PORT_TYPE = "portType";
    String PORT = "port";
    String BINDING = "binding";
    String SERVICE = "service";
    String TARGET_NAME_SPACE = "targetNamespace";
    String ELEMENT_FORM_DEFAULT = "elementFormDefault";
    String ATTRIBUTE_FORM_DEFAULT = "attributeFormDefault";
    String COMPLEX_TYPE = "complexType";
    String SIMPLE_TYPE = "simpleType";
    String NAME = "name";
    String VALUE = "value";
    String TYPE = "type";
    String REF = "ref";
    String SEQUENCE = "sequence";
    String RESTRICTION = "restriction";
    String ELEMENT = "element";
    String ENUMERATION = "enumeration";
    String NILLABLE = "nillable";
    String MIN_OCCURS = "minOccurs";
    String MAX_OCCURS = "maxOccurs";
    String EXTENSION = "extension";
    String BASE = "base";
    String INPUT = "input";
    String OUTPUT = "output";
    String FAULT = "fault";
    String OPERATION = "operation";
    String PART = "part";
    String ADDRESS = "address";
    String LOCATION = "location";
    String STYLE = "style";
    String TRANSPORT = "transport";
    String HEADER = "header";
    String BODY = "body";
    String IMPORT = "import";
    String ANNOTATION = "annotation";
    String ATTRIBUTE = "attribute";
    String ALL = "all";
    String CHOICE = "choice";
}
