package com.baidu.beidou.api.external.cprogroup.vo.locale;

/**
 * ClassName: GroupRegionItem
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-4-12
 */
public class GroupRegionItem {
	
	private int index;	// 参数中所在索引
	
	private int type;	// 地域类型，取值范围：1:自然地域，2:中国电信，3:中国联通，4:校园网，5:网吧
	
	private int regionId;	// 地域id，请参考地域列表
	
	public GroupRegionItem(int index, int type, int regionId) {
		this.index = index;
		this.type = type;
		this.regionId = regionId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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
