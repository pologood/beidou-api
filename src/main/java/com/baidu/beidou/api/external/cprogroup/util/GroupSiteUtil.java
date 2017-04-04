package com.baidu.beidou.api.external.cprogroup.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.SiteConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.constant.WhiteListCache;
import com.baidu.beidou.cprogroup.vo.BDSiteInfo;

public class GroupSiteUtil {

	private static Log log = LogFactory.getLog(GroupSiteUtil.class);

	/**
	 * 根据行业ID和网站ID获得不重复网站序号列表，网站id没有限制，但函数内部对网站内存序号进行排序
	 * 
	 * @param siteTradeIds
	 * @param siteIds
	 * @return add version:1.1.3
	 */
	public static List<Integer> getDistictSiteSeqs(List<Integer> siteTradeIds,
			List<Integer> siteIds, final int displayType) {

		List<Integer> distictSiteSeqs = new ArrayList<Integer>();

		List<Integer> siteTradeIndex = getSiteTradeIndex(siteTradeIds, displayType);
		List<Integer> siteIndex = getSiteIndex(siteIds);

		Set<Integer> seqSet = new HashSet<Integer>();
		seqSet.addAll(siteTradeIndex);
		seqSet.addAll(siteIndex);

		distictSiteSeqs.addAll(seqSet);

		return distictSiteSeqs;
	}

	/**
	 * 获得行业ID的索引
	 * 
	 * @param siteTradeIds
	 * @return 计算网站热度时也在使用，zp，version：1.1.3
	 */
	private static List<Integer> getSiteTradeIndex(List<Integer> siteTradeIds,
			final int displayType) {

		if (CollectionUtils.isEmpty(siteTradeIds)) {
			return new ArrayList<Integer>(0);
		}

		List<Integer> siteTradeIndexList = new ArrayList<Integer>();

		for (Integer tradeId : siteTradeIds) {
			// 必须判空，解决某些行业没有网站对应的情况
			List<Integer> ids = UnionSiteCache.tradeInfoCache.getSiteList(tradeId, displayType);
			if (CollectionUtils.isEmpty(ids)) {
				log.debug("trade id:" + tradeId + " has no site!");
				continue;
			}
			siteTradeIndexList.addAll(ids);
		}

		return siteTradeIndexList;
	}

	/**
	 * 获得网站索引
	 * 
	 * @param siteIds
	 * @return
	 */
	public static List<Integer> getSiteIndex(List<Integer> siteIds) {

		if (CollectionUtils.isEmpty(siteIds)) {
			return new ArrayList<Integer>(0);
		}

		List<Integer> siteList = new ArrayList<Integer>(siteIds.size());

		for (Integer siteId : siteIds) {
			if (UnionSiteCache.siteInfoCache.getReverseIndexBySiteId().get(siteId) == null) {
				log.debug("site id:" + siteId + " not exist!");
				continue;
			}
			siteList.add(UnionSiteCache.siteInfoCache.getReverseIndexBySiteId().get(siteId));
		}

		return siteList;
	}

	/**
	 * 将数据表中存储的网站ID字符串转换成列表
	 * 
	 * @param siteListStr
	 *            网站ID字符串
	 * @param siteInfoList
	 *            用户存储回传的信息
	 */
	public static void unConvertSiteList(final String siteListStr,
			List<Map<String, Object>> siteInfoList) {

		// 获得ID列表
		List<Integer> siteIdList = unConvertStrToList(siteListStr);

		unConvertSiteList(siteIdList, siteInfoList);
	}

	/**
	 * 将数据表中存储的网站ID列表串转换成列表
	 * 
	 * @param siteIdList
	 *            网站ID列表
	 * @param siteInfoList
	 *            用户存储回传的信息
	 */
	public static void unConvertSiteList(final List<Integer> siteIdList,
			List<Map<String, Object>> siteInfoList) {

		// 获得网站序号列表
		List<Integer> siteSeqList = getSiteIndex(siteIdList);

		genSiteListtoView(siteSeqList, siteInfoList);
	}

