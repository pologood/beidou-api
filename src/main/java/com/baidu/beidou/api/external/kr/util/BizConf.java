package com.baidu.beidou.api.external.kr.util;


public class BizConf {

	// 推词匹配模式
	public static final Long KR_MATCH_TYPE_ACCU = 0L;// 精确匹配
	public static final Long KR_MATCH_TYPE_WORD = 1L;// 短语匹配
	public static final Long KR_MATCH_TYPE_WIDE = 2L;// 广泛匹配
	

	public static int TOOLS_KRGW_WORDLEN = 64;

	public static int TOOLS_KRPAGE_URLLEN = 1017;

	// 有关krgw及krpage的请求包参数设定
	public static long KRGW_REQUEST_NUM = 450L;
	public static long KRGW_RESPONSE_NUM = 300L;
	public static long KRGW_REQUEST_PV_LOWER = 1L;
	public static long KRGW_REQUEST_PV_UPPER = 2147483647L;// 0x7FFFFFFF;
	public static long KRGW_REQUEST_KWC_LOWER = 0L;
	public static long KRGW_REQUEST_KWC_UPPER = -1L;


	/**
	 * @param tOOLSKRGWWORDLEN
	 *            the tOOLS_KRGW_WORDLEN to set
	 */
	public void setTOOLS_KRGW_WORDLEN(int tOOLSKRGWWORDLEN) {
		TOOLS_KRGW_WORDLEN = tOOLSKRGWWORDLEN;
	}

	/**
	 * @param tOOLSKRPAGEURLLEN
	 *            the tOOLS_KRPAGE_URLLEN to set
	 */
	public void setTOOLS_KRPAGE_URLLEN(int tOOLSKRPAGEURLLEN) {
		TOOLS_KRPAGE_URLLEN = tOOLSKRPAGEURLLEN;
	}

	/**
	 * @param kRGWREQUESTNUM
	 *            the kRGW_REQUEST_NUM to set
	 */
	public void setKRGW_REQUEST_NUM(long kRGWREQUESTNUM) {
		KRGW_REQUEST_NUM = kRGWREQUESTNUM;
	}

	/**
	 * @param kRGWRESPONSENUM
	 *            the kRGW_RESPONSE_NUM to set
	 */
	public void setKRGW_RESPONSE_NUM(long kRGWRESPONSENUM) {
		KRGW_RESPONSE_NUM = kRGWRESPONSENUM;
	}

	/**
	 * @param kRGWREQUESTPVLOWER
	 *            the kRGW_REQUEST_PV_LOWER to set
	 */
	public void setKRGW_REQUEST_PV_LOWER(long kRGWREQUESTPVLOWER) {
		KRGW_REQUEST_PV_LOWER = kRGWREQUESTPVLOWER;
	}

	/**
	 * @param kRGWREQUESTPVUPPER
	 *            the kRGW_REQUEST_PV_UPPER to set
	 */
	public void setKRGW_REQUEST_PV_UPPER(long kRGWREQUESTPVUPPER) {
		KRGW_REQUEST_PV_UPPER = kRGWREQUESTPVUPPER;
	}

	/**
	 * @param kRGWREQUESTKWCLOWER
	 *            the kRGW_REQUEST_KWC_LOWER to set
	 */
	public void setKRGW_REQUEST_KWC_LOWER(long kRGWREQUESTKWCLOWER) {
		KRGW_REQUEST_KWC_LOWER = kRGWREQUESTKWCLOWER;
	}

	/**
	 * @param kRGWREQUESTKWCUPPER
	 *            the kRGW_REQUEST_KWC_UPPER to set
	 */
	public void setKRGW_REQUEST_KWC_UPPER(long kRGWREQUESTKWCUPPER) {
		KRGW_REQUEST_KWC_UPPER = kRGWREQUESTKWCUPPER;
	}
	
}
