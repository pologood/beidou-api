package com.baidu.beidou.api.external.util.vo;

import java.util.Map;

/**
 * 
 * ClassName: ApiOption <br>
 * Function: DR-API传递过来的冗余信息 <p>
 * 其中options里面目前包括如下数据： <br>
 * tokenId：API权限代码（token）的Id <br>
 * tokenType：API权限代码（token）的类型（0 普通；1 SEM；2 EDITOR； 3 内部）<br>
 * remoteIp：API请求来源IP <br>
 * logId：API服务器日志序列号 <br>
 * fromServer：API服务器名 <br>
 *	挂接方可以利用这些信息来做审计、授权，以及反查DR-API日志。 <br>
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 21, 2011
 */
public class ApiOption {

	private Map<String, String> options;

	public Map<String, String> getOptions() {
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}
	
}
