package com.baidu.beidou.api.external.cprogroup.vo.locale;

import java.util.List;

/**
 * 
 * ClassName: GroupItItem  <br>
 * Function: 内部使用对象
 *
 * @author zhangxu
 * @date Jun 1, 2012
 */
public class GroupItItem {
	
	private int itId;
	
	private boolean hasError;
	
	public GroupItItem() {
		
	}
	
	public GroupItItem(int itId, boolean hasError) {
		this.itId = itId;
		this.hasError = hasError;
	}

	public int getItId() {
		return itId;
	}

	public void setItId(int itId) {
		this.itId = itId;
	}

	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}
	
	
}
