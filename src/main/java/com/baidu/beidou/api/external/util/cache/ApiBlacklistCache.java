package com.baidu.beidou.api.external.util.cache;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * ClassName: ApiBlacklistCache  <br>
 * Function: api黑名单用户缓存
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public class ApiBlacklistCache {

	/*
	 * api黑名单用户缓存集合
	 */
	public static Set<Integer> BLACKLIST_USERID_SET = new HashSet<Integer>();
	
}
