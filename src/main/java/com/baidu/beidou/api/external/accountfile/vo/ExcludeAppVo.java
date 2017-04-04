package com.baidu.beidou.api.external.accountfile.vo;

/**
 * 
 * ClassName: ExcludeAppVo  <br>
 * Function: 排除移动应用数据
 *
 */
public class ExcludeAppVo implements AbstractVo{

	private Integer groupid;
	
	private String appName;
	
	private Long appId;
	
	public String[] toStringArray(){
		String[] str = new String[3];
		str[0] = String.valueOf(this.getGroupid());
		str[1] = String.valueOf(this.getAppName());
		str[2] = String.valueOf(this.getAppId());
		return str;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
	
}
