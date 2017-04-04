package com.baidu.beidou.api.external.cproplan.exporter.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.cproplan.exporter.CampaignService;
import com.baidu.beidou.api.external.cproplan.vo.CampaignType;
import com.baidu.beidou.api.external.cproplan.vo.ScheduleType;
import com.baidu.beidou.api.external.cproplan.vo.request.AddCampaignRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.GetCampaignByCampaignIdRequest;
import com.baidu.beidou.api.external.cproplan.vo.request.UpdateCampaignRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.util.DateUtils;
@Ignore
public class CampaignServiceTest extends ApiBaseRPCTest<CampaignService> {

	@Test
	public void testAddCampaign() throws Exception {
		CampaignService exporter = getServiceProxy(CampaignService.class, ApiExternalConstant.CAMPAIGN_SERVICE_URL);

		AddCampaignRequest request = new AddCampaignRequest();

		CampaignType[] types = new CampaignType[1];

		CampaignType type0 = new CampaignType();
		type0.setCampaignName("neo食品2911");
		type0.setBudget(60);
		type0.setStatus(0);
		type0.setStartDate(DateUtils.getNextDay());
		type0.setEndDate(DateUtils.strToDate("20120804"));
		ScheduleType sch = new ScheduleType();
		sch.setStartTime(0);
		sch.setEndTime(22);
		sch.setWeekDay(1);
		type0.setSchedule(new ScheduleType[] { sch });

		types[0] = type0;
		request.setCampaignTypes(types);
		ApiResult<CampaignType> result = exporter.addCampaign(dataUser, request, apiOption);
		System.out.println(result);
	}

	@Test
	public void testUpdateCampaign() throws Exception {
		CampaignService exporter = getServiceProxy(CampaignService.class, ApiExternalConstant.CAMPAIGN_SERVICE_URL);

		UpdateCampaignRequest request = new UpdateCampaignRequest();

		CampaignType[] types = new CampaignType[2];

		CampaignType type0 = new CampaignType();
		type0.setCampaignId(984809);
		type0.setCampaignName("neo食品23");
		type0.setBudget(600);
		type0.setStatus(2);
		type0.setStartDate(DateUtils.getDate(20120206));
		type0.setEndDate(DateUtils.strToDate("20120304"));
		ScheduleType sch = new ScheduleType();
		sch.setStartTime(0);
		sch.setEndTime(23);
		sch.setWeekDay(1);
		type0.setSchedule(new ScheduleType[] { sch });

		CampaignType type1 = new CampaignType();
		type1.setCampaignId(757455);
		type1.setCampaignName("neo食品23");
		type1.setBudget(600);
		type1.setStatus(0);
		type1.setStartDate(DateUtils.getNextDay());
		type1.setEndDate(DateUtils.strToDate("20120305"));
		ScheduleType sch1 = new ScheduleType();
		sch1.setStartTime(0);
		sch1.setEndTime(22);
		sch1.setWeekDay(1);
		type1.setSchedule(new ScheduleType[] { sch1 });

		types[0] = type0;
		types[1] = type1;
		request.setCampaignTypes(types);
		ApiResult<CampaignType> result = exporter.updateCampaign(dataUser, request, apiOption);
		System.out.println(result);
	}

	@Test
	public void testGetCampaign() throws Exception {
		CampaignService exporter = getServiceProxy(CampaignService.class, ApiExternalConstant.CAMPAIGN_SERVICE_URL);

		GetCampaignByCampaignIdRequest request = new GetCampaignByCampaignIdRequest();
		request.setCampaignIds(new long[] { 984809l });
		ApiResult<CampaignType> result = exporter.getCampaignByCampaignId(dataUser, request, apiOption);
		System.out.println(result);
	}

}
