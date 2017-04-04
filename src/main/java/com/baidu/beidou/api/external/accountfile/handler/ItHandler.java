package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.ItVo;
import com.baidu.beidou.cprogroup.bo.BaseCproGroupIT;
import com.baidu.beidou.cprogroup.bo.CproGroupIT;
import com.baidu.beidou.cprogroup.bo.CproGroupITExclude;
import com.baidu.beidou.cprogroup.bo.CustomInterest;
import com.baidu.beidou.cprogroup.bo.GroupInterestPrice;
import com.baidu.beidou.cprogroup.service.CproGroupITMgr;
import com.baidu.beidou.cprogroup.service.CustomITMgr;
import com.baidu.beidou.cprogroup.service.GroupITPriceMgr;
import com.baidu.beidou.cprogroup.service.InterestMgr;
import com.baidu.beidou.cprogroup.util.ITUtils;
import com.baidu.beidou.cprogroup.vo.CustomInterestVo;
import com.baidu.beidou.cprogroup.vo.InterestCacheObject;
import com.baidu.beidou.cprogroup.vo.InterestVo;
import com.baidu.beidou.util.BeanMapperProxy;

/**
 * 
 * ClassName: ItHandler  <br>
 * Function: 兴趣设置
 *
 * @author zhangxu
 * @date Jun 8, 2012
 */
public class ItHandler extends Handler {

	private static final Log log = LogFactory.getLog(ItHandler.class);

	private static final Integer IS_IT_EXCLUDE = 1;  //排除兴趣标志位
	private static final Integer IS_NOT_IT_EXCLUDE = 0;  
	
	private static final Integer IS_CUSTOMINTEREST = 1;  //是否为兴趣组合标志位
	private static final Integer IS_NOT_CUSTOMINTEREST = 0; 

	private CproGroupITMgr cproGroupITMgr;
	
	private InterestMgr interestMgr;

	private CustomITMgr customITMgr;
	
	private GroupITPriceMgr groupITPriceMgr;
	
