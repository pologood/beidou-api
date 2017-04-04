package com.baidu.beidou.api.external.cprounit2.exporter.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cprogroup.exporter.impl.GroupServiceImpl;
import com.baidu.beidou.api.external.cprogroup.vo.GroupType;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateGroupRequest;
import com.baidu.beidou.api.external.cprounit2.exporter.AdService2;
import com.baidu.beidou.api.external.cprounit2.vo.AdType;
import com.baidu.beidou.api.external.cprounit2.vo.StatusType;
import com.baidu.beidou.api.external.cprounit2.vo.request.AddAdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.CopyAdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.DeleteAdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.GetAdByAdIdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.GetAdByGroupIdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.GetAdIdByGroupIdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.ReplaceAdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.SetAdStatusRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.UpdateAdRequest;
import com.baidu.beidou.api.external.util.ImageUtil;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.SuccessObject;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
@Ignore
public class AdServiceImplTest2 extends AbstractShardAddbTestCaseLegacy {

	private static int userId = 499;

	@Override
	public int getShard() {
		return userId;
	}

	@Resource
	private AdService2 adService2;

	@Test
	public void testAddAdForText() {
		final int groupId = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userMgr, userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		AddAdRequest request = new AddAdRequest();
		AdType[] adTypes = new AdType[1];
		AdType ad = new AdType();
		ad.setTitle("neoremind");
		ad.setDescription1("neo描述1");
		ad.setDescription2("neo描述2");
		ad.setDisplayUrl("winworld.cn");
		ad.setDestinationUrl("http://winworld.cn");
		ad.setAppDisplayUrl("winworld.cn/app");
		ad.setAppDestinationUrl("http://winworld.cn/app");
		ad.setGroupId(groupId);
		ad.setLocalId(111);
		ad.setType(1);
		adTypes[0] = ad;

		request.setAdTypes(adTypes);

		ApiResult<AdType> result = adService2.addAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		AdType adType = (AdType) result.getData().get(0);
		assertThat(adType.getAdId(), greaterThan(0L));
		assertThat(adType.getType(), is(1));
	}

