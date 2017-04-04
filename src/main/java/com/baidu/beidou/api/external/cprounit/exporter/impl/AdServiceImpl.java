package com.baidu.beidou.api.external.cprounit.exporter.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;

import com.baidu.beidou.api.external.cprounit.constant.AdConstant;
import com.baidu.beidou.api.external.cprounit.error.AdErrorCode;
import com.baidu.beidou.api.external.cprounit.exporter.AdService;
import com.baidu.beidou.api.external.cprounit.service.UnitService;
import com.baidu.beidou.api.external.cprounit.util.AdBeanMapper;
import com.baidu.beidou.api.external.cprounit.vo.AdType;
import com.baidu.beidou.api.external.cprounit.vo.StatusType;
import com.baidu.beidou.api.external.cprounit.vo.request.AddAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.CopyAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.DeleteAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.GetAdByAdIdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.GetAdByGroupIdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.GetAdIdByGroupIdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.ReplaceAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.SetAdStatusRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.UpdateAdRequest;
import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.exception.CproUnitDeleteException;
import com.baidu.beidou.cprounit.exception.CproUnitUpdateException;
import com.baidu.beidou.cprounit.exception.UnitUpdateStateException;
import com.baidu.beidou.cprounit.mcdriver.bean.response.GrantResult;
import com.baidu.beidou.cprounit.mcdriver.constant.AmConstant;
import com.baidu.beidou.cprounit.mcdriver.mcparser.ParseMC;
import com.baidu.beidou.cprounit.service.AmService;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.UnitBeanUtils;
import com.baidu.beidou.cprounit.vo.ModUnitOptVo;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.cprounit.vo.UnitInfoView2;
import com.baidu.beidou.cprounit.vo.UnitQuery;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.BeanMapperProxy;
import com.baidu.beidou.util.CollectionsUtil;
import com.baidu.beidou.util.MD5;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.chuangyi.flash.decode.DecodeResult;
import com.baidu.chuangyi.flash.decode.FlashDecoder;

/**
 * ClassName: AdServiceImpl
 * Function: unit层级对外接口实现
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-15
 */
public class AdServiceImpl implements AdService {
	
	private static final Log LOG = LogFactory.getLog(AdServiceImpl.class);

	private UserMgr userMgr = null;
	private CproUnitMgr unitMgr = null;
	private UnitService unitService = null;
	private AmService amService;
	private CproGroupMgr cproGroupMgr = null;
	
	// 推广创意设置相关阀值限制
	private int addAdMax;
	private int getAdByAdIdMax;
	private int updateAdMax;
	private int replaceAdMax;
	private int copyAdForAdMax;
	private int copyAdForGroupMax;
	private int deleteAdMax;
	private int setAdStatusMax;

	public ApiResult<AdType> addAd(DataPrivilege user,
			AddAdRequest request, ApiOption apiOption) {
		ApiResult<AdType> result = new ApiResult<AdType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		if (bdUser == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.USER);
			
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}

