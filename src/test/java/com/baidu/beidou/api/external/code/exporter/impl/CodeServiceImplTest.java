package com.baidu.beidou.api.external.code.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.code.exporter.CodeService;
import com.baidu.beidou.api.external.code.vo.Category;
import com.baidu.beidou.api.external.code.vo.Region;
import com.baidu.beidou.api.external.code.vo.request.GetAllCategoryRequest;
import com.baidu.beidou.api.external.code.vo.request.GetAllRegionRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
@Ignore
public class CodeServiceImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 19;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private CodeService codeService;
	
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

	// -- getAllCategory test cases --//
	@Test
	public void testGetAllCategory() {
		final int userId = 480786;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId,userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		GetAllCategoryRequest request = new GetAllCategoryRequest();
		
		ApiResult<Category> result  = codeService.getAllCategory(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), greaterThan(160));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		Category catagory = result.getData().get(0);
		assertThat(catagory.getCategoryId(), is(1));
		assertThat(catagory.getName(), is("音乐影视"));
		assertThat(catagory.getParentId(), is(0));
		
		Category catagory2 = result.getData().get(1);
		assertThat(catagory2.getCategoryId(), is(2));
		assertThat(catagory2.getName(), is("休闲娱乐"));
		assertThat(catagory2.getParentId(), is(0));		
	}
	
	// -- getAllRegion test cases --//
	@Test
	public void testGetAllRegion() {
		final int userId = 480786;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId,userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		GetAllRegionRequest request = new GetAllRegionRequest();
		request.setVersion("1.1");
		
		ApiResult<Region> result  = codeService.getAllRegion(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), greaterThan(460));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		Region region = result.getData().get(0);
		assertThat(region.getName(), is("北京"));
		assertThat(region.getParentId(), is(0));
		assertThat(region.getRegionId(), is(1));
		assertThat(region.getType(), is(1));
	}
	
}
