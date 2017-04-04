package com.baidu.beidou.api.internal.audit.util;

import java.util.ArrayList;
import java.util.List;

public class UnitAuditSql {

	private String sql;
	private List<Object> params = new ArrayList<Object>();
	
	public UnitAuditSql(String sql, List<Object> params) {
		this.sql = sql;
		this.params = params;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	
}
