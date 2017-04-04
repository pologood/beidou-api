package com.baidu.beidou.api.external.tool.service.vo.impl;

import java.util.ArrayList;
import java.util.List;

import com.baidu.beidou.api.external.report.vo.GroupStatViewItem;
import com.baidu.beidou.api.external.tool.service.vo.AbstractReportData;
import com.baidu.beidou.api.external.tool.vo.OneReportResponseType;
import com.baidu.beidou.api.external.tool.vo.response.GetOneReportResponse;
/**
 * 
 * ClassName: GroupReportDataImpl <br>
 * Function: 推广组层级数据封装
 * 
 * @author caichao
 * @date 9 11, 2013
 */
public class GroupReportDataImpl extends AbstractReportData {
	/**
	 * 推广组层级数据库信心填充
	 * @param responseData 封装数据库部分数据
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public GetOneReportResponse fillData(List responseData) {
		GetOneReportResponse response = new GetOneReportResponse();
        List<OneReportResponseType> reports = new ArrayList<OneReportResponseType>();
		List<GroupStatViewItem> items = responseData;
		for(GroupStatViewItem item:items){
			OneReportResponseType report = new OneReportResponseType();
			report.setUserId(item.getUserid());
			report.setUserName(item.getUsername());
			report.setCampaignId(item.getPlanid());
			report.setCampaignName(item.getPlanname());
			report.setGroupId(item.getGroupid());
			report.setGroupName(item.getGroupname());
			report = this.fillDorisData(report, item);
			reports.add(report);
		}
		response.setData(reports);
		return response;
	}

}
