package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.List;


public class GroupAttachInfoType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer groupId;
	List<AttachInfoType> attachTypes;
	
	

	public GroupAttachInfoType() {
	}

	public GroupAttachInfoType(Integer groupId, List<AttachInfoType> attachTypes) {
		this.groupId = groupId;
		this.attachTypes = attachTypes;
	}

	public List<AttachInfoType> getAttachTypes() {
		return attachTypes;
	}

	public void setAttachTypes(List<AttachInfoType> attachTypes) {
		this.attachTypes = attachTypes;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
}
