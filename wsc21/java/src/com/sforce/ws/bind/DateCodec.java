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
package com.sforce.ws.bind;

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Taken from axis:
 * <p/>
 * The DateSerializer deserializes a Date.  Much of the work is done in the
 * base class.
 *
 * @author Sam Ruby (rubys@us.ibm.com)
 *         Modified for JAX-RPC @author Rich Scheuerle (scheu@us.ibm.com)
 */
public class DateCodec {

    private static final SimpleDateFormat zulu =
            new SimpleDateFormat("yyyy-MM-dd");
    //  0123456789 0 123456789


    public String getValueAsString(Object value) {
        Date date = value instanceof Date ? (Date) value : ((Calendar) value).getTime();

        // Serialize including convert to GMT
        synchronized (zulu) {
          // Sun JDK bug http://developer.java.sun.com/developer/bugParade/bugs/4229798.html
          return zulu.format(date);
        }
    }


    /**
     * The simple deserializer provides most of the stuff.
     * We just need to override makeValue().
     * @param source source
     * @return calendar
     */
    public Calendar deserialize(String source) {
        if (source == null) {
            throw new NumberFormatException("Unable to parse date");
        }

        Date result;
        boolean bc = false;
        Calendar calendar = Calendar.getInstance();

        // validate fixed portion of format
        if (source != null) {
            if (source.length() < 10)
                throw new NumberFormatException("Unable to parse date");

            if (source.charAt(0) == '+')
                source = source.substring(1);

            if (source.charAt(0) == '-') {
                source = source.substring(1);
                bc = true;
            }

            if (source.charAt(4) != '-' || source.charAt(7) != '-')
                throw new NumberFormatException("unable to parse date");
        }

        // convert what we have validated so far
        try {
            synchronized (zulu) {
                result = zulu.parse(source.substring(0, 10));
            }
        } catch (Exception e) {
            throw new NumberFormatException(e.toString());
        }

        // support dates before the Christian era
        if (bc) {
            calendar.setTime(result);
            calendar.set(Calendar.ERA, GregorianCalendar.BC);
            //result = calendar.getTime();
        } else {
            calendar.setTime(result);
        }

        return calendar;
    }
}

