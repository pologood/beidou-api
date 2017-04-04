package com.baidu.beidou.api.internal.unit.facade.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.baidu.beidou.api.internal.unit.facade.UnitTagFacade;
import com.baidu.beidou.cprounit.service.UnitTagMgr;
import com.baidu.beidou.cprounit.vo.UnitTagInfo;
import com.baidu.beidou.multidatabase.datasource.MultiDataSourceSupport;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.unbiz.common.CollectionUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

public class UnitTagFacadeImpl implements UnitTagFacade {
    private UnitTagMgr unitTagMgr;
    
    @Override
    public List<UnitTagInfo> getUnitTags(int userId, List<Long> unitIds) {
        return unitTagMgr.getUnitTags(userId, unitIds);
    }

    @Override
    public List<Long> updateUnitTags(List<UnitTagInfo> unitTagInfoList) {
        if (CollectionUtil.isEmpty(unitTagInfoList)) {
            return Collections.emptyList();
        }

        // 初始化更新失败创意Ids
        List<Long> result = Lists.newArrayListWithExpectedSize(unitTagInfoList.size() / 5);

        // 创意分库处理
        ArrayListMultimap<Integer, UnitTagInfo> dbShardUnitIdMap = shardUnitIdByDb(unitTagInfoList);

        Set<Integer> userIdKeys = dbShardUnitIdMap.keySet();
        for (Integer userIdKey : userIdKeys) {
            ThreadContext.putUserId(userIdKey);

            List<Long> thisShardErrors = null;
            List<UnitTagInfo> thisShardUnitTags = dbShardUnitIdMap.get(userIdKey);
            try {
                thisShardErrors = unitTagMgr.updateUnitTags(thisShardUnitTags);
            } catch (Exception e) {
                for (UnitTagInfo unitTagInfo : thisShardUnitTags) {
                    result.add(unitTagInfo.getUnitId()); // 抛异常，事务回滚，记录unitid
                }
            }
            if (CollectionUtil.isNotEmpty(thisShardErrors)) {
                result.addAll(thisShardErrors);
            }
        }

        return result;
    }

    /**
     * 根据userid将unitid做数据库分片处理
     * 
     * @param unitTagInfoList 创意标签列表
     * @return 每一个db分片对应的unitid集合
     */
    private ArrayListMultimap<Integer, UnitTagInfo> shardUnitIdByDb(List<UnitTagInfo> unitTagInfoList) {
        ArrayListMultimap<Integer, UnitTagInfo> result = ArrayListMultimap.create();
        for (UnitTagInfo unitTagInfo : unitTagInfoList) {
            int userIdKey = MultiDataSourceSupport.DB_INDEX[(unitTagInfo.getUserId() >>> 6) & 7];
            result.put(userIdKey, unitTagInfo);
        }
        return result;
    }

    public void setUnitTagMgr(UnitTagMgr unitTagMgr) {
        this.unitTagMgr = unitTagMgr;
    }
}
