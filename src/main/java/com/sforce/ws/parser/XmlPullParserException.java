package com.sforce.ws.parser;

/* -*-             c-basic-offset: 4; indent-tabs-mode: nil; -*-  //------100-columns-wide------>|*/
// for license please see accompanying LICENSE.txt file (available also at http://www.xmlpull.org/)

/**
 * This exception is thrown to signal XML Pull Parser related faults.
 *
 * @author <a href="http://www.extreme.indiana.edu/~aslom/">Aleksander Slominski</a>
 */
public class XmlPullParserException extends PullParserException {
  /**
	 * 
	 */
	private static final long serialVersionUID = 2205184286094770620L;
protected int row = -1;
  protected int column = -1;


  public XmlPullParserException(String s) {
    super(s);
  }

  public XmlPullParserException(String msg, XmlPullParser parser, Throwable chain) {
    super((msg == null ? "" : msg + " ")
        + (parser == null ? "" : "(position:" + parser.getPositionDescription() + ") ")
        + (chain == null ? "" : "caused by: " + chain), chain);

    if (parser != null) {
      this.row = parser.getLineNumber();
      this.column = parser.getColumnNumber();
    }
  }

  public int getLineNumber() {
    return row;
  }

  public int getColumnNumber() {
    return column;
  }
}
