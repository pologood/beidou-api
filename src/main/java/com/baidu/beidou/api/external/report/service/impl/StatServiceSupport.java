package com.baidu.beidou.api.external.report.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.facade.CproITFacade;
import com.baidu.beidou.cprogroup.facade.GroupPackFacade;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.SeniorUnitMgrFilter;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.ReflectionUtil;
import com.baidu.unbiz.common.StringUtil;
import com.baidu.unbiz.common.cache.FieldCache;
import com.baidu.unbiz.common.logger.CachedLogger;
import com.baidu.unbiz.olap.obj.BaseItem;

/**
 * 
 * ClassName: StatServiceSupport <br>
 * Function: 报告数据查询帮助类，主要注入各种依赖到mgr，以及提供非doris存储数据的信息
 * 
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 30, 2011
 */
public class StatServiceSupport extends CachedLogger {

    // 报表数据量
    protected Integer size = 100000;

    protected UserMgr userMgr;

    protected CproPlanMgr cproPlanMgr;

    protected CproGroupMgr cproGroupMgr;

    protected CproUnitMgr cproUnitMgr;

    protected CproKeywordMgr cproKeywordMgr;

    protected GroupPackFacade groupPackFacade;

    protected CproITFacade cproITFacade;

    /**
     * 获取用户信息
     * 
     * @param userId
     * @return Map<Integer,User> userid->用户信息字典
     */
    public Map<Integer, User> findAccountInfo(Integer _userId) {
        Map<Integer, User> result = new HashMap<Integer, User>();
        if (_userId <= 0) {
            logger.error("userId is null");
            return result;
        }
        User user = userMgr.findUserBySFid(_userId);
        if (user == null) {
            logger.error("cannot get user from shifen");
            return result;
        }
        result.put(_userId, user);
        return result;
    }

    /**
     * 获取推广计划信息
     * 
     * @param userId
     * @param planIds
     * @return Map<Integer,CproPlan> 推广计划id->推广计划信息字典
     */
    public Map<Integer, CproPlan> findPlanInfo(Integer _userId, List<Integer> _planIds) {
        Map<Integer, CproPlan> result = new HashMap<Integer, CproPlan>();

        List<CproPlan> planList = null;
        if (CollectionUtils.isEmpty(_planIds)) {
            planList = cproPlanMgr.findCproPlanByUserId(_userId);
        } else {
            planList = cproPlanMgr.findCproPlanByPlanIds(_planIds);
        }

        if (CollectionUtils.isEmpty(planList)) {
            logger.warn("PlanStatServiceImpl got empty plan from db, userId=[{}]", _userId);
            return result;
        }

        for (CproPlan cproPlan : planList) {
            if (cproPlan.getPlanId() != null) {
                result.put(cproPlan.getPlanId(), cproPlan);
            }
        }
        return result;
    }

    /**
     * 获取推广计划信息，根据指定推广组ids来获取
     * 
     * @param userId
     * @param croupInfoData 推广组id->推广组信息字典
     * @return Map<Integer,CproPlan> 推广计划id->推广计划信息字典
     */
    public Map<Integer, CproPlan> findPlanInfo(Integer _userId, Map<Integer, CproGroup> croupInfoData) {
        List<Integer> _planIds = new ArrayList<Integer>();
        for (CproGroup cprogroup : croupInfoData.values()) {
            _planIds.add(cprogroup.getPlanId());
        }
        return findPlanInfo(_userId, _planIds);
    }

    /**
     * 获取推广组信息
     * 
     * @param userId
     * @param planIds
     * @param groupIds
     * @return Map<Integer,CproGroup> 推广组id->推广组信息字典
     */
    public Map<Integer, CproGroup> findGroupInfo(Integer _userId, List<Integer> _planIds, List<Integer> _groupIds) {
        Map<Integer, CproGroup> result = new HashMap<Integer, CproGroup>();

        List<CproGroup> groupList = null;

        if (CollectionUtils.isEmpty(_groupIds)) {
            if (CollectionUtils.isEmpty(_planIds)) {
                _groupIds = cproGroupMgr.findGroupIdsByUserId(_userId);
            } else {
                _groupIds = cproGroupMgr.findGroupIdsByPlanIds(_planIds);
            }
        }
        if (CollectionUtils.isEmpty(_groupIds)) {
            logger.warn("got empty group ids from db");
            return result;
        }
        groupList = cproGroupMgr.findCproGroupByGroupIds(_groupIds);
        if (CollectionUtils.isEmpty(groupList)) {
            logger.error("got empty group from db");
            return result;
        }

        for (CproGroup cproGroup : groupList) {
            if (cproGroup.getGroupId() != null) {
                result.put(cproGroup.getGroupId(), cproGroup);
            }
        }
        return result;
    }

    /**
     * 获取各层级下groupId列表
     * 
     * @param _userId
     * @param _planIds
     * @param _groupIds
     * @return
     */
    public List<Integer> findGroupIds(Integer _userId, List<Integer> _planIds, List<Integer> _groupIds) {
        if (!CollectionUtils.isEmpty(_groupIds)) {
            return _groupIds;
        }
        if (CollectionUtils.isEmpty(_planIds)) {
            _groupIds = cproGroupMgr.findGroupIdsByUserId(_userId);
        } else {
            _groupIds = cproGroupMgr.findGroupIdsByPlanIds(_planIds);
        }
        if (CollectionUtils.isEmpty(_groupIds)) {
            // gogogo
            // logger.warn("got empty group ids from db,userId ["+_userId+"],planIds ["+_planIds+"],groupIds ["+_groupIds+"]",_userId,_planIds,_groupIds);
            logger.warn("got empty group ids from db,userId [{}],planIds [{}],groupIds [{}]", new Object[] { _userId,
                    _planIds, _groupIds });
        }
        return _groupIds;
    }

