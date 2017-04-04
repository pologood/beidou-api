package com.baidu.beidou.api.internal.dmp.exporter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.baidu.beidou.api.internal.dmp.exporter.DmpSupportService;
import com.baidu.beidou.api.internal.dmp.vo.DmpPlanResult;
import com.baidu.beidou.api.internal.dmp.vo.PlanType;
import com.baidu.beidou.api.internal.dmp.vo.VtCodeResult;
import com.baidu.beidou.cprogroup.bo.VtCode;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.service.VtCodeMgr;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * 支持dmp获取计划列表
 * 
 * @author caichao
 * 
 */
public class DmpSupportServiceImpl implements DmpSupportService {
    private CproPlanMgr cproPlanMgr;
    private VtPeopleMgr vtPeopleMgr;
    private VtCodeMgr vtCodeMgr;
    
    @Override
    public DmpPlanResult getPlanList(int userId) {
        DmpPlanResult result = new DmpPlanResult();
        List<CproPlan> planList = cproPlanMgr.findCproPlanByUserId(userId);

        if (CollectionUtils.isNotEmpty(planList)) {
            List<PlanType> plans = new ArrayList<PlanType>(planList.size());
            for (CproPlan plan : planList) {
                plans.add(new PlanType(plan.getPlanId(), plan.getPlanName(), plan.getPlanState(), plan.getAddTime()));
            }

            result.setData(plans);
        }
        return result;
    }
    
    @Override
    public List<String> getUserDomain(int userId) {
        List<String> userDomainList = vtPeopleMgr.getDomainByUserid(userId);
        if (CollectionUtil.isNotEmpty(userDomainList)) {
            return userDomainList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<VtCodeResult> getVtCodeList(int userId, int isAllSite) {
        List<VtCodeResult> result = new ArrayList<VtCodeResult>();

        
        if (CproGroupConstant.VT_CODE_ALL_SITE == isAllSite) {
            VtCode vtCode = vtCodeMgr.findAllSiteCodeByUserId(userId);
            if (vtCode != null) {
                result.add(getVtCodeResult(vtCode));
            }
        } else if (CproGroupConstant.VT_CODE_NON_ALL_SITE == isAllSite) {
            List<VtCode> vtCodes = vtCodeMgr.findNonAllSiteCodeByUserId(userId);
            if (CollectionUtil.isNotEmpty(vtCodes)) {
                result.addAll(getVtCodeResults(vtCodes));
            }
        } else {
            List<VtCode> vtCodes = vtCodeMgr.findByUserId(userId);
            if (CollectionUtil.isNotEmpty(vtCodes)) {
                result.addAll(getVtCodeResults(vtCodes));
            }
        }

        return result;
    }
    
    private List<VtCodeResult> getVtCodeResults(List<VtCode> vtCodes) {
        List<VtCodeResult> result = new ArrayList<VtCodeResult>();
        for (VtCode vtCode : vtCodes) {
            result.add(getVtCodeResult(vtCode));
        }
        return result;
    }

    private VtCodeResult getVtCodeResult(VtCode vtCode) {
        VtCodeResult result = new VtCodeResult();
        BeanUtils.copyProperties(vtCode, result);
        result.setIsAllSite(vtCode.getIs_all_site());
        return result;
    }

    public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
        this.cproPlanMgr = cproPlanMgr;
    }

    public void setVtPeopleMgr(VtPeopleMgr vtPeopleMgr) {
        this.vtPeopleMgr = vtPeopleMgr;
    }

    public void setVtCodeMgr(VtCodeMgr vtCodeMgr) {
        this.vtCodeMgr = vtCodeMgr;
    }
}
