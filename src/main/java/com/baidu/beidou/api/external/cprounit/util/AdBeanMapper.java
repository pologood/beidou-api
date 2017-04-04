package com.baidu.beidou.api.external.cprounit.util;

import com.baidu.beidou.api.external.cprounit.vo.AdType;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.cprounit.vo.UnitInfoView2;

public class AdBeanMapper {

	public static AdType transformUnitInfoView2AdType(UnitInfoView unitInfoView){
		AdType result = new AdType();
		result.setGroupId(unitInfoView.getGroupid());
		result.setAdId(unitInfoView.getUnitid());
		result.setType(unitInfoView.getWuliaoType());
		result.setStatus(unitInfoView.getStateView().getViewState());
		result.setTitle(unitInfoView.getTitle());
		result.setDisplayUrl(unitInfoView.getShowUrl());
		result.setDestinationUrl(unitInfoView.getTargetUrl());
		result.setImageUrl(unitInfoView.getMaterUrl());
		result.setDescription1(unitInfoView.getDescription1());
		result.setDescription2(unitInfoView.getDescription2());
		result.setWidth(unitInfoView.getWidth());
		result.setHeight(unitInfoView.getWidth());
		return result;
	}
	
	public static AdType transformUnitInfoView22AdType(UnitInfoView2 unitInfoView2){
		AdType result = new AdType();
		result.setGroupId(unitInfoView2.getGroupid());
		result.setType(unitInfoView2.getWuliaoType());
		result.setStatus(unitInfoView2.getStateView().getViewState());
		result.setTitle(unitInfoView2.getTitle());
		result.setDisplayUrl(unitInfoView2.getShowUrl());
		result.setDestinationUrl(unitInfoView2.getTargetUrl());
		result.setDescription1(unitInfoView2.getDescription1());
		result.setDescription2(unitInfoView2.getDescription2());
		result.setWidth(unitInfoView2.getWidth());
		result.setHeight(unitInfoView2.getWidth());
		return result;
	}
	
}
