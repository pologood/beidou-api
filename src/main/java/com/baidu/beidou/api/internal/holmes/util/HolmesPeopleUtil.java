/**
 * beidou-api-rt3#com.baidu.beidou.api.internal.holmes.util.HolmesPeopleUtil.java
 * 12:38:47 AM created by Zhang Xu
 */
package com.baidu.beidou.api.internal.holmes.util;

import java.util.Date;

import com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleType;
import com.baidu.beidou.cprogroup.bo.VtPeople;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.VtPeopleConstant;
import com.baidu.beidou.util.StringUtils;

public class HolmesPeopleUtil {

	public static final int STAT_ENABLE = 1;

	public static final long DEFAULT_COOKIENUM = 0;

	public static VtPeople buildVtPeople(Integer userId, HolmesPeopleType holmesPeopleType) {
		VtPeople newVtPeople = null;
		newVtPeople = new VtPeople();
		newVtPeople.setPid(holmesPeopleType.getHolmesPid());
		newVtPeople.setUserId(userId);
		newVtPeople.setHpid(holmesPeopleType.getHolmesPid());
		newVtPeople.setHsiteid(holmesPeopleType.getHolmesSiteid());
		newVtPeople.setName(holmesPeopleType.getName());
		newVtPeople.setAliveDays(holmesPeopleType.getAlivedays());
		newVtPeople.setStat(STAT_ENABLE);
		newVtPeople.setCookieNum(DEFAULT_COOKIENUM);
		newVtPeople.setType(CproGroupConstant.PEOPLE_TYPE_HOLMES_ADVANCED);
		Date dateTime = new Date();
		newVtPeople.setActiveTime(dateTime);
		newVtPeople.setAddTime(dateTime);
		newVtPeople.setModTime(dateTime);
		newVtPeople.setAddUser(userId);
		newVtPeople.setModUser(userId);
		newVtPeople.setUrls(null);
		newVtPeople.setJsid(VtPeopleConstant.DEFAULT_JSID);
		return newVtPeople;
	}

	public static boolean isValidHolmesPeopleName(String name) {
		if (name == null || name.length() < 1) {
			return false;
		}
		// 人群名称name只能是中文、英文、数字、-、_
		if (StringUtils.validateHasSpecialChar(name)) {
			return false;
		}

		// 人群名称长度限制
		if (!StringUtils.validBeidouGbkStr(name, true, VtPeopleConstant.VTPEOPLE_NAME_MIN_LENGTH,
				VtPeopleConstant.VTPEOPLE_NAME_MAX_LENGTH)) {
			return false;
		}

		return true;
	}

	public static boolean isValidHolmesPeopleAlivedays(Integer alivedays) {
		if (alivedays == null) {
			return false;
		}
		if (!CproGroupConstant.VT_PEOPLE_ALIVEDAYS_LIST.contains(alivedays)) {
			return false;
		}
		return true;
	}

}
