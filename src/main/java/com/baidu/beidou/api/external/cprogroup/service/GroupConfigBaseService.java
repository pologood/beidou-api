package com.baidu.beidou.api.external.cprogroup.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.baidu.beidou.cprogroup.bo.GroupIpFilter;
import com.baidu.beidou.cprogroup.bo.GroupSiteFilter;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.service.CproGroupVTMgr;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.cprogroup.vo.CproGroupVTVo;

public class GroupConfigBaseService {

	private static final Log log = LogFactory.getLog(GroupConfigBaseService.class);
	
	protected Map<Long, CproGroupVTVo> makeVTMap(Long[] pidArray, boolean isInclude) {
		if (ArrayUtils.isEmpty(pidArray)) {
			return Collections.emptyMap();
		}
		Map<Long, CproGroupVTVo> vtVoMap = new HashMap<Long, CproGroupVTVo>();
		for (Long pid : pidArray) {
			CproGroupVTVo vtVo = new CproGroupVTVo(pid,
					(isInclude ? CproGroupConstant.GROUP_VT_INCLUDE_CROWD
							: CproGroupConstant.GROUP_VT_EXCLUDE_CROWD));
			vtVoMap.put(pid, vtVo);
		}
		return vtVoMap;
	}
	
	protected Map<Long, CproGroupVTVo> makeVTMap(List<Long> pidArray, boolean isInclude) {
		if (CollectionUtils.isEmpty(pidArray)) {
			return Collections.emptyMap();
		}
		Map<Long, CproGroupVTVo> vtVoMap = new HashMap<Long, CproGroupVTVo>();
		for (Long pid : pidArray) {
			CproGroupVTVo vtVo = new CproGroupVTVo(pid,
					(isInclude ? CproGroupConstant.GROUP_VT_INCLUDE_CROWD
							: CproGroupConstant.GROUP_VT_EXCLUDE_CROWD));
			vtVoMap.put(pid, vtVo);
		}
		return vtVoMap;
	}
	
	protected Map<Long, CproGroupVTVo> makeVTMap(List<CproGroupVTVo> sourceVTList, Integer groupId) {
		if (CollectionUtils.isEmpty(sourceVTList)) {
			return Collections.emptyMap();
		}
		Map<Long, CproGroupVTVo> vtVoMap = new HashMap<Long, CproGroupVTVo>(sourceVTList.size());
		for (CproGroupVTVo sourceVTVo : sourceVTList) {
			if (vtVoMap.containsKey(sourceVTVo.getId())) {
				log.error("a cprogroupvt has combined two same vtpeople, pid=" + sourceVTVo.getId()
						+ ", groupId=" + groupId);
				continue;
			}
			vtVoMap.put(sourceVTVo.getId(), sourceVTVo);
		}
		return vtVoMap;
	}
	
	protected String toCproGroupVTVoString(Collection<CproGroupVTVo> vtVoList) {
		String vtVoString = String.valueOf("");
		if (CollectionUtils.isEmpty(vtVoList)) {
			return vtVoString;
		}
		try {
			vtVoString = JSONUtil.serialize(vtVoList);
		} catch (JSONException e) {
			log.error("parse vtVoList to json error:" + e.getMessage(), e.getCause());
		}
		return vtVoString;
	}
	
	protected void initVtPeopleName(List<CproGroupVTVo> vtPeopleList, Integer userId,
			Integer groupId, VtPeopleMgr vtPeopleMgr, CproGroupVTMgr cproGroupVTMgr) {
		if (CollectionUtils.isEmpty(vtPeopleList)) {
			return;
		}
		List<Long> pIdLIst = new ArrayList<Long>();
		for (CproGroupVTVo vtVo : vtPeopleList) {
			pIdLIst.add(vtVo.getId());
		}
		List<CproGroupVTVo> initedNameVtList = vtPeopleMgr.getVtPeopleListByIds(userId, pIdLIst);
		Map<Long, CproGroupVTVo> initedNameVtMap = cproGroupVTMgr.makeVTMap(initedNameVtList,
				groupId);
		Map<Long, CproGroupVTVo> sourceVTRelationMap = cproGroupVTMgr.makeVTMap(vtPeopleList,
				groupId);
		for (Long sourcePid : sourceVTRelationMap.keySet()) {
			if (!initedNameVtMap.containsKey(sourcePid)) {
				log.error("can't find vtpeopleInfo,pid=" + sourcePid);
				continue;
			}
			sourceVTRelationMap.get(sourcePid).setName(initedNameVtMap.get(sourcePid).getName());
		}
	}
	
	/**
	 * 修改vt的方法
	 */
	protected static final String VALUE_BEFORE = "before";
	protected static final String VALUE_AFTER = "after";
	
