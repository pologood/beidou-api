package com.baidu.beidou.api.internal.business.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.api.internal.business.exporter.NameService;
import com.baidu.beidou.api.internal.business.vo.KeywordInfo;
import com.baidu.beidou.api.internal.business.vo.KeywordResult;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;

public class NameServiceImplTest4 extends AbstractShardAddbTestCaseLegacy {

    private static int userId = 8;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private NameService nameService;

	@Test
	public void testGetKeywordLitera2() {
		int groupId = 245324;
		List<Long> wordIdList = new ArrayList<Long>();
		wordIdList.add(324773658l);
		wordIdList.add(857624l);
		wordIdList.add(324775204l);
		wordIdList.add(324777157l); //del

		KeywordResult result = nameService.getKeywordLiteral2(userId, groupId, wordIdList);
		assertThat(result.getStatus(), is(0));
		Map<Long, KeywordInfo> map = result.getKeywordid2Name();
		assertThat(map.size(), is(4));
		Set<Long> set = map.keySet();
		for (Long keywordid : set) {
			System.out.println("keywordid=" + keywordid);
			System.out.println("literal=" + map.get(keywordid).getLiteral());
			System.out.println("isDel=" + (map.get(keywordid).getIsDeleted() == 1 ? true : false));
			System.out.println("----");
		}

		assertThat(map.get(324775204l).getLiteral(), is("坑梓空压机热交换器"));
		assertThat(map.get(324775204l).getIsDeleted(), is(1));
		assertThat(map.get(857624l).getLiteral(), is("鲜花种类"));
		assertThat(map.get(857624L).getIsDeleted(), is(1));
	}

}
