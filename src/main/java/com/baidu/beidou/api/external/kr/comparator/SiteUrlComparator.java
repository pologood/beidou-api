package com.baidu.beidou.api.external.kr.comparator;

import java.util.Comparator;

/**
 * @author zhuqian
 *
 */
public class SiteUrlComparator implements Comparator<String> {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(String o1, String o2) {
		
		if(o1 == null) return 1;
		if(o2 == null) return -1;
		
		return o1.compareTo(o2);
	}

}
