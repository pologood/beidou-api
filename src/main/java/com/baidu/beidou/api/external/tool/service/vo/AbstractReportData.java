package com.baidu.beidou.api.external.tool.service.vo;

import java.util.List;

import com.baidu.beidou.api.external.report.vo.AbstractStatViewItem;
import com.baidu.beidou.api.external.tool.vo.OneReportResponseType;
import com.baidu.beidou.api.external.tool.vo.response.GetOneReportResponse;
/**
 * 
 * ClassName: AbstractReportData <br>
 * Function: 填充doris数据接口定义
 * 
 * @author caichao
 * @date 2013-09-11
 */
public abstract class AbstractReportData {
	
	public abstract GetOneReportResponse fillData(List<AbstractStatViewItem> responseData); 
	/**
	 * 公共部分放到父类实现
	 * @param report
	 * @param item
	 * @return
	 */
	public OneReportResponseType fillDorisData(OneReportResponseType report,AbstractStatViewItem item){
		report.setDate(item.getDay());
		report.setSrch(item.getSrchs());
		report.setClk(item.getClks());
		report.setCost(item.getCost());
		report.setCtr(item.getCtr());
		report.setAcp(item.getAcp());
		report.setCpm(item.getCpm());
		return report;
   }
}
