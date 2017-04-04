package com.baidu.beidou.api.internal.business.exporter;

import java.util.List;
import java.util.Map;

import com.baidu.beidou.api.internal.business.vo.GroupResult;
import com.baidu.beidou.api.internal.business.vo.KeywordResult;
import com.baidu.beidou.api.internal.business.vo.KtEnabledMultiResult;
import com.baidu.beidou.api.internal.business.vo.PlanResult;
import com.baidu.beidou.api.internal.business.vo.KtEnabledResult;
import com.baidu.beidou.api.internal.business.vo.SiteResult;
import com.baidu.beidou.api.internal.business.vo.UnitResult;
import com.baidu.beidou.api.internal.business.vo.UnitResultOne;

public interface NameService {
	
	public PlanResult getPlanNamebyId(Integer userId, List<Integer> planIds);
	
	public GroupResult getGroupNamebyId(Integer userId, List<Integer> groupIds);
	
	public UnitResult getUnitMaterialbyId(Integer userId, List<Long> unitIds);
	
	public SiteResult getSiteNamebyKey(List<String> keys);
	
	public KtEnabledResult isKtEnabledByUserid(Integer userId);
	
	public KtEnabledMultiResult isKtEnabledByUserids(List<Integer> userIdList);
	
	public KtEnabledResult isKtEnabledByUseridAndPlanid(Integer userId, Integer planId);
	
	public KtEnabledResult isKtEnabledByUseridAndGroupid(Integer userId, Integer groupId);
	
	public PlanResult getEffectivePlansByUserid(Integer userId);
	
	public GroupResult getEffectiveGroupsByUseridAndPlanid(Integer userId, Integer planId);

	public KeywordResult getKeywordLiteral(List<Map<String, Long>> keywordIdAndAtomIdList);

	public KeywordResult getKeywordLiteral2(Integer userId, Integer groupId, List<Long> wordIdList);
	
	//added by lvzichan
	public UnitResultOne getOneUnitMaterialbyId(Integer userId, Long unitId);

}
