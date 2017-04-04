package com.baidu.beidou.api.internal.audit.vo.request;

/**
 * ClassName: QueryProduct
 * Function: 智能创意产品预览列表请求
 *
 * @author genglei
 * @version cpweb-699
 * @date Mar 5, 2014
 */
public class QueryProduct {
	
	private Long unitId;
	private Integer groupId;
	private Integer planId;
	private Integer userId;
	private Integer templateId;	// 智能创意模板ID
	private Integer width;
	private Integer height;
	private Integer type;	// 物料类型，type=1代表智能文本，type=9代表智能图片html
	
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getPlanId() {
		return planId;
	}
	public void setPlanId(Integer planId) {
		this.planId = planId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
