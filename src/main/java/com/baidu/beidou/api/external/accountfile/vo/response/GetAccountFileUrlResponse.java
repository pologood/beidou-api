package com.baidu.beidou.api.external.accountfile.vo.response;

/**
 * 
 * ClassName: GetAccountFileUrlResponse  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class GetAccountFileUrlResponse {

	private String filePath;
	
	private String md5;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
}
