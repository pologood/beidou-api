package com.baidu.beidou.api.internal.audit.vo;

public class TemplateElementUrlVo {
    
    private String literal;
    private String targetUrl;
    private String wirelessTargetUrl;
    
    public TemplateElementUrlVo() {
    }
    
    public TemplateElementUrlVo(String literal, String targetUrl, String wirelessTargetUrl) {
        this.literal = literal;
        this.targetUrl = targetUrl;
        this.wirelessTargetUrl = wirelessTargetUrl;
    }
    
    public String getLiteral() {
        return literal;
    }
    public void setLiteral(String literal) {
        this.literal = literal;
    }
    public String getTargetUrl() {
        return targetUrl;
    }
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
    public String getWirelessTargetUrl() {
        return wirelessTargetUrl;
    }
    public void setWirelessTargetUrl(String wirelessTargetUrl) {
        this.wirelessTargetUrl = wirelessTargetUrl;
    }
    
}
