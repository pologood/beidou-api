package com.baidu.beidou.api.external.cproplan.exporter.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cproplan.exporter.CampaignService;
import com.baidu.beidou.api.external.cproplan.vo.CampaignType;
import com.baidu.beidou.api.external.cproplan.vo.ScheduleType;
import com.baidu.beidou.api.external.cproplan.vo.request.AddCampaignRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.GetCampaignByCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.GetCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.GetCampaignRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.UpdateCampaignRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
import com.baidu.beidou.util.DateUtils;
// @Ignore
public class CampaignServiceImplTest extends AbstractShardAddbTestCaseLegacy {

    private static int userId = 8;
    private long planId;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource(name = "planService")
	private CampaignService campaignService;

	@Test
	public void testAddCampaign() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		AddCampaignRequest request = new AddCampaignRequest();
		CampaignType[] campaignTypes = new CampaignType[1];

		CampaignType plan = new CampaignType();
		plan.setCampaignName("abek-plan-1");
		plan.setBudget(100);
		plan.setStatus(0);
		Date currDate = DateUtils.getRoundedDayCurDate();
		Date startDate = DateUtils.getNextDay(currDate, 1L);
		Date endDate = DateUtils.getNextDay(currDate, 10L);
		plan.setStartDate(startDate);
		plan.setEndDate(endDate);

		ScheduleType[] schedule = new ScheduleType[3];
		schedule[0] = new ScheduleType();
		schedule[0].setWeekDay(1);
		schedule[0].setStartTime(0);
		schedule[0].setEndTime(24);
		schedule[1] = new ScheduleType();
		schedule[1].setWeekDay(2);
		schedule[1].setStartTime(8);
		schedule[1].setEndTime(21);
		schedule[2] = new ScheduleType();
		schedule[2].setWeekDay(3);
		schedule[2].setStartTime(10);
		schedule[2].setEndTime(23);
		plan.setSchedule(schedule);
		campaignTypes[0] = plan;

		request.setCampaignTypes(campaignTypes);

		ApiResult<CampaignType> result = campaignService.addCampaign(dataUser, request, apiOption);
		
		planId = result.getData().get(0).getCampaignId();

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		CampaignType planType = (CampaignType) result.getData().get(0);
		assertThat(planType.getCampaignId(), greaterThan(0L));
		assertThat(planType.getStatus(), is(3));
	}

    /**
     * testGetCampaignByCampaignId
     */
	@Ignore
	@Test
	public void testGetCampaignByCampaignId() {
		final long planId = this.planId;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		GetCampaignByCampaignIdRequest request = new GetCampaignByCampaignIdRequest();
		request.setCampaignIds(new long[] { planId });

		ApiResult<CampaignType> result = campaignService.getCampaignByCampaignId(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		CampaignType planType = (CampaignType) result.getData().get(0);
		assertThat(planType.getCampaignId(), is(105042L));
	}

	/**
	 * testGetCampaignId
	 */
	@Ignore
	@Test
	public void testGetCampaignId() {
		final long planId = this.planId;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		GetCampaignIdRequest request = new GetCampaignIdRequest();

		ApiResult<Long> result = campaignService.getCampaignId(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), greaterThan(1));
		assertThat(result.getPayment().getTotal(), greaterThan(1));
		assertThat(result.getPayment().getSuccess(), greaterThan(1));

		// 验证结果
		assertThat(planId, isIn(result.getData()));
	}

	/**
	 * testGetCampaign
	 */
	@Ignore
	@Test
	public void testGetCampaign() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		GetCampaignRequest request = new GetCampaignRequest();

		ApiResult<CampaignType> result = campaignService.getCampaign(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), greaterThan(1));
		assertThat(result.getPayment().getTotal(), greaterThan(1));
		assertThat(result.getPayment().getSuccess(), greaterThan(1));

		// 验证结果
		CampaignType planType = (CampaignType) result.getData().get(0);
		assertThat(planType.getCampaignId(), greaterThan(105042L));
		assertThat(planType.getStatus(), is(1));
	}

	@Test
	public void testUpdateCampaign() {
		final long planId = this.planId;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		UpdateCampaignRequest request = new UpdateCampaignRequest();
		CampaignType[] campaignTypes = new CampaignType[1];

		CampaignType plan = new CampaignType();
		plan.setCampaignId(planId);
		plan.setCampaignName("abek-plan-2");
		plan.setBudget(100);
		plan.setStatus(0);
		//		Date currDate = DateUtils.getRoundedDayCurDate();
		//		Date startDate = DateUtils.getNextDay(currDate, 1L);
		//		Date endDate = DateUtils.getNextDay(currDate, 10L);
		//		plan.setStartDate(startDate);
		//		plan.setEndDate(endDate);

		ScheduleType[] schedule = new ScheduleType[3];
		schedule[0] = new ScheduleType();
		schedule[0].setWeekDay(1);
		schedule[0].setStartTime(0);
		schedule[0].setEndTime(24);
		schedule[1] = new ScheduleType();
		schedule[1].setWeekDay(2);
		schedule[1].setStartTime(8);
		schedule[1].setEndTime(21);
		schedule[2] = new ScheduleType();
		schedule[2].setWeekDay(3);
		schedule[2].setStartTime(10);
		schedule[2].setEndTime(23);
		plan.setSchedule(schedule);
		campaignTypes[0] = plan;

		request.setCampaignTypes(campaignTypes);

		ApiResult<CampaignType> result = campaignService.updateCampaign(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
	}

	/**
	 * testUpdateCampaignWhenFailed
	 */
	@Ignore
	@Test
	public void testUpdateCampaignWhenFailed() {
		final long planId = this.planId;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		UpdateCampaignRequest request = new UpdateCampaignRequest();
		CampaignType[] campaignTypes = new CampaignType[1];

		CampaignType plan = new CampaignType();
		plan.setCampaignId(planId);
		plan.setCampaignName("abek-plan-1");
		plan.setBudget(100);
		plan.setStatus(0);
		Date currDate = DateUtils.getRoundedDayCurDate();
		Date startDate = DateUtils.getNextDay(currDate, 1L);
		Date endDate = DateUtils.getNextDay(currDate, 10L);
		plan.setStartDate(startDate);
		plan.setEndDate(endDate);

		ScheduleType[] schedule = new ScheduleType[3];
		schedule[0] = new ScheduleType();
		schedule[0].setWeekDay(0);
		schedule[0].setStartTime(0);
		schedule[0].setEndTime(24);
		schedule[1] = new ScheduleType();
		schedule[1].setWeekDay(1);
		schedule[1].setStartTime(8);
		schedule[1].setEndTime(21);
		schedule[2] = new ScheduleType();
		schedule[2].setWeekDay(2);
		schedule[2].setStartTime(10);
		schedule[2].setEndTime(23);
		plan.setSchedule(schedule);
		campaignTypes[0] = plan;

		request.setCampaignTypes(campaignTypes);

		ApiResult<CampaignType> result = campaignService.updateCampaign(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(0));

		// 验证结果
	}
}
