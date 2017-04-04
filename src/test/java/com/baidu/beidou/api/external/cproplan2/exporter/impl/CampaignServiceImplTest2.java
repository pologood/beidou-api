package com.baidu.beidou.api.external.cproplan2.exporter.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cproplan2.exporter.CampaignService2;
import com.baidu.beidou.api.external.cproplan2.vo.CampaignType;
import com.baidu.beidou.api.external.cproplan2.vo.ScheduleType;
import com.baidu.beidou.api.external.cproplan2.vo.request.AddCampaignRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.GetCampaignByCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.GetCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.GetCampaignRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.UpdateCampaignRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.cproplan.service.CproPlanConstantMgr;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.beidou.util.DateUtils;
@Ignore
public class CampaignServiceImplTest2 extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 499;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource(name = "planService2")
	private CampaignService2 campaignService2;
	
	@Resource
	private CproPlanConstantMgr cproPlanConstantMgr;
	
	static {
	}

	@Test
	public void init() {
		cproPlanConstantMgr.loadCproplanConstant();
	}
	
	@Test
	public void testAddCampaign() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		AddCampaignRequest request = new AddCampaignRequest();
		CampaignType[] campaignTypes = new CampaignType[1];

		CampaignType plan = new CampaignType();
		plan.setCampaignName("bmob-计划007");
		plan.setBudget(100);
		plan.setStatus(0);
		Date currDate = DateUtils.getRoundedDayCurDate();
		Date startDate = DateUtils.getNextDay(currDate, 1L);
		Date endDate = DateUtils.getNextDay(currDate, 10L);
		plan.setStartDate(startDate);
		plan.setEndDate(endDate);
		plan.setWirelessBidRatio(90);
		plan.setType(1);

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

		ApiResult<CampaignType> result = campaignService2.addCampaign(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		CampaignType planType = (CampaignType) result.getData().get(0);
		assertThat(planType.getCampaignId(), greaterThan(105042L));
		assertThat(planType.getStatus(), is(3));
	}
	
	@Test
	public void testAddCampaignForBmob() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		AddCampaignRequest request = new AddCampaignRequest();
		CampaignType[] campaignTypes = new CampaignType[1];

		CampaignType plan = new CampaignType();
		plan.setCampaignName("bmob1-计划000");
		plan.setBudget(100);
		plan.setStatus(0);
		Date currDate = DateUtils.getRoundedDayCurDate();
		Date startDate = DateUtils.getNextDay(currDate, 1L);
		Date endDate = DateUtils.getNextDay(currDate, 10L);
		plan.setStartDate(startDate);
		plan.setEndDate(endDate);
		plan.setWirelessBidRatio(90);
		plan.setType(1);

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

		ApiResult<CampaignType> result = campaignService2.addCampaign(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		CampaignType planType = (CampaignType) result.getData().get(0);
		assertThat(planType.getCampaignId(), greaterThan(105042L));
		assertThat(planType.getStatus(), is(3));
	}

	@Test
	public void testGetCampaignByCampaignId() {
		final long planId = 105042;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		GetCampaignByCampaignIdRequest request = new GetCampaignByCampaignIdRequest();
		request.setCampaignIds(new long[] { planId });

		ApiResult<CampaignType> result = campaignService2.getCampaignByCampaignId(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		CampaignType planType = (CampaignType) result.getData().get(0);
		assertThat(planType.getCampaignId(), is(105042L));
	}

	@Test
	public void testGetCampaignId() {
		final long planId = 105042;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		GetCampaignIdRequest request = new GetCampaignIdRequest();

		ApiResult<Long> result = campaignService2.getCampaignId(dataUser, request, apiOption);

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

	@Test
	public void testGetCampaign() {
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		GetCampaignRequest request = new GetCampaignRequest();

		ApiResult<CampaignType> result = campaignService2.getCampaign(dataUser, request, apiOption);

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
		final long planId = 105042;
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
		plan.setType(0);
		plan.setWirelessBidRatio(50);
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

		ApiResult<CampaignType> result = campaignService2.updateCampaign(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
	}

	@Test
	public void testUpdateCampaignWhenFailed() {
		final long planId = 105042;
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

		ApiResult<CampaignType> result = campaignService2.updateCampaign(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(0));

		// 验证结果
	}
	
	/**
	 * 
# 移动设备、操作系统从低位到高位的定义：
0	其它
1	iPhone
2	android
3	iPad
4-9	空
10	iOS 6.2
11	iOS 6.1
12	iOS 6.0
13	iOS 5.1
14	iOS 5.0
15	iOS 4.3
16	iOS 4.2
17	iOS 4.1
18	iOS 4.0
19	iOS 3.2
20	iOS 3.1
21	iOS 3.0
22	iOS other
23	android 4.2
24	android 4.1
25	android 4.0
26	android 3.1
27	android 3.0
28	android 2.3
29	android 2.2
30	android 2.1
31	android 1.6
32	android 1.5
33	android other
	 *
	 */
	@Test
	public void testAddCampaign_BMOB() {
		init();
		
		int device = 1<<0 | 1<<1;
		List<Long> os = new ArrayList<Long>();
		os.add(1l<<11);
		
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		AddCampaignRequest request = new AddCampaignRequest();
		CampaignType[] campaignTypes = new CampaignType[1];

		CampaignType plan = new CampaignType();
		plan.setCampaignName("bmob-计划008");
		plan.setBudget(100);
		plan.setStatus(0);
		Date currDate = DateUtils.getRoundedDayCurDate();
		Date startDate = DateUtils.getNextDay(currDate, 1L);
		Date endDate = DateUtils.getNextDay(currDate, 10L);
		plan.setStartDate(startDate);
		plan.setEndDate(endDate);
		plan.setWirelessBidRatio(100);
		plan.setType(1); // 仅移动的推广类型, 移动溢价必须为100

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
		
		plan.setIsDeviceEnabled(true);
		plan.setDevice(device);
		plan.setIsOsEnabled(false);
		plan.setOs(CollectionsUtil.tranformLongListToLongArray(os));
		
		campaignTypes[0] = plan;

		request.setCampaignTypes(campaignTypes);

		ApiResult<CampaignType> result = campaignService2.addCampaign(dataUser, request, apiOption);

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
		
		final long planId = planType.getCampaignId();

		GetCampaignByCampaignIdRequest request2 = new GetCampaignByCampaignIdRequest();
		request2.setCampaignIds(new long[] { planId });

		ApiResult<CampaignType> result2 = campaignService2.getCampaignByCampaignId(dataUser, request2, apiOption);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));

		// 验证结果
		CampaignType planType2 = (CampaignType) result2.getData().get(0);
		assertThat(planType2.getIsDeviceEnabled(), is(true));
		assertThat(planType2.getIsOsEnabled(), is(true));
		assertThat(planType2.getDevice(), is(device));
		assertThat(planType2.getOs().length, greaterThan(8));
	}
	
	@Test
	public void testUpdateCampaign_BMOB() {
		init();
		
		int device = 1<<1;
		List<Long> os = new ArrayList<Long>();
		os.add(1l<<11);
		os.add(1l<<12);
		
		final long planId = 105042;
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
		plan.setType(0);
		plan.setWirelessBidRatio(50);
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
		
		plan.setIsDeviceEnabled(true);
		plan.setDevice(device);
		plan.setIsOsEnabled(true);
		plan.setOs(CollectionsUtil.tranformLongListToLongArray(os));
		
		campaignTypes[0] = plan;

		request.setCampaignTypes(campaignTypes);

		ApiResult<CampaignType> result = campaignService2.updateCampaign(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		GetCampaignByCampaignIdRequest request2 = new GetCampaignByCampaignIdRequest();
		request2.setCampaignIds(new long[] { planId });

		ApiResult<CampaignType> result2 = campaignService2.getCampaignByCampaignId(dataUser, request2, apiOption);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));

		// 验证结果
		CampaignType planType2 = (CampaignType) result2.getData().get(0);
		assertThat(planType2.getIsDeviceEnabled(), is(true));
		assertThat(planType2.getIsOsEnabled(), is(true));
		assertThat(planType2.getDevice(), is(device));
		assertThat(planType2.getOs().length, is(2));
	}

}
