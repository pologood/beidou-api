package com.baidu.beidou.api.external.cprogroup.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.error.GroupErrorCode;
import com.baidu.beidou.api.external.cprogroup.facade.GroupExcludePeopleFacade;
import com.baidu.beidou.api.external.cprogroup.facade.GroupPriceFacade;
import com.baidu.beidou.api.external.cprogroup.service.GroupConfigBaseService;
import com.baidu.beidou.api.external.cprogroup.service.GroupConfigSetService;
import com.baidu.beidou.api.external.cprogroup.util.ApiTargetTypeUtil;
import com.baidu.beidou.api.external.cprogroup.util.GroupBoMappingUtil;
import com.baidu.beidou.api.external.cprogroup.util.ItUtils;
import com.baidu.beidou.api.external.cprogroup.vo.GroupKeywordItemType;
import com.baidu.beidou.api.external.cprogroup.vo.InterestInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.KtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.PackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.api.external.cprogroup.vo.VtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupItItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupKtItem;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.ListArrayUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.interceptor.CacheInterceptor;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.cprogroup.bo.CustomInterest;
import com.baidu.beidou.cprogroup.bo.GroupIpFilter;
import com.baidu.beidou.cprogroup.bo.GroupPack;
import com.baidu.beidou.cprogroup.bo.GroupSiteFilter;
import com.baidu.beidou.cprogroup.bo.GroupSitePrice;
import com.baidu.beidou.cprogroup.bo.GroupTradePrice;
import com.baidu.beidou.cprogroup.bo.Interest;
import com.baidu.beidou.cprogroup.bo.WordExclude;
import com.baidu.beidou.cprogroup.bo.WordPackExclude;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.ExcludeConstant;
import com.baidu.beidou.cprogroup.constant.GroupPackConstant;
import com.baidu.beidou.cprogroup.constant.InterestConstant;
import com.baidu.beidou.cprogroup.error.GroupErrorConst;
import com.baidu.beidou.cprogroup.exception.ExcludeException;
import com.baidu.beidou.cprogroup.exception.GroupPackException;
import com.baidu.beidou.cprogroup.facade.CproITFacade;
import com.baidu.beidou.cprogroup.facade.CproKeywordFacade;
import com.baidu.beidou.cprogroup.facade.GroupPackFacade;
import com.baidu.beidou.cprogroup.facade.WordExcludeFacade;
import com.baidu.beidou.cprogroup.service.CproGroupITMgr;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproGroupVTMgr;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.cprogroup.service.CustomITMgr;
import com.baidu.beidou.cprogroup.service.GroupSiteConfigMgr;
import com.baidu.beidou.cprogroup.service.InterestMgr;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.cprogroup.util.KTKeywordUtil;
import com.baidu.beidou.cprogroup.util.TargettypeUtil;
import com.baidu.beidou.cprogroup.vo.CproGroupVTVo;
import com.baidu.beidou.cprogroup.vo.CustomInterestVo;
import com.baidu.beidou.cprogroup.vo.GroupPackVo;
import com.baidu.beidou.cprogroup.vo.InterestCacheObject;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.util.OperationHistoryUtils;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.vo.APIResult;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * ClassName: GroupConfigSetServiceImpl
 * Function: 推广组设置内部service
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
public class GroupConfigSetServiceImpl extends GroupConfigBaseService 
		implements GroupConfigSetService {

	private CproKeywordFacade cproKeywordFacade;
	
	private CproGroupMgr cproGroupMgr;
	
	private CproKeywordMgr cproKeywordMgr;

	private GroupSiteConfigMgr groupSiteConfigMgr;
	
	private GroupExcludePeopleFacade groupExcludePeopleFacade;
	
	private GroupPriceFacade groupPriceFacade;

	private static final Log log = LogFactory.getLog(GroupConfigSetServiceImpl.class);

	public boolean saveTradePrice(Integer groupId, boolean isModified,
			List<GroupTradePrice> toBeModifiedTradePriceList,
			GroupSiteConfigMgr groupSiteConfigMgr) {
		if (!isModified) {
			return true;
		}
		
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

		/**
		 * 删除原来的行业出价
		 */
		log.info("### to be delete trade price...");
		List<GroupTradePrice> toBeRemoved = groupSiteConfigMgr.findAllTradePrice(null, null, groupId);
		if (!CollectionUtils.isEmpty(toBeRemoved)) {
			// 打印要删除的行业出价，做log记录
			StringBuilder delSB = new StringBuilder();
			for (GroupTradePrice gtp : toBeRemoved) {
				delSB.delete(0, delSB.length());
				delSB.append("### delete trade price [").append(gtp.getId())
						.append(",").append(gtp.getTradeid()).append(",")
						.append(gtp.getPlanid()).append(",")
						.append(gtp.getGroupid()).append(",")
						.append(gtp.getUserid()).append(",")
						.append(gtp.getPrice()).append("]");

				log.info(delSB);
			}

			groupSiteConfigMgr.delGroupTradePrice(toBeRemoved, visitor);
		}

		/**
		 * 新增分行业出价
		 */
		log.info("### to be add trade price...");
		StringBuilder addSB = new StringBuilder();
		for (GroupTradePrice gtp : toBeModifiedTradePriceList) {
			addSB.delete(0, addSB.length());
			addSB.append("### add trade price [").append(gtp.getId()).append(
					",").append(gtp.getTradeid()).append(",").append(
					gtp.getPlanid()).append(",").append(gtp.getGroupid())
					.append(",").append(gtp.getUserid()).append(",").append(
							gtp.getPrice()).append("]");
			log.info(addSB);
		}

		/**
		 * 此处重新再查一次的原因是为了能正确的设置分行业出价 当新增的行业和上面删除的记录有相同行业时会出现插入失败
		 */
		groupSiteConfigMgr.findAllTradePrice(null, null, groupId);

		if (toBeModifiedTradePriceList.size() == 0) {
			return true;
		}
		// 设置分行业出价
		boolean success = groupSiteConfigMgr.addGroupTradePrice(groupId,
				toBeModifiedTradePriceList, visitor);

		return success;
	}

	public boolean saveSitePrice(Integer groupId, boolean isModified, 
			List<GroupSitePrice> toBeModifiedSitePriceList,
			GroupSiteConfigMgr groupSiteConfigMgr) {
		if (!isModified) {
			return true;
		}
		
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		/**
		 * 删除原来的分网站价格（注意新老记录的兼容）
		 */
		List<GroupSitePrice> curGroupPrice = groupSiteConfigMgr
				.findSitePriceByGroupId(groupId);
		List<GroupSitePrice> toBeRemoved = new ArrayList<GroupSitePrice>();
		List<GroupSitePrice> toBeUpdated = new ArrayList<GroupSitePrice>();

		log.info("### to be delete or update site price...");
		StringBuilder delSB = new StringBuilder();

		if (!CollectionUtils.isEmpty(curGroupPrice)) {
			for (GroupSitePrice gsp : curGroupPrice) {
				if (StringUtils.isNotEmpty(gsp.getTargeturl())) {
					// 这里不能影响原来的分网站点击链接功能，需要再save回去
					gsp.setPrice(null);
					toBeUpdated.add(gsp);
				} else {
					toBeRemoved.add(gsp);
				}

				delSB.delete(0, delSB.length());
				delSB.append("### delete or update site price [").append(
						gsp.getId()).append(",").append(gsp.getGroupid())
						.append(",").append(gsp.getSiteid()).append(",")
						.append(gsp.getSiteurl()).append(",").append(
								gsp.getPrice()).append(",").append(
								gsp.getTargeturl()).append(",").append(
								gsp.getPlanid()).append(",").append(
								gsp.getUserid()).append("]");
				log.info(delSB);
			}

			groupSiteConfigMgr.delAndUpdateGroupSitePrice(toBeRemoved,
					toBeUpdated, visitor);
		}

		/**
		 * 新增分网站价格
		 */
		log.info("### to be add site price...");
		StringBuilder addSB = new StringBuilder();
		for (GroupSitePrice gsp : toBeModifiedSitePriceList) {
			addSB.delete(0, delSB.length());
			addSB.append("### add site price [").append(gsp.getId())
					.append(",").append(gsp.getGroupid()).append(",").append(
							gsp.getSiteid()).append(",").append(
							gsp.getSiteurl()).append(",")
					.append(gsp.getPrice()).append(",").append(
							gsp.getTargeturl()).append(",").append(
							gsp.getPlanid()).append(",")
					.append(gsp.getUserid()).append("]");
			log.info(addSB);
		}

		if (toBeModifiedSitePriceList.size() == 0) {
			return true;
		}
		
		groupSiteConfigMgr.findSitePriceByGroupId(groupId);
		boolean success = groupSiteConfigMgr.addGroupSitePrice(groupId,
				toBeModifiedSitePriceList, visitor);

		return success;
	}

	public ApiResult<Object> saveSiteTargetUrl(ApiResult<Object> result,
			Integer groupId,
			List<GroupSitePrice> toBeModifiedSiteTargetUrlList,
			GroupSiteConfigMgr groupSiteConfigMgr) {
		PaymentResult pay = result.getPayment();

		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

		/**
		 * 删除原来的分网站点击链接（注意新老记录的兼容）
		 */
		List<GroupSitePrice> curGroupPrice = groupSiteConfigMgr
				.findSiteTargetUrlByGroupId(groupId);
		List<GroupSitePrice> toBeRemoved = new ArrayList<GroupSitePrice>();
		List<GroupSitePrice> toBeUpdated = new ArrayList<GroupSitePrice>();

		log.info("### to be delete or update site targeturl...");
		StringBuilder delSB = new StringBuilder();

		if (!CollectionUtils.isEmpty(curGroupPrice)) {
			for (GroupSitePrice gsp : curGroupPrice) {
				if (gsp.getPrice() != null) {
					// 这里不能影响原有分网站点击链接功能，需要再save回去
					gsp.setTargeturl(null);
					toBeUpdated.add(gsp);
				} else {
					toBeRemoved.add(gsp);
				}

				delSB.delete(0, delSB.length());
				delSB.append("### delete or update site targeturl [").append(
						gsp.getId()).append(",").append(gsp.getGroupid())
						.append(",").append(gsp.getSiteid()).append(",")
						.append(gsp.getSiteurl()).append(",").append(
								gsp.getPrice()).append(",").append(
								gsp.getTargeturl()).append(",").append(
								gsp.getPlanid()).append(",").append(
								gsp.getUserid()).append("]");
				log.info(delSB);
			}

			groupSiteConfigMgr.delAndUpdateGroupSitePrice(toBeRemoved,
					toBeUpdated, visitor);
		}

		/**
		 * 新增分网站点击链接
		 */
		log.info("### to be add site targeturl...");
		StringBuilder addSB = new StringBuilder();
		for (GroupSitePrice gsp : toBeModifiedSiteTargetUrlList) {
			addSB.delete(0, addSB.length());
			addSB.append("### add site targeturl [").append(gsp.getId())
					.append(",").append(gsp.getGroupid()).append(",").append(
							gsp.getSiteid()).append(",").append(
							gsp.getSiteurl()).append(",")
					.append(gsp.getPrice()).append(",").append(
							gsp.getTargeturl()).append(",").append(
							gsp.getPlanid()).append(",")
					.append(gsp.getUserid()).append("]");
			log.info(addSB);
		}
		
		boolean success = false;
		
		if (toBeModifiedSiteTargetUrlList.size() == 0) {
			success = true;
		} else {
			success = groupSiteConfigMgr.addGroupSiteTargetUrl(groupId,
				toBeModifiedSiteTargetUrlList, visitor);
		}

		if (success) {
			
			//------- 保存历史操作记录 start ------
			try{
				User dataUser = (User)(SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER));
				
				List<OptContent> optContents = new ArrayList<OptContent>();
				
				OpTypeVo opTypeVo = OptHistoryConstant.OPTYPE_GROUP_SITE_URL;
				List<String> siteTargetUrlStringList = null;
				//如果添加成功，则将添加的内容加入操作历史记录
				OptContent optContent = new OptContent();
				optContent.setUserid(dataUser.getUserid());
				optContent.setOpObjId(groupId);
				optContent.setOpLevel(opTypeVo.getOpLevel());
				optContent.setOpType(opTypeVo.getOpType());
				siteTargetUrlStringList = groupSiteConfigMgr.findSiteTargetUrlStringListByGroupId(curGroupPrice);
				String preContent = opTypeVo.getTransformer().toDbString(siteTargetUrlStringList);
				optContent.setPreContent(preContent);//保存之前的列表
				
				List<GroupSitePrice> siteTargetUrlList = groupSiteConfigMgr.findSiteTargetUrlByGroupId(groupId);
				siteTargetUrlStringList = groupSiteConfigMgr.findSiteTargetUrlStringListByGroupId(siteTargetUrlList);
				String postContent = opTypeVo.getTransformer().toDbString(siteTargetUrlStringList);
				optContent.setPostContent(postContent);//保存之后的列表

				optContents.add(optContent);

				SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
			} catch(Exception e){
			  log.error("failed to contruct opt history content. " + e.getMessage(), e);
			}
			//------- 保存历史操作记录 end ------
			
			pay.setSuccess(1);
		}  else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.SITE_URL_REQ);
			apiPosition.addParam(GroupConstant.SITE_URL_LIST);

			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.SET_SITE_URL_FAILED.getValue(),
					GroupConfigErrorCode.SET_SITE_URL_FAILED.getMessage(), 
					apiPosition.getPosition(), null);
		}


		return result;
	}

	public boolean saveVT(ApiResult<Object> result, CproGroup group,
			VtItemType vtItem, VtPeopleMgr vtPeopleMgr, CproGroupVTMgr cproGroupVTMgr, List<OptContent> optContents) {
		
		Long[] includeVTIds = vtItem.getRelatedPeopleIds();
		Long[] excludeVTIds = vtItem.getUnRelatePeopleIds();

		// 验证vt的关联人群
		if (ArrayUtils.isEmpty(includeVTIds)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TARGET_INFO)
					.addParam(GroupConstant.VT_ITEM)
					.addParam(GroupConstant.POSITION_VT_RELATED_PEOPLE);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.VT_RELATED_PEOPLE_ERROR.getValue(),
					GroupConfigErrorCode.VT_RELATED_PEOPLE_ERROR.getMessage(), 
					apiPosition.getPosition(),
					null);
			return false;
		}

		// 验证vt人群的长度
		if(ArrayUtils.isEmpty(excludeVTIds)){
			excludeVTIds = new Long[0];
		}
		if (includeVTIds.length + excludeVTIds.length
				> CproGroupConstant.GROUP_VT_INCLUDE_CROWD_MAX_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TARGET_INFO)
					.addParam(GroupConstant.VT_ITEM)
					.addParam(GroupConstant.POSITION_VT_RELATED_PEOPLE);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.VT_PEOPLE_MAX.getValue(),
					GroupConfigErrorCode.VT_PEOPLE_MAX.getMessage(),
					apiPosition.getPosition(), null);
			return false;
		}
		
		// 验证该人群id是否属于该用户
		

		/**
		 * save VT info
		 */
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

		// 记录简单日志
		StringBuilder sb = new StringBuilder("### save RT, ")
				.append(" userId=").append(group.getUserId())
				.append(", groupId=").append(group.getGroupId())
				.append(", related people size=").append(includeVTIds.length)
				.append(", unrelated people size==").append(includeVTIds.length);
		log.info(sb);
		
		
		List<CproGroupVTVo> oldVTRelationList = cproGroupVTMgr
				.findVTRelationByGroup(group.getGroupId(),	group.getUserId());
		List<CproGroupVTVo> vtPeopleList = vtPeopleMgr.getAllVtPeople(group.getUserId());
		Set<Long> vtPeopleIdSet = new HashSet<Long>();
		for (CproGroupVTVo people : vtPeopleList) {
			vtPeopleIdSet.add(people.getId());
		}
		
		List<CproGroupVTVo> newVTRelationList = new ArrayList<CproGroupVTVo>();
		Map<Long, CproGroupVTVo> includePidMap = makeVTMap(includeVTIds, true);
		Map<Long, CproGroupVTVo> excludePidMap = makeVTMap(excludeVTIds, false);
		for (Long includePid : includePidMap.keySet()) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TARGET_INFO)
					.addParam(GroupConstant.VT_ITEM)
					.addParam(GroupConstant.POSITION_VT_RELATED_PEOPLE);
			
			if (!vtPeopleIdSet.contains(includePid)) {
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.VT_PEOPLE_ERROR.getValue(),
						GroupConfigErrorCode.VT_PEOPLE_ERROR.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
			if (excludePidMap.containsKey(includePid)) {
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getValue(),
						GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
			newVTRelationList.add(includePidMap.get(includePid));
		}
		for (Long excludePid : excludePidMap.keySet()) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TARGET_INFO)
					.addParam(GroupConstant.VT_ITEM)
					.addParam(GroupConstant.POSITION_VT_RELATED_PEOPLE);
			
			if (!vtPeopleIdSet.contains(excludePid)) {
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.VT_PEOPLE_ERROR.getValue(),
						GroupConfigErrorCode.VT_PEOPLE_ERROR.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
			
			newVTRelationList.add(excludePidMap.get(excludePid));
		}

		int targetType = TargettypeUtil.clearKT(group.getTargetType());
		targetType = TargettypeUtil.clearRT(targetType);
		targetType = targetType | CproGroupConstant.GROUP_TARGET_TYPE_VT;
		cproGroupVTMgr.modTargetTypeToVT(targetType, newVTRelationList, oldVTRelationList, group.getGroupId(), visitor);
		
		//------- 保存历史操作记录 start ------
		try{
			// 记录操作历史信息
			// 批量初始化新建人群对应的“人群 名称”
			initVtPeopleName(newVTRelationList, group.getUserId(), group.getGroupId(), vtPeopleMgr, cproGroupVTMgr);
			OpTypeVo opType = null;
			Map<String, List<String>> includeChangeMap = getChangedPeopleNameMap(newVTRelationList,
					oldVTRelationList, group.getGroupId(), CproGroupConstant.GROUP_VT_INCLUDE_CROWD);
			Map<String, List<String>> exCludeChangeMap = getChangedPeopleNameMap(newVTRelationList,
					oldVTRelationList, group.getGroupId(), CproGroupConstant.GROUP_VT_EXCLUDE_CROWD);
			// 变动关联人群
			if ((includeChangeMap != null) && ((!CollectionUtils.isEmpty(includeChangeMap.get(VALUE_BEFORE))) || (!CollectionUtils
					.isEmpty(includeChangeMap.get(VALUE_AFTER))))) {
				opType = OptHistoryConstant.OPTYPE_GROUP_VTPEOPLE_ADD;
				OptContent content = new OptContent(group.getUserId(), opType.getOpType(), opType
						.getOpLevel(), group.getGroupId(), opType.getTransformer().toDbString(
						includeChangeMap.get(VALUE_BEFORE)), opType.getTransformer().toDbString(
						includeChangeMap.get(VALUE_AFTER)));
				optContents.add(content);
			}
			// 变动排除人群
			if ((exCludeChangeMap != null)
					&& ((!CollectionUtils.isEmpty(exCludeChangeMap.get(VALUE_BEFORE))) || (!CollectionUtils
							.isEmpty(exCludeChangeMap.get(VALUE_AFTER))))) {
				opType = OptHistoryConstant.OPTYPE_GROUP_VTPEOPLE_DELETE;
				OptContent content = new OptContent(group.getUserId(), opType.getOpType(), opType
						.getOpLevel(), group.getGroupId(), opType.getTransformer().toDbString(
						exCludeChangeMap.get(VALUE_BEFORE)), opType.getTransformer().toDbString(
						exCludeChangeMap.get(VALUE_AFTER)));
				optContents.add(content);

			}
			// 定向方式修改
			if (!TargettypeUtil.hasVT(group.getTargetType()))  {
				opType = OptHistoryConstant.OPTYPE_GROUP_XT_CHANGE;
				OptContent content = new OptContent(group.getUserId(), opType.getOpType(), opType
						.getOpLevel(), group.getGroupId(), opType.getTransformer().toDbString(
								group.getTargetType()), opType.getTransformer().toDbString(targetType));
				optContents.add(content);
			}
		} catch(Exception e){
		  log.error("failed to contruct opt history content. " + e.getMessage(), e);
		}
		//------- 保存历史操作记录 end ------
		
		return true;
	}
	
	public boolean saveIT(ApiResult<Object> result, 
			CproGroup group,
			InterestInfoType interestInfo,
			CproITFacade cproITFacade,
			CproGroupITMgr cproGroupItMgr,
			InterestMgr interestMgr,
			CustomITMgr customITMgr,
			int userId,
			int opUserId,
			List<OptContent> optContents){ 
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		int groupId = group.getGroupId();
		int planId = group.getPlanId();
		
		int[] interestIds = interestInfo.getInterestIds();  //正向关联兴趣
		int[] exceptInterestIds = interestInfo.getExceptInterestIds();  //反向关联兴趣

		boolean itEnabled = interestInfo.isEnable();
		
		// 启用IT并且interestIds数组为空，则报错
		if (itEnabled && (interestIds == null)){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.INTEREST_INFO);
			apiPosition.addParam(GroupConstant.INTEREST_IDS);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.INTEREST_EMPTY_ERROR.getValue(),
					GroupConfigErrorCode.INTEREST_EMPTY_ERROR.getMessage(), apiPosition.getPosition(), null);
			return false;
		}
		
		// interestIds数组长度不能大于阈值
