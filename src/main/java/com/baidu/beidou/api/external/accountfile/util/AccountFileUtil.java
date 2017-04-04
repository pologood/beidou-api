package com.baidu.beidou.api.external.accountfile.util;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;

/**
 * 
 * ClassName: AccountFileUtil  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 11, 2012
 */
public class AccountFileUtil {

	private static FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd-hhmmss-");
	
	public static String getSavedFilePath(Date addTime, String fileId, String suffix){
		String path = AccountFileWebConstants.DOWNLOAD_FILE_SAVE_PATH + File.separator + dateFormat.format(addTime) + fileId + suffix;
		return path;
	}
	
	public static String getDownloadFilePath(Date addTime, String fileId, String suffix){
		String path = dateFormat.format(addTime) + fileId + suffix;
		return path;
	}
	
}
