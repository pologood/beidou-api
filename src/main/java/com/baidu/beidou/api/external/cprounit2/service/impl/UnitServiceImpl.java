package com.baidu.beidou.api.external.cprounit2.service.impl;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;

import com.baidu.beidou.api.external.cprounit2.constant.AdConstant;
import com.baidu.beidou.api.external.cprounit2.error.AdErrorCode;
import com.baidu.beidou.api.external.cprounit2.service.UnitService;
import com.baidu.beidou.api.external.cprounit2.vo.AdType;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.util.GroupTypeUtil;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.constant.CproUnitConfig;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.dao.UnitIconDao;
import com.baidu.beidou.cprounit.exception.CproUnitAddException;
import com.baidu.beidou.cprounit.icon.bo.SystemIcon;
import com.baidu.beidou.cprounit.icon.bo.UserUploadIcon;
import com.baidu.beidou.cprounit.icon.service.IconMgr;
import com.baidu.beidou.cprounit.mcdriver.bean.response.GrantResult;
import com.baidu.beidou.cprounit.mcdriver.constant.AmConstant;
import com.baidu.beidou.cprounit.mcdriver.mcparser.ParseMC;
import com.baidu.beidou.cprounit.service.AmService;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.UbmcService;
import com.baidu.beidou.cprounit.service.UnitBeanUtils;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestBaseMaterial;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestIconMaterial;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestLite;
import com.baidu.beidou.cprounit.ubmcdriver.material.response.ResponseBaseMaterial;
import com.baidu.beidou.cprounit.ubmcdriver.material.response.ResponseIconMaterial;
import com.baidu.beidou.cprounit.validate.ImageScale;
import com.baidu.beidou.cprounit.validate.InvalidUnitException;
import com.baidu.beidou.cprounit.validate.UnitAkaAudit;
import com.baidu.beidou.cprounit.validate.UnitInfoAudit;
import com.baidu.beidou.cprounit.vo.CprounitWirelessUrlBoolean;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.cprounit.vo.UnitInfoView2;
import com.baidu.beidou.cprounit.vo.UnitQuery;
import com.baidu.beidou.exception.ValidationException;
import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.user.service.UserInfoMgr;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.BeanMapperProxy;
import com.baidu.beidou.util.MD5;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.akadriver.constant.AKA_RES_CODE;
import com.baidu.chuangyi.flash.decode.DecodeResult;
import com.baidu.chuangyi.flash.decode.FlashDecoder;

/**
 * ClassName: UnitServiceImpl
 * Function: 推广创意设置
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-15
 */
public class UnitServiceImpl implements UnitService {
	
	private static final Log LOG = LogFactory.getLog(UnitServiceImpl.class);

	private CproUnitMgr unitMgr = null;
	private CproGroupMgr cproGroupMgr = null;
	private IconMgr iconMgr = null;
	private UbmcService ubmcService;
	private AmService amService;
	private UserMgr userMgr;
	private UserInfoMgr userInfoMgr;
	private CproPlanMgr planMgr;
	private UnitIconDao unitIconDao;

	@SuppressWarnings("unused")
	public boolean addUnit(int index, AdType adType,
			ApiResult<AdType> result, User bdUser, Map<Integer, CproGroup> groupIdEntityMap, int totalUnitCount, List<OptContent> optContents) {

		StopWatch sw = new StopWatch();
		sw.start();
		
		// 处理admaker生成的图片
		byte[] data = adType.getImageData();
		Mapper mapper = BeanMapperProxy.getMapper();
		UnitInfoView unit = mapper.map(adType, UnitInfoView2.class);
		unit.setUnitid(null);
		if (data != null && data.length > 0) {
			((UnitInfoView2)unit).setData(data);
			String fileSrcMd5 = MD5.getMd5(data);
			unit.setMaterUrlMd5(fileSrcMd5);
		}
		unit.setUserid(bdUser.getUserid());
		
		// 获取创意专家制作物料的templateId，无论图片的或者flash的
		boolean isImage = (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_PICTURE) ? true : false;
		long tpId = ParseMC.getTemplateId(data, isImage);
		
		if (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_FLASH) {
			if (tpId > 0) {
				FlashDecoder decoder = new FlashDecoder();
				DecodeResult decodeResult = decoder.decodeSwfDescJson(data);
				if (decodeResult != null && decodeResult.getStatus() == 0) {
					String descJson = null;
					descJson = decodeResult.getMessage();
					
					if (!StringUtils.isEmpty(descJson)) {
						GrantResult grantResult = amService.grantAuthority(descJson, tpId);
						
						ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
						apiPosition.addParam(AdConstant.AD_TYPES, index);
						if (grantResult.getStatusCode() == AmConstant.AM_GRANT_STATUS_RETRY_FAIL) {
							LOG.error("admaker grantAuthority retry three or more times failed for tpId=" 
									+ tpId + ", descJson=" + descJson);
							
							result = ApiResultBeanUtils.addApiError(result,
									AdErrorCode.ADD_AD_ERROR.getValue(),
									AdErrorCode.ADD_AD_ERROR.getMessage(), apiPosition.getPosition(), null);
							return false;
						} else if (grantResult.getStatusCode() == AmConstant.AM_GRANT_STATUS_FAIL) {
							// @cpweb649, 不走正常逻辑分支
							// 针对这种情况，也创建失败
							LOG.error("admaker grantAuthority failed for tpId=" + tpId + ", descJson=" + descJson 
									+ ", statusCode=" + grantResult.getStatusCode());
							result = ApiResultBeanUtils.addApiError(result,
									AdErrorCode.ADD_AD_ERROR.getValue(),
									AdErrorCode.ADD_AD_ERROR.getMessage(), apiPosition.getPosition(), null);
							return false;
						} else if (grantResult.getStatusCode() == AmConstant.AM_GRANT_STATUS_OK) {
							Long mcId = grantResult.getMcId();
							Integer versionId = grantResult.getVersionId();
							
							unit = mapper.map(adType, UnitInfoView.class);
							unit.setUnitid(null);
							unit.setUserid(bdUser.getUserid());
							boolean flag = unitMgr.fillUnitUrlInfoFromUbmc(mcId, versionId, unit);
							if (!flag) {
								LOG.error("get material filesrc from ubmc failed for mcId=" + mcId + ", versionId=" + versionId);
								result = ApiResultBeanUtils.addApiError(result,
										AdErrorCode.ADD_AD_ERROR.getValue(),
										AdErrorCode.ADD_AD_ERROR.getMessage(), apiPosition.getPosition(), null);
								return false;
							}
						} else {
							// 其他error，无法处理
							LOG.error("admaker grantAuthority failed for tpId=" + tpId + ", descJson=" + descJson);
							result = ApiResultBeanUtils.addApiError(result,
									AdErrorCode.ADD_AD_ERROR.getValue(),
									AdErrorCode.ADD_AD_ERROR.getMessage(), apiPosition.getPosition(), null);
							return false;
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
        }

		int groupId =  unit.getGroupid().intValue();
		CproGroup cproGroup = groupIdEntityMap.get(groupId);
		if (cproGroup == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			apiPosition.addParam(AdConstant.GROUPID);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.NO_GROUP.getValue(),
					AdErrorCode.NO_GROUP.getMessage(),
					apiPosition.getPosition(), null);
			
			return false;
		}
		
		if (cproGroup.getIsSmart() == CproGroupConstant.IS_SMART_TRUE) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			apiPosition.addParam(AdConstant.GROUPID);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.SMARTIDEA_GROUP_CANNOT_ADD_NORMAL_CREATIVE.getValue(),
					AdErrorCode.SMARTIDEA_GROUP_CANNOT_ADD_NORMAL_CREATIVE.getMessage(),
					apiPosition.getPosition(), null);
			
			return false;
		}
		
		sw.stop();
		LOG.info("Step1 using " + sw.getTime() + " ms");
		sw.reset();
		sw.start();
		
		// add for bmob
		fillWirelessProperties4AdAdd(bdUser, unit, groupId);
		
		boolean validateResult = validateAddUnit(index, unit, 
				cproGroup, result, bdUser, totalUnitCount);

		if (validateResult == false) {
			return false;
		}
		
		// 如果是图文物料，则添加icon
		if (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
			boolean flag = addIcon(index, (UnitInfoView2)unit, result, bdUser);
			if (!flag) {
				return false;
			}
		}
		sw.stop();
		LOG.info("Step2 using " + sw.getTime() + " ms");
		sw.reset();
		sw.start();
		
		unit.setKeyword(null);
		Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
		User user = (User) SessionHolder.getSession().get(SessionHolder.USER_KEY);
		UnitInfoView unitResult = null;
		try {
			// @version cpweb641, @author genglei
			// 过滤特殊字符集合
			UnitBeanUtils.filterSpecialChar(unit);
			
			unitResult = unitMgr.addUnit(visitor, unit, cproGroup, user);
			
			if (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_PICTURE
					|| unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_FLASH
					|| unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
				String url = unitMgr.getTmpUrl(unitResult.getMcId(), unitResult.getMcVersionId());
				unitResult.setMaterUrl(StringUtils.isEmpty(url) ? null : url);
			}
		} catch (CproUnitAddException e) {
			LOG.error(e.getMessage(), e);
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.ADD_AD_ERROR.getValue(),
					AdErrorCode.ADD_AD_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return false;
		}
		
		sw.stop();
		LOG.info("Step3 using " + sw.getTime() + " ms");
		sw.reset();
		sw.start();
		
		if (unitResult != null) {
			AdType resultAd = mapper.map(unitResult, AdType.class);
			resultAd.setStatus(CproUnitConstant.UNIT_STATE_AUDITING);
			resultAd.setLocalId(adType.getLocalId());
			result = ApiResultBeanUtils.addApiResult(result, resultAd);
			
			// 记录历史操作记录
			OpTypeVo optype = OptHistoryConstant.OPTYPE_UNIT_NEW;
			OptContent content = new OptContent(cproGroup.getUserId(),
					optype.getOpType(),
					optype.getOpLevel(), unitResult.getUnitid(), null,
					optype.getTransformer().toDbString(unitResult.getTitle()));
			content.setGroupId(cproGroup.getGroupId());
			optContents.add(content);
			
			return true;
		} else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.ADD_AD_ERROR.getValue(),
					AdErrorCode.ADD_AD_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return false;
		}
	}

