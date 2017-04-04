package com.baidu.beidou.api.external.cprogroup.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.vo.SitePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.SiteUrlItemType;
import com.baidu.beidou.api.external.cprogroup.vo.TradePriceType;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupExcludeIpItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupExcludeSiteItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupSitePriceItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupTradeItem;
import com.baidu.beidou.api.external.cprogroup.vo.locale.GroupTradePriceItem;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.ListArrayUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.internal.util.converter.YuanToCentConverter;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.GroupSitePrice;
import com.baidu.beidou.cprogroup.bo.GroupTradePrice;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.facade.GroupSiteConfigHelper;
import com.baidu.beidou.cprogroup.service.GroupSiteConfigMgr;
import com.baidu.beidou.cprogroup.vo.GroupTradeSitePriceOptVo;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.vo.TradeInfo;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.validate.UnitAkaAudit;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.util.LogUtils;
import com.baidu.beidou.util.StringUtils;
import com.baidu.beidou.util.UrlParser;

/**
 * ClassName: GroupConfigValidator Function: 推广组设置相关验证
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
public class GroupConfigValidator {

	private static final Log LOG = LogFactory
			.getLog(GroupConfigValidator.class);

	private static final Pattern IPPATTERN = Pattern
			.compile(
					"^(([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\."
							+ "((0)|([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\."
							+ "((0)|([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\."
							+ "(([\\*])|(0)|([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))$",
					Pattern.CASE_INSENSITIVE);

	private static final Pattern SITEPATTERN = Pattern.compile(
			"^[a-zA-Z0-9\\-\\._]+$", Pattern.CASE_INSENSITIVE);

	private CproPlanMgr cproPlanMgr;
	
	private GroupSiteConfigMgr groupSiteConfigMgr;
	
	private CproGroupMgr cproGroupMgr;

	private static boolean isDomain(String site) {
		if (site == null) {
			return false;
		}
		Matcher matcher = SITEPATTERN.matcher(site);
		return matcher.matches();
	}

	private static boolean isIp(String ip) {
		if (ip == null) {
			return false;
		}
		Matcher matcher = IPPATTERN.matcher(ip);
		return matcher.matches();
	}

	public ApiResult<Object> validateIpFilter(final int groupid,
			final List<String> ipList, Set<String> reList,
			ApiResult<Object> result) {
		if (ipList == null) {
			return null;
		}
		if (ipList.size() == 1 && ipList.get(0) != null
				&& ipList.get(0).equals(GroupConstant.CLEAR_FLAG_STR)) {
			return result;
		}

		if (ipList.size() > CproGroupConstant.IPFILTER_ALL_MAX_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(
					PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_IP_REQ);
			apiPosition.addParam(GroupConstant.EXCLUDE_IP_TYPE);

			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.IPFILTER_MAX.getValue(),
					GroupConfigErrorCode.IPFILTER_MAX.getMessage(), apiPosition
							.getPosition(), null);
			return result;
		}
		// 对每一条记录进行验证
		Map<String, Boolean> listCompare = new HashMap<String, Boolean>();

		for (int i = 0; i < ipList.size(); i++) {
			String ip = ipList.get(i);
			ApiErrorPosition apiPosition = new ApiErrorPosition(
					PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_IP_REQ);
			apiPosition.addParam(GroupConstant.EXCLUDE_IP_TYPE, i);

			if (!isIp(ip)) {
				// 判断是否为Ip
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.IPFILTER_ERROR.getValue(),
						GroupConfigErrorCode.IPFILTER_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			} else if (listCompare.get(ip) != null) { // 只进行字符串匹配的去重
				// 去重
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.IPFILTER_DUP.getValue(),
						GroupConfigErrorCode.IPFILTER_DUP.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			} else {
				// 待保存的过滤IP
				reList.add(ip);
				listCompare.put(ip, true);
			}
		}

		return result;

	}
	
	public boolean validateAddExcludeIp(List<GroupExcludeIpItem> addExcludeIpList, 
			List<String> selectedExcludeIps, List<String> addedExcludeIps, ApiResult<Object> result) {
		if (CollectionUtils.isEmpty(addExcludeIpList)) {
			return false;
		}
		
		// 检验是否超过单个推广组过滤IP设置的最大值
		if (addExcludeIpList.size() + selectedExcludeIps.size() 
				> CproGroupConstant.IPFILTER_ALL_MAX_NUM) {
			for (GroupExcludeIpItem item : addExcludeIpList) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_IPS, item.getIndex());
				apiPosition.addParam(GroupConstant.EXCLUDE_IP);
				
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.IPFILTER_MAX.getValue(),
						GroupConfigErrorCode.IPFILTER_MAX.getMessage(), 
						apiPosition.getPosition(), null);
			}
			return false;
		}
		
		// 首先将已选择的加入过滤IP合并的集合
		Set<String> mergedSet = new HashSet<String>();
		mergedSet.addAll(selectedExcludeIps);
		
		// 对每一条记录进行验证
		for (GroupExcludeIpItem item : addExcludeIpList) {
			String ip = item.getExcludeIp();
			
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_IPS, item.getIndex());
			apiPosition.addParam(GroupConstant.EXCLUDE_IP);

			if (!isIp(ip)) {
				// 判断是否为Ip
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.IPFILTER_ERROR.getValue(),
						GroupConfigErrorCode.IPFILTER_ERROR.getMessage(),
						apiPosition.getPosition(), null);
			} else if (mergedSet.contains(ip)) {
				// 只进行字符串匹配的去重
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.IPFILTER_DUP.getValue(),
						GroupConfigErrorCode.IPFILTER_DUP.getMessage(),
						apiPosition.getPosition(), null);
			} else {
				// 待保存的过滤IP
				mergedSet.add(ip);
				addedExcludeIps.add(ip);
			}
		}
		return true;
	}
	
	public boolean validateDeleteExcludeIp(List<GroupExcludeIpItem> deleteExcludeIpList, 
			List<String> selectedExcludeIps, List<String> deletedExcludeIps, ApiResult<Object> result) {
		if (CollectionUtils.isEmpty(deleteExcludeIpList) 
				|| CollectionUtils.isEmpty(selectedExcludeIps)) {
			return false;
		}
		
		// 对每一条记录进行验证
		for (GroupExcludeIpItem item : deleteExcludeIpList) {
			String ip = item.getExcludeIp();
			
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_IPS, item.getIndex());
			apiPosition.addParam(GroupConstant.EXCLUDE_IP);

			if (!isIp(ip)) {
				// 判断是否为Ip
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.IPFILTER_ERROR.getValue(),
						GroupConfigErrorCode.IPFILTER_ERROR.getMessage(),
						apiPosition.getPosition(), null);
			} else if (!selectedExcludeIps.contains(ip)) {
				// 如果在数据库中并不存在该过滤IP，则删掉
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.EXCLUDE_IP_DEL_ERROR.getValue(),
						GroupConfigErrorCode.EXCLUDE_IP_DEL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
			} else {
				// 待保存的过滤IP
				deletedExcludeIps.add(ip);
			}
		}
		return true;
	}

	/*
	 * private static final int PRICE_MAX =
	 * CproGroupConstant.GROUP_CONFIG_PRICE_MAX; private static final int
	 * PRICE_MIN = CproGroupConstant.GROUP_CONFIG_PRICE_MIN; private static
	 * final int TARGETURL_MAX = CproGroupConstant.GROUP_CONFIG_TARGETURL_MAX;
	 * private static final int FILTER_SITE_MAX =
	 * CproGroupConstant.GROUP_CONFIG_FILTER_SITE_MAX;
	 */
	public ApiResult<Object> validateSiteFilter(int groupId,
			List<String> siteList, Set<String> reList, ApiResult<Object> result) {
		if (siteList == null) {
			return null;
		}

		if (siteList.size() > CproGroupConstant.SITEFILTER_ALL_MAX_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(
					PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_SITE_REQ);

			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.SITEFILTER_MAX.getValue(),
					GroupConfigErrorCode.SITEFILTER_MAX.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		Map<String, Boolean> listCompare = new HashMap<String, Boolean>();
		// 对每一条记录进行验证
		for (int i = 0; i < siteList.size(); i++) {
			String site = siteList.get(i).toLowerCase();

			ApiErrorPosition apiPosition = new ApiErrorPosition(
					PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_SITE_REQ);
			apiPosition.addParam(GroupConstant.EXCLUDE_SITE_TYPE, i);

			if (!isDomain(site)) {
				// 判断是否为网站
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_ERROR.getValue(),
						GroupConfigErrorCode.SITE_FILTER_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			} else if (!UrlParser.isDomainOrSecondDomain(site)) {
				// 判断域名是否符合规范
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_ERROR.getValue(),
						GroupConfigErrorCode.SITE_FILTER_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			} else if (listCompare.get(site) != null) { // 只进行字符串匹配的去重,区分大小写
				// 去重
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_DUP.getValue(),
						GroupConfigErrorCode.SITE_FILTER_DUP.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			} else if (StringUtils.byteLength(site) > CproGroupConstant.GROUP_CONFIG_FILTER_SITE_MAX) {
				// 域名的最大长度500
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_TOO_LONG.getValue(),
						GroupConfigErrorCode.SITE_FILTER_TOO_LONG.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			} else {
				// 待保存的过滤网站
				reList.add(site);
				listCompare.put(site, true);
			}
		}
		return result;

	}
	
	public boolean validateAddExcludeSite(List<GroupExcludeSiteItem> addExcludeSiteList,
			List<String> selectedExcludeSites, List<String> addedExcludeSites, ApiResult<Object> result) {
		if (CollectionUtils.isEmpty(addExcludeSiteList)) {
			return false;
		}

		// 检验是否超过单个推广组过滤IP设置的最大值
		if (addExcludeSiteList.size() + selectedExcludeSites.size() > CproGroupConstant.SITEFILTER_ALL_MAX_NUM) {
			for (GroupExcludeSiteItem item : addExcludeSiteList) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.EXCLUDE_SITES, item.getIndex());
				apiPosition.addParam(GroupConstant.EXCLUDE_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEFILTER_MAX.getValue(),
						GroupConfigErrorCode.SITEFILTER_MAX.getMessage(),
						apiPosition.getPosition(), null);
			}
			return false;
		}

		// 首先将已选择的加入过滤IP合并的集合
		Set<String> mergedSet = new HashSet<String>();
		mergedSet.addAll(selectedExcludeSites);

		// 对每一条记录进行验证
		for (GroupExcludeSiteItem item : addExcludeSiteList) {
			String site = item.getExcludeSite();

			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_SITES, item.getIndex());
			apiPosition.addParam(GroupConstant.EXCLUDE_SITE);

			if (!isDomain(site)) {
				// 判断是否为网站
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_ERROR.getValue(),
						GroupConfigErrorCode.SITE_FILTER_ERROR.getMessage(),
						apiPosition.getPosition(), null);
			} else if (!UrlParser.isDomainOrSecondDomain(site)) {
				// 判断域名是否符合规范
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_ERROR.getValue(),
						GroupConfigErrorCode.SITE_FILTER_ERROR.getMessage(),
						apiPosition.getPosition(), null);
			} else if (mergedSet.contains(site)) {
				// 只进行字符串匹配的去重,区分大小写
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_DUP.getValue(),
						GroupConfigErrorCode.SITE_FILTER_DUP.getMessage(),
						apiPosition.getPosition(), null);
			} else if (StringUtils.byteLength(site) > CproGroupConstant.GROUP_CONFIG_FILTER_SITE_MAX) {
				// 域名的最大长度500
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_TOO_LONG.getValue(),
						GroupConfigErrorCode.SITE_FILTER_TOO_LONG.getMessage(),
						apiPosition.getPosition(), null);
			} else {
				// 待保存的过滤网站
				mergedSet.add(site);
				addedExcludeSites.add(site);
			}
		}
		
		return true;
	}
	
	public boolean validateDeleteExcludeSite(List<GroupExcludeSiteItem> deleteExcludeSiteList,
			List<String> selectedExcludeSites, List<String> deletedExcludeSites, ApiResult<Object> result) {
		if (CollectionUtils.isEmpty(deleteExcludeSiteList)
				|| CollectionUtils.isEmpty(selectedExcludeSites)) {
			return false;
		}

		// 对每一条记录进行验证
		for (GroupExcludeSiteItem item : deleteExcludeSiteList) {
			String site = item.getExcludeSite();

			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.EXCLUDE_SITES, item.getIndex());
			apiPosition.addParam(GroupConstant.EXCLUDE_SITE);

			if (!isDomain(site)) {
				// 判断是否为网站
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_ERROR.getValue(),
						GroupConfigErrorCode.SITE_FILTER_ERROR.getMessage(),
						apiPosition.getPosition(), null);
			} else if (!UrlParser.isDomainOrSecondDomain(site)) {
				// 判断域名是否符合规范
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_ERROR.getValue(),
						GroupConfigErrorCode.SITE_FILTER_ERROR.getMessage(),
						apiPosition.getPosition(), null);
			} else if (StringUtils.byteLength(site) > CproGroupConstant.GROUP_CONFIG_FILTER_SITE_MAX) {
				// 域名的最大长度500
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_FILTER_TOO_LONG.getValue(),
						GroupConfigErrorCode.SITE_FILTER_TOO_LONG.getMessage(),
						apiPosition.getPosition(), null);
			} else if (!selectedExcludeSites.contains(site)) {
				// 如果在数据库中并不存在该过滤网站，则删掉
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.EXCLUDE_SITE_DEL_ERROR.getValue(),
						GroupConfigErrorCode.EXCLUDE_SITE_DEL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
			} else {
				// 待保存的过滤网站
				deletedExcludeSites.add(site);
			}
		}
		
		return true;
	}

	/**
	 * 验证TradePrice ：最大个数（输入个数，总个数），输入格式，同时为空,价格范围，去重，有效性 <br>
	 * 并设置推广组id
	 * 
	 * @author zengyunfeng
	 * @param group
	 *            :推广组id
	 * @param support
	 *            :保存错误信息,返回后包含验证没有通过的网站和错误提示信息的key
	 * @param curGroupFilter：
	 *            现有的过滤配置
	 * @param siteList :
	 *            待增加的网站列表
	 * @return 返回错误码
	 */
	public ApiResult<Object> validateTradePrice(final CproGroup group,
			final List<TradePriceType> inputTradePriceList,
			List<GroupTradePrice> toBeModifiedTradePriceList,
			ApiResult<Object> result) {

		if (group == null || inputTradePriceList == null
				|| toBeModifiedTradePriceList == null) {
			return null;
		}
		
		if (inputTradePriceList.size() == 1
				&& inputTradePriceList.get(0) != null
				&& (Math.abs(-1 - inputTradePriceList.get(0).getPrice()) <= 0.001)) {
			return result;
		}

		YuanToCentConverter conv = new YuanToCentConverter();
		CproPlan plan = cproPlanMgr.findCproPlanById(group.getPlanId());
		Map<Integer, Boolean> listCompare = new HashMap<Integer, Boolean>();

		for (int i = 0; i < inputTradePriceList.size(); i++) {

			TradePriceType tp = inputTradePriceList.get(i);

			// 看TradePrice是否为空，tradeid是否<=0
			if (tp == null || tp.getTradeId() <= 0) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.TRADE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_TRADE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.TRADEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.TRADEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 验证出价
			if (tp.getPrice() <= 0) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.TRADE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_PRICE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.TRADEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.TRADEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			int newPrice = conv.convertFrom(tp.getPrice());// 分

			if (!CproGroupConstant.isValidPriceValueRange(newPrice)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.TRADE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_PRICE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.TRADEPRICE_RANGE.getValue(),
						GroupConfigErrorCode.TRADEPRICE_RANGE.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}
			if (newPrice > plan.getBudget() * 100) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.TRADE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_PRICE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.TRADEPRICE_RANGE.getValue(),
						GroupConfigErrorCode.TRADEPRICE_RANGE.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 验证行业重复
			if (listCompare.get(tp.getTradeId()) != null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.TRADE_PRICE_LIST, i);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.TRADEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.TRADEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 验证行业有效性
			TradeInfo tradeInfo = UnionSiteCache.tradeInfoCache
					.getTradeInfoById(tp.getTradeId());
			if (tradeInfo == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.TRADE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_TRADE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(),
						GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			boolean isAllSite = group.getGroupInfo().getIsAllSite() == CproGroupConstant.GROUP_ALLSITE;
			if (!isAllSite) {
				// 不是全网投放，但没有设置分行业
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.TRADE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_TRADE);

				String groupTradeSiteStr = group.getGroupInfo()
						.getSiteTradeListStr();
				if (StringUtils.isEmpty(groupTradeSiteStr)) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(),
							GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(),
							apiPosition.getPosition(), null);
					return result;
				}

				String[] idsStrArr = groupTradeSiteStr.split("\\"
						+ CproGroupConstant.FIELD_SEPERATOR);
				List<Integer> idList = new ArrayList<Integer>();
				for (String idStr : idsStrArr) {
					try {
						idList.add(Integer.valueOf(idStr));
					} catch (NumberFormatException e) {
						return result;
					}
				}

				// 要出价的行业不在推广组设置的行业列表中
				if (!idList.contains(tradeInfo.getTradeid())
						&& !idList.contains(tradeInfo.getParentid())) {
					result = ApiResultBeanUtils.addApiError(result,
							GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(),
							GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(),
							apiPosition.getPosition(), null);
					return result;
				}
			}

			GroupTradePrice gtp = new GroupTradePrice();
			gtp.setGroupid(group.getGroupId());
			gtp.setPlanid(plan.getPlanId());
			gtp.setPrice(newPrice);
			gtp.setUserid(plan.getUserId());
			gtp.setTradeid(tp.getTradeId());

			toBeModifiedTradePriceList.add(gtp);
			listCompare.put(tp.getTradeId(), true);
		}

		return result;
	}

	/**
	 * 验证SitePrice ：最大个数（输入个数，总个数），输入格式，同时为空,价格范围，去重，有效性 <br>
	 * 并设置推广组id
	 * 
	 * @author zengyunfeng
	 * @param group
	 *            :推广组id
	 * @param support
	 *            :保存错误信息,返回后包含验证没有通过的网站和错误提示信息的key
	 * @param curGroupFilter：
	 *            现有的过滤配置
	 * @param siteList :
	 *            待增加的网站列表
	 * @return 返回错误码
	 */
	public ApiResult<Object> validateSitePrice(final CproGroup group,
			final List<SitePriceType> inputSitePriceList,
			List<GroupSitePrice> toBeModifiedList, ApiResult<Object> result) {

		if (group == null || inputSitePriceList == null
				|| toBeModifiedList == null) {
			return null;
		}
		
		if (inputSitePriceList.size() == 1
				&& inputSitePriceList.get(0) != null
				&& (Math.abs(-1 - inputSitePriceList.get(0).getPrice()) <= 0.001)) {
			return result;
		}

		// 判断是否超出单个推广组下分站点价格的最大数量
		if (inputSitePriceList.size() > CproGroupConstant.SITEPRICE_ALL_MAX_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(
					PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
			apiPosition.addParam(GroupConstant.SITE_PRICE_LIST);

			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.SITEPRICE_MAX.getValue(),
					GroupConfigErrorCode.SITEPRICE_MAX.getMessage(),
					PositionConstant.PARAM, null);
			return result;
		}

		YuanToCentConverter conv = new YuanToCentConverter();
		CproPlan plan = cproPlanMgr.findCproPlanById(group.getPlanId());
		Map<String, Boolean> listCompare = new HashMap<String, Boolean>();

		for (int i = 0; i < inputSitePriceList.size(); i++) {

			SitePriceType sp = inputSitePriceList.get(i);

			// 判断SitePrice参数是否为空
			if (sp == null || StringUtils.isEmpty(sp.getSite())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.SITE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 判断价格
			if (sp.getPrice() <= 0) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.SITE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_PRICE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			int newPrice = conv.convertFrom(sp.getPrice());// 分
			if (!CproGroupConstant.isValidPriceValueRange(newPrice)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.SITE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_PRICE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_RANGE.getValue(),
						GroupConfigErrorCode.SITEPRICE_RANGE.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}
			if (newPrice > plan.getBudget() * 100) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.SITE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_PRICE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 域名去重
			if (listCompare.get(sp.getSite()) != null) { // 字符串匹配，区分大小写
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.SITE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_DUP.getValue(),
						GroupConfigErrorCode.SITEPRICE_DUP.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 判断分网站是否在beidou的站点全库中
			Map<String, Integer> siteIdMap = UnionSiteCache.siteInfoCache.getSiteIdMap();
			Integer siteid = siteIdMap.get(sp.getSite().toLowerCase());
			if (siteid == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.SITE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_NOT_FOUND.getValue(),
						GroupConfigErrorCode.SITE_NOT_FOUND.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			GroupSiteConfigHelper helper = new GroupSiteConfigHelper();

			// 判断分网站是否属于推广组的自选行业和自选网站内
			boolean isValid = helper.isValid(siteid, group.getGroupInfo().getIsAllSite(), 
					group.getGroupInfo().getSiteTradeListStr(), 
					group.getGroupInfo().getSiteListStr());
			if (!isValid) {
				// 域名无效
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_SITE_PRICE_REQ);
				apiPosition.addParam(GroupConstant.SITE_PRICE_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			GroupSitePrice gsp = new GroupSitePrice();
			gsp.setGroupid(group.getGroupId());
			gsp.setPrice(newPrice);
			gsp.setSiteid(siteid);
			gsp.setSiteurl(sp.getSite());
			gsp.setPlanid(plan.getPlanId());
			gsp.setUserid(plan.getUserId());

			toBeModifiedList.add(gsp);
			listCompare.put(sp.getSite(), true);
		}

		return result;
	}
	
	public boolean validateAddSitePrice(CproGroup group, 
			List<GroupSitePriceItem> groupSitePriceList, List<GroupSitePrice> toBeModifiedList, 	
			ApiResult<Object> result, List<OptContent> optContents) {

		if (group == null || CollectionUtils.isEmpty(groupSitePriceList)) {
			return false;
		}
		
		// 获取已有的分网站出价
		List<GroupSitePrice> selectedSitePriceList = groupSiteConfigMgr.findSitePriceByGroupId(group.getGroupId());

		// 判断是否超出单个推广组下分站点价格的最大数量
		if (groupSitePriceList.size() + selectedSitePriceList.size() > CproGroupConstant.SITEPRICE_ALL_MAX_NUM) {
			for (GroupSitePriceItem item : groupSitePriceList) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITE_PRICES, item.getIndex());
	
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_MAX.getValue(),
						GroupConfigErrorCode.SITEPRICE_MAX.getMessage(),
						PositionConstant.PARAM, null);
			}
			return false;
		}
		
		Map<Integer, GroupSitePrice> selectedSitePriceMap = new HashMap<Integer, GroupSitePrice>();
		for (GroupSitePrice sitePrice : selectedSitePriceList) {
			selectedSitePriceMap.put(sitePrice.getSiteid(), sitePrice);
		}

		YuanToCentConverter conv = new YuanToCentConverter();
		CproPlan plan = cproPlanMgr.findCproPlanById(group.getPlanId());
		Map<String, Boolean> listCompare = new HashMap<String, Boolean>();
		
		final OpTypeVo opTypeAdd = OptHistoryConstant.OPTYPE_GROUP_SITE_PRICE_BATCH_ADD;
		final OpTypeVo opTypeEdit = OptHistoryConstant.OPTYPE_GROUP_SITE_PRICE_BATCH_EDIT;
		for (int i = 0; i < groupSitePriceList.size(); i++) {
			GroupSitePriceItem sp = groupSitePriceList.get(i);

			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.SITE_PRICES, sp.getIndex());
			
			// 判断SitePrice参数是否为空
			if (sp == null || StringUtils.isEmpty(sp.getSite())) {
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			// 判断价格
			if (sp.getPrice() <= 0) {
				apiPosition.addParam(GroupConstant.POSITION_PRICE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			int newPrice = conv.convertFrom(sp.getPrice());// 分
			if (!CproGroupConstant.isValidPriceValueRange(newPrice)) {
				apiPosition.addParam(GroupConstant.POSITION_PRICE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_RANGE.getValue(),
						GroupConfigErrorCode.SITEPRICE_RANGE.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}
			if (newPrice > plan.getBudget() * 100) {
				apiPosition.addParam(GroupConstant.POSITION_PRICE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			String site = sp.getSite().toLowerCase();
			// 域名去重
			if (listCompare.get(site) != null) { // 字符串匹配，区分大小写
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_DUP.getValue(),
						GroupConfigErrorCode.SITEPRICE_DUP.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			// 判断分网站是否在beidou的站点全库中
			Map<String, Integer> siteIdMap = UnionSiteCache.siteInfoCache.getSiteIdMap();
			Integer siteId = siteIdMap.get(site);
			if (siteId == null) {
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_NOT_FOUND.getValue(),
						GroupConfigErrorCode.SITE_NOT_FOUND.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			GroupSiteConfigHelper helper = new GroupSiteConfigHelper();

			// 判断分网站是否属于推广组的自选行业和自选网站内
			boolean isValid = helper.isValid(siteId, group.getGroupInfo().getIsAllSite(), 
					group.getGroupInfo().getSiteTradeListStr(), 
					group.getGroupInfo().getSiteListStr());
			if (!isValid) {
				// 域名无效
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}
			GroupSitePrice selectedSitePrice = selectedSitePriceMap.get(siteId);
			if (selectedSitePrice == null) {
				GroupSitePrice gsp = new GroupSitePrice();
				gsp.setGroupid(group.getGroupId());
				gsp.setPrice(newPrice);
				gsp.setSiteid(siteId);
				gsp.setSiteurl(site);
				gsp.setPlanid(plan.getPlanId());
				gsp.setUserid(plan.getUserId());

				toBeModifiedList.add(gsp);
				
				// 记录历史操作
				GroupTradeSitePriceOptVo after = new GroupTradeSitePriceOptVo();
				after.setId(siteId);
				after.setPrice(newPrice);
				OptContent content = new OptContent(group.getUserId(), opTypeAdd.getOpType(),
						opTypeAdd.getOpLevel(), group.getGroupId(),	null, 
						opTypeAdd.getTransformer().toDbString(after));
				optContents.add(content);
			} else {
				Integer oldPrice = selectedSitePrice.getPrice();
				selectedSitePrice.setPrice(newPrice);
				toBeModifiedList.add(selectedSitePrice);
				
				// 记录历史操作
				GroupTradeSitePriceOptVo before = new GroupTradeSitePriceOptVo();
				before.setId(selectedSitePrice.getSiteid());
				before.setPrice(oldPrice);	
				GroupTradeSitePriceOptVo after = new GroupTradeSitePriceOptVo();
				after.setId(selectedSitePrice.getSiteid());
				after.setPrice(newPrice);
				OptContent content = new OptContent(group.getUserId(), opTypeEdit.getOpType(),
						opTypeEdit.getOpLevel(), group.getGroupId(),
						opTypeEdit.getTransformer().toDbString(before), 
						opTypeEdit.getTransformer().toDbString(after));
				optContents.add(content);
			}
			
			listCompare.put(sp.getSite(), true);
		}

		return true;
	}
	
	public boolean validateDeleteSitePrice(CproGroup group, List<GroupSitePriceItem> groupSitePriceList, 
			List<GroupSitePrice> toBeRemoved, List<GroupSitePrice> toBeUpdated, 
			ApiResult<Object> result, List<OptContent> optContents) {

		if (group == null || CollectionUtils.isEmpty(groupSitePriceList)) {
			return false;
		}
		
		// 获取已有的分网站出价
		List<GroupSitePrice> selectedSitePriceList = groupSiteConfigMgr.findSitePriceByGroupId(group.getGroupId());
		Map<Integer, GroupSitePrice> selectedSitePriceMap = new HashMap<Integer, GroupSitePrice>();
		for (GroupSitePrice sitePrice : selectedSitePriceList) {
			selectedSitePriceMap.put(sitePrice.getSiteid(), sitePrice);
		}

		//YuanToCentConverter conv = new YuanToCentConverter();
		//CproPlan plan = cproPlanMgr.findCproPlanById(group.getPlanId());
		Map<String, Boolean> listCompare = new HashMap<String, Boolean>();
		
		final OpTypeVo opTypeDel = OptHistoryConstant.OPTYPE_GROUP_SITE_PRICE_BATCH_DELETE;
		for (int i = 0; i < groupSitePriceList.size(); i++) {
			GroupSitePriceItem sp = groupSitePriceList.get(i);

			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.SITE_PRICES, sp.getIndex());
			
			// 判断SitePrice参数是否为空
			if (sp == null || StringUtils.isEmpty(sp.getSite())) {
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			String site = sp.getSite().toLowerCase();
			// 域名去重
			if (listCompare.get(site) != null) { // 字符串匹配，区分大小写
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_DUP.getValue(),
						GroupConfigErrorCode.SITEPRICE_DUP.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			// 判断分网站是否在beidou的站点全库中
			Map<String, Integer> siteIdMap = UnionSiteCache.siteInfoCache.getSiteIdMap();
			Integer siteId = siteIdMap.get(site);
			if (siteId == null) {
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_NOT_FOUND.getValue(),
						GroupConfigErrorCode.SITE_NOT_FOUND.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			GroupSiteConfigHelper helper = new GroupSiteConfigHelper();

			// 判断分网站是否属于推广组的自选行业和自选网站内
			boolean isValid = helper.isValid(siteId, group.getGroupInfo().getIsAllSite(), 
					group.getGroupInfo().getSiteTradeListStr(), 
					group.getGroupInfo().getSiteListStr());
			if (!isValid) {
				// 域名无效
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_ERROR.getValue(),
						GroupConfigErrorCode.SITEPRICE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}
			
			GroupSitePrice selectedSitePrice = selectedSitePriceMap.get(siteId);
			if (selectedSitePrice == null) {
				// 域名无效
				apiPosition.addParam(GroupConstant.POSITION_SITE);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_PRICE_DEL_ERROR.getValue(),
						GroupConfigErrorCode.SITE_PRICE_DEL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			} else {
				GroupTradeSitePriceOptVo before = new GroupTradeSitePriceOptVo();
				before.setId(selectedSitePrice.getSiteid());
				before.setPrice(selectedSitePrice.getPrice());
				if (StringUtils.isEmpty(selectedSitePrice.getTargeturl())) {
					toBeRemoved.add(selectedSitePrice);
				} else {
					// 这里不能影响分网站点击链接功能，需要再save回去
					selectedSitePrice.setPrice(null);
					toBeUpdated.add(selectedSitePrice);
				}
				OptContent content = new OptContent(group.getUserId(), opTypeDel.getOpType(),
						opTypeDel.getOpLevel(),  group.getGroupId(), 
						opTypeDel.getTransformer().toDbString(before), null);
				optContents.add(content);
			}
			
			listCompare.put(sp.getSite(), true);
		}

		return true;
	}

	/**
	 * 验证SiteTargetUrl ：最大个数（输入个数，总个数），输入格式，同时为空,去重，有效性，点击链接格式，长度，GBK,域名绑定，aka,
	 * <br>
	 * 并设置推广组id
	 * 
	 * @author zengyunfeng
	 * @param group
	 *            :推广组id
	 * @param support
	 *            :保存错误信息,返回后包含验证没有通过的网站和错误提示信息的key
	 * @param curGroupFilter：
	 *            现有的过滤配置
	 * @param siteList :
	 *            待增加的网站列表
	 * @return 返回错误码
	 */
	public ApiResult<Object> validateSiteTargetUrl(final CproGroup group,
			final List<SiteUrlItemType> siteUrlList,
			List<GroupSitePrice> reList, ApiResult<Object> result,
			List<String> userDomains) {
		if (group == null || siteUrlList == null || reList == null) {
			return null;
		}

		// 超过单个推广组下分站点价格和链接的最大个数
		if (siteUrlList.size() > CproGroupConstant.SITEPRICE_ALL_MAX_NUM) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(
					PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.SITE_URL_REQ);

			result = ApiResultBeanUtils.addApiError(result,
					GroupConfigErrorCode.SITE_TARGETURL_MAX.getValue(),
					GroupConfigErrorCode.SITE_TARGETURL_MAX.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		if (siteUrlList.size() == 0) {
			return result;
		}

		Map<String, Boolean> listCompare = new HashMap<String, Boolean>();

		for (int i = 0; i < siteUrlList.size(); i++) {

			SiteUrlItemType siteUrl = siteUrlList.get(i);

			// 站点url为空
			if (StringUtils.isEmpty(siteUrl.getSiteUrl())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITE_URL_REQ);
				apiPosition.addParam(GroupConstant.SITE_URL_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_SITE_URL);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_TARGETURL_ERROR.getValue(),
						GroupConfigErrorCode.SITE_TARGETURL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 点击url不合规范
			if (StringUtils.isEmpty(siteUrl.getTargetUrl())
					|| !siteUrl.getTargetUrl().matches("^http[s]?:\\/\\/.*")) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITE_URL_REQ);
				apiPosition.addParam(GroupConstant.SITE_URL_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_TARGET_URL);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_TARGETURL_ERROR.getValue(),
						GroupConfigErrorCode.SITE_TARGETURL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 域名去重
			if (listCompare.get(siteUrl.getSiteUrl()) != null) { // 字符串匹配，区分大小写
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITE_URL_REQ);
				apiPosition.addParam(GroupConstant.SITE_URL_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_SITE_URL);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_TARGETURL_DUP.getValue(),
						GroupConfigErrorCode.SITE_TARGETURL_DUP.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 验证域名有效性
			Map<String, Integer> siteIdMap = UnionSiteCache.siteInfoCache
					.getSiteIdMap();
			Integer siteid = siteIdMap.get(siteUrl.getSiteUrl().toLowerCase());
			if (siteid == null) {
				// 域名不在beidou推荐中
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITE_URL_REQ);
				apiPosition.addParam(GroupConstant.SITE_URL_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_SITE_URL);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_NOT_FOUND.getValue(),
						GroupConfigErrorCode.SITE_NOT_FOUND.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 判断分网站是否在该推广组所选的行业或网站中
			GroupSiteConfigHelper helper = new GroupSiteConfigHelper();
			boolean isValid = helper.isValid(siteid, group.getGroupInfo().getIsAllSite(),
					group.getGroupInfo().getSiteTradeListStr(), 
					group.getGroupInfo().getSiteListStr());
			if (!isValid) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITE_URL_REQ);
				apiPosition.addParam(GroupConstant.SITE_URL_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_SITE_URL);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_TARGETURL_ERROR.getValue(),
						GroupConfigErrorCode.SITE_TARGETURL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 验证targeturl是否是用户的domain，及长度等
			if (!validateTargetUrl(siteUrl.getTargetUrl(), group.getUserId(), userDomains)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(
						PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.SITE_URL_REQ);
				apiPosition.addParam(GroupConstant.SITE_URL_LIST, i);
				apiPosition.addParam(GroupConstant.POSITION_TARGET_URL);

				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITE_TARGETURL_ERROR.getValue(),
						GroupConfigErrorCode.SITE_TARGETURL_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}

			// 验证通过
			// 待保存的过滤网站
			GroupSitePrice gsp = new GroupSitePrice();
			gsp.setGroupid(group.getGroupId());
			gsp.setSiteid(siteid);
			gsp.setSiteurl(siteUrl.getSiteUrl());
			gsp.setPlanid(group.getPlanId());
			gsp.setUserid(group.getUserId());
			gsp.setTargeturl(siteUrl.getTargetUrl());

			reList.add(gsp);
			listCompare.put(siteUrl.getSiteUrl(), true);
		}

		// aka批量审核
		Map<Integer, GroupSitePrice> akaResult = akaValidate(reList, group.getUserId());
		int length = reList.size();
		reList.clear();
		for (int i = 0; i < length; i++) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(GroupConstant.SITE_URL_REQ);
			apiPosition.addParam(GroupConstant.SITE_URL_LIST, i);
			apiPosition.addParam(GroupConstant.POSITION_SITE_URL);

			GroupSitePrice sitePrice2 = akaResult.get(i);
			if (sitePrice2 == null) {
				// 错误的
				result = ApiResultBeanUtils.addApiError(result,
						GroupConfigErrorCode.SITEPRICE_AUDIT.getValue(),
						GroupConfigErrorCode.SITEPRICE_AUDIT.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			} else {
				// 验证通过的
				reList.add(sitePrice2);
			}
		}

		return result;
	}

	/**
	 * 点击链接长度，GBK,域名绑定，aka
	 * 
	 * @author zengyunfeng
	 * @param support
	 * @param textProvider
	 * @param targetUrl
	 * @param userid
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private boolean validateTargetUrl(final String targetUrl,
			final Integer userid, List<String> userDomain) {
		if (org.apache.commons.lang.StringUtils.isEmpty(targetUrl)) {
			return true;
		}
		if (!StringUtils.isGBK(targetUrl)) {
			return false;
		}
		int length = StringUtils.byteLength(targetUrl);
		if (length > CproGroupConstant.GROUP_CONFIG_TARGETURL_MAX) {
			return false;
		}

		String domain = UrlParser.getMainDomain(targetUrl);

		boolean match = false;
		for (String dm : userDomain) {
			if (dm.equalsIgnoreCase(domain)) {
				match = true;
			}
		}
		return match;
	}

	/**
	 * aka批量审核， 验证特殊字符
	 * 
	 * @author chenlu
	 * @param sitePriceList
	 *            带审核的分网站价格和链接设置
	 * @param userid
	 * @return 审核通过的分网站价格和链接设置(对应sitePriceList中的序号)
	 */
	private Map<Integer, GroupSitePrice> akaValidate(
			final List<GroupSitePrice> sitePriceList, final int userid) {
		if (CollectionUtils.isEmpty(sitePriceList)) {
			return null;
		}

		int length = sitePriceList.size();
		List<UnitInfoView> unitList = new ArrayList<UnitInfoView>(sitePriceList
				.size());
		// aka请求中的序号对应在sitePriceList中的值
		Map<Integer, Integer> akaIndexMap = new HashMap<Integer, Integer>(
				length);
		int akaIndex = 0;

		Map<Integer, GroupSitePrice> allSitePrice = new HashMap<Integer, GroupSitePrice>();

		for (int index = 0; index < length; index++) {
			GroupSitePrice sp = sitePriceList.get(index);
			allSitePrice.put(index, sp);
			if (!org.apache.commons.lang.StringUtils.isEmpty(sp.getTargeturl())) {
				UnitInfoView unit = new UnitInfoView();
				unit.setWuliaoType(CproUnitConstant.MATERIAL_TYPE_PICTURE);
				unit.setShowUrl("1");
				unit.setTargetUrl(sp.getTargeturl());
				unitList.add(akaIndex, unit);
				akaIndexMap.put(akaIndex, index);
				akaIndex++;
			}
		}
		if (unitList.isEmpty()) {
			// 全部不需要aka验证，直接返回
			return allSitePrice;
		}

		List<String> valInfoList = UnitAkaAudit.akaAudit(unitList, userid);
		if (valInfoList == null || valInfoList.size() != unitList.size()) {
			// aka验证
			LogUtils.fatal(LOG,
					"aka response number not equal with request number");
			return null;
		}

		Set<Integer> errorIndexes = new HashSet<Integer>();
		Integer sitePriceIndex = 0;
		int akaLength = valInfoList.size();
		for (int index = 0; index < akaLength; index++) {
			String valInfo = valInfoList.get(index);
			if (valInfo == null) {
				sitePriceIndex = akaIndexMap.get(index);
				if (sitePriceIndex == null) { // 实际中不会出现
					continue;
				}
				errorIndexes.add(sitePriceIndex);
			} else if (!"".equals(valInfo)) {
				sitePriceIndex = akaIndexMap.get(index);
				if (sitePriceIndex == null) {
					continue;
				}
				errorIndexes.add(sitePriceIndex);
			}
		}

		Map<Integer, GroupSitePrice> result = new HashMap<Integer, GroupSitePrice>(
				sitePriceList.size());
		for (int index = 0; index < length; index++) {
			if (!errorIndexes.contains(index)) {
				result.put(index, sitePriceList.get(index));
			}
		}
		return result;
	}

	/**
	 * 
	 * @param groupTradePriceMap
	 * @param needGroupIdMap
	 * @param result
	 * @return
	 */
	public ApiResult<Object> validateAddTradePrice(Map<Long, 
			List<GroupTradePriceItem>> groupTradePriceMap, 
			Map<Long, List<GroupTradePriceItem>> canOptItemMap,
			Map<Long, CproGroup> needGroupIdMap, ApiResult<Object> result) {
		if (MapUtils.isEmpty(groupTradePriceMap)) {
			return result;
		}
		YuanToCentConverter conv = new YuanToCentConverter();
		// 获取推广组所属于推广计划
		List<Integer> groupIdList = ListArrayUtils.transLongList(groupTradePriceMap.keySet());
		Map<Long, CproGroup> groupMap = new HashMap<Long, CproGroup>();
		Map<Long, CproPlan> groupIdPlanMap = new HashMap<Long, CproPlan>();

		mappingGroupAndPlan(groupIdList, groupMap, groupIdPlanMap);
		
		// 拷贝推广组Id->推广组映射
		for(Long groupId : groupMap.keySet()){
			needGroupIdMap.put(groupId, groupMap.get(groupId));
		}
		
		// 分“推广组”验证
		for (Long groupId : groupTradePriceMap.keySet()) {
			// 用于验证“一个推广组下的需要修改出价的行业是否有重复”
			Map<Integer, Boolean> listCompare = new HashMap<Integer, Boolean>();

			// 用于“保留验证通过”的item
			List<GroupTradePriceItem> canOptItemList = canOptItemMap.get(groupId);
			
			List<GroupTradePriceItem> itemList = groupTradePriceMap.get(groupId);
			CproPlan plan = groupIdPlanMap.get(groupId);
			CproGroup group = groupMap.get(groupId);

			labelgroupLevel: 
			for (GroupTradePriceItem item : itemList) {
				// 验证“价格范围”
				int newPrice = conv.convertFrom(item.getPrice());
				if (!CproGroupConstant.isValidPriceValueRange(newPrice)) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.POSITION_PRICE);

					result = ApiResultBeanUtils.addApiError(result, 
							GroupConfigErrorCode.TRADEPRICE_RANGE.getValue(), 
							GroupConfigErrorCode.TRADEPRICE_RANGE.getMessage(), 
							apiPosition.getPosition(), null);
					deletInvalidElement(canOptItemList,item);
					continue;
				}

				// 验证“价格和预算关系”
				if (newPrice > plan.getBudget() * 100) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.POSITION_PRICE);

					result = ApiResultBeanUtils.addApiError(result, 
							GroupConfigErrorCode.TRADEPRICE_RANGE.getValue(), 
							GroupConfigErrorCode.TRADEPRICE_RANGE.getMessage(), 
							apiPosition.getPosition(), null);
					deletInvalidElement(canOptItemList,item);
					continue;
				}

				// 验证“行业重复”
				if (listCompare.containsKey(item.getTrade())) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.TRADE);

					result = ApiResultBeanUtils.addApiError(result, 
							GroupConfigErrorCode.TRADEPRICE_DUP.getValue(), 
							GroupConfigErrorCode.TRADEPRICE_DUP.getMessage(), 
							apiPosition.getPosition(), null);
					deletInvalidElement(canOptItemList,item);
					continue;
				}

				// 验证“行业有效性”
				TradeInfo tradeInfo = UnionSiteCache.tradeInfoCache.getTradeInfoById(item.getTrade());
				if (tradeInfo == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.TRADE);

					result = ApiResultBeanUtils.addApiError(result, 
							GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(), 
							GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);
					deletInvalidElement(canOptItemList,item);
					continue;
				}

				// 验证“非全网，但没有设置行业” && “需要修改出价的行业不在推广组所选择的行业中”
				boolean isAllSite = group.getGroupInfo().getIsAllSite() == CproGroupConstant.GROUP_ALLSITE;
				if (!isAllSite) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.TRADE);

					String groupTradeSiteStr = group.getGroupInfo().getSiteTradeListStr();
					if (StringUtils.isEmpty(groupTradeSiteStr)) {
						result = ApiResultBeanUtils.addApiError(result, 
								GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(), 
								GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(), 
								apiPosition.getPosition(), null);
						deletInvalidElement(canOptItemList,item);
						continue;
					}

					String[] idsStrArr = groupTradeSiteStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);
					List<Integer> idList = new ArrayList<Integer>();
					for (String idStr : idsStrArr) {
						try {
							idList.add(Integer.valueOf(idStr));
						} catch (NumberFormatException e) {
							continue labelgroupLevel;
						}
					}

					if (!idList.contains(tradeInfo.getTradeid()) && !idList.contains(tradeInfo.getParentid())) {
						result = ApiResultBeanUtils.addApiError(result, 
								GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(), 
								GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(), 
								apiPosition.getPosition(), null);
						deletInvalidElement(canOptItemList,item);
						continue;
					}
				}
				
				listCompare.put(item.getTrade(), Boolean.TRUE);

			}// end-一个推广组下的所有分行业出价
		}// end-需要处理的所有推广组
		return result;
	}
	
	private void deletInvalidElement(List<GroupTradePriceItem> canOptItemList, GroupTradePriceItem item) {
		for (GroupTradePriceItem sourceItem : canOptItemList) {
			if (sourceItem.getIndex() == item.getIndex()) {
				canOptItemList.remove(sourceItem);
				return;
			}
		}
	}
	
	private void mappingGroupAndPlan(List<Integer> groupIdList, 
			Map<Long, CproGroup> groupMap, 
			Map<Long, CproPlan> groupIdPlanMap) {
		List<CproGroup> cprogroupList = cproGroupMgr.findCproGroupByGroupIds(groupIdList);
		for (CproGroup group : cprogroupList) {
			groupMap.put(Long.valueOf(group.getGroupId()), group);
			CproPlan plan = cproPlanMgr.findCproPlanById(group.getPlanId());
			groupIdPlanMap.put(Long.valueOf(group.getGroupId()), plan);
		}
	}
	
	
	/**
	 * 
	 * @param groupTradeMap
	 * @param groupIdMap
	 * @param result
	 * @return
	 */
	public ApiResult<Object> validateDeleteTradePrice(Map<Long, List<GroupTradeItem>> groupTradeMap, 
			Map<Long, List<GroupTradeItem>> canOptItemMap,
			Map<Long, Map<Integer, GroupTradePrice>> needGroupTradePriceMap, 
			Map<Long, CproGroup> needGroupIdMap, ApiResult<Object> result) {
		if (MapUtils.isEmpty(groupTradeMap)) {
			return result;
		}
		List<Integer> groupIdList = ListArrayUtils.transLongList(groupTradeMap.keySet());

		// 获取“推广组->推广组分行业出价映射”
		Map<Long, Map<Integer, GroupTradePrice>> groupTradePriceMap = mappingGroupTradePrice(groupIdList);
		for (Long groupId : groupTradePriceMap.keySet()) {
			needGroupTradePriceMap.put(groupId, groupTradePriceMap.get(groupId));
		}

		// 获取“推广组Id->推广组映射”
		Map<Long, CproGroup> groupMap = mappingGroupAndId(groupIdList);
		for (Long groupId : groupMap.keySet()) {
			needGroupIdMap.put(groupId, groupMap.get(groupId));
		}

		// 分“推广组”验证
		for (Long groupId : groupTradeMap.keySet()) {
			// 用于验证“一个推广组下的需要修改出价的行业是否有重复”
			Map<Integer, Boolean> listCompare = new HashMap<Integer, Boolean>();

			// 用于“保留验证通过”的item
			List<GroupTradeItem> canOptItemList = canOptItemMap.get(groupId);

			List<GroupTradeItem> itemList = groupTradeMap.get(groupId);
			CproGroup group = groupMap.get(groupId);

			for (GroupTradeItem item : itemList) {
				// 验证“行业重复”
				if (listCompare.containsKey(item.getTrade())) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.TRADE);

					result = ApiResultBeanUtils.addApiError(result, 
							GroupConfigErrorCode.TRADE_DUPLICATE.getValue(), 
							GroupConfigErrorCode.TRADE_DUPLICATE.getMessage(), 
							apiPosition.getPosition(), null);

					deletInvalidElement(canOptItemList, item);
					continue;
				}

				// 验证“行业有效性”
				TradeInfo tradeInfo = UnionSiteCache.tradeInfoCache.getTradeInfoById(item.getTrade());
				if (tradeInfo == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.TRADE);

					result = ApiResultBeanUtils.addApiError(result, 
							GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(), 
							GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(), 
							apiPosition.getPosition(), null);

					deletInvalidElement(canOptItemList, item);
					continue;
				}

				// 验证“非全网，但没有设置行业”
				boolean isAllSite = group.getGroupInfo().getIsAllSite() == CproGroupConstant.GROUP_ALLSITE;
				if (!isAllSite) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(GroupConstant.TRADE_PRICES, item.getIndex());
					apiPosition.addParam(GroupConstant.TRADE);

					String groupTradeSiteStr = group.getGroupInfo().getSiteTradeListStr();
					if (StringUtils.isEmpty(groupTradeSiteStr)) {
						result = ApiResultBeanUtils.addApiError(result, 
								GroupConfigErrorCode.TRADE_NOT_FOUND.getValue(), 
								GroupConfigErrorCode.TRADE_NOT_FOUND.getMessage(), 
								apiPosition.getPosition(), null);
						deletInvalidElement(canOptItemList, item);
						continue;
					}
				}

				// 验证“需要删除行业出价的行业不在“推广组的单独出价列表中””
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(GroupConstant.TRADE_PRICES, item.getIndex());
				apiPosition.addParam(GroupConstant.TRADE);
				Map<Integer, GroupTradePrice> tradePriceMap = groupTradePriceMap.get(groupId);
				if (!tradePriceMap.containsKey(tradeInfo.getTradeid())) {
					result = ApiResultBeanUtils.addApiError(result, 
							GroupConfigErrorCode.TRADE_PRICE_DEL_NOT_EXIT_ERROR.getValue(),
							GroupConfigErrorCode.TRADE_PRICE_DEL_NOT_EXIT_ERROR.getMessage(), 
							apiPosition.getPosition(), null);

					deletInvalidElement(canOptItemList, item);
					continue;
				}
				
				listCompare.put(item.getTrade(), Boolean.TRUE);

			}// end-一个推广组下的所有分行业出价
		}// end-需要处理的所有推广组
		return result;
	}
	
	private void deletInvalidElement(List<GroupTradeItem> canOptItemList, GroupTradeItem item) {
		for (GroupTradeItem sourceItem : canOptItemList) {
			if (sourceItem.getIndex() == item.getIndex()) {
				canOptItemList.remove(sourceItem);
				return;
			}
		}
	}
	
	private Map<Long, Map<Integer, GroupTradePrice>> mappingGroupTradePrice(List<Integer> groupIdList) {
		Map<Long, Map<Integer, GroupTradePrice>> groupTradePriceMap = new HashMap<Long, Map<Integer, GroupTradePrice>>(groupIdList.size());
		for (Integer groupId : groupIdList) {
			Map<Integer, GroupTradePrice> tradePriceMap = new HashMap<Integer, GroupTradePrice>();

			List<GroupTradePrice> tradePriceList = groupSiteConfigMgr.findTradePriceByGroupId(groupId);
			if (CollectionUtils.isNotEmpty(tradePriceList)) {
				for (GroupTradePrice tradePrice : tradePriceList) {
					tradePriceMap.put(tradePrice.getTradeid(), tradePrice);
				}
			}
			groupTradePriceMap.put(Long.valueOf(groupId), tradePriceMap);
		}
		return groupTradePriceMap;
	}
	
	private Map<Long, CproGroup> mappingGroupAndId(List<Integer> groupIdList) {
		List<CproGroup> cprogroupList = cproGroupMgr.findCproGroupByGroupIds(groupIdList);
		Map<Long, CproGroup> groupMap = new HashMap<Long, CproGroup>(cprogroupList.size());
		for (CproGroup group : cprogroupList) {
			groupMap.put(Long.valueOf(group.getGroupId()), group);
		}
		return groupMap;
	}
	
	public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
		this.cproPlanMgr = cproPlanMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public void setGroupSiteConfigMgr(GroupSiteConfigMgr groupSiteConfigMgr) {
		this.groupSiteConfigMgr = groupSiteConfigMgr;
	}
}
