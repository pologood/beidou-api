package com.baidu.beidou.api.external.cprogroup.constant;

import com.baidu.beidou.cprogroup.constant.CproGroupConstant;

/**
 * ClassName: GroupConstant Function: 推广组层级相关配置
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
public class GroupConstant {

	public static final String GROUP = "group";
	public static final String ADS = "ads";
	public static final String PLANID = "campaignId";
	public static final String GROUPIDS = "groupIds";
	public static final String GROUPID = "groupId";
	public static final String GROUP_INFO = "groupTypes";
	public static final String TARGET_INFO = "targetInfo";
	public static final String KT_ITEM = "ktItem";
	public static final String RT_ITEM = "rtItem";
	public static final String VT_ITEM = "vtItem";
	public static final String KEYWORDID_LIST = "keywordIds";
	public static final String WORDID_LIST = "wordIds";
	public static final String INTEREST_INFO = "interestInfo";

	public static final String TARGETTYPE = "targetType";
	public static final String EXCLUDE_IP_REQ = "excludeIp";
	public static final String EXCLUDE_IP_TYPE = "excludeIp";
	public static final String REGION_CONFIG_REQ = "regionConfig";
	public static final String REGION_CONFIG_TYPE = "regionList";
	public static final String SITE_CONFIG_REQ = "siteConfig";
	public static final String SITE_CONFIG_SITELIST = "siteList";
	public static final String SITE_CONFIG_CATEGORYLIST = "categoryList";
	public static final String EXCLUDE_SITE_REQ = "excludeSite";
	public static final String EXCLUDE_SITE_TYPE = "excludeSite";
	public static final String TRADE_SITE_PRICE_REQ = "tradeSitePrice";
	public static final String TRADE_PRICE_LIST = "tradePriceList";
	public static final String SITE_PRICE_LIST = "sitePriceList";
	public static final String SITE_URL_REQ = "siteUrl";
	public static final String SITE_URL_LIST = "siteUrlList";
	public static final String INTEREST_IDS = "interestIds";
	public static final String EXCEPT_INTEREST_IDS = "exceptInterestIds";
	
	// GroupConfigService add&delete
	public static final String TYPE = "type";
	public static final String KEYWORDS = "keywords";
	public static final String INTERESTS = "interests";	
	public static final String PATTERN = "pattern";
	public static final String PATTERN_TYPE = "patternType";
	public static final String KEYWORD = "keyword";
	public static final String RT_RELATIONS = "rtRelations";
	public static final String RT_FC_PLAN_ID = "fcPlanId";
	public static final String RT_FC_UNIT_ID = "fcUnitId";
	public static final String VT_PEOPLES = "vtPeoples";
	public static final String VT_PEOPLE_ID = "peopleId";
	public static final String SITES = "sites";
	public static final String SITE = "site";
	public static final String TRADES = "trades";
	public static final String TRADE = "trade";
	public static final String REGIONS = "regions";
	public static final String REGION_ID = "regionId";
	public static final String EXCLUDE_IPS = "excludeIps";
	public static final String EXCLUDE_IP = "excludeIp";
	public static final String EXCLUDE_SITES = "excludeSites";
	public static final String EXCLUDE_APPS = "excludeApps";
	public static final String EXCLUDE_APP = "excludeApp";
	public static final String EXCLUDE_SITE = "excludeSite";
	public static final String SITE_PRICES = "sitePrices";
	public static final String TRADE_PRICES = "tradePrices";
	public static final String EXCLUDE_KEYWORD = "excludeKeyword";
	public static final String EXCLUDE_KEYWORDS = "excludeKeywords";
	public static final String EXCLUDE_KEYWORD_PACKID = "excludeKeywordPackId";
	public static final String EXCLUDE_KEYWORD_PACKIDS = "excludeKeywordPackIds";
	public static final String EXCLUDE_PEOPLE = "excludePeople";
	public static final String EXCLUDE_PEOPLES = "excludePeoples";
	public static final String EXCLUDE_PEOPLEID = "excludePeopleId";
	public static final String EXCLUDE_PEOPLEIDS = "excludePeopleIds";
	public static final String PACK_INFO = "packInfo";
	public static final String PACK_ITEMS = "packItems";
	public static final String PACKS = "packs";
	public static final String PACKID = "packId";
	public static final String PRICES = "prices";
	public static final String PRICE = "price";
	public static final String ID1 = "id1";
	public static final String ID2 = "id2";
	public static final String PACKTYPE = "packType";
	
	public static final String POSITION_GROUPNAME = "groupName";
	public static final String POSITION_PRICE = "price";
	public static final String POSITION_STATUS = "status";
	public static final String POSITION_SITE = "site";
	public static final String POSITION_TRADE = "tradeId";
	public static final String POSITION_TARGET_URL = "targetUrl";
	public static final String POSITION_SITE_URL = "siteUrl";
	public static final String POSITION_WORD = "word";
	public static final String POSITION_RT_ALIVEDAYS = "aliveDays";
	public static final String POSITION_KT_ALIVEDAYS = "aliveDays";
	public static final String POSITION_RT_RELATIONLIST = "rtRelationList";
	public static final String POSITION_KT_WORDLIST = "ktWordList";
	public static final String POSITION_KT_TARGETTYPE = "targetType";
	public static final String POSITION_GROUPTYPE = "type";
	public static final String POSITION_GROUP_GENDERINFO = "excludeGender";
	
    public static final String POSITION_ATTACH_INFO = "attachInfos";
    public static final String POSITION_ATTACH_TYPE_LIST = "attachTypes";
    public static final String POSITION_ATTACH_TYPE = "attachType";
    public static final String POSITION_ATTACH_SUB_URL_PARAM = "attachSubUrlParam";
    public static final String POSITION_ATTACH_SUB_URL_LIST = "attachSubUrls";
    public static final String POSITION_ATTACH_SUB_URL_TITLE = "attachSubUrlTitle";
    public static final String POSITION_ATTACH_SUB_URL_LINK = "attachSubUrlLink";
    public static final String POSITION_ATTACH_SUB_URL_WIRELESS_LINK = "attachSubUrlWirelessLink";

	public static final String POSITION_VT_RELATED_PEOPLE = "relatedPeopleIds";
	
	public static final String CLEAR_FLAG_STR = "$";

	// 查询推广组最多返回的数量（因北斗星中对一个计划下的推广组总数量不再限制，为防止极端情况，添加了数量限制）
	public static final int MAX_QUERY_GROUP_NUM = 1500;
	
	// add和delete接口相关阈值设置
	public static final int ADD_DEL_MAX_GROUPS_NUM = 100;		// 一次添加删除操作最多的目标推广组个数
	
	public static final int KEYWORDS_ADD_MAX_NUM = 2000;		// 一次最多添加的KT词的个数
	public static final int KEYWORDS_DEL_MAX_NUM = 2000;		// 一次最多删除的KT词的个数
	public static final int RT_ADD_MAX_RELATIONS_NUM = 1000;	// 一次最多添加的RT关联关系的个数
	public static final int RT_DEL_MAX_RELATIONS_NUM = 1000;	// 一次最多删除的RT关联关系的个数
	public static final int VT_ADD_MAX_PEOPLES_NUM = 100;		// 一次最多添加的VT人群的个数
	public static final int VT_DEL_MAX_PEOPLES_NUM = 100;		// 一次最多删除的VT人群的个数
	public static final int IT_ADD_MAX_RELATIONS_NUM = 1000;	// 一次最多添加的IT关联关系的个数
	public static final int IT_DEL_MAX_RELATIONS_NUM = 1000;	// 一次最多添加的IT关联关系的个数
	public static final int SITE_ADD_MAX_NUM = 1000;		// 一次最多添加的投放网站的个数
	public static final int SITE_DEL_MAX_NUM = 1000;		// 一次最多删除的投放网站的个数
	public static final int TRADE_ADD_MAX_NUM = 1000;		// 一次最多添加的投放行业的个数
	public static final int TRADE_DEL_MAX_NUM = 1000;		// 一次最多删除的投放行业的个数
	public static final int REGION_ADD_MAX_NUM = 1000;		// 一次最多添加的投放地域的个数
	public static final int REGION_DEL_MAX_NUM = 1000;		// 一次最多删除的投放地域的个数
	public static final int EXCLUDE_IP_ADD_MAX_NUM = 1000;		// 一次最多添加的投放地域的个数
	public static final int EXCLUDE_IP_DEL_MAX_NUM = 1000;		// 一次最多删除的投放地域的个数
	public static final int EXCLUDE_SITE_ADD_MAX_NUM = 1000;		// 一次最多添加的投放网站的个数
	public static final int EXCLUDE_APP_ADD_MAX_NUM = 100;		// 一次最多添加的排除应用的个数
	public static final int GROUP_EXCLUDE_APP_MAX_NUM = 20000;  //一个推广组最多排除的应用个数
	public static final int EXCLUDE_SITE_DEL_MAX_NUM = 1000;		// 一次最多删除的投放地域的个数
	public static final int SITE_PRICE_ADD_MAX_NUM = 1000;		// 一次最多添加的投放地域的个数
	public static final int SITE_PRICE_DEL_MAX_NUM = 1000;		// 一次最多删除的投放地域的个数
	public static final int TRADE_PRICE_ADD_MAX_NUM = 1000;			// 一次最多添加的分行业出价个数
	public static final int TRADE_PRICE_DEL_MAX_NUM = 1000;			// 一次最多删除的分行业出价个数
	public static final int EXCLUDE_KEYWORD_OR_PACK_ADD_MAX_NUM = 1000;			// 一次最多添加的排除关键词、组合数量
	public static final int EXCLUDE_KEYWORD_OR_PACK_DEL_MAX_NUM = 1000;			// 一次最多删除的排除关键词、组合数量
	public static final int EXCLUDE_PEOPLE_ADD_MAX_NUM = 1000;			// 一次最多添加的排除人群数量
	public static final int EXCLUDE_PEOPLE_DEL_MAX_NUM = 1000;			// 一次最多删除的排除人群数量
	public static final int GROUP_PACK_ADD_MAX_NUM = 1000;			// 一次最多添加的受众组合数量
	public static final int GROUP_PACK_DEL_MAX_NUM = 1000;			// 一次最多删除的受众组合数量
	public static final int GROUP_PRICE_SET_MAX_NUM = 500;			// 一次最多设置的出价设置
	
	public static final int RT_RELATIONS_MAX_NUM = 1000;	// 单个推广组可以添加的最多RT关联关系个数
	
	public static final int GROUP_PRICE_NOT_CHANGE_VALUE = -1;	// 当前端提交的price=-1时，表示不更新推广组出价
	
	public static final int API_TARGET_TYPE_RT = 1;
	public static final int API_TARGET_TYPE_KT = 2;
	public static final int API_TARGET_TYPE_NONE = 3;
	public static final int API_TARGET_TYPE_VT = 4;
	public static final int API_TARGET_TYPE_ADVANCED = 5;
	public static final int API_TARGET_TYPE_ATRIGHT = 6;
	
	// GroupType中的type特殊含义，代表设置高级组合投放
	public static final int API_GROUP_TYPE_ADVANCED = 8;
	
	public static final int API_KT_TARGET_TYPE_CT = 1;
	public static final int API_KT_TARGET_TYPE_QT = 1<<1;
	public static final int API_KT_TARGET_TYPE_HCT = 1<<2;

	//全局不更新某些属性的标志位
	public static final int API_GLOBAL_NOT_UPDATE_FLAG_INT = -1;
	public static final String API_GLOBAL_NOT_UPDATE_FLAG_STR = "-1";
	public static final String API_KEYWORD_NOT_UPDATE_FLAG = "*";
	
	//API出价接口用类型参数
	public static final int GROUP_INTEREST_PRICE_TYPE = 1;
	public static final int GROUP_PACK_PRICE_TYPE = 2;
	public static final int GROUP_ADVANCED_INTEREST_PRICE_TYPE = 3;
	public static final int API_PRICE_NOT_UPDATE_FLAG = -1;
	
	// 新建推广组的默认展现类型：固定+悬浮
	public static final int ADD_GROUP_DEFAULT_GROUP_TYPE = 
			CproGroupConstant.GROUP_TYPE_FIXED | CproGroupConstant.GROUP_TYPE_FLOW;

	public static final int LENGTH_GROUP_NAME = 30;
}
