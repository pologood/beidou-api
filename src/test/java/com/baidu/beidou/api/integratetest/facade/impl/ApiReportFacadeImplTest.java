package com.baidu.beidou.api.integratetest.facade.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.constant.ApiReportTaskConstant;
import com.baidu.beidou.api.external.report.facade.ApiReportFacade;
import com.baidu.beidou.api.external.report.service.ApiReportTaskMgr;
import com.baidu.beidou.api.external.report.util.JsonConverter;
import com.baidu.beidou.api.external.report.util.ReportIdGenerator;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.test.common.AbstractShardXdbTestCaseLegacy;
import com.baidu.beidou.util.DateUtils;

/**
 * 报告服务单测
 * 
 * @author work
 * 
 */
@Ignore
public class ApiReportFacadeImplTest extends AbstractShardXdbTestCaseLegacy {

    @Resource
    private ApiReportFacade apiReportFacade;

    @Resource
    private ApiReportTaskMgr apiReportTaskMgr;


    /**
     * testAddTask
     * 
     * @throws Exception Exception
     */
    @Test
    @Rollback(true)
    public void testAddTask() throws Exception {

        String hostname = InetAddress.getLocalHost().getHostName();
        int userid = 499;
        int opuser = 1;
        boolean idonly = false;
        String start = "20111231";
        String end = "20120104";
        int statRange = 2;
        int reportType = 2;
        int iszip = 0;
        int performanceData = (1 << 0);

        ApiReportTask task = new ApiReportTask();

        // 构造ApiReportQueryParameter
        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(757447);
        planIds.add(740918);
        ApiReportQueryParameter queryParam =
                getQueryParam(userid, start, end, planIds, null, null, null, null, reportType, statRange, idonly);
        String queryParamStr = JsonConverter.toJson(queryParam);
        task.setQueryparam(queryParamStr);
        task.setUserid(userid);
        task.setMachineid(hostname);
        task.setPerformancedata(performanceData);
        String reportid = ReportIdGenerator.get(queryParamStr);
        task.setReportid(reportid);
        task.setOpuser(opuser);
        task.setIszip(iszip);
        task.setStatus(ApiReportTaskConstant.TASK_STATUS_NEW);

        long id = apiReportTaskMgr.addTask(task);
        System.out.println("primary key=[" + id + "]");
        task.setId(id);
        assertThat(id, greaterThan(0L));
        Thread.sleep(2);

        apiReportFacade.addTask(task);

        // Thread.sleep(10000);
    }

    /**
     * .append("userid",userid) .append("startDate", startDate) .append("endDate", endDate) .append("planIds", planIds)
     * .append("groupIds", groupIds) .append("unitIds", unitIds) .append("qtKeywordIds", qtKeywordIds)
     * .append("ctKeywordIds", ctKeywordIds) .append("idOnly", idOnly)
     */
    protected ApiReportQueryParameter getQueryParam(int userid, String start, String end, List<Integer> planIds,
            List<Integer> groupIds, List<Long> unitIds, List<Integer> qtKeywordIds, List<Integer> ctKeywordIds,
            int reportType, int statRange, boolean idonly) throws Exception {
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate(start);
        Date endDate = DateUtils.strToDate(end);
        request.setUserid(userid);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setPlanIds(planIds);
        request.setGroupIds(groupIds);
        request.setUnitIds(unitIds);
        request.setReportType(reportType);
        request.setStatRange(statRange);
        request.setIdOnly(idonly);
        return request;
    }

}
