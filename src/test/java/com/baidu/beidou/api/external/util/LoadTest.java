package com.baidu.beidou.api.external.util;

import org.junit.Assert;
import org.junit.Ignore;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupService;
import com.baidu.beidou.api.external.cprogroup.vo.AdditionalGroupType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupKeywordItemType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddGroupRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateAdditionalGroupRequest;
import com.baidu.beidou.api.external.cprounit.exporter.AdService;
import com.baidu.beidou.api.external.cprounit.vo.AdType;
import com.baidu.beidou.api.external.cprounit.vo.request.AddAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.GetAdByGroupIdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.UpdateAdRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class LoadTest {

	public void testAddKeyword() {
		int userid = 499;
		int opuser = 499;
		int planId = 1351;

		DataPrivilege user = DarwinApiHelper.getDataPrivilege(userid, opuser);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		McpackRpcProxy proxy1 = new McpackRpcProxy("http://10.81.31.95:8231/api/GroupService", "UTF-8", new ExceptionHandler());
		GroupService exporter1 = ProxyFactory.createProxy(GroupService.class, proxy1);
		AddGroupRequest request1 = new AddGroupRequest();
		GroupType[] groupTypes = new GroupType[1];
		GroupType groupType0 = new GroupType();
		groupType0.setCampaignId(planId); //manipulate specific plan
		//type0.setGroupId(groupId);
		groupType0.setGroupName("士力架7");
		groupType0.setPrice(10);
		groupType0.setStatus(0);
		groupType0.setType(1);
		groupTypes[0] = groupType0;
		request1.setGroupTypes(groupTypes);
		ApiResult<GroupType> result1 = exporter1.addGroup(user, request1, apiOption);
		System.out.println(result1);
		Assert.assertEquals(result1.getData().size(), 1);
		
		long groupId = result1.getData().get(0).getGroupId(); // get the groupid from the successfully saved group 

		UpdateAdditionalGroupRequest request11 = new UpdateAdditionalGroupRequest();
		AdditionalGroupType[] types = new AdditionalGroupType[1];
		AdditionalGroupType type0 = new AdditionalGroupType();
		type0.setGroupId(groupId);
		type0.setTargetType(2);
		types[0] = type0;
		request11.setGroupTypes(types);
		ApiResult<AdditionalGroupType> result11 = exporter1.updateAdditionalGroup(user, request11, apiOption);  // set target type to kt
		System.out.println(result11);
		
		McpackRpcProxy proxy2 = new McpackRpcProxy("http://10.81.31.95:8231/api/GroupConfigService", "UTF-8", new ExceptionHandler());
		GroupConfigService exporter2 = ProxyFactory.createProxy(GroupConfigService.class, proxy2);
		AddKeywordRequest request = new AddKeywordRequest();
		GroupKeywordItemType[] keywordTypes = new GroupKeywordItemType[1];
		GroupKeywordItemType type = new GroupKeywordItemType();
		keywordTypes[0] = type;
		type.setGroupId(groupId); //manipulate group that created above
		type.setKeyword("test");
		type.setPattern(0);
		request.setKeywords(keywordTypes);
		ApiResult<Object> result2 = exporter2.addKeyword(user, request, apiOption);
		System.out.println(result2);
	}
	
	public void testAddAd() {
		int userid = 499;
		int opuser = 499;
		int planId = 1351;

		DataPrivilege user = DarwinApiHelper.getDataPrivilege(userid, opuser);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		McpackRpcProxy proxy1 = new McpackRpcProxy("http://10.81.31.95:8231/api/GroupService", "UTF-8", new ExceptionHandler());
		GroupService exporter1 = ProxyFactory.createProxy(GroupService.class, proxy1);
		AddGroupRequest request1 = new AddGroupRequest();
		GroupType[] groupTypes = new GroupType[1];
		GroupType groupType0 = new GroupType();
		groupType0.setCampaignId(planId); //manipulate specific plan
		//type0.setGroupId(groupId);
		groupType0.setGroupName("士力架4");
		groupType0.setPrice(10);
		groupType0.setStatus(0);
		groupType0.setType(2);  //XX1:固定, X1X:悬浮,1XX:贴片,1XXX表示为启用高级组合设置，必须在addGroup调用时就指定，否则创建的推广组为标准设置并且无法切换到高级组合设置。
		groupTypes[0] = groupType0;
		request1.setGroupTypes(groupTypes);
		ApiResult<GroupType> result1 = exporter1.addGroup(user, request1, apiOption);
		System.out.println(result1);

		long groupId = result1.getData().get(0).getGroupId(); // get the groupid from the successfully saved group 

		McpackRpcProxy proxy2 = new McpackRpcProxy("http://10.81.31.95:8231/api/AdService", "UTF-8", new ExceptionHandler());
		AdService exporter2 = ProxyFactory.createProxy(AdService.class, proxy2);
		
		AddAdRequest request2 = new AddAdRequest();
		AdType[] adTypes = new AdType[1];
		AdType ad1 = new AdType();
		ad1.setAdId(1l);   // When add ad, the value is ignored
		ad1.setTitle("这里是标题");
		ad1.setDescription1("这里是描述1");
		ad1.setDescription2("这里是描述2");  
		ad1.setDisplayUrl("wenhua.com");
		ad1.setDestinationUrl("http://wenhua.com");
		ad1.setGroupId(groupId);
		ad1.setLocalId(111);   //local id can be set what ever value you want
		ad1.setType(2);  //1：文字；2：图片；3：flash;5：图文混排;非固定推广组，只允许设置图片类型或者flash类型。
		ad1.setImageData(ImageUtil.GetImageByte("e://120x270.jpg"));  //图片文件可以从/test/com/baidu/beidou/api/external/resources中取
		ad1.setHeight(270);  //必须和系统支持的尺寸一致
		ad1.setWidth(120);   //必须和系统支持的尺寸一致
		adTypes[0] = ad1;

		request2.setAdTypes(adTypes);
		
		ApiResult<AdType> result2 = exporter2.addAd(user, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
	}
	
	public void testUpdateAd() {
		int userid = 499;
		int opuser = 499;
		int groupId = 123;

		DataPrivilege user = DarwinApiHelper.getDataPrivilege(userid, opuser);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		McpackRpcProxy proxy2 = new McpackRpcProxy("http://10.81.31.95:8231/api/AdService", "UTF-8", new ExceptionHandler());
		AdService exporter2 = ProxyFactory.createProxy(AdService.class, proxy2);
		
		GetAdByGroupIdRequest request1 = new GetAdByGroupIdRequest();
		request1.setGroupId(groupId);
		ApiResult<AdType> adType = exporter2.getAdByGroupId(user, request1, apiOption);
		System.out.println(adType);
		
		for (AdType ad : adType.getData()) {
			long adId = ad.getAdId();
			UpdateAdRequest request2 = new UpdateAdRequest();
			AdType[] adTypes = new AdType[1];
			AdType ad1 = new AdType();
			ad1.setAdId(adId);   // When update ad, the value MUST BE SET!
			ad1.setTitle("这里是标题");
//			ad1.setDescription1("这里是描述1");
//			ad1.setDescription2("这里是描述2");  
//			ad1.setDisplayUrl("wenhua.com");
//			ad1.setDestinationUrl("http://wenhua.com");
//			ad1.setGroupId(groupId);
//			ad1.setLocalId(111);   //local id can be set what ever value you want
//			ad1.setType(2);  //1：文字；2：图片；3：flash;5：图文混排;非固定推广组，只允许设置图片类型或者flash类型。
//			ad1.setImageData(ImageUtil.GetImageByte("e://120x270.jpg"));  //图片文件可以从/test/com/baidu/beidou/api/external/resources中取
//			ad1.setHeight(270);  //必须和系统支持的尺寸一致
//			ad1.setWidth(120);   //必须和系统支持的尺寸一致
			adTypes[0] = ad1;

			request2.setAdTypes(adTypes);
			
			ApiResult<AdType> result2 = exporter2.updateAd(user, request2, apiOption);
			
			// 打印返回
			System.out.println(result2);
		}
		
	}

	public static void main(String[] args) {
		new LoadTest().testAddKeyword();
		//new LoadTest().testAddAd();
		//new LoadTest().testUpdateAd();
	}

}
