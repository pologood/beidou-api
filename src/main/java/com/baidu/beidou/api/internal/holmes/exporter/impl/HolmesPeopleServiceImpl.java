/**
 * beidou-api-rt3#com.baidu.beidou.api.internal.holmes.exporter.impl.HolmesPeopleServiceImpl.java
 * 7:16:52 PM created by Zhang Xu
 */
package com.baidu.beidou.api.internal.holmes.exporter.impl;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.baidu.beidou.api.internal.holmes.constant.Status;
import com.baidu.beidou.api.internal.holmes.error.HolmesPeopleErrorCode;
import com.baidu.beidou.api.internal.holmes.exporter.HolmesPeopleService;
import com.baidu.beidou.api.internal.holmes.util.HolmesPeopleUtil;
import com.baidu.beidou.api.internal.holmes.vo.AddHolmesPeopleResult;
import com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleResult;
import com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleType;
import com.baidu.beidou.common.bo.ObjectTypes;
import com.baidu.beidou.cprogroup.bo.VtPeople;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.pack.facade.PackFacade;
import com.baidu.beidou.tool.vo.OptContent;

/**
 * Holmes人群相关接口
 * 
 * @author Zhang Xu
 */
public class HolmesPeopleServiceImpl implements HolmesPeopleService {

	private static final Log log = LogFactory.getLog(HolmesPeopleServiceImpl.class);

	private VtPeopleMgr vtPeopleMgr;
	
	private PackFacade packFacade;

	public AddHolmesPeopleResult addHolmesPeople(Integer userId, HolmesPeopleType holmesPeopleType) {
		AddHolmesPeopleResult result = new AddHolmesPeopleResult();
		try {

			// 人群个数限制检查
			Integer exsitVtPeopleNum = vtPeopleMgr.getVtPeopleNum(userId);
			if (exsitVtPeopleNum.intValue() >= CproGroupConstant.USER_VT_PACK_MAX_NUM) {
				result.setStatus(HolmesPeopleErrorCode.PEOPLE_NUM_EXCEED.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_NUM_EXCEED.getMessage());
				return result;
			}
			
			// 人群名称是否合法
			if (!HolmesPeopleUtil.isValidHolmesPeopleName(holmesPeopleType.getName())) {
				result.setStatus(HolmesPeopleErrorCode.PEOPLE_NAME_INVALID.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_NAME_INVALID.getMessage());
				return result;
			}
			
			// 人群有效期是否合法
			if (!HolmesPeopleUtil.isValidHolmesPeopleAlivedays((holmesPeopleType.getAlivedays()))) {
				result.setStatus(HolmesPeopleErrorCode.ALIVEDAYS_INVALID.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.ALIVEDAYS_INVALID.getMessage());
				return result;
			}

			// 人群名称是否重复
			VtPeople vtPeople = vtPeopleMgr.findVtPeopleByNameAndUserId(holmesPeopleType.getName(), userId);
			if (vtPeople != null) {
				result.setStatus(HolmesPeopleErrorCode.PEOPLE_NAME_DUPLICATE.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_NAME_DUPLICATE.getMessage());
				return result;
			}

			VtPeople newVtPeople = HolmesPeopleUtil.buildVtPeople(userId, holmesPeopleType);
			vtPeopleMgr.addVtPeople(newVtPeople, false, false);

			result.setPid(newVtPeople.getPid());
			result.setStatus(Status.SUCCESS);
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.setStatus(Status.ERROR);
			return result;
		}
	}