	/**
	 * 生成兴趣VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();
		
		//获取所有的兴趣点
		List<InterestCacheObject> interests = interestMgr.getInterestCacheList();
		List<InterestVo> interestVoList = load2SimpleInterestVoList(interests);
		List<CustomInterestVo> customInterests = getCustomInterestsByUserId(userId);
		
		List<CproGroupIT> groupITList = new ArrayList<CproGroupIT>();
		List<CproGroupITExclude> groupITExcList = new ArrayList<CproGroupITExclude>();
		List<GroupInterestPrice> groupInterestPriceList = new ArrayList<GroupInterestPrice>();
		
		//将两者做为缓存
		Map<Integer, InterestVo> interestVoMap = trans2InterestMap(interestVoList);
		Map<Integer, CustomInterestVo> customInterestMap = ITUtils.trans2Map(customInterests, CustomInterestVo.class, "getId");
		
		for (Integer groupId : groupIds) {
			groupITList.addAll(cproGroupITMgr.findGroupITList(groupId));
			groupITExcList.addAll(cproGroupITMgr.findGroupITExcludeList(groupId));
			groupInterestPriceList.addAll(groupITPriceMgr.findInterestPriceByGroupId(groupId));
		}

		if (CollectionUtils.isNotEmpty(groupITList) || CollectionUtils.isNotEmpty(groupITExcList) ) {
			processGroupIt(list, groupITList, groupInterestPriceList, interestVoMap, customInterestMap, false);
			processGroupIt(list, groupITExcList, null, interestVoMap, customInterestMap, true);
		}

		return list;
	}
	
	/**
	 * 将数据库中cprogroupit和cprogroupit_exclude转换成API的VO，并添加进list<VO>中 <br>
	 * @param list 要添加对象的List<VO>
	 * @param groupITList 兴趣与GroupId的关联关系列表
	 * @param groupInterestPriceList 兴趣出价
	 * @param interestVoMap 兴趣点map
	 * @param customInterestMap 兴趣组合map
	 * @param isExclude 是否为排除兴趣
	 * @return 
	 */
	private void processGroupIt(List<AbstractVo> list, List<? extends BaseCproGroupIT> groupITList, List<GroupInterestPrice> groupInterestPriceList, Map<Integer, InterestVo> interestVoMap, Map<Integer, CustomInterestVo> customInterestMap, boolean isExclude){
		Mapper mapper = BeanMapperProxy.getMapper();
		Map<Integer, Integer> itPriceMap = new HashMap<Integer, Integer>();
		if (groupInterestPriceList != null) {
			for (GroupInterestPrice groupInterestPrice : groupInterestPriceList) {
				itPriceMap.put(groupInterestPrice.getIid(), groupInterestPrice.getPrice());
			}
		}
		for (BaseCproGroupIT cprogroupIt : groupITList) {
			ItVo itVo = mapper.map(cprogroupIt, ItVo.class);
			if(isExclude){
				itVo.setIsExclude(IS_IT_EXCLUDE);
			} else {
				itVo.setIsExclude(IS_NOT_IT_EXCLUDE);  //位取反
			}
			if(cprogroupIt.isInterest()){
				itVo.setType(IS_NOT_CUSTOMINTEREST);
				InterestVo interestVo = interestVoMap.get(itVo.getItId());
				if(interestVo == null){
					log.error("Can not find itid=" + itVo.getItId());
					continue;
				}
				itVo.setItName(interestVo.getName());
				if (itPriceMap.containsKey(itVo.getItId())) {
					itVo.setPrice(itPriceMap.get(itVo.getItId()));
				}
				list.add(itVo);
			} else {
				itVo.setType(IS_CUSTOMINTEREST);
				CustomInterestVo customInterestVo = customInterestMap.get(itVo.getItId());
				if(customInterestVo == null){
					log.error("Can not find itid=" + itVo.getItId());
					continue;
				}
				itVo.setItName(customInterestVo.getName());
				if (itPriceMap.containsKey(itVo.getItId())) {
					itVo.setPrice(itPriceMap.get(itVo.getItId()));
				}
				list.add(itVo);
			}
		}
	}
	
	
	/**
	 * 将兴趣点的缓存对象列表加载为VO对象类表，不做任何操作
	 * @param interests
	 * @return
	 */
	public final static List<InterestVo> load2SimpleInterestVoList(
			List<InterestCacheObject> interests) {
		List<InterestVo> voList = new LinkedList<InterestVo>();
		for(InterestCacheObject o : interests){
			voList.add(new InterestVo(o));
		}
		return voList;
	}

	public List<CustomInterestVo> getCustomInterestsByUserId(int userId) {
		List<CustomInterest> customInterests = customITMgr.getCustomItListByUserId(userId);
		List<CustomInterestVo> customInterestVoList = new LinkedList<CustomInterestVo>();
		for(CustomInterest o : customInterests){
			customInterestVoList.add(new CustomInterestVo(o));
		}
		return customInterestVoList;
	}
	
	private Map<Integer, InterestVo> trans2InterestMap(
			List<InterestVo> interestVoList) {
		Map<Integer, InterestVo> voMap = new HashMap<Integer, InterestVo>();
		for(InterestVo vo : interestVoList){
			voMap.put(vo.getId(), vo);
			List<InterestVo> children = vo.getChildren();
			if(children != null && children.size() > 0){
				voMap.putAll(trans2InterestMap(children));
			}
		}
		return voMap;
	}

	public CproGroupITMgr getCproGroupITMgr() {
		return cproGroupITMgr;
	}

	public void setCproGroupITMgr(CproGroupITMgr cproGroupITMgr) {
		this.cproGroupITMgr = cproGroupITMgr;
	}

	public InterestMgr getInterestMgr() {
		return interestMgr;
	}

	public void setInterestMgr(InterestMgr interestMgr) {
		this.interestMgr = interestMgr;
	}

	public CustomITMgr getCustomITMgr() {
		return customITMgr;
	}

	public void setCustomITMgr(CustomITMgr customITMgr) {
		this.customITMgr = customITMgr;
	}

	public GroupITPriceMgr getGroupITPriceMgr() {
		return groupITPriceMgr;
	}

	public void setGroupITPriceMgr(GroupITPriceMgr groupITPriceMgr) {
		this.groupITPriceMgr = groupITPriceMgr;
	}
	
	
}
