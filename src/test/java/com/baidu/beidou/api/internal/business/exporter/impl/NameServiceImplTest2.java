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

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.baidu.beidou.api.internal.business.exporter.NameService;
import com.baidu.beidou.api.internal.business.vo.KtEnabledResult;
import com.baidu.beidou.api.internal.business.vo.SiteResult;
import com.baidu.beidou.api.internal.business.vo.UnitInfo;
import com.baidu.beidou.api.internal.business.vo.UnitResult;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;

public class NameServiceImplTest2 extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 480787;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private NameService nameService;

	// -- getUnitMaterialbyId test cases --//
	@Test
	@Rollback(true)
	public void testGetUnitMaterialbyId() {
		List<Long> unitIds = new ArrayList<Long>();
		unitIds.add(1l); // state=0, 有效
		unitIds.add(4l); // state=1, 暂停
		unitIds.add(2l); // state=2, 删除
		unitIds.add(179519l); // state=3, 审核组
		unitIds.add(28721l); // state=4, 审核拒绝
		unitIds.add(1590257l); // 图文混排
		unitIds.add(20500l); // plan not belong to user
		UnitResult result = nameService.getUnitMaterialbyId(userId, unitIds);
		Map<Long, UnitInfo> unitid2Name = result.getUnitid2Name();
		assertThat(result.getStatus(), is(0));
		assertThat(unitid2Name.keySet().size(), is(0));

		Iterator<Map.Entry<Long, UnitInfo>> iter = unitid2Name.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Long, UnitInfo> entry = iter.next();
			long unitId = entry.getKey();
			String title = entry.getValue().getTitle();
			int type = entry.getValue().getType();
			String desc1 = entry.getValue().getDesc1();
			String desc2 = entry.getValue().getDesc2();
			int isDel = entry.getValue().getIsDeleted();
			System.out.println(unitId);
			System.out.println(type);
			System.out.println(desc1);
			System.out.println(desc2);
			System.out.println(isDel);
			switch (Integer.valueOf(Long.toString(unitId))) {
			case 1:
				assertThat(title, is("北大青鸟培训 赠英语课程!!"));
				assertThat(type, is(1));
				assertThat(desc1, is("金牌讲师团队,先进教学理念,"));
				assertThat(desc2, is("北大青鸟it培训"));
				assertThat(isDel, is(0));
				break;
			case 4:
				assertThat(title, is("大中专生IT就业培训专区!!"));
				assertThat(type, is(1));
				assertThat(desc1, is("找工作?跳槽?转行?"));
				assertThat(desc2, is("来北大青鸟参加职业IT培训"));
				assertThat(isDel, is(0));
				break;
			case 2:
				assertThat(title, is("精锐教育"));
				assertThat(type, is(2));
				assertThat(desc1, nullValue());
				assertThat(desc2, nullValue());
				assertThat(isDel, is(1));
				break;
			case 179519:
				assertThat(title, is("北大青鸟培训 赠英语课程!!!"));
				assertThat(type, is(1));
				assertThat(desc1, is("金牌讲师团队,先进教学理念,"));
				assertThat(desc2, is("北大青鸟it培训"));
				assertThat(isDel, is(0));
				break;
			case 28721:
				assertThat(title, is("oujsfghhh"));
				assertThat(type, is(1));
				assertThat(desc1, is("23577fhh"));
				assertThat(desc2, is("3567dfhj"));
				assertThat(isDel, is(0));
				break;
			case 1590257:
				assertThat(title, is("MyTitle111111111111111"));
				assertThat(type, is(5));
				break;
			default:
				fail("unitId wrong!");
			}
		}
	}

	@Test
	@Rollback(true)
	public void testGetUnitMaterialbyId_ParamErrorNull() {
		List<Long> unitIds = new ArrayList<Long>();
		UnitResult result = nameService.getUnitMaterialbyId(userId, unitIds);
		assertThat(result.getStatus(), is(2));
		assertThat(result.getUnitid2Name(), nullValue());
	}

	@Test
	@Rollback(true)
	public void testGetUnitMaterialbyId_ParamErrorTooMany() {
		List<Long> unitIds = new ArrayList<Long>();
		for (long i = 0; i < 2000; i++) {
			unitIds.add(i);
		}
		UnitResult result = nameService.getUnitMaterialbyId(userId, unitIds);
		assertThat(result.getStatus(), is(2));
		assertThat(result.getUnitid2Name(), nullValue());
	}

	// -- getSiteNamebyKey test cases --//
	@Test
	@Rollback(true)
	public void testGetSiteNamebyKey() {
		List<String> keys = new ArrayList<String>();
		keys.add("4049147698973926190"); // av18.cn
		keys.add("1910023754027077715"); // alexphor.6621.cn
		keys.add("2243892728934257477"); // rocmc.freebbs.tw
		keys.add("111111"); // not exist
		SiteResult result = nameService.getSiteNamebyKey(keys);
		Map<String, String> siteKey2Url = result.getSiteKey2Url();
		assertThat(result.getStatus(), is(0));
		assertThat(siteKey2Url.keySet().size(), is(0));

		Iterator<Map.Entry<String, String>> iter = siteKey2Url.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String url = entry.getValue();
			System.out.println(key);
			System.out.println(url);
			if (key.equals("4049147698973926190")) {
				assertThat(url, is("av18.cn"));
			} else if (key.equals("1910023754027077715")) {
				assertThat(url, is("alexphor.6621.cn"));
			} else if (key.equals("2243892728934257477")) {
				assertThat(url, is("rocmc.freebbs.tw"));
			} else {
				fail("key wrong!");
			}
		}
	}

	@Test
	@Rollback(true)
	public void testGetSiteNamebyKey_ParamErrorNull() {
		List<String> keys = new ArrayList<String>();
		SiteResult result = nameService.getSiteNamebyKey(keys);
		assertThat(result.getStatus(), is(2));
		assertThat(result.getSiteKey2Url(), nullValue());
	}

	@Test
	@Rollback(true)
	public void testGetSiteNamebyKey_ParamErrorTooMany() {
		List<String> keys = new ArrayList<String>();
		for (int i = 0; i < 2000; i++) {
			keys.add(Integer.toString(i));
		}
		SiteResult result = nameService.getSiteNamebyKey(keys);
		assertThat(result.getStatus(), is(2));
		assertThat(result.getSiteKey2Url(), nullValue());
	}

	// -- isKtEnabledByUserid test cases --//
	@Test
	@Rollback(true)
	public void testIsKtEnabledByUserid() {
		KtEnabledResult result1 = nameService.isKtEnabledByUserid(userId);
		assertThat(result1.getStatus(), is(0));
		assertThat(result1.getIsEnabled(), is(0));
	}

	// -- isKtEnabledByUseridAndPlanid test cases --//
	@Test
	@Rollback(true)
	public void testIsKtEnabledByUseridAndPlanid() {
		int planid1 = 1;
		KtEnabledResult result1 = nameService.isKtEnabledByUseridAndPlanid(userId, planid1);
		assertThat(result1.getStatus(), is(0));
		assertThat(result1.getIsEnabled(), is(0));
	}

	// -- isKtEnabledByUseridAndGroupid test cases --//
	@Test
	@Rollback(true)
	public void testIsKtEnabledByUseridAndGroupid() {
		int groupid1 = 1;
		KtEnabledResult result1 = nameService.isKtEnabledByUseridAndGroupid(userId, groupid1);
		assertThat(result1.getStatus(), is(0));
		assertThat(result1.getIsEnabled(), is(0));
	}

}
