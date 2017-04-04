package com.baidu.beidou.api.external.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.AccountStatViewItem;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.AccountViewItem;

/**
 * 
 * ClassName: AccountStatServiceImpl Function: 账户报告查询，以DB查询出到数据为基准
 * 
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 30, 2011
 */
public class AccountStatServiceImpl extends GenericStatServiceImpl<AccountStatViewItem> implements
        GenericStatService<AccountStatViewItem> {

    @Resource
    private ReportLiteService reportLiteService;

    /**
     * 获取账户报告数据
     * 
     * @param request 查询参数
     * @return List<AccountStatViewItem> 账户报告数据列表
     */
    public List<AccountStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<AccountViewItem> allData = reportLiteService.queryAccountLiteReport(buildQueryParam(request));
        logger.info("AccountStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());

        List<AccountStatViewItem> result = getEmptyViewItem();
        logger.info("AccountStatServiceImpl ：request=[{}] contruct {} info data from db", request, result.size());

        int cnt = 0;
        begin: for (AccountStatViewItem item : result) {
            for (AccountViewItem userAccountViewItem : allData) {
                Date date = DateUtils.olapStrToDate(userAccountViewItem.getShowDate());
                // FROMDATE一致，才进行合并
                if (item.getDay().equals(dateFormat.format(date))) {
                    item.fillStatRecord(userAccountViewItem);
                    cnt++;
                    continue begin;
                }
            }
        }
        logger.info("AccountStatServiceImpl ：request=[{}] merge {} item with olpa and db", request, cnt);
        if (allData.size() != cnt) {
            logger.error("AccountStatServiceImpl : db info data and olap data are not completely match {}", request);
        }

        return result;
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据的账户信息
     * 
     * @param request 查询参数
     * @return List<AccountStatViewItem> 空绩效数据的账户报告数据列表
     */
    public List<AccountStatViewItem> getEmptyViewItem() throws Exception {
        List<AccountStatViewItem> result = new ArrayList<AccountStatViewItem>();

        Map<Integer, User> infoData = findAccountInfo(userId);
        if (infoData.get(userId) == null) {
            logger.error("AccountStatServiceImpl: cannot get userId=[{}]", userId);
            return result;
        }
        String username = infoData.get(userId).getUsername();

        Date start = (Date) (from.clone());
        for (; start.getTime() <= to.getTime(); start = DateUtils.getNextDay(start)) {
            AccountStatViewItem stat = new AccountStatViewItem();
            // stat.fillZeroStat();
            stat.setDay(dateFormat.format(start));
            stat.setUserid(userId);
            stat.setUsername(username);
            result.add(stat);
        }

        return result;
    }

    /**
     * main
     */
    public static void main(String[] args) throws Exception {

        AccountStatServiceImpl service;
        String beanName = "accountStatService";
        service = (AccountStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20131229");
        Date endDate = DateUtils.strToDate("20140103");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        List<AccountStatViewItem> result = service.queryStat(request);
        for (AccountStatViewItem item : result) {
            System.out.println(item);
        }

    }

}
