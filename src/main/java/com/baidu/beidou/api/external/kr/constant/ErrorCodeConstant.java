package com.baidu.beidou.api.external.kr.constant;


public class ErrorCodeConstant {
	
	/**
	 * 1300-1399
	 */
	
	/** 用户在KR Server被封禁*/
	public static final int ERROR_TOOL_KR_SERVER_FORBID = 1302;
	public static final String ERRMSG_TOOL_KR_SERVER_FORBID = "user forbidden by kr-server";
	
	/** KR查询类型不支持 */
	public static final int ERROR_TOOL_KR_QUEREYTYPE_NOTSUPPORT = 1303;
	public static final String ERRMSG_TOOL_KR_QUEREYTYPE_NOTSUPPOR = "kr query type is not support";
	
	/** KR查询词为空 */
	public static final int ERROR_TOOL_KR_WORD_ISNULL = 1304;
	public static final String ERRMSG_TOOL_KR_WORD_ISNULL = "kr query word is null";
	
	/** KR查询词太长 */
	public static final int ERROR_TOOL_KR_WORD_TOOLONG = 1305;
	public static final String ERRMSG_TOOL_KR_WORD_TOOLONG = "kr query word is too long";
	
	/** KR查询URL为空 */
	public static final int ERROR_TOOL_KR_URL_ISNULL = 1306;
	public static final String ERRMSG_TOOL_KR_URL_ISNULL = "kr query url is null";
	
	/** KR查询URL太长 */
	public static final int ERROR_TOOL_KR_URL_TOOLONG = 1307;
	public static final String ERRMSG_TOOL_KR_URL_TOOLONG = "kr query url is too long";
	
	/** KR行业潜力词为空 */
	public static final int ERROR_TOOL_KR_TRADEWORD_ISNULL = 1308;
	public static final String ERRMSG_TOOL_KR_TRADEWORD_ISNULL = "kr query tradeword is null";
	
	/** KR行业潜力词太长 */
	public static final int ERROR_TOOL_KR_TRADEWORD_TOOLONG = 1309;
	public static final String ERRMSG_TOOL_KR_TRADEWORD_TOOLONG = "kr query tradeword is too long";
	
	/** KR种子类型不支持 */
	public static final int ERROR_TOOL_KR_SEEDTYPE_NOTSUPPORT = 1310;
	public static final String ERRMSG_TOOL_KR_SEEDTYPE_NOTSUPPOR = "kr seed type is not support";
	
	/** KR推词过程中内部错误 */
	public static final int ERROR_TOOL_KR_SEEDTYPE_INNER_ERROR = 1311;
	public static final String ERRMSG_TOOL_KR_SEEDTYPE_INNER_ERROR = "kr inner error.";
	
}
