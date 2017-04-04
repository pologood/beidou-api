package com.baidu.beidou.api.internal.dsp.exporter.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.baidu.beidou.api.internal.dsp.constant.DspConstant;
import com.baidu.beidou.api.internal.dsp.error.DspPeopleErrorCode;
import com.baidu.beidou.api.internal.dsp.exporter.DspPeopleService;
import com.baidu.beidou.api.internal.dsp.vo.DspPeopleCodeResult;
import com.baidu.beidou.api.internal.dsp.vo.DspPeoplePidResult;
import com.baidu.beidou.api.internal.dsp.vo.PeopleResult;
import com.baidu.beidou.api.internal.dsp.vo.PeopleType;
import com.baidu.beidou.cprogroup.bo.VtCode;
import com.baidu.beidou.cprogroup.bo.VtPeople;
import com.baidu.beidou.cprogroup.service.VtCodeMgr;
import com.baidu.beidou.cprogroup.service.VtPeopleMgr;
import com.baidu.beidou.tool.service.HolmesPeopleMgr;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;

/**
 * DSP人群相关接口实现
 * 
 * @author cachao
 */
public class DspPeopleServiceImpl implements DspPeopleService{
	private static final Log log = LogFactory.getLog(DspPeopleServiceImpl.class);
	
	private UserMgr userMgr;
	private VtCodeMgr vtCodeMgr;
	private VtPeopleMgr vtPeopleMgr;
	
    private HolmesPeopleMgr holmesPeopleMgr;
	
	public DspPeopleCodeResult getDspPeopleCode(int userId,int type) {
		DspPeopleCodeResult result = new DspPeopleCodeResult();
		try {
			//判断请求中userid是否是北斗用户
			User user = userMgr.findUserBySFid(userId);
			if (user == null) {
				result.setStatus(DspPeopleErrorCode.USERID_INVALID.getValue());
				result.setErrorMsg(DspPeopleErrorCode.USERID_INVALID.getMessage());
				return result;
			}
			//type类型只能为1 和 2 1:表示基于标记的代码 2：表示返回一站式代码
			if (type != DspConstant.SIGN_CODE && type != DspConstant.ALL_SITE_CODE) {
				result.setStatus(DspPeopleErrorCode.TYPE_INVALID.getValue());
				result.setErrorMsg(DspPeopleErrorCode.TYPE_INVALID.getMessage());
				return result;
			}
			VtCode vtCode = null;
			if (type == DspConstant.SIGN_CODE) {
				vtCode = vtPeopleMgr.saveNewSiteCodeWithUserId(userId);
				result.setCode(vtCode.getSign());
				result.setJsid(vtCode.getJsid());
				result.setStatus(DspConstant.SUCCESS);
			} else {
				vtCode = vtCodeMgr.findAllSiteCodeByUserId(userId);
				//第一次为null，说明数据库确实没有全站代码
				if (vtCode == null) {
					vtCode = vtPeopleMgr.saveAllSiteCodeWithUserId(userId);
				}
				//第二次为null,说明由于web端并发，web端生成全站代码，这种情况再次查询数据库获取全站代码,理论上概率很小
				if (vtCode == null) {
					vtCode = vtCodeMgr.findAllSiteCodeByUserId(userId);
				}
				//第三次null 说明数据保存失败
				if (vtCode == null) {
					result.setStatus(DspPeopleErrorCode.SAVE_DB_FAIL.getValue());
					result.setErrorMsg(DspPeopleErrorCode.SAVE_DB_FAIL.getMessage());
					return result;
				}
				result.setCode(vtCode.getSign());
				result.setJsid(vtCode.getJsid());
				result.setStatus(DspConstant.SUCCESS);
			}
			
		} catch (Exception e){
			log.error(e.getMessage(), e);
			result.setStatus(DspConstant.ERROR);
			result.setErrorMsg(e.getMessage());
			return result;
		}
		
		return result;
	}
	
	public DspPeoplePidResult getDspPeoplePid(int userId) {
		DspPeoplePidResult result = new DspPeoplePidResult();
		
		try {
			//判断请求中userid是否是北斗用户
			User user = userMgr.findUserBySFid(userId);
			if (user == null) {
				result.setStatus(DspPeopleErrorCode.USERID_INVALID.getValue());
				result.setErrorMsg(DspPeopleErrorCode.USERID_INVALID.getMessage() + "[userId] : " + userId);
				return result;
			}
			Long pid = holmesPeopleMgr.getNextDmpGroupId();
			if (pid <= 0) {
				result.setStatus(DspPeopleErrorCode.RETURN_PID_INVALID.getValue());
				result.setErrorMsg(DspPeopleErrorCode.RETURN_PID_INVALID.getMessage());
				return result;
			}
			result.setPid(pid);
			result.setStatus(DspConstant.SUCCESS);
			
		} catch (Exception e){
			log.error(e.getMessage(), e);
			result.setStatus(DspConstant.ERROR);
			result.setErrorMsg(e.getMessage());
			return result;
		}
		
		return result;
	}
	
	@Override
	public PeopleResult getPeoples(int userId) {
		
		PeopleResult result = new PeopleResult();
		try {
			//判断请求中userid是否是北斗用户
			User user = userMgr.findUserBySFid(userId);
			if (user == null) {
				result.setStatus(DspPeopleErrorCode.USERID_INVALID.getValue());
				result.setErrorMsg(DspPeopleErrorCode.USERID_INVALID.getMessage());
				return result;
			}
			
			List<VtPeople> peopleList = vtPeopleMgr.getPeoples(userId);
			
			if (CollectionUtils.isEmpty(peopleList)) {
				result.setPeopleList(new ArrayList<PeopleType>(0));
				return result;
			}
			
			//排除掉两种人群4：无代码人群 5：全站人群
			List<PeopleType> resultList = new ArrayList<PeopleType>(peopleList.size());
			for (VtPeople vtpeople : peopleList) {
//				if (vtpeople.getType().equals(CproGroupConstant.PEOPLE_TYPE_SELF_PEOPLE) 
//						|| vtpeople.getType().equals(CproGroupConstant.PEOPLE_TYPE_SELF_PEOPLE_ALLSIZE)) {
//					continue;
//				}
				PeopleType people = new PeopleType();
				people.setPid(vtpeople.getPid());
				people.setName(vtpeople.getName());
				people.setAliveDays(vtpeople.getAliveDays());
				people.setCookieNum(vtpeople.getCookieNum());
				people.setType(vtpeople.getType());
				resultList.add(people);
			}
			
			//超过2000个人群时，截取前2000个 
			int limit = 2000;
			if (resultList.size() > limit) {
				resultList = resultList.subList(0, limit);
			}
			
			result.setPeopleList(resultList);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.setStatus(DspConstant.ERROR);
			result.setErrorMsg(e.getMessage());
			return result;
		}
		
		return result;
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public VtCodeMgr getVtCodeMgr() {
		return vtCodeMgr;
	}

	public void setVtCodeMgr(VtCodeMgr vtCodeMgr) {
		this.vtCodeMgr = vtCodeMgr;
	}

	public VtPeopleMgr getVtPeopleMgr() {
		return vtPeopleMgr;
	}

	public void setVtPeopleMgr(VtPeopleMgr vtPeopleMgr) {
		this.vtPeopleMgr = vtPeopleMgr;
	}

    public void setHolmesPeopleMgr(HolmesPeopleMgr holmesPeopleMgr) {
        this.holmesPeopleMgr = holmesPeopleMgr;
    }
}
