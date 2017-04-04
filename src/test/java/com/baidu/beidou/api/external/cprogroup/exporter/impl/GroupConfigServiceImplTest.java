package com.baidu.beidou.api.external.cprogroup.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.error.GroupErrorCode;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeIpType;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeSiteType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeIpType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeSiteType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupInterestInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupKeywordItemType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupRegionType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupRtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupSiteType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupTradePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupTradeType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupVtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.InterestInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.KtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.RegionConfigType;
import com.baidu.beidou.api.external.cprogroup.vo.RegionItemType;
import com.baidu.beidou.api.external.cprogroup.vo.RtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.RtRelationType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteConfigType;
import com.baidu.beidou.api.external.cprogroup.vo.SitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteUrlItemType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteUrlType;
import com.baidu.beidou.api.external.cprogroup.vo.TargetInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.TradePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.TradeSitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.VtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.WordType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddRegionRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddRtRelationRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddTradePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddTradeRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddVtPeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteRegionRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteRtRelationRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteTradePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteTradeRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteVtPeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetKeywordByWordIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetRegionConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetSiteUrlRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetTargetInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetTradeSitePriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeIpRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeSiteRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetInterestInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetRegionConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetSiteConfigRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetSiteUrlRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetTargetInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetTradeSitePriceRequest;
import com.baidu.beidou.api.external.util.vo.ApiError;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.SuccessObject;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.test.common.BasicTestCaseLegacy;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
@Ignore
public class GroupConfigServiceImplTest extends BasicTestCaseLegacy {

	@Resource
	private GroupConfigService groupConfigService;
	
	@Resource
	private CproGroupConstantMgr cproGroupConstantMgr;
	
	@Test
	public void testLoad() {
		cproGroupConstantMgr.loadSystemConf();
	}
	
	@Test
	public void testGetTargetInfoForNone() {
		final int userId = 499;
		final int groupId = 244553;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetTargetInfoRequest request = new GetTargetInfoRequest();
		request.setGroupIds(new long[]{groupId});
		ApiResult<TargetInfoType> result  = groupConfigService.getTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		TargetInfoType info = (TargetInfoType) result.getData().get(0);
		assertThat(info.getType(), is(3));
	}
	
	@Test
	public void testSetTargetInfoForNone() {
		final int userId = 499;
		 
		final int groupId = 245320;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		targetInfo.setGroupId(groupId);
		targetInfo.setType(3);
		request.setTargetInfo(targetInfo);
		
		ApiResult<Object> result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
		
		GetTargetInfoRequest request2 = new GetTargetInfoRequest();
		request2.setGroupIds(new long[]{groupId});
		ApiResult<TargetInfoType> result2  = groupConfigService.getTargetInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		TargetInfoType info2 = (TargetInfoType) result2.getData().get(0);
		assertThat(info2.getType(), is(3));
		
	}
	
	//@Test
	public void testGetTargetInfoForRT() {
		final int userId = 499;
		final int groupId = 245321;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetTargetInfoRequest request = new GetTargetInfoRequest();
		request.setGroupIds(new long[]{groupId});
		ApiResult<TargetInfoType> result  = groupConfigService.getTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		TargetInfoType info = (TargetInfoType) result.getData().get(0);
		assertThat(info.getType(), is(1));
	}
	
	//@Test
	public void testSetTargetInfoForRT() {
		final int userId = 499;
		 
		final int groupId = 244542;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		targetInfo.setGroupId(groupId);
		targetInfo.setType(1);
		RtItemType rtItem = new RtItemType();
		rtItem.setAliveDays(30);
		List<RtRelationType> rtList = new ArrayList<RtRelationType>();
		RtRelationType type = new RtRelationType();
		type.setFcPlanId(67180836L);
		type.setFcPlanName("123");
		type.setRelationType(1);
		rtList.add(type);
		type = new RtRelationType();
		type.setFcPlanId(67147881L);
		type.setFcPlanName("123");
		type.setFcUnitId(93928095L);
		type.setFcUnitName("456");
		type.setRelationType(0);
		rtList.add(type);
		rtItem.setRtRelationList(rtList);
		targetInfo.setRtItem(rtItem);
		request.setTargetInfo(targetInfo);
		
		ApiResult<Object> result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
		
		GetTargetInfoRequest request2 = new GetTargetInfoRequest();
		request2.setGroupIds(new long[]{groupId});
		ApiResult<TargetInfoType> result2  = groupConfigService.getTargetInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		TargetInfoType info2 = (TargetInfoType) result2.getData().get(0);
		assertThat(info2.getType(), is(1));
		assertThat(info2.getRtItem().getRtRelationList().get(0).getFcPlanName(), is("query地域测试"));
		
	}
	
	@Test
	public void testGetTargetInfoForKT() {
		final int userId = 499;
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetTargetInfoRequest request = new GetTargetInfoRequest();
		request.setGroupIds(new long[]{groupId});
		ApiResult<TargetInfoType> result  = groupConfigService.getTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		TargetInfoType info = (TargetInfoType) result.getData().get(0);
		assertThat(info.getType(), is(2));
	}
	
	
	@Test
	public void testSetTargetInfoForKT() {
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		targetInfo.setGroupId(groupId);
		targetInfo.setType(2);
		KtItemType ktItem = new KtItemType();
		KeywordType[] wordList = new KeywordType[3];
		ktItem.setTargetType(3);
		ktItem.setAliveDays(7);
		KeywordType word = new KeywordType();
		word.setKeyword("111");
		wordList[0] = word;
		word = new KeywordType();
		word.setKeyword("222");
		wordList[1] = word;
		word = new KeywordType();
		word.setKeyword("333");
		wordList[2] = word;

		ktItem.setKtWordList(wordList);
		targetInfo.setKtItem(ktItem);
		request.setTargetInfo(targetInfo);
		
		ApiResult<Object> result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
		
		GetTargetInfoRequest request2 = new GetTargetInfoRequest();
		request2.setGroupIds(new long[]{groupId});
		ApiResult<TargetInfoType> result2  = groupConfigService.getTargetInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		TargetInfoType info2 = (TargetInfoType) result2.getData().get(0);
		assertThat(info2.getType(), is(2));
		assertThat(info2.getKtItem().getAliveDays(), is(7));
		assertThat(info2.getKtItem().getTargetType(), is(3));
		assertThat(info2.getKtItem().getKtWordList().length, is(3));
		assertThat(info2.getKtItem().getKtWordList()[0].getKeyword(), is("111"));
		assertThat(info2.getKtItem().getKtWordList()[1].getKeyword(), is("333"));

	}
	
