package com.baidu.beidou.api.external.cprogroup.error;

import java.io.Serializable;
import java.util.List;

import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.ErrorParam;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * ClassName: GroupConfigErrorCode
 * Function: 推广组设置相关errorcode
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
public enum GroupConfigErrorCode {
	
	SITE_NOT_FOUND(5001, "The website does not exsit"), //网站不存在
	TRADE_NOT_FOUND(5002, "The trade does not exsit"), //行业不存在
	REGION_NOT_FOUND(5003, "The region does not exsit"), //地域不存在
	SITE_FILTER_ERROR(5004, "Invalid website address"), //站点过滤验证错误
	SITEFILTER_MAX(5005, "The number of filtered websites exceeds the limit"), //站点过滤数量超过限制
	SITE_FILTER_DUP(5006, "The filtered websites have duplicate"), //站点过滤重复
	SITE_FILTER_TOO_LONG(5007, "The lenghth of the filtered website exceeds the limit"), //站点过滤过长
	KEYWORD_INVALID(5008, "Failure of audit for the keyword"), //关键词验证错误
	KEYWORD_TOO_MANY(5009, "The mumber of keywords exceeds the limit"), //关键词数量过多
	KEYWORD_CREATION(5010, "Failed to add keywords"), //关键词创建错误
	SITEPRICE_MAX(5011, "The number of website price exceeds the limit"), //站点价格数量超过限制
	SITEPRICE_ERROR(5012, "Failed to set the price per clicks at the site level"), //分网站点击价格设置错误
	SITEPRICE_RANGE(5013, "The website price exceeds the limit"), //站点价格超出范围
	SITEPRICE_DUP(5014, "The website prices have duplicate"), //站点价格重复
	SITEPRICE_AUDIT(5015, "Failure of audit for the website"), //站点审核错误
	IPFILTER_MAX(5016, "The number of the filtered IP exceeds the limit"), //ip过滤数量超过限制
	IPFILTER_ERROR(5017, "Invalid IP address"), //ip验证错误
	IPFILTER_DUP(5018, "The IP addresses have duplicate"), //ip重复
	REGION_DUP(5019, "The regions have duplicate"), //地域重复
	SITE_DUP(5020, "The website settings have duplicate"), //站点设置重复
	CATEGORY_DUP(5021, "The category settings have duplicate"), //站点分类设置重复
	KEYWORD_DUP(5022, "The keywords have duplicate"), //关键词重复
	KEYWORD_EMPTY(5023, "There is no keyword"), //没有关键词
	NO_BAIDU_SITE_PRIVELEGE(5024, "Have no authorization to add baidu site"), // 没有添加百度自有流量网站的权限
	NO_BAIDU_TRADE_PRIVELEGE(5025, "Have no authorization to add baidu trade"), // 没有添加百度自有流量行业的权限
	SITE_TARGETURL_MAX(5026, "The number of site's targeturls exceeds the limit"), // 分网站点击链接数量超过限制
	SITE_TARGETURL_ERROR(5027, "Failed to set the destination URL at the site level"), // 分网站点击链接错误
	SITE_TARGETURL_DUP(5028, "The website targeturl have duplicate"), // 分网站点击链接重复
	TRADEPRICE_ERROR(5029, "Failed to set the price per clicks at the trade level"), //分行业点击价格错误
	TRADEPRICE_RANGE(5030, "The trade price exceeds the limit"), // 行业价格超出范围
	TRADEPRICE_DUP(5031, "The trade prices have duplicate"), // 行业价格重复
	TARGETTYPE_TYPE_ERROR(5032, "Invalid group targettype"), // 定向方式错误
	TARGETTYPE_DATA_ERROR(5033, "Failed to set target data for the specific targettype"), // 定向方式和定向数据不匹配
	RT_ALIVEDAYS_ERROR(5034, "Invalid alivedays for RT"), // RT的有效期错误
	KT_ALIVEDAYS_ERROR(5035, "Invalid alivedays for KT"), // KT的有效期错误
	RT_RELATIONS_ERROR(5036, "Invalid relations for RT"), // RT的关联关系错误
	KT_WORDS_ERROR(5037, "Invalid words for KT"), // KT词错误
	RT_RELATIONS_MAX(5038, "The number of RT relations exceeds the limit"), // RT关联关系超过限制
	KT_WORDS_MAX(5039, "The number of KT words exceeds the limit"), // 关键词数量超过限制
	FC_DATA_ERROR(5040, "Failed to get fengchao data"), // 获取凤巢数据异常
	VT_PEOPLE_ERROR(5041, "Invalid vt relations for VT"), // VT人群错误
	VT_RELATED_PEOPLE_ERROR(5042, "Invalid related people for VT"), // VT关联人群错误
	VT_PEOPLE_MAX(5043, "The number related and unrelated people exceeds the limit"), // VT关联人群和排除人群总数超过限制
	VT_PEOPLE_CONTAINS_SAME_PEOPLE(5044, "The related people and unrelated people has the same id"), // 关联人群与排除人群中有相同ID
	SET_EXCLUDE_IP_FAILED(5045, "Failed to set exclude ip"), // 设置IP过滤失败
	SET_EXCLUDE_SITE_FAILED(5046, "Failed to set exclude site"), // 设置过滤站点失败
	TRADE_SITE_PRICE_EMPTY(5047, "The configs of trade and site price are all null or empty"), // 网站行业出价设置均为空
	TRADE_SITE_PRICE_INVALID(5048, "The configs of valid trade and site price are empty"), // 有效的网站行业出价设置均为空
	SET_TRADE_SITE_PRICE_FAILED(5049, "Failed to set trade and site price"), // 设置分行业网站出价失败
	SET_SITE_URL_FAILED(5050, "Failed to set site targeturl"), // 设置分点击URL失败
	
