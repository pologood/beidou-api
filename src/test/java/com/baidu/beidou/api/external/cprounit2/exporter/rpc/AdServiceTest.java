package com.baidu.beidou.api.external.cprounit2.exporter.rpc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.cprounit2.vo.request.GetAdByAdIdRequest;
import com.baidu.beidou.api.external.cprounit2.exporter.AdService2;
import com.baidu.beidou.api.external.cprounit2.vo.AdType;
import com.baidu.beidou.api.external.cprounit2.vo.StatusType;
import com.baidu.beidou.api.external.cprounit2.vo.request.AddAdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.CopyAdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.DeleteAdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.ReplaceAdRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.SetAdStatusRequest;
import com.baidu.beidou.api.external.cprounit2.vo.request.UpdateAdRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.ImageUtil;
import com.baidu.beidou.api.external.util.vo.ApiResult;

@Ignore
public class AdServiceTest extends ApiBaseRPCTest<AdService2> {

    @Test
    public void testAddAd4Image() throws Exception {
        AdService2 exporter = getServiceProxy(AdService2.class, ApiExternalConstant.AD_SERVICE2_URL);

        AddAdRequest request = new AddAdRequest();

        AdType[] adTypes = new AdType[3];

        AdType ad = new AdType();
        ad.setAdId(228);
        ad.setTitle("abeekabekabek111");
        ad.setDescription1("abekabbekabek111");
        ad.setDescription2("abekabeekabek111");
        ad.setDisplayUrl("baidu.com");
        ad.setDestinationUrl("http://baidu.com");
        ad.setGroupId(7245789);
        ad.setLocalId(111);
        ad.setType(2);
        ad.setImageData(ImageUtil.GetImageByte(getImageFilePath("250_250.jpg")));
        ad.setHeight(250);
        ad.setWidth(250);
        adTypes[0] = ad;

        AdType ad2 = new AdType();
        ad2.setAdId(228);
        ad2.setTitle("abeekabekabek111");
        ad2.setDescription1("abekabbekabek111");
        ad2.setDescription2("abekabeekabek111");
        ad2.setDisplayUrl("baidu.com");
        ad2.setDestinationUrl("http://baidu.com");
        ad2.setGroupId(7245764);
        ad2.setLocalId(112);
        ad2.setType(2);
        ad2.setImageData(ImageUtil.GetImageByte(getImageFilePath("250_250.jpg")));
        ad2.setHeight(250);
        ad2.setWidth(250);
        adTypes[1] = ad2;

        AdType ad3 = new AdType();
        ad3.setAdId(228);
        ad3.setTitle("abeekabekabek111");
        ad3.setDescription1("abekabbekabek111");
        ad3.setDescription2("abekabeekabek111");
        ad3.setDisplayUrl("baidu.com");
        ad3.setDestinationUrl("http://baidu.com");
        ad3.setGroupId(7245764);
        ad3.setLocalId(112);
        ad3.setType(2);
        ad3.setImageData(ImageUtil.GetImageByte(getImageFilePath("250_250.jpg")));
        ad3.setHeight(250);
        ad3.setWidth(250);
        adTypes[2] = ad3;

        request.setAdTypes(adTypes);

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
    public void testAddAd4AdmakerFlash() throws Exception {
        AdService2 exporter = getServiceProxy(AdService2.class, ApiExternalConstant.AD_SERVICE2_URL);

        AddAdRequest request = new AddAdRequest();
        AdType[] adTypes = new AdType[1];
        AdType ad = new AdType();
        ad.setAdId(228);
        ad.setTitle("ubmc-admaker11111");
        // ad.setDescription1("ubmc-admaker11111");
        // ad.setDescription2("ubmc-admaker11111");
        ad.setDisplayUrl("baidu.com");
        ad.setDestinationUrl("http://baidu.com");
        ad.setGroupId(6774582);
        ad.setLocalId(111);
        ad.setType(3);

        ad.setImageData(ImageUtil.GetImageByte(getImageFilePath("admaker_580_90.swf")));
        ad.setHeight(580);
        ad.setWidth(90);
        adTypes[0] = ad;

        request.setAdTypes(adTypes);

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
    public void testUpdateAd4AdmakerFlash() throws Exception {
        AdService2 exporter = getServiceProxy(AdService2.class, ApiExternalConstant.AD_SERVICE2_URL);

        UpdateAdRequest request = new UpdateAdRequest();
        AdType[] adTypes = new AdType[1];
        AdType ad = new AdType();
        ad.setAdId(93834044);
        ad.setTitle("ubmc-admaker1111");
        ad.setDescription1("ubmc-admaker1111");
        ad.setDescription2("ubmc-admaker1111");
        ad.setDisplayUrl("baidu.com");
        ad.setDestinationUrl("http://baidu.com");
        ad.setGroupId(7245789);
        ad.setLocalId(111);
        ad.setType(1);

        // ad.setImageData(ImageUtil.GetImageByte(getImageFilePath("admaker_580_90.swf")));
        // ad.setHeight(580);
        // ad.setWidth(90);
        adTypes[0] = ad;

        request.setAdTypes(adTypes);

        ApiResult<AdType> result = exporter.updateAd(dataUser, request, apiOption);

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
    public void testUpdateAdOnlyWirelessUrl() throws Exception {
        AdService2 exporter = getServiceProxy(AdService2.class, ApiExternalConstant.AD_SERVICE2_URL);

        UpdateAdRequest request = new UpdateAdRequest();
        AdType[] adTypes = new AdType[1];
        AdType ad = new AdType();
        ad.setAdId(82606241);
        ad.setTitle("text:sjfljdslkjflksd");
        ad.setDescription1("text:sdlfkjsdlkjflkdsjf");
        ad.setDescription2("text:sdlfjslkdjflksdjfkl");
        ad.setDisplayUrl("wenhua.sh.cn");
        ad.setDestinationUrl("http://wenhua.sh.cn");
        ad.setAppDisplayUrl("wenhua.sh.cn");
        // ad.setAppDestinationUrl("http://siteapp.baidu.com/app/union/?src=wenhua.sh.cn");
        ad.setAppDestinationUrl("http://wenhua.sh.cn/");
        ad.setGroupId(6713369);
        ad.setLocalId(82606241);
        ad.setType(5);

        // ad.setImageData(ImageUtil.GetImageByte(getImageFilePath("admaker_120_270.swf")));
        // ad.setHeight(270);
        // ad.setWidth(120);
        adTypes[0] = ad;

        request.setAdTypes(adTypes);

        dataUser.setDataUser(499);
        dataUser.setOpUser(499);

        ApiResult<AdType> result = exporter.updateAd(dataUser, request, apiOption);

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
    public void testCopy() throws Exception {
        AdService2 exporter = getServiceProxy(AdService2.class, ApiExternalConstant.AD_SERVICE2_URL);
        CopyAdRequest request = new CopyAdRequest();

        request.setGroupIds(new long[] { 6774350 });
        request.setAdIds(new long[] { 93834044 });

        exporter.copyAd(dataUser, request, apiOption);
    }

    @Test
    public void testSetStatus() throws Exception {
        AdService2 exporter = getServiceProxy(AdService2.class, ApiExternalConstant.AD_SERVICE2_URL);

        SetAdStatusRequest request = new SetAdStatusRequest();
        StatusType type = new StatusType();
        type.setAdId(93834076l);
        type.setStatus(0);

        request.setStatusTypes(new StatusType[] { type });

        ApiResult<Object> result = exporter.setAdStatus(dataUser, request, apiOption);
        System.out.println(result);
    }

    @Test
    public void testDelete() throws Exception {
        AdService2 exporter = getServiceProxy(AdService2.class, ApiExternalConstant.AD_SERVICE2_URL);
        DeleteAdRequest request = new DeleteAdRequest();

        request.setAdIds(new long[] { 93834076 });

        ApiResult<Object> result = exporter.deleteAd(dataUser, request, apiOption);

        System.out.println(result);
    }

    @Test
    public void testReplace() throws Exception {
        AdService2 exporter = getServiceProxy(AdService2.class, ApiExternalConstant.AD_SERVICE2_URL);
        ReplaceAdRequest request = new ReplaceAdRequest();

        request.setAdIds(new long[] { 93834068 });
        request.setAdId(93834084);

        ApiResult<Object> result = exporter.replaceAd(dataUser, request, apiOption);

        System.out.println(result);
    }

    @Test
    public void testGetAd() throws Exception {
        AdService2 exporter = getServiceProxy(AdService2.class, ApiExternalConstant.AD_SERVICE2_URL);
        GetAdByAdIdRequest request = new GetAdByAdIdRequest();
        // request.setAdIds(new long[] {84629784, 84629880});
        request.setAdIds(new long[] {141324304, 141324296});
        ApiResult<AdType> result = exporter.getAdByAdId(dataUser, request, apiOption);
        System.out.println(result);
    }
}
