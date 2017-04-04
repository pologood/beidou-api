package com.baidu.beidou.api.external.cprogroup.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetPriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetPriceRequest;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.test.common.BasicTestCaseLegacy;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 推广组各种出价单测
 * 
 * @author work
 * 
 */
public class GroupConfigServiceImplTestPrice extends BasicTestCaseLegacy {

    @Resource
    private GroupConfigService groupConfigService;

    /**
     * inid data from file
     */
    @Before
    public void setUp() {
        super.prepareDataFromClasspathScript(new String[] 
                { "com/baidu/beidou/api/external/cprogroup/exporter/impl/GroupConfigServiceImplTest_ExcludePeople_data.sql" });
    }

    /**
     * 获取出价信息
     */
    @Test
    public void testGetPrice() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        GetPriceRequest request = new GetPriceRequest();
        request.setGroupIds(new long[] { 1649492L });

        BaseResponse<PriceType> result = groupConfigService.getPrice(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(3));

        assertThat(((PriceType) result.getData()[0]).getGroupId(), is(1649492L));
        assertThat(((PriceType) result.getData()[0]).getId1(), is(2));
        assertThat(((PriceType) result.getData()[0]).getPrice(), is(120));
        assertThat(((PriceType) result.getData()[0]).getType(), is(1));

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

    }

    /**
     * 获取兴趣出价
     */
    @Test
    public void testSetGetPrice_InterestPrice() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPriceRequest request = new SetPriceRequest();
        List<PriceType> prices = new ArrayList<PriceType>();

        PriceType type1 = new PriceType();
        type1.setGroupId(1649492L);
        type1.setId1(2);
        type1.setPrice(100);
        type1.setType(1);
        prices.add(type1);

        PriceType type2 = new PriceType();
        type2.setGroupId(1649492L);
        type2.setId1(8);
        type2.setPrice(200);
        type2.setType(1);
        prices.add(type2);

        PriceType type3 = new PriceType();
        type3.setGroupId(1649492L);
        type3.setId1(10);
        type3.setPrice(1000);
        type3.setType(1);
        prices.add(type3);

        request.setPrices(prices);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPrice(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(3));
        assertThat(result.getOptions().getTotal(), is(3));

        // 验证结果
        GetPriceRequest request2 = new GetPriceRequest();
        request2.setGroupIds(new long[] { 1649492L });

        BaseResponse<PriceType> result2 = groupConfigService.getPrice(user, request2, options);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        /**
         * [data=[com.baidu.beidou.api.external.cprogroup.vo.PriceType@1f16e6e[type=1,groupId=245434,id1=2,packType=-1,
         * id2=-1,isCustomInterest=false,price=100], com.baidu.beidou.api.external.cprogroup.vo.PriceType@1890510
         * 
         * [type=1,groupId=245442,id1=8,packType=-1,id2=-1,isCustomInterest=false,price=200]], errors=null,
         * options=BaseResponseOptions [total=2, success=2]]
         */
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().length, is(3));

        for (PriceType priceType : result2.getData()) {
            if (priceType.getGroupId() == 1649492L) {
                if (priceType.getId1() == 2) {
                    assertThat(priceType.getId1(), is(2));
                    assertThat(priceType.getPrice(), is(100));
                    assertThat(priceType.getType(), is(1));
                    assertThat(priceType.isCustomInterest(), is(false));
                }
                if (priceType.getId1() == 100001) {
                    assertThat(priceType.getId1(), is(100001));
                    assertThat(priceType.getPrice(), is(1000));
                    assertThat(priceType.getType(), is(1));
                    assertThat(priceType.isCustomInterest(), is(true));
                }
            }
        }
        // assertThat(((PriceType)result2.getData()[0]).getGroupId(), is(245434l));
        // assertThat(((PriceType)result2.getData()[0]).getId1(), is(2));
        // assertThat(((PriceType)result2.getData()[0]).getPrice(), is(100));
        // assertThat(((PriceType)result2.getData()[0]).getType(), is(1));
        // assertThat(((PriceType)result2.getData()[0]).isCustomInterest(), is(false));
        //
        // assertThat(((PriceType)result2.getData()[1]).getGroupId(), is(245442l));
        // assertThat(((PriceType)result2.getData()[1]).getId1(), is(8));
        // assertThat(((PriceType)result2.getData()[1]).getPrice(), is(200));
        // assertThat(((PriceType)result2.getData()[1]).getType(), is(1));
        // assertThat(((PriceType)result2.getData()[1]).isCustomInterest(), is(false));
        //
        // assertThat(((PriceType)result2.getData()[2]).getGroupId(), is(245434l));
        // assertThat(((PriceType)result2.getData()[2]).getId1(), is(100001));
        // assertThat(((PriceType)result2.getData()[2]).getPrice(), is(1000));
        // assertThat(((PriceType)result2.getData()[2]).getType(), is(1));
        // assertThat(((PriceType)result2.getData()[2]).isCustomInterest(), is(true));

        assertThat(result2.getOptions().getSuccess(), is(1));
        assertThat(result2.getOptions().getTotal(), is(1));

    }

    /**
     * 取消兴趣出价
     */
    @Test
    public void testSetGetPrice_InterestPrice_Cancel() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPriceRequest request = new SetPriceRequest();
        List<PriceType> prices = new ArrayList<PriceType>();

        PriceType type1 = new PriceType();
        type1.setGroupId(1649492L);
        type1.setId1(2);
        type1.setPrice(-1);
        type1.setType(1);
        prices.add(type1);

        PriceType type2 = new PriceType();
        type2.setGroupId(1649492L);
        type2.setId1(8);
        type2.setPrice(-1);
        type2.setType(1);
        prices.add(type2);

        PriceType type3 = new PriceType();
        type3.setGroupId(1649492L);
        type3.setId1(10);
        type3.setPrice(-1);
        type3.setType(1);
        prices.add(type3);

        request.setPrices(prices);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPrice(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(3));
        assertThat(result.getOptions().getTotal(), is(3));

        // 验证结果
        GetPriceRequest request2 = new GetPriceRequest();
        request2.setGroupIds(new long[] { 1649492L });

        BaseResponse<PriceType> result2 = groupConfigService.getPrice(user, request2, options);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        /**
         * [data=[com.baidu.beidou.api.external.cprogroup.vo.PriceType@1f16e6e[type=1,groupId=245434,id1=2,packType=-1,
         * id2=-1,isCustomInterest=false,price=100], com.baidu.beidou.api.external.cprogroup.vo.PriceType@1890510
         * 
         * [type=1,groupId=245442,id1=8,packType=-1,id2=-1,isCustomInterest=false,price=200]], errors=null,
         * options=BaseResponseOptions [total=2, success=2]]
         */
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().length, is(0));
        assertThat(result2.getOptions().getSuccess(), is(1));
        assertThat(result2.getOptions().getTotal(), is(1));

    }

    /**
     * 设置不存在和错误类型出价
     */
    @Test
    public void testSetGetPrice_InterestPrice_Negative_InterestNotExistAndTypeWrong() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPriceRequest request = new SetPriceRequest();
        List<PriceType> prices = new ArrayList<PriceType>();

        PriceType type1 = new PriceType();
        type1.setGroupId(1649492L);
        type1.setId1(2); //
        type1.setPrice(-1);
        type1.setType(1);
        prices.add(type1);

        PriceType type2 = new PriceType();
        type2.setGroupId(1649492L);
        type2.setId1(8);
        type2.setPrice(-1);
        type2.setType(9999999); //
        prices.add(type2);

        PriceType type3 = new PriceType();
        type3.setGroupId(1649492L);
        type3.setId1(10);
        type3.setPrice(-1);
        type3.setType(1);
        prices.add(type3);

        request.setPrices(prices);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPrice(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors().size(), is(1));
        assertThat(result.getData(), nullValue());

        assertThat(result.getOptions().getSuccess(), is(2));
        assertThat(result.getOptions().getTotal(), is(3));

    }

    /**
     * 组合出价
     */
    @Test
    public void testSetGetPrice_Pack() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPriceRequest request = new SetPriceRequest();
        List<PriceType> prices = new ArrayList<PriceType>();

        PriceType type1 = new PriceType();
        type1.setGroupId(1649492L);
        type1.setId1(20);
        type1.setPrice(100);
        type1.setType(2);
        type1.setPackType(3);
        prices.add(type1);

        PriceType type2 = new PriceType();
        type2.setGroupId(1649492L);
        type2.setId1(80);
        type2.setPrice(500);
        type2.setType(2);
        type2.setPackType(3);
        prices.add(type2);

        PriceType type3 = new PriceType();
        type3.setGroupId(1649492L);
        type3.setId1(100);
        type3.setPrice(700);
        type3.setType(2);
        type3.setPackType(3);
        prices.add(type3);

        request.setPrices(prices);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPrice(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(3));
        assertThat(result.getOptions().getTotal(), is(3));

        // 验证结果
        GetPriceRequest request2 = new GetPriceRequest();
        request2.setGroupIds(new long[] { 1649492L });

        BaseResponse<PriceType> result2 = groupConfigService.getPrice(user, request2, options);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        /**
         * [data=[com.baidu.beidou.api.external.cprogroup.vo.PriceType@1c221b2[
         * type=2,groupId=245490,id1=1,packType=4,id2=-1,isCustomInterest=false,price=100],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@326c98[
         * type=2,groupId=245490,id1=8,packType=4,id2=-1,isCustomInterest=false,price=0],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@777255[
         * type=2,groupId=245490,id1=1,packType=3,id2=-1,isCustomInterest=false,price=500],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@1f357d3[
         * type=2,groupId=245490,id1=100001,packType=1,id2=-1,isCustomInterest=false,price=0],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@10ae2bf[
         * type=2,groupId=245490,id1=4,packType=2,id2=-1,isCustomInterest=false,price=0],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@1ac8e09[
         * type=2,groupId=245491,id1=2,packType=4,id2=-1,isCustomInterest=false,price=0],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@1a0ff10[
         * type=2,groupId=245491,id1=100002,packType=1,id2=-1,isCustomInterest=false,price=700]], errors=null,
         * options=BaseResponseOptions [total=2, success=2]]
         */
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().length, is(6));

        assertThat(((PriceType) result2.getData()[0]).getGroupId(), is(1649492L));
        assertThat(((PriceType) result2.getData()[0]).getId1(), is(20));
        assertThat(((PriceType) result2.getData()[0]).getPrice(), is(100));
        assertThat(((PriceType) result2.getData()[0]).getType(), is(2));
        assertThat(((PriceType) result2.getData()[0]).getPackType(), is(3));

        assertThat(((PriceType) result2.getData()[2]).getGroupId(), is(1649492L));
        assertThat(((PriceType) result2.getData()[2]).getId1(), is(100));
        assertThat(((PriceType) result2.getData()[2]).getPrice(), is(700));
        assertThat(((PriceType) result2.getData()[2]).getType(), is(2));
        assertThat(((PriceType) result2.getData()[2]).getPackType(), is(3));

        assertThat(result2.getOptions().getSuccess(), is(1));
        assertThat(result2.getOptions().getTotal(), is(1));

    }

    /**
     * 取消组合出价
     */
    @Test
    public void testSetGetPrice_Pack_Cancel() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPriceRequest request = new SetPriceRequest();
        List<PriceType> prices = new ArrayList<PriceType>();

        PriceType type1 = new PriceType();
        type1.setGroupId(1649492L);
        type1.setId1(80);
        type1.setPrice(-1);
        type1.setType(2);
        type1.setPackType(3);
        prices.add(type1);

        PriceType type2 = new PriceType();
        type2.setGroupId(1649492L);
        type2.setId1(20);
        type2.setPrice(-1);
        type2.setType(2);
        type2.setPackType(3);
        prices.add(type2);

        PriceType type3 = new PriceType();
        type3.setGroupId(1649492L);
        type3.setId1(100);
        type3.setPrice(-1);
        type3.setType(2);
        type3.setPackType(3);
        prices.add(type3);

        request.setPrices(prices);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPrice(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(3));
        assertThat(result.getOptions().getTotal(), is(3));

        // 验证结果
        GetPriceRequest request2 = new GetPriceRequest();
        request2.setGroupIds(new long[] { 1649492L });

        BaseResponse<PriceType> result2 = groupConfigService.getPrice(user, request2, options);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        /**
         * [data=[com.baidu.beidou.api.external.cprogroup.vo.PriceType@1c221b2[
         * type=2,groupId=245490,id1=1,packType=4,id2=-1,isCustomInterest=false,price=100],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@326c98[
         * type=2,groupId=245490,id1=8,packType=4,id2=-1,isCustomInterest=false,price=0],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@777255[
         * type=2,groupId=245490,id1=1,packType=3,id2=-1,isCustomInterest=false,price=500],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@1f357d3[
         * type=2,groupId=245490,id1=100001,packType=1,id2=-1,isCustomInterest=false,price=0],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@10ae2bf[
         * type=2,groupId=245490,id1=4,packType=2,id2=-1,isCustomInterest=false,price=0],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@1ac8e09[
         * type=2,groupId=245491,id1=2,packType=4,id2=-1,isCustomInterest=false,price=0],
         * com.baidu.beidou.api.external.cprogroup.vo.PriceType@1a0ff10[
         * type=2,groupId=245491,id1=100002,packType=1,id2=-1,isCustomInterest=false,price=700]], errors=null,
         * options=BaseResponseOptions [total=2, success=2]]
         */
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().length, is(3));

        assertThat(result2.getOptions().getSuccess(), is(1));
        assertThat(result2.getOptions().getTotal(), is(1));

    }

    /**
     * 错误类型的组合的出价
     */
    @Test
    public void testSetGetPrice_Pack_Negative_PackNotRelated() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPriceRequest request = new SetPriceRequest();
        List<PriceType> prices = new ArrayList<PriceType>();

        PriceType type1 = new PriceType();
        type1.setGroupId(1649492L);
        type1.setId1(20);
        type1.setPrice(-1);
        type1.setType(2);
        type1.setPackType(3);
        prices.add(type1);

        PriceType type2 = new PriceType();
        type2.setGroupId(1649492L);
        type2.setId1(80);
        type2.setPrice(-1);
        type2.setType(2);
        type2.setPackType(3);
        prices.add(type2);

        PriceType type3 = new PriceType();
        type3.setGroupId(1649492L);
        type3.setId1(100);
        type3.setPrice(-1);
        type3.setType(2);
        type3.setPackType(3);
        prices.add(type3);

        request.setPrices(prices);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPrice(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());

        assertThat(result.getOptions().getSuccess(), is(3));
        assertThat(result.getOptions().getTotal(), is(3));

    }

}
