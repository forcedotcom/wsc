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

package com.sforce.ws.bind;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The CalendarSerializer deserializes a dateTime.
 * Much of the work is done in the base class.
 *
 * @author Sam Ruby (rubys@us.ibm.com)
 *         Modified for JAX-RPC @author Rich Scheuerle (scheu@us.ibm.com)
 * @author http://cheenath.com adopted from Apache AXIS
 * @version 1.0
 * @since 1.0  Dec 2, 2005
 */
public class CalendarCodec {

  private static final SimpleDateFormat zulu =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  //  0123456789 0 123456789

  static {
    zulu.setTimeZone(TimeZone.getTimeZone("GMT"));
  }

  public String getValueAsString(Object value) {
    Date date = value instanceof Date ? (Date) value :
        ((Calendar) value).getTime();

    // Serialize including convert to GMT
    synchronized (zulu) {
      // Sun JDK bug http://developer.java.sun.com/developer/bugParade/bugs/4229798.html
      return zulu.format(date);
    }
  }

  /**
   * The simple deserializer provides most of the stuff.
   * We just need to override makeValue().
   * @param source source string to deserialize
   * @return calendar created 
   */
  public Calendar deserialize(String source) {
    Calendar calendar = Calendar.getInstance();
    Date date;
    boolean bc = false;

    // validate fixed portion of format
    if (source == null || source.length() == 0) {
      throw new NumberFormatException("Unable to parse dateTime");
    }
    if (source.charAt(0) == '+') {
      source = source.substring(1);
    }
    if (source.charAt(0) == '-') {
      source = source.substring(1);
      bc = true;
    }
    if (source.length() < 19) {
      throw new NumberFormatException("Unable to parse dateTime");
    }
    if (source.charAt(4) != '-' || source.charAt(7) != '-' ||
        source.charAt(10) != 'T') {
      throw new NumberFormatException("Unable to parse dateTime");
    }
    if (source.charAt(13) != ':' || source.charAt(16) != ':') {
      throw new NumberFormatException("Unable to parse dateTime");
    }
    // convert what we have validated so far
    try {
      synchronized (zulu) {
        date = zulu.parse(source.substring(0, 19) + ".000Z");
      }
    } catch (Exception e) {
      throw new NumberFormatException(e.toString());
    }
    int pos = 19;

    // parse optional milliseconds
    if (pos < source.length() && source.charAt(pos) == '.') {
      int milliseconds;
      int start = ++pos;
      while (pos < source.length() &&
          Character.isDigit(source.charAt(pos))) {
        pos++;
      }
      String decimal = source.substring(start, pos);
      if (decimal.length() == 3) {
        milliseconds = Integer.parseInt(decimal);
      } else if (decimal.length() < 3) {
        milliseconds = Integer.parseInt((decimal + "000")
            .substring(0, 3));
      } else {
        milliseconds = Integer.parseInt(decimal.substring(0, 3));
        if (decimal.charAt(3) >= '5') {
          ++milliseconds;
        }
      }

      // add milliseconds to the current date
      date.setTime(date.getTime() + milliseconds);
    }

    // parse optional timezone
    if (pos + 5 < source.length() &&
        (source.charAt(pos) == '+' || (source.charAt(pos) == '-'))) {
      if (!Character.isDigit(source.charAt(pos + 1)) ||
          !Character.isDigit(source.charAt(pos + 2)) ||
          source.charAt(pos + 3) != ':' ||
          !Character.isDigit(source.charAt(pos + 4)) ||
          !Character.isDigit(source.charAt(pos + 5))) {
        throw new NumberFormatException("Unable to parse dateTime");
      }
      int hours = (source.charAt(pos + 1) - '0') * 10
          + source.charAt(pos + 2) - '0';
      int mins = (source.charAt(pos + 4) - '0') * 10
          + source.charAt(pos + 5) - '0';
      int milliseconds = (hours * 60 + mins) * 60 * 1000;

      // subtract milliseconds from current date to obtain GMT
      if (source.charAt(pos) == '+') {
        milliseconds = -milliseconds;
      }
      date.setTime(date.getTime() + milliseconds);
      pos += 6;
    }
    if (pos < source.length() && source.charAt(pos) == 'Z') {
      pos++;
      calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    if (pos < source.length()) {
      throw new NumberFormatException("Unable to parse dateTime");
    }
    calendar.setTime(date);

    // support dates before the Christian era
    if (bc) {
      calendar.set(Calendar.ERA, GregorianCalendar.BC);
    }
    /*
   if (super.javaType == Date.class) {
       return date;
   } else {
      return calendar;
   } */
    return calendar;
  }
}
