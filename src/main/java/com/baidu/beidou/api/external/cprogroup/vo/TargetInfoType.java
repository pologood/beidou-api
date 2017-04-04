package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: TargetInfoType
 * Function: 推广组定向信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class TargetInfoType implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
		1表示回头客定向，
		2表示关键词定向，
		3表示不启用受众行为，
		4表示到访定向
		5标识高级组合投放 
		6表示atright投放
	 */
	private int type;

	// 推广组ID
	private long groupId;

	// RT关联关系及有效期
	private RtItemType rtItem;
	
	// 关键词
	private KtItemType ktItem;

	// VT关联人群和排除人群
	private VtItemType vtItem;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public KtItemType getKtItem() {
		return ktItem;
	}

	public void setKtItem(KtItemType ktItem) {
		this.ktItem = ktItem;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public VtItemType getVtItem() {
		return vtItem;
	}

	public void setVtItem(VtItemType vtItem) {
		this.vtItem = vtItem;
	}

	public RtItemType getRtItem() {
		return rtItem;
	}

	public void setRtItem(RtItemType rtItem) {
		this.rtItem = rtItem;
	}
	
	

}
