package com.baidu.beidou.api.internal.unit.vo;

import java.util.ArrayList;
import java.util.List;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;

public class UnitResult<T> {
	/**
	 * 'status' => $?, //0--成功，1--失败 ，2--参数错误
	 */
	private int status = ResponseStatus.SUCCESS;
	private String msg;
	
	private List<T> data;
	
	private Integer totalPage = 0;
	private Integer totalNum = 0;
	
	public void addResult(T result) {
		if (data == null) {
			data = new ArrayList<T>();
		}
		data.add(result);
	}
	
	public void addResults(List<T> results) {
		if (data == null) {
			data = new ArrayList<T>();
		}
		data.addAll(results);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<T>  getData() {
		return data;
	}

	public void setData(List<T>  data) {
		this.data = data;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
