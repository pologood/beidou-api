package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.List;

public class AttachInfoType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int attachType;
	private String attachContent;
	private String attachMessage;
	private int status;

    /**
     * 附加子链统计参数，attach_type=512时该值为短信内容
     */
    private String attachSubUrlParam;
    /**
     * 附加子链静态子链标题和链接，attach_type=512时有效
     */
    private List<AttachSubUrlItemType> attachSubUrls;

    /**
     * 默认构造器
     * 
     */
    public AttachInfoType() {
    }

    /**
     * 构造器
     * 
     * @param attachType    attachType
     * @param attachContent attachContent
     * @param status    status
     * @param attachSubUrlParam attachSubUrlParam
     * @param attachSubUrls attachSubUrls
     */
    public AttachInfoType(int attachType, String attachContent, int status, 
            String attachSubUrlParam, List<AttachSubUrlItemType> attachSubUrls) {
        this.attachType = attachType;
        this.attachContent = attachContent;
        this.status = status;
        this.attachSubUrlParam = attachSubUrlParam;
        this.attachSubUrls = attachSubUrls;
    }
	
	public int getAttachType() {
		return attachType;
	}
	public void setAttachType(int attachType) {
		this.attachType = attachType;
	}
	public String getAttachContent() {
		return attachContent;
	}
	public void setAttachContent(String attachContent) {
		this.attachContent = attachContent;
	}
	
	public String getAttachMessage() {
		return attachMessage;
	}

	public void setAttachMessage(String attachMessage) {
		this.attachMessage = attachMessage;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

    /**
     * Function: getAttachSubUrlParam
     * 
     * @author genglei01
     * @return String
     */
    public String getAttachSubUrlParam() {
        return attachSubUrlParam;
    }

    /**
     * Function: setAttachSubUrlParam
     * 
     * @author genglei01
     * @param attachSubUrlParam attachSubUrlParam
     */
    public void setAttachSubUrlParam(String attachSubUrlParam) {
        this.attachSubUrlParam = attachSubUrlParam;
    }

    /**
     * Function: getAttachSubUrls
     * 
     * @author genglei01
     * @return List<AttachSubUrlItem>
     */
    public List<AttachSubUrlItemType> getAttachSubUrls() {
        return attachSubUrls;
    }

    /**
     * Function: setAttachSubUrls
     * 
     * @author genglei01
     * @param attachSubUrls attachSubUrls
     */
    public void setAttachSubUrls(List<AttachSubUrlItemType> attachSubUrls) {
        this.attachSubUrls = attachSubUrls;
    }

}
