package com.baidu.beidou.api.external.accountfile.config;

import java.util.List;

/**
 * 
 * InterfaceName: AccountFileConfigService  <br>
 * Function: 账户信息数据配置服务
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 29, 2012
 */
public interface AccountFileConfigService {

	/**
	 * 加载账户信息文件处理的配置
	 * @param  
	 * @return 
	 */
	public void loadConfig();
	
	/**
	 * 获取所有处理账户信息结构的项目
	 * @param  
	 * @return List<AccountFileItem> 所有处理账户信息结构的项目
	 */
	public List<AccountFileItem> getItems();

}
