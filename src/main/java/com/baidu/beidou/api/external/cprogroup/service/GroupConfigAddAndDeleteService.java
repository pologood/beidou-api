package com.baidu.beidou.api.external.cprogroup.service;

import java.util.List;

import com.baidu.beidou.api.external.cprogroup.vo.GroupAttachInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeAppType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeIpType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeKeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeSiteType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupInterestInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupKeywordItemType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupPackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupRegionType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSiteType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupTradePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupTradeType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupVtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.RegionConfigType;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupRegionItem;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.google.common.collect.HashMultimap;

/**
 * ClassName: GroupConfigAddAndDeleteService
 * Function: 推广组设置内部service增删接口
 *
 * @author Baidu API Team
 * @date 2012-3-27
 */
public interface GroupConfigAddAndDeleteService {
	
	public ApiResult<Object> addKeyword(ApiResult<Object> result,
			GroupKeywordItemType[] keywords);
	
	public ApiResult<Object> addVtPeople(ApiResult<Object> result,
			GroupVtItemType[] vtPeoples);
	
	public ApiResult<Object> addSite(ApiResult<Object> result,
			GroupSiteType[] sites);
	
	public ApiResult<Object> addTrade(ApiResult<Object> result,
			GroupTradeType[] trades);
	
//	public ApiResult<Object> addRegion(ApiResult<Object> result,
//			GroupRegionType[] regions);
	
	public ApiResult<Object> addExcludeIp(ApiResult<Object> result,
			GroupExcludeIpType[] excludeIps);
	
	public ApiResult<Object> addExcludeSite(ApiResult<Object> result,
			GroupExcludeSiteType[] excludeSites);
	
	public ApiResult<Object> addExcludeApp(ApiResult<Object> result,
			List<GroupExcludeAppType> excludeApps, int userId, int opUser);

	public ApiResult<Object> addSitePrice(ApiResult<Object> result,
			GroupSitePriceType[] sitePrices);

	public ApiResult<Object> addInterestInfo(ApiResult<Object> result,
			GroupInterestInfoType[] interestInfos);

	public ApiResult<Object> deleteKeyword(ApiResult<Object> result,
			GroupKeywordItemType[] keywords);
	
	public ApiResult<Object> deleteVtPeople(ApiResult<Object> result,
			GroupVtItemType[] vtPeoples);
	
	public ApiResult<Object> deleteSite(ApiResult<Object> result,
			GroupSiteType[] sites);
	
	public ApiResult<Object> deleteExcludeApp(ApiResult<Object> result,
			List<GroupExcludeAppType> excludeApps, int userId, int opUser);
	
	public ApiResult<Object> deleteTrade(ApiResult<Object> result,
			GroupTradeType[] trades);
		
	public ApiResult<Object> deleteExcludeIp(ApiResult<Object> result,
			GroupExcludeIpType[] excludeIps);
	
	public ApiResult<Object> deleteExcludeSite(ApiResult<Object> result,
			GroupExcludeSiteType[] excludeSites);
	
	public ApiResult<Object> deleteSitePrice(ApiResult<Object> result,
			GroupSitePriceType[] sitePrices);
	
	public ApiResult<Object> deleteInterestInfo(ApiResult<Object> result,
			GroupInterestInfoType[] interestInfos);
	
	public ApiResult<Object> addTradePrice(ApiResult<Object> result,
			GroupTradePriceType[] tradePrices);
	
	public ApiResult<Object> deleteTradePrice(ApiResult<Object> result,
			GroupTradeType[] tradePrices);
	
	public void addExcludeKeyword(BaseResponse<PlaceHolderResult> response,
			List<GroupExcludeKeywordType> groupExcludeKeywordTypes,
			int userId,
			int opUser);
	
	public void deleteExcludeKeyword(BaseResponse<PlaceHolderResult> response,
			List<GroupExcludeKeywordType> groupExcludeKeywordTypes,
			int userId,
			int opUser);
	
	public void addExcludePeople(BaseResponse<PlaceHolderResult> response,
			List<GroupExcludePeopleType> excludePids,
			int userId,
			int opUser);
	
	public void deleteExcludePeople(BaseResponse<PlaceHolderResult> response,
			List<GroupExcludePeopleType> excludePids,
			int userId,
			int opUser);
	
	public void addPackInfo(BaseResponse<PlaceHolderResult> response,
			List<GroupPackItemType> packs,
			int userId,
			int opUser);
	
	public void deletePackInfo(BaseResponse<PlaceHolderResult> response,
			List<GroupPackItemType> packs,
			int userId,
			int opUser);
	
	public void updateAttachInfo(BaseResponse<PlaceHolderResult> response,List<GroupAttachInfoType> attachInfos,Visitor visitor,int userId);
	
    /**
     * 增加推广组投放地域
     * 
     * @param result 设置结果
     * @param regionMap 地域集合
     * @return 设置结果
     */
    public ApiResult<Object> addRegion(ApiResult<Object> result, HashMultimap<Long, GroupRegionItem> regionMap);
    
    public ApiResult<Object> deleteRegion(ApiResult<Object> result, HashMultimap<Long, GroupRegionItem> regionMap);
    
    /**
     * 设置推广组地域
     * 
     * @param result 参数校验结果
     * @param regionConfig 地域设置参数
     * @return 是否成功
     */
    public boolean setRegion(ApiResult<Object> result, RegionConfigType regionConfig);
	
}
