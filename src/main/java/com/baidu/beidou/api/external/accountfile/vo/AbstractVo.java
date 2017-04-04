package com.baidu.beidou.api.external.accountfile.vo;


/**
 * 
 * ClassName: AbstractVo  <br>
 * Function: 账户信息数据的抽象基类
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 30, 2012
 */
public interface AbstractVo {
	
	/**
	 * 返回子类各个属性的字符串数组
	 * @param  
	 * @return String[] 属性字符串数组
	 */
	public String[] toStringArray();
	
}
