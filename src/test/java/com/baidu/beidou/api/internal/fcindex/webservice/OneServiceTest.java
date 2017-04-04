/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.fcindex.webservice;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.fcindex.service.OneService;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.ReportHourViewItem;

/**
 * Created by hewei18 on 2015-12-30.
 */
public class OneServiceTest {

    @Test
    @Ignore
    public void testGetHourUserReport() {
        McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/HolmesPeopleService", "UTF-8",
                new ExceptionHandler());

        OneService exporter = ProxyFactory.createProxy(OneService.class, proxy);
        List<ReportHourViewItem> itemList = exporter.getHourUserReport(19, "2015-12-01", "2015-12-28");
        for (ReportHourViewItem item : itemList) {
            System.out.println(item);
        }

    }
}
