package com.baidu.beidou.api.external.cprogroup.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cprogroup.vo.AdditionalGroupType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeKeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupPackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.PackInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.PackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.GroupInterestPrice;
import com.baidu.beidou.cprogroup.bo.GroupPack;
import com.baidu.beidou.cprogroup.bo.WordExclude;
import com.baidu.beidou.cprogroup.bo.WordPackExclude;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.InterestConstant;
import com.baidu.beidou.cprogroup.util.TargettypeUtil;
import com.baidu.beidou.cprogroup.vo.GroupPackVo;
import com.baidu.beidou.cprogroup.vo.VtPeopleExcludeMapperVo;

/**
 * 
 * ClassName: GroupBoMappingUtil  <br>
 * Function: Business Object 映射工具类，专用于API与beidou-core内的BO映射做转换
 *
 * @author zhangxu
 * @date Aug 28, 2012
 */
public class GroupBoMappingUtil {

	/**
	 * 排除关键词与关键词组合BO转换
	 * @param wordExcludes 排除关键词
	 * @param wordPackExcludes 排除关键词组合
	 * @return ExcludeKeywordType[]
	 */
	public static ExcludeKeywordType[] transWordExclude2ExcludeKeywordType (List<WordExclude> wordExcludes, List<WordPackExclude> wordPackExcludes) {
		Map<Integer, ExcludeKeywordType> excludeKeywordMap = new HashMap<Integer, ExcludeKeywordType>();
		
		// 填充排除关键词
		for (WordExclude wordExclude : wordExcludes) {
			Integer groupId = wordExclude.getGroupId();
			String keyword = wordExclude.getKeyword();
			if (!excludeKeywordMap.containsKey(groupId)) {
				ExcludeKeywordType excludeKeywordType = new ExcludeKeywordType();
				excludeKeywordType.setGroupId(groupId);
				excludeKeywordMap.put(groupId, excludeKeywordType);
			}
			ExcludeKeywordType bo = excludeKeywordMap.get(groupId);
			KeywordType keywordType = new KeywordType();
			keywordType.setKeyword(keyword);
			List<KeywordType> excludeKeywords = bo.getExcludeKeywords();
			if (excludeKeywords == null) {
				excludeKeywords = new ArrayList<KeywordType>();
				bo.setExcludeKeywords(excludeKeywords);
			}
			bo.getExcludeKeywords().add(keywordType);
		}
		
		// 填充排除关键词组合
		for (WordPackExclude wordPackExclude : wordPackExcludes) {
			Integer groupId = wordPackExclude.getGroupId();
			Integer packId = wordPackExclude.getPackId();
			if (!excludeKeywordMap.containsKey(groupId)) {
				ExcludeKeywordType excludeKeywordType = new ExcludeKeywordType();
				excludeKeywordType.setGroupId(groupId);
				excludeKeywordMap.put(groupId, excludeKeywordType);
			}
			ExcludeKeywordType bo = excludeKeywordMap.get(groupId);
			List<Integer> excludeKeywordPackIds = bo.getExcludeKeywordPackIds();
			if (excludeKeywordPackIds == null) {
				excludeKeywordPackIds = new ArrayList<Integer>();
				bo.setExcludeKeywordPackIds(excludeKeywordPackIds);
			}
			bo.getExcludeKeywordPackIds().add(packId);
		}
		
		ExcludeKeywordType[] result = new ExcludeKeywordType[0];
		if (excludeKeywordMap.size() != 0) {
			result = excludeKeywordMap.values().toArray(new ExcludeKeywordType[0]);
		}
		
		return result;
	}
	
	/**
	 * 排除人群VO转换
	 * @param voList 排除人群VO
	 * @return ExcludePeopleType[]
	 */
	public static ExcludePeopleType[] transVtPeopleExcludeMapperVo2ExcludePeopleType (List<VtPeopleExcludeMapperVo> voList) {
		Map<Integer, ExcludePeopleType> excludePeopleMap = new HashMap<Integer, ExcludePeopleType>();
		
		// 填充排除关键词
		for (VtPeopleExcludeMapperVo vtPeopleExcludeMapperVo : voList) {
			Integer groupId = vtPeopleExcludeMapperVo.getGroupid();
			long pid = vtPeopleExcludeMapperVo.getPid();
			if (!excludePeopleMap.containsKey(groupId)) {
				ExcludePeopleType excludePeopleType = new ExcludePeopleType();
				excludePeopleType.setGroupId(groupId);
				excludePeopleType.getExcludePeopleIds().add(pid);
				excludePeopleMap.put(groupId, excludePeopleType);
				continue;
			}
			ExcludePeopleType excludePeopleType = excludePeopleMap.get(groupId);
			excludePeopleType.setGroupId(groupId);
			excludePeopleType.getExcludePeopleIds().add(pid);
		}
		
		ExcludePeopleType[] result = new ExcludePeopleType[0];
		if (excludePeopleMap.size() != 0) {
			result = excludePeopleMap.values().toArray(new ExcludePeopleType[0]);
		}
		
		return result;
	}
	
