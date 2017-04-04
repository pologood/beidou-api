package com.baidu.beidou.api.external.accountfile.facade.impl;

import java.util.List;

import com.baidu.beidou.api.external.accountfile.config.AccountFileConfigService;
import com.baidu.beidou.api.external.accountfile.config.AccountFileItem;
import com.baidu.beidou.api.external.accountfile.facade.AccountFileOutputFacade;

/**
 * 
 * ClassName: AccountFileOutputFacadeImpl  <br>
 * Function: 输出账户信息数据文件的facade类
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 30, 2012
 */
public class AccountFileOutputFacadeImpl implements AccountFileOutputFacade{

	private AccountFileConfigService accountFileConfigService;
	
	/**
	 * 输出账户信息数据文件
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @param tmpPath 临时文件保存路径
	 */
	public void output(int userId, List<Integer> planIds, List<Integer> groupIds, String tmpPath) throws Exception{
		// 获取所有配置的item，依次调用其handle方法，传入参数为userId，planIds，临时文件保存路径
		List<AccountFileItem> items = accountFileConfigService.getItems();
		for(AccountFileItem item: items){
			item.handle(
					userId, 
					planIds,
					groupIds,
					tmpPath
					);
		}
	}

	public AccountFileConfigService getAccountFileConfigService() {
		return accountFileConfigService;
	}

	public void setAccountFileConfigService(
			AccountFileConfigService accountFileConfigService) {
		this.accountFileConfigService = accountFileConfigService;
	}
	
	
}