	@Test
	public void testAddAdForImage() {
		final int groupId = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userMgr, userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		AddAdRequest request = new AddAdRequest();
		AdType[] adTypes = new AdType[1];
		AdType ad = new AdType();
		ad.setTitle("neoremind");
		ad.setDisplayUrl("winworld.cn");
		ad.setDestinationUrl("http://winworld.cn");
		ad.setAppDisplayUrl("winworld.cn/app");
		ad.setAppDestinationUrl("http://winworld.cn/app");
		ad.setGroupId(groupId);
		ad.setLocalId(111);
		ad.setType(2);
		ad.setImageData(ImageUtil.GetImageByte(getImageFilePath("250_250.jpg")));
		ad.setWidth(250);
		ad.setHeight(250);
		adTypes[0] = ad;

		request.setAdTypes(adTypes);

		ApiResult<AdType> result = adService2.addAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		AdType adType = (AdType) result.getData().get(0);
		assertThat(adType.getAdId(), greaterThan(0L));
		assertThat(adType.getType(), is(2));

		GetAdByAdIdRequest request2 = new GetAdByAdIdRequest();
		request2.setAdIds(new long[] { adType.getAdId() });

		ApiResult<AdType> result2 = adService2.getAdByAdId(dataUser, request2, apiOption);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));

		// 验证结果
		AdType adType2 = (AdType) result2.getData().get(0);
		assertThat(adType2.getAdId(), is(adType.getAdId()));
		assertThat(adType2.getTitle(), is("neoremind"));
		assertThat(adType2.getHeight(), is(250));
		assertThat(adType2.getStatus(), is(CproUnitConstant.UNIT_STATE_AUDITING));
		assertThat(adType2.getImageUrl(), notNullValue());
	}

	@Test
	public void testAddAdForIcon() {
		final int userId = 499;
		final int groupId = 2288;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userMgr, userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		AddAdRequest request = new AddAdRequest();
		AdType[] adTypes = new AdType[1];
		AdType ad = new AdType();
		ad.setTitle("abekabekabekabek");
		ad.setDescription1("abekabekabekabek");
		ad.setDescription2("abekabekabekabek");
		ad.setDisplayUrl("winworld.cn");
		ad.setDestinationUrl("http://winworld.cn");
		ad.setGroupId(groupId);
		ad.setLocalId(111);
		ad.setType(5);
		ad.setImageData(ImageUtil.GetImageByte(getImageFilePath("60_60.jpg")));
		adTypes[0] = ad;

		request.setAdTypes(adTypes);

		ApiResult<AdType> result = adService2.addAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		AdType adType = (AdType) result.getData().get(0);
		assertThat(adType.getAdId(), greaterThan(0L));
		assertThat(adType.getImageUrl(), notNullValue());
	}

	@Test
	public void testGetAdByAdId() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		GetAdByAdIdRequest request = new GetAdByAdIdRequest();
		request.setAdIds(new long[] { 1589559, 1590640 });

		ApiResult<AdType> result = adService2.getAdByAdId(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));

		// 验证结果
		AdType adType = (AdType) result.getData().get(1);
		assertThat(adType.getAdId(), greaterThan(0L));
	}

	@Test
	public void testGetAdByGroupId() {
		final int userId = 499;
		final int groupId = 244595;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		GetAdByGroupIdRequest request = new GetAdByGroupIdRequest();
		request.setGroupId(groupId);

		ApiResult<AdType> result = adService2.getAdByGroupId(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));

		// 验证结果
		AdType adType = (AdType) result.getData().get(1);
		assertThat(adType.getAdId(), greaterThan(0L));
	}

	@Test
	public void testGetAdIdByGroupId() {
		final int userId = 499;
		final int groupId = 244595;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		GetAdIdByGroupIdRequest request = new GetAdIdByGroupIdRequest();
		request.setGroupId(groupId);

		ApiResult<Long> result = adService2.getAdIdByGroupId(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(2));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));

		// 验证结果
		Long adId = (Long) result.getData().get(1);
		assertThat(adId, greaterThan(0L));
	}

	@Test
	public void testUpdateAdForImage() {
		final int groupId = 245201;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userMgr, userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		AddAdRequest request = new AddAdRequest();
		AdType[] adTypes = new AdType[1];
		AdType ad = new AdType();
		ad.setTitle("neoremind");
		ad.setDisplayUrl("winworld.cn");
		ad.setDestinationUrl("http://winworld.cn");
		ad.setAppDisplayUrl("winworld.cn/app");
		ad.setAppDestinationUrl("http://winworld.cn/app");
		ad.setGroupId(groupId);
		ad.setLocalId(111);
		ad.setType(2);
		ad.setImageData(ImageUtil.GetImageByte(getImageFilePath("250_250.jpg")));
		ad.setWidth(250);
		ad.setHeight(250);
		adTypes[0] = ad;

		request.setAdTypes(adTypes);

		ApiResult<AdType> result = adService2.addAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		UpdateAdRequest request2 = new UpdateAdRequest();
		AdType[] adTypes2 = new AdType[1];
		AdType ad2 = new AdType();
		ad2.setAdId(result.getData().get(0).getAdId());
		ad2.setTitle("abeekabekabek111");
		ad2.setDisplayUrl("winworld.cn");
		ad2.setDestinationUrl("http://winworld.cn");
		ad2.setGroupId(groupId);
		ad2.setLocalId(111);
		ad2.setType(3);

		ad2.setImageData(ImageUtil.GetImageByte(getImageFilePath("250_250.swf")));
		ad2.setHeight(250);
		ad2.setWidth(250);
		adTypes2[0] = ad2;

		request2.setAdTypes(adTypes2);

		ApiResult<AdType> result2 = adService2.updateAd(dataUser, request2, apiOption);

		// 打印返回
		System.out.println(result2);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));

		// 验证结果
		AdType adType2 = (AdType) result2.getData().get(0);
		assertThat(adType2.getAdId(), greaterThan(0L));
	}

	@Test
	public void testDeleteAd() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		DeleteAdRequest request = new DeleteAdRequest();
		request.setAdIds(new long[] { 1590804, 1590805 });

		ApiResult<Object> result = adService2.deleteAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));

		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
	}

	@Test
	public void testSetAdStatus() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		SetAdStatusRequest request = new SetAdStatusRequest();
		StatusType[] statusTypes = new StatusType[1];
		StatusType status = new StatusType();
		status.setAdId(1589573);
		status.setStatus(1);
		statusTypes[0] = status;

		request.setStatusTypes(statusTypes);

		ApiResult<Object> result = adService2.setAdStatus(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
		assertThat(result.getData().get(0), is(SuccessObject.class));
	}

	//@Test
	public void testReplaceAd() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		ReplaceAdRequest request = new ReplaceAdRequest();
		request.setAdIds(new long[] { 1589575, 1589574 });
		request.setAdId(1589576);

		ApiResult<Object> result = adService2.replaceAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), notNullValue());
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
	}

	//@Test
	public void testReplaceAdForIcon() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		ReplaceAdRequest request = new ReplaceAdRequest();
		request.setAdIds(new long[] { 1589575, 1589574 });
		request.setAdId(1589576);

		ApiResult<Object> result = adService2.replaceAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), notNullValue());
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getData(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(1));

		// 验证结果
	}

	//@Test
	public void testCopyAd() {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		CopyAdRequest request = new CopyAdRequest();
		request.setGroupIds(new long[] { 245293, 245294 });
		request.setAdIds(new long[] { 1590809, 1590793 });

		ApiResult<Object> result = adService2.copyAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData(), notNullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));

		// 验证结果
		//		assertThat(result.getData().get(0), is(SuccessObject.class));
	}

	//@Test
	public void testCopyAdForGroupType() {
		this.prepareTestCopyAdForGroupType();
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();

		CopyAdRequest request = new CopyAdRequest();
		request.setGroupIds(new long[] { 245293, 245294 });
		request.setAdIds(new long[] { 1590809, 1590793 });

		ApiResult<Object> result = adService2.copyAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getPayment().getTotal(), is(2));
		assertThat(result.getPayment().getSuccess(), is(2));

		// 验证结果
		//		assertThat(result.getData().get(0), is(SuccessObject.class));
	}

	private void prepareTestCopyAdForGroupType() {
		String beanName = "groupService";
		GroupServiceImpl groupService = (GroupServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));

		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);

		UpdateGroupRequest request = new UpdateGroupRequest();
		GroupType[] groupTypes = new GroupType[2];
		GroupType group = new GroupType();
		group.setCampaignId(105041);
		group.setGroupId(245293);
		group.setGroupName("fixed-flow");
		group.setPrice(100);
		group.setStatus(1);
		group.setType(3);
		groupTypes[0] = group;

		group = new GroupType();
		group.setCampaignId(105041);
		group.setGroupId(245294);
		group.setGroupName("fixed-film");
		group.setPrice(100);
		group.setStatus(2);
		group.setType(5);
		groupTypes[1] = group;
		request.setGroupTypes(groupTypes);

		ApiResult<GroupType> result = groupService.updateGroup(dataUser, request, apiOption);

		System.out.println(result);

	}

	public static String getImageFilePath(String fileName) {
		return new File("").getAbsolutePath() + "\\src\\test\\java\\com\\baidu\\beidou\\api\\external\\images\\" + fileName;
	}
}
