package com.baidu.beidou.api.external.cprounit.exporter.rpc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.cprounit.exporter.AdService;
import com.baidu.beidou.api.external.cprounit.vo.AdType;
import com.baidu.beidou.api.external.cprounit.vo.request.AddAdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.GetAdByAdIdRequest;
import com.baidu.beidou.api.external.cprounit.vo.request.UpdateAdRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.ImageUtil;
import com.baidu.beidou.api.external.util.vo.ApiResult;
@Ignore
public class AdServiceTest extends ApiBaseRPCTest<AdService> {
	
	@Test
	public void testAddAd4Image() throws Exception {
		AdService exporter = getServiceProxy(AdService.class, ApiExternalConstant.AD_SERVICE_URL);

		AddAdRequest request = new AddAdRequest();
		AdType[] adTypes = new AdType[1];
		AdType ad = new AdType();
		ad.setAdId(228);
		ad.setTitle("abeekabekabek111");
		ad.setDisplayUrl("baidu.com");
		ad.setDestinationUrl("http://baidu.com");
		ad.setGroupId(6260514);
		ad.setLocalId(111);
		ad.setType(3);

		ad.setImageData(ImageUtil.GetImageByte(getImageFilePath("drmc-flash.swf")));
		ad.setHeight(728);
		ad.setWidth(90);
		adTypes[0] = ad;

		request.setAdTypes(adTypes);

		dataUser.setDataUser(8);
		dataUser.setOpUser(8);
		ApiResult<AdType> result = exporter.addAd(dataUser, request, apiOption);

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
		
	}
	
	@Test
	public void testUpdateAd4Image() throws Exception  {
		AdService exporter = getServiceProxy(AdService.class, ApiExternalConstant.AD_SERVICE_URL);
		
		UpdateAdRequest request = new UpdateAdRequest();
		AdType[] adTypes = new AdType[1];
		AdType ad = new AdType();
		ad.setAdId(76093216L);
		ad.setTitle("728*90");
		ad.setDisplayUrl("baidu.com");
		ad.setDestinationUrl("http://baidu.com");
		ad.setGroupId(6260514);
		ad.setLocalId(111);
		ad.setType(3);
		ad.setImageData(ImageUtil.GetImageByte(getImageFilePath("drmc-flash.swf")));
		ad.setWidth(728);
		ad.setHeight(90);
		adTypes[0] = ad;

		request.setAdTypes(adTypes);

		dataUser.setDataUser(8);
		dataUser.setOpUser(8);
		ApiResult<AdType> result = exporter.updateAd(dataUser, request, apiOption);

		// 打印返回
		System.out.println(result);

		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
	}
	
	@Test
	public void testGetAdByAdId() throws Exception {
		
		AdService exporter = getServiceProxy(AdService.class, ApiExternalConstant.AD_SERVICE_URL);
		
		GetAdByAdIdRequest request = new GetAdByAdIdRequest();
		request.setAdIds(new long[]{ 76603756L });
		
		dataUser.setDataUser(2233117);
		dataUser.setOpUser(2233117);
		ApiResult<AdType> result = exporter.getAdByAdId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
	}
	
}
