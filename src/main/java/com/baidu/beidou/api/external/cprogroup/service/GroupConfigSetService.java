package com.baidu.beidou.api.external.cprogroup.service;

import java.util.List;
import java.util.Set;

import com.baidu.beidou.api.external.cprogroup.vo.GroupKeywordItemType;
import com.baidu.beidou.api.external.cprogroup.vo.InterestInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.KtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.PackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.api.external.cprogroup.vo.VtItemType;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.GroupSitePrice;
import com.baidu.beidou.cprogroup.bo.GroupTradePrice;
import com.baidu.beidou.cprogroup.facade.CproITFacade;
import com.baidu.beidou.cprogroup.facade.GroupPackFacade;
import com.baidu.beidou.cprogroup.facade.WordExcludeFacade;
import com.baidu.beidou.cprogroup.service.CproGroupITMgr;
import com.baidu.beidou.cprogroup.service.CproGroupVTMgr;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.cprogroup.service.CustomITMgr;
import com.baidu.beidou.cprogroup.service.GroupSiteConfigMgr;
import com.baidu.beidou.cprogroup.service.InterestMgr;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * ClassName: GroupConfigSetService
 * Function: 推广组设置内部service接口
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
public interface GroupConfigSetService {

	public boolean saveSitePrice(Integer groupId, boolean isModified, 
			List<GroupSitePrice> sitePrice, GroupSiteConfigMgr groupSiteConfigMgr);

	public boolean saveTradePrice(Integer groupId, boolean isModified, 
			List<GroupTradePrice> sitePrice, GroupSiteConfigMgr groupSiteConfigMgr);

	public ApiResult<Object> saveSiteTargetUrl(ApiResult<Object> result,
			Integer groupId, List<GroupSitePrice> sitePrice,
			GroupSiteConfigMgr groupSiteConfigMgr);

	public ApiResult<Object> saveIpFilter(ApiResult<Object> result,
			Integer groupId, Integer userid,  Set<String> allIp);

	public ApiResult<Object> saveSiteFilter(ApiResult<Object> result,
			Integer groupId, Set<String> allSite);
	
	public ApiResult<Object> setKeyword(ApiResult<Object> result,
			GroupKeywordItemType[] keywords);
	
	public boolean saveKT(ApiResult<Object> result, 
			CproGroup group,
			KtItemType ktItem, 
			CproKeywordMgr cproKeywordMgr, 
			List<OptContent> optContents);
	
	public boolean saveVT(ApiResult<Object> result, 
			CproGroup group,
			VtItemType vtItem, 
			VtPeopleMgr vtPeopleMgr, 
			CproGroupVTMgr cproGroupVTMgr, 
			List<OptContent> optContents);
	
	public boolean saveNone(ApiResult<Object> result, 
			CproGroup group, 
			List<OptContent> optContents);
	
	public boolean saveIT(ApiResult<Object> result, 
			CproGroup group,
			InterestInfoType interestInfo,
			CproITFacade cproITFacade,
			CproGroupITMgr cproGroupItMgr,
			InterestMgr interestMgr,
			CustomITMgr customITMgr,
			int userId,
			int opUserId,
			List<OptContent> optContents);
	
	public void saveExcludeKeyword(BaseResponse<PlaceHolderResult> response, 
			Integer groupId,
			List<KeywordType> excludeKeywords,
			List<Integer> excludeKeywordPackIds,
			WordExcludeFacade wordExcludeFacade,
			int userId,
			int opUser);
	
	public void saveExcludePeople(BaseResponse<PlaceHolderResult> response, 
			Integer groupId,
			List<Long> excludePids,
			VtPeopleMgr vtPeopleMgr, 
			CproGroupVTMgr cproGroupVTMgr, 
			int userId,
			int opUser);
	
	public void savePackInfo(BaseResponse<PlaceHolderResult>response, 
			Integer groupId,
			List<PackItemType> packs,
			GroupPackFacade groupPackFacade,
			int userId,
			int opUser);
	
	public void savePrice(BaseResponse<PlaceHolderResult>response, 
			List<PriceType> prices,
			int userId,
			int opUser);
	
}
