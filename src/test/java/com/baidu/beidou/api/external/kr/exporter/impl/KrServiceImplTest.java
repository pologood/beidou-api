package com.baidu.beidou.api.external.kr.exporter.impl;

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
import org.springframework.test.annotation.Rollback;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.kr.error.KrErrorCode;
import com.baidu.beidou.api.external.kr.facade.KrKeywordFacade;
import com.baidu.beidou.api.external.kr.vo.KRResultType;
import com.baidu.beidou.api.external.kr.vo.KrInfoVo;
import com.baidu.beidou.api.external.kr.vo.request.GetKRBySeedRequest;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
@Ignore
public class KrServiceImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 499;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private KrServiceImpl apiKrService;

	private KrKeywordFacade krKeywordFacade;

	@Resource
	private CproGroupConstantMgr cproGroupConstantMgr;
	
	@Test
	public void testLoad() {
		cproGroupConstantMgr.loadSystemConf();
	}
	
	protected Mockery context = new JUnit4Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	@Before
	public void beforeEach() {
		krKeywordFacade = context.mock(KrKeywordFacade.class);
		((KrServiceImpl)apiKrService).setKrKeywordFacade(krKeywordFacade);
	}

	@After
	public void afterEach() {
		//context.assertIsSatisfied();
	}


	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	public void testGetKRBySeed() throws Exception{
		final int userId = 499;
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		context.checking(new Expectations() {
			{
				List<KrInfoVo> returnValue = new ArrayList<KrInfoVo>();
				KrInfoVo vo1 = new KrInfoVo();
				vo1.setWordid(123l);
				vo1.setUv(1000l);
				vo1.setKw("网盟123");
				vo1.setCompdg(6);
				vo1.setAwgdscnt(600l);
				
				KrInfoVo vo2 = new KrInfoVo();
				vo2.setWordid(456l);
				vo2.setUv(99l);
				vo2.setKw("helloWorld");
				vo2.setCompdg(1);
				vo2.setAwgdscnt(0l);
				
				KrInfoVo vo3 = new KrInfoVo();
				vo3.setWordid(777l);
				vo3.setUv(10l);
				vo3.setKw("哈哈");
				vo3.setCompdg(0);
				vo3.setAwgdscnt(5l);
				
				returnValue.add(vo1);
				returnValue.add(vo2);
				returnValue.add(vo3);
				
				oneOf(krKeywordFacade).getRecommWord(with(any(Integer.class)), 
						with(any(Integer.class)), 
						with(any(String.class)), 
						with(any(List.class)), 
						with(any(Integer.class)), 
						with(any(Boolean.class)), 
						with(any(Integer.class)), 
						with(any(Integer.class)));
				will(returnValue(returnValue));
			}
		});
		
		GetKRBySeedRequest request = new GetKRBySeedRequest();
		request.setSeed("123");
		request.setGroupId(228);
		//request.setRegionList(new int[]{1});
		request.setAliveDays(15);
		request.setTargetType(7);
		
		BaseResponse<KRResultType> result  = apiKrService.getKRBySeed(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(3));

		assertThat(((KRResultType)result.getData()[0]).getWord(), is("网盟123"));
		assertThat(result.getData()[0].getUv(), is(1000));
		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	public void testGetKRBySeedNegative_RegionNotFound() throws Exception{
		final int userId = 499;
		 
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		context.checking(new Expectations() {
			{
				List<KrInfoVo> returnValue = new ArrayList<KrInfoVo>();
				KrInfoVo vo1 = new KrInfoVo();
				vo1.setWordid(123l);
				vo1.setUv(1000l);
				vo1.setKw("网盟123");
				vo1.setCompdg(6);
				vo1.setAwgdscnt(600l);
				
				KrInfoVo vo2 = new KrInfoVo();
				vo2.setWordid(456l);
				vo2.setUv(99l);
				vo2.setKw("helloWorld");
				vo2.setCompdg(1);
				vo2.setAwgdscnt(0l);
				
				KrInfoVo vo3 = new KrInfoVo();
				vo3.setWordid(777l);
				vo3.setUv(10l);
				vo3.setKw("哈哈");
				vo3.setCompdg(0);
				vo3.setAwgdscnt(5l);
				
				returnValue.add(vo1);
				returnValue.add(vo2);
				returnValue.add(vo3);
				
				oneOf(krKeywordFacade).getRecommWord(with(any(Integer.class)), 
						with(any(Integer.class)), 
						with(any(String.class)), 
						with(any(List.class)), 
						with(any(Integer.class)), 
						with(any(Boolean.class)), 
						with(any(Integer.class)), 
						with(any(Integer.class)));
				will(returnValue(returnValue));
			}
		});
		
		GetKRBySeedRequest request = new GetKRBySeedRequest();
		request.setSeed("123");
		request.setGroupId(228);
		request.setRegionList(new int[]{9999,1});
		request.setAliveDays(15);
		request.setTargetType(7);
		
		BaseResponse<KRResultType> result  = apiKrService.getKRBySeed(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getErrors().get(0).getCode(), is(KrErrorCode.REGION_NOT_FOUND.getValue()));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(1));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	public void testGetKRBySeedNegative_GroupInvalid() throws Exception{
		final int userId = 499;
		 
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		context.checking(new Expectations() {
			{
				List<KrInfoVo> returnValue = new ArrayList<KrInfoVo>();
				KrInfoVo vo1 = new KrInfoVo();
				vo1.setWordid(123l);
				vo1.setUv(1000l);
				vo1.setKw("网盟123");
				vo1.setCompdg(6);
				vo1.setAwgdscnt(600l);
				
				KrInfoVo vo2 = new KrInfoVo();
				vo2.setWordid(456l);
				vo2.setUv(99l);
				vo2.setKw("helloWorld");
				vo2.setCompdg(1);
				vo2.setAwgdscnt(0l);
				
				KrInfoVo vo3 = new KrInfoVo();
				vo3.setWordid(777l);
				vo3.setUv(10l);
				vo3.setKw("哈哈");
				vo3.setCompdg(0);
				vo3.setAwgdscnt(5l);
				
				returnValue.add(vo1);
				returnValue.add(vo2);
				returnValue.add(vo3);
				
				oneOf(krKeywordFacade).getRecommWord(with(any(Integer.class)), 
						with(any(Integer.class)), 
						with(any(String.class)), 
						with(any(List.class)), 
						with(any(Integer.class)), 
						with(any(Boolean.class)), 
						with(any(Integer.class)), 
						with(any(Integer.class)));
				will(returnValue(returnValue));
			}
		});
		
		GetKRBySeedRequest request = new GetKRBySeedRequest();
		request.setSeed("123");
		request.setGroupId(999);
		//request.setRegionList(new int[]{9999,1});
		request.setAliveDays(15);
		request.setTargetType(7);
		
		BaseResponse<KRResultType> result  = apiKrService.getKRBySeed(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getErrors().get(0).getCode(), is(GlobalErrorCode.UNAUTHORIZATION.getValue()));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(1));
		
	}

	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	public void testGetKRBySeedNegative_TargetTypeInvalid() throws Exception{
		final int userId = 499;
		 
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		context.checking(new Expectations() {
			{
				List<KrInfoVo> returnValue = new ArrayList<KrInfoVo>();
				KrInfoVo vo1 = new KrInfoVo();
				vo1.setWordid(123l);
				vo1.setUv(1000l);
				vo1.setKw("网盟123");
				vo1.setCompdg(6);
				vo1.setAwgdscnt(600l);
				
				KrInfoVo vo2 = new KrInfoVo();
				vo2.setWordid(456l);
				vo2.setUv(99l);
				vo2.setKw("helloWorld");
				vo2.setCompdg(1);
				vo2.setAwgdscnt(0l);
				
				KrInfoVo vo3 = new KrInfoVo();
				vo3.setWordid(777l);
				vo3.setUv(10l);
				vo3.setKw("哈哈");
				vo3.setCompdg(0);
				vo3.setAwgdscnt(5l);
				
				returnValue.add(vo1);
				returnValue.add(vo2);
				returnValue.add(vo3);
				
				oneOf(krKeywordFacade).getRecommWord(with(any(Integer.class)), 
						with(any(Integer.class)), 
						with(any(String.class)), 
						with(any(List.class)), 
						with(any(Integer.class)), 
						with(any(Boolean.class)), 
						with(any(Integer.class)), 
						with(any(Integer.class)));
				will(returnValue(returnValue));
			}
		});
		
		GetKRBySeedRequest request = new GetKRBySeedRequest();
		request.setSeed("123");
		request.setGroupId(228);
		//request.setRegionList(new int[]{9999,1});
		request.setAliveDays(15);
		request.setTargetType(8);
		
		BaseResponse<KRResultType> result  = apiKrService.getKRBySeed(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getErrors().get(0).getCode(), is(KrErrorCode.KT_TARGETTYPE_INTERNAL_ERROR.getValue()));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(1));
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Rollback(true)
	public void testGetKRBySeedNegative_AliveDaysInvalid() throws Exception{
		final int userId = 499;
		 
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		context.checking(new Expectations() {
			{
				List<KrInfoVo> returnValue = new ArrayList<KrInfoVo>();
				KrInfoVo vo1 = new KrInfoVo();
				vo1.setWordid(123l);
				vo1.setUv(1000l);
				vo1.setKw("网盟123");
				vo1.setCompdg(6);
				vo1.setAwgdscnt(600l);
				
				KrInfoVo vo2 = new KrInfoVo();
				vo2.setWordid(456l);
				vo2.setUv(99l);
				vo2.setKw("helloWorld");
				vo2.setCompdg(1);
				vo2.setAwgdscnt(0l);
				
				KrInfoVo vo3 = new KrInfoVo();
				vo3.setWordid(777l);
				vo3.setUv(10l);
				vo3.setKw("哈哈");
				vo3.setCompdg(0);
				vo3.setAwgdscnt(5l);
				
				returnValue.add(vo1);
				returnValue.add(vo2);
				returnValue.add(vo3);
				
				oneOf(krKeywordFacade).getRecommWord(with(any(Integer.class)), 
						with(any(Integer.class)), 
						with(any(String.class)), 
						with(any(List.class)), 
						with(any(Integer.class)), 
						with(any(Boolean.class)), 
						with(any(Integer.class)), 
						with(any(Integer.class)));
				will(returnValue(returnValue));
			}
		});
		
		GetKRBySeedRequest request = new GetKRBySeedRequest();
		request.setSeed("123");
		request.setGroupId(228);
		//request.setRegionList(new int[]{9999,1});
		request.setAliveDays(99);
		request.setTargetType(7);
		
		BaseResponse<KRResultType> result  = apiKrService.getKRBySeed(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getErrors().get(0).getCode(), is(KrErrorCode.KT_ALIVEDAYS_ERROR.getValue()));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(1));
		
	}
	
		
}

