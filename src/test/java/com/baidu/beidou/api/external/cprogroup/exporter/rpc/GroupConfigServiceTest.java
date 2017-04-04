package com.baidu.beidou.api.external.cprogroup.exporter.rpc;

import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.*;
import com.baidu.beidou.api.external.cprogroup.vo.request.*;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.fengchao.sun.base.BaseResponse;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
@Ignore
public class GroupConfigServiceTest extends ApiBaseRPCTest<GroupConfigService> {

	@Test
	@Ignore
	public void testGetTargetInfo() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetTargetInfoRequest request = new GetTargetInfoRequest();
		request.setGroupIds(new long[]{6774697l});
		ApiResult<TargetInfoType> res = exporter.getTargetInfo(dataUser, request, apiOption);
		System.out.println(res);
	}
	
	@Test
	@Ignore
	public void testSetTargetInfoKT() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType type = new TargetInfoType();
		/**
		 * 定向方式
		 * 0  为主题词定向
		 * 1  为回头客定向
		 * 2  为搜客定向
		 * 3  为不定向
		 */
//		private int targetType;
//
//		// 推广组ID
//		private long groupId;
//
//		// CT主题词及状态
//		private CtItemType ctItem;
//
//		// RT关联关系及有效期
//		private RtItemType rtItem;
//
//		// QT词及有效期
//		private QtItemType qtItem;
//
//		// VT关联人群和排除人群
//		private VtItemType vtItem;
		type.setType(2);
		type.setGroupId(6774697l);
		KtItemType ktItem = new KtItemType();
		ktItem.setTargetType(7);
		ktItem.setAliveDays(30);
		KeywordType[] list = new KeywordType[1];
		KeywordType type1 = new KeywordType();
		type1.setKeyword("abcddd");
//		KeywordType type2 = new KeywordType();
//		type2.setKeyword("abc");
		list[0] = type1;
		//list[1] = type2;
		ktItem.setKtWordList(list);
		
		type.setKtItem(ktItem);
		request.setTargetInfo(type);
		ApiResult<Object> result = exporter.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
		
		GetTargetInfoRequest request2 = new GetTargetInfoRequest();
		request2.setGroupIds(new long[]{6774697l});
		ApiResult<TargetInfoType> result2 = exporter.getTargetInfo(dataUser, request2, apiOption);
		System.out.println(result2);
	}
	
	@Test
	@Ignore
	public void testSetTargetInfoVT() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType type = new TargetInfoType();
		/**
		 * 定向方式
		 * 0表示主题词定向，
1表示回头客定向，
2表示搜客定向，
3表示不启用定向投放
4 表示到访定向

		 */

//		// CT主题词及状态
//		private CtItemType ctItem;
//
//		// RT关联关系及有效期
//		private RtItemType rtItem;
//
//		// QT词及有效期
//		private QtItemType qtItem;
//
//		// VT关联人群和排除人群
//		private VtItemType vtItem;
		type.setType(4);
		type.setGroupId(245504);
		VtItemType vtItem = new VtItemType();
		vtItem.setRelatedPeopleIds(new Long[]{});
		vtItem.setUnRelatePeopleIds(new Long[]{});
		type.setVtItem(vtItem);
		request.setTargetInfo(type);
		ApiResult<Object> result = exporter.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
	}
	
