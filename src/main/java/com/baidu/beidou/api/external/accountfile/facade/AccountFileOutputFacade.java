package com.baidu.beidou.api.external.accountfile.facade;

import java.util.List;

/**
 * 
 * ClassName: AccountFileOutputFacade  <br>
 * Function: 输出账户信息数据文件的facade接口定义
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 30, 2012
 */
public interface AccountFileOutputFacade {

	/**
	 * 输出账户信息数据文件
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @param tmpPath 临时文件保存路径
	 */
	public void output(int userId, List<Integer> planIds, List<Integer> groupIds, String tmpPath) throws Exception;
	
}
