package com.baidu.beidou.api.external.user.vo;

import java.io.Serializable;

/**
 * ClassName: AccountInfoType
 * Function: 用户信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class AccountInfoType implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer userid; // 输入的用户id
	private String username; // 用户名
	private Float balance; // 余额,以元为单元
	//private Float wangmeng_balance;//网盟专属余额 由于editor未升级，暂时注释，后续打开

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

//	public Float getWangmeng_balance() {
//		return wangmeng_balance;
//	}
//
//	public void setWangmeng_balance(Float wangmeng_balance) {
//		this.wangmeng_balance = wangmeng_balance;
//	}
//	
	
}