	SITE_SET_MAX(5051, "The number of website settings exceeds the limit"), // 一个推广组下网站数量超过限制
	SITE_TRADE_NOT_NULL_ERROR(5052, "The sites and trades in the targeted group can not be empty"), // 一个推广组下网站和行业不能全部为空
	
	SITE_TRADE_ALL_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP(5053, "None of the sites' support type match group type, check your submitted sites or trades"), // 一个推广组下网站和行业不能全部为空
	SITE_TRADE_SOME_FILTER_OUT_DUE_TO_TYPE_NOT_MATCH_GROUP(5054, "One or more sites' support type can not match group type, check your submitted sites or trades"), 
	
	SET_TARGETTYPE_ERROR(5055, "Failed to set target info"), 
	KT_TARGETTYPE_INTERNAL_ERROR(5056, "Invalid KT Target type"), 
	KT_TARGETTYPE_ERROR(5057, "Invalid KT Target type"), 
	KT_WORD_ALREADY_EXISTS(5058, "KT word already exists"), 
	KT_WORDS_DUP(5059, "Duplicate KT words"), 
	KT_WORDS_EMPTY(5060, "Empty KT words"), 
	KEYWORD_NULL_ERROR(5061, "The keyword is null"), // 关键词无效
	KEYWORD_OR_GROUPID_NULL_ERROR(5062, "The keyword or its group id is null"), // 关键词无效
	ADD_MAX_KEYWORDS_ERROR(5063, "The number of keywords to add exceeds the limit"), // 一次最多添加的关键词数量超出限制
	SET_MAX_KEYWORDS_ERROR(5064, "The number of keywords to set exceeds the limit"), // 一次最多修改的关键词数量超出限制
	DEL_MAX_KEYWORDS_ERROR(5065, "The number of keywords to delete exceeds the limit"), // 一次最多删除的关键词数量超出限制
	KT_GROUP_TYPE_ERROR(5066, "The type of targeted group is not keyword type"), // 目标推广组不是关键词受众行为定向
	KT_WORDS_PATTERN_TYPE_ERROR(5067, "Invalid pattern type for keywords"), // 关键词匹配模式错误
	KEYWORD_NOT_FOUND_ERROR(5068, "This keyword is not found in the group"), // 目标推广组不存在该关键词
	KEYWORD_PATTERN_TYPE_NO_NEED_TO_UPDATE_ERROR(5069, "This keyword pattern is already set to the specified value"), //关键词匹配模式不需要修改
	KT_WORDS_PATTERN_TYPE_NOT_SUPPORT_QT_ERROR(5070, "Invalid pattern type for keywords in the specified group target type"), // 关键词匹配模式不能在不含QT推广组中修改
	
