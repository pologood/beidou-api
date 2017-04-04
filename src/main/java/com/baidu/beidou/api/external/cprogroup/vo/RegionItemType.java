package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: RegionItem
 * Function: 地域信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class RegionItemType implements Serializable {

	private static final long serialVersionUID = 6588663292158299694L;
	private int type;		// type：投放类型，遗留类型，之前分地域、电信、网通、网吧等
	private int regionId;	// 投放地域ID，目前修改为北斗地域ID，@cpweb443(beidou 3.0)

	public RegionItemType() {
		super();
	}

	public RegionItemType(int type, int regionId) {
		this.type = type;
		this.regionId = regionId;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + regionId;
		result = prime * result + type;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RegionItemType other = (RegionItemType) obj;
		if (regionId != other.regionId) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

}
