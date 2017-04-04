/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.unit.exporter.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.unit.constant.UnitConstant;
import com.baidu.beidou.api.internal.unit.exporter.UnitNaireService;
import com.baidu.beidou.api.internal.unit.vo.UnitResult;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprounit.constant.UnitNaireConstant;
import com.baidu.beidou.cprounit.service.UnitNaireMgr;
import com.baidu.beidou.cprounit.vo.UnitNaireConfVo;
import com.baidu.beidou.cprounit.vo.UnitNaireKey;
import com.baidu.beidou.cprounit.vo.UnitNaireVo;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * Created by hewei18 on 2016-02-22.
 */
public class UnitNaireServiceImpl implements UnitNaireService {
    private UnitNaireMgr unitNaireMgr;

    @Override
    public UnitResult<UnitNaireConfVo> getUnitNaireConf() {
        UnitResult<UnitNaireConfVo> result = new UnitResult<UnitNaireConfVo>();
        List<UnitNaireConfVo> confList = UnionSiteCache.unitNaireConfCache.getUnitNaireConfList();
        result.setData(confList);
        result.setTotalNum(confList.size());
        return result;
    }

    @Override
    public UnitResult<UnitNaireVo> getUnitNaireOfUser(int userId, int offset, int limit) {
        UnitResult<UnitNaireVo> result = new UnitResult<UnitNaireVo>();
        if (limit > UnitConstant.GET_UNIT_NAIRE_MAX) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            result.setMsg("param error");
            return result;
        }
        ThreadContext.putUserId(userId);
        List<UnitNaireVo> unitNaireList = unitNaireMgr.getUnitNaireOfUser(userId,
                UnitNaireConstant.UnitNaireState.STATE_ANSWERED.getValue(), offset, limit);
        result.setData(unitNaireList);
        result.setTotalNum(unitNaireList.size());
        return result;
    }

    @Override
    public UnitResult<UnitNaireVo> batchGetUnitNaires(int userId, List<UnitNaireKey> unitNaireKeys) {
        UnitResult<UnitNaireVo> result = new UnitResult<UnitNaireVo>();
        if (CollectionUtil.isEmpty(unitNaireKeys) || unitNaireKeys.size() > UnitConstant.GET_UNIT_NAIRE_MAX) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            result.setMsg("param error");
            return result;
        }
        ThreadContext.putUserId(userId);
        List<UnitNaireVo> naireList = unitNaireMgr.batchGetUnitNaires(userId, unitNaireKeys);
        result.setData(naireList);
        result.setTotalNum(naireList.size());
        return result;
    }

    @Override
    public UnitResult<UnitNaireVo> getUnitNaireResult(int userId, long unitId, long mcId, int mcVersionId) {
        UnitResult<UnitNaireVo> result = new UnitResult<UnitNaireVo>();
        ThreadContext.putUserId(userId);
        UnitNaireVo naire = unitNaireMgr.getUnitNaire(unitId, mcId, mcVersionId);
        if (naire != null) {
            result.addResult(naire);
            result.setTotalNum(1);
        }
        return result;
    }

    public UnitResult<Long> batchAuditUnitNaires(List<UnitNaireVo> auditNaires) {
        UnitResult<Long> result = new UnitResult<Long>();
        if (CollectionUtils.isEmpty(auditNaires)) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            result.setMsg("param error");
            return result;
        }
        // 批量审核要求一次审核的都是同一用户的问卷
        ThreadContext.putUserId((auditNaires.get(0)).getUserId());
        List<Long> failedIds = unitNaireMgr.batchAuditUnitNaire(auditNaires);
        result.setData(failedIds);
        result.setTotalNum(failedIds.size());
        return result;
    }

    @Override
    public UnitResult<UnitNaireVo> auditUnitNaire(UnitNaireVo naire) {
        UnitResult<UnitNaireVo> result = new UnitResult<UnitNaireVo>();
        if (naire.getUnitId() == null || naire.getMcId() == null
                || naire.getMcVersionId() == null || naire.getUserId() == null) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            result.setMsg("param error");
            return result;
        }
        ThreadContext.putUserId(naire.getUserId());
        UnitNaireVo naireVo = unitNaireMgr.getUnitNaire(naire.getUnitId(), naire.getMcId(), naire.getMcVersionId());
        if (naireVo == null) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            result.setMsg("unit naire not exits");
            return result;
        }
        naireVo.setAuditAnswers(naire.getAuditAnswers());
        result.addResult(unitNaireMgr.auditUnitNaire(naireVo));
        result.setTotalNum(1);
        return result;
    }

    public void setUnitNaireMgr(UnitNaireMgr unitNaireMgr) {
        this.unitNaireMgr = unitNaireMgr;
    }
}
