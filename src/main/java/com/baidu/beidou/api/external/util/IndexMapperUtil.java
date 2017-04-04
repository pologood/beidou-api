package com.baidu.beidou.api.external.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baidu.beidou.api.external.util.vo.IndexMapper;

public class IndexMapperUtil {

	/**
	 * 填充推广组设置数据与设置下标
	 * @param groupIdKey 推广组id，作为map的key
	 * @param data 填充的数据
	 * @param index 传入数据在参数列表中的索引
	 * @param dataMap 推广组数据字典
	 * @param indexMap 推广组数据在原始参数中的下标映射字典
	 */
	public static <T> void mapIndex(Integer groupIdKey,
			T data,
			int index, 
			Map<Integer, List<T>> dataMap, 
			Map<Integer, IndexMapper> indexMap) {
		
		// 添加推广组数据
		List<T> list = dataMap.get(groupIdKey);
		if (list == null) {
			List<T> value = new ArrayList<T>();
			dataMap.put(groupIdKey, value);
		}
		dataMap.get(groupIdKey).add(data);
		
		// 设置索引
		IndexMapper indexMapper = indexMap.get(groupIdKey);
		if (indexMapper == null) {
			IndexMapper mapper = new IndexMapper();
			indexMap.put(groupIdKey, mapper);
		}
		indexMap.get(groupIdKey).setIndex(index);
	}
	
}