//		if (interestIds != null && interestIds.length > InterestConstant.MAX_INTEREST_PER_GROUP) {
//			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
//			apiPosition.addParam(GroupConstant.INTEREST_INFO);
//			apiPosition.addParam(GroupConstant.INTEREST_IDS);
//			
//			result = ApiResultBeanUtils.addApiError(result,
//					GroupConfigErrorCode.ADD_MAX_INTEREST_ERROR.getValue(),
//					GroupConfigErrorCode.ADD_MAX_INTEREST_ERROR.getMessage(), apiPosition.getPosition(), null);
//			return false;
//		}
		
		// exceptInterestIds数组若不为空，那么长度不能大于阈值
//		if (exceptInterestIds != null && exceptInterestIds.length > InterestConstant.MAX_EXCLUDE_INTEREST_PER_GROUP) {
//			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
//			apiPosition.addParam(GroupConstant.INTEREST_INFO);
//			apiPosition.addParam(GroupConstant.EXCEPT_INTEREST_IDS);
//			
//			result = ApiResultBeanUtils.addApiError(result,
//					GroupConfigErrorCode.ADD_MAX_EXCLUDE_INTEREST_ERROR.getValue(),
//					GroupConfigErrorCode.ADD_MAX_EXCLUDE_INTEREST_ERROR.getMessage(), apiPosition.getPosition(), null);
//			return false;
//		}
		
		List<Integer> interestIdsList =  ListArrayUtils.asList(interestIds);
		List<Integer> exceptInterestIdsList = ListArrayUtils.asList(exceptInterestIds);
		List<GroupItItem> interestIdsItemList = ItUtils.transItIdList2GroupItItemList(interestIds);
		List<GroupItItem> exceptInterestIdsItemList = ItUtils.transItIdList2GroupItItemList(exceptInterestIds);
		
		if(interestIdsList.size() == 1 && interestIdsList.get(0) == GroupConstant.API_GLOBAL_NOT_UPDATE_FLAG_INT && 
				exceptInterestIdsList.size() == 1 && exceptInterestIdsList.get(0) == GroupConstant.API_GLOBAL_NOT_UPDATE_FLAG_INT){
			log.info("not update group interest info, groupId=[" + group.getGroupId() + "]");
		} else {
			// 启用IT，正向关联兴趣至少有一个
			if (itEnabled && (interestIds != null && interestIds.length < 1)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.INTEREST_INFO);
				apiPosition.addParam(GroupConstant.INTEREST_IDS);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.GROUP_INTEREST_AT_LEAST_ONE_ERROR.getValue(),
						GroupConfigErrorCode.GROUP_INTEREST_AT_LEAST_ONE_ERROR.getMessage(), apiPosition.getPosition(), null);
				return false;
			}
			
			// 不启用IT，正向关联兴趣不能有
			if (!itEnabled && (interestIds != null && interestIds.length > 0)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.INTEREST_INFO);
				apiPosition.addParam(GroupConstant.INTEREST_IDS);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.GROUP_INTEREST_DISABLE_BUT_HAVE_RELATE_INTEREST.getValue(),
						GroupConfigErrorCode.GROUP_INTEREST_DISABLE_BUT_HAVE_RELATE_INTEREST.getMessage(), apiPosition.getPosition(), null);
				return false;
			}
			
			// 在正向关联兴趣列表中就不能在反向关联中
			Set<Integer> interestIdsSet =  new HashSet<Integer>(interestIdsList);
			Set<Integer> exceptInterestIdsSet = new HashSet<Integer>(exceptInterestIdsList);
			for(int index = 0; index < exceptInterestIdsList.size(); index++){
				if(interestIdsSet.contains(exceptInterestIdsList.get(index))){
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.INTEREST_INFO);
					apiPosition.addParam(GroupConstant.EXCEPT_INTEREST_IDS, index);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.INTEREST_SHOULD_NOT_IN_BOTH_SET.getValue(),
							GroupConfigErrorCode.INTEREST_SHOULD_NOT_IN_BOTH_SET.getMessage(), apiPosition.getPosition(), null);
					return false;
				}
			}
					
			//兴趣点是缓存的，直接获取；兴趣组合查数据库，所以如果有兴趣组合才去查兴趣组合的数据库信息
			Map<Integer, Interest> interestMap = interestMgr.getInterestMap();
			
			//验证每一个interestId,保存一个合法的兴趣点名字一个合法的兴趣组合的名字,优先从缓存中获取
			Map<Integer, CustomInterestVo> customInterestMap = CustomInterestVo.load2VoMap(customITMgr.getCustomItListByUserId(userId));
			if(customInterestMap == null){
				List<CustomInterest> customInterests = customITMgr.getCustomItListByUserId(userId);
				List<CustomInterestVo> customInterestVoList = new LinkedList<CustomInterestVo>();
				for(CustomInterest o : customInterests){
					customInterestVoList.add(new CustomInterestVo(o));
				}
				customInterestMap = ItUtils.transVo2Map(customInterestVoList);
			}
			
			// 验证兴趣是否合法存在
			Map<Integer,InterestCacheObject> cache = interestMgr.getInterestCacheMap();
			Set<Integer> validInterestIdsSet = new HashSet<Integer>();
			Set<Integer> validExceptInterestIdsSet = new HashSet<Integer>();
			if(!ItUtils.validateInterestNotDuplicateInChildren(result, interestIdsItemList, exceptInterestIdsSet, cache, GroupConstant.INTEREST_INFO, GroupConstant.INTEREST_IDS, true, validInterestIdsSet) ||
				!ItUtils.validateInterestNotDuplicateInChildren(result, exceptInterestIdsItemList, interestIdsSet, cache, GroupConstant.INTEREST_INFO, GroupConstant.EXCEPT_INTEREST_IDS, true, validExceptInterestIdsSet) ||
				!ItUtils.validateInterestExist(result, interestIdsItemList, interestMap, customInterestMap, GroupConstant.INTEREST_INFO, GroupConstant.INTEREST_IDS, true, validInterestIdsSet) ||
				!ItUtils.validateInterestExist(result, exceptInterestIdsItemList, interestMap, customInterestMap, GroupConstant.INTEREST_INFO, GroupConstant.EXCEPT_INTEREST_IDS, true, validExceptInterestIdsSet) ){
				return false;
			}
			
			//保存正向反向关联关系
			cproGroupItMgr.saveGroupIT(groupId, interestIdsList, planId, userId, opUserId);
			cproGroupItMgr.saveGroupITExclude(groupId, exceptInterestIdsList, planId, userId, opUserId);
		}
	
		// 不启用IT，如果推广组启用过则clear掉
		if (!itEnabled) {
			int beforeTargetType = group.getTargetType();
			int afterTargetType = TargettypeUtil.clearIT(group.getGroupType());
			if(beforeTargetType != afterTargetType){
				group.setTargetType(afterTargetType);
				cproGroupMgr.modCproGroup(visitor, group);
				//历史操作记录
				OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_IT_ENABLE;
				OptContent content = new OptContent(group.getUserId(), opType.getOpType(), opType
						.getOpLevel(), group.getGroupId(), opType.getTransformer().toDbString(
								beforeTargetType), opType.getTransformer().toDbString(afterTargetType));
				OperationHistoryUtils.putOperationContent(content);
				optContents.addAll(OperationHistoryUtils.getOptContents());
			}
		} else {
			//保存推广组信息
			int beforeTargetType = group.getTargetType();
			int afterTargetType = group.getTargetType() | CproGroupConstant.GROUP_TARGET_TYPE_IT;
			if(beforeTargetType != afterTargetType){
				group.setTargetType(afterTargetType);
				cproGroupMgr.modCproGroup(visitor, group);
				
				//历史操作记录
				OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_IT_ENABLE;
				OptContent content = new OptContent(group.getUserId(), opType.getOpType(), opType
						.getOpLevel(), group.getGroupId(), opType.getTransformer().toDbString(
								beforeTargetType), opType.getTransformer().toDbString(afterTargetType));
				OperationHistoryUtils.putOperationContent(content);
			}
		}
		
		if(OperationHistoryUtils.getOptContents() != null){
			optContents.addAll(OperationHistoryUtils.getOptContents());
		}
		
		return true;
	}

	
	public ApiResult<Object> saveIpFilter(ApiResult<Object> result,
			Integer groupId, Integer userid, Set<String> allIp) {
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

		List<GroupIpFilter> curGroupFilter = groupSiteConfigMgr.findIpFilterByGroupId(groupId);

		Map<String, Boolean> map = new HashMap<String, Boolean>();

		for (String site : allIp) {
			map.put(site, true);
		}
		
		List<String> deleteExcludeIpList = new ArrayList<String>(); //临时对象
		for (GroupIpFilter filter : curGroupFilter) {
			String fIp = filter.getIp().toLowerCase();
			if (map.get(fIp) == null) {
				groupSiteConfigMgr.delGroupIpFilter(filter.getId(), visitor);
				deleteExcludeIpList.add(fIp);
			} else {
				map.remove(fIp);
			}
		}

		PaymentResult pay = result.getPayment();
		if (map.size() == 0) {
			
			//------- 保存历史操作记录 start ------
			try{
				OpTypeVo opTypeVo = OptHistoryConstant.OPTYPE_GROUP_IPFILTER_BATCH;

				User dataUser = (User)(SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER));

				List<OptContent> optContents = new ArrayList<OptContent>();

				OptContent optContent = new OptContent();
				optContent.setUserid(dataUser.getUserid());
				optContent.setOpObjId(groupId);
				optContent.setOpLevel(opTypeVo.getOpLevel());
				optContent.setOpType(opTypeVo.getOpType());
				String preContent = opTypeVo.getTransformer().toDbString(deleteExcludeIpList);
				optContent.setPreContent(preContent);//保存之前的列表
				String postContent = opTypeVo.getTransformer().toDbString(new ArrayList<String>() );
				optContent.setPostContent(postContent);//保存之后的列表

				optContents.add(optContent);

				SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
			} catch(Exception e){
			  log.error("failed to contruct opt history content. " + e.getMessage(), e);
			}
			//------- 保存历史操作记录 end ------
			
			pay.setSuccess(1);
			ApiResultBeanUtils.setSuccessObject(result);
		} else {
			List<String> addList = new ArrayList<String>();
			for (String ip : map.keySet()) {
				addList.add(ip);
			}
			
			boolean success = false;
			
			if (addList.size() == 0) {
				success = true;
			} else {
				success = groupSiteConfigMgr.addGroupIpFilter(groupId, userid, addList,	visitor);
			}
			
			if (success) {
				
				//------- 保存历史操作记录 start ------
				try{
					OpTypeVo opTypeVo = OptHistoryConstant.OPTYPE_GROUP_IPFILTER_BATCH;

					User dataUser = (User)(SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER));

					List<OptContent> optContents = new ArrayList<OptContent>();

					OptContent optContent = new OptContent();
					optContent.setUserid(dataUser.getUserid());
					optContent.setOpObjId(groupId);
					optContent.setOpLevel(opTypeVo.getOpLevel());
					optContent.setOpType(opTypeVo.getOpType());
					List<String> ipFilterStringList = null; //临时对象
					ipFilterStringList = this.findExcludeIpStringList(curGroupFilter);
					String preContent = opTypeVo.getTransformer().toDbString(ipFilterStringList);
					optContent.setPreContent(preContent);//保存之前的列表
					List<GroupIpFilter> ipFilterList = groupSiteConfigMgr.findIpFilterByGroupId(groupId);
					ipFilterStringList = this.findExcludeIpStringList(ipFilterList);
					String postContent = opTypeVo.getTransformer().toDbString(ipFilterStringList);
					optContent.setPostContent(postContent);//保存之后的列表

					optContents.add(optContent);

					SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
				} catch(Exception e){
				  log.error("failed to contruct opt history content. " + e.getMessage(), e);
				}
				//------- 保存历史操作记录 end ------
				
				pay.setSuccess(1);
				ApiResultBeanUtils.setSuccessObject(result);
			} else {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_IP_REQ);
				apiPosition.addParam(GroupConstant.EXCLUDE_IP_TYPE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SET_EXCLUDE_IP_FAILED.getValue(),
						GroupConfigErrorCode.SET_EXCLUDE_IP_FAILED.getMessage(), 
						apiPosition.getPosition(), null);
			}
		}

		return result;
	}

	public ApiResult<Object> saveSiteFilter(ApiResult<Object> result,
			Integer groupId, Set<String> allSite) {
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

		List<GroupSiteFilter> curGroupFilter = groupSiteConfigMgr
				.findSiteFilterByGroupId(groupId);

		Map<String, Boolean> map = new HashMap<String, Boolean>();

		for (String site : allSite) {
			map.put(site, true);
		}

		List<String> deleteExcludeSiteList = new ArrayList<String>(); //临时对象
		for (GroupSiteFilter filter : curGroupFilter) {
			String fSite = filter.getSite().toLowerCase();
			if (map.get(fSite) == null) {
				groupSiteConfigMgr.delGroupSiteFilter(filter.getId(), visitor);
				deleteExcludeSiteList.add(fSite);
			} else {
				map.remove(fSite);
			}
		}
		
		PaymentResult pay = result.getPayment();
		if (map.size() == 0) {
			//------- 保存历史操作记录 start ------
			try{
				User dataUser = (User)(SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER));

				List<OptContent> optContents = new ArrayList<OptContent>();

				// 记录删除全部过滤网站的历史操作记录
				if (!CollectionUtils.isEmpty(deleteExcludeSiteList)) {
					OpTypeVo opTypeVoDel = OptHistoryConstant.OPTYPE_GROUP_SITEFILTER_BATCH_DELETE;
					optContents.add(new OptContent(dataUser.getUserid(), opTypeVoDel.getOpType(), opTypeVoDel.getOpLevel(),
							groupId, opTypeVoDel.getTransformer().toDbString(deleteExcludeSiteList), null));
				}

				SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
			} catch(Exception e){
			  log.error("failed to contruct opt history content. " + e.getMessage(), e);
			}
			//------- 保存历史操作记录 end ------
			
			pay.setSuccess(1);
			ApiResultBeanUtils.setSuccessObject(result);
		} else {
			List<String> addList = new ArrayList<String>();
			for (String site : map.keySet()) {
				addList.add(site);
			}

			boolean success = false;
			
			if (addList.size() == 0) {
				success = true;
			} else {
				success = groupSiteConfigMgr.addGroupSiteFilter(groupId, addList, visitor);
			}

			if (success) {
				//------- 保存历史操作记录 start ------
				try{
					User dataUser = (User)(SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER));

					List<OptContent> optContents = new ArrayList<OptContent>();

					List<String> siteFilterStringList = null; //临时对象
					siteFilterStringList = this.findExcludeSiteStringList(curGroupFilter);

					// 先记录删除全部过滤网站的历史操作记录
					if(!CollectionUtils.isEmpty(siteFilterStringList)){
						OpTypeVo opTypeVoDel = OptHistoryConstant.OPTYPE_GROUP_SITEFILTER_BATCH_DELETE;			
						optContents.add(new OptContent(dataUser.getUserid(), opTypeVoDel.getOpType(), 
								opTypeVoDel.getOpLevel(), groupId,	opTypeVoDel.getTransformer().toDbString(siteFilterStringList), null));
					}

					// 再记录添加的过滤网站历史操作记录
					if(!CollectionUtils.isEmpty(addList)){
						OpTypeVo opTypeVoAdd = OptHistoryConstant.OPTYPE_GROUP_SITEFILTER_BATCH_ADD;			
						optContents.add(new OptContent(dataUser.getUserid(), opTypeVoAdd.getOpType(), 
								opTypeVoAdd.getOpLevel(), groupId,	null, 
								opTypeVoAdd.getTransformer().toDbString(new ArrayList<String>(allSite))));
					}

					SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
				} catch(Exception e){
				  log.error("failed to contruct opt history content. " + e.getMessage(), e);
				}
				//------- 保存历史操作记录 end ------
				
				pay.setSuccess(1);
				ApiResultBeanUtils.setSuccessObject(result);
			} else {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_SITE_REQ);
				apiPosition.addParam(GroupConstant.EXCLUDE_SITE_TYPE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SET_EXCLUDE_SITE_FAILED.getValue(),
						GroupConfigErrorCode.SET_EXCLUDE_SITE_FAILED.getMessage(), 
						apiPosition.getPosition(), null);
			}
		}
		
		return result;
	}
	
	/**
	 * 修改关键词的匹配模式
	 * @param result
	 * @param keywords
	 * @return ApiResult<Object>
	 */
	public ApiResult<Object> setKeyword(ApiResult<Object> result,
			GroupKeywordItemType[] keywords){
		PaymentResult pay = result.getPayment();
		pay.setTotal(keywords.length);
		result.setPayment(pay);
		
		// 按照推广组将关键词分组，key为groupId
		Map<Long, List<GroupKtItem>> groupKeywords = new HashMap<Long, List<GroupKtItem>>();
		for (int index = 0; index < keywords.length; index++) {
			GroupKeywordItemType item = keywords[index];
			Long groupId = item.getGroupId();
			
			// 简单验证关键词信息是否完整，包括传入的GroupId、keyword字面，不完整则不进行下一步
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.KEYWORDS, index);
			
			if (item.getGroupId() == 0) {
				apiPosition.addParam(GroupConstant.GROUPID);
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KEYWORD_OR_GROUPID_NULL_ERROR.getValue(),
						GroupConfigErrorCode.KEYWORD_OR_GROUPID_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}
			
			if (StringUtils.isEmpty(item.getKeyword())) {
				apiPosition.addParam(GroupConstant.KEYWORD);
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KEYWORD_OR_GROUPID_NULL_ERROR.getValue(),
						GroupConfigErrorCode.KEYWORD_OR_GROUPID_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}
			
			if (item.getGroupId() == 0) {
				apiPosition.addParam(GroupConstant.GROUPID);
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KEYWORD_OR_GROUPID_NULL_ERROR.getValue(),
						GroupConfigErrorCode.KEYWORD_OR_GROUPID_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			List<GroupKtItem> groupKeywordList = groupKeywords.get(groupId);
			if (groupKeywordList == null) {
				groupKeywordList = new ArrayList<GroupKtItem>();
				groupKeywords.put(groupId, groupKeywordList);
			}

			groupKeywordList.add(new GroupKtItem(index, item.getKeyword(), item.getPattern()));
		}

		// 验证待修改关键词推广组的个数
		if (groupKeywords.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.KEYWORDS);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		
		// 依照推广组修改关键词
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupKeywords.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupKtItem> groupKeywordList = groupKeywords.get(groupId);

			// 推广组不存在
			if (group == null) {
				for (GroupKtItem item : groupKeywordList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.KEYWORDS, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}

			int targetType = group.getTargetType();
			// 定向方式错误
			if (!TargettypeUtil.hasKT(targetType)) {
				for (GroupKtItem item : groupKeywordList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.KEYWORDS, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KT_GROUP_TYPE_ERROR.getValue(),
							GroupConfigErrorCode.KT_GROUP_TYPE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 获得用户当前关键词
			Map<String, CproKeyword> dbKeywordsMap = new HashMap<String, CproKeyword>();
			List<CproKeyword> dbKeywords = cproKeywordMgr.findByGroupId(groupId.intValue(), bdUser.getUserid());
			for(CproKeyword keyword: dbKeywords){
				dbKeywordsMap.put(keyword.getKeyword(), keyword);
			}

			Set<String> toSetWordStringSet = new HashSet<String>();

			// 对于原始输入的处理，大小写不敏感去重
			String REG = "[^a-zA-Z0-9\\.\\+\\#\\/\\ \\-\u4e00-\u9fa5]";
			Pattern pattern = Pattern.compile(REG);
			for (GroupKtItem item : groupKeywordList) {
				String word = item.getKeyword();
				
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.KEYWORDS, item.getIndex());
				
				apiPosition.addParam(GroupConstant.KEYWORD);
				if (StringUtils.isEmpty(word)) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
							GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				
				try {
					word = word.trim();
					int wordLen = com.baidu.beidou.util.StringUtils.getGBKLen(word);
					if (wordLen > 40 || wordLen == 0) {
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
								GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(),
								apiPosition.getPosition(), null);
						continue;
					}
				} catch (Exception e) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
							GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}

				Matcher matcher = pattern.matcher(word);
				if (matcher.find()) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
							GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}

				word = word.replaceAll("\\s+", " ");

				if (toSetWordStringSet.contains(word)) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KT_WORDS_DUP.getValue(),
							GroupConfigErrorCode.KT_WORDS_DUP.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				toSetWordStringSet.add(word);
				
				if(!dbKeywordsMap.containsKey(word)){
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KEYWORD_NOT_FOUND_ERROR.getValue(),
							GroupConfigErrorCode.KEYWORD_NOT_FOUND_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
			}

			if (toSetWordStringSet.size() > 0) {
				
				// 获得需要新增、需要删除、改为高级匹配、改为短语匹配  的关键词
				List<CproKeyword> toAddList = new ArrayList<CproKeyword>();
				List<CproKeyword> toDelList = new ArrayList<CproKeyword>();
				
				KTKeywordUtil.filterKeyword(group, visitor, dbKeywords, toSetWordStringSet, toAddList, toDelList, CproKeyword.class);
				StringBuilder keywordSb = new StringBuilder("### toAddKeywordNum=[")
						.append(toAddList.size()).append("], toDelKeywordNum=[")
						.append(toDelList.size()).append("]");
				log.info(keywordSb);
				
				// 修改推广组中的关键词
				cproKeywordMgr.modifyKeywords(groupId.intValue(), bdUser.getUserid(), visitor, toAddList, null);
				pay.increSuccess(toSetWordStringSet.size());
				
				// 记录简单日志
				StringBuilder sb = new StringBuilder("### update kt keywords pattern, size is ")
						.append(toSetWordStringSet.size()).append(" userId=")
						.append(bdUser.getUserid()).append(", groupId=").append(groupId);
				log.info(sb);
				
			} else {
				continue;
			}
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
		
	}
	
	/**
	 * 修改推广组定向方式为关键词
	 * @param result
	 * @param group
	 * @param optContents
	 * @return boolean 是否修改成功
	 */
	public boolean saveKT(ApiResult<Object> result, 
			CproGroup group,
			KtItemType ktItem, 
			CproKeywordMgr cproKeywordMgr, 
			List<OptContent> optContents){
		if (ktItem == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TARGET_INFO);
			apiPosition.addParam(GroupConstant.KT_ITEM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
					GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return false;
		}
		
		// 两个变量可以传GroupConstant.API_GLOBAL_NOT_UPDATE_FLAG=-1，表示不修改
		int targetType;
		int aliveDays;
		
		// 验证关键词的aliveday
		if(ktItem.getAliveDays() == GroupConstant.API_GLOBAL_NOT_UPDATE_FLAG_INT){  
			aliveDays = group.getQtAliveDays(); //不更新有效期，则leave it alone
		} else {
			aliveDays = ktItem.getAliveDays();
			if (!Arrays.asList(CproGroupConstant.GROUP_KT_ALIVEDAYS).contains(aliveDays)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TARGET_INFO)
						.addParam(GroupConstant.KT_ITEM)
						.addParam(GroupConstant.POSITION_KT_ALIVEDAYS);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KT_ALIVEDAYS_ERROR.getValue(),
						GroupConfigErrorCode.KT_ALIVEDAYS_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}
		}
		
		
		// 验证kt的targetType
		if(ktItem.getTargetType() == GroupConstant.API_GLOBAL_NOT_UPDATE_FLAG_INT){
			targetType = group.getTargetType(); //不更新关键词定向方式，则leave it alone，这里做了二次校验数据库中的TargetType到底是不是KT
			if (!TargettypeUtil.hasKT(targetType)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TARGET_INFO)
						.addParam(GroupConstant.KT_ITEM)
						.addParam(GroupConstant.POSITION_KT_TARGETTYPE);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KT_TARGETTYPE_INTERNAL_ERROR.getValue(),
						GroupConfigErrorCode.KT_TARGETTYPE_INTERNAL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}
		} else {
			targetType = ktItem.getTargetType();
			if (!ApiTargetTypeUtil.isValidKtTargetType(targetType)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TARGET_INFO)
						.addParam(GroupConstant.KT_ITEM)
						.addParam(GroupConstant.POSITION_KT_TARGETTYPE);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KT_TARGETTYPE_ERROR.getValue(),
						GroupConfigErrorCode.KT_TARGETTYPE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}
			targetType = TargettypeUtil.clearCT(group.getTargetType());
			targetType = TargettypeUtil.clearQT(targetType);
			targetType = TargettypeUtil.clearHCT(targetType);
			targetType = TargettypeUtil.clearRT(targetType);
			targetType = TargettypeUtil.clearVT(targetType);
			targetType = targetType | ktItem.getTargetType();
		}
		
		// 验证kt的word
		if (ArrayUtils.isEmpty(ktItem.getKtWordList())) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TARGET_INFO);
			apiPosition.addParam(GroupConstant.KT_ITEM);
			apiPosition.addParam(GroupConstant.POSITION_KT_WORDLIST);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.KT_WORDS_EMPTY.getValue(),
					GroupConfigErrorCode.KT_WORDS_EMPTY.getMessage(),
					apiPosition.getPosition(), null);
			return false;
		}

		// 验证kt词的长度
		if (ktItem.getKtWordList().length > CproGroupConstant.KT_KEYWORDS_ALL_MAX_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TARGET_INFO)
					.addParam(GroupConstant.KT_ITEM)
					.addParam(GroupConstant.POSITION_KT_WORDLIST);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.KT_WORDS_MAX.getValue(),
					GroupConfigErrorCode.KT_WORDS_MAX.getMessage(), 
					apiPosition.getPosition(), null);
			return false;
		}
		
		// TRICK HERE: 如果传入的词唯一，切为-1则表示不更新关键词
		boolean isNotUpdateKeyword = false;
		if (ktItem.getKtWordList() != null && ktItem.getKtWordList().length == 1 && ktItem.getKtWordList()[0].getKeyword().equals(GroupConstant.API_KEYWORD_NOT_UPDATE_FLAG)) {
			ktItem.setKtWordList(new KeywordType[]{});
			isNotUpdateKeyword = true;
		}

		Set<String> inputWordSet = new HashSet<String>();
		String REG = "[^a-zA-Z0-9\\.\\+\\#\\/\\ \\-\u4e00-\u9fa5]";
		Pattern pattern = Pattern.compile(REG);

		// 获得去重后的KT词，去除特殊字符
		KeywordType[] ktKeywords = ktItem.getKtWordList();
		for (int i = 0; i < ktKeywords.length; i++) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TARGET_INFO)
					.addParam(GroupConstant.KT_ITEM)
					.addParam(GroupConstant.POSITION_KT_WORDLIST, i);
			
			if (ktKeywords[i] == null) {
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KEYWORD_NULL_ERROR.getValue(),
						GroupConfigErrorCode.KEYWORD_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}
			
			apiPosition.addParam(GroupConstant.KEYWORD);
			String word = ktKeywords[i].getKeyword();
			if (StringUtils.isEmpty(word)) {
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
						GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}

			try {
				word = word.trim();
				int wordLen = com.baidu.beidou.util.StringUtils.getGBKLen(word);
				if (wordLen > 40 || wordLen == 0) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
							GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					return false;
				}
			} catch (Exception e) {
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
						GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}

			Matcher matcher = pattern.matcher(word);
			if (matcher.find()) {
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
						GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}

			word = word.replaceAll("\\s+", " ");
			
			if (inputWordSet.contains(word)) {
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.KT_WORDS_DUP.getValue(),
						GroupConfigErrorCode.KT_WORDS_DUP.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}
			inputWordSet.add(word);
		}
		

		//过滤后的KT词为空
		if (!isNotUpdateKeyword && inputWordSet.size() == 0) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TARGET_INFO)
					.addParam(GroupConstant.KT_ITEM)
					.addParam(GroupConstant.POSITION_KT_WORDLIST);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.KT_WORDS_ERROR.getValue(),
					GroupConfigErrorCode.KT_WORDS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return false;
		}

		
		/**
		 * 存储KT
		 */
		
		// 获得需要新增、需要删除、改为高级匹配、改为短语匹配  的主题词
		List<CproKeyword> toAddList = new ArrayList<CproKeyword>();
		List<CproKeyword> toDelList = new ArrayList<CproKeyword>();
		
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		if(!isNotUpdateKeyword){
			List<CproKeyword> beforeWordList = cproKeywordFacade.getKeywordsByGroup(group.getGroupId(), group.getUserId());
			KTKeywordUtil.filterKeyword(group, visitor, beforeWordList, inputWordSet, toAddList, toDelList, CproKeyword.class);
			
			StringBuilder keywordSb = new StringBuilder("### toAddKeywordNum=[")
					.append(toAddList.size()).append("], toDelKeywordNum=[")
					.append(toDelList.size()).append("], toIMKeywordNum=[");
			log.info(keywordSb);
		}

		// 将要修改的主题词持久化到推广组
		cproKeywordMgr.modTargetTypeToKT(targetType, aliveDays, group, group.getUserId(), visitor, toAddList, toDelList);

		// 为防止主从同步延迟做的缓存
		CproGroup g = new CproGroup();
		try{
			BeanUtils.copyProperties(g, group);
			g.setTargetType(targetType);
			g.setQtAliveDays(aliveDays);
			CacheInterceptor.putCache(CacheInterceptor.GROUP_KEY + g.getGroupId(), g);
			log.info("Add key=[" + CacheInterceptor.GROUP_KEY + g.getGroupId() + " to cache");
		}catch(Exception e){
			log.error("Failed Add key=[" + CacheInterceptor.GROUP_KEY + g.getGroupId() + " to cache" + e.getMessage(), e);
		}
		
		// 记录简单日志
		StringBuilder sb = new StringBuilder("### save KT, size is ").append(
				inputWordSet.size()).append(" userId=").append(
				group.getUserId()).append(", groupId=").append(
				group.getGroupId());
		log.info(sb);
		
		try {
			
			if(!isNotUpdateKeyword){
				// 历史记录 -- 新增/删除/修改关键词
				List<String> beforeWords = CollectionUtils.isEmpty(toAddList)? null :
					new ArrayList<String>(toAddList.size());
				List<String> afterWords = CollectionUtils.isEmpty(toDelList)? null :
					new ArrayList<String>(toDelList.size());
				if(KTKeywordUtil.isNeedToSaveKeywordsHistory(toAddList, toDelList, beforeWords, afterWords)){
					OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_KT_KEYWORD;
					OptContent content = new OptContent(group.getUserId(),
							opType.getOpType(), opType.getOpLevel(),
							group.getGroupId(),
							opType.getTransformer().toDbString(beforeWords),
							opType.getTransformer().toDbString(afterWords));
					optContents.add(content);
				}
			}
			
			// 历史记录 -- 修改行为有效期
			if(!group.getQtAliveDays().equals(aliveDays)){
				OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_KT_COOKIE;
				OptContent content = new OptContent(group.getUserId(),
						opType.getOpType(), opType.getOpLevel(),
						group.getGroupId(),
						opType.getTransformer().toDbString(group.getQtAliveDays()),
						opType.getTransformer().toDbString(aliveDays));
				optContents.add(content);
			}
			
			// 历史记录 -- 修改XT定向方式
			if(TargettypeUtil.hasKT(group.getTargetType()) ^ TargettypeUtil.hasKT(targetType)){
				OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_XT_CHANGE;
				OptContent content = new OptContent(group.getUserId(),
						opType.getOpType(), opType.getOpLevel(),
						group.getGroupId(),
						opType.getTransformer().toDbString(group.getTargetType()),
						opType.getTransformer().toDbString(targetType));
				optContents.add(content);
			} else {
				// 历史记录 -- 修改KT定向方式
				if(TargettypeUtil.getKTType(group.getTargetType()) != TargettypeUtil.getKTType(targetType)){
					OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_KT_TARGETTYPE_CHANGE;
					OptContent content = new OptContent(group.getUserId(),
							opType.getOpType(), opType.getOpLevel(),
							group.getGroupId(),
							opType.getTransformer().toDbString(group.getTargetType()),
							opType.getTransformer().toDbString(targetType));
					optContents.add(content);
				}
			}
		} catch (Exception e) {
			log.error("Failed to contruct opt history content. " + e.getMessage(), e);
		}
		
		return true;
	}
	
	/**
	 * 修改推广组定向方式为非定向
	 * @param result
	 * @param group
	 * @param optContents
	 * @return boolean 是否修改成功
	 */
	public boolean saveNone(ApiResult<Object> result, 
			CproGroup group, 
			List<OptContent> optContents){
		if(group.getTargetType() != CproGroupConstant.GROUP_TARGET_TYPE_NONE){
			int targetType = group.getTargetType();
			//==>准备记录操作历史
			OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_XT_CHANGE;
			OptContent content = new OptContent(group.getUserId(),
					opType.getOpType(), opType.getOpLevel(),
					group.getGroupId(),
					opType.getTransformer().toDbString(group.getTargetType()),
					opType.getTransformer().toDbString(CproGroupConstant.GROUP_TARGET_TYPE_NONE));
			//<==准备记录操作历史
			
			// 删除KT、RT、VT的定向方式
			targetType = TargettypeUtil.clearKT(targetType);
			targetType = TargettypeUtil.clearRT(targetType);
			targetType = TargettypeUtil.clearVT(targetType);
			
			Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
			group.setTargetType(targetType);
			group.setModTime(new Date());
			group.setModUserId(visitor.getUserid());
			cproGroupMgr.modCproGroup(visitor, group);
			
			optContents.add(content);//记录历史
		} 
		return true;
	}
	
	public void saveExcludeKeyword(BaseResponse<PlaceHolderResult> response, 
			Integer groupId,
			List<KeywordType> excludeKeywords,
			List<Integer> excludeKeywordPackIds,
			WordExcludeFacade wordExcludeFacade,
			int userId,
			int opUser) {
		// 将API的BO转换成北斗core的VO
		List<String> keywordList = KeywordType.extractKeywords(excludeKeywords);
		
		// 根据推广组ID取出PlanId
		Integer planId = cproGroupMgr.findPlanIdByGroupId(groupId);
		if (planId == null) {
			throw new RuntimeException("Cannot get planId from groupId=" + groupId);
		}
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		// 设置排除关键词
		if (CollectionUtils.isNotEmpty(excludeKeywords)) {
			APIResult<WordExclude> wordExcludeResult = null;
			try {
				wordExcludeResult = wordExcludeFacade.addOrSetWordExclude(userId, opUser, planId, groupId, keywordList, true, true, optContents);
				if (wordExcludeResult.hasErrors()) {
					Set<Integer> indexSet = wordExcludeResult.getErrors().keySet();
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
					for (Integer index : indexSet) {
						Integer errorCode = wordExcludeResult.getErrors().get(index);
						apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, index);
						if (errorCode.equals(GroupErrorConst.KT_WORDS_PATTERN_TYPE_ERROR.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.EXCLUDE_WORDS_PATTERN_TYPE_ERROR.getValue(),
									GroupConfigErrorCode.EXCLUDE_WORDS_PATTERN_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
						} else if (errorCode.equals(GroupErrorConst.KT_WORDS_ERROR.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.EXCLUDE_WORDS_ERROR.getValue(),
									GroupConfigErrorCode.EXCLUDE_WORDS_ERROR.getMessage(), apiPosition.getPosition(), null);
						} else if (errorCode.equals(GroupErrorConst.EXCLUDE_WORD_OR_PACK_DUP.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.EXCLUDE_WORD_OR_PACK_DUP.getValue(),
									GroupConfigErrorCode.EXCLUDE_WORD_OR_PACK_DUP.getMessage(), apiPosition.getPosition(), null);
						} 
						return;
					}
				}
			} catch (ExcludeException e) {
				if (e.getMessage().equals(ExcludeConstant.ADD_EXCLUDE_WORD_PARAM_MISS_MSG)) {
					throw new RuntimeException("Param error");
				} else if (e.getMessage().equals(ExcludeConstant.EXCLUDE_WORD_COUNT_EXCEED)) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GroupConfigErrorCode.EXCLUDE_KEYWORDS_EXCEED_LIMIT.getValue(),
							GroupConfigErrorCode.EXCLUDE_KEYWORDS_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
					return;
				}
			}
		}
		
		// 设置排除关键词组合
		if (CollectionUtils.isNotEmpty(excludeKeywordPackIds)) {
			APIResult<WordPackExclude> wordPackExcludeResult = null;
			try {
				wordPackExcludeResult = wordExcludeFacade.addOrSetWordExcludePack(userId, opUser, planId, groupId, excludeKeywordPackIds, true, true, optContents);
				if (wordPackExcludeResult.hasErrors()) {
					Set<Integer> indexSet = wordPackExcludeResult.getErrors().keySet();
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
					for (Integer index : indexSet) {
						Integer errorCode = wordPackExcludeResult.getErrors().get(index);
						apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD_PACKIDS, index);
						if (errorCode.equals(GroupErrorConst.EXCLUDE_WORD_PACK_NOT_EXIST.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_NOT_EXIST.getValue(),
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_NOT_EXIST.getMessage(), apiPosition.getPosition(), null);
						} else if (errorCode.equals(GroupErrorConst.EXCLUDE_WORD_PACK_RELATED.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_RELATED.getValue(),
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_RELATED.getMessage(), apiPosition.getPosition(), null);
						} else if (errorCode.equals(GroupErrorConst.EXCLUDE_WORD_PACK_TARGET_TYPE_INVALID.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_TARGET_TYPE_INVALID.getValue(),
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_TARGET_TYPE_INVALID.getMessage(), apiPosition.getPosition(), null);
						} else if (errorCode.equals(GroupErrorConst.EXCLUDE_WORD_PACK_HAS_BEEN_OPTIMIZED.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_HAS_BEEN_OPTIMIZED.getValue(),
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_HAS_BEEN_OPTIMIZED.getMessage(), apiPosition.getPosition(), null);
						} 
						return;
					}
				}
			} catch (ExcludeException e) {
				if (e.getMessage().equals(ExcludeConstant.ADD_EXCLUDE_WORD_PARAM_MISS_MSG)) {
					throw new RuntimeException("Param error");
				}
			}
		}
		
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
	}
	
	public void saveExcludePeople(BaseResponse<PlaceHolderResult> response, 
			Integer groupId,
			List<Long> excludePids,
			VtPeopleMgr vtPeopleMgr, 
			CproGroupVTMgr cproGroupVTMgr, 
			int userId,
			int opUser) {
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		groupExcludePeopleFacade.addOrSetExcludePeople(response, groupId, excludePids, null, userId, opUser, false, true, true, optContents);
		
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
	}
	
	public void savePackInfo(BaseResponse<PlaceHolderResult>response, 
			Integer groupId,
			List<PackItemType> packs,
			GroupPackFacade groupPackFacade,
			int userId,
			int opUser) {
		// 将API的BO转换成北斗core的VO
		List<GroupPackVo> packVoList = GroupBoMappingUtil.transPackItemType2GroupPackVo(packs);
		
		// 根据推广组ID取出PlanId
		CproGroup group = cproGroupMgr.findCproGroupById(groupId);
		Integer planId = group.getPlanId();
		if (planId == null) {
			throw new RuntimeException("Cannot get planId from groupId=" + groupId);
		}
		
		// 推广组targetType不支持受众组合时不能设置组合
		int targetType = group.getTargetType();
		if (!TargettypeUtil.isPack(targetType)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.PACK_INFO);
			apiPosition.addParam(GroupConstant.GROUPID);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.GROUP_TARGETTYPE_NOT_SUPPORT_PACK_CONFIG.getValue(),
					GroupConfigErrorCode.GROUP_TARGETTYPE_NOT_SUPPORT_PACK_CONFIG.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		// 设置推广组受众组合
		if (CollectionUtils.isNotEmpty(packVoList)) {
			APIResult<GroupPack> groupPackResult = null;
			try {
				groupPackResult = groupPackFacade.addOrSetGroupPack4API(userId, opUser, planId, groupId, packVoList, true, true, optContents);
				if (groupPackResult.hasErrors()) {
					Set<Integer> indexSet = groupPackResult.getErrors().keySet();
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.PACK_INFO);
					for (Integer index : indexSet) {
						Integer errorCode = groupPackResult.getErrors().get(index);
						apiPosition.addParam(GroupConstant.PACK_ITEMS, index);
						if (errorCode.equals(GroupErrorConst.GROUP_PACK_NOT_EXIST_OR_OPTIMIZED.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.GROUP_PACK_NOT_EXIST_OR_TYPE_WRONG_OR_OPTIMIZED.getValue(),
									GroupConfigErrorCode.GROUP_PACK_NOT_EXIST_OR_TYPE_WRONG_OR_OPTIMIZED.getMessage(), apiPosition.getPosition(), null);
						} else if (errorCode.equals(GroupErrorConst.GROUP_PACK_HAS_EXCLUDED.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.GROUP_PACK_HAS_EXCLUDED.getValue(),
									GroupConfigErrorCode.GROUP_PACK_HAS_EXCLUDED.getMessage(), apiPosition.getPosition(), null);
						} else if (errorCode.equals(GroupErrorConst.GROUP_PACK_TYPE_WRONG.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.GROUP_PACK_TYPE_WRONG.getValue(),
									GroupConfigErrorCode.GROUP_PACK_TYPE_WRONG.getMessage(), apiPosition.getPosition(), null);
						} else if (errorCode.equals(GroupErrorConst.GROUP_PACK_DUPLICATE.getValue())) {
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.GROUP_PACK_DUPLICATE.getValue(),
									GroupConfigErrorCode.GROUP_PACK_DUPLICATE.getMessage(), apiPosition.getPosition(), null);
						}
						return;
					}
				}
			} catch (GroupPackException e) {
				if (e.getMessage().equals(GroupPackConstant.ADD_GROUP_PACK_EXCEED_LIMIT_MSG)) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.PACK_INFO);
					apiPosition.addParam(GroupConstant.PACK_ITEMS);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GroupConfigErrorCode.GROUP_PACK_EXCEED_LIMIT.getValue(),
							GroupConfigErrorCode.GROUP_PACK_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
					return;
				} else if (e.getMessage().equals(GroupPackConstant.ADD_GROUP_KEYWORD_PACK_EXCEED_LIMIT_MSG)) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.PACK_INFO);
					apiPosition.addParam(GroupConstant.PACK_ITEMS);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GroupConfigErrorCode.GROUP_KEYWORD_PACK_EXCEED_LIMIT.getValue(),
							GroupConfigErrorCode.GROUP_KEYWORD_PACK_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
					return;
				} else if (e.getMessage().equals(GroupPackConstant.ADD_GROUP_ADVANCED_PACK_EXCEED_LIMIT_MSG)) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.PACK_INFO);
					apiPosition.addParam(GroupConstant.PACK_ITEMS);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GroupConfigErrorCode.GROUP_ADVANCED_PACK_EXCEED_LIMIT.getValue(),
							GroupConfigErrorCode.GROUP_ADVANCED_PACK_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
					return;
				} 
			} catch (Exception e) {
				log.error("Failed to set group pack info. " + e.getMessage(), e);
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PACK_INFO);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GroupConfigErrorCode.GROUP_PACK_CONFIG_FAILED.getValue(),
						GroupConfigErrorCode.GROUP_PACK_CONFIG_FAILED.getMessage(), apiPosition.getPosition(), null);
				return;
			}
		}
		
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
	}
	
	public void savePrice(BaseResponse<PlaceHolderResult> response, 
			List<PriceType> prices,
			int userId,
			int opUser) {
		groupPriceFacade.setPrice(response, prices, userId, opUser);
	}
	
	public void setGroupSiteConfigMgr(GroupSiteConfigMgr groupSiteConfigMgr) {
		this.groupSiteConfigMgr = groupSiteConfigMgr;
	}

	public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
		this.cproKeywordMgr = cproKeywordMgr;
	}
	
	public void setCproKeywordFacade(CproKeywordFacade cproKeywordFacade) {
		this.cproKeywordFacade = cproKeywordFacade;
	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public CproKeywordFacade getCproKeywordFacade() {
		return cproKeywordFacade;
	}

	public CproKeywordMgr getCproKeywordMgr() {
		return cproKeywordMgr;
	}

	public GroupSiteConfigMgr getGroupSiteConfigMgr() {
		return groupSiteConfigMgr;
	}

	public GroupExcludePeopleFacade getGroupExcludePeopleFacade() {
		return groupExcludePeopleFacade;
	}

	public void setGroupExcludePeopleFacade(
			GroupExcludePeopleFacade groupExcludePeopleFacade) {
		this.groupExcludePeopleFacade = groupExcludePeopleFacade;
	}

	public GroupPriceFacade getGroupPriceFacade() {
		return groupPriceFacade;
	}

	public void setGroupPriceFacade(GroupPriceFacade groupPriceFacade) {
		this.groupPriceFacade = groupPriceFacade;
	}
	
	
	
}