	@Test
	public void testSetTargetInfoForKTNegative() {
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		targetInfo.setGroupId(groupId);
		targetInfo.setType(2);
		KtItemType ktItem = new KtItemType();
		ktItem.setTargetType(3);
		KeywordType[] wordList = new KeywordType[3];
		KeywordType word = new KeywordType();
		word.setKeyword("111");
		wordList[0] = word;
		ktItem.setKtWordList(wordList);
		targetInfo.setKtItem(ktItem);
		request.setTargetInfo(targetInfo);
		
		targetInfo.setType(5); // 错误的TargetType 
		ApiResult<Object> result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.TARGETTYPE_TYPE_ERROR.getValue()) );
		targetInfo.setType(2); // 修正为正确的
		
		ktItem.setTargetType(8); // 错误的KT TargetType 
		result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.KT_TARGETTYPE_ERROR.getValue()) );
		ktItem.setTargetType(3); // 修正为正确的
		
		ktItem.setAliveDays(999); // 错误的KT 有效期
		result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.KT_ALIVEDAYS_ERROR.getValue()) );
		ktItem.setAliveDays(7); // 修正为正确的
		
		KeywordType word1 = new KeywordType(); // 重复的KT词
		word1.setKeyword("111");
		wordList[1] = word1;
		result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.KT_WORDS_DUP.getValue()) );
		word1.setKeyword("222"); // 修正为正确的
		
		word1.setKeyword("12345678901234567890123456789012345678901234567890"); // 超长的KT词
		result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.KT_WORDS_ERROR.getValue()) );
		word1.setKeyword("222"); // 修正为正确的
		
		word1.setKeyword("    "); // 为空的KT词
		result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.KT_WORDS_ERROR.getValue()) );
		word1.setKeyword("222"); // 修正为正确的
		
	}
	
	
	@Test
	public void testGetTargetInfoForVT() {
		final int userId = 499;
		final int groupId = 245322;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetTargetInfoRequest request = new GetTargetInfoRequest();
		request.setGroupIds(new long[]{groupId});
		ApiResult<TargetInfoType> result  = groupConfigService.getTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		TargetInfoType info = (TargetInfoType) result.getData().get(0);
		assertThat(info.getType(), is(4));
	}
	
	@Test
	public void testSetTargetInfoForVT() {
		final int userId = 499;
		 
		final int groupId = 244543;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		targetInfo.setGroupId(groupId);
		targetInfo.setType(4);
		VtItemType vtItem = new VtItemType();
		vtItem.setRelatedPeopleIds(new Long[]{15L, 16L});
		vtItem.setUnRelatePeopleIds(new Long[]{17L});
		targetInfo.setVtItem(vtItem);
		request.setTargetInfo(targetInfo);
		
		ApiResult<Object> result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
		
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		GetTargetInfoRequest request2 = new GetTargetInfoRequest();
		request2.setGroupIds(new long[]{groupId});
		ApiResult<TargetInfoType> result2  = groupConfigService.getTargetInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		TargetInfoType info2 = (TargetInfoType) result2.getData().get(0);
		assertThat(info2.getType(), is(4));
		assertThat(info2.getVtItem().getRelatedPeopleIds().length, is(2));
		assertThat(info2.getVtItem().getRelatedPeopleIds()[0], is(16l));
		assertThat(info2.getVtItem().getUnRelatePeopleIds().length, is(1));
		assertThat(info2.getVtItem().getUnRelatePeopleIds()[0], is(17l));

	}
	
	@Test
	public void testGetSiteConfig() {
		
	}
	
	@Test
	public void testSetSiteConfig() {
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetSiteConfigRequest request = new SetSiteConfigRequest();
		SiteConfigType siteConfig = new SiteConfigType();
		siteConfig.setGroupId(groupId);
		siteConfig.setAllSite(false);
		String[] siteList = new String[] { "sina.com.cn", "tieba.baidu.com" };
		siteConfig.setSiteList(siteList);
		int[] categoryList = new int[] { 1, 2, 201 };
		siteConfig.setCategoryList(categoryList);
		request.setSiteConfig(siteConfig);
		
		ApiResult<Object> result  = groupConfigService.setSiteConfig(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testGetRegionConfig() {
		// 执行完毕set后进行数据check
		this.testSetRegionConfig();
		
		final int userId = 499;
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetRegionConfigRequest request = new GetRegionConfigRequest();
		request.setGroupIds(new long[]{groupId});
		request.setVersion("1.1");
		ApiResult<RegionConfigType> result  = groupConfigService.getRegionConfig(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
	}
	
	@Test
	public void testSetRegionConfig() {
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetRegionConfigRequest request = new SetRegionConfigRequest();
		RegionConfigType siteConfig = new RegionConfigType();
		siteConfig.setGroupId(groupId);
		siteConfig.setAllRegion(false);
		
		// type: 1, regionId: 1, name: 北京市
		// type: 1, regionId: 742, name: 昌平区
		// type: 1, regionId: 2, name: 上海市
		RegionItemType[] regions = new RegionItemType[3];
		RegionItemType region = new RegionItemType();
		region.setType(1);
		region.setRegionId(1);
		regions[0] = region;
		region = new RegionItemType();
		region.setType(1);
		region.setRegionId(742);
		regions[1] = region;
		region = new RegionItemType();
		region.setType(1);
		region.setRegionId(2);
		regions[2] = region;

		siteConfig.setRegionList(regions);
		request.setRegionConfig(siteConfig);
		request.setVersion("1.1");
		
		ApiResult<Object> result  = groupConfigService.setRegionConfig(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testGetExcludeIp() {
		// 执行完毕set后进行数据check
		this.testSetExcludeIp();
		
		final int userId = 499;
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetExcludeIpRequest request = new GetExcludeIpRequest();
		request.setGroupIds(new long[]{groupId});
		ApiResult<ExcludeIpType> result  = groupConfigService.getExcludeIp(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
	}
	
	@Test
	public void testSetExcludeIp() {
		final int userId = 499;
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		SetExcludeIpRequest request = new SetExcludeIpRequest();
		ExcludeIpType excludeIp = new ExcludeIpType();
		String[] ipList = new String[] {
			"111.111.111.1",
			"111.111.111.11"
		};
		excludeIp.setExcludeIp(ipList);
		excludeIp.setGroupId(groupId);
		request.setExcludeIp(excludeIp);
		
		ApiResult<Object> result  = groupConfigService.setExcludeIp(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
//		this.testGetExcludeIp();
		
	}
	
	@Test
	public void testSetExcludeIpForEmpty() {
		this.testSetExcludeIp();
		final int userId = 499;
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		SetExcludeIpRequest request = new SetExcludeIpRequest();
		ExcludeIpType excludeIp = new ExcludeIpType();
		
		String[] ipList = new String[1];
		ipList[0] = "$";
		excludeIp.setExcludeIp(ipList);
		excludeIp.setGroupId(groupId);
		request.setExcludeIp(excludeIp);
		
		ApiResult<Object> result  = groupConfigService.setExcludeIp(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
//		this.testGetExcludeIp();
	}
	
	@Test
	public void testGetExcludeSite() {
		// 执行完毕set后进行数据check
		this.testSetExcludeSite();
		
		final int userId = 499;
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetExcludeSiteRequest request = new GetExcludeSiteRequest();
		request.setGroupIds(new long[]{groupId});
		ApiResult<ExcludeSiteType> result  = groupConfigService.getExcludeSite(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
	}
	
	@Test
	public void testSetExcludeSite() {
		final int userId = 499;
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		SetExcludeSiteRequest request = new SetExcludeSiteRequest();
		ExcludeSiteType excludeSite = new ExcludeSiteType();
		String[] siteList = new String[] {
			"baidu.com",
			"tieba.baidu.com"
		};
		excludeSite.setExcludeSite(siteList);
		excludeSite.setGroupId(groupId);
		request.setExcludeSite(excludeSite);
		
		ApiResult<Object> result  = groupConfigService.setExcludeSite(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
//		this.testGetExcludeIp();
		
	}
	
	@Test
	public void testSetExcludeSiteForEmpty() {
		final int userId = 499;
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		SetExcludeSiteRequest request = new SetExcludeSiteRequest();
		ExcludeSiteType excludeSite = new ExcludeSiteType();
		
		String[] siteList = new String[0];
		excludeSite.setExcludeSite(siteList);
		excludeSite.setGroupId(groupId);
		request.setExcludeSite(excludeSite);
		
		ApiResult<Object> result  = groupConfigService.setExcludeSite(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
//		this.testGetExcludeIp();
	}
	
	@Test
	public void testGetTradeSitePrice() {
		// 执行完毕set后进行数据check
		this.testSetTradeSitePrice();
		
		final int userId = 499;
		final int groupId = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetTradeSitePriceRequest request = new GetTradeSitePriceRequest();
		request.setGroupIds(new long[]{groupId});
		ApiResult<TradeSitePriceType> result  = groupConfigService.getTradeSitePrice(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
	}
	
	@Test
	public void testSetTradeSitePrice() {
		final int userId = 499;
		final int groupId = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		SetTradeSitePriceRequest request = new SetTradeSitePriceRequest();
		TradeSitePriceType tradeSitePrice = new TradeSitePriceType();
		
		List<TradePriceType> tradePriceList = new ArrayList<TradePriceType>();
		TradePriceType tradePrice = new TradePriceType();
		tradePrice.setTradeId(2);
		tradePrice.setPrice(100);
		tradePriceList.add(tradePrice);
		tradeSitePrice.setTradePriceList(tradePriceList);
		
		List<SitePriceType> sitePriceList = new ArrayList<SitePriceType>();
		SitePriceType sitePrice = new SitePriceType();
		sitePrice.setSite("sina.com.cn");
		sitePrice.setPrice(100);
		sitePriceList.add(sitePrice);
		tradeSitePrice.setSitePriceList(sitePriceList);
		
		tradeSitePrice.setGroupId(groupId);
		request.setTradeSitePrice(tradeSitePrice);
		
		ApiResult<Object> result  = groupConfigService.setTradeSitePrice(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
//		this.testGetExcludeIp();
		
	}
	
	@Test
	public void testSetTradeSitePriceForEmpty() {
		final int userId = 499;
		final int groupId = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		SetTradeSitePriceRequest request = new SetTradeSitePriceRequest();
		TradeSitePriceType tradeSitePrice = new TradeSitePriceType();
		
		List<TradePriceType> tradePriceList = new ArrayList<TradePriceType>();
		TradePriceType tradePrice = new TradePriceType();
		tradePrice.setTradeId(2);
		tradePrice.setPrice(-1);
		tradePriceList.add(tradePrice);
		tradeSitePrice.setTradePriceList(tradePriceList);		
		List<SitePriceType> sitePriceList = new ArrayList<SitePriceType>();
		SitePriceType sitePrice = new SitePriceType();
		sitePrice.setSite("sina.com.cn");
		sitePrice.setPrice(-1);
		sitePriceList.add(sitePrice);
		tradeSitePrice.setSitePriceList(sitePriceList);
		
		tradeSitePrice.setGroupId(groupId);
		request.setTradeSitePrice(tradeSitePrice);
		
		ApiResult<Object> result  = groupConfigService.setTradeSitePrice(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
//		this.testGetExcludeIp();
	}
	
	@Test
	public void testGetSiteUrl() {
		// 执行完毕set后进行数据check
		this.testSetSiteUrl();
		
		final int userId = 499;
		final int groupId = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetSiteUrlRequest request = new GetSiteUrlRequest();
		request.setGroupIds(new long[]{groupId});
		ApiResult<SiteUrlType> result  = groupConfigService.getSiteUrl(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
	}
	
	@Test
	public void testSetSiteUrl() {
		final int userId = 499;
		final int groupId = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		SetSiteUrlRequest request = new SetSiteUrlRequest();
		SiteUrlType siteUrl = new SiteUrlType();
		
		List<SiteUrlItemType> siteUrlList = new ArrayList<SiteUrlItemType>();
		SiteUrlItemType item = new SiteUrlItemType();
		item.setSiteUrl("sina.com.cn");
		item.setTargetUrl("http://winworld.cn");
		siteUrlList.add(item);
		siteUrl.setSiteUrlList(siteUrlList);
		siteUrl.setGroupId(groupId);
		request.setSiteUrl(siteUrl);
		
		ApiResult<Object> result  = groupConfigService.setSiteUrl(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
//		this.testGetExcludeIp();
		
	}
	
	@Test
	public void testSetSiteUrlForEmpty() {
		final int userId = 499;
		final int groupId = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		SetSiteUrlRequest request = new SetSiteUrlRequest();
		SiteUrlType siteUrl = new SiteUrlType();
		
		List<SiteUrlItemType> siteUrlList = new ArrayList<SiteUrlItemType>();
		siteUrl.setSiteUrlList(siteUrlList);
		siteUrl.setGroupId(groupId);
		request.setSiteUrl(siteUrl);
		
		ApiResult<Object> result  = groupConfigService.setSiteUrl(dataUser, request, apiOption);;
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
//		this.testGetExcludeIp();
	}
	
	@Test
	public void testAddKeyword() {
		this.prepareDataForTestAddKeyword001();
		this.prepareDataForTestAddKeyword002();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddKeywordRequest request = new AddKeywordRequest();
		GroupKeywordItemType[] array = new GroupKeywordItemType[6];
		GroupKeywordItemType item1 = new GroupKeywordItemType();
		item1.setGroupId(245326);
		item1.setKeyword("abc");
		GroupKeywordItemType item2 = new GroupKeywordItemType();
		item2.setGroupId(245326);
		item2.setKeyword("API");
		GroupKeywordItemType item3 = new GroupKeywordItemType();
		item3.setGroupId(245326);
		item3.setKeyword("百度网络联盟");
		item3.setPattern(1);
		GroupKeywordItemType item4 = new GroupKeywordItemType();
		item4.setGroupId(245326);
		item4.setKeyword("做RD苦啊");
		item4.setPattern(1);
		GroupKeywordItemType item5 = new GroupKeywordItemType();
		item5.setGroupId(245327);
		item5.setKeyword("neo");
		item5.setPattern(1);
		GroupKeywordItemType item6 = new GroupKeywordItemType();
		item6.setGroupId(245327);
		item6.setKeyword("remind");
		item6.setPattern(1);
		array[0] = item1;
		array[1] = item2;
		array[2] = item3;
		array[3] = item4;
		array[4] = item5;
		array[5] = item6;
		
		request.setKeywords(array);
		
		ApiResult<Object> result  = groupConfigService.addKeyword(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(6));
		assertThat(result.getPayment().getSuccess(), is(6));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
		
		GetTargetInfoRequest request2 = new GetTargetInfoRequest();
		request2.setGroupIds(new long[]{245326});
		ApiResult<TargetInfoType> result2  = groupConfigService.getTargetInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList().length , is(7));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[0].getKeyword() , is("做RD苦啊"));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[0].getPattern() , is(0));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[0].getQualification() , is(3));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[1].getKeyword() , is("abc"));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[1].getPattern() , is(0));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[1].getQualification() , is(1));
		
	}
	
	
	
	@Test
	public void testAddKeywordNegative() {
		this.prepareDataForTestAddKeyword001();
		this.prepareDataForTestAddKeyword002();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddKeywordRequest request = new AddKeywordRequest();
		GroupKeywordItemType[] array = new GroupKeywordItemType[12];
		GroupKeywordItemType item1 = new GroupKeywordItemType();
		item1.setGroupId(245326);
		item1.setKeyword("java是神马");  //already exist in db, even case sensitive
		GroupKeywordItemType item2 = new GroupKeywordItemType();
		item2.setGroupId(245326);
		item2.setKeyword("超长词1234567890超长词1234567890超长词1234567890超长词1234567890超长词1234567890");  //too long
		GroupKeywordItemType item3 = new GroupKeywordItemType();
		item3.setGroupId(245326);
		item3.setKeyword("重复词");
		item3.setPattern(1);
		GroupKeywordItemType item4 = new GroupKeywordItemType();
		item4.setGroupId(245326);
		item4.setKeyword("重复词");  // duplicate input
		GroupKeywordItemType item5 = new GroupKeywordItemType();
		item5.setGroupId(245326);
		item5.setKeyword("没有groupId啊"); 
		GroupKeywordItemType item6 = new GroupKeywordItemType();
		item6.setGroupId(245326);
		item6.setKeyword("没有keyword啊");  
		GroupKeywordItemType item7 = new GroupKeywordItemType();
		item7.setGroupId(245326);
		item7.setKeyword("推广组不存在");  
		GroupKeywordItemType item8 = new GroupKeywordItemType();
		item8.setGroupId(244543);
		item8.setKeyword("推广组定向方式不对");  // 推广组定向方式不对
		GroupKeywordItemType item9 = new GroupKeywordItemType();
		item9.setGroupId(245326);
		item9.setKeyword("百度网络联盟");
		GroupKeywordItemType item10 = new GroupKeywordItemType();
		item10.setGroupId(245326);
		item10.setKeyword("做Rd苦啊"); 
		item10.setPattern(1);
		GroupKeywordItemType item11 = new GroupKeywordItemType();
		item11.setGroupId(245327);
		item11.setKeyword("neo");
		item11.setPattern(1);
		GroupKeywordItemType item12 = new GroupKeywordItemType();
		item12.setGroupId(245327);
		item12.setKeyword("高尔夫");  //already exist in db
		item12.setPattern(1);
		array[0] = item1;
		array[1] = item2;
		array[2] = item3;
		array[3] = item4;
		array[4] = item5;
		array[5] = item6;
		array[6] = item7;
		array[7] = item8;
		array[8] = item9;
		array[9] = item10;
		array[10] = item11;
		array[11] = item12;
		
		request.setKeywords(array);
		
		ApiResult<Object> result  = groupConfigService.addKeyword(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		/**
{"code":5059,"content":null,"message":"KT word already exists","position":"_param.keywords[11].keyword"},
{"code":5114,"content":null,"message":"The type of targeted group is not keyword type","position":"_param.keywords[7].groupId"},
{"code":5059,"content":null,"message":"KT word already exists","position":"_param.keywords[0].keyword"},
{"code":5037,"content":null,"message":"Invalid words for KT","position":"_param.keywords[1].keyword"},
{"code":5059,"content":null,"message":"Duplicate KT words","position":"_param.keywords[3].keyword"}]	
		 */
		assertThat(result.getErrors().size(), is(5));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.KT_WORD_ALREADY_EXISTS.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(1))).getCode(), is(GroupConfigErrorCode.KT_GROUP_TYPE_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(2))).getCode(), is(GroupConfigErrorCode.KT_WORD_ALREADY_EXISTS.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(3))).getCode(), is(GroupConfigErrorCode.KT_WORDS_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(4))).getCode(), is(GroupConfigErrorCode.KT_WORDS_DUP.getValue()) );
		assertThat(result.getPayment().getTotal(), is(12));
		assertThat(result.getPayment().getSuccess(), is(7));
		
	}
	
	//@Test
	public void testGetKeywordByWordId() {
		this.prepareDataForTestAddKeyword001();
		final int userId = 499;
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		
		GetKeywordByWordIdRequest request = new GetKeywordByWordIdRequest();
		request.setWordIds(new long[]{324884764L, 324884765L, 324884766L});
		
		BaseResponse<WordType> result  = groupConfigService.getKeywordByWordId(user, request, options);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getOptions().getSuccess(), is(3));
		assertThat(result.getOptions().getTotal(), is(3));
	
		assertThat( ((WordType)(result.getData()[0])).getKeyword() , is("smalltalk不小声说话"));
		assertThat( ((WordType)(result.getData()[1])).getKeyword() , is("java是神马"));
		assertThat( ((WordType)(result.getData()[2])).getKeyword() , is("python是神器"));
	}
	
	@Test
	public void testGetKeyword() {
		this.prepareDataForTestAddKeyword001();
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetKeywordRequest request = new GetKeywordRequest();
		request.setKeywordIds(new Long[]{3715236l, 3715215l, 3715245l});
		
		ApiResult<KeywordType> result  = groupConfigService.getKeyword(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(3));
		assertThat(result.getPayment().getSuccess(), is(3));
		
		// 验证结果内容
		/*
		 * [
    
    {
        "keyword": "百度",
        "pattern": 0,
        "qualification": 3
    },
    {
        "keyword": "函授大专",
        "pattern": 0,
        "qualification": 3
    },
    {
        "keyword": "鲜花饼",
        "pattern": 0,
        "qualification": 3
    },
    {
        "keyword": "A95622130-51183-3",
        "pattern": 0,
        "qualification": 3
    }]
		 */

		assertThat( ((KeywordType)(result.getData().get(0))).getKeyword() , is("函授大专"));
		assertThat( ((KeywordType)(result.getData().get(1))).getKeyword() , is("鲜花饼"));
		assertThat( ((KeywordType)(result.getData().get(2))).getKeyword() , is("A95622130-51183-3"));
	}
	
	@Test
	public void testGetKeywordNegative() {
		this.prepareDataForTestAddKeyword001();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetKeywordRequest request = new GetKeywordRequest();
		request.setKeywordIds(new Long[]{999999l, 3715236l, 888888l, 3715245l});
		
		ApiResult<KeywordType> result  = groupConfigService.getKeyword(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(4));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		/*
		 * [
    
    {
        "keyword": "百度",
        "pattern": 0,
        "qualification": 3
    },
    {
        "keyword": "函授大专",
        "pattern": 0,
        "qualification": 3
    },
    {
        "keyword": "鲜花饼",
        "pattern": 0,
        "qualification": 3
    },
    {
        "keyword": "A95622130-51183-3",
        "pattern": 0,
        "qualification": 3
    }]
		 */
		
		assertThat( ( (ApiError)(result.getErrors().get(0)) ).getCode() , is(GroupErrorCode.KEYWORD_NOT_FOUND.getValue()));
		assertThat( ((KeywordType)(result.getData().get(0))).getKeyword() , is("函授大专"));
		assertThat( ((KeywordType)(result.getData().get(1))).getKeyword() , is("A95622130-51183-3"));
	}
	
	@Test
	public void testSetKeyword() {
		/*
		 * 在delete之前。
		 */
		this.prepareDataForTestAddKeyword001();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetKeywordRequest request = new SetKeywordRequest();
		GroupKeywordItemType[] array = new GroupKeywordItemType[3];
		GroupKeywordItemType item1 = new GroupKeywordItemType();
		item1.setGroupId(245326);
		item1.setKeyword("java是神马");
		GroupKeywordItemType item2 = new GroupKeywordItemType();
		item2.setGroupId(245326);
		item2.setKeyword("python是神器"); // case in-sensitive is not ok when check unauth
		GroupKeywordItemType item3 = new GroupKeywordItemType();
		item3.setGroupId(244536);
		item3.setKeyword("test9");  //初始化就存在的关键词
		array[0] = item1;
		array[1] = item2;
		array[2] = item3;
		
		request.setKeywords(array);
		
		ApiResult<Object> result  = groupConfigService.setKeyword(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(3));
		//assertThat(result.getPayment().getSuccess(), is(5));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
		
		GetTargetInfoRequest request2 = new GetTargetInfoRequest();
		request2.setGroupIds(new long[]{245326});
		ApiResult<TargetInfoType> result2  = groupConfigService.getTargetInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList().length , is(3));
		//assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[1].getKeyword() , is("python是神器"));
		
	}
	
	@Test
	public void testDeleteKeyword() {
		this.prepareDataForTestAddKeyword001();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteKeywordRequest request = new DeleteKeywordRequest();
		GroupKeywordItemType[] array = new GroupKeywordItemType[2];
		GroupKeywordItemType item1 = new GroupKeywordItemType();
		item1.setGroupId(245326);
		item1.setKeyword("java是神马");
		GroupKeywordItemType item2 = new GroupKeywordItemType();
		item2.setGroupId(245326);
		item2.setKeyword("python是神器"); // case in-sensitive is not ok when checking auth
		array[0] = item1;
		array[1] = item2;
		
		request.setKeywords(array);
		
		ApiResult<Object> result  = groupConfigService.deleteKeyword(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
		
		GetTargetInfoRequest request2 = new GetTargetInfoRequest();
		request2.setGroupIds(new long[]{245326});
		ApiResult<TargetInfoType> result2  = groupConfigService.getTargetInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList().length , is(1));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[0].getKeyword() , is("smalltalk不小声说话"));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[0].getPattern() , is(0));
		assertThat( ((TargetInfoType)(result2.getData().get(0))).getKtItem().getKtWordList()[0].getQualification() , is(3));
				
	}
	
	@Test
	public void testDeleteKeywordNegative() {
		this.prepareDataForTestAddKeyword001();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteKeywordRequest request = new DeleteKeywordRequest();
		GroupKeywordItemType[] array = new GroupKeywordItemType[4];
		GroupKeywordItemType item1 = new GroupKeywordItemType();
		item1.setGroupId(245326);
		item1.setKeyword("123");
		GroupKeywordItemType item2 = new GroupKeywordItemType();
		item2.setGroupId(244536);
		item2.setKeyword("456"); 
		GroupKeywordItemType item3 = new GroupKeywordItemType();
		item3.setGroupId(245326);
		item3.setKeyword("456"); 
		GroupKeywordItemType item4 = new GroupKeywordItemType();
		item4.setGroupId(244536);
		item4.setKeyword("789");  
		item4.setPattern(1);
		array[0] = item1;
		array[1] = item2;
		array[2] = item3;
		array[3] = item4;
		
		request.setKeywords(array);
		
		ApiResult<Object> result  = groupConfigService.deleteKeyword(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(4));
		assertThat(result.getPayment().getTotal(), is(4));
		assertThat(result.getPayment().getSuccess(), is(0));
		
		// 验证结果内容
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.KEYWORD_NOT_FOUND_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(1))).getCode(), is(GroupConfigErrorCode.KEYWORD_NOT_FOUND_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(2))).getCode(), is(GroupConfigErrorCode.KEYWORD_NOT_FOUND_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(3))).getCode(), is(GroupConfigErrorCode.KEYWORD_NOT_FOUND_ERROR.getValue()) );
		
	}
	
	
	//@Test
	public void testAddRtRelation() {
		this.testSetTargetInfoForRT();
		final int userId = 499;
		 
		final int groupId = 244542;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddRtRelationRequest request = new AddRtRelationRequest();
		GroupRtItemType[] rtRelations = new GroupRtItemType[2];
		GroupRtItemType type = new GroupRtItemType();
		type.setGroupId(groupId);
		type.setFcPlanId(67142079L);
		type.setRelationType(1);
		rtRelations[0] = type;
		type = new GroupRtItemType();
		type.setGroupId(groupId);
		type.setFcPlanId(67147881L);
		type.setFcUnitId(95639275L);
		type.setRelationType(0);
		rtRelations[1] = type;
		request.setRtRelations(rtRelations);
		
		ApiResult<Object> result  = groupConfigService.addRtRelation(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	//@Test
	public void testDeleteRtRelation() {
		this.testSetTargetInfoForRT();
		final int userId = 499;
		 
		final int groupId = 244542;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteRtRelationRequest request = new DeleteRtRelationRequest();
		GroupRtItemType[] rtRelations = new GroupRtItemType[2];
		GroupRtItemType type = new GroupRtItemType();
		type.setGroupId(groupId);
		type.setFcPlanId(67180836L);
		type.setRelationType(1);
		rtRelations[0] = type;
		type = new GroupRtItemType();
		type.setGroupId(groupId);
		type.setFcPlanId(67147881L);
		type.setFcUnitId(93928095L);
		type.setRelationType(0);
		rtRelations[1] = type;
		request.setRtRelations(rtRelations);
		
		ApiResult<Object> result  = groupConfigService.deleteRtRelation(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testAddVtPeople() {
		this.testSetTargetInfoForVT();
		final int userId = 499;
		 
		final int groupId = 244543;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddVtPeopleRequest request = new AddVtPeopleRequest();
		GroupVtItemType[] vtPeoples = new GroupVtItemType[2];
		GroupVtItemType type = new GroupVtItemType();
		type.setGroupId(groupId);
		type.setPeopleId(18L);
		type.setRelationType(0);
		vtPeoples[0] = type;
		type = new GroupVtItemType();
		type.setGroupId(groupId);
		type.setPeopleId(19L);
		type.setRelationType(1);
		vtPeoples[1] = type;
		request.setVtPeoples(vtPeoples);
		
		ApiResult<Object> result  = groupConfigService.addVtPeople(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testDeleteVtPeople() {
		this.testSetTargetInfoForVT();
		final int userId = 499;
		 
		final int groupId = 244543;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteVtPeopleRequest request = new DeleteVtPeopleRequest();
		GroupVtItemType[] vtPeoples = new GroupVtItemType[2];
		GroupVtItemType type = new GroupVtItemType();
		type.setGroupId(groupId);
		type.setPeopleId(15L);
		type.setRelationType(0);
		vtPeoples[0] = type;
		type = new GroupVtItemType();
		type.setGroupId(groupId);
		type.setPeopleId(17L);
		type.setRelationType(1);
		vtPeoples[1] = type;
		request.setVtPeoples(vtPeoples);
		
		ApiResult<Object> result  = groupConfigService.deleteVtPeople(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testAddSite() {
		this.testSetSiteConfig();
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddSiteRequest request = new AddSiteRequest();
		GroupSiteType[] sites = new GroupSiteType[2];
		GroupSiteType site = new GroupSiteType();
		site.setGroupId(groupId);
		site.setSite("sinacc.com");
		sites[0] = site;
		site = new GroupSiteType();
		site.setGroupId(groupId);
		site.setSite("sh.sina.com.cn");
		sites[1] = site;
		request.setSites(sites);
		
		ApiResult<Object> result  = groupConfigService.addSite(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testDeleteSite() {
		this.testSetSiteConfig();
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteSiteRequest request = new DeleteSiteRequest();
		GroupSiteType[] sites = new GroupSiteType[2];
		GroupSiteType site = new GroupSiteType();
		site.setGroupId(groupId);
		site.setSite("sina.com.cn");
		sites[0] = site;
		site = new GroupSiteType();
		site.setGroupId(groupId);
		site.setSite("tieba.baidu.com");
		sites[1] = site;
		request.setSites(sites);
		
		ApiResult<Object> result  = groupConfigService.deleteSite(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testAddTrade() {
		this.testSetSiteConfig();
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddTradeRequest request = new AddTradeRequest();
		GroupTradeType[] trades = new GroupTradeType[2];
		GroupTradeType trade = new GroupTradeType();
		trade.setGroupId(groupId);
		trade.setTrade(3);
		trades[0] = trade;
		trade = new GroupTradeType();
		trade.setGroupId(groupId);
		trade.setTrade(4);
		trades[1] = trade;
		request.setTrades(trades);
		
		ApiResult<Object> result  = groupConfigService.addTrade(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testDeleteTrade() {
		this.testSetSiteConfig();
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteTradeRequest request = new DeleteTradeRequest();
		GroupTradeType[] trades = new GroupTradeType[2];
		GroupTradeType trade = new GroupTradeType();
		trade.setGroupId(groupId);
		trade.setTrade(1);
		trades[0] = trade;
		trade = new GroupTradeType();
		trade.setGroupId(groupId);
		trade.setTrade(201);
		trades[1] = trade;
		request.setTrades(trades);
		
		ApiResult<Object> result  = groupConfigService.deleteTrade(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testAddRegion() {
		this.testSetRegionConfig();
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		// type: 1, fcRegionId: 1000, regionId: 1, name: 北京市
		// type: 1, fcRegionId: 1390, regionId: 742, name: 昌平区
		// type: 1, fcRegionId: 2000, regionId: 2, name: 上海市
		// type: 1, fcRegionId: 3000, regionId: 3, name: 天津市
		// type: 1, fcRegionId: 3418, regionId: 773, name: 南开区
		// type: 1, fcRegionId: 3419, regionId: 765, name: 河北区
		// type: 1, fcRegionId: 36000, regionId: 5, name: 澳门特别行政区
		
		AddRegionRequest request = new AddRegionRequest();
		GroupRegionType[] regions = new GroupRegionType[2];
		GroupRegionType region = new GroupRegionType();
		region.setGroupId(groupId);
		region.setType(1);
		region.setRegionId(3);
		regions[0] = region;
		region = new GroupRegionType();
		region.setGroupId(groupId);
		region.setType(1);
		region.setRegionId(5);
		regions[1] = region;
		request.setRegions(regions);
		request.setVersion("1.1");
		
		ApiResult<Object> result  = groupConfigService.addRegion(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testDeleteRegion() {
		this.testSetRegionConfig();
		final int userId = 499;
		 
		final int groupId = 228;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		// type: 1, fcRegionId: 1000, regionId: 1, name: 北京市
		// type: 1, fcRegionId: 1390, regionId: 742, name: 昌平区
		// type: 1, fcRegionId: 2000, regionId: 2, name: 上海市
		// type: 1, fcRegionId: 3000, regionId: 3, name: 天津市
		// type: 1, fcRegionId: 3418, regionId: 773, name: 南开区
		// type: 1, fcRegionId: 3419, regionId: 765, name: 河北区
		// type: 1, fcRegionId: 36000, regionId: 5, name: 澳门特别行政区
		
		DeleteRegionRequest request = new DeleteRegionRequest();
		GroupRegionType[] regions = new GroupRegionType[2];
		GroupRegionType region = new GroupRegionType();
		region.setGroupId(groupId);
		region.setType(1);
		region.setRegionId(742);
		regions[0] = region;
		region = new GroupRegionType();
		region.setGroupId(groupId);
		region.setType(1);
		region.setRegionId(2);
		regions[1] = region;
		request.setRegions(regions);
		request.setVersion("1.1");
		
		ApiResult<Object> result  = groupConfigService.deleteRegion(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testAddExcludeIp() {
		this.testSetExcludeIp();
		final int userId = 499;
		 
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddExcludeIpRequest request = new AddExcludeIpRequest();
		GroupExcludeIpType[] excludeIps = new GroupExcludeIpType[2];
		GroupExcludeIpType excludeIp = new GroupExcludeIpType();
		excludeIp.setGroupId(groupId);
		excludeIp.setExcludeIp("111.111.111.*");
		excludeIps[0] = excludeIp;
		excludeIp = new GroupExcludeIpType();
		excludeIp.setGroupId(groupId);
		excludeIp.setExcludeIp("111.111.111.111");
		excludeIps[1] = excludeIp;
		request.setExcludeIps(excludeIps);
		
		ApiResult<Object> result  = groupConfigService.addExcludeIp(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testDeleteExcludeIp() {
		this.testSetExcludeIp();
		final int userId = 499;
		 
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteExcludeIpRequest request = new DeleteExcludeIpRequest();
		GroupExcludeIpType[] excludeIps = new GroupExcludeIpType[2];
		GroupExcludeIpType excludeIp = new GroupExcludeIpType();
		excludeIp.setGroupId(groupId);
		excludeIp.setExcludeIp("111.111.111.1");
		excludeIps[0] = excludeIp;
		excludeIp = new GroupExcludeIpType();
		excludeIp.setGroupId(groupId);
		excludeIp.setExcludeIp("111.111.111.11");
		excludeIps[1] = excludeIp;
		request.setExcludeIps(excludeIps);
		
		ApiResult<Object> result  = groupConfigService.deleteExcludeIp(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testAddExcludeSite() {
		this.testSetExcludeSite();
		final int userId = 499;
		 
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddExcludeSiteRequest request = new AddExcludeSiteRequest();
		GroupExcludeSiteType[] excludeSites = new GroupExcludeSiteType[2];
		GroupExcludeSiteType excludeSite = new GroupExcludeSiteType();
		excludeSite.setGroupId(groupId);
		excludeSite.setExcludeSite("sina.com.cn");
		excludeSites[0] = excludeSite;
		excludeSite = new GroupExcludeSiteType();
		excludeSite.setGroupId(groupId);
		excludeSite.setExcludeSite("sh.sina.com.cn");
		excludeSites[1] = excludeSite;
		request.setExcludeSites(excludeSites);
		
		ApiResult<Object> result  = groupConfigService.addExcludeSite(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testDeleteExcludeSite() {
		this.testSetExcludeSite();
		final int userId = 499;
		 
		final int groupId = 245297;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteExcludeSiteRequest request = new DeleteExcludeSiteRequest();
		GroupExcludeSiteType[] excludeSites = new GroupExcludeSiteType[2];
		GroupExcludeSiteType excludeSite = new GroupExcludeSiteType();
		excludeSite.setGroupId(groupId);
		excludeSite.setExcludeSite("baidu.com");
		excludeSites[0] = excludeSite;
		excludeSite = new GroupExcludeSiteType();
		excludeSite.setGroupId(groupId);
		excludeSite.setExcludeSite("tieba.baidu.com");
		excludeSites[1] = excludeSite;
		request.setExcludeSites(excludeSites);
		
		ApiResult<Object> result  = groupConfigService.deleteExcludeSite(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
	}
	
	@Test
	public void testGetInterestInfo() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetInterestInfoRequest request = new GetInterestInfoRequest();
		request.setGroupIds(new long[]{228,244262,244627});
		
		ApiResult<InterestInfoType> result  = groupConfigService.getInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(3));
		assertThat(result.getPayment().getSuccess(), is(3));
		
		assertThat(result.getData().get(0).isEnable(), is(false));
		assertThat(result.getData().get(1).isEnable(), is(true));
		assertThat(result.getData().get(1).getGroupId(), is(244262l));
		assertThat(result.getData().get(1).getInterestIds().length, is(3));
		assertThat(result.getData().get(1).getExceptInterestIds(), is(new int[]{}));
		assertThat(result.getData().get(2).isEnable(), is(true));
		assertThat(result.getData().get(2).getGroupId(), is(244627l));
		assertThat(result.getData().get(2).getInterestIds()[1], is(37));
		assertThat(result.getData().get(2).getExceptInterestIds().length, is(3));
	}
	
	@Test
	public void testSetInterestInfoEnable() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(true);
		interestInfo.setGroupId(244557);
		interestInfo.setInterestIds(new int[]{1,2,23,601,100001});
		interestInfo.setExceptInterestIds(new int[]{602,53});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		GetInterestInfoRequest request2 = new GetInterestInfoRequest();
		request2.setGroupIds(new long[]{244557});
		
		ApiResult<InterestInfoType> result2  = groupConfigService.getInterestInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		assertThat(result2.getData().get(0).isEnable(), is(true));
		assertThat(result2.getData().get(0).getGroupId(), is(244557l));
		assertThat(result2.getData().get(0).getInterestIds().length, is(5));
		assertThat(result2.getData().get(0).getInterestIds()[0], is(1));
		assertThat(result2.getData().get(0).getExceptInterestIds().length, is(2));
		assertThat(result2.getData().get(0).getExceptInterestIds()[0], is(53));
	}
	
	@Test
	public void testSetInterestInfoDisable() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(false);
		interestInfo.setGroupId(244555);
		//interestInfo.setInterestIds(new int[]{1,2,23,601,100001});
		//interestInfo.setExceptInterestIds(new int[]{602,53});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		GetInterestInfoRequest request2 = new GetInterestInfoRequest();
		request2.setGroupIds(new long[]{244555});
		
		ApiResult<InterestInfoType> result2  = groupConfigService.getInterestInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		assertThat(result2.getData().get(0).isEnable(), is(false));
		assertThat(result2.getData().get(0).getGroupId(), is(244555l));
		assertThat(result2.getData().get(0).getInterestIds().length, is(0));
		assertThat(result2.getData().get(0).getExceptInterestIds().length, is(0));

	}
	
	@Test
	public void testSetInterestInfoOnlyExceptInterests() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(false);
		interestInfo.setGroupId(244555);
		//interestInfo.setInterestIds(new int[]{1,2,23,601,100001});
		interestInfo.setExceptInterestIds(new int[]{602,53});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		GetInterestInfoRequest request2 = new GetInterestInfoRequest();
		request2.setGroupIds(new long[]{244555});
		
		ApiResult<InterestInfoType> result2  = groupConfigService.getInterestInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		assertThat(result2.getData().get(0).isEnable(), is(false));
		assertThat(result2.getData().get(0).getGroupId(), is(244555l));
		assertThat(result2.getData().get(0).getInterestIds().length, is(0));
		assertThat(result2.getData().get(0).getExceptInterestIds().length, greaterThan(0));

	}
	
	@Test
	public void testSetInterestInfoNegative_INTEREST_EMPTY_ERROR() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(true);
		interestInfo.setGroupId(244555);
		//interestInfo.setInterestIds(new int[]{1,2,23,601,100001});
		//interestInfo.setExceptInterestIds(new int[]{602,53});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(0));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.INTEREST_EMPTY_ERROR.getValue()) );
	}
	
	@Test
	public void testSetInterestInfoNegative_GROUP_INTEREST_AT_LEAST_ONE_ERROR() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(true);
		interestInfo.setGroupId(244555);
		interestInfo.setInterestIds(new int[]{});
		interestInfo.setExceptInterestIds(new int[]{602,53});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(0));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.GROUP_INTEREST_AT_LEAST_ONE_ERROR.getValue()) );
	}
	
	@Test
	public void testSetInterestInfoNegative_INTEREST_SHOULD_NOT_IN_BOTH_SET() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(true);
		interestInfo.setGroupId(244555);
		interestInfo.setInterestIds(new int[]{1});
		interestInfo.setExceptInterestIds(new int[]{1,53});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(0));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.INTEREST_SHOULD_NOT_IN_BOTH_SET.getValue()) );
	}
	
	@Test
	public void testSetInterestInfoNegative_INTEREST_NULL_ERROR() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(true);
		interestInfo.setGroupId(244555);
		interestInfo.setInterestIds(new int[]{1,2,3,9999});
		interestInfo.setExceptInterestIds(new int[]{8,53});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(0));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue()) );
	}
	
	@Test
	public void testSetInterestInfoNegative_INTEREST_CHILDREN_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(true);
		interestInfo.setGroupId(244555);
		/*
		 * 10汽车：11车讯行情 12养车用车
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 */
		interestInfo.setInterestIds(new int[]{10,31,56});
		interestInfo.setExceptInterestIds(new int[]{30,3,4,5});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(0));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.INTEREST_CHILDREN_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR.getValue()) );
	}
	
	@Test
	public void testAddInterestInfo() {
		this.prepareDataForTestAddDeleteInterestInfo001();
		this.prepareDataForTestAddDeleteInterestInfo002();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddInterestInfoRequest request = new AddInterestInfoRequest();
		/*
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 6食品餐饮:7烹饪和菜谱 8休闲食品 9餐馆美食
		 * 10汽车：11车讯行情 12养车用车
		 * 13影视: 14院线电影 15网络影视剧 16动画漫画
		 * 22音乐: 23古典 24流行 25音响器材 26乐器
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 * 601消费潜力高
		 * 602白领
		 * 603学生
		 * 604网购
		interestInfo.setInterestIds(new int[]{2,14,15,31,56,601,100001});
		interestInfo.setExceptInterestIds(new int[]{10,23,24,57,603,100002});
		
		interestInfo.setInterestIds(new int[]{2,7,601,100003});
		interestInfo.setExceptInterestIds(new int[]{});
		 */
		
		GroupInterestInfoType[] groupInterestInfoType = new GroupInterestInfoType[2];
		
		GroupInterestInfoType interestInfo1 = new GroupInterestInfoType();
		interestInfo1.setGroupId(6620);
		interestInfo1.setInterestIds(new int[]{6,25,602,100005});
		interestInfo1.setExceptInterestIds(new int[]{38,48,100003});
		groupInterestInfoType[0] = interestInfo1;
		
		GroupInterestInfoType interestInfo2 = new GroupInterestInfoType();
		interestInfo2.setGroupId(244262);
		interestInfo2.setInterestIds(new int[]{8,100001});
		interestInfo2.setExceptInterestIds(new int[]{33});
		groupInterestInfoType[1] = interestInfo2;
		
		request.setInterests(groupInterestInfoType);
		
		ApiResult<Object> result  = groupConfigService.addInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(10));
		assertThat(result.getPayment().getSuccess(), is(10));
		
	}
	
	@Test
	public void testAddInterestInfoNegative_GROUP_IT_DISABLED() {
		this.prepareDataForTestAddDeleteInterestInfo001();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddInterestInfoRequest request = new AddInterestInfoRequest();
		/*
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 6食品餐饮:7烹饪和菜谱 8休闲食品 9餐馆美食
		 * 10汽车：11车讯行情 12养车用车
		 * 13影视: 14院线电影 15网络影视剧 16动画漫画
		 * 22音乐: 23古典 24流行 25音响器材 26乐器
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 * 601消费潜力高
		 * 602白领
		 * 603学生
		 * 604网购
		interestInfo.setInterestIds(new int[]{2,14,15,31,56,601,100001});  7个
		interestInfo.setExceptInterestIds(new int[]{10,23,24,57,603,100002});   6个
		
		interestInfo.setInterestIds(new int[]{2,7,601,100003});
		interestInfo.setExceptInterestIds(new int[]{});
		 */
		
		GroupInterestInfoType[] groupInterestInfoType = new GroupInterestInfoType[2];
		
		GroupInterestInfoType interestInfo1 = new GroupInterestInfoType();
		interestInfo1.setGroupId(228);
		interestInfo1.setInterestIds(new int[]{6,25,602,100004});
		interestInfo1.setExceptInterestIds(new int[]{38,99,100003});
		groupInterestInfoType[0] = interestInfo1;
		
		GroupInterestInfoType interestInfo2 = new GroupInterestInfoType();
		interestInfo2.setGroupId(244540);
		interestInfo2.setInterestIds(new int[]{8,100001});
		interestInfo2.setExceptInterestIds(new int[]{33});
		groupInterestInfoType[1] = interestInfo2;
		
		request.setInterests(groupInterestInfoType);
		
		ApiResult<Object> result  = groupConfigService.addInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(0));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.GROUP_IT_DISABLED.getValue()) );
	
	}
	
	@Test
	public void testAddInterestInfoNegative_INTEREST_NULL_ERROR() {
		this.prepareDataForTestAddDeleteInterestInfo001();
		this.prepareDataForTestAddDeleteInterestInfo002();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddInterestInfoRequest request = new AddInterestInfoRequest();
		/*
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 6食品餐饮:7烹饪和菜谱 8休闲食品 9餐馆美食
		 * 10汽车：11车讯行情 12养车用车
		 * 13影视: 14院线电影 15网络影视剧 16动画漫画
		 * 22音乐: 23古典 24流行 25音响器材 26乐器
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 * 601消费潜力高
		 * 602白领
		 * 603学生
		 * 604网购
		interestInfo.setInterestIds(new int[]{2,14,15,31,56,601,100001});  7个
		interestInfo.setExceptInterestIds(new int[]{10,23,24,57,603,100002});   6个
		
		interestInfo.setInterestIds(new int[]{2,7,601,100003});
		interestInfo.setExceptInterestIds(new int[]{});
		 */
		
		GroupInterestInfoType[] groupInterestInfoType = new GroupInterestInfoType[2];
		
		GroupInterestInfoType interestInfo1 = new GroupInterestInfoType();
		interestInfo1.setGroupId(6620);
		interestInfo1.setInterestIds(new int[]{6,25,602,100004});
		interestInfo1.setExceptInterestIds(new int[]{38,99,100003});
		groupInterestInfoType[0] = interestInfo1;
		
		GroupInterestInfoType interestInfo2 = new GroupInterestInfoType();
		interestInfo2.setGroupId(244262);
		interestInfo2.setInterestIds(new int[]{99,100001});
		interestInfo2.setExceptInterestIds(new int[]{98});
		groupInterestInfoType[1] = interestInfo2;
		
		request.setInterests(groupInterestInfoType);
		
		ApiResult<Object> result  = groupConfigService.addInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(4));
		assertThat(result.getPayment().getTotal(), is(10));
		assertThat(result.getPayment().getSuccess(), is(6));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(1))).getCode(), is(GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(2))).getCode(), is(GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(3))).getCode(), is(GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue()) );
		
		// check
		GetInterestInfoRequest request2 = new GetInterestInfoRequest();
		request2.setGroupIds(new long[]{6620,244262});
		
		ApiResult<InterestInfoType> result2  = groupConfigService.getInterestInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getPayment().getTotal(), is(2));
		assertThat(result2.getPayment().getSuccess(), is(2));
		
		assertThat(result2.getData().get(0).isEnable(), is(true));
		assertThat(result2.getData().get(0).getGroupId(), is(6620l));
		assertThat(result2.getData().get(0).getInterestIds().length, is(10));
		assertThat(result2.getData().get(0).getExceptInterestIds().length, is(8));
		assertThat(result2.getData().get(1).isEnable(), is(true));
		assertThat(result2.getData().get(1).getGroupId(), is(244262l));
		assertThat(result2.getData().get(1).getInterestIds().length, is(5));
		assertThat(result2.getData().get(1).getExceptInterestIds().length, is(0));
	
	}
	
	@Test
	public void testAddInterestInfoNegative_INTEREST_SHOULD_NOT_IN_BOTH_SET() {
		this.prepareDataForTestAddDeleteInterestInfo001();
		this.prepareDataForTestAddDeleteInterestInfo002();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddInterestInfoRequest request = new AddInterestInfoRequest();
		/*
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 6食品餐饮:7烹饪和菜谱 8休闲食品 9餐馆美食
		 * 10汽车：11车讯行情 12养车用车
		 * 13影视: 14院线电影 15网络影视剧 16动画漫画
		 * 22音乐: 23古典 24流行 25音响器材 26乐器
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 * 601消费潜力高
		 * 602白领
		 * 603学生
		 * 604网购
		interestInfo.setInterestIds(new int[]{2,14,15,31,56,601,100001});  7个
		interestInfo.setExceptInterestIds(new int[]{10,23,24,57,603,100002});   6个
		
		interestInfo.setInterestIds(new int[]{2,7,601,100003});
		interestInfo.setExceptInterestIds(new int[]{});
		 */
		
		GroupInterestInfoType[] groupInterestInfoType = new GroupInterestInfoType[2];
		
		GroupInterestInfoType interestInfo1 = new GroupInterestInfoType();
		interestInfo1.setGroupId(6620);
		interestInfo1.setInterestIds(new int[]{6,25,602});
		interestInfo1.setExceptInterestIds(new int[]{25,99,100003});
		groupInterestInfoType[0] = interestInfo1;
		
		GroupInterestInfoType interestInfo2 = new GroupInterestInfoType();
		interestInfo2.setGroupId(244262);
		interestInfo2.setInterestIds(new int[]{100001});
		interestInfo2.setExceptInterestIds(new int[]{});
		groupInterestInfoType[1] = interestInfo2;
		
		request.setInterests(groupInterestInfoType);
		
		ApiResult<Object> result  = groupConfigService.addInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(7));
		assertThat(result.getPayment().getSuccess(), is(5));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.INTEREST_SHOULD_NOT_IN_BOTH_SET.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(1))).getCode(), is(GroupConfigErrorCode.INTEREST_NULL_ERROR.getValue()) );
		
		// check
		GetInterestInfoRequest request2 = new GetInterestInfoRequest();
		request2.setGroupIds(new long[]{6620,244262});
		
		ApiResult<InterestInfoType> result2  = groupConfigService.getInterestInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getPayment().getTotal(), is(2));
		assertThat(result2.getPayment().getSuccess(), is(2));
		
		assertThat(result2.getData().get(0).isEnable(), is(true));
		assertThat(result2.getData().get(0).getGroupId(), is(6620l));
		assertThat(result2.getData().get(0).getInterestIds().length, is(10));
		assertThat(result2.getData().get(0).getExceptInterestIds().length, is(7));
		assertThat(result2.getData().get(1).isEnable(), is(true));
		assertThat(result2.getData().get(1).getGroupId(), is(244262l));
		assertThat(result2.getData().get(1).getInterestIds().length, is(5));
		assertThat(result2.getData().get(1).getExceptInterestIds().length, is(0));
	
	}
	
	@Test
	public void testAddInterestInfoNegative_INTEREST_CHILDREN_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR() {
		this.prepareDataForTestAddDeleteInterestInfo001();
		this.prepareDataForTestAddDeleteInterestInfo002();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddInterestInfoRequest request = new AddInterestInfoRequest();
		/*
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 6食品餐饮:7烹饪和菜谱 8休闲食品 9餐馆美食
		 * 10汽车：11车讯行情 12养车用车
		 * 13影视: 14院线电影 15网络影视剧 16动画漫画
		 * 22音乐: 23古典 24流行 25音响器材 26乐器
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 * 601消费潜力高
		 * 602白领
		 * 603学生
		 * 604网购
		interestInfo.setInterestIds(new int[]{2,14,15,31,56,601,100001});  7个
		interestInfo.setExceptInterestIds(new int[]{10,23,24,57,603,100002});   6个
		
		interestInfo.setInterestIds(new int[]{2,7,601,100003});
		interestInfo.setExceptInterestIds(new int[]{});
		 */
		
		GroupInterestInfoType[] groupInterestInfoType = new GroupInterestInfoType[2];
		
		GroupInterestInfoType interestInfo1 = new GroupInterestInfoType();
		interestInfo1.setGroupId(6620);
		interestInfo1.setInterestIds(new int[]{3,6,22});
		interestInfo1.setExceptInterestIds(new int[]{100003,4});
		groupInterestInfoType[0] = interestInfo1;
		
		GroupInterestInfoType interestInfo2 = new GroupInterestInfoType();
		interestInfo2.setGroupId(244262);
		interestInfo2.setInterestIds(new int[]{6});
		interestInfo2.setExceptInterestIds(new int[]{});
		groupInterestInfoType[1] = interestInfo2;
		
		request.setInterests(groupInterestInfoType);
		
		ApiResult<Object> result  = groupConfigService.addInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(4));
		assertThat(result.getPayment().getTotal(), is(6));
		assertThat(result.getPayment().getSuccess(), is(2));
		assertThat( ((ApiError)(result.getErrors().get(0))).getCode(), is(GroupConfigErrorCode.INTEREST_CHILDREN_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(1))).getCode(), is(GroupConfigErrorCode.INTEREST_PARENT_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(2))).getCode(), is(GroupConfigErrorCode.INTEREST_PARENT_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR.getValue()) );
		assertThat( ((ApiError)(result.getErrors().get(3))).getCode(), is(GroupConfigErrorCode.INTEREST_CHILDREN_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR.getValue()) );
		
		sleep(2);
		
		// check
		GetInterestInfoRequest request2 = new GetInterestInfoRequest();
		request2.setGroupIds(new long[]{6620,244262});
		
		ApiResult<InterestInfoType> result2  = groupConfigService.getInterestInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getPayment().getTotal(), is(2));
		assertThat(result2.getPayment().getSuccess(), is(2));
		
		assertThat(result2.getData().get(0).isEnable(), is(true));
		assertThat(result2.getData().get(0).getGroupId(), is(6620l));
		assertThat(result2.getData().get(0).getInterestIds().length, is(8));
		assertThat(result2.getData().get(0).getExceptInterestIds().length, is(7));
		assertThat(result2.getData().get(1).isEnable(), is(true));
		assertThat(result2.getData().get(1).getGroupId(), is(244262l));
		assertThat(result2.getData().get(1).getInterestIds().length, is(4));
		assertThat(result2.getData().get(1).getExceptInterestIds().length, is(0));
	
	}
	
	@Test
	public void testDeleteInterestInfo() {
		this.prepareDataForTestAddDeleteInterestInfo001();
		this.prepareDataForTestAddDeleteInterestInfo002();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteInterestInfoRequest request = new DeleteInterestInfoRequest();
		/*
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 6食品餐饮:7烹饪和菜谱 8休闲食品 9餐馆美食
		 * 10汽车：11车讯行情 12养车用车
		 * 13影视: 14院线电影 15网络影视剧 16动画漫画
		 * 22音乐: 23古典 24流行 25音响器材 26乐器
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 * 601消费潜力高
		 * 602白领
		 * 603学生
		 * 604网购
		interestInfo.setInterestIds(new int[]{2,14,15,31,56,601,100001});
		interestInfo.setExceptInterestIds(new int[]{10,23,24,57,603,100002});
		
		interestInfo.setInterestIds(new int[]{2,7,601,100003});
		interestInfo.setExceptInterestIds(new int[]{});
		 */
		
		GroupInterestInfoType[] groupInterestInfoType = new GroupInterestInfoType[2];
		
		GroupInterestInfoType interestInfo1 = new GroupInterestInfoType();
		interestInfo1.setGroupId(6620);
		interestInfo1.setInterestIds(new int[]{2,15,31,100001});
		interestInfo1.setExceptInterestIds(new int[]{10,603});
		groupInterestInfoType[0] = interestInfo1;
		
		GroupInterestInfoType interestInfo2 = new GroupInterestInfoType();
		interestInfo2.setGroupId(244262);
		interestInfo2.setInterestIds(new int[]{7,601});
		interestInfo2.setExceptInterestIds(new int[]{});
		groupInterestInfoType[1] = interestInfo2;
		
		request.setInterests(groupInterestInfoType);
		
		ApiResult<Object> result  = groupConfigService.deleteInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(8));
		assertThat(result.getPayment().getSuccess(), is(8));
		
		// check
		GetInterestInfoRequest request2 = new GetInterestInfoRequest();
		request2.setGroupIds(new long[]{6620,244262});
		
		ApiResult<InterestInfoType> result2  = groupConfigService.getInterestInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getPayment().getTotal(), is(2));
		assertThat(result2.getPayment().getSuccess(), is(2));
		
		assertThat(result2.getData().get(0).isEnable(), is(true));
		assertThat(result2.getData().get(0).getGroupId(), is(6620l));
		assertThat(result2.getData().get(0).getInterestIds().length, is(3));
		assertThat(result2.getData().get(0).getExceptInterestIds().length, is(4));
		assertThat(result2.getData().get(1).isEnable(), is(true));
		assertThat(result2.getData().get(1).getGroupId(), is(244262l));
		assertThat(result2.getData().get(1).getInterestIds().length, is(2));
		assertThat(result2.getData().get(1).getExceptInterestIds().length, is(0));
		
	}
	
	@Test
	public void testDeleteInterestInfo_INTEREST_NULL_ERROR() {
		this.prepareDataForTestAddDeleteInterestInfo001();
		this.prepareDataForTestAddDeleteInterestInfo002();
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		DeleteInterestInfoRequest request = new DeleteInterestInfoRequest();
		/*
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 6食品餐饮:7烹饪和菜谱 8休闲食品 9餐馆美食
		 * 10汽车：11车讯行情 12养车用车
		 * 13影视: 14院线电影 15网络影视剧 16动画漫画
		 * 22音乐: 23古典 24流行 25音响器材 26乐器
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 * 601消费潜力高
		 * 602白领
		 * 603学生
		 * 604网购
		interestInfo.setInterestIds(new int[]{2,14,15,31,56,601,100001});
		interestInfo.setExceptInterestIds(new int[]{10,23,24,57,603,100002});
		
		interestInfo.setInterestIds(new int[]{2,7,601,100003});
		interestInfo.setExceptInterestIds(new int[]{});
		 */
		
		GroupInterestInfoType[] groupInterestInfoType = new GroupInterestInfoType[2];
		
		GroupInterestInfoType interestInfo1 = new GroupInterestInfoType();
		interestInfo1.setGroupId(6620);
		interestInfo1.setInterestIds(new int[]{3,15,4,100002});
		interestInfo1.setExceptInterestIds(new int[]{11,24,58});
		groupInterestInfoType[0] = interestInfo1;
		
		GroupInterestInfoType interestInfo2 = new GroupInterestInfoType();
		interestInfo2.setGroupId(244262);
		interestInfo2.setInterestIds(new int[]{7,99});
		interestInfo2.setExceptInterestIds(new int[]{98});
		groupInterestInfoType[1] = interestInfo2;
		
		request.setInterests(groupInterestInfoType);
		
		ApiResult<Object> result  = groupConfigService.deleteInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(7));
		assertThat(result.getPayment().getTotal(), is(10));
		assertThat(result.getPayment().getSuccess(), is(3));
		
		// check
		GetInterestInfoRequest request2 = new GetInterestInfoRequest();
		request2.setGroupIds(new long[]{6620,244262});
		
		ApiResult<InterestInfoType> result2  = groupConfigService.getInterestInfo(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getPayment().getTotal(), is(2));
		assertThat(result2.getPayment().getSuccess(), is(2));
		
		assertThat(result2.getData().get(0).isEnable(), is(true));
		assertThat(result2.getData().get(0).getGroupId(), is(6620l));
		assertThat(result2.getData().get(0).getInterestIds().length, is(6));
		assertThat(result2.getData().get(0).getExceptInterestIds().length, is(5));
		assertThat(result2.getData().get(1).isEnable(), is(true));
		assertThat(result2.getData().get(1).getGroupId(), is(244262l));
		assertThat(result2.getData().get(1).getInterestIds().length, is(3));
		assertThat(result2.getData().get(1).getExceptInterestIds().length, is(0));
		
	}
	
	@Test
	public void testAddTradePrice() {
		final int userId = 499;
		 
		final int groupId = 243168;
		final int groupId2 = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		AddTradePriceRequest request = new AddTradePriceRequest();
		GroupTradePriceType[] tradePrices = new GroupTradePriceType[4];

		GroupTradePriceType tradePriceType = new GroupTradePriceType();
		tradePriceType.setGroupId(groupId);
		tradePriceType.setPrice(100f);
		tradePriceType.setTrade(3);
		tradePrices[0] = tradePriceType;

		tradePriceType = new GroupTradePriceType();
		tradePriceType.setGroupId(groupId);
		tradePriceType.setPrice(300f);
		tradePriceType.setTrade(8);
		tradePrices[1] = tradePriceType;

		tradePriceType = new GroupTradePriceType();
		tradePriceType.setGroupId(groupId);
		tradePriceType.setPrice(800f);
		tradePriceType.setTrade(260);
		tradePrices[2] = tradePriceType;

		tradePriceType = new GroupTradePriceType();
		tradePriceType.setGroupId(groupId2);
		tradePriceType.setPrice(50f);
		tradePriceType.setTrade(260);
		tradePrices[3] = tradePriceType;

		request.setTradePrices(tradePrices);

		ApiResult<Object> result = groupConfigService.addTradePrice(dataUser, request, apiOption);

		System.out.println(result);

		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(4));
		assertThat(result.getPayment().getSuccess(), is(4));
	}

	@Test
	public void testDeleteTradePrice() {
		final int userId = 499;
		 
		final int groupId = 243168;
		final int groupId2 = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		DeleteTradePriceRequest request = new DeleteTradePriceRequest();

		GroupTradeType[] tradePrices = new GroupTradeType[3];

		GroupTradeType tradeType = new GroupTradeType();
		tradeType.setGroupId(groupId);
		tradeType.setTrade(3);
		tradePrices[0] = tradeType;

		tradeType = new GroupTradeType();
		tradeType.setGroupId(groupId);
		tradeType.setTrade(260);
		tradePrices[1] = tradeType;

		tradeType = new GroupTradeType();
		tradeType.setGroupId(groupId2);
		tradeType.setTrade(260);
		tradePrices[2] = tradeType;

		request.setTradePrices(tradePrices);

		ApiResult<Object> result = groupConfigService.deleteTradePrice(dataUser, request, apiOption);

		System.out.println(result);

		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(3));
		assertThat(result.getPayment().getSuccess(), is(3));
	}
	
	@Test
	public void testAddSitePrice() {
		final int userId = 499;
		 
		final int groupId = 243168;
		final int groupId2 = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		AddSitePriceRequest request = new AddSitePriceRequest();
		GroupSitePriceType[] sitePrices = new GroupSitePriceType[3];

		GroupSitePriceType sitePriceType = new GroupSitePriceType();
		sitePriceType.setGroupId(groupId);
		sitePriceType.setPrice(100f);
		sitePriceType.setSite("readnovel.com");
		sitePrices[0] = sitePriceType;

		sitePriceType = new GroupSitePriceType();
		sitePriceType.setGroupId(groupId);
		sitePriceType.setPrice(300f);
		sitePriceType.setSite("4399.com");
		sitePrices[1] = sitePriceType;

		sitePriceType = new GroupSitePriceType();
		sitePriceType.setGroupId(groupId2);
		sitePriceType.setPrice(50f);
		sitePriceType.setSite("readnovel.com");
		sitePrices[2] = sitePriceType;

		request.setSitePrices(sitePrices);

		ApiResult<Object> result = groupConfigService.addSitePrice(dataUser, request, apiOption);

		System.out.println(result);

		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(3));
		assertThat(result.getPayment().getSuccess(), is(3));
	}
	
	@Test
	public void testDeleteSitePrice() {
		this.testAddSitePrice();
		final int userId = 499;
		 
		final int groupId = 243168;
		final int groupId2 = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		DeleteSitePriceRequest request = new DeleteSitePriceRequest();
		GroupSitePriceType[] sitePrices = new GroupSitePriceType[3];

		GroupSitePriceType sitePriceType = new GroupSitePriceType();
		sitePriceType.setGroupId(groupId);
		sitePriceType.setSite("readnovel.com");
		sitePrices[0] = sitePriceType;

		sitePriceType = new GroupSitePriceType();
		sitePriceType.setGroupId(groupId);
		sitePriceType.setSite("4399.com");
		sitePrices[1] = sitePriceType;

		sitePriceType = new GroupSitePriceType();
		sitePriceType.setGroupId(groupId2);
		sitePriceType.setSite("readnovel.com");
		sitePrices[2] = sitePriceType;

		request.setSitePrices(sitePrices);

		ApiResult<Object> result = groupConfigService.deleteSitePrice(dataUser, request, apiOption);

		System.out.println(result);

		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(3));
		assertThat(result.getPayment().getSuccess(), is(3));
	}
	
	/********************************* prepare methods *****************************************/
	
	private void prepareDataForTestAddKeyword001(){
		final int userId = 499;
		 
		final int groupId = 245326;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		targetInfo.setGroupId(groupId);
		targetInfo.setType(2);
		KtItemType ktItem = new KtItemType();
		KeywordType[] wordList = new KeywordType[3];
		ktItem.setTargetType(6);
		ktItem.setAliveDays(7);
		KeywordType word = new KeywordType();
		word.setKeyword("java是神马");
		wordList[0] = word;
		word = new KeywordType();
		word.setKeyword("python是神器");
		wordList[1] = word;
		word = new KeywordType();
		word.setKeyword("smalltalk不小声说话");
		wordList[2] = word;

		ktItem.setKtWordList(wordList);
		targetInfo.setKtItem(ktItem);
		request.setTargetInfo(targetInfo);
		
		ApiResult<Object> result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
		
	}
	
	private void prepareDataForTestAddKeyword002(){
		final int userId = 499;
		 
		final int groupId = 245327;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		targetInfo.setGroupId(groupId);
		targetInfo.setType(2);
		KtItemType ktItem = new KtItemType();
		KeywordType[] wordList = new KeywordType[1];
		ktItem.setTargetType(3);
		ktItem.setAliveDays(30);
		KeywordType word = new KeywordType();
		word.setKeyword("高尔夫");
		wordList[0] = word;
		
		ktItem.setKtWordList(wordList);
		targetInfo.setKtItem(ktItem);
		request.setTargetInfo(targetInfo);
		
		ApiResult<Object> result  = groupConfigService.setTargetInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果内容
		assertThat(result.getData().get(0), is(SuccessObject.class));
		SuccessObject info = (SuccessObject) result.getData().get(0);
		assertThat(info.getResponse(), is("placeholder"));
		
	}

	private void prepareDataForTestAddDeleteInterestInfo001(){
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(true);
		interestInfo.setGroupId(6620);
		/*
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 6食品餐饮:7烹饪和菜谱 8休闲食品 9餐馆美食
		 * 10汽车：11车讯行情 12养车用车
		 * 13影视: 14院线电影 15网络影视剧 16动画漫画
		 * 22音乐: 23古典 24流行 25音响器材 26乐器
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 * 601消费潜力高
		 * 602白领
		 * 603学生
		 * 604网购
		 */
		interestInfo.setInterestIds(new int[]{2,14,15,31,56,601,100001});
		interestInfo.setExceptInterestIds(new int[]{10,23,24,57,603,100002});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
	}
	
	private void prepareDataForTestAddDeleteInterestInfo002(){
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType interestInfo = new InterestInfoType();
		interestInfo.setEnable(true);
		interestInfo.setGroupId(244262);
		/*
		 * 2个护美容：3护肤 4护理 5彩妆
		 * 6食品餐饮:7烹饪和菜谱 8休闲食品 9餐馆美食
		 * 10汽车：11车讯行情 12养车用车
		 * 13影视: 14院线电影 15网络影视剧 16动画漫画
		 * 22音乐: 23古典 24流行 25音响器材 26乐器
		 * 30游戏：31大型客户端游戏 32网页网游 33手机平板
		 * 38体育：39运动 40健身 41户外
		 * 45教育培训：48MBA 49职业技能
		 * 56健康保健：
		 * 57旅游：
		 * 601消费潜力高
		 * 602白领
		 * 603学生
		 * 604网购
		 */
		interestInfo.setInterestIds(new int[]{2,7,601,100003});
		interestInfo.setExceptInterestIds(new int[]{});
		request.setInterestInfo(interestInfo);
		
		ApiResult<Object> result  = groupConfigService.setInterestInfo(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
	}
}