		PaymentResult payment = new PaymentResult();
		if (request != null && request.getAdTypes() != null) {
			if (request.getAdTypes().length > this.addAdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TOOMANY_NUM.getValue(),
						AdErrorCode.TOOMANY_NUM.getMessage(),
						apiPosition.getPosition(), null);
				
				return result;
			}
		} else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);

			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			
			return result;
		}

		AdType[] adTypes = request.getAdTypes();
		payment.setTotal(adTypes.length);
		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();

		for (int i = 0; i < adTypes.length; i++) {
			long start = System.currentTimeMillis();
			
			AdType ad = adTypes[i];
			boolean isSuccess = unitService.addUnit(i, ad, result, bdUser, optContents);
			if (isSuccess) {
				payment.increSuccess();
			}
			
			LOG.info("Add " + (i+1) + " unit using " + (System.currentTimeMillis() - start) + " millis");
		}

		result.setPayment(payment);
		
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); 
		
		return result;
	}

	public ApiResult<AdType> getAdByAdId(DataPrivilege user,
			GetAdByAdIdRequest request, ApiOption apiOption) {
		PaymentResult payment = new PaymentResult();
		ApiResult<AdType> result = new ApiResult<AdType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		if (bdUser == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.USER);
			
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}

		if (request != null && request.getAdIds() != null) {
			if (request.getAdIds().length > getAdByAdIdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TOOMANY_QUERYNUM.getValue(),
						AdErrorCode.TOOMANY_QUERYNUM.getMessage(), 
						apiPosition.getPosition(), null);
				return result;
			}
		} else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		payment.setTotal(request.getAdIds().length);

		List<UnitInfoView> unitList = new ArrayList<UnitInfoView>();
		int idIndex = 0;
		for (Long adId : request.getAdIds()) {
			UnitInfoView unitInfo = unitMgr.findUnitById(bdUser.getUserid(), adId);

			if (unitInfo == null || unitInfo.getStateView().getViewState() == CproUnitConstant.UNIT_STATE_DELETE) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, idIndex);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NO_UNIT.getValue(), 
						AdErrorCode.NO_UNIT.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}

			if (!unitInfo.getUserid().equals(user.getDataUser())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, idIndex);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.WRONG_USER.getValue(),
						AdErrorCode.WRONG_USER.getMessage(), 
						apiPosition.getPosition(), null);
			} else {
				unitList.add(unitInfo);
			}
			idIndex++;
		}

		for (UnitInfoView unitInfo : unitList) {
			AdType adUnit = AdBeanMapper.transformUnitInfoView2AdType(unitInfo);
			result = ApiResultBeanUtils.addApiResult(result, adUnit);
			payment.increSuccess();
		}
		
		unitList = null;
		
		result.setPayment(payment);
		return result;
	}

	public ApiResult<Long> getAdIdByGroupId(DataPrivilege user,
			GetAdIdByGroupIdRequest groupId, ApiOption apiOption) {
		PaymentResult payment = new PaymentResult();
		ApiResult<Long> result = new ApiResult<Long>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		if (bdUser == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.USER);
			
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}

		if (groupId == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		List<UnitInfoView> unitList = new ArrayList<UnitInfoView>();

		UnitQuery condition = new UnitQuery();
		condition.setGid(Long.valueOf(groupId.getGroupId()).intValue());
		unitList = unitMgr.findUnitByGroupId(-1, -1, bdUser.getUserid(), condition);

		if (unitList != null) {
			payment.setTotal(unitList.size());
			for (UnitInfoView unitInfo : unitList) {
				result = ApiResultBeanUtils.addApiResult(result, unitInfo.getUnitid());
				payment.increSuccess();
			}
		}
		result.setPayment(payment);
		return result;
	}

	public ApiResult<AdType> getAdByGroupId(DataPrivilege user,
			GetAdByGroupIdRequest groupId, ApiOption apiOption) {
		PaymentResult payment = new PaymentResult();
		ApiResult<AdType> result = new ApiResult<AdType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		if (bdUser == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.USER);
			
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}

		if (groupId == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		List<UnitInfoView> unitList = new ArrayList<UnitInfoView>();

		UnitQuery condition = new UnitQuery();
		condition.setGid(Long.valueOf(groupId.getGroupId()).intValue());
		unitList = unitMgr.findUnitByGroupId(-1, -1, bdUser.getUserid(), condition);

		if (unitList != null) {
			payment.setTotal(unitList.size());
			for (UnitInfoView unitInfo : unitList) {
				AdType adUnit = AdBeanMapper.transformUnitInfoView2AdType(unitInfo);
				result = ApiResultBeanUtils.addApiResult(result, adUnit);
				payment.increSuccess();
			}
		}
		
		unitList = null;
		
		result.setPayment(payment);
		return result;
	}

	public ApiResult<Object> deleteAd(DataPrivilege user, 
			DeleteAdRequest request, ApiOption apiOption) {
		PaymentResult payment = new PaymentResult();
		ApiResult<Object> result = new ApiResult<Object>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		if (bdUser == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.USER);
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}

		if (request != null && request.getAdIds() != null) {
			if (request.getAdIds().length > deleteAdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS);
				
				result = ApiResultBeanUtils.addApiError(result, 
						AdErrorCode.TOOMANY_DELNUM.getValue(), 
						AdErrorCode.TOOMANY_DELNUM.getMessage(), 
						apiPosition.getPosition(), null);
				return result;
			}
		} else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		payment.setTotal(request.getAdIds().length);

		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		int idIndex = 0;
		int total = request.getAdIds().length;
		int success = 0;
		
		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (Long adId : request.getAdIds()) {
			UnitInfoView unitInfo = unitMgr.findUnitById(bdUser.getUserid(), adId);

			if (unitInfo == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, idIndex);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NO_UNIT.getValue(), 
						AdErrorCode.NO_UNIT.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}

			if (!unitInfo.getUserid().equals(user.getDataUser())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, idIndex);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.WRONG_USER.getValue(),
						AdErrorCode.WRONG_USER.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}

			try {
				List<Long> tmpList = new ArrayList<Long>();
				tmpList.add(unitInfo.getUnitid());
				unitMgr.delUnit(visitor, bdUser.getUserid(), tmpList);
				
				// 记录历史操作记录
				OpTypeVo opType = OptHistoryConstant.OPTYPE_UNIT_DELETE;
				OptContent optContent = new OptContent(unitInfo.getUserid(),
						opType.getOpType(),	opType.getOpLevel(), unitInfo.getUnitid(), 
						opType.getTransformer().toDbString(unitInfo.getTitle()), null);
				optContent.setGroupId(unitInfo.getGroupid().intValue());
				optContents.add(optContent);
			} catch (CproUnitDeleteException e) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, idIndex);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.DEL_ERROR.getValue(),
						AdErrorCode.DEL_ERROR.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}

			payment.increSuccess();
			success++;
			idIndex++;
		}
		
		result.setPayment(payment);
		if (total == success) {
			ApiResultBeanUtils.setSuccessObject(result);
		}
		
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);
		
		return result;
	}

	public ApiResult<Object> setAdStatus(DataPrivilege user, 
			SetAdStatusRequest request, ApiOption apiOption) {
		PaymentResult payment = new PaymentResult();
		ApiResult<Object> result = new ApiResult<Object>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		User bdUser = (User) SessionHolder.getSession().get(
				ApiConstant.KEY_SESSION_USER);
		if (bdUser == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.USER);
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}

		if (request != null && request.getStatusTypes() != null) {
			if (request.getStatusTypes().length > setAdStatusMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.STATUS_TYPES);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TOOMANY_CHGSTATUSNUM.getValue(),
						AdErrorCode.TOOMANY_CHGSTATUSNUM.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}
		} else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		StatusType[] adStatus = request.getStatusTypes();
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);

		int total = adStatus.length;
		int success = 0;
		payment.setTotal(adStatus.length);

		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		for (int i = 0; i < adStatus.length; i++) {
			UnitInfoView unitInfo = unitMgr.findUnitById(bdUser.getUserid(),
					adStatus[i].getAdId());

			if (unitInfo == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.STATUS_TYPES, i);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NO_UNIT.getValue(), 
						AdErrorCode.NO_UNIT.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}

			if (!unitInfo.getUserid().equals(user.getDataUser())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.STATUS_TYPES, i);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.WRONG_USER.getValue(),
						AdErrorCode.WRONG_USER.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			if (adStatus[i].getStatus() == 0) {
				List<Long> idList = new ArrayList<Long>();
				idList.add(adStatus[i].getAdId());
				List<Long> stausResult = unitMgr.resumeUnit(visitor, bdUser.getUserid(), idList);
				if (stausResult == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(AdConstant.STATUS_TYPES, i);
					
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.RESUME_STATUS_ERROR.getValue(),
							AdErrorCode.RESUME_STATUS_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				
				if (stausResult.size() == 1) {
					OpTypeVo opType = OptHistoryConstant.OPTYPE_UNIT_RESUME;
					OptContent optContent = new OptContent(unitInfo.getUserid(),
							opType.getOpType(), opType.getOpLevel(), unitInfo.getUnitid(), 
							opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_PAUSE),
							opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_NORMAL));
					optContent.setGroupId(unitInfo.getGroupid().intValue());
					optContents.add(optContent);
				}
				
			} else if (adStatus[i].getStatus() == 1) {
				List<Long> idList = new ArrayList<Long>();
				idList.add(adStatus[i].getAdId());
				List<Long> stausResult = unitMgr.pauseUnit(visitor, bdUser.getUserid(), idList);
				if (stausResult == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(AdConstant.STATUS_TYPES, i);
					
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.STOP_STATUS_ERROR.getValue(),
							AdErrorCode.STOP_STATUS_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				}
				
				if (stausResult.size() == 1) {
					OpTypeVo opType = OptHistoryConstant.OPTYPE_UNIT_PAUSE;
					OptContent optContent = new OptContent(unitInfo.getUserid(),
							opType.getOpType(), opType.getOpLevel(), unitInfo.getUnitid(), 
							opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_NORMAL),
							opType.getTransformer().toDbString(CproUnitConstant.UNIT_STATE_PAUSE));
					optContent.setGroupId(unitInfo.getGroupid().intValue());
					optContents.add(optContent);
				}
			} else {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.STATUS_TYPES, i);
				apiPosition.addParam(AdConstant.STATUS);
				
				result = ApiResultBeanUtils.addApiError(result,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
						apiPosition.getPosition(), null);
				continue;
			}

			success++;
			payment.increSuccess();
		}
		result.setPayment(payment);
		if (total == success) {
			ApiResultBeanUtils.setSuccessObject(result);
		}
		
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);
		
		return result;
	}

	public ApiResult<AdType> updateAd(DataPrivilege user,
			UpdateAdRequest request, ApiOption apiOption) {
		PaymentResult payment = new PaymentResult();
		ApiResult<AdType> result = new ApiResult<AdType>();
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		if (bdUser == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.USER);
			
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}

		if (request != null && request.getAdTypes() != null) {
			if (request.getAdTypes().length > this.updateAdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TOOMANY_NUM.getValue(),
						AdErrorCode.TOOMANY_NUM.getMessage(), 
						apiPosition.getPosition(), null);
				return result;
			}
		} else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}

		AdType[] adTypes = request.getAdTypes();

		payment.setTotal(adTypes.length);

		Mapper mapper = BeanMapperProxy.getMapper();
		
		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();
		
		// 批量获取unitInfoView和group
		List<Long> adIds = new ArrayList<Long>();
		List<Integer> groupIds = new ArrayList<Integer>();
		for (AdType adType : adTypes) {
			adIds.add(adType.getAdId());
		}
		List<UnitInfoView> unitInfos = unitMgr.findUnitInfoViewsByIds(bdUser.getUserid(), adIds);
		Map<Long, UnitInfoView> unitInfoMap = new HashMap<Long, UnitInfoView>();
		Map<Integer, CproGroup> groupMap = new HashMap<Integer, CproGroup>();
		for (UnitInfoView unitInfoView : unitInfos) {
			unitInfoMap.put(unitInfoView.getUnitid(), unitInfoView);
			groupIds.add(unitInfoView.getGroupid().intValue());
		}
		List<CproGroup> cproGroups = cproGroupMgr.findCproGroupByGroupIds(groupIds);
		for (CproGroup cproGroup : cproGroups) {
			groupMap.put(cproGroup.getGroupId(), cproGroup);
		}
		
		for (int i = 0; i < adTypes.length; i++) {
			AdType adContent = adTypes[i];

			//UnitInfoView unitInfo = unitMgr.findUnitById(bdUser.getUserid(), adContent.getAdId());
			UnitInfoView unitInfo = unitInfoMap.get(adContent.getAdId());

			if (unitInfo == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, i);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NO_UNIT.getValue(), 
						AdErrorCode.NO_UNIT.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}

			if (!unitInfo.getUserid().equals(user.getDataUser())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, i);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.WRONG_USER.getValue(),
						AdErrorCode.WRONG_USER.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}

			// 如果类型转变，则有几项值为必须
			if (adContent.getType() != unitInfo.getWuliaoType()) {
				if (adContent.getType() == CproUnitConstant.MATERIAL_TYPE_LITERAL
						|| adContent.getType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
					if (adContent.getDescription1() == null) {
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(AdConstant.AD_TYPES, i);
						apiPosition.addParam(AdConstant.DESCRIPTION1);
						
						result = ApiResultBeanUtils.addApiError(result,
								AdErrorCode.DESCRIPTION_NONE.getValue(),
								AdErrorCode.DESCRIPTION_NONE.getMessage(),
								apiPosition.getPosition(), null);
						continue;
					}

					if (adContent.getDescription2() == null) {
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(AdConstant.AD_TYPES, i);
						apiPosition.addParam(AdConstant.DESCRIPTION2);
						
						result = ApiResultBeanUtils.addApiError(result,
								AdErrorCode.DESCRIPTION2_NONE.getValue(),
								AdErrorCode.DESCRIPTION2_NONE.getMessage(),
								apiPosition.getPosition(), null);
						continue;
					}
					
					if (adContent.getType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON
							&& adContent.getImageData() == null) {
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(AdConstant.AD_TYPES, i);
						apiPosition.addParam(AdConstant.IMAGE_DATA);
						
						result = ApiResultBeanUtils.addApiError(result,
								AdErrorCode.IMAGE_DATA_NONE.getValue(),
								AdErrorCode.IMAGE_DATA_NONE.getMessage(),
								apiPosition.getPosition(), null);
						continue;
					}
				} else if (adContent.getType() == CproUnitConstant.MATERIAL_TYPE_PICTURE
						|| adContent.getType() == CproUnitConstant.MATERIAL_TYPE_FLASH) {
					if (adContent.getImageData() == null) {
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(AdConstant.AD_TYPES, i);
						apiPosition.addParam(AdConstant.IMAGE_DATA);
						
						result = ApiResultBeanUtils.addApiError(result,
								AdErrorCode.IMAGE_DATA_NONE.getValue(),
								AdErrorCode.IMAGE_DATA_NONE.getMessage(),
								apiPosition.getPosition(), null);
						continue;
					}
				}
			}

			UnitInfoView2 unitInfo2 = mapper.map(unitInfo, UnitInfoView2.class);
			boolean hasImageData = false;	// 若请求没有imageData数据，则该标记为false；否则为true
			// 更新参数中的非空项
			{
				if (adContent.getType() > 0) {
					unitInfo2.setWuliaoType(adContent.getType());
				}
				if (adContent.getDescription1() != null && 
						(adContent.getType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON
								|| adContent.getType() == CproUnitConstant.MATERIAL_TYPE_LITERAL)) {
					unitInfo2.setDescription1(adContent.getDescription1());
				}
				if (adContent.getDescription2() != null && 
						(adContent.getType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON
								|| adContent.getType() == CproUnitConstant.MATERIAL_TYPE_LITERAL)) {
					unitInfo2.setDescription2(adContent.getDescription2());
				}
				if (adContent.getImageData() != null && 
						adContent.getType() > CproUnitConstant.MATERIAL_TYPE_LITERAL) {
					unitInfo2.setData(adContent.getImageData());
					String fileSrcMd5 = MD5.getMd5(adContent.getImageData());
					unitInfo2.setMaterUrlMd5(fileSrcMd5);
					unitInfo2.setHeight(adContent.getHeight());
					unitInfo2.setWidth(adContent.getWidth());
					hasImageData = true;	// 有imageData数据，标记为true
				}
				if (adContent.getDisplayUrl() != null) {
					unitInfo2.setShowUrl(adContent.getDisplayUrl());
				}
				if (adContent.getDestinationUrl() != null) {
					unitInfo2.setTargetUrl(adContent.getDestinationUrl());
				}
				if (adContent.getTitle() != null) {
					unitInfo2.setTitle(adContent.getTitle());
				}
			}
			
			UnitInfoView unit;
			if (hasImageData) {
				unit = unitInfo2;
				
				// 处理admaker生成的图片
				byte[] data = unitInfo2.getData();
				
				// 获取创意专家制作物料的templateId，无论图片的或者flash的
				boolean isImage = (unitInfo2.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_PICTURE) ? true : false;
				long tpId = ParseMC.getTemplateId(data, isImage);
								
				if (unitInfo2.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_FLASH) {
					if (tpId > 0) {
						FlashDecoder decoder = new FlashDecoder();
						DecodeResult decodeResult = decoder.decodeSwfDescJson(data);
						if (decodeResult != null && decodeResult.getStatus() == 0) {
							String descJson = null;
							descJson = decodeResult.getMessage();
							
							if (!StringUtils.isEmpty(descJson)) {
								GrantResult grantResult = amService.grantAuthority(descJson, tpId);
								
								ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
								apiPosition.addParam(AdConstant.AD_TYPES, i);
								if (grantResult.getStatusCode() == AmConstant.AM_GRANT_STATUS_RETRY_FAIL) {
									LOG.error("admaker grantAuthority retry three or more times failed for tpId=" 
											+ tpId + ", descJson=" + descJson);
									
									result = ApiResultBeanUtils.addApiError(result,
											AdErrorCode.MOD_ERROR.getValue(),
											AdErrorCode.MOD_ERROR.getMessage(), apiPosition.getPosition(), null);
									continue;
								} else if (grantResult.getStatusCode() == AmConstant.AM_GRANT_STATUS_FAIL) {
									// @cpweb649, 不走正常逻辑分支
									// 针对这种情况，也创建失败
									LOG.error("admaker grantAuthority failed for tpId=" + tpId + ", descJson=" + descJson 
											+ ", statusCode=" + grantResult.getStatusCode());
									result = ApiResultBeanUtils.addApiError(result,
											AdErrorCode.MOD_ERROR.getValue(),
											AdErrorCode.MOD_ERROR.getMessage(), apiPosition.getPosition(), null);
									continue;
								} else if (grantResult.getStatusCode() == AmConstant.AM_GRANT_STATUS_OK) {
									Long mcId = grantResult.getMcId();
									Integer versionId = grantResult.getVersionId();
									
									unit = mapper.map(unitInfo2, UnitInfoView.class);
									boolean flag = unitMgr.fillUnitUrlInfoFromUbmc(mcId, versionId, unit);
									if (!flag) {
										LOG.error("get material filesrc from ubmc failed for mcId=" + mcId + ", versionId=" + versionId);
										result = ApiResultBeanUtils.addApiError(result,
												AdErrorCode.MOD_ERROR.getValue(),
												AdErrorCode.MOD_ERROR.getMessage(), apiPosition.getPosition(), null);
										continue;
									}
								} else {
									// 其他error，无法处理
									LOG.error("admaker grantAuthority failed for tpId=" + tpId + ", descJson=" + descJson);
									result = ApiResultBeanUtils.addApiError(result,
											AdErrorCode.MOD_ERROR.getValue(),
											AdErrorCode.MOD_ERROR.getMessage(), apiPosition.getPosition(), null);
									continue;
								}
							} else {
								LOG.error("descJson is null or empty for tpId=" + tpId);
							}
						} else {
							LOG.error("FlashDecoder decode flash failed for tpId=" + tpId);
						}
					}
				}
				
				// 设置admaker物料模板id值
				unit.setAmTemplateId(tpId);
		        // 如果本地上传图片或flash检测出admaker的模板id，设置对应unitSource为admaker
                if (tpId > 0) {
                    unit.setUnitSource(CproUnitConstant.UNIT_SOURCE_ADMAKER);
                } else {
                    unit.setUnitSource(CproUnitConstant.UNIT_SOURCE_COMMON);
                }
			} else {
				unit = mapper.map(unitInfo2, UnitInfoView.class);
			}
			
			// 非图片或者flash的物料，清空amTemplateId
			if (adContent.getType() > 0 
			        && adContent.getType() != CproUnitConstant.MATERIAL_TYPE_PICTURE
					&& adContent.getType() != CproUnitConstant.MATERIAL_TYPE_FLASH) {
				unit.setAmTemplateId(0L);
			}

			int groupId = unit.getGroupid().intValue();
			CproGroup cproGroup = groupMap.get(groupId);
			
			if (cproGroup.getIsSmart() == CproGroupConstant.IS_SMART_TRUE) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, i);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.SMARTIDEA_GROUP_CANNOT_ADD_NORMAL_CREATIVE.getValue(), 
						AdErrorCode.SMARTIDEA_GROUP_CANNOT_ADD_NORMAL_CREATIVE.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			boolean validateResult = unitService.validateUnit(i, unit, cproGroup.getGroupType(), result, bdUser);

			if (validateResult == true) {
				// 修改前物料信息
				OpTypeVo opType = OptHistoryConstant.OPTYPE_UNIT_MOD;
				ModUnitOptVo preContent = new ModUnitOptVo(unitInfo.getTitle(), 
						unitInfo.getDescription1(), unitInfo.getDescription2(),
						unitInfo.getShowUrl(), unitInfo.getTargetUrl(), unitInfo.getWuliaoType(), 
						unitInfo.getInteractive(), unitInfo.getWirelessShowUrl(), unitInfo.getWirelessTargetUrl());
				
				boolean isSucc = true;
				// 如果是图文物料，则添加icon
				if (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON
						&& unit instanceof UnitInfoView2) {
					boolean flag = unitService.addIcon(i, (UnitInfoView2)unit, result, bdUser);
					if (!flag) {
						continue;
					}
				}
				
				Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
				try {
					// add for bmob
					fillWirelessProperties4AdUpdate(bdUser, unit, groupId);
					
					// @version cpweb641, @author genglei
					// 过滤特殊字符集合
					UnitBeanUtils.filterSpecialChar(unit);
					
					unitMgr.modUnit(visitor, unit, true, false);
					boolean isPicModified = getMdPicFlagForOpHistory(unitInfo, unit, false);
					
					if (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_PICTURE
							|| unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_FLASH
							|| unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
						String url = unitMgr.getTmpUrl(unit.getMcId(), unit.getMcVersionId());
						unit.setMaterUrl(StringUtils.isEmpty(url) ? null : url);
					}
					unit.getStateView().setViewState(CproUnitConstant.UNIT_STATE_AUDITING);
					
					// 修改后物料信息
					ModUnitOptVo postContent = new ModUnitOptVo(unit.getTitle(), 
							unit.getDescription1(), unit.getDescription2(),
							unit.getShowUrl(), unit.getTargetUrl(), unit.getWuliaoType(),
							unit.getInteractive(), unit.getWirelessShowUrl(), unit.getWirelessTargetUrl());
					postContent.setMdPic(isPicModified);
					
					// 记录操作历史
					OptContent optContent = new OptContent(unitInfo.getUserid(),
							opType.getOpType(),	opType.getOpLevel(), unitInfo.getUnitid(),
							opType.getTransformer().toDbString(preContent), 
							opType.getTransformer().toDbString(postContent));
					optContent.setGroupId(unitInfo.getGroupid().intValue());
					optContents.add(optContent);
				} catch (UnitUpdateStateException e) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(AdConstant.AD_TYPES, i);
					
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.MOD_STATUS_ERROR.getValue(),
							AdErrorCode.MOD_STATUS_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					isSucc = false;
				} catch (CproUnitUpdateException e) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(AdConstant.AD_TYPES, i);
					
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.MOD_ERROR.getValue(),
							AdErrorCode.MOD_ERROR.getMessage(), apiPosition.getPosition(), null);
					isSucc = false;
				}

				if (isSucc) {
					AdType adType = mapper.map(unit, AdType.class);
					adType.setLocalId(adContent.getLocalId());
					result = ApiResultBeanUtils.addApiResult(result, adType);
					
					payment.increSuccess();	
				}
			}
		}

		result.setPayment(payment);
		
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);
		
		return result;
	}
	
	private void fillWirelessProperties4AdUpdate(User bdUser, UnitInfoView unit, int groupId) throws CproUnitUpdateException {
		List<Integer> groupIdList = new ArrayList<Integer>();
		groupIdList.add(groupId);
		Map<Integer, Integer> groupPromotionTypeMapping = cproGroupMgr.getGroupPromotionTypeMapping(bdUser.getUserid(), groupIdList);
		if (MapUtils.isEmpty(groupPromotionTypeMapping)) {
			throw new CproUnitUpdateException("can't find groupPromotionTypeMapping for groupIds: " + groupIdList);
		}
		int promotionType = groupPromotionTypeMapping.get(groupId);
		if (promotionType == CproPlanConstant.PROMOTIONTYPE_WIRELESS) {
			unit.setWirelessShowUrl(unit.getShowUrl());
			unit.setWirelessTargetUrl(unit.getTargetUrl());
		}
	}
	
	// 获取图片是否修改标识，用于历史记录，仅用于历史记录，对于批量替换创意单独走一个逻辑判断分支
	private static boolean getMdPicFlagForOpHistory(UnitInfoView preUnit,
			UnitInfoView unit, boolean isReplaced) {
		boolean isPicModified = false;

		int[] picWuliaoTypes = new int[] {
				CproUnitConstant.MATERIAL_TYPE_PICTURE,
				CproUnitConstant.MATERIAL_TYPE_FLASH };

		if (ArrayUtils.contains(picWuliaoTypes, preUnit.getWuliaoType())
				&& ArrayUtils.contains(picWuliaoTypes, unit.getWuliaoType())) {
			// 图片-->图片
			if (unit instanceof UnitInfoView2) {// 表明是从本地上传，都对图片变更了
				isPicModified = true;
			} else if (!unit.getFileSrc().equals(preUnit.getFileSrc()) && isReplaced) {// 或者fileSrc变更了并且是批量替换创意
				isPicModified = true;
			}

		} else if (preUnit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON
				&& unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
			// 图文-->图文
			if (!unit.getIconId().equals(preUnit.getIconId())) {
				isPicModified = true;
			}
		} else {
			// 其它情况：图文-->图文/文字、文字-->文字/图文/图片、图片-->文字、图文
			isPicModified = false;
		}
		
		return isPicModified;
	}
	
	public ApiResult<Object> replaceAd(DataPrivilege user,
			ReplaceAdRequest request, ApiOption apiOption) {
		
		ApiResult<Object> result = new ApiResult<Object>();
		PaymentResult payment = result.getPayment();
		
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		if (bdUser == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.USER);
			
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		int userId = bdUser.getUserid();

		if (request != null && !ArrayUtils.isEmpty(request.getAdIds()) 
				&& request.getAdId() > 0) {
			if (request.getAdIds().length > this.replaceAdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TOOMANY_REPLACE_AD_ERROR.getValue(),
						AdErrorCode.TOOMANY_REPLACE_AD_ERROR.getMessage(), 
						apiPosition.getPosition(), null);
				return result;
			}
		} else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		long[] adIds = request.getAdIds();
		payment.setTotal(adIds.length);
		
		// 获取替换的创意信息内容
		UnitInfoView srcUnit = unitMgr.findUnitById(userId, request.getAdId());
		if (srcUnit == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_ID);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.NO_UNIT.getValue(), 
					AdErrorCode.NO_UNIT.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		if (srcUnit.getStateView().getViewState() == CproUnitConstant.UNIT_STATE_DELETE) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_ID);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.REPLACE_AD_STATE_ERROR.getValue(), 
					AdErrorCode.REPLACE_AD_STATE_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		CproGroup cproGroup = cproGroupMgr.findCproGroupById(srcUnit.getGroupid().intValue());
		if (cproGroup == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_ID);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.NO_GROUP.getValue(),
					AdErrorCode.NO_GROUP.getMessage(),
					apiPosition.getPosition(), null);
			
			return result;
		}

		if (cproGroup.getIsSmart() == CproGroupConstant.IS_SMART_TRUE) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_ID);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.SMARTIDEA_GROUP_CANNOT_ADD_NORMAL_CREATIVE.getValue(),
					AdErrorCode.SMARTIDEA_GROUP_CANNOT_ADD_NORMAL_CREATIVE.getMessage(),
					apiPosition.getPosition(), null);
			
			return result;
		}
		
		int total = request.getAdIds().length;
		int success = 0;
		
		// 验证“待复制的创意”和“复制到的推广组”的“推广类型”是否一致 add for bmob
		List<Long> srcUnitIdList = new ArrayList<Long>();
		srcUnitIdList.add(request.getAdId());
		Map<Long, Integer> srcUnitPromotionTypeMapping = unitMgr.getUnitPromotionTypeMapping(userId, srcUnitIdList);
		if (MapUtils.isEmpty(srcUnitPromotionTypeMapping)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_ID);

			result = ApiResultBeanUtils.addApiError(result, AdErrorCode.REPLACE_SRC_AD_PROMOTION_TYPE_ERROR.getValue(), AdErrorCode.REPLACE_SRC_AD_PROMOTION_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
			return result;
		}
		List<Long> targetUnitIdList = CollectionsUtil.tranformLongArrayToLongList(adIds);
		Map<Long, Integer> targetUnitPromotionTypeMapping = unitMgr.getUnitPromotionTypeMapping(userId, targetUnitIdList);
		if (MapUtils.isEmpty(targetUnitPromotionTypeMapping)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_IDS);

			result = ApiResultBeanUtils.addApiError(result, AdErrorCode.REPLACE_TARGET_AD_PROMOTION_TYPE_ERROR.getValue(), AdErrorCode.REPLACE_TARGET_AD_PROMOTION_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
			return result;
		}

		boolean isPromotionTypeTheSame = isUnitsPromotionTypeTheSame(srcUnitPromotionTypeMapping.get(request.getAdId()), targetUnitPromotionTypeMapping);
		if (!isPromotionTypeTheSame) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_IDS);

			result = ApiResultBeanUtils.addApiError(result, AdErrorCode.REPLACE_AD_PROMOTION_TYPE_ERROR.getValue(), AdErrorCode.REPLACE_AD_PROMOTION_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
			return result;
		}
		// 根据“待拷贝创意”和 “目标推广组的”推广类型判断是否需要验证“域名绑定” add for bmob
		int promotionType = srcUnitPromotionTypeMapping.get(request.getAdId());
		boolean needVerifyUrlBinding = (promotionType == CproPlanConstant.PROMOTIONTYPE_ALL) ? true : false;
		
		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (int index = 0; index < adIds.length; index++) {
			long adId = adIds[index];
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_IDS, index);
			
			UnitInfoView unitInfo = unitMgr.findUnitById(userId, adId);
			
			if (unitInfo == null) {
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NO_UNIT.getValue(), 
						AdErrorCode.NO_UNIT.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			if (unitInfo.getStateView().getViewState() == CproUnitConstant.UNIT_STATE_DELETE) {
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.REPLACE_AD_STATE_ERROR.getValue(), 
						AdErrorCode.REPLACE_AD_STATE_ERROR.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			if (!unitInfo.getUserid().equals(user.getDataUser())) {				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.WRONG_USER.getValue(),
						AdErrorCode.WRONG_USER.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			
			// 修改前物料信息
			OpTypeVo opType = OptHistoryConstant.OPTYPE_UNIT_MOD;
			ModUnitOptVo preContent = new ModUnitOptVo(unitInfo.getTitle(), 
					unitInfo.getDescription1(), unitInfo.getDescription2(),
					unitInfo.getShowUrl(), unitInfo.getTargetUrl(), unitInfo.getWuliaoType(), 
					unitInfo.getInteractive(), unitInfo.getWirelessShowUrl(),unitInfo.getWirelessTargetUrl());
			boolean isPicModified = getMdPicFlagForOpHistory(unitInfo, srcUnit, true);
			
			// 更新参数中的非空项
			{
				if (StringUtils.isNotEmpty(srcUnit.getDescription1())) {
					unitInfo.setDescription1(srcUnit.getDescription1());
				}
				if (StringUtils.isNotEmpty(srcUnit.getDescription2())) {
					unitInfo.setDescription2(srcUnit.getDescription2());
				}
				if (StringUtils.isNotEmpty(srcUnit.getFileSrc())) {
					unitInfo.setFileSrc(srcUnit.getFileSrc());
					unitInfo.setHeight(srcUnit.getHeight());
					unitInfo.setWidth(srcUnit.getWidth());
					unitInfo.setMaterUrlMd5(srcUnit.getMaterUrlMd5());
					
					// 设置admaker物料相关参数
					unitInfo.setAttribute(srcUnit.getAttribute());
					unitInfo.setRefMcId(srcUnit.getRefMcId());
					unitInfo.setDescInfo(srcUnit.getDescInfo());
				}
				if (StringUtils.isNotEmpty(srcUnit.getShowUrl())) {
					unitInfo.setShowUrl(srcUnit.getShowUrl());
				}
				if (StringUtils.isNotEmpty(srcUnit.getTargetUrl())) {
					unitInfo.setTargetUrl(srcUnit.getTargetUrl());
				}
				if (StringUtils.isNotEmpty(srcUnit.getTitle())) {
					unitInfo.setTitle(srcUnit.getTitle());
				}
				if (srcUnit.getWuliaoType() > 0) {
					if (srcUnit.getWuliaoType() > 1) {
						
					}
					unitInfo.setWuliaoType(srcUnit.getWuliaoType());
				}
				if (srcUnit.getIconId() != null && srcUnit.getIconId() > 0) {
					unitInfo.setIconId(srcUnit.getIconId());
				}
				// add for bmob
				if (StringUtils.isNotEmpty(srcUnit.getWirelessShowUrl())) {
					unitInfo.setWirelessShowUrl(srcUnit.getWirelessShowUrl());
				}
				if (StringUtils.isNotEmpty(srcUnit.getWirelessTargetUrl())) {
					unitInfo.setWirelessTargetUrl(srcUnit.getWirelessTargetUrl());
				}
				
				// 拷贝创意专家模板ID
				unitInfo.setAmTemplateId(srcUnit.getAmTemplateId());
				// 拷贝创意来源
				unitInfo.setUnitSource(srcUnit.getUnitSource());
			}
			
			boolean validateResult = unitService.validateReplaceUnit(index, unitInfo,
					cproGroup.getGroupType(), result, bdUser, needVerifyUrlBinding);

			if (validateResult == true) {

				Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
				try {
					// 对于批量替换接口，物料发生变化，同时无需进行仅修改移动链接的标记
					unitMgr.modUnit(visitor, unitInfo, true, false);
					

					if (unitInfo.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_PICTURE
							|| unitInfo.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_FLASH
							|| unitInfo.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
						String url = unitMgr.getTmpUrl(unitInfo.getMcId(), unitInfo.getMcVersionId());
						unitInfo.setMaterUrl(StringUtils.isEmpty(url) ? null : url);
					}
					
					// 修改后物料信息
					ModUnitOptVo postContent = new ModUnitOptVo(unitInfo.getTitle(), 
							unitInfo.getDescription1(), unitInfo.getDescription2(),
							unitInfo.getShowUrl(), unitInfo.getTargetUrl(), unitInfo.getWuliaoType(), 
							unitInfo.getInteractive(), unitInfo.getWirelessShowUrl(), unitInfo.getWirelessTargetUrl());
					postContent.setMdPic(isPicModified);
					
					// 记录操作历史
					OptContent optContent = new OptContent(unitInfo.getUserid(),
							opType.getOpType(),	opType.getOpLevel(), unitInfo.getUnitid(),
							opType.getTransformer().toDbString(preContent), 
							opType.getTransformer().toDbString(postContent));
					optContent.setGroupId(unitInfo.getGroupid().intValue());
					optContents.add(optContent);
				} catch (UnitUpdateStateException e) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.MOD_STATUS_ERROR.getValue(),
							AdErrorCode.MOD_STATUS_ERROR.getMessage(),
							apiPosition.getPosition(), null);
					continue;
				} catch (CproUnitUpdateException e) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.MOD_ERROR.getValue(),
							AdErrorCode.MOD_ERROR.getMessage(), apiPosition.getPosition(), null);

					continue;
				}

				payment.increSuccess();
				success++;
			}
		}
		
		if (total == success) {
			ApiResultBeanUtils.setSuccessObject(result);
		}
		
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);
		
		return result;
	}
	
	public ApiResult<Object> copyAd(DataPrivilege user,
			CopyAdRequest request, ApiOption apiOption) {
		ApiResult<Object> result = new ApiResult<Object>();
		PaymentResult payment = result.getPayment();
		
		result = ApiResultBeanUtils.validateUser(result, user);
		if (result.hasErrors()) {
			return result;
		}

		User bdUser = (User) SessionHolder.getSession().get(ApiConstant.KEY_SESSION_USER);
		if (bdUser == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.USER);
			
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), 
					UserErrorCode.NO_USER.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		int userId = bdUser.getUserid();

		if (request != null && !ArrayUtils.isEmpty(request.getAdIds()) 
				&& !ArrayUtils.isEmpty(request.getGroupIds())) {
			if (request.getAdIds().length > this.copyAdForAdMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TOOMANY_COPY_AD_ERROR.getValue(),
						AdErrorCode.TOOMANY_COPY_AD_ERROR.getMessage(), 
						apiPosition.getPosition(), null);
				return result;
			}
			if (request.getGroupIds().length > this.copyAdForGroupMax) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.GROUP_IDS);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TOOMANY_COPY_GROUP_ERROR.getValue(),
						AdErrorCode.TOOMANY_COPY_GROUP_ERROR.getMessage(), 
						apiPosition.getPosition(), null);
				return result;
			}
		} else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.NOPARAM);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		long[] adIds = request.getAdIds();
		long[] groupIds = request.getGroupIds();
		payment.setTotal(groupIds.length);	// 成功次数以推广组个数为主，按照推广组计数
		
		List<UnitInfoView> unitList = new ArrayList<UnitInfoView>();
		for (int index = 0; index < adIds.length; index++) {
			Long adId = adIds[index];
			
			UnitInfoView unitInfo = unitMgr.findUnitById(userId, adId);
			if (unitInfo == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NO_UNIT.getValue(), 
						AdErrorCode.NO_UNIT.getMessage(), 
						apiPosition.getPosition(), null);
				continue;
			}
			unitList.add(unitInfo);
		}
		
		// 如果已经存在错误，则返回
		if (result.hasErrors()) {
			return result;
		}
		
		List<CproGroup> groupList = new ArrayList<CproGroup>();
		Set<Long> groupIdSet = new HashSet<Long>();
		for (int index = 0; index < groupIds.length; index++) {
			Long groupId = groupIds[index];
			if (groupIdSet.contains(groupId)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.GROUP_IDS, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.COPY_GROUP_DUP_ERROR.getValue(),
						AdErrorCode.COPY_GROUP_DUP_ERROR.getMessage(),
						apiPosition.getPosition(), null);
				
				continue;
			}
			groupIdSet.add(groupId);
			
			CproGroup cproGroup = cproGroupMgr.findCproGroupById(groupId.intValue());
			if (cproGroup == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.GROUP_IDS, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NO_GROUP.getValue(),
						AdErrorCode.NO_GROUP.getMessage(),
						apiPosition.getPosition(), null);
				
				continue;
			}
			
			if (cproGroup.getIsSmart() == CproGroupConstant.IS_SMART_TRUE) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.GROUP_IDS, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.SMARTIDEA_GROUP_CANNOT_ADD_NORMAL_CREATIVE.getValue(),
						AdErrorCode.SMARTIDEA_GROUP_CANNOT_ADD_NORMAL_CREATIVE.getMessage(),
						apiPosition.getPosition(), null);
				
				continue;
			}
			
			groupList.add(cproGroup);
		}
		
		// 如果已经存在错误，则返回
		if (result.hasErrors()) {
			return result;
		}
		
		// 验证“待复制的创意”和“复制到的推广组”的“推广类型”是否一致 add for bmob
		List<Integer> groupIdList = CollectionsUtil.tranformLongArrayToIntList(groupIds);
		Map<Integer, Integer> groupPromotionTypeMapping = cproGroupMgr.getGroupPromotionTypeMapping(userId, groupIdList);
		if (MapUtils.isEmpty(groupPromotionTypeMapping)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.GROUP_IDS);
			
			result = ApiResultBeanUtils.addApiError(result, AdErrorCode.COPY_GROUP_PROMOTION_TYPE_ERROR.getValue(), AdErrorCode.COPY_GROUP_PROMOTION_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
			return result;
		}

		List<Long> unitIdList = CollectionsUtil.tranformLongArrayToLongList(adIds);
		Map<Long, Integer> unitPromotionTypeMapping = unitMgr.getUnitPromotionTypeMapping(userId, unitIdList);
		if (MapUtils.isEmpty(unitPromotionTypeMapping)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_IDS);

			result = ApiResultBeanUtils.addApiError(result, AdErrorCode.COPY_AD_PROMOTION_TYPE_ERROR.getValue(), AdErrorCode.COPY_AD_PROMOTION_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
			return result;
		}
		boolean isPromotionTypeTheSame = isUnitsAndGroupsPromotionTypeTheSame(groupPromotionTypeMapping, unitPromotionTypeMapping);
		if (!isPromotionTypeTheSame) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.GROUP_IDS);

			result = ApiResultBeanUtils.addApiError(result, AdErrorCode.COPY_GROUP_AD_PROMOTION_TYPE_ERROR.getValue(), AdErrorCode.COPY_GROUP_AD_PROMOTION_TYPE_ERROR.getMessage(), apiPosition.getPosition(), null);
			return result;
		}
		// 根据“待拷贝创意”和 “目标推广组的”推广类型判断是否需要验证“域名绑定” add for bmob
		int promotionType = groupPromotionTypeMapping.get(groupIdList.get(0));
		boolean needVerifyUrlBinding = (promotionType == CproPlanConstant.PROMOTIONTYPE_ALL) ? true : false;

		// 验证创意信息，包括：所有创意能够获取，所有创意的推广组类型与目标推广组类型一致等
		boolean flag = unitService.validateCopyUnit(unitList, groupList, result, bdUser, needVerifyUrlBinding);
		if (!flag) {
			return result;
		}
		 
		int total = groupIds.length;
		int success = 0;
		
		// 历史操作记录保存对象
		List<OptContent> optContents = new ArrayList<OptContent>();
		for (int index = 0; index < groupList.size(); index++) {
			CproGroup group = groupList.get(index);
			
			boolean isSuccess = unitService.copyUnit(index, unitList, group, 
					result, bdUser, user.getOpUser(), optContents);
			
			if (isSuccess) {
				payment.increSuccess();
				success++;
			}
		}
		
		if (total == success) {
			ApiResultBeanUtils.setSuccessObject(result);
		}
		
		// 加入session中，后续有拦截器处理
		SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents);
		
		return result;
	}
	
	private boolean isUnitsAndGroupsPromotionTypeTheSame(Map<Integer, Integer> groupPromotionTypeMapping, Map<Long, Integer> unitPromotionTypeMapping) {
		int groupAllCount = 0;
		int groupWirelessCount = 0;
		int groupPromotionType = CproPlanConstant.PROMOTIONTYPE_ALL;

		int unitAllCount = 0;
		int unitWirelessCount = 0;
		int unitPromotionType = CproPlanConstant.PROMOTIONTYPE_ALL;

		for (int promotionType : groupPromotionTypeMapping.values()) {
			if (promotionType == CproPlanConstant.PROMOTIONTYPE_ALL) {
				groupAllCount++;
			} else {
				groupWirelessCount++;
			}
		}

		for (int promotionType : unitPromotionTypeMapping.values()) {
			if (promotionType == CproPlanConstant.PROMOTIONTYPE_ALL) {
				unitAllCount++;
			} else {
				unitWirelessCount++;
			}
		}

		if (groupAllCount == groupPromotionTypeMapping.size()) {
			groupPromotionType = CproPlanConstant.PROMOTIONTYPE_ALL;
		} else if (groupWirelessCount == groupPromotionTypeMapping.size()) {
			groupPromotionType = CproPlanConstant.PROMOTIONTYPE_WIRELESS;
		} else {
			return false;
		}

		if (unitAllCount == unitPromotionTypeMapping.size()) {
			unitPromotionType = CproPlanConstant.PROMOTIONTYPE_ALL;
		} else if (unitWirelessCount == unitPromotionTypeMapping.size()) {
			unitPromotionType = CproPlanConstant.PROMOTIONTYPE_WIRELESS;
		} else {
			return false;
		}

		if (groupPromotionType != unitPromotionType) {
			return false;
		}

		return true;
	}
	
	private boolean isUnitsPromotionTypeTheSame(int srcUnitPromotionType, Map<Long, Integer> targetUnitPromotionTypeMapping) {

		int targetUnitAllCount = 0;
		int targetUnitWirelessCount = 0;
		int targetUnitPromotionType = CproPlanConstant.PROMOTIONTYPE_ALL;

		for (int promotionType : targetUnitPromotionTypeMapping.values()) {
			if (promotionType == CproPlanConstant.PROMOTIONTYPE_ALL) {
				targetUnitAllCount++;
			} else {
				targetUnitWirelessCount++;
			}
		}

		if (targetUnitAllCount == targetUnitPromotionTypeMapping.size()) {
			targetUnitPromotionType = CproPlanConstant.PROMOTIONTYPE_ALL;
		} else if (targetUnitWirelessCount == targetUnitPromotionTypeMapping.size()) {
			targetUnitPromotionType = CproPlanConstant.PROMOTIONTYPE_WIRELESS;
		} else {
			return false;
		}

		if (srcUnitPromotionType != targetUnitPromotionType) {
			return false;
		}

		return true;
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public CproUnitMgr getUnitMgr() {
		return unitMgr;
	}

	public void setUnitMgr(CproUnitMgr unitMgr) {
		this.unitMgr = unitMgr;
	}

	public UnitService getUnitService() {
		return unitService;
	}

	public void setUnitService(UnitService unitService) {
		this.unitService = unitService;
	}

	private static final char[] S_BASE64CHAR = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '+', '/' };
	private static final char S_BASE64PAD = '=';
	private static final byte[] S_DECODETABLE = new byte[128];
	static {
		for (int i = 0; i < S_DECODETABLE.length; i++)
			S_DECODETABLE[i] = Byte.MAX_VALUE; // 127
		for (int i = 0; i < S_BASE64CHAR.length; i++)
			// 0 to 63
			S_DECODETABLE[S_BASE64CHAR[i]] = (byte) i;
	}

	private static int decode0(char[] ibuf, byte[] obuf, int wp)
			throws UnsupportedEncodingException {
		int outlen = 3;
		if (ibuf[3] == S_BASE64PAD)
			outlen = 2;
		if (ibuf[2] == S_BASE64PAD)
			outlen = 1;
		int b0 = S_DECODETABLE[ibuf[0]];
		int b1 = S_DECODETABLE[ibuf[1]];
		int b2 = S_DECODETABLE[ibuf[2]];
		int b3 = S_DECODETABLE[ibuf[3]];
		switch (outlen) {
		case 1:
			obuf[wp] = (byte) (b0 << 2 & 0xfc | b1 >> 4 & 0x3);
			return 1;
		case 2:
			obuf[wp++] = (byte) (b0 << 2 & 0xfc | b1 >> 4 & 0x3);
			obuf[wp] = (byte) (b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
			return 2;
		case 3:
			obuf[wp++] = (byte) (b0 << 2 & 0xfc | b1 >> 4 & 0x3);
			obuf[wp++] = (byte) (b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
			obuf[wp] = (byte) (b2 << 6 & 0xc0 | b3 & 0x3f);
			return 3;
		default:
			throw new UnsupportedEncodingException();
		}
	}

	/**
	 * 将string型转化为字节数组
	 * 
	 * @param String
	 * @return byte[]
	 * @author modi
	 * @version 1.0.0
	 */
	public static byte[] stringToBytes(String input)
			throws UnsupportedEncodingException {
		// byte[] result=null;
		// if(input!=null){
		// result=input.getBytes("GBK");
		// }
		// byte[] finalret = new byte[result.length+1];
		// System.arraycopy(result, 0, finalret, 0, result.length);
		//	
		// return finalret;

		char[] ibuf = new char[4];
		int ibufcount = 0;
		if (input == null) {
			return null;
		}
		byte[] obuf = new byte[input.length() / 4 * 3 + 3];
		int obufcount = 0;
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (ch == S_BASE64PAD || ch < S_DECODETABLE.length
					&& S_DECODETABLE[ch] != Byte.MAX_VALUE) {
				ibuf[ibufcount++] = ch;
				if (ibufcount == ibuf.length) {
					ibufcount = 0;
					obufcount += decode0(ibuf, obuf, obufcount);
				}
			}
		}
		if (obufcount == obuf.length)
			return obuf;
		byte[] ret = new byte[obufcount];
		System.arraycopy(obuf, 0, ret, 0, obufcount);
		return ret;

	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}
	
	public AmService getAmService() {
		return amService;
	}

	public void setAmService(AmService amService) {
		this.amService = amService;
	}

	public void setAddAdMax(int addAdMax) {
		this.addAdMax = addAdMax;
	}

	public void setGetAdByAdIdMax(int getAdByAdIdMax) {
		this.getAdByAdIdMax = getAdByAdIdMax;
	}

	public void setUpdateAdMax(int updateAdMax) {
		this.updateAdMax = updateAdMax;
	}

	public void setDeleteAdMax(int deleteAdMax) {
		this.deleteAdMax = deleteAdMax;
	}

	public void setSetAdStatusMax(int setAdStatusMax) {
		this.setAdStatusMax = setAdStatusMax;
	}

	public void setReplaceAdMax(int replaceAdMax) {
		this.replaceAdMax = replaceAdMax;
	}

	public void setCopyAdForAdMax(int copyAdForAdMax) {
		this.copyAdForAdMax = copyAdForAdMax;
	}

	public void setCopyAdForGroupMax(int copyAdForGroupMax) {
		this.copyAdForGroupMax = copyAdForGroupMax;
	}
}
