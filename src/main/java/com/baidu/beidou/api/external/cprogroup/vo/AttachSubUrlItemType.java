package com.baidu.beidou.api.external.cprogroup.vo;

/**
 * Function: 附加信息，静态子链具体信息，包括标题、URL或者移动URL。
 * 
 * @ClassName: AttachSubUrlItemType
 * @author genglei01
 * @date Jun 18, 2015 4:17:51 PM
 */
public class AttachSubUrlItemType {
    
    /**
     * 附加子链静态子链标题和链接，attach_type=512时有效
     */
    private String attachSubUrlTitle = "";
    private String attachSubUrlLink = "";
    private String attachSubUrlWirelessLink = "";
    
    /**
     * Function: getAttachSubUrlTitle
     * 
     * @author genglei01
     * @return String 标题
     */
    public String getAttachSubUrlTitle() {
        return attachSubUrlTitle;
    }
    /**
     * Function: setAttachSubUrlTitle
     * 
     * @author genglei01
     * @param attachSubUrlTitle 标题
     */
    public void setAttachSubUrlTitle(String attachSubUrlTitle) {
        this.attachSubUrlTitle = attachSubUrlTitle;
    }
    /**
     * Function: getAttachSubUrlLink
     * 
     * @author genglei01
     * @return String 静态子链URL
     */
    public String getAttachSubUrlLink() {
        return attachSubUrlLink;
    }
    /**
     * Function: setAttachSubUrlLink
     * 
     * @author genglei01
     * @param attachSubUrlLink 静态子链URL
     */
    public void setAttachSubUrlLink(String attachSubUrlLink) {
        this.attachSubUrlLink = attachSubUrlLink;
    }
    /**
     * Function: getAttachSubUrlWirelessLink
     * 
     * @author genglei01
     * @return String 静态子链无线URL
     */
    public String getAttachSubUrlWirelessLink() {
        return attachSubUrlWirelessLink;
    }
    /**
     * Function: setAttachSubUrlWirelessLink
     * 
     * @author genglei01
     * @param attachSubUrlWirelessLink 静态子链无线URL
     */
    public void setAttachSubUrlWirelessLink(String attachSubUrlWirelessLink) {
        this.attachSubUrlWirelessLink = attachSubUrlWirelessLink;
    }
    
}
