package com.baidu.beidou.api.integratetest.service.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
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
import com.baidu.beidou.api.external.report.service.ApiReportTaskMgr;
import com.baidu.beidou.api.external.report.util.JsonConverter;
import com.baidu.beidou.api.external.report.util.ReportIdGenerator;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.util.DateUtils;

/**
 * 报告服务单测
 * 
 * @author work
 * 
 */
@Ignore
public class ApiReportTaskMgrImplTest extends StatServiceImplTestSupport {

    private static final String STARTDATE = "20110101";
    private static final String ENDDATE = "20110130";
    private static final String PREFIX = "primary key=[";
    private static final String SUF = "]";
    private static final String LOG_PREFIX = "query param =>a";
    @Resource
    private ApiReportTaskMgr apiReportTaskMgr;


    /**
     * testAddAndFindTask
     * 
     * @throws Exception Exception
     */
    @Test
    @Rollback(true)
    public void testAddAndFindTask() throws Exception {
        String hostname = InetAddress.getLocalHost().getHostName();
        int userid = 19;

        int opuser = 1;
        boolean idonly = true;
        String start = STARTDATE;
        String end = ENDDATE;
        int reportType = 1;
        int iszip = 0;
        int statRange = 1;
        int performancedata = 63;

        ApiReportTask task = new ApiReportTask();

        // 构造ApiReportQueryParameter
        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(1111);
        planIds.add(2222);
        planIds.add(3333);
        planIds.add(4444);
        ApiReportQueryParameter queryParam =
                getQueryParam(userid, start, end, planIds, null, null, null, reportType, idonly, statRange);
        String queryParamStr = JsonConverter.toJson(queryParam);
        task.setQueryparam(queryParamStr);
        task.setUserid(userid);
        task.setMachineid(hostname);
        String reportid = ReportIdGenerator.get(queryParamStr);
        task.setReportid(reportid);
        task.setOpuser(opuser);
        task.setIszip(iszip);
        task.setPerformancedata(performancedata);

        long id = apiReportTaskMgr.addTask(task);
        System.out.println(PREFIX + id + SUF);
        assertThat(id, greaterThan(0L));
        Thread.sleep(1000);

        ApiReportTask task2 = apiReportTaskMgr.findTasksById(id);
        assertThat(task2.getUserid(), is(userid));
        assertThat(task2.getMachineid(), is(hostname));
        assertThat(task2.getReportid(), is(reportid));
        assertThat(task2.getOpuser(), is(opuser));
        assertThat(task2.getIszip(), is(iszip));
        assertThat(task2.getPerformancedata(), is(performancedata));
        ApiReportQueryParameter queryParam2 = JsonConverter.fromJson(task2.getQueryparam());
        System.out.println(LOG_PREFIX + queryParam2);
        assertThat(queryParam2.getPlanIds(), is(planIds));
        assertThat(queryParam2.getStartDate(), is(DateUtils.strToDate(start)));
        assertThat(queryParam2.getEndDate(), is(DateUtils.strToDate(end)));
        assertThat(queryParam2.getUserid(), is(userid));
        assertThat(queryParam2.getReportType(), is(reportType));
        assertThat(queryParam2.getGroupIds(), nullValue());
        assertThat(queryParam.getStatRange(), is(statRange));
    }


