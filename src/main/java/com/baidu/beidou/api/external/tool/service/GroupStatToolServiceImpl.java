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
import com.baidu.beidou.api.external.report.vo.GroupStatViewItem;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportBasicService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.GroupViewItem;

/**
 * 
 * ClassName: GroupStatToolServiceImpl Function: 推广组报告查询，以DB查询出到数据为基准
 *
 * @author caichao
 * @date 2013-09-12
 */
public class GroupStatToolServiceImpl extends StatToolServiceSupport implements
        GenericToolStatService<GroupStatViewItem> {

    private final static Log log = LogFactory.getLog(GroupStatToolServiceImpl.class);
    
    @Resource
    private ReportBasicService reportBasicService;

    protected FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd");

    public List<GroupStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        // 查询olap数据
        List<GroupViewItem> olapData = reportBasicService.queryGroupBasicReport(buildQueryParam(request));
        log.info("GroupStatToolServiceImpl ：request=[" + request + "] got " + olapData.size() + " result from olap");

        // 生成空数据
        List<GroupStatViewItem> result =
                getEmptyViewItem(request.getUserid(), request.getPlanIds(), request.getGroupIds(),
                        request.getStartDate(), request.getEndDate());
        log.info("GroupStatToolServiceImpl ：request=[" + request + "] contruct " + result.size() + " info data from db");

        // 合并从数据库中获取到数据与doris查询出来到数据
        int cnt = 0;
        begin: for (GroupStatViewItem item : result) {
            for (GroupViewItem res : olapData) {
                Date date = DateUtils.olapStrToDate(res.getShowDate());
                Integer planid = res.getPlanId();
                Integer groupid = res.getGroupId();
                // FROMDATE、PLAN、GROUP 三者一致，才进行合并
                if (date.equals(DateUtils.strToDate(item.getDay())) && item.getGroupid() == groupid
                        && item.getPlanid() == planid) {
                    item.fillStatRecord(res);
                    cnt++;
                    continue begin;
                }
            }
        }
        log.info("GroupStatToolServiceImpl ：request=[" + request + "] merge " + cnt + " item with olap and db");
        if (olapData.size() != cnt) {
            log.error("GroupStatToolServiceImpl : db info data and doris data are not completely match" + request);
        }

        return result;
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据的推广组信息
     * 
     * @param request 查询参数
     * @return List<GroupStatViewItem> 空绩效数据的推广计划组数据列表
     */
    public List<GroupStatViewItem> getEmptyViewItem(int userId, List<Integer> planIds, List<Integer> groupIds,
            Date from, Date to) throws Exception {
        List<GroupStatViewItem> result = new ArrayList<GroupStatViewItem>();

        // Step1.
        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            log.error("GroupStatToolServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        // Step2.
        Map<Integer, CproGroup> groupInfoData = findGroupInfo(userId, planIds, groupIds);
        if (CollectionUtils.isEmpty(groupIds)) {
            groupIds = new ArrayList<Integer>();
            groupIds.addAll(groupInfoData.keySet());
        }

        // Step3.
        Map<Integer, CproPlan> planInfoData = findPlanInfo(userId, groupInfoData);

        // Step4.
        Date start = (Date) (from.clone());
        for (; start.getTime() <= to.getTime(); start = DateUtils.getNextDay(start)) {
            for (Integer groupId : groupIds) {
                GroupStatViewItem stat = new GroupStatViewItem();
                // stat.fillZeroStat();
                stat.setDay(dateFormat.format(start));
                stat.setUserid(userId);
                stat.setUsername(username);

                if (groupInfoData.get(groupId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get group id=["
                            + groupId + "]");
                }
                Integer planId = groupInfoData.get(groupId).getPlanId();
                stat.setPlanid(planId);

                if (planInfoData.get(planId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get plan id=["
                            + planId + "]");
                }
                String planName = planInfoData.get(planId).getPlanName();
                stat.setPlanname(planName);

                stat.setGroupid(groupId);
                String groupName = groupInfoData.get(groupId).getGroupName();
                stat.setGroupname(groupName);

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
}
