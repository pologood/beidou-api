package com.baidu.beidou.api.internal.audit.vo;

/**
 * ClassName: ProductView
 * Function: 产品预览信息
 *
 * @author genglei
 * @version cpweb-699
 * @date Mar 5, 2014
 */
public class ProductView {
	
	private Long unitId;	// 创意ID
	private Long productId;	// 产品ID
	private String htmlSnippet;	// 产品预览html
	private String url;	// 产品点击url
	private Integer userId;	// 用户ID
	private Integer width;
	private Integer height;
	
	// 智能文本创意需要以下三个字段
	private String title;
	private String desc1;
	private String desc2;
	
    // 智能图文创意增加图标url
    private String iconUrl;
	
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getHtmlSnippet() {
		return htmlSnippet;
	}
	public void setHtmlSnippet(String htmlSnippet) {
		this.htmlSnippet = htmlSnippet;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
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
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