	/**
	 * 将数据表中存储和网站ID字符串转换成URL list add by lingbing 2010-4-29
	 * 
	 * @version 1.3.0
	 * @param siteListStr
	 * @param siteUrlList
	 */
	public static void unConvertSiteUrlList(final String siteListStr,
			List<String> siteUrlList) {
		// 获得ID列表
		List<Integer> siteIdList = unConvertStrToList(siteListStr);
		// 获得网站序号列表
		List<Integer> siteSeqList = getSiteIndex(siteIdList);
		for (Integer siteSeq : siteSeqList) {
			BDSiteInfo site = UnionSiteCache.siteInfoCache.getSiteInfoList().get(siteSeq);
			if (site != null) {
				siteUrlList.add(site.getSiteurl());
			}
		}
		// 根据url进行排序
		Collections.sort(siteUrlList);
	}

	/**
	 * 
	 * @param siteSeqList
	 * @param siteInfoList上午08:32:19
	 *            增加排序功能，zp，version：1.1.3
	 */
	public static void genSiteListtoView(final Collection<Integer> siteSeqList,
			List<Map<String, Object>> siteInfoList) {

		for (Integer siteSeq : siteSeqList) {

			BDSiteInfo site = UnionSiteCache.siteInfoCache.getSiteInfoList().get(siteSeq);
			if (site != null) {
				Map<String, Object> siteInfo = new HashMap<String, Object>(2);
				siteInfo.put("id", site.getSiteid());
				siteInfo.put("url", site.getSiteurl());
				siteInfoList.add(siteInfo);
			}
		}

		// 根据url进行排序
		Collections.sort(siteInfoList, new Comparator() {
			public int compare(Object o1, Object o2) {
				Map<String, Object> st1 = (Map<String, Object>) o1;
				Map<String, Object> st2 = (Map<String, Object>) o2;
				return ((String) st1.get("url")).compareTo((String) st2.get("url"));
			}
		});
	}

	/**
	 * 将存储的id字符串反转成id列表，例如：地域、网站行业、网站
	 * 
	 * @param idsStr
	 * @return下午01:33:26
	 */
	public static List<Integer> unConvertStrToList(final String idsStr) {
		List<Integer> idList = new ArrayList<Integer>();

		if (StringUtils.isEmpty(idsStr)) {
			return idList;
		}

		String[] idsStrArr = idsStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);

		for (String idStr : idsStrArr) {
			try {
				idList.add(Integer.valueOf(idStr));
			} catch (NumberFormatException e) {
				log.error(idStr + " is not a number");
				continue;
			}
		}

