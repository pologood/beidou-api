package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: PackInfoType  <br>
 * Function: 推广组受众组合配置类
 *
 * @author zhangxu
 * @date Aug 31, 2012
 */
public class PackInfoType implements Serializable {
	
	private static final long serialVersionUID = 134975394721L;

	private long groupId; 
	
	private List<PackItemType> packItems = new ArrayList<PackItemType>();

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public List<PackItemType> getPackItems() {
		return packItems;
	}

	public void setPackItems(List<PackItemType> packItems) {
		this.packItems = packItems;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("groupId",groupId)
		.append("packItems",packItems)
        .toString();
	}

}