	protected Map<String, List<String>> getChangedPeopleNameMap(
			List<CproGroupVTVo> newVTRelationList, List<CproGroupVTVo> oldVTRelationList,
			Integer groupId, Integer relateType) {
		Map<String, List<String>> changedNameMap = new LinkedHashMap<String, List<String>>();
		Map<Long, CproGroupVTVo> newVtMap = null;
		Map<Long, CproGroupVTVo> oldVtMap = null;
		if (CollectionUtils.isEmpty(newVTRelationList)
				&& CollectionUtils.isEmpty(oldVTRelationList)) {
			return null;
		}
		if (CollectionUtils.isEmpty(oldVTRelationList)) {
			newVtMap = getRealteTypeVtMap(newVTRelationList, relateType, groupId);
			changedNameMap.put(VALUE_BEFORE, null);
			changedNameMap.put(VALUE_AFTER, getVtPeopleNameList(newVtMap));
			return changedNameMap;
		}
		if (CollectionUtils.isEmpty(newVTRelationList)) {
			oldVtMap = getRealteTypeVtMap(oldVTRelationList, relateType, groupId);
			changedNameMap.put(VALUE_BEFORE, getVtPeopleNameList(oldVtMap));
			changedNameMap.put(VALUE_AFTER, null);
			return changedNameMap;
		}
		newVtMap = getRealteTypeVtMap(newVTRelationList, relateType, groupId);
		oldVtMap = getRealteTypeVtMap(oldVTRelationList, relateType, groupId);
		boolean notChange = isVtVoMapEqual(newVtMap, oldVtMap);
		if (notChange) {
			return null;
		}
		changedNameMap.put(VALUE_BEFORE, getVtPeopleNameList(oldVtMap));
		changedNameMap.put(VALUE_AFTER, getVtPeopleNameList(newVtMap));
		return changedNameMap;
	}
	
	private Map<Long, CproGroupVTVo> getRealteTypeVtMap(List<CproGroupVTVo> vtVoList,
			Integer relateType, Integer groupId) {
		Map<Long, CproGroupVTVo> relateTypeMap = new HashMap<Long, CproGroupVTVo>();
		for (CproGroupVTVo vtVo : vtVoList) {
			if (vtVo.getType() == relateType.intValue()) {
				if (relateTypeMap.containsKey(vtVo.getId())) {
					log.error("a cprogroupvt has combined two same vtpeople, pid=" + vtVo.getId()
							+ ", groupId=" + groupId);
					continue;
				}
				relateTypeMap.put(Long.valueOf(vtVo.getId()), vtVo);
			}
		}
		return relateTypeMap;
	}
	
	private boolean isVtVoMapEqual(Map<Long, CproGroupVTVo> newVtPeopleMap,
			Map<Long, CproGroupVTVo> oldVTRelationMap) {
		if (MapUtils.isEmpty(newVtPeopleMap) && MapUtils.isEmpty(oldVTRelationMap)) {
			return true;
		}
		if (MapUtils.isEmpty(newVtPeopleMap) || MapUtils.isEmpty(oldVTRelationMap)) {
			return false;
		}
		if (newVtPeopleMap.size() != oldVTRelationMap.size()) {
			return false;
		}
		for (Long pId : newVtPeopleMap.keySet()) {
			if (!oldVTRelationMap.containsKey(pId)) {
				return false;
			}
		}
		return true;
	}

	private List<String> getVtPeopleNameList(Map<Long, CproGroupVTVo> vtVoMap) {
		List<String> vtPeopleNameList = new ArrayList<String>();
		if (MapUtils.isEmpty(vtVoMap)) {
			return vtPeopleNameList;
		}
		for (CproGroupVTVo vtVo : vtVoMap.values()) {
			vtPeopleNameList.add(vtVo.getName());
		}
		return vtPeopleNameList;
	}
	

	/**
	 * 根据推广组id, 获得该推广组的过滤IP列表(仅包含ip字符串，用于历史操作查询)
	 * 
	 * @author zhangxu
	 * @return
	 */
	public List<String> findExcludeIpStringList(List<GroupIpFilter> ipFilterList) {
		if (ipFilterList == null) {
			return new ArrayList<String>(0);
		} else {
			List<String> stringList = new ArrayList<String>(ipFilterList.size());
			for (GroupIpFilter ipfilter : ipFilterList) {
				stringList.add(ipfilter.getIp());
			}
			return stringList;
		}
	}
	
	/**
	 * 根据推广组id, 获得该推广组的过滤网站列表(仅包含网站url字符串，用于历史操作查询)
	 * @author zhangxu
	 * @return
	 */
	public List<String>  findExcludeSiteStringList(List<GroupSiteFilter> siteFilterList){
		if (siteFilterList == null){
			return new ArrayList<String>(0);
		} else {
			List<String> stringList = new ArrayList<String>(siteFilterList.size()); 
			for (GroupSiteFilter sitefilter : siteFilterList){
				stringList.add(sitefilter.getSite());
			}
			return stringList;
		}
	}
	
	
}
