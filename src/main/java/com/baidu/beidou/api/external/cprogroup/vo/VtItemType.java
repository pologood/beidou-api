package com.baidu.beidou.api.external.cprogroup.vo;

/**
 * ClassName: VtItemType
 * Function: RT回头客——到访定向信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class VtItemType {
	Long[] relatedPeopleIds;	// 关联人群id列表	不为null，至少有一个id
	Long[] unRelatePeopleIds;	// 排查人群id列表	可为null
	
	public Long[] getRelatedPeopleIds() {
		return relatedPeopleIds;
	}
	public void setRelatedPeopleIds(Long[] relatedPeopleIds) {
		this.relatedPeopleIds = relatedPeopleIds;
	}
	public Long[] getUnRelatePeopleIds() {
		return unRelatePeopleIds;
	}
	public void setUnRelatePeopleIds(Long[] unRelatePeopleIds) {
		this.unRelatePeopleIds = unRelatePeopleIds;
	}
}
