package com.baidu.beidou.api.external.cprogroup.exporter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.error.GroupErrorCode;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigValidator;
import com.baidu.beidou.api.external.cprogroup.service.GroupConfigAddAndDeleteService;
import com.baidu.beidou.api.external.cprogroup.service.GroupConfigSetService;
import com.baidu.beidou.api.external.cprogroup.util.ApiTargetTypeUtil;
import com.baidu.beidou.api.external.cprogroup.util.GroupBoMappingUtil;
import com.baidu.beidou.api.external.cprogroup.vo.AttachInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.AttachSubUrlItemType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeIpType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeKeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeSiteType;
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
import com.baidu.beidou.api.external.cprogroup.vo.GroupSimilarPeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSiteType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupTradePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupTradeType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupVtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.InterestInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.KtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.PackInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.PackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.api.external.cprogroup.vo.RegionConfigType;
import com.baidu.beidou.api.external.cprogroup.vo.RegionItemType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteConfigType;
import com.baidu.beidou.api.external.cprogroup.vo.SitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteUrlItemType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteUrlType;
import com.baidu.beidou.api.external.cprogroup.vo.TargetInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.TradePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.TradeSitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.VtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.WordType;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupRegionItem;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddRegionRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddRtRelationRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddTradePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddTradeRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddVtPeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteAttachInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeletePackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteRegionRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteRtRelationRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteTradePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteTradeRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteVtPeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetAttachInfosRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetKeywordByWordIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetPriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetRegionConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetSimilarPeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetSiteConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetSiteUrlRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetTargetInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetTradeSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetPriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetRegionConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetSiteConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetSiteUrlRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetTargetInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetTradeSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateAttachInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateSimilarPeopleRequest;
import com.baidu.beidou.api.external.tool.vo.AttachInfoUserResponseType;
import com.baidu.beidou.api.external.tool.vo.request.AttachInfoUserRequestType;
import com.baidu.beidou.api.external.tool.vo.response.AttachInfoUserResponse;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.ErrorParam;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.api.internal.util.converter.YuanToCentConverter;
import com.baidu.beidou.api.util.RegionConvert;
import com.baidu.beidou.api.version.ApiVersionConstant;
import com.baidu.beidou.api.version.ApiVersionUtils;
import com.baidu.beidou.cprogroup.bo.AppExclude;
import com.baidu.beidou.cprogroup.bo.AttachInfo;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproGroupIT;
import com.baidu.beidou.cprogroup.bo.CproGroupITExclude;
import com.baidu.beidou.cprogroup.bo.CproGroupInfo;
import com.baidu.beidou.cprogroup.bo.CproGroupVT;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.cprogroup.bo.GroupInterestPrice;
import com.baidu.beidou.cprogroup.bo.GroupIpFilter;
import com.baidu.beidou.cprogroup.bo.GroupPack;
import com.baidu.beidou.cprogroup.bo.GroupSiteFilter;
import com.baidu.beidou.cprogroup.bo.GroupSitePrice;
import com.baidu.beidou.cprogroup.bo.GroupTradePrice;
import com.baidu.beidou.cprogroup.bo.WordExclude;
import com.baidu.beidou.cprogroup.bo.WordPackExclude;
import com.baidu.beidou.cprogroup.constant.AttachInfoConstant;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.SimilarPeopleConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
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
import com.baidu.beidou.cprogroup.service.GroupITPriceMgr;
import com.baidu.beidou.cprogroup.service.GroupPackMgr;
import com.baidu.beidou.cprogroup.service.GroupSiteConfigMgr;
import com.baidu.beidou.cprogroup.service.InterestMgr;
import com.baidu.beidou.cprogroup.service.SimilarPeopleMgr;
import com.baidu.beidou.cprogroup.service.UnionSiteCalculator;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.cprogroup.util.AttachInfoUtil;
import com.baidu.beidou.cprogroup.util.RegionIdConverter;
import com.baidu.beidou.cprogroup.util.SimilarPeopleUtil;
import com.baidu.beidou.cprogroup.util.TargettypeUtil;
import com.baidu.beidou.cprogroup.vo.AttachInfoVo;
import com.baidu.beidou.cprogroup.vo.BDSiteInfo;
import com.baidu.beidou.cprogroup.vo.GroupRegionOptVo;
import com.baidu.beidou.cprogroup.vo.GroupSiteOptVo;
import com.baidu.beidou.cprogroup.vo.GroupTradeSitePriceOptVo;
import com.baidu.beidou.cprogroup.vo.SiteSumInfo;
import com.baidu.beidou.cprogroup.vo.TradeInfo;
import com.baidu.beidou.cprogroup.vo.VtPeopleExcludeMapperVo;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.ubmcdriver.constant.UbmcConstant;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.user.service.UserInfoMgr;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.atomdriver.AtomUtils;
import com.baidu.beidou.util.bridgedriver.bo.BridgeResult;
import com.baidu.beidou.util.bridgedriver.constant.BridgeConstant;
import com.baidu.beidou.util.bridgedriver.service.BridgeService;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.sun.base.BaseResponseOptions;
import com.baidu.fengchao.tools.annotation.RPCMethod;
import com.baidu.fengchao.tools.annotation.RPCService;
import com.baidu.fengchao.tools.conf.ReturnType;
import com.baidu.unbiz.common.CollectionUtil;
import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.primitives.Ints;

