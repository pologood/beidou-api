/**
 * 
 */
package com.baidu.beidou.api.external.kr.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.cprogroup.bo.CproGroupInfo;
import com.baidu.beidou.api.external.kr.comparator.SiteCmpLevelComparator;
import com.baidu.beidou.api.external.kr.comparator.SiteCmpSizeComparator;
import com.baidu.beidou.api.external.kr.comparator.SiteUrlComparator;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.SiteConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.constant.WhiteListCache;
import com.baidu.beidou.cprogroup.vo.BDSiteInfo;
import com.baidu.beidou.api.external.kr.vo.CmpLevelVO;
import com.baidu.beidou.api.external.kr.vo.GroupSiteVO;
import com.baidu.beidou.api.external.kr.vo.RegionSelectionVo;
import com.baidu.beidou.cprogroup.vo.RegionSumInfo;
import com.baidu.beidou.cprogroup.vo.SiteSumInfo;
import com.baidu.beidou.api.external.kr.vo.TradeSelectionVo;
import com.baidu.beidou.api.external.kr.util.TmpReginfoCache;


public class GroupSiteUtil {
	
	private static Log log = LogFactory.getLog(GroupSiteUtil.class);
	
//	public final static String SITESORTITEM_FLAGE = "siteSortItem";		//二级行业request前缀
//	public final static String REGIONSORTITEM_FLAGE = "regionSortItem";	
//	public final static String REGADVANCESORTITEM_FLAGE = "regionAdvanceSortItem";
	
	/**
	 * 根据行业ID和网站ID获得不重复网站个数，网站id没有限制，但函数内部对网站内存序号进行排序
	 * 
	 * @param siteTradeIds
	 * @param siteIds
	 * @return
	 */
	/*public static int countSite(List<Integer> siteTradeIds, List<Integer> siteIds){

		List<Integer> distictSiteSeqs = getDistictSiteSeqs(siteTradeIds, siteIds);		
		return distictSiteSeqs.size();	     
	}*/
	
	
	/**
	 * 根据行业ID和网站ID获得不重复网站序号列表，网站id没有限制，但函数内部对网站内存序号进行排序
	 * @param siteTradeIds
	 * @param siteIds
	 * @return
	 * add version:1.1.3
	 */
	public static List<Integer> getDistictSiteSeqs(List<Integer> siteTradeIds, List<Integer> siteIds, final int displayType){

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
	 * @param siteTradeIds
	 * @return
	 * 计算网站热度时也在使用，zp，version：1.1.3
	 */
	private static List<Integer> getSiteTradeIndex(List<Integer> siteTradeIds, final int displayType){
		
		if(CollectionUtils.isEmpty(siteTradeIds)){
			return new ArrayList<Integer>(0);
		}
		
		List<Integer> siteTradeIndexList = new ArrayList<Integer>();
		
		for(Integer tradeId : siteTradeIds){
			//必须判空，解决某些行业没有网站对应的情况
			List<Integer> ids = UnionSiteCache.tradeInfoCache.getSiteList(tradeId, displayType);
			if(CollectionUtils.isEmpty(ids)){
				log.debug("trade id : " + tradeId + " : has no site!");
				continue;
			}
			siteTradeIndexList.addAll(ids);		
		}
		
		return siteTradeIndexList; 		
	}
/**
 * 获得网站索引
 * @param siteIds
 * @return
 */
	public static List<Integer> getSiteIndex(List<Integer> siteIds){
		
		if(CollectionUtils.isEmpty(siteIds)){
			return new ArrayList<Integer>(0);
		}
		
		List<Integer> siteList = new ArrayList<Integer>(siteIds.size());
		
		for(Integer siteId : siteIds){
			if(UnionSiteCache.siteInfoCache.getReverseIndexBySiteId().get(siteId) == null){
				log.debug("site id : " + siteId + " : not exist!");
				continue;
			}
			siteList.add(UnionSiteCache.siteInfoCache.getReverseIndexBySiteId().get(siteId));			
		}
		
		return siteList; 		
	}
	/**
	 * 为了兼容删除的高级地域和几个二级地域，调用新方法unConvertRegListAdapter
	 * 将数据表中存储的地域ID字符串转换成一级地域列表，二级地域列表,参数必须事先初始化
	 * @param regListStr
	 * @param firstLevelRegIdList
	 * @param secondLevelRegId
	 * @deprecated 小心使用，终将废弃此方法
	 */
	public static void unConvertRegListAdapter(final String regListStr, 
			List<Integer> firstLevelProvRegIdList, 
			Map<Integer, List<Integer>> secondLevelProvRegId,
			List<Integer> firstLevelOperRegIdList, 
			Map<Integer, List<Integer>> secondLevelOperRegId){
		
		if(StringUtils.isEmpty(regListStr)){
			return;
		}
		
//		正则表达式，转义
		String[] regList = regListStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);
		
		for(String regIdStr : regList){
			//一级分类
			Integer regId = Integer.valueOf(regIdStr);
			
			Integer[] regInfo = UnionSiteCache.regCache.getRegInfoMap().get(regId);
			
			if(regInfo == null){
				// 从临时的地域cache中取出：高级地域，及删除的二级地域的相关数据
				regInfo = TmpReginfoCache.get(regId);
				if(regInfo == null){
					log.debug("reg id : " + regId + " : not exist");
					continue;
				}
			}
			
			/**
			 * 对于一级地域，[0]=0，[1]=分类标识（省级地域/高级地域）
			 * 对于二级地域，[0]=一级地域ID，[1]=分类标识（省级地域/高级地域）
			 */
			if(regInfo[0] == 0){
				if(regInfo[1] == UnionSiteCache.regCache.REG_TYPE_PROVINCE){
					firstLevelProvRegIdList.add(regId);
				}else{
					firstLevelOperRegIdList.add(regId);
				}
				
			}else{
				
				if(regInfo[1] == UnionSiteCache.regCache.REG_TYPE_PROVINCE){
					List<Integer> currSecondLevelRegId = secondLevelProvRegId.get(regInfo[0]);
					
					if(currSecondLevelRegId == null){
						currSecondLevelRegId = new ArrayList<Integer>();
						secondLevelProvRegId.put(regInfo[0], currSecondLevelRegId);
						currSecondLevelRegId.add(regId);
					}else{
						currSecondLevelRegId.add(regId);
					}
				}else{
					List<Integer> currSecondLevelRegId = secondLevelOperRegId.get(regInfo[0]);
					
					if(currSecondLevelRegId == null){
						currSecondLevelRegId = new ArrayList<Integer>();
						secondLevelOperRegId.put(regInfo[0], currSecondLevelRegId);
						currSecondLevelRegId.add(regId);
					}else{
						currSecondLevelRegId.add(regId);
					}
				}
				
			}
		}
	}
	
