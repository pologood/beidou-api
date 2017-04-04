package com.baidu.beidou.api.external.cprounit.service;

import java.util.List;

import com.baidu.beidou.api.external.cprounit.vo.AdType;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprounit.vo.CprounitWirelessUrlBoolean;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.cprounit.vo.UnitInfoView2;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.User;

/**
 * ClassName: UnitService
 * Function: unit层级新增及验证内部service
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-15
 */
public interface UnitService {

	public boolean addUnit(int index, AdType adType,
			ApiResult<AdType> result, User bdUser, List<OptContent> optContents);
	
	public boolean addIcon(int index, UnitInfoView2 unit,
			ApiResult<AdType> result, User bdUser);

	public boolean validateUnit(int index, UnitInfoView unit, int groupType,
			ApiResult<AdType> result, User bdUser);
	
	public boolean validateReplaceUnit(int index, UnitInfoView unit,
			Integer srcGroupType, ApiResult<Object> result, User bdUser, boolean needVerifyUrlBinding);
	
	public boolean copyUnit(int index, List<UnitInfoView> unitList,	CproGroup cproGroup, 
			ApiResult<Object> result, User bdUser, Integer opUser, List<OptContent> optContents);
	
	public boolean validateCopyUnit(List<UnitInfoView> unitList, List<CproGroup> groupList,
			ApiResult<Object> result, User user, boolean needVerifyUrlBinding);
	
}
