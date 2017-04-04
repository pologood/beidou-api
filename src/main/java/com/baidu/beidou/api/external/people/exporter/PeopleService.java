package com.baidu.beidou.api.external.people.exporter;

import com.baidu.beidou.api.external.people.vo.PeopleType;
import com.baidu.beidou.api.external.people.vo.request.GetAllPeopleRequest;
import com.baidu.beidou.api.external.people.vo.request.GetPeopleRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * 
 * ClassName: PeopleService  <br>
 * Function: 通过PepleService，查询账户的人群设置
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 9, 2012
 */
public interface PeopleService {
	
	/**
	 * 
	 * getAllPeople 获取所有人群
	 * @version 2.0.0
	 * @author zhangxu
	 * @date Jan 10, 2012
	 */
	public ApiResult<PeopleType> getAllPeople(DataPrivilege user, GetAllPeopleRequest request, ApiOption apiOption);
	
	/**
	 * 
	 * getPeople 根据peopleIds获取人群
	 * @version 2.0.0
	 * @author zhangxu
	 * @date Jan 10, 2012
	 */
	public ApiResult<PeopleType> getPeople(DataPrivilege user, GetPeopleRequest request, ApiOption apiOption);
	
}