    /**
     * testFindTasksByReportId
     * 
     * @throws Exception Exception
     */
    @Test
    public void testFindTasksByReportId() throws Exception {
        String hostname = InetAddress.getLocalHost().getHostName();
        int userid = 19;

        int opuser = 1;
        boolean idonly = true;
        String start = STARTDATE;
        String end = ENDDATE;
        int reportType = 1;
        int iszip = 0;
        int statRange = 1;
        int performancedata = 63;

        ApiReportTask task = new ApiReportTask();

        // 构造ApiReportQueryParameter
        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(1111);
        planIds.add(2222);
        planIds.add(3333);
        planIds.add(4444);
        ApiReportQueryParameter queryParam =
                getQueryParam(userid, start, end, planIds, null, null, null, reportType, idonly, statRange);
        String queryParamStr = JsonConverter.toJson(queryParam);
        task.setQueryparam(queryParamStr);
        task.setUserid(userid);
        task.setMachineid(hostname);
        String reportid = ReportIdGenerator.get(queryParamStr);
        task.setReportid(reportid);
        task.setOpuser(opuser);
        task.setIszip(iszip);
        task.setPerformancedata(performancedata);

        long id = apiReportTaskMgr.addTask(task);
        System.out.println(PREFIX + id + SUF);
        assertThat(id, greaterThan(0L));
        Thread.sleep(1000);

        ApiReportTask task2 = apiReportTaskMgr.findTasksByReportId(reportid);
        assertThat(task2.getUserid(), is(userid));
        assertThat(task2.getMachineid(), is(hostname));
        assertThat(task2.getReportid(), is(reportid));
        assertThat(task2.getOpuser(), is(opuser));
        assertThat(task2.getIszip(), is(iszip));
        ApiReportQueryParameter queryParam2 = JsonConverter.fromJson(task2.getQueryparam());
        System.out.println(LOG_PREFIX + queryParam2);
        assertThat(queryParam2.getPlanIds(), is(planIds));
        assertThat(queryParam2.getStartDate(), is(DateUtils.strToDate(start)));
        assertThat(queryParam2.getEndDate(), is(DateUtils.strToDate(end)));
        assertThat(queryParam2.getUserid(), is(userid));
        assertThat(queryParam2.getReportType(), is(reportType));
        assertThat(queryParam2.getGroupIds(), nullValue());
    }


    /**
     * testFindFailTasksByMachieid
     * 
     * @throws Exception Exception
     */
    @SuppressWarnings("unused")
    @Test
    public void testFindFailTasksByMachieid() throws Exception {
        String hostname = InetAddress.getLocalHost().getHostName();
        int userid = 19;

        int opuser = 1;
        boolean idonly = true;
        String start = STARTDATE;
        String end = ENDDATE;
        int reportType = 1;
        int iszip = 0;
        int statRange = 1;
        int performancedata = 63;

        ApiReportTask task = new ApiReportTask();

        // 构造ApiReportQueryParameter
        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(1111);
        planIds.add(2222);
        planIds.add(3333);
        planIds.add(4444);
        ApiReportQueryParameter queryParam =
                getQueryParam(userid, start, end, planIds, null, null, null, reportType, idonly, statRange);
        String queryParamStr = JsonConverter.toJson(queryParam);
        task.setQueryparam(queryParamStr);
        task.setUserid(userid);
        task.setMachineid(hostname);
        String reportid = ReportIdGenerator.get(queryParamStr);
        task.setReportid(reportid);
        task.setOpuser(opuser);
        task.setIszip(iszip);
        task.setPerformancedata(performancedata);

        long id = apiReportTaskMgr.addTask(task);
        System.out.println(PREFIX + id + SUF);
        assertThat(id, greaterThan(0L));
        Thread.sleep(10000);

        ApiReportTask task2 = apiReportTaskMgr.findTasksById(id);

        apiReportTaskMgr.updateTaskStatusFailIncrementRetry(task2);
        ApiReportTask task3 = apiReportTaskMgr.findTasksById(id);
        assertThat(task3.getStatus(), is(ApiReportTaskConstant.TASK_STATUS_FAIL_WITH_RETRY));
        Thread.sleep(5000);
        List<ApiReportTask> failList = apiReportTaskMgr.findFailTasksByMachieid(hostname);
        assertThat(task3.getStatus(), is(ApiReportTaskConstant.TASK_STATUS_FAIL_WITH_RETRY));
        assertThat(task3.getUserid(), is(userid));
        assertThat(task3.getMachineid(), is(hostname));
        assertThat(task3.getReportid(), is(reportid));
        assertThat(task3.getOpuser(), is(opuser));
        assertThat(task3.getIszip(), is(iszip));
        ApiReportQueryParameter queryParam2 = JsonConverter.fromJson(task2.getQueryparam());
        System.out.println(LOG_PREFIX + queryParam2);
        assertThat(queryParam2.getPlanIds(), is(planIds));
        assertThat(queryParam2.getStartDate(), is(DateUtils.strToDate(start)));
        assertThat(queryParam2.getEndDate(), is(DateUtils.strToDate(end)));
        assertThat(queryParam2.getUserid(), is(userid));
        assertThat(queryParam2.getReportType(), is(reportType));
        assertThat(queryParam2.getGroupIds(), nullValue());
    }