    /**
     * 获取推广组信息，根据指定创意ids来获取
     * 
     * @param userId
     * @param unitInfoData 创意id->创意信息字典
     * @return Map<Integer,CproGroup> 推广组id->推广组信息字典
     */
    public Map<Integer, CproGroup> findGroupInfo(Integer _userId, Map<Long, UnitInfoView> unitInfoData) {
        List<Integer> _groupIds = new ArrayList<Integer>();
        for (UnitInfoView unitInfoView : unitInfoData.values()) {
            _groupIds.add(Long.valueOf(unitInfoView.getGroupid()).intValue());
        }
        return findGroupInfo(_userId, null, _groupIds);
    }

    /**
     * 获取创意信息
     * 
     * @param userId
     * @param planIds
     * @param groupIds
     * @param unitIds
     * @return Map<Integer,UnitInfoView> 创意id->创意信息字典
     */
    public Map<Long, UnitInfoView> findUnitInfo(Integer _userId, List<Integer> _planIds, List<Integer> _groupIds,
            List<Long> _unitIds) {
        Map<Long, UnitInfoView> result = new HashMap<Long, UnitInfoView>();

        SeniorUnitMgrFilter filter = new SeniorUnitMgrFilter();
        filter.setUserid(_userId);
        filter.setPlanId(_planIds);
        filter.setGroupId(_groupIds);
        filter.setUnitId(_unitIds);
        _unitIds = cproUnitMgr.findUnitIdWithDeleteUnit(0, -1, _userId, filter);

        List<UnitInfoView> unitList = cproUnitMgr.findUnitInfoViewsByIdsWithDelete(_userId, _unitIds);

        for (UnitInfoView unitInfoView : unitList) {
            if (unitInfoView.getUnitid() != null) {
                result.put(unitInfoView.getUnitid(), unitInfoView);
            }
        }
        return result;
    }

    public UserMgr getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserMgr userMgr) {
        this.userMgr = userMgr;
    }

    public CproPlanMgr getCproPlanMgr() {
        return cproPlanMgr;
    }

    public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
        this.cproPlanMgr = cproPlanMgr;
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

    public CproKeywordMgr getCproKeywordMgr() {
        return cproKeywordMgr;
    }

    public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
        this.cproKeywordMgr = cproKeywordMgr;
    }

    public GroupPackFacade getGroupPackFacade() {
        return groupPackFacade;
    }

    public void setGroupPackFacade(GroupPackFacade groupPackFacade) {
        this.groupPackFacade = groupPackFacade;
    }

    public CproITFacade getCproITFacade() {
        return cproITFacade;
    }

    public void setCproITFacade(CproITFacade cproITFacade) {
        this.cproITFacade = cproITFacade;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    protected static final FieldCache cache = FieldCache.getInstance();

    protected static final Set<String> dorisFields = CollectionUtil.createHashSet("srchuv", "clkuv", "transuv",
            "srsur", "cusur", "cocur", "holmesClks", "arrivalCnt", "effectArrCnt", "hopCnt", "arrivalRate", "hopRate",
            "resTime", "avgResTime", "resTimeStr", "directTrans", "indirectTrans");

    protected <T extends BaseItem> List<T> mergeList(List<T> mainList, List<T> subList, Set<String> mergeKeys,
            Class<? extends BaseItem> clazz) {
        if (CollectionUtils.isEmpty(mergeKeys) || clazz == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(mainList) && CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>();
        }
        if (CollectionUtils.isEmpty(mainList)) {
            return new ArrayList<T>(subList);
        } else if (CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>(mainList);
        }

        List<T> finalList = new ArrayList<T>(mainList);

        Map<String, T> itemMap = new HashMap<String, T>();
        // FIXME 恶心
        List<Field> fieldList = CollectionUtil.createArrayList(3);
        for (String key : mergeKeys) {
            Field field = ReflectionUtil.getField(clazz, StringUtil.toCamelCase(key));
            field.setAccessible(true);
            fieldList.add(field);
        }

        try {
            // 将次表处理成key为联合key，value为次表item的map，供join使用
            for (T item : subList) {
                String multiKey = "";
                for (Field field : fieldList) {
                    Object res = field.get(item);
                    multiKey = multiKey + "-" + (res == null ? "" : res.toString());
                }
                itemMap.put(multiKey, item);
            }

            // 向主表的item中填充此表的value字段
            for (T item : finalList) {
                String multiKey = "";
                for (Field field : fieldList) {
                    Object res = field.get(item);
                    multiKey = multiKey + "-" + (res == null ? "" : res.toString());
                }
                T subItem = itemMap.get(multiKey);
                if (subItem != null) {
                    for (String dorisField : dorisFields) {
                        Field field = ReflectionUtil.getField(subItem.getClass(), dorisField);

                        ReflectionUtil.writeField(field, item, ReflectionUtil.readField(dorisField, subItem));
                    }

                    itemMap.remove(multiKey);
                }
            }
            finalList.addAll(itemMap.values());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return finalList;
    }

    public static final Set<String> reverseStatMergeVals = CollectionUtil.createHashSet();

}
