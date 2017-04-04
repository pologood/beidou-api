/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.unit.exporter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.unit.constant.UnitConstant;
import com.baidu.beidou.api.internal.unit.exporter.UnitTagService;
import com.baidu.beidou.api.internal.unit.facade.UnitTagFacade;
import com.baidu.beidou.cprounit.vo.UnitTagInfo;
import com.baidu.unbiz.common.CollectionUtil;
import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;

/**
 * 创意标签API接口实现
 * 
 * @author Wang Yu
 * 
 */
public class UnitTagServiceImpl implements UnitTagService {
    private UnitTagFacade unitTagFacade;

    @Override
    public AuditResult<UnitTagInfo> getUnitTag(Integer userId, List<Long> unitIds) {
        AuditResult<UnitTagInfo> result = new AuditResult<UnitTagInfo>();
        if (userId == null || userId <= 0) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }

        if (CollectionUtil.isEmpty(unitIds) || unitIds.size() > UnitConstant.GET_UNIT_TAG_MAX) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }

        List<UnitTagInfo> unitTagList = unitTagFacade.getUnitTags(userId, unitIds);
        if (CollectionUtil.isEmpty(unitTagList)) {
            result.setStatus(ResponseStatus.ERROR);
            return result;
        }

        result.addResults(unitTagList);
        result.setTotalNum(unitTagList.size());
        result.setTotalPage(1);

        return result;
    }

    @Override
    public AuditResult<Long> updateUnitTag(List<UnitTagInfo> unitTagInfoList) {
        AuditResult<Long> result = new AuditResult<Long>();
        if (CollectionUtil.isEmpty(unitTagInfoList)) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }

        if (unitTagInfoList.size() > UnitConstant.UPDATE_UNIT_TAG_MAX) {
            List<Long> errorUnitIds = Lists.transform(unitTagInfoList, new Function<UnitTagInfo, Long>() {

                @Override
                public Long apply(UnitTagInfo unitTagInfo) {
                    return unitTagInfo.getUnitId();
                }

            });
            result.setStatus(ResponseStatus.PARAM_ERROR);
            result.setData(errorUnitIds);
            return result;
        }

        // 判断传入参数中是否有重复的unitId
        HashMultimap<Long, UnitTagInfo> multiMap = HashMultimap.create();
        for (UnitTagInfo unitTagInfo : unitTagInfoList) {
            multiMap.put(unitTagInfo.getUnitId(), unitTagInfo);
        }

        List<Long> errorUnitIds = new ArrayList<Long>();
        List<UnitTagInfo> updateUnitTagInfoList = new ArrayList<UnitTagInfo>(unitTagInfoList.size());
        Set<Long> keys = multiMap.keySet();
        for (long key : keys) {
            Set<UnitTagInfo> infos = multiMap.get(key);
            if (infos.size() > 1) {
                errorUnitIds.add(key);
            } else {
                updateUnitTagInfoList.addAll(infos);
            }
        }

        List<Long> errorUpdateUnitIds = unitTagFacade.updateUnitTags(updateUnitTagInfoList);
        if (CollectionUtil.isNotEmpty(errorUpdateUnitIds)) {
            errorUnitIds.addAll(errorUpdateUnitIds);
        }

        result.setData(errorUnitIds);

        return result;
    }

    public void setUnitTagFacade(UnitTagFacade unitTagFacade) {
        this.unitTagFacade = unitTagFacade;
    }
}