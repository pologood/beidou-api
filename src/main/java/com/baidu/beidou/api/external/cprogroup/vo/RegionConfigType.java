package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: RegionConfigType
 * Function: 投放地域信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class RegionConfigType implements Serializable {
	private static final long serialVersionUID = 1L;
	private long groupId;
	private boolean allRegion;				// 是否全地域投放，true表示选择了全地域投放
	private RegionItemType[] regionList;	// allRegion为false时，需设置地域信息
	/**
	 * @return the groupId
	 */
	public long getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the allRegion
	 */
	public boolean isAllRegion() {
		return allRegion;
	}
	/**
	 * @param allRegion the allRegion to set
	 */
	public void setAllRegion(boolean allRegion) {
		this.allRegion = allRegion;
	}
	/**
	 * regionList
	 *
	 * @return  the regionList
	 */
	
	public RegionItemType[] getRegionList() {
		return regionList;
	}
	/**
	 * regionList
	 *
	 * @param   regionList    the regionList to set
	 */
	
	public void setRegionList(RegionItemType[] regionList) {
		this.regionList = regionList;
	}

}
