package com.baidu.beidou.api.external.tool.vo.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baidu.beidou.api.external.tool.vo.OneReportResponseType;
/**
 * 
 * ClassName: GetOneReportResponse
 * Function: api返回数据定义
 *
 * @author caichao
 * @date 2013-09-12
 */
public class GetOneReportResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6442724291799467381L;
	private List<OneReportResponseType> data = new ArrayList<OneReportResponseType>();
	private int budget;//所有有效计划预算总额
	
	
	public List<OneReportResponseType> getData() {
		return data;
	}
	public void setData(List<OneReportResponseType> data) {
		this.data = data;
	}
	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}
    
	public String toString(){
		StringBuffer sb = new StringBuffer();
		if(data != null || data.size() > 0){
			for(OneReportResponseType response : data){
				sb.append(response);
			}
		}
		sb.append("budget : ").append(budget);
		return sb.toString();
	}
}
