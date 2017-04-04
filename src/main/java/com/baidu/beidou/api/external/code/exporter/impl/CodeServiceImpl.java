package com.baidu.beidou.api.external.code.exporter.impl;

import java.util.List;

import org.dozer.Mapper;

import com.baidu.beidou.api.external.code.exporter.CodeService;
import com.baidu.beidou.api.external.code.vo.Category;
import com.baidu.beidou.api.external.code.vo.Region;
import com.baidu.beidou.api.external.code.vo.request.GetAllCategoryRequest;
import com.baidu.beidou.api.external.code.vo.request.GetAllRegionRequest;
import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.api.version.ApiVersionConstant;
import com.baidu.beidou.api.version.ApiVersionUtils;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.ub.beidou.region.bo.RegionInfo;
import com.baidu.beidou.cprogroup.vo.TradeInfo;
import com.baidu.beidou.user.service.UserInfoMgr;
import com.baidu.beidou.util.BeanMapperProxy;

/**
 * 
 * ClassName: CodeServiceImpl
 * Function: 查询网盟推广的网站行业分类代码与地域代码信息
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 23, 2011
 */
public class CodeServiceImpl implements CodeService {	
	
	//private static final Log log = LogFactory.getLog(CodeServiceImpl.class);

	private UserInfoMgr userInfoMgr;
	
	/**
	 * 获取所有的网站分类信息
	 * @param user
	 * @param reverseData
	 * @return
	 */
	public ApiResult<Category> getAllCategory(DataPrivilege user,
			GetAllCategoryRequest request, ApiOption apiOption) {
		ApiResult<Category> result = new ApiResult<Category>();
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		payment.setSuccess(1);
		result.setPayment(payment);
		
		List<TradeInfo> allTrade = UnionSiteCache.tradeInfoCache.getAllTrade();
		
		Mapper mapper = BeanMapperProxy.getMapper();
		for(TradeInfo trade : allTrade){
			Category category = mapper.map(trade, Category.class);
			//给editor区分百度自有流量
			if(trade.getViewstat() == CproGroupConstant.TRADE_WHITELIST && trade.getParentid() == 0){
				category.setParentId(-1);
			}
			result = ApiResultBeanUtils.addApiResult(result, category);
		}
		
		return result;
	}

	@Override
	public ApiResult<Region> getAllRegion(DataPrivilege user, GetAllRegionRequest request, ApiOption apiOption) {
        ApiResult<Region> result = new ApiResult<Region>();
        PaymentResult payment = new PaymentResult();
        payment.setTotal(1);
        payment.setSuccess(1);
        result.setPayment(payment);

        List<RegionInfo> allRegion = null;
		double version = 0;
		if (request != null && request.getVersion() != null) {
			version = Double.parseDouble(request.getVersion());
		}
		if (version >= ApiVersionConstant.SYS_REG_VERSION) {
            allRegion = UnionSiteCache.sysRegCache.getRegionList();
        } else {
            allRegion = UnionSiteCache.regCache.getRegionList();
        }

        for (RegionInfo region : allRegion) {
            Region category = new Region();
            category.setName(region.getName());
            category.setType(region.getType());
            category.setRegionId(region.getId());
            if (region.getParentId() != 0) {
                category.setParentId(region.getParentId());
            }
            result = ApiResultBeanUtils.addApiResult(result, category);
        }
        return result;
    }

	public UserInfoMgr getUserInfoMgr() {
		return userInfoMgr;
	}

	public void setUserInfoMgr(UserInfoMgr userInfoMgr) {
		this.userInfoMgr = userInfoMgr;
	}
	
}
