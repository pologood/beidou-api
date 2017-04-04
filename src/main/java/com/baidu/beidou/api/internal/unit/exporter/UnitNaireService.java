/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.unit.exporter;

import java.util.List;

import com.baidu.beidou.api.internal.unit.vo.UnitResult;
import com.baidu.beidou.cprounit.vo.UnitNaireConfVo;
import com.baidu.beidou.cprounit.vo.UnitNaireKey;
import com.baidu.beidou.cprounit.vo.UnitNaireVo;

/**
 * Created by hewei18 on 2016-02-18.
 */
public interface UnitNaireService {

    /**
     * 获取问卷配置
     * @return
     */
    UnitResult<UnitNaireConfVo> getUnitNaireConf();

    /**
     * 查询某个用户的问卷信息
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    UnitResult<UnitNaireVo> getUnitNaireOfUser(int userId, int offset, int limit);

    /**
     * 批量查询问卷创意
     * @param userId
     * @param unitNaireKeys
     * @return
     */
    UnitResult<UnitNaireVo> batchGetUnitNaires(int userId, List<UnitNaireKey> unitNaireKeys);

    /**
     * 通过创意id，物料id等获取单个问卷信息
     * @param userId
     * @param unitId
     * @param mcId
     * @param mcVersionId
     * @return
     */
    UnitResult<UnitNaireVo> getUnitNaireResult(int userId, long unitId, long mcId, int mcVersionId);

    /**
     * 批量审核创意问卷
     * @param auditNaires
     * @return
     */
    UnitResult<Long> batchAuditUnitNaires(List<UnitNaireVo> auditNaires);

    /**
     * 审核单个创意问卷
     * @param unitNaire
     * @return
     */
    UnitResult<UnitNaireVo> auditUnitNaire(UnitNaireVo unitNaire);
}