	CAN_NOT_ADD_SITE_DUE_TO_EXIST_SITE_HAS_BEEN_INVALID(5071, "One or more existing configured sites have become invalid, please acquire group site configrutaion first and double check, then set and override group site configrutaion with valid sites"), 
	
	
	// add && delete && set 接口errorcode
	ADD_DEL_SET_MAX_GROUPS_ERROR(5100, "The number of targeted groups exceeds the limit"), // 一次最多添加或者删除的目标推广组数量超出限制
	
	/* invalid error code since beidou3.0
	CT_KEYWORD_NULL_ERROR(5101, "The ctkeyword is null"), // CT关键词无效
	CT_ADD_MAX_KEYWORDS_ERROR(5102, "The number of ctkeywords to add exceeds the limit"), // 一次最多添加的CT词数量超出限制
	CT_DEL_MAX_KEYWORDS_ERROR(5103, "The number of ctkeywords to delete exceeds the limit"), // 一次最多删除的CT词数量超出限制
	CT_GROUP_TYPE_ERROR(5104, "The type of targeted group is not ct-type"), // 目标推广组不是CT推广组
	CT_KEYWORD_NOT_FOUND_ERROR(5105, "This ct-keyword is not found in the targeted group"), // 目标推广组不存在该CT关键词
	CT_KEYWORD_ADD_ERROR(5106, "Failed to add ctkeywords"), //CT关键词创建错误
	CT_KEYWORD_DEL_ERROR(5107, "Failed to delete ctkeywords"), //CT关键词删除错误
	
	QT_KEYWORD_NULL_ERROR(5111, "The keyword or its group id is null"), // QT关键词无效
	QT_ADD_MAX_KEYWORDS_ERROR(5112, "The number of qtkeywords to add exceeds the limit"), // 一次最多添加的QT词数量超出限制
	QT_DEL_MAX_KEYWORDS_ERROR(5113, "The number of qtkeywords to delete exceeds the limit"), // 一次最多删除的QT词数量超出限制
	QT_GROUP_TYPE_ERROR(5114, "The type of targeted group is not qt-type"), // 目标推广组不是QT推广组
	QT_KEYWORD_NOT_FOUND_ERROR(5115, "This qt-keyword is not found in the targeted group"), // 目标推广组不存在该QT关键词
	QT_KEYWORD_ADD_ERROR(5116, "Failed to add qtkeywords"), //QT关键词创建错误
	QT_KEYWORD_DEL_ERROR(5117, "Failed to delete qtkeywords"), //QT关键词删除错误
	*/
	
	RT_RELATION_NULL_ERROR(5121, "The relation is null"), // RT关联关系无效
	RT_ADD_MAX_RELATIONS_ERROR(5122, "The number of relations to add exceeds the limit"), // 一次最多添加的RT关联关系数量超出限制
	RT_DEL_MAX_RELATIONS_ERROR(5123, "The number of relations to delete exceeds the limit"), // 一次最多删除的RT关联关系数量超出限制
	RT_GROUP_TYPE_ERROR(5124, "The type of targeted group is not rt-type"), // 目标推广组不是RT推广组
	RT_FC_PLAN_ID_ERROR(5125, "The relation to fc-plan is not found in the targeted group"), // 目标推广组不存在关联至推广计划的关联关系
	RT_FC_UNIT_ID_ERROR(5126, "The relation to fc-unit is not found in the targeted group"), // 目标推广组不存在关联至推广单元的关联关系
	
	VT_PEOPLE_NULL_ERROR(5131, "The people is null"), // VT关联关系无效
	VT_ADD_MAX_PEOPLES_ERROR(5132, "The number of peoples to add exceeds the limit"), // 一次最多添加的VT人群数量超出限制
	VT_DEL_MAX_PEOPLES_ERROR(5133, "The number of peoples to delete exceeds the limit"), // 一次最多删除的VT人群数量超出限制
	VT_GROUP_TYPE_ERROR(5134, "The type of targeted group is not vt-type"), // 目标推广组不是VT推广组
	VT_DEL_PEOPLE_ID_ERROR(5135, "The people to delete is not found in the targeted group"), // 目标推广组不存在待删除的VT人群
	
