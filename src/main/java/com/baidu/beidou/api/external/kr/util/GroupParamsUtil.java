package com.baidu.beidou.api.external.kr.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.vo.CproGroupVTVo;
import com.baidu.beidou.api.external.kr.vo.RegionSelectionVo;
import com.baidu.beidou.api.external.kr.vo.TradeSelectionVo;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.ValidationAware;

/**
 * 用于进行参数收集
 * @author hejinggen
 * 
 */
public class GroupParamsUtil {

	private final static String RT_PLAN_NAME_FLAG = "fcPlanName";
	private final static String RT_UNIT_ID_FLAG = "fcUnitId";
	private final static String RT_UNIT_NAME_FLAG = "fcUnitName";

	private final static String SITEID_GROUP_ID_FlAG = "siteId";
	private final static String TRADE_GROUP_ID_FLAG = "tradeId";
	private final static String SITEURL_GROUP_ID_FLAG = "siteUrl";

	public static Map<Integer, List<Integer>> collectParamsForTRADE(List<Integer> groupIds,
			HttpServletRequest request) {
		if (CollectionUtils.isEmpty(groupIds)) {
			return new HashMap<Integer, List<Integer>>(0);
		}
		Map<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>(groupIds.size());
		for (Integer groupId : groupIds) {
			if (groupId == null || groupId == 0) {
				continue;
			}
			String[] tradeIds = request.getParameterValues(TRADE_GROUP_ID_FLAG + groupId);
			if (!ArrayUtils.isEmpty(tradeIds)) {
				List<Integer> trades = new ArrayList<Integer>(tradeIds.length);
				for (String trade : tradeIds) {
					trades.add(Integer.valueOf(trade));
				}
				result.put(groupId, trades);
			}
		}
		return result;
	}

	public static Map<Integer, List<Integer>> collectParamsForSiteId(List<Integer> groupIds,
			HttpServletRequest request) {
		if (CollectionUtils.isEmpty(groupIds)) {
			return new HashMap<Integer, List<Integer>>(0);
		}
		Map<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>(groupIds.size());
		for (Integer groupId : groupIds) {
			if (groupId == null || groupId == 0) {
				continue;
			}
			String[] siteIds = request.getParameterValues(SITEID_GROUP_ID_FlAG + groupId);
			if (!ArrayUtils.isEmpty(siteIds)) {
				List<Integer> sites = new ArrayList<Integer>(siteIds.length);
				for (String site : siteIds) {
					sites.add(Integer.valueOf(site));
				}
				result.put(groupId, sites);
			}
		}
		return result;
	}

	public static Map<Integer, List<String>> collectParamsForSiteUrl(List<Integer> groupIds,
			HttpServletRequest request) {
		if (CollectionUtils.isEmpty(groupIds)) {
			return new HashMap<Integer, List<String>>(0);
		}
		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>(groupIds.size());
		for (Integer groupId : groupIds) {
			if (groupId == null || groupId == 0) {
				continue;
			}
			String[] siteUrls = request.getParameterValues(SITEURL_GROUP_ID_FLAG + groupId);
			if (!ArrayUtils.isEmpty(siteUrls)) {
				List<String> sites = new ArrayList<String>(siteUrls.length);
				for (String site : siteUrls) {
					sites.add(site);
				}
				result.put(groupId, sites);
			}
		}
		return result;
	}


	/**
	 * add by kanghongwei since cpweb304
	 * 
	 * @param request
	 * @param validationAware
	 * @param textProvider
	 * @return
	 */
	public static List<CproGroupVTVo> collectParamsForVT(HttpServletRequest request,
			ValidationAware validationAware, TextProvider textProvider) {
		if (request == null || validationAware == null || textProvider == null) {
			validationAware.addActionError(textProvider.getText("没有权限"));
			return null;
		}
		String[] includeVTIds = request.getParameterValues("includePIds");
		String[] excludeVTIds = request.getParameterValues("excludePIds");
		// 验证“关联”必须性
		if (ArrayUtils.isEmpty(includeVTIds)) {
			validationAware.addActionError(textProvider
					.getText("errors.group.vt.includePIds.notvalid"));
			return null;
		}
		if (ArrayUtils.isEmpty(excludeVTIds)) {
			excludeVTIds = ArrayUtils.EMPTY_STRING_ARRAY;
		}
		// 验证总数
		if ((includeVTIds.length + excludeVTIds.length) > 100) {
			validationAware.addActionError(textProvider
					.getText("errors.group.vt.excludePIds.notvalid"));
			return null;
		}
		// 验证重复性
		List<CproGroupVTVo> pidList = new ArrayList<CproGroupVTVo>();
		Map<Long, CproGroupVTVo> includePidMap = makeVTMap(includeVTIds, true);
		Map<Long, CproGroupVTVo> excludePidMap = makeVTMap(excludeVTIds, false);
		for (Long includePid : includePidMap.keySet()) {
			if (excludePidMap.containsKey(includePid)) {
				validationAware.addActionError(textProvider.getText("推广组关联和排除了同一个人群！"));
				return null;
			}
			pidList.add(includePidMap.get(includePid));
		}
		for (Long excludePid : excludePidMap.keySet()) {
			pidList.add(excludePidMap.get(excludePid));
		}
		return pidList;
	}

