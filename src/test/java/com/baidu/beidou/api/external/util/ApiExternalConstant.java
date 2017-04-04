package com.baidu.beidou.api.external.util;

public class ApiExternalConstant {

	public static int OPUSER;
	public static int DATAUSER;
	public static String SERVER_URL;
	
	public static String CODE_SERVICE_URL;
	public static String ACCOUNTFILE_SERVICE_URL;
	public static String SITEFILE_SERVICE_URL;
	public static String GROUP_SERVICE_URL;
	public static String GROUPCONFIG_SERVICE_URL;
	public static String CAMPAIGN_SERVICE_URL;
	public static String CAMPAIGN_SERVICE2_URL;
	public static String AD_SERVICE_URL;
	public static String AD_SERVICE2_URL;
	public static String USERACCOUNT_SERVICE_URL;
	public static String FC_SERVICE_URL;
	public static String KR_SERVICE_URL;
	public static String TOOL_SERVICE_URL;
	public static String APIREPORT_SERVICE_URL;

	static {
		OPUSER = Integer.parseInt(ApiExternalConfig.getValue("OPUSER"));
		DATAUSER = Integer.parseInt(ApiExternalConfig.getValue("DATAUSER"));
		SERVER_URL = ApiExternalConfig.getValue("SERVER_URL");
		
		CODE_SERVICE_URL = SERVER_URL + "/api/CodeService";
		ACCOUNTFILE_SERVICE_URL = SERVER_URL + "/api/AccountFileService";
		SITEFILE_SERVICE_URL = SERVER_URL + "/api/SiteFileService";
		GROUP_SERVICE_URL = SERVER_URL + "/api/GroupService";
		GROUPCONFIG_SERVICE_URL = SERVER_URL + "/api/GroupConfigService";
		CAMPAIGN_SERVICE_URL = SERVER_URL + "/api/CampaignService";
		CAMPAIGN_SERVICE2_URL = SERVER_URL + "/api/CampaignService2";
		AD_SERVICE_URL = SERVER_URL + "/api/AdService";
		AD_SERVICE2_URL = SERVER_URL + "/api/AdService2";
		USERACCOUNT_SERVICE_URL = SERVER_URL + "/api/UserAccountService";
		FC_SERVICE_URL = SERVER_URL + "/api/FCService";
		KR_SERVICE_URL = SERVER_URL + "/api/KrService";
		TOOL_SERVICE_URL = SERVER_URL + "/api/ToolService";
		APIREPORT_SERVICE_URL = SERVER_URL + "/api/ApiReportService";
	}

}