		return idList;
	}
	
    public static Set<Integer> unConvertStrToSet(final String idsStr) {
        Set<Integer> idSet = new HashSet<Integer>();

        if (StringUtils.isEmpty(idsStr)) {
            return idSet;
        }

        String[] idsStrArr = idsStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);

        for (String idStr : idsStrArr) {
            try {
                idSet.add(Integer.valueOf(idStr));
            } catch (NumberFormatException e) {
                log.error(idStr + " is not a number");
                continue;
            }
        }

        return idSet;
    }

	/**
	 * 将数据表中存储的网站行业ID字符串转换成一级行业列表，二级行业列表
	 * 
	 * @param siteTradeListStr
	 * @param firstLevelTradeIdList
	 * @param secondLevelTradeId
	 */
	public static void unConvertSiteTradeList(final String siteTradeListStr,
			List<Integer> firstLevelTradeIdList,
			Map<Integer, List<Integer>> secondLevelTradeId) {

		if (StringUtils.isEmpty(siteTradeListStr)) {
			return;
		}

		String[] siteTradeList = siteTradeListStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);
		for (String tradeIdStr : siteTradeList) {
			// 一级分类
			Integer tradeId = Integer.valueOf(tradeIdStr);
			/**
			 * 一级返回0，二级返回对应的一级ID
			 */
			Integer tradeClass = UnionSiteCache.tradeInfoCache.getSiteTradeLevel().get(tradeId);

			if (tradeClass == null) {
				continue;
			}

			if (tradeClass == 0) {
				// 过滤掉其他一级类别，version：1.3.3
				if (!UnionSiteCache.tradeInfoCache.getOtherFirstTrade().contains(tradeId)) {
					firstLevelTradeIdList.add(tradeId);
				}
			} else {
				List<Integer> currSecondLevelTradeId = secondLevelTradeId
						.get(tradeClass);

				if (currSecondLevelTradeId == null) {
					currSecondLevelTradeId = new ArrayList<Integer>();
					secondLevelTradeId.put(tradeClass, currSecondLevelTradeId);
					currSecondLevelTradeId.add(tradeId);
				} else {
					currSecondLevelTradeId.add(tradeId);
				}
			}
		}
	}

	public static List<Integer> filterByWhiteListSites(final Integer userid,
			final List<Integer> allSiteIdList) {

		// 如果是白名单用户，则不需要过滤白名单网站
		if (WhiteListCache.useBaiduUsers.has(userid)) {
			return allSiteIdList;
		}

		List<Integer> seqList = new ArrayList<Integer>(allSiteIdList.size());
		seqList.addAll(allSiteIdList);

		// @version1.
		seqList.removeAll(WhiteListCache.baiduSites.getIdList());
		return seqList;
	}

	/**
	 * add by hanxu
	 * version: 1.3.45
	 * project: cpweb-221-RPC优化及其他小功能升级
	 * 
	 * 修复百度自由流量的bug：不在白名单中的用户可以搜索出百度贴吧网站
	 * 此方法和上面的filterByWhiteListSites功能一样，不同的是传入的参数为 UNION_SITEINFO的list序号
	 * 
	 * @param userid
	 * @param allSiteIdList
	 * @return
	 */
	public static List<Integer> filterByWhiteListSitesWithSeqId(
			final Integer userid, final List<Integer> allSiteIdList) {

		// 如果是白名单用户，则不需要过滤白名单网站
		if (WhiteListCache.useBaiduUsers.has(userid)) {
			return allSiteIdList;
		}

		List<Integer> seqList = new ArrayList<Integer>(allSiteIdList.size());
		seqList.addAll(allSiteIdList);

		// @version1.
		seqList.removeAll(WhiteListCache.baiduSites.getSeqIdList());
		return seqList;
	}

	public static List<Integer> filterByDisplayType(final int displayType,
			final List<Integer> allSeqList) {
		List<Integer> seqList = new ArrayList<Integer>();

		for (Integer seqId : allSeqList) {
			BDSiteInfo info = UnionSiteCache.siteInfoCache.getSiteInfoList().get(seqId);
			if (SiteConstant.bitOp_supports(info.getDisplayType(), displayType)) {
				seqList.add(seqId);
			}
		}

		return seqList;
	}

	public static List<Integer> filterByWhiteListTrades(final Integer userid,
			final List<Integer> tradeIds) {

		// 如果是白名单用户，则不需要过滤白名单行业
		if (WhiteListCache.useBaiduUsers.has(userid)) {
			return tradeIds;
		}

		List<Integer> tradeList = new ArrayList<Integer>(tradeIds.size());
		tradeList.addAll(tradeIds);

		tradeList.removeAll(WhiteListCache.baiduTrades.getList());
		return tradeList;
	}

	/**
	 * 计算推广组热度
	 */
	public static Integer genGroupCmpLevel(List<Integer> siteSeqs) {

		/**
		 * ke为网站热度等级，value为网站个数
		 */
		Map<Integer, Integer> siteCmplevelMap = new HashMap<Integer, Integer>();

		siteCmplevelMap.put(CproGroupConstant.CMP_LEVEL_NONE, 0);
		siteCmplevelMap.put(CproGroupConstant.CMP_LEVEL_LOW, 0);
		siteCmplevelMap.put(CproGroupConstant.CMP_LEVEL_MODERATE, 0);
		siteCmplevelMap.put(CproGroupConstant.CMP_LEVEL_MEDIAN, 0);
		siteCmplevelMap.put(CproGroupConstant.CMP_LEVEL_HIGH, 0);
		siteCmplevelMap.put(CproGroupConstant.CMP_LEVEL_HEATED, 0);

		// 去除一二级对应关系
		List<Integer> siteSeqsTmp = rollupGroupSiteSeqs(siteSeqs);

		for (Integer siteSeq : siteSeqsTmp) {

			BDSiteInfo info = UnionSiteCache.siteInfoCache.getSiteInfoList().get(siteSeq);
			if (info == null) {
				continue;
			}

			int siteCmpLevel = info.getCmplevel();

			Integer hasNum = siteCmplevelMap.get(siteCmpLevel);

			if (hasNum != null) {
				siteCmplevelMap.put(siteCmpLevel, hasNum + 1);
			}
		}

		int cmplevel = CproGroupConstant.CMP_LEVEL_NONE;

		int size = 0;
		for (Entry<Integer, Integer> levelInfo : siteCmplevelMap.entrySet()) {

			int value = levelInfo.getValue();
			int level = levelInfo.getKey();

			// bug：当一个网站没有时，不应该走选取最高的情况，而是默认没有网站热度
			if (value > 0) {
				if (value > size) {
					cmplevel = level;
					size = value;
				} else if (value == size && level > cmplevel) {
					cmplevel = level;
				}
			}

		}

		return cmplevel;
	}

	/**
	 * 返回去重后的网站序号列表，将网站行业对应的网站与直接选择的网站进行去重
	 * 
	 * @param groupInfo
	 * @param targetTradeId
	 * @return上午10:09:14
	 */

	/**
	 * 去重一二级对应的情况
	 * 
	 * @param siteIds
	 * @return上午11:15:02
	 */
	private static List<Integer> rollupGroupSiteSeqs(List<Integer> siteSeqs) {

		if (CollectionUtils.isEmpty(siteSeqs)) {
			return new ArrayList<Integer>(0);
		}

		List<Integer> selected = new ArrayList<Integer>(siteSeqs.size());
		Set<Integer> tmpSiteSeqSet = new HashSet<Integer>(siteSeqs.size());
		// 去除同域名的情况
		for (Integer siteSeq : siteSeqs) {
			tmpSiteSeqSet.add(siteSeq);
		}

		for (Integer siteSeq : siteSeqs) {

			Integer parentSeq = getParentSiteSeq(siteSeq);

			/**
			 * 同时一级域名，二级域名选择了
			 */
			if (siteSeq.equals(parentSeq)) {
				selected.add(siteSeq);
			} else {
				if (tmpSiteSeqSet.contains(parentSeq)) {
					// do nothing
				} else {
					selected.add(siteSeq);
				}
			}
		}

		return selected;
	}

	/**
	 * 返回顶级域名的序号，如果没有顶级，则返回自己，否则返回父节点
	 * 
	 * @param siteSeq
	 * @return上午11:17:36
	 */
	private static Integer getParentSiteSeq(Integer siteSeq) {
		if (siteSeq == null) {
			return null;
		}

		BDSiteInfo info = UnionSiteCache.siteInfoCache.getSiteInfoList().get(siteSeq);
		if (info == null) {
			return null;
		}
		Integer parentId = info.getParentid();

		if (parentId == null || parentId == 0) {
			return siteSeq;
		}

		Integer index = UnionSiteCache.siteInfoCache.getReverseIndexBySiteId()
				.get(parentId);
		return index;
	}
}