	private void fillWirelessProperties4AdAdd(User bdUser, UnitInfoView unit, int groupId) {
		List<Integer> groupIdList = new ArrayList<Integer>();
		groupIdList.add(groupId);
		Map<Integer, Integer> groupPromotionTypeMapping = cproGroupMgr.getGroupPromotionTypeMapping(bdUser.getUserid(), groupIdList);
		int promotionType = groupPromotionTypeMapping.get(groupId);
		if (promotionType == CproPlanConstant.PROMOTIONTYPE_WIRELESS) {
			unit.setShowUrl(unit.getWirelessShowUrl());
			unit.setTargetUrl(unit.getWirelessTargetUrl());
		}
	}
	
	public boolean addIcon(int index, UnitInfoView2 unit,
			ApiResult<AdType> result, User bdUser) {
		UserUploadIcon iconInfo = new UserUploadIcon();
		
		try {
			byte[] data = unit.getData();
			String fileSrcMd5 = MD5.getMd5(data);
			
			// 调用ubmcService服务，将图片信息插入ubmc物料库中
			List<RequestBaseMaterial> requests = new LinkedList<RequestBaseMaterial>();
			RequestBaseMaterial request = new RequestIconMaterial(null, null, unit.getWidth(), 
					unit.getHeight(), data, fileSrcMd5);
			requests.add(request);
			List<ResponseBaseMaterial> responses = ubmcService.insert(requests);
			if (CollectionUtils.isEmpty(responses) || responses.get(0) == null) {
				String msg = "insert unit into ubmc failed";
				throw new Exception(msg);
			}
			
			ResponseIconMaterial response = (ResponseIconMaterial)responses.get(0);
			
			// 设置获取的物料的WID及fileSrc
			iconInfo.setWid(response.getMcId());
			iconInfo.setMcId(response.getMcId());
			iconInfo.setUbmcsyncflag(CproUnitConstant.UBMC_SYNC_FLAG_YES);
			iconInfo.setFileSrc(response.getFileSrc());
			iconInfo.setWidth(response.getWidth());
			iconInfo.setHight(response.getHeight());
			iconInfo.setUserId(bdUser.getUserid());
			iconInfo.setAddTime(new Date());
	
			iconMgr.addUserUploadIcon(bdUser.getUserid(), iconInfo);
	
			unit.setIconId(iconInfo.getMcId());
			unit.setMcVersionId(1);
			unit.setFileSrc(iconInfo.getFileSrc());
			
			//记录用户最近使用图标
	    	iconMgr.updateUserLastUsedIcon(bdUser.getUserid(), unit);
	    	        	
	    	//系统图标使用率计数
	    	SystemIcon systemIcom = iconMgr.findSystemIconById(unit.getIconId());
	    	if(systemIcom!=null){
	    		iconMgr.addIconUsed(unit.getIconId());
	    	}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.ADD_ICON_ERROR.getValue(),
					AdErrorCode.ADD_ICON_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			
			return false;
		}
    	
    	return true;
	}

