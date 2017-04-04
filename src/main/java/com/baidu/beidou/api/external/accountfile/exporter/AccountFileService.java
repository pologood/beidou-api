package com.baidu.beidou.api.external.accountfile.exporter;

import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileIdRequest;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileStateRequest;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileUrlRequest;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileIdResponse;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileStateResponse;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileUrlResponse;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * 
 * ClassName: AccountFileService  <br>
 * Function: 获取账户或者指定推广计划下的完整数据
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public interface AccountFileService {
	
	/**
	 * 
	 * 获取账户或者指定推广计划下的完整数据fileId <br>
	 * 
	 * 主要功能为： <br>
	 * 1. 验证请求包装类参数 <br>
	 * 2. 插入数据库新的任务 <br>
	 * 3. 插入新到任务到JMS中 <br>
	 * 4. 返回fileId <br>
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetAccountFileIdResponse> 包含fileId
	 */
	public ApiResult<GetAccountFileIdResponse> getAccountFileId(DataPrivilege user,
			GetAccountFileIdRequest request, 
			ApiOption apiOption);
	
	/**
	 * 
	 * 查询fileId状态 <br>
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetAccountFileStateResponse> 包含fileId对应数据文件生成状态
	 */
	public ApiResult<GetAccountFileStateResponse> getAccountFileState(DataPrivilege user,
			GetAccountFileStateRequest request, 
			ApiOption apiOption);
	
	/**
	 * 
	 * 获取报告fileId对应到下载url
	 *  
	 * @param user 用户
	 * @param request 请求包装类
	 * @param apiOption api冗余信息
	 * 
	 * @return ApiResult<GetAccountFileUrlResponse> 包含fileId
	 */
	public ApiResult<GetAccountFileUrlResponse> getAccountFileUrl(DataPrivilege user,
			GetAccountFileUrlRequest request, 
			ApiOption apiOption);
	
}