	public HolmesPeopleResult updateHolmesPeopleName(Integer userId, Long holmesPid, String name) {
		HolmesPeopleResult result = new HolmesPeopleResult();
		try {
			
			VtPeople oldVtPeople = vtPeopleMgr.findVtPeopleByHpid(holmesPid, userId);
			
			// 验证holmes人群是否存在
			if (oldVtPeople == null) {
				result.setStatus(HolmesPeopleErrorCode.PEOPLE_ID_NOT_FOUND.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_ID_NOT_FOUND.getMessage());
				return result;
			}
			
			// 人群名称是否合法
			if (!HolmesPeopleUtil.isValidHolmesPeopleName(name)) {
				result.setStatus(HolmesPeopleErrorCode.PEOPLE_NAME_INVALID.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_NAME_INVALID.getMessage());
				return result;
			}
			
			// 人群所属用户是否合法
			if (!oldVtPeople.getUserId().equals(userId)) {
				result.setStatus(HolmesPeopleErrorCode.PEOPLE_ID_USERID_NOT_MATCH.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_ID_USERID_NOT_MATCH.getMessage());
				return result;
			}
			
			// 人群名称是否重复
			VtPeople vtPeople = vtPeopleMgr.findVtPeopleByNameAndUserId(name, userId);
			if (vtPeople != null) {
				if (!vtPeople.getPid().equals(oldVtPeople.getPid()) ) {
					result.setStatus(HolmesPeopleErrorCode.PEOPLE_NAME_DUPLICATE.getValue());
					result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_NAME_DUPLICATE.getMessage());
					return result;
				}
			}
			
			oldVtPeople.setName(name);
			vtPeopleMgr.modifyVtPeople(oldVtPeople, false, false, null, null);
			
			result.setStatus(Status.SUCCESS);
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.setStatus(Status.ERROR);
			return result;
		}
	}

	public HolmesPeopleResult updateHolmesPeopleAlivedays(Integer userId, Long holmesPid, Integer alivedays) {
		HolmesPeopleResult result = new HolmesPeopleResult();
		try {
			
			VtPeople oldVtPeople = vtPeopleMgr.findVtPeopleByHpid(holmesPid, userId);
			
			// 验证holmes人群是否存在
			if (oldVtPeople == null) {
				result.setStatus(HolmesPeopleErrorCode.PEOPLE_ID_NOT_FOUND.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_ID_NOT_FOUND.getMessage());
				return result;
			}
			
			// 人群有效期是否合法
			if (!HolmesPeopleUtil.isValidHolmesPeopleAlivedays(alivedays)) {
				result.setStatus(HolmesPeopleErrorCode.ALIVEDAYS_INVALID.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.ALIVEDAYS_INVALID.getMessage());
				return result;
			}
			
			// 人群所属用户是否合法
			if (!oldVtPeople.getUserId().equals(userId)) {
				result.setStatus(HolmesPeopleErrorCode.PEOPLE_ID_USERID_NOT_MATCH.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_ID_USERID_NOT_MATCH.getMessage());
				return result;
			}
			
			oldVtPeople.setAliveDays(alivedays);
			vtPeopleMgr.modifyVtPeople(oldVtPeople, false, false, null, null);
			
			result.setStatus(Status.SUCCESS);
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.setStatus(Status.ERROR);
			return result;
		}
	}

	public HolmesPeopleResult deleteHolmesPeople(Integer userId, Long holmesPid) {
		HolmesPeopleResult result = new HolmesPeopleResult();
		try {
			
			VtPeople oldVtPeople = vtPeopleMgr.findVtPeopleByHpid(holmesPid, userId);
			
			// 验证holmes人群是否存在
			if (oldVtPeople == null) {
				result.setStatus(HolmesPeopleErrorCode.PEOPLE_ID_NOT_FOUND.getValue());
				result.setErrorMsg(HolmesPeopleErrorCode.PEOPLE_ID_NOT_FOUND.getMessage());
				return result;
			}
	
			// 删除人群时暂不记录历史操作记录
			packFacade.delPackByPackIdAndType(oldVtPeople.getPid().intValue(), ObjectTypes.TYPE_VT_PEOPLE, userId, new ArrayList<OptContent>(), false);
			
			result.setStatus(Status.SUCCESS);
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.setStatus(Status.ERROR);
			return result;
		}
	}

	public VtPeopleMgr getVtPeopleMgr() {
		return vtPeopleMgr;
	}

	public void setVtPeopleMgr(VtPeopleMgr vtPeopleMgr) {
		this.vtPeopleMgr = vtPeopleMgr;
	}

	public PackFacade getPackFacade() {
		return packFacade;
	}

	public void setPackFacade(PackFacade packFacade) {
		this.packFacade = packFacade;
	}

}
