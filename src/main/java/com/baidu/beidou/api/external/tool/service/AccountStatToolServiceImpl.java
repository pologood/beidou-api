package com.baidu.beidou.api.external.tool.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.vo.AccountStatViewItem;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportBasicService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.AccountViewItem;

/**
 * ClassName: AccountStatToolServiceImpl Function: 账户报告查询，以DB查询出到数据为基准
 * 
 * @author caichao
 * @date 2013-09-12
 */
public class AccountStatToolServiceImpl extends StatToolServiceSupport implements
        GenericToolStatService<AccountStatViewItem> {
    private static final Log log = LogFactory.getLog(AccountStatToolServiceImpl.class);

    @Resource
    private ReportBasicService reportBasicService;

    protected FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd");

    public List<AccountStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        // 查询olap数据
        List<AccountViewItem> olapData = reportBasicService.queryAccountBasicReport(buildQueryParam(request).setFrom(
                    DateUtils.getDateCeil(request.getStartDate()).getTime()).setTo(
                    DateUtils.getDateCeil(request.getEndDate()).getTime()));
        log.info("AccountStatToolServiceImpl ：request=[" + request + "] got " + olapData.size() + " result from olap");

        List<AccountStatViewItem> result = getEmptyViewItem(request.getUserid(), request.getStartDate(),
                request.getEndDate());
        log.info("AccountStatToolServiceImpl ：request=[" + request + "] contruct " + result.size()
                + " info data from db");

        int cnt = 0;
        begin: for (AccountStatViewItem item : result) {
            for (AccountViewItem res : olapData) {
                Date date = DateUtils.olapStrToDate(res.getShowDate());
                // FROMDATE一致，才进行合并
                if (item.getDay().equals(dateFormat.format(date))) {
                    item.fillStatRecord(res);
                    cnt++;
                    continue begin;
                }
            }
        }
        log.info("AccountStatToolServiceImpl ：request=[" + request + "] merge " + cnt + " item with olap and db");
        if (olapData.size() != cnt) {
            log.error("AccountStatToolServiceImpl : db info data and doris data are not completely match" + request);
        }

        return result;
    }

    /**
     * 查询用户下所有有效计划的预算总额
     * 
     * @param request请求参数
     * @return 预算总额
     */
    public int querySumBudget(ApiReportQueryParameter request) {
        return this.getTotalBudget(request.getUserid());
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据的账户信息
     * 
     * @param request
     *            查询参数
     * @return List<AccountStatViewItem> 空绩效数据的账户报告数据列表
     */
    public List<AccountStatViewItem> getEmptyViewItem(int userId, Date from, Date to) throws Exception {
        List<AccountStatViewItem> result = new ArrayList<AccountStatViewItem>();

        Map<Integer, User> infoData = findAccountInfo(userId);
        if (infoData.get(userId) == null) {
            log.error("AccountStatToolServiceImpl: cannot get userId=[" + userId + "]");
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
        ThreadContext.putUserId(19);
        AccountStatToolServiceImpl service;
        String beanName = "accountStatToolService";
        service = (AccountStatToolServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20120607");
        Date endDate = DateUtils.strToDate("20120609");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        int result1 = service.querySumBudget(request);
        List<AccountStatViewItem> result = service.queryStat(request);
        System.out.println(result1);
        for (AccountStatViewItem item : result) {
            System.out.println(item);
        }
        // Long l = 1339084800*1000l;
        // System.out.println(l);
        // Date f = new Date(l);
        // DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // System.out.println(format.format(f));

    }
}
