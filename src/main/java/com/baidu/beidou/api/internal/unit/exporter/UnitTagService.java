/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.unit.exporter;

import java.util.List;

import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.cprounit.vo.UnitTagInfo;

/**
 * 创意标签API接口
 * 
 * @author Wang Yu
 * 
 */
public interface UnitTagService {
    /**
     * 获取创意标签
     * 
     * @param userId 用户ID
     * @param unitIds 创意ID列表
     * @return 创意标注信息列表
     */
    public AuditResult<UnitTagInfo> getUnitTag(Integer userId, List<Long> unitIds);

    /**
     * 更新创意标签
     * 
     * @param unitTagInfoList 创意标注信息列表
     * @return 更新状态,返回更新失败的unitid
     */
    public AuditResult<Long> updateUnitTag(List<UnitTagInfo> unitTagInfoList);
}