	SITE_NULL_ERROR(5141, "The site is null"), // 投放网络无效
	SITE_ADD_MAX_ERROR(5142, "The number of sites to add exceeds the limit"), // 一次最多添加的投放网站数量超出限制
	SITE_DEL_MAX_ERROR(5143, "The number of sites to delete exceeds the limit"), // 一次最多删除的投放网站数量超出限制
	SITE_NOT_OPTIONAL_ERROR(5144, "The targeted group is not optional"), // 目标推广组不是自选投放网络的
	SITE_DEL_ERROR(5145, "The site to delete is not found in the targeted group"), // 目标推广组不存在待删除的投放网站
	
	TRADE_NULL_ERROR(5151, "The trade is null"), // 投放网站行业无效
	TRADE_ADD_MAX_ERROR(5152, "The number of trades to add exceeds the limit"), // 一次最多添加的投放行业数量超出限制
	TRADE_DEL_MAX_ERROR(5153, "The number of trades to delete exceeds the limit"), // 一次最多删除的投放行业数量超出限制
	TRADE_NOT_OPTIONAL_ERROR(5154, "The targeted group is not optional"), // 目标推广组不是自选投放网络的
	TRADE_GROUP_TYPE_ERROR(5155, "The targeted group is a film-type group"), // 目标推广组是贴片推广组的（不支持选择投放行业）
	TRADE_DEL_ERROR(5156, "The trade to delete is not found in the targeted group"), // 目标推广组不存在待删除的投放行业
	TRADE_FIRST_SECOND_TRADE_ERROR(5157, "The first tradeid and the second tradeid can not be in the same group"), // 一二级行业不能同时存在于一个推广组中
	
	REGION_NULL_ERROR(5161, "The region is null"), // 投放地域无效
	REGION_ADD_MAX_ERROR(5162, "The number of regions to add exceeds the limit"), // 一次最多添加的投放地域数量超出限制
	REGION_DEL_MAX_ERROR(5163, "The number of regions to delete exceeds the limit"), // 一次最多删除的投放地域数量超出限制
	REGION_NOT_OPTIONAL_ERROR(5164, "The targeted group is not optional"), // 目标推广组不是自选投放地域的
	REGION_DEL_ERROR(5165, "The region to delete is not found in the targeted group"), // 目标推广组不存在待删除的投放地域
	REGION_FIRST_SECOND_REGION_ERROR(5166, "The first regionid and the second regionid can not be in the same group"), // 一二级投放地域不能同时存在于一个推广组中
	REGION_NOT_NULL_ERROR(5167, "The trades in the targeted group can not be empty"), // 一个推广组下网站和行业不能全部为空
	
	EXCLUDE_IP_NULL_ERROR(5171, "The excludeip is null"), // IP过滤无效
	EXCLUDE_IP_ADD_MAX_ERROR(5172, "The number of excludeips to add exceeds the limit"), // 一次最多添加的IP过滤数量超出限制
	EXCLUDE_IP_DEL_MAX_ERROR(5173, "The number of excludeips to delete exceeds the limit"), // 一次最多删除的IP过滤数量超出限制
	EXCLUDE_IP_DEL_ERROR(5174, "The excludeip to delete is not found in the targeted group"), // 目标推广组不存在待删除的IP过滤
	
	EXCLUDE_SITE_NULL_ERROR(5181, "The excludesite is null"), // 网站过滤无效
	EXCLUDE_SITE_ADD_MAX_ERROR(5182, "The number of excludesites to add exceeds the limit"), // 一次最多添加的网站过滤数量超出限制
	EXCLUDE_SITE_DEL_MAX_ERROR(5183, "The number of excludesites to delete exceeds the limit"), // 一次最多删除的网站过滤数量超出限制
	EXCLUDE_SITE_DEL_ERROR(5184, "The excludesite to delete is not found in the targeted group"), // 目标推广组不存在待删除的网站过滤
	
