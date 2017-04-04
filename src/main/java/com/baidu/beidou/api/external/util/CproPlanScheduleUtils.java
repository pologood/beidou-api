package com.baidu.beidou.api.external.util;

import org.apache.commons.lang.StringUtils;

import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.bo.TmpCproPlan;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;

/**
 * 推广计划投放日程字符串到整数的转换工具类
 * @author guojichun
 * @version 2.0.0
 *
 */
public class CproPlanScheduleUtils {

	
	/**
	 * 组建页面显示使用的字符串scheme
	 * 
	 * @param cproPlan
	 * @return
	 */
	public static String formateScheme(CproPlan cproPlan) {

		Integer mondayscheme = cproPlan.getMondayScheme();
		Integer tuesdayscheme = cproPlan.getTuesdayScheme();
		Integer wednesdayscheme = cproPlan.getWednesdayScheme();
		Integer thursdayscheme = cproPlan.getThursdayScheme();
		Integer fridayscheme = cproPlan.getFridayScheme();
		Integer saturdayscheme = cproPlan.getSaturdayScheme();
		Integer sundayscheme = cproPlan.getSundayScheme();

		StringBuilder builder = new StringBuilder();
		// 周一在第一位
		if (mondayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(mondayscheme));
		}

		if (tuesdayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(tuesdayscheme));
		}

		if (wednesdayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(wednesdayscheme));
		}

		if (thursdayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(thursdayscheme));
		}

		if (fridayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(fridayscheme));
		}

		if (saturdayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(saturdayscheme));
		}

		if (sundayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(sundayscheme));
		}

		return builder.toString();
	}
	
	/**
	 * 组建页面显示使用的字符串scheme
	 * 
	 * @param cproPlan
	 * @return
	 */
	public static String formateScheme(TmpCproPlan cproPlan) {

		Integer mondayscheme = cproPlan.getMondayScheme();
		Integer tuesdayscheme = cproPlan.getTuesdayScheme();
		Integer wednesdayscheme = cproPlan.getWednesdayScheme();
		Integer thursdayscheme = cproPlan.getThursdayScheme();
		Integer fridayscheme = cproPlan.getFridayScheme();
		Integer saturdayscheme = cproPlan.getSaturdayScheme();
		Integer sundayscheme = cproPlan.getSundayScheme();

		StringBuilder builder = new StringBuilder();
		// 周一在第一位
		if (mondayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(mondayscheme));
		}

		if (tuesdayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(tuesdayscheme));
		}

		if (wednesdayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(wednesdayscheme));
		}

		if (thursdayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(thursdayscheme));
		}

		if (fridayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(fridayscheme));
		}

		if (saturdayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(saturdayscheme));
		}

		if (sundayscheme == null) {
			builder.append(CproPlanConstant.DAY_SCHEME_NONE);
		} else {
			builder.append(CproPlanScheduleUtils.decDayScheme(sundayscheme));
		}

		return builder.toString();
	}

	/**
	 * 每天的投放计划是以一个int值存储的，低位位0点，高位为23点， 返回在页面上显示的字符串是高位为0点，低位为23点的定长24的字符串
	 * 
	 * @param dayScheme
	 * @return
	 */
	public static String decDayScheme(int dayScheme) {

		if (dayScheme == 0) {
			return CproPlanConstant.DAY_SCHEME_NONE;
		}

		StringBuilder builder = new StringBuilder(24);

		String binaryStr = Integer.toBinaryString(dayScheme);
		/**
		 * Integer.numberOfLeadingZeros 对0是一种特殊情况返回32，造成多一位0，必须特殊处理
		 */
		for (int i = 8; i < Integer.numberOfLeadingZeros(dayScheme); i++) {
			builder.append('0');
		}
		builder.append(binaryStr);

		return StringUtils.reverse(builder.toString());
	}

	/**
	 * 每天的投放计划是以一个int值存储的，低位位0点，高位为23点， 返回在页面上显示的字符串是高位为0点，低位为23点的定长24的字符串
	 * 
	 * @param dayScheme
	 * @return
	 */
	public static int encDayScheme(String dayScheme) {
		return Integer.valueOf(StringUtils.reverse(dayScheme), 2);
	}

	/**
	 * 判断用户是否没有选定任何时间计划
	 * 
	 * @param dayScheme
	 * @return
	 */
	public static boolean validNullScheme(final String dayScheme) {
		if (StringUtils.isEmpty(dayScheme)) {
			return true;
		}

		for (int i = 0; i < dayScheme.length(); i++) {
			char hourScheme = dayScheme.charAt(i);
			if (hourScheme == '1') {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * 是否合法投放日程字符串
	 * 
	 * @param scheduleStr
	 * @return boolean
	 * @author guojichun
	 * @since 2.0.0
	 */
	public static boolean isVaildSchedule(final String scheduleStr) {
		if (null == scheduleStr || scheduleStr.length()!=168) {
			return false;
		}
		for (int i = 0; i < 168; i++) {
			char hourScheme = scheduleStr.charAt(i);
			if (hourScheme != '1' && hourScheme !='0') {
				return false;
			}
		}
		return true;
	}
}

