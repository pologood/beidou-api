package com.baidu.beidou.api.external.cprogroup.exporter.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.cprogroup.exporter.GroupService;
import com.baidu.beidou.api.external.cprogroup.vo.AdditionalGroupType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddGroupRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupByCampaignIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupIdByCampaignIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateAdditionalGroupRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateGroupRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;
@Ignore
public class GroupServiceTest extends ApiBaseRPCTest<GroupService> {

	@Test
	public void testAddGroup() throws Exception {
		GroupService exporter = getServiceProxy(GroupService.class, ApiExternalConstant.GROUP_SERVICE_URL);
		AddGroupRequest request = new AddGroupRequest();

		GroupType[] types = new GroupType[1];

		GroupType type0 = new GroupType();
		type0.setCampaignId(1987428);
		type0.setGroupId(-530634100008240L);
		type0.setGroupName("911445");
		type0.setPrice(10);
		type0.setStatus(0);
		type0.setType(1);

		types[0] = type0;
		request.setGroupTypes(types);
		ApiResult<GroupType> result = exporter.addGroup(dataUser, request, apiOption);
		System.out.println(result);
	}

	@Test
	public void testUpdateGroup() throws Exception {
		GroupService exporter = getServiceProxy(GroupService.class, ApiExternalConstant.GROUP_SERVICE_URL);
		UpdateGroupRequest request = new UpdateGroupRequest();

		GroupType[] types = new GroupType[1];

		GroupType type0 = new GroupType();
		type0.setCampaignId(984810);
		type0.setGroupId(3066357);
		type0.setGroupName("士力架1");
		type0.setPrice(555);
		type0.setStatus(2);
		type0.setType(1);

		GroupType type1 = new GroupType();
		type1.setCampaignId(757454);
		type1.setGroupId(2166163);
		type1.setGroupName("提拉米苏1");
		type1.setPrice(12);
		type1.setStatus(1);
		type1.setType(1);

		GroupType type2 = new GroupType();
		type2.setCampaignId(757454);
		type2.setGroupId(2166162);
		type2.setGroupName("牛肉");
		type2.setPrice(100);
		type2.setStatus(1);
		type2.setType(1);

		types[0] = type0;
		//types[1]=type1;
		//types[2]=type2;
		request.setGroupTypes(types);
		ApiResult<GroupType> result = exporter.updateGroup(dataUser, request, apiOption);
		System.out.println(result);
	}

	@Test
	public void testUpdateAdditionalGroup() throws Exception {
		GroupService exporter = getServiceProxy(GroupService.class, ApiExternalConstant.GROUP_SERVICE_URL);
		UpdateAdditionalGroupRequest request = new UpdateAdditionalGroupRequest();

		AdditionalGroupType[] types = new AdditionalGroupType[1];

		AdditionalGroupType type0 = new AdditionalGroupType();
		type0.setGroupId(245252);
		//		type0.setGroupName("更新名称123");
		//		type0.setPrice(49);
		//		type0.setStatus(2);
		//		type0.setExcludeGender(1);
		//		type0.setType(7);
		//		type0.setTargetType(2);
		//		type0.setAllRegion(false);
		//		type0.setAllSite(true);
		type0.setItEnabled(false);

		AdditionalGroupType type1 = new AdditionalGroupType();
		type1.setGroupId(2166163);
		type1.setGroupName("提拉米苏1");
		type1.setPrice(12);
		type1.setStatus(1);
		type1.setType(1);

		types[0] = type0;
		//types[1]=type1;
		//types[2]=type2;
		request.setGroupTypes(types);
		ApiResult<AdditionalGroupType> result = exporter.updateAdditionalGroup(dataUser, request, apiOption);
		System.out.println(result);
	}

	@Test
	public void testGetGroupIdByCampaignId() throws Exception {
		GroupService exporter = getServiceProxy(GroupService.class, ApiExternalConstant.GROUP_SERVICE_URL);
		GetGroupIdByCampaignIdRequest request = new GetGroupIdByCampaignIdRequest();
		request.setCampaignId(1987480);
		ApiResult<Long> result = exporter.getGroupIdByCampaignId(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	public void testGetGroupByCampaignId() throws Exception {
		GroupService exporter = getServiceProxy(GroupService.class, ApiExternalConstant.GROUP_SERVICE_URL);
		GetGroupByCampaignIdRequest request = new GetGroupByCampaignIdRequest();
		request.setCampaignId(1987480);
		ApiResult<GroupType> result = exporter.getGroupByCampaignId(dataUser, request, apiOption);
		System.out.println(result);
	}
}	
