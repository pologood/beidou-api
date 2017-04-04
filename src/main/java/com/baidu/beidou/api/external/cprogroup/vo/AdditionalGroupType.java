package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: AdditionalGroupType  <br>
 * Function: 推广组核心状态位以及出价更新接口
 *
 * @author zhangxu
 * @date Nov 5, 2012
 */
public class AdditionalGroupType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long groupId; 

	private String groupName; 
	
	private Integer price; //点击价格, 单位为分
	
	private Integer status; //状态，0：生效；1: 搁置；2：删除
	
	private Integer excludeGender;

	private Integer type; //推广组类型，bit位表示，可复选，1：固定；2：悬浮；4：贴片；

	private Integer targetType; //推广组定向方式
	
	private Boolean isAllRegion; //是否全地域
	
	private Boolean isAllSite; //是否全网
	
	private Boolean isItEnabled; //是否启用兴趣定向
	
	private Integer other1; 
	
	private String other2; 
	
	private Integer reserved1;
	
	private String reserved2;
	
	
	public long getGroupId() {
		return groupId;
	}



	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}



	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public Integer getPrice() {
		return price;
	}



	public void setPrice(Integer price) {
		this.price = price;
	}



	public Integer getStatus() {
		return status;
	}



	public void setStatus(Integer status) {
		this.status = status;
	}



	public Integer getExcludeGender() {
		return excludeGender;
	}



	public void setExcludeGender(Integer excludeGender) {
		this.excludeGender = excludeGender;
	}



	public Integer getType() {
		return type;
	}



	public void setType(Integer type) {
		this.type = type;
	}



	public Integer getTargetType() {
		return targetType;
	}



	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}



	public Boolean isAllRegion() {
		return isAllRegion;
	}



	public void setAllRegion(boolean isAllRegion) {
		this.isAllRegion = isAllRegion;
	}



	public Boolean isAllSite() {
		return isAllSite;
	}



	public void setAllSite(boolean isAllSite) {
		this.isAllSite = isAllSite;
	}



	public Boolean isItEnabled() {
		return isItEnabled;
	}



	public void setItEnabled(boolean isItEnabled) {
		this.isItEnabled = isItEnabled;
	}



	public Integer getOther1() {
		return other1;
	}



	public void setOther1(Integer other1) {
		this.other1 = other1;
	}



	public String getOther2() {
		return other2;
	}



	public void setOther2(String other2) {
		this.other2 = other2;
	}



	public Integer getReserved1() {
		return reserved1;
	}



	public void setReserved1(Integer reserved1) {
		this.reserved1 = reserved1;
	}



	public String getReserved2() {
		return reserved2;
	}



	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}



	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("groupId",groupId)
		.append("groupName",groupName)
		.append("price",price)
		.append("status",status)
		.append("excludeGender",excludeGender)
		.append("type",type)
		.append("targetType",targetType)
		.append("isAllRegion",isAllRegion)
		.append("isAllSite",isAllSite)
		.append("isItEnabled",isItEnabled)
		.append("other1",other1)
		.append("other2",other2)
		.append("reserved1",reserved1)
		.append("reserved2",reserved2)
        .toString();
	}

}
