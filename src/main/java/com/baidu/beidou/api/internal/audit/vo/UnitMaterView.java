package com.baidu.beidou.api.internal.audit.vo;

public class UnitMaterView {
    
    private Integer type;	// 1：文字，2：图片，3：flash，5：图文
	private String title;
	private String desc1;
	private String desc2;
	private String targetUrl;
	private String showUrl;
	private String wirelessTargetUrl;
	private String wirelessShowUrl;
	private String materUrl;
	private Integer width;
	private Integer height;
	
	private Integer isSmart = 0;	// 智能创意标识，0：普通创意，默认值；1：智能创意
	private boolean isAudit;	// 是否是初审，true代表初审，false则代表复审或者审核历史页面
	private Integer templateId = 0;	// 智能创意模板id，默认为0，没有模板
	
	private TemplateElementUrlVo[] elementUrlList;  // 当为智能创意时，该列表才有可能存在
    private String fileSrcMd5; // 多媒体MD5
	
	public UnitMaterView() {
    }
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getDesc2() {
		return desc2;
	}
	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public String getShowUrl() {
		return showUrl;
	}
	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}
	public String getWirelessTargetUrl() {
		return wirelessTargetUrl;
	}
	public void setWirelessTargetUrl(String wirelessTargetUrl) {
		this.wirelessTargetUrl = wirelessTargetUrl;
	}
	public String getWirelessShowUrl() {
		return wirelessShowUrl;
	}
	public void setWirelessShowUrl(String wirelessShowUrl) {
		this.wirelessShowUrl = wirelessShowUrl;
	}
	public String getMaterUrl() {
		return materUrl;
	}
	public void setMaterUrl(String materUrl) {
		this.materUrl = materUrl;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getIsSmart() {
		return isSmart;
	}
	public void setIsSmart(Integer isSmart) {
		this.isSmart = isSmart;
	}
	public boolean getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(boolean isAudit) {
		this.isAudit = isAudit;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
    public TemplateElementUrlVo[] getElementUrlList() {
        return elementUrlList;
    }
    public void setElementUrlList(TemplateElementUrlVo[] elementUrlList) {
        this.elementUrlList = elementUrlList;
    }
    public String getFileSrcMd5() {
        return fileSrcMd5;
    }
    public void setFileSrcMd5(String fileSrcMd5) {
        this.fileSrcMd5 = fileSrcMd5;
    }
}
