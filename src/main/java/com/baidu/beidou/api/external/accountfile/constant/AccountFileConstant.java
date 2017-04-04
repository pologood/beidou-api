package com.baidu.beidou.api.external.accountfile.constant;

/**
 * 
 * ClassName: AccountFileConstant  <br>
 * Function: 前端参数不同接口对应的字段名称
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class AccountFileConstant {

	public interface POSITION_GET_ACCOUNTFILE_ID{
		public static final String POSITION_FORMAT = "format";
		public static final String POSITION_CAMPAIGNIDS = "campaignIds";
	}
	
	public interface POSITION_GET_ACCOUNTFILE_STATE{
		public static final String POSITION_ACCOUNTFILEID = "fileId";
	}
	
	public interface POSITION_GET_ACCOUNTFILE_FILEURL{
		public static final String POSITION_ACCOUNTFILEID = "fileId";
	}
	
	
}