//	@Test
//	public void testSetTargetInfoKT() throws Exception{
//		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(19,832979);
//		ApiOption apiOption = DarwinApiHelper.getApiOptions();
//		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/GroupConfigService", "UTF-8", new ExceptionHandler());
//		GroupConfigService exporter = ProxyFactory.createProxy(GroupConfigService.class, proxy);
//		SetTargetInfoRequest request = new SetTargetInfoRequest();
//		TargetInfoType type = new TargetInfoType();
//		/**
//		 * 定向方式
//		 * 0表示主题词定向，
//1表示回头客定向，
//2表示搜客定向，
//3表示不启用定向投放
//4 表示到访定向
//
//		 */
//
////		// CT主题词及状态
////		private CtItemType ctItem;
////
////		// RT关联关系及有效期
////		private RtItemType rtItem;
////
////		// QT词及有效期
////		private QtItemType qtItem;
////
////		// VT关联人群和排除人群
////		private VtItemType vtItem;
//		type.setTargetType(0);
//		type.setGroupId(2166159);
//		CtItemType ctItem = new CtItemType();
//		KeywordType keyword1 = new KeywordType();
//		keyword1.setStatus(0);
//		keyword1.setWord("dell123");
//		KeywordType keyword2 = new KeywordType();
//		keyword2.setStatus(0);
//		keyword2.setWord("世界");
//		KeywordType keyword3 = new KeywordType();
//		keyword3.setStatus(0);
//		keyword3.setWord("文明笔");
//		ctItem.setWordList(new KeywordType[]{
//				keyword1,
//				keyword2,
//				keyword3
//		});
//		type.setCtItem(ctItem);
//		request.setTargetInfo(type);
//		ApiResult<Object> result = exporter.setTargetInfo(dataUser, request, apiOption);
//		System.out.println(result);
//	}
	
	@Test
	@Ignore
	public void testSetTargetInfoRT() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType type = new TargetInfoType();
		/**
		 * 定向方式
		 * 0表示主题词定向，
1表示回头客定向，
2表示搜客定向，
3表示不启用定向投放
4 表示到访定向

		 */

		type.setType(1);
		type.setGroupId(6774697);
		RtItemType rtItem = new RtItemType();
		rtItem.setAliveDays(30);
		List<RtRelationType> list = new ArrayList<RtRelationType>();
		RtRelationType rtRel = new RtRelationType();
		rtRel.setFcPlanId(123l);
		rtRel.setFcPlanName("123");
		rtRel.setFcUnitId(456l);
		rtRel.setFcUnitName("456");
		rtRel.setRelationType(0);
		list.add(rtRel);
		rtItem.setRtRelationList(list);
		type.setRtItem(rtItem);
		request.setTargetInfo(type);
		ApiResult<Object> result = exporter.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
	}

	@Test
	@Ignore
	public void testSetTargetInfoNone() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetTargetInfoRequest request = new SetTargetInfoRequest();
		TargetInfoType type = new TargetInfoType();
		/**
		 * 定向方式
		 * 0表示主题词定向，
1表示回头客定向，
2表示搜客定向，
3表示不启用定向投放
4 表示到访定向

		 */

//		// CT主题词及状态
//		private CtItemType ctItem;
//
//		// RT关联关系及有效期
//		private RtItemType rtItem;
//
//		// QT词及有效期
//		private QtItemType qtItem;
//
//		// VT关联人群和排除人群
//		private VtItemType vtItem;
		type.setType(3);
		type.setGroupId(2166159);
