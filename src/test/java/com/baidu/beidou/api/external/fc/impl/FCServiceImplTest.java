package com.baidu.beidou.api.external.fc.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.fc.exporter.impl.FCServiceImpl;
import com.baidu.beidou.api.external.fc.vo.FCCampaignType;
import com.baidu.beidou.api.external.fc.vo.FCCampaignUnitIdType;
import com.baidu.beidou.api.external.fc.vo.FCCampaignUnitType;
import com.baidu.beidou.api.external.fc.vo.FCUnitType;
import com.baidu.beidou.api.external.fc.vo.request.GetFCCampaignByFCCampaignIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCCampaignIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCCampaignRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitByFCCampaignIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitByFCCampaignIdsRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitByFCUnitIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitIdByFCCampaignIdRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitIdByFCCampaignIdsRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.fengchao.BDPlan;
import com.baidu.beidou.fengchao.BDUnit;
import com.baidu.beidou.fengchao.FcFacade;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;

//@RunWith(JMock.class)
@Ignore
public class FCServiceImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 19;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private FCServiceImpl fCService;

    private FcFacade fcFacade;

	protected Mockery context = new JUnit4Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	@Before
	public void beforeEach() {
        fcFacade = context.mock(FcFacade.class);
		fCService.setFcFacade(fcFacade);
	}

	@After
	public void afterEach() {
		context.assertIsSatisfied();
	}

	/** All tested methods:
	 * 
	 *
	public ApiResult<FCCampaignType> getFCCampaign(DataPrivilege user, GetFCCampaignRequest request, ApiOption apiOption);

	public ApiResult<Long> getFCCampaignId(DataPrivilege user, GetFCCampaignIdRequest request, ApiOption apiOption);
	
	public ApiResult<FCCampaignType> getFCCampaignByFCCampaignId(DataPrivilege user, GetFCCampaignByFCCampaignIdRequest request, ApiOption apiOption);
	
	public ApiResult<FCUnitType> getFCUnitByFCCampaignId(DataPrivilege user, GetFCUnitByFCCampaignIdRequest request, ApiOption apiOption);
	
	public ApiResult<Long> getFCUnitIdByFCCampaignId(DataPrivilege user, GetFCUnitIdByFCCampaignIdRequest request, ApiOption apiOption);

	public ApiResult<FCUnitType> getFCUnitByFCUnitId(DataPrivilege user, GetFCUnitByFCUnitIdRequest request, ApiOption apiOption);
	
	 */
	
	@Test
	public void testGetFCCampaign() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				for(int i = 0; i < 10; i++){
					BDPlan type = new BDPlan();
					type.setPlanid(i);
					type.setPlanname("myfc00" + i);
					type.setBudget(Double.valueOf("10." + i));
					fcPlanList.add(type);
				}
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
			}
		});
		
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCCampaignRequest request = new GetFCCampaignRequest();
		ApiResult<FCCampaignType> result  = fCService.getFCCampaign(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(10));
		assertThat(result.getPayment().getTotal(), is(10));
		assertThat(result.getPayment().getSuccess(), is(10));
		
		// 验证结果
		FCCampaignType planType = (FCCampaignType)result.getData().get(1);
		assertThat(planType.getCampaignId(), is(1l));
		assertThat(planType.getCampaignName(), is("myfc001"));
		//assertThat(planType.getStatus(), is(3));
	}
	
	@Test
	public void testGetFCCampaignEmptyData() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
			}
		});
		
		final int userId = 6;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCCampaignRequest request = new GetFCCampaignRequest();
		ApiResult<FCCampaignType> result  = fCService.getFCCampaign(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData(), nullValue());
		assertThat(result.getPayment().getTotal(), is(0));
		assertThat(result.getPayment().getSuccess(), is(0));
		
	}
	
	@Test
	public void testGetFCCampaignId() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				for(int i = 0; i < 10; i++){
					BDPlan type = new BDPlan();
					type.setPlanid(i);
					type.setPlanname("myfc00" + i);
					type.setBudget(Double.valueOf("10." + i));
					fcPlanList.add(type);
				}
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
			}
		});
		
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCCampaignIdRequest request = new GetFCCampaignIdRequest();
		ApiResult<Long> result  = fCService.getFCCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(10));
		assertThat(result.getPayment().getTotal(), is(10));
		assertThat(result.getPayment().getSuccess(), is(10));
		
		// 验证结果
		Long planType = (Long)result.getData().get(1);
		assertThat(planType, is(1l));
		//assertThat(planType.getStatus(), is(3));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testGetFCCampaignIdEmptyData() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(null));
			}
		});
		
		final int userId = 6;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCCampaignIdRequest request = new GetFCCampaignIdRequest();
		ApiResult<Long> result  = fCService.getFCCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData(), nullValue());
		assertThat(result.getPayment().getTotal(), is(0));
		assertThat(result.getPayment().getSuccess(), is(0));
	}
	
	@Test
	public void testGetFCCampaignByFCCampaignId() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				for(int i = 0; i < 10; i++){
					BDPlan type = new BDPlan();
					type.setPlanid(i);
					type.setPlanname("myfc00" + i);
					type.setBudget(Double.valueOf("10." + i));
					fcPlanList.add(type);
				}
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
			}
		});
		
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCCampaignByFCCampaignIdRequest request = new GetFCCampaignByFCCampaignIdRequest();
		long[] ids = new long[4];
		ids[0] = 1l;
		ids[1] = 2l;
		ids[2] = 3l;
		ids[3] = 100l; //invalid
		request.setCampaignIds(ids);
		ApiResult<FCCampaignType> result = fCService.getFCCampaignByFCCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData().size(), is(3));
		assertThat(result.getPayment().getTotal(), is(4));
		assertThat(result.getPayment().getSuccess(), is(3));
		
		// 验证结果
		FCCampaignType planType = (FCCampaignType)result.getData().get(0);
		assertThat(planType.getCampaignId(), is(1l));
		assertThat(planType.getCampaignName(), is("myfc001"));
		//assertThat(planType.getStatus(), is(3));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testGetFCCampaignByFCCampaignIdUnauthorized() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				allowing(fcFacade).getFcPlanlist(6);
				will(returnValue(null));
			}
		});
		
		final int userId = 6;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCCampaignByFCCampaignIdRequest request = new GetFCCampaignByFCCampaignIdRequest();
		long[] ids = new long[4];
		ids[0] = 1l;
		ids[1] = 2l;
		ids[2] = 3l;
		ids[3] = 100l; //invalid
		request.setCampaignIds(ids);
		ApiResult<FCCampaignType> result = fCService.getFCCampaignByFCCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(4));
		assertThat(result.getData(), nullValue());
		assertThat(result.getPayment().getTotal(), is(4));
		assertThat(result.getPayment().getSuccess(), is(0));
		
	}
	
	// -----------------unit----------------------//
	/** All tested methods:
	 * 
	 *
	public ApiResult<FCCampaignType> getFCCampaign(DataPrivilege user, GetFCCampaignRequest request, ApiOption apiOption);

	public ApiResult<Long> getFCCampaignId(DataPrivilege user, GetFCCampaignIdRequest request, ApiOption apiOption);
	
	public ApiResult<FCCampaignType> getFCCampaignByFCCampaignId(DataPrivilege user, GetFCCampaignByFCCampaignIdRequest request, ApiOption apiOption);
	
	public ApiResult<FCUnitType> getFCUnitByFCCampaignId(DataPrivilege user, GetFCUnitByFCCampaignIdRequest request, ApiOption apiOption);
	
	public ApiResult<Long> getFCUnitIdByFCCampaignId(DataPrivilege user, GetFCUnitIdByFCCampaignIdRequest request, ApiOption apiOption);

	public ApiResult<FCUnitType> getFCUnitByFCUnitId(DataPrivilege user, GetFCUnitByFCUnitIdRequest request, ApiOption apiOption);
	
	 */
	
	@Test
	public void testGetFCUnitByFCCampaignId() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				for(int i = 0; i < 10; i++){
					BDPlan type = new BDPlan();
					type.setPlanid(i);
					type.setPlanname("myfc00" + i);
					type.setBudget(Double.valueOf("10." + i));
					fcPlanList.add(type);
				}
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
				
				List<BDUnit> fcUnitList = new ArrayList<BDUnit>();
				for(int i = 0; i < 10; i++){
					BDUnit type = new BDUnit();
					type.setUnitid(i);
					type.setUnitname("myfcunit00" + i);
					type.setPaused(false);
					fcUnitList.add(type);
				}
                allowing(fcFacade).getFcUnitlist(499, 1L, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
				will(returnValue(fcUnitList));
			}
		});
		
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCUnitByFCCampaignIdRequest request = new GetFCUnitByFCCampaignIdRequest();
		request.setCampaignId(1l);
		ApiResult<FCUnitType> result  = fCService.getFCUnitByFCCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(10));
		assertThat(result.getPayment().getTotal(), is(10));
		assertThat(result.getPayment().getSuccess(), is(10));
		
		// 验证结果
		FCUnitType unitType = (FCUnitType)result.getData().get(1);
		assertThat(unitType.getUnitId(), is(1l));
		assertThat(unitType.getUnitName(), is("myfcunit001"));
		//assertThat(planType.getStatus(), is(3));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testGetFCUnitByFCCampaignIdEmptyData() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				for(int i = 0; i < 10; i++){
					BDPlan type = new BDPlan();
					type.setPlanid(i);
					type.setPlanname("myfc00" + i);
					type.setBudget(Double.valueOf("10." + i));
					fcPlanList.add(type);
				}
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
				
				List<BDUnit> fcUnitList = new ArrayList<BDUnit>();
                allowing(fcFacade).getFcUnitlist(499, 1L, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
				will(returnValue(null));
			}
		});
		
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCUnitByFCCampaignIdRequest request = new GetFCUnitByFCCampaignIdRequest();
		request.setCampaignId(1l);
		ApiResult<FCUnitType> result  = fCService.getFCUnitByFCCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData(), nullValue());
		assertThat(result.getPayment().getTotal(), is(0));
		assertThat(result.getPayment().getSuccess(), is(0));
		
	}
	
	@Test
	public void testGetFCUnitByFCCampaignIdUnauthorized() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				for(int i = 0; i < 10; i++){
					BDPlan type = new BDPlan();
					type.setPlanid(i);
					type.setPlanname("myfc00" + i);
					type.setBudget(Double.valueOf("10." + i));
					fcPlanList.add(type);
				}
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
				
				List<BDUnit> fcUnitList = new ArrayList<BDUnit>();
				for(int i = 0; i < 10; i++){
					BDUnit type = new BDUnit();
					type.setUnitid(i);
					type.setUnitname("myfcunit00" + i);
					type.setPaused(false);
					fcUnitList.add(type);
				}
                allowing(fcFacade).getFcUnitlist(499, 100L, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
				will(returnValue(fcUnitList));
			}
		});
		
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCUnitByFCCampaignIdRequest request = new GetFCUnitByFCCampaignIdRequest();
		request.setCampaignId(100l);
		ApiResult<FCUnitType> result  = fCService.getFCUnitByFCCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().get(0).getCode(), is(2));
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());
		assertThat(result.getPayment().getTotal(), is(0));
		assertThat(result.getPayment().getSuccess(), is(0));
		
	}
	
	@Test
	public void testGetFCUnitByFCCampaignIds() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				for(int i = 0; i < 10; i++){
					BDPlan type = new BDPlan();
					type.setPlanid(i);
					type.setPlanname("myfc00" + i);
					type.setBudget(Double.valueOf("10." + i));
					fcPlanList.add(type);
				}
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
				
				List<BDUnit> fcUnitList = new ArrayList<BDUnit>();
				for(int i = 0; i < 10; i++){
					BDUnit type = new BDUnit();
					type.setUnitid(i);
					type.setUnitname("myfcunit00" + i);
					type.setPaused(false);
					fcUnitList.add(type);
				}
                allowing(fcFacade).getFcUnitlist(499, 1L, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
				will(returnValue(fcUnitList));
			}
		});
		
		final int userId = 499;
		 
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCUnitByFCCampaignIdsRequest request = new GetFCUnitByFCCampaignIdsRequest();
		request.setCampaignIds(new long[]{1l});
		BaseResponse<FCCampaignUnitType> result  = fCService.getFCUnitByFCCampaignIds(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));
		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));
		
		// 验证结果
		FCCampaignUnitType unitType = (FCCampaignUnitType)(result.getData()[0]);
		assertThat(unitType.getCampaignId(), is(1l));
		assertThat(unitType.getUnits()[0].getUnitId(), is(0l));
		//assertThat(planType.getStatus(), is(3));
	}
	
	@Test
	public void testGetFCUnitIdByFCCampaignId() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				for(int i = 0; i < 10; i++){
					BDPlan type = new BDPlan();
					type.setPlanid(i);
					type.setPlanname("myfc00" + i);
					type.setBudget(Double.valueOf("10." + i));
					fcPlanList.add(type);
				}
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
				
				List<BDUnit> fcUnitList = new ArrayList<BDUnit>();
				for(int i = 0; i < 10; i++){
					BDUnit type = new BDUnit();
					type.setUnitid(i);
					type.setUnitname("myfcunit00" + i);
					type.setPaused(false);
					fcUnitList.add(type);
				}
                allowing(fcFacade).getFcUnitlist(499, 1L, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
				will(returnValue(fcUnitList));
			}
		});
		
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCUnitIdByFCCampaignIdRequest request = new GetFCUnitIdByFCCampaignIdRequest();
		request.setCampaignId(1l);
		ApiResult<Long> result  = fCService.getFCUnitIdByFCCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(10));
		assertThat(result.getPayment().getTotal(), is(10));
		assertThat(result.getPayment().getSuccess(), is(10));
		
		// 验证结果
		Long unitType = (Long)result.getData().get(1);
		assertThat(unitType, is(1l));
		//assertThat(planType.getStatus(), is(3));
	}
	
	@Test
	public void testGetFCUnitIdByFCCampaignIds() {
		context.checking(new Expectations() {
			{
				List<BDPlan> fcPlanList = new ArrayList<BDPlan>();
				for(int i = 0; i < 10; i++){
					BDPlan type = new BDPlan();
					type.setPlanid(i);
					type.setPlanname("myfc00" + i);
					type.setBudget(Double.valueOf("10." + i));
					fcPlanList.add(type);
				}
				allowing(fcFacade).getFcPlanlist(with(any(Integer.class)));
				will(returnValue(fcPlanList));
				
				List<BDUnit> fcUnitList = new ArrayList<BDUnit>();
				for(int i = 0; i < 10; i++){
					BDUnit type = new BDUnit();
					type.setUnitid(i);
					type.setUnitname("myfcunit00" + i);
					type.setPaused(false);
					fcUnitList.add(type);
				}
                allowing(fcFacade).getFcUnitlist(499, 1L, FcFacade.FC_UNIT_STATE_INCLUDE_PAUSE);
				will(returnValue(fcUnitList));
			}
		});
		
		final int userId = 499;
		 
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCUnitIdByFCCampaignIdsRequest request = new GetFCUnitIdByFCCampaignIdsRequest();
		request.setCampaignIds(new long[]{1l});
		BaseResponse<FCCampaignUnitIdType> result  = fCService.getFCUnitIdByFCCampaignIds(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));
		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));
		assertThat(((FCCampaignUnitIdType)result.getData()[0]).getCampaignId(), is(1l));
		assertThat(((FCCampaignUnitIdType)result.getData()[0]).getUnitIds().length, is(10));
		
	}
	
	@Test
	public void testGetFCUnitByFCUnitId() {
		context.checking(new Expectations() {
			{	
				List<BDUnit> fcUnitList = new ArrayList<BDUnit>();
				for(int i = 0; i < 10; i++){
					BDUnit type = new BDUnit();
					type.setUnitid(i);
					type.setUnitname("myfcunit00" + i);
					type.setPaused(false);
					fcUnitList.add(type);
				}
				List<Long> unitIds = new ArrayList<Long>();
				unitIds.add(1l);
				unitIds.add(2l);
				unitIds.add(3l);
				unitIds.add(100l);
				allowing(fcFacade).getFcUnitListByUnitids(499, unitIds, null);
				will(returnValue(fcUnitList));
			}
		});
		
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetFCUnitByFCUnitIdRequest request = new GetFCUnitByFCUnitIdRequest();
		long[] ids = new long[4];
		ids[0] = 1l;
		ids[1] = 2l;
		ids[2] = 3l;
		ids[3] = 100l; //invalid
		request.setUnitIds(ids);
		ApiResult<FCUnitType> result  = fCService.getFCUnitByFCUnitId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData().size(), is(3));
		assertThat(result.getPayment().getTotal(), is(4));
		assertThat(result.getPayment().getSuccess(), is(3));
		
		// 验证结果
		FCUnitType unitType = (FCUnitType)result.getData().get(1);
		assertThat(unitType.getUnitId(), is(2l));
		assertThat(unitType.getUnitName(), is("myfcunit002"));
		//assertThat(planType.getStatus(), is(3));
	}
	
}
