package com.baidu.beidou.api.external.tool.exporter.impl;

import static org.hamcrest.Matchers.is;
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
import com.baidu.beidou.api.external.tool.exporter.ToolService;
import com.baidu.beidou.api.external.tool.vo.FCKeyword;
import com.baidu.beidou.api.external.tool.vo.SiteInfo;
import com.baidu.beidou.api.external.tool.vo.request.GetFCKeywordByUnitIdsRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetSiteInfoRequest;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.fengchao.BDUnit;
import com.baidu.beidou.fengchao.FcFacade;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
@Ignore
public class ToolServiceImplTest2 extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 19;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private ToolService toolService;
	
	@Resource
	private CproGroupConstantMgr cproGroupConstantMgr;
	
	protected Mockery context = new JUnit4Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	@Before
	public void beforeEach() {
	}

	@After
	public void afterEach() {
		context.assertIsSatisfied();
	}
	
	@Test
	public void testLoad() {
		cproGroupConstantMgr.loadSystemConf();
	}
	
	@Test
	@Rollback(true)
	public void testGetSiteInfo() throws Exception{
		final int userId = 499;
		 
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetSiteInfoRequest request = new GetSiteInfoRequest();
		request.setSites(new String[]{"sina.com.cn", "tieba.baidu.com", "nonono.cn"});
		
		BaseResponse<SiteInfo> result  = toolService.getSiteInfo(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData().length, is(2));
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(((SiteInfo)result.getData()[0]).getSite(), is("sina.com.cn"));
		assertThat(((SiteInfo)result.getData()[0]).getFirstTradeId(), is(20));
		assertThat(((SiteInfo)result.getData()[0]).getSecondTradeId(), is(20));
		
		assertThat(result.getOptions().getSuccess(), is(2));
		assertThat(result.getOptions().getTotal(), is(3));
		
	}
	
	@Test
	@Rollback(true)
	public void testGetFCKeywordByUnitIds() throws Exception{
		
		final int userId = 499;
		 
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		ToolServiceImpl service = (ToolServiceImpl)(toolService);
		
        final FcFacade fcFacade = context.mock(FcFacade.class);
		service.setFcFacade(fcFacade);
		
		context.checking(new Expectations() {
			{	
				List<BDUnit> fcUnitList = new ArrayList<BDUnit>();
				BDUnit type = new BDUnit();
				type.setUnitid(100l);
				type.setUnitname("myfcunit100");
				type.setPaused(false);
				fcUnitList.add(type);
				BDUnit type2 = new BDUnit();
				type2.setUnitid(200l);
				type2.setUnitname("myfcunit200");
				type2.setPaused(false);
				fcUnitList.add(type2);
				List<Long> unitIds = new ArrayList<Long>();
				unitIds.add(100l);
				unitIds.add(200l);
				unitIds.add(300l);
				allowing(fcFacade).getFcUnitListByUnitids(userId, unitIds, null);
				will(returnValue(fcUnitList));
			}
		});
		
		context.checking(new Expectations() {
			{
				List<String> returnValue = new ArrayList<String>();
				returnValue.add("测试关键词1");
				returnValue.add("测试关键词2");
				returnValue.add("测试关键词3");
				List<Long> param1 = new ArrayList<Long>();
				param1.add(100l);
				allowing(fcFacade).getFcKeywordsByUnitIds(userId, param1, CproGroupConstant.KT_KEYWORDS_ALL_MAX_NUM );
				will(returnValue(returnValue));
			}
		});
		
		context.checking(new Expectations() {
			{
				List<String> returnValue = new ArrayList<String>();
				returnValue.add("电话");
				returnValue.add("洗衣机");
				returnValue.add("电脑");
				List<Long> param1 = new ArrayList<Long>();
				param1.add(200l);
				allowing(fcFacade).getFcKeywordsByUnitIds(userId, param1, CproGroupConstant.KT_KEYWORDS_ALL_MAX_NUM );
				will(returnValue(returnValue));
			}
		});
		
		context.checking(new Expectations() {
			{
				List<String> returnValue = new ArrayList<String>();
				List<Long> param1 = new ArrayList<Long>();
				param1.add(300l);
				allowing(fcFacade).getFcKeywordsByUnitIds(userId, param1, CproGroupConstant.KT_KEYWORDS_ALL_MAX_NUM );
				will(returnValue(returnValue));
			}
		});
		
		GetFCKeywordByUnitIdsRequest request = new GetFCKeywordByUnitIdsRequest();
		request.setUnitIds(new long[]{100l,200l,300l});
		
		BaseResponse<FCKeyword> result  = service.getFCKeywordByUnitIds(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData().length, is(2));
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(((FCKeyword)result.getData()[0]).getKeywords()[0], is("测试关键词1"));
		assertThat(((FCKeyword)result.getData()[1]).getKeywords()[0], is("电话"));
		
		assertThat(result.getOptions().getSuccess(), is(2));
		assertThat(result.getOptions().getTotal(), is(3));
		
	}
	
}

