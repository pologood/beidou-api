package com.baidu.beidou.api.external.tool.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.PlanStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportBasicService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.PlanViewItem;

/**
 * 
 * ClassName: PlanStatToolServiceImpl Function: 推广计划报告查询，以DB查询出到数据为基准
 *
 * @author caichao
 * @date 2013-09-12
 */
public class PlanStatToolServiceImpl extends StatToolServiceSupport implements GenericToolStatService<PlanStatViewItem> {

    private final static Log log = LogFactory.getLog(PlanStatToolServiceImpl.class);
    
    @Resource
    private ReportBasicService reportBasicService;

    protected FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd");

    public List<PlanStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        // 查询olap数据
        List<PlanViewItem> olapData = reportBasicService.queryPlanBasicReport(buildQueryParam(request));
        log.info("PlanStatToolServiceImpl ：request=[" + request + "] got " + olapData.size() + " result from olap");

        List<PlanStatViewItem> result =
                getEmptyViewItem(request.getUserid(), request.getPlanIds(), request.getStartDate(),
                        request.getEndDate());
        log.info("PlanStatToolServiceImpl ：request=[" + request + "] contruct " + result.size() + " info data from db");

        int cnt = 0;
        begin: for (PlanStatViewItem item : result) {
            for (PlanViewItem res : olapData) {
                Date date = DateUtils.olapStrToDate(res.getShowDate());
                Integer planid = res.getPlanId();

                if (date.equals(DateUtils.strToDate(item.getDay())) && item.getPlanid() == planid) {
                    item.fillStatRecord(res);
                    cnt++;
                    continue begin;
                }
            }
        }
        log.info("PlanStatToolServiceImpl ：request=[" + request + "] merge " + cnt + " item with olap and db");
        if (olapData.size() != cnt) {
            log.error("PlanStatToolServiceImpl : db info data and doris data are not completely match" + request);
        }

        return result;
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据的推广计划信息
     * 
     * @param request 查询参数
     * @return List<PlanStatViewItem> 空绩效数据的推广计划报告数据列表
     */
    public List<PlanStatViewItem> getEmptyViewItem(int userId, List<Integer> planIds, Date from, Date to)
            throws Exception {
        List<PlanStatViewItem> result = new ArrayList<PlanStatViewItem>();

        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            log.error("PlanStatToolServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        Map<Integer, CproPlan> planInfoData = findPlanInfo(userId, planIds);
        if (CollectionUtils.isEmpty(planIds)) {
            planIds = new ArrayList<Integer>();
            planIds.addAll(planInfoData.keySet());
        }

        Date start = (Date) (from.clone());
        for (; start.getTime() <= to.getTime(); start = DateUtils.getNextDay(start)) {
            for (Integer planId : planIds) {
                PlanStatViewItem stat = new PlanStatViewItem();
                // stat.fillZeroStat();
                stat.setDay(dateFormat.format(start));
                stat.setUserid(userId);
                stat.setUsername(username);
                stat.setPlanid(planId);

                if (planInfoData.get(planId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get plan id=["
                            + planId + "]");
                }
                stat.setPlanname(planInfoData.get(planId).getPlanName());
                result.add(stat);
            }
        }

        return result;
    }

    /**
     * 查询有效计划预算总额
     * 
     * @param 请求参数
     * @return 总额
     */
    public int querySumBudget(ApiReportQueryParameter request) {
        return this.getTotalBudget(request.getUserid());
    }

    /**
     * main
     */
    public static void main(String[] args) throws Exception {

        ThreadContext.putUserId(19);
        PlanStatToolServiceImpl service;
        String beanName = "planStatToolService";
        service = (PlanStatToolServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20130606");
        Date endDate = DateUtils.strToDate("20130607");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStatRange(2);
        List<Integer> planids = new ArrayList<Integer>();
        // planids.add(50482);
        // planids.add(50530);
        // planids.add(1857880);
        // planids.add(380471);

        request.setPlanIds(planids);
        int result1 = service.querySumBudget(request);
        // List<AccountStatViewItem> result = service.queryStat(request);
        System.out.println(result1);
        // for(AccountStatViewItem item : result){
        // System.out.println(item);
        // }
    }
}
