package com.baidu.beidou.api.internal.dsp.exporter;

import com.baidu.beidou.api.internal.dsp.vo.DspPeopleCodeResult;
import com.baidu.beidou.api.internal.dsp.vo.DspPeoplePidResult;
import com.baidu.beidou.api.internal.dsp.vo.PeopleResult;

/**
 * DSP&凤巢人群相关接口
 * 
 * @author cachao
 */
public interface DspPeopleService {
	/**
	 * 获取用户网盟一站式人群代码
	 * @param userId
	 * @param type 1:表示基于标记  2:表示一站式代码
	 * @return
	 * @author caichao
	 */
	DspPeopleCodeResult getDspPeopleCode(int userId,int type);
	
	/**
	 * 获取网盟人群id
	 * @param userId
	 * @return
	 * @author caichao
	 */
	DspPeoplePidResult getDspPeoplePid(int userId);
	
	/**
	 * 提供凤巢到访人群代码
	 */
	PeopleResult getPeoples(int userId);
}
