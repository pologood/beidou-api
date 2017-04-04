package com.baidu.beidou.api.external.tool.service.vo.impl;

import java.util.ArrayList;
import java.util.List;

import com.baidu.beidou.api.external.report.vo.AccountStatViewItem;
import com.baidu.beidou.api.external.tool.service.vo.AbstractReportData;
import com.baidu.beidou.api.external.tool.vo.OneReportResponseType;
import com.baidu.beidou.api.external.tool.vo.response.GetOneReportResponse;
/**
 * 
 * ClassName: AccountReportDataImpl <br>
 * Function: 账户层级数据封装
 * 
 * @author caichao
 * @date 9 11, 2013
 */
public  class AccountReportDataImpl extends AbstractReportData {
	/**
	 * 账户层级数据库信心填充
	 * @param responseData 封装数据库部分数据
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public GetOneReportResponse fillData(List responseData) {
        GetOneReportResponse response = new GetOneReportResponse();
        List<OneReportResponseType> reports = new ArrayList<OneReportResponseType>();
		List<AccountStatViewItem> items = responseData;
        if(items != null && items.size() > 0){
        	for(AccountStatViewItem item : items){ 
        		OneReportResponseType report = new OneReportResponseType(); 
        		report.setUserId(item.getUserid());
        		report.setUserName(item.getUsername());
        		report = this.fillDorisData(report, item);
        		reports.add(report);
            }
        }
        response.setData(reports);
		return response;
	} 

	 
}
