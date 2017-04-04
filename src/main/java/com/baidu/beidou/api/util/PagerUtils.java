package com.baidu.beidou.api.util;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class PagerUtils {
	
	public static <T extends Object> List<T> getPage(List<T> list, int page, int pageSize) {
		if (CollectionUtils.isEmpty(list) || page < 0 || pageSize <= 0) {
			return Collections.emptyList();
		}
		
		int pageNum = (list.size() / pageSize) + 1;
		if ((list.size() % pageSize) == 0) {
			pageNum -= 1;
		}
		
		int from = page * pageSize;
		if (page >= pageNum) {
			from = 0;
		}
		int to = from + pageSize;
		if (to > list.size()) {
			to = list.size();
		}
		
		return list.subList(from, to);
	}
	
}