	/**
	 * 将数据表中存储的地域ID字符串转换成一级地域列表，二级地域列表,参数必须事先初始化
	 * @param regListStr
	 * @param firstLevelRegIdList
	 * @param secondLevelRegId
	 * @deprecated 小心使用，终将废弃此方法
	 */
	public static void unConvertRegList(final String regListStr, 
			List<Integer> firstLevelProvRegIdList, 
			Map<Integer, List<Integer>> secondLevelProvRegId,
			List<Integer> firstLevelOperRegIdList, 
			Map<Integer, List<Integer>> secondLevelOperRegId){
		
		if(StringUtils.isEmpty(regListStr)){
			return;
		}
		
//		正则表达式，转义
		String[] regList = regListStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);
		
		for(String regIdStr : regList){
			//一级分类
			Integer regId = Integer.valueOf(regIdStr);
			
			Integer[] regInfo = UnionSiteCache.regCache.getRegInfoMap().get(regId);
			
			if(regInfo == null){
				log.debug("reg id : " + regId + " : not exist");
				continue;
			}
			
			/**
			 * 对于一级地域，[0]=0，[1]=分类标识（省级地域/高级地域）
			 * 对于二级地域，[0]=一级地域ID，[1]=分类标识（省级地域/高级地域）
			 */
			if(regInfo[0] == 0){
				if(regInfo[1] == UnionSiteCache.regCache.REG_TYPE_PROVINCE){
					firstLevelProvRegIdList.add(regId);
				}else{
					firstLevelOperRegIdList.add(regId);
				}
				
			}else{
				
				if(regInfo[1] == UnionSiteCache.regCache.REG_TYPE_PROVINCE){
					List<Integer> currSecondLevelRegId = secondLevelProvRegId.get(regInfo[0]);
					
					if(currSecondLevelRegId == null){
						currSecondLevelRegId = new ArrayList<Integer>();
						secondLevelProvRegId.put(regInfo[0], currSecondLevelRegId);
						currSecondLevelRegId.add(regId);
					}else{
						currSecondLevelRegId.add(regId);
					}
				}else{
					List<Integer> currSecondLevelRegId = secondLevelOperRegId.get(regInfo[0]);
					
					if(currSecondLevelRegId == null){
						currSecondLevelRegId = new ArrayList<Integer>();
						secondLevelOperRegId.put(regInfo[0], currSecondLevelRegId);
						currSecondLevelRegId.add(regId);
					}else{
						currSecondLevelRegId.add(regId);
					}
				}
				
			}
		}
	}
	/**
	 * 将数据表中存储的网站ID字符串转换成列表
	 * @param siteListStr 网站ID字符串
	 * @param siteInfoList 用户存储回传的信息
	 */
	public static void unConvertSiteList(final String siteListStr, List<Map<String, Object>> siteInfoList){
		
//		获得ID列表
		List<Integer> siteIdList = unConvertStrToList(siteListStr);
		
		unConvertSiteList(siteIdList,siteInfoList);
	}
	
	/**
	 * 将数据表中存储的网站ID列表串转换成列表
	 * @param siteIdList 网站ID列表
	 * @param siteInfoList 用户存储回传的信息 
	 */
	public static void unConvertSiteList(final List<Integer> siteIdList, List<Map<String, Object>> siteInfoList){
		
		//获得网站序号列表
		List<Integer> siteSeqList = getSiteIndex(siteIdList);
		
		genSiteListtoView(siteSeqList, siteInfoList);		
	}
	
	/**
	 * 将数据表中存储和网站ID字符串转换成URL list
	 * add by lingbing
	 * 2010-4-29
	 * @version 1.3.0
	 * @param siteListStr
	 * @param siteUrlList
	 */
	public static void unConvertSiteUrlList(final String siteListStr, List<String> siteUrlList){
//		获得ID列表
		List<Integer> siteIdList = unConvertStrToList(siteListStr);
		//获得网站序号列表
		List<Integer> siteSeqList = getSiteIndex(siteIdList);		
		for(Integer siteSeq : siteSeqList){			
			BDSiteInfo site = UnionSiteCache.siteInfoCache.getSiteInfoList().get(siteSeq);
			if(site != null){				
				siteUrlList.add(site.getSiteurl());
			}			
		}
		//根据url进行排序
		Collections.sort( siteUrlList);	   
	}
	
	/**
	 * 
	 * @param siteSeqList
	 * @param siteInfoList上午08:32:19
	 * 增加排序功能，zp，version：1.1.3
	 */
	public static void genSiteListtoView(final Collection<Integer> siteSeqList, List<Map<String, Object>> siteInfoList){
		
			
		for(Integer siteSeq : siteSeqList){
			
			BDSiteInfo site = UnionSiteCache.siteInfoCache.getSiteInfoList().get(siteSeq);
			if(site != null){
				Map<String, Object> siteInfo = new HashMap<String, Object>(2);
				siteInfo.put("id", site.getSiteid());
				siteInfo.put("url", site.getSiteurl());
				siteInfoList.add(siteInfo);
			}			
		}
		
		//根据url进行排序
		Collections.sort(siteInfoList, new Comparator(){  
	        public int compare(Object o1, Object o2){  
	        	Map<String, Object> st1 =(Map<String, Object>)o1;  
	        	Map<String, Object> st2 =(Map<String, Object>)o2;  
	        	return  ((String)st1.get("url")).compareTo((String)st2.get("url"));  
	        }  
		}); 
	}
