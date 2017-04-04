package com.baidu.beidou.api.external.tool.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.tool.error.ToolErrorCode;
import com.baidu.beidou.api.external.tool.exporter.ToolService;
import com.baidu.beidou.api.external.tool.vo.AdInfo;
import com.baidu.beidou.api.external.tool.vo.AdvancedPackType;
import com.baidu.beidou.api.external.tool.vo.KeywordPackType;
import com.baidu.beidou.api.external.tool.vo.OneReportRequestType;
import com.baidu.beidou.api.external.tool.vo.request.GetAdInfoRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetAdvancedPackByIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetAllAdvancedPackIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetAllKeywordPackIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetKeywordPackByIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetOneReportRequest;
import com.baidu.beidou.api.external.tool.vo.response.GetOneReportResponse;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
import com.baidu.beidou.util.DateUtils;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
@Ignore
public class ToolServiceImplTest  extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 19;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private ToolService toolService;

	@Test
	@Rollback(true)
	public void testGetAdInfo() throws Exception{
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetAdInfoRequest request = new GetAdInfoRequest();
		request.setAdIds(new long[]{89439993,89439985,89439977});
		System.out.println(user.getDataUser());
		BaseResponse<AdInfo> result  = toolService.getAdInfo(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(3));

		assertThat(((AdInfo)result.getData()[0]).getAdId(), is(1589576l));
		assertThat(((AdInfo)result.getData()[0]).getStatus(), is(3));
		assertThat(((AdInfo)result.getData()[0]).getRefuseReason(), nullValue());
		
		assertThat(((AdInfo)result.getData()[2]).getAdId(), is(1589573l));
		//assertThat(((AdInfo)result.getData()[2]).getStatus(), is(0));
		//assertThat(((AdInfo)result.getData()[2]).getRefuseReason(), is("请您修改文字内容，确保文字通顺完整，信息客观真实。"));
		assertThat(((AdInfo)result.getData()[2]).getRefuseReason(), is(""));
		assertThat(result.getOptions().getSuccess(), is(3));
		assertThat(result.getOptions().getTotal(), is(3));
		
	}
	
	@Test
	@Rollback(true)
	public void testGetAdInfo_NoAuth() throws Exception{
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetAdInfoRequest request = new GetAdInfoRequest();
		request.setAdIds(new long[]{2l,13375l});
		
		BaseResponse<AdInfo> result  = toolService.getAdInfo(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(2));
		//assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(2));
		
	}
	
	@Test
	@Rollback(true)
	public void testGetAdInfo_CanNotGetDeletedAd() throws Exception{ 
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetAdInfoRequest request = new GetAdInfoRequest();
		request.setAdIds(new long[]{1589573l,11135l});
		
		BaseResponse<AdInfo> result  = toolService.getAdInfo(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(((AdInfo)result.getData()[0]).getAdId(), is(1589573l));
		//assertThat(((AdInfo)result.getData()[0]).getStatus(), is(0));
		assertThat(((AdInfo)result.getData()[0]).getRefuseReason(), is(""));
		//assertThat(((AdInfo)result.getData()[0]).getRefuseReason(), is("请您修改文字内容，确保文字通顺完整，信息客观真实。"));
		
		assertThat(result.getErrors().get(0).getCode(), is(ToolErrorCode.NO_UNIT.getValue()));
		
		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(2));
		
	}
	
	
	@Test
	@Rollback(true)
	public void testGetAllKeywordPackId() throws Exception{
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetAllKeywordPackIdRequest request = new GetAllKeywordPackIdRequest();
		
		BaseResponse<Integer> result  = toolService.getAllKeywordPackId(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(11));
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getData()[0], is(1));
		assertThat(result.getData()[1], is(2));
		assertThat(result.getData()[2], is(3));
		
		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));
		
	}
	
	@Test
	@Rollback(true)
	public void testGetKeywordPackById() throws Exception{
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetKeywordPackByIdRequest request = new GetKeywordPackByIdRequest();
		request.setIds(new int[]{11111,1,8,3,2});
		
		BaseResponse<KeywordPackType> result  = toolService.getKeywordPackById(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getErrors().size(), is(2));
		assertThat(result.getData().length, is(3));
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(((KeywordPackType)(result.getData()[0])).getId(), is(1));
		assertThat(((KeywordPackType)(result.getData()[0])).getAliveDays(), is(15));
		assertThat(((KeywordPackType)(result.getData()[0])).getName(), is("计算机pack"));
		assertThat(((KeywordPackType)(result.getData()[0])).getTargetType(), is(7));
		
		assertThat(result.getOptions().getSuccess(), is(3));
		assertThat(result.getOptions().getTotal(), is(5));
		
	}
	
	@Test
	@Rollback(true)
	public void testGetKeywordPackById_Optimize() throws Exception{
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetKeywordPackByIdRequest request = new GetKeywordPackByIdRequest();
		request.setIds(new int[]{6});
		
		BaseResponse<KeywordPackType> result  = toolService.getKeywordPackById(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(((KeywordPackType)(result.getData()[0])).getId(), is(6));
		assertThat(((KeywordPackType)(result.getData()[0])).getName(), is("学生关键词"));
		
		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));
		
	}
	
	@Test
	@Rollback(true)
	public void testGetAllAdvancedPackId() throws Exception{
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetAllAdvancedPackIdRequest request = new GetAllAdvancedPackIdRequest();
		
		BaseResponse<Integer> result  = toolService.getAllAdvancedPackId(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(8));
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getData()[0], is(802));
		assertThat(result.getData()[1], is(801));
		assertThat(result.getData()[2], is(8));
		
		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));
		
	}
	
	@Test
	@Rollback(true)
	public void testGetAdvancedPackById() throws Exception{
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetAdvancedPackByIdRequest request = new GetAdvancedPackByIdRequest();
		request.setIds(new int[]{1111,1,999,3,2});
		
		BaseResponse<AdvancedPackType> result  = toolService.getAdvancedPackById(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getErrors().size(), is(2));
		assertThat(result.getData().length, is(3));
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(((AdvancedPackType)(result.getData()[0])).getId(), is(1));
		assertThat(((AdvancedPackType)(result.getData()[0])).getBasicPacks().get(0).getId(), is(1));
		assertThat(((AdvancedPackType)(result.getData()[0])).getBasicPacks().get(0).getType(), is(3));
		
		assertThat(result.getOptions().getSuccess(), is(3));
		assertThat(result.getOptions().getTotal(), is(5));
		
	}
		
	@Test
	@Rollback(true)
	public void testGetAdvancedPackById_Optimize() throws Exception{
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetAdvancedPackByIdRequest request = new GetAdvancedPackByIdRequest();
		request.setIds(new int[]{8});
		
		BaseResponse<AdvancedPackType> result  = toolService.getAdvancedPackById(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(((AdvancedPackType)(result.getData()[0])).getId(), is(8));
		assertThat(((AdvancedPackType)(result.getData()[0])).getName(), is("已优化高级组合"));
		assertThat(((AdvancedPackType)(result.getData()[0])).getBasicPacks().get(0).getId(), is(2));
		assertThat(((AdvancedPackType)(result.getData()[0])).getBasicPacks().get(0).getType(), is(3));
		
		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));
		
	}
	@Test
	@Rollback(true)
	public void getOneReport()throws Exception{
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions apiOption =  DarwinApiHelper.getBaseRequestOptions();
		GetOneReportRequest parameters = new GetOneReportRequest();
		OneReportRequestType request = new OneReportRequestType();
		String startDate = "20120413";
		String endDate = "20120416";
		request.setStartDate(DateUtils.strToDate(startDate));
		request.setEndDate(DateUtils.strToDate(endDate));
		request.setReportType(1);
		request.setStatRange(1);
		request.setStatIds(new long[]{});
		parameters.setOneReportRequestType(request);
		BaseResponse<GetOneReportResponse> result = toolService.getOneReport(user, parameters, apiOption);
		
		System.out.println(result);
	}
}