	TRADE_PRICE_ADD_MAX_ERROR(5191, "The number of tradePriceList to add exceeds the limit"), // 一次最多添加的分行业出价数量超出限制
	TRADE_PRICE_ILLGAL_ERROR(5192, "The tradePrice is illegal"), // 分行业出价参数非法
	TRADE_PRICE_DEL_MAX_ERROR(5193, "The number of tradePriceList to add exceeds the limit"), // 一次最多删除的分行业出价数量超出限制
	TRADE_PRICE_DEL_NOT_EXIT_ERROR(5194, "The tradeId need to delete not exist"), // 待删除的分行业出价的行业不存在
	TRADE_DUPLICATE(5195, "The tradeId have duplicate"), // 行业Id重复
	
	INTEREST_EMPTY_ERROR(5200, "Interest is empty"), // 设置兴趣为空
	INTEREST_DUP_ERROR(5201, "Interest duplicated"), // 兴趣点重复
	INTEREST_NULL_ERROR(5202, "The interest not exist"), // 找不到兴趣点或者兴趣组合
	SET_GROUP_INTEREST_ERROR(5203, "Set group interest failed"), // 设置受众兴趣失败
	ADD_MAX_INTEREST_ERROR(5204, "The number of interests exceeds the limit"), // 正向关联兴趣的最大值超过阈值
	ADD_MAX_EXCLUDE_INTEREST_ERROR(5205, "The number of exclude interests exceeds the limit"), // 反向关联兴趣的最大值超过阈值
	GROUP_INTEREST_AT_LEAST_ONE_ERROR(5206, "The number of interests to be set should be at least one"), // 正向关联兴趣不能为空
	GROUP_NOT_ENABLE_INTEREST(5207, "The group has not enabled interest"), // 设置兴趣为空
	INTEREST_SHOULD_NOT_IN_BOTH_SET(5208, "The interest is duplicated as shown in both related and unrelated interest set"), // 正向反向关联都存在同一个id
	INTEREST_CHILDREN_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR(5209, "The interest whose children is in the relate or unrelate interest set "), // 正向反向关联的子节点兴趣不能包含在其他集合中
	INTEREST_PARENT_CONTAINS_IN_RELATE_OR_UNRELATE_ERROR(5210, "The interest whose parent is in the relate or unrelate interest set "), // 正向反向关联的子节点兴趣不能包含在其他集合中
	IT_ADD_MAX_ERROR(5211, "The number of interests to add exceeds the limit"), // 一次最多添加的IT数量超出限制
	IT_DEL_MAX_ERROR(5212, "The number of interests to delete exceeds the limit"), // 一次最多删除的IT数量超出限制
	INTEREST_OR_GROUPID_NULL_ERROR(5213, "The interest or its group id is null"), // 推广组ID或者其下的关联/排除兴趣无效
	GROUP_IT_DISABLED(5214, "The group has not enabled interest"), // 推广组未启用兴趣
	INTEREST_CAN_NOT_DELETE_TO_EMPTY(5215, "The interest can not be deleted to empty"), // 关联兴趣不能删空
	GROUP_INTEREST_DISABLE_BUT_HAVE_RELATE_INTEREST(5216, "The number of interests to be set should be zero if it not enabled"), // 如果未启用兴趣，正向关联兴趣应为空
	
	SITE_PRICE_NULL_ERROR(5221, "The siteprice is null"), // 分网站出价无效
	SITE_PRICE_ADD_MAX_ERROR(5222, "The number of siteprices to add exceeds the limit"), // 一次最多添加的分网站出价数量超出限制
	SITE_PRICE_DEL_MAX_ERROR(5223, "The number of siteprices to delete exceeds the limit"), // 一次最多删除的分网站出价数量超出限制
	SITE_PRICE_DEL_ERROR(5224, "The siteprice to delete is not found in the targeted group"), // 目标推广组不存在待删除的分网站出价
	REGION_SHOULD_NOT_BE_EMPTY(5225, "The region is empty"), // 非地域投放，地域却为空
	