	/**
	 * add by kanghongwei since cpweb304
	 * 
	 * @param pidArray
	 * @param isInclude
	 * @return
	 */
	private static Map<Long, CproGroupVTVo> makeVTMap(String[] pidArray, boolean isInclude) {
		if (ArrayUtils.isEmpty(pidArray)) {
			return Collections.emptyMap();
		}
		Map<Long, CproGroupVTVo> vtVoMap = new HashMap<Long, CproGroupVTVo>();
		for (String pidStr : pidArray) {
			Long pid = Long.valueOf(pidStr);
			CproGroupVTVo vtVo = new CproGroupVTVo(pid,
					(isInclude ? CproGroupConstant.GROUP_VT_INCLUDE_CROWD
							: CproGroupConstant.GROUP_VT_EXCLUDE_CROWD));
			vtVoMap.put(pid, vtVo);
		}
		return vtVoMap;
	}

	public final static String SECOND_TRADE_PARAM_PREFIX = "siteSortItem"; // 二级行业request
																			// parameter前缀
	public final static String SECOND_REGION_PARAM_PREFIX = "regionSortItem"; // 二级地域request
																				// parameter前缀

	// 收集行业选择参数，将前端传入的siteSortItemN=xxx&siteSortItemN=xxx&siteSortItemM=xxx&siteSortItemM=xxx收集成Map
	public static TradeSelectionVo collectParamsForTrade(List<Integer> selectedFirstTrades,
			HttpServletRequest request) {

		TradeSelectionVo result = new TradeSelectionVo();
		result.setFirstTrades(new ArrayList<Integer>());
		result.setSecondTradeMap(new HashMap<Integer, List<Integer>>());

		if (CollectionUtils.isNotEmpty(selectedFirstTrades)) {

			for (Integer firstTradeId : selectedFirstTrades) {
				result.getFirstTrades().add(firstTradeId);

				// 获得二级地域ID
				String[] secondTradeIds = request.getParameterValues(SECOND_TRADE_PARAM_PREFIX
						+ firstTradeId);

				if (!ArrayUtils.isEmpty(secondTradeIds)) {

					List<Integer> secondTradeList = new ArrayList<Integer>();

					for (String secondTradeId : secondTradeIds) {
						if (StringUtils.isNotEmpty(secondTradeId)) {
							secondTradeList.add(Integer.parseInt(secondTradeId));
						}
					}

					if (CollectionUtils.isNotEmpty(secondTradeList)) {
						result.getSecondTradeMap().put(firstTradeId, secondTradeList);
					}
				}
			}
		}

		return result;
	}

	// 收集地域选择参数，将前端传入的regionSortItemN=xxx&regionSortItemN=xxx&regionSortItemM=xxx&regionSortItemM=xxx收集成Map
	public static RegionSelectionVo collectParamsForRegion(List<Integer> selectedFirstRegions,
			HttpServletRequest request) {

		RegionSelectionVo result = new RegionSelectionVo();
		result.setFirstRegions(new ArrayList<Integer>());
		result.setSecondRegionMap(new HashMap<Integer, List<Integer>>());

		if (CollectionUtils.isNotEmpty(selectedFirstRegions)) {

			for (Integer firstRegionId : selectedFirstRegions) {
				result.getFirstRegions().add(firstRegionId);

				// 获得二级地域ID
				String[] secondRegionIds = request.getParameterValues(SECOND_REGION_PARAM_PREFIX
						+ firstRegionId);

				if (!ArrayUtils.isEmpty(secondRegionIds)) {

					List<Integer> secondRegionList = new ArrayList<Integer>();

					for (String secondRegionId : secondRegionIds) {
						if (StringUtils.isNotEmpty(secondRegionId)) {
							secondRegionList.add(Integer.parseInt(secondRegionId));
						}
					}

					if (CollectionUtils.isNotEmpty(secondRegionList)) {
						result.getSecondRegionMap().put(firstRegionId, secondRegionList);
					}
				}
			}
		}
		filterInvalidRegionId(result);
		return result;
	}

