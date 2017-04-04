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
import com.baidu.beidou.api.external.report.vo.UnitStatViewItem;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportBasicService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.UnitViewItem;

/**
 * 
 * ClassName: UnitStatToolServiceImpl Function: 推广创意报告查询，以DB查询出到数据为基准
 *
 * @author caichao
 * @date 2013-09-12
 */
public class UnitStatToolServiceImpl extends StatToolServiceSupport implements GenericToolStatService<UnitStatViewItem> {

    private final static Log log = LogFactory.getLog(UnitStatToolServiceImpl.class);
    
    @Resource
    private ReportBasicService reportBasicService;
    
    private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd");

    public List<UnitStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        // 查询olap数据
        List<UnitViewItem> olapData = reportBasicService.queryUnitBasicReport(buildQueryParam(request));
        log.info("UnitStatToolServiceImpl ：request=[" + request + "] got " + olapData.size() + " result from olap");

        // 如果doris查询成功，生成空数据
        List<UnitStatViewItem> result =
                getEmptyViewItem(request.getUserid(), request.getPlanIds(), request.getGroupIds(),
                        request.getUnitIds(), request.getStartDate(), request.getEndDate());
        log.info("UnitStatToolServiceImpl ：request=[" + request + "] contruct " + result.size() + " info data from db");

        // 合并从数据库中获取到数据与doris查询出来到数据
        int cnt = 0;
        begin: for (UnitStatViewItem item : result) {
            for (UnitViewItem res : olapData) {
                Date date = DateUtils.olapStrToDate(res.getShowDate());
                Integer planid = res.getPlanId();
                Integer groupid = res.getGroupId();
                Long unitid = res.getUnitId();
                // FROMDATE、PLAN、GROUP、UNIT 四者一致，才进行合并
                if (date.equals(DateUtils.strToDate(item.getDay())) && item.getGroupid() == groupid
                        && item.getPlanid() == planid && Long.valueOf(item.getUnitid()).equals(unitid)) {
                    item.fillStatRecord(res);
                    cnt++;
                    continue begin;
                }
            }
        }
        log.info("UnitStatServiceImpl ：request=[" + request + "] merge " + cnt + " item with olap and db");

        return result;
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据创意信息
     * 
     * @param request 查询参数
     * @return List<UnitStatViewItem> 空绩效数据的创意报告数据列表
     */
    public List<UnitStatViewItem> getEmptyViewItem(int userId, List<Integer> planIds, List<Integer> groupIds,
            List<Long> unitIds, Date from, Date to) throws Exception {
        List<UnitStatViewItem> result = new ArrayList<UnitStatViewItem>();

        // Step1.
        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            log.error("UnitStatToolServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        // Step2.
        Map<Long, UnitInfoView> unitInfoData = findUnitInfo(userId, planIds, groupIds, unitIds);
        if (CollectionUtils.isEmpty(unitIds)) {
            unitIds = new ArrayList<Long>();
            unitIds.addAll(unitInfoData.keySet());
        }

        // Step3.
        Map<Integer, CproGroup> groupInfoData = findGroupInfo(userId, unitInfoData);
        Map<Integer, CproPlan> planInfoData = findPlanInfo(userId, groupInfoData);

        // Step4.
        Date start = (Date) (from.clone());
        for (; start.getTime() <= to.getTime(); start = DateUtils.getNextDay(start)) {
            for (Long unitId : unitIds) {
                UnitStatViewItem stat = new UnitStatViewItem();
                // stat.fillZeroStat();
                stat.setDay(dateFormat.format(start));
                stat.setUserid(userId);
                stat.setUsername(username);

                UnitInfoView dbVo = unitInfoData.get(unitId);
                if (dbVo == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get unit id=["
                            + unitId + "]");
                }

                Integer planId = Long.valueOf(dbVo.getPlanid()).intValue();
                stat.setPlanid(planId);
                if (planInfoData.get(planId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get plan id=["
                            + planId + "]");
                }
                String planName = planInfoData.get(planId).getPlanName();
                stat.setPlanname(planName);

                Integer groupId = Long.valueOf(dbVo.getGroupid()).intValue();
                stat.setGroupid(groupId);
                if (groupInfoData.get(groupId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get group id=["
                            + groupId + "]");
                }
                String groupName = groupInfoData.get(groupId).getGroupName();
                stat.setGroupname(groupName);

                stat.setUnitid(unitId);
                String title = dbVo.getTitle();
                stat.setTitle(title);
                int wuliaotype = dbVo.getWuliaoType();
                stat.setWuliaotype(wuliaotype);

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
