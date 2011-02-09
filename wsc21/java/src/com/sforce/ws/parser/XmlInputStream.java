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
package com.sforce.ws.parser;

import com.sforce.ws.ConnectionException;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is a minimal pull parser. It currently delegates to XPP parser available at
 * http://www.extreme.indiana.edu/xgws/xsoap/xpp/
 *
 * @author  http://cheenath.com
 * @version 1.0
 * @since   1.0   Nov 5, 2005
 */
public final class XmlInputStream {

  private MXParser parser = new MXParser();

  public static final int END_DOCUMENT = XmlPullParser.END_DOCUMENT;
  public static final int START_DOCUMENT = XmlPullParser.START_DOCUMENT;
  public static final int START_TAG = XmlPullParser.START_TAG;
  public static final int END_TAG = XmlPullParser.END_TAG;
  public static final int TEXT = XmlPullParser.TEXT;

  private static final int EMPTY = -99999;

  private int peekTag = EMPTY;

  public XmlInputStream() {
    try {
      parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
    } catch (XmlPullParserException e) {
      throw new InternalError("Unable to set feature:" + e);
    }
  }

  public void setInput(InputStream inputStream, String inputEncoding) throws PullParserException {
    parser.setInput(inputStream, inputEncoding);
  }

  public String getNamespace(String prefix) {
    return parser.getNamespace(prefix);
  }

  public String getPositionDescription() {
    return parser.getPositionDescription();
  }

  public int getLineNumber() {
      return parser.getLineNumber();
  }

  public int getColumnNumber() {
      return parser.getColumnNumber();
  }

  public String getNamespace() {
    return parser.getNamespace();
  }

  public String getName() {
    return parser.getName();
  }

  public String getAttributeValue(String namespace, String name) {
    return parser.getAttributeValue(namespace, name);
  }

  public int getAttributeCount() {
      return parser.getAttributeCount();
  }

  public String getAttributeValue(int index) {
      return parser.getAttributeValue(index);
  }

  public String getAttributeName(int index) {
      return parser.getAttributeName(index);
  }

  public String getAttributeNamespace(int index) {
      return parser.getAttributeNamespace(index);
  }

  public void consumePeeked() {
      peekTag = EMPTY;
  }

  public int getEventType() throws ConnectionException{
    if (peekTag != EMPTY) {
      return peekTag;
    } else {
        try {
            return parser.getEventType();
        } catch (XmlPullParserException e) {
            throw new ConnectionException("Failed to get event type", e);
        }
    }
  }

  public int next() throws IOException, ConnectionException {
    if (peekTag != EMPTY) {
      int t = peekTag;
      peekTag = EMPTY;
      return t;
    }

    try {
      return parser.next();
    } catch (XmlPullParserException e) {
      throw new ConnectionException("Found invalid XML. " + e.getMessage(), e);
    }
  }

  @Override
  public String toString() {
    return parser.getPositionDescription();
  }

  public String nextText() throws IOException, ConnectionException {
    try {
      return parser.nextText();
    } catch (XmlPullParserException e) {
      throw new ConnectionException("Failed to get text", e);
    }
  }

  public String getText() {
      return parser.getText();
  }

  public int nextTag() throws IOException, ConnectionException {
    if (peekTag != EMPTY) {
      int t = peekTag;
      peekTag = EMPTY;
      return t;
    }

    try {
      return parser.nextTag();
    } catch (XmlPullParserException e) {
      throw new ConnectionException("Failed to get next element", e);
    }
  }

  public int peekTag() throws ConnectionException, IOException {
    if (peekTag != EMPTY) {
      return peekTag;
    }

    peekTag = nextTag();
    return peekTag;
  }
}