	/**
	 * 过滤掉无效，重复的地域信息 add by kanghongwei since cpweb429(qtIM)
	 * 
	 * @param regionSelectionVo
	 */
	private static void filterInvalidRegionId(RegionSelectionVo regionSelectionVo) {
		if (regionSelectionVo == null
				|| CollectionUtils.isEmpty(regionSelectionVo.getFirstRegions())) {
			return;
		}
		Map<Integer, List<Integer>> allFirstSecondRegIdMap = UnionSiteCache.regCache
				.getFirstLevelRegChildren();
		Set<Integer> allFirstRegIdSet = UnionSiteCache.regCache.getFirstRegChildrenSum().keySet();

		List<Integer> curFirstRegionList = regionSelectionVo.getFirstRegions();
		Map<Integer, List<Integer>> curSecondRegionMap = regionSelectionVo.getSecondRegionMap();

		Set<Integer> delDupFirstRegionSet = new HashSet<Integer>(curFirstRegionList);
		Map<Integer, List<Integer>> delDupRegionMap = new HashMap<Integer, List<Integer>>(
				curSecondRegionMap.size());

		// 清理无效，重复一级行业
		for (Integer curFirstRegionId : curFirstRegionList) {
			if (!allFirstRegIdSet.contains(curFirstRegionId)) {
				delDupFirstRegionSet.remove(curFirstRegionId);
				if (curSecondRegionMap.containsKey(curFirstRegionId)) {
					curSecondRegionMap.remove(curFirstRegionId);
				}
			}
		}
		// 清理无效，重读二级行业
		for (Integer curFirstRegionId : curSecondRegionMap.keySet()) {
			List<Integer> curSecondRegIdList = curSecondRegionMap.get(curFirstRegionId);
			List<Integer> allSecondRegIdList = allFirstSecondRegIdMap.get(curFirstRegionId);
			Set<Integer> delDupSecondRegionSet = new HashSet<Integer>(curSecondRegIdList);

			for (Integer curSecondRegId : curSecondRegIdList) {
				if (!allSecondRegIdList.contains(curSecondRegId)) {
					delDupSecondRegionSet.remove(curSecondRegId);
				}
			}
			delDupRegionMap.put(curFirstRegionId, new ArrayList<Integer>(delDupSecondRegionSet));
		}
		regionSelectionVo.setFirstRegions(new ArrayList<Integer>(delDupFirstRegionSet));
		regionSelectionVo.setSecondRegionMap(delDupRegionMap);
	}

	/**
	 * 从request中获取参数集合
	 * @param request
	 * @return
	 */
	public static Map<Integer, List<Integer>> collectParamsForInterest(
			HttpServletRequest request) {
		
		//验证
		@SuppressWarnings("unchecked")
		Map<String, Object> params = request.getParameterMap();
		if(params.size() == 0){
			return Collections.emptyMap();
		}
		
		//构造循环中需要使用的变量
		int start = InterestParamMapPrefix.length();
		Map<Integer, List<Integer>> groupInterestMap = new HashMap<Integer, List<Integer>>();
		
		for(Entry<String, Object> entry : params.entrySet()){
			String key = entry.getKey();
			if(!key.startsWith(InterestParamMapPrefix)){
				continue;
			}
			
			//获取groupId与对应的interestId
			String sGroupId = key.substring(start);
			String[] interests = request.getParameterValues(key);

			//如果没有参数
			if(interests == null || interests.length == 0){
				continue;
			}
			
			//获取整形的ID
			Integer groupId = Integer.valueOf(sGroupId);
			List<Integer> interestIds = load2IntegerList(interests);
			
			groupInterestMap.put(groupId, interestIds);
		}
		
		if(groupInterestMap.size() == 0)
			return null;
		return groupInterestMap;
	}
	
	private static List<Integer> load2IntegerList(String[] interests) {
		List<Integer> list = new LinkedList<Integer>();
		for(String s : interests){
			list.add(Integer.valueOf(s));
		}
		return list;
	}

	public final static String InterestParamMapPrefix = "interestId";
}
