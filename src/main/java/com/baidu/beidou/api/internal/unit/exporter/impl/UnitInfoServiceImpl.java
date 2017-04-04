package com.baidu.beidou.api.internal.unit.exporter.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.unit.constant.UnitConstant;
import com.baidu.beidou.api.internal.unit.exporter.UnitInfoService;
import com.baidu.beidou.api.internal.unit.vo.MediaResult;
import com.baidu.beidou.api.internal.unit.vo.UBMCTextParam;
import com.baidu.beidou.api.internal.unit.vo.UnitParam;
import com.baidu.beidou.api.internal.unit.vo.UnitPreResult;
import com.baidu.beidou.api.internal.unit.vo.UnitResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.SmartIdeaProductFilterVo;
import com.baidu.beidou.cprogroup.service.SmartIdeaProductFilterMgr;
import com.baidu.beidou.cprogroup.vo.ProductFilterCondition;
import com.baidu.beidou.cprounit.bo.SmartIdeaTemplateElementConfVo;
import com.baidu.beidou.cprounit.bo.TemplateElementInfo;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.SmartIdeaTemplateConfMgr;
import com.baidu.beidou.cprounit.service.UbmcService;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestBaseMaterial;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestLite;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestMaterialForTest;
import com.baidu.beidou.cprounit.ubmcdriver.material.response.ResponseBaseMaterial;
import com.baidu.beidou.cprounit.ubmcdriver.material.response.ResponseMaterialForTest;
import com.baidu.beidou.cprounit.validate.ImageScale;
import com.baidu.beidou.cprounit.vo.PropertyMappingVo;
import com.baidu.beidou.cprounit.vo.TemplateSize;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.beidou.util.sidriver.bo.SiProductBiz.FlowTagType;
import com.baidu.beidou.util.sidriver.bo.SiProductBiz.ProductPreviewItem;
import com.baidu.beidou.util.sidriver.exception.SIException;
import com.baidu.beidou.util.sidriver.service.SmartIdeaService;
import com.baidu.unbiz.common.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class UnitInfoServiceImpl implements UnitInfoService {
	
	private final static Log LOG = LogFactory.getLog(UnitInfoServiceImpl.class);
	private CproUnitMgr unitMgr;
	private SmartIdeaTemplateConfMgr smartIdeaTemplateConfMgr;
	private SmartIdeaProductFilterMgr smartIdeaProductFilterMgr;
	private List<PropertyMappingVo> propertyMappingVoList = null;		//模板参数转换后对象
	private SmartIdeaService smartIdeaService;
	
    private UbmcService ubmcService;

	public UnitResult<String> getUbmcTmpUrl(Long mcId, Integer versionId) {
		ThreadContext.putUserId(1);
		UnitResult<String> result = new UnitResult<String>();
		
		String url = unitMgr.getTmpUrl(mcId, versionId);
		result.addResult(url);
		return result;
	}
	
	@Override
	public Map<Long, String> getUBMCUrlByMcIdAndMcVersionId(List<UBMCTextParam> paramList) {
		if (CollectionUtils.isEmpty(paramList)) {
			return Maps.newHashMap();
		}
		// 构造请求
		List<RequestBaseMaterial> requests = new ArrayList<RequestBaseMaterial>(paramList.size());
		Map<RequestBaseMaterial, UBMCTextParam> dict = Maps.newHashMapWithExpectedSize(paramList.size());
		for (UBMCTextParam param : paramList) {
			RequestLite request = new RequestLite(param.getMcId(), param.getMcVersionId());
			dict.put(request, param);
			requests.add(request);
		}
		
		// 获取图片的临时URL，预览URL
		Map<RequestBaseMaterial, String> urlMap = unitMgr.generateMaterUrl(requests);
		Map<Long, String> resultSet = Maps.newHashMapWithExpectedSize(paramList.size());
		for (Entry<RequestBaseMaterial, String> entry : urlMap.entrySet()) {
			UBMCTextParam param = dict.get(entry.getKey());
			if (param == null) {
				continue;
			}
			
			resultSet.put(param.getCreativeId(), entry.getValue());
		}
		
		return resultSet;
	}
	
    /**
     * Function: 获取创意的预览html片段，创意必须是智能创意
     * @author genglei01
     * @param unitId    unitId
     * @param userId    userId
     * @param flowTag    0-beidou, 1-google
     * @return string
     */
	public UnitResult<UnitPreResult> getHtmlSnippetForSmartUnit(Long unitId, Integer userId, Integer flowTag) {
		ThreadContext.putUserId(userId);
		UnitResult<UnitPreResult> result = new UnitResult<UnitPreResult>();
		if(userId == null||unitId == null||flowTag == null) {
			LOG.error("operation:getHtmlSnippetForSmartUnit, result fail,userId:NULL or unitId:NULL or flowTag:null");
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		
		if(flowTag != FlowTagType.FROM_BEIDOU_VALUE && flowTag != FlowTagType.FROM_GOOGLE_VALUE) {
			LOG.error("operation:getHtmlSnippetForSmartUnit, result fail,flowTag:error");
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}

		//根据创意id获取推广组相关信息
		Unit unit = unitMgr.findSmartIdeaById(userId, unitId);
		if(unit == null) {
			LOG.error("operation:getHtmlSnippetForSmartUnit, result fail,no unit");
			result.setStatus(ResponseStatus.ERROR);
			return result;
		}
		if(unit.getState() == CproUnitConstant.UNIT_STATE_DELETE) {
			LOG.error("operation:getHtmlSnippetForSmartUnit, result fail,unit state is delete");
			result.setStatus(ResponseStatus.ERROR);
			return result;
		}

        String iconUrl = null;
        if (unit.getMaterial().getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
            iconUrl = unitMgr.getTmpUrl(unit.getMaterial().getMcId(), unit.getMaterial().getMcVersionId());
        }

		CproGroup tmpGroup = unit.getGroup();

		//关联模板id
		Integer templateId = unit.getMaterial().getTemplateId();

		SmartIdeaTemplateElementConfVo templateConf = smartIdeaTemplateConfMgr.findByGroupId(tmpGroup.getGroupId(), userId);
		List<TemplateElementInfo> templateElementInfoList =  templateConf.getTemplateElementInfoList();
		propertyMappingVoList = new ArrayList<PropertyMappingVo>();
		for(TemplateElementInfo tp : templateElementInfoList) {
			PropertyMappingVo pv = new PropertyMappingVo();
			pv.setIndex(Integer.valueOf(tp.getIndex()));
			pv.setName(tp.getName());
			pv.setValue(tp.getValue());
			pv.setLiteral(tp.getLiteral());
			propertyMappingVoList.add(pv);
		}

		//获取推广组过滤条件
		SmartIdeaProductFilterVo smartIdeaProductFilter = smartIdeaProductFilterMgr.findVoByGroupId(tmpGroup.getGroupId(), userId);
		List<ProductFilterCondition> filterCondictionList = smartIdeaProductFilter.getFilterCondition();


		//获取推广组下所有非删除智能创意尺寸
		int id = ImageScale.getScaleId(unit.getMaterial().getWidth(), unit.getMaterial().getHeight(),tmpGroup.getGroupType());
		List<TemplateSize> sizeList = new ArrayList<TemplateSize>();
		ImageScale scale = ImageScale.IMAGE_SCALE_MAP.get(id);
		if(scale==null) {
			LOG.error("operation:getHtmlSnippetForSmartUnit, result fail,no scale");
			result.setStatus(ResponseStatus.ERROR);
			return result;
		}
		TemplateSize tpSize = new TemplateSize(scale.getType(), scale.getWidth(), scale.getHeight(), id);
		sizeList.add(tpSize);

		//尺寸不能为空
		if(sizeList.isEmpty()) {
			LOG.error("operation:getHtmlSnippetForSmartUnit, result fail,no unit size");
			result.setStatus(ResponseStatus.ERROR);
			return result;
		}

		List<ProductPreviewItem> previewResList = null;
		try {
            previewResList = smartIdeaService.getPreviewInfos(filterCondictionList, templateId, 
                    propertyMappingVoList, sizeList, userId, unit.getMaterial().getWuliaoType(), 
                    unit.getMaterial().getTargetUrl(), unit.getMaterial().getShowUrl(), 
                    unit.getMaterial().getWirelessTargetUrl(), unit.getMaterial().getWirelessShowUrl(), 
                    flowTag, iconUrl);
		} catch (SIException e) {
			LOG.error("operation:getHtmlSnippetForSmartUnit, result fail, getPreviewInfos failed, reason:", e);
			result.setStatus(ResponseStatus.ERROR);
			return result;
		}
		for(ProductPreviewItem item : previewResList) {
			UnitPreResult unitPreResult = new UnitPreResult();
			unitPreResult.setHtmlSnippet(item.getHtmlSnippet().toStringUtf8());
			if(item.getTargetUrlsList().size() > 0) {
				unitPreResult.setTargetUrls(item.getTargetUrlsList());
			}
			result.addResult(unitPreResult);
			return result;
		}
		return result;
	}

    /**
     * Function: 上传实验性的二进制多媒体数据
     * 
     * @author genglei01
     * @param desc  描述
     * @param data  二进制data
     * @return MediaResult
     */
    public UnitResult<MediaResult> uploadMedia(String desc, byte[] data) {
        ThreadContext.putUserId(1);
        UnitResult<MediaResult> result = new UnitResult<MediaResult>();
        
        if (data == null || data.length == 0) {
            return result;
        }
        
        MediaResult mediaResult = new MediaResult();
        List<RequestBaseMaterial> units = new LinkedList<RequestBaseMaterial>();
        RequestMaterialForTest request = new RequestMaterialForTest(null, null, desc, data);
        units.add(request);

        List<ResponseBaseMaterial> responseList = ubmcService.insert(units);
        if (CollectionUtils.isEmpty(responseList) || responseList.get(0) == null) {
            LOG.error("ubmcService.insert failed");
        } else {
            ResponseMaterialForTest response = (ResponseMaterialForTest) responseList.get(0);
            String fileSrc = response.getFileSrc();
            Long mediaId = ubmcService.getMediaIdFromFileSrc(fileSrc);
            mediaResult.setMcId(response.getMcId());
            mediaResult.setVersionId(response.getVersionId());
            mediaResult.setMediaId(mediaId);
        }

		return result;
	}

	/**
	 * @see UnitInfoService#getUnitList(List)
	 */
	@Override
	public UnitResult<UnitInfoView> getUnitList(List<UnitParam> unitParamList) {
		UnitResult<UnitInfoView> result = new UnitResult<UnitInfoView>();
		if (CollectionUtil.isEmpty(unitParamList)) {
			return result;
		}
		if (unitParamList.size() > UnitConstant.GET_UNIT_INFO_MAX || CollectionUtil.isAnyEmpty(unitParamList)) {
			result.setStatus(ResponseStatus.PARAM_ERROR);
			return result;
		}
		List<UnitInfoView> unitResultList = Lists.newArrayListWithExpectedSize(unitParamList.size());

		// 这里再循环中查询效率低下, 暂时重用external api成熟的实现, 性能不够再修改
		for (UnitParam unitParam : unitParamList) {
			UnitInfoView unit = null;
			if (unitParam.getUserId() != null && unitParam.getUnitId() != null) {
				ThreadContext.putUserId(unitParam.getUserId());
				unit = unitMgr.findBaseUnitInfoById(unitParam.getUserId(), unitParam.getUnitId());
			}

			// 即使结果是null,也放入List
			unitResultList.add(unit);
		}
		result.setData(unitResultList);
		return result;
	}


	public CproUnitMgr getUnitMgr() {
		return unitMgr;
	}

	public void setUnitMgr(CproUnitMgr unitMgr) {
		this.unitMgr = unitMgr;
	}

	public SmartIdeaTemplateConfMgr getSmartIdeaTemplateConfMgr() {
		return smartIdeaTemplateConfMgr;
	}

	public void setSmartIdeaTemplateConfMgr(
			SmartIdeaTemplateConfMgr smartIdeaTemplateConfMgr) {
		this.smartIdeaTemplateConfMgr = smartIdeaTemplateConfMgr;
	}

	public SmartIdeaProductFilterMgr getSmartIdeaProductFilterMgr() {
		return smartIdeaProductFilterMgr;
	}

	public void setSmartIdeaProductFilterMgr(
			SmartIdeaProductFilterMgr smartIdeaProductFilterMgr) {
		this.smartIdeaProductFilterMgr = smartIdeaProductFilterMgr;
	}

	public List<PropertyMappingVo> getPropertyMappingVoList() {
		return propertyMappingVoList;
	}

	public void setPropertyMappingVoList(
			List<PropertyMappingVo> propertyMappingVoList) {
		this.propertyMappingVoList = propertyMappingVoList;
	}

	public SmartIdeaService getSmartIdeaService() {
		return smartIdeaService;
	}

	public void setSmartIdeaService(SmartIdeaService smartIdeaService) {
		this.smartIdeaService = smartIdeaService;
	}

    public UbmcService getUbmcService() {
        return ubmcService;
    }

    public void setUbmcService(UbmcService ubmcService) {
        this.ubmcService = ubmcService;
    }

}
