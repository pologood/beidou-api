package com.baidu.beidou.api.external.tool.service.vo.impl;

import java.util.ArrayList;
import java.util.List;

import com.baidu.beidou.api.external.report.vo.UnitStatViewItem;
import com.baidu.beidou.api.external.tool.service.vo.AbstractReportData;
import com.baidu.beidou.api.external.tool.vo.OneReportResponseType;
import com.baidu.beidou.api.external.tool.vo.response.GetOneReportResponse;
/**
 * 
 * Function: 推广创意层级数据封装
 * 
 * @author caichao
 * @date 9 11, 2013
 */
public class UnitReportDataImpl extends AbstractReportData {
	/**
	 * 创意层级数据库信心填充
	 * @param responseData 封装数据库部分数据
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public GetOneReportResponse fillData(List responseData) {
		GetOneReportResponse response = new GetOneReportResponse();
        List<OneReportResponseType> reports = new ArrayList<OneReportResponseType>();
		List<UnitStatViewItem> items = responseData;
		for(UnitStatViewItem item : items){
			OneReportResponseType report = new OneReportResponseType();
			report.setUserId(item.getUserid());
			report.setUserName(item.getUsername());
			report.setCampaignId(item.getPlanid());
			report.setCampaignName(item.getPlanname());
			report.setGroupId(item.getGroupid());
			report.setGroupName(item.getGroupname());
			report.setAdId(item.getUnitid());
			report = this.fillDorisData(report, item);
			reports.add(report);
		}
		response.setData(reports);
		return response;
	}

}
