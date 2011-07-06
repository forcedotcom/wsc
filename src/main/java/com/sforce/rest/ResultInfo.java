package com.sforce.rest;

/**
 * 
 * @author gwester
 * @since 172
 */
public class ResultInfo {
    private String type;
    private String url;
    
    /**
     * This constructor is to fool static analysis.
     * I use reflection / GSON on this so it's not needed.
     * @param type
     * @param url
     */
    protected ResultInfo(String type, String url) {
        this.type = type;
        this.url = url;
    }
    
    public String getType() {
        return this.type;
    }
    public String getUrl() {
        return this.url;
    }
}
