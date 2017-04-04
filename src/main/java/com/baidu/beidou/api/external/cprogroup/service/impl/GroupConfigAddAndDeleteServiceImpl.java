package com.baidu.beidou.api.external.cprogroup.service.impl;

import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.error.GroupErrorCode;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigValidator;
import com.baidu.beidou.api.external.cprogroup.facade.GroupExcludePeopleFacade;
import com.baidu.beidou.api.external.cprogroup.service.GroupConfigAddAndDeleteService;
import com.baidu.beidou.api.external.cprogroup.service.GroupConfigBaseService;
import com.baidu.beidou.api.external.cprogroup.util.GroupBoMappingUtil;
import com.baidu.beidou.api.external.cprogroup.util.GroupSiteUtil;
import com.baidu.beidou.api.external.cprogroup.util.ItUtils;
import com.baidu.beidou.api.external.cprogroup.vo.AttachInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.AttachSubUrlItemType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupAttachInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeAppType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeIpType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeKeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeSiteType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupInterestInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupKeywordItemType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupPackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSiteType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupTradePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupTradeType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupVtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.RegionConfigType;
import com.baidu.beidou.api.external.cprogroup.vo.RegionItemType;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupExcludeIpItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupExcludeSiteItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupItItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupKtItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupRegionItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupSiteItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupSitePriceItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupTradeItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupTradePriceItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupVtItem;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.IndexMapperUtil;
import com.baidu.beidou.api.external.util.ListArrayUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.ErrorParam;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.IndexMapper;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.cprogroup.bo.AppExclude;
import com.baidu.beidou.cprogroup.bo.AttachInfo;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproGroupIT;
import com.baidu.beidou.cprogroup.bo.CproGroupITExclude;
import com.baidu.beidou.cprogroup.bo.CproGroupInfo;
import com.baidu.beidou.cprogroup.bo.CproGroupVT;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.cprogroup.bo.CustomInterest;
import com.baidu.beidou.cprogroup.bo.GroupIpFilter;
import com.baidu.beidou.cprogroup.bo.GroupPack;
import com.baidu.beidou.cprogroup.bo.GroupSiteFilter;
import com.baidu.beidou.cprogroup.bo.GroupSitePrice;
import com.baidu.beidou.cprogroup.bo.GroupTradePrice;
import com.baidu.beidou.cprogroup.bo.Interest;
import com.baidu.beidou.cprogroup.bo.VtPeople;
import com.baidu.beidou.cprogroup.bo.WordExclude;
import com.baidu.beidou.cprogroup.bo.WordPackExclude;
import com.baidu.beidou.cprogroup.constant.AttachInfoConstant;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.ExcludeConstant;
import com.baidu.beidou.cprogroup.constant.GroupPackConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.dao.CproGroupVTDao;
import com.baidu.beidou.cprogroup.dao.GroupTradePriceDao;
import com.baidu.beidou.cprogroup.error.GroupErrorConst;
import com.baidu.beidou.cprogroup.exception.ExcludeException;
import com.baidu.beidou.cprogroup.exception.GroupPackException;
import com.baidu.beidou.cprogroup.facade.AppExcludeFacade;
import com.baidu.beidou.cprogroup.facade.CproITFacade;
import com.baidu.beidou.cprogroup.facade.CproKeywordFacade;
import com.baidu.beidou.cprogroup.facade.GroupPackFacade;
import com.baidu.beidou.cprogroup.facade.WordExcludeFacade;
import com.baidu.beidou.cprogroup.service.AppExcludeMgr;
import com.baidu.beidou.cprogroup.service.AttachInfoMgr;
import com.baidu.beidou.cprogroup.service.CproGroupITMgr;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproGroupVTMgr;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.cprogroup.service.CustomITMgr;
import com.baidu.beidou.cprogroup.service.GroupAttachInfoMgr;
import com.baidu.beidou.cprogroup.service.GroupSiteConfigMgr;
import com.baidu.beidou.cprogroup.service.InterestMgr;
import com.baidu.beidou.cprogroup.service.UnionSiteCalculator;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.cprogroup.util.AttachInfoSubUrlError;
import com.baidu.beidou.cprogroup.util.AttachInfoUtil;
import com.baidu.beidou.cprogroup.util.ITUtils;
import com.baidu.beidou.cprogroup.util.KTKeywordUtil;
import com.baidu.beidou.cprogroup.util.RegionIdConverter;
import com.baidu.beidou.cprogroup.util.TargettypeUtil;
import com.baidu.beidou.cprogroup.vo.AppInfo;
import com.baidu.beidou.cprogroup.vo.AttachInfoVo;
import com.baidu.beidou.cprogroup.vo.BDSiteInfo;
import com.baidu.beidou.cprogroup.vo.BDSiteLiteInfo;
import com.baidu.beidou.cprogroup.vo.CproGroupVTVo;
import com.baidu.beidou.cprogroup.vo.CustomInterestVo;
import com.baidu.beidou.cprogroup.vo.GroupPackVo;
import com.baidu.beidou.cprogroup.vo.GroupRegionOptVo;
import com.baidu.beidou.cprogroup.vo.GroupSiteOptVo;
import com.baidu.beidou.cprogroup.vo.GroupTradeSitePriceOptVo;
import com.baidu.beidou.cprogroup.vo.InterestCacheObject;
import com.baidu.beidou.cprogroup.vo.SiteSumInfo;
import com.baidu.beidou.cprogroup.vo.TradeInfo;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.ubmcdriver.constant.UbmcConstant;
import com.baidu.beidou.cprounit.validate.UnitAkaAudit;
import com.baidu.beidou.pack.constant.PackTypeConstant;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.user.service.UserInfoMgr;
import com.baidu.beidou.util.LogUtils;
import com.baidu.beidou.util.OperationHistoryUtils;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.bridgedriver.bo.BridgeResult;
import com.baidu.beidou.util.bridgedriver.constant.BridgeConstant;
import com.baidu.beidou.util.bridgedriver.service.BridgeService;
import com.baidu.beidou.util.sequence.constant.SequenceConstant;
import com.baidu.beidou.util.sequence.service.SequenceDriver;
import com.baidu.beidou.util.vo.APIResult;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.sun.base.BaseResponseOptions;
import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;

import static com.baidu.beidou.cprogroup.constant.UnionSiteCache.sysRegCache;

