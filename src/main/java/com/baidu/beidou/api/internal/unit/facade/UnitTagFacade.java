package com.baidu.beidou.api.internal.unit.facade;

import java.util.List;

import com.baidu.beidou.cprounit.vo.UnitTagInfo;

/**
 * 创意标注Facade层接口
 * 
 * @author Wang Yu
 * 
 */
public interface UnitTagFacade {

    /**
     * 获取创意标签信息
     * 
     * @param userId 用户ID
     * @param unitIds 创意ID
     * @return 创意标签信息
     */
    public List<UnitTagInfo> getUnitTags(int userId, List<Long> unitIds);

    /**
     * 更新创意标签
     * 
     * @param unitTagInfoList 创意标注信息列表
     * @return 更新失败的unitid列表
     */
    public List<Long> updateUnitTags(List<UnitTagInfo> unitTagInfoList);
}
