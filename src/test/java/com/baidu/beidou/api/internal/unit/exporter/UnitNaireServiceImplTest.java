/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.unit.exporter;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.unit.vo.UnitResult;
import com.baidu.beidou.cprounit.vo.UnitNaireConfVo;

/**
 * Created by hewei18 on 2016-02-22.
 */
public class UnitNaireServiceImplTest {

    @Resource
    private UnitNaireService unitNaireService;

    @Test
    @Ignore
    public void testGetUnitNaireConf() throws Exception {
        UnitResult<UnitNaireConfVo> result = unitNaireService.getUnitNaireConf();
        for (UnitNaireConfVo vo : result.getData()) {
            System.out.println(vo);
        }
    }

    public void testGetUnitNaireResult() throws Exception {

    }

    public void testAuditUnitNaire() throws Exception {

    }
}