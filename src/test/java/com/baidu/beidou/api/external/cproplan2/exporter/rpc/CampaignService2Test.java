package com.baidu.beidou.api.external.cproplan2.exporter.rpc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cproplan2.exporter.CampaignService2;
import com.baidu.beidou.api.external.cproplan2.vo.CampaignType;
import com.baidu.beidou.api.external.cproplan2.vo.ScheduleType;
import com.baidu.beidou.api.external.cproplan2.vo.request.AddCampaignRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.GetCampaignByCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.GetCampaignRequest;
import com.baidu.beidou.api.external.cproplan2.vo.request.UpdateCampaignRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.beidou.util.DateUtils;
@Ignore
public class CampaignService2Test  extends ApiBaseRPCTest<CampaignService2> {

	@Test
	public void testAddCampaign() throws Exception {
		CampaignService2 exporter = getServiceProxy(CampaignService2.class, ApiExternalConstant.CAMPAIGN_SERVICE2_URL);

		AddCampaignRequest request = new AddCampaignRequest();

		CampaignType[] types = new CampaignType[1];

		CampaignType type0 = new CampaignType();
		type0.setCampaignName("1234211");
		type0.setBudget(60);
		type0.setStatus(0);
		type0.setStartDate(DateUtils.getNextDay());
		type0.setEndDate(DateUtils.strToDate("20131225"));
		ScheduleType sch = new ScheduleType();
		sch.setStartTime(0);
		sch.setEndTime(22);
		sch.setWeekDay(1);
		//type0.setType(0);
		//type0.setWirelessBidRatio(100);
		type0.setSchedule(new ScheduleType[] { sch });

		types[0] = type0;
		request.setCampaignTypes(types);
		ApiResult<CampaignType> result = exporter.addCampaign(dataUser, request, apiOption);
		System.out.println(result);
	}

	@Test
	public void testUpdateCampaign() throws Exception {
		CampaignService2 exporter = getServiceProxy(CampaignService2.class, ApiExternalConstant.CAMPAIGN_SERVICE2_URL);

		UpdateCampaignRequest request = new UpdateCampaignRequest();

		CampaignType[] types = new CampaignType[1];

		CampaignType type0 = new CampaignType();
		type0.setCampaignId(58);
		//type0.setCampaignName("neo食品23");
		//type0.setType(1);
		type0.setBudget(90);
		//type0.setEndDate(DateUtils.getDate(20140429));
		//type0.setWirelessBidRatio(30);
		//type0.setStatus(2);
		//type0.setStartDate(DateUtils.getDate(20131126));
		//type0.setEndDate(DateUtils.strToDate("20131129"));
		//ScheduleType sch = new ScheduleType();
		//sch.setStartTime(0);
		//sch.setEndTime(23);
		//sch.setWeekDay(1);
		//type0.setSchedule(new ScheduleType[] { sch });
		// 设置移动操作系统版本
		type0.setIsDeviceEnabled(true);
		type0.setIsOsEnabled(true);
		int device = 1 << 1;    // iPhone
		List<Long> os = new ArrayList<Long>();
		os.add(1L << 47); // iOS 8.1
		os.add(1L << 48); // iOS 8.2
		os.add(1L << 49); // iOS 8.3
		type0.setDevice(device);
		type0.setOs(CollectionsUtil.tranformLongListToLongArray(os));

		//CampaignType type1 = new CampaignType();
		//type1.setCampaignId(809866);
		//type1.setCampaignName("neo食品231");
		//type1.setBudget(400);
		//type1.setStatus(0);
		//type1.setStartDate(DateUtils.getNextDay());
		//type1.setStartDate(DateUtils.getDate(20131126));
		//type1.setEndDate(DateUtils.strToDate("20131127"));
		//ScheduleType sch1 = new ScheduleType();
		//sch1.setStartTime(0);
		//sch1.setEndTime(22);
		//sch1.setWeekDay(1);
		//type1.setSchedule(new ScheduleType[] { sch1 });
		//type1.setIsDeviceEnabled(null);
		//type1.setIsOsEnabled(null);

		types[0] = type0;
		//types[1] = type1;
		request.setCampaignTypes(types);
		ApiResult<CampaignType> result = exporter.updateCampaign(dataUser, request, apiOption);
		System.out.println(result);
	}

	@Test
	public void testGetCampaign() throws Exception {
		CampaignService2 exporter = getServiceProxy(CampaignService2.class, ApiExternalConstant.CAMPAIGN_SERVICE2_URL);

		GetCampaignRequest request = new GetCampaignRequest();
		ApiResult<CampaignType> result = exporter.getCampaign(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	public void testGetCampaignById() throws Exception {
		CampaignService2 exporter = getServiceProxy(CampaignService2.class, ApiExternalConstant.CAMPAIGN_SERVICE2_URL);

		GetCampaignByCampaignIdRequest request = new GetCampaignByCampaignIdRequest();
		request.setCampaignIds(new long[] { 1987415 });
		ApiResult<CampaignType> result = exporter.getCampaignByCampaignId(dataUser, request, apiOption);
		System.out.println(result);
	}


	@Test
	public void testAddCampaignForBmob() throws Exception {
		CampaignService2 exporter = getServiceProxy(CampaignService2.class, ApiExternalConstant.CAMPAIGN_SERVICE2_URL);
		
		Integer userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		AddCampaignRequest request = new AddCampaignRequest();
		CampaignType[] campaignTypes = new CampaignType[1];

		CampaignType plan = new CampaignType();
		plan.setCampaignName("bmob1-计划003");
		plan.setBudget(1000);
		plan.setStatus(0);
		Date currDate = DateUtils.getRoundedDayCurDate();
		Date startDate = DateUtils.getNextDay(currDate, 1L);
		Date endDate = DateUtils.getNextDay(currDate, 10L);
		plan.setStartDate(startDate);
		plan.setEndDate(endDate);

		// 设置移动操作系统和版本
		int device = 1 << 1 | 1 << 2;    // iPhone & android
		List<Long> os = new ArrayList<Long>();
		os.add(1L << 47); // iOS 8.1
		os.add(1L << 48); // iOS 8.2
		os.add(1L << 49); // iOS 8.3
		os.add(1L << 49); // iOS 8.3

		os.add(1L << 37); // android 5.1
		os.add(1L << 36); // android 5.0
		os.add(1L << 35); // android 4.4
		os.add(1L << 34); // android 4.3
		plan.setIsDeviceEnabled(true);
		plan.setIsOsEnabled(true);
		plan.setDevice(device);
		plan.setOs(CollectionsUtil.tranformLongListToLongArray(os));

		//plan.setWirelessBidRatio(90);
		//plan.setType(1);

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

		ApiResult<CampaignType> result = exporter.addCampaign(dataUser, request, apiOption);

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

}
