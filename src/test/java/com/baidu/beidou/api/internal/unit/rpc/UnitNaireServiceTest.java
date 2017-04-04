/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.unit.rpc;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.unit.exporter.UnitNaireService;
import com.baidu.beidou.api.internal.unit.vo.UnitResult;
import com.baidu.beidou.cprounit.vo.NaireAnswer;
import com.baidu.beidou.cprounit.vo.UnitNaireConfVo;
import com.baidu.beidou.cprounit.vo.UnitNaireKey;
import com.baidu.beidou.cprounit.vo.UnitNaireVo;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
import com.google.common.collect.Lists;

import junit.framework.Assert;

/**
 * Created by hewei18 on 2016-02-22.
 */
@Ignore
public class UnitNaireServiceTest {
    McpackRpcProxy proxy =
            new McpackRpcProxy("http://localhost:8080/api/UnitNaireService", "UTF-8",
                    new ExceptionHandler());

    UnitNaireService exporter = ProxyFactory.createProxy(UnitNaireService.class, proxy);

    @Test
    public void testGetUnitNaireConf() {
        UnitResult<UnitNaireConfVo> result = exporter.getUnitNaireConf();
        for (UnitNaireConfVo vo : result.getData()) {
            System.out.println(vo);
        }
    }

    @Test
    public void testGetUnitNaire() {
        UnitResult<UnitNaireVo> result = exporter.getUnitNaireResult(19, 98L, 98L, 1);
        System.out.println(result.getData().get(0));
        result = exporter.getUnitNaireResult(19, 97L, 98L, 1);
        System.out.println(result.getData().get(0));
    }

    @Test
    public void testBatchGetUnitNaires() {
        UnitResult<UnitNaireVo> result = exporter.batchGetUnitNaires(19,
                Lists.newArrayList(new UnitNaireKey( 206599400L, 222362436026L, 1),
                        new UnitNaireKey(206599392L, 222362436023L, 1),
                        new UnitNaireKey(206599368L, 222362436020L, 1))); // not exist
        for (UnitNaireVo vo : result.getData()) {
            System.out.println(vo);
        }
        Assert.assertEquals(2, result.getData().size());
    }

    @Test
    public void testGetUnitNaireOfUser() {
        UnitResult<UnitNaireVo> result = exporter.getUnitNaireOfUser(19, 0, 11);
        for (UnitNaireVo v : result.getData()) {
            System.out.println(v);
        }
        Assert.assertEquals(11, result.getData().size());
    }

    @Test
    public void testBatchAuditUnitNairs() {
        List<NaireAnswer> answerList = Lists.newArrayList();
        NaireAnswer a1 = new NaireAnswer();
        a1.setId(1);
        a1.setAnswerValue(1);
        a1.setComment("有少儿不宜");
        answerList.add(a1);
        NaireAnswer a2 = new NaireAnswer();
        a2.setId(2);
        a2.setAnswerValue(1);
        a2.setComment("填写错误");
        answerList.add(a2);
        NaireAnswer a3 = new NaireAnswer();
        a2.setId(3);
        a2.setAnswerValue(1);
        a2.setComment("必要提示");
        answerList.add(a3);
        UnitNaireVo v1 = new UnitNaireVo(19, 206599400L, 222362436026L, 1);
        v1.setAuditAnswers(answerList);
        UnitNaireVo v2 = new UnitNaireVo(19, 206599392L, 222362436023L, 1);
        v2.setAuditAnswers(answerList);
        UnitNaireVo v3 = new UnitNaireVo(19, 206599384L, 222362436020L, 1);
        v3.setAuditAnswers(answerList);
        UnitNaireVo v4 = new UnitNaireVo(19, 206599368L, 222362436020L, 1);
        v4.setAuditAnswers(answerList);
        List<UnitNaireVo> auditNaires =
                Lists.newArrayList(v1, v2, v3, v4);
        UnitResult<Long> failedIds =  exporter.batchAuditUnitNaires(auditNaires);
        Assert.assertEquals(1, failedIds.getData().size());
        Assert.assertEquals(206599368L, failedIds.getData().get(0).longValue());
    }

    @Test
    public void testAuditUnitNaire() {
        UnitNaireVo audit = new UnitNaireVo();
        audit.setUserId(19);
        audit.setUnitId(98L);
        audit.setMcId(98L);
        audit.setMcVersionId(1);

        List<NaireAnswer> answerList = Lists.newArrayList();
        NaireAnswer a1 = new NaireAnswer();
        a1.setId(1);
        a1.setAnswerValue(1);
        a1.setComment("有少儿不宜");
        answerList.add(a1);
        NaireAnswer a2 = new NaireAnswer();
        a2.setId(2);
        a2.setAnswerValue(1);
        a2.setComment("填写错误");
        answerList.add(a2);
        audit.setAuditAnswers(answerList);
        UnitResult<UnitNaireVo> result = exporter.auditUnitNaire(audit);
        System.out.println(result.getMsg());
        System.out.println(result.getData().get(0));
    }

}
