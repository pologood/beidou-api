/**
 * beidou-api-rt3#com.baidu.beidou.api.internal.holmes.exporter.HolmesPeopleService.java
 * 7:16:31 PM created by Zhang Xu
 */
package com.baidu.beidou.api.internal.holmes.exporter;

import com.baidu.beidou.api.internal.holmes.vo.AddHolmesPeopleResult;
import com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleResult;
import com.baidu.beidou.api.internal.holmes.vo.HolmesPeopleType;

/**
 * Holmes人群相关接口
 * 
 * @author Zhang Xu
 */
public interface HolmesPeopleService {

	AddHolmesPeopleResult addHolmesPeople(Integer userId, HolmesPeopleType holmesPeopleType);

	HolmesPeopleResult updateHolmesPeopleName(Integer userId, Long holmesPid, String name);

	HolmesPeopleResult updateHolmesPeopleAlivedays(Integer userId, Long holmesPid, Integer alivedays);

	HolmesPeopleResult deleteHolmesPeople(Integer userId, Long holmesPid);

}