//	
//	/**
//	 * 将数据表中存储的网站ID列表字符串转换成ID列表
//	 * @param siteListStr
//	 * @param siteIdList
//	 */
//	private static List<Integer> unConvertSiteList(final String siteListStr){
//		
//		List<Integer> siteIdList = new ArrayList<Integer>();
//		
//		if(StringUtils.isEmpty(siteListStr)){
//			return siteIdList;
//		}
//		
//		String[] siteList = siteListStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);		
//		
//		for(String siteIdStr : siteList){
//			siteIdList.add(Integer.valueOf(siteIdStr));			
//		}
//		return siteIdList;
//	}
	
	
	/**
	 * 将存储的id字符串反转成id列表，例如：地域、网站行业、网站
	 * @param idsStr
	 * @return下午01:33:26
	 */
	public static List<Integer> unConvertStrToList(final String idsStr){
		List<Integer> idList = new ArrayList<Integer>();
		
		if(StringUtils.isEmpty(idsStr)){
			return idList;
		}
		
		String[] idsStrArr = idsStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);
		
		for(String idStr : idsStrArr){
			try {
				idList.add(Integer.valueOf(idStr));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				log.error(idStr+" is not a number");
				continue;
			}
		}
		
		return idList;
	}
	
	
	/**
	 * 将数据表中存储的网站行业ID字符串转换成一级行业列表，二级行业列表
	 * @param siteTradeListStr
	 * @param firstLevelTradeIdList
	 * @param secondLevelTradeId
	 */
	public static void unConvertSiteTradeList(final String siteTradeListStr, List<Integer> firstLevelTradeIdList, Map<Integer, List<Integer>> secondLevelTradeId){
		
		if(StringUtils.isEmpty(siteTradeListStr)){
			return;
		}
		
		String[] siteTradeList = siteTradeListStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);
		for(String tradeIdStr : siteTradeList){
			//一级分类
			Integer tradeId = Integer.valueOf(tradeIdStr);
			/**
			 * 一级返回0，二级返回对应的一级ID
			 */
			Integer tradeClass = UnionSiteCache.tradeInfoCache.getSiteTradeLevel().get(tradeId);
			
			if(tradeClass == null){
				continue;
			}
			
			if(tradeClass == 0){
				//过滤掉其他一级类别，version：1.3.3
				if(! UnionSiteCache.tradeInfoCache.getOtherFirstTrade().contains(tradeId)){
					firstLevelTradeIdList.add(tradeId);
				}				
			}else{
				List<Integer> currSecondLevelTradeId = secondLevelTradeId.get(tradeClass);
				
				if(currSecondLevelTradeId == null){
					currSecondLevelTradeId = new ArrayList<Integer>();
					secondLevelTradeId.put(tradeClass, currSecondLevelTradeId);
					currSecondLevelTradeId.add(tradeId);
				}else{
					currSecondLevelTradeId.add(tradeId);
				}
			}
		}
	}
