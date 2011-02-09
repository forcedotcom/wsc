/*
 * Copyright, 1999-2006, SALESFORCE.com
 * All Rights Reserved
 * Company Confidential
 */
package com.sforce.ws;

import java.net.URL;

/**
 * MessageHandler
 *
 * @author mcheenath
 * @since 144
 */
public interface MessageHandler {
    /**
     *
     * @param request request data
     * @param endpoint endpoint url
     */
    void handleRequest(URL endpoint, byte[] request);

    /**
     *
     * @param response response data
     * @param endpoint endpoint url
     */
    void handleResponse(URL endpoint, byte[] response);
}
