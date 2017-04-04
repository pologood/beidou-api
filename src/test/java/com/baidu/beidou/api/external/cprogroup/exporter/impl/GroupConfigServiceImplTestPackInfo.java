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
import com.baidu.beidou.api.external.cprogroup.vo.GroupPackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.PackInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.PackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeletePackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetPackInfoRequest;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.test.common.BasicTestCaseLegacy;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 推广组组合相关测试 呵呵
 * 
 * @author caichao
 * 
 */
public class GroupConfigServiceImplTestPackInfo extends BasicTestCaseLegacy {

    @Resource
    private GroupConfigService groupConfigService;

    /**
     * inid data from file
     */
    @Before
    public void setUp() {
        super.prepareDataFromClasspathScript(new String[] { "com/baidu/beidou/api/external/cprogroup/exporter/impl/GroupConfigServiceImplTest_ExcludePeople_data.sql" });
    }

    /**
     * 获取组合单测
     */
    @Test
    public void testGetPackInfo() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        GetPackInfoRequest request = new GetPackInfoRequest();
        request.setGroupIds(new long[] { 1649493L });

        BaseResponse<PackInfoType> result = groupConfigService.getPackInfo(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(((PackInfoType) result.getData()[0]).getGroupId(), is(1649493L));
        assertThat(((PackInfoType) result.getData()[0]).getPackItems().size(), is(3));
        assertThat(((PackInfoType) result.getData()[0]).getPackItems().get(0).getPackId(), is(20));
        assertThat(((PackInfoType) result.getData()[0]).getPackItems().get(0).getType(), is(3));

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

    }

    /**
     * 设置组合单测
     */
    @Test
    public void testSetPackInfo() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPackInfoRequest request = new SetPackInfoRequest();
        PackInfoType type = new PackInfoType();
        type.setGroupId(1649493L);
        List<PackItemType> packItems = new ArrayList<PackItemType>();

        PackItemType type2 = new PackItemType();
        type2.setType(3);
        type2.setPackId(20); // 关键词

        packItems.add(type2);
        type.setPackItems(packItems);

        request.setPackInfo(type);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPackInfo(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

        // 验证结果
        GetPackInfoRequest request2 = new GetPackInfoRequest();
        request2.setGroupIds(new long[] { 1649493L });

        BaseResponse<PackInfoType> result2 = groupConfigService.getPackInfo(user, request2, options);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().length, is(1));

        assertThat(((PackInfoType) result2.getData()[0]).getGroupId(), is(1649493L));
        assertThat(((PackInfoType) result2.getData()[0]).getPackItems().size(), is(1));

