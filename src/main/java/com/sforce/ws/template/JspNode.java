package com.sforce.ws.template;

/**
 * Root node for the JSP elements
 *
 * @author http://cheenath
 * @version 1.0
 * @since 1.0  Nov 21, 2005
 */
public interface JspNode {

    /**
     * convert this to JavaScript code.
     *
     * @param sb string buffer in which the JavaScript code is added.
     */
    public void toJavaScript(StringBuilder sb);
}
