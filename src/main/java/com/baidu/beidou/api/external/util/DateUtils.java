package com.baidu.beidou.api.external.util;

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

/**
 * 
 * ClassName: DateUtils  <br>
 * Function: 格式化日期工具类
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class DateUtils {

	public static final FastDateFormat ISO_DATE_FORMAT = FastDateFormat.getInstance("yyyyMMdd"); 
	
	public static String toISOString(Date date){
		if(date == null){
			return "";
		}
		return ISO_DATE_FORMAT.format(date);
	}
	
}