public class GroupConfigAddAndDeleteServiceImpl extends GroupConfigBaseService
		implements GroupConfigAddAndDeleteService {
	private CproGroupMgr cproGroupMgr;
	private CproKeywordFacade cproKeywordFacade;
	private CproKeywordMgr cproKeywordMgr;
	private VtPeopleMgr vtPeopleMgr;
	private CproGroupVTMgr cproGroupVTMgr;
	private CproGroupVTDao cproGroupVTDao;
	private CproITFacade cproITFacade;
	private CproGroupITMgr cproGroupITMgr;
	private InterestMgr interestMgr;
	private CustomITMgr customITMgr;
	private GroupSiteConfigMgr groupSiteConfigMgr;
	private GroupConfigValidator groupConfigValidator;
	private GroupTradePriceDao groupTradePriceDao;
	private WordExcludeFacade wordExcludeFacade;
	private GroupExcludePeopleFacade groupExcludePeopleFacade;
	private GroupPackFacade groupPackFacade;
	private SequenceDriver sequenceDriver;
	private AppExcludeFacade appExcludeFacade;
	private AppExcludeMgr appExcludeMgr;
	private UserInfoMgr userInfoMgr;
	private GroupAttachInfoMgr groupAttachInfoMgr;
	private BridgeService bridgeService;
	private AttachInfoMgr attachInfoMgr;
	private CproUnitMgr unitMgr;

	private static final Log log = LogFactory.getLog(GroupConfigAddAndDeleteServiceImpl.class);
	
	public ApiResult<Object> addKeyword(ApiResult<Object> result,
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
			
			if (item.getGroupId() < 1) {
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
			
			List<GroupKtItem> groupKeywordList = groupKeywords.get(groupId);
			if (groupKeywordList == null) {
				groupKeywordList = new ArrayList<GroupKtItem>();
				groupKeywords.put(groupId, groupKeywordList);
			}

			groupKeywordList.add(new GroupKtItem(index, item.getKeyword(), item.getPattern()));
		}

		// 验证待添加关键词推广组的个数
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
		
		// 依照推广组添加关键词
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
			Set<String> dbKeywordsSet = new HashSet<String>();
			List<CproKeyword> dbKeywords = cproKeywordMgr.findByGroupId(groupId.intValue(), bdUser.getUserid());
			for(CproKeyword keyword: dbKeywords){
				dbKeywordsSet.add(keyword.getKeyword());
			}
			
			Set<String> inputWordSet = new HashSet<String>();

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
				
				if(dbKeywordsSet.contains(word)){
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KT_WORD_ALREADY_EXISTS.getValue(),
							GroupConfigErrorCode.KT_WORD_ALREADY_EXISTS.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				
				if (inputWordSet.contains(word)) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KT_WORDS_DUP.getValue(),
							GroupConfigErrorCode.KT_WORDS_DUP.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				inputWordSet.add(word);
			}

			if (inputWordSet.size() > 0) {
				// 关键词总数超出限制
				if (inputWordSet.size() + dbKeywords.size() 
						> CproGroupConstant.KT_KEYWORDS_ALL_MAX_NUM) {
					for (GroupKtItem item : groupKeywordList) {
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(GroupConstant.KEYWORDS, item.getIndex());
						apiPosition.addParam(GroupConstant.GROUPID);
						
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.KT_WORDS_MAX.getValue(),
								GroupConfigErrorCode.KT_WORDS_MAX.getMessage(), 
								apiPosition.getPosition(), null);
					}
					continue;
				}
				
				
				// 获得需要新增、需要删除、改为高级匹配、改为短语匹配  的关键词
				List<CproKeyword> toAddList = new ArrayList<CproKeyword>();
				List<CproKeyword> toDelList = new ArrayList<CproKeyword>();
				
				KTKeywordUtil.filterKeyword(group, visitor, dbKeywords, inputWordSet, toAddList, toDelList, CproKeyword.class);
				
				StringBuilder keywordSb = new StringBuilder("### toAddKeywordNum=[")
						.append(toAddList.size()).append("], toDelKeywordNum=[")
						.append(toDelList.size()).append("]");
				log.info(keywordSb);
				
				// 将新增关键词添加到推广组
				int succ = cproKeywordMgr.modifyKeywords(groupId.intValue(), bdUser.getUserid(), visitor, toAddList, null);
				pay.increSuccess(inputWordSet.size());
				
				// 记录简单日志
				StringBuilder sb = new StringBuilder("### add kt keywords, size is ")
						.append(inputWordSet.size()).append(", userId=")
						.append(bdUser.getUserid()).append(", groupId=").append(groupId);
				log.info(sb);
				
				//------- 保存历史操作记录 start ------
				try{
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
								opType.getTransformer().toDbString(new ArrayList<String>())); //添加关键词历史操作记录afterWords应为空
						optContents.add(content);
					}
				} catch(Exception e){
					log.error("Failed to contruct opt history content. " + e.getMessage(), e);
				}
				//------- 保存历史操作记录 end ------

			} else {
				continue;
			}

		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
    /**
     * 更新附加信息（包括新增逻辑）
     * 
     * @param response 返回的信息
     * @param attachInfos 传入附加信息
     * @param visitor 访客
     * @param userId 用户id
     */
    public void updateAttachInfo(BaseResponse<PlaceHolderResult> response, List<GroupAttachInfoType> attachInfos,
            Visitor visitor, int userId) {
        if (CollectionUtils.isEmpty(attachInfos)) {
            return;
        }

        BaseResponseOptions options = response.getOptions();

        int index = 0;
        for (GroupAttachInfoType type : attachInfos) {
            // 验证请求中一个推广组中同类型附加信息是否只有一个List<GroupAttachInfoType> attachInfos
            List<Integer> infoTypes = new ArrayList<Integer>(10);
            response = validator(response, userId, index, type, infoTypes);

            if (response.getErrors() != null && CollectionUtils.isNotEmpty(response.getErrors())) {
                return;
            }

            List<AttachInfo> existInfo = attachInfoMgr.getAttachInfoByGroupId(type.getGroupId());
            List<AttachInfoType> requestInfo = type.getAttachTypes();

            List<AttachInfoVo> add = new ArrayList<AttachInfoVo>();
            List<AttachInfoVo> update = new ArrayList<AttachInfoVo>();

            fillAttachInfo(existInfo, requestInfo, add, update);

            CproGroup group = cproGroupMgr.findCproGroupById(type.getGroupId());

            if (!CollectionUtils.isEmpty(add)) {
                groupAttachInfoMgr.addAttachInfo(group, add, visitor, true);
            }

            StopWatch watch = new StopWatch();
            watch.start();
            if (!CollectionUtils.isEmpty(update)) {
                List<Integer> groupIdList = new ArrayList<Integer>();
                groupIdList.add(group.getGroupId());
                for (AttachInfoVo info : update) {
                    if (info.getAttachType() == AttachInfoConstant.ATTACH_INFO_PHONE) {
                        groupAttachInfoMgr.modifyPhone(groupIdList, info.getAttachContent(), visitor, userId);
                    }

                    if (info.getAttachType() == AttachInfoConstant.ATTACH_INFO_MESSAGE) {
                        groupAttachInfoMgr.modifyMessage(groupIdList, info.getAttachContent(), info.getAttachMessage(),
                                visitor, userId);
                    }
                    
                    if (info.getAttachType() == AttachInfoConstant.ATTACH_INFO_SUB_URL) {
                        groupAttachInfoMgr.modifySubUrl(groupIdList, info.getAttachSubUrlParam(),
                                info.getAttachSubUrlTitle(), info.getAttachSubUrlLink(),
                                info.getAttachSubUrlWirelessLink(), visitor, userId);
                    }
                }
            }

            Map<Integer, Integer> existStatusMap = getExistStatus(type.getGroupId());
            update.addAll(add);
            updatePhoneEnableOrDisable(update, group.getGroupId(), existStatusMap, visitor, userId);
            updateMessageEnableOrDisable(update, group.getGroupId(), existStatusMap, visitor, userId);
            updateConsultEnableOrDisable(update, group.getGroupId(), existStatusMap, visitor, userId);
            updateSubUrlEnableOrDisable(update, group.getGroupId(), existStatusMap, visitor, userId);

            watch.stop();

            log.info("core update use " + watch.getTime() + "ms");
            index++;

            options.setSuccess(options.getSuccess() + 1);
        }

    }

    /**
     * 校验每个推广组传入的附加信息是否合法
     * 
     * @param response 返回的信息
     * @param userId 用户id
     * @param index 索引
     * @param type 前段传入参数
     * @param infoTypes 用户判断类型唯一
     * @return BaseResponse<PlaceHolderResult>
     */
    private BaseResponse<PlaceHolderResult> validator(BaseResponse<PlaceHolderResult> response, int userId, int index,
            GroupAttachInfoType type, List<Integer> infoTypes) {
        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
        apiPosition.addParam(GroupConstant.POSITION_ATTACH_INFO, index);
        for (int attachInfoTypeIdx = 0; attachInfoTypeIdx < type.getAttachTypes().size(); attachInfoTypeIdx++) {
            AttachInfoType attachInfo = type.getAttachTypes().get(attachInfoTypeIdx);
            apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE_LIST, attachInfoTypeIdx);
            if (infoTypes.contains(attachInfo.getAttachType())) {
                apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                response =
                        DRAPIMountAPIBeanUtils.addApiError(response,
                                GroupConfigErrorCode.ATTACH_TYPE_IN_ONE_GROUP_DUP.getValue(),
                                GroupConfigErrorCode.ATTACH_TYPE_IN_ONE_GROUP_DUP.getMessage(),
                                apiPosition.getPosition(), null);
                break;
            } else {
                infoTypes.add(attachInfo.getAttachType());
            }

            // 验证type是否正确
            if (!AttachInfoUtil.isValidAttachType(attachInfo.getAttachType())) {
                apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                response =
                        DRAPIMountAPIBeanUtils.addApiError(response,
                                GroupConfigErrorCode.ATTACH_TYPE_INVALID.getValue(),
                                GroupConfigErrorCode.ATTACH_TYPE_INVALID.getMessage(), apiPosition.getPosition(), null);
                break;
            }

            // 验证子链是否符合规范
            if (attachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_SUB_URL) {
                boolean subUrlParamError = false;
                // 1.验证子链的统计参数
                switch (AttachInfoUtil.validateSubUrlParam(attachInfo.getAttachSubUrlParam())) {
                    case AttachInfoUtil.SUB_URL_ERROR_CODE_OK:
                        break;
                    case AttachInfoUtil.SUB_URL_ERROR_CODE_FIRST_CHAR:
                        apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_PARAM);
                        response =
                                DRAPIMountAPIBeanUtils.addApiError(response,
                                        GroupConfigErrorCode.ATTACH_SUBURL_PARAM_INVALID.getValue(),
                                        GroupConfigErrorCode.ATTACH_SUBURL_PARAM_INVALID.getMessage(),
                                        apiPosition.getPosition(), null);
                        subUrlParamError = true;
                        break;
                    case AttachInfoUtil.SUB_URL_ERROR_CODE_EXCEED_LENGTH:
                        apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_PARAM);
                        response =
                                DRAPIMountAPIBeanUtils.addApiError(response,
                                        GroupConfigErrorCode.ATTACH_SUBURL_PARAM_EXCEED_LIMIT.getValue(),
                                        GroupConfigErrorCode.ATTACH_SUBURL_PARAM_EXCEED_LIMIT.getMessage(),
                                        apiPosition.getPosition(), null);
                        subUrlParamError = true;
                        break;
                    case AttachInfoUtil.COS_SUB_URL_NAME_ERROR_CODE_HAS_SPECIAL_CHAR:
                        apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_PARAM);
                        response =
                                DRAPIMountAPIBeanUtils.addApiError(response,
                                        GroupConfigErrorCode.ATTACH_SUBURL_PARAM_INVALID_CHAR.getValue(),
                                        GroupConfigErrorCode.ATTACH_SUBURL_PARAM_INVALID_CHAR.getMessage(),
                                        apiPosition.getPosition(), null);
                        subUrlParamError = true;
                        break;
                    default:
                        break;
                }
                if (subUrlParamError) {
                    break; // 子链的统计参数校验出错，跳过后面的检查
                }

                List<AttachSubUrlItemType> subUrlItems = attachInfo.getAttachSubUrls();
                if (subUrlItems == null || subUrlItems.size() == 0) {
                    // 2.验证动态子链
                    continue;
                } else {
                    // 3.验证静态子链
                    if (!(subUrlItems.size() >= AttachInfoConstant.MIN_SUB_URL_NUM 
                            && subUrlItems.size() <= AttachInfoConstant.MAX_SUB_URL_NUM)) {
                        apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_LIST);
                        response =
                                DRAPIMountAPIBeanUtils.addApiError(response,
                                        GroupConfigErrorCode.ATTACH_SUBURL_EXCEED_LIMIT.getValue(),
                                        GroupConfigErrorCode.ATTACH_SUBURL_EXCEED_LIMIT.getMessage(),
                                        apiPosition.getPosition(), null);
                        break;
                    }

                    boolean subUrlError = false;
                    for (int attachSubUrlItemTypeIdx = 0; 
                            attachSubUrlItemTypeIdx < subUrlItems.size(); 
                            attachSubUrlItemTypeIdx++) {
                        AttachSubUrlItemType item = subUrlItems.get(attachSubUrlItemTypeIdx);
                        apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_LIST, attachSubUrlItemTypeIdx);
                        // 3.1验证子链标题
                        if (AttachInfoUtil.validateSublinkName(item.getAttachSubUrlTitle())  
                                == AttachInfoUtil.COS_SUB_URL_NAME_ERROR_CODE_EMPTY) {
                            apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_TITLE);
                            response =
                                    DRAPIMountAPIBeanUtils.addApiError(response,
                                            GroupConfigErrorCode.ATTACH_SUBURL_TITLE_EMPTY.getValue(),
                                            GroupConfigErrorCode.ATTACH_SUBURL_TITLE_EMPTY.getMessage(),
                                            apiPosition.getPosition(), null);
                            subUrlError = true;
                            break;
                        } else if (AttachInfoUtil.validateSublinkName(item.getAttachSubUrlTitle())  
                                == AttachInfoUtil.COS_SUB_URL_NAME_ERROR_CODE_EXCEED_LENGTH) {
                            apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_TITLE);
                            response =
                                    DRAPIMountAPIBeanUtils.addApiError(response,
                                            GroupConfigErrorCode.ATTACH_SUBURL_TITLE_EXCEED_LIMIT.getValue(),
                                            GroupConfigErrorCode.ATTACH_SUBURL_TITLE_EXCEED_LIMIT.getMessage(),
                                            apiPosition.getPosition(), null);
                            subUrlError = true;
                            break;
                        } else if (AttachInfoUtil.validateSublinkName(item.getAttachSubUrlTitle())  
                                == AttachInfoUtil.COS_SUB_URL_NAME_ERROR_CODE_HAS_SPECIAL_CHAR) {
                            apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_TITLE);
                            response =
                                    DRAPIMountAPIBeanUtils.addApiError(response,
                                            GroupConfigErrorCode.ATTACH_SUBURL_TITLE_INVALID.getValue(),
                                            GroupConfigErrorCode.ATTACH_SUBURL_TITLE_INVALID.getMessage(),
                                            apiPosition.getPosition(), null);
                            subUrlError = true;
                            break;
                        }

                        // 3.1验证子链和移动子链url
                        AttachInfoSubUrlError attachInfoSubUrlError =
                                AttachInfoUtil.validateSublinkURL(item.getAttachSubUrlLink(),
                                        item.getAttachSubUrlWirelessLink());
                        if (attachInfoSubUrlError.getErrorCode() == AttachInfoUtil.COS_SUB_URL_URL_ERROR_CODE_EMPTY) {
                            if (attachInfoSubUrlError.getSublinkUrlVal()
                                    .equals(AttachInfoUtil.COS_SUB_URL_PARAM_LINK)) {
                                apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_LINK);
                            } else {
                                apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_WIRELESS_LINK);
                            }
                            response =
                                    DRAPIMountAPIBeanUtils.addApiError(response,
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_EMPTY.getValue(),
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_EMPTY.getMessage(),
                                            apiPosition.getPosition(), null);
                            subUrlError = true;
                            break;
                        } else if (attachInfoSubUrlError.getErrorCode()  
                                == AttachInfoUtil.COS_SUB_URL_URL_ERROR_CODE_EXCEED_LENGTH) {
                            if (attachInfoSubUrlError.getSublinkUrlVal().equals(item.getAttachSubUrlLink())) {
                                apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_LINK);
                            } else {
                                apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_WIRELESS_LINK);
                            }
                            response =
                                    DRAPIMountAPIBeanUtils.addApiError(response,
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_EXCEED_LIMIT.getValue(),
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_EXCEED_LIMIT.getMessage(),
                                            apiPosition.getPosition(), null);
                            subUrlError = true;
                            break;
                        } else if (attachInfoSubUrlError.getErrorCode()  
                                == AttachInfoUtil.COS_SUB_URL_URL_ERROR_CODE_NOT_URL) {
                            if (attachInfoSubUrlError.getSublinkUrlVal().equals(item.getAttachSubUrlLink())) {
                                apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_LINK);
                            } else {
                                apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_WIRELESS_LINK);
                            }
                            response =
                                    DRAPIMountAPIBeanUtils.addApiError(response,
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_INVALID.getValue(),
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_INVALID.getMessage(),
                                            apiPosition.getPosition(), null);
                            subUrlError = true;
                            break;
                        } else if (attachInfoSubUrlError.getErrorCode()  
                                == AttachInfoUtil.COS_SUB_URL_URL_ERROR_CODE_HAS_SPECIAL_CHAR) {
                            if (attachInfoSubUrlError.getSublinkUrlVal().equals(item.getAttachSubUrlLink())) {
                                apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_LINK);
                            } else {
                                apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_WIRELESS_LINK);
                            }
                            response =
                                    DRAPIMountAPIBeanUtils.addApiError(response,
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_INVALID_CHAR.getValue(),
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_INVALID_CHAR.getMessage(),
                                            apiPosition.getPosition(), null);
                            subUrlError = true;
                            break;
                        }

                        if (StringUtils.isNotEmpty(item.getAttachSubUrlLink())
                                && !unitMgr.isBindingUrl(item.getAttachSubUrlLink(), userId)) {
                            apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_LINK);
                            response =
                                    DRAPIMountAPIBeanUtils.addApiError(response,
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_BIND_SITE.getValue(),
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_BIND_SITE.getMessage(),
                                            apiPosition.getPosition(), null);
                            subUrlError = true;
                            break;
                        }

                        if (StringUtils.isNotEmpty(item.getAttachSubUrlWirelessLink())
                                && !unitMgr.isBindingUrl(item.getAttachSubUrlWirelessLink(), userId)) {
                            apiPosition.addParam(GroupConstant.POSITION_ATTACH_SUB_URL_WIRELESS_LINK);
                            response =
                                    DRAPIMountAPIBeanUtils.addApiError(response,
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_BIND_SITE.getValue(),
                                            GroupConfigErrorCode.ATTACH_SUBURL_LINK_BIND_SITE.getMessage(),
                                            apiPosition.getPosition(), null);
                            subUrlError = true;
                            break;
                        }

                    }

                    if (subUrlError) {
                        break; // 校验静态子链出错时，跳过后面的校验
                    }

                }

            }
            
            // 验证电话号码是否符合规范
            if (attachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_PHONE
                    && !AttachInfoUtil.validatePhone(attachInfo.getAttachContent())) {
                apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                response =
                        DRAPIMountAPIBeanUtils
                                .addApiError(response, GroupConfigErrorCode.ATTACH_PHONE_INVALID.getValue(),
                                        GroupConfigErrorCode.ATTACH_PHONE_INVALID.getMessage(),
                                        apiPosition.getPosition(), null);
                break;
            }

            // 如果是短信，需校验电话号码5-20
            if (attachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_MESSAGE
                    && !AttachInfoUtil.validateMessagePhone(attachInfo.getAttachContent())) {
                apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                response =
                        DRAPIMountAPIBeanUtils.addApiError(response,
                                GroupConfigErrorCode.ATTACH_MESSAGE_PHONE_INVALID.getValue(),
                                GroupConfigErrorCode.ATTACH_MESSAGE_PHONE_INVALID.getMessage(),
                                apiPosition.getPosition(), null);
                break;
            }

            // 验证消息长度是否合法
            if (attachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_MESSAGE) {
                if (attachInfo.getAttachMessage() == null) {
                    attachInfo.setAttachMessage("");
                }
                int messageError = AttachInfoUtil.validateMessage(attachInfo.getAttachMessage());
                if (messageError == AttachInfoUtil.MESSAGE_ERROR_CODE_EXCEED_LENGTH) {
                    apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                    response =
                            DRAPIMountAPIBeanUtils.addApiError(response,
                                    GroupConfigErrorCode.ATTACH_MESSAGE_LENGTH_INVALID.getValue(),
                                    GroupConfigErrorCode.ATTACH_MESSAGE_LENGTH_INVALID.getMessage(),
                                    apiPosition.getPosition(), null);
                    break;
                }
            }

            // 验证消息内容是否合法
            if (attachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_MESSAGE) {
                int messageError = AttachInfoUtil.validateMessage(attachInfo.getAttachMessage());
                if (messageError != AttachInfoUtil.MESSAGE_ERROR_CODE_OK) {
                    apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                    response =
                            DRAPIMountAPIBeanUtils.addApiError(response,
                                    GroupConfigErrorCode.ATTACH_MESSAGE_INVALID.getValue(),
                                    GroupConfigErrorCode.ATTACH_MESSAGE_INVALID.getMessage(),
                                    apiPosition.getPosition(), null);
                    break;
                }
            }

            // 验证消息内容是否通过aka校验
            if (attachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_MESSAGE
                    && StringUtils.isNotEmpty(UnitAkaAudit.akaAuditMessage(attachInfo.getAttachMessage(), userId))) {
                apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                response =
                        DRAPIMountAPIBeanUtils.addApiError(response,
                                GroupConfigErrorCode.ATTACH_MESSAGE_INVALID.getValue(),
                                GroupConfigErrorCode.ATTACH_MESSAGE_INVALID.getMessage(), apiPosition.getPosition(),
                                null);
                break;
            }

            // 如果是咨询需验证用户是否开通商桥
            if (attachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_CONSULT) {
                BridgeResult bridgeResult = bridgeService.getUserInfo(userId);
                boolean bridgeUser = false;
                if (bridgeResult != null && bridgeResult.getStatus() == BridgeConstant.STATUS_OK) {
                    bridgeUser = BridgeConstant.getBridgeUser(bridgeResult.getData().getBridge_status());
                }

                if (!(bridgeUser)) {
                    apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                    response =
                            DRAPIMountAPIBeanUtils.addApiError(response,
                                    GroupConfigErrorCode.ATTACH_CONSULT_INVALID.getValue(),
                                    GroupConfigErrorCode.ATTACH_CONSULT_INVALID.getMessage(),
                                    apiPosition.getPosition(), null);
                    break;
                }
            }

            // 目前还未上线寻呼，如果请求中带有寻呼
            if (attachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_CALL) {
                apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                response =
                        DRAPIMountAPIBeanUtils.addApiError(response,
                                GroupConfigErrorCode.ATTACH_CALL_INVALID.getValue(),
                                GroupConfigErrorCode.ATTACH_CALL_INVALID.getMessage(), apiPosition.getPosition(), null);
                break;
            }

            // status只允许0,1
            if (attachInfo.getStatus() != AttachInfoConstant.STATUS_NORMAL
                    && attachInfo.getStatus() != AttachInfoConstant.STATUS_PAUSE) {
                apiPosition.addParam(GroupConstant.POSITION_ATTACH_TYPE);
                response =
                        DRAPIMountAPIBeanUtils.addApiError(response,
                                GroupConfigErrorCode.ATTACH_STATUS_INVALID.getValue(),
                                GroupConfigErrorCode.ATTACH_STATUS_INVALID.getMessage(), apiPosition.getPosition(),
                                null);
                break;
            }
        }
        
        return response;
    }

    /**
     * 修改附加信息-电话的状态
     * 
     * @param update 待更新的附加信息
     * @param groupId 推广组id
     * @param existStatusMap 类型对应的状态
     * @param visitor visitor
     * @param userId 用户id
     */
    private void updatePhoneEnableOrDisable(List<AttachInfoVo> update, int groupId,
            Map<Integer, Integer> existStatusMap, Visitor visitor, int userId) {
        if (CollectionUtils.isEmpty(update)) {
            return;
        }

        List<Integer> enablePhone = new ArrayList<Integer>();
        List<Integer> disablePhone = new ArrayList<Integer>();

        for (AttachInfoVo request : update) {

            if (request.getAttachState() == existStatusMap.get(request.getAttachType())) {
                continue;
            }

            if (request.getAttachType() == AttachInfoConstant.ATTACH_INFO_PHONE) {
                boolean success =
                        request.getAttachState() == AttachInfoConstant.STATUS_NORMAL ? enablePhone.add(groupId)
                                : disablePhone.add(groupId);
            }
        }

        if (CollectionUtils.isNotEmpty(enablePhone)) {
            groupAttachInfoMgr.enablePhone(enablePhone, visitor, userId);
        }

        if (CollectionUtils.isNotEmpty(disablePhone)) {
            groupAttachInfoMgr.disablePhone(disablePhone, visitor, userId);
        }

    }

    /**
     * 
     * 修改附加信息-短信的状态
     * 
     * @param update 待更新的附加信息
     * @param groupId 推广组id
     * @param existStatusMap 类型对应的状态
     * @param visitor visitor
     * @param userId 用户id
     */
    private void updateMessageEnableOrDisable(List<AttachInfoVo> update, int groupId,
            Map<Integer, Integer> existStatusMap, Visitor visitor, int userId) {
        if (CollectionUtils.isEmpty(update)) {
            return;
        }

        List<Integer> enableMessage = new ArrayList<Integer>();
        List<Integer> disableMessage = new ArrayList<Integer>();

        for (AttachInfoVo request : update) {

            if (request.getAttachState() == existStatusMap.get(request.getAttachType())) {
                continue;
            }

            if (request.getAttachType() == AttachInfoConstant.ATTACH_INFO_MESSAGE) {
                boolean success =
                        request.getAttachState() == AttachInfoConstant.STATUS_NORMAL ? enableMessage.add(groupId)
                                : disableMessage.add(groupId);
            }
        }

        if (CollectionUtils.isNotEmpty(enableMessage)) {
            groupAttachInfoMgr.enableMessage(enableMessage, visitor, userId);
        }

        if (CollectionUtils.isNotEmpty(disableMessage)) {
            groupAttachInfoMgr.disableMessage(disableMessage, visitor, userId);
        }

    }

    /**
     * 
     * 修改附加信息-咨询的状态
     * 
     * @param update 待更新的附加信息
     * @param groupId 推广组id
     * @param existStatusMap 类型对应的状态
     * @param visitor visitor
     * @param userId 用户id
     */
    private void updateConsultEnableOrDisable(List<AttachInfoVo> update, int groupId,
            Map<Integer, Integer> existStatusMap, Visitor visitor, int userId) {
        if (CollectionUtils.isEmpty(update)) {
            return;
        }

        List<Integer> enableConsult = new ArrayList<Integer>();
        List<Integer> disableConsult = new ArrayList<Integer>();

        for (AttachInfoVo request : update) {

            if (request.getAttachState() == existStatusMap.get(request.getAttachType())) {
                continue;
            }

            if (request.getAttachType() == AttachInfoConstant.ATTACH_INFO_CONSULT) {
                boolean success =
                        request.getAttachState() == AttachInfoConstant.STATUS_NORMAL ? enableConsult.add(groupId)
                                : disableConsult.add(groupId);
            }
        }

        if (CollectionUtils.isNotEmpty(enableConsult)) {
            groupAttachInfoMgr.modConsultState(enableConsult, visitor, userId, AttachInfoConstant.STATUS_NORMAL);
        }

        if (CollectionUtils.isNotEmpty(disableConsult)) {
            groupAttachInfoMgr.modConsultState(disableConsult, visitor, userId, AttachInfoConstant.STATUS_PAUSE);
        }

    }

    /**
     * 
     * 修改附加信息-子链的状态
     * 
     * @param update 待更新的附加信息
     * @param groupId 推广组id
     * @param existStatusMap 类型对应的状态
     * @param visitor visitor
     * @param userId 用户id
     */
    private void updateSubUrlEnableOrDisable(List<AttachInfoVo> update, int groupId,
            Map<Integer, Integer> existStatusMap, Visitor visitor, int userId) {
        if (CollectionUtils.isEmpty(update)) {
            return;
        }

        List<Integer> enableSubUrl = new ArrayList<Integer>();
        List<Integer> disableSubUrl = new ArrayList<Integer>();

        for (AttachInfoVo request : update) {

            if (request.getAttachState() == existStatusMap.get(request.getAttachType())) {
                continue;
            }

            if (request.getAttachType() == AttachInfoConstant.ATTACH_INFO_SUB_URL) {
                if (request.getAttachState() == AttachInfoConstant.STATUS_NORMAL) {
                    enableSubUrl.add(groupId);
                } else {
                    disableSubUrl.add(groupId);
                }      
            }
        }

        if (CollectionUtils.isNotEmpty(enableSubUrl)) {
            groupAttachInfoMgr.enableSubUrl(enableSubUrl, visitor, userId);
        }

        if (CollectionUtils.isNotEmpty(disableSubUrl)) {
            groupAttachInfoMgr.disableSubUrl(disableSubUrl, visitor, userId);
        }

    }
    
    /**
     * 获取存在的状态
     * 
     * @param groupId groupId
     * @return Map<Integer, Integer>
     */
    private Map<Integer, Integer> getExistStatus(int groupId) {
        // 重新查询一次的目的是防止editor本地新建的附加信息没有提交，但是状态变成了暂停
        List<AttachInfo> existInfo = attachInfoMgr.getAttachInfoByGroupId(groupId);

        Map<Integer, Integer> existMap = new HashMap<Integer, Integer>(existInfo.size());

        for (AttachInfo existType : existInfo) {
            existMap.put(existType.getAttachType(), existType.getState());
        }
        return existMap;
    }

	private void fillAttachInfo(List<AttachInfo> existInfo,
			List<AttachInfoType> requestInfo, List<AttachInfoVo> addAttach,
			List<AttachInfoVo> updateAttach) {
	    for (AttachInfoType request : requestInfo) {
            if (CollectionUtils.isEmpty(existInfo)) {
                AttachInfoVo attachInfoVo =
                        new AttachInfoVo(0, request.getAttachType(), request.getAttachContent(),
                                request.getAttachMessage(), request.getStatus(), "", "", "", "");
                setAttachInfoVoSubUrlByRequest(request, attachInfoVo); // 如果attachType==512则设置子链的附加信息
                addAttach.add(attachInfoVo);
                continue;
            }
            boolean isAdd = false;
            AttachInfoVo attachInfoVo = null;
            for (AttachInfo exist : existInfo) {
                attachInfoVo =
                        new AttachInfoVo(0, request.getAttachType(), request.getAttachContent(),
                                request.getAttachMessage(), request.getStatus(), "", "", "", "");
                if (request.getAttachType() == exist.getAttachType()) {
                    setAttachInfoVoSubUrlByRequest(request, attachInfoVo); // 如果attachType==512则设置子链的附加信息
                    updateAttach.add(attachInfoVo);
                    isAdd = false;
                    break;
                } else {
                    isAdd = true;
                }
            }

            if (isAdd) {
                addAttach.add(attachInfoVo);
            }

        }
	}
	
	private void setAttachInfoVoSubUrlByRequest(AttachInfoType request, AttachInfoVo attachInfoVo) {
        if (request.getAttachType() == AttachInfoConstant.ATTACH_INFO_SUB_URL) {
            attachInfoVo.setAttachSubUrlParam(request.getAttachSubUrlParam());
            List<AttachSubUrlItemType> subUrls = request.getAttachSubUrls();
            String subUrlTitle = "";
            String subUrlLink = "";
            String subUrlWirelessLink = "";
            if (subUrls == null) {
                subUrlTitle = "";
            } else {
                for (int i = 0; i < subUrls.size(); i++) {
                    if (i != subUrls.size() - 1) {
                        subUrlTitle = subUrlTitle + subUrls.get(i).getAttachSubUrlTitle() 
                                + UbmcConstant.VALUE_ITEM_ELEMENT_DELIMITER;
                        subUrlLink = subUrlLink + subUrls.get(i).getAttachSubUrlLink() 
                                + UbmcConstant.VALUE_ITEM_ELEMENT_DELIMITER;
                        subUrlWirelessLink = subUrlWirelessLink + subUrls.get(i).getAttachSubUrlWirelessLink() 
                                + UbmcConstant.VALUE_ITEM_ELEMENT_DELIMITER;
                    } else {
                        subUrlTitle += subUrls.get(i).getAttachSubUrlTitle();
                        subUrlLink += subUrls.get(i).getAttachSubUrlLink();
                        subUrlWirelessLink += subUrls.get(i).getAttachSubUrlWirelessLink();
                    }
                }
            }
            attachInfoVo.setAttachSubUrlTitle(subUrlTitle);
            attachInfoVo.setAttachSubUrlLink(subUrlLink);
            attachInfoVo.setAttachSubUrlWirelessLink(subUrlWirelessLink);
        }
    }

	public ApiResult<Object> addVtPeople(ApiResult<Object> result,
			GroupVtItemType[] vtPeoples) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将vt人群分组，key为groupId
		Map<Long, List<GroupVtItem>> groupVtPeoples = new HashMap<Long, List<GroupVtItem>>();
		for (int index = 0; index < vtPeoples.length; index++) {
			GroupVtItemType vtPeople = vtPeoples[index];
			Long groupId = vtPeople.getGroupId();

			List<GroupVtItem> groupVtPeopleList = groupVtPeoples.get(groupId);
			if (groupVtPeopleList == null) {
				groupVtPeopleList = new ArrayList<GroupVtItem>();
				groupVtPeoples.put(groupId, groupVtPeopleList);
			}

			groupVtPeopleList.add(new GroupVtItem(index, 
					vtPeople.getRelationType(), 
					vtPeople.getPeopleId()));
		}

		// 验证待vt人群推广组的个数
		if (groupVtPeoples.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.VT_PEOPLES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		// 依照推广组添加vt人群
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupVtPeoples.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupVtItem> groupVtPeopleList = groupVtPeoples.get(groupId);

			// 推广组不存在
			if (group == null) {
				for (GroupVtItem item : groupVtPeopleList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.VT_PEOPLES, item.getIndex());
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
			if (!TargettypeUtil.hasVT(targetType)) {
				for (GroupVtItem item : groupVtPeopleList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.VT_PEOPLES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.VT_GROUP_TYPE_ERROR.getValue(),
							GroupConfigErrorCode.VT_GROUP_TYPE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 获取该用户的人群设置以及指定推广组的人群设置
			List<CproGroupVTVo> oldVtPeopleList = cproGroupVTMgr
					.findVTRelationByGroup(group.getGroupId(), group.getUserId());
			List<CproGroupVTVo> vtPeopleList = vtPeopleMgr.getAllVtPeople(group.getUserId());
			Set<Long> vtPeopleIdSet = new HashSet<Long>();
			for (CproGroupVTVo people : vtPeopleList) {
				vtPeopleIdSet.add(people.getId());
			}
			
			// 校验vt的peoples数据
			List<Long> includePeopleIds = new ArrayList<Long>();
			List<Long> excludePeopleIds = new ArrayList<Long>();
			List<Long> oldPeopleIds = new ArrayList<Long>();
			for (CproGroupVTVo vtVo : oldVtPeopleList) {
				oldPeopleIds.add(vtVo.getId());
			}
			
			for (GroupVtItem item : groupVtPeopleList) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.VT_PEOPLES, item.getIndex());
				
				// 该用户未设置请求中的人群
				if (!vtPeopleIdSet.contains(item.getPeopleId())) {
					apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.VT_PEOPLE_ERROR.getValue(),
							GroupConfigErrorCode.VT_PEOPLE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
					continue;
				}
				
				// 数据库中该推广组已经存在该人群
				if (oldPeopleIds.contains(item.getPeopleId())) {
					apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getValue(),
							GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getMessage(), 
							apiPosition.getPosition(), null);
					continue;
				}
				
				if (item.getRelationType() == CproGroupConstant.GROUP_VT_INCLUDE_CROWD) {
					// 请求中为关联人群
					if (includePeopleIds.contains(item.getPeopleId())) {
						apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
						
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getValue(),
								GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getMessage(), 
								apiPosition.getPosition(), null);
						continue;
					} else {
						includePeopleIds.add(item.getPeopleId());
					}
				} else if (item.getRelationType() == CproGroupConstant.GROUP_VT_EXCLUDE_CROWD) {
					// 请求中为排除人群
					if (excludePeopleIds.contains(item.getPeopleId())) {
						apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
						
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getValue(),
								GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getMessage(), 
								apiPosition.getPosition(), null);
						continue;
					} else {
						excludePeopleIds.add(item.getPeopleId());
					}
				} else {
					// 请求中为无效人群
					apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.VT_PEOPLE_ERROR.getValue(),
							GroupConfigErrorCode.VT_PEOPLE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
			}
			
			int total = includePeopleIds.size() + excludePeopleIds.size();
			if (total == 0) {
				continue;
			}
			
			// 检验是否超过单个推广组VT人群设置的最大值
			if (total + oldVtPeopleList.size() 
					> CproGroupConstant.GROUP_VT_INCLUDE_CROWD_MAX_NUM) {
				for (GroupVtItem item : groupVtPeopleList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.VT_PEOPLES, item.getIndex());
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.VT_PEOPLE_MAX.getValue(),
							GroupConfigErrorCode.VT_PEOPLE_MAX.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			Set<Long> addVtPeopleIdSet = new HashSet<Long>();
			addVtPeopleIdSet.addAll(includePeopleIds);
			addVtPeopleIdSet.addAll(excludePeopleIds);
			if (addVtPeopleIdSet.size() < total) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.VT_PEOPLES);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getValue(),
						GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			List<CproGroupVTVo> addVtPeopleList = new ArrayList<CproGroupVTVo>();
			Map<Long, CproGroupVTVo> includePidMap = makeVTMap(includePeopleIds, true);
			Map<Long, CproGroupVTVo> excludePidMap = makeVTMap(excludePeopleIds, false);
			addVtPeopleList.addAll(includePidMap.values());
			addVtPeopleList.addAll(excludePidMap.values());
			
			Map<Long, CproGroupVTVo> addVtPeopleMap = makeVTMap(addVtPeopleList, groupId.intValue());
			
			List<CproGroupVTVo> addResult = addVtPeopleToDb(addVtPeopleMap, groupId.intValue(),
					group.getPlanId(), group.getUserId(), visitor);

			// 保存历史操作记录-START
			try {
				// 历史记录：添加RT关联关系
				if (CollectionUtils.isNotEmpty(addResult)) {
					// 批量初始化新建人群对应的“人群 名称”
					initVtPeopleName(addResult, group.getUserId(), group.getGroupId(), vtPeopleMgr, cproGroupVTMgr);
					oldVtPeopleList = new ArrayList<CproGroupVTVo>();
					Map<String, List<String>> includeChangeMap = getChangedPeopleNameMap(addResult,
							oldVtPeopleList, group.getGroupId(), CproGroupConstant.GROUP_VT_INCLUDE_CROWD);
					Map<String, List<String>> exCludeChangeMap = getChangedPeopleNameMap(addResult,
							oldVtPeopleList, group.getGroupId(), CproGroupConstant.GROUP_VT_EXCLUDE_CROWD);
					
					OpTypeVo opType = null;
					
					// 变动关联人群
					if ((includeChangeMap != null) 
							&& ((!CollectionUtils.isEmpty(includeChangeMap.get(VALUE_BEFORE))) 
									|| (!CollectionUtils.isEmpty(includeChangeMap.get(VALUE_AFTER))))) {
						opType = OptHistoryConstant.OPTYPE_GROUP_VTPEOPLE_ADD;
						OptContent content = new OptContent(group.getUserId(), 
								opType.getOpType(),	opType.getOpLevel(), group.getGroupId(), 
								opType.getTransformer().toDbString(includeChangeMap.get(VALUE_BEFORE)), 
								opType.getTransformer().toDbString(includeChangeMap.get(VALUE_AFTER)));
						optContents.add(content);
					}
					// 变动排除人群
					if ((exCludeChangeMap != null)
							&& ((!CollectionUtils.isEmpty(exCludeChangeMap.get(VALUE_BEFORE))) 
									|| (!CollectionUtils.isEmpty(exCludeChangeMap.get(VALUE_AFTER))))) {
						opType = OptHistoryConstant.OPTYPE_GROUP_VTPEOPLE_DELETE;
						OptContent content = new OptContent(group.getUserId(), 
								opType.getOpType(), opType.getOpLevel(), group.getGroupId(), 
								opType.getTransformer().toDbString(exCludeChangeMap.get(VALUE_BEFORE)), 
								opType.getTransformer().toDbString(exCludeChangeMap.get(VALUE_AFTER)));
						optContents.add(content);
					}
					
					// 记录成功数
					int success = pay.getSuccess();
					pay.setSuccess(success + addResult.size());
				}	
				// 历史记录：添加RT关联关系
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	private List<CproGroupVTVo> addVtPeopleToDb(Map<Long, CproGroupVTVo> addVtPeopleMap,
			Integer groupId, Integer planId, Integer userId, Visitor visitor) {
		List<CproGroupVTVo> result = new ArrayList<CproGroupVTVo>();
		
		if (CollectionUtils.isEmpty(addVtPeopleMap.keySet())) {
			return result;
		}
		
		LogUtils.businessInfo(visitor, "add CproGroupVTPeople(1): ", 
				toCproGroupVTVoString(addVtPeopleMap.values()));
		
		// 当前包含待添加VT人群
		if (!CollectionUtils.isEmpty(addVtPeopleMap.entrySet())) {
			for (CproGroupVTVo addVtVo : addVtPeopleMap.values()) {
				VtPeople vtPeople = vtPeopleMgr.findVtPeople(addVtVo.getId(), userId);
				if (vtPeople == null) {
					log.error("can't find vtPeople error,pid=" + addVtVo.getId());
					continue;
				}
				// 从id服务取id
				Long nextVtId = sequenceDriver.getNextId(SequenceConstant.SEQ_GROUPVT);
				CproGroupVT newGroupVt = new CproGroupVT(nextVtId, groupId, 
						planId, userId, CproGroupConstant.GROUP_TARGET_TYPE_VT, 
						vtPeople, addVtVo.getType(), new Date(), new Date(), 
						Integer.valueOf(visitor.getUserid()), 
						Integer.valueOf(visitor.getUserid()));
				cproGroupVTDao.addVTRelation(newGroupVt, userId);
				result.add(addVtVo);
			}
		}
		
		LogUtils.businessInfo(visitor, "add CproGroupVTPeople(2): ", 
				toCproGroupVTVoString(addVtPeopleMap.values()));
		
		return result;
	}
	
	public ApiResult<Object> addSite(ApiResult<Object> result,
			GroupSiteType[] sites) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将site分组，key为groupId
		Map<Long, List<GroupSiteItem>> groupSites = new HashMap<Long, List<GroupSiteItem>>();
		for (int index = 0; index < sites.length; index++) {
			GroupSiteType site = sites[index];
			Long groupId = site.getGroupId();

			List<GroupSiteItem> groupSiteList = groupSites.get(groupId);
			if (groupSiteList == null) {
				groupSiteList = new ArrayList<GroupSiteItem>();
				groupSites.put(groupId, groupSiteList);
			}

			groupSiteList.add(new GroupSiteItem(index, site.getSite()));
		}

		// 验证待添加site推广组的个数
		if (groupSites.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.SITES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		
		// 依照推广组添加site
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupSites.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());

			List<GroupSiteItem> groupSiteList = groupSites.get(groupId);
			
			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupSiteItem item : groupSiteList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.SITES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}

			// 该推广组为全网投放时不能进行添加site
			int isAllSite = group.getGroupInfo().getIsAllSite();
			if (isAllSite == CproGroupConstant.GROUP_ALLSITE) {
				for (GroupSiteItem item : groupSiteList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.SITES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.SITE_NOT_OPTIONAL_ERROR.getValue(),
							GroupConfigErrorCode.SITE_NOT_OPTIONAL_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			CproGroupInfo groupInfo = group.getGroupInfo();
			CproGroupInfo groupInfoBefore = new CproGroupInfo();
			try {
				BeanUtils.copyProperties(groupInfoBefore, groupInfo);
			} catch (Exception e) {
				log.error("failed to copy groupinfo. " + e.getMessage(), e);
			}

			// 获取该推广组已经添加的siteId
			List<Integer> selectedSiteIds = GroupSiteUtil.unConvertStrToList(groupInfo.getSiteListStr());
			
			// 获取该推广组已经添加的tradeId
			List<Integer> tradeIds = GroupSiteUtil.unConvertStrToList(groupInfo.getSiteTradeListStr());
			
			List<Integer> addedSiteIds = new ArrayList<Integer>();
			Map<String, Integer> siteIdMap = UnionSiteCache.siteInfoCache.getSiteIdMap();
			
			// key: siteId, value: index in request
			Map<Integer, Integer> siteIndexMap = new HashMap<Integer, Integer>();
			
			// 将数据库中的投放网站和待添加的网站合并为mergedSiteIds
			Set<Integer> mergedSiteIds = new HashSet<Integer>();
			mergedSiteIds.addAll(selectedSiteIds);
			for (GroupSiteItem item : groupSiteList) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITES, item.getIndex());
				apiPosition.addParam(GroupConstant.SITE);
				
				Integer siteId = siteIdMap.get(item.getSite().toLowerCase());	
				if (siteId != null) {
					if (mergedSiteIds.contains(siteId)) {
						// 如果已经存在，则去重
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.SITE_DUP.getValue(),
								GroupConfigErrorCode.SITE_DUP.getMessage(),
								apiPosition.getPosition(), null);
//					} else if (WhiteListCache.baiduSites.hasId(siteId)
//							&& !WhiteListCache.useBaiduUsers.has(bdUser.getUserid())) {
//						// 如果网站在网站白名单中，但是用户不在用户白名单中，则跳过此条并记录错误信息
//						result = ApiResultBeanUtils.addApiError(result,
//								GroupConfigErrorCode.NO_BAIDU_SITE_PRIVELEGE.getValue(),
//								GroupConfigErrorCode.NO_BAIDU_SITE_PRIVELEGE.getMessage(), 
//								apiPosition.getPosition(), null);
					} else {
						addedSiteIds.add(siteId);
						mergedSiteIds.add(siteId);
						siteIndexMap.put(siteId, item.getIndex());
					}
				} else {
					// 该网站不存在
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.SITE_NOT_FOUND.getValue(),
							GroupConfigErrorCode.SITE_NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
			}
			
			// 检验是否超过单个推广组投放网站设置的最大值
			if (mergedSiteIds.size() > CproGroupConstant.SITE_SELECTED_MAX_NUM) {
				for (GroupSiteItem item : groupSiteList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.SITES, item.getIndex());
					apiPosition.addParam(GroupConstant.SITE);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.SITE_SET_MAX.getValue(),
							GroupConfigErrorCode.SITE_SET_MAX.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 设置其他参数，如siteSum，cmpLevel等
			List<Integer> siteIds = new ArrayList<Integer>();
			siteIds.addAll(addedSiteIds);
			Collections.sort(siteIds); // 必须先排序
			
			// 这里冗余的增加一个，为了不删除已经存在站点信息，设置其他参数，如siteSum，cmpLevel等
			List<Integer> tmpSiteIds = new ArrayList<Integer>();
			tmpSiteIds.addAll(addedSiteIds);
			Collections.sort(tmpSiteIds); // 必须先排序
			
			SiteSumInfo siteInfo = UnionSiteCalculator.genSiteInfo(siteIds,	tradeIds, group.getUserId(), group.getGroupType());
			SiteSumInfo tmpSiteInfo = UnionSiteCalculator.genSiteInfo(tmpSiteIds, tradeIds, group.getUserId(), group.getGroupType());

			// 推广组类型和网站类型不匹配时则报错
			int filterOutSiteNum = siteIds.size() - siteInfo.getSiteList().size();
			if (filterOutSiteNum != 0) {
				for (Integer siteId : siteIds) {
					if (!siteInfo.getSiteList().contains(siteId)) {
						Integer index = siteIndexMap.get(siteId);
						/* 
						 * 注释掉该逻辑：
						 * 由于siteIds.addAll(addedSiteIds);加入的是添加站点，因此已经经过了是否存在于unionsitecache的校验
						 * 
						if (index == null) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(GroupConstant.SITES);
							return ApiResultBeanUtils.addApiError(result,
									GroupConfigErrorCode.CAN_NOT_ADD_SITE_DUE_TO_EXIST_SITE_HAS_BEEN_INVALID.getValue(),
									GroupConfigErrorCode.CAN_NOT_ADD_SITE_DUE_TO_EXIST_SITE_HAS_BEEN_INVALID.getMessage(), 
									apiPosition.getPosition(), null);
						}
						*/
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(GroupConstant.SITES, index);
						apiPosition.addParam(GroupConstant.SITE);
		
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.SITE_TRADE_SOME_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP.getValue(),
								GroupConfigErrorCode.SITE_TRADE_SOME_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP.getMessage(), 
								apiPosition.getPosition(), null);
					}
				}
			}
			
			Set<Integer> toSavedSiteIdSet = new HashSet<Integer>();
			toSavedSiteIdSet.addAll(selectedSiteIds);
			toSavedSiteIdSet.addAll(siteInfo.getSiteList());
			
			/*
			 * 注释掉该逻辑：
			 * 此处不判断是否非全网投放，而不设置网站或者行业
			if (CollectionUtils.isEmpty(siteInfo.getSiteList())
					&& CollectionUtils.isEmpty(siteInfo.getSiteTradeList())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITES);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_TRADE_ALL_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP.getValue(),
						GroupConfigErrorCode.SITE_TRADE_ALL_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}
			*/
			
			List<Integer> toSavedSiteIds = new ArrayList<Integer>(toSavedSiteIdSet);
			groupInfo.setSiteSum(tmpSiteInfo.getSiteSum());
			groupInfo.setCmpLevel(tmpSiteInfo.getCmpLevel());
			
			// 避免由于""被设到db中造成错误，并结算添加成功多少个
			int addNum = 0;
			String siteListStr2 = com.baidu.beidou.util.StringUtils.makeStrFromCollectionForSite(toSavedSiteIds, CproGroupConstant.FIELD_SEPERATOR);
			if (siteListStr2 == null || siteListStr2.length() == 0) {
				siteListStr2 = null;
			} else {
				addNum = siteInfo.getSiteList().size();
			}
			groupInfo.setSiteListStr(siteListStr2);

			// 添加网站不为空时才保存
			if (CollectionUtils.isNotEmpty(siteInfo.getSiteList())) {
				cproGroupMgr.modCproGroupInfo(visitor, groupInfo);
			}
			
			// 保存历史操作记录-START
			try {
				// 历史记录：添加投放网站
				OpTypeVo opTypeSite = OptHistoryConstant.OPTYPE_GROUP_SITE_EDIT;
				OptContent contentSite = new OptContent(group.getUserId(), 
						opTypeSite.getOpType(), opTypeSite.getOpLevel(), group.getGroupId(), 
						opTypeSite.getTransformer().toDbString(new GroupSiteOptVo(groupInfoBefore)),  
						opTypeSite.getTransformer().toDbString(new GroupSiteOptVo(groupInfo)));
				if (!contentSite.getPreContent().equals(contentSite.getPostContent())) {
					optContents.add(contentSite);
				}
				
				// 记录成功数
				int success = pay.getSuccess();
				pay.setSuccess(success + addNum);
				
				// 历史记录：添加投放网站
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> addTrade(ApiResult<Object> result,
			GroupTradeType[] trades) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将trade分组，key为groupId
		Map<Long, List<GroupTradeItem>> groupTrades = new HashMap<Long, List<GroupTradeItem>>();
		for (int index = 0; index < trades.length; index++) {
			GroupTradeType trade = trades[index];
			Long groupId = trade.getGroupId();

			List<GroupTradeItem> groupTradeList = groupTrades.get(groupId);
			if (groupTradeList == null) {
				groupTradeList = new ArrayList<GroupTradeItem>();
				groupTrades.put(groupId, groupTradeList);
			}

			groupTradeList.add(new GroupTradeItem(index, trade.getTrade()));
		}

		// 验证待添加trade推广组的个数
		if (groupTrades.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TRADES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		
		// 依照推广组添加trade
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupTrades.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupTradeItem> groupTradeList = groupTrades.get(groupId);

			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupTradeItem item : groupTradeList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}

			// 该推广组为全网投放时不能进行添加trade
			int isAllSite = group.getGroupInfo().getIsAllSite();
			if (isAllSite == CproGroupConstant.GROUP_ALLSITE) {
				for (GroupTradeItem item : groupTradeList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TRADE_NOT_OPTIONAL_ERROR.getValue(),
							GroupConfigErrorCode.TRADE_NOT_OPTIONAL_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			CproGroupInfo groupInfo = group.getGroupInfo();
			CproGroupInfo groupInfoBefore = new CproGroupInfo();
			try {
				BeanUtils.copyProperties(groupInfoBefore, groupInfo);
			} catch (Exception e) {
				log.error("failed to copy groupinfo. " + e.getMessage(), e);
			}

			// 获取该推广组已经添加的siteId
			List<Integer> siteIds = GroupSiteUtil.unConvertStrToList(groupInfo.getSiteListStr());
			
			// 获取该推广组已经添加的tradeId
			List<Integer> selectedTradeIds = GroupSiteUtil.unConvertStrToList(groupInfo.getSiteTradeListStr());
			
			// 贴片推广组不支持设置分类
			// @version cpweb443 仅为贴片推广组时不能添加行业
			if (group.getGroupType() == CproGroupConstant.GROUP_TYPE_FILM) {
				for (GroupTradeItem item : groupTradeList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TRADE_GROUP_TYPE_ERROR.getValue(),
							GroupConfigErrorCode.TRADE_GROUP_TYPE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			List<Integer> addedTradeIds = new ArrayList<Integer>();
			
			// 将数据库中的投放行业和待添加的行业合并为mergedTradeIds
			Set<Integer> mergedTradeIds = new HashSet<Integer>();
			mergedTradeIds.addAll(selectedTradeIds);
			Map<Integer, GroupTradeItem> groupTradeMap = new HashMap<Integer, GroupTradeItem>();
			for (GroupTradeItem item : groupTradeList) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADES, item.getIndex());
				apiPosition.addParam(GroupConstant.TRADE);
				
				Integer tradeId = item.getTrade();
				TradeInfo tradeInfo = UnionSiteCache.tradeInfoCache.getTradeInfoById(tradeId);
				if (tradeInfo == null) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(),
							GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				} else {
					if (mergedTradeIds.contains(tradeId)) {
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.CATEGORY_DUP.getValue(),
								GroupConfigErrorCode.CATEGORY_DUP.getMessage(), 
								apiPosition.getPosition(), null);
//					} else if (WhiteListCache.baiduTrades.has(tradeId)
//							&& !WhiteListCache.useBaiduUsers.has(bdUser.getUserid())) {
//						// 如果行业在行业白名单中，但是用户不在用户白名单中，则跳过此条并记录错误信息
//						result = ApiResultBeanUtils.addApiError(result,
//								GroupConfigErrorCode.NO_BAIDU_TRADE_PRIVELEGE.getValue(),
//								GroupConfigErrorCode.NO_BAIDU_TRADE_PRIVELEGE.getMessage(),
//								apiPosition.getPosition(), null);
					} else {
						addedTradeIds.add(tradeId);
						mergedTradeIds.add(tradeId);
						groupTradeMap.put(tradeId, item);
					}
				}
			}
			
			// 检查已经存在一级行业的二级行业，并准备将其删除
			List<Integer> toDelTradeIds = new ArrayList<Integer>();
			Map<Integer, List<Integer>> tradeChildrenMap = UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildren();
			for (Integer tradeId : addedTradeIds) {
				TradeInfo tradeInfo = UnionSiteCache.tradeInfoCache.getTradeInfoById(tradeId);
				
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADES, groupTradeMap.get(tradeId).getIndex());
				apiPosition.addParam(GroupConstant.TRADE);
				
				// 如果待添加的为二级行业，并且其所属的一级行业也已经在待添加集合中，那么删除该二级行业
				if (tradeInfo.getParentid() != 0
						&& mergedTradeIds.contains(tradeInfo.getParentid())) {
					toDelTradeIds.add(tradeId);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TRADE_FIRST_SECOND_TRADE_ERROR.getValue(),
							GroupConfigErrorCode.TRADE_FIRST_SECOND_TRADE_ERROR.getMessage(),
							apiPosition.getPosition(), null);
				} else if (tradeInfo.getParentid() == 0) {
					// 如果待添加的为一级行业，并且其下存在二级行业在数据库中已经选择的地域列表中，则删除该一级行业
					List<Integer> tradeChildren = tradeChildrenMap.get(tradeId);
					if (tradeChildren == null) {
						continue;
					}
					for (Integer trade : tradeChildren) {
						if (selectedTradeIds.contains(trade)) {
							toDelTradeIds.add(trade);
						}/* else if (mergedTradeIds.contains(trade)) {
							if (!toDelTradeIds.contains(trade)) {
								toDelTradeIds.add(trade);
								result = ApiResultBeanUtils.addApiError(result,
										GroupConfigErrorCode.TRADE_FIRST_SECOND_TRADE_ERROR.getValue(),
										GroupConfigErrorCode.TRADE_FIRST_SECOND_TRADE_ERROR.getMessage(),
										apiPosition.getPosition(), null);
							}
						}*/
					}
				}
			}
			// 删除待删除的二级行业
			addedTradeIds.removeAll(toDelTradeIds);
			mergedTradeIds.removeAll(toDelTradeIds);
			
			// 设置其他参数，如siteSum，cmpLevel等
			List<Integer> tradeIds = new ArrayList<Integer>();
			tradeIds.addAll(mergedTradeIds);
			
			// 必须先排序
			Collections.sort(tradeIds);
			
			SiteSumInfo siteInfo = UnionSiteCalculator.genSiteInfo(siteIds,	tradeIds, 
					group.getUserId(), group.getGroupType());
			if (CollectionUtils.isEmpty(siteInfo.getSiteList())
					&& CollectionUtils.isEmpty(siteInfo.getSiteTradeList())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADES);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(),
						GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}
			groupInfo.setSiteSum(siteInfo.getSiteSum());
			groupInfo.setCmpLevel(siteInfo.getCmpLevel());
			
			// 避免由于""被设到db中造成错误，并结算添加成功多少个
			int addNum = addedTradeIds.size();
			String tradeListStr2 = com.baidu.beidou.util.StringUtils.makeStrFromCollectionForSite(
					siteInfo.getSiteTradeList(), CproGroupConstant.FIELD_SEPERATOR);

			groupInfo.setSiteTradeListStr(tradeListStr2);

			cproGroupMgr.modCproGroupInfo(visitor, groupInfo);

			// 保存历史操作记录-START
			try {
				// 历史记录：添加投放行业
				OpTypeVo opTypeTrade = OptHistoryConstant.OPTYPE_GROUP_TRADE_EDIT;
				OptContent contentTrade = new OptContent(group.getUserId(), 
						opTypeTrade.getOpType(), opTypeTrade.getOpLevel(), group.getGroupId(), 
						opTypeTrade.getTransformer().toDbString(new GroupSiteOptVo(groupInfoBefore)),  
						opTypeTrade.getTransformer().toDbString(new GroupSiteOptVo(groupInfo)));
				if (!contentTrade.getPreContent().equals(contentTrade.getPostContent())) {
					optContents.add(contentTrade);
				}
				
				// 记录成功数
				int success = pay.getSuccess();
				pay.setSuccess(success + addNum);
				
				// 历史记录：添加投放行业
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}

    @Override
    public ApiResult<Object> addRegion(ApiResult<Object> result, HashMultimap<Long, GroupRegionItem> regionMap) {
        PaymentResult pay = result.getPayment();

        // 获得用户信息
        Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

        List<OptContent> optContents = new ArrayList<OptContent>();
        Set<Long> groupIds = regionMap.keySet();
        for (Long groupId : groupIds) {
            CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
            Set<GroupRegionItem> groupRegionList = regionMap.get(groupId);

            if (!regionUpdateGroupCheck(result, group, groupRegionList)) {
                continue;
            }

            CproGroupInfo groupInfo = group.getGroupInfo();
            String preRegListStr = groupInfo.getSysRegListStr();
            Integer preRegSum = groupInfo.getRegSum();
            // 获取该推广组已经添加的regionId
            Set<Integer> selectedRegionIds = GroupSiteUtil.unConvertStrToSet(preRegListStr);

            int addNum = 0;
            for (GroupRegionItem item : groupRegionList) {
                Integer[] regInfo = sysRegCache.getRegInfoMap().get(item.getRegionId());
                if (regInfo == null || regInfo.length != 2) {
                    GroupConfigErrorCode.REGION_NOT_FOUND
                            .getErrorResponse(result, Lists.newArrayList(
                                    new ErrorParam(GroupConstant.REGIONS, item.getIndex()), new ErrorParam(
                                            GroupConstant.REGION_ID, null)), null);
                    continue;
                }

                if (selectedRegionIds.contains(item.getRegionId())) {
                    GroupConfigErrorCode.REGION_DUP
                            .getErrorResponse(result, Lists.newArrayList(
                                    new ErrorParam(GroupConstant.REGIONS, item.getIndex()), new ErrorParam(
                                            GroupConstant.REGION_ID, null)), null);
                    continue;
                }

                if (regInfo[0] == 0) { // 待添加地域为一级地域
                    List<Integer> regChildren = sysRegCache.getFirstLevelRegChildren().get(item.getRegionId());
                    if (regChildren != null) {
                        selectedRegionIds.removeAll(regChildren);
                    }
                } else { // 待添加地域为二级地域
                    if (selectedRegionIds.contains(regInfo[0])) {
                        GroupConfigErrorCode.REGION_FIRST_SECOND_REGION_ERROR.getErrorResponse(result, Lists
                                .newArrayList(new ErrorParam(GroupConstant.REGIONS, item.getIndex()), new ErrorParam(
                                        GroupConstant.REGION_ID, null)), null);
                        continue;
                    }
                }

                selectedRegionIds.add(item.getRegionId());
                addNum++;
            }

            groupInfo.setRegSum(getFirstRegionCount(selectedRegionIds));
            groupInfo.setSysRegListStr(getRegionStr(selectedRegionIds));
            groupInfo.setRegListStr(RegionIdConverter.getBeidouRegListStrFromSys(groupInfo.getSysRegListStr()));
            cproGroupMgr.modCproGroupInfo(visitor, groupInfo);

            // 记录操作历史纪录
            OpTypeVo opTypeRegion = OptHistoryConstant.OPTYPE_GROUP_REGION_BATCH;
            OptContent contentRegion =
                    new OptContent(group.getUserId(), opTypeRegion.getOpType(), opTypeRegion.getOpLevel(),
                            group.getGroupId(), opTypeRegion.getTransformer().toDbString(
                                    new GroupRegionOptVo(groupInfo.getIsAllRegion(), preRegSum, preRegListStr)),
                            opTypeRegion.getTransformer().toDbString(new GroupRegionOptVo(groupInfo)));
            if (!contentRegion.getPreContent().equals(contentRegion.getPostContent())) {
                optContents.add(contentRegion);
            }

            pay.setSuccess(pay.getSuccess() + addNum);
        }
        SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); // 加入session中，后续有拦截器处理

        return result;
    }
	
	public ApiResult<Object> addExcludeIp(ApiResult<Object> result,
			GroupExcludeIpType[] excludeIps) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将excludeIp分组，key为groupId
		Map<Long, List<GroupExcludeIpItem>> groupExcludeIps = new HashMap<Long, List<GroupExcludeIpItem>>();
		for (int index = 0; index < excludeIps.length; index++) {
			GroupExcludeIpType excludeIp = excludeIps[index];
			Long groupId = excludeIp.getGroupId();

			List<GroupExcludeIpItem> groupExcludeIpList = groupExcludeIps.get(groupId);
			if (groupExcludeIpList == null) {
				groupExcludeIpList = new ArrayList<GroupExcludeIpItem>();
				groupExcludeIps.put(groupId, groupExcludeIpList);
			}

			groupExcludeIpList.add(new GroupExcludeIpItem(index, excludeIp.getExcludeIp().toLowerCase()));
		}

		// 验证待添加excludeIp推广组的个数
		if (groupExcludeIps.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_IPS);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		// 依照推广组添加excludeIp
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupExcludeIps.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupExcludeIpItem> groupExcludeIpList = groupExcludeIps.get(groupId);

			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupExcludeIpItem item : groupExcludeIpList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_IPS, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 获取该推广组已经添加的excludeIp
			List<GroupIpFilter> curExcludeIps = groupSiteConfigMgr.findIpFilterByGroupId(groupId.intValue());
			List<String> selectedExcludeIps = this.findExcludeIpStringList(curExcludeIps);
			
			// 并将新增的添加进addedExcludeIps
			List<String> addedExcludeIps = new ArrayList<String>();
			boolean flag = groupConfigValidator.validateAddExcludeIp(groupExcludeIpList, 
					selectedExcludeIps, addedExcludeIps, result);
			if (!flag || addedExcludeIps.size() == 0) {
				// 校验失败则跳过
				continue;
			}
			
			// 添加待添加的过滤IP
			groupSiteConfigMgr.addGroupIpFilter(groupId.intValue(), group.getUserId(), addedExcludeIps, visitor);

			// 保存历史操作记录-START
			try {
				// 历史记录：添加过滤IP
				OpTypeVo opTypeRegion = OptHistoryConstant.OPTYPE_GROUP_IPFILTER_BATCH;
				OptContent contentRegion = new OptContent();
				contentRegion.setUserid(group.getUserId());
				contentRegion.setOpObjId(group.getGroupId());
				contentRegion.setOpType(opTypeRegion.getOpType());
				contentRegion.setOpLevel(opTypeRegion.getOpLevel());
				
				String preContent = opTypeRegion.getTransformer().toDbString(selectedExcludeIps);
				contentRegion.setPreContent(preContent);//保存之前的列表
				
				List<GroupIpFilter> addedExcludeIpList = groupSiteConfigMgr.findIpFilterByGroupId(groupId.intValue());
				List<String> excludeIpList = null; //临时对象
				excludeIpList = this.findExcludeIpStringList(addedExcludeIpList);
				String postContent = opTypeRegion.getTransformer().toDbString(excludeIpList);
				contentRegion.setPostContent(postContent);//保存之后的列表
				
				optContents.add(contentRegion);
				
				// 记录成功数
				int success = pay.getSuccess();
				pay.setSuccess(success + addedExcludeIps.size());
				
				// 历史记录：添加过滤IP
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> addExcludeSite(ApiResult<Object> result,
			GroupExcludeSiteType[] excludeSites) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将excludeSite分组，key为groupId
		Map<Long, List<GroupExcludeSiteItem>> groupExcludeSites = new HashMap<Long, List<GroupExcludeSiteItem>>();
		for (int index = 0; index < excludeSites.length; index++) {
			GroupExcludeSiteType excludeSite = excludeSites[index];
			Long groupId = excludeSite.getGroupId();

			List<GroupExcludeSiteItem> groupExcludeSiteList = groupExcludeSites.get(groupId);
			if (groupExcludeSiteList == null) {
				groupExcludeSiteList = new ArrayList<GroupExcludeSiteItem>();
				groupExcludeSites.put(groupId, groupExcludeSiteList);
			}

			groupExcludeSiteList.add(new GroupExcludeSiteItem(index, excludeSite.getExcludeSite().toLowerCase()));
		}

		// 验证待添加excludeSite推广组的个数
		if (groupExcludeSites.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_SITES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		// 依照推广组添加excludeSite
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupExcludeSites.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupExcludeSiteItem> groupExcludeSiteList = groupExcludeSites.get(groupId);

			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupExcludeSiteItem item : groupExcludeSiteList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_SITES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 获取该推广组已经添加的excludeSite
			List<GroupSiteFilter> curExcludeSites = groupSiteConfigMgr.findSiteFilterByGroupId(groupId.intValue());
			List<String> selectedExcludeSites = this.findExcludeSiteStringList(curExcludeSites);
			
			// 并将新增的添加进addedExcludeSites
			List<String> addedExcludeSites = new ArrayList<String>();
			boolean flag = groupConfigValidator.validateAddExcludeSite(groupExcludeSiteList, 
					selectedExcludeSites, addedExcludeSites, result);
			if (!flag || addedExcludeSites.size() == 0) {
				// 校验失败则跳过
				continue;
			}
			
			// 添加待添加的过滤网站
			groupSiteConfigMgr.addGroupSiteFilter(groupId.intValue(), addedExcludeSites, visitor);

			// 保存历史操作记录-START
			try {
				// 历史记录：添加过滤网站
				OpTypeVo opTypeVoAdd = OptHistoryConstant.OPTYPE_GROUP_SITEFILTER_BATCH_ADD;			
				OptContent contentExcludeSite = new OptContent(group.getUserId(), 
						opTypeVoAdd.getOpType(), opTypeVoAdd.getOpLevel(), groupId,	null, 
						opTypeVoAdd.getTransformer().toDbString(new ArrayList(addedExcludeSites)));
				
				optContents.add(contentExcludeSite);
				
				// 记录成功数
				int success = pay.getSuccess();
				pay.setSuccess(success + addedExcludeSites.size());
				
				// 历史记录：添加过滤网站
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> addExcludeApp(ApiResult<Object> result,
			List<GroupExcludeAppType> excludeApps, int userId, int opUser) {
		PaymentResult pay = result.getPayment();
		
		// 依照推广组添加excludeApp
		List<OptContent> optContents = new ArrayList<OptContent>();
		//Map<String, Integer> appName2IdCache = UnionSiteCache.appCache.getReverseIndexByAppName();
		Map<Long,Integer> appId2IdCache = UnionSiteCache.appCache.getReverseIndexByAppId();
		//List<AppInfo> appInfoList = UnionSiteCache.appCache.getAppInfoList();
		Set<Integer> alreadySaveGroupIdSet = new HashSet<Integer>();
		
		start:
		for (int index = 0; index < excludeApps.size(); index++) {
			Integer groupId = new Long(excludeApps.get(index).getGroupId()).intValue();
			List<Long> appFilters = excludeApps.get(index).getExcludeApp();
			
			if (alreadySaveGroupIdSet.contains(groupId)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_APPS, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.EXCLUDE_APP_GROUP_DUP.getValue(),
						GroupConfigErrorCode.EXCLUDE_APP_GROUP_DUP.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			
			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_APPS, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupErrorCode.NOT_FOUND.getValue(),
						GroupErrorCode.NOT_FOUND.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			List<Long> appIds = new ArrayList<Long>();
			for (int i = 0; i < appFilters.size(); i++) {
				if (!appId2IdCache.containsKey(appFilters.get(i))) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_APPS, index);
					apiPosition.addParam(GroupConstant.EXCLUDE_APP, i);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.EXCLUDE_APP_NOT_FOUND.getValue(),
							GroupConfigErrorCode.EXCLUDE_APP_NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
					continue start;
				}
				
				//Long appId = appInfoList.get(appName2IdCache.get(appFilters.get(i))).getId();
				Long appId = appFilters.get(i);
				appIds.add(appId);
			}
			
			try {
				appExcludeFacade.addAppExclude(userId, opUser, group.getPlanId(), groupId, appIds, optContents);
			} catch (ExcludeException e) {
				log.error(e.getMessage(), e);
			}
			
			alreadySaveGroupIdSet.add(groupId);
			pay.increSuccess();
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> addSitePrice(ApiResult<Object> result,
			GroupSitePriceType[] sitePrices) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将分网站出价分组，key为groupId
		Map<Long, List<GroupSitePriceItem>> groupSites = new HashMap<Long, List<GroupSitePriceItem>>();
		for (int index = 0; index < sitePrices.length; index++) {
			GroupSitePriceType sitePrice = sitePrices[index];
			Long groupId = sitePrice.getGroupId();

			List<GroupSitePriceItem> groupSitePriceList = groupSites.get(groupId);
			if (groupSitePriceList == null) {
				groupSitePriceList = new ArrayList<GroupSitePriceItem>();
				groupSites.put(groupId, groupSitePriceList);
			}

			groupSitePriceList.add(new GroupSitePriceItem(index, sitePrice.getSite(), sitePrice.getPrice()));
		}

		// 验证待添加分网站出价推广组的个数
		if (groupSites.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.SITE_PRICES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		// 依照推广组添加分网站出价
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupSites.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupSitePriceItem> groupSitePriceList = groupSites.get(groupId);

			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupSitePriceItem item : groupSitePriceList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.SITE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 验证分网站出价
			// 并将新增或者修改的的添加进toBeModifiedSitePriceList
			List<GroupSitePrice> toBeModifiedSitePriceList = new ArrayList<GroupSitePrice>();
			List<OptContent> tmpContents = new ArrayList<OptContent>();
			boolean flag = groupConfigValidator.validateAddSitePrice(group, groupSitePriceList,
					toBeModifiedSitePriceList, result, tmpContents);
			if (!flag || toBeModifiedSitePriceList.size() == 0) {
				// 校验失败则跳过
				continue;
			}
			
			if (CollectionUtils.isNotEmpty(toBeModifiedSitePriceList)) {
				boolean isSucc = groupSiteConfigMgr.addGroupSitePriceOnly(groupId.intValue(), 
						toBeModifiedSitePriceList, visitor);
				if (isSucc) {
					optContents.addAll(tmpContents);
					
					// 记录成功数
					int success = pay.getSuccess();
					pay.setSuccess(success + toBeModifiedSitePriceList.size());
				}
				
			}
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> addInterestInfo(ApiResult<Object> result,
			GroupInterestInfoType[] interestInfos){
		PaymentResult pay = result.getPayment();
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		int userId = bdUser.getUserid();
		
		// 依照推广组添加兴趣
		List<OptContent> optContents = new ArrayList<OptContent>();
		loop: for (int index = 0; index < interestInfos.length; index++) {
			
			GroupInterestInfoType item = interestInfos[index];
			Long groupIdLong = item.getGroupId();
			int groupId = groupIdLong.intValue();
			
			// 简单验证兴趣信息是否完整，包括传入的GroupId、interestIds、exceptInterestIds，不完整则不进行下一步
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.INTERESTS, index);
			String apiPositionParam = GroupConstant.INTERESTS + "[" + index + "]";
			
			if (item.getGroupId() < 1) {
				apiPosition.addParam(GroupConstant.GROUPID);
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.INTEREST_OR_GROUPID_NULL_ERROR.getValue(),
						GroupConfigErrorCode.INTEREST_OR_GROUPID_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				pay.increTotal(1);
				continue;
			}
			
			if( (item.getInterestIds() == null || item.getInterestIds().length == 0) && 
					(item.getExceptInterestIds() == null || item.getExceptInterestIds().length == 0)){
				apiPosition.addParam(GroupConstant.GROUPID);
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.INTEREST_OR_GROUPID_NULL_ERROR.getValue(),
						GroupConfigErrorCode.INTEREST_OR_GROUPID_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				pay.increTotal(1);
				continue;
			}

			CproGroup group = cproGroupMgr.findWithInfoById(groupId);
			int planId = group.getPlanId();
			
			// 推广组未启用兴趣，不能添加关联兴趣
			if (!TargettypeUtil.hasIT(group.getTargetType()) && (item.getInterestIds() != null && item.getInterestIds().length > 0)) {
				apiPosition.addParam(GroupConstant.GROUPID);
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.GROUP_IT_DISABLED.getValue(),
						GroupConfigErrorCode.GROUP_IT_DISABLED.getMessage(),
						apiPosition.getPosition(), null);
				pay.increTotal(1);
				continue;
			}
			
			List<Integer> interestIdsList = ListArrayUtils.asList(item.getInterestIds());
			List<Integer> exceptInterestIdsList = ListArrayUtils.asList(item.getExceptInterestIds());
			List<GroupItItem> interestIdsItemList = ItUtils.transItIdList2GroupItItemList(item.getInterestIds());
			List<GroupItItem> exceptInterestIdsItemList = ItUtils.transItIdList2GroupItItemList(item.getExceptInterestIds());
			pay.increTotal(interestIdsList.size() + exceptInterestIdsList.size());
			
			List<CproGroupIT> its = cproGroupITMgr.findGroupITList(groupId);
			Set<Integer> existItIdSet = ITUtils.extractInterestIds(its);
			Set<Integer> allItIdSet = ITUtils.extractInterestIds(its);
			allItIdSet.addAll(interestIdsList);
			
			List<CproGroupITExclude> exceptIts = cproGroupITMgr.findGroupITExcludeList(groupId);
			Set<Integer> existExceptItIdSet = ITUtils.extractInterestIds(exceptIts);
			Set<Integer> allExceptItIdSet = ITUtils.extractInterestIds(exceptIts);
			allExceptItIdSet.addAll(existExceptItIdSet);
			
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
			ItUtils.validateInterestNotDuplicate(result, interestIdsItemList, allExceptItIdSet, apiPositionParam, GroupConstant.INTEREST_IDS, false, validInterestIdsSet);
			ItUtils.validateInterestNotDuplicate(result, exceptInterestIdsItemList, allItIdSet, apiPositionParam, GroupConstant.EXCEPT_INTEREST_IDS, false, validExceptInterestIdsSet);
			ItUtils.validateInterestNotDuplicateInChildren(result, interestIdsItemList, allExceptItIdSet, cache, apiPositionParam, GroupConstant.INTEREST_IDS, false, validInterestIdsSet);
			ItUtils.validateInterestNotDuplicateInChildren(result, exceptInterestIdsItemList, allItIdSet, cache, apiPositionParam, GroupConstant.EXCEPT_INTEREST_IDS, false, validExceptInterestIdsSet);
			ItUtils.validateInterestNotDuplicateInChildren(result, interestIdsItemList, allItIdSet, cache, apiPositionParam, GroupConstant.INTEREST_IDS, false, validInterestIdsSet);
			ItUtils.validateInterestNotDuplicateInChildren(result, exceptInterestIdsItemList, allExceptItIdSet, cache, apiPositionParam, GroupConstant.EXCEPT_INTEREST_IDS, false, validExceptInterestIdsSet);
			ItUtils.validateInterestNotDuplicateInParent(result, interestIdsItemList, allItIdSet, cache, apiPositionParam, GroupConstant.INTEREST_IDS, false, validInterestIdsSet);
			ItUtils.validateInterestNotDuplicateInParent(result, exceptInterestIdsItemList, allExceptItIdSet, cache, apiPositionParam, GroupConstant.EXCEPT_INTEREST_IDS, false, validExceptInterestIdsSet);
			ItUtils.validateInterestNotDuplicateInParent(result, interestIdsItemList, allExceptItIdSet, cache, apiPositionParam, GroupConstant.INTEREST_IDS, false, validInterestIdsSet);
			ItUtils.validateInterestNotDuplicateInParent(result, exceptInterestIdsItemList, allItIdSet, cache, apiPositionParam, GroupConstant.EXCEPT_INTEREST_IDS, false, validExceptInterestIdsSet);
			ItUtils.validateInterestExist(result, interestIdsItemList, interestMap, customInterestMap, apiPositionParam, GroupConstant.INTEREST_IDS, false, validInterestIdsSet);
			ItUtils.validateInterestExist(result, exceptInterestIdsItemList, interestMap, customInterestMap, apiPositionParam, GroupConstant.EXCEPT_INTEREST_IDS, false, validExceptInterestIdsSet);
			
			StringBuilder logSb = new StringBuilder("### toAddValidInterestIdsNum=[")
				.append(validInterestIdsSet.size()).append("], toAddValidExceptInterestIdsNum=[")
				.append(validExceptInterestIdsSet.size()).append("], existInterestIdsNum=[")
				.append(existItIdSet.size()).append("], existExceptInterestIdsNum=[")
				.append(existExceptItIdSet.size()).append("]");
			log.info(logSb);
			
			pay.increSuccess(validInterestIdsSet.size() + validExceptInterestIdsSet.size());
			validInterestIdsSet.addAll(existItIdSet);
			validExceptInterestIdsSet.addAll(existExceptItIdSet);
			List<Integer> toSaveInterestIdsList = ListArrayUtils.asList(validInterestIdsSet);
			List<Integer> toSaveExceptInterestIdsList = ListArrayUtils.asList(validExceptInterestIdsSet);
		
			//去除每个推广组最多一次添加/删除100个关联兴趣点的限制
//			if (toSaveInterestIdsList != null && toSaveInterestIdsList.size() > InterestConstant.MAX_INTEREST_PER_GROUP) {
//				apiPosition.addParam(GroupConstant.GROUPID);
//				result = ApiResultBeanUtils.addApiError(result,
//						GroupConfigErrorCode.ADD_MAX_INTEREST_ERROR.getValue(),
//						GroupConfigErrorCode.ADD_MAX_INTEREST_ERROR.getMessage(),
//						apiPosition.getPosition(), null);
//				pay.increTotal(1);
//				continue;
//			}
//			if (toSaveExceptInterestIdsList != null && toSaveExceptInterestIdsList.size() > InterestConstant.MAX_EXCLUDE_INTEREST_PER_GROUP) {
//				apiPosition.addParam(GroupConstant.GROUPID);
//				result = ApiResultBeanUtils.addApiError(result,
//						GroupConfigErrorCode.ADD_MAX_EXCLUDE_INTEREST_ERROR.getValue(),
//						GroupConfigErrorCode.ADD_MAX_EXCLUDE_INTEREST_ERROR.getMessage(),
//						apiPosition.getPosition(), null);
//				pay.increTotal(1);
//				continue;
//			}
			
			//保存正向反向关联关系
			cproGroupITMgr.saveGroupIT(groupId, toSaveInterestIdsList, planId, userId, visitor.getUserid());
			cproGroupITMgr.saveGroupITExclude(groupId, toSaveExceptInterestIdsList, planId, userId, visitor.getUserid());
			
			/*
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
			*/
			
			optContents.addAll(OperationHistoryUtils.getOptContents());
			
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		return result;
	}
	
	public ApiResult<Object> deleteKeyword(ApiResult<Object> result,
			GroupKeywordItemType[] keywords) {
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
			
			if (item.getGroupId() < 1) {
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
			
			List<GroupKtItem> groupKeywordList = groupKeywords.get(groupId);
			if (groupKeywordList == null) {
				groupKeywordList = new ArrayList<GroupKtItem>();
				groupKeywords.put(groupId, groupKeywordList);
			}

			groupKeywordList.add(new GroupKtItem(index, item.getKeyword(), item.getPattern()));
		}

		// 验证待删除关键词推广组的个数
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
		
		// 依照推广组删除关键词
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

			Set<Long> toDelWordSet = new HashSet<Long>();
			Set<String> toDelWordStringSet = new HashSet<String>();

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
				
				if(!dbKeywordsMap.containsKey(word)){
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KEYWORD_NOT_FOUND_ERROR.getValue(),
							GroupConfigErrorCode.KEYWORD_NOT_FOUND_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				
				if (toDelWordStringSet.contains(word)) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.KT_WORDS_DUP.getValue(),
							GroupConfigErrorCode.KT_WORDS_DUP.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				toDelWordSet.add(dbKeywordsMap.get(word).getWordId());
				toDelWordStringSet.add(dbKeywordsMap.get(word).getKeyword());
			}

			if (toDelWordSet.size() > 0) {
				List<Long> toDelWordList = new ArrayList<Long>();
				toDelWordList.addAll(toDelWordSet);
				Map<Integer, List<Long>> groupId2WordIdListMap = new HashMap<Integer, List<Long>>();
				groupId2WordIdListMap.put(new Long(groupId).intValue(), toDelWordList);
		
				cproKeywordFacade.deleteMultiKeywords(groupId2WordIdListMap, bdUser.getUserid(), visitor, optContents);  //包含历史操作记录
				pay.increSuccess(toDelWordList.size());
				
				StringBuilder sb = new StringBuilder("### del kt keywords, size is ")
						.append(toDelWordList.size()).append(" userId=")
						.append(bdUser.getUserid()).append(", groupId=").append(groupId);
				log.info(sb);

			} else {
				continue;
			}
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> deleteVtPeople(ApiResult<Object> result,
			GroupVtItemType[] vtPeoples) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将vt人群分组，key为groupId
		Map<Long, List<GroupVtItem>> groupVtPeoples = new HashMap<Long, List<GroupVtItem>>();
		for (int index = 0; index < vtPeoples.length; index++) {
			GroupVtItemType vtPeople = vtPeoples[index];
			Long groupId = vtPeople.getGroupId();

			List<GroupVtItem> groupVtPeopleList = groupVtPeoples.get(groupId);
			if (groupVtPeopleList == null) {
				groupVtPeopleList = new ArrayList<GroupVtItem>();
				groupVtPeoples.put(groupId, groupVtPeopleList);
			}

			groupVtPeopleList.add(new GroupVtItem(index, 
					vtPeople.getRelationType(), 
					vtPeople.getPeopleId()));
		}

		// 验证待vt人群推广组的个数
		if (groupVtPeoples.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.VT_PEOPLES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		// 依照推广组添加vt人群
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupVtPeoples.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupVtItem> groupVtPeopleList = groupVtPeoples.get(groupId);

			// 推广组不存在
			if (group == null) {
				for (GroupVtItem item : groupVtPeopleList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.VT_PEOPLES, item.getIndex());
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
			if (!TargettypeUtil.hasVT(targetType)) {
				for (GroupVtItem item : groupVtPeopleList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.VT_PEOPLES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.VT_GROUP_TYPE_ERROR.getValue(),
							GroupConfigErrorCode.VT_GROUP_TYPE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 获取该用户的人群设置以及指定推广组的人群设置
			List<CproGroupVTVo> oldVtPeopleList = cproGroupVTMgr
					.findVTRelationByGroup(group.getGroupId(), group.getUserId());
			List<CproGroupVTVo> vtPeopleList = vtPeopleMgr.getAllVtPeople(group.getUserId());
			Set<Long> vtPeopleIdSet = new HashSet<Long>();
			for (CproGroupVTVo people : vtPeopleList) {
				vtPeopleIdSet.add(people.getId());
			}
			
			// 校验vt的peoples数据
			List<Long> includePeopleIds = new ArrayList<Long>();
			List<Long> excludePeopleIds = new ArrayList<Long>();
			Map<Long, CproGroupVTVo> oldVtPeopleMap = makeVTMap(oldVtPeopleList, groupId.intValue());
			
			for (GroupVtItem item : groupVtPeopleList) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.VT_PEOPLES, item.getIndex());
				
				// 该用户未设置请求中的人群
				if (!vtPeopleIdSet.contains(item.getPeopleId())) {
					apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.VT_PEOPLE_ERROR.getValue(),
							GroupConfigErrorCode.VT_PEOPLE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
					continue;
				}
				
				if (!oldVtPeopleMap.containsKey(item.getPeopleId())) {
					// 如果数据库中不存在该人群，则返回错误
					apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.VT_DEL_PEOPLE_ID_ERROR.getValue(),
							GroupConfigErrorCode.VT_DEL_PEOPLE_ID_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
					continue;
				} else {
					CproGroupVTVo oldVt = oldVtPeopleMap.get(item.getPeopleId());
					if (oldVt.getType() != item.getRelationType()) {
						// 如果数据库中存在该人群但关联类型不一致，则返回错误
						apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
						
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.VT_DEL_PEOPLE_ID_ERROR.getValue(),
								GroupConfigErrorCode.VT_DEL_PEOPLE_ID_ERROR.getMessage(), 
								apiPosition.getPosition(), null);
						continue;
					}
				}
				
				if (item.getRelationType() == CproGroupConstant.GROUP_VT_INCLUDE_CROWD) {
					// 请求中为关联人群
					if (includePeopleIds.contains(item.getPeopleId())) {
						apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
						
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getValue(),
								GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getMessage(), 
								apiPosition.getPosition(), null);
						continue;
					} else {
						includePeopleIds.add(item.getPeopleId());
					}
				} else if (item.getRelationType() == CproGroupConstant.GROUP_VT_EXCLUDE_CROWD) {
					// 请求中为排除人群
					if (excludePeopleIds.contains(item.getPeopleId())) {
						apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
						
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getValue(),
								GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getMessage(), 
								apiPosition.getPosition(), null);
						continue;
					} else {
						excludePeopleIds.add(item.getPeopleId());
					}
				} else {
					// 请求中为无效人群
					apiPosition.addParam(GroupConstant.VT_PEOPLE_ID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.VT_PEOPLE_ERROR.getValue(),
							GroupConfigErrorCode.VT_PEOPLE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
			}
			
			int total = includePeopleIds.size() + excludePeopleIds.size();
			if (total == 0) {
				continue;
			}
			
			Set<Long> deleteVtPeopleIdSet = new HashSet<Long>();
			deleteVtPeopleIdSet.addAll(includePeopleIds);
			deleteVtPeopleIdSet.addAll(excludePeopleIds);
			if (deleteVtPeopleIdSet.size() < total) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.VT_PEOPLES);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getValue(),
						GroupConfigErrorCode.VT_PEOPLE_CONTAINS_SAME_PEOPLE.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			List<CproGroupVTVo> deleteVtPeopleList = new ArrayList<CproGroupVTVo>();
			Map<Long, CproGroupVTVo> includePidMap = makeVTMap(includePeopleIds, true);
			Map<Long, CproGroupVTVo> excludePidMap = makeVTMap(excludePeopleIds, false);
			deleteVtPeopleList.addAll(includePidMap.values());
			deleteVtPeopleList.addAll(excludePidMap.values());
			
			Map<Long, CproGroupVTVo> deleteVtPeopleMap = makeVTMap(deleteVtPeopleList, groupId.intValue());
			
			List<CproGroupVTVo> deleteResult = deleteVtPeopleFromDb(deleteVtPeopleMap, oldVtPeopleMap, 
					groupId.intValue(),	group.getPlanId(), group.getUserId(), visitor);

			// 保存历史操作记录-START
			try {
				// 历史记录：添加RT关联关系
				if (CollectionUtils.isNotEmpty(deleteResult)) {
					// 批量初始化新建人群对应的“人群 名称”
					initVtPeopleName(deleteResult, group.getUserId(), group.getGroupId(), vtPeopleMgr, cproGroupVTMgr);
					oldVtPeopleList = new ArrayList<CproGroupVTVo>();
					Map<String, List<String>> includeChangeMap = getChangedPeopleNameMap(oldVtPeopleList,
							deleteResult, group.getGroupId(), CproGroupConstant.GROUP_VT_INCLUDE_CROWD);
					Map<String, List<String>> exCludeChangeMap = getChangedPeopleNameMap(oldVtPeopleList,
							deleteResult, group.getGroupId(), CproGroupConstant.GROUP_VT_EXCLUDE_CROWD);
					
					OpTypeVo opType = null;
					
					// 变动关联人群
					if ((includeChangeMap != null) 
							&& ((!CollectionUtils.isEmpty(includeChangeMap.get(VALUE_BEFORE))) 
									|| (!CollectionUtils.isEmpty(includeChangeMap.get(VALUE_AFTER))))) {
						opType = OptHistoryConstant.OPTYPE_GROUP_VTPEOPLE_ADD;
						OptContent content = new OptContent(group.getUserId(), 
								opType.getOpType(),	opType.getOpLevel(), group.getGroupId(), 
								opType.getTransformer().toDbString(includeChangeMap.get(VALUE_BEFORE)), 
								opType.getTransformer().toDbString(includeChangeMap.get(VALUE_AFTER)));
						optContents.add(content);
					}
					// 变动排除人群
					if ((exCludeChangeMap != null)
							&& ((!CollectionUtils.isEmpty(exCludeChangeMap.get(VALUE_BEFORE))) 
									|| (!CollectionUtils.isEmpty(exCludeChangeMap.get(VALUE_AFTER))))) {
						opType = OptHistoryConstant.OPTYPE_GROUP_VTPEOPLE_DELETE;
						OptContent content = new OptContent(group.getUserId(), 
								opType.getOpType(), opType.getOpLevel(), group.getGroupId(), 
								opType.getTransformer().toDbString(exCludeChangeMap.get(VALUE_BEFORE)), 
								opType.getTransformer().toDbString(exCludeChangeMap.get(VALUE_AFTER)));
						optContents.add(content);
					}
					
					// 记录成功数
					int success = pay.getSuccess();
					pay.setSuccess(success + deleteResult.size());
				}	
				// 历史记录：添加RT关联关系
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	private List<CproGroupVTVo> deleteVtPeopleFromDb(Map<Long, CproGroupVTVo> deleteVtPeopleMap,
			Map<Long, CproGroupVTVo> oldVtPeopleMap, Integer groupId, 
			Integer planId,	Integer userId, Visitor visitor) {
		List<CproGroupVTVo> result = new ArrayList<CproGroupVTVo>();
		
		if (CollectionUtils.isEmpty(deleteVtPeopleMap.keySet())) {
			return result;
		}
		
		LogUtils.businessInfo(visitor, "delele CproGroupVTPeople(1): ", 
				toCproGroupVTVoString(deleteVtPeopleMap.values()));
		
		// 当前包含待删除VT人群
		if (!CollectionUtils.isEmpty(deleteVtPeopleMap.entrySet())) {
			for (Long deletePid : deleteVtPeopleMap.keySet()) {
				CproGroupVT toUpdateVt = cproGroupVTDao.findById(oldVtPeopleMap.get(deletePid).getVtId());
				if (toUpdateVt == null) {
					log.error("can't find cproGroupVt, vtId=" + oldVtPeopleMap.get(deletePid));
					
					continue;
				}
				cproGroupVTDao.makeTransient(toUpdateVt);
				result.add(deleteVtPeopleMap.get(deletePid));
			}
		}
		
		LogUtils.businessInfo(visitor, "delele CproGroupVTPeople(2): ", 
				toCproGroupVTVoString(result));
		
		return result;
	}
	
	public ApiResult<Object> deleteSite(ApiResult<Object> result,
			GroupSiteType[] sites) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将site分组，key为groupId
		Map<Long, List<GroupSiteItem>> groupSites = new HashMap<Long, List<GroupSiteItem>>();
		for (int index = 0; index < sites.length; index++) {
			GroupSiteType site = sites[index];
			Long groupId = site.getGroupId();

			List<GroupSiteItem> groupSiteList = groupSites.get(groupId);
			if (groupSiteList == null) {
				groupSiteList = new ArrayList<GroupSiteItem>();
				groupSites.put(groupId, groupSiteList);
			}

			groupSiteList.add(new GroupSiteItem(index, site.getSite()));
		}

		// 验证待添加site推广组的个数
		if (groupSites.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.SITES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		
		// 依照推广组添加site
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupSites.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupSiteItem> groupSiteList = groupSites.get(groupId);

			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupSiteItem item : groupSiteList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.SITES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}

			// 该推广组为全网投放时不能进行添加site
			int isAllSite = group.getGroupInfo().getIsAllSite();
			if (isAllSite == CproGroupConstant.GROUP_ALLSITE) {
				for (GroupSiteItem item : groupSiteList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.SITES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.SITE_NOT_OPTIONAL_ERROR.getValue(),
							GroupConfigErrorCode.SITE_NOT_OPTIONAL_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			CproGroupInfo groupInfo = group.getGroupInfo();
			CproGroupInfo groupInfoBefore = new CproGroupInfo();
			try {
				BeanUtils.copyProperties(groupInfoBefore, groupInfo);
			} catch (Exception e) {
				log.error("failed to copy groupinfo. " + e.getMessage(), e);
			}

			// 获取该推广组已经添加的siteId
			List<Integer> selectedSiteIds = GroupSiteUtil.unConvertStrToList(groupInfo.getSiteListStr());
			
			// 获取该推广组已经添加的tradeId
			List<Integer> tradeIds = GroupSiteUtil.unConvertStrToList(groupInfo.getSiteTradeListStr());
			
			// 将数据库中的投放网站和待添加的网站合并为mergedSiteIds
			Set<Integer> siteSet = new HashSet<Integer>();
			siteSet.addAll(selectedSiteIds);
			
			List<Integer> toDelSiteIds = new ArrayList<Integer>();
			
			Map<String, Integer> siteIdMap = UnionSiteCache.siteInfoCache.getSiteIdMap();
			for (GroupSiteItem item : groupSiteList) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITES, item.getIndex());
				apiPosition.addParam(GroupConstant.SITE);
				
				String site = item.getSite().toLowerCase();
				Integer siteId = siteIdMap.get(site);	
				if (siteId != null) {
//					if (WhiteListCache.baiduSites.hasId(siteId)
//							&& !WhiteListCache.useBaiduUsers.has(bdUser.getUserid())) {
//						// 如果网站在网站白名单中，但是用户不在用户白名单中，则跳过此条并记录错误信息
//						result = ApiResultBeanUtils.addApiError(result,
//								GroupConfigErrorCode.NO_BAIDU_SITE_PRIVELEGE.getValue(),
//								GroupConfigErrorCode.NO_BAIDU_SITE_PRIVELEGE.getMessage(), 
//								apiPosition.getPosition(), null);
//					} else 
					if (!siteSet.contains(siteId)) {
						// 如果不存在，则报错
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.SITE_DEL_ERROR.getValue(),
								GroupConfigErrorCode.SITE_DEL_ERROR.getMessage(),
								apiPosition.getPosition(), null);
					} else {
						siteSet.remove(siteId);
						toDelSiteIds.add(siteId);
					}
				} else {
					BDSiteLiteInfo liteInfo = UnionSiteCache.allSiteLiteCache.getSiteLiteInfoByUrl(site);
					if (liteInfo == null) {
						// 该网站不存在
						result = ApiResultBeanUtils.addApiError(result,
								GroupConfigErrorCode.SITE_NOT_FOUND.getValue(),
								GroupConfigErrorCode.SITE_NOT_FOUND.getMessage(), 
								apiPosition.getPosition(), null);
//					} else if (WhiteListCache.baiduSites.hasId(siteId)
//							&& !WhiteListCache.useBaiduUsers.has(bdUser.getUserid())) {
//						// 如果网站在网站白名单中，但是用户不在用户白名单中，则跳过此条并记录错误信息
//						result = ApiResultBeanUtils.addApiError(result,
//								GroupConfigErrorCode.NO_BAIDU_SITE_PRIVELEGE.getValue(),
//								GroupConfigErrorCode.NO_BAIDU_SITE_PRIVELEGE.getMessage(), 
//								apiPosition.getPosition(), null);
					} else {			
						siteSet.remove(liteInfo.getSiteId());
						toDelSiteIds.add(liteInfo.getSiteId());
					}
				}
			}
			
			// 如果没有修改则返回
			if (selectedSiteIds.size() == siteSet.size()) {
				continue;
			}
			
			// 设置其他参数，如siteSum，cmpLevel等
			List<Integer> siteIds = new ArrayList<Integer>();
			siteIds.addAll(siteSet);
			// 必须先排序
			Collections.sort(siteIds);
			
			SiteSumInfo siteInfo = UnionSiteCalculator.genSiteInfo(siteIds,	tradeIds, 
					group.getUserId(), group.getGroupType());
			
			/**
			 * 一个推广组下的投放网站与投放行业可以为空
			if (CollectionUtils.isEmpty(siteInfo.getSiteList())
					&& CollectionUtils.isEmpty(siteInfo.getSiteTradeList())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITES);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_TRADE_NOT_NULL_ERROR.getValue(),
						GroupConfigErrorCode.SITE_TRADE_NOT_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			} 
			**/
			
			groupInfo.setSiteSum(siteInfo.getSiteSum());
			groupInfo.setCmpLevel(siteInfo.getCmpLevel());
			
			// 避免由于""被设到db中造成错误，并结算添加成功多少个
			int deleteNum = 0;
			String siteListStr2 = com.baidu.beidou.util.StringUtils.makeStrFromCollectionForSite(
					siteInfo.getSiteList(), CproGroupConstant.FIELD_SEPERATOR);
			if (siteListStr2 == null || siteListStr2.length() == 0) {
				siteListStr2 = null;
				deleteNum = selectedSiteIds.size();
			} else {
				int resultNum = siteInfo.getSiteList().size();
				if (resultNum < selectedSiteIds.size()) {
					deleteNum = selectedSiteIds.size() - resultNum;
				}
			}
			groupInfo.setSiteListStr(siteListStr2);

			List<GroupSitePrice> sitePrice = groupSiteConfigMgr
					.findSitePriceByGroupIdAndSiteIds(groupId.intValue(), toDelSiteIds);
			List<GroupSitePrice> toUpdateSitePrice = new ArrayList<GroupSitePrice>();
			List<GroupSitePrice> toDeleteSitePrice = new ArrayList<GroupSitePrice>();
			List<OptContent> tmpOpt = new ArrayList<OptContent>(sitePrice.size());
			OpTypeVo opTypeSitePrice = OptHistoryConstant.OPTYPE_GROUP_SITE_PRICE_BATCH_DELETE;
			for (GroupSitePrice groupSitePrice : sitePrice) {
				GroupTradeSitePriceOptVo before = new GroupTradeSitePriceOptVo();
				before.setId(groupSitePrice.getSiteid());
				before.setPrice(groupSitePrice.getPrice());
				OptContent op = new OptContent(group.getUserId(), opTypeSitePrice.getOpType(),
						opTypeSitePrice.getOpLevel(), group.getGroupId(),
						opTypeSitePrice.getTransformer().toDbString(before), null);
				tmpOpt.add(op);
				if (groupSitePrice.getTargeturl() == null) {
					toDeleteSitePrice.add(groupSitePrice);
				} else {
					groupSitePrice.setPrice(null);
					toUpdateSitePrice.add(groupSitePrice);
				}
			}
			this.cproGroupMgr.modSiteTradeInfo(visitor, group.getGroupInfo(),
					toUpdateSitePrice, toDeleteSitePrice, null);
			
			// 保存历史操作记录-START
			try {
				// 历史记录：删除投放网站
				// 记录删除投放网站
				OpTypeVo opTypeSite = OptHistoryConstant.OPTYPE_GROUP_SITE_EDIT;
				OptContent contentSite = new OptContent(group.getUserId(), 
						opTypeSite.getOpType(), opTypeSite.getOpLevel(), group.getGroupId(), 
						opTypeSite.getTransformer().toDbString(new GroupSiteOptVo(groupInfoBefore)),  
						opTypeSite.getTransformer().toDbString(new GroupSiteOptVo(groupInfo)));
				if (!contentSite.getPreContent().equals(contentSite.getPostContent())) {
					optContents.add(contentSite);
				}
				
				// tmpOpt记录了删除的投放网站出价设置
				optContents.addAll(tmpOpt);
				
				// 记录成功数
				int success = pay.getSuccess();
				pay.setSuccess(success + deleteNum);
				
				// 历史记录：：删除投放网站
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> deleteTrade(ApiResult<Object> result,
			GroupTradeType[] trades) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将trade分组，key为groupId
		Map<Long, List<GroupTradeItem>> groupTrades = new HashMap<Long, List<GroupTradeItem>>();
		for (int index = 0; index < trades.length; index++) {
			GroupTradeType trade = trades[index];
			Long groupId = trade.getGroupId();

			List<GroupTradeItem> groupTradeList = groupTrades.get(groupId);
			if (groupTradeList == null) {
				groupTradeList = new ArrayList<GroupTradeItem>();
				groupTrades.put(groupId, groupTradeList);
			}

			groupTradeList.add(new GroupTradeItem(index, trade.getTrade()));
		}

		// 验证待删除trade推广组的个数
		if (groupTrades.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TRADES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		
		// 依照推广组删除trade
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupTrades.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupTradeItem> groupTradeList = groupTrades.get(groupId);

			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupTradeItem item : groupTradeList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}

			// 该推广组为全网投放时不能进行添加trade
			int isAllSite = group.getGroupInfo().getIsAllSite();
			if (isAllSite == CproGroupConstant.GROUP_ALLSITE) {
				for (GroupTradeItem item : groupTradeList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TRADE_NOT_OPTIONAL_ERROR.getValue(),
							GroupConfigErrorCode.TRADE_NOT_OPTIONAL_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			CproGroupInfo groupInfo = group.getGroupInfo();
			CproGroupInfo groupInfoBefore = new CproGroupInfo();
			try {
				BeanUtils.copyProperties(groupInfoBefore, groupInfo);
			} catch (Exception e) {
				log.error("failed to copy groupinfo. " + e.getMessage(), e);
			}

			// 获取该推广组已经添加的siteId
			List<Integer> siteIds = GroupSiteUtil.unConvertStrToList(groupInfo.getSiteListStr());
			Set<Integer> chosenSiteSet = new HashSet<Integer>(siteIds.size());
			chosenSiteSet.addAll(siteIds);
			
			// 获取该推广组已经添加的tradeId
			List<Integer> selectedTradeIds = GroupSiteUtil.unConvertStrToList(groupInfo.getSiteTradeListStr());
			
			// 贴片推广组不支持设置分类
			// @version cpweb443 仅为贴片推广组时不能添加行业
			if (group.getGroupType() == CproGroupConstant.GROUP_TYPE_FILM) {
				for (GroupTradeItem item : groupTradeList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TRADE_GROUP_TYPE_ERROR.getValue(),
							GroupConfigErrorCode.TRADE_GROUP_TYPE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 数据库中不会出现一级行业和二级行业同时被选中的情况
			Set<Integer> tradeSet = new HashSet<Integer>(selectedTradeIds.size());
			tradeSet.addAll(selectedTradeIds);
			List<Integer> toRemovePriceTrades = new ArrayList<Integer>();
			
			// 删除数据库中存在的投放行业
			int deleteNum = 0;
			for (GroupTradeItem item : groupTradeList) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADES, item.getIndex());
				apiPosition.addParam(GroupConstant.TRADE);
				
				Integer tradeId = item.getTrade();
				TradeInfo tradeInfo = UnionSiteCache.tradeInfoCache.getTradeInfoById(tradeId);
				if (tradeInfo == null) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(),
							GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
//				} else if (WhiteListCache.baiduTrades.has(tradeId)
//						&& !WhiteListCache.useBaiduUsers.has(bdUser.getUserid())) {
//					// 如果行业在行业白名单中，但是用户不在用户白名单中，则跳过此条并记录错误信息
//					result = ApiResultBeanUtils.addApiError(result,
//							GroupConfigErrorCode.NO_BAIDU_TRADE_PRIVELEGE.getValue(),
//							GroupConfigErrorCode.NO_BAIDU_TRADE_PRIVELEGE.getMessage(),
//							apiPosition.getPosition(), null);
				} else {
					if (tradeInfo.getParentid() == 0) {
						// 删除一级行业
						if (tradeSet.contains(tradeId)) {
							// 如该推广组已选中了该一级行业，则删除该行业、该行业出价以及该一级行业下二级行业的出价
							tradeSet.remove(tradeId);
							toRemovePriceTrades.add(tradeId);
							// 因为数据库中一二级不可能同时选中，如果删除一级，那么二级肯定是没有的，这时候，一级下面的二级出价也需要删除
							List<Integer> children = UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildren().get(tradeId);
							if (CollectionUtils.isNotEmpty(children)){
								toRemovePriceTrades.addAll(children);
							}
							deleteNum++;
						} else {
							// 假如该一级行业未选中则报错
							result = ApiResultBeanUtils.addApiError(result,
									GroupConfigErrorCode.TRADE_DEL_ERROR.getValue(),
									GroupConfigErrorCode.TRADE_DEL_ERROR.getMessage(), 
									apiPosition.getPosition(), null);
						}
					} else {
						// 删除二级行业
						if (tradeSet.contains(tradeId)) {
							// 首先判断是否选过这个二级行业，如果选过，直接删除
							tradeSet.remove(tradeId);
							toRemovePriceTrades.add(tradeId);
							deleteNum++;
						} else if (tradeSet.contains(tradeInfo.getParentid())) {
							// 如果选中了这个二级行业所属的一级行业，则要把一级删除，再把其下其余的二级（不包含“其他”）再加回去
							// 这块顺序不会出现问题
							tradeSet.remove(tradeInfo.getParentid());
							toRemovePriceTrades.add(tradeInfo.getTradeid());
							toRemovePriceTrades.add(tradeInfo.getParentid());
							List<Integer> subTradeList = UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildren().get(tradeInfo.getParentid());
							tradeSet.addAll(subTradeList);
							tradeSet.remove(tradeId);
							deleteNum++;
						} else {
							// 假如该二级行业未选中则报错
							result = ApiResultBeanUtils.addApiError(result,
									GroupConfigErrorCode.TRADE_DEL_ERROR.getValue(),
									GroupConfigErrorCode.TRADE_DEL_ERROR.getMessage(), 
									apiPosition.getPosition(), null);
						}
					}
				}
			}
			
			// 处理推广组网站和行业信息
			List<Integer> tradeIds = new ArrayList<Integer>();
			tradeIds.addAll(tradeSet);
			// 必须先排序
			Collections.sort(tradeIds);
			SiteSumInfo siteInfo = UnionSiteCalculator.genSiteInfo(siteIds,	tradeIds, 
					group.getUserId(), group.getGroupType());

			/**
			 * 一个推广组下的投放网站与投放行业可以为空
			if (CollectionUtils.isEmpty(siteInfo.getSiteList())
					&& CollectionUtils.isEmpty(siteInfo.getSiteTradeList())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADES);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_TRADE_NOT_NULL_ERROR.getValue(),
						GroupConfigErrorCode.SITE_TRADE_NOT_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}
			*/
			
			// 需要将全部SitePrice取出
			List<GroupSitePrice> sitePrice = groupSiteConfigMgr.findSitePriceByGroupId(group.getGroupId());
			List<GroupSitePrice> toRemoveSitePrice = new ArrayList<GroupSitePrice>();
			List<GroupSitePrice> toUpdateSitePrice = new ArrayList<GroupSitePrice>();
			List<OptContent> tmpOpt = new ArrayList<OptContent>();
			
			// 处理全部的SitePrice
			OpTypeVo opTypeDelSitePrice = OptHistoryConstant.OPTYPE_GROUP_SITE_PRICE_BATCH_DELETE;
			for (GroupSitePrice gsp : sitePrice) {
				if (chosenSiteSet.contains(gsp.getSiteid())) {
					// 对于还在自选网站列表的，仍然保留
					continue;
				}
				BDSiteInfo bdSiteInfo = UnionSiteCache.siteInfoCache.getSiteInfoBySiteId(gsp.getSiteid());
				if (bdSiteInfo == null) {
					// 对于已经不在站点全库中，由脚本处理，这里不管
					continue;
				}
				if (tradeSet.contains(bdSiteInfo.getSecondtradeid())
						|| tradeSet.contains(bdSiteInfo.getFirsttradeid())) {
					// 对于被选择了行业的网站出价，仍然保留
					continue;
				}
				GroupTradeSitePriceOptVo before = new GroupTradeSitePriceOptVo();
				before.setId(gsp.getSiteid());
				before.setPrice(gsp.getPrice());
				OptContent op = new OptContent(group.getUserId(), opTypeDelSitePrice.getOpType(), 
						opTypeDelSitePrice.getOpLevel(), group.getGroupId(),
						opTypeDelSitePrice.getTransformer().toDbString(before),	null);
				tmpOpt.add(op);
				if (gsp.getTargeturl() == null) {
					toRemoveSitePrice.add(gsp);
				} else {
					gsp.setPrice(null);
					toUpdateSitePrice.add(gsp);
				}
			}
			
			// 设置其他参数，如siteSum，cmpLevel等
			groupInfo.setSiteSum(siteInfo.getSiteSum());
			groupInfo.setCmpLevel(siteInfo.getCmpLevel());
			
			// 避免由于""被设到db中造成错误
			String tradeListStr2 = com.baidu.beidou.util.StringUtils.makeStrFromCollectionForSite(
					siteInfo.getSiteTradeList(), CproGroupConstant.FIELD_SEPERATOR);
			if (tradeListStr2 == null || tradeListStr2.length() == 0) {
				tradeListStr2 = null;
			}
			groupInfo.setSiteTradeListStr(tradeListStr2);
			
			// 修改行业设置
			List<GroupTradePrice> tradePrice = groupSiteConfigMgr
					.findTradePriceByGroupIdAndTradeIds(groupId.intValue(), toRemovePriceTrades);
			cproGroupMgr.modSiteTradeInfo(visitor, group.getGroupInfo(), toUpdateSitePrice, toRemoveSitePrice, tradePrice);

			// 保存历史操作记录-START
			try {
				// 历史记录：删除投放行业
				// 记录删除投放行业
				OpTypeVo opTypeTrade = OptHistoryConstant.OPTYPE_GROUP_TRADE_EDIT;
				OptContent contentTrade = new OptContent(group.getUserId(), 
						opTypeTrade.getOpType(), opTypeTrade.getOpLevel(), group.getGroupId(), 
						opTypeTrade.getTransformer().toDbString(new GroupSiteOptVo(groupInfoBefore)),  
						opTypeTrade.getTransformer().toDbString(new GroupSiteOptVo(groupInfo)));
				if (!contentTrade.getPreContent().equals(contentTrade.getPostContent())) {
					optContents.add(contentTrade);
				}
				
				// 记录删除投放行业出价
				OpTypeVo opTypeDelTradePrice = OptHistoryConstant.OPTYPE_GROUP_TRADE_PRICE_BATCH_DELETE;
				if(CollectionUtils.isNotEmpty(tradePrice)){
					for(GroupTradePrice groupTradePrice : tradePrice){
						GroupTradeSitePriceOptVo before = new GroupTradeSitePriceOptVo();
						before.setId(groupTradePrice.getTradeid());
						before.setPrice(groupTradePrice.getPrice());
						OptContent op = new OptContent(group.getUserId(), opTypeDelTradePrice.getOpType(), 
								opTypeDelTradePrice.getOpLevel(), group.getGroupId(),
								opTypeDelTradePrice.getTransformer().toDbString(before), null);
						optContents.add(op);
					}
				}
				
				// tmpOpt记录了删除的投放网站出价设置
				optContents.addAll(tmpOpt);
				
				// 记录成功数
				int success = pay.getSuccess();
				pay.setSuccess(success + deleteNum);
				
				// 历史记录：删除投放行业
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
    @Override
    public boolean setRegion(ApiResult<Object> result, RegionConfigType regionConfig) {
        int groupId = (int) regionConfig.getGroupId();

        CproGroup group = cproGroupMgr.findWithInfoById(groupId);
        if (group == null) {
            GroupErrorCode.NOT_FOUND.getErrorResponse(result, Lists.newArrayList(new ErrorParam(
                    GroupConstant.REGION_CONFIG_REQ, null), new ErrorParam(GroupConstant.GROUPID, null)), null);
            return false;
        }

        CproGroupInfo groupInfo = group.getGroupInfo();
        String preRegionStr = groupInfo.getSysRegListStr();
        Integer preIsAllRegion = groupInfo.getIsAllRegion();
        Integer preRegionSum = groupInfo.getRegSum();

        boolean isNotUpdateRegion = false;
        if (regionConfig.getRegionList() != null && regionConfig.getRegionList().length == 1
                && regionConfig.getRegionList()[0].getRegionId() == GroupConstant.API_GLOBAL_NOT_UPDATE_FLAG_INT) {
            isNotUpdateRegion = true;
        }

        if (regionConfig.isAllRegion()) {
            groupInfo.setIsAllRegion(CproGroupConstant.GROUP_ALLREGION);
            if (!isNotUpdateRegion) {
                groupInfo.setSysRegListStr("");
                groupInfo.setRegListStr(null);
                groupInfo.setRegSum(0);
            }
        } else {
            if (regionConfig.getRegionList() == null || regionConfig.getRegionList().length == 0) {
                GlobalErrorCode.UNEXPECTED_PARAMETER.getErrorResponse(result,
                        Lists.newArrayList(new ErrorParam(GroupConstant.REGION_CONFIG_REQ, null), new ErrorParam(
                                GroupConstant.REGION_CONFIG_TYPE, null)), null);
                return false;
            }

            groupInfo.setIsAllRegion(0);

            if (!isNotUpdateRegion) {
                Set<Integer> allRegionIds = this.getEffectiveRegion(result, regionConfig.getRegionList());

                groupInfo.setRegSum(getFirstRegionCount(allRegionIds));
                groupInfo.setSysRegListStr(getRegionStr(allRegionIds));
                groupInfo.setRegListStr(RegionIdConverter.getBeidouRegListStrFromSys(groupInfo.getSysRegListStr()));
            } else {
                if (groupInfo.getRegSum() == 0) {
                    GroupConfigErrorCode.REGION_SHOULD_NOT_BE_EMPTY.getErrorResponse(result, Lists.newArrayList(
                            new ErrorParam(GroupConstant.REGION_CONFIG_REQ, null), new ErrorParam(
                                    GroupConstant.REGION_CONFIG_TYPE, null)), null);
                    return false;
                }
            }
        }

        Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
        cproGroupMgr.modCproGroupInfo(visitor, groupInfo);

        // 记录操作历史纪录
        List<OptContent> optContents = new ArrayList<OptContent>();
        OpTypeVo opType = OptHistoryConstant.OPTYPE_GROUP_REGION_BATCH;
        OptContent content =
                new OptContent(group.getUserId(), opType.getOpType(), opType.getOpLevel(), group.getGroupId(), opType
                        .getTransformer().toDbString(new GroupRegionOptVo(preIsAllRegion, preRegionSum, preRegionStr)),
                        opType.getTransformer().toDbString(new GroupRegionOptVo(groupInfo)));
        optContents.add(content);
        SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); // 加入session中，后续有拦截器处理

        StringBuilder sb =
                new StringBuilder("### save RegionConfig, ").append(", groupId=").append(groupId).append(", userId=")
                        .append(group.getUserId()).append(", isAllRegion is ").append(regionConfig.isAllRegion());
        sb.append(", regionList is [");
        if (regionConfig.getRegionList() != null) {
            for (RegionItemType regionItem : regionConfig.getRegionList()) {
                if (regionItem != null) {
                    sb.append(regionItem.getRegionId()).append(",");
                }
            }
        }
        sb.append("]");
        log.info(sb);

        return true;
    }
	
    @Override
    public ApiResult<Object> deleteRegion(ApiResult<Object> result, HashMultimap<Long, GroupRegionItem> regionMap) {
        PaymentResult pay = result.getPayment();

        // 获得用户信息
        Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

        Set<Long> groupIds = regionMap.keySet();
        List<OptContent> optContents = new ArrayList<OptContent>(groupIds.size());
        for (Long groupId : groupIds) {
            CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
            Set<GroupRegionItem> groupRegionList = regionMap.get(groupId);

            if (!regionUpdateGroupCheck(result, group, groupRegionList)) {
                continue;
            }

            CproGroupInfo groupInfo = group.getGroupInfo();
            int deleteNum = 0;
            // 获取该推广组已经添加的regionId
            String preRegListStr = groupInfo.getSysRegListStr();
            Integer preRegSum = groupInfo.getRegSum();
            Set<Integer> selectedRegionIds = GroupSiteUtil.unConvertStrToSet(preRegListStr);
            for (GroupRegionItem item : groupRegionList) {
                Integer[] regInfo = sysRegCache.getRegInfoMap().get(item.getRegionId());
                if (regInfo == null || regInfo.length != 2) {
                    GroupConfigErrorCode.REGION_NOT_FOUND
                            .getErrorResponse(result, Lists.newArrayList(
                                    new ErrorParam(GroupConstant.REGIONS, item.getIndex()), new ErrorParam(
                                            GroupConstant.GROUPID, null)), null);
                    continue;
                }

                if (regInfo[0] == 0) {
                    // 删除一级地域
                    if (selectedRegionIds.contains(item.getRegionId())) {
                        // 首先判断是否选过这个一级地域，如果选过，直接删除
                        selectedRegionIds.remove(item.getRegionId());
                        deleteNum++;
                    } else {
                        // 假如该一级地域未选中则报错
                        GroupConfigErrorCode.REGION_DEL_ERROR.getErrorResponse(result, Lists.newArrayList(
                                new ErrorParam(GroupConstant.REGIONS, item.getIndex()), new ErrorParam(
                                        GroupConstant.REGION_ID, null)), null);
                    }
                } else {
                    // 删除二级地域
                    if (selectedRegionIds.contains(item.getRegionId())) {
                        // 首先判断是否选过这个二级地域，如果选过，直接删除
                        selectedRegionIds.remove(item.getRegionId());
                        deleteNum++;
                    } else if (selectedRegionIds.contains(regInfo[0])) {
                        // 如果选中了这个二级地域所属的一级地域，则要把一级删除，再把其下其余的二级再加回去
                        // 这块顺序不能出现问题
                        selectedRegionIds.remove(regInfo[0]);
                        List<Integer> children = sysRegCache.getFirstLevelRegChildren().get(regInfo[0]);
                        selectedRegionIds.addAll(children);
                        selectedRegionIds.remove(item.getRegionId());
                        deleteNum++;
                    } else {
                        // 假如该二级地域未选中则报错
                        GroupConfigErrorCode.REGION_DEL_ERROR.getErrorResponse(result, Lists.newArrayList(
                                new ErrorParam(GroupConstant.REGIONS, item.getIndex()), new ErrorParam(
                                        GroupConstant.REGION_ID, null)), null);
                    }
                }
            }

            if (CollectionUtils.isEmpty(selectedRegionIds)) {
                for (GroupRegionItem item : groupRegionList) {
                    GroupConfigErrorCode.REGION_NOT_NULL_ERROR.getErrorResponse(result,
                            Lists.newArrayList(new ErrorParam(GroupConstant.REGIONS, item.getIndex())), null);
                }
                continue;
            }

            groupInfo.setRegSum(getFirstRegionCount(selectedRegionIds));
            groupInfo.setSysRegListStr(getRegionStr(selectedRegionIds));
            groupInfo.setRegListStr(RegionIdConverter.getBeidouRegListStrFromSys(groupInfo.getSysRegListStr()));
            cproGroupMgr.modCproGroupInfo(visitor, groupInfo);

            // 记录操作历史纪录
            OpTypeVo opTypeRegion = OptHistoryConstant.OPTYPE_GROUP_REGION_BATCH;
            OptContent contentRegion =
                    new OptContent(group.getUserId(), opTypeRegion.getOpType(), opTypeRegion.getOpLevel(),
                            group.getGroupId(), opTypeRegion.getTransformer().toDbString(
                                    new GroupRegionOptVo(groupInfo.getIsAllRegion(), preRegSum, preRegListStr)),
                            opTypeRegion.getTransformer().toDbString(new GroupRegionOptVo(groupInfo)));
            if (!contentRegion.getPreContent().equals(contentRegion.getPostContent())) {
                optContents.add(contentRegion);
            }

            pay.setSuccess(pay.getSuccess() + deleteNum);
        }
        SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); // 加入session中，后续有拦截器处理

        return result;
    }
	
	public ApiResult<Object> deleteExcludeIp(ApiResult<Object> result,
			GroupExcludeIpType[] excludeIps) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将excludeIp分组，key为groupId
		Map<Long, List<GroupExcludeIpItem>> groupExcludeIps = new HashMap<Long, List<GroupExcludeIpItem>>();
		for (int index = 0; index < excludeIps.length; index++) {
			GroupExcludeIpType excludeIp = excludeIps[index];
			Long groupId = excludeIp.getGroupId();

			List<GroupExcludeIpItem> groupExcludeIpList = groupExcludeIps.get(groupId);
			if (groupExcludeIpList == null) {
				groupExcludeIpList = new ArrayList<GroupExcludeIpItem>();
				groupExcludeIps.put(groupId, groupExcludeIpList);
			}

			groupExcludeIpList.add(new GroupExcludeIpItem(index, excludeIp.getExcludeIp().toLowerCase()));
		}

		// 验证待删除excludeIp推广组的个数
		if (groupExcludeIps.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_IPS);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		// 依照推广组删除excludeIp
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupExcludeIps.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupExcludeIpItem> groupExcludeIpList = groupExcludeIps.get(groupId);

			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupExcludeIpItem item : groupExcludeIpList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_IPS, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 获取该推广组已经添加的excludeIp
			List<GroupIpFilter> curExcludeIps = groupSiteConfigMgr.findIpFilterByGroupId(groupId.intValue());
			List<String> selectedExcludeIps = this.findExcludeIpStringList(curExcludeIps);
			
			// 并将待删除的添加进deletedExcludeIps
			List<String> deletedExcludeIps = new ArrayList<String>();
			boolean flag = groupConfigValidator.validateDeleteExcludeIp(groupExcludeIpList, 
					selectedExcludeIps, deletedExcludeIps, result);
			if (!flag || deletedExcludeIps.size() == 0) {
				// 校验失败则跳过
				continue;
			}
			
			// 删除待删除的过滤IP
			// 注意：deletedExcludeIps是curExcludeIps的子集，由上面validateDeleteExcludeIp()函数来保证
			for (GroupIpFilter excludeIp : curExcludeIps) {
				String ip = excludeIp.getIp().toLowerCase();
				if (deletedExcludeIps.contains(ip)) {
					groupSiteConfigMgr.delGroupIpFilter(excludeIp.getId(), visitor);
				}
			}

			// 保存历史操作记录-START
			try {
				// 历史记录：删除过滤IP
				OpTypeVo opTypeRegion = OptHistoryConstant.OPTYPE_GROUP_IPFILTER_BATCH;
				OptContent contentRegion = new OptContent();
				contentRegion.setUserid(group.getUserId());
				contentRegion.setOpObjId(group.getGroupId());
				contentRegion.setOpType(opTypeRegion.getOpType());
				contentRegion.setOpLevel(opTypeRegion.getOpLevel());
				
				String preContent = opTypeRegion.getTransformer().toDbString(selectedExcludeIps);
				contentRegion.setPreContent(preContent);//保存之前的列表
				
				List<GroupIpFilter> addedExcludeIpList = groupSiteConfigMgr.findIpFilterByGroupId(groupId.intValue());
				List<String> excludeIpList = null; //临时对象
				excludeIpList = this.findExcludeIpStringList(addedExcludeIpList);
				String postContent = opTypeRegion.getTransformer().toDbString(excludeIpList);
				contentRegion.setPostContent(postContent);//保存之后的列表
				
				optContents.add(contentRegion);
				
				// 记录成功数
				int success = pay.getSuccess();
				pay.setSuccess(success + deletedExcludeIps.size());
				
				// 历史记录：删除过滤IP
				
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> deleteExcludeSite(ApiResult<Object> result,
			GroupExcludeSiteType[] excludeSites) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将excludeSite分组，key为groupId
		Map<Long, List<GroupExcludeSiteItem>> groupExcludeSites = new HashMap<Long, List<GroupExcludeSiteItem>>();
		for (int index = 0; index < excludeSites.length; index++) {
			GroupExcludeSiteType excludeSite = excludeSites[index];
			Long groupId = excludeSite.getGroupId();

			List<GroupExcludeSiteItem> groupExcludeSiteList = groupExcludeSites.get(groupId);
			if (groupExcludeSiteList == null) {
				groupExcludeSiteList = new ArrayList<GroupExcludeSiteItem>();
				groupExcludeSites.put(groupId, groupExcludeSiteList);
			}

			groupExcludeSiteList.add(new GroupExcludeSiteItem(index, excludeSite.getExcludeSite().toLowerCase()));
		}

		// 验证待删除excludeSite推广组的个数
		if (groupExcludeSites.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_SITES);
			
			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		// 依照推广组删除excludeSite
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupExcludeSites.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupExcludeSiteItem> groupExcludeSiteList = groupExcludeSites.get(groupId);

			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupExcludeSiteItem item : groupExcludeSiteList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_SITES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 获取该推广组已经添加的excludeSite
			List<GroupSiteFilter> curExcludeSites = groupSiteConfigMgr.findSiteFilterByGroupId(groupId.intValue());
			List<String> selectedExcludeSites = this.findExcludeSiteStringList(curExcludeSites);
			
			// 并将删除的添加进addedExcludeSites
			List<String> deletedExcludeSites = new ArrayList<String>();
			boolean flag = groupConfigValidator.validateDeleteExcludeSite(groupExcludeSiteList, 
					selectedExcludeSites, deletedExcludeSites, result);
			if (!flag || deletedExcludeSites.size() == 0) {
				// 校验失败则跳过
				continue;
			}
			
			// 删除待删除的过滤网站
			// 注意：deletedExcludeSites是curExcludeSites的子集，由上面validateDeleteExcludeSite()函数来保证
			for (GroupSiteFilter excludeSite : curExcludeSites) {
				String site = excludeSite.getSite().toLowerCase();
				if (deletedExcludeSites.contains(site)) {
					groupSiteConfigMgr.delGroupSiteFilter(excludeSite.getId(), visitor);
				}
			}

			// 保存历史操作记录-START
			try {
				// 历史记录：删除过滤网站
				OpTypeVo opTypeVoAdd = OptHistoryConstant.OPTYPE_GROUP_SITEFILTER_BATCH_DELETE;			
				OptContent contentExcludeSite = new OptContent(group.getUserId(), 
						opTypeVoAdd.getOpType(), opTypeVoAdd.getOpLevel(), groupId,	 
						opTypeVoAdd.getTransformer().toDbString(new ArrayList(deletedExcludeSites)), null);
				
				optContents.add(contentExcludeSite);
				
				// 记录成功数
				int success = pay.getSuccess();
				pay.setSuccess(success + deletedExcludeSites.size());
				
				// 历史记录：删除过滤网站
			} catch (Exception e) {
				log.error("failed to construct opt history content. " + e.getMessage(), e);
			}
			// 保存历史操作记录-END
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> deleteExcludeApp(ApiResult<Object> result,
			List<GroupExcludeAppType> excludeApps, int userId, int opUser) {
		PaymentResult pay = result.getPayment();
		
		// 依照推广组添加excludeApp
		List<OptContent> optContents = new ArrayList<OptContent>();
		//Map<String, Integer> appName2IdCache = UnionSiteCache.appCache.getReverseIndexByAppName();
		Map<Long,Integer> appId2IdCache = UnionSiteCache.appCache.getReverseIndexByAppId();
		Set<Integer> alreadySaveGroupIdSet = new HashSet<Integer>();
		
		start:
		for (int index = 0; index < excludeApps.size(); index++) {
			Integer groupId = new Long(excludeApps.get(index).getGroupId()).intValue();
			List<Long> appFilters = excludeApps.get(index).getExcludeApp();
			
			if (alreadySaveGroupIdSet.contains(groupId)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_APPS, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.EXCLUDE_APP_GROUP_DUP.getValue(),
						GroupConfigErrorCode.EXCLUDE_APP_GROUP_DUP.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			
			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_APPS, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupErrorCode.NOT_FOUND.getValue(),
						GroupErrorCode.NOT_FOUND.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			Map<Long, Long> sid2IdMap = new HashMap<Long, Long>();
			List<Integer> groupIds = new ArrayList<Integer>();
			groupIds.add(groupId);
			List<AppExclude> groupAppExclude = appExcludeMgr.findAppExcludeByCondition(userId, groupIds, null);
			for (AppExclude appExclude : groupAppExclude) {
				sid2IdMap.put(appExclude.getAppSid(), appExclude.getId());
			}
			
			List<Long> delIds = new ArrayList<Long>();
			for (int i = 0; i < appFilters.size(); i++) {
				if (!appId2IdCache.containsKey(appFilters.get(i))) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_APPS, index);
					apiPosition.addParam(GroupConstant.EXCLUDE_APP, i);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.EXCLUDE_APP_NOT_FOUND.getValue(),
							GroupConfigErrorCode.EXCLUDE_APP_NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
					continue start;
				}
				AppInfo appInfo = UnionSiteCache.appCache.getAppInfoById(appFilters.get(i));
				if (sid2IdMap.containsKey(appInfo.getSid())) {
					delIds.add(sid2IdMap.get(appInfo.getSid()));
				} else {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_APPS, index);
					apiPosition.addParam(GroupConstant.EXCLUDE_APP, i);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.EXCLUDE_APP_NOT_FOUND_IN_GROUP.getValue(),
							GroupConfigErrorCode.EXCLUDE_APP_NOT_FOUND_IN_GROUP.getMessage(), 
							apiPosition.getPosition(), null);
					continue start;
				}
			}
			
			try {
				appExcludeFacade.delAppExclude(userId, group.getPlanId(), groupId, delIds, optContents);
			} catch (ExcludeException e) {
				log.error(e.getMessage(), e);
			}
			
			alreadySaveGroupIdSet.add(groupId);
			pay.increSuccess();
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> deleteSitePrice(ApiResult<Object> result,
			GroupSitePriceType[] sitePrices) {
		PaymentResult pay = result.getPayment();
		
		// 按照推广组将分网站出价分组，key为groupId
		Map<Long, List<GroupSitePriceItem>> groupSites = new HashMap<Long, List<GroupSitePriceItem>>();
		for (int index = 0; index < sitePrices.length; index++) {
			GroupSitePriceType sitePrice = sitePrices[index];
			Long groupId = sitePrice.getGroupId();
			List<GroupSitePriceItem> groupSitePriceList = groupSites.get(groupId);
			if (groupSitePriceList == null) {
				groupSitePriceList = new ArrayList<GroupSitePriceItem>();
				groupSites.put(groupId, groupSitePriceList);
			}

			groupSitePriceList.add(new GroupSitePriceItem(index, sitePrice.getSite(), sitePrice.getPrice()));
		}
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		
		// 依照推广组添加分网站出价
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long groupId : groupSites.keySet()) {
			CproGroup group = cproGroupMgr.findWithInfoById(groupId.intValue());
			List<GroupSitePriceItem> groupSitePriceList = groupSites.get(groupId);

			// 推广组不存在
			if (group == null || group.getGroupInfo() == null) {
				for (GroupSitePriceItem item : groupSitePriceList) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.SITE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.GROUPID);
					
					result = ApiResultBeanUtils.addApiError(result,
							GroupErrorCode.NOT_FOUND.getValue(),
							GroupErrorCode.NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
				}
				continue;
			}
			
			// 验证分网站出价
			// 并将新增或者修改的的添加进toBeModifiedSitePriceList
			List<GroupSitePrice> toBeRemoved = new ArrayList<GroupSitePrice>();
			List<GroupSitePrice> toBeUpdated = new ArrayList<GroupSitePrice>();
			List<OptContent> tmpContents = new ArrayList<OptContent>();
			boolean flag = groupConfigValidator.validateDeleteSitePrice(group, groupSitePriceList,
					toBeRemoved, toBeUpdated, result, tmpContents);
			if (!flag || (toBeRemoved.size() == 0 && toBeUpdated.size() == 0)) {
				// 校验失败则跳过
				continue;
			}
			
			boolean isSucc = groupSiteConfigMgr.delAndUpdateGroupSitePrice(toBeRemoved,	toBeUpdated, visitor);
			if (isSucc) {
				optContents.addAll(tmpContents);
				
				// 记录成功数
				int success = pay.getSuccess();
				pay.setSuccess(success + toBeRemoved.size() + toBeUpdated.size());
			}
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		return result;
	}
	
	public ApiResult<Object> deleteInterestInfo(ApiResult<Object> result,
			GroupInterestInfoType[] interestInfos){
		PaymentResult pay = result.getPayment();
		
		// 获得用户信息
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		int userId = bdUser.getUserid();
		
		// 依照推广组删除兴趣
		List<OptContent> optContents = new ArrayList<OptContent>();
		loop: for (int index = 0; index < interestInfos.length; index++) {
			
			GroupInterestInfoType item = interestInfos[index];
			Long groupIdLong = item.getGroupId();
			int groupId = groupIdLong.intValue();
			
			// 简单验证兴趣信息是否完整，包括传入的GroupId、interestIds、exceptInterestIds，不完整则不进行下一步
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.INTERESTS, index);
			String apiPositionParam = GroupConstant.INTERESTS + "[" + index + "]";
			
			if (item.getGroupId() < 1) {
				apiPosition.addParam(GroupConstant.GROUPID);
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.INTEREST_OR_GROUPID_NULL_ERROR.getValue(),
						GroupConfigErrorCode.INTEREST_OR_GROUPID_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				pay.increTotal(1);
				continue;
			}
			
			if( (item.getInterestIds() == null || item.getInterestIds().length == 0) && 
					(item.getExceptInterestIds() == null || item.getExceptInterestIds().length == 0)){
				apiPosition.addParam(GroupConstant.GROUPID);
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.INTEREST_OR_GROUPID_NULL_ERROR.getValue(),
						GroupConfigErrorCode.INTEREST_OR_GROUPID_NULL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				pay.increTotal(1);
				continue;
			}

			CproGroup group = cproGroupMgr.findWithInfoById(groupId);
			int planId = group.getPlanId();
			
			List<Integer> interestIdsList = ListArrayUtils.asList(item.getInterestIds());
			List<Integer> exceptInterestIdsList = ListArrayUtils.asList(item.getExceptInterestIds());
			List<GroupItItem> interestIdsItemList = ItUtils.transItIdList2GroupItItemList(item.getInterestIds());
			List<GroupItItem> exceptInterestIdsItemList = ItUtils.transItIdList2GroupItItemList(item.getExceptInterestIds());
			pay.increTotal(interestIdsList.size() + exceptInterestIdsList.size());
			
			List<CproGroupIT> its = cproGroupITMgr.findGroupITList(groupId);
			Set<Integer> existItIdSet = ITUtils.extractInterestIds(its);
			
			List<CproGroupITExclude> exceptIts = cproGroupITMgr.findGroupITExcludeList(groupId);
			Set<Integer> existExceptItIdSet = ITUtils.extractInterestIds(exceptIts);
			
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
			Set<Integer> validInterestIdsSet = new HashSet<Integer>();
			Set<Integer> validExceptInterestIdsSet = new HashSet<Integer>();
			ItUtils.validateInterestNotShowMoreThanOnce(result, interestIdsItemList, apiPositionParam, GroupConstant.INTEREST_IDS, false);
			ItUtils.validateInterestNotShowMoreThanOnce(result, exceptInterestIdsItemList, apiPositionParam, GroupConstant.INTEREST_IDS, false);
			ItUtils.validateDeleteInterestExist(result, interestIdsItemList, existItIdSet, apiPositionParam, GroupConstant.INTEREST_IDS, false, validInterestIdsSet);
			ItUtils.validateDeleteInterestExist(result, exceptInterestIdsItemList, existExceptItIdSet, apiPositionParam, GroupConstant.EXCEPT_INTEREST_IDS, false, validExceptInterestIdsSet);
			
			/* 
			 * 注释掉此部分逻辑，目前兴趣允许删空
			if(validInterestIdsSet.size() == existItIdSet.size()){
				pay.descrTotal(interestIdsList.size() + exceptInterestIdsList.size() - 1);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.INTEREST_CAN_NOT_DELETE_TO_EMPTY.getValue(),
						GroupConfigErrorCode.INTEREST_CAN_NOT_DELETE_TO_EMPTY.getMessage(), apiPosition.getPosition(), null);
				continue;
			}*/
			
			StringBuilder logSb = new StringBuilder("### toRemoveValidInterestIdsNum=[")
				.append(validInterestIdsSet.size()).append("], toRemoveValidExceptInterestIdsNum=[")
				.append(validExceptInterestIdsSet.size()).append("], existInterestIdsNum=[")
				.append(existItIdSet.size()).append("], existExceptInterestIdsNum=[")
				.append(existExceptItIdSet.size()).append("]");
			log.info(logSb);
			
			pay.increSuccess(validInterestIdsSet.size() + validExceptInterestIdsSet.size());
			existItIdSet.removeAll(validInterestIdsSet);
			existExceptItIdSet.removeAll(validExceptInterestIdsSet);
			List<Integer> toSaveInterestIdsList = ListArrayUtils.asList(existItIdSet);
			List<Integer> toSaveExceptInterestIdsList = ListArrayUtils.asList(existExceptItIdSet);
			
			//保存正向反向关联关系
			cproGroupITMgr.saveGroupIT(groupId, toSaveInterestIdsList, planId, userId, visitor.getUserid());
			cproGroupITMgr.saveGroupITExclude(groupId, toSaveExceptInterestIdsList, planId, userId, visitor.getUserid());
			
			/*
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
			*/
			
			optContents.addAll(OperationHistoryUtils.getOptContents());
			
		}
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		return result;
	}

	
	/**
	 * 推广组“添加分行业出价”
	 * 
	 * @author kanghongwei
	 * @since 2012-6-13
	 */
	public ApiResult<Object> addTradePrice(ApiResult<Object> result, GroupTradePriceType[] tradePrices) {

		// 按照“推广组”进行分组
		Map<Long, List<GroupTradePriceItem>> groupTradePriceMap = new HashMap<Long, List<GroupTradePriceItem>>();
		for (int index = 0; index < tradePrices.length; index++) {
			GroupTradePriceType item = tradePrices[index];
			Long groupId = item.getGroupId();
			List<GroupTradePriceItem> groupTradePriceList = groupTradePriceMap.get(groupId);
			if (groupTradePriceList == null) {
				groupTradePriceList = new ArrayList<GroupTradePriceItem>();
				groupTradePriceMap.put(groupId, groupTradePriceList);
			}
			groupTradePriceList.add(new GroupTradePriceItem(index, item.getTrade(), item.getPrice()));
		}

		// 验证“操作的推广组个数”
		if (groupTradePriceMap.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TRADE_PRICES);
			result = ApiResultBeanUtils.addApiError(result, 
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(), 
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		// 查询“推广组和id的映射关系”
		Map<Long, CproGroup> groupIdMap = new HashMap<Long, CproGroup>();

		// 获取“验证通过,可操作的记录”
		Map<Long, List<GroupTradePriceItem>> canOPtItemMap = copyMap4TradePriceAdd(groupTradePriceMap);
		
		// 验证“行业和出价”
		groupConfigValidator.validateAddTradePrice(groupTradePriceMap, canOPtItemMap, groupIdMap, result);

		// 准备“历史操作记录”
		List<OptContent> optContent = new ArrayList<OptContent>();
		
		// 准备“记录修改成功个数”
		Integer successCount = 0;

		// 分推广组“添加行业出价”
		for (Long groupId : canOPtItemMap.keySet()) {
			List<GroupTradePrice> needSaveOrUpdateList = new ArrayList<GroupTradePrice>();
			CproGroup currentGroup = groupIdMap.get(groupId);

			List<GroupTradePriceItem> itemList = canOPtItemMap.get(groupId);

			List<GroupTradePrice> oriTradePriceList = groupSiteConfigMgr.findAllTradePrice(null, null, groupId.intValue());
			Map<Integer, GroupTradePrice> oriTradePriceMap = new HashMap<Integer, GroupTradePrice>(oriTradePriceList.size());
			for (GroupTradePrice tradePrice : oriTradePriceList) {
				oriTradePriceMap.put(tradePrice.getTradeid(), tradePrice);
			}

			for (GroupTradePriceItem item : itemList) {
				TradeInfo info = UnionSiteCache.tradeInfoCache.getTradeInfoById(item.getTrade());
				generateTradePriceAdd(needSaveOrUpdateList, optContent, info, oriTradePriceMap, currentGroup, parsePrice(item.getPrice()));
			}

			for (GroupTradePrice trade : needSaveOrUpdateList) {
				trade.setGroupid(groupId.intValue());
				trade = groupTradePriceDao.makePersistent(trade);
				if (trade != null && trade.getId() > 0) {
					successCount++;
				}

			}
		}

		// 设置“全部添加的分行业出价”个数
		result.getPayment().increSuccess(successCount);

		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContent);
		return result;
	}

	private Map<Long, List<GroupTradePriceItem>> copyMap4TradePriceAdd(Map<Long, List<GroupTradePriceItem>> groupTradePriceMap) {
		Map<Long, List<GroupTradePriceItem>> copyedMap = new HashMap<Long, List<GroupTradePriceItem>>();
		for (Long groupId : groupTradePriceMap.keySet()) {
			List<GroupTradePriceItem> sourceItemList = groupTradePriceMap.get(groupId);
			List<GroupTradePriceItem> targetItemList = new LinkedList<GroupTradePriceItem>();
			for (GroupTradePriceItem item : sourceItemList) {
				targetItemList.add(new GroupTradePriceItem(item.getIndex(), item.getTrade(), item.getPrice()));
			}
			copyedMap.put(groupId.longValue(), targetItemList);
		}
		return copyedMap;
	}
	
	private Integer parsePrice(float price) {
		return Long.valueOf(Math.round(price * 100)).intValue();
	}

	private void generateTradePriceAdd(List<GroupTradePrice> tradePrice, List<OptContent> optContents, TradeInfo tradeInfo, Map<Integer, GroupTradePrice> oriMap, CproGroup group, Integer price) {
		final OpTypeVo opTypeAdd = OptHistoryConstant.OPTYPE_GROUP_TRADE_PRICE_BATCH_ADD;
		final OpTypeVo opTypeEdit = OptHistoryConstant.OPTYPE_GROUP_TRADE_PRICE_BATCH_EDIT;

		Integer trade = tradeInfo.getTradeid();
		Integer groupId = group.getGroupId();
		Integer userId = group.getUserId();
		Integer planId = group.getPlanId();
		GroupTradePrice gtp = oriMap.get(trade);
		if (gtp == null) {
			gtp = new GroupTradePrice();
			gtp.setGroupid(groupId);
			gtp.setPlanid(planId);
			gtp.setPrice(price);
			gtp.setUserid(userId);
			gtp.setTradeid(trade);
			GroupTradeSitePriceOptVo after = new GroupTradeSitePriceOptVo();
			after.setId(trade);
			after.setPrice(price);
			OptContent content = new OptContent(userId, opTypeAdd.getOpType(), opTypeAdd.getOpLevel(), groupId, null, opTypeAdd.getTransformer().toDbString(after));
			optContents.add(content);
		} else {
			if (!price.equals(gtp.getPrice())) {
				GroupTradeSitePriceOptVo before = new GroupTradeSitePriceOptVo();
				before.setId(gtp.getTradeid());
				before.setPrice(gtp.getPrice());
				gtp.setPrice(price);
				GroupTradeSitePriceOptVo after = new GroupTradeSitePriceOptVo();
				after.setId(trade);
				after.setPrice(price);
				OptContent content = new OptContent(userId, opTypeEdit.getOpType(), opTypeEdit.getOpLevel(), groupId, opTypeEdit.getTransformer().toDbString(before), // before
						opTypeEdit.getTransformer().toDbString(after)); // after
				optContents.add(content);
			}
		}
		tradePrice.add(gtp);
	}

	/**
	 * 推广组“删除分行业出价”
	 * 
	 * @author kanghongwei
	 * @since 2012-6-13
	 */
	public ApiResult<Object> deleteTradePrice(ApiResult<Object> result, GroupTradeType[] tradePrices) {

		// 按照“推广组”进行分组
		Map<Long, List<GroupTradeItem>> groupTradeMap = new HashMap<Long, List<GroupTradeItem>>();
		for (int index = 0; index < tradePrices.length; index++) {
			GroupTradeType item = tradePrices[index];
			Long groupId = item.getGroupId();
			List<GroupTradeItem> groupTradeList = groupTradeMap.get(groupId);
			if (groupTradeList == null) {
				groupTradeList = new ArrayList<GroupTradeItem>();
				groupTradeMap.put(groupId, groupTradeList);
			}
			groupTradeList.add(new GroupTradeItem(index, item.getTrade()));
		}

		// 验证“操作的推广组个数”
		if (groupTradeMap.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TRADE_PRICES);
			result = ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(), GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		// “推广组->推广组分行业出价映射”
		Map<Long, Map<Integer, GroupTradePrice>> groupTradePriceMap = new HashMap<Long, Map<Integer, GroupTradePrice>>();

		// “推广组id->推广组映射关系”
		Map<Long, CproGroup> groupIdMap = new HashMap<Long, CproGroup>();

		// 获取“验证通过,可操作的记录”
		Map<Long, List<GroupTradeItem>> canOPtItemMap = copyMap4TradePriceDel(groupTradeMap);

		// 验证“行业信息”
		groupConfigValidator.validateDeleteTradePrice(groupTradeMap, canOPtItemMap, groupTradePriceMap, groupIdMap, result);

		// 准备“历史操作记录”
		List<OptContent> optContent = new ArrayList<OptContent>();

		// 分推广组“删除行业出价”
		List<GroupTradePrice> needDeleteList = new ArrayList<GroupTradePrice>();
		for (Long groupId : canOPtItemMap.keySet()) {
			Map<Integer, GroupTradePrice> oriTradePriceMap = groupTradePriceMap.get(groupId);
			CproGroup currentGroup = groupIdMap.get(groupId);

			List<GroupTradeItem> itemList = canOPtItemMap.get(groupId);

			for (GroupTradeItem item : itemList) {
				GroupTradePrice needDeleteItem = oriTradePriceMap.get(item.getTrade());
				needDeleteList.add(needDeleteItem);
				generateTradePriceDel(optContent, needDeleteItem, currentGroup);
			}
		}

		for (GroupTradePrice trade : needDeleteList) {
			groupTradePriceDao.makeTransient(trade);
		}
		// 设置“全部删除的分行业出价”个数
		result.getPayment().increSuccess(needDeleteList.size());

		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContent);
		return result;
	}
	
	private Map<Long, List<GroupTradeItem>> copyMap4TradePriceDel(Map<Long, List<GroupTradeItem>> groupTradeMap) {
		Map<Long, List<GroupTradeItem>> copyedMap = new HashMap<Long, List<GroupTradeItem>>();
		for (Long groupId : groupTradeMap.keySet()) {
			List<GroupTradeItem> sourceItemList = groupTradeMap.get(groupId);
			List<GroupTradeItem> targetItemList = new LinkedList<GroupTradeItem>();
			for (GroupTradeItem item : sourceItemList) {
				targetItemList.add(new GroupTradeItem(item.getIndex(), item.getTrade()));
			}
			copyedMap.put(groupId.longValue(), targetItemList);
		}
		return copyedMap;
	}
	

	private void generateTradePriceDel(List<OptContent> optContents, GroupTradePrice needDeleteItem, CproGroup currentGroup) {
		final OpTypeVo opTypeDel = OptHistoryConstant.OPTYPE_GROUP_TRADE_PRICE_BATCH_DELETE;

		GroupTradeSitePriceOptVo before = new GroupTradeSitePriceOptVo();
		before.setId(needDeleteItem.getTradeid());
		before.setPrice(needDeleteItem.getPrice());
		OptContent content = new OptContent(currentGroup.getUserId(), opTypeDel.getOpType(), opTypeDel.getOpLevel(), currentGroup.getGroupId(), opTypeDel.getTransformer().toDbString(before), null);
		optContents.add(content);
	}
	
	public void addExcludeKeyword(BaseResponse<PlaceHolderResult> response,
			List<GroupExcludeKeywordType> groupExcludeKeywordTypes,
			int userId,
			int opUser) {
		
		// 构造按照推广组将关键词分组的字典，key为groupId
		Map<Integer, List<KeywordType>> groupExcludeKeywords = new HashMap<Integer, List<KeywordType>>();
		Map<Integer, List<Integer>> groupExcludeKeywordPacks = new HashMap<Integer, List<Integer>>();
		
		// 保存推广组内的列表下标还原到原始下标
		Map<Integer, IndexMapper> groupExcludeKeywordsIndexMap = new HashMap<Integer, IndexMapper>();
		Map<Integer, IndexMapper> groupExcludeKeywordPacksIndexMap = new HashMap<Integer, IndexMapper>();
		
		// 设置总的配额消耗
		response.getOptions().setTotal(groupExcludeKeywordTypes.size());
		int success = groupExcludeKeywordTypes.size();
		
		// 验证基本参数并按照推广组将关键词分组
		for (int index = 0; index < groupExcludeKeywordTypes.size(); index++) {
			GroupExcludeKeywordType item = groupExcludeKeywordTypes.get(index);
			
			// 获取所有参数
			Integer groupId = new Long(item.getGroupId()).intValue();
			int type = item.getType();
			KeywordType excludeKeyword = item.getExcludeKeyword();
			int excludeKeywordPackId = item.getExcludeKeywordPackId();
			
			// 推广组id不合法
			if (groupId < 1l) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 判断排除关键词类型
			if (type == CproGroupConstant.EXCLUDE_WORD) {
				if (excludeKeyword == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, index);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
					continue;
				}
				
				IndexMapperUtil.mapIndex(groupId, excludeKeyword, index, groupExcludeKeywords, groupExcludeKeywordsIndexMap);
				
			} else if (type == CproGroupConstant.EXCLUDE_WORD_PACK) {
				if (excludeKeywordPackId < 1) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, index);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD_PACKID);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
					continue;
				}

				IndexMapperUtil.mapIndex(groupId, excludeKeywordPackId, index, groupExcludeKeywordPacks, groupExcludeKeywordPacksIndexMap);
				
			} else {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, index);
				apiPosition.addParam(GroupConstant.TYPE);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
		}

		// 验证待添加排除关键词推广组的个数
		if (groupExcludeKeywords.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		if (groupExcludeKeywordPacks.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		for (Integer groupId : groupExcludeKeywords.keySet()) {
			
			List<KeywordType> excludeKeywords = groupExcludeKeywords.get(groupId);
			IndexMapper indexMapper = groupExcludeKeywordsIndexMap.get(groupId);
			
			// 将API的BO转换成北斗core的VO
			List<String> keywordList = KeywordType.extractKeywords(excludeKeywords);
			
			// 根据推广组ID取出PlanId
			Integer planId = cproGroupMgr.findPlanIdByGroupId(groupId);
			if (planId == null) {
				throw new RuntimeException("Cannot get planId from groupId=" + groupId);
			}
			
			// 设置排除关键词
			if (CollectionUtils.isNotEmpty(excludeKeywords)) {
				APIResult<WordExclude> wordExcludeResult = null;
				try {
					wordExcludeResult = wordExcludeFacade.addOrSetWordExclude(userId, opUser, planId, groupId, keywordList, false, false, optContents);
					if (wordExcludeResult.hasErrors()) {
						Set<Integer> indexSet = wordExcludeResult.getErrors().keySet();
						for (Integer index : indexSet) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							Integer errorCode = wordExcludeResult.getErrors().get(index);
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
							if (errorCode.equals(GroupErrorConst.KT_WORDS_PATTERN_TYPE_ERROR.getValue())) {
								apiPosition.addParam(GroupConstant.PATTERN_TYPE);
								response = DRAPIMountAPIBeanUtils.addApiError(response,
										GroupConfigErrorCode.EXCLUDE_WORDS_PATTERN_TYPE_ERROR.getValue(),
										GroupConfigErrorCode.EXCLUDE_WORDS_PATTERN_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
							} else if (errorCode.equals(GroupErrorConst.KT_WORDS_ERROR.getValue())) {
								apiPosition.addParam(GroupConstant.KEYWORD);
								response = DRAPIMountAPIBeanUtils.addApiError(response,
										GroupConfigErrorCode.EXCLUDE_WORDS_ERROR.getValue(),
										GroupConfigErrorCode.EXCLUDE_WORDS_ERROR.getMessage(), apiPosition.getPosition(), null);
							} else if (errorCode.equals(GroupErrorConst.EXCLUDE_WORD_OR_PACK_DUP.getValue())) {
								apiPosition.addParam(GroupConstant.KEYWORD);
								response = DRAPIMountAPIBeanUtils.addApiError(response,
										GroupConfigErrorCode.EXCLUDE_WORD_OR_PACK_DUP.getValue(),
										GroupConfigErrorCode.EXCLUDE_WORD_OR_PACK_DUP.getMessage(), apiPosition.getPosition(), null);
							} 
						}
					}
				} catch (ExcludeException e) {
					if (e.getMessage().equals(ExcludeConstant.ADD_EXCLUDE_WORD_PARAM_MISS_MSG)) {
						throw new RuntimeException("Param error");
					} else if (e.getMessage().equals(ExcludeConstant.EXCLUDE_WORD_COUNT_EXCEED)) {
						for (int index = 0; index < keywordList.size(); index++) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.EXCLUDE_KEYWORDS_EXCEED_LIMIT.getValue(),
									GroupConfigErrorCode.EXCLUDE_KEYWORDS_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
						}
					}
				}
			}
		}
			
		for (Integer groupId : groupExcludeKeywordPacks.keySet()) {
			
			List<Integer> excludeKeywordPacks = groupExcludeKeywordPacks.get(groupId);
			IndexMapper indexMapper = groupExcludeKeywordPacksIndexMap.get(groupId);
			
			// 根据推广组ID取出PlanId
			Integer planId = cproGroupMgr.findPlanIdByGroupId(groupId);
			if (planId == null) {
				throw new RuntimeException("Cannot get planId from groupId=" + groupId);
			}
			
			// 设置排除关键词组合
			if (CollectionUtils.isNotEmpty(excludeKeywordPacks)) {
				APIResult<WordPackExclude> wordPackExcludeResult = null;
				try {
					wordPackExcludeResult = wordExcludeFacade.addOrSetWordExcludePack(userId, opUser, planId, groupId, excludeKeywordPacks, false, false, optContents);
					if (wordPackExcludeResult.hasErrors()) {
						Set<Integer> indexSet = wordPackExcludeResult.getErrors().keySet();
						
						for (Integer index : indexSet) {
							Integer errorCode = wordPackExcludeResult.getErrors().get(index);
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD_PACKID);
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
						}
					}
				} catch (ExcludeException e) {
					if (e.getMessage().equals(ExcludeConstant.ADD_EXCLUDE_WORD_PARAM_MISS_MSG)) {
						throw new RuntimeException("Param error");
					}  else if (e.getMessage().equals(ExcludeConstant.EXCLUDE_WORD_PACK_COUNT_EXCEED)) {
						for (int index = 0; index < excludeKeywordPacks.size(); index++) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD_PACKID);
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_COUNT_EXCEED.getValue(),
									GroupConfigErrorCode.EXCLUDE_WORD_PACK_COUNT_EXCEED.getMessage(), apiPosition.getPosition(), null);
						}
					}
				} 
			}
		}
		
		if (response.getErrors() != null) {
			success -= response.getErrors().size();
		}
		
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		response.getOptions().setSuccess(success);
	}
	
	public void deleteExcludeKeyword(BaseResponse<PlaceHolderResult> response,
			List<GroupExcludeKeywordType> groupExcludeKeywordTypes,
			int userId,
			int opUser) {
		
		// 构造按照推广组将关键词分组的字典，key为groupId
		Map<Integer, List<KeywordType>> groupExcludeKeywords = new HashMap<Integer, List<KeywordType>>();
		Map<Integer, List<Integer>> groupExcludeKeywordPacks = new HashMap<Integer, List<Integer>>();
		
		// 保存推广组内的列表下标还原到原始下标
		Map<Integer, IndexMapper> groupExcludeKeywordsIndexMap = new HashMap<Integer, IndexMapper>();
		Map<Integer, IndexMapper> groupExcludeKeywordPacksIndexMap = new HashMap<Integer, IndexMapper>();
		
		// 设置总的配额消耗
		response.getOptions().setTotal(groupExcludeKeywordTypes.size());
		int success = groupExcludeKeywordTypes.size();
		
		// 验证基本参数并按照推广组将关键词分组
		for (int index = 0; index < groupExcludeKeywordTypes.size(); index++) {
			GroupExcludeKeywordType item = groupExcludeKeywordTypes.get(index);
			
			// 获取所有参数
			Integer groupId = new Long(item.getGroupId()).intValue();
			int type = item.getType();
			KeywordType excludeKeyword = item.getExcludeKeyword();
			int excludeKeywordPackId = item.getExcludeKeywordPackId();
			
			// 推广组id不合法
			if (groupId < 1l) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				success -= 1;
				continue;
			}
			
			// 判断排除关键词类型
			if (type == CproGroupConstant.EXCLUDE_WORD) {
				if (excludeKeyword == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, index);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
					success -= 1;
					continue;
				}
				
				IndexMapperUtil.mapIndex(groupId, excludeKeyword, index, groupExcludeKeywords, groupExcludeKeywordsIndexMap);
				
			} else if (type == CproGroupConstant.EXCLUDE_WORD_PACK) {
				if (excludeKeywordPackId < 1) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, index);
					apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD_PACKID);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
					success -= 1;
					continue;
				}

				IndexMapperUtil.mapIndex(groupId, excludeKeywordPackId, index, groupExcludeKeywordPacks, groupExcludeKeywordPacksIndexMap);
				
			} else {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, index);
				apiPosition.addParam(GroupConstant.TYPE);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				success -= 1;
				continue;
			}
		}

		// 验证待删除排除关键词推广组的个数
		if (groupExcludeKeywords.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		if (groupExcludeKeywordPacks.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		for (Integer groupId : groupExcludeKeywords.keySet()) {
			
			List<KeywordType> excludeKeywords = groupExcludeKeywords.get(groupId);
			IndexMapper indexMapper = groupExcludeKeywordsIndexMap.get(groupId);
			
			// 将API的BO转换成北斗core的VO
			List<String> keywordList = KeywordType.extractKeywords(excludeKeywords);
			
			
			// 根据推广组ID取出PlanId
			Integer planId = cproGroupMgr.findPlanIdByGroupId(groupId);
			if (planId == null) {
				throw new RuntimeException("Cannot get planId from groupId=" + groupId);
			}
			
			// 设置排除关键词
			if (CollectionUtils.isNotEmpty(excludeKeywords)) {
				APIResult<WordExclude> wordExcludeResult = null;
				try {
					wordExcludeResult = wordExcludeFacade.delWordExclude(userId, opUser, planId, groupId, keywordList, optContents);
					success -= wordExcludeResult.getErrorsSize();
					if (wordExcludeResult.hasErrors()) {
						Set<Integer> indexSet = wordExcludeResult.getErrors().keySet();
						for (Integer index : indexSet) {
							Integer errorCode = wordExcludeResult.getErrors().get(index);
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
							if (errorCode.equals(GroupErrorConst.KT_WORDS_PATTERN_TYPE_ERROR.getValue())) {
								apiPosition.addParam(GroupConstant.PATTERN_TYPE);
								response = DRAPIMountAPIBeanUtils.addApiError(response,
										GroupConfigErrorCode.EXCLUDE_WORDS_PATTERN_TYPE_ERROR.getValue(),
										GroupConfigErrorCode.EXCLUDE_WORDS_PATTERN_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
							} else if (errorCode.equals(GroupErrorConst.KT_WORDS_ERROR.getValue())) {
								apiPosition.addParam(GroupConstant.KEYWORD);
								response = DRAPIMountAPIBeanUtils.addApiError(response,
										GroupConfigErrorCode.EXCLUDE_WORDS_ERROR.getValue(),
										GroupConfigErrorCode.EXCLUDE_WORDS_ERROR.getMessage(), apiPosition.getPosition(), null);
							} else if (errorCode.equals(GroupErrorConst.EXCLUDE_WORD_NOT_EXIST_IN_GROUP.getValue())) {
								apiPosition.addParam(GroupConstant.KEYWORD);
								response = DRAPIMountAPIBeanUtils.addApiError(response,
										GroupConfigErrorCode.EXCLUDE_WORD_NOT_EXIST.getValue(),
										GroupConfigErrorCode.EXCLUDE_WORD_NOT_EXIST.getMessage(), apiPosition.getPosition(), null);
							} 
						}
					}
				} catch (ExcludeException e) {
					if (e.getMessage().equals(ExcludeConstant.ADD_EXCLUDE_WORD_PARAM_MISS_MSG)) {
						throw new RuntimeException("Param error");
					}
				}
			}
		}
			
		for (Integer groupId : groupExcludeKeywordPacks.keySet()) {
			
			List<Integer> excludeKeywordPacks = groupExcludeKeywordPacks.get(groupId);
			IndexMapper indexMapper = groupExcludeKeywordPacksIndexMap.get(groupId);
			
			// 根据推广组ID取出PlanId
			Integer planId = cproGroupMgr.findPlanIdByGroupId(groupId);
			if (planId == null) {
				throw new RuntimeException("Cannot get planId from groupId=" + groupId);
			}
			
			// 设置排除关键词组合
			if (CollectionUtils.isNotEmpty(excludeKeywordPacks)) {
				APIResult<WordPackExclude> wordPackExcludeResult = null;
				try {
					wordPackExcludeResult = wordExcludeFacade.delWordExcludePack(userId, opUser, planId, groupId, excludeKeywordPacks, optContents);
					success -= wordPackExcludeResult.getErrorsSize();
					if (wordPackExcludeResult.hasErrors()) {
						Set<Integer> indexSet = wordPackExcludeResult.getErrors().keySet();
						for (Integer index : indexSet) {
							Integer errorCode = wordPackExcludeResult.getErrors().get(index);
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD_PACKID);
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
							} 
						}
					}
				} catch (ExcludeException e) {
					if (e.getMessage().equals(ExcludeConstant.ADD_EXCLUDE_WORD_PARAM_MISS_MSG)) {
						throw new RuntimeException("Param error");
					}
				}
			}
		}
		
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		response.getOptions().setSuccess(success);
	}
	
	public void addExcludePeople(BaseResponse<PlaceHolderResult> response,
			List<GroupExcludePeopleType> excludePeopleTypes,
			int userId,
			int opUser){
		
		// 构造按照推广组将人群分组的字典，key为groupId
		Map<Integer, List<GroupExcludePeopleType>> groupExcludePeoples = new HashMap<Integer, List<GroupExcludePeopleType>>();
		
		// 保存推广组内的列表下标还原到原始下标
		Map<Integer, IndexMapper> groupExcludePeoplesIndexMap = new HashMap<Integer, IndexMapper>();
		
		// 设置总的配额消耗
		response.getOptions().setTotal(excludePeopleTypes.size());
		int success = excludePeopleTypes.size();
		
		// 验证基本参数并按照推广组将人群分组
		for (int index = 0; index < excludePeopleTypes.size(); index++) {
			GroupExcludePeopleType item = excludePeopleTypes.get(index);
			
			// 获取所有参数
			Integer groupId = new Long(item.getGroupId()).intValue();
			Long pid = new Long(item.getExcludePeopleId());
			
			// 推广组id不合法
			if (groupId < 1l) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 人群id不合法
			if (pid == null || pid < 1l) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES, index);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			IndexMapperUtil.mapIndex(groupId, item, index, groupExcludePeoples, groupExcludePeoplesIndexMap);
			
		}

		// 验证待添加排除人群推广组的个数
		if (groupExcludePeoples.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		for (Integer groupId : groupExcludePeoples.keySet()) {
			
			List<GroupExcludePeopleType> excludePeoples = groupExcludePeoples.get(groupId);
			IndexMapper indexMapper = groupExcludePeoplesIndexMap.get(groupId);
			
			List<Long> excludePids = GroupBoMappingUtil.transGroupExcludePeopleType2Long(excludePeoples);
			
			groupExcludePeopleFacade.addOrSetExcludePeople(response, groupId, excludePids, indexMapper, userId, opUser, true, false, false, optContents);
			
		}
		
		if (response.getErrors() != null) {
			success -= response.getErrors().size();
		}
		
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		response.getOptions().setSuccess(success);
		
	}
	
	public void deleteExcludePeople(BaseResponse<PlaceHolderResult> response,
			List<GroupExcludePeopleType> excludePeopleTypes,
			int userId,
			int opUser) {
		// 构造按照推广组将人群分组的字典，key为groupId
		Map<Integer, List<GroupExcludePeopleType>> groupExcludePeoples = new HashMap<Integer, List<GroupExcludePeopleType>>();
		
		// 保存推广组内的列表下标还原到原始下标
		Map<Integer, IndexMapper> groupExcludePeoplesIndexMap = new HashMap<Integer, IndexMapper>();
		
		// 设置总的配额消耗
		response.getOptions().setTotal(excludePeopleTypes.size());
		int success = excludePeopleTypes.size();
		
		// 验证基本参数并按照推广组将排除人群分组
		for (int index = 0; index < excludePeopleTypes.size(); index++) {
			GroupExcludePeopleType item = excludePeopleTypes.get(index);
			
			// 获取所有参数
			Integer groupId = new Long(item.getGroupId()).intValue();
			Long pid = new Long(item.getExcludePeopleId());
			
			// 推广组id不合法
			if (groupId < 1l) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 人群id不合法
			if (pid == null || pid < 1l) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES, index);
				apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			IndexMapperUtil.mapIndex(groupId, item, index, groupExcludePeoples, groupExcludePeoplesIndexMap);
			
		}

		// 验证待添加排除人群推广组的个数
		if (groupExcludePeoples.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		for (Integer groupId : groupExcludePeoples.keySet()) {
			
			List<GroupExcludePeopleType> excludePeoples = groupExcludePeoples.get(groupId);
			IndexMapper indexMapper = groupExcludePeoplesIndexMap.get(groupId);
			
			List<Long> excludePids = GroupBoMappingUtil.transGroupExcludePeopleType2Long(excludePeoples);
			
			groupExcludePeopleFacade.deleteExcludePeople(response, groupId, excludePids, indexMapper, userId, opUser, optContents);
			
		}
		
		if (response.getErrors() != null) {
			success -= response.getErrors().size();
		}
		
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		response.getOptions().setSuccess(success);
	}
	
	public void addPackInfo(BaseResponse<PlaceHolderResult> response,
			List<GroupPackItemType> packs,
			int userId,
			int opUser) {
		// 构造按照推广组将受众组合分组的字典，key为groupId
		Map<Integer, List<GroupPackItemType>> groupPacks = new HashMap<Integer, List<GroupPackItemType>>();
		
		// 保存推广组内的列表下标还原到原始下标
		Map<Integer, IndexMapper> groupPacksIndexMap = new HashMap<Integer, IndexMapper>();
		
		// 设置总的配额消耗
		response.getOptions().setTotal(packs.size());
		int success = packs.size();
		
		// 验证基本参数并按照推广组将受众组合分组
		for (int index = 0; index < packs.size(); index++) {
			GroupPackItemType item = packs.get(index);
			
			// 获取所有参数
			Integer groupId = new Long(item.getGroupId()).intValue();
			int packId = item.getPackId();
			int type = item.getType();
			
			// 推广组id不合法
			if (groupId < 1l) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PACKS, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 受众组合id不合法
			if (packId < 1) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PACKS, index);
				apiPosition.addParam(GroupConstant.PACKID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 受众组合类型不合法 受众组合类型只支持关键词组合
			if(type != PackTypeConstant.TYPE_KEYWORD_PACK ){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PACKS, index);
				apiPosition.addParam(GroupConstant.TYPE);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			IndexMapperUtil.mapIndex(groupId, item, index, groupPacks, groupPacksIndexMap);
			
		}

		// 验证待添加受众组合推广组的个数
		if (groupPacks.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.PACKS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		for (Integer groupId : groupPacks.keySet()) {
			
			List<GroupPackItemType> packList = groupPacks.get(groupId);
			IndexMapper indexMapper = groupPacksIndexMap.get(groupId);
			
			List<GroupPackVo> packVoList = GroupBoMappingUtil.transGroupPackItemType2GroupPackVo(packList);
			
			// 根据推广组ID取出PlanId
			CproGroup group = cproGroupMgr.findCproGroupById(groupId);
			Integer planId = group.getPlanId();
			if (planId == null) {
				throw new RuntimeException("Cannot get planId from groupId=" + groupId);
			}
			
			// 设置推广组受众组合
			if (CollectionUtils.isNotEmpty(packVoList)) {
				
				// 推广组targetType不支持受众组合时不能设置组合
				int targetType = group.getTargetType();
				if (!TargettypeUtil.isPack(targetType)) {
					for (int index = 0; index < packVoList.size(); index++) {
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(GroupConstant.PACKS, indexMapper.getIndex(index));
						apiPosition.addParam(GroupConstant.GROUPID);
						response = DRAPIMountAPIBeanUtils.addApiError(response,
								GroupConfigErrorCode.GROUP_TARGETTYPE_NOT_SUPPORT_PACK_CONFIG.getValue(),
								GroupConfigErrorCode.GROUP_TARGETTYPE_NOT_SUPPORT_PACK_CONFIG.getMessage(), apiPosition.getPosition(), null);
					}
					continue;
				}
				
				APIResult<GroupPack> groupPackResult = null;
				try {
					groupPackResult = groupPackFacade.addOrSetGroupPack4API(userId, opUser, planId, groupId, packVoList, false, false, optContents);
					if (groupPackResult.hasErrors()) {
						Set<Integer> indexSet = groupPackResult.getErrors().keySet();
						for (Integer index : indexSet) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							Integer errorCode = groupPackResult.getErrors().get(index);
							apiPosition.addParam(GroupConstant.PACKS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.PACKID);
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
						}
					}
				} catch (GroupPackException e) {
					if (e.getMessage().equals(GroupPackConstant.ADD_GROUP_PACK_EXCEED_LIMIT_MSG)) {
						for (int index = 0; index < packVoList.size(); index++) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(GroupConstant.PACKS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.PACKID);
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.GROUP_PACK_EXCEED_LIMIT.getValue(),
									GroupConfigErrorCode.GROUP_PACK_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
						}
					} else if (e.getMessage().equals(GroupPackConstant.ADD_GROUP_KEYWORD_PACK_EXCEED_LIMIT_MSG)) {
						for (int index = 0; index < packVoList.size(); index++) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(GroupConstant.PACKS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.PACKID);
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.GROUP_KEYWORD_PACK_EXCEED_LIMIT.getValue(),
									GroupConfigErrorCode.GROUP_KEYWORD_PACK_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
						}
					} else if (e.getMessage().equals(GroupPackConstant.ADD_GROUP_ADVANCED_PACK_EXCEED_LIMIT_MSG)) {
						for (int index = 0; index < packVoList.size(); index++) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(GroupConstant.PACKS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.PACKID);
							response = DRAPIMountAPIBeanUtils.addApiError(response,
									GroupConfigErrorCode.GROUP_ADVANCED_PACK_EXCEED_LIMIT.getValue(),
									GroupConfigErrorCode.GROUP_ADVANCED_PACK_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
						}
					} 
				} catch (Exception e) {
					log.error("Failed to add group pack info. " + e.getMessage(), e);
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.PACKS);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GroupConfigErrorCode.GROUP_PACK_CONFIG_FAILED.getValue(),
							GroupConfigErrorCode.GROUP_PACK_CONFIG_FAILED.getMessage(), apiPosition.getPosition(), null);
				}
			}
		}
		
		if (response.getErrors() != null) {
			success -= response.getErrors().size();
		}
		
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		response.getOptions().setSuccess(success);
	}
	
	public void deletePackInfo(BaseResponse<PlaceHolderResult> response,
			List<GroupPackItemType> packs,
			int userId,
			int opUser) {
		// 构造按照推广组将受众组合分组的字典，key为groupId
		Map<Integer, List<GroupPackItemType>> groupPacks = new HashMap<Integer, List<GroupPackItemType>>();
		
		// 保存推广组内的列表下标还原到原始下标
		Map<Integer, IndexMapper> groupPacksIndexMap = new HashMap<Integer, IndexMapper>();
		
		// 设置总的配额消耗
		response.getOptions().setTotal(packs.size());
		int success = packs.size();
		
		// 验证基本参数并按照推广组将受众组合分组
		for (int index = 0; index < packs.size(); index++) {
			GroupPackItemType item = packs.get(index);
			
			// 获取所有参数
			Integer groupId = new Long(item.getGroupId()).intValue();
			int packId = item.getPackId();
			int type = item.getType();
			
			// 推广组id不合法
			if (groupId < 1l) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PACKS, index);
				apiPosition.addParam(GroupConstant.GROUPID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 受众组合id不合法
			if (packId < 1) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PACKS, index);
				apiPosition.addParam(GroupConstant.PACKID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			// 受众组合类型不合法 北斗3.5下线后只有关键词推广组删除
			if(type != PackTypeConstant.TYPE_KEYWORD_PACK){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.PACKS, index);
				apiPosition.addParam(GroupConstant.TYPE);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
				continue;
			}
			
			IndexMapperUtil.mapIndex(groupId, item, index, groupPacks, groupPacksIndexMap);
			
		}

		// 验证待添加受众组合推广组的个数
		if (groupPacks.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.PACKS);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
					GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return;
		}
		
		// 历史操作记录常量
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		for (Integer groupId : groupPacks.keySet()) {
			
			List<GroupPackItemType> packList = groupPacks.get(groupId);
			IndexMapper indexMapper = groupPacksIndexMap.get(groupId);
			
			List<GroupPackVo> packVoList = GroupBoMappingUtil.transGroupPackItemType2GroupPackVo(packList);
			
			// 根据推广组ID取出PlanId
			CproGroup group = cproGroupMgr.findCproGroupById(groupId);
			Integer planId = group.getPlanId();
			if (planId == null) {
				throw new RuntimeException("Cannot get planId from groupId=" + groupId);
			}
			
			// 设置推广组受众组合
			if (CollectionUtils.isNotEmpty(packVoList)) {
				
				// 推广组targetType不支持受众组合时不能设置组合
				int targetType = group.getTargetType();
				if (!TargettypeUtil.isPack(targetType)) {
					for (int index = 0; index < packVoList.size(); index++) {
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(GroupConstant.PACKS, indexMapper.getIndex(index));
						apiPosition.addParam(GroupConstant.GROUPID);
						response = DRAPIMountAPIBeanUtils.addApiError(response,
								GroupConfigErrorCode.GROUP_TARGETTYPE_NOT_SUPPORT_PACK_CONFIG.getValue(),
								GroupConfigErrorCode.GROUP_TARGETTYPE_NOT_SUPPORT_PACK_CONFIG.getMessage(), apiPosition.getPosition(), null);
					}
					continue;
				}
				
				APIResult<GroupPack> groupPackResult = null;
				try {
					groupPackResult = groupPackFacade.delGroupPack4API(userId, opUser, planId, groupId, packVoList, optContents);
					if (groupPackResult.hasErrors()) {
						Set<Integer> indexSet = groupPackResult.getErrors().keySet();
						for (Integer index : indexSet) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							Integer errorCode = groupPackResult.getErrors().get(index);
							apiPosition.addParam(GroupConstant.PACKS, indexMapper.getIndex(index));
							apiPosition.addParam(GroupConstant.PACKID);
							if (errorCode.equals(GroupErrorConst.GROUP_PACK_NOT_RELATED.getValue())) {
								response = DRAPIMountAPIBeanUtils.addApiError(response,
										GroupConfigErrorCode.GROUP_PACK_NOT_RELATED.getValue(),
										GroupConfigErrorCode.GROUP_PACK_NOT_RELATED.getMessage(), apiPosition.getPosition(), null);
							} 
						}
					}
				} catch (Exception e) {
					log.error("Failed to delete group pack info. " + e.getMessage(), e);
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.PACKS);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							GroupConfigErrorCode.GROUP_PACK_CONFIG_FAILED.getValue(),
							GroupConfigErrorCode.GROUP_PACK_CONFIG_FAILED.getMessage(), apiPosition.getPosition(), null);
				}
			}
		}
		
		if (response.getErrors() != null) {
			success -= response.getErrors().size();
		}
		
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);  // 加入session中，后续有拦截器处理
		
		response.getOptions().setSuccess(success);
	}

    /**
     * 修改地域设置，验证推广组相关参数
     * 
     * @param group 推广组
     * @param groupRegionList 推广组对应地域
     * @return 参数验证结果
     */
    private boolean regionUpdateGroupCheck(ApiResult<Object> result, CproGroup group,
            Set<GroupRegionItem> groupRegionList) {
        // 推广组不存在
        if (group == null || group.getGroupInfo() == null) {
            for (GroupRegionItem item : groupRegionList) {
                GroupErrorCode.NOT_FOUND.getErrorResponse(result, Lists.newArrayList(new ErrorParam(
                        GroupConstant.REGIONS, item.getIndex()), new ErrorParam(GroupConstant.GROUPID, null)), null);
            }
            return false;
        }

        // 该推广组为全网投放时不能进行添加region
        if (CproGroupConstant.GROUP_ALLREGION == group.getGroupInfo().getIsAllRegion()) {
            for (GroupRegionItem item : groupRegionList) {
                GroupConfigErrorCode.REGION_NOT_OPTIONAL_ERROR.getErrorResponse(result, Lists.newArrayList(
                        new ErrorParam(GroupConstant.REGIONS, item.getIndex()), new ErrorParam(GroupConstant.GROUPID,
                                null)), null);
            }
            return false;
        }

        return true;
    }

    /**
     * 获取用户选择的一级地域数量
     * 
     * @param selectedRegionIds 用户选择的全部地域
     * @return 用户选择的一级地域数量
     */
    private int getFirstRegionCount(Set<Integer> selectedRegionIds) {
        Set<Integer> firstRegionIds = new HashSet<Integer>();
        for (Integer regionId : selectedRegionIds) {
            Integer[] regInfo = sysRegCache.getRegInfoMap().get(regionId);
            if (regInfo[0] == 0) {
                firstRegionIds.add(regionId);
            } else {
                firstRegionIds.add(regInfo[0]);
            }
        }

        return firstRegionIds.size();
    }
    
    /**
     * 地域集合转换为DB存储的字符串
     * 
     * @param selectedRegionIds 地域集合
     * @return 地域字符串
     */
    private String getRegionStr(Set<Integer> selectedRegionIds) {
        List<Integer> regionIds = new ArrayList<Integer>(selectedRegionIds.size());
        regionIds.addAll(selectedRegionIds);

        Collections.sort(regionIds);

        return com.baidu.beidou.util.StringUtils.makeStrFromCollectionForSite(regionIds,
                CproGroupConstant.FIELD_SEPERATOR);
    }
    
    /**
     * 获取全部有效的地域信息（已被一级地域包含的二级地域删除）
     * 
     * @param result 响应对象
     * @param regionItemTypes 用户提交设置的地域信息
     * @return 有效地域信息
     */
    private Set<Integer> getEffectiveRegion(ApiResult<Object> result, RegionItemType[] regionItemTypes) {
        Set<Integer> firstRegionIds = new HashSet<Integer>();
        Set<Integer> secondRegionIds = new HashSet<Integer>();

        List<Integer> regionList =
                Lists.transform(Lists.newArrayList(regionItemTypes), new Function<RegionItemType, Integer>() {

                    @Override
                    public Integer apply(RegionItemType regionItemType) {
                        return regionItemType.getRegionId();
                    }
                });

        for (int i = 0; i < regionList.size(); i++) {
            Integer regionId = regionList.get(i);
            Integer[] regInfo = sysRegCache.getRegInfoMap().get(regionId);
            if (regInfo == null || regInfo.length != 2) {
                GroupConfigErrorCode.REGION_NOT_FOUND.getErrorResponse(result, Lists.newArrayList(new ErrorParam(
                        GroupConstant.REGION_CONFIG_REQ, null), new ErrorParam(GroupConstant.REGION_CONFIG_TYPE, i)),
                        null);
                continue;
            }

            if (regInfo[0] == 0) {
                if (firstRegionIds.contains(regionId)) {
                    GroupConfigErrorCode.REGION_DUP.getErrorResponse(result, Lists
                            .newArrayList(new ErrorParam(GroupConstant.REGION_CONFIG_REQ, null), new ErrorParam(
                                    GroupConstant.REGION_CONFIG_TYPE, i)), null);
                } else {
                    firstRegionIds.add(regionId);
                }
            } else {
                if (secondRegionIds.contains(regionId)) {
                    GroupConfigErrorCode.REGION_DUP.getErrorResponse(result, Lists
                            .newArrayList(new ErrorParam(GroupConstant.REGION_CONFIG_REQ, null), new ErrorParam(
                                    GroupConstant.REGION_CONFIG_TYPE, i)), null);
                } else {
                    secondRegionIds.add(regionId);
                }
            }
        }

        Set<Integer> allRegionIds = new HashSet<Integer>(firstRegionIds.size() + secondRegionIds.size());
        // 如果二级地域对应的一级地域不在用户选择的一级地域中，则添加该二级地域
        for (Integer regionId : secondRegionIds) {
            Integer[] regInfo = sysRegCache.getRegInfoMap().get(regionId);
            if (!firstRegionIds.contains(regInfo[0])) {
                allRegionIds.add(regionId);
            }
        }

        // 添加全部一级地域
        allRegionIds.addAll(firstRegionIds);

        return allRegionIds;
    }
	
	public void setCproKeywordFacade(CproKeywordFacade cproKeywordFacade) {
		this.cproKeywordFacade = cproKeywordFacade;
	}

	public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
		this.cproKeywordMgr = cproKeywordMgr;
	}

	public void setGroupSiteConfigMgr(GroupSiteConfigMgr groupSiteConfigMgr) {
		this.groupSiteConfigMgr = groupSiteConfigMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public void setVtPeopleMgr(VtPeopleMgr vtPeopleMgr) {
		this.vtPeopleMgr = vtPeopleMgr;
	}

	public void setCproGroupVTMgr(CproGroupVTMgr cproGroupVTMgr) {
		this.cproGroupVTMgr = cproGroupVTMgr;
	}

	public void setCproGroupVTDao(CproGroupVTDao cproGroupVTDao) {
		this.cproGroupVTDao = cproGroupVTDao;
	}

	public void setGroupConfigValidator(GroupConfigValidator groupConfigValidator) {
		this.groupConfigValidator = groupConfigValidator;
	}

	public CproGroupITMgr getCproGroupITMgr() {
		return cproGroupITMgr;
	}

	public void setCproGroupITMgr(CproGroupITMgr cproGroupITMgr) {
		this.cproGroupITMgr = cproGroupITMgr;
	}

	public InterestMgr getInterestMgr() {
		return interestMgr;
	}

	public void setInterestMgr(InterestMgr interestMgr) {
		this.interestMgr = interestMgr;
	}

	public CustomITMgr getCustomITMgr() {
		return customITMgr;
	}

	public void setCustomITMgr(CustomITMgr customITMgr) {
		this.customITMgr = customITMgr;
	}

	public CproITFacade getCproITFacade() {
		return cproITFacade;
	}

	public void setCproITFacade(CproITFacade cproITFacade) {
		this.cproITFacade = cproITFacade;
	}

	public void setGroupTradePriceDao(GroupTradePriceDao groupTradePriceDao) {
		this.groupTradePriceDao = groupTradePriceDao;
	}

	public WordExcludeFacade getWordExcludeFacade() {
		return wordExcludeFacade;
	}

	public void setWordExcludeFacade(WordExcludeFacade wordExcludeFacade) {
		this.wordExcludeFacade = wordExcludeFacade;
	}

	public GroupExcludePeopleFacade getGroupExcludePeopleFacade() {
		return groupExcludePeopleFacade;
	}

	public void setGroupExcludePeopleFacade(
			GroupExcludePeopleFacade groupExcludePeopleFacade) {
		this.groupExcludePeopleFacade = groupExcludePeopleFacade;
	}

	public GroupPackFacade getGroupPackFacade() {
		return groupPackFacade;
	}

	public void setGroupPackFacade(GroupPackFacade groupPackFacade) {
		this.groupPackFacade = groupPackFacade;
	}

	public void setSequenceDriver(SequenceDriver sequenceDriver) {
        this.sequenceDriver = sequenceDriver;
    }

    public AppExcludeFacade getAppExcludeFacade() {
		return appExcludeFacade;
	}

	public void setAppExcludeFacade(AppExcludeFacade appExcludeFacade) {
		this.appExcludeFacade = appExcludeFacade;
	}

	public AppExcludeMgr getAppExcludeMgr() {
		return appExcludeMgr;
	}

	public void setAppExcludeMgr(AppExcludeMgr appExcludeMgr) {
		this.appExcludeMgr = appExcludeMgr;
	}

	public UserInfoMgr getUserInfoMgr() {
		return userInfoMgr;
	}

	public void setUserInfoMgr(UserInfoMgr userInfoMgr) {
		this.userInfoMgr = userInfoMgr;
	}

	public void setGroupAttachInfoMgr(GroupAttachInfoMgr groupAttachInfoMgr) {
		this.groupAttachInfoMgr = groupAttachInfoMgr;
	}

	public void setBridgeService(BridgeService bridgeService) {
		this.bridgeService = bridgeService;
	}

	public void setAttachInfoMgr(AttachInfoMgr attachInfoMgr) {
		this.attachInfoMgr = attachInfoMgr;
	}

	/**
	 * 
	 * @return 返回unitMgr
	 */
    public CproUnitMgr getUnitMgr() {
        return unitMgr;
    }

    /**
     * 
     * @param unitMgr 设置unitMgr
     */
    public void setUnitMgr(CproUnitMgr unitMgr) {
        this.unitMgr = unitMgr;
    }
	
}
