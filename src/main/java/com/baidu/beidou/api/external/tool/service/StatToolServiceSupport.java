package com.baidu.beidou.api.external.tool.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.SeniorUnitMgrFilter;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.unbiz.soma.module.reportmodule.report.common.vo.QueryParam;
import com.baidu.unbiz.soma.module.reportmodule.report.constant.ReportConstants;
import com.google.common.base.Preconditions;

/**
 * 
 * ClassName: StatToolServiceSupport Function: 基础服务类
 *
 * @author caichao
 * @date 2013-09-12
 */
public class StatToolServiceSupport {
    private static final Log log = LogFactory.getLog(StatToolServiceSupport.class);
    protected UserMgr userMgr;

    protected CproPlanMgr cproPlanMgr;

    protected CproGroupMgr cproGroupMgr;

    protected CproUnitMgr cproUnitMgr;

    /**
     * 获取用户信息
     * 
     * @param userId
     * @return Map<Integer,User> userid->用户信息字典
     */
    public Map<Integer, User> findAccountInfo(Integer _userId) {
        Map<Integer, User> result = new HashMap<Integer, User>();
        if (_userId <= 0) {
            log.error("userId is null");
            return result;
        }
        User user = userMgr.findUserBySFid(_userId);
        if (user == null) {
            log.error("cannot get user from shifen");
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
            log.warn("PlanToolStatServiceImpl got empty plan from db, userId=[" + _userId + "]");
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
            log.warn("got empty group ids from db");
            return result;
        }
        groupList = cproGroupMgr.findCproGroupByGroupIds(_groupIds);
        if (CollectionUtils.isEmpty(groupList)) {
            log.error("got empty group from db");
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
            log.warn("got empty group ids from db,userId [" + _userId + "],planIds [" + _planIds + "],groupIds ["
                    + _groupIds + "]");
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
        _unitIds = cproUnitMgr.findUnitIdByFilter(0, -1, _userId, filter);

        List<UnitInfoView> unitList = cproUnitMgr.findUnitInfoViewsByIds(_userId, _unitIds);

        for (UnitInfoView unitInfoView : unitList) {
            if (unitInfoView.getUnitid() != null) {
                result.put(unitInfoView.getUnitid(), unitInfoView);
            }
        }
        return result;
    }

    /**
     * 根据userId获取账户有效计划预算总额
     * 
     * @param userId
     * @return
     */
    public Integer getTotalBudget(Integer userId) {
        return cproPlanMgr.findEffectivePlanBudgetByUserId(userId);
    }
    
    /**
     * 为实时报表服务 <code>ReportService<code>构造请求对象
     * {@link com.baidu.unbiz.soma.module.reportmodule.skeleton.vo.QueryParam QueryParam}
     * 
     * @param apiQueryParam
     * @return QueryParam
     */
    public static QueryParam buildQueryParam(ApiReportQueryParameter apiQueryParam) {
        Preconditions.checkNotNull(apiQueryParam);
  
        QueryParam result = QueryParam.newInstance();
        result.setFrom(apiQueryParam.getStartDate());
        result.setTo(apiQueryParam.getEndDate());
        result.setUserId(apiQueryParam.getUserid());
        result.setPlanIds(apiQueryParam.getPlanIds());
        result.setGroupIds(apiQueryParam.getGroupIds());
        result.setUnitIds(apiQueryParam.getUnitIds());
        result.setTimeUnit(ReportConstants.TU_DAY);

        log.info("QueryParam=" + result);
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

}
