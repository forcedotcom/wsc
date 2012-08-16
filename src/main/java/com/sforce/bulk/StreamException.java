package com.sforce.bulk;


/**
 * This class represents
 * <p/>
 * User: mcheenath
 * Date: Dec 14, 2010
 */
public class StreamException extends Exception {

    public StreamException(String message) {
        super(message);
    }

    public StreamException(String s, Throwable e) {
        super(s, e);
    }
}
