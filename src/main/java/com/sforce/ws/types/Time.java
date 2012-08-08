/*
 * Copyright 2002-2004 The Apache Software Foundation.
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
package com.sforce.ws.types;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class that represents the xsd:time XML Schema type
 *
 * Taken from Apache Axis 1.4.  Modified slightly by btsai.
 */
public class Time implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5384500866129738074L;


	private Calendar _value;


    /**
     * a shared java.text.SimpleDateFormat instance used for parsing the basic
     * component of the timestamp
     */
    private static final SimpleDateFormat zulu =
       new SimpleDateFormat("HH:mm:ss.SSS'Z'");

    // We should always format dates in the GMT timezone
    static {
        zulu.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    /**
     * Initialize with a Calender, year month and date are ignored
     * @param value initial value
     */
    public Time(Calendar value) {
        this._value = value;
        _value.set(0,0,0);      // ignore year, month, date
    }

    /**
     * Converts a string formatted as HH:mm:ss[.SSS][+/-offset]
     * @param value value
     * @throws NumberFormatException failed to parse value
     */
    public Time(String value) throws NumberFormatException {
        _value = makeValue(value);
    }

    /**
     * Initializes with a Calendar instance in GMT based on the number
     * of milliseconds since midnight.  Year, month, and date are ignored.
     * @param value value
     */
    public Time(long value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(value);
        calendar.set(0,0,0);    // ignore year, month, date

        _value = calendar;
    }

    /**
     * Returns the time in milliseconds since midnight in UTC.
     * @return time in milli sec
     */
    public long getTimeInMillis() {
        Calendar zeroCalendar = Calendar.getInstance();
        zeroCalendar.set(0,0,0,0,0,0);
        zeroCalendar.set(Calendar.MILLISECOND, 0);

        return _value.getTimeInMillis() - zeroCalendar.getTimeInMillis();
    }

    /**
     * Utility function that parses xsd:time strings and returns a Date object
     * @param source source
     * @return calendar
     * @throws NumberFormatException failed to parse
     */
    private Calendar makeValue(String source) throws NumberFormatException {
        Calendar calendar = Calendar.getInstance();
        Date date;

        validateSource(source);

        // convert what we have validated so far
        date = ParseHoursMinutesSeconds(source);

        int pos = 8;    // The "." in hh:mm:ss.sss

        // parse optional milliseconds
        if ( source != null ) {
            if (pos < source.length() && source.charAt(pos)=='.') {
                int milliseconds;
                int start = ++pos;
                while (pos<source.length() &&
                       Character.isDigit(source.charAt(pos))) {
                    pos++;
                }


                String decimal=source.substring(start,pos);
                if (decimal.length()==3) {
                    milliseconds=Integer.parseInt(decimal);
                } else if (decimal.length() < 3) {
                    milliseconds=Integer.parseInt((decimal+"000")
                                                  .substring(0,3));
                } else {
                    milliseconds=Integer.parseInt(decimal.substring(0,3));
                    if (decimal.charAt(3)>='5') {
                        ++milliseconds;
                    }
                }

                // add milliseconds to the current date
                date.setTime(date.getTime()+milliseconds);
            }

            // parse optional timezone
            if (pos+5 < source.length() &&
                (source.charAt(pos)=='+' || (source.charAt(pos)=='-'))) {
                    if (!Character.isDigit(source.charAt(pos+1)) ||
                        !Character.isDigit(source.charAt(pos+2)) ||
                        source.charAt(pos+3) != ':'              ||
                        !Character.isDigit(source.charAt(pos+4)) ||
                        !Character.isDigit(source.charAt(pos+5)))
                    {
                        throw new IllegalArgumentException("Could not parse string for Time, invalid timezone: '"+source+"'");
                    }

                    int hours = (source.charAt(pos+1)-'0')*10
                        +source.charAt(pos+2)-'0';
                    int mins  = (source.charAt(pos+4)-'0')*10
                        +source.charAt(pos+5)-'0';
                    int milliseconds = (hours*60+mins)*60*1000;

                    // subtract milliseconds from current date to obtain GMT
                    if (source.charAt(pos)=='+') {
                        milliseconds=-milliseconds;
                    }
                    date.setTime(date.getTime()+milliseconds);
                    pos+=6;
            }

            if (pos < source.length() && source.charAt(pos)=='Z') {
                pos++;
                calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            }

            if (pos < source.length()) {
                throw new IllegalArgumentException("Could not parse string for Time, unexpected characters: '"+source+"'");
            }
        }

        calendar.setTime(date);
        calendar.set(0,0,0);    // ignore year, month, date

        return calendar;
    }

    /**
     * parse the hours, minutes and seconds of a string, by handing it off to
     * the java runtime.
     * The relevant code will return null if a null string is passed in, so this
     * code may return a null date in response
     * @param source source
     * @return Date
     * @throws NumberFormatException in the event of trouble
     */
    private static Date ParseHoursMinutesSeconds(String source) {
        Date date;
        try {
            if (source == null) {
                throw new NullPointerException("Source can not be null");
            }
            synchronized (zulu) {
                String fulltime = source.substring(0,8)+".000Z";
                date = zulu.parse(fulltime);
            }
        } catch (Exception e) {
            throw new NumberFormatException(e.toString());
        }
        return date;
    }

    /**
     * validate the source
     * @param source source
     */
    private void validateSource(String source) {
        // validate fixed portion of format
        if ( source != null ) {
            if (source.charAt(2) != ':' || source.charAt(5) != ':') {
                throw new IllegalArgumentException("Could not parse string for Time, invalid time: '"+source+"'");
            }
            if (source.length() < 8) {
                throw new IllegalArgumentException("Could not parse string for Time, invalid time: '"+source+"'");
            }
        }
    }

    /**
     * stringify method returns the time as it would be in GMT, only accurate to the
     * second...millis probably get lost.
     * @return string
     */
    @Override
    public String toString() {
        if(_value==null) {
            return "unassigned Time";
        }
        synchronized (zulu) {
            return zulu.format(_value.getTime());
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Time)) return false;
        Time other = (Time) obj;
        if (this == obj) return true;

        boolean _equals;
        _equals = true &&
            ((_value ==null && other._value ==null) ||
             (_value !=null &&
              _value.getTime().equals(other._value.getTime())));

        return _equals;

    }

    /**
     * Returns the hashcode of the underlying calendar.
     *
     * @return an <code>int</code> value
     */
    @Override
    public int hashCode() {
        return _value == null ? 0 : _value.hashCode();
    }
}
