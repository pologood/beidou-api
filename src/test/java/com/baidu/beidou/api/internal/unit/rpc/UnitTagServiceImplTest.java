/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.unit.rpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.unit.exporter.UnitTagService;
import com.baidu.beidou.cprounit.vo.UnitTagInfo;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
import com.google.common.collect.Lists;

/**
 * 创意标签RPC UT
 * 
 * @author Wang Yu
 * 
 */
public class UnitTagServiceImplTest {

    @Test
    @Ignore
    public void testGetUnitTag() {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://127.0.0.1:8080/api/UnitTagService", "UTF-8", new ExceptionHandler());

        UnitTagService exporter = ProxyFactory.createProxy(UnitTagService.class, proxy);

        int userId = 499;
        List<Long> unitIds = Lists.newArrayList(84627328L, 84627336L);

        AuditResult<UnitTagInfo> result = exporter.getUnitTag(userId, unitIds);
        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            List<UnitTagInfo> list = result.getData();
            for (UnitTagInfo info : list) {
                System.out.println("-------" + info.getUnitId() + "--------");
                Map<Integer, Integer> unitTags = info.getUnitTags();
                for (Map.Entry<Integer, Integer> entry : unitTags.entrySet()) {
                    System.out.println(entry.getKey() + "=" + entry.getValue());
                }
            }
        }
    }

    @Test
    @Ignore
    public void testUpdateUnitTag() {
        McpackRpcProxy proxy =
                new McpackRpcProxy("http://127.0.0.1:8080/api/UnitTagService", "UTF-8", new ExceptionHandler());

        UnitTagService exporter = ProxyFactory.createProxy(UnitTagService.class, proxy);

        UnitTagInfo unit1 = new UnitTagInfo();
        unit1.setUnitId(84627328L);
        unit1.setUserId(499);
        unit1.setMcId(202360065734L);
        unit1.setMcVersionId(2);
        Map<Integer, Integer> unitTags1 = new HashMap<Integer, Integer>();
        unitTags1.put(1, 6);
        unitTags1.put(14, 3);
        unitTags1.put(22, 1);
        unitTags1.put(25, 0);
        unitTags1.put(59, 1);
        unitTags1.put(63, 2);
        unit1.setUnitTags(unitTags1);

        UnitTagInfo unit2 = new UnitTagInfo();
        unit2.setUnitId(84627336L);
        unit2.setUserId(499);
        unit2.setMcId(202360065749L);
        unit2.setMcVersionId(1);
        Map<Integer, Integer> unitTags2 = new HashMap<Integer, Integer>();
        unitTags2.put(2, 3);
        unitTags2.put(3, 8);
        unitTags2.put(64, 3);
        unit2.setUnitTags(unitTags2);
        
        UnitTagInfo unit3 = new UnitTagInfo();
        unit3.setUnitId(84627328L);
        unit3.setUserId(499);
        unit3.setMcId(202360065734L);
        unit3.setMcVersionId(2);
        Map<Integer, Integer> unitTags3 = new HashMap<Integer, Integer>();
        unitTags3.put(1, 6);
        unitTags3.put(14, 3);
        unitTags3.put(22, 1);
        unitTags3.put(25, 0);
        unitTags3.put(59, 1);
        unitTags3.put(63, 2);
        unit3.setUnitTags(unitTags3);

        List<UnitTagInfo> unitTagInfoList = Lists.newArrayList(unit1, unit2, unit3);
        AuditResult<Long> result = exporter.updateUnitTag(unitTagInfoList);
        if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
            System.out.println(result.getStatus());
        }
    }
}
