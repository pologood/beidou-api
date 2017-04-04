package com.baidu.beidou.api.internal.business.exporter.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.cprogroup.util.ApiTargetTypeUtil;
import com.baidu.beidou.api.internal.business.constant.NameConstant;
import com.baidu.beidou.api.internal.business.constant.Status;
import com.baidu.beidou.api.internal.business.exporter.NameService;
import com.baidu.beidou.api.internal.business.vo.GroupInfo;
import com.baidu.beidou.api.internal.business.vo.GroupResult;
import com.baidu.beidou.api.internal.business.vo.KeywordInfo;
import com.baidu.beidou.api.internal.business.vo.KeywordResult;
import com.baidu.beidou.api.internal.business.vo.KtEnabledInfo;
import com.baidu.beidou.api.internal.business.vo.KtEnabledMultiResult;
import com.baidu.beidou.api.internal.business.vo.KtEnabledResult;
import com.baidu.beidou.api.internal.business.vo.PlanInfo;
import com.baidu.beidou.api.internal.business.vo.PlanResult;
import com.baidu.beidou.api.internal.business.vo.SiteResult;
import com.baidu.beidou.api.internal.business.vo.UnitInfo;
import com.baidu.beidou.api.internal.business.vo.UnitResult;
import com.baidu.beidou.api.internal.business.vo.UnitResultOne;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.cprogroup.vo.KeywordInfoLite;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.bo.UnitMater;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestBaseMaterial;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestLite;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.multidatabase.datasource.MultiDataSourceSupport;
import com.baidu.beidou.stat.dao.StatDAO2;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.beidou.util.atomdriver.AtomUtils;
import com.baidu.beidou.util.dao.MultiDataSourceDaoImpl;

/**
 * 
 * ClassName: NameServiceImpl <br>
 * Function: 提供给holmes的api
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 16, 2011
 */
public class NameServiceImpl implements NameService {

    private static final Log log = LogFactory.getLog(NameServiceImpl.class);

    private CproPlanMgr cproPlanMgr;

    private CproGroupMgr cproGroupMgr;

    private CproUnitMgr cproUnitMgr;

    private UserMgr userMgr;

    private CproKeywordMgr cproKeywordMgr;

    private StatDAO2 statDAO;

    private Integer planNumMax;
    private Integer groupNumMax;
    private Integer unitNumMax;
    private Integer siteNumMax;
    private Integer keywordNumMax;

