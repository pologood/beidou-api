package com.baidu.beidou.api.external.util.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ApiConstant
 * Function: 用户session名称配置
 *
 * @author genglei
 * @version 
 * @since TODO
 * @date 2011-12-20
 */
public class ApiConstant {
	
	public static final String KEY_SESSION_USER = "__session_user" ;
	public static final String KEY_OPTHISTORY_CONTENT = "__session_opthistory" ;
	public static final String KEY_THRUPUT_CONTROL = "__session_thruputcontrol" ;
	
	/**
	 * 操作终端： api
	 */
	public static final int CLIENT_TYPE_API = 0;
	public static final int CLIENT_TYPE_EDITOR = 2;
	public static final int CLIENT_TYPE_OTHER = 3;
	
	public static Map<Integer, Integer> DRAPI2BEIDOUAPI_CLIENT_TYPE_CONVERT_MAP = new HashMap<Integer, Integer>();
	

	// 从dr api中的apioption取出的ip
	public static final String CLIENT_IP_FROM_APIOPTION = "remoteIp";
	public static final String CLIENT_TYPE_FROM_APIOPTION = "tokenType";
	
	// ApiResult中toString用的变量
	public static final String TOSTRING_DATA = "data:" ;
	public static final String TOSTRING_ERRORS = "errors:" ;
	public static final String TOSTRING_PAYMENT = "payment:" ;
	
	// ApiResult中配额到键
	public static final String PAYMENT_TOTAL = "total";
	public static final String PAYMENT_SUCCESS = "success";	
	
	// 记录日志用的api返回状态码
	public static final int API_STATUS_OK = 200;
	public static final int API_STATUS_ERROR = 500;
	
	// 流量控制模块未限制流量的标示
	public static final int UNLIMIT_THRESHOLD = -1;
	
	// 请求默认数据大小，多数存在也get请求中
	public static final int REQUEST_DEFAULT_DATA_SIZE = 1;
	
	// 封禁策略设置
	public static final String PRISON_LEVEL1_USERKEY = "p1:uid:";
	public static final String PRISON_LEVEL2_USERKEY = "p2:uid:";
	public static final String PRISON_LEVEL3_USERKEY = "p3:uid:";
	
	public static final String BAN_LEVEL1_KEY = "b1:uid:";
	public static final String BAN_LEVEL2_KEY = "b2:uid:";
	public static final String BAN_LEVEL3_KEY = "b3:uid:";
	public static final String BAN_VALUE = "b";
	
	public Map<Integer, Integer> getDRAPI2BEIDOUAPI_CLIENT_TYPE_CONVERT_MAP() {
		return DRAPI2BEIDOUAPI_CLIENT_TYPE_CONVERT_MAP;
	}
	
	public void setDRAPI2BEIDOUAPI_CLIENT_TYPE_CONVERT_MAP(
			Map<Integer, Integer> drapi2beidouapi_client_type_convert_map) {
		DRAPI2BEIDOUAPI_CLIENT_TYPE_CONVERT_MAP = drapi2beidouapi_client_type_convert_map;
	}
	
	
}