	/**
	 * 推广组受众组合BO转换
	 * @param boList 
	 * @return PackInfoType[] 
	 */
	public static PackInfoType[] transGroupPack2PackInfoType(List<GroupPack> boList) {
		Map<Integer, PackInfoType> groupPackMap = new HashMap<Integer, PackInfoType>();
		
		// 填充排除关键词
		for (GroupPack groupPack : boList) {
			Integer groupId = groupPack.getGroupId();
			PackInfoType packInfo = groupPackMap.get(groupId);
			if (packInfo == null) {
				PackInfoType pack = new PackInfoType();
				pack.setGroupId(groupId);
				PackItemType item = new PackItemType();
				item.setPackId(groupPack.getPackId());
				item.setType(groupPack.getPackType());
				pack.getPackItems().add(item);
				groupPackMap.put(groupId, pack);
				continue;
			}
			PackItemType item = new PackItemType();
			item.setPackId(groupPack.getPackId());
			item.setType(groupPack.getPackType());
			packInfo.getPackItems().add(item);
		}
		
		PackInfoType[] result = new PackInfoType[0];
		if (groupPackMap.size() != 0) {
			result = groupPackMap.values().toArray(new PackInfoType[0]);
		}
		
		return result;
	}
	
	/**
	 * API关键词BO转换成GroupPackVo
	 * @param packs 推广组关联受众组合
	 * @return List<GroupPackVo>
	 */
	public static List<GroupPackVo> transPackItemType2GroupPackVo(List<PackItemType> packs) {
		List<GroupPackVo> result = new ArrayList<GroupPackVo>();
		if (CollectionUtils.isEmpty(packs)) {
			return result;
		}
		for (PackItemType packItemType : packs) {
			GroupPackVo vo = new GroupPackVo();
			vo.setPackId(packItemType.getPackId());
			vo.setType(packItemType.getType());
			result.add(vo);
		}
		return result;
	}
	
	/**
	 * API推广组关联受众组合BO转换成GroupPackVo
	 * @param packList 
	 * @return List<GroupPackVo> 
	 */
	public static List<GroupPackVo> transGroupPackItemType2GroupPackVo(List<GroupPackItemType> packList) {
		List<GroupPackVo> result = new ArrayList<GroupPackVo>();
		if (CollectionUtils.isEmpty(packList)) {
			return result;
		}
		for (GroupPackItemType packItemType : packList) {
			GroupPackVo vo = new GroupPackVo();
			vo.setPackId(packItemType.getPackId());
			vo.setType(packItemType.getType());
			result.add(vo);
		}
		return result;
	}
	
	/**
	 * API排除人群到id列表
	 * @param excludePeoples 排除人群
	 * @return List<Long> 人群id列表
	 */
	public static List<Long> transGroupExcludePeopleType2Long(List<GroupExcludePeopleType> excludePeoples) {
		List<Long> result = new ArrayList<Long>();
		if (CollectionUtils.isEmpty(excludePeoples)) {
			return result;
		}
		for (GroupExcludePeopleType type : excludePeoples) {
			result.add(type.getExcludePeopleId());
		}
		return result;
	}
	
	
	/**
	 * API出价转换
	 * @param groupPackPriceList 推广组受众组合出价
	 * @param groupInterestPriceList 推广组兴趣出价
	 * @return List<Long> 人群id列表
	 */
	public static PriceType[] transPriceBo2PackInfoType(List<GroupPack> groupPackPriceList,
			List<GroupInterestPrice> groupInterestPriceList) {
		List<PriceType> result = new ArrayList<PriceType>();
		if (CollectionUtils.isEmpty(groupPackPriceList) &&
				CollectionUtils.isEmpty(groupInterestPriceList) ) {
			return new PriceType[0];
		}

		for (GroupPack groupPackPrice : groupPackPriceList) {
			if (!groupPackPrice.getPrice().equals(0)) {
				PriceType type = new PriceType(GroupConstant.GROUP_PACK_PRICE_TYPE, 
						groupPackPrice.getGroupId(), 
						groupPackPrice.getPackId(), 
						groupPackPrice.getPrice());
				type.setPackType(groupPackPrice.getPackType());
				result.add(type);
			}
		}
		
		for (GroupInterestPrice groupInterestPrice : groupInterestPriceList) {
			if (!groupInterestPrice.getPrice().equals(0)) {
				//判断是兴趣点还是兴趣组合
				boolean isCustomInterest = true;
				if(groupInterestPrice.getIid() <= InterestConstant.MAX_INTEREST_ID){
					isCustomInterest = false;
				}
				PriceType type = new PriceType(GroupConstant.GROUP_INTEREST_PRICE_TYPE, 
						groupInterestPrice.getGroupId(), 
						groupInterestPrice.getIid(), 
						isCustomInterest, 
						groupInterestPrice.getPrice());
				result.add(type);
			}
		}
		
		return result.toArray(new PriceType[0]);
	}
	
	/**
	 * API更新推广组状态以及出价信息类BO转换
	 * @param group 推广组
	 * @return AdditionalGroupType
	 */
	public static AdditionalGroupType transGroupBo2AdditionalGroupType(CproGroup group) {
		AdditionalGroupType result = new AdditionalGroupType();
		result.setGroupId(group.getGroupId());
		result.setGroupName(group.getGroupName());
		result.setPrice(group.getGroupInfo().getPrice());
		result.setStatus(group.getGroupState());
		result.setType(group.getGroupType());
		result.setAllRegion(group.getGroupInfo().getIsAllRegion() == CproGroupConstant.GROUP_ALLREGION ? true: false);
		result.setAllSite(group.getGroupInfo().getIsAllSite() == CproGroupConstant.GROUP_ALLSITE ? true: false);
		result.setTargetType(ApiTargetTypeUtil.toApiGroupTargetType(group.getTargetType()));
		result.setExcludeGender(group.getGroupInfo().getGenderInfo());
		result.setItEnabled(TargettypeUtil.hasIT(group.getTargetType()));
		return result;
	}
	
}