/**
 * ClassName: GroupConfigServiceImpl Function: 推广组设置
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
@RPCService(serviceName = "GroupConfigService")
public class GroupConfigServiceImpl implements GroupConfigService {

    private static final Log log = LogFactory.getLog(GroupConfigServiceImpl.class);

    private CproGroupMgr cproGroupMgr;

    private CproUnitMgr unitMgr;

    private CproKeywordMgr cproKeywordMgr;

    private CproKeywordFacade cproKeywordFacade;

    private GroupSiteConfigMgr groupSiteConfigMgr;

    private GroupConfigValidator groupConfigValidator;

    private GroupConfigSetService groupConfigSetService;

    private GroupConfigAddAndDeleteService groupConfigADService;

    private UserMgr userMgr;

    private CproGroupVTMgr cproGroupVTMgr;

    private VtPeopleMgr vtPeopleMgr;

    private CproITFacade cproITFacade;

    private CproGroupITMgr cproGroupITMgr;

    private InterestMgr interestMgr;

    private CustomITMgr customITMgr;

    private WordExcludeFacade wordExcludeFacade;

    private GroupPackMgr groupPackMgr;

    private GroupPackFacade groupPackFacade;

    private GroupITPriceMgr groupITPriceMgr;

    private AppExcludeMgr appExcludeMgr;

    private UserInfoMgr userInfoMgr;

    private AttachInfoMgr attachInfoMgr;
    private GroupAttachInfoMgr groupAttachInfoMgr;
    private BridgeService bridgeService;

    private SimilarPeopleMgr similarPeopleMgr;

    // 推广组设置相关阀值限制
    private int getRegionConfigMax;
    private int getSiteConfigMax;
    private int getExcludeIpMax;
    private int getExcludeSiteMax;
    private int getExcludeAppMax;
    private int getKeywordMax;
    private int getWordMax;
    private int getTargetInfoMax = 10;
    private int getTradeSitePriceMax = 10;
    private int getSiteUrlMax = 10;
    private int getInterestInfoMax = 10;
    private int getExcludeKeywordMax = 100;
    private int getExcludePeopleMax = 100;
    private int getPackInfoMax = 100;
    private int getPriceMax = 100;
    private int getAttachInfoMax = 100;
    private int delAttachInfoMax = 100;
    private int addAttachInfoMax = 300;
    private int getSimilarPeopleMax = 100;
    private int updateSimilarPeopleMax = 100;

    public ApiResult<TargetInfoType> getTargetInfo(DataPrivilege user, GetTargetInfoRequest request, ApiOption apiOption) {
        ApiResult<TargetInfoType> result = new ApiResult<TargetInfoType>();
        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getGroupIds() == null || request.getGroupIds().length > getTargetInfoMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }
        // ---------------------------获取数据---------------------------------//

        long[] groupIdArray = request.getGroupIds();
        pay.setTotal(groupIdArray.length);

        for (int i = 0; i < groupIdArray.length; i++) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS, i);

            // groupid小于0
            if (groupIdArray[i] <= 0) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());

                continue;
            }

            // 未找到对应的group
            Integer groupId = Integer.parseInt(String.valueOf(groupIdArray[i]));
            CproGroup group = cproGroupMgr.findCproGroupById(groupId);
            if (group == null) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.NOT_FOUND.getMessage());

                continue;
            }

            // 组装数据
            TargetInfoType targetInfo = new TargetInfoType();
            targetInfo.setGroupId(groupId);

            int targetType = group.getTargetType();
            targetInfo.setType((ApiTargetTypeUtil.toApiGroupTargetType(targetType))); // 前端传递过来值需要转换成内部的TargetType

            if (TargettypeUtil.hasKT(targetType)) {
                /** 查询KT */
                User visitor = userMgr.findUserBySFid(user.getDataUser());
                List<CproKeyword> groupKtKeywordList =
                        cproKeywordMgr.getCproKeywordsByGroup(groupId, visitor.getUserid());

                if (!CollectionUtils.isEmpty(groupKtKeywordList)) {

                    Map<Integer, Integer> ktBlackList = cproKeywordMgr.getKTBlackListByUserId(user.getDataUser());

                    List<KeywordType> ktKeywordList = new ArrayList<KeywordType>();
                    for (CproKeyword kw : groupKtKeywordList) {
                        KeywordType keyWord = new KeywordType();
                        // 设置关键词字面
                        keyWord.setKeyword(kw.getKeyword());
                        // 设置展现资格
                        int qualification = getKtWordQuality(ktBlackList, kw.getKeyword(), kw.getWordId().intValue());
                        keyWord.setQualification(qualification);
                        ktKeywordList.add(keyWord);
                    }

                    KtItemType ktItem = new KtItemType();
                    ktItem.setKtWordList(ktKeywordList.toArray(new KeywordType[ktKeywordList.size()]));
                    ktItem.setAliveDays(group.getQtAliveDays()); // 设置KT有效期
                    ktItem.setTargetType(ApiTargetTypeUtil.toApiKtTargetType(targetType)); // 设置KT的定向方式
                    targetInfo.setKtItem(ktItem);
                }
            } else if (TargettypeUtil.hasRT(targetType)) {
                /*
                 * RT(RT2) has been deprecated since cpweb661 in 2013/10/28
                 */
            } else if (TargettypeUtil.hasVT(targetType)) {
                /** 查询VT */
                List<CproGroupVT> vtPeoples = cproGroupVTMgr.findCompleteVTRelationByGroup(groupId, user.getDataUser());
                if (CollectionUtils.isNotEmpty(vtPeoples)) {
                    List<Long> relatedPeopleIdList = new ArrayList<Long>();
                    List<Long> unrelatedPeopleIdList = new ArrayList<Long>();

                    for (CproGroupVT people : vtPeoples) {
                        Long peopleId = people.getPid();
                        if (people.getRelateType() == CproGroupConstant.GROUP_VT_INCLUDE_CROWD) {
                            relatedPeopleIdList.add(peopleId);
                        } else if (people.getRelateType() == CproGroupConstant.GROUP_VT_EXCLUDE_CROWD) {
                            unrelatedPeopleIdList.add(peopleId);
                        }
                    }

                    VtItemType vtItem = new VtItemType();
                    vtItem.setRelatedPeopleIds(relatedPeopleIdList.toArray(new Long[0]));
                    vtItem.setUnRelatePeopleIds(unrelatedPeopleIdList.toArray(new Long[0]));

                    targetInfo.setVtItem(vtItem);
                }
            }

            result = ApiResultBeanUtils.addApiResult(result, targetInfo);
            pay.increSuccess();
        }

        result.setPayment(pay);

        return result;
    }

    public ApiResult<ExcludeIpType> getExcludeIp(DataPrivilege user, GetExcludeIpRequest request, ApiOption apiOption) {
        ApiResult<ExcludeIpType> result = new ApiResult<ExcludeIpType>();

        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getGroupIds() == null || request.getGroupIds().length > getExcludeIpMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // ---------------------------获取数据---------------------------------//
        long[] groupIdArray = request.getGroupIds();
        pay.setTotal(groupIdArray.length);

        for (int i = 0; i < groupIdArray.length; i++) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS, i);
            if (groupIdArray[i] <= 0) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());
            } else {
                Integer groupId = Integer.parseInt(String.valueOf(groupIdArray[i]));

                List<GroupIpFilter> filters = groupSiteConfigMgr.findIpFilterByGroupId(groupId);

                if (filters == null) {
                    result =
                            ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                    GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                    GroupErrorCode.NOT_FOUND.getMessage());
                } else {

                    ExcludeIpType filter = new ExcludeIpType();

                    filter.setGroupId(groupId);

                    String[] strFilters = new String[filters.size()];

                    for (int j = 0; j < filters.size(); j++) {
                        strFilters[j] = filters.get(j).getIp();
                    }

                    filter.setExcludeIp(strFilters);

                    result = ApiResultBeanUtils.addApiResult(result, filter);

                    pay.increSuccess();
                }
            }
        }

        result.setPayment(pay);

        return result;
    }

    public ApiResult<RegionConfigType> getRegionConfig(DataPrivilege user, GetRegionConfigRequest request,
            ApiOption apiOption) {
        ApiResult<RegionConfigType> result = new ApiResult<RegionConfigType>();
        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getGroupIds() == null || request.getGroupIds().length > getRegionConfigMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // ---------------------------获取数据---------------------------------//

        long[] groupIdArray = request.getGroupIds();
        pay.setTotal(groupIdArray.length);

        // 如果调用方还未升级，当前使用的是北斗地域词表，需先转换成sys地域词表
        double version = 0;
        if (request.getVersion() != null) {
            version = Double.parseDouble(request.getVersion());
        }
        
        for (int i = 0; i < groupIdArray.length; i++) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS, i);
            if (groupIdArray[i] <= 0) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());
            } else {
                Integer groupId = Integer.parseInt(String.valueOf(groupIdArray[i]));
                CproGroupInfo g = cproGroupMgr.findCproGroupInfoById(groupId);

                if (g == null) {
                    result =
                            ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                    GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                    GroupErrorCode.NOT_FOUND.getMessage());
                } else {

                    RegionConfigType site = new RegionConfigType();

                    site.setGroupId(g.getGroupId());
                    site.setAllRegion(g.getIsAllRegion() == CproGroupConstant.GROUP_ALLREGION);
                    String regionStr = g.getSysRegListStr();
                    if (StringUtils.isNotEmpty(regionStr)) {
                        if (version < ApiVersionConstant.SYS_REG_VERSION) {
                            regionStr = RegionIdConverter.getBeidouRegListStrFromSys(regionStr);
                        }

                        String[] regions = regionStr.split("\\|");
                        List<RegionItemType> regList = new ArrayList<RegionItemType>(regions.length);
                        for (int j = 0; j < regions.length; j++) {
                            Integer regionId = Integer.parseInt(regions[j]);
                            RegionItemType regItem = new RegionItemType(1, regionId);
                            regList.add(regItem);
                        }
                        RegionItemType[] regs = new RegionItemType[regList.size()];
                        regs = regList.toArray(regs);
                        site.setRegionList(regs);
                    }

                    result = ApiResultBeanUtils.addApiResult(result, site);
                    pay.increSuccess();
                }
            }
        }
        result.setPayment(pay);

        return result;
    }

    public ApiResult<SiteConfigType> getSiteConfig(DataPrivilege user, GetSiteConfigRequest request, ApiOption apiOption) {
        ApiResult<SiteConfigType> result = new ApiResult<SiteConfigType>();
        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getGroupIds() == null || request.getGroupIds().length > getSiteConfigMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }
        // ---------------------------获取数据---------------------------------//

        long[] groupIdArray = request.getGroupIds();
        pay.setTotal(groupIdArray.length);

        for (int i = 0; i < groupIdArray.length; i++) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS, i);
            if (groupIdArray[i] <= 0) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());
            } else {
                Integer groupId = Integer.parseInt(String.valueOf(groupIdArray[i]));
                CproGroupInfo g = cproGroupMgr.findCproGroupInfoById(groupId);

                if (g == null) {
                    result =
                            ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                    GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                    GroupErrorCode.NOT_FOUND.getMessage());
                } else {

                    SiteConfigType site = new SiteConfigType();

                    site.setGroupId(g.getGroupId());
                    site.setAllSite(g.getIsAllSite() == CproGroupConstant.GROUP_ALLSITE);
                    if (g.getSiteListStr() != null) {
                        String[] siteIds = g.getSiteListStr().split("\\|");
                        // 加入Set的目的，主要是为了做滤重
                        Set<String> siteStr = new HashSet<String>();
                        if (siteIds != null) {
                            List<BDSiteInfo> infos = UnionSiteCache.siteInfoCache.getSiteInfoList();
                            Map<Integer, Integer> siteIndexMap = UnionSiteCache.siteInfoCache.getReverseIndexBySiteId();
                            for (int k = 0; k < siteIds.length; k++) {
                                // 为了避免出现""，因此加入判断
                                if (siteIds[k] == null || siteIds[k].length() == 0) {
                                    continue;
                                }
                                Integer index = siteIndexMap.get(Integer.valueOf(siteIds[k]));
                                if (index != null) {
                                    siteStr.add(infos.get(index).getSiteurl());
                                }
                            }
                        }
                        String[] siteStrs = new String[siteStr.size()];
                        siteStrs = siteStr.toArray(siteStrs);
                        site.setSiteList(siteStrs);
                    }
                    if (StringUtils.isNotEmpty(g.getSiteTradeListStr())) {
                        String[] trades = g.getSiteTradeListStr().split("\\|");
                        int[] cates = new int[trades.length];
                        for (int j = 0; j < trades.length; j++) {
                            cates[j] = Integer.parseInt(trades[j]);
                        }
                        site.setCategoryList(cates);
                    }

                    result = ApiResultBeanUtils.addApiResult(result, site);

                    pay.increSuccess();
                }
            }
        }

        result.setPayment(pay);

        return result;

    }

    public ApiResult<ExcludeSiteType> getExcludeSite(DataPrivilege user, GetExcludeSiteRequest request,
            ApiOption apiOption) {
        ApiResult<ExcludeSiteType> result = new ApiResult<ExcludeSiteType>();
        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getGroupIds() == null || request.getGroupIds().length > getExcludeSiteMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // ---------------------------获取数据---------------------------------//

        long[] groupIdArray = request.getGroupIds();
        pay.setTotal(groupIdArray.length);

        for (int i = 0; i < groupIdArray.length; i++) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS, i);

            if (groupIdArray[i] <= 0) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());
            } else {
                Integer groupId = Integer.parseInt(String.valueOf(groupIdArray[i]));

                List<GroupSiteFilter> filters = groupSiteConfigMgr.findSiteFilterByGroupId(groupId);

                if (filters == null) {
                    result =
                            ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                    GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                    GroupErrorCode.NOT_FOUND.getMessage());
                } else {

                    ExcludeSiteType site = new ExcludeSiteType();

                    site.setGroupId(groupId);

                    String[] strFilters = new String[filters.size()];

                    for (int j = 0; j < filters.size(); j++) {
                        strFilters[j] = filters.get(j).getSite();
                    }

                    site.setExcludeSite(strFilters);

                    result = ApiResultBeanUtils.addApiResult(result, site);

                    pay.increSuccess();
                }
            }
        }

        result.setPayment(pay);

        return result;
    }

    public ApiResult<TradeSitePriceType> getTradeSitePrice(DataPrivilege user, GetTradeSitePriceRequest request,
            ApiOption apiOption) {
        ApiResult<TradeSitePriceType> result = new ApiResult<TradeSitePriceType>();
        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getGroupIds() == null || request.getGroupIds().length > getTradeSitePriceMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }
        // ---------------------------获取数据---------------------------------//

        long[] groupIdArray = request.getGroupIds();
        pay.setTotal(groupIdArray.length);

        for (int i = 0; i < groupIdArray.length; i++) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS, i);

            // groupid小于0
            if (groupIdArray[i] <= 0) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());

                continue;
            }

            // 未找到对应的group
            Integer groupId = Integer.parseInt(String.valueOf(groupIdArray[i]));
            CproGroupInfo g = cproGroupMgr.findCproGroupInfoById(groupId);
            if (g == null) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.NOT_FOUND.getMessage());

                continue;
            }

            TradeSitePriceType tradeSitePrice = new TradeSitePriceType();
            tradeSitePrice.setGroupId(groupId);

            YuanToCentConverter conv = new YuanToCentConverter();

            // 获取分行业出价
            List<TradePriceType> tradePriceList = new ArrayList<TradePriceType>();
            tradeSitePrice.setTradePriceList(tradePriceList);

            List<GroupTradePrice> tradePriceDBList =
                    groupSiteConfigMgr.findAllTradePrice(user.getDataUser(), null, groupId);
            if (!CollectionUtils.isEmpty(tradePriceDBList)) {
                for (GroupTradePrice groupTradePrice : tradePriceDBList) {
                    TradePriceType tp = new TradePriceType();
                    tp.setTradeId(groupTradePrice.getTradeid());
                    tp.setPrice(conv.convertTo(groupTradePrice.getPrice()));
                    tradePriceList.add(tp);
                }
            }

            // 获取分网站出价
            List<SitePriceType> sitePriceList = new ArrayList<SitePriceType>();
            tradeSitePrice.setSitePriceList(sitePriceList);

            List<GroupSitePrice> sitePriceDBList =
                    groupSiteConfigMgr.findAllSitePrice(user.getDataUser(), null, groupId);
            if (!CollectionUtils.isEmpty(sitePriceDBList)) {
                for (GroupSitePrice groupSitePrice : sitePriceDBList) {
                    SitePriceType sp = new SitePriceType();
                    sp.setSite(groupSitePrice.getSiteurl());
                    sp.setPrice(conv.convertTo(groupSitePrice.getPrice()));
                    sitePriceList.add(sp);
                }
            }

            result = ApiResultBeanUtils.addApiResult(result, tradeSitePrice);

            pay.increSuccess();
        }

        result.setPayment(pay);

        return result;
    }

    public ApiResult<SiteUrlType> getSiteUrl(DataPrivilege user, GetSiteUrlRequest request, ApiOption apiOption) {
        ApiResult<SiteUrlType> result = new ApiResult<SiteUrlType>();
        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getGroupIds() == null || request.getGroupIds().length > getSiteUrlMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }
        // ---------------------------获取数据---------------------------------//

        long[] groupIdArray = request.getGroupIds();
        pay.setTotal(groupIdArray.length);

        for (int i = 0; i < groupIdArray.length; i++) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS, i);

            if (groupIdArray[i] <= 0) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());

                continue;
            }

            Integer groupId = Integer.parseInt(String.valueOf(groupIdArray[i]));
            CproGroupInfo g = cproGroupMgr.findCproGroupInfoById(groupId);
            if (g == null) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.NOT_FOUND.getMessage());

                continue;
            }

            SiteUrlType adSiteUrl = new SiteUrlType();
            adSiteUrl.setGroupId(groupId);

            List<SiteUrlItemType> siteUrlList = new ArrayList<SiteUrlItemType>();
            adSiteUrl.setSiteUrlList(siteUrlList);

            List<GroupSitePrice> list = groupSiteConfigMgr.findSiteTargetUrlByGroupId(groupId);
            if (!CollectionUtils.isEmpty(list)) {
                for (GroupSitePrice sitePrice : list) {
                    SiteUrlItemType siteUrl = new SiteUrlItemType();

                    if (sitePrice != null) {
                        siteUrl.setSiteUrl(sitePrice.getSiteurl());
                        siteUrl.setTargetUrl(sitePrice.getTargeturl());

                        siteUrlList.add(siteUrl);
                    }
                }
            }

            result = ApiResultBeanUtils.addApiResult(result, adSiteUrl);
            pay.increSuccess();
        }

        result.setPayment(pay);

        return result;
    }

    public ApiResult<KeywordType> getKeyword(DataPrivilege user, GetKeywordRequest request, ApiOption apiOption) {
        ApiResult<KeywordType> result = new ApiResult<KeywordType>();
        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getKeywordIds() == null || request.getKeywordIds().length == 0
                || request.getKeywordIds().length > getKeywordMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.KEYWORDID_LIST);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        User userInfo = userMgr.findUserBySFid(user.getDataUser());

        // ---------------------------获取数据---------------------------------//

        pay.setTotal(request.getKeywordIds().length);
        List<Long> toFindList = new ArrayList<Long>(request.getKeywordIds().length);

        for (int i = 0; i < request.getKeywordIds().length; i++) {
            Long keywordId = request.getKeywordIds()[i];
            if (keywordId <= 0) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.KEYWORDID_LIST, i);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());
            } else {
                toFindList.add(keywordId);
            }
        }

        List<CproKeyword> found = cproKeywordFacade.findByIds(toFindList, userInfo.getUserid());

        Map<Long, CproKeyword> wordMap = new HashMap<Long, CproKeyword>();
        if (CollectionUtils.isNotEmpty(found)) {
            for (CproKeyword keyword : found) {
                wordMap.put(Long.valueOf(keyword.getId()), keyword);
            }
        }

        Map<Integer, Integer> ktBlackList = cproKeywordMgr.getKTBlackListByUserId(userInfo.getUserid());
        for (int i = 0; i < request.getKeywordIds().length; i++) {
            Long keywordId = request.getKeywordIds()[i];
            if (keywordId > 0) {
                CproKeyword keyword = wordMap.get(keywordId);
                if (keyword == null) {
                    ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                    apiPosition.addParam(GroupConstant.KEYWORDID_LIST, i);

                    result =
                            ApiResultBeanUtils.addApiError(result, GroupErrorCode.KEYWORD_NOT_FOUND.getValue(),
                                    GroupErrorCode.KEYWORD_NOT_FOUND.getMessage(), apiPosition.getPosition(),
                                    GroupErrorCode.KEYWORD_NOT_FOUND.getMessage());
                } else {
                    KeywordType keywordType = new KeywordType();
                    keywordType.setKeyword(keyword.getKeyword());

                    int qualification =
                            getKtWordQuality(ktBlackList, keyword.getKeyword(), keyword.getWordId().intValue());
                    keywordType.setQualification(qualification);

                    result = ApiResultBeanUtils.addApiResult(result, keywordType);
                    pay.increSuccess();
                }
            }
        }
        result.setPayment(pay);

        return result;
    }

    @RPCMethod(methodName = "getKeywordByWordId", returnType = ReturnType.ARRAY)
    public BaseResponse<WordType> getKeywordByWordId(BaseRequestUser user, GetKeywordByWordIdRequest param,
            BaseRequestOptions apiOption) {

        BaseResponse<WordType> response = new BaseResponse<WordType>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        // 检查传入的id列表不为空
        long[] wordIds = param.getWordIds();
        if (ArrayUtils.isEmpty(wordIds)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.WORDID_LIST);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 验证传入参数长度
        if (wordIds.length > getWordMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.WORDID_LIST);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 配额总数设为查询数量
        option.setTotal(wordIds.length);

        List<Long> toFindList = new ArrayList<Long>(wordIds.length);
        Set<Long> toFindSet = new HashSet<Long>(wordIds.length);

        for (int i = 0; i < wordIds.length; i++) {
            Long wordId = wordIds[i];
            if (wordId <= 0) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.WORDID_LIST, i);

                response =
                        DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                                GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            } else {
                toFindList.add(wordId);
                toFindSet.add(wordId);
            }
        }

        List<WordType> result = new ArrayList<WordType>(wordIds.length);
        Map<Long, String> wordMap = AtomUtils.getWordById(toFindSet);
        for (int i = 0; i < toFindList.size(); i++) {
            Long wordId = toFindList.get(i);
            String word = wordMap.get(wordId);
            if (word == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.WORDID_LIST, i);

                response =
                        DRAPIMountAPIBeanUtils.addApiError(response, GroupErrorCode.WORD_NOT_FOUND.getValue(),
                                GroupErrorCode.WORD_NOT_FOUND.getMessage(), apiPosition.getPosition(), null);

            } else {
                WordType wordType = new WordType();
                wordType.setKeyword(word.trim());
                result.add(wordType);
            }
        }

        response.getOptions().setSuccess(result.size());
        response.setData(result.toArray(new WordType[0]));

        return response;
    }

    public ApiResult<InterestInfoType> getInterestInfo(DataPrivilege user, GetInterestInfoRequest request,
            ApiOption apiOption) {
        ApiResult<InterestInfoType> result = new ApiResult<InterestInfoType>();
        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getGroupIds() == null || request.getGroupIds().length > getInterestInfoMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }
        // ---------------------------获取数据---------------------------------//

        long[] groupIdArray = request.getGroupIds();
        pay.setTotal(groupIdArray.length);

        for (int i = 0; i < groupIdArray.length; i++) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS, i);

            if (groupIdArray[i] <= 0) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());

                continue;
            }

            Integer groupId = Integer.parseInt(String.valueOf(groupIdArray[i]));
            CproGroup group = cproGroupMgr.findCproGroupById(groupId);
            if (group == null) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.NOT_FOUND.getMessage());

                continue;
            }

            InterestInfoType interestInfoType = new InterestInfoType();
            interestInfoType.setGroupId(groupId);
            if (!TargettypeUtil.hasIT(group.getTargetType())) {
                interestInfoType.setEnable(false);
            } else {
                interestInfoType.setEnable(true);
            }

            // 获取该推广组所关联与排除的关系
            List<CproGroupIT> groupITList = cproGroupITMgr.findGroupITList(groupId);
            List<CproGroupITExclude> groupITExcList = cproGroupITMgr.findGroupITExcludeList(groupId);
            int[] interestIds = new int[groupITList.size()];
            int[] exceptInterestIds = new int[groupITExcList.size()];
            for (int index = 0; index < groupITList.size(); index++) {
                interestIds[index] = groupITList.get(index).getIid();
            }
            for (int index = 0; index < groupITExcList.size(); index++) {
                exceptInterestIds[index] = groupITExcList.get(index).getIid();
            }
            interestInfoType.setInterestIds(interestIds);
            interestInfoType.setExceptInterestIds(exceptInterestIds);

            result = ApiResultBeanUtils.addApiResult(result, interestInfoType);
            pay.increSuccess();
        }

        result.setPayment(pay);

        return result;
    }

    /**
     * getKtWordQuality: 填充关键词展现资格
     * 
     * @param keyword 关键词字面
     * @param wordId atomId
     * @version GroupConfigServiceImpl
     * @author zhangxu
     * @date 2012-4-1
     */
    private int getKtWordQuality(Map<Integer, Integer> blackList, String keyword, Integer wordId) {
        if (blackList != null) {
            Integer q = blackList.get(wordId);
            if (q != null) {
                return q;
            } else if (com.baidu.beidou.util.StringUtils.hitKTBlackRules(keyword)) {
                return CproGroupConstant.KT_WORD_QUALITY_DEGREE_1;
            }
        }

        return CproGroupConstant.KT_WORD_QUALITY_DEGREE_3;
    }

    public ApiResult<Object> setTargetInfo(DataPrivilege user, SetTargetInfoRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getTargetInfo() == null || request.getTargetInfo().getGroupId() <= 0) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TARGET_INFO);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        TargetInfoType targetInfo = request.getTargetInfo();

        // ---------------------------设置数据---------------------------------//
        Integer groupId = Integer.valueOf(String.valueOf(targetInfo.getGroupId()));

        CproGroup group = cproGroupMgr.findWithInfoById(groupId);

        if (group == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TARGET_INFO);
            apiPosition.addParam(GroupConstant.GROUPID);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupErrorCode.NOT_FOUND.getValue(),
                            GroupErrorCode.NOT_FOUND.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        if (TargettypeUtil.isPack(group.getTargetType())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TARGET_INFO);
            apiPosition.addParam(GroupConstant.TARGETTYPE);

            result =
                    ApiResultBeanUtils.addApiError(result,
                            GroupConfigErrorCode.TARGETTYPE_CANNOT_CHANGE_IF_SET_TO_PACK.getValue(),
                            GroupConfigErrorCode.TARGETTYPE_CANNOT_CHANGE_IF_SET_TO_PACK.getMessage(),
                            apiPosition.getPosition(), null);
            return result;
        }

        int type = targetInfo.getType();

        // 定向方式错误
        if (!ApiTargetTypeUtil.isValidTargetType(type)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TARGET_INFO);
            apiPosition.addParam(GroupConstant.TARGETTYPE);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.TARGETTYPE_TYPE_ERROR.getValue(),
                            GroupConfigErrorCode.TARGETTYPE_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // 智能创意推广组不能更新定向方式为不指定
        if (group.getIsSmart() == CproGroupConstant.IS_SMART_TRUE && type == GroupConstant.API_TARGET_TYPE_NONE) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TARGET_INFO);
            apiPosition.addParam(GroupConstant.TARGETTYPE);

            result =
                    ApiResultBeanUtils.addApiError(result,
                            GroupConfigErrorCode.SMARTIDEA_GROUP_CANNOT_SET_TO_PT_TARGETTYPE.getValue(),
                            GroupConfigErrorCode.SMARTIDEA_GROUP_CANNOT_SET_TO_PT_TARGETTYPE.getMessage(),
                            apiPosition.getPosition(), null);
            return result;
        }

        boolean isSucc = false;

        // 历史操作记录保存对象
        List<OptContent> optContents = new ArrayList<OptContent>();

        try {
            // 处理KT
            if (type == GroupConstant.API_TARGET_TYPE_KT) {
                isSucc =
                        groupConfigSetService
                                .saveKT(result, group, targetInfo.getKtItem(), cproKeywordMgr, optContents);

            }

            // 处理非定向
            if (type == GroupConstant.API_TARGET_TYPE_NONE) {
                isSucc = groupConfigSetService.saveNone(result, group, optContents);
            }

            // 处理RT
            if (type == GroupConstant.API_TARGET_TYPE_RT) {
                /*
                 * RT(RT2) has been deprecated since cpweb661 in 2013/10/28
                 */
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.TARGET_INFO);
                apiPosition.addParam(GroupConstant.RT_ITEM);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.RT_DEPREATED_ERROR.getValue(),
                                GroupConfigErrorCode.RT_DEPREATED_ERROR.getMessage(), apiPosition.getPosition(), null);
                return result;
            }

            // 处理VT
            if (type == GroupConstant.API_TARGET_TYPE_VT) {
                // 验证VT
                if (targetInfo.getVtItem() == null) {
                    ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                    apiPosition.addParam(GroupConstant.TARGET_INFO);
                    apiPosition.addParam(GroupConstant.VT_ITEM);

                    result =
                            ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.VT_PEOPLE_ERROR.getValue(),
                                    GroupConfigErrorCode.VT_PEOPLE_ERROR.getMessage(), apiPosition.getPosition(), null);
                    return result;
                }
                isSucc =
                        groupConfigSetService.saveVT(result, group, targetInfo.getVtItem(), vtPeopleMgr,
                                cproGroupVTMgr, optContents);
            }

        } catch (Exception e) {
            StringBuffer sb =
                    new StringBuffer("Change targetType from ").append(group.getTargetType()).append(" to ")
                            .append(CproGroupConstant.GROUP_TARGET_TYPE_NONE).append(" userId=")
                            .append(group.getUserId()).append(", groupId=").append(group.getGroupId())
                            .append(" failed! ").append(e.getMessage());
            log.error(sb.toString(), e);

            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TARGET_INFO);
            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SET_TARGETTYPE_ERROR.getValue(),
                            GroupConfigErrorCode.SET_TARGETTYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
        }

        // 如果成功才记录历史操作记录，并incr成功值
        if (isSucc) {
            StringBuffer sb =
                    new StringBuffer("Change targetType from ").append(group.getTargetType()).append(" to ")
                            .append(type).append(" userId=").append(group.getUserId()).append(", groupId=")
                            .append(group.getGroupId()).append(" successfully!");
            log.info(sb);
            SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); // 加入session中，后续有拦截器处理
            pay.increSuccess();
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> setExcludeIp(DataPrivilege user, SetExcludeIpRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        ExcludeIpType ipFilter = request.getExcludeIp();

        if (ipFilter == null || ipFilter.getGroupId() <= 0 || ipFilter.getExcludeIp() == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_IP_REQ);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // ---------------------------设置数据---------------------------------//
        Integer groupId = Integer.valueOf(String.valueOf(ipFilter.getGroupId()));

        String[] ips = ipFilter.getExcludeIp();
        Set<String> allIp = new HashSet<String>();

        List<String> ipList = Arrays.asList(ips);
        result = groupConfigValidator.validateIpFilter(groupId, ipList, allIp, result);

        // 验证是否通过
        if (!CollectionUtils.isEmpty(result.getErrors())) {
            return result;
        }

        // List<String> toAddSite = GroupConfigValidator.validateSiteFilter(
        // groupId, support, siteFilterList, siteList, textProvider);
        // delGroupSiteFilter(id, visitor);

        if (allIp.size() >= 0) {
            result = this.groupConfigSetService.saveIpFilter(result, groupId, user.getDataUser(), allIp);

            StringBuilder sb =
                    new StringBuilder("### save IpFilter, size is ").append(allIp.size()).append(", groupId=")
                            .append(groupId);
            log.info(sb);
        }

        return result;
    }

    public ApiResult<Object> setRegionConfig(DataPrivilege user, SetRegionConfigRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();


        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        RegionConfigType regionConfig = request.getRegionConfig();
        if (regionConfig == null || regionConfig.getGroupId() <= 0) {
            return GlobalErrorCode.UNEXPECTED_PARAMETER.getErrorResponse(result,
                    Lists.newArrayList(new ErrorParam(GroupConstant.REGION_CONFIG_REQ, null)), null);
        }
        
        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);
        
        // 如果调用方还未升级，当前使用的是北斗地域词表，需先转换成sys地域词表
        double version = 0;
        if (request.getVersion() != null) {
            version = Double.parseDouble(request.getVersion());
        }
        if (version < ApiVersionConstant.SYS_REG_VERSION && regionConfig.getRegionList() != null
                && regionConfig.getRegionList().length > 0) {
            regionConfig.setRegionList(RegionConvert.bdToSys(regionConfig.getRegionList()));
        }

        boolean setResult = groupConfigADService.setRegion(result, regionConfig);
        if (setResult) {
            pay.setSuccess(1);
            ApiResultBeanUtils.setSuccessObject(result);
        }
        return result;
    }

    public ApiResult<Object> setSiteConfig(DataPrivilege user, SetSiteConfigRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();
        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        SiteConfigType siteConfig = request.getSiteConfig();

        if (siteConfig == null || siteConfig.getGroupId() <= 0) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITE_CONFIG_REQ);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // ---------------------------设置数据---------------------------------//
        Integer groupId = Integer.valueOf(String.valueOf(siteConfig.getGroupId()));

        CproGroup group = cproGroupMgr.findWithInfoById(groupId);
        if (group == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITE_CONFIG_REQ);
            apiPosition.addParam(GroupConstant.GROUPID);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupErrorCode.NOT_FOUND.getValue(),
                            GroupErrorCode.NOT_FOUND.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        CproGroupInfo groupInfo = group.getGroupInfo();
        CproGroupInfo groupInfoBefore = new CproGroupInfo();
        try {
            BeanUtils.copyProperties(groupInfoBefore, groupInfo);
        } catch (Exception e) {
            log.error("failed to copy groupinfo. " + e.getMessage(), e);
        }

        boolean isNotUpdate = false;
        if (siteConfig.getSiteList() != null && siteConfig.getCategoryList() != null
                && siteConfig.getSiteList().length == 1
                && siteConfig.getSiteList()[0].equals(GroupConstant.API_GLOBAL_NOT_UPDATE_FLAG_STR)
                && siteConfig.getCategoryList().length == 1
                && siteConfig.getCategoryList()[0] == GroupConstant.API_GLOBAL_NOT_UPDATE_FLAG_INT) {
            isNotUpdate = true;
        }

        if (siteConfig.isAllSite()) {
            groupInfo.setIsAllSite(CproGroupConstant.GROUP_ALLSITE);
            if (!isNotUpdate) {
                groupInfo.setSiteListStr(null);
                groupInfo.setSiteSum(0);
                groupInfo.setSiteTradeListStr(null);
            }
        } else {
            String[] sites = siteConfig.getSiteList();
            int[] trades = siteConfig.getCategoryList();

            if ((sites == null || sites.length == 0) && (trades == null || trades.length == 0)) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.SITE_CONFIG_REQ);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(), null);
                return result;
            }

            List<Integer> siteIds = new ArrayList<Integer>();
            List<Integer> tradeIds = new ArrayList<Integer>();

            Map<String, Integer> siteIdMap = UnionSiteCache.siteInfoCache.getSiteIdMap();
            groupInfo.setIsAllSite(0);

            if (!isNotUpdate) {

                // key: siteId, value: index in request
                Map<Integer, Integer> siteIndexMap = new HashMap<Integer, Integer>();
                if (sites != null && sites.length != 0) {
                    Map<Integer, Boolean> listCompare = new HashMap<Integer, Boolean>();

                    for (Integer index = 0; index < sites.length; index++) {
                        Integer siteId = siteIdMap.get(sites[index].toLowerCase());
                        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                        apiPosition.addParam(GroupConstant.SITE_CONFIG_REQ);
                        apiPosition.addParam(GroupConstant.SITE_CONFIG_SITELIST, index);

                        if (siteId != null) {
                            if (listCompare.get(siteId) != null) {
                                result =
                                        ApiResultBeanUtils.addApiError(result,
                                                GroupConfigErrorCode.SITE_DUP.getValue(),
                                                GroupConfigErrorCode.SITE_DUP.getMessage(), apiPosition.getPosition(),
                                                null);
                                // } else if (WhiteListCache.baiduSites.hasId(siteId)
                                // && !WhiteListCache.useBaiduUsers.has(user.getDataUser())) {
                                // // 如果网站在网站白名单中，但是用户不在用户白名单中，则跳过此条并记录错误信息
                                // result = ApiResultBeanUtils.addApiError(result,
                                // GroupConfigErrorCode.NO_BAIDU_SITE_PRIVELEGE.getValue(),
                                // GroupConfigErrorCode.NO_BAIDU_SITE_PRIVELEGE.getMessage(),
                                // apiPosition.getPosition(), null);
                            } else {
                                siteIds.add(siteId);
                                listCompare.put(siteId, true);
                                siteIndexMap.put(siteId, index);
                            }
                        } else {
                            result =
                                    ApiResultBeanUtils.addApiError(result,
                                            GroupConfigErrorCode.SITE_NOT_FOUND.getValue(),
                                            GroupConfigErrorCode.SITE_NOT_FOUND.getMessage(),
                                            apiPosition.getPosition(), null);
                        }
                    }
                }

                // 检验是否超过单个推广组投放网络设置的最大值
                if (siteIds.size() > CproGroupConstant.SITE_SELECTED_MAX_NUM) {
                    ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                    apiPosition.addParam(GroupConstant.SITE_CONFIG_REQ);

                    result =
                            ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SITE_SET_MAX.getValue(),
                                    GroupConfigErrorCode.SITE_SET_MAX.getMessage(), apiPosition.getPosition(), null);

                    return result;
                }

                if (trades != null && trades.length != 0) {
                    // 贴片推广组不支持设置分类
                    // @version cpweb443 仅为贴片推广组时不能添加行业
                    if (group.getGroupType() == CproGroupConstant.GROUP_TYPE_FILM) {
                        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                        apiPosition.addParam(GroupConstant.SITE_CONFIG_REQ);
                        apiPosition.addParam(GroupConstant.SITE_CONFIG_CATEGORYLIST);

                        result =
                                ApiResultBeanUtils.addApiError(result,
                                        GroupConfigErrorCode.TRADE_GROUP_TYPE_ERROR.getValue(),
                                        GroupConfigErrorCode.TRADE_GROUP_TYPE_ERROR.getMessage(),
                                        apiPosition.getPosition(), null);
                    } else {
                        Map<Integer, String> tradeMap = UnionSiteCache.tradeInfoCache.getSiteTradeNameList();

                        Map<Integer, Boolean> listCompare = new HashMap<Integer, Boolean>();

                        for (Integer p = 0; p < trades.length; p++) {
                            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                            apiPosition.addParam(GroupConstant.SITE_CONFIG_REQ);
                            apiPosition.addParam(GroupConstant.SITE_CONFIG_CATEGORYLIST, p);

                            if (tradeMap.get(trades[p]) == null) {
                                result =
                                        ApiResultBeanUtils.addApiError(result,
                                                GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(),
                                                GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(),
                                                apiPosition.getPosition(), null);
                            } else {
                                if (listCompare.get(trades[p]) != null) {
                                    result =
                                            ApiResultBeanUtils.addApiError(result,
                                                    GroupConfigErrorCode.CATEGORY_DUP.getValue(),
                                                    GroupConfigErrorCode.CATEGORY_DUP.getMessage(),
                                                    apiPosition.getPosition(), null);
                                    // } else if (WhiteListCache.baiduTrades.has(trades[p])
                                    // && !WhiteListCache.useBaiduUsers.has(user.getDataUser())) {
                                    // // 如果行业在行业白名单中，但是用户不在用户白名单中，则跳过此条并记录错误信息
                                    // result = ApiResultBeanUtils.addApiError(result,
                                    // GroupConfigErrorCode.NO_BAIDU_TRADE_PRIVELEGE.getValue(),
                                    // GroupConfigErrorCode.NO_BAIDU_TRADE_PRIVELEGE.getMessage(),
                                    // apiPosition.getPosition(), null);
                                } else {
                                    tradeIds.add(trades[p]);
                                    listCompare.put(trades[p], true);
                                }
                            }
                        }

                        // 检查已经存在一级行业的二级行业，并准备将其删除
                        List<Integer> toDelTradeIds = new ArrayList<Integer>();
                        for (Integer tradeId : tradeIds) {
                            TradeInfo tradeInfo = UnionSiteCache.tradeInfoCache.getTradeInfoById(tradeId);

                            // 如果待添加的为二级行业，并且其所属的一级行业也已经在待添加集合中，那么删除该二级行业
                            if (tradeInfo.getParentid() != 0 && tradeIds.contains(tradeInfo.getParentid())) {
                                toDelTradeIds.add(tradeId);
                            }
                        }

                        // 删除待删除的二级行业
                        tradeIds.removeAll(toDelTradeIds);
                    }

                }

                // 如果验证出现错误，就直接返回
                if (CollectionUtils.isNotEmpty(result.getErrors())) {
                    return result;
                }

                // 必须先排序
                Collections.sort(siteIds);
                Collections.sort(tradeIds);

                SiteSumInfo siteInfo =
                        UnionSiteCalculator.genSiteInfo(siteIds, tradeIds, group.getUserId(), group.getGroupType());

                // add by zhangxu 如果有些网站因为推广组类型和网站类型不匹配被过滤掉，则报错
                int filterOutSiteNum = siteIds.size() - siteInfo.getSiteList().size();
                if (filterOutSiteNum != 0) {
                    for (Integer siteId : siteIds) {
                        if (!siteInfo.getSiteList().contains(siteId)) {
                            Integer index = siteIndexMap.get(siteId);
                            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                            apiPosition.addParam(GroupConstant.SITE_CONFIG_REQ);
                            apiPosition.addParam(GroupConstant.SITE_CONFIG_SITELIST, index);

                            result =
                                    ApiResultBeanUtils.addApiError(result,
                                            GroupConfigErrorCode.SITE_TRADE_SOME_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP
                                                    .getValue(),
                                            GroupConfigErrorCode.SITE_TRADE_SOME_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP
                                                    .getMessage(), apiPosition.getPosition(), null);
                        }
                    }
                    return result;
                }

                if (CollectionUtils.isEmpty(siteInfo.getSiteList())
                        && CollectionUtils.isEmpty(siteInfo.getSiteTradeList())) {
                    ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                    apiPosition.addParam(GroupConstant.SITE_CONFIG_REQ);

                    result =
                            ApiResultBeanUtils.addApiError(result,
                                    GroupConfigErrorCode.SITE_TRADE_ALL_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP
                                            .getValue(),
                                    GroupConfigErrorCode.SITE_TRADE_ALL_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP
                                            .getMessage(), apiPosition.getPosition(), null);
                    return result;
                }
                groupInfo.setSiteSum(siteInfo.getSiteSum());
                groupInfo.setCmpLevel(siteInfo.getCmpLevel());
                // 避免由于""被设到db中造成错误
                String siteListStr =
                        com.baidu.beidou.util.StringUtils.makeStrFromCollectionForSite(siteInfo.getSiteList(),
                                CproGroupConstant.FIELD_SEPERATOR);
                if (siteListStr == null || siteListStr.length() == 0) {
                    siteListStr = null;
                }
                String tradeListStr =
                        com.baidu.beidou.util.StringUtils.makeStrFromCollectionForSite(siteInfo.getSiteTradeList(),
                                CproGroupConstant.FIELD_SEPERATOR);
                if (tradeListStr == null || tradeListStr.length() == 0) {
                    tradeListStr = null;
                }
                groupInfo.setSiteListStr(siteListStr);
                groupInfo.setSiteTradeListStr(tradeListStr);
            }
        }

        Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

        CproGroupInfo cg = cproGroupMgr.modCproGroupInfo(visitor, groupInfo);

        if (cg != null) {

            // ------- 保存历史操作记录 start ------
            try {
                List<OptContent> optContents = new ArrayList<OptContent>();

                OpTypeVo opTypeTrade = OptHistoryConstant.OPTYPE_GROUP_TRADE_EDIT;
                OptContent contentTrade =
                        new OptContent(group.getUserId(), opTypeTrade.getOpType(), opTypeTrade.getOpLevel(),
                                group.getGroupId(), opTypeTrade.getTransformer().toDbString(
                                        new GroupSiteOptVo(groupInfoBefore)), opTypeTrade.getTransformer().toDbString(
                                        new GroupSiteOptVo(groupInfo)));
                if (!contentTrade.getPreContent().equals(contentTrade.getPostContent())) {
                    optContents.add(contentTrade);
                }

                OpTypeVo opTypeSite = OptHistoryConstant.OPTYPE_GROUP_SITE_EDIT;
                OptContent contentSite =
                        new OptContent(group.getUserId(), opTypeSite.getOpType(), opTypeSite.getOpLevel(),
                                group.getGroupId(), opTypeSite.getTransformer().toDbString(
                                        new GroupSiteOptVo(groupInfoBefore)), opTypeSite.getTransformer().toDbString(
                                        new GroupSiteOptVo(groupInfo)));
                if (!contentSite.getPreContent().equals(contentSite.getPostContent())) {
                    optContents.add(contentSite);
                }

                SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); // 加入session中，后续有拦截器处理
            } catch (Exception e) {
                log.error("failed to contruct opt history content. " + e.getMessage(), e);
            }
            // ------- 保存历史操作记录 end ------

            pay.setSuccess(1);

            StringBuilder sb =
                    new StringBuilder("### save SiteConfig, isAllSite is ").append(siteConfig.isAllSite())
                            .append(", groupId=").append(groupId).append(", userId=").append(group.getUserId());

            sb.append(", SiteList is [");
            if (siteConfig.getSiteList() != null) {
                for (String siteStr : siteConfig.getSiteList()) {
                    sb.append(siteStr).append(",");
                }
            }
            sb.append("]");

            sb.append(", TradeList is [");
            if (siteConfig.getCategoryList() != null) {
                for (int tradeId : siteConfig.getCategoryList()) {
                    sb.append(tradeId).append(",");
                }
            }
            sb.append("]");
            log.info(sb);
        }

        ApiResultBeanUtils.setSuccessObject(result);
        return result;
    }

    public ApiResult<Object> setExcludeSite(DataPrivilege user, SetExcludeSiteRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        ExcludeSiteType siteFilter = request.getExcludeSite();
        if (siteFilter == null || siteFilter.getGroupId() <= 0 || siteFilter.getExcludeSite() == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_SITE_REQ);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // ---------------------------设置数据---------------------------------//
        Integer groupId = Integer.valueOf(String.valueOf(siteFilter.getGroupId()));

        String[] sites = siteFilter.getExcludeSite();
        Set<String> allSite = new HashSet<String>();

        List<String> siteList = Arrays.asList(sites);
        result = groupConfigValidator.validateSiteFilter(groupId, siteList, allSite, result);

        // 验证是否通过
        if (!CollectionUtils.isEmpty(result.getErrors())) {
            return result;
        }

        // List<String> toAddSite = GroupConfigValidator.validateSiteFilter(
        // groupId, support, siteFilterList, siteList, textProvider);
        // delGroupSiteFilter(id, visitor);

        if (allSite.size() >= 0) {
            result = this.groupConfigSetService.saveSiteFilter(result, groupId, allSite);

            StringBuilder sb =
                    new StringBuilder("### save ExcludeSite, size is ").append(allSite.size()).append(", groupId=")
                            .append(groupId);
            log.info(sb);
        }

        return result;
    }

    public ApiResult<Object> setTradeSitePrice(DataPrivilege user, SetTradeSitePriceRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        PaymentResult pay = new PaymentResult();
        result.setPayment(pay);
        pay.setTotal(1);

        result.setPayment(pay);

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        TradeSitePriceType tradeSitePrice = request.getTradeSitePrice();
        if (tradeSitePrice == null || tradeSitePrice.getGroupId() <= 0) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        if (tradeSitePrice.getTradePriceList() == null && tradeSitePrice.getSitePriceList() == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.TRADE_SITE_PRICE_EMPTY.getValue(),
                            GroupConfigErrorCode.TRADE_SITE_PRICE_EMPTY.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // ---------------------------设置数据---------------------------------//
        Integer groupId = Integer.valueOf(String.valueOf(tradeSitePrice.getGroupId()));
        CproGroup group = cproGroupMgr.findWithInfoById(groupId);

        if (group == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
            apiPosition.addParam(GroupConstant.GROUPID);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupErrorCode.NOT_FOUND.getValue(),
                            GroupErrorCode.NOT_FOUND.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        boolean tradePriceModifiedFlag = true;
        if (tradeSitePrice.getTradePriceList() == null) {
            tradePriceModifiedFlag = false;
        }

        boolean sitePriceModifiedFlag = true;
        if (tradeSitePrice.getSitePriceList() == null) {
            sitePriceModifiedFlag = false;
        }

        // 验证分网站出价
        List<GroupSitePrice> toBeModifiedSitePriceList = new ArrayList<GroupSitePrice>();
        groupConfigValidator.validateSitePrice(group, tradeSitePrice.getSitePriceList(), toBeModifiedSitePriceList,
                result);

        // 验证分网站出价不通过
        if (!CollectionUtils.isEmpty(result.getErrors())) {
            return result;
        }

        // 验证分行业出价
        List<GroupTradePrice> toBeModifiedTradePriceList = new ArrayList<GroupTradePrice>();
        groupConfigValidator.validateTradePrice(group, tradeSitePrice.getTradePriceList(), toBeModifiedTradePriceList,
                result);

        // 验证分行业出价不通过
        if (!CollectionUtils.isEmpty(result.getErrors())) {
            return result;
        }

        // 先查询出修改前的以备历史操作记录
        List<GroupTradePrice> toBeRemovedTradePriceList = groupSiteConfigMgr.findAllTradePrice(null, null, groupId);
        List<GroupSitePrice> toBeRemovedSitePriceList = groupSiteConfigMgr.findAllSitePrice(null, null, groupId);

        // 设置分行业出价
        boolean isTradeSucc =
                this.groupConfigSetService.saveTradePrice(groupId, tradePriceModifiedFlag, toBeModifiedTradePriceList,
                        groupSiteConfigMgr);

        // 设置分网站出价
        boolean isSiteSucc =
                this.groupConfigSetService.saveSitePrice(groupId, sitePriceModifiedFlag, toBeModifiedSitePriceList,
                        groupSiteConfigMgr);

        if (isTradeSucc && isSiteSucc) {
            // ------- 保存历史操作记录 start ------
            try {
                List<OptContent> optContents = new ArrayList<OptContent>();

                OpTypeVo opTypeTradeDel = OptHistoryConstant.OPTYPE_GROUP_TRADE_PRICE_BATCH_DELETE;
                OpTypeVo opTypeTradeAdd = OptHistoryConstant.OPTYPE_GROUP_TRADE_PRICE_BATCH_ADD;
                OpTypeVo opTypeSiteDel = OptHistoryConstant.OPTYPE_GROUP_SITE_PRICE_BATCH_DELETE;
                OpTypeVo opTypeSiteAdd = OptHistoryConstant.OPTYPE_GROUP_SITE_PRICE_BATCH_ADD;

                if (tradePriceModifiedFlag) {
                    // 记录删除前的分行业出价
                    for (GroupTradePrice groupTradePrice : toBeRemovedTradePriceList) {
                        GroupTradeSitePriceOptVo before = new GroupTradeSitePriceOptVo();
                        before.setId(groupTradePrice.getTradeid());
                        before.setPrice(groupTradePrice.getPrice());
                        OptContent content =
                                new OptContent(group.getUserId(), opTypeTradeDel.getOpType(),
                                        opTypeTradeDel.getOpLevel(), groupId, opTypeTradeDel.getTransformer()
                                                .toDbString(before), null);
                        optContents.add(content);
                    }

                    // 记录保存后的分行业出价
                    for (GroupTradePrice groupTradePrice : toBeModifiedTradePriceList) {
                        GroupTradeSitePriceOptVo after = new GroupTradeSitePriceOptVo();
                        after.setId(groupTradePrice.getTradeid());
                        after.setPrice(groupTradePrice.getPrice());
                        OptContent content =
                                new OptContent(group.getUserId(), opTypeTradeAdd.getOpType(),
                                        opTypeTradeAdd.getOpLevel(), groupId, null, opTypeTradeAdd.getTransformer()
                                                .toDbString(after));
                        optContents.add(content);
                    }
                }

                if (sitePriceModifiedFlag) {
                    // 记录删除前的分网站出价
                    for (GroupSitePrice groupSitePrice : toBeRemovedSitePriceList) {
                        GroupTradeSitePriceOptVo before = new GroupTradeSitePriceOptVo();
                        before.setId(groupSitePrice.getSiteid());
                        before.setPrice(groupSitePrice.getPrice());
                        OptContent content =
                                new OptContent(group.getUserId(), opTypeSiteDel.getOpType(),
                                        opTypeSiteDel.getOpLevel(), groupId, opTypeSiteDel.getTransformer().toDbString(
                                                before), null);
                        optContents.add(content);
                    }

                    // 记录保存后的分网站出价
                    for (GroupSitePrice groupSitePrice : toBeModifiedSitePriceList) {
                        GroupTradeSitePriceOptVo after = new GroupTradeSitePriceOptVo();
                        after.setId(groupSitePrice.getSiteid());
                        after.setPrice(groupSitePrice.getPrice());
                        OptContent content =
                                new OptContent(group.getUserId(), opTypeSiteAdd.getOpType(),
                                        opTypeSiteAdd.getOpLevel(), groupId, null, opTypeSiteAdd.getTransformer()
                                                .toDbString(after));
                        optContents.add(content);
                    }
                }

                SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); // 加入session中，后续有拦截器处理
            } catch (Exception e) {
                log.error("failed to contruct opt history content. " + e.getMessage(), e);
            }
            // ------- 保存历史操作记录 end ------

            pay.setSuccess(1);
            ApiResultBeanUtils.setSuccessObject(result);
        } else {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SET_TRADE_SITE_PRICE_FAILED.getValue(),
                            GroupConfigErrorCode.SET_TRADE_SITE_PRICE_FAILED.getMessage(), apiPosition.getPosition(),
                            null);
        }

        return result;
    }

    public ApiResult<Object> setSiteUrl(DataPrivilege user, SetSiteUrlRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();
        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);

        result.setPayment(pay);

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        SiteUrlType siteUrl = request.getSiteUrl();
        if (siteUrl == null || siteUrl.getSiteUrlList() == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITE_URL_REQ);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // ---------------------------设置数据---------------------------------//
        Integer groupId = Integer.valueOf(String.valueOf(siteUrl.getGroupId()));

        CproGroup group = cproGroupMgr.findWithInfoById(groupId);

        if (group == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITE_URL_REQ);
            apiPosition.addParam(GroupConstant.GROUPID);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupErrorCode.NOT_FOUND.getValue(),
                            GroupErrorCode.NOT_FOUND.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        List<String> userDomains = unitMgr.getUserDomain(group.getUserId());
        List<GroupSitePrice> allsp = new ArrayList<GroupSitePrice>();
        result =
                groupConfigValidator.validateSiteTargetUrl(group, siteUrl.getSiteUrlList(), allsp, result, userDomains);

        if (result.hasErrors()) {
            // 验证出错，直接返回
            return result;
        }

        result = this.groupConfigSetService.saveSiteTargetUrl(result, groupId, allsp, groupSiteConfigMgr);

        ApiResultBeanUtils.setSuccessObject(result);
        return result;
    }

    public ApiResult<Object> setKeyword(DataPrivilege user, SetKeywordRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();
        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getKeywords())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.KEYWORDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupKeywordItemType[] keywords = request.getKeywords();
        // 验证关键词的个数
        if (keywords.length > GroupConstant.KEYWORDS_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.KEYWORDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SET_MAX_KEYWORDS_ERROR.getValue(),
                            GroupConfigErrorCode.SET_MAX_KEYWORDS_ERROR.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        result = groupConfigSetService.setKeyword(result, keywords);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> setInterestInfo(DataPrivilege user, SetInterestInfoRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getInterestInfo() == null || request.getInterestInfo().getGroupId() <= 0) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.INTEREST_INFO);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        InterestInfoType interestInfo = request.getInterestInfo();

        // ---------------------------设置数据---------------------------------//
        Integer groupId = Integer.valueOf(String.valueOf(interestInfo.getGroupId()));

        CproGroup group = cproGroupMgr.findWithInfoById(groupId);

        if (group == null) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.INTEREST_INFO);
            apiPosition.addParam(GroupConstant.GROUPID);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupErrorCode.NOT_FOUND.getValue(),
                            GroupErrorCode.NOT_FOUND.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        if (TargettypeUtil.isPack(group.getTargetType())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TARGET_INFO);
            apiPosition.addParam(GroupConstant.TARGETTYPE);

            result =
                    ApiResultBeanUtils.addApiError(result,
                            GroupConfigErrorCode.INTEREST_ENABLED_CANNOT_CHANGE_IF_SET_TO_PACK.getValue(),
                            GroupConfigErrorCode.INTEREST_ENABLED_CANNOT_CHANGE_IF_SET_TO_PACK.getMessage(),
                            apiPosition.getPosition(), null);
            return result;
        }

        boolean isSucc = false;

        // 历史操作记录保存对象
        List<OptContent> optContents = new ArrayList<OptContent>();

        try {
            isSucc =
                    groupConfigSetService.saveIT(result, group, interestInfo, cproITFacade, cproGroupITMgr,
                            interestMgr, customITMgr, user.getDataUser(), user.getOpUser(), optContents);
        } catch (Exception e) {
            StringBuffer sb =
                    new StringBuffer("Change interestInfo ").append(" userId=").append(group.getUserId())
                            .append(", groupId=").append(group.getGroupId()).append(" failed! ").append(e.getMessage());
            log.error(sb.toString(), e);

            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.INTEREST_INFO);
            result =
                    ApiResultBeanUtils
                            .addApiError(result, GroupConfigErrorCode.SET_GROUP_INTEREST_ERROR.getValue(),
                                    GroupConfigErrorCode.SET_GROUP_INTEREST_ERROR.getMessage(),
                                    apiPosition.getPosition(), null);
        }

        // 如果成功才记录历史操作记录，并incr成功值
        if (isSucc) {
            StringBuffer sb =
                    new StringBuffer("Change interestInfo from ").append(" userId=").append(group.getUserId())
                            .append(", groupId=").append(group.getGroupId()).append(" successfully!");
            log.info(sb);
            SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); // 加入session中，后续有拦截器处理
            pay.increSuccess();
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> addKeyword(DataPrivilege user, AddKeywordRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();
        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getKeywords())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.KEYWORDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupKeywordItemType[] keywords = request.getKeywords();
        // 验证关键词的个数
        if (keywords.length > GroupConstant.KEYWORDS_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.KEYWORDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.ADD_MAX_KEYWORDS_ERROR.getValue(),
                            GroupConfigErrorCode.ADD_MAX_KEYWORDS_ERROR.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        result = groupConfigADService.addKeyword(result, keywords);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> addRtRelation(DataPrivilege user, AddRtRelationRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        /*
         * RT(RT2) has been deprecated since cpweb661 in 2013/10/28
         */
        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
        apiPosition.addParam(GroupConstant.RT_RELATIONS);

        result =
                ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.RT_DEPREATED_ERROR.getValue(),
                        GroupConfigErrorCode.RT_DEPREATED_ERROR.getMessage(), apiPosition.getPosition(), null);

        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        return result;
    }

    public ApiResult<Object> addVtPeople(DataPrivilege user, AddVtPeopleRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getVtPeoples())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.VT_PEOPLES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupVtItemType[] vtPeoples = request.getVtPeoples();
        // 验证ct词的个数
        if (vtPeoples.length > GroupConstant.VT_ADD_MAX_PEOPLES_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.VT_PEOPLES);

            result =
                    ApiResultBeanUtils
                            .addApiError(result, GroupConfigErrorCode.VT_ADD_MAX_PEOPLES_ERROR.getValue(),
                                    GroupConfigErrorCode.VT_ADD_MAX_PEOPLES_ERROR.getMessage(),
                                    apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证vt人群是否有效
        for (int index = 0; index < vtPeoples.length; index++) {
            GroupVtItemType item = vtPeoples[index];
            if (item == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.VT_PEOPLES, index);

                result =
                        ApiResultBeanUtils
                                .addApiError(result, GroupConfigErrorCode.VT_PEOPLE_NULL_ERROR.getValue(),
                                        GroupConfigErrorCode.VT_PEOPLE_NULL_ERROR.getMessage(),
                                        apiPosition.getPosition(), null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(vtPeoples.length);
        result.setPayment(pay);

        result = groupConfigADService.addVtPeople(result, vtPeoples);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> addSite(DataPrivilege user, AddSiteRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getSites())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupSiteType[] sites = request.getSites();
        // 验证投放网站的个数
        if (sites.length > GroupConstant.SITE_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITES);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SITE_ADD_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.SITE_ADD_MAX_ERROR.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证投放网站是否有效
        for (int index = 0; index < sites.length; index++) {
            GroupSiteType item = sites[index];
            if (item == null || item.getSite() == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.SITES, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SITE_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.SITE_NULL_ERROR.getMessage(), apiPosition.getPosition(), null);
            }
        }

        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(sites.length);
        result.setPayment(pay);

        result = groupConfigADService.addSite(result, sites);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> addTrade(DataPrivilege user, AddTradeRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getTrades())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupTradeType[] trades = request.getTrades();
        // 验证投放行业的个数
        if (trades.length > GroupConstant.TRADE_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADES);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.TRADE_ADD_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.TRADE_ADD_MAX_ERROR.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证投放行业是否有效
        for (int index = 0; index < trades.length; index++) {
            GroupTradeType item = trades[index];
            if (item == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.TRADES, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.TRADE_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.TRADE_NULL_ERROR.getMessage(), apiPosition.getPosition(), null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(trades.length);
        result.setPayment(pay);

        result = groupConfigADService.addTrade(result, trades);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }
    
    
    /**
     * 按照推广组ID分组地域设置
     * 
     * @param regions 地域设置
     * @return 按照推广组ID分组的地域设置
     */
    private HashMultimap<Long, GroupRegionItem> regionGroupByGroup(GroupRegionType[] regions) {
        HashMultimap<Long, GroupRegionItem> regionMap = HashMultimap.create();
        for (int i = 0; i < regions.length; i++) {
            regionMap.put(regions[i].getGroupId(),
                    new GroupRegionItem(i, regions[i].getType(), regions[i].getRegionId()));
        }
        return regionMap;
    }

    public ApiResult<Object> addRegion(DataPrivilege user, AddRegionRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getRegions())) {
            return GlobalErrorCode.UNEXPECTED_PARAMETER.getErrorResponse(result,
                    Lists.newArrayList(new ErrorParam(GroupConstant.REGIONS, null)), null);
        }

        GroupRegionType[] regions = request.getRegions();
        
        // 验证投放地域的个数
        if (regions.length > GroupConstant.REGION_ADD_MAX_NUM) {
            return GroupConfigErrorCode.REGION_ADD_MAX_ERROR
                    .getErrorResponse(result, Lists.newArrayList(new ErrorParam(GroupConstant.REGIONS, null)), null);
        }

        for (int i = 0; i < regions.length; i++) {
            GroupRegionType item = regions[i];
            if (item == null) {
                result =
                        GroupConfigErrorCode.REGION_NULL_ERROR.getErrorResponse(result,
                                Lists.newArrayList(new ErrorParam(GroupConstant.REGIONS, i)), null);
            }
        }

        if (result.hasErrors()) {
            return result;
        }
        
        // 如果调用方还未升级，当前使用的是北斗地域词表，需先转换成sys地域词表
        double version = 0;
        if (request.getVersion() != null) {
            version = Double.parseDouble(request.getVersion());
        }
        if (version < ApiVersionConstant.SYS_REG_VERSION) {
            regions = RegionConvert.bdToSys(regions);
        }

        HashMultimap<Long, GroupRegionItem> regionMap = this.regionGroupByGroup(regions);
        if (regionMap.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
            return GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getErrorResponse(result,
                    Lists.newArrayList(new ErrorParam(GroupConstant.REGIONS, null)), null);
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(regions.length);
        result.setPayment(pay);

        result = groupConfigADService.addRegion(result, regionMap);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> addExcludeIp(DataPrivilege user, AddExcludeIpRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getExcludeIps())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_IPS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupExcludeIpType[] excludeIps = request.getExcludeIps();
        // 验证过滤IP的个数
        if (excludeIps.length > GroupConstant.EXCLUDE_IP_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_IPS);

            result =
                    ApiResultBeanUtils
                            .addApiError(result, GroupConfigErrorCode.EXCLUDE_IP_ADD_MAX_ERROR.getValue(),
                                    GroupConfigErrorCode.EXCLUDE_IP_ADD_MAX_ERROR.getMessage(),
                                    apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证过滤IP是否有效
        for (int index = 0; index < excludeIps.length; index++) {
            GroupExcludeIpType item = excludeIps[index];
            if (item == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.EXCLUDE_IPS, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_IP_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.EXCLUDE_IP_NULL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(excludeIps.length);
        result.setPayment(pay);

        result = groupConfigADService.addExcludeIp(result, excludeIps);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> addExcludeSite(DataPrivilege user, AddExcludeSiteRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getExcludeSites())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_SITES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupExcludeSiteType[] excludeSites = request.getExcludeSites();
        // 验证过滤网站的个数
        if (excludeSites.length > GroupConstant.EXCLUDE_SITE_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_SITES);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_SITE_ADD_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.EXCLUDE_SITE_ADD_MAX_ERROR.getMessage(), apiPosition.getPosition(),
                            null);
            return result;
        }

        // 简单验证过滤网站是否有效
        for (int index = 0; index < excludeSites.length; index++) {
            GroupExcludeSiteType item = excludeSites[index];
            if (item == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.EXCLUDE_SITES, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_SITE_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.EXCLUDE_SITE_NULL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(excludeSites.length);
        result.setPayment(pay);

        result = groupConfigADService.addExcludeSite(result, excludeSites);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    /**
     * @author kanghongwei
     * @since 2012-6-13
     */
    public ApiResult<Object> addTradePrice(DataPrivilege user, AddTradePriceRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();
        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getTradePrices())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADE_PRICES);
            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupTradePriceType[] tradePrices = request.getTradePrices();

        // 验证批量添加“分行业出价的个数”
        if (tradePrices.length > GroupConstant.TRADE_PRICE_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADE_PRICES);
            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.TRADE_PRICE_ADD_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.TRADE_PRICE_ADD_MAX_ERROR.getMessage(), apiPosition.getPosition(),
                            null);
            return result;
        }

        // 验证参数有效性
        for (int index = 0; index < tradePrices.length; index++) {
            GroupTradePriceType item = tradePrices[index];
            if (item == null || (item.getGroupId() <= 0) || (item.getTrade() <= 0) || (item.getPrice() <= 0)) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.TRADE_PRICES, index);
                result =
                        ApiResultBeanUtils.addApiError(result,
                                GroupConfigErrorCode.TRADE_PRICE_ILLGAL_ERROR.getValue(),
                                GroupConfigErrorCode.TRADE_PRICE_ILLGAL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
                continue;
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        // “基础验证”通过，则修改处理总个数
        pay.setTotal(tradePrices.length);

        result = groupConfigADService.addTradePrice(result, tradePrices);
        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }
        return result;
    }

    public ApiResult<Object> addSitePrice(DataPrivilege user, AddSitePriceRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getSitePrices())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITE_PRICES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupSitePriceType[] sitePrices = request.getSitePrices();
        // 验证分网站出价的个数
        if (sitePrices.length > GroupConstant.SITE_PRICE_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITE_PRICES);

            result =
                    ApiResultBeanUtils
                            .addApiError(result, GroupConfigErrorCode.SITE_PRICE_ADD_MAX_ERROR.getValue(),
                                    GroupConfigErrorCode.SITE_PRICE_ADD_MAX_ERROR.getMessage(),
                                    apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证分网站出价是否有效
        for (int index = 0; index < sitePrices.length; index++) {
            GroupSitePriceType item = sitePrices[index];
            if (item == null || item.getSite() == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.SITE_PRICES, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SITE_PRICE_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.SITE_PRICE_NULL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(sitePrices.length);
        result.setPayment(pay);

        result = groupConfigADService.addSitePrice(result, sitePrices);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> addInterestInfo(DataPrivilege user, AddInterestInfoRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();
        PaymentResult pay = new PaymentResult();
        result.setPayment(pay);

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getInterests())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.INTERESTS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            pay.setTotal(1);
            return result;
        }

        GroupInterestInfoType[] interestInfos = request.getInterests();
        // 验证推广组的个数
        if (interestInfos.length > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.INTERESTS);

            result =
                    ApiResultBeanUtils.addApiError(result,
                            GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
                            GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(),
                            null);
            pay.setTotal(1);
            return result;
        }

        // 验证兴趣点的个数
        int totalInterestNum = 0;
        for (GroupInterestInfoType groupInterestInfoType : interestInfos) {
            if (groupInterestInfoType != null) {
                if (groupInterestInfoType.getInterestIds() != null) {
                    totalInterestNum += groupInterestInfoType.getInterestIds().length;
                }
                if (groupInterestInfoType.getExceptInterestIds() != null) {
                    totalInterestNum += groupInterestInfoType.getExceptInterestIds().length;
                }
            }
        }
        if (totalInterestNum > GroupConstant.IT_ADD_MAX_RELATIONS_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.INTERESTS);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.IT_ADD_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.IT_ADD_MAX_ERROR.getMessage(), apiPosition.getPosition(), null);
            pay.setTotal(1);
            return result;
        }

        result = groupConfigADService.addInterestInfo(result, interestInfos);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> deleteKeyword(DataPrivilege user, DeleteKeywordRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();
        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getKeywords())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.KEYWORDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupKeywordItemType[] keywords = request.getKeywords();
        // 验证关键词的个数
        if (keywords.length > GroupConstant.KEYWORDS_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.KEYWORDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.DEL_MAX_KEYWORDS_ERROR.getValue(),
                            GroupConfigErrorCode.DEL_MAX_KEYWORDS_ERROR.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        result = groupConfigADService.deleteKeyword(result, keywords);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> deleteRtRelation(DataPrivilege user, DeleteRtRelationRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        /*
         * RT(RT2) has been deprecated since cpweb661 in 2013/10/28
         */
        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
        apiPosition.addParam(GroupConstant.RT_RELATIONS);

        result =
                ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.RT_DEPREATED_ERROR.getValue(),
                        GroupConfigErrorCode.RT_DEPREATED_ERROR.getMessage(), apiPosition.getPosition(), null);

        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        return result;
    }

    public ApiResult<Object> deleteVtPeople(DataPrivilege user, DeleteVtPeopleRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getVtPeoples())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.VT_PEOPLES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupVtItemType[] vtPeoples = request.getVtPeoples();
        // 验证ct词的个数
        if (vtPeoples.length > GroupConstant.VT_DEL_MAX_PEOPLES_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.VT_PEOPLES);

            result =
                    ApiResultBeanUtils
                            .addApiError(result, GroupConfigErrorCode.VT_DEL_MAX_PEOPLES_ERROR.getValue(),
                                    GroupConfigErrorCode.VT_DEL_MAX_PEOPLES_ERROR.getMessage(),
                                    apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证vt人群是否有效
        for (int index = 0; index < vtPeoples.length; index++) {
            GroupVtItemType item = vtPeoples[index];
            if (item == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.VT_PEOPLES, index);

                result =
                        ApiResultBeanUtils
                                .addApiError(result, GroupConfigErrorCode.VT_PEOPLE_NULL_ERROR.getValue(),
                                        GroupConfigErrorCode.VT_PEOPLE_NULL_ERROR.getMessage(),
                                        apiPosition.getPosition(), null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(vtPeoples.length);
        result.setPayment(pay);

        result = groupConfigADService.deleteVtPeople(result, vtPeoples);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> deleteSite(DataPrivilege user, DeleteSiteRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getSites())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupSiteType[] sites = request.getSites();
        // 验证投放网站的个数
        if (sites.length > GroupConstant.SITE_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITES);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SITE_DEL_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.SITE_DEL_MAX_ERROR.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证投放网站是否有效
        for (int index = 0; index < sites.length; index++) {
            GroupSiteType item = sites[index];
            if (item == null || item.getSite() == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.SITES, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SITE_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.SITE_NULL_ERROR.getMessage(), apiPosition.getPosition(), null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(sites.length);
        result.setPayment(pay);

        result = groupConfigADService.deleteSite(result, sites);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> deleteTrade(DataPrivilege user, DeleteTradeRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getTrades())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupTradeType[] trades = request.getTrades();
        // 验证投放行业的个数
        if (trades.length > GroupConstant.TRADE_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADES);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.TRADE_DEL_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.TRADE_DEL_MAX_ERROR.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证投放行业是否有效
        for (int index = 0; index < trades.length; index++) {
            GroupTradeType item = trades[index];
            if (item == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.TRADES, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.TRADE_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.TRADE_NULL_ERROR.getMessage(), apiPosition.getPosition(), null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(trades.length);
        result.setPayment(pay);

        result = groupConfigADService.deleteTrade(result, trades);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> deleteRegion(DataPrivilege user, DeleteRegionRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getRegions())) {
            return GlobalErrorCode.UNEXPECTED_PARAMETER.getErrorResponse(result,
                    Lists.newArrayList(new ErrorParam(GroupConstant.REGIONS, null)), null);
        }

        GroupRegionType[] regions = request.getRegions();
        
        // 验证投放行业的个数
        if (regions.length > GroupConstant.REGION_DEL_MAX_NUM) {
            
            
            return GroupConfigErrorCode.REGION_DEL_MAX_ERROR
                    .getErrorResponse(result, Lists.newArrayList(new ErrorParam(GroupConstant.REGIONS, null)), null);
        }

        // 简单验证投放行业是否有效
        for (int index = 0; index < regions.length; index++) {
            GroupRegionType item = regions[index];
            if (item == null) {
                GroupConfigErrorCode.REGION_NULL_ERROR.getErrorResponse(result,
                        Lists.newArrayList(new ErrorParam(GroupConstant.REGIONS, index)), null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }
        
        // 如果调用方还未升级，当前使用的是北斗地域词表，需先转换成sys地域词表
        double version = 0;
        if (request.getVersion() != null) {
            version = Double.parseDouble(request.getVersion());
        }
        if (version < ApiVersionConstant.SYS_REG_VERSION) {
            regions = RegionConvert.bdToSys(regions);
        }

        HashMultimap<Long, GroupRegionItem> regionMap = this.regionGroupByGroup(regions);
        if (regionMap.keySet().size() > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
            return GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getErrorResponse(result,
                    Lists.newArrayList(new ErrorParam(GroupConstant.REGIONS, null)), null);
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(regions.length);
        result.setPayment(pay);

        result = groupConfigADService.deleteRegion(result, regionMap);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> deleteExcludeIp(DataPrivilege user, DeleteExcludeIpRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getExcludeIps())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_IPS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupExcludeIpType[] excludeIps = request.getExcludeIps();
        // 验证过滤IP的个数
        if (excludeIps.length > GroupConstant.EXCLUDE_IP_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_IPS);

            result =
                    ApiResultBeanUtils
                            .addApiError(result, GroupConfigErrorCode.EXCLUDE_IP_DEL_MAX_ERROR.getValue(),
                                    GroupConfigErrorCode.EXCLUDE_IP_DEL_MAX_ERROR.getMessage(),
                                    apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证过滤IP是否有效
        for (int index = 0; index < excludeIps.length; index++) {
            GroupExcludeIpType item = excludeIps[index];
            if (item == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.EXCLUDE_IPS, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_IP_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.EXCLUDE_IP_NULL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(excludeIps.length);
        result.setPayment(pay);

        result = groupConfigADService.deleteExcludeIp(result, excludeIps);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> deleteExcludeSite(DataPrivilege user, DeleteExcludeSiteRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getExcludeSites())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_SITES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupExcludeSiteType[] excludeSites = request.getExcludeSites();
        // 验证过滤网站的个数
        if (excludeSites.length > GroupConstant.EXCLUDE_SITE_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_SITES);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_SITE_DEL_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.EXCLUDE_SITE_DEL_MAX_ERROR.getMessage(), apiPosition.getPosition(),
                            null);
            return result;
        }

        // 简单验证过滤网站是否有效
        for (int index = 0; index < excludeSites.length; index++) {
            GroupExcludeSiteType item = excludeSites[index];
            if (item == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.EXCLUDE_SITES, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_SITE_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.EXCLUDE_SITE_NULL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(excludeSites.length);
        result.setPayment(pay);

        result = groupConfigADService.deleteExcludeSite(result, excludeSites);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    /**
     * @author kanghongwei
     * @since 2012-6-13
     */
    public ApiResult<Object> deleteTradePrice(DataPrivilege user, DeleteTradePriceRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();
        PaymentResult pay = new PaymentResult();
        pay.setTotal(1);
        result.setPayment(pay);

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getTradePrices())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADE_PRICES);
            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }
        GroupTradeType[] tradePrices = request.getTradePrices();

        // 验证批量删除“分行业出价的个数”
        if (tradePrices.length > GroupConstant.TRADE_PRICE_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.TRADE_PRICES);
            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.TRADE_PRICE_DEL_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.TRADE_PRICE_DEL_MAX_ERROR.getMessage(), apiPosition.getPosition(),
                            null);
            return result;
        }

        // 验证参数有效性
        for (int index = 0; index < tradePrices.length; index++) {
            GroupTradeType item = tradePrices[index];
            if (item == null || (item.getGroupId() <= 0) || (item.getTrade() <= 0)) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.TRADE_PRICES, index);
                result =
                        ApiResultBeanUtils.addApiError(result,
                                GroupConfigErrorCode.TRADE_PRICE_ILLGAL_ERROR.getValue(),
                                GroupConfigErrorCode.TRADE_PRICE_ILLGAL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
                continue;
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        // “基础验证”通过，则修改处理总个数
        pay.setTotal(tradePrices.length);

        result = groupConfigADService.deleteTradePrice(result, tradePrices);
        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }
        return result;
    }

    /**
     * 删除分网站出价
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param request
     * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return SuccessObject
     */
    public ApiResult<Object> deleteSitePrice(DataPrivilege user, DeleteSitePriceRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getSitePrices())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITE_PRICES);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        GroupSitePriceType[] sitePrices = request.getSitePrices();
        // 验证分网站出价的个数
        if (sitePrices.length > GroupConstant.SITE_PRICE_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.SITE_PRICES);

            result =
                    ApiResultBeanUtils
                            .addApiError(result, GroupConfigErrorCode.SITE_PRICE_DEL_MAX_ERROR.getValue(),
                                    GroupConfigErrorCode.SITE_PRICE_DEL_MAX_ERROR.getMessage(),
                                    apiPosition.getPosition(), null);
            return result;
        }

        // 简单验证分网站出价是否有效
        for (int index = 0; index < sitePrices.length; index++) {
            GroupSitePriceType item = sitePrices[index];
            if (item == null || item.getSite() == null) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.SITE_PRICES, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.SITE_PRICE_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.SITE_PRICE_NULL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(sitePrices.length);
        result.setPayment(pay);

        result = groupConfigADService.deleteSitePrice(result, sitePrices);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    public ApiResult<Object> deleteInterestInfo(DataPrivilege user, DeleteInterestInfoRequest request,
            ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();
        PaymentResult pay = new PaymentResult();
        result.setPayment(pay);

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || ArrayUtils.isEmpty(request.getInterests())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.INTERESTS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            pay.setTotal(1);
            return result;
        }

        GroupInterestInfoType[] interestInfos = request.getInterests();
        // 验证推广组的个数
        if (interestInfos.length > GroupConstant.ADD_DEL_MAX_GROUPS_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.INTERESTS);

            result =
                    ApiResultBeanUtils.addApiError(result,
                            GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getValue(),
                            GroupConfigErrorCode.ADD_DEL_SET_MAX_GROUPS_ERROR.getMessage(), apiPosition.getPosition(),
                            null);
            pay.setTotal(1);
            return result;
        }

        // 验证兴趣点的个数
        int totalInterestNum = 0;
        for (GroupInterestInfoType groupInterestInfoType : interestInfos) {
            if (groupInterestInfoType != null) {
                if (groupInterestInfoType.getInterestIds() != null) {
                    totalInterestNum += groupInterestInfoType.getInterestIds().length;
                }
                if (groupInterestInfoType.getExceptInterestIds() != null) {
                    totalInterestNum += groupInterestInfoType.getExceptInterestIds().length;
                }
            }
        }
        if (totalInterestNum > GroupConstant.IT_DEL_MAX_RELATIONS_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.INTERESTS);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.IT_DEL_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.IT_DEL_MAX_ERROR.getMessage(), apiPosition.getPosition(), null);
            pay.setTotal(1);
            return result;
        }

        result = groupConfigADService.deleteInterestInfo(result, interestInfos);

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    /**
     * 设置推广组排除关键词
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "setExcludeKeyword", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> setExcludeKeyword(BaseRequestUser user, SetExcludeKeywordRequest param,
            BaseRequestOptions options) {

        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        ExcludeKeywordType inputParam = param.getExcludeKeyword();
        int groupId = new Long(inputParam.getGroupId()).intValue();
        List<KeywordType> excludeKeywords = inputParam.getExcludeKeywords();
        List<Integer> excludeKeywordPackIds = inputParam.getExcludeKeywordPackIds();
        if (inputParam.getGroupId() < 1l) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
            apiPosition.addParam(GroupConstant.GROUPID);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }
        if (CollectionUtils.isEmpty(excludeKeywords) && CollectionUtils.isEmpty(excludeKeywordPackIds)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 先验证排除关键词和组合数量是否超过限制
        if (!CollectionUtils.isEmpty(excludeKeywords)
                && excludeKeywords.size() > CproGroupConstant.GROUP_EXCLUDE_KEYWORD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response,
                            GroupConfigErrorCode.EXCLUDE_KEYWORDS_EXCEED_LIMIT.getValue(),
                            GroupConfigErrorCode.EXCLUDE_KEYWORDS_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(),
                            null);
            return response;
        }
        if (!CollectionUtils.isEmpty(excludeKeywordPackIds)
                && excludeKeywordPackIds.size() > CproGroupConstant.GROUP_EXCLUDE_WORD_PACK_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORD_PACKIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response,
                            GroupConfigErrorCode.EXCLUDE_WORD_PACK_COUNT_EXCEED.getValue(),
                            GroupConfigErrorCode.EXCLUDE_WORD_PACK_COUNT_EXCEED.getMessage(),
                            apiPosition.getPosition(), null);
            return response;
        }

        // 保存排除设置
        groupConfigSetService.saveExcludeKeyword(response, groupId, excludeKeywords, excludeKeywordPackIds,
                wordExcludeFacade, userId, opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.getOptions().setSuccess(1);
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 查询推广组排除关键词
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "getExcludeKeyword", returnType = ReturnType.ARRAY)
    public BaseResponse<ExcludeKeywordType> getExcludeKeyword(BaseRequestUser user, GetExcludeKeywordRequest param,
            BaseRequestOptions options) {

        BaseResponse<ExcludeKeywordType> response = new BaseResponse<ExcludeKeywordType>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的推广组id列表不为空
        long[] groupids = param.getGroupIds();
        if (ArrayUtils.isEmpty(groupids)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 验证传入参数长度
        if (groupids.length > getExcludeKeywordMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getValue(),
                            GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 配额总数设为查询数量
        option.setTotal(groupids.length);

        List<Integer> groupIds = CollectionsUtil.tranformLongArrayToIntList(groupids);
        List<WordExclude> wordExcludes = wordExcludeFacade.getWordExcludeList(userId, groupIds);
        List<WordPackExclude> wordPackExcludes = wordExcludeFacade.getWordPackExcludeList(userId, groupIds);
        ExcludeKeywordType[] result =
                GroupBoMappingUtil.transWordExclude2ExcludeKeywordType(wordExcludes, wordPackExcludes);

        response.getOptions().setSuccess(groupids.length);
        response.setData(result);

        return response;
    }

    /**
     * 添加推广组排除关键词
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "addExcludeKeyword", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> addExcludeKeyword(BaseRequestUser user, AddExcludeKeywordRequest param,
            BaseRequestOptions options) {

        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        List<GroupExcludeKeywordType> inputParam = param.getExcludeKeywords();
        if (CollectionUtils.isEmpty(inputParam)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        if (!CollectionUtils.isEmpty(inputParam)
                && inputParam.size() > GroupConstant.EXCLUDE_KEYWORD_OR_PACK_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response,
                            GroupConfigErrorCode.EXCLUDE_WORD_AND_PACK_TOO_MANY.getValue(),
                            GroupConfigErrorCode.EXCLUDE_WORD_AND_PACK_TOO_MANY.getMessage(),
                            apiPosition.getPosition(), null);
            return response;
        }

        // 保存添加的排除关键词
        groupConfigADService.addExcludeKeyword(response, inputParam, userId, opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 删除推广组排除关键词
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "deleteExcludeKeyword", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> deleteExcludeKeyword(BaseRequestUser user,
            DeleteExcludeKeywordRequest param, BaseRequestOptions options) {
        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        List<GroupExcludeKeywordType> inputParam = param.getExcludeKeywords();
        if (CollectionUtils.isEmpty(inputParam)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        if (!CollectionUtils.isEmpty(inputParam)
                && inputParam.size() > GroupConstant.EXCLUDE_KEYWORD_OR_PACK_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_KEYWORDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response,
                            GroupConfigErrorCode.EXCLUDE_WORD_AND_PACK_TOO_MANY.getValue(),
                            GroupConfigErrorCode.EXCLUDE_WORD_AND_PACK_TOO_MANY.getMessage(),
                            apiPosition.getPosition(), null);
            return response;
        }

        // 保存删除的排除关键词
        groupConfigADService.deleteExcludeKeyword(response, inputParam, userId, opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 设置推广组排除人群
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "setExcludePeople", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> setExcludePeople(BaseRequestUser user, SetExcludePeopleRequest param,
            BaseRequestOptions options) {

        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        ExcludePeopleType inputParam = param.getExcludePeople();
        int groupId = new Long(inputParam.getGroupId()).intValue();
        List<Long> excludePids = inputParam.getExcludePeopleIds();
        if (inputParam.getGroupId() < 1l) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLE);
            apiPosition.addParam(GroupConstant.GROUPID);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }
        if (CollectionUtils.isEmpty(excludePids)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLE);
            apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 先验证排除人群是否超过限制
        if (!CollectionUtils.isEmpty(excludePids)
                && excludePids.size() > CproGroupConstant.GROUP_VT_EXCLUDE_CROWD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLE);
            apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLEIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response,
                            GroupConfigErrorCode.EXCLUDE_PEOPLEIDS_EXCEED_LIMIT.getValue(),
                            GroupConfigErrorCode.EXCLUDE_PEOPLEIDS_EXCEED_LIMIT.getMessage(),
                            apiPosition.getPosition(), null);
            return response;
        }

        // 保存排除设置
        groupConfigSetService.saveExcludePeople(response, groupId, excludePids, vtPeopleMgr, cproGroupVTMgr, userId,
                opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.getOptions().setSuccess(1);
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 查询推广组排除人群
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "getExcludePeople", returnType = ReturnType.ARRAY)
    public BaseResponse<ExcludePeopleType> getExcludePeople(BaseRequestUser user, GetExcludePeopleRequest param,
            BaseRequestOptions options) {

        BaseResponse<ExcludePeopleType> response = new BaseResponse<ExcludePeopleType>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的推广组id列表不为空
        long[] groupids = param.getGroupIds();
        if (ArrayUtils.isEmpty(groupids)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 验证传入参数长度
        if (groupids.length > getExcludePeopleMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getValue(),
                            GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 配额总数设为查询数量
        option.setTotal(groupids.length);

        // 获取推广组的被排除人群
        List<Integer> groupIds = CollectionsUtil.tranformLongArrayToIntList(groupids);
        List<VtPeopleExcludeMapperVo> voList = cproGroupVTMgr.findPeopleExcludeByCondition(userId, groupIds, null);
        ExcludePeopleType[] result = GroupBoMappingUtil.transVtPeopleExcludeMapperVo2ExcludePeopleType(voList);

        response.getOptions().setSuccess(groupids.length);
        response.setData(result);

        return response;

    }

    /**
     * 添加推广组排除人群
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "addExcludePeople", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> addExcludePeople(BaseRequestUser user, AddExcludePeopleRequest param,
            BaseRequestOptions options) {

        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        List<GroupExcludePeopleType> inputParam = param.getExcludePeoples();
        if (CollectionUtils.isEmpty(inputParam)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        if (!CollectionUtils.isEmpty(inputParam) && inputParam.size() > GroupConstant.EXCLUDE_PEOPLE_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response,
                            GroupConfigErrorCode.EXCLUDE_PEOPLE_TOO_MANY.getValue(),
                            GroupConfigErrorCode.EXCLUDE_PEOPLE_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 保存添加的排除人群
        groupConfigADService.addExcludePeople(response, inputParam, userId, opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 删除推广组排除人群
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "deleteExcludePeople", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> deleteExcludePeople(BaseRequestUser user, DeleteExcludePeopleRequest param,
            BaseRequestOptions options) {

        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        List<GroupExcludePeopleType> inputParam = param.getExcludePeoples();
        if (CollectionUtils.isEmpty(inputParam)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        if (!CollectionUtils.isEmpty(inputParam) && inputParam.size() > GroupConstant.EXCLUDE_PEOPLE_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_PEOPLES);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response,
                            GroupConfigErrorCode.EXCLUDE_PEOPLE_TOO_MANY.getValue(),
                            GroupConfigErrorCode.EXCLUDE_PEOPLE_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 保存删除的排除人群
        groupConfigADService.deleteExcludePeople(response, inputParam, userId, opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 设置推广组受众组合
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "setPackInfo", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> setPackInfo(BaseRequestUser user, SetPackInfoRequest param,
            BaseRequestOptions options) {

        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        PackInfoType inputParam = param.getPackInfo();
        int groupId = new Long(inputParam.getGroupId()).intValue();
        List<PackItemType> packs = inputParam.getPackItems();
        if (inputParam.getGroupId() < 1l) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.PACK_INFO);
            apiPosition.addParam(GroupConstant.GROUPID);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }
        if (CollectionUtils.isEmpty(packs)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.PACK_INFO);
            apiPosition.addParam(GroupConstant.PACK_ITEMS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 先验证推广组受众组合是否超过限制
        if (!CollectionUtils.isEmpty(packs) && packs.size() > CproGroupConstant.GROUP_PACK_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.PACK_INFO);
            apiPosition.addParam(GroupConstant.PACK_ITEMS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.PACKS_EXCEED_LIMIT.getValue(),
                            GroupConfigErrorCode.PACKS_EXCEED_LIMIT.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 保存推广组受众组合设置
        groupConfigSetService.savePackInfo(response, groupId, packs, groupPackFacade, userId, opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.getOptions().setSuccess(1);
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 查询推广组受众组合
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "getPackInfo", returnType = ReturnType.ARRAY)
    public BaseResponse<PackInfoType> getPackInfo(BaseRequestUser user, GetPackInfoRequest param,
            BaseRequestOptions options) {

        BaseResponse<PackInfoType> response = new BaseResponse<PackInfoType>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        // 检查传入的推广组id列表不为空
        long[] groupids = param.getGroupIds();
        if (ArrayUtils.isEmpty(groupids)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 验证传入参数长度
        if (groupids.length > getPackInfoMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getValue(),
                            GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 配额总数设为查询数量
        option.setTotal(groupids.length);

        // 获取推广组的被排除人群
        List<Integer> groupIds = CollectionsUtil.tranformLongArrayToIntList(groupids);
        List<GroupPack> boList = groupPackMgr.getGroupPacksByGroupIdList(groupIds);
        PackInfoType[] result = GroupBoMappingUtil.transGroupPack2PackInfoType(boList);

        response.getOptions().setSuccess(groupids.length);
        response.setData(result);

        return response;
    }

    /**
     * 添加推广组受众组合
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "addPackInfo", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> addPackInfo(BaseRequestUser user, AddPackInfoRequest param,
            BaseRequestOptions options) {

        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        List<GroupPackItemType> inputParam = param.getPacks();
        if (CollectionUtils.isEmpty(inputParam)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.PACKS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        if (!CollectionUtils.isEmpty(inputParam) && inputParam.size() > GroupConstant.GROUP_PACK_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.PACKS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.GROUP_PACK_TOO_MANY.getValue(),
                            GroupConfigErrorCode.GROUP_PACK_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 保存添加的受众组合
        groupConfigADService.addPackInfo(response, inputParam, userId, opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 删除推广组受众组合
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "deletePackInfo", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> deletePackInfo(BaseRequestUser user, DeletePackInfoRequest param,
            BaseRequestOptions options) {

        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        List<GroupPackItemType> inputParam = param.getPacks();
        if (CollectionUtils.isEmpty(inputParam)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.PACKS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        if (!CollectionUtils.isEmpty(inputParam) && inputParam.size() > GroupConstant.GROUP_PACK_DEL_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.PACKS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.GROUP_PACK_TOO_MANY.getValue(),
                            GroupConfigErrorCode.GROUP_PACK_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 保存删除的受众组合
        groupConfigADService.deletePackInfo(response, inputParam, userId, opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 出价设置
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "setPrice", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> setPrice(BaseRequestUser user, SetPriceRequest param,
            BaseRequestOptions options) {

        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];

        // 检查传入的参数不为空
        List<PriceType> inputParam = param.getPrices();
        if (CollectionUtils.isEmpty(inputParam)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.PRICES);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        if (!CollectionUtils.isEmpty(inputParam) && inputParam.size() > GroupConstant.GROUP_PRICE_SET_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.PRICES);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.PRICES_TOO_MANY.getValue(),
                            GroupConfigErrorCode.PRICES_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 保存出价设置
        groupConfigSetService.savePrice(response, inputParam, userId, opUser);

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    /**
     * 查询出价
     * 
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 调用参数
     * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
     * @return BaseResponse
     */
    @RPCMethod(methodName = "getPrice", returnType = ReturnType.ARRAY)
    public BaseResponse<PriceType> getPrice(BaseRequestUser user, GetPriceRequest param, BaseRequestOptions options) {
        BaseResponse<PriceType> response = new BaseResponse<PriceType>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        // 检查传入的推广组id列表不为空
        long[] groupids = param.getGroupIds();
        if (ArrayUtils.isEmpty(groupids)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 验证传入参数长度
        if (groupids.length > getPackInfoMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getValue(),
                            GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 配额总数设为查询数量
        option.setTotal(groupids.length);

        // 获取推广组兴趣出价
        List<Integer> groupIds = CollectionsUtil.tranformLongArrayToIntList(groupids);
        List<GroupPack> groupPackPriceList = groupPackMgr.getGroupPacksByGroupIdList(groupIds);
        List<GroupInterestPrice> groupInterestPriceList = groupITPriceMgr.findITPriceByGroupIds(groupIds);
        PriceType[] result = GroupBoMappingUtil.transPriceBo2PackInfoType(groupPackPriceList, groupInterestPriceList);

        response.getOptions().setSuccess(groupids.length);
        response.setData(result);

        return response;
    }

    @RPCMethod(methodName = "getAttachInfos", returnType = ReturnType.ARRAY)
    public BaseResponse<GroupAttachInfoType> getAttachInfos(BaseRequestUser user, GetAttachInfosRequest param,
            BaseRequestOptions options) {
        BaseResponse<GroupAttachInfoType> response = new BaseResponse<GroupAttachInfoType>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        // 检查传入的推广组id列表不为空
        List<Integer> groupids = param.getGroupIds();
        if (CollectionUtils.isEmpty(groupids)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 验证传入参数长度
        if (groupids.size() > getAttachInfoMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getValue(),
                            GroupConfigErrorCode.GROUP_ID_NUM_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 配额总数设为查询数量
        option.setTotal(groupids.size());

        List<AttachInfo> attachInfos = attachInfoMgr.getAttachInfoByGroupIds(groupids);

        if (CollectionUtils.isEmpty(attachInfos)) {
            response.getOptions().setSuccess(groupids.size());
            response.setData(null);
            return response;
        }

        // 按照groupid分组
        Map<Integer, List<AttachInfoType>> groupAttachInfo = new HashMap<Integer, List<AttachInfoType>>();
        for (AttachInfo info : attachInfos) {
            List<AttachInfoType> infoList = groupAttachInfo.get(info.getGroupId());
            if (infoList == null) {
                infoList = new ArrayList<AttachInfoType>();
                groupAttachInfo.put(info.getGroupId(), infoList);
            }
            AttachInfoType type =
                    new AttachInfoType(info.getAttachType(), info.getAttachContent(), info.getState(), "", null);

            // 补充其他不在DB中的附加信息，例如短信、子链，需从ubmc中获取
            if (info.getAttachType() == AttachInfoConstant.ATTACH_INFO_MESSAGE) {
                // 如果附加信息是短信，需请求ubmc获取短信内容
                CproGroup group = cproGroupMgr.findCproGroupById(info.getGroupId());
                type.setAttachMessage(groupAttachInfoMgr.getMessageFromUbmc(group.getGroupInfo().getAttachUbmcId(),
                        group.getGroupInfo().getAttachUbmcVersionId()));
            } else if (info.getAttachType() == AttachInfoConstant.ATTACH_INFO_SUB_URL) {
                CproGroup group = cproGroupMgr.findCproGroupById(info.getGroupId());
                AttachInfoVo attach = groupAttachInfoMgr.getSubUrlInfoFromUbmc(group.getGroupId());
                type.setAttachSubUrlParam(attach.getAttachSubUrlParam());
                // 三个数组长度要保证一致
                String[] subUrlTitles = attach.getAttachSubUrlTitle().split(
                        UbmcConstant.VALUE_ITEM_ELEMENT_DELIMITER);
                String[] subUrlLinks = attach.getAttachSubUrlLink().split(
                        UbmcConstant.VALUE_ITEM_ELEMENT_DELIMITER);
                String[] subUrlWirelessLinks = attach.getAttachSubUrlWirelessLink().split(
                        UbmcConstant.VALUE_ITEM_ELEMENT_DELIMITER);
                List<AttachSubUrlItemType> attachSubUrls = new ArrayList<AttachSubUrlItemType>();
                for (int i = 0; i < subUrlTitles.length; i++) {
                    AttachSubUrlItemType item = new AttachSubUrlItemType();
                    item.setAttachSubUrlTitle(subUrlTitles[i]);
                    item.setAttachSubUrlLink(subUrlLinks[i]);
                    item.setAttachSubUrlWirelessLink(subUrlWirelessLinks[i]);
                    attachSubUrls.add(item);
                }
                type.setAttachSubUrls(attachSubUrls);
            }

            infoList.add(type);
        }

        GroupAttachInfoType[] result = new GroupAttachInfoType[groupAttachInfo.size()];
        int i = 0;
        for (Map.Entry<Integer, List<AttachInfoType>> entry : groupAttachInfo.entrySet()) {
            GroupAttachInfoType type = new GroupAttachInfoType(entry.getKey(), entry.getValue());
            result[i] = type;
            i++;
        }

        // GetAttachInfoResponse data = new GetAttachInfoResponse();
        // data.setData(result);

        response.getOptions().setSuccess(groupids.size());
        response.setData(result);
        return response;
    }

    @RPCMethod(methodName = "deleteAttachInfos", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> deleteAttachInfos(BaseRequestUser user, DeleteAttachInfoRequest param,
            BaseRequestOptions options) {
        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(param.getAttachInfos().size());
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        // 检查传入的推广组id列表不为空
        List<GroupAttachInfoType> attachInfos = param.getAttachInfos();
        if (CollectionUtils.isEmpty(attachInfos)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 验证传入参数长度
        if (attachInfos.size() > delAttachInfoMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response,
                            GroupConfigErrorCode.DEL_ATTACH_NUM_TOO_MANY.getValue(),
                            GroupConfigErrorCode.DEL_ATTACH_NUM_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        int index = 0;
        for (GroupAttachInfoType type : attachInfos) {
            // 验证请求中一个推广组中同类型附加信息是否只有一个List<GroupAttachInfoType> attachInfos
            List<Integer> infoTypes = new ArrayList<Integer>(10);
            for (AttachInfoType attachInfo : type.getAttachTypes()) {
                if (infoTypes.contains(attachInfo.getAttachType())) {
                    ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                    apiPosition.addParam(GroupConstant.GROUPIDS, index);
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
                    ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                    apiPosition.addParam(GroupConstant.GROUPIDS, index);
                    response =
                            DRAPIMountAPIBeanUtils.addApiError(response,
                                    GroupConfigErrorCode.ATTACH_TYPE_INVALID.getValue(),
                                    GroupConfigErrorCode.ATTACH_TYPE_INVALID.getMessage(), apiPosition.getPosition(),
                                    null);
                    break;
                }
            }

            index++;
        }

        // 将附加创意按照类型分开
        List<Integer> phoneType = new ArrayList<Integer>();
        List<Integer> callType = new ArrayList<Integer>();
        List<Integer> consultType = new ArrayList<Integer>();
        List<Integer> messageType = new ArrayList<Integer>();
        List<Integer> suburlType = new ArrayList<Integer>();

        response = apportion(response, attachInfos, phoneType, callType, consultType, messageType, suburlType);

        if (response.getErrors() != null) {
            return response;
        }
        option.setTotal(attachInfos.size());

        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];
        Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

        if (CollectionUtils.isNotEmpty(phoneType)) {

            groupAttachInfoMgr.deletePhone(phoneType, visitor, userId);
        }

        if (CollectionUtils.isNotEmpty(consultType)) {
            groupAttachInfoMgr.deleteConsult(consultType, visitor, userId);
        }

        if (CollectionUtils.isNotEmpty(messageType)) {
            groupAttachInfoMgr.deleteMessage(messageType, visitor, userId);
        }

        if (CollectionUtils.isNotEmpty(suburlType)) {
            groupAttachInfoMgr.deleteSubUrl(suburlType, visitor, userId);
        }

        response.getOptions().setSuccess(attachInfos.size());

        if (response.getErrors() == null || response.getErrors().size() == 0) {
            response.setData(PlaceHolderResult.createResult());
        }

        return response;
    }

    private BaseResponse<PlaceHolderResult> apportion(BaseResponse<PlaceHolderResult> response,
            List<GroupAttachInfoType> attachInfos, List<Integer> phoneType, List<Integer> callType,
            List<Integer> consultType, List<Integer> messageType, List<Integer> suburlType) {
        if (CollectionUtils.isEmpty(attachInfos)) {
            return response;
        }
        for (int index = 0; index < attachInfos.size(); index++) {
            int groupId = attachInfos.get(index).getGroupId();
            List<AttachInfoType> infos = attachInfos.get(index).getAttachTypes();
            for (AttachInfoType info : infos) {
                int attachType = info.getAttachType();
                switch (attachType) {
                    case AttachInfoConstant.ATTACH_INFO_PHONE:
                        phoneType.add(groupId);
                        break;
                    case AttachInfoConstant.ATTACH_INFO_CONSULT:
                        consultType.add(groupId);
                        break;
                    case AttachInfoConstant.ATTACH_INFO_MESSAGE:
                        messageType.add(groupId);
                        break;
                    case AttachInfoConstant.ATTACH_INFO_SUB_URL:
                        suburlType.add(groupId);
                        break;
                    default:
                        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                        apiPosition.addParam(GroupConstant.GROUPID, index);
                        response =
                                DRAPIMountAPIBeanUtils.addApiError(response,
                                        GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                                        GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(),
                                        null);
                }
            }

        }

        return response;
    }

    @RPCMethod(methodName = "updateAttachInfos", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> updateAttachInfos(BaseRequestUser user, UpdateAttachInfoRequest param,
            BaseRequestOptions options) {
        BaseResponse<PlaceHolderResult> response = new BaseResponse<PlaceHolderResult>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(param.getAttachInfos().size());
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        // 检查传入的推广组id列表不为空
        List<GroupAttachInfoType> attachInfos = param.getAttachInfos();
        
        if (CollectionUtils.isEmpty(attachInfos)) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        // 验证传入参数长度
        if (attachInfos.size() > addAttachInfoMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);
            response =
                    DRAPIMountAPIBeanUtils.addApiError(response,
                            GroupConfigErrorCode.ADD_ATTACH_NUM_TOO_MANY.getValue(),
                            GroupConfigErrorCode.ADD_ATTACH_NUM_TOO_MANY.getMessage(), apiPosition.getPosition(), null);
            return response;
        }

        StopWatch watch = new StopWatch();
        List<Integer> groupIds = new ArrayList<Integer>();
        int index = 0;
        watch.start();
        for (GroupAttachInfoType type : attachInfos) {
            // 验证请求中groupid是否会重复
            if (groupIds.contains(type.getGroupId())) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.GROUPIDS, index);
                response =
                        DRAPIMountAPIBeanUtils.addApiError(response, GroupConfigErrorCode.ATTACH_GROUP_DUP.getValue(),
                                GroupConfigErrorCode.ATTACH_GROUP_DUP.getMessage(), apiPosition.getPosition(), null);
                return response;
            } else {
                groupIds.add(type.getGroupId());
            }
            index++;
        }
        watch.stop();

        log.info("check data use " + watch.getTime() + "ms");

        int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];
        Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

        watch.reset();
        watch.start();
        groupConfigADService.updateAttachInfo(response, attachInfos, visitor, userId);
        watch.stop();

        log.info("update use " + watch.getTime() + "ms");
        return response;
    }

    @RPCMethod(methodName = "isBridgeUser", returnType = ReturnType.ARRAY)
    public BaseResponse<AttachInfoUserResponse> isBridgeUser(BaseRequestUser user, AttachInfoUserRequestType param,
            BaseRequestOptions apiOption) {
        BaseResponse<AttachInfoUserResponse> response = new BaseResponse<AttachInfoUserResponse>();
        BaseResponseOptions option = new BaseResponseOptions();
        option.setTotal(1);
        response.setOptions(option);

        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtils.isNotEmpty(response.getErrors())) {
            return response;
        }

        int userId = Long.valueOf(user.getDataUser()).intValue();

        AttachInfoUserResponse data = new AttachInfoUserResponse();
        AttachInfoUserResponseType responseType = new AttachInfoUserResponseType();

        BridgeResult bridgeResult = bridgeService.getUserInfo(userId);
        boolean bridgeUser = false;
        if (bridgeResult != null && bridgeResult.getStatus() == BridgeConstant.STATUS_OK) {
            bridgeUser = BridgeConstant.getBridgeUser(bridgeResult.getData().getBridge_status());
        }

        int sign = bridgeUser ? 1 : 0;

        responseType.setUserId(userId);
        responseType.setSign(sign);

        data.setData(responseType);
        response.getOptions().setSuccess(1);
        response.setData(new AttachInfoUserResponse[] { data });
        return response;
    }

    /**
     * 
     * 获取排除移动应用
     * 
     * @param user
     * @param request
     * @param apiOption
     * @return
     */
    public ApiResult<GroupExcludeAppType> getExcludeApp(DataPrivilege user, GetExcludeAppRequest request,
            ApiOption apiOption) {
        ApiResult<GroupExcludeAppType> result = new ApiResult<GroupExcludeAppType>();
        PaymentResult pay = new PaymentResult();

        // ---------------------------参数验证---------------------------------//
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        if (request == null || request.getGroupIds() == null || request.getGroupIds().length > getExcludeAppMax) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        // ---------------------------获取数据---------------------------------//

        long[] groupIdArray = request.getGroupIds();
        pay.setTotal(groupIdArray.length);

        Map<Integer, List<Long>> appExcludeMap = new HashMap<Integer, List<Long>>();
        List<AppExclude> appExcludes =
                appExcludeMgr.findAppExcludeByCondition(user.getDataUser(),
                        CollectionsUtil.tranformLongArrayToIntList(groupIdArray), null);
        for (AppExclude appExclude : appExcludes) {
            if (!appExcludeMap.containsKey(appExclude.getGroupId())) {
                List<Long> appExcludeList = new ArrayList<Long>();
                appExcludeMap.put(appExclude.getGroupId(), appExcludeList);
            }
            // String name = UnionSiteCache.appCache.getAppNameById(appExclude.getAppSid());
            Long appSid = appExclude.getAppSid();
            // if (StringUtils.isNotEmpty(name)) {
            // appExcludeMap.get(appExclude.getGroupId()).add(name);
            // }
            appExcludeMap.get(appExclude.getGroupId()).add(appSid);
        }

        for (int i = 0; i < groupIdArray.length; i++) {
            Integer groupId = new Long(groupIdArray[i]).intValue();
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.GROUPIDS, i);
            if (groupIdArray[i] <= 0) {
                result =
                        ApiResultBeanUtils.addApiError(result, GroupErrorCode.PARAMETER_ERROR.getValue(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage(), apiPosition.getPosition(),
                                GroupErrorCode.PARAMETER_ERROR.getMessage());
            }

            GroupExcludeAppType excludeAppType = new GroupExcludeAppType();
            excludeAppType.setGroupId(groupId);
            if (appExcludeMap.containsKey(groupId)) {
                excludeAppType.setExcludeApp(appExcludeMap.get(groupId));
            } else {
                excludeAppType.setExcludeApp(new ArrayList<Long>());
            }

            result = ApiResultBeanUtils.addApiResult(result, excludeAppType);
            pay.increSuccess();
        }

        result.setPayment(pay);

        return result;
    }

    /**
     * 
     * 新增排除移动应用
     * 
     * @param user
     * @param request
     * @param apiOption
     * @return
     */
    public ApiResult<Object> addExcludeApp(DataPrivilege user, AddExcludeAppRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || CollectionUtils.isEmpty(request.getExcludeApps())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_APPS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        List<GroupExcludeAppType> excludeApps = request.getExcludeApps();
        if (excludeApps.size() > GroupConstant.EXCLUDE_APP_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_APPS);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_APP_ADD_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.EXCLUDE_APP_ADD_MAX_ERROR.getMessage(), apiPosition.getPosition(),
                            null);
            return result;
        }

        for (int index = 0; index < excludeApps.size(); index++) {
            GroupExcludeAppType item = excludeApps.get(index);
            if (item == null || CollectionUtils.isEmpty(item.getExcludeApp())) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.EXCLUDE_APPS, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_APP_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.EXCLUDE_APP_NULL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
            }
            // 每个推广组排除的应用个数不超过20000个
            if (CollectionUtils.isEmpty(item.getExcludeApp())
                    || item.getExcludeApp().size() > GroupConstant.GROUP_EXCLUDE_APP_MAX_NUM) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.EXCLUDE_APP, index);

                result =
                        ApiResultBeanUtils.addApiError(result,
                                GroupConfigErrorCode.GROUP_EXCLUDE_APP_MAX_ERROR.getValue(),
                                GroupConfigErrorCode.GROUP_EXCLUDE_APP_MAX_ERROR.getMessage(),
                                apiPosition.getPosition(), null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(excludeApps.size());
        result.setPayment(pay);

        result = groupConfigADService.addExcludeApp(result, excludeApps, user.getDataUser(), user.getOpUser());

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    /**
     * 
     * 删除排除移动应用
     * 
     * @param user
     * @param request
     * @param apiOption
     * @return
     */
    public ApiResult<Object> deleteExcludeApp(DataPrivilege user, DeleteExcludeAppRequest request, ApiOption apiOption) {
        ApiResult<Object> result = new ApiResult<Object>();

        // 验证user信息
        result = ApiResultBeanUtils.validateUser(result, user);
        if (result.hasErrors()) {
            return result;
        }

        // 验证request信息
        if (request == null || CollectionUtils.isEmpty(request.getExcludeApps())) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_APPS);

            result =
                    ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
                            GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(), apiPosition.getPosition(), null);
            return result;
        }

        List<GroupExcludeAppType> excludeApps = request.getExcludeApps();
        if (excludeApps.size() > GroupConstant.EXCLUDE_APP_ADD_MAX_NUM) {
            ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
            apiPosition.addParam(GroupConstant.EXCLUDE_APPS);

            result =
                    ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_APP_ADD_MAX_ERROR.getValue(),
                            GroupConfigErrorCode.EXCLUDE_APP_ADD_MAX_ERROR.getMessage(), apiPosition.getPosition(),
                            null);
            return result;
        }

        for (int index = 0; index < excludeApps.size(); index++) {
            GroupExcludeAppType item = excludeApps.get(index);
            if (item == null || CollectionUtils.isEmpty(item.getExcludeApp())) {
                ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
                apiPosition.addParam(GroupConstant.EXCLUDE_APPS, index);

                result =
                        ApiResultBeanUtils.addApiError(result, GroupConfigErrorCode.EXCLUDE_APP_NULL_ERROR.getValue(),
                                GroupConfigErrorCode.EXCLUDE_APP_NULL_ERROR.getMessage(), apiPosition.getPosition(),
                                null);
            }
        }
        if (result.hasErrors()) {
            return result;
        }

        PaymentResult pay = new PaymentResult();
        pay.setTotal(excludeApps.size());
        result.setPayment(pay);

        result = groupConfigADService.deleteExcludeApp(result, excludeApps, user.getDataUser(), user.getOpUser());

        if (!result.hasErrors()) {
            ApiResultBeanUtils.setSuccessObject(result);
        }

        return result;
    }

    @Override
    @RPCMethod(methodName = "getSimilarPeople", returnType = ReturnType.ARRAY)
    public BaseResponse<GroupSimilarPeopleType> getSimilarPeople(BaseRequestUser user, GetSimilarPeopleRequest param,
            BaseRequestOptions apiOption) {
        BaseResponse<GroupSimilarPeopleType> response = DRAPIMountAPIBeanUtils.createResponse(param.getDataSize());

        response = checkGetSimilarPeopleParams(response, user, param);
        if (CollectionUtil.isNotEmpty(response.getErrors())) {
            return response;
        }

        List<Integer> groupIdList = Ints.asList(param.getGroupIds());
        Map<Integer, CproGroup> groupMap =
                cproGroupMgr.findGroupMapByGroupIds(groupIdList, CproGroupConstant.GROUP_QUERY_STATE_ALL);
        if (CollectionUtil.isEmpty(groupMap)) {
            return GlobalErrorCode.UNEXPECTED_PARAMETER.getErrorResponse(response, GroupConstant.GROUPIDS, null, null);
        }

        List<GroupSimilarPeopleType> result = new ArrayList<GroupSimilarPeopleType>(groupIdList.size());
        for (Integer groupId : groupIdList) {
            if (groupMap.get(groupId) != null) {
                GroupSimilarPeopleType groupSimilarPeopleType = new GroupSimilarPeopleType();
                groupSimilarPeopleType.setGroupId(groupId);
                groupSimilarPeopleType.setSimilarFlag(groupMap.get(groupId).getSimilarFlag());
                result.add(groupSimilarPeopleType);
            }
        }

        response.getOptions().setSuccess(result.size());
        response.setData(result.toArray(new GroupSimilarPeopleType[0]));

        return response;
    }

    /**
     * 获取相似人群设置参数校验
     * 
     * @param response 接口返回值
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 请求参数，包含需要查询的推广组ID
     * @return 接口返回值
     */
    private BaseResponse<GroupSimilarPeopleType> checkGetSimilarPeopleParams(
            BaseResponse<GroupSimilarPeopleType> response, BaseRequestUser user, GetSimilarPeopleRequest param) {
        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtil.isNotEmpty(response.getErrors())) {
            return response;
        }

        // 检查传入的id列表不为空
        int[] groupIds = param.getGroupIds();
        if (ArrayUtils.isEmpty(groupIds)) {
            return GlobalErrorCode.UNEXPECTED_PARAMETER.getErrorResponse(response, GroupConstant.GROUPIDS, null, null);
        }

        // 验证传入参数长度
        if (groupIds.length > getSimilarPeopleMax) {
            return GroupConfigErrorCode.SIMILAR_PEOPLE_GET_MAX_ERROR.getErrorResponse(response, GroupConstant.GROUPIDS,
                    null, null);
        }
        return response;
    }

    @Override
    @RPCMethod(methodName = "updateSimilarPeople", returnType = ReturnType.ARRAY)
    public BaseResponse<PlaceHolderResult> updateSimilarPeople(BaseRequestUser user, UpdateSimilarPeopleRequest param,
            BaseRequestOptions options) {
        BaseResponse<PlaceHolderResult> response = DRAPIMountAPIBeanUtils.createResponse(param.getDataSize());

        response = checkUpdateSimilarPeopleParams(response, user, param);
        if (CollectionUtil.isNotEmpty(response.getErrors())) {
            return response;
        }

        List<GroupSimilarPeopleType> similarPeoples = param.getSimilarPeoples();
        List<Integer> groupIds = Lists.transform(similarPeoples, new Function<GroupSimilarPeopleType, Integer>() {
            @Override
            public Integer apply(GroupSimilarPeopleType type) {
                return type.getGroupId();
            }
        });

        Map<Integer, CproGroup> groupMap =
                cproGroupMgr.findGroupMapByGroupIds(groupIds, CproGroupConstant.GROUP_QUERY_STATE_ALL);
        if (CollectionUtil.isEmpty(groupMap)) {
            return GlobalErrorCode.UNEXPECTED_PARAMETER.getErrorResponse(response, GroupConstant.GROUPIDS, null, null);
        }
        StopWatch watch = new StopWatch();
        watch.start();
        HashMultimap<Integer, Integer> modMap = HashMultimap.create();
        List<Integer> vtOnList = Lists.newLinkedList();
        for (GroupSimilarPeopleType type : similarPeoples) {
            CproGroup group = groupMap.get(type.getGroupId());
            // 推广组存在；推广组支持相似人群；推广组当前相似人群设置满足修改条件；可修改
            if (group != null
                    && SimilarPeopleUtil.groupSupportSimilarPeople(group.getTargetType())
                    && (SimilarPeopleConstant.FLAG_ON == type.getSimilarFlag() || SimilarPeopleConstant.FLAG_OFF == type
                            .getSimilarFlag())) {
                modMap.put(type.getSimilarFlag(), type.getGroupId());

                if (TargettypeUtil.hasVT(group.getTargetType())
                        && SimilarPeopleConstant.FLAG_ON == type.getSimilarFlag()) {
                    vtOnList.add(type.getGroupId());
                }
            }
        }

        if (modMap.isEmpty()) {
            return GroupConfigErrorCode.SIMILAR_PEOPLE_PARAM_ERROR.getErrorResponse(response, GroupConstant.GROUPIDS,
                    null, null);
        }

        if (modMap.keySet().size() == 2) {
            // 验证请求参数（包括是否有groupId相同，similarFlag不同的参数）
            SetView<Integer> commonGroupId =
                    Sets.intersection(modMap.get(SimilarPeopleConstant.FLAG_OFF),
                            modMap.get(SimilarPeopleConstant.FLAG_ON));
            if (CollectionUtil.isNotEmpty(commonGroupId)) {
                return GroupConfigErrorCode.SIMILAR_PEOPLE_PARAM_ERROR.getErrorResponse(response,
                        GroupConstant.GROUPIDS, null, null);
            }
        }
        watch.stop();
        log.info(String.format("check data use %d ms", watch.getTime()));

        Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

        watch.reset();
        watch.start();
        int success = 0;
        Set<Integer> similarFlags = modMap.keySet();
        for (int similarFlag : similarFlags) {
            Set<Integer> groupIdSet = modMap.get(similarFlag);
            List<Integer> groupIdList = Lists.newArrayList(groupIdSet.iterator());

            int changeOverFlag = SimilarPeopleUtil.changeOverFlag(similarFlag);
            if (SimilarPeopleConstant.FLAG_ILLEGAL != changeOverFlag) {
                Integer modCount = cproGroupMgr.modSimilarFlag(visitor, groupIdList, changeOverFlag, similarFlag);
                if (modCount != null) {
                    success += modCount;
                }
            }
        }

        similarPeopleMgr.batchCreateIfNotExist(vtOnList, new Long(user.getDataUser()).intValue(), 
                visitor.getUserid()); // 对于VT推广组，需生成人群ID
        response.getOptions().setSuccess(success);
        watch.stop();

        recordUpdateSimilarPeopleHistory(modMap, groupMap);

        log.info(String.format("update use %d ms", watch.getTime()));
        return response;
    }

    /**
     * 修改相似人群设置参数校验
     * 
     * @param response 接口返回值
     * @param user 用户信息，包含操作者和被操作者id
     * @param param 请求参数，包含需要查询的推广组ID
     * @return 接口返回值
     */
    private BaseResponse<PlaceHolderResult> checkUpdateSimilarPeopleParams(BaseResponse<PlaceHolderResult> response,
            BaseRequestUser user, UpdateSimilarPeopleRequest param) {
        response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
        if (CollectionUtil.isNotEmpty(response.getErrors())) {
            return response;
        }

        // 检查传入的推广组id列表不为空
        List<GroupSimilarPeopleType> similarPeoples = param.getSimilarPeoples();
        if (CollectionUtil.isEmpty(similarPeoples)) {
            return GlobalErrorCode.UNEXPECTED_PARAMETER.getErrorResponse(response, GroupConstant.GROUPIDS, null, null);
        }

        // 验证传入参数长度
        if (similarPeoples.size() > updateSimilarPeopleMax) {
            return GroupConfigErrorCode.SIMILAR_PEOPLE_UPDATE_MAX_ERROR.getErrorResponse(response,
                    GroupConstant.GROUPIDS, null, null);
        }
        return response;
    }

    /**
     * 记录修改相似人群设置历史纪录
     * 
     * @param modMap
     * @param groupMap 推广组信息map
     */
    private void recordUpdateSimilarPeopleHistory(HashMultimap<Integer, Integer> modMap,
            Map<Integer, CproGroup> groupMap) {
        List<OptContent> optContents = new ArrayList<OptContent>(groupMap.size());
        Set<Integer> similarFlags = modMap.keySet();
        OpTypeVo opType = null;
        CproGroup group = null;
        for (int destFlag : similarFlags) {
            int srcFlag = SimilarPeopleUtil.changeOverFlag(destFlag);
            if (SimilarPeopleConstant.FLAG_ILLEGAL == srcFlag) {
                continue;
            }

            Set<Integer> groupIdSet = modMap.get(destFlag);
            for (Integer groupId : groupIdSet) {
                group = groupMap.get(groupId);
                if (group != null) {
                    if (TargettypeUtil.hasKT(group.getTargetType())) {
                        opType = OptHistoryConstant.OPTYPE_GROUP_SIMILAR_KT_FLAG;
                    } else if (TargettypeUtil.hasVT(group.getTargetType())) {
                        opType = OptHistoryConstant.OPTYPE_GROUP_SIMILAR_VT_FLAG;
                    } else {
                        continue;
                    }
                    OptContent content =
                            new OptContent(group.getUserId(), opType.getOpType(), opType.getOpLevel(), groupId, opType
                                    .getTransformer().toDbString(srcFlag), opType.getTransformer().toDbString(destFlag));
                    optContents.add(content);
                }
            }
        }

        SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);
    }

    public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
        this.cproGroupMgr = cproGroupMgr;
    }

    public void setUnitMgr(CproUnitMgr unitMgr) {
        this.unitMgr = unitMgr;
    }

    public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
        this.cproKeywordMgr = cproKeywordMgr;
    }

    public void setGroupSiteConfigMgr(GroupSiteConfigMgr groupSiteConfigMgr) {
        this.groupSiteConfigMgr = groupSiteConfigMgr;
    }

    public void setGroupConfigValidator(GroupConfigValidator groupConfigValidator) {
        this.groupConfigValidator = groupConfigValidator;
    }

    public void setUserMgr(UserMgr userMgr) {
        this.userMgr = userMgr;
    }

    public void setGroupConfigSetService(GroupConfigSetService groupConfigSetService) {
        this.groupConfigSetService = groupConfigSetService;
    }

    public void setGetRegionConfigMax(int getRegionConfigMax) {
        this.getRegionConfigMax = getRegionConfigMax;
    }

    public void setGetSiteConfigMax(int getSiteConfigMax) {
        this.getSiteConfigMax = getSiteConfigMax;
    }

    public void setGetExcludeIpMax(int getExcludeIpMax) {
        this.getExcludeIpMax = getExcludeIpMax;
    }

    public void setGetExcludeSiteMax(int getExcludeSiteMax) {
        this.getExcludeSiteMax = getExcludeSiteMax;
    }

    public int getGetExcludeAppMax() {
        return getExcludeAppMax;
    }

    public void setGetExcludeAppMax(int getExcludeAppMax) {
        this.getExcludeAppMax = getExcludeAppMax;
    }

    public void setGetTargetInfoMax(int getTargetInfoMax) {
        this.getTargetInfoMax = getTargetInfoMax;
    }

    public void setGetTradeSitePriceMax(int getTradeSitePriceMax) {
        this.getTradeSitePriceMax = getTradeSitePriceMax;
    }

    public void setGetSiteUrlMax(int getSiteUrlMax) {
        this.getSiteUrlMax = getSiteUrlMax;
    }

    public int getGetKeywordMax() {
        return getKeywordMax;
    }

    public void setGetKeywordMax(int getKeywordMax) {
        this.getKeywordMax = getKeywordMax;
    }

    public int getGetInterestInfoMax() {
        return getInterestInfoMax;
    }

    public void setGetInterestInfoMax(int getInterestInfoMax) {
        this.getInterestInfoMax = getInterestInfoMax;
    }

    public void setCproGroupVTMgr(CproGroupVTMgr cproGroupVTMgr) {
        this.cproGroupVTMgr = cproGroupVTMgr;
    }

    public VtPeopleMgr getVtPeopleMgr() {
        return vtPeopleMgr;
    }

    public void setVtPeopleMgr(VtPeopleMgr vtPeopleMgr) {
        this.vtPeopleMgr = vtPeopleMgr;
    }

    public void setGroupConfigADService(GroupConfigAddAndDeleteService groupConfigADService) {
        this.groupConfigADService = groupConfigADService;
    }

    public CproKeywordFacade getCproKeywordFacade() {
        return cproKeywordFacade;
    }

    public void setCproKeywordFacade(CproKeywordFacade cproKeywordFacade) {
        this.cproKeywordFacade = cproKeywordFacade;
    }

    public CproITFacade getCproITFacade() {
        return cproITFacade;
    }

    public void setCproITFacade(CproITFacade cproITFacade) {
        this.cproITFacade = cproITFacade;
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

    public WordExcludeFacade getWordExcludeFacade() {
        return wordExcludeFacade;
    }

    public void setWordExcludeFacade(WordExcludeFacade wordExcludeFacade) {
        this.wordExcludeFacade = wordExcludeFacade;
    }

    public int getGetExcludeKeywordMax() {
        return getExcludeKeywordMax;
    }

    public void setGetExcludeKeywordMax(int getExcludeKeywordMax) {
        this.getExcludeKeywordMax = getExcludeKeywordMax;
    }

    public int getGetExcludePeopleMax() {
        return getExcludePeopleMax;
    }

    public void setGetExcludePeopleMax(int getExcludePeopleMax) {
        this.getExcludePeopleMax = getExcludePeopleMax;
    }

    public int getGetPackInfoMax() {
        return getPackInfoMax;
    }

    public void setGetPackInfoMax(int getPackInfoMax) {
        this.getPackInfoMax = getPackInfoMax;
    }

    public int getGetPriceMax() {
        return getPriceMax;
    }

    public void setGetPriceMax(int getPriceMax) {
        this.getPriceMax = getPriceMax;
    }

    public GroupPackMgr getGroupPackMgr() {
        return groupPackMgr;
    }

    public void setGroupPackMgr(GroupPackMgr groupPackMgr) {
        this.groupPackMgr = groupPackMgr;
    }

    public GroupPackFacade getGroupPackFacade() {
        return groupPackFacade;
    }

    public void setGroupPackFacade(GroupPackFacade groupPackFacade) {
        this.groupPackFacade = groupPackFacade;
    }

    public GroupITPriceMgr getGroupITPriceMgr() {
        return groupITPriceMgr;
    }

    public void setGroupITPriceMgr(GroupITPriceMgr groupITPriceMgr) {
        this.groupITPriceMgr = groupITPriceMgr;
    }

    public void setGetWordMax(int getWordMax) {
        this.getWordMax = getWordMax;
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

    public void setAttachInfoMgr(AttachInfoMgr attachInfoMgr) {
        this.attachInfoMgr = attachInfoMgr;
    }

    public void setGroupAttachInfoMgr(GroupAttachInfoMgr groupAttachInfoMgr) {
        this.groupAttachInfoMgr = groupAttachInfoMgr;
    }

    public void setBridgeService(BridgeService bridgeService) {
        this.bridgeService = bridgeService;
    }

    public void setGetSimilarPeopleMax(int getSimilarPeopleMax) {
        this.getSimilarPeopleMax = getSimilarPeopleMax;
    }

    public void setUpdateSimilarPeopleMax(int updateSimilarPeopleMax) {
        this.updateSimilarPeopleMax = updateSimilarPeopleMax;
    }

    public void setSimilarPeopleMgr(SimilarPeopleMgr similarPeopleMgr) {
        this.similarPeopleMgr = similarPeopleMgr;
    }
}
