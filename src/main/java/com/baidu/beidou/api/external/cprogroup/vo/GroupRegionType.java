package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: GroupRegionType
 * Function: 投放地域
 *
 * @author Baidu API Team
 * @date 2012-3-31
 */
public class GroupRegionType implements Serializable {
	
	private static final long serialVersionUID = -5154059810066747531L;

	private long groupId;	// 推广组id
	
	private int type;	// 地域类型，取值范围：1:自然地域，2:中国电信，3:中国联通，4:校园网，5:网吧
	
	private int regionId;	// 地域id，请参考地域列表，目前修改为北斗地域ID，@cpweb443(beidou 3.0)

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

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
}
