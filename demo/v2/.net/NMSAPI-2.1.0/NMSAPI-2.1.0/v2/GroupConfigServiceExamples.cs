using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.GroupConfigService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class GroupConfigServiceExamples
    {
        public static void getKeyword()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            KeywordType[] result = null;

            ResHeader resHeader = client.getKeyword(authHeader, new long[] { 202132875L, 202132876L, 202132877L }, out result);

            Console.WriteLine("GroupConfigService.getKeyword(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getExcludeIp()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            ExcludeIpType[] result = null;

            ResHeader resHeader = client.getExcludeIp(authHeader, new long[] { 2562221L }, out result);

            Console.WriteLine("GroupConfigService.getExcludeIp(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getExcludeSite()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            ExcludeSiteType[] result = null;

            ResHeader resHeader = client.getExcludeSite(authHeader, new long[] { 2562221L }, out result);

            Console.WriteLine("GroupConfigService.getExcludeSite(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getRegionConfig()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            RegionConfigType[] result = null;

            ResHeader resHeader = client.getRegionConfig(authHeader, new long[] { 2562221L }, out result);

            Console.WriteLine("GroupConfigService.getRegionConfig(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getSiteConfig()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            SiteConfigType[] result = null;

            ResHeader resHeader = client.getSiteConfig(authHeader, new long[] { 2562221L }, out result);

            Console.WriteLine("GroupConfigService.getSiteConfig(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getSiteUrl()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            SiteUrlType[] result = null;

            ResHeader resHeader = client.getSiteUrl(authHeader, new long[] { 2562221L }, out result);

            Console.WriteLine("GroupConfigService.getSiteUrl(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getTargetInfo()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            TargetInfoType[] result = null;

            ResHeader resHeader = client.getTargetInfo(authHeader, new long[] { 3066066L }, out result);

            Console.WriteLine("GroupConfigService.getTargetInfo(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getTradeSitePrice()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            TradeSitePriceType[] result = null;

            ResHeader resHeader = client.getTradeSitePrice(authHeader, new long[] { 2562221L }, out result);

            Console.WriteLine("GroupConfigService.getTradeSitePrice(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getInterestInfo()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            InterestInfoType[] result = null;

            ResHeader resHeader = client.getInterestInfo(authHeader, new long[] { 3066066L }, out result);

            Console.WriteLine("GroupConfigService.getInterestInfo(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void setExcludeIp()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            ExcludeIpType parameters = new ExcludeIpType();
            parameters.groupId = 2562221L;
            parameters.excludeIp = new String[2];
            parameters.excludeIp[0] = "111.111.111.111";
            parameters.excludeIp[1] = "111.111.111.*";

            String result = null;

            ResHeader resHeader = client.setExcludeIp(authHeader, parameters, out result);

            Console.WriteLine("GroupConfigService.setExcludeIp(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void setExcludeSite()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            ExcludeSiteType parameters = new ExcludeSiteType();
            parameters.groupId = 2562221L;
            parameters.excludeSite = new String[2];
            parameters.excludeSite[0] = "7k7k.com";
            parameters.excludeSite[1] = "autohome.com.cn";

            String result = null;

            ResHeader resHeader = client.setExcludeSite(authHeader, parameters, out result);

            Console.WriteLine("GroupConfigService.setExcludeSite(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void setRegionConfig()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            RegionConfigType parameters = new RegionConfigType();
            parameters.groupId = 2562221L;
            parameters.allRegion = false;
            parameters.regionList = new RegionItemType[2];
            RegionItemType regionType = new RegionItemType();
            regionType.type = 1;
            regionType.regionId = 1000;
            parameters.regionList[0] = regionType;
            regionType = new RegionItemType();
            regionType.type = 1;
            regionType.regionId = 2000;
            parameters.regionList[1] = regionType;

            String result = null;

            ResHeader resHeader = client.setRegionConfig(authHeader, parameters, out result);

            Console.WriteLine("GroupConfigService.setRegionConfig(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void setSiteConfig()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            SiteConfigType parameters = new SiteConfigType();
            parameters.groupId = 2562221L;
            parameters.allSite = false;
            parameters.siteList = new String[2];
            parameters.siteList[0] = "7k7k.com";
            parameters.siteList[1] = "autohome.com.cn";
            parameters.categoryList = new int[2];
            parameters.categoryList[0] = 1;
            parameters.categoryList[1] = 2;

            String result = null;

            ResHeader resHeader = client.setSiteConfig(authHeader, parameters, out result);

            Console.WriteLine("GroupConfigService.setSiteConfig(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void setSiteUrl()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            SiteUrlType parameters = new SiteUrlType();
            parameters.groupId = 2562221L;
            parameters.siteUrlList = new SiteUrlItemType[1];
            SiteUrlItemType siteUrl = new SiteUrlItemType();
            siteUrl.siteUrl = "autohome.com.cn";
            siteUrl.targetUrl = "http://baidu.com/autohome.com.cn";
            parameters.siteUrlList[0] = siteUrl;

            String result = null;

            ResHeader resHeader = client.setSiteUrl(authHeader, parameters, out result);

            Console.WriteLine("GroupConfigService.setSiteUrl(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void setTargetInfo()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            TargetInfoType parameters = new TargetInfoType();
            parameters.groupId = 3066066L;

            //取值范围：
            //1表示回头客定向，
		    //2表示关键词定向，
		    //3表示不启用受众行为，
		    //4表示到访定向
            parameters.type = 2;
            KtItemType ktItem = new KtItemType();
            ktItem.aliveDays = 30;
            ktItem.targetType = 5;
            KeywordType[] wordList = new KeywordType[3];
            KeywordType word = new KeywordType();
            word.keyword = "鲜花";
            word.pattern = 0;
            wordList[0] = word;
            word = new KeywordType();
            word.keyword = "百合";
            word.pattern = 1;
            wordList[1] = word;
            word = new KeywordType();
            word.keyword = "玫瑰";
            word.pattern = 0;
            wordList[2] = word;
            ktItem.ktWordList = wordList;
            parameters.ktItem = ktItem;

            String result = null;

            ResHeader resHeader = client.setTargetInfo(authHeader, parameters, out result);

            Console.WriteLine("GroupConfigService.setTargetInfo(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void setTradeSitePrice()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            TradeSitePriceType parameters = new TradeSitePriceType();
            parameters.groupId = 2562221L;
            parameters.tradePriceList = new TradePriceType[1];
            TradePriceType tradePrice = new TradePriceType();
            tradePrice.tradeId = 2;
            tradePrice.price = 2.0f;
            parameters.tradePriceList[0] = tradePrice;

            parameters.sitePriceList = new SitePriceType[1];
            SitePriceType sitePrice = new SitePriceType();
            sitePrice.site = "7k7k.com";
            sitePrice.price = 2.0f;
            parameters.sitePriceList[0] = sitePrice;

            String result = null;

            ResHeader resHeader = client.setTradeSitePrice(authHeader, parameters, out result);

            Console.WriteLine("GroupConfigService.setTradeSitePrice(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void setInterestInfo()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            InterestInfoType interestInfo = new InterestInfoType();
            interestInfo.groupId = 3066066L;
            interestInfo.enable = true;
            interestInfo.interestIds = new int[] { 52, 56};
            interestInfo.exceptInterestIds = new int[] { 69 };

            String result = null;

            ResHeader resHeader = client.setInterestInfo(authHeader, interestInfo, out result);

            Console.WriteLine("GroupConfigService.setInterestInfo(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addInterestInfo()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupInterestInfoType[] interests = new GroupInterestInfoType[1];
            GroupInterestInfoType groupIt = new GroupInterestInfoType();
            groupIt.groupId = 3066066L;
            groupIt.interestIds = new long[] { 8, 9 };
            groupIt.exceptInterestIds = new long[] { 10 };
            interests[0] = groupIt;

            String result = null;

            ResHeader resHeader = client.addInterestInfo(authHeader, interests, out result);

            Console.WriteLine("GroupConfigService.addInterestInfo(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addKeyword()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupKeywordItemType[] keywords = new GroupKeywordItemType[1];
            GroupKeywordItemType keyword = new GroupKeywordItemType();
            keyword.groupId = 3066066L;
            keyword.keyword = "鲜花11";
            keyword.pattern = 0;
            keyword.patternSpecified = true;
            keywords[0] = keyword;

            String result = null;

            ResHeader resHeader = client.addKeyword(authHeader, keywords, out result);

            Console.WriteLine("GroupConfigService.addKeyword(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addSite()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupSiteType[] sites = new GroupSiteType[1];
            GroupSiteType site = new GroupSiteType();
            site.groupId = 3066066L;
            site.site = "2144.cn";
            sites[0] = site;

            String result = null;

            ResHeader resHeader = client.addSite(authHeader, sites, out result);

            Console.WriteLine("GroupConfigService.addSite(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addTrade()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupTradeType[] trades = new GroupTradeType[1];
            GroupTradeType trade = new GroupTradeType();
            trade.groupId = 3066066L;
            trade.trade = 1;
            trades[0] = trade;

            String result = null;

            ResHeader resHeader = client.addTrade(authHeader, trades, out result);

            Console.WriteLine("GroupConfigService.addTrade(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addRegion()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupRegionType[] regions = new GroupRegionType[1];
            GroupRegionType region = new GroupRegionType();
            region.groupId = 3066066L;
            region.regionId = 1;
            regions[0] = region;

            String result = null;

            ResHeader resHeader = client.addRegion(authHeader, regions, out result);

            Console.WriteLine("GroupConfigService.addRegion(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addExcludeIp()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupExcludeIpType[] excludeIps = new GroupExcludeIpType[1];
            GroupExcludeIpType excludeIp = new GroupExcludeIpType();
            excludeIp.groupId = 3066066L;
            excludeIp.excludeIp = "111.111.11.1";
            excludeIps[0] = excludeIp;

            String result = null;

            ResHeader resHeader = client.addExcludeIp(authHeader, excludeIps, out result);

            Console.WriteLine("GroupConfigService.addExcludeIp(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addExcludeSite()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupExcludeSiteType[] excludeSites = new GroupExcludeSiteType[1];
            GroupExcludeSiteType excludeSite = new GroupExcludeSiteType();
            excludeSite.groupId = 3066066L;
            excludeSite.excludeSite = "2144.cn";
            excludeSites[0] = excludeSite;

            String result = null;

            ResHeader resHeader = client.addExcludeSite(authHeader, excludeSites, out result);

            Console.WriteLine("GroupConfigService.addExcludeSite(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addTradePrice()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupTradePriceType[] tradePrices = new GroupTradePriceType[1];
            GroupTradePriceType tradePrice = new GroupTradePriceType();
            tradePrice.groupId = 3066066L;
            tradePrice.trade = 1;
            tradePrice.price = 111;
            tradePrices[0] = tradePrice;

            String result = null;

            ResHeader resHeader = client.addTradePrice(authHeader, tradePrices, out result);

            Console.WriteLine("GroupConfigService.addTradePrice(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addSitePrice()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupSitePriceType[] sitePrices = new GroupSitePriceType[1];
            GroupSitePriceType sitePrice = new GroupSitePriceType();
            sitePrice.groupId = 3066066L;
            sitePrice.site = "sina.com.cn";
            sitePrice.price = 111;
            sitePrices[0] = sitePrice;

            String result = null;

            ResHeader resHeader = client.addSitePrice(authHeader, sitePrices, out result);

            Console.WriteLine("GroupConfigService.addSitePrice(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addRtRelation()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            // 回头客的关联类型：1-关联到推广计划，0-关联到推广单元
            GroupRtItemType[] rtItems = new GroupRtItemType[1];
            GroupRtItemType rtItem = new GroupRtItemType();
            rtItem.groupId = 3066066L;
            rtItem.relationType = 1;
            rtItem.fcPlanId = 415916L;
            rtItems[0] = rtItem;

            String result = null;

            ResHeader resHeader = client.addRtRelation(authHeader, rtItems, out result);

            Console.WriteLine("GroupConfigService.addRtRelation(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void addVtPeople()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            // 人群类型：0-关联人群，1-排除人群
            GroupVtItemType[] vtItems = new GroupVtItemType[1];
            GroupVtItemType vtItem = new GroupVtItemType();
            vtItem.groupId = 3066066L;
            vtItem.relationType = 1;
            vtItem.peopleId = 90000317;
            vtItems[0] = vtItem;

            String result = null;

            ResHeader resHeader = client.addVtPeople(authHeader, vtItems, out result);

            Console.WriteLine("GroupConfigService.addVtPeople(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteVtPeople()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            // 人群类型：0-关联人群，1-排除人群
            GroupVtItemType[] vtItems = new GroupVtItemType[1];
            GroupVtItemType vtItem = new GroupVtItemType();
            vtItem.groupId = 3066066L;
            vtItem.relationType = 1;
            vtItem.peopleId = 90000317;
            vtItems[0] = vtItem;

            String result = null;

            ResHeader resHeader = client.deleteVtPeople(authHeader, vtItems, out result);

            Console.WriteLine("GroupConfigService.deleteVtPeople(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteRtRelation()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            // 回头客的关联类型：1-关联到推广计划，0-关联到推广单元
            GroupRtItemType[] rtItems = new GroupRtItemType[1];
            GroupRtItemType rtItem = new GroupRtItemType();
            rtItem.groupId = 3066066L;
            rtItem.relationType = 1;
            rtItem.fcPlanId = 415916L;
            rtItems[0] = rtItem;

            String result = null;

            ResHeader resHeader = client.deleteRtRelation(authHeader, rtItems, out result);

            Console.WriteLine("GroupConfigService.deleteRtRelation(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteSitePrice()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupSiteType[] sitePrices = new GroupSiteType[1];
            GroupSiteType sitePrice = new GroupSiteType();
            sitePrice.groupId = 3066066L;
            sitePrice.site = "sina.com.cn";
            sitePrices[0] = sitePrice;

            String result = null;

            ResHeader resHeader = client.deleteSitePrice(authHeader, sitePrices, out result);

            Console.WriteLine("GroupConfigService.deleteSitePrice(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteTradePrice()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupTradeType[] tradePrices = new GroupTradeType[1];
            GroupTradeType tradePrice = new GroupTradeType();
            tradePrice.groupId = 3066066L;
            tradePrice.trade = 1;
            tradePrices[0] = tradePrice;

            String result = null;

            ResHeader resHeader = client.deleteTradePrice(authHeader, tradePrices, out result);

            Console.WriteLine("GroupConfigService.deleteTradePrice(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteExcludeSite()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupExcludeSiteType[] excludeSites = new GroupExcludeSiteType[1];
            GroupExcludeSiteType excludeSite = new GroupExcludeSiteType();
            excludeSite.groupId = 3066066L;
            excludeSite.excludeSite = "2144.cn";
            excludeSites[0] = excludeSite;

            String result = null;

            ResHeader resHeader = client.deleteExcludeSite(authHeader, excludeSites, out result);

            Console.WriteLine("GroupConfigService.deleteExcludeSite(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteExcludeIp()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupExcludeIpType[] excludeIps = new GroupExcludeIpType[1];
            GroupExcludeIpType excludeIp = new GroupExcludeIpType();
            excludeIp.groupId = 3066066L;
            excludeIp.excludeIp = "111.111.11.1";
            excludeIps[0] = excludeIp;

            String result = null;

            ResHeader resHeader = client.deleteExcludeIp(authHeader, excludeIps, out result);

            Console.WriteLine("GroupConfigService.deleteExcludeIp(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteRegion()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupRegionType[] regions = new GroupRegionType[1];
            GroupRegionType region = new GroupRegionType();
            region.groupId = 3066066L;
            region.regionId = 1;
            regions[0] = region;

            String result = null;

            ResHeader resHeader = client.deleteRegion(authHeader, regions, out result);

            Console.WriteLine("GroupConfigService.deleteRegion(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteTrade()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupTradeType[] trades = new GroupTradeType[1];
            GroupTradeType trade = new GroupTradeType();
            trade.groupId = 3066066L;
            trade.trade = 1;
            trades[0] = trade;

            String result = null;

            ResHeader resHeader = client.deleteTrade(authHeader, trades, out result);

            Console.WriteLine("GroupConfigService.deleteTrade(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteSite()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupSiteType[] sites = new GroupSiteType[1];
            GroupSiteType site = new GroupSiteType();
            site.groupId = 3066066L;
            site.site = "2144.cn";
            sites[0] = site;

            String result = null;

            ResHeader resHeader = client.deleteSite(authHeader, sites, out result);

            Console.WriteLine("GroupConfigService.deleteSite(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteKeyword()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupKeywordItemType[] keywords = new GroupKeywordItemType[1];
            GroupKeywordItemType keyword = new GroupKeywordItemType();
            keyword.groupId = 3066066L;
            keyword.keyword = "鲜花11";
            keyword.pattern = 0;
            keyword.patternSpecified = true;
            keywords[0] = keyword;

            String result = null;

            ResHeader resHeader = client.deleteKeyword(authHeader, keywords, out result);

            Console.WriteLine("GroupConfigService.deleteKeyword(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteInterestInfo()
        {
            GroupConfigServiceClient client = new GroupConfigServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupInterestInfoType[] interests = new GroupInterestInfoType[1];
            GroupInterestInfoType groupIt = new GroupInterestInfoType();
            groupIt.groupId = 3066066L;
            groupIt.interestIds = new long[] { 8, 9 };
            groupIt.exceptInterestIds = new long[] { 10 };
            interests[0] = groupIt;

            String result = null;

            ResHeader resHeader = client.deleteInterestInfo(authHeader, interests, out result);

            Console.WriteLine("GroupConfigService.deleteInterestInfo(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }
    }
}
