package com.baidu.beidou.api.external.report.validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.report.constant.ReportConstant;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.error.ReportErrorCode;
import com.baidu.beidou.api.external.report.vo.ReportRequestType;
import com.baidu.beidou.api.external.report.vo.response.GetReportIdResponse;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.SeniorUnitMgrFilter;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.DateUtils;

/**
 * 
 * ClassName: ApiReportValidator  <br>
 * Function: 查询报告验证器
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 7, 2012
 */
public class ApiReportValidator {
	
	// 主题词开始时间
	private int ktStartDateYYYY = 2011;
	private int ktStartDateMM = 11;
	private int ktStartDateDD = 14;
	
	// 搜客开始时间
	private int qtStartDateYYYY = 2011;
	private int qtStartDateMM = 6;
	private int qtStartDateDD = 22;
	
	// 北斗上线时间
	private int beidouStartDateYYYY = 2008;
	private int beidouStartDateMM = 11;
	private int beidouStartDateDD = 13;

	protected UserMgr userMgr;

	protected CproPlanMgr cproPlanMgr;

	protected CproGroupMgr cproGroupMgr;

	protected CproUnitMgr cproUnitMgr;

	protected CproKeywordMgr cproKeywordMgr;


	/**
	 * 验证请求是否合法，如果不合法则往ApiResult中填充ApiError
	 * @param user
	 * @param request
	 * @param result
	 * @return
	 */
	public void validate(DataPrivilege user, ReportRequestType request, ApiResult<GetReportIdResponse> result) {

		String[] performanceData = request.getPerformanceData();
		Date startDate = request.getStartDate();
		Date endDate = request.getEndDate();
		int reportType = request.getReportType();
		int statRange = request.getStatRange();
		long[] statIds = request.getStatIds();
		int format = request.getFormat();

		// 绩效数据不为空，则校验字符串是否合法
		if (performanceData != null) {
			if(performanceData.length > 16){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_PERFORMANCEDATA);
				result = ApiResultBeanUtils.addApiError(result,
								GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
								GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
								apiPosition.getPosition(),
								null);
				
				return;
			}
			for (String data : performanceData) {
				if (!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_SRCH) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CLICK) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_COST) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CTR) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CPM) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_ACP) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_SRCHUV) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CLICKUV) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_SRSUR) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_CUSUR) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_COCUR) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_ARRIVAL_RATE) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_HOP_RATE) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_RES_TIME) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_DIRECT_TRANS_CNT) &&
						!data.equalsIgnoreCase(ReportWebConstants.PERFORMANCE_DATE_INDIRECT_TRANS_CNT)) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_PERFORMANCEDATA);
					result = ApiResultBeanUtils.addApiError(result,
									GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
									GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
									apiPosition.getPosition(),
									null);
					return;
				}
			}
		}

		// 起始时间是否合法
		if (startDate == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STARTDATE);
			result = ApiResultBeanUtils.addApiError(result,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
							apiPosition.getPosition(),
							null);
			return;
		}

		// 结束时间是否合法
		if (endDate == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_ENDDATE);
			result = ApiResultBeanUtils.addApiError(result,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
							apiPosition.getPosition(),
							null);
			return;
		}
		
		// 开始和结束时间不能早于2008-11-13
		Date beidouStart00 = DateUtils.getDate(beidouStartDateYYYY, beidouStartDateMM, beidouStartDateDD);
		if(startDate.before(beidouStart00) || endDate.before(beidouStart00)){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_ENDDATE);
			result = ApiResultBeanUtils.addApiError(result,
						ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_TOO_EARLY.getValue(),
						ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_TOO_EARLY.getMessage(),
							apiPosition.getPosition(),
							null);
			return;
		}
		
		// 结束时间必须在开始时间之后
		if(!endDate.equals(startDate)){
			if (endDate.before(startDate)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_ENDDATE);
				result = ApiResultBeanUtils.addApiError(result,
							ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_NOT_BEFORE_AFTERDATE.getValue(),
							ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_NOT_BEFORE_AFTERDATE.getMessage(),
								apiPosition.getPosition(),
								null);
				return;
			}
		}

		// 报告下载类型值是否合法
		if (format != ReportWebConstants.FORMAT_CSV
				&& format != ReportWebConstants.FORMAT_ZIP) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_FORMAT);
			result = ApiResultBeanUtils.addApiError(result,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
							apiPosition.getPosition(),
							null);
			return;
		}

		// 报告类型是否合法
		if (reportType != ReportWebConstants.REPORT_TYPE.ACCOUNT
				&& reportType != ReportWebConstants.REPORT_TYPE.PLAN
				&& reportType != ReportWebConstants.REPORT_TYPE.GROUP
				&& reportType != ReportWebConstants.REPORT_TYPE.UNIT
				&& reportType != ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL
				&& reportType != ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL_COMPATIBLE
				&& reportType != ReportWebConstants.REPORT_TYPE.SITE_SHOWN
				&& reportType != ReportWebConstants.REPORT_TYPE.INTEREST_SHOWN
				&& reportType != ReportWebConstants.REPORT_TYPE.PACK
				&& reportType != ReportWebConstants.REPORT_TYPE.KEYWORD_PACK
				&& reportType != ReportWebConstants.REPORT_TYPE.REGION
				&& reportType != ReportWebConstants.REPORT_TYPE.GENDER
				&& reportType != ReportWebConstants.REPORT_TYPE.INTEREST_CHOSEN
				&& reportType != ReportWebConstants.REPORT_TYPE.SITE_CHOSEN
				&& reportType != ReportWebConstants.REPORT_TYPE.TRADE_CHOSEN
				&& reportType != ReportWebConstants.REPORT_TYPE.APP
				&& reportType != ReportWebConstants.REPORT_TYPE.DEVICE
				&& reportType != ReportWebConstants.REPORT_TYPE.ATTACH_PHONE
				&& reportType != ReportWebConstants.REPORT_TYPE.ATTACH_MESSAGE
				&& reportType != ReportWebConstants.REPORT_TYPE.ATTACH_CONSULT
				&& reportType != ReportWebConstants.REPORT_TYPE.ATTACH_SUB_URL) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_REPORTTYPE);
			result = ApiResultBeanUtils.addApiError(result,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
							apiPosition.getPosition(),
							null);
			return;
		}
		

		// CT/QT查询时间不能过早
		if (reportType == ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL) {
			Date ktStart00 = DateUtils.getDate(ktStartDateYYYY, ktStartDateMM, ktStartDateDD);
			if(startDate.before(ktStart00)){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_ENDDATE);
				result = ApiResultBeanUtils.addApiError(result,
							ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_CT_TOO_EARLY.getValue(),
							ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_CT_TOO_EARLY.getMessage(),
								apiPosition.getPosition(),
								null);
				return;
			}
		} else if (reportType == ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL_COMPATIBLE) {
			Date qtStart00 = DateUtils.getDate(qtStartDateYYYY, qtStartDateMM, qtStartDateDD);
			if(startDate.before(qtStart00)){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_ENDDATE);
				result = ApiResultBeanUtils.addApiError(result,
							ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_QT_TOO_EARLY.getValue(),
							ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_OR_ENDATE_QT_TOO_EARLY.getMessage(),
								apiPosition.getPosition(),
								null);
				return;
			}
		}

		// 报告范围为选填，如果为空，则默认为账户范围
		if (statRange == 0) {
			statRange = ReportWebConstants.REPORT_RANGE.ACCOUNT;
		}
		
		// 报告范围值是否合法
		if (statRange != ReportWebConstants.REPORT_RANGE.ACCOUNT
				&& statRange != ReportWebConstants.REPORT_RANGE.PLAN
				&& statRange != ReportWebConstants.REPORT_RANGE.GROUP
				&& statRange != ReportWebConstants.REPORT_RANGE.UNIT) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STATRANGE);
			result = ApiResultBeanUtils.addApiError(result,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
							apiPosition.getPosition(),
							null);
			return;
		}
		
		// 验证特定的查询范围下可以查询特定的报告类型
		if(!ReportWebConstants.REPORT_RANGE_ALLOWED_TYPES_MAP.get(statRange).contains(reportType)){
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STATRANGE);
			ApiResultBeanUtils.addApiError(result,
					ReportErrorCode.UNEXPECTED_PARAMETER_STATRANGE_REPORTTYPE_MISMATCH.getValue(),
					ReportErrorCode.UNEXPECTED_PARAMETER_STATRANGE_REPORTTYPE_MISMATCH.getMessage(), 
					apiPosition.getPosition(), 
					null);
			return;
		}

		// statIds数量要小于100
		if(statIds != null && statIds.length > 0){
			if(statIds.length > ReportWebConstants.MAX_STATIDS){
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STATIDS);
				result = ApiResultBeanUtils.addApiError(
						result,ReportErrorCode.UNEXPECTED_PARAMETER_TOO_LONG_STATIDS.getValue(),
						ReportErrorCode.UNEXPECTED_PARAMETER_TOO_LONG_STATIDS.getMessage(),
						apiPosition.getPosition(),
						null);
				return;
			}
		}
		
		// 报告范围必须和statIds配合使用，并且报告范围下的statId必须是该用户有权限的
		if (statRange == ReportWebConstants.REPORT_RANGE.ACCOUNT) {
			// do nothing 不验证是否有statIds
		} else {
//			// 不是必须有statIds
//			if(statIds.length == 0){
//				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
//				apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STATRANGE);
//				result = ApiResultBeanUtils.addApiError(
//						result,ReportErrorCode.UNEXPECTED_PARAMETER_STATIDS.getValue(),
//						ReportErrorCode.UNEXPECTED_PARAMETER_STATIDS.getMessage(),
//						apiPosition.getPosition(),
//						null);
//				return;
//			}
			// 验证planIds都有权限的
			if (statRange == ReportWebConstants.REPORT_RANGE.PLAN) {
				if (statIds != null && statIds.length != 0) {
					for (int index = 0; index < statIds.length; index++) {
						List<Integer> planIds = new ArrayList<Integer>();
						planIds.add(Long.valueOf(statIds[index]).intValue());
						List<Integer> userIds = cproPlanMgr.findUserIdByPlanIds(planIds);
						if (userIds == null || userIds.isEmpty() || userIds.get(0) != user.getDataUser()) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STATIDS,index);
							ApiResultBeanUtils.addApiError(result,
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getValue(),
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getMessage(), 
											apiPosition.getPosition(), 
											null);
						}
					}
				}
			} else if (statRange == ReportWebConstants.REPORT_RANGE.GROUP) { // 验证groupIds都有权限的
				if (statIds != null && statIds.length != 0) {
					for (int index = 0; index < statIds.length; index++) {
						List<Integer> groupIds = new ArrayList<Integer>();
						groupIds.add(Long.valueOf(statIds[index]).intValue());
						List<Integer> userIds = cproGroupMgr.findUserIdByGroupIds(groupIds);
						if (userIds == null || userIds.isEmpty() || userIds.get(0) != user.getDataUser()) {
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STATIDS,index);
							ApiResultBeanUtils.addApiError(result,
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_GROUP.getValue(),
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_GROUP.getMessage(), 
											apiPosition.getPosition(), 
											null);
						}
					}
				}
			} else if (statRange == ReportWebConstants.REPORT_RANGE.UNIT) { // 验证unitIds都有权限的
				if (statIds != null && statIds.length != 0) {
					// 必须取出userid
					User dataUser = userMgr.findUserBySFid(user.getDataUser());
					if (dataUser == null) {
						ApiResultBeanUtils.addApiError(result,
								GlobalErrorCode.UNAUTHORIZATION.getValue(),
								GlobalErrorCode.UNAUTHORIZATION.getMessage(),
								PositionConstant.USER, 
								null);
						return;
					}
					for (int index = 0; index < statIds.length; index++) {
						List<Long> unitIds = new ArrayList<Long>();
						unitIds.add(statIds[index]);
						SeniorUnitMgrFilter filter = new SeniorUnitMgrFilter();
						filter.setUserid(dataUser.getUserid());
						filter.setUnitId(unitIds);
						List<Long> _unitIds = cproUnitMgr.findUnitIdByFilter(0, -1, dataUser.getUserid(), filter);
						if(CollectionUtils.isEmpty(_unitIds)){
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STATIDS,index);
							ApiResultBeanUtils.addApiError(result,
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_UNIT.getValue(),
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_UNIT.getMessage(), 
											apiPosition.getPosition(), 
											null);
						}
					}
				}
			}
		}	

	}
	
	protected Date[] ensureDate(Date from, Date to){
		//ensure t1<t2, t1为0时，t2为23时
		Date t1 = DateUtils.getDateCeil(from).getTime();
		Date t2 = DateUtils.getDateCeil(to).getTime();
		if (t1.compareTo(t2) <= 0){
			t2 = DateUtils.getDateFloor(to).getTime();
		} else {
			t1 = t2;
			t2 = DateUtils.getDateFloor(from).getTime();
		}
		
		//日期约束
		Date start00 = DateUtils.getDate(qtStartDateYYYY, qtStartDateMM, qtStartDateDD);
		Date start23 = DateUtils.getDate(qtStartDateYYYY, qtStartDateMM, qtStartDateDD, 23);
		Date tody00 = DateUtils.getCurDateCeil().getTime();
		Date tody23 = DateUtils.getCurDateFloor().getTime();
		if (t1.compareTo(start00)<0 && t2.compareTo(start23)>=0){
			t1 = start00;
		}
		if (t2.compareTo(tody23)>0 && t1.compareTo(tody00)<=0){
			t2 = tody23;
		}
		//20081113日统计数据为空
		if ((t1.compareTo(start00)<0 && t2.compareTo(start23)<0) 
				|| (t1.compareTo(tody00)>0 && t2.compareTo(tody23)>0)){
			throw new RuntimeException("查询时间无效[" + from +"," + to + "]，返回空");
		}
		
		return new Date[]{t1, t2};
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public CproPlanMgr getCproPlanMgr() {
		return cproPlanMgr;
	}

	public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
		this.cproPlanMgr = cproPlanMgr;
	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public CproUnitMgr getCproUnitMgr() {
		return cproUnitMgr;
	}

	public void setCproUnitMgr(CproUnitMgr cproUnitMgr) {
		this.cproUnitMgr = cproUnitMgr;
	}

	public CproKeywordMgr getCproKeywordMgr() {
		return cproKeywordMgr;
	}

	public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
		this.cproKeywordMgr = cproKeywordMgr;
	}

	public int getKtStartDateYYYY() {
		return ktStartDateYYYY;
	}

	public void setKtStartDateYYYY(int ktStartDateYYYY) {
		this.ktStartDateYYYY = ktStartDateYYYY;
	}

	public int getKtStartDateMM() {
		return ktStartDateMM;
	}

	public void setKtStartDateMM(int ktStartDateMM) {
		this.ktStartDateMM = ktStartDateMM;
	}

	public int getKtStartDateDD() {
		return ktStartDateDD;
	}

	public void setKtStartDateDD(int ktStartDateDD) {
		this.ktStartDateDD = ktStartDateDD;
	}

	public int getQtStartDateYYYY() {
		return qtStartDateYYYY;
	}

	public void setQtStartDateYYYY(int qtStartDateYYYY) {
		this.qtStartDateYYYY = qtStartDateYYYY;
	}

	public int getQtStartDateMM() {
		return qtStartDateMM;
	}

	public void setQtStartDateMM(int qtStartDateMM) {
		this.qtStartDateMM = qtStartDateMM;
	}

	public int getQtStartDateDD() {
		return qtStartDateDD;
	}

	public void setQtStartDateDD(int qtStartDateDD) {
		this.qtStartDateDD = qtStartDateDD;
	}

	public int getBeidouStartDateYYYY() {
		return beidouStartDateYYYY;
	}

	public void setBeidouStartDateYYYY(int beidouStartDateYYYY) {
		this.beidouStartDateYYYY = beidouStartDateYYYY;
	}

	public int getBeidouStartDateMM() {
		return beidouStartDateMM;
	}

	public void setBeidouStartDateMM(int beidouStartDateMM) {
		this.beidouStartDateMM = beidouStartDateMM;
	}

	public int getBeidouStartDateDD() {
		return beidouStartDateDD;
	}

	public void setBeidouStartDateDD(int beidouStartDateDD) {
		this.beidouStartDateDD = beidouStartDateDD;
	}

}