	GROUP_ID_NUM_TOO_MANY(5226, "The group id list exceeds the limit"), // 传入推广组id超过限制
	EXCLUDE_WORDS_PATTERN_TYPE_ERROR(5227, "Invalid pattern type for keyword"), // 关键词匹配模式错误
	EXCLUDE_WORDS_ERROR(5228, "Invalid keyword"), // 关键词错误
	EXCLUDE_WORD_PACK_NOT_EXIST(5229, "Exclude keyword pack not exist"), //排除关键词组合不存在
	EXCLUDE_WORD_PACK_RELATED(5230, "Exclude keyword pack already added to the group you specified"), //排除关键词组合已经和推广组关联
	EXCLUDE_KEYWORDS_EXCEED_LIMIT(5231, "The exclude keywords exceeds the limit"), // 推广组排除关键词数量超过限制
	EXCLUDE_WORD_PACK_COUNT_EXCEED(5232, "Exclude keyword packs exceed the limit of one group"), //排除关键词组合数量超过单个推广组上限
	EXCLUDE_WORD_PACK_TARGET_TYPE_INVALID(5233, "Exclude keyword pack target type must has QT"), //排除关键词组合必须包含QT
	EXCLUDE_WORD_NOT_EXIST(5234, "Exclude keyword not exist"), //排除关键词不存在
	EXCLUDE_WORD_AND_PACK_TOO_MANY(5235, "Exclude keyword params are too much"), //排除关键词和组合参数长度过长
	EXCLUDE_WORD_OR_PACK_DUP(5236, "Exclude keyword or keyword pack is duplicated"), //排除设置的关键词或者组合重复
	EXCLUDE_WORD_PACK_HAS_BEEN_OPTIMIZED(5237, "Exclude keyword pack has been optimized"), //已优化的关键词组合无法关联
	
	EXCLUDE_PEOPLEIDS_EXCEED_LIMIT(5238, "The exclude people ids exceeds the limit"), // 推广组排除人群数量超过限制
	EXCLUDE_PEOPLE_TOO_MANY(5239, "Exclude people params are too much"), //排除人群组合参数长度过长
	EXCLUDE_PEOPLE_DUP(5240, "Exclude people is duplicated"), //排除设置的人群重复
	EXCLUDE_PEOPLE_NOT_EXIST(5241, "Exclude people not exist"), //排除设置的人群不存在
	EXCLUDE_PEOPLE_RELATED(5242, "Exclude people already added to the group you specified"), //排除设置的人群已关联
	
	GROUP_TARGETTYPE_NOT_SUPPORT_PACK_CONFIG(5243, "Group cannot be config to pack due to target type wrong"), //不是高级组合的推广组无法设置受众组合
	GROUP_PACK_NOT_EXIST_OR_TYPE_WRONG_OR_OPTIMIZED(5244, "Pack not exist or has been optimized"), //组合不存在或者已优化
	GROUP_PACK_NOT_RELATED(5245, "Pack does not related to the group"), //组合没有和推广组关联
	GROUP_PACK_HAS_EXCLUDED(5246, "Pack has been excluded"), //组合已经被排除
	GROUP_PACK_CONFIG_FAILED(5247, "Failed to config group pack"), //设置推广组关联受众组合发生错误
	GROUP_PACK_TYPE_WRONG(5248, "Pack type wrong"), //组合类型错误
	GROUP_PACK_DUPLICATE(5249, "Pack duplicated"), //组合提交重复
	GROUP_PACK_EXCEED_LIMIT(5250, "Group pack exceeds the limit"), //推广组关联组合超过数量
	PACKS_EXCEED_LIMIT(5251, "The packs exceeds the limit"), // 推广组关联受众组合超过限制
	GROUP_KEYWORD_PACK_EXCEED_LIMIT(5252, "Group keyword pack exceeds the limit"), //推广组关联关键词组合超过数量
	GROUP_ADVANCED_PACK_EXCEED_LIMIT(5253, "Group advanced pack exceeds the limit"), //推广组关联高级组合超过数量
	GROUP_PACK_TOO_MANY(5254, "Packs are too much"), //组合参数过长
	
	PRICES_TOO_MANY(5255, "Prices are too much"), //出价参数过长
	PRICE_NOT_VALID(5256, "Price is not in valid range"), //出价值不在合法范围内
	GROUP_IS_NOT_PACK_TARGET(5257, "Group is not configured to advanced pack"), //推广组不是高级组合投放类型
	PRICE_EXCEED_PLAN_BUDGET(5258, "Price exceeds plan budget"), //出价高于推广计划出价
	INTEREST_NOT_FOUND_IN_GROUP(5259, "Interest not found in group"), //推广组未配置该兴趣
	PACK_NOT_FOUND_IN_GROUP(5260, "Pack not related to group"), //推广组未配置该受众组合
	