    /**
     * 
     * 查询推广计划
     * 
     * @param userId
     * @param planIds 推广计划id列表
     * @return PlanResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public PlanResult getPlanNamebyId(Integer userId, List<Integer> planIds) {

        PlanResult result = new PlanResult();
        try {

            Map<Integer, PlanInfo> planid2Name = new HashMap<Integer, PlanInfo>();

            if (userId == null || userId <= 0) {
                log.warn("getPlanNamebyId userId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            if (planIds == null || planIds.size() == 0 || planIds.size() > planNumMax) {
                log.warn("getPlanNamebyId planIds error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            List<CproPlan> plans = cproPlanMgr.findCproPlanByPlanIds(planIds);

            if (CollectionUtils.isEmpty(plans)) {
                log.warn("getPlanNamebyId plans null");
                result.setStatus(Status.SUCCESS);
                result.setPlanid2Name(planid2Name);
                return result;
            }

            for (CproPlan plan : plans) {

                if (!plan.getUserId().equals(userId)) {
                    log.warn("getPlanNamebyId user not allow planId: " + plan.getPlanId());
                    continue;
                }

                PlanInfo info = new PlanInfo();
                info.setName(plan.getPlanName());
                info.setIsDeleted(plan.getPlanState() == CproPlanConstant.PLAN_STATE_DELETE ? 1 : 0);

                planid2Name.put(plan.getPlanId(), info);
            }

            result.setStatus(Status.SUCCESS);
            result.setPlanid2Name(planid2Name);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }

    }

    /**
     * 
     * 查询推广组
     * 
     * @param userId
     * @param groupIds 推广组id列表
     * @return GroupResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public GroupResult getGroupNamebyId(Integer userId, List<Integer> groupIds) {
        GroupResult result = new GroupResult();
        try {

            Map<Integer, GroupInfo> groupid2Name = new HashMap<Integer, GroupInfo>();

            if (userId == null || userId <= 0) {
                log.warn("getGroupNamebyId userId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            if (groupIds == null || groupIds.size() == 0 || groupIds.size() > groupNumMax) {
                log.warn("getGroupNamebyId groupIds error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            List<CproGroup> groups = cproGroupMgr.findCproGroupByGroupIds(groupIds);

            if (CollectionUtils.isEmpty(groups)) {
                log.warn("getGroupNamebyId groups null");
                result.setStatus(Status.SUCCESS);
                result.setGroupid2Name(groupid2Name);
                return result;
            }

            for (CproGroup group : groups) {

                if (!group.checkUser(userId)) {
                    log.warn("getGroupNamebyId user not allow groupId: " + group.getGroupId());
                    continue;
                }

                GroupInfo info = new GroupInfo();
                info.setName(group.getGroupName());
                info.setTargettype(ApiTargetTypeUtil.toApiGroupTargetType(group.getTargetType()));
                info.setIsDeleted(group.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE ? 1 : 0);

                groupid2Name.put(group.getGroupId(), info);
            }

            result.setStatus(Status.SUCCESS);
            result.setGroupid2Name(groupid2Name);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }
    }

    /**
     * 
     * 查询创意
     * 
     * @param userId
     * @param unitIds 创意id列表
     * @return UnitResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public UnitResult getUnitMaterialbyId(Integer userId, List<Long> unitIds) {
        UnitResult result = new UnitResult();

        try {
            if (userId == null) {
                log.warn("getUnitMaterialbyId userId error");
                result.setStatus(Status.PARAM_ERROR);
            }
            User user = userMgr.findUserBySFid(userId);
            if (user == null) {
                log.warn("getUnitMaterialbyId user null");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            if (unitIds == null || unitIds.size() == 0 || unitIds.size() > unitNumMax) {
                log.warn("getUnitMaterialbyId unitIds error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            Map<Long, UnitInfo> unitId2Name = new HashMap<Long, UnitInfo>();

            List<Unit> units = cproUnitMgr.findUnitsByIds(user.getUserid(), unitIds);

            if (CollectionUtils.isEmpty(units)) {
                log.warn("getUnitMaterialbyId units null");
                result.setStatus(Status.SUCCESS);
                result.setUnitid2Name(unitId2Name);
                return result;
            }

            List<RequestBaseMaterial> requests = new ArrayList<RequestBaseMaterial>();
            for (Unit unit : units) {
                RequestLite request =
                        new RequestLite(unit.getMaterial().getMcId(), unit.getMaterial().getMcVersionId());
                requests.add(request);
            }
            Map<RequestBaseMaterial, String> urlMap = cproUnitMgr.generateMaterUrl(requests);

            for (int index = 0; index < units.size(); index++) {

                Unit unit = units.get(index);
                RequestBaseMaterial request = requests.get(index);

                if (!unit.getUser().getUserid().equals(userId)) {
                    log.warn("getUnitMaterialbyId user not allow unitId: " + unit.getId());
                    continue;
                }

                int type = unit.getMaterial().getWuliaoType();

                UnitMater info = unit.getMaterial();
                UnitInfo unitInfo = null;

                unitInfo = new UnitInfo();
                if (type == CproUnitConstant.MATERIAL_TYPE_LITERAL) {
                    unitInfo.setType(1);
                    unitInfo.setDesc1(info.getDescription1());
                    unitInfo.setDesc2(info.getDescription2());
                } else if (type == CproUnitConstant.MATERIAL_TYPE_PICTURE
                        || type == CproUnitConstant.MATERIAL_TYPE_FLASH) {
                    unitInfo.setType(type == CproUnitConstant.MATERIAL_TYPE_PICTURE ? 2 : 3);
                    unitInfo.setUrl(urlMap.get(request));
                    unitInfo.setWidth(info.getWidth());
                    unitInfo.setHeight(info.getHeight());
                } else if (type == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
                    unitInfo.setType(CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON);
                    unitInfo.setUrl(urlMap.get(request));
                    unitInfo.setWidth(info.getWidth());
                    unitInfo.setHeight(info.getHeight());
                    unitInfo.setDesc1(info.getDescription1()); // 图文加入描述1/2
                    unitInfo.setDesc2(info.getDescription2());
                }

                unitInfo.setTitle(info.getTitle());
                unitInfo.setShowUrl(info.getShowUrl());
                unitInfo.setTargetUrl(info.getTargetUrl());
                unitInfo.setIsDeleted(unit.getState() == CproUnitConstant.UNIT_STATE_DELETE ? 1 : 0);

                unitId2Name.put(unit.getId(), unitInfo);

            }

            result.setStatus(Status.SUCCESS);
            result.setUnitid2Name(unitId2Name);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }
    }

    /**
     * 
     * 根据站点的签名返回url
     * 
     * @param keys 网站数字签名
     * @return SiteResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public SiteResult getSiteNamebyKey(List<String> keys) {
        SiteResult result = new SiteResult();

        try {

            if (keys == null || keys.size() == 0 || keys.size() > siteNumMax) {
                log.warn("getSiteNamebyKey keys error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            Set<BigInteger> set = new HashSet<BigInteger>(keys.size());

            for (String key : keys) {
                try {
                    set.add(new BigInteger(key));
                } catch (Exception e) {
                    log.warn("getSiteNamebyKey key error key:" + key);
                }
            }
            Map<BigInteger, String> infos = statDAO.querySiteValue(set);

            Map<String, String> siteInfos = new HashMap<String, String>(keys.size());
            for (BigInteger key : infos.keySet()) {
                siteInfos.put(key.toString(), infos.get(key));
            }

            result.setStatus(Status.SUCCESS);
            result.setSiteKey2Url(siteInfos);

            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }
    }

    /**
     * 
     * 判断用户是否启用来KT定向方式
     * 
     * @param userId
     * @return KtEnabledResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public KtEnabledResult isKtEnabledByUserid(Integer userId) {
        KtEnabledResult result = new KtEnabledResult();
        try {

            if (userId == null || userId <= 0) {
                log.warn("isKtEnabledByUserid userId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            boolean enabled = cproGroupMgr.hasKtGroup(userId, null, null);

            if (enabled == false) {
                result.setStatus(Status.SUCCESS);
                result.setIsEnabled(NameConstant.KT_DISABLED);
                return result;
            } else {
                result.setStatus(Status.SUCCESS);
                result.setIsEnabled(NameConstant.KT_ENABLED);
                return result;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }
    }

    /**
     * 
     * 批量判断用户是否启用来KT定向方式
     * 
     * @param userId
     * @return KtEnabledMultiResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public KtEnabledMultiResult isKtEnabledByUserids(List<Integer> userIdList) {
        KtEnabledMultiResult result = new KtEnabledMultiResult();
        try {

            if (userIdList == null || userIdList.size() <= 0) {
                log.warn("isKtEnabledByUserids userId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            Map<Integer, KtEnabledInfo> userid2KtEnabled = new HashMap<Integer, KtEnabledInfo>();

            for (Integer userId : userIdList) {
                ThreadContext.putUserId(userId);
                boolean enabled = cproGroupMgr.hasKtGroup(userId, null, null);

                if (enabled == false) {
                    KtEnabledInfo info = new KtEnabledInfo();
                    info.setIsEnabled(NameConstant.KT_DISABLED);
                    userid2KtEnabled.put(userId, info);
                } else {
                    KtEnabledInfo info = new KtEnabledInfo();
                    info.setIsEnabled(NameConstant.KT_ENABLED);
                    userid2KtEnabled.put(userId, info);
                }
            }

            result.setStatus(Status.SUCCESS);
            result.setUserid2KtEnabled(userid2KtEnabled);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }
    }

    /**
     * 
     * 判断推广计划是否启用来KT定向方式
     * 
     * @param userId
     * @param planId
     * @return KtEnabledResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public KtEnabledResult isKtEnabledByUseridAndPlanid(Integer userId, Integer planId) {
        KtEnabledResult result = new KtEnabledResult();
        try {

            if (userId == null || userId <= 0) {
                log.warn("isKtEnabledByUseridAndPlanid userId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            if (planId == null || planId <= 0) {
                log.warn("isKtEnabledByUseridAndPlanid planId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            boolean enabled = cproGroupMgr.hasKtGroup(userId, planId, null);

            if (enabled == false) {
                result.setStatus(Status.SUCCESS);
                result.setIsEnabled(NameConstant.KT_DISABLED);
                return result;
            } else {
                result.setStatus(Status.SUCCESS);
                result.setIsEnabled(NameConstant.KT_ENABLED);
                return result;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }
    }

    /**
     * 
     * 判断推广组是否启用来KT定向方式
     * 
     * @param userId
     * @param groupId
     * @return KtEnabledResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public KtEnabledResult isKtEnabledByUseridAndGroupid(Integer userId, Integer groupId) {
        KtEnabledResult result = new KtEnabledResult();
        try {

            if (userId == null || userId <= 0) {
                log.warn("isKtEnabledByUseridAndGroupid userId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            if (groupId == null || groupId <= 0) {
                log.warn("isKtEnabledByUseridAndGroupid groupId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            boolean enabled = cproGroupMgr.hasKtGroup(userId, null, groupId);

            if (enabled == false) {
                result.setStatus(Status.SUCCESS);
                result.setIsEnabled(NameConstant.KT_DISABLED);
                return result;
            } else {
                result.setStatus(Status.SUCCESS);
                result.setIsEnabled(NameConstant.KT_ENABLED);
                return result;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }
    }

    /**
     * 
     * 获取有效推广计划
     * 
     * @param userId
     * @return PlanResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public PlanResult getEffectivePlansByUserid(Integer userId) {
        PlanResult result = new PlanResult();
        try {

            Map<Integer, PlanInfo> planid2Name = new HashMap<Integer, PlanInfo>();

            if (userId == null || userId <= 0) {
                log.warn("getEffectivePlansByUserid userId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            List<CproPlan> plans = cproPlanMgr.findEffectiveByUserId(userId);

            if (CollectionUtils.isEmpty(plans)) {
                log.warn("getEffectivePlansByUserid plans null");
                result.setStatus(Status.SUCCESS);
                result.setPlanid2Name(planid2Name);
                return result;
            }

            for (CproPlan plan : plans) {

                if (!plan.getUserId().equals(userId)) {
                    log.warn("getEffectivePlansByUserid user not allow planId: " + plan.getPlanId());
                    continue;
                }

                PlanInfo info = new PlanInfo();
                info.setName(plan.getPlanName());
                info.setIsDeleted(plan.getPlanState() == CproPlanConstant.PLAN_STATE_DELETE ? 1 : 0);

                planid2Name.put(plan.getPlanId(), info);
            }

            result.setStatus(Status.SUCCESS);
            result.setPlanid2Name(planid2Name);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }

    }

    /**
     * 
     * 获取有效推广组
     * 
     * @param userId
     * @param planId
     * @return GroupResult
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public GroupResult getEffectiveGroupsByUseridAndPlanid(Integer userId, Integer planId) {
        GroupResult result = new GroupResult();
        try {

            Map<Integer, GroupInfo> groupid2Name = new HashMap<Integer, GroupInfo>();

            if (userId == null || userId <= 0 || planId == null || planId <= 0) {
                log.warn("getEffectiveGroupsByUseridAndPlanid userId or planId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            List<CproGroup> groups = cproGroupMgr.findEffectiveByPlanId(planId);

            if (CollectionUtils.isEmpty(groups)) {
                log.warn("getEffectiveGroupsByUseridAndPlanid groups null");
                result.setStatus(Status.SUCCESS);
                result.setGroupid2Name(groupid2Name);
                return result;
            }

            for (CproGroup group : groups) {

                if (!group.checkUser(userId)) {
                    log.warn("getEffectiveGroupsByUseridAndPlanid user not allow groupId: " + group.getGroupId());
                    continue;
                }

                GroupInfo info = new GroupInfo();
                info.setName(group.getGroupName());
                info.setIsDeleted(group.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE ? 1 : 0);

                groupid2Name.put(group.getGroupId(), info);
            }

            result.setStatus(Status.SUCCESS);
            result.setGroupid2Name(groupid2Name);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }
    }

    /**
     * 
     * 获取关键词字面与状态（删除与否）<br>
     * 如果keywordid与atomid不匹配，返回错误 如果keywordid在北斗找不到则认为是删除状态
     * 
     * @param keywordIdAndAtomIdList 包含keywordid和atomid，并一一对应，形如array(array(keywordid=> XXX, atomid =>XXX),…)
     * @return KeywordResult 关键词字面与状态（删除与否）
     * @version 2.0.0
     * @author zhangxu
     * @date Dec 16, 2011
     */
    public KeywordResult getKeywordLiteral(List<Map<String, Long>> keywordIdAndAtomIdList) {
        KeywordResult result = new KeywordResult();
        Set<Long> deleteWordIds = new HashSet<Long>(); // 保存在北斗找不到的keywordid集合
        Map<Long, Long> atomId2keywordIdMap = new HashMap<Long, Long>(); // 保存在北斗找不到的keywordid与atomid字典
        try {

            Map<Long, KeywordInfo> keywordid2Name = new HashMap<Long, KeywordInfo>();

            if (keywordIdAndAtomIdList == null || keywordIdAndAtomIdList.size() == 0) {
                log.warn("getKeywordLiteral keywordIdAndAtomIdList param error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            if (keywordIdAndAtomIdList.size() > keywordNumMax) {
                log.warn("getKeywordLiteral keywordIdAndAtomIdList param too long");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            List<Long> keywordIdsForSearch = new ArrayList<Long>();
            Map<Long, Long> keywordid_wordid_map = new HashMap<Long, Long>();
            for (Map<String, Long> item : keywordIdAndAtomIdList) {// 循环中只是过滤出来keyworid的列表，后面集中查询数据库
                if (item == null || item.isEmpty() || item.size() != 2) {
                    log.warn("getKeywordLiteral keywordIdAndAtomIdList param inner array index num wrong");
                    result.setStatus(Status.PARAM_ERROR);
                    return result;
                }

                Long keywordId = item.get(NameConstant.KEYWORDID_CONST);
                Long atomId = item.get(NameConstant.ATOMID_CONST);

                if (keywordId == null || keywordId <= 0) {
                    log.warn("getKeywordLiteral keywordIdAndAtomIdList param inner keywordid is invalid");
                    result.setStatus(Status.PARAM_ERROR);
                    return result;
                }

                if (atomId == null || atomId <= 0) {
                    log.warn("getKeywordLiteral keywordIdAndAtomIdList param inner atomid is invalid");
                    result.setStatus(Status.PARAM_ERROR);
                    return result;
                }
                keywordIdsForSearch.add(keywordId);
                keywordid_wordid_map.put(keywordId, atomId);
            }
            // 根据前面过滤出来的keyword，分别在八库上查询数据
            Set<Long> keywordIdsFound = new HashSet<Long>();
            for (int i = 0; i < MultiDataSourceDaoImpl.shardingNum; i++) {
                ThreadContext.putUserId(MultiDataSourceSupport.DB_INDEX[i]);
                List<CproKeyword> keywords_sharding = cproKeywordMgr.findByIds(keywordIdsForSearch);
                for (CproKeyword keyword : keywords_sharding) {
                    if (MultiDataSourceSupport.isUserIdInSharding(keyword.getUserId(), i)) {
                        KeywordInfo keywordInfo = new KeywordInfo();
                        Long atomId = keywordid_wordid_map.get(keyword.getId());
                        if (atomId == null || !atomId.equals(keyword.getWordId())) {
                            log.warn("getKeywordLiteral keywordIdAndAtomIdList keywordid and atomid not match for keywordid=["
                                    + keyword + "] and atomid=[" + keyword.getWordId() + "]");
                            result.setStatus(Status.PARAM_ERROR);
                            return result;
                        }
                        keywordInfo.setLiteral(keyword.getKeyword());
                        keywordInfo.setIsDeleted(NameConstant.KEYWORD_NOT_DELETED);
                        keywordid2Name.put(keyword.getWordId(), keywordInfo);
                        keywordIdsFound.add(keyword.getId());
                    }
                }
            }
            for (Long keywordid : keywordIdsForSearch) {
                if (!keywordIdsFound.contains(keywordid)) {
                    deleteWordIds.add(keywordid_wordid_map.get(keywordid));
                    atomId2keywordIdMap.put(keywordid_wordid_map.get(keywordid), keywordid);
                }
            }
            // 存在在北斗中找不到到keywordid，直接去atom查找字面值，并标记keywordid为删除状态
            if (deleteWordIds.size() != 0) {
                Map<Long, String> wordResult = AtomUtils.getWordById(deleteWordIds);
                if (wordResult.keySet().size() != atomId2keywordIdMap.size()) {
                    log.warn("getKeywordLiteral atom word size = [" + wordResult.keySet().size()
                            + "] and atom2keyword size = [ " + atomId2keywordIdMap.size()
                            + " ] not match, atom server maybe got error");
                    result.setStatus(Status.PARAM_ERROR);
                    return result;
                }
                if (wordResult != null && wordResult.keySet().size() > 0) {
                    log.info("find " + wordResult.keySet().size() + " deleted words");
                    Iterator<Map.Entry<Long, String>> iter = wordResult.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<Long, String> entry = iter.next();
                        Long atomId = entry.getKey();
                        KeywordInfo keywordInfo = new KeywordInfo();
                        keywordInfo.setLiteral(entry.getValue());
                        keywordInfo.setIsDeleted(NameConstant.KEYWORD_DELETED);
                        Long keywordId = atomId2keywordIdMap.get(atomId);
                        if (keywordId == null) {
                            log.warn("keywordId not found from atom id = [" + entry.getKey() + "]");
                            continue;
                        }
                        keywordid2Name.put(atomId, keywordInfo);
                    }
                }

            }

            result.setStatus(Status.SUCCESS);
            result.setKeywordid2Name(keywordid2Name);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }

    }

    /**
     * 
     * 获取关键词字面
     * 
     * @param userId
     * @param wordIdList
     * @return KeywordResult
     */
    public KeywordResult getKeywordLiteral2(Integer userId, Integer groupId, List<Long> wordIdList) {
        KeywordResult result = new KeywordResult();
        try {
            Map<Long, KeywordInfo> keywordid2Name = new HashMap<Long, KeywordInfo>();

            if (userId == null || userId < 1) {
                log.warn("getKeywordLiteral2 userId param error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            if (wordIdList == null || wordIdList.size() == 0) {
                log.warn("getKeywordLiteral2 userIdAndWordIdList param error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            if (wordIdList.size() > keywordNumMax) {
                log.warn("getKeywordLiteral2 userIdAndWordIdList param too long");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            ThreadContext.putUserId(userId);
            List<KeywordInfoLite> keywordsInDb = cproKeywordMgr.findKeywordLiteByWordIds(wordIdList, groupId, userId);
            for (KeywordInfoLite keywordInfoLite : keywordsInDb) {
                KeywordInfo keywordInfo = new KeywordInfo();
                keywordInfo.setLiteral(keywordInfoLite.getKeyword());
                keywordInfo.setIsDeleted(NameConstant.KEYWORD_NOT_DELETED);
                keywordid2Name.put(keywordInfoLite.getWordId(), keywordInfo);
            }

            Set<Long> wordIdsInDbSet = new HashSet<Long>();
            for (KeywordInfoLite keywordInfoLite : keywordsInDb) {
                wordIdsInDbSet.add(keywordInfoLite.getWordId());
            }

            Set<Long> toQueryWordIdSet = new HashSet<Long>(wordIdList);
            toQueryWordIdSet.removeAll(wordIdsInDbSet);

            // removeAll后剩余的未在DB中查询到的WORD ID集合
            if (toQueryWordIdSet != null) {
                Set<Long> toQueryWordIdSetFromAtom = new HashSet<Long>();
                for (Long notQueriedFromDbWordId : toQueryWordIdSet) {
                    toQueryWordIdSetFromAtom.add(notQueriedFromDbWordId);
                }
                Map<Long, String> wordResult = AtomUtils.getWordById(toQueryWordIdSetFromAtom);
                if (wordResult != null && wordResult.keySet().size() > 0) {
                    log.info("Find " + wordResult.keySet().size() + " deleted words from atom");
                    Iterator<Map.Entry<Long, String>> iter = wordResult.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<Long, String> entry = iter.next();
                        Long atomId = entry.getKey();
                        KeywordInfo keywordInfo = new KeywordInfo();
                        keywordInfo.setLiteral(entry.getValue());
                        keywordInfo.setIsDeleted(NameConstant.KEYWORD_DELETED);
                        keywordid2Name.put(atomId, keywordInfo);
                    }
                }
            }

            result.setStatus(Status.SUCCESS);
            result.setKeywordid2Name(keywordid2Name);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }

    }

    /**
     * added by lvzichan，2013.07.17 根据userId和unitId查询创意
     * 
     * @param userId
     * @param unitId 创意id
     * @return UnitResultOne
     */
    public UnitResultOne getOneUnitMaterialbyId(Integer userId, Long unitId) {
        UnitResultOne result = new UnitResultOne();

        try {
            // 1.检查参数合法性
            if (userId == null || userId <= 0) {
                log.warn("getOneUnitMaterialbyId userId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }
            if (unitId == null || unitId <= 0) {
                log.warn("getOneUnitMaterialbyId unitId error");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            // 2.查找，并判断查询结果是否有效
            UnitInfoView srcUnitInfoView = cproUnitMgr.findUnitById(userId, unitId);

            if (srcUnitInfoView == null) {
                log.warn("getOneUnitMaterialbyId unit null");
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }
            if (!srcUnitInfoView.getUserid().equals(userId)) {
                log.warn("getOneUnitMaterialbyId user:" + userId + ",not allow unitId: " + unitId);
                result.setStatus(Status.PARAM_ERROR);
                return result;
            }

            // 3.正常情况下，设置查询结果
            UnitInfo unitInfo = new UnitInfo();
            int type = srcUnitInfoView.getWuliaoType();
            if (type == CproUnitConstant.MATERIAL_TYPE_LITERAL) { // 文字
                unitInfo.setDesc1(srcUnitInfoView.getDescription1());
                unitInfo.setDesc2(srcUnitInfoView.getDescription2());
            } else if (type == CproUnitConstant.MATERIAL_TYPE_PICTURE || type == CproUnitConstant.MATERIAL_TYPE_FLASH) { // 图片，flash
                unitInfo.setUrl(srcUnitInfoView.getMaterUrl());
                unitInfo.setWidth(srcUnitInfoView.getWidth());
                unitInfo.setHeight(srcUnitInfoView.getHeight());
            } else if (type == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {// 图文混排
                unitInfo.setUrl(srcUnitInfoView.getMaterUrl());
                unitInfo.setWidth(srcUnitInfoView.getWidth());
                unitInfo.setHeight(srcUnitInfoView.getHeight());
                unitInfo.setDesc1(srcUnitInfoView.getDescription1());
                unitInfo.setDesc2(srcUnitInfoView.getDescription2());
            }

            unitInfo.setType(type);
            unitInfo.setTitle(srcUnitInfoView.getTitle());
            unitInfo.setShowUrl(srcUnitInfoView.getShowUrl());
            unitInfo.setTargetUrl(srcUnitInfoView.getTargetUrl());

            result.setStatus(Status.SUCCESS);
            result.setUnitid2UnitInfo(unitInfo);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(Status.ERROR);
            return result;
        }
    }

    public CproPlanMgr getCproPlanMgr() {
        return cproPlanMgr;
    }

    public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
        this.cproPlanMgr = cproPlanMgr;
    }

    public UserMgr getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserMgr userMgr) {
        this.userMgr = userMgr;
    }

    public Integer getPlanNumMax() {
        return planNumMax;
    }

    public void setPlanNumMax(Integer planNumMax) {
        this.planNumMax = planNumMax;
    }

    public Integer getGroupNumMax() {
        return groupNumMax;
    }

    public void setGroupNumMax(Integer groupNumMax) {
        this.groupNumMax = groupNumMax;
    }

    public Integer getUnitNumMax() {
        return unitNumMax;
    }

    public void setUnitNumMax(Integer unitNumMax) {
        this.unitNumMax = unitNumMax;
    }

    public Integer getSiteNumMax() {
        return siteNumMax;
    }

    public void setSiteNumMax(Integer siteNumMax) {
        this.siteNumMax = siteNumMax;
    }

    public CproKeywordMgr getCproKeywordMgr() {
        return cproKeywordMgr;
    }

    public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
        this.cproKeywordMgr = cproKeywordMgr;
    }

    public Integer getKeywordNumMax() {
        return keywordNumMax;
    }

    public void setKeywordNumMax(Integer keywordNumMax) {
        this.keywordNumMax = keywordNumMax;
    }

    public CproGroupMgr getCproGroupMgr() {
        return cproGroupMgr;
    }

    public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
        this.cproGroupMgr = cproGroupMgr;
    }

    public CproUnitMgr getCproUnitMgr() {
        return cproUnitMgr;
    }

    public void setCproUnitMgr(CproUnitMgr cproUnitMgr) {
        this.cproUnitMgr = cproUnitMgr;
    }

    public StatDAO2 getStatDAO() {
        return statDAO;
    }

    public void setStatDAO(StatDAO2 statDAO) {
        this.statDAO = statDAO;
    }
}
