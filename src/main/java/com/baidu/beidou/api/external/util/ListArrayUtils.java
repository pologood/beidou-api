package com.baidu.beidou.api.external.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class ListArrayUtils {

	public static List<Integer> asList(int[] ids) {
		if (ids == null) {
			return new ArrayList<Integer>();
		}
		List<Integer> result = new ArrayList<Integer>(ids.length);
		for (int index = 0; index < ids.length; index++) {
			result.add(ids[index]);
		}
		return result;
	}

	public static List<Integer> transLongList(Collection<Long> sourceCollection) {
		if (CollectionUtils.isEmpty(sourceCollection)) {
			return Collections.emptyList();
		}
		List<Integer> targetList = new ArrayList<Integer>(sourceCollection.size());
		for(Long item: sourceCollection){
			targetList.add(item.intValue());
		}
		return targetList;
	}
	public static List<Integer> asList(Set<Integer> ids){
		if(ids == null){
			return new ArrayList<Integer>();
		}
		List<Integer> result = new ArrayList<Integer>(ids.size());
		for (Integer id : ids) {
			result.add(id);
		}
		return result;
	}
	
}
