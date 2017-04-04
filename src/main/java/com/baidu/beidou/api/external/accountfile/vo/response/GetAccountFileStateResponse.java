package com.baidu.beidou.api.external.accountfile.vo.response;

/**
 * 
 * ClassName: GetAccountFileStateResponse  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class GetAccountFileStateResponse {

	/**
	 * 1：等待中
		2：处理中
		3：处理成功
		4: 处理失败
	 */
	private int isGenerated;

	public int getIsGenerated() {
		return isGenerated;
	}

	public void setIsGenerated(int isGenerated) {
		this.isGenerated = isGenerated;
	}
	
}