//	/**
//	 * 
//	 * @param request
//	 * @param regionSortItem
//	 * @param itemRequestFlage
//	 * @return
//	 */
//	public static Map genRegionInfo(HttpServletRequest request, List<Integer> regionSortItem, final String itemRequestFlage){
//		
//		int regSum = 0;
//		StringBuilder regBuilder = new StringBuilder();	
//		
//		//地域不为空
//		if(CollectionUtils.isNotEmpty(regionSortItem)){
//			
//			regSum += regionSortItem.size();
//			
//			List<Integer> secRegIdList = new ArrayList<Integer>();
//			/**
//			 * 判断是否是新增操作，如果是修改，则如果已有地域数据为空，表示未修改
//			 */
//			
//			
//			for(Integer regId : regionSortItem){
//				//mod by lingbing, 2010-08-13, regId可能为空
//				if (regId == null) {
//					continue;
//				}
//				
//				
//				//获得二级地域ID
//				String[] secRegIds = request.getParameterValues(itemRequestFlage + regId);
//				
////				全选中时只存储一级，部分选中时存储部分
//				//北京等属于此种情况,没有二级分类，则添加一级分类
//				if(ArrayUtils.isEmpty(secRegIds)){//区分新增和修改
//					regBuilder.append(regId).append(CproGroupConstant.FIELD_SEPERATOR);					
//					continue;
//				}
//				
//				//重复复使用
//				secRegIdList.clear();
//				
//				for(String secRegId : secRegIds){
//					if(StringUtils.isNotEmpty(secRegId)){
//						secRegIdList.add(Integer.parseInt(secRegId));
//					}
//				}
//				
//				if(UnionSiteCache.regCache.getFirstRegChildrenSum().get(regId) != null
//						&& UnionSiteCache.regCache.getFirstRegChildrenSum().get(regId) == secRegIdList.size()){
//					regBuilder.append(regId).append(CproGroupConstant.FIELD_SEPERATOR);
//				}else{
//					
//					for(Integer secRegId : secRegIdList){
//						regBuilder.append(secRegId).append(CproGroupConstant.FIELD_SEPERATOR);
//					}
//				}
//			}
//		}
//		
//		Map resultInfo = new HashMap(2);
//		
//		resultInfo.put("regSum", regSum);
//		resultInfo.put("regStr", regBuilder.toString());
//		
//		return resultInfo;		
//	}
//	/**
//	 * 
//	 * @param request
//	 * @param siteSortItem
//	 * @param siteIds
//	 * @param itemRequestFlage
//	 * @param secondLevelTradeId 为空标识新增，或者以前为全网站投放，不为空标识修改，还需要保持以前选择二级行业
//	 * @return
//	 * 废弃，version：1.2.0
//	 */
//	@Deprecated
//	public static Map genSiteInfo(HttpServletRequest request, List<Integer> siteSortItem, List<Integer> siteIds, final String itemRequestFlage){
//		
//		StringBuilder tradeBuilder = new StringBuilder();
//		StringBuilder siteBuilder = new StringBuilder();
//					
//	//	计算非重复使用
//		if(CollectionUtils.isNotEmpty(siteIds)){
//			for(Integer siteId : siteIds){
//				siteBuilder.append(siteId).append(CproGroupConstant.FIELD_SEPERATOR);				
//			}
//		}			
//		
//		
//		//计算非重复使用
//		List<Integer> secondTradeIdList = new ArrayList<Integer>();
//		List<Integer> tradeIdList = new ArrayList<Integer>();
//		
//	//	一级行业不为空
//		if(CollectionUtils.isNotEmpty(siteSortItem)){
//			
//			
//			List<Integer> secTradeIdList = new ArrayList<Integer>();
//			
//			
//			for(Integer siteTradeId : siteSortItem){
//	//			获得二级地域ID
//				String[] secTradeIds = request.getParameterValues(itemRequestFlage + siteTradeId);
//				
//	//			全选中时只存储一级，部分选中时存储部分
//				//北京等属于此种情况，不存在二级分类，则直接添加一级分类
//				if(ArrayUtils.isEmpty(secTradeIds)){
//										
//					tradeIdList.add(siteTradeId);
//										
//					//break;//需要继续执行循环，张鹏，1.0.0.1
//					continue;
//				}
//				
//	//			重复使用
//				secTradeIdList.clear();
//				
//				for(String secTradeId : secTradeIds){
//					if(StringUtils.isNotEmpty(secTradeId)){
//						secTradeIdList.add(Integer.parseInt(secTradeId));
//					}
//				}
//				
//				if(UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildrenSum().get(siteTradeId) != null
//						&& UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildrenSum().get(siteTradeId) == secTradeIdList.size()){
//	//				计算非重复网站个数使用
//					tradeIdList.add(siteTradeId);
//				}else{
//					
//					secondTradeIdList.addAll(secTradeIdList);					
//				}					
//			}
//			
//		}
//		
//		//如果选择了全部一级行业，则补足“其他”
//		if(CollectionUtils.isEmpty(secondTradeIdList) &&
//				tradeIdList.size() == UnionSiteCache.tradeInfoCache.getFirstTradeNum()){
////			补足其他一级行业
//			tradeIdList.addAll(UnionSiteCache.tradeInfoCache.getOtherFirstTrade());
//		}else{
//			//补足二级行业
//			tradeIdList.addAll(secondTradeIdList);
//		}
//		
//		for(Integer tradeId : tradeIdList){
//			tradeBuilder.append(tradeId).append(CproGroupConstant.FIELD_SEPERATOR);				
//		}
//		
//		Map resultInfo = new HashMap(3);
//		//传入的行业id已经是按照一级行业，二级行业排序的了
//		List<Integer> distictSiteSeqs = getDistictSiteSeqs(tradeIdList, siteIds, SiteConstant.DISP_FIXED_FLAG);	//已废弃
//		resultInfo.put("siteSum", distictSiteSeqs.size());
//		resultInfo.put("siteTradeStr", tradeBuilder.toString());
//		resultInfo.put("siteStr", siteBuilder.toString());
//		//网站热度
//		resultInfo.put("cmpLevel", GroupSiteUtil.genGroupCmpLevel(distictSiteSeqs));
//		//TODO:
//		
//		return resultInfo;
//	}
//	
//	/**
//	 * 
//	 * @param request
//	 * @param siteSortItem 一级行业列表，直接在前端传
//	 * @param siteIds	网站列表
//	 * @return 行业list，网站总数（包括行业中的网站），推广组热度
//	 */
//	public static SiteSumInfo genSiteInfo(HttpServletRequest request, List<Integer> siteSortItem, List<Integer> siteIds, boolean isWhiteUser, final int groupType){
//		
//		//计算非重复使用
//		List<Integer> secondTradeIdList = new ArrayList<Integer>();
//		List<Integer> tradeIdList = new ArrayList<Integer>();
//		
//	//	一级行业不为空
//		if(CollectionUtils.isNotEmpty(siteSortItem)){
//			
//			
//			List<Integer> secTradeIdList = new ArrayList<Integer>();
//			
//			
//			for(Integer siteTradeId : siteSortItem){
//				//mod by lingbing, 2010-08-13, regId可能为空
//				if (siteTradeId == null) {
//					continue;
//				}
//				
//	//			获得二级地域ID
//				String[] secTradeIds = request.getParameterValues(SITESORTITEM_FLAGE + siteTradeId);
//				
//	//			全选中时只存储一级，部分选中时存储部分
//				//北京等属于此种情况，不存在二级分类，则直接添加一级分类
//				if(ArrayUtils.isEmpty(secTradeIds)){
//										
//					tradeIdList.add(siteTradeId);
//										
//					//break;//需要继续执行循环，张鹏，1.0.0.1
//					continue;
//				}
//				
//	//			重复使用
//				secTradeIdList.clear();
//				
//				for(String secTradeId : secTradeIds){
//					if(StringUtils.isNotEmpty(secTradeId)){
//						secTradeIdList.add(Integer.parseInt(secTradeId));
//					}
//				}
//				
//				if(UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildrenSum().get(siteTradeId) != null
//						&& UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildrenSum().get(siteTradeId) == secTradeIdList.size()){
//	//				计算非重复网站个数使用
//					tradeIdList.add(siteTradeId);
//				}else{
//					
//					secondTradeIdList.addAll(secTradeIdList);					
//				}					
//			}
//			
//		}
//		
//		//added by zhuqian @1.2.16 投放百度自有流量功能
//		//如果是白名单用户，只要是选择的所有的常规一级行业，则需要补足一级的“其他”
//		List<Integer> tmpIdList = new ArrayList<Integer>(tradeIdList.size());
//		tmpIdList.addAll(tradeIdList);
//		tmpIdList.removeAll(WhiteListCache.baiduTrades.getList());
//		int regularFirstTradeSize = tmpIdList.size();
//		
//		//如果选择了全部一级行业，则补足“其他”
//		if(CollectionUtils.isEmpty(secondTradeIdList) &&
//				regularFirstTradeSize == UnionSiteCache.tradeInfoCache.getFirstTradeNum()){
////			补足其他一级行业
//			tradeIdList.addAll(UnionSiteCache.tradeInfoCache.getOtherFirstTrade());
//		}else{
//			//补足二级行业
//			tradeIdList.addAll(secondTradeIdList);
//		}
//		
//		//传入的行业id已经是按照一级行业，二级行业排序的了
//		int displayType = SiteConstant.getDisplayTypeFromGroupType(groupType);
//		
//		List<Integer> distictSiteSeqs = getDistictSiteSeqs(tradeIdList, siteIds, displayType);	
//		
//		SiteSumInfo siteSumInfo = new SiteSumInfo();
//		siteSumInfo.setSiteTradeList(tradeIdList);
//		siteSumInfo.setSiteSum(distictSiteSeqs.size());
//		siteSumInfo.setCmpLevel(GroupSiteUtil.genGroupCmpLevel(distictSiteSeqs));
//		siteSumInfo.setSiteList(siteIds);
//		
//		return siteSumInfo;
//	}
	
	public static List<Integer> filterByWhiteListSites(final Integer userid,
			final List<Integer> allSiteIdList) {

		//如果是白名单用户，则不需要过滤白名单网站
		if(WhiteListCache.useBaiduUsers.has(userid)){
			return allSiteIdList;
		}
		
		List<Integer> seqList = new ArrayList<Integer>(allSiteIdList.size());
		seqList.addAll(allSiteIdList);
		
		//@version1.
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
	public static List<Integer> filterByWhiteListSitesWithSeqId(final Integer userid,
			final List<Integer> allSiteIdList) {

		//如果是白名单用户，则不需要过滤白名单网站
		if(WhiteListCache.useBaiduUsers.has(userid)){
			return allSiteIdList;
		}
		
		List<Integer> seqList = new ArrayList<Integer>(allSiteIdList.size());
		seqList.addAll(allSiteIdList);
		
		//@version1.
		seqList.removeAll(WhiteListCache.baiduSites.getSeqIdList());
		return seqList;
	}
	
	public static List<Integer> filterByDisplayType(final int displayType, final List<Integer> allSeqList){
		List<Integer> seqList = new ArrayList<Integer>();
		
		for(Integer seqId : allSeqList){
			
			BDSiteInfo info = UnionSiteCache.siteInfoCache.getSiteInfoList().get(seqId);
			if(SiteConstant.bitOp_supports(info.getDisplayType(), displayType)){
				seqList.add(seqId);
			}
		}
		
		return seqList;
	}
	
	public static List<Integer> filterByWhiteListTrades(final Integer userid, final List<Integer> tradeIds){
		
		//如果是白名单用户，则不需要过滤白名单行业
		if(WhiteListCache.useBaiduUsers.has(userid)){
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
	public static Integer genGroupCmpLevel(List<Integer> siteSeqs){
		
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
		
//		去除一二级对应关系		
		List<Integer> siteSeqsTmp = rollupGroupSiteSeqs(siteSeqs);
		
		
		for(Integer siteSeq : siteSeqsTmp){
			
			BDSiteInfo info = UnionSiteCache.siteInfoCache.getSiteInfoList().get(siteSeq); 
			if(info == null){
				continue;
			}
			
			int siteCmpLevel = info.getCmplevel();
			
			Integer hasNum = siteCmplevelMap.get(siteCmpLevel);
			
			if(hasNum != null){
				siteCmplevelMap.put(siteCmpLevel, hasNum + 1);
			}
		}
		
		
		
		int cmplevel = CproGroupConstant.CMP_LEVEL_NONE;
		
		int size = 0;
		for(Entry<Integer, Integer> levelInfo : siteCmplevelMap.entrySet()){
			
			int value =levelInfo.getValue();
			int level = levelInfo.getKey();
			
			//bug：当一个网站没有时，不应该走选取最高的情况，而是默认没有网站热度
			if(value > 0){
				if(value > size){
					cmplevel = level;
					size = value;
				}else if(value == size && level > cmplevel){
					cmplevel = level;
				}
			}
			
		}
		
		return cmplevel;
	}
	
	/**
	 * 在网站热度详细列表中使用
	 * @param groupInfo
	 * @param targetTradeId
	 * @return上午11:13:39
	 */	
	public static List<GroupSiteVO> getRollupGroupSiteDetail(CproGroupInfo groupInfo, Integer targetTradeId, final int displayType){
		
//		判空处理
		if(groupInfo == null 
				|| groupInfo.getIsAllSite() == null 
				|| groupInfo.getIsAllSite() == 1){
			return new ArrayList<GroupSiteVO>(0);
		}
		
//		获取推广组定向的网站ID
		List<Integer> siteIds = unConvertStrToList(groupInfo.getSiteListStr());
//		获取推广组定向的网站行业ID
		List<Integer> tradeIds = unConvertStrToList(groupInfo.getSiteTradeListStr());
		
		List<Integer> siteSeqs = null;
		
		if(targetTradeId == null){
			siteSeqs = GroupSiteUtil.getDistictSiteSeqs(tradeIds, siteIds, displayType);
		}else{
			
			List<Integer> siteIdFilters = filterSiteIdbyTradeId(siteIds, targetTradeId);
			List<Integer> tradeIdFilters = filterTradeIdbyTradeId(tradeIds, targetTradeId);
			
			siteSeqs = GroupSiteUtil.getDistictSiteSeqs(tradeIdFilters, siteIdFilters, displayType);
		}
		
//		去除一二级对应关系		
		siteSeqs = rollupGroupSiteSeqs(siteSeqs);
		
		List<GroupSiteVO> result = getGroupSiteDetailFromSiteSeqs(siteSeqs);
		
		calculateHeatLength(result);
		
		//sortByHeat(result, 1);
				
		return result;
	}
	
	/**
	 * 使用行业ID过滤网站ID列表
	 * @param siteIdList 网站ID列表
	 * @param targetTradeId 行业ID
	 * @return下午04:20:40 返回过滤后的网站ID
	 */
	private static List<Integer> filterSiteIdbyTradeId(List<Integer> siteIdList, Integer targetTradeId){

		List<Integer> siteIdFilterList = new ArrayList<Integer>();
		
//		必须判空，解决某些行业没有网站对应的情况
		int[] idRange = UnionSiteCache.tradeInfoCache.getSiteRange(targetTradeId);
		if(idRange == null){
			return siteIdFilterList;
		}
		
		for(Integer siteId : siteIdList){
			Integer siteSeq = UnionSiteCache.siteInfoCache.getReverseIndexBySiteId().get(siteId);
			if(siteSeq != null &&
					(siteSeq >= idRange[0] && siteSeq <= idRange[1])){
				siteIdFilterList.add(siteId);
			}
		}
		
		return siteIdFilterList;
	}
	
	/**
	 * 行业过滤，考虑目标为一级行业和二级行业的情况
	 * @param tradeIdList
	 * @param targetTradeId
	 * @return下午03:39:53
	 */
	private static List<Integer> filterTradeIdbyTradeId(List<Integer> tradeIdList, Integer targetTradeId){

		List<Integer> tradeIdFilterList = new ArrayList<Integer>();		
		
		// 过滤行业是一级还是二级行业
		boolean isFirstLevel = Integer.valueOf(0).equals(UnionSiteCache.tradeInfoCache.getSiteTradeLevel().get(targetTradeId));
		Integer parentIdTarget = UnionSiteCache.tradeInfoCache.getSiteTradeLevel().get(targetTradeId);
		
		
		if(isFirstLevel){//一级，则需选择本身，或者下属二级
			Integer parentId = null;
			
			for(Integer tradeId : tradeIdList){
				
				parentId = UnionSiteCache.tradeInfoCache.getSiteTradeLevel().get(tradeId);
				
				if(parentId != null){
					
					if(parentId == 0){//一级
						
						if(targetTradeId.equals(tradeId)){
							tradeIdFilterList.add(tradeId);
						}
						
					}else{
						if(targetTradeId.equals(parentId)){
							tradeIdFilterList.add(tradeId);
						}
					}
					
				}
			}
			
		}else{//二级，本身或者当选择了一级时，返回此二级
			
			if(parentIdTarget == null){ //没有一级
				if(tradeIdList.contains(targetTradeId)){
					tradeIdFilterList.add(targetTradeId);
				}
			}else{
				for(Integer tradeId : tradeIdList){					
					//输入1301，要么tradeid=1301，添加1301，要么tradeid=13，添加1301
					if(targetTradeId.equals(tradeId) || parentIdTarget.equals(tradeId)){
						tradeIdFilterList.add(targetTradeId);
						break;
					}
				}
			}
			
			
			
		}
		
		return tradeIdFilterList;
	}
	
	
	
	/**
	 * 返回去重后的网站序号列表，将网站行业对应的网站与直接选择的网站进行去重
	 * @param groupInfo
	 * @param targetTradeId
	 * @return上午10:09:14
	 */
		
	/**
	 * 去重一二级对应的情况
	 * @param siteIds
	 * @return上午11:15:02
	 */
	private static List<Integer> rollupGroupSiteSeqs(List<Integer> siteSeqs){
		
		if(CollectionUtils.isEmpty(siteSeqs)){
			return new ArrayList<Integer>(0);
		}
		
		List<Integer> selected = new ArrayList<Integer>(siteSeqs.size());		
		Set<Integer> tmpSiteSeqSet = new HashSet<Integer>(siteSeqs.size());
//		去除同域名的情况
		for (Integer siteSeq : siteSeqs){
			tmpSiteSeqSet.add(siteSeq);
		}
		
		for(Integer siteSeq : siteSeqs){
			
			Integer parentSeq = getParentSiteSeq(siteSeq);		
			
			/**
			 * 同时一级域名，二级域名选择了
			 */
			if(siteSeq.equals(parentSeq)){
				selected.add(siteSeq);
			}else{
				if(tmpSiteSeqSet.contains(parentSeq)){
					//do nothing
				}else{
					selected.add(siteSeq);
				}
			}
		}
		
		return selected;
	}
	
	/**
	 * 返回顶级域名的序号，如果没有顶级，则返回自己，否则返回父节点
	 * @param siteSeq
	 * @return上午11:17:36
	 */
	private static Integer getParentSiteSeq(Integer siteSeq){
		if(siteSeq == null){
			return null;
		}
		
		BDSiteInfo info = UnionSiteCache.siteInfoCache.getSiteInfoList().get(siteSeq);
		if(info == null){
			return null;
		}
		Integer parentId = info.getParentid();
		
		if(parentId == null || parentId == 0){
			return siteSeq;
		}
		
		Integer index = UnionSiteCache.siteInfoCache.getReverseIndexBySiteId().get(parentId);
		return index;
	}
	
	/**
	 * 根据网站序号列表返回对应的网站热度详细信息
	 * @param siteIds
	 * @return上午10:55:11
	 */
	private static List<GroupSiteVO> getGroupSiteDetailFromSiteSeqs(List<Integer> siteSeqs){
		
		if(CollectionUtils.isEmpty(siteSeqs)){
			return new ArrayList<GroupSiteVO>(0);
		}
		
		List<GroupSiteVO> result = new ArrayList<GroupSiteVO>(siteSeqs.size());
		
//		找出等级2的值域，最小值和中位数
		int min = Long.valueOf(Math.round(UnionSiteCache.siteInfoCache.getMinRateCompete()*100)).intValue();
		int median = Long.valueOf(Math.round(UnionSiteCache.siteInfoCache.getMedianRateCompete()*100)).intValue();
		
		int cmpSizeInHeated = 0;
		
		for(Integer siteSeq : siteSeqs){
			BDSiteInfo info = UnionSiteCache.siteInfoCache.getSiteInfoList().get(siteSeq); 
			if(info == null){
				continue;
			}
			GroupSiteVO vo = new GroupSiteVO();
			vo.setSiteId(info.getSiteid());
			vo.setSiteUrl(info.getSiteurl());
			vo.setFirstTradeId(info.getFirsttradeid());
			vo.setFirstTradeName(UnionSiteCache.tradeInfoCache.getSiteTradeNameList().get(vo.getFirstTradeId()));
			vo.setSecondTradeId(info.getSecondtradeid());
			vo.setSecondTradeName(UnionSiteCache.tradeInfoCache.getSiteTradeNameList().get(vo.getSecondTradeId()));
			
			CmpLevelVO heat = new CmpLevelVO(info.getSiteid(), info.getCmplevel());
			heat.setRateCmp(info.getRatecmp());
			heat.setScoreCmp(info.getScorecmp());
			if(heat.getCmpLevel().equals(CproGroupConstant.CMP_LEVEL_HEATED)){
				cmpSizeInHeated = Long.valueOf(Math.round(heat.getRateCmp()*100)).intValue();
				heat.setCmpSize(cmpSizeInHeated);
			}else if(heat.getCmpLevel().equals(CproGroupConstant.CMP_LEVEL_MEDIAN)){
				heat.setCmpSize(median);
			}else if(heat.getCmpLevel().equals(CproGroupConstant.CMP_LEVEL_LOW)){
				heat.setCmpSize(min);
			}
			
			vo.setHeat(heat);
			
			result.add(vo);
		}
		
		return result;
	}
	
	
	/**
	 * 
	 * @param list
	 * @return 
	 * @return上午11:36:45
	 */
	private static void calculateHeatLength(List<GroupSiteVO> list){
	
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		
		//只需要对热度为2和4的两个等级做处理
		//找出这两个等级的记录，并按升序排列
		List<GroupSiteVO> list2 = new ArrayList<GroupSiteVO>();
		List<GroupSiteVO> list4 = new ArrayList<GroupSiteVO>();
		
		for(GroupSiteVO vo : list){
			CmpLevelVO heat = vo.getHeat();
			if(heat != null){
				if(heat.getCmpLevel().equals(CproGroupConstant.CMP_LEVEL_MODERATE)){
					list2.add(vo);
				}else if(heat.getCmpLevel().equals(CproGroupConstant.CMP_LEVEL_HIGH)){
					list4.add(vo);
				}
			}			
		}
		
		sortByHeat(list2, 1);
		sortByHeat(list4, 1);

		//找出等级2的值域
		int min = Long.valueOf(Math.round(UnionSiteCache.siteInfoCache.getMinRateCompete()*100)).intValue();
		int median = Long.valueOf(Math.round(UnionSiteCache.siteInfoCache.getMedianRateCompete()*100)).intValue();
		int max = 90;
		
		getRandomHeatSize(list2, min, median);
		getRandomHeatSize(list4, median, max);
		
		return;
	}
	/**
	 * 
	 * @param list
	 * @param min
	 * @param max
	 * @return上午11:38:21
	 */
	private static void getRandomHeatSize(List<GroupSiteVO> list, int min, int max){
		
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		
		int n = list.size();
		double step = (max - min) * 1.0 / (n+1); 	
			//步伐取整数，体现不出来，和PM确认
		for(int i = 0; i < n; i ++){
			list.get(i).getHeat().setCmpSize(Long.valueOf(Math.round(min + (i+1) * step)).intValue());
		}

		return;
	}
	
	/**
	 * 根据url进行排序
	 * @param list
	 * @param order上午11:37:22
	 */
	public static void sortBySiteUrl(final List<GroupSiteVO> list, final int order){
		Collections.sort(list, new Comparator<GroupSiteVO>(){

			public int compare(GroupSiteVO o1, GroupSiteVO o2) {
				if(o1 == null) return 1;
				if(o2 == null) return -1;
				
				Comparator<String> cmp = new SiteUrlComparator();
				return cmp.compare(o1.getSiteUrl(), o2.getSiteUrl()) * order;
			}
		});
	}
	
	/**
	 * 根据热度进行排序
	 * @param list
	 * @param order上午11:37:25
	 */
	private static void sortByHeat(final List<GroupSiteVO> list, final int order){
		Collections.sort(list, new Comparator<GroupSiteVO>(){

			public int compare(GroupSiteVO o1, GroupSiteVO o2) {
				if(o1 == null) return 1;
				if(o2 == null) return -1;
				
				Comparator<CmpLevelVO> cmp = new SiteCmpLevelComparator();
				return cmp.compare(o1.getHeat(), o2.getHeat()) * order;
			}
		});
	}
	
	/**
	 * 根据热度进行排序
	 * @param list
	 * @param order上午11:37:25
	 */
	public static void sortByCmpSize(final List<GroupSiteVO> list, final int order){
		Collections.sort(list, new Comparator<GroupSiteVO>(){

			public int compare(GroupSiteVO o1, GroupSiteVO o2) {
				if(o1 == null) return 1;
				if(o2 == null) return -1;
				
				Comparator<CmpLevelVO> cmp = new SiteCmpSizeComparator();
				return cmp.compare(o1.getHeat(), o2.getHeat()) * order;
			}
		});
	}
	
	
	//如果用户不是行业白名单用户，则过滤掉用户hack进来的请求
	private static TradeSelectionVo filterByWhiteList(Integer userId, TradeSelectionVo input){
		TradeSelectionVo output = new TradeSelectionVo();
		output.setFirstTrades(new ArrayList<Integer>());
		output.setSecondTradeMap(new HashMap<Integer, List<Integer>>());

		if (input != null && input.getFirstTrades() != null) {
			output.getFirstTrades().addAll(GroupSiteUtil.filterByWhiteListTrades(userId, input.getFirstTrades()));
		}
		if (input !=null && input.getSecondTradeMap()!=null) {
			for (Integer firstTradeId : output.getFirstTrades()) {
				List<Integer> secondTradeList = input.getSecondTradeMap().get(firstTradeId);
				if (CollectionUtils.isNotEmpty(secondTradeList)) {
					output.getSecondTradeMap().put(firstTradeId, GroupSiteUtil.filterByWhiteListTrades(userId, secondTradeList));
				}
			}
		}

		return output;
	}
	
	//获取常规一级行业的数量
	//输入必须为一级行业
	private static Integer getRegularFirstTradeCount(List<Integer> firstTradeList){
		if (CollectionUtils.isEmpty(firstTradeList)) {
			return 0;
		}
		
		List<Integer> tmpIdList = new ArrayList<Integer>(firstTradeList.size());
		tmpIdList.addAll(firstTradeList);
		tmpIdList.removeAll(WhiteListCache.baiduTrades.getList());

		return tmpIdList.size();
	}
	
	public static SiteSumInfo genSiteInfo(Integer userId, final TradeSelectionVo selectedTradeVo , final List<Integer> selectedSideIds, final int groupType){
		
		//依据白名单进行网站过滤
		List<Integer> filteredSiteIds = new ArrayList<Integer>();
		if (selectedSideIds != null) {
			filteredSiteIds.addAll(GroupSiteUtil.filterByWhiteListSites(userId,selectedSideIds));
			Collections.sort(filteredSiteIds);
		}
		
		//依据白名单进行行业过滤
		TradeSelectionVo filteredTradeVo = filterByWhiteList(userId, selectedTradeVo);
		
		
		List<Integer> resultFirstTradeList = new ArrayList<Integer>();
		List<Integer> resultSecondTradeIdList = new ArrayList<Integer>();
		
		for (Integer firstTradeId : filteredTradeVo.getFirstTrades()) {

			List<Integer> secondTradeList = filteredTradeVo.getSecondTradeMap().get(firstTradeId);
			
			if (CollectionUtils.isEmpty(secondTradeList)//只选择了一级行业，或者全选了二级行业，则添加一级行业ID
					||
				UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildrenSum().get(firstTradeId) != null
					&&
				UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildrenSum().get(firstTradeId) == secondTradeList.size()
			) {
				resultFirstTradeList.add(firstTradeId);
				
			} else {//否则添加二级行业
				resultSecondTradeIdList.addAll(secondTradeList);
			}
			
		}

		/******************************* 不再需要补充一级“其他” modify by guojichun since 2.0.0 **********************/
		/*
		//如果用户选择了所有的常规一级行业，则需要补足一级的“其他”
		int regularFirstTradeSize = getRegularFirstTradeCount(resultFirstTradeList);
		if(CollectionUtils.isEmpty(resultSecondTradeIdList) && regularFirstTradeSize == UnionSiteCache.tradeInfoCache.getFirstTradeNum()){
			resultFirstTradeList.addAll(UnionSiteCache.tradeInfoCache.getOtherFirstTrade());
		}*/
		
		List<Integer> allTradeList = new ArrayList<Integer>();
		allTradeList.addAll(resultFirstTradeList);
		allTradeList.addAll(resultSecondTradeIdList);
		
		
		//传入的行业id已经是按照一级行业，二级行业排序的了
		int displayType = SiteConstant.getDisplayTypeFromGroupType(groupType);
		
		List<Integer> distictSiteSeqs = GroupSiteUtil.getDistictSiteSeqs(allTradeList, filteredSiteIds, displayType);	
		
		SiteSumInfo siteSumInfo = new SiteSumInfo();
		siteSumInfo.setSiteTradeList(allTradeList);
		siteSumInfo.setSiteSum(distictSiteSeqs.size());
		siteSumInfo.setCmpLevel(genGroupCmpLevel(distictSiteSeqs));
		siteSumInfo.setSiteList(filteredSiteIds);
		
		return siteSumInfo;
	}
	
	public static List<Integer> genRegionList(List<Integer> firstRegionIds, Map<Integer, List<Integer>> secondRegionMap){
		if(CollectionUtils.isNotEmpty(firstRegionIds)){
			List<Integer> ret = new ArrayList<Integer>();
			for (Integer firstRegionId : firstRegionIds) {
				List<Integer> secondRegionList = null;
				if (secondRegionMap != null) {
					secondRegionList = secondRegionMap.get(firstRegionId);
				}
				
				if (CollectionUtils.isEmpty(secondRegionList)//只选择了一级地域
						||
					UnionSiteCache.regCache.getFirstRegChildrenSum().get(firstRegionId) != null //或者选择了所有的二级地域
						&& 
					UnionSiteCache.regCache.getFirstRegChildrenSum().get(firstRegionId) == secondRegionList.size())
				{
					ret.add(firstRegionId);
					
				}else{					
					ret.addAll(secondRegionList);
				}
			}
			return ret;
		}
		return new ArrayList<Integer>(0);
	}
	
	public static RegionSumInfo genRegionInfo(RegionSelectionVo selectedRegionVo){

		int regSum = 0;
		StringBuilder regBuilder = new StringBuilder();

		if(selectedRegionVo != null && selectedRegionVo.getFirstRegions() != null) {
			
			regSum = selectedRegionVo.getFirstRegions().size();
			
			for (Integer firstRegionId : selectedRegionVo.getFirstRegions()) {
				List<Integer> secondRegionList = null;
				if (selectedRegionVo.getSecondRegionMap() != null) {
					secondRegionList = selectedRegionVo.getSecondRegionMap().get(firstRegionId);
				}
				
				if (CollectionUtils.isEmpty(secondRegionList)//只选择了一级地域
						||
					UnionSiteCache.regCache.getFirstRegChildrenSum().get(firstRegionId) != null //或者选择了所有的二级地域
						&& 
					UnionSiteCache.regCache.getFirstRegChildrenSum().get(firstRegionId) == secondRegionList.size())
				{
					regBuilder.append(firstRegionId).append(CproGroupConstant.FIELD_SEPERATOR);
					
				}else{
					
					for(Integer secRegId : secondRegionList){
						regBuilder.append(secRegId).append(CproGroupConstant.FIELD_SEPERATOR);
					}
				}
			}
		}
		
		
		RegionSumInfo resultInfo = new RegionSumInfo();
		
		resultInfo.setRegSum(regSum);
		resultInfo.setRegStr(regBuilder.toString());
		
		return resultInfo;		
	
	}
	
	/**
	 * removeWWWforSite: 对输入站点进行空串过滤，并且去除"www."
	 * @version GroupSiteConfigAction
	 * @author genglei01
	 * @date 2012-5-30
	 */
	public static List<String> removeWWWforSite(List<String> sites){
		List<String> result = new ArrayList<String>();
		if (CollectionUtils.isEmpty(sites)) {
			return result;
		}
		
		for (String site : sites) {
			if (StringUtils.isEmpty(site)) {
				continue;
			}
			if (site.startsWith("www.")) {
				result.add(site.substring(4, site.length()));
			} else {
				result.add(site);
			}
		}
		return result;
	}
}
