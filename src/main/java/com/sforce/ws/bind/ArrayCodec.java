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
package com.sforce.ws.bind;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.repackaged.com.google.common.base.Joiner;
import com.google.appengine.repackaged.com.google.common.base.Splitter;

/**
 * For embedding non-XML serialization of basic arrays within an XML document.
 * This treats the array as an atomic data-type which occupies a leaf-node on
 * the document tree.
 *
 * This should handle any T where "t equals new T( t.toString() )
 * @author wgray
 */
public class ArrayCodec {

    private static final String UTF8 = "UTF-8";

    /** Uses toString() to yield string serialization.
     *  Expects new X( String ) for deserialization. */
    public <T> String getValueAsString( Class<T> componentType, Object value ) {
        validateComponentType( componentType );
        try {
            Object[] array = (Object[])value;
            List<String> encoded = new ArrayList<String>( array.length );
            for( Object obj: array ) {
                if( obj == null ) {
                    encoded.add( null );
                } else {
                    encoded.add( '"' + URLEncoder.encode( obj.toString(), UTF8 ) + '"' );
                }
            }
            return "[" + Joiner.on(",").useForNull( "null" ).join( encoded ) + "]";
        } catch( UnsupportedEncodingException e ) {
            throw new RuntimeException( "Cannot encode array", e );
        }
    }

    public <T> T[] deserialize( Class<T> componentType, String source ) {
        if( source == null ) {
            throw new ArrayCodecException( "Unable to parse null array." );
        }
        List<T> result = new ArrayList<T>();
        source = trimEnclosing( source, '[', ']' ).trim();
        if( source.length() > 0 ) {
            List<String> encoded = Splitter.on( "," ).splitToList( source );
            for( String s: encoded ) {
                if( "null".equals( s )) {
                    result.add( null );
                } else {
                    try {
                        s = trimEnclosing( s, '"', '"' );
                        s = URLDecoder.decode( s, UTF8 );
                        T t = (T)componentType.getConstructor( String.class ).newInstance( s );
                        result.add( t );
                    } catch( Exception e ) {
                        throw new ArrayCodecException( "Cannot parse array", e );
                    }
                }
            }
        }
        @SuppressWarnings("unchecked")
        T[] t = (T[])result.toArray();
        return t;
    }

    private String trimEnclosing( String source, char open, char close ) {
        if( source.charAt( 0 ) != open || source.charAt( source.length() - 1 ) != close ) {
            throw new ArrayCodecException( "Cannot trim enclosing characters." );
        }
        return source.substring( 1, source.length() - 1 );
    }

    private <T> void validateComponentType( Class<T> type ) {
        // No multi-dimensioned arrays
        if( type.isArray() ) throw new ArrayCodecException( "Multi-dimensional arrays not supported." );
        // Must have constructor that takes String
        try {
            type.getConstructor( String.class );
        } catch( NoSuchMethodException e ) {
            throw new ArrayCodecException( "Cannot find suitable constructor" );
        }
    }

    @SuppressWarnings("serial")
    public static class ArrayCodecException extends RuntimeException {
        public ArrayCodecException( String message ) {
            super( message );
        }

        public ArrayCodecException( String message, Throwable th ) {
            super( message, th );
        }
    }

}