	TARGETTYPE_CANNOT_CHANGE_IF_SET_TO_PACK(5261, "Group type can not be updated due to group has been configured to advanced pack targettype"), //推广组定向方式无法从高级组合改为基本设置
	INTEREST_ENABLED_CANNOT_CHANGE_IF_SET_TO_PACK(5262, "Interest can not be turned to enable or disabled due to group has been configured to advanced pack targettype"), //推广组兴趣定向在启用高级组合时修改
	
	SITELIST_INVALID_DUE_TO_WIRELESS_CAMPAIGN(5263, "Site list can not be set or updated if group's campaign type is wireless only"), //因推广组所属计划为仅移动，故网站无法设置或修改
	
	EXCLUDE_APP_ADD_MAX_ERROR(5264, "The number of excludeapps to add exceeds the limit"), // 一次最多添加的排除应用数量超出限制
	EXCLUDE_APP_NULL_ERROR(5265, "The excludeapps is null"), // 排除应用为空
	EXCLUDE_APP_NOT_FOUND(5266, "The excludeapp is not found"), // 排除应用不存在
	EXCLUDE_APP_NOT_FOUND_IN_GROUP(5267, "The excludeapp is not found in group"), // 排除应用不存在
	EXCLUDE_APP_GROUP_DUP(5268, "The excludeapp group is duplicate"), //排除应用推广组重复
	WIRELESS_TRADE_UNAUTH(5269, "The wireless trade can not be set because privilege is not granted"), //无线行业无法被非白名单用户设置
	
	
	RT_DEPREATED_ERROR(5270, "RT targeting has been deprecated and migrated to VT targeting, please configure group targeting through VT targeting"), //RT定向方式已经废弃并迁移到VT定向方式
	GROUP_EXCLUDE_APP_MAX_ERROR(5271, "The number of excludeapps for group  exceeds the limit"),//一个推广组最多排除的应用个数超出限制
	
	SMARTIDEA_GROUP_CANNOT_SET_TO_PT_TARGETTYPE(5273, "Invalid group targettype for smart idea group"), // 智能创意推广组定向方式错误
	DEL_ATTACH_NUM_TOO_MANY(5274, "The delete attachinfo list exceeds the limit"),//一次 删除的附加创意超过限制1000
	
	ADD_ATTACH_NUM_TOO_MANY(5275, "The add attachinfo list exceeds the limit"),
	ATTACH_GROUP_DUP(5276,"the attachinfo group is duplicate"),//添加附加创意时推广组重复
	ATTACH_TYPE_IN_ONE_GROUP_DUP(5277,"dup attach type in one group"),//一个推广组对应的同种类型的附加信息只有一个
	ATTACH_TYPE_INVALID(5278,"attach type invalid"),//附加信息类型不合法
	ATTACH_PHONE_INVALID(5279,"attach phone invalid,need 5-12 numbers"),//附加信息之电话内容不合法
	ATTACH_MESSAGE_INVALID(5280,"attach message invalid"),//附加信息之消息内容不合法
	ATTACH_MESSAGE_LENGTH_INVALID(5281,"attach message length too big"),//附加信息之消息长度不合法
	ATTACH_CONSULT_INVALID(5282,"the userid is not bridger user"),//未开通商桥用户不能提交咨询信息
	ATTACH_CALL_INVALID(5283,"the call is not online"),//附加信息 寻呼还未上线
	ATTACH_STATUS_INVALID(5284,"attach status invalid"),//附加信息状态只允许有效，暂停
	ATTACH_MESSAGE_PHONE_INVALID(5285,"attach message phone invalid,need 5-20 numbers"),//短信号码不合法
	
	SIMILAR_PEOPLE_GET_MAX_ERROR(5290, "The get similar people list exceeds the limit"),// 一次获取的相似人群设置超过限制100
	SIMILAR_PEOPLE_UPDATE_MAX_ERROR(5291, "The update similar people list exceeds the limit"),// 一次更新的相似人群设置超过限制100
	SIMILAR_PEOPLE_PARAM_ERROR(5292, "The param of similar flag is illegal"),// 相似人群设置参数有误，开启(1)或者关闭(0)
    
