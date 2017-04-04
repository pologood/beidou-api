package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;

import com.baidu.beidou.account.service.MfcService;
import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.AccountVo;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.BeanMapperProxy;

/**
 * 
 * ClassName: AccountHandler <br>
 * Function: 账户文件输出VO的handler
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class AccountHandler extends Handler {

	//private static final Log log = LogFactory.getLog(AccountHandler.class);

	private UserMgr userMgr = null;

	private MfcService mfcService = null;

	/**
	 * 生成账户VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		User userInfo = userMgr.findUserBySFid(userId);

		Mapper mapper = BeanMapperProxy.getMapper();
		AccountVo accountVo = mapper.map(userInfo, AccountVo.class);

		Double mfcDataResult = mfcService.getUserBalance(userId);

		if (mfcDataResult == null) {
			accountVo.setBalance(0f);
		} else {
			accountVo.setBalance(mfcDataResult.floatValue());
		}

		list.add(accountVo);

		return list;
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public MfcService getMfcService() {
		return mfcService;
	}

	public void setMfcService(MfcService mfcService) {
		this.mfcService = mfcService;
	}

}
