package com.baidu.beidou.api.external.util;

import org.apache.commons.lang.RandomStringUtils;

import com.baidu.beidou.util.MD5;

/**
 * 
 * ClassName: ReportIdGenerator
 * Function: 生成reportId，32位字母、数字组成到串，由md5过来
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public class UUIDGenerator {
	
	private static final int RANDOM_SIZE = 16;
	
	/**
	 * 生成reportId，可以传入多个字符串作为md5哈希的key
	 */
	public static String get(String... member){
		String randomStr = RandomStringUtils.randomAlphanumeric(RANDOM_SIZE);
		String md5srcStr = randomStr;
		for(String m:member){
			md5srcStr += m;
		}
		return MD5.getMd5(md5srcStr);
	}

}
