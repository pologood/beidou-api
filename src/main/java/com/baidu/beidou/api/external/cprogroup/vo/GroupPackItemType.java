package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class GroupPackItemType implements Serializable {
	
	private static final long serialVersionUID = 134975394563211L;

	private long groupId;
	
	private int type;
	
	private int packId;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPackId() {
		return packId;
	}

	public void setPackId(int packId) {
		this.packId = packId;
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("groupId",groupId)
		.append("type",type)
		.append("packId",packId)
        .toString();
	}


}
