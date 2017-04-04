package com.baidu.beidou.api.external.tool.validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.report.constant.ReportConstant;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.error.ReportErrorCode;
import com.baidu.beidou.api.external.tool.constant.ToolConstant;
import com.baidu.beidou.api.external.tool.vo.OneReportRequestType;
import com.baidu.beidou.api.external.tool.vo.response.GetOneReportResponse;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.SeniorUnitMgrFilter;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.DateUtils;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * ClassName: ApiReportValidator  <br>
 * Function: 查询报告验证器
 * 
 * @author caichao
 * @date 13-9-9
 */
public class ApiReportValidator { 
	
	// 北斗上线时间
	private int beidouStartDateYYYY = 2008;
	private int beidouStartDateMM = 11;
	private int beidouStartDateDD = 13;

	protected UserMgr userMgr;

	protected CproPlanMgr cproPlanMgr;

	protected CproGroupMgr cproGroupMgr;

	protected CproUnitMgr cproUnitMgr;


	/**
	 * 验证请求是否合法，如果不合法则往ApiResult中填充ApiError
	 * @param user
	 * @param request
	 * @param result
	 * @return
	 */
	public void validate(BaseRequestUser user, OneReportRequestType request, BaseResponse<GetOneReportResponse> result) {

		Date startDate = request.getStartDate();
		Date endDate = request.getEndDate();
		int reportType = request.getReportType();
		int statRange = request.getStatRange();
		long[] statIds = request.getStatIds();

		// 起始时间是否合法
		if (startDate == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STARTDATE);
			result = DRAPIMountAPIBeanUtils.addApiError(result,
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
			result = DRAPIMountAPIBeanUtils.addApiError(result,
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
			result = DRAPIMountAPIBeanUtils.addApiError(result,
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
				result = DRAPIMountAPIBeanUtils.addApiError(result,
							ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_NOT_BEFORE_AFTERDATE.getValue(),
							ReportErrorCode.UNEXPECTED_PARAMETER_STARTDATE_NOT_BEFORE_AFTERDATE.getMessage(),
								apiPosition.getPosition(),
								null);
				return;
			}
		}


		// 报告类型是否合法
		if (reportType != ReportWebConstants.REPORT_TYPE.ACCOUNT
				&& reportType != ReportWebConstants.REPORT_TYPE.PLAN
				&& reportType != ReportWebConstants.REPORT_TYPE.GROUP
				&& reportType != ReportWebConstants.REPORT_TYPE.UNIT) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_REPORTTYPE);
			result = DRAPIMountAPIBeanUtils.addApiError(result,
							GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
							GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
							apiPosition.getPosition(),
							null);
			return;
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
			result = DRAPIMountAPIBeanUtils.addApiError(result,
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
			DRAPIMountAPIBeanUtils.addApiError(result,
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
				result = DRAPIMountAPIBeanUtils.addApiError(
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
                    //批量查询数据库校验用户权限
					List<Integer> userIds = cproPlanMgr.findUserIdByPlanIds(convert(statIds));
					if(userIds == null || userIds.size() == 0){
						DRAPIMountAPIBeanUtils.addApiError(result,
										ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getValue(),
										ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getMessage(), 
										ToolConstant.POSITION_ALLSTATIDS, 
										null);
					}
					for(int i = 0;i<userIds.size();i++){
						if(userIds.get(i) != user.getDataUser()){
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STATIDS,i);
							DRAPIMountAPIBeanUtils.addApiError(result,
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getValue(),
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getMessage(), 
											apiPosition.getPosition(), 
											null);
						}
					}
				}
			} else if (statRange == ReportWebConstants.REPORT_RANGE.GROUP) { // 验证groupIds都有权限的
				if (statIds != null && statIds.length != 0) {
					//批量查询数据库校验用户权限
					List<Integer> userIds = cproGroupMgr.findUserIdByGroupIds(convert(statIds));
					if(userIds == null || userIds.size() == 0){
						DRAPIMountAPIBeanUtils.addApiError(result,
										ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getValue(),
										ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getMessage(), 
										ToolConstant.POSITION_ALLSTATIDS, 
										null);
					}
					for(int i = 0;i<userIds.size();i++){
						if(userIds.get(i) != user.getDataUser()){
							ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
							apiPosition.addParam(ReportConstant.POSITION_GET_REPORT_ID.POSITION_STATIDS,i);
							DRAPIMountAPIBeanUtils.addApiError(result,
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getValue(),
											ReportErrorCode.UNAUTHORIZATION_STATRANGE_PLAN.getMessage(), 
											apiPosition.getPosition(), 
											null);
						}
					}
				}
			} else if (statRange == ReportWebConstants.REPORT_RANGE.UNIT) { // 验证unitIds都有权限的
				if (statIds != null && statIds.length != 0) {
					// 必须取出userid
					User dataUser = userMgr.findUserBySFid(Long.valueOf(user.getDataUser()).intValue());
					if (dataUser == null) {
						DRAPIMountAPIBeanUtils.addApiError(result,
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
							DRAPIMountAPIBeanUtils.addApiError(result,
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
	
	
	protected List<Integer> convert(long[] ids){
		List<Integer> retList = new ArrayList<Integer>();
		for(Long id : ids){
			retList.add(id.intValue());
		}
		return retList;
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
