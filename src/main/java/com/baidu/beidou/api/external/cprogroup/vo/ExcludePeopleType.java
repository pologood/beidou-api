package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: ExcludePeopleType  <br>
 * Function: 推广组排除人群类
 *
 * @author zhangxu
 * @date Aug 31, 2012
 */
public class ExcludePeopleType implements Serializable {
	
	private static final long serialVersionUID = 12587628412354L;

	private long groupId;
	
	private List<Long> excludePeopleIds = new ArrayList<Long>();

	public long getGroupId() {
		return groupId;
	}


	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public List<Long> getExcludePeopleIds() {
		return excludePeopleIds;
	}

	public void setExcludePeopleIds(List<Long> excludePeopleIds) {
		this.excludePeopleIds = excludePeopleIds;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("groupId",groupId)
		.append("excludePeopleIds",excludePeopleIds)
        .toString();
	}
	
}
