package com.baidu.beidou.api.external.people.exporter.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;

import com.baidu.beidou.api.external.people.constant.PeopleConstant;
import com.baidu.beidou.api.external.people.error.PeopleErrorCode;
import com.baidu.beidou.api.external.people.exporter.PeopleService;
import com.baidu.beidou.api.external.people.vo.PeopleType;
import com.baidu.beidou.api.external.people.vo.request.GetAllPeopleRequest;
import com.baidu.beidou.api.external.people.vo.request.GetPeopleRequest;
import com.baidu.beidou.api.external.user.error.UserErrorCode;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cprogroup.bo.VtPeople;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.cprogroup.vo.VtPeopleVo;
import com.baidu.beidou.util.BeanMapperProxy;

 /**
  * 
  * ClassName: PeopleServiceImpl  <br>
  * Function: 查询账户的人群设置
  *
  * @author zhangxu
  * @version 2.0.0
  * @since cpweb357
  * @date Feb 3, 2012
  */
public class PeopleServiceImpl implements PeopleService {
	
	private VtPeopleMgr vtPeopleMgr;
	
	private int pidListMax;

	/**
	 * 
	 * getAllPeople 获取所有人群
	 * @version 2.0.0
	 * @author zhangxu
	 * @date Jan 10, 2012
	 */
	public ApiResult<PeopleType> getAllPeople(DataPrivilege user, GetAllPeopleRequest request, ApiOption apiOption){
		ApiResult<PeopleType> result = new ApiResult<PeopleType>();
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
		List<VtPeopleVo> voPeopleList = vtPeopleMgr.findVtPeopleList(user.getDataUser(), null);
		if (voPeopleList == null || CollectionUtils.isEmpty(voPeopleList)) {
			result.setData(new ArrayList<PeopleType>());
			PaymentResult payment = new PaymentResult();
			payment.setTotal(1);
			payment.setSuccess(1);
			result.setPayment(payment);
			return result;
		}
		
		Mapper mapper = BeanMapperProxy.getMapper();
		for(VtPeopleVo vo : voPeopleList){
			
			PeopleType people = mapper.map(vo, PeopleType.class);
			result = ApiResultBeanUtils.addApiResult(result, people);
		}
		
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		payment.setSuccess(1);
		result.setPayment(payment);
		return result;
	}
	
	/**
	 * 
	 * getPeople 获取指定人群
	 * @version 2.0.0
	 * @author zhangxu
	 * @date Jan 10, 2012
	 */
	public ApiResult<PeopleType> getPeople(DataPrivilege user, GetPeopleRequest request, ApiOption apiOption){
		ApiResult<PeopleType> result = new ApiResult<PeopleType>();
		
		if (user == null) {
			result = ApiResultBeanUtils.addApiError(result,
					UserErrorCode.NO_USER.getValue(), UserErrorCode.NO_USER
							.getMessage(), null, null);
			return result;
		}
		
		long[] pids = request.getPeopleIds();
		if(pids == null || pids.length == 0){
			return result;
		}
		
		if (pids.length > pidListMax) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(PeopleConstant.PIDS);
			
			result = ApiResultBeanUtils.addApiError(result,
					PeopleErrorCode.PEOPLE_LIST_TOO_LONG.getValue(),
					PeopleErrorCode.PEOPLE_LIST_TOO_LONG.getMessage(), 
					apiPosition.getPosition(), null);
			return result;
		}
		
		List<Long> pidList = new ArrayList<Long>();
		for(long pid: pids){
			pidList.add(pid);
		}
		List<VtPeople> voPeopleList = new ArrayList<VtPeople>();
		for(long pid: pids){
			VtPeople people = vtPeopleMgr.findVtPeople(pid, user.getDataUser());
			voPeopleList.add(people);
		}
		
		Mapper mapper = BeanMapperProxy.getMapper();
		for(VtPeople vo : voPeopleList){
			if (vo != null) {
				PeopleType people = mapper.map(vo, PeopleType.class);
				result = ApiResultBeanUtils.addApiResult(result, people);
			}
		}
		
		PaymentResult payment = new PaymentResult();
		payment.setTotal(1);
		payment.setSuccess(1);
		result.setPayment(payment);
		return result;
	}

	public VtPeopleMgr getVtPeopleMgr() {
		return vtPeopleMgr;
	}

	public void setVtPeopleMgr(VtPeopleMgr vtPeopleMgr) {
		this.vtPeopleMgr = vtPeopleMgr;
	}

	public int getPidListMax() {
		return pidListMax;
	}

	public void setPidListMax(int pidListMax) {
		this.pidListMax = pidListMax;
	}
	
}
