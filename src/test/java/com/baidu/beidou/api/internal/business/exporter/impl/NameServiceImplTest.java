package com.baidu.beidou.api.internal.business.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.baidu.beidou.api.internal.business.exporter.NameService;
import com.baidu.beidou.api.internal.business.vo.GroupInfo;
import com.baidu.beidou.api.internal.business.vo.GroupResult;
import com.baidu.beidou.api.internal.business.vo.PlanInfo;
import com.baidu.beidou.api.internal.business.vo.PlanResult;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
@Ignore
public class NameServiceImplTest extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 480786;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private NameService nameService;

	// -- getPlanNamebyId test cases --//
	@Test
	@Rollback(true)
	public void testGetPlanNamebyId() {
		List<Integer> planIds = new ArrayList<Integer>();
		planIds.add(243); // planstate=2
		planIds.add(4); // planstate=0
		planIds.add(3862); // planstate=1
		planIds.add(1); // plan not belong to user
		PlanResult result = nameService.getPlanNamebyId(userId, planIds);
		Map<Integer, PlanInfo> planid2Name = result.getPlanid2Name();
		assertThat(result.getStatus(), is(0));
		assertThat(planid2Name.keySet().size(), is(3));

		Iterator<Map.Entry<Integer, PlanInfo>> iter = planid2Name.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, PlanInfo> entry = iter.next();
			int planId = entry.getKey();
			String planName = entry.getValue().getName();
			int isDel = entry.getValue().getIsDeleted();
			System.out.println(planId);
			System.out.println(planName);
			System.out.println(isDel);
			switch (planId) {
			case 243:
				assertThat(planName, is("test"));
				assertThat(isDel, is(1));
				break;
			case 4:
				assertThat(planName, is("叶子美容整形王慧2"));
				assertThat(isDel, is(0));
				break;
			case 3862:
				assertThat(planName, is("四级培训曹妮娜"));
				assertThat(isDel, is(0));
				break;
			default:
				fail("planId wrong!");
			}
		}
	}

	@Test
	@Rollback(true)
	public void testGetPlanNamebyId_ParamErrorNull() {
		List<Integer> planIds = new ArrayList<Integer>();
		PlanResult result = nameService.getPlanNamebyId(userId, planIds);
		assertThat(result.getStatus(), is(2));
		assertThat(result.getPlanid2Name(), nullValue());
	}

	@Test
	@Rollback(true)
	public void testGetPlanNamebyId_ParamErrorTooMany() {
		List<Integer> planIds = new ArrayList<Integer>();
		for (int i = 0; i < 2000; i++) {
			planIds.add(i);
		}
		PlanResult result = nameService.getPlanNamebyId(userId, planIds);
		assertThat(result.getStatus(), is(2));
		assertThat(result.getPlanid2Name(), nullValue());
	}

	// -- getGroupNamebyId test cases --//
	@Test
	@Rollback(true)
	public void testGetGroupNamebyId() {
		List<Integer> groupIds = new ArrayList<Integer>();
		groupIds.add(7641); // groupstate=2
		groupIds.add(5); // groupstate=0
		groupIds.add(7653); // groupstate=1
		groupIds.add(8); // plan not belong to user
		GroupResult result = nameService.getGroupNamebyId(userId, groupIds);
		Map<Integer, GroupInfo> groupid2Name = result.getGroupid2Name();
		assertThat(result.getStatus(), is(0));
		assertThat(groupid2Name.keySet().size(), is(3));

		Iterator<Map.Entry<Integer, GroupInfo>> iter = groupid2Name.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, GroupInfo> entry = iter.next();
			int groupId = entry.getKey();
			String groupName = entry.getValue().getName();
			int targettype = entry.getValue().getTargettype();
			int isDel = entry.getValue().getIsDeleted();
			System.out.println(groupId);
			System.out.println(targettype);
			System.out.println(groupName);
			System.out.println(isDel);
			switch (groupId) {
			case 7641:
				assertThat(groupName, is("太便宜"));
				assertThat(targettype, is(1));
				assertThat(isDel, is(1));
				break;
			case 5:
				assertThat(groupName, is("整形组"));
				assertThat(targettype, is(3));
				assertThat(isDel, is(0));
				break;
			case 7653:
				assertThat(groupName, is("北京地域"));
				assertThat(targettype, is(2));
				assertThat(isDel, is(0));
				break;
			default:
				fail("groupId wrong!");
			}
		}
	}

	@Test
	@Rollback(true)
	public void testGetGroupNamebyId_ParamErrorNull() {
		List<Integer> groupIds = new ArrayList<Integer>();
		GroupResult result = nameService.getGroupNamebyId(userId, groupIds);
		assertThat(result.getStatus(), is(2));
		assertThat(result.getGroupid2Name(), nullValue());
	}

	@Test
	@Rollback(true)
	public void testGetGroupNamebyId_ParamErrorTooMany() {
		List<Integer> groupIds = new ArrayList<Integer>();
		for (int i = 0; i < 2000; i++) {
			groupIds.add(i);
		}
		GroupResult result = nameService.getGroupNamebyId(userId, groupIds);
		assertThat(result.getStatus(), is(2));
		assertThat(result.getGroupid2Name(), nullValue());
	}

}