//		VtItemType vtItem = new VtItemType();
//		vtItem.setRelatedPeopleIds(new Long[]{13l,14l});
//		vtItem.setUnRelatePeopleIds(new Long[]{15l});
//		type.setVtItem(vtItem);
		request.setTargetInfo(type);
		ApiResult<Object> result = exporter.setTargetInfo(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testGetKeywordByWordId() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetKeywordByWordIdRequest request = new GetKeywordByWordIdRequest();
		request.setWordIds(new long[]{1l,2l,0l});
		long start = System.currentTimeMillis();
		BaseResponse<WordType> result = exporter.getKeywordByWordId(dataUser2, request, apiOption2);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		System.out.println(result);
		
	}
	
	@Test
	@Ignore
	public void testSetExcludeIp() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetExcludeIpRequest request = new SetExcludeIpRequest();
		ExcludeIpType type = new ExcludeIpType();
		type.setGroupId(2166146);
		type.setExcludeIp(new String[]{
				"1.1.1.1",
//				"2.2.2.2",
//				"3.3.3.3",
//				"4.4.4.4",
//				"5.5.5.1"
				//"6.6.6.6",
		});
		request.setExcludeIp(type);
		ApiResult<Object> result = exporter.setExcludeIp(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testSetExcludeSite() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetExcludeSiteRequest request = new SetExcludeSiteRequest();
		ExcludeSiteType type = new ExcludeSiteType();
		type.setGroupId(2166146);
		type.setExcludeSite(new String[]{
				//"sina.com.cn",
				//"ifeng.com",
				//"china.com",
				//"exam8.com",
				//"huanqiu.com",
				//"7k7k.com",
				//"27.cn"
		});
		request.setExcludeSite(type);
		ApiResult<Object> result = exporter.setExcludeSite(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	public void testSetRegionConfig() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetRegionConfigRequest request = new SetRegionConfigRequest();
		RegionConfigType type = new RegionConfigType();
		type.setGroupId(228);
		type.setAllRegion(false);
		type.setRegionList(new RegionItemType[]{
//				new RegionItemType(1, 314), //北京
				new RegionItemType(1, 315), //昌平
//				new RegionItemType(1, 743),
//				new RegionItemType(1, 744),
//				new RegionItemType(1, 745),
//				new RegionItemType(1, 746),
//				new RegionItemType(1, 747),
//				new RegionItemType(1, 748),
//				new RegionItemType(1, 749),
//				new RegionItemType(1, 750), 
//				new RegionItemType(1, 751), 
//				new RegionItemType(1, 752),
//				new RegionItemType(1, 753),
//				new RegionItemType(1, 754), 
//				new RegionItemType(1, 755), 
//				new RegionItemType(1, 756), 
//				new RegionItemType(1, 757), 
//				new RegionItemType(1, 758), 
//				new RegionItemType(1, 759), 
		});
		request.setRegionConfig(type);
		request.setVersion("1.1");
		ApiResult<Object> result = exporter.setRegionConfig(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testAddRegionConfig() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		AddRegionRequest request = new AddRegionRequest();
		GroupRegionType[] types = new GroupRegionType[1];
		
		GroupRegionType type0 = new GroupRegionType();
		type0.setGroupId(228);
		type0.setRegionId(3);
		type0.setType(1);
		types[0] = type0;
		
//		GroupRegionType type1 = new GroupRegionType();
//		type1.setGroupId(228);
//		type1.setRegionId(54);
//		type1.setType(1);
//		types[1] = type1;
//		
//	      GroupRegionType type2 = new GroupRegionType();
//	        type2.setGroupId(228);
//	        type2.setRegionId(19);
//	        type2.setType(1);
//	        types[2] = type2;

		
		request.setRegions(types);
//		apiOption.getOptions().put("version", "1.1");
		request.setVersion("1.1");
		
		ApiResult<Object> result = exporter.addRegion(dataUser, request, apiOption);
		System.out.println(result);
	}
	
    @Test
    @Ignore
    public void testDeleteRegionConfig() throws Exception {
        GroupConfigService exporter =
                getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
        DeleteRegionRequest request = new DeleteRegionRequest();
        GroupRegionType[] types = new GroupRegionType[1];
        GroupRegionType type0 = new GroupRegionType();
        type0.setGroupId(228);
        type0.setRegionId(3);
        type0.setType(1);
        types[0] = type0;

        request.setRegions(types);
//        apiOption.getOptions().put("version", "1.1");
		request.setVersion("1.1");
        
        ApiResult<Object> result = exporter.deleteRegion(dataUser, request, apiOption);
        System.out.println(result);
    }
    
	@Test
	@Ignore
    public void testGetRegionConfig() throws Exception {
        GroupConfigService exporter =
                getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
        GetRegionConfigRequest request = new GetRegionConfigRequest();
        long[] groupids = new long[] { 228l };
        request.setGroupIds(groupids);

        // apiOption.getOptions().put("version", "1.1");
		request.setVersion("1.1");

        ApiResult<RegionConfigType> result = exporter.getRegionConfig(dataUser, request, apiOption);
        System.out.println(result);
    }
	
	@Test
	@Ignore
	public void testGetSiteConfig() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetSiteConfigRequest request = new GetSiteConfigRequest();
		request.setGroupIds(new long[]{228l});
		ApiResult<SiteConfigType> result = exporter.getSiteConfig(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testSetSiteConfig() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetSiteConfigRequest request = new SetSiteConfigRequest();
		SiteConfigType type = new SiteConfigType();
		type.setGroupId(245504);
		type.setAllSite(false);
//		type.setSiteList(new String[]{
//				"sina.com.cn",
//				"ifeng.com", //not match
//				//"china.com",  //not exist
//				//"exam8.com",  //not exist
//				//"huanqiu.com", 
//				//"7k7k.com", //not match
//				//"27.cn"
//		});
		type.setCategoryList(new int[]{
				9,
				11,
				283,
				260
		});
		request.setSiteConfig(type);
		ApiResult<Object> result = exporter.setSiteConfig(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testAddSite() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		AddSiteRequest request = new AddSiteRequest();
		
		GroupSiteType[] types = new GroupSiteType[3];
		
		GroupSiteType type0 = new GroupSiteType();
		type0.setGroupId(228);
		type0.setSite("7k7k.com");
		types[0] = type0;
		
		GroupSiteType type1 = new GroupSiteType();
		type1.setGroupId(228);
		type1.setSite("sina.com.cn");
		types[1] = type1;
		
		GroupSiteType type2 = new GroupSiteType();
		type2.setGroupId(228);
		type2.setSite("huanqiu.com");
		types[2] = type2;
		
		//"huanqiu.com", 
		//"ifeng.com", //not match
		//"china.com",  //not exist
		//"exam8.com",  //not exist
		//"7k7k.com", //not match
		
		request.setSites(types);
		ApiResult<Object> result = exporter.addSite(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testDeleteSite() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		DeleteSiteRequest request = new DeleteSiteRequest();
		
		GroupSiteType[] types = new GroupSiteType[3];
		
		GroupSiteType type0 = new GroupSiteType();
		type0.setGroupId(228);
		type0.setSite("7k7k.com");
		types[0] = type0;
		
		GroupSiteType type1 = new GroupSiteType();
		type1.setGroupId(228);
		type1.setSite("sina.com.cn");
		types[1] = type1;
		
		GroupSiteType type2 = new GroupSiteType();
		type2.setGroupId(228);
		type2.setSite("huanqiu.com");
		types[2] = type2;
		
		//"huanqiu.com", 
		//"ifeng.com", //not match
		//"china.com",  //not exist
		//"exam8.com",  //not exist
		//"7k7k.com", //not match
		
		request.setSites(types);
		ApiResult<Object> result = exporter.deleteSite(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testAddTrade() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		AddTradeRequest request = new AddTradeRequest();
		
		GroupTradeType[] types = new GroupTradeType[1];
		
		GroupTradeType type0 = new GroupTradeType();
		type0.setGroupId(228);
		type0.setTrade(1);
		types[0] = type0;
		
//		GroupTradeType type1 = new GroupTradeType();
//		type1.setGroupId(228);
//		type1.setTrade(3);
//		types[1] = type1;
//		
//		GroupTradeType type2 = new GroupTradeType();
//		type2.setGroupId(228);
//		type2.setTrade(260);
//		types[2] = type2;
		
		//"huanqiu.com", 
		//"ifeng.com", //not match
		//"china.com",  //not exist
		//"exam8.com",  //not exist
		//"7k7k.com", //not match
		
		request.setTrades(types);
		ApiResult<Object> result = exporter.addTrade(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testDeleteTrade() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		DeleteTradeRequest request = new DeleteTradeRequest();
		
		GroupTradeType[] types = new GroupTradeType[3];
		
		GroupTradeType type0 = new GroupTradeType();
		type0.setGroupId(228);
		type0.setTrade(2);
		types[0] = type0;
		
		GroupTradeType type1 = new GroupTradeType();
		type1.setGroupId(228);
		type1.setTrade(284);
		types[1] = type1;
		
		GroupTradeType type2 = new GroupTradeType();
		type2.setGroupId(228);
		type2.setTrade(270);
		types[2] = type2;
		
		//"huanqiu.com", 
		//"ifeng.com", //not match
		//"china.com",  //not exist
		//"exam8.com",  //not exist
		//"7k7k.com", //not match
		
		request.setTrades(types);
		ApiResult<Object> result = exporter.deleteTrade(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testSetSiteUrl() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetSiteUrlRequest request = new SetSiteUrlRequest();
		SiteUrlType type = new SiteUrlType();
		type.setGroupId(2166146);
		List<SiteUrlItemType> siteUrlList = new ArrayList<SiteUrlItemType>();
		
		SiteUrlItemType item1= new SiteUrlItemType();
		item1.setSiteUrl("27.cn");
		item1.setTargetUrl("http://baidu.com/27.cn");
		siteUrlList.add(item1);
		
		SiteUrlItemType item2= new SiteUrlItemType();
		item2.setSiteUrl("china.com");
		item2.setTargetUrl("http://baidu.com/china.com");
		siteUrlList.add(item2);
		
		SiteUrlItemType item3= new SiteUrlItemType();
		item3.setSiteUrl("ifeng.com");
		item3.setTargetUrl("http://baidu.com/ifeng.com");
		siteUrlList.add(item3);
		
		type.setSiteUrlList(siteUrlList);
		request.setSiteUrl(type);
		ApiResult<Object> result = exporter.setSiteUrl(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	
	
	@Test
	@Ignore
	public void testSetTradeSitePrice() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetTradeSitePriceRequest request = new SetTradeSitePriceRequest();
		TradeSitePriceType type = new TradeSitePriceType();
		type.setGroupId(2166146);

		List<TradePriceType> tradePriceList = new ArrayList<TradePriceType>();
		
		TradePriceType tradeItem1= new TradePriceType();
		tradeItem1.setTradeId(9);
		tradeItem1.setPrice(2.11f);
		//tradePriceList.add(tradeItem1);
		
		TradePriceType tradeItem2= new TradePriceType();
		tradeItem2.setTradeId(11);
		tradeItem2.setPrice(2.12f);
		//tradePriceList.add(tradeItem2);
		
		TradePriceType tradeItem3= new TradePriceType();
		tradeItem3.setTradeId(14);
		tradeItem3.setPrice(2.13f);
		//tradePriceList.add(tradeItem3);
		
		type.setTradePriceList(tradePriceList);
		
		List<SitePriceType> sitePriceList = new ArrayList<SitePriceType>();
		
		SitePriceType siteItem1= new SitePriceType();
		siteItem1.setSite("27.cn");
		siteItem1.setPrice(2.7f);
		//sitePriceList.add(siteItem1);
		
		SitePriceType siteItem2= new SitePriceType();
		siteItem2.setSite("sina.com.cn");
		siteItem2.setPrice(2.1111f);
		//sitePriceList.add(siteItem2);
		
		SitePriceType siteItem3= new SitePriceType();
		siteItem3.setSite("china.com");
		siteItem3.setPrice(3.33333f);
		//sitePriceList.add(siteItem3);
		
		type.setSitePriceList(sitePriceList);
		
		request.setTradeSitePrice(type);
		ApiResult<Object> result = exporter.setTradeSitePrice(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	
	
	@Test
	@Ignore
	public void testGetInterestInfo() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetInterestInfoRequest request = new GetInterestInfoRequest();
		request.setGroupIds(new long[]{228});
		ApiResult<InterestInfoType> result = exporter.getInterestInfo(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testSetInterestInfo() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetInterestInfoRequest request = new SetInterestInfoRequest();
		InterestInfoType type = new InterestInfoType();
		type.setEnable(false);
		type.setGroupId(228);
		type.setInterestIds(new int[]{-1});
		type.setExceptInterestIds(new int[]{-1});
		request.setInterestInfo(type);
		ApiResult<Object> result = exporter.setInterestInfo(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testAddInterestInfo() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		AddInterestInfoRequest request = new AddInterestInfoRequest();
		GroupInterestInfoType[] types = new GroupInterestInfoType[1];
		GroupInterestInfoType type = new GroupInterestInfoType();
		types[0] = type;
		type.setGroupId(228);
		type.setInterestIds(new int[]{0, 2, 23, 601, 100001});
		request.setInterests(types);
		ApiResult<Object> result = exporter.addInterestInfo(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testDeleteInterestInfo() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		DeleteInterestInfoRequest request = new DeleteInterestInfoRequest();
		GroupInterestInfoType[] types = new GroupInterestInfoType[1];
		GroupInterestInfoType type = new GroupInterestInfoType();
		types[0] = type;
		type.setGroupId(228);
		type.setInterestIds(new int[]{0, 2, 23, 601, 100001});
		request.setInterests(types);
		ApiResult<Object> result = exporter.deleteInterestInfo(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testSetKeyword() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetKeywordRequest request = new SetKeywordRequest();
		GroupKeywordItemType[] types = new GroupKeywordItemType[1];
		GroupKeywordItemType type = new GroupKeywordItemType();
		types[0] = type;
		type.setGroupId(228);
		type.setKeyword("test");
		type.setPattern(0);
		request.setKeywords(types);
		ApiResult<Object> result = exporter.setKeyword(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testGetKeyword() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetKeywordRequest request = new GetKeywordRequest();
		request.setKeywordIds(new Long[1]);
		ApiResult<KeywordType> result = exporter.getKeyword(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testAddKeyword() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		AddKeywordRequest request = new AddKeywordRequest();
		GroupKeywordItemType[] types = new GroupKeywordItemType[1];
		GroupKeywordItemType type = new GroupKeywordItemType();
		types[0] = type;
		type.setGroupId(228);
		type.setKeyword("test");
		type.setPattern(0);
		request.setKeywords(types);
		ApiResult<Object> result = exporter.addKeyword(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testDeleteKeyword() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		DeleteKeywordRequest request = new DeleteKeywordRequest();
		GroupKeywordItemType[] types = new GroupKeywordItemType[1];
		GroupKeywordItemType type = new GroupKeywordItemType();
		types[0] = type;
		type.setGroupId(228);
		type.setKeyword("test");
		type.setPattern(0);
		request.setKeywords(types);
		ApiResult<Object> result = exporter.deleteKeyword(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testAddRtRelation() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		AddRtRelationRequest request = new AddRtRelationRequest();
		GroupRtItemType[] rtRelations = new GroupRtItemType[1];
		GroupRtItemType type = new GroupRtItemType();
		type.setGroupId(6774697l);
		type.setRelationType(0);
		rtRelations[0] = type;
		request.setRtRelations(rtRelations);
//		GroupRegionType type1 = new GroupRegionType();
//		type1.setGroupId(228);
//		type1.setRegionId(742);
//		type1.setType(1);
//		types[1] = type0;
		
		ApiResult<Object> result = exporter.addRtRelation(dataUser, request, apiOption);
		System.out.println(result);
	}
	
}
