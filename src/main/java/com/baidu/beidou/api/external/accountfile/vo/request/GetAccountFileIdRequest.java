package com.baidu.beidou.api.external.accountfile.vo.request;

import com.baidu.beidou.api.external.accountfile.vo.AccountFileRequestType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetAccountFileIdRequest  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class GetAccountFileIdRequest implements ApiRequest {
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

	private AccountFileRequestType accountFileRequestType;

	public AccountFileRequestType getAccountFileRequestType() {
		return accountFileRequestType;
	}

	public void setAccountFileRequestType(
			AccountFileRequestType accountFileRequestType) {
		this.accountFileRequestType = accountFileRequestType;
	}
	
	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}
	
}
