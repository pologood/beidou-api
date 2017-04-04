package com.baidu.beidou.api.external.report.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.exception.JsonConverterException;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.util.DateUtils;
import com.baidu.gson.Gson;

/**
 * 
 * ClassName: JsonConverter
 * Function: 为API报告模块特殊定制到JSON序列号、反序列号工具类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public class JsonConverter {

	private static final Log log = LogFactory.getLog(JsonConverter.class);
	
	/**
	 * string -> obj
	 */
	public static ApiReportQueryParameter fromJson(String json) throws JsonConverterException {
		try{
			Gson gson = new Gson();
			ApiReportQueryParameter result = gson.fromJson(json, ApiReportQueryParameter.class);
			// 兼容百度json库时间转化的小问题，00:00分转化成了12:00
			result.setStartDate(DateUtils.getDateCeil(result.getStartDate()).getTime());
			result.setEndDate(DateUtils.getDateCeil(result.getEndDate()).getTime());
			return result;
		} catch (Exception e){
			log.error(e.getMessage(),e);
			throw new JsonConverterException("error occurred when build ApiReportQueryParameter from json string. " + e.getMessage());
		}
		
	}
	
	/**
	 * obj -> json
	 */
	public static String toJson(ApiReportQueryParameter obj) throws JsonConverterException {
		try{
			Gson gson = new Gson();
			return gson.toJson(obj);
		} catch (Exception e){
			log.error(e.getMessage(),e);
			throw new JsonConverterException("error occurred when get json string from ApiReportQueryParameter object. " + e.getMessage());
		}
	}

}
