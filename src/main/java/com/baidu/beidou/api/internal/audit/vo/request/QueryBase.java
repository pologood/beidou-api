package com.baidu.beidou.api.internal.audit.vo.request;

public class QueryBase {
	
    private Integer queryType; // 请求类型，1-用户名查询，2-用户ID查询
    private String query; // 请求内容，内容为空时全文搜索
    private Integer page; // 页码
    private Integer pageSize; // 每页大小
	
	public Integer getQueryType() {
		return queryType;
	}
	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
}