        assertThat(result2.getOptions().getSuccess(), is(1));
        assertThat(result2.getOptions().getTotal(), is(1));

    }

    /**
     * 设置组合单测
     */
    @Test
    public void testSetPackInfo_Negative_Optimzed() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPackInfoRequest request = new SetPackInfoRequest();
        PackInfoType type = new PackInfoType();
        type.setGroupId(1649493L);
        List<PackItemType> packItems = new ArrayList<PackItemType>();

        PackItemType type2 = new PackItemType();
        type2.setType(3);
        type2.setPackId(20); // 关键词

        packItems.add(type2);
        type.setPackItems(packItems);

        request.setPackInfo(type);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPackInfo(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

    }

    /**
     * 设置组合配置
     */
    @Test
    public void testSetPackInfo_Negative_Type() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPackInfoRequest request = new SetPackInfoRequest();
        PackInfoType type = new PackInfoType();
        type.setGroupId(1649493L);
        List<PackItemType> packItems = new ArrayList<PackItemType>();

        PackItemType type2 = new PackItemType();
        type2.setType(3);
        type2.setPackId(20); // 关键词

        packItems.add(type2);

        type.setPackItems(packItems);

        request.setPackInfo(type);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPackInfo(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

    }

    /**
     * 设置不存在的组合
     */
    @Test
    public void testSetPackInfo_Negative_NotExistPack() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetPackInfoRequest request = new SetPackInfoRequest();
        PackInfoType type = new PackInfoType();
        type.setGroupId(1649493L);
        List<PackItemType> packItems = new ArrayList<PackItemType>();

        PackItemType type2 = new PackItemType();
        type2.setType(3);
        type2.setPackId(20); // 关键词

        packItems.add(type2);

        type.setPackItems(packItems);

        request.setPackInfo(type);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setPackInfo(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

    }

    /**
     * 添加组合
     */
    @Test
    public void testAddPackInfo() {
        // 准备数据
        testSetPackInfo();

        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        AddPackInfoRequest request = new AddPackInfoRequest();
        List<GroupPackItemType> packs = new ArrayList<GroupPackItemType>();

        GroupPackItemType type2 = new GroupPackItemType();
        type2.setGroupId(1649493L);
        type2.setType(3);
        type2.setPackId(100);

        packs.add(type2);

        request.setPacks(packs);

        BaseResponse<PlaceHolderResult> result = groupConfigService.addPackInfo(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

        GetPackInfoRequest request2 = new GetPackInfoRequest();
        request2.setGroupIds(new long[] { 1649493L });

        BaseResponse<PackInfoType> result2 = groupConfigService.getPackInfo(user, request2, options);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().length, is(1));

        assertThat(((PackInfoType) result2.getData()[0]).getGroupId(), is(1649493L));
        assertThat(((PackInfoType) result2.getData()[0]).getPackItems().size(), is(2));

        assertThat(result2.getOptions().getSuccess(), is(1));
        assertThat(result2.getOptions().getTotal(), is(1));

    }

    /**
     * 对错误类型单测
     */
    @Test
    public void testAddPackInfo_Negative_TypeWrongAndNotExist() {
        // 准备数据
        testSetPackInfo();

        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        AddPackInfoRequest request = new AddPackInfoRequest();
        List<GroupPackItemType> packs = new ArrayList<GroupPackItemType>();

        GroupPackItemType type1 = new GroupPackItemType();
        type1.setGroupId(1649493L);
        type1.setType(88);
        type1.setPackId(802);

        GroupPackItemType type2 = new GroupPackItemType();
        type2.setGroupId(1649493L);
        type2.setType(3);
        type2.setPackId(4);

        GroupPackItemType type3 = new GroupPackItemType();
        type3.setGroupId(1649493L);
        type3.setType(2);
        type3.setPackId(999);

        GroupPackItemType type4 = new GroupPackItemType();
        type4.setGroupId(1649493L);
        type4.setType(1);
        type4.setPackId(100002);

        GroupPackItemType type5 = new GroupPackItemType();
        type5.setGroupId(1649493L);
        type5.setType(4);
        type5.setPackId(801);

        packs.add(type1);
        packs.add(type2);
        packs.add(type3);
        packs.add(type4);
        packs.add(type5);

        request.setPacks(packs);

        BaseResponse<PlaceHolderResult> result = groupConfigService.addPackInfo(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors().size(), is(5));
        assertThat(result.getData(), nullValue());

        assertThat(result.getOptions().getSuccess(), is(0));
        assertThat(result.getOptions().getTotal(), is(5));

    }

    /**
     * 关键词组合单测
     */
    @Test
    public void testAddPackInfo_Negative_ExcludeKeyword() {
        // 准备数据
        testSetPackInfo();

        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        AddPackInfoRequest request = new AddPackInfoRequest();
        List<GroupPackItemType> packs = new ArrayList<GroupPackItemType>();

        GroupPackItemType type1 = new GroupPackItemType();
        type1.setGroupId(1649493L);
        type1.setType(3);
        type1.setPackId(11);

        packs.add(type1);

        request.setPacks(packs);

        BaseResponse<PlaceHolderResult> result = groupConfigService.addPackInfo(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors().size(), is(1));
        assertThat(result.getData(), nullValue());

        assertThat(result.getOptions().getSuccess(), is(0));
        assertThat(result.getOptions().getTotal(), is(1));

    }

    // @Test
    // public void testAddPackInfo_Negative_ExcludePeople() {
    // // 准备数据
    // testSetPackInfo();
    //
    // final int userId = 8;
    //
    // BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId,userId);
    // BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
    //
    // AddPackInfoRequest request = new AddPackInfoRequest();
    // List<GroupPackItemType> packs = new ArrayList<GroupPackItemType>();
    //
    // GroupPackItemType type1 = new GroupPackItemType();
    // type1.setGroupId(245481);
    // type1.setType(2);
    // type1.setPackId(37);
    //
    // packs.add(type1);
    //
    // request.setPacks(packs);
    //
    // BaseResponse<PlaceHolderResult> result = groupConfigService.addPackInfo(user, request, options);
    //
    // // 打印返回
    // System.out.println(result);
    //
    // // 成功后没有error，并且返回该用户的信息
    // assertThat(result.getErrors().size(), is(1));
    // assertThat(result.getData(), nullValue());
    //
    // assertThat(result.getOptions().getSuccess(), is(0));
    // assertThat(result.getOptions().getTotal(), is(1));
    //
    // }

    /**
     * 删除组合配置
     */
    @Test
    public void testDeletePackInfo() {
        // 准备数据
        testSetPackInfo();

        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        /**
         * PackItemType type1 = new PackItemType(); type1.setType(4); type1.setPackId(801); // 高级
         * 
         * PackItemType type2 = new PackItemType(); type2.setType(3); type2.setPackId(1); // 关键词
         * 
         * PackItemType type3 = new PackItemType(); type3.setType(2); type3.setPackId(36); // 人群
         * 
         * PackItemType type4 = new PackItemType(); type4.setType(1); type4.setPackId(100001); //兴趣
         */
        DeletePackInfoRequest request = new DeletePackInfoRequest();
        List<GroupPackItemType> packs = new ArrayList<GroupPackItemType>();

        GroupPackItemType type2 = new GroupPackItemType();
        type2.setGroupId(1649493L);
        type2.setType(3);
        type2.setPackId(20); // 关键词

        packs.add(type2);

        request.setPacks(packs);

        BaseResponse<PlaceHolderResult> result = groupConfigService.deletePackInfo(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

        // 验证结果
        GetPackInfoRequest request2 = new GetPackInfoRequest();
        request2.setGroupIds(new long[] { 1649493L });

        BaseResponse<PackInfoType> result2 = groupConfigService.getPackInfo(user, request2, options);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().length, is(0));

        assertThat(result2.getOptions().getSuccess(), is(1));
        assertThat(result2.getOptions().getTotal(), is(1));

    }
}