    /**
     * testUpdateTaskStatusDoing
     * 
     * @throws Exception Exception
     */
    @Test
    public void testUpdateTaskStatusDoing() throws Exception {
        String hostname = InetAddress.getLocalHost().getHostName();
        int userid = 19;

        int opuser = 1;
        boolean idonly = true;
        String start = STARTDATE;
        String end = ENDDATE;
        int reportType = 1;
        int iszip = 0;
        int statRange = 1;
        int performancedata = 63;

        ApiReportTask task = new ApiReportTask();

        // 构造ApiReportQueryParameter
        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(1111);
        planIds.add(2222);
        planIds.add(3333);
        planIds.add(4444);
        ApiReportQueryParameter queryParam =
                getQueryParam(userid, start, end, planIds, null, null, null, reportType, idonly, statRange);
        String queryParamStr = JsonConverter.toJson(queryParam);
        task.setQueryparam(queryParamStr);
        task.setUserid(userid);
        task.setMachineid(hostname);
        String reportid = ReportIdGenerator.get(queryParamStr);
        task.setReportid(reportid);
        task.setOpuser(opuser);
        task.setIszip(iszip);
        task.setPerformancedata(performancedata);

        long id = apiReportTaskMgr.addTask(task);
        System.out.println(PREFIX + id + SUF);
        assertThat(id, greaterThan(0L));
        Thread.sleep(1000);

        ApiReportTask task2 = apiReportTaskMgr.findTasksById(id);

        assertThat(task2.getStatus(), is(ApiReportTaskConstant.TASK_STATUS_NEW));
        apiReportTaskMgr.updateTaskStatusDoing(task2);
        Thread.sleep(1000);
        ApiReportTask task3 = apiReportTaskMgr.findTasksById(id);
        assertThat(task3.getStatus(), is(ApiReportTaskConstant.TASK_STATUS_DOING));

        assertThat(task3.getUserid(), is(userid));
        assertThat(task3.getMachineid(), is(hostname));
        assertThat(task3.getReportid(), is(reportid));
        assertThat(task3.getOpuser(), is(opuser));
        assertThat(task3.getIszip(), is(iszip));
        ApiReportQueryParameter queryParam3 = JsonConverter.fromJson(task3.getQueryparam());
        System.out.println(LOG_PREFIX + queryParam3);
        assertThat(queryParam3.getPlanIds(), is(planIds));
        assertThat(queryParam3.getStartDate(), is(DateUtils.strToDate(start)));
        assertThat(queryParam3.getEndDate(), is(DateUtils.strToDate(end)));
        assertThat(queryParam3.getUserid(), is(userid));
        assertThat(queryParam3.getReportType(), is(reportType));
        assertThat(queryParam3.getGroupIds(), nullValue());
    }