    ATTACH_SUBURL_PARAM_INVALID(5300, "attach suburl parameter must start with ? or # or &"), // 附加子链统计参数非空时必须为?#&三者之一开头
    ATTACH_SUBURL_PARAM_EXCEED_LIMIT(5301, "the length of attach suburl parameter exceeds the limit"), // 附加子链统计参数非空时长度不可超过256
    ATTACH_SUBURL_PARAM_INVALID_CHAR(5302, "attach suburl parameter cannot contain the special character"), // 附加子链统计参数非空时不可包含特殊字符\t，\n，\r，\0等
    ATTACH_SUBURL_EXCEED_LIMIT(5303, "the length of attach suburl info is not in valid range"), // 附加子链信息非空时长度需在2~4之间
    ATTACH_SUBURL_TITLE_EMPTY(5304, "attach suburl title cannot be empty"), // 附加子链标题不可为空
    ATTACH_SUBURL_TITLE_EXCEED_LIMIT(5305, "the length of attach suburl title exceeds the limit"), // 附加子链标题不可超过24个字符或者12个中文字符
    ATTACH_SUBURL_TITLE_INVALID(5306, "attach suburl title cannot contain the special character"), // 附加子链标题不可包含特殊字符\t，\n，\r，\0等
    ATTACH_SUBURL_LINK_EMPTY(5307, "attach suburl link cannot be empty"), // 附加子链链接不可为空
    ATTACH_SUBURL_LINK_EXCEED_LIMIT(5308, "the length of attach suburl link exceeds the limit"), // 附加子链链接不可超过512
    ATTACH_SUBURL_LINK_INVALID(5309, "attach suburl link must start with http://"), // 附加子链链接必须以http://开头
    ATTACH_SUBURL_LINK_BIND_SITE(5310, "attach suburl link must bind with the netsite registered by user"), // 附加子链链接必须绑定用户主域
    ATTACH_SUBURL_LINK_INVALID_CHAR(5311, "attach suburl link cannot contain the special character")// 附加子链链接不可包含特殊字符\t，\n，\r，\0等
    ;
    
	private int value  =0;
	private String message = null;
	
	private GroupConfigErrorCode(int value, String message){
		this.value =value; 
		this.message = message;
	}
	
	public int getValue(){
		return value;
	}
	
	public String getMessage(){
		return message;
	}

    /**
     * 返回Error响应
     * 
     * @param response 响应对象
     * @param param 参数类型
     * @param pos 位置
     * @param content 错误信息
     * @return 包含错误信息的响应对象
     */
    public <T extends Serializable> BaseResponse<T> getErrorResponse(BaseResponse<T> response, String param,
            Integer pos, String content) {
        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
        if (pos != null) {
            apiPosition.addParam(param, pos.intValue());
        } else {
            apiPosition.addParam(param);
        }
        response = DRAPIMountAPIBeanUtils.addApiError(response, value, message, apiPosition.getPosition(), content);
        return response;
    }
    
    /**
     * 返回Error响应
     * 
     * @param result 响应对象
     * @param groupItem 推广组项
     * @param pos 位置
     * @param content 错误信息
     * @return 包含错误信息的响应对象
     */
//    public ApiResult<Object> getErrorResponse(ApiResult<Object> result, String groupItem, Integer pos, String content) {
//        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
//        if (pos != null) {
//            apiPosition.addParam(groupItem, pos.intValue());
//        } else {
//            apiPosition.addParam(groupItem);
//        }
//        result = ApiResultBeanUtils.addApiError(result, value, message, apiPosition.getPosition(), content);
//        return result;
//    }
    
    public ApiResult<Object> getErrorResponse(ApiResult<Object> result, List<ErrorParam> params, String content) {
        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
        for (ErrorParam param : params) {
            if (param.getPosition() != null) {
                apiPosition.addParam(param.getParamName(), param.getPosition().intValue());
            } else {
                apiPosition.addParam(param.getParamName());
            }
        }

        result = ApiResultBeanUtils.addApiError(result, value, message, apiPosition.getPosition(), content);
        return result;
    }
}