	private boolean validateAddUnit(int index, UnitInfoView unit,
			CproGroup group, ApiResult<AdType> result, User bdUser, int totalUnitCount) {
		if (unit == null) {
			return false;
		}
		if (unit instanceof UnitInfoView2 && ((UnitInfoView2)unit).getData() == null
				&& unit.getWuliaoType() != CproUnitConstant.MATERIAL_TYPE_LITERAL) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			apiPosition.addParam(AdConstant.IMAGE_DATA);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.IMAGE_ERROR.getValue(),
					AdErrorCode.IMAGE_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			
			return false;
		}
		return validateUnit(index, unit, group, result, bdUser, totalUnitCount);
	}

	/**
	 * genWirelessUrlBoolean: 校验生成无线URL判断标记
	 * @version cpweb-567
	 * @author genglei01
	 * @date Nov 27, 2013
	 */
	public CprounitWirelessUrlBoolean genWirelessUrlBoolean(UnitInfoView newUnitInfo, 
			UnitInfoView oldUnitInfo, boolean hasImageData) {
		boolean isOnlyWirelessShowUrlModified = false;
		boolean isOnlyWirelessTargetUrlModified = false;
		boolean isWirelessUrlModified = false;
		
		int oldState = oldUnitInfo.getStateView().getViewState();
		String oldPcTargetUrl = oldUnitInfo.getTargetUrl();
		String newWirelessTargetUrl = newUnitInfo.getWirelessTargetUrl();
		
		if (newUnitInfo.getWuliaoType() != oldUnitInfo.getWuliaoType()) {
			CprounitWirelessUrlBoolean booleanVO = CproUnitConstant.genWirelessUrlBoolean(oldState, false, 
					false, false, oldPcTargetUrl, newWirelessTargetUrl);
			return booleanVO;
		}
		
		// 判断是否只有wireless中的一种修改了
		if(isWirelessUrlModified(oldUnitInfo.getWirelessShowUrl(), newUnitInfo.getWirelessShowUrl()) 
				&& !isWirelessUrlModified(oldUnitInfo.getWirelessTargetUrl(), newUnitInfo.getWirelessTargetUrl())){
			isOnlyWirelessShowUrlModified = true;
		}
		if(!isWirelessUrlModified(oldUnitInfo.getWirelessShowUrl(), newUnitInfo.getWirelessShowUrl()) 
				&& isWirelessUrlModified(oldUnitInfo.getWirelessTargetUrl(), newUnitInfo.getWirelessTargetUrl())){
			isOnlyWirelessTargetUrlModified = true;
		}
		if (isWirelessUrlModified(oldUnitInfo.getWirelessShowUrl(), newUnitInfo.getWirelessShowUrl()) 
				&& isWirelessUrlModified(oldUnitInfo.getWirelessTargetUrl(), newUnitInfo.getWirelessTargetUrl())) {
			isWirelessUrlModified = true;
		}
		
		if ((!newUnitInfo.getShowUrl().equals(oldUnitInfo.getShowUrl())) 
				|| (!newUnitInfo.getTargetUrl().equals(oldUnitInfo.getTargetUrl()))) {
			CprounitWirelessUrlBoolean booleanVO = CproUnitConstant.genWirelessUrlBoolean(oldState, false, 
					false, false, oldPcTargetUrl, newWirelessTargetUrl);
			return booleanVO;
		}
		
		if (newUnitInfo.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL) {//文字类
			if ((!newUnitInfo.getTitle().equals(oldUnitInfo.getTitle())) 
					|| (!newUnitInfo.getDescription1().equals(oldUnitInfo.getDescription1())) 
					|| (!newUnitInfo.getDescription2().equals(oldUnitInfo.getDescription2()))) {
				CprounitWirelessUrlBoolean booleanVO = CproUnitConstant.genWirelessUrlBoolean(oldState, false, 
						false, false, oldPcTargetUrl, newWirelessTargetUrl);
				return booleanVO;
			}

			//增加图文类型是否修改检查
		} else if (newUnitInfo.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
			if ((!newUnitInfo.getTitle().equals(oldUnitInfo.getTitle())) 
					|| (!newUnitInfo.getDescription1().equals(oldUnitInfo.getDescription1())) 
					|| (!newUnitInfo.getDescription2().equals(oldUnitInfo.getDescription2())) 
					|| (!newUnitInfo.getIconId().equals(oldUnitInfo.getIconId()))) {
				CprounitWirelessUrlBoolean booleanVO = CproUnitConstant.genWirelessUrlBoolean(oldState, false, 
						false, false, oldPcTargetUrl, newWirelessTargetUrl);
				return booleanVO;
			}
		} else {//图片 or flash
			if (hasImageData) {//表明是从本地上传
				CprounitWirelessUrlBoolean booleanVO = CproUnitConstant.genWirelessUrlBoolean(oldState, false, 
						false, false, oldPcTargetUrl, newWirelessTargetUrl);
				return booleanVO;
			}
		}
		
		CprounitWirelessUrlBoolean booleanVO = CproUnitConstant.genWirelessUrlBoolean(oldState, isOnlyWirelessTargetUrlModified, 
				isOnlyWirelessShowUrlModified, isWirelessUrlModified, oldPcTargetUrl, newWirelessTargetUrl);
		
		return booleanVO;
	}
	
	private boolean isWirelessUrlModified(String preUrl, String afterUrl) {
		if (StringUtils.isEmpty(preUrl) && StringUtils.isEmpty(afterUrl)) {
			return false;
		} else if (StringUtils.isEmpty(preUrl) && StringUtils.isNotEmpty(afterUrl)) {
			return true;
		} else if (StringUtils.isNotEmpty(preUrl) && StringUtils.isEmpty(afterUrl)) {
			return true;
		} else {
			return !(preUrl.equalsIgnoreCase(afterUrl));
		}
	}
	
	public boolean validateUnit(int index, UnitInfoView unit, CproGroup group,
			ApiResult<AdType> result, User bdUser, int totalUnitCount) {
		if (group == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			apiPosition.addParam(AdConstant.GROUPID);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.NO_GROUP.getValue(),
					AdErrorCode.NO_GROUP.getMessage(),
					apiPosition.getPosition(), null);
			
			return false;
		}
		
		// add by zhangxu for API Bmob
		if ( (StringUtils.isEmpty(unit.getWirelessShowUrl()) && StringUtils.isNotEmpty(unit.getWirelessTargetUrl())) ||
				(StringUtils.isNotEmpty(unit.getWirelessShowUrl()) && StringUtils.isEmpty(unit.getWirelessTargetUrl())) ) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			apiPosition.addParam(AdConstant.APP_SHOWURL);
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.APP_SHOWURL_TARGETURL_BOTH_NOT_NULL.getValue(), 
					AdErrorCode.APP_SHOWURL_TARGETURL_BOTH_NOT_NULL.getMessage(), 
					apiPosition.getPosition(), null);
			return false;
		}
		
		if (unit.getUnitid() == null && totalUnitCount >= CproUnitConfig.MAX_UNIT_NUMBER) {
			// 添加创意的时候教育个数限制
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.TOOMANY_TOTAL_NUM.getValue(),
					AdErrorCode.TOOMANY_TOTAL_NUM.getMessage(),
					apiPosition.getPosition(), null);
			return false;
		}

		switch (unit.getWuliaoType()) {
			case CproUnitConstant.MATERIAL_TYPE_LITERAL:
				break;
			case CproUnitConstant.MATERIAL_TYPE_FLASH:
				unit.setPlayer(7);
			case CproUnitConstant.MATERIAL_TYPE_PICTURE:
				break;
			case CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON:
				break;
			default: {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, index);
				apiPosition.addParam(AdConstant.TYPE);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.WRONG_TYPE.getValue(),
						AdErrorCode.WRONG_TYPE.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}
		}
		
		// 满足以下几个条件时，需进行icon校验：
		// 1. 图文创意
		// 2. 新增创意或者修改创意时包含了imageData信息
		if (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON
				&& unit instanceof UnitInfoView2) {
			boolean flag = validateIcon(index, (UnitInfoView2)unit, result);
			if (!flag) {
				return false;
			}
		}

		try {
			UnitInfoAudit.validateUnitInfo(unit, group.getGroupType());
		} catch (ValidationException e) {
			LOG.warn(e.getMessage(), e);
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			
			if (e.getErrorCode() > 3010 && e.getErrorCode() < 3020) {
				apiPosition.addParam(AdConstant.TITLE);
				if (e.getErrorCode() == 3011) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.NO_TITLE.getValue(),
							AdErrorCode.NO_TITLE.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3012) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.TITLE_TOOLONG.getValue(),
							AdErrorCode.TITLE_TOOLONG.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3013) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.TITLE_ERROR.getValue(),
							AdErrorCode.TITLE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3014) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.IMAGE_TITLE_ERROR.getValue(),
							AdErrorCode.IMAGE_TITLE_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				return false;
			} else if (e.getErrorCode() > 3020 && e.getErrorCode() < 3030) {
				if (e instanceof InvalidUnitException) {
					if (((InvalidUnitException)e).getField() == UnitInfoAudit.FIELD_WIRELESS_TARGET_URL) {
						apiPosition.addParam(AdConstant.APP_SHOWURL);
					} else {
						apiPosition.addParam(AdConstant.SHOWURL);
					}
				} else {
					apiPosition.addParam(AdConstant.SHOWURL);
				}
				
				if (e.getErrorCode() == 3021) {
					result = ApiResultBeanUtils.addApiError(result, 
							AdErrorCode.NO_SHOWURL.getValue(), 
							AdErrorCode.NO_SHOWURL.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3022) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.SHOWURL_TOOLONG.getValue(),
							AdErrorCode.SHOWURL_TOOLONG.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3023) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.SHOWURL_ERROR.getValue(),
							AdErrorCode.SHOWURL_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3024) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.SHOWURL_FORMAT.getValue(),
							AdErrorCode.SHOWURL_FORMAT.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3025) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.SHOWURL_FULLWIDTH_ERROR.getValue(),
							AdErrorCode.SHOWURL_FULLWIDTH_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				}
				return false;
			} else if (e.getErrorCode() > 3030 && e.getErrorCode() < 3040) {
				if (e instanceof InvalidUnitException) {
					if (((InvalidUnitException)e).getField() == UnitInfoAudit.FIELD_WIRELESS_TARGET_URL) {
						apiPosition.addParam(AdConstant.APP_TARGETURL);
					} else {
						apiPosition.addParam(AdConstant.TARGETURL);
					}
				} else {
					apiPosition.addParam(AdConstant.TARGETURL);
				}
				
				if (e.getErrorCode() == 3031) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.NO_TARGETURL.getValue(),
							AdErrorCode.NO_TARGETURL.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3032) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.TARGETURL_TOOLONG.getValue(),
							AdErrorCode.TARGETURL_TOOLONG.getMessage(),
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3033) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.TARGETURL_ERROR.getValue(),
							AdErrorCode.TARGETURL_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3034) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.TARGETURL_FORMAT.getValue(),
							AdErrorCode.TARGETURL_FORMAT.getMessage(),
							apiPosition.getPosition(), null);
				}
				return false;
			} else if (e.getErrorCode() > 3040 && e.getErrorCode() < 3050) {
				apiPosition.addParam(AdConstant.DESCRIPTION1);
				if (e.getErrorCode() == 3041) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.DESCRIPTION_TOOSHORT.getValue(),
							AdErrorCode.DESCRIPTION_TOOSHORT.getMessage(),
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3042) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.DESCRIPTION_TOOLONG.getValue(),
							AdErrorCode.DESCRIPTION_TOOLONG.getMessage(),
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3043) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.DESCRIPTION_ERROR.getValue(),
							AdErrorCode.DESCRIPTION_ERROR.getMessage(),
							apiPosition.getPosition(), null);
				}
				return false;
			} else if (e.getErrorCode() > 3050 && e.getErrorCode() < 3060) {
				apiPosition.addParam(AdConstant.DESCRIPTION2);
				if (e.getErrorCode() == 3051) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.DESCRIPTION2_TOOSHORT.getValue(),
							AdErrorCode.DESCRIPTION2_TOOSHORT.getMessage(),
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3052) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.DESCRIPTION2_TOOLONG.getValue(),
							AdErrorCode.DESCRIPTION2_TOOLONG.getMessage(),
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3053) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.DESCRIPTION2_ERROR.getValue(),
							AdErrorCode.DESCRIPTION2_ERROR.getMessage(),
							apiPosition.getPosition(), null);
				}
				return false;
			} else if (e.getErrorCode() > 3060 && e.getErrorCode() < 3070) {
				apiPosition.addParam(AdConstant.IMAGE_DATA);
				if (e.getErrorCode() == 3061) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.FILESRC_TOOBIG.getValue(),
							AdErrorCode.FILESRC_TOOBIG.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3062) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.FILESRC_WRONGSIZE.getValue(),
							AdErrorCode.FILESRC_WRONGSIZE.getMessage(),
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3063) {
					if (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_PICTURE) {
						result = ApiResultBeanUtils.addApiError(result,
								AdErrorCode.FILESRC_WRONGTYPE_PIC.getValue(),
								AdErrorCode.FILESRC_WRONGTYPE_PIC.getMessage(),
								apiPosition.getPosition(), null);
					} else if (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_FLASH) {
						result = ApiResultBeanUtils.addApiError(result,
								AdErrorCode.FILESRC_WRONGTYPE_FLASH.getValue(),
								AdErrorCode.FILESRC_WRONGTYPE_FLASH.getMessage(), 
								apiPosition.getPosition(), null);
					}
				} else if (e.getErrorCode() == 3064) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.FILESRC_COLOR.getValue(),
							AdErrorCode.FILESRC_COLOR.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3065) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.FILESRC_SIZE.getValue(),
							AdErrorCode.FILESRC_SIZE.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3066) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.FILESRC_WRONGTYPE_PIC_FILM.getValue(),
							AdErrorCode.FILESRC_WRONGTYPE_PIC_FILM.getMessage(), 
							apiPosition.getPosition(), null);
				} else if (e.getErrorCode() == 3067) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.FILESRC_WRONGTYPE_GROUP.getValue(),
							AdErrorCode.FILESRC_WRONGTYPE_GROUP.getMessage(),
							apiPosition.getPosition(), null);
				}
				return false;
			} else {
				result = ApiResultBeanUtils.addApiError(result,
						GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
						GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}
		}

		// aka校验
		List<UnitInfoView> unitList = new ArrayList<UnitInfoView>();
		unitList.add(unit);
		List<Long> tokens = UnitAkaAudit.akaAuditBatch(bdUser.getUserid(), unitList);
		if (tokens == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					GlobalErrorCode.UNEXPECTED_PARAMETER.getValue(),
					GlobalErrorCode.UNEXPECTED_PARAMETER.getMessage(),
					apiPosition.getPosition(), null);
			return false;
		} else {
			// 因为只对单个校验，所以取第一位返回值
			long tokenResult = tokens.get(0);
			
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);

			if ((tokenResult & AKA_RES_CODE.URLSC.getValue()) == AKA_RES_CODE.URLSC.getValue()) {
				apiPosition.addParam(AdConstant.TARGETURL);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.URLSC.getValue(), 
						AdErrorCode.URLSC.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.TITI.getValue()) == AKA_RES_CODE.TITI.getValue()) {
				apiPosition.addParam(AdConstant.TITLE);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TITI.getValue(), 
						AdErrorCode.TITI.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.CORI.getValue()) == AKA_RES_CODE.CORI.getValue()) {
				apiPosition.addParam(AdConstant.TITLE);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.CORI.getValue(), 
						AdErrorCode.CORI.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.URLEND.getValue()) == AKA_RES_CODE.URLEND.getValue()) {
				apiPosition.addParam(AdConstant.TARGETURL);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.URLEND.getValue(), 
						AdErrorCode.URLEND.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.URLHEAD.getValue()) == AKA_RES_CODE.URLHEAD.getValue()) {
				apiPosition.addParam(AdConstant.TARGETURL);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.URLHEAD.getValue(), 
						AdErrorCode.URLHEAD.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.DESC1I.getValue()) == AKA_RES_CODE.DESC1I.getValue()) {
				apiPosition.addParam(AdConstant.DESCRIPTION1);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.DESC1I.getValue(), 
						AdErrorCode.DESC1I.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.DESC2I.getValue()) == AKA_RES_CODE.DESC2I.getValue()) {
				apiPosition.addParam(AdConstant.DESCRIPTION2);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.DESC2I.getValue(), 
						AdErrorCode.DESC2I.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.TITM.getValue()) == AKA_RES_CODE.TITM.getValue()) {
				apiPosition.addParam(AdConstant.TITLE);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TITM.getValue(), 
						AdErrorCode.TITM.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.DESC1M.getValue()) == AKA_RES_CODE.DESC1M.getValue()) {
				apiPosition.addParam(AdConstant.DESCRIPTION1);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.DESC1M.getValue(), 
						AdErrorCode.DESC1M.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.DESC2M.getValue()) == AKA_RES_CODE.DESC2M.getValue()) {
				apiPosition.addParam(AdConstant.DESCRIPTION2);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.DESC2M.getValue(), 
						AdErrorCode.DESC2M.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.CORM.getValue()) == AKA_RES_CODE.CORM.getValue()) {
				apiPosition.addParam(AdConstant.TITLE);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.CORM.getValue(), 
						AdErrorCode.CORM.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.TITC.getValue()) == AKA_RES_CODE.TITC.getValue()) {
				apiPosition.addParam(AdConstant.TITLE);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.TITC.getValue(), 
						AdErrorCode.TITC.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.DESC1C.getValue()) == AKA_RES_CODE.DESC1C.getValue()) {
				apiPosition.addParam(AdConstant.DESCRIPTION1);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.DESC1C.getValue(), 
						AdErrorCode.DESC1C.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.DESC2C.getValue()) == AKA_RES_CODE.DESC2C.getValue()) {
				apiPosition.addParam(AdConstant.DESCRIPTION2);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.DESC2C.getValue(), 
						AdErrorCode.DESC2C.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if ((tokenResult & AKA_RES_CODE.CORC.getValue()) == AKA_RES_CODE.CORC.getValue()) {
				apiPosition.addParam(AdConstant.TITLE);
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.CORC.getValue(), 
						AdErrorCode.CORC.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
		}

		List<Integer> groupIdList = new ArrayList<Integer>();
		groupIdList.add(group.getGroupId());
		Map<Integer, Integer> groupPromotionTypeMapping = cproGroupMgr.getGroupPromotionTypeMapping(bdUser.getUserid(), groupIdList);
		int promotionType = groupPromotionTypeMapping.get(group.getGroupId());
		
		// 计划为“全部类型”, 则对“pc url”进行主域校验
		if (promotionType == CproPlanConstant.PROMOTIONTYPE_ALL) {
			if (!unitMgr.isBindingUrl(unit.getTargetUrl(), bdUser.getUserid())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, index);
				apiPosition.addParam(AdConstant.TARGETURL);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NOT_BINDING.getValue(), 
						AdErrorCode.NOT_BINDING.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}

			if (!unitMgr.isBindingUrl(unit.getShowUrl(), bdUser.getUserid())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, index);
				apiPosition.addParam(AdConstant.SHOWURL);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.SHOWURL_NOT_BINDING.getValue(),
						AdErrorCode.SHOWURL_NOT_BINDING.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
		}

		// 计划为“仅无线”或“全部类型，且单独设置了无线url”，则对“无线url”进行主域校验
		if ((promotionType == CproPlanConstant.PROMOTIONTYPE_WIRELESS)
				|| ((promotionType == CproPlanConstant.PROMOTIONTYPE_ALL)
						&& (StringUtils.isNotEmpty(unit.getWirelessTargetUrl())) 
						&& (StringUtils.isNotEmpty(unit.getWirelessShowUrl())))) {
			String wirelessTargetUrl = CproUnitConstant.filterWirelessTargetUrlPrefix(unit.getWirelessTargetUrl());
			if (!unitMgr.isBindingUrl(wirelessTargetUrl, bdUser.getUserid())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, index);
				apiPosition.addParam(AdConstant.APP_TARGETURL);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.APP_TARGETURL_NOT_BINDING.getValue(), 
						AdErrorCode.APP_TARGETURL_NOT_BINDING.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
			String wirelessShowUrl = CproUnitConstant.filterWirelessShowUrlPrefix(unit.getWirelessShowUrl());
			if (!unitMgr.isBindingUrl(wirelessShowUrl, bdUser.getUserid())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_TYPES, index);
				apiPosition.addParam(AdConstant.APP_SHOWURL);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.APP_SHOWURL_NOT_BINDING.getValue(),
						AdErrorCode.APP_SHOWURL_NOT_BINDING.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
		}

		return true;
	}
	
	private boolean validateIcon(int index, UnitInfoView2 unit, ApiResult<AdType> result) {
		boolean success;
		int width = 0;
		int height = 0;

		// 图片
		try {
			ByteArrayInputStream image = new ByteArrayInputStream(unit.getData());
			BufferedImage img = ImageIO.read(image);
			width = img.getWidth();
			height = img.getHeight();

			// 判断尺寸
			boolean flag = validateIconSize(width, height);

			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			apiPosition.addParam(AdConstant.IMAGE_DATA);
			if (flag) {
				// 判断色彩空间
				ColorSpace img_cs = img.getColorModel().getColorSpace();
				if (ColorSpace.TYPE_RGB != img_cs.getType()) {
					success = false;
					
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.FILESRC_COLOR.getValue(),
							AdErrorCode.FILESRC_COLOR.getMessage(),
							apiPosition.getPosition(), null);
				} else {
					success = true;
				}
			} else {
				success = false;
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.ICON_SIZE_ERROR.getValue(),
						AdErrorCode.ICON_SIZE_ERROR.getMessage(),
						apiPosition.getPosition(), null);
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			success = false;
			
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			apiPosition.addParam(AdConstant.IMAGE_DATA);
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.IMAGE_ERROR.getValue(),
					AdErrorCode.IMAGE_ERROR.getMessage(),
					apiPosition.getPosition(), null);
		}

		unit.setWidth(width);
		unit.setHeight(height);

		return success;
	}

	private boolean validateIconSize(int width, int height) {
		if (width != CproUnitConstant.LITERAL_WITH_ICON_DEFAULT_WIDTH
				|| height != CproUnitConstant.LITERAL_WITH_ICON_DEFAULT_HEIGHT) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean validateModUnit(int index, UnitInfoView newUnitInfo, UnitInfoView oldUnitInfo,  
			boolean hasImageData, CproGroup group, ApiResult<AdType> result, User bdUser, int totalUnitCount) {
		boolean flag = validateUnit(index, newUnitInfo, group, result, bdUser, totalUnitCount);
		if (flag == false) {
			return flag;
		}
		
		// 增加创意是否修改的逻辑，如果创意没有修改，则报错，直接返回
		if (isUnitModified(newUnitInfo, oldUnitInfo, hasImageData)) {
			return true;
		} else {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_TYPES, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.MOD_ERROR.getValue(),
					AdErrorCode.MOD_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return false;
		}
	}
	
	/**
	 * isUnitModified: 判断创意是否修改
	 * @version cpweb-567
	 * @author genglei01
	 * @date Nov 28, 2013
	 */
	private boolean isUnitModified(UnitInfoView newUnitInfo, UnitInfoView oldUnitInfo, boolean hasImageData) {
		if (hasImageData) {
			return true;
		}
		
		if (newUnitInfo.getWuliaoType() != oldUnitInfo.getWuliaoType()) {
			return true;
		}
		
		if ((!oldUnitInfo.getShowUrl().equals(newUnitInfo.getShowUrl())) 
				|| (!oldUnitInfo.getTargetUrl().equals(newUnitInfo.getTargetUrl()))
				|| isWirelessUrlModified(oldUnitInfo.getWirelessShowUrl(), newUnitInfo.getWirelessShowUrl())
				|| isWirelessUrlModified(oldUnitInfo.getWirelessTargetUrl(), newUnitInfo.getWirelessTargetUrl())) {
			return true;
		}
		
		if (true) {
			if (newUnitInfo.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL) {//文字类
				if ((!newUnitInfo.getTitle().equals(oldUnitInfo.getTitle())) 
						|| (!newUnitInfo.getDescription1().equals(oldUnitInfo.getDescription1())) 
						|| (!newUnitInfo.getDescription2().equals(oldUnitInfo.getDescription2()))) {
					return true;
				}

				//增加图文类型是否修改检查
			} else if (newUnitInfo.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
				if ((!newUnitInfo.getTitle().equals(oldUnitInfo.getTitle())) 
						|| (!newUnitInfo.getDescription1().equals(oldUnitInfo.getDescription1())) 
						|| (!newUnitInfo.getDescription2().equals(oldUnitInfo.getDescription2())) 
						|| (!newUnitInfo.getIconId().equals(oldUnitInfo.getIconId()))) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean validateReplaceUnit(int index, UnitInfoView unit, Integer srcGroupType, 
			ApiResult<Object> result, User bdUser, int promotionType) {
		
		CproGroup cproGroup = cproGroupMgr.findCproGroupById(unit.getGroupid().intValue());
		if (cproGroup == null) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_IDS, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.NO_GROUP.getValue(),
					AdErrorCode.NO_GROUP.getMessage(),
					apiPosition.getPosition(), null);
			
			return false;
		}
		
		Integer groupType = cproGroup.getGroupType();
		if (!isValidSize(unit, groupType)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_IDS, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.FILESRC_SIZE.getValue(),
					AdErrorCode.FILESRC_SIZE.getMessage(),
					apiPosition.getPosition(), null);
			
			return false;
		}
		
		srcGroupType = getGroupTypeForUnit(unit, srcGroupType);
		if (!GroupTypeUtil.isIntersectionNotEmpty(srcGroupType, groupType)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.AD_IDS, index);
			
			result = ApiResultBeanUtils.addApiError(result,	
					AdErrorCode.REPLACE_GROUP_TYPE_ERROR.getValue(),
					AdErrorCode.REPLACE_GROUP_TYPE_ERROR.getMessage(), 
					apiPosition.getPosition(), null);
			return false;
		}

		// 计划为“全部类型”, 则对“pc url”进行主域校验
		if (promotionType == CproPlanConstant.PROMOTIONTYPE_ALL) {
			if (!unitMgr.isBindingUrl(unit.getTargetUrl(), bdUser.getUserid())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, index);
				apiPosition.addParam(AdConstant.TARGETURL);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NOT_BINDING.getValue(), 
						AdErrorCode.NOT_BINDING.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}

			if (!unitMgr.isBindingUrl(unit.getShowUrl(), bdUser.getUserid())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, index);
				apiPosition.addParam(AdConstant.SHOWURL);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.SHOWURL_NOT_BINDING.getValue(),
						AdErrorCode.SHOWURL_NOT_BINDING.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
		}

		// 计划为“仅无线”或“全部类型，且单独设置了无线url”，则对“无线url”进行主域校验
		if ((promotionType == CproPlanConstant.PROMOTIONTYPE_WIRELESS)
				|| ((promotionType == CproPlanConstant.PROMOTIONTYPE_ALL)
						&& (StringUtils.isNotEmpty(unit.getWirelessTargetUrl())) 
						&& (StringUtils.isNotEmpty(unit.getWirelessShowUrl())))) {
			String wirelessTargetUrl = CproUnitConstant.filterWirelessTargetUrlPrefix(unit.getWirelessTargetUrl());
			if (!unitMgr.isBindingUrl(wirelessTargetUrl, bdUser.getUserid())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, index);
				apiPosition.addParam(AdConstant.APP_TARGETURL);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.APP_TARGETURL_NOT_BINDING.getValue(), 
						AdErrorCode.APP_TARGETURL_NOT_BINDING.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
			String wirelessShowUrl = CproUnitConstant.filterWirelessShowUrlPrefix(unit.getWirelessShowUrl());
			if (!unitMgr.isBindingUrl(wirelessShowUrl, bdUser.getUserid())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, index);
				apiPosition.addParam(AdConstant.APP_SHOWURL);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.APP_SHOWURL_NOT_BINDING.getValue(),
						AdErrorCode.APP_SHOWURL_NOT_BINDING.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
		}

		return true;
	}
	
	private boolean isValidSize(UnitInfoView unit, Integer groupType) {
		if (unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL
				|| unit.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
			// 文字物料和图文物料不用校验尺寸，直接合法
			return true;
		} else {
			boolean isImageSizeValid = ImageScale.isImageSizeValid(groupType,
					unit.getWidth(), unit.getHeight());
			if (isImageSizeValid) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean copyUnit(int index, List<UnitInfoView> unitList,
			CproGroup cproGroup, ApiResult<Object> result, User bdUser, 
			Integer opUser, List<OptContent> optContents) {
		Integer groupId = cproGroup.getGroupId();
		
		ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
		apiPosition.addParam(AdConstant.AD_IDS, index);
		
		// 向ubmc发送复制请求
		List<ResponseBaseMaterial> newUnitList = null;
		
		try {
			List<RequestBaseMaterial> requests = new LinkedList<RequestBaseMaterial>();
			for (int i = 0; i < unitList.size(); i++){
				UnitInfoView unit = unitList.get(i);
				
				RequestBaseMaterial request = new RequestLite(unit.getMcId(), unit.getMcVersionId());
				requests.add(request);
			}
			
			if (requests.size() > 0) {
				newUnitList = ubmcService.copy(requests);
				if (CollectionUtils.isEmpty(newUnitList) || (newUnitList.size() != unitList.size())) {
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.COPY_AD_ERROR.getValue(), 
							AdErrorCode.COPY_AD_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
					return false;
				} else {
					for (int i = 0; i < newUnitList.size(); i++) {
						ResponseBaseMaterial item = newUnitList.get(i);
						UnitInfoView view = unitList.get(i);
						
						if (item == null) {
							result = ApiResultBeanUtils.addApiError(result,
									AdErrorCode.COPY_AD_ERROR.getValue(), 
									AdErrorCode.COPY_AD_ERROR.getMessage(), 
									apiPosition.getPosition(), null);
							return false;
						} else {
							view.setWid(item.getMcId());
							view.setMcId(item.getMcId());
							view.setMcVersionId(item.getVersionId());
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error("复制创意出错：groupId = " + groupId, e);
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.COPY_AD_ERROR.getValue(), 
					AdErrorCode.COPY_AD_ERROR.getMessage(), 
					apiPosition.getPosition(), null);

			return false;
		}
		
		try{
			for (UnitInfoView unitInfo : unitList) {
				// 复制创意到推广组
				unitInfo.setUserid(cproGroup.getUserId());
				Unit newUnit = unitMgr.createUnitFromUnitInfoViewForGroupClone(unitInfo, opUser, new Date());
				
				CproPlan plan = planMgr.findCproPlanById(cproGroup.getPlanId());
				Visitor visitor = (Visitor) SessionHolder.getSession().get(SessionHolder.VISITOR_KEY);
				User user = userMgr.findUserBySFid(cproGroup.getUserId());
				
				newUnit.setGroup(cproGroup);
				newUnit.setPlan(plan);
				newUnit.setUser(user);
				newUnit.getMaterial().setIftitle(0);
				Unit saveUnit = unitMgr.cloneUnit(visitor, newUnit);
				
				if (saveUnit == null){
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.COPY_AD_ERROR.getValue(), 
							AdErrorCode.COPY_AD_ERROR.getMessage(), 
							apiPosition.getPosition(), null);
					return false;
				}
				
                // 如果拷贝的创意状态为有效，则需要将创意提交至online_unit，以便创意进行投放
                if (saveUnit.getState() == CproUnitConstant.UNIT_STATE_NORMAL) {
                    unitMgr.commitOnlineUnit(saveUnit);
                }

				//对于图文类型创意，增加记录iconId关联记录
				if(saveUnit.getMaterial().getWuliaoType().equals(CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON)){
					unitIconDao.recordIconForUnit(saveUnit.getUser().getUserid(), saveUnit.getId(), unitInfo.getIconId());
				}
				
				OpTypeVo optype = OptHistoryConstant.OPTYPE_UNIT_COPY_NEW;
				OptContent content = new OptContent(cproGroup.getUserId(),
						optype.getOpType(), optype.getOpLevel(), saveUnit.getId(), null,
						optype.getTransformer().toDbString(saveUnit.getMaterial().getTitle()));
				content.setGroupId(cproGroup.getGroupId());
				optContents.add(content);
			}
			
			return true;
		} catch(Exception e){
			LOG.error("复制创意出错：groupId = " + groupId, e);
			return false;
		}

	}

	public boolean validateCopyUnit(List<UnitInfoView> unitList, List<CproGroup> groupList, 
			ApiResult<Object> result, User user, int promotionType) {
		for (int index = 0; index < unitList.size(); index++) {
			UnitInfoView unit = unitList.get(index);
			
			// 检查创意是否是删除状态，是否属于该用户
			if (unit.getStateView().getViewState() == CproUnitConstant.UNIT_STATE_DELETE) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.COPY_AD_STATE_ERROR.getValue(), 
						AdErrorCode.COPY_AD_STATE_ERROR.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			} else if (!unit.getUserid().equals(user.getUserid())) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.WRONG_USER.getValue(),
						AdErrorCode.WRONG_USER.getMessage(), 
						apiPosition.getPosition(), null);
				return false;
			}
			
			// 计划为“全部类型”, 则对“pc url”进行主域校验
			if (promotionType == CproPlanConstant.PROMOTIONTYPE_ALL) {
				if (!unitMgr.isBindingUrl(unit.getTargetUrl(), user.getUserid())) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(AdConstant.AD_IDS, index);
					apiPosition.addParam(AdConstant.TARGETURL);
					
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.NOT_BINDING.getValue(), 
							AdErrorCode.NOT_BINDING.getMessage(), 
							apiPosition.getPosition(), null);
					return false;
				}

				if (!unitMgr.isBindingUrl(unit.getShowUrl(), user.getUserid())) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(AdConstant.AD_IDS, index);
					apiPosition.addParam(AdConstant.SHOWURL);
					
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.SHOWURL_NOT_BINDING.getValue(),
							AdErrorCode.SHOWURL_NOT_BINDING.getMessage(), 
							apiPosition.getPosition(), null);
					return false;
				}
			}

			// 计划为“仅无线”或“全部类型，且单独设置了无线url”，则对“无线url”进行主域校验
			if ((promotionType == CproPlanConstant.PROMOTIONTYPE_WIRELESS)
					|| ((promotionType == CproPlanConstant.PROMOTIONTYPE_ALL)
							&& (StringUtils.isNotEmpty(unit.getWirelessTargetUrl())) 
							&& (StringUtils.isNotEmpty(unit.getWirelessShowUrl())))) {
				String wirelessTargetUrl = CproUnitConstant.filterWirelessTargetUrlPrefix(unit.getWirelessTargetUrl());
				if (!unitMgr.isBindingUrl(wirelessTargetUrl, user.getUserid())) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(AdConstant.AD_IDS, index);
					apiPosition.addParam(AdConstant.APP_TARGETURL);
					
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.APP_TARGETURL_NOT_BINDING.getValue(), 
							AdErrorCode.APP_TARGETURL_NOT_BINDING.getMessage(), 
							apiPosition.getPosition(), null);
					return false;
				}
				String wirelessShowUrl = CproUnitConstant.filterWirelessShowUrlPrefix(unit.getWirelessShowUrl());
				if (!unitMgr.isBindingUrl(wirelessShowUrl, user.getUserid())) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(AdConstant.AD_IDS, index);
					apiPosition.addParam(AdConstant.APP_SHOWURL);
					
					result = ApiResultBeanUtils.addApiError(result,
							AdErrorCode.APP_SHOWURL_NOT_BINDING.getValue(),
							AdErrorCode.APP_SHOWURL_NOT_BINDING.getMessage(), 
							apiPosition.getPosition(), null);
					return false;
				}
			}
		}
		
		for (int index = 0; index < groupList.size(); index++) {
			CproGroup group = groupList.get(index);
			
			boolean validateResult = validateCopyUnitToGroup(index, unitList, group, result, user);
			if (validateResult == false) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateCopyUnitToGroup(int index, List<UnitInfoView> unitList,
			CproGroup targetGroup, ApiResult<Object> result, User user) {
		Integer targetGroupId = targetGroup.getGroupId();
		
		// 判断推广组下的创意是否超限
		UnitQuery condition = new UnitQuery();
		condition.setGid(targetGroupId);
		int count = unitMgr.countUnitByGroupId(user.getUserid(), condition);
		if (count + unitList.size() > CproUnitConfig.MAX_UNIT_NUMBER) {
			// 添加创意的时候教育个数限制
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(AdConstant.GROUP_IDS, index);
			
			result = ApiResultBeanUtils.addApiError(result,
					AdErrorCode.TOOMANY_TOTAL_NUM.getValue(),
					AdErrorCode.TOOMANY_TOTAL_NUM.getMessage(),
					apiPosition.getPosition(), null);
			return false;
		}
		
		// 创意要与目标推广组类型匹配
		Integer targetGroupType = targetGroup.getGroupType();
		for (int indexUnit = 0; indexUnit < unitList.size(); indexUnit++) {
			UnitInfoView unit = unitList.get(indexUnit);
						
			if (!isValidSize(unit, targetGroupType)) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.GROUP_IDS, index);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.FILESRC_SIZE.getValue(),
						AdErrorCode.FILESRC_SIZE.getMessage(),
						apiPosition.getPosition(), null);
				
				return false;
			}
			
			// 检查推广组是否存在
			CproGroup srcGroup = cproGroupMgr.findCproGroupById(unit.getGroupid().intValue());
			if (srcGroup == null) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(AdConstant.AD_IDS, indexUnit);
				
				result = ApiResultBeanUtils.addApiError(result,
						AdErrorCode.NO_GROUP.getValue(),
						AdErrorCode.NO_GROUP.getMessage(),
						apiPosition.getPosition(), null);
				return false;
			}
			
			Integer srcGroupType = getGroupTypeForUnit(unit, srcGroup.getGroupType());
			if (!GroupTypeUtil.isIntersectionNotEmpty(srcGroupType, targetGroupType)) {
				ApiErrorPosition apiPositionForGroup = new ApiErrorPosition(PositionConstant.PARAM);
				apiPositionForGroup.addParam(AdConstant.GROUP_IDS, index);
				
				ApiErrorPosition apiPositionForUnit = new ApiErrorPosition(PositionConstant.PARAM);
				apiPositionForUnit.addParam(AdConstant.AD_IDS, indexUnit);
				
				String msg = apiPositionForUnit.combinePosition(apiPositionForGroup);
				result = ApiResultBeanUtils.addApiError(result,	
						AdErrorCode.COPY_GROUP_TYPE_ERROR.getValue(),
						AdErrorCode.COPY_GROUP_TYPE_ERROR.getMessage(), msg, null);
				return false;
			}
		}
		
		return true;
	}
	
	private Integer getGroupTypeForUnit(UnitInfoView unitInfoView, Integer groupType) {
		Integer resultGroupType = 0;
		if (unitInfoView.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL
				|| unitInfoView.getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
			resultGroupType = resultGroupType | CproGroupConstant.GROUP_TYPE_FIXED;
			return resultGroupType;
		}

		if (GroupTypeUtil.containsFixed(groupType)
				&& ImageScale.isImageSizeValid(CproGroupConstant.GROUP_TYPE_FIXED, 
						unitInfoView.getWidth(), unitInfoView.getHeight())) {
			resultGroupType = resultGroupType | CproGroupConstant.GROUP_TYPE_FIXED;
		}

		if (GroupTypeUtil.containsFlow(groupType)
				&& ImageScale.isImageSizeValid(CproGroupConstant.GROUP_TYPE_FLOW, 
						unitInfoView.getWidth(), unitInfoView.getHeight())) {
			resultGroupType = resultGroupType | CproGroupConstant.GROUP_TYPE_FLOW;
		}
		if (GroupTypeUtil.containsFilm(groupType)
				&& ImageScale.isImageSizeValid(CproGroupConstant.GROUP_TYPE_FILM, 
						unitInfoView.getWidth(), unitInfoView.getHeight())) {
			resultGroupType = resultGroupType | CproGroupConstant.GROUP_TYPE_FILM;
		}
		
		return resultGroupType;
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

	public CproUnitMgr getUnitMgr() {
		return unitMgr;
	}

	public void setUnitMgr(CproUnitMgr unitMgr) {
		this.unitMgr = unitMgr;
	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public IconMgr getIconMgr() {
		return iconMgr;
	}

	public void setIconMgr(IconMgr iconMgr) {
		this.iconMgr = iconMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public void setPlanMgr(CproPlanMgr planMgr) {
		this.planMgr = planMgr;
	}

	public void setUnitIconDao(UnitIconDao unitIconDao) {
		this.unitIconDao = unitIconDao;
	}

	public UserInfoMgr getUserInfoMgr() {
		return userInfoMgr;
	}

	public void setUserInfoMgr(UserInfoMgr userInfoMgr) {
		this.userInfoMgr = userInfoMgr;
	}

	public void setUbmcService(UbmcService ubmcService) {
		this.ubmcService = ubmcService;
	}
	
	public AmService getAmService() {
		return amService;
	}

	public void setAmService(AmService amService) {
		this.amService = amService;
	}
}
