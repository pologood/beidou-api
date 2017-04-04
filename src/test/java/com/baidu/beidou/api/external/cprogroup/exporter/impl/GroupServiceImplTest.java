package com.baidu.beidou.api.external.cprogroup.exporter.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cprogroup.vo.AdditionalGroupType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddGroupRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupByCampaignIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupByGroupIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetGroupIdByCampaignIdRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateAdditionalGroupRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateGroupRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;

//@RunWith(JMock.class)
@Ignore
public class GroupServiceImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 499;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource(name = "groupService")
	private GroupServiceImpl groupService;
	
	@Test
	public void testAddGroup() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddGroupRequest request = new AddGroupRequest();
		GroupType[] groupTypes = new GroupType[2];
		GroupType group = new GroupType();
		group.setCampaignId(105043);
		group.setGroupName("abek3");
		group.setPrice(100);
		group.setStatus(0);
		group.setType(5);
		group.setExcludeGender(8);
		groupTypes[0] = group;
		
		group = new GroupType();
		group.setCampaignId(105044);
		group.setGroupName("abek4");
		group.setPrice(100);
		group.setStatus(0);
		group.setType(1);
		groupTypes[1] = group;
		request.setGroupTypes(groupTypes);
		
		ApiResult<GroupType> result  = groupService.addGroup(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果
		long groupId1 = ((GroupType)(result.getData().get(0))).getGroupId();
		long planId1 = ((GroupType)(result.getData().get(0))).getCampaignId();
		assertThat(((GroupType)(result.getData().get(0))).getGroupName(), is("abek3"));
		assertThat(((GroupType)(result.getData().get(0))).getStatus(), is(0));
		assertThat(((GroupType)(result.getData().get(0))).getPrice(), is(100));
		assertThat(((GroupType)(result.getData().get(0))).getType(), is(5));
		assertThat(((GroupType)(result.getData().get(0))).getExcludeGender(), is(8)); 
		assertThat(((GroupType)(result.getData().get(1))).getExcludeGender(), is(0)); //排除性别默认为0
		
		GetGroupByGroupIdRequest request2 = new GetGroupByGroupIdRequest();
		request2.setGroupIds(new long[]{groupId1});
		ApiResult<GroupType> result2  = groupService.getGroupByGroupId(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		// 验证结果
		GroupType group1 = (GroupType)result2.getData().get(0);
		assertThat(group1.getCampaignId(), is(planId1));
		assertThat(group1.getGroupName(), is("abek3"));
		assertThat(group1.getExcludeGender(), is(8));
		assertThat(group1.getPrice(), is(100));
		assertThat(group1.getStatus(), is(0));
		assertThat(group1.getType(), is(5));
		
	}
	
	@Test
	public void testAddAdvancedGroup() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddGroupRequest request = new AddGroupRequest();
		GroupType[] groupTypes = new GroupType[1];
		GroupType group = new GroupType();
		group.setCampaignId(105043);
		group.setGroupName("neo1");
		group.setPrice(100);
		group.setStatus(0);
		group.setType(9);
		group.setExcludeGender(8);
		groupTypes[0] = group;
		
		request.setGroupTypes(groupTypes);
		
		ApiResult<GroupType> result  = groupService.addGroup(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		// 验证结果
		long groupId1 = ((GroupType)(result.getData().get(0))).getGroupId();
		long planId1 = ((GroupType)(result.getData().get(0))).getCampaignId();
		assertThat(((GroupType)(result.getData().get(0))).getGroupName(), is("neo1"));
		assertThat(((GroupType)(result.getData().get(0))).getStatus(), is(0));
		assertThat(((GroupType)(result.getData().get(0))).getPrice(), is(100));
		assertThat(((GroupType)(result.getData().get(0))).getType(), is(1));
		assertThat(((GroupType)(result.getData().get(0))).getExcludeGender(), is(8)); 
		
		GetGroupByGroupIdRequest request2 = new GetGroupByGroupIdRequest();
		request2.setGroupIds(new long[]{groupId1});
		ApiResult<GroupType> result2  = groupService.getGroupByGroupId(dataUser, request2, apiOption);
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		// 验证结果
		GroupType group1 = (GroupType)result2.getData().get(0);
		assertThat(group1.getCampaignId(), is(planId1));
		assertThat(group1.getGroupName(), is("neo1"));
		assertThat(group1.getExcludeGender(), is(8));
		assertThat(group1.getPrice(), is(100));
		assertThat(group1.getStatus(), is(0));
		assertThat(group1.getType(), is(1));
		
	}
	
	@Test
	public void testAddGroupDefault() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		AddGroupRequest request = new AddGroupRequest();
		GroupType[] groupTypes = new GroupType[2];
		GroupType group = new GroupType();
		group.setCampaignId(105043);
		group.setGroupName("abek-default");
		group.setPrice(100);
		group.setStatus(0);
		// 不设置展现类型，默认应该为固定+悬浮
//		group.setType(5);
		group.setExcludeGender(8);
		groupTypes[0] = group;
		
		group = new GroupType();
		group.setCampaignId(105043);
		group.setGroupName("abek4-fixed-film");
		group.setPrice(100);
		group.setStatus(0);
		// 设置展现类型：固定+贴片
		group.setType(5);
		groupTypes[1] = group;
		request.setGroupTypes(groupTypes);
		
		ApiResult<GroupType> result  = groupService.addGroup(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
	}
	
	@Test
	public void testGetGroupByCampaignId() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		
		GetGroupByCampaignIdRequest request = new GetGroupByCampaignIdRequest();
		request.setCampaignId(105044);
		ApiResult<GroupType> result  = groupService.getGroupByCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), greaterThan(1));
		assertThat(result.getPayment().getTotal(), greaterThan(1));
		assertThat(result.getPayment().getSuccess(), greaterThan(1));
		
		// 验证结果
		GroupType group = (GroupType)result.getData().get(0);
		assertThat(group.getCampaignId(), is(105044L));
	}
	
	@Test
	public void testGetGroupIdByCampaignId() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetGroupIdByCampaignIdRequest request = new GetGroupIdByCampaignIdRequest();
		request.setCampaignId(105044);
		ApiResult<Long> result  = groupService.getGroupIdByCampaignId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), greaterThan(0));
		assertThat(result.getPayment().getTotal(), greaterThan(1));
		assertThat(result.getPayment().getSuccess(), greaterThan(1));
		
		// 验证结果
	}
	
	@Test
	public void testGetGroupByGroupId() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		
		GetGroupByGroupIdRequest request = new GetGroupByGroupIdRequest();
		request.setGroupIds(new long[]{245297, 245298});
		ApiResult<GroupType> result  = groupService.getGroupByGroupId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果
		GroupType group = (GroupType)result.getData().get(0);
		assertThat(group.getCampaignId(), is(105044L));
		group = (GroupType)result.getData().get(1);
		assertThat(group.getGroupName(), is("Imported"));
	}
	
	//@Test
	/**
	 * TODO 该case对groupType升级没有改动，耿磊改下吧
	 */
	public void testUpdateGroup() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		UpdateGroupRequest request = new UpdateGroupRequest();
		GroupType[] groupTypes = new GroupType[2];
		GroupType group = new GroupType();
		group.setCampaignId(105043);
		group.setGroupId(245296);
		group.setGroupName("abek33");
		group.setPrice(100);
		group.setStatus(1);
		group.setType(0);
		groupTypes[0] = group;
		
		group = new GroupType();
		group.setCampaignId(105044);
		group.setGroupId(245297);
		group.setGroupName("abek44");
		group.setPrice(100);
		group.setStatus(2);
		group.setType(0);
		groupTypes[1] = group;
		request.setGroupTypes(groupTypes);
		
		ApiResult<GroupType> result  = groupService.updateGroup(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		
		// 验证结果
		group = (GroupType)result.getData().get(0);
		assertThat(group.getCampaignId(), is(105043L));
		group = (GroupType)result.getData().get(1);
		assertThat(group.getGroupName(), is("abek44"));
	}
	
	@Test
	public void testUpdateGroupForGroupType() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		UpdateGroupRequest request = new UpdateGroupRequest();
		GroupType[] groupTypes = new GroupType[2];
		GroupType group = new GroupType();
		group.setCampaignId(105043);
		group.setGroupId(245296);
		group.setGroupName("abek-fixed-to-flow");
		group.setPrice(100);
		group.setStatus(2);
		group.setType(2);
		groupTypes[0] = group;
		
		group = new GroupType();
		group.setCampaignId(105044);
		group.setGroupId(245297);
		group.setGroupName("abek-fixed-add-film");
		group.setPrice(100);
		group.setStatus(2);
		group.setType(5);
		groupTypes[1] = group;
		request.setGroupTypes(groupTypes);
		
		ApiResult<GroupType> result  = groupService.updateGroup(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(1));
	}
	
	@Test
	public void testUpdateAdditionalGroup_Name_Price_Status_ExcludeGender() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		UpdateAdditionalGroupRequest request = new UpdateAdditionalGroupRequest();
		AdditionalGroupType[] groupTypes = new AdditionalGroupType[2];
		AdditionalGroupType group = new AdditionalGroupType();
		group.setGroupId(245249);
		group.setGroupName("更新名称123");
		group.setPrice(49);
		//group.setStatus(2);
		//group.setExcludeGender(2);
		//group.setType(5);
		//group.setTargetType(1);
		//group.setAllRegion(false);
		//group.setAllSite(true);
		//group.setItEnabled(true);
		groupTypes[0] = group;
		
		group = new AdditionalGroupType();
		group.setGroupId(245250);
		//group.setGroupName("更新名称123");
		//group.setPrice(49);
		group.setStatus(2);
		group.setExcludeGender(8);
		//group.setType(5);
		//group.setTargetType(1);
		//group.setAllRegion(false);
		//group.setAllSite(true);
		//group.setItEnabled(true);
		groupTypes[1] = group;
		request.setGroupTypes(groupTypes);
		
		ApiResult<AdditionalGroupType> result  = groupService.updateAdditionalGroup(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		List<AdditionalGroupType> data = result.getData();
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		assertThat(data.get(0).getGroupName(), is("更新名称123"));
		assertThat(data.get(0).getPrice(), is(49));
		assertThat(data.get(0).getStatus(), is(0));
		assertThat(data.get(0).getType(), is(1));
		assertThat(data.get(0).getTargetType(), is(3));
		assertThat(data.get(1).getStatus(), is(2));
		assertThat(data.get(1).getExcludeGender(), is(8));
		assertThat(data.get(1).getType(), is(1));
		assertThat(data.get(1).getTargetType(), is(3));
		assertThat(result.getPayment().getSuccess(), is(2));
		
	}
	
	@Test
	public void testUpdateAdditionalGroup_Targetype_Type() {
		final int userId = 499;
		 
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		UpdateAdditionalGroupRequest request = new UpdateAdditionalGroupRequest();
		AdditionalGroupType[] groupTypes = new AdditionalGroupType[2];
		AdditionalGroupType group = new AdditionalGroupType();
		group.setGroupId(245249);
		group.setGroupName("更新名称123");
		//group.setPrice(49);
		//group.setStatus(2);
		//group.setExcludeGender(2);
		group.setType(5);
		group.setTargetType(2);
		//group.setAllRegion(false);
		//group.setAllSite(true);
		group.setItEnabled(true);
		groupTypes[0] = group;
		
		group = new AdditionalGroupType();
		group.setGroupId(245250);
		//group.setGroupName("更新名称123");
		//group.setPrice(49);
		//group.setStatus(2);
		//group.setExcludeGender(1);
		//group.setType(5);
		group.setTargetType(2);
		//group.setAllRegion(false);
		//group.setAllSite(true);
		//group.setItEnabled(true);
		group.setItEnabled(true);
		groupTypes[1] = group;
		request.setGroupTypes(groupTypes);
		
		ApiResult<AdditionalGroupType> result  = groupService.updateAdditionalGroup(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		List<AdditionalGroupType> data = result.getData();
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		assertThat(data.get(0).getGroupName(), is("更新名称123"));
		assertThat(data.get(0).getPrice(), is(150));
		assertThat(data.get(0).getStatus(), is(0));
		assertThat(data.get(0).getType(), is(5));
		assertThat(data.get(0).getTargetType(), is(2));
		assertThat(data.get(1).getStatus(), is(0));
		assertThat(data.get(1).getExcludeGender(), is(0));
		assertThat(data.get(1).getType(), is(1));
		assertThat(data.get(1).getTargetType(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
	}
	
	@Test
	public void testUpdateAdditionalGroup_TargetType_isAllRegion_isAllSite_isItEnabled() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		UpdateAdditionalGroupRequest request = new UpdateAdditionalGroupRequest();
		AdditionalGroupType[] groupTypes = new AdditionalGroupType[2];
		AdditionalGroupType group = new AdditionalGroupType();
		group.setGroupId(245249);
		group.setGroupName("更新名称123");
		group.setPrice(49);
		//group.setStatus(2);
		//group.setExcludeGender(2);
		//group.setType(5);
		group.setTargetType(1);
		//group.setAllRegion(false);
		//group.setAllSite(true);
		group.setItEnabled(true);
		groupTypes[0] = group;
		
		group = new AdditionalGroupType();
		group.setGroupId(245250);
		//group.setGroupName("更新名称123");
		//group.setPrice(49);
		//group.setStatus(2);
		//group.setExcludeGender(1);
		//group.setType(5);
		group.setTargetType(4);
		group.setAllRegion(false);
		group.setAllSite(true);
		group.setItEnabled(true);
		groupTypes[1] = group;
		request.setGroupTypes(groupTypes);
		
		ApiResult<AdditionalGroupType> result  = groupService.updateAdditionalGroup(dataUser, request, apiOption);
		
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		List<AdditionalGroupType> data = result.getData();
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));
		assertThat(data.get(0).getGroupName(), is("更新名称123"));
		assertThat(data.get(0).getPrice(), is(49));
		assertThat(data.get(0).getStatus(), is(0));
		assertThat(data.get(0).getType(), is(1));
		assertThat(data.get(0).getTargetType(), is(1));
		assertThat(data.get(0).isAllRegion(), is(true));
		assertThat(data.get(0).isAllSite(), is(false));
		assertThat(data.get(0).isItEnabled(), is(true));
		assertThat(data.get(1).getStatus(), is(0));
		assertThat(data.get(1).getExcludeGender(), is(0));
		assertThat(data.get(1).getType(), is(1));
		assertThat(data.get(1).getTargetType(), is(4));
		assertThat(data.get(1).isAllRegion(), is(false));
		assertThat(data.get(1).isAllSite(), is(true));
		assertThat(data.get(1).isItEnabled(), is(true));
		assertThat(result.getPayment().getSuccess(), is(2));
		
	}
	
}
