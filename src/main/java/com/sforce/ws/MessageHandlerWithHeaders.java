/*
 * Copyright, 1999-2010 
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.ws;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * MessageHandlerWithHeaders
 *
 * @author dkador
 * @since 166
 */
public interface MessageHandlerWithHeaders extends MessageHandler {

    void handleRequest(URL endpoint, byte[] request, Map<String, List<String>> headers);

    void handleResponse(URL endpoint, byte[] response, Map<String, List<String>> headers);

}