    /**
     * testUpdateTaskStatusFailIncrementRetry
     * 
     * @throws Exception Exception
     */
    @Test
    public void testUpdateTaskStatusFailIncrementRetry() throws Exception {
        String hostname = InetAddress.getLocalHost().getHostName();
        int userid = 19;

        int opuser = 1;
        boolean idonly = true;
        String start = STARTDATE;
        String end = ENDDATE;
        int reportType = 1;
        int iszip = 0;
        int statRange = 1;
        int performancedata = 63;

        ApiReportTask task = new ApiReportTask();

        // 构造ApiReportQueryParameter
        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(1111);
        planIds.add(2222);
        planIds.add(3333);
        planIds.add(4444);
        ApiReportQueryParameter queryParam =
                getQueryParam(userid, start, end, planIds, null, null, null, reportType, idonly, statRange);
        String queryParamStr = JsonConverter.toJson(queryParam);
        task.setQueryparam(queryParamStr);
        task.setUserid(userid);
        task.setMachineid(hostname);
        String reportid = ReportIdGenerator.get(queryParamStr);
        task.setReportid(reportid);
        task.setOpuser(opuser);
        task.setIszip(iszip);
        task.setPerformancedata(performancedata);

        long id = apiReportTaskMgr.addTask(task);
        Thread.sleep(1000);
        System.out.println(PREFIX + id + SUF);
        assertThat(id, greaterThan(0L));
        ApiReportTask task1 = apiReportTaskMgr.findTasksById(id);
        assertThat(task1.getStatus(), is(ApiReportTaskConstant.TASK_STATUS_NEW));

        apiReportTaskMgr.updateTaskStatusFailIncrementRetry(task1);
        Thread.sleep(1000);
        ApiReportTask task2 = apiReportTaskMgr.findTasksById(id);
        assertThat(task2.getStatus(), is(ApiReportTaskConstant.TASK_STATUS_FAIL_WITH_RETRY));

        apiReportTaskMgr.updateTaskStatusFailIncrementRetry(task2);
        Thread.sleep(1000);
        ApiReportTask task3 = apiReportTaskMgr.findTasksById(id);
        assertThat(task3.getStatus(), is(ApiReportTaskConstant.TASK_STATUS_FAIL_WITH_RETRY));

        apiReportTaskMgr.updateTaskStatusFailIncrementRetry(task3);
        Thread.sleep(1000);
        ApiReportTask task4 = apiReportTaskMgr.findTasksById(id);
        assertThat(task4.getStatus(), is(ApiReportTaskConstant.TASK_STATUS_FAIL));
        Thread.sleep(1000);
        assertThat(task3.getUserid(), is(userid));
        assertThat(task3.getMachineid(), is(hostname));
        assertThat(task3.getReportid(), is(reportid));
        assertThat(task3.getOpuser(), is(opuser));
        assertThat(task3.getIszip(), is(iszip));
        ApiReportQueryParameter queryParam3 = JsonConverter.fromJson(task3.getQueryparam());
        System.out.println(LOG_PREFIX + queryParam3);
        assertThat(queryParam3.getPlanIds(), is(planIds));
        assertThat(queryParam3.getStartDate(), is(DateUtils.strToDate(start)));
        assertThat(queryParam3.getEndDate(), is(DateUtils.strToDate(end)));
        assertThat(queryParam3.getUserid(), is(userid));
        assertThat(queryParam3.getReportType(), is(reportType));
        assertThat(queryParam3.getGroupIds(), nullValue());
    }

    /**
     * .append("userid",userid) .append("startDate", startDate) .append("endDate", endDate) .append("planIds", planIds)
     * .append("groupIds", groupIds) .append("unitIds", unitIds) .append("keywordIds", keywordIds) .append("idOnly",
     * idOnly)
     */
    protected ApiReportQueryParameter getQueryParam(int userid, String start, String end, List<Integer> planIds,
            List<Integer> groupIds, List<Long> unitIds, List<Integer> keywordIds, int reportType, boolean idonly,
            int statRange) throws Exception {
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
        request.setIdOnly(idonly);
        request.setStatRange(statRange);
        return request;
    }

}
