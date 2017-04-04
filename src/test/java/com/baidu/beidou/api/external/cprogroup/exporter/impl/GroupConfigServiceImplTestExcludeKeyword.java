package com.baidu.beidou.api.external.cprogroup.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cprogroup.error.GroupConfigErrorCode;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeKeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeKeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeKeywordRequest;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.test.common.BasicTestCaseLegacy;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 排除关键词相关测试
 * 
 * @author work
 * 
 */
@Ignore
public class GroupConfigServiceImplTestExcludeKeyword extends BasicTestCaseLegacy {

    @Resource
    private GroupConfigService groupConfigService;

	@Test
	public void testGetExcludeKeyword() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		GetExcludeKeywordRequest request = new GetExcludeKeywordRequest();
		request.setGroupIds(new long[] { 228l, 6620l });

		BaseResponse<ExcludeKeywordType> result = groupConfigService.getExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(0));

		assertThat(((ExcludeKeywordType) result.getData()[0]).getGroupId(), is(6620l));
		assertThat(((ExcludeKeywordType) result.getData()[0]).getExcludeKeywords().size(), is(3));
		assertThat(((ExcludeKeywordType) result.getData()[0]).getExcludeKeywords().get(1).getKeyword(), is("洗衣机"));
		assertThat(((ExcludeKeywordType) result.getData()[0]).getExcludeKeywords().get(1).getPattern(), is(0));
		assertThat(((ExcludeKeywordType) result.getData()[0]).getExcludeKeywordPackIds().size(), is(2));
		assertThat(((ExcludeKeywordType) result.getData()[0]).getExcludeKeywordPackIds().get(0), is(1));

		assertThat(result.getOptions().getSuccess(), is(2));
		assertThat(result.getOptions().getTotal(), is(2));

	}

	@Test
	public void testGetExcludeKeyword_NotConfigExcludeKeyword() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		GetExcludeKeywordRequest request = new GetExcludeKeywordRequest();
		request.setGroupIds(new long[] { 244696l });

		BaseResponse<ExcludeKeywordType> result = groupConfigService.getExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(0));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_OnlyKeyword() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244538l);
		List<KeywordType> keywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("总监");
		kt1.setPattern(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(1);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("董事长");
		kt3.setPattern(0);
		keywords.add(kt1);
		keywords.add(kt2);
		keywords.add(kt3);
		type.setExcludeKeywords(keywords);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244538l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(244538l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().size(), is(3));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().get(1).getKeyword(), is("总监"));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().get(1).getPattern(), is(0));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_OnlyKeyword_CannotAddDuplicate() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244538l);
		List<KeywordType> keywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("总监");
		kt1.setPattern(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(1);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("董事长");
		kt3.setPattern(0);
		keywords.add(kt1);
		keywords.add(kt2);
		keywords.add(kt3);
		type.setExcludeKeywords(keywords);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244538l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(244538l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().size(), is(3));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().get(1).getKeyword(), is("总监"));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().get(1).getPattern(), is(0));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_OnlyKeyword_Update() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244538l);
		List<KeywordType> keywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("总监");
		kt1.setPattern(1);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("董事长123");
		kt3.setPattern(0);
		keywords.add(kt1);
		keywords.add(kt2);
		keywords.add(kt3);
		type.setExcludeKeywords(keywords);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

		sleep(2);
		
		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244538l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(244538l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().size(), is(3));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().get(1).getKeyword(), is("总监"));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().get(1).getPattern(), is(0));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().get(2).getKeyword(), is("董事长123"));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_OnlyKeyword_InvalidKeyword() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244538l);
		List<KeywordType> keywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("");
		kt1.setPattern(1);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("董事长123");
		kt3.setPattern(0);
		keywords.add(kt1);
		keywords.add(kt2);
		keywords.add(kt3);
		type.setExcludeKeywords(keywords);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());

		assertThat((result.getErrors().get(0)).getCode(), is(GroupConfigErrorCode.EXCLUDE_WORDS_ERROR.getValue()));

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_OnlyKeywordPack() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244541l);
		List<Integer> keywordPackIds = new ArrayList<Integer>();
		keywordPackIds.add(1);
		keywordPackIds.add(5);
		type.setExcludeKeywordPackIds(keywordPackIds);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244541l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(244541l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords(), nullValue());
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds().size(), is(2));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds().get(0), is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_OnlyKeywordPack_CannotAddDuplicate() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244541l);
		List<Integer> keywordPackIds = new ArrayList<Integer>();
		keywordPackIds.add(1);
		keywordPackIds.add(5);
		type.setExcludeKeywordPackIds(keywordPackIds);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244541l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(244541l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords(), nullValue());
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds().size(), is(2));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds().get(0), is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_OnlyKeywordPack_Update() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244541l);
		List<Integer> keywordPackIds = new ArrayList<Integer>();
		keywordPackIds.add(1);
		keywordPackIds.add(2);
		type.setExcludeKeywordPackIds(keywordPackIds);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244541l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(244541l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords(), nullValue());
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds().size(), is(2));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds().get(1), is(2));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_OnlyKeywordPack_NotExistPackId() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244541l);
		List<Integer> keywordPackIds = new ArrayList<Integer>();
		keywordPackIds.add(11111111);
		type.setExcludeKeywordPackIds(keywordPackIds);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(1));
	}

	@Test
	public void testSetExcludeKeyword_OnlyKeywordPack_NotHasQTPack() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244541l);
		List<Integer> keywordPackIds = new ArrayList<Integer>();
		keywordPackIds.add(3);
		type.setExcludeKeywordPackIds(keywordPackIds);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(1));
	}

	@Test
	public void testSetExcludeKeyword_NoGroupId() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		List<KeywordType> keywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("总监");
		kt1.setPattern(1);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(1);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("董事长");
		kt3.setPattern(1);
		keywords.add(kt1);
		keywords.add(kt2);
		keywords.add(kt3);
		type.setExcludeKeywords(keywords);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_NoKeywordsOrKeywordPacks() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(228l);
		List<KeywordType> keywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("总监");
		kt1.setPattern(1);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(1);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("董事长");
		kt3.setPattern(1);
		keywords.add(kt1);
		keywords.add(kt2);
		keywords.add(kt3);
		//type.setExcludeKeywords(keywords);
		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testSetExcludeKeyword_BothKeywordAndPack() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(244540l);
		List<KeywordType> keywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("总监");
		kt1.setPattern(1);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("董事长");
		kt3.setPattern(0);
		keywords.add(kt1);
		keywords.add(kt2);
		keywords.add(kt3);
		type.setExcludeKeywords(keywords);

		List<Integer> keywordPackIds = new ArrayList<Integer>();
		keywordPackIds.add(1);
		type.setExcludeKeywordPackIds(keywordPackIds);
		request.setExcludeKeyword(type);

		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

		sleep(2);
		
		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244540l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(244540l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().size(), is(3));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds().size(), is(1));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds().get(0), is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

	}

	@Test
	public void testAddExcludeKeyword_BothKeywordAndPack() {
		// 准备数据
		testSetExcludeKeyword_BothKeywordAndPack();
		setExcludeKeyword_BothKeywordAndPack();

		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		AddExcludeKeywordRequest request = new AddExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();

		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(244540l);
		type1.setType(0);
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("IBM");
		kt1.setPattern(1);
		type1.setExcludeKeyword(kt1);

		GroupExcludeKeywordType type2 = new GroupExcludeKeywordType();
		type2.setGroupId(244540l);
		type2.setType(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("HP");
		kt2.setPattern(1);
		type2.setExcludeKeyword(kt2);

		GroupExcludeKeywordType type3 = new GroupExcludeKeywordType();
		type3.setGroupId(243168l);
		type3.setType(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("菠萝");
		kt3.setPattern(0);
		type3.setExcludeKeyword(kt3);

		GroupExcludeKeywordType type4 = new GroupExcludeKeywordType();
		type4.setGroupId(244540l);
		type4.setType(1);
		type4.setExcludeKeywordPackId(2);

		GroupExcludeKeywordType type5 = new GroupExcludeKeywordType();
		type5.setGroupId(243168l);
		type5.setType(1);
		type5.setExcludeKeywordPackId(5);

		excludeKeywords.add(type1);
		excludeKeywords.add(type2);
		excludeKeywords.add(type3);
		excludeKeywords.add(type4);
		excludeKeywords.add(type5);
		request.setExcludeKeywords(excludeKeywords);

		BaseResponse<PlaceHolderResult> result = groupConfigService.addExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(5));
		assertThat(result.getOptions().getTotal(), is(5));

		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244540l, 243168l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().length, is(2));

		assertThat(((ExcludeKeywordType) result2.getData()[1]).getGroupId(), is(244540l));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywords().size(), is(5));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywords().get(3).getKeyword(), is("HP"));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywordPackIds().size(), is(2));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(243168l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().size(), is(3));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().get(2).getKeyword(), is("菠萝"));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds().get(0), is(5));

		assertThat(result2.getOptions().getSuccess(), is(2));
		assertThat(result2.getOptions().getTotal(), is(2));

	}

	@Test
	public void testAddExcludeKeyword_Update() {
		// 准备数据
		testSetExcludeKeyword_BothKeywordAndPack();
		setExcludeKeyword_BothKeywordAndPack();

		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		AddExcludeKeywordRequest request = new AddExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();

		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(244540l);
		type1.setType(0);
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("董事长");
		kt1.setPattern(1);
		type1.setExcludeKeyword(kt1);

		GroupExcludeKeywordType type4 = new GroupExcludeKeywordType();
		type4.setGroupId(243168l);
		type4.setType(1);
		type4.setExcludeKeywordPackId(2);

		excludeKeywords.add(type1);
		excludeKeywords.add(type4);
		request.setExcludeKeywords(excludeKeywords);

		BaseResponse<PlaceHolderResult> result = groupConfigService.addExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(2));
		assertThat(result.getOptions().getTotal(), is(2));

		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244540l, 243168l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().length, is(2));

		assertThat(((ExcludeKeywordType) result2.getData()[1]).getGroupId(), is(244540l));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywords().size(), is(3));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywordPackIds().get(0), is(1));

		assertThat(result2.getOptions().getSuccess(), is(2));
		assertThat(result2.getOptions().getTotal(), is(2));

	}

	@Test
	public void testAddExcludeKeyword_Negative() {
		// 准备数据
		testSetExcludeKeyword_BothKeywordAndPack();
		setExcludeKeyword_BothKeywordAndPack();

		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		AddExcludeKeywordRequest request = new AddExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();

		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(244540l);
		type1.setType(0);
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("");
		kt1.setPattern(1111);
		type1.setExcludeKeyword(kt1);

		GroupExcludeKeywordType type4 = new GroupExcludeKeywordType();
		type4.setGroupId(243168l);
		type4.setType(1);
		type4.setExcludeKeywordPackId(999);

		excludeKeywords.add(type1);
		excludeKeywords.add(type4);

		request.setExcludeKeywords(excludeKeywords);

		BaseResponse<PlaceHolderResult> result = groupConfigService.addExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(2));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(0));
		assertThat(result.getOptions().getTotal(), is(2));

	}

	@Test
	public void testDeleteExcludeKeyword_OnlyKeyword() {
		// 准备数据
		testSetExcludeKeyword_BothKeywordAndPack();
		setExcludeKeyword_BothKeywordAndPack();

		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		DeleteExcludeKeywordRequest request = new DeleteExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();

		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(244540l);
		type1.setType(0);
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("董事长");
		kt1.setPattern(1);
		type1.setExcludeKeyword(kt1);

		GroupExcludeKeywordType type2 = new GroupExcludeKeywordType();
		type2.setGroupId(244540l);
		type2.setType(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(1);
		type2.setExcludeKeyword(kt2);

		GroupExcludeKeywordType type3 = new GroupExcludeKeywordType();
		type3.setGroupId(243168l);
		type3.setType(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("苹果");
		kt3.setPattern(1);
		type3.setExcludeKeyword(kt3);

		excludeKeywords.add(type1);
		excludeKeywords.add(type2);
		excludeKeywords.add(type3);
		request.setExcludeKeywords(excludeKeywords);

		BaseResponse<PlaceHolderResult> result = groupConfigService.deleteExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(3));
		assertThat(result.getOptions().getTotal(), is(3));

		sleep(2);
		
		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244540l, 243168l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().length, is(2));

		assertThat(((ExcludeKeywordType) result2.getData()[1]).getGroupId(), is(244540l));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywords().size(), is(1));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywords().get(0).getKeyword(), is("总监"));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywordPackIds().size(), is(1));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(243168l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().size(), is(1));

		assertThat(result2.getOptions().getSuccess(), is(2));
		assertThat(result2.getOptions().getTotal(), is(2));

	}

	@Test
	public void testDeleteExcludeKeyword_OnlyKeywordPack() {
		// 准备数据
		testSetExcludeKeyword_BothKeywordAndPack();
		setExcludeKeyword_BothKeywordAndPack();

		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		DeleteExcludeKeywordRequest request = new DeleteExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();

		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(244540l);
		type1.setType(1);
		type1.setExcludeKeywordPackId(1);

		excludeKeywords.add(type1);
		request.setExcludeKeywords(excludeKeywords);

		BaseResponse<PlaceHolderResult> result = groupConfigService.deleteExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244540l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().length, is(1));

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(244540l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().size(), is(3));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywordPackIds(), nullValue());

		assertThat(result2.getOptions().getSuccess(), is(1));
		assertThat(result2.getOptions().getTotal(), is(1));

	}

	@Test
	public void testDeleteExcludeKeyword_Negative() {
		// 准备数据
		testSetExcludeKeyword_BothKeywordAndPack();
		setExcludeKeyword_BothKeywordAndPack();

		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		DeleteExcludeKeywordRequest request = new DeleteExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();

		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(244540l);
		type1.setType(0);
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("");
		kt1.setPattern(1);
		type1.setExcludeKeyword(kt1);

		GroupExcludeKeywordType type2 = new GroupExcludeKeywordType();
		type2.setGroupId(244540l);
		type2.setType(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(11111);
		type2.setExcludeKeyword(kt2);

		GroupExcludeKeywordType type3 = new GroupExcludeKeywordType();
		type3.setGroupId(243168l);
		type3.setType(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("苹果");
		kt3.setPattern(1);
		type3.setExcludeKeyword(kt3);

		GroupExcludeKeywordType type4 = new GroupExcludeKeywordType();
		type4.setGroupId(244540l);
		type4.setType(1);
		type4.setExcludeKeywordPackId(1);

		GroupExcludeKeywordType type5 = new GroupExcludeKeywordType();
		type5.setGroupId(243168l);
		type5.setType(1);
		type5.setExcludeKeywordPackId(1);

		excludeKeywords.add(type1);
		excludeKeywords.add(type2);
		excludeKeywords.add(type3);
		excludeKeywords.add(type4);
		excludeKeywords.add(type5);
		request.setExcludeKeywords(excludeKeywords);

		BaseResponse<PlaceHolderResult> result = groupConfigService.deleteExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors().size(), is(2));
		assertThat(result.getData(), nullValue());

		assertThat(result.getOptions().getSuccess(), is(3));
		assertThat(result.getOptions().getTotal(), is(5));

		// 验证结果
		GetExcludeKeywordRequest request2 = new GetExcludeKeywordRequest();
		request2.setGroupIds(new long[] { 244540l, 243168l });

		BaseResponse<ExcludeKeywordType> result2 = groupConfigService.getExcludeKeyword(user, request2, options);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().length, is(2));

		assertThat(((ExcludeKeywordType) result2.getData()[1]).getGroupId(), is(244540l));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywords().size(), is(2));
		assertThat(((ExcludeKeywordType) result2.getData()[1]).getExcludeKeywordPackIds(), nullValue());

		assertThat(((ExcludeKeywordType) result2.getData()[0]).getGroupId(), is(243168l));
		assertThat(((ExcludeKeywordType) result2.getData()[0]).getExcludeKeywords().size(), is(1));

		assertThat(result2.getOptions().getSuccess(), is(2));
		assertThat(result2.getOptions().getTotal(), is(2));

	}

	// 准备数据
	public void setExcludeKeyword_BothKeywordAndPack() {
		final int userId = 8;

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType type = new ExcludeKeywordType();
		type.setGroupId(243168l);
		List<KeywordType> keywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("苹果");
		kt1.setPattern(1);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("香蕉");
		kt2.setPattern(0);
		keywords.add(kt1);
		keywords.add(kt2);
		type.setExcludeKeywords(keywords);

		request.setExcludeKeyword(type);

		BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludeKeyword(user, request, options);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().length, is(1));

		assertThat(result.getOptions().getSuccess(), is(1));
		assertThat(result.getOptions().getTotal(), is(1));

	}
}
