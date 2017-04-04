package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: RtItemType
 * Function: RT回头客——点击定向信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class RtItemType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// RT词的有效期。请参阅CproGroupConstant.GROUP_RT_ALIVEDAYS
	private int aliveDays;
	
	// RT关联的FC计划和单元
	private List<RtRelationType> rtRelationList;
	
	public int getAliveDays() {
		return aliveDays;
	}
	public void setAliveDays(int aliveDays) {
		this.aliveDays = aliveDays;
	}
	public List<RtRelationType> getRtRelationList() {
		return rtRelationList;
	}
	public void setRtRelationList(List<RtRelationType> rtRelationList) {
		this.rtRelationList = rtRelationList;
	}
	
}
