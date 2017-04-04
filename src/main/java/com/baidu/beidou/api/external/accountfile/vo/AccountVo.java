package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: AccountVo  <br>
 * Function: 账户数据 <br>
 * 
 * 属性包括：UserId,UserName,余额
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 30, 2012
 */
public class AccountVo implements AbstractVo{

	private Integer userid; // 输入的用户id
	
	private String username; // 用户名
	
	private Float balance; // 余额,以元为单元
	
	public String[] toStringArray(){
		String[] str = new String[3];
		str[0] = String.valueOf(this.getUserid());
		str[1] = String.valueOf(this.getUsername());
		str[2] = String.valueOf(this.getBalance());
		return str;
	}

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
	

}
