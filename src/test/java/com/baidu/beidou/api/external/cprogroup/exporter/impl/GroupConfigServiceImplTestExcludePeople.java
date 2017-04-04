package com.baidu.beidou.api.external.cprogroup.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.ExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludePeopleRequest;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.cprogroup.service.CproGroupVTMgr;
import com.baidu.beidou.cprogroup.service.impl.CproGroupVTMgrImpl;
import com.baidu.beidou.test.common.BasicTestCaseLegacy;
import com.baidu.beidou.test.common.MockSequenceIdDaoApi;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 推广组排除人群相关单测
 * 
 * @author caichao
 * 
 */
public class GroupConfigServiceImplTestExcludePeople extends BasicTestCaseLegacy {

    @Resource
    private GroupConfigService groupConfigService;

    /**
     * inid data from file
     */
    @Before
    public void setUp() {
        super.prepareDataFromClasspathScript(new String[] 
                { "com/baidu/beidou/api/external/cprogroup/exporter/impl/GroupConfigServiceImplTest_ExcludePeople_data.sql" });
        GroupConfigServiceImpl impl = (GroupConfigServiceImpl) groupConfigService;
        Field field = null;
        CproGroupVTMgr cproGroupVTMgr = null;
        try {
            field = impl.getClass().getDeclaredField("cproGroupVTMgr");
            field.setAccessible(true);
            cproGroupVTMgr = (CproGroupVTMgr) field.get(impl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (AopUtils.isJdkDynamicProxy(cproGroupVTMgr)) {
            CproGroupVTMgrImpl cproGroupVTMgrImpl;
            try {
                cproGroupVTMgrImpl = (CproGroupVTMgrImpl) ((Advised) cproGroupVTMgr).getTargetSource().getTarget();
//                cproGroupVTMgrImpl.setSequenceIdDao(new MockSequenceIdDaoApi());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            CproGroupVTMgrImpl cproGroupVTMgrImpl = (CproGroupVTMgrImpl) cproGroupVTMgr;
//            cproGroupVTMgrImpl.setSequenceIdDao(new MockSequenceIdDaoApi());
        }
    }

    /**
     * 获取排除人群
     */
    @Test
    public void testGetExcludePeople() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        GetExcludePeopleRequest request = new GetExcludePeopleRequest();
        request.setGroupIds(new long[] { 1649492 });

        BaseResponse<ExcludePeopleType> result = groupConfigService.getExcludePeople(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));
    }

    /**
     * 设置排除人群
     */
    @Test
    public void testSetExcludePeople() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetExcludePeopleRequest request = new SetExcludePeopleRequest();
        ExcludePeopleType type = new ExcludePeopleType();
        type.setGroupId(1649492);

        List<Long> ids = new ArrayList<Long>();
        ids.add(119L);
        type.setExcludePeopleIds(ids);
        request.setExcludePeople(type);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludePeople(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

    }

    /**
     * 设置排除人群
     */
    @Test
    public void testSetExcludePeopleOverride() {
        // 先设置
        testSetExcludePeople();

        // 再覆盖
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetExcludePeopleRequest request = new SetExcludePeopleRequest();
        ExcludePeopleType type = new ExcludePeopleType();
        type.setGroupId(1649492L);

        List<Long> ids = new ArrayList<Long>();
        ids.add(119L);
        type.setExcludePeopleIds(ids);
        request.setExcludePeople(type);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludePeople(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

        // 验证结果
        GetExcludePeopleRequest request2 = new GetExcludePeopleRequest();
        request2.setGroupIds(new long[] { 1649492L });

        BaseResponse<ExcludePeopleType> result2 = groupConfigService.getExcludePeople(user, request2, options);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result2.getErrors(), nullValue());

        assertThat(result2.getOptions().getSuccess(), is(1));
        assertThat(result2.getOptions().getTotal(), is(1));

    }

    /**
     * 排除人群重复
     */
    @Test
    public void testSetExcludePeopleNegativeDuplicated() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetExcludePeopleRequest request = new SetExcludePeopleRequest();
        ExcludePeopleType type = new ExcludePeopleType();
        type.setGroupId(228L);

        List<Long> ids = new ArrayList<Long>();
        ids.add(35L);
        ids.add(35L);
        ids.add(36L);
        ids.add(37L);
        type.setExcludePeopleIds(ids);
        request.setExcludePeople(type);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludePeople(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors().size(), is(1));
        assertThat(result.getData(), nullValue());

        assertThat(result.getOptions().getSuccess(), is(0));
        assertThat(result.getOptions().getTotal(), is(1));

    }

    /**
     * 排除人群不存在
     */
    @Test
    public void testSetExcludePeopleNegativePeopleNotExist() {
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        SetExcludePeopleRequest request = new SetExcludePeopleRequest();
        ExcludePeopleType type = new ExcludePeopleType();
        type.setGroupId(228L);

        List<Long> ids = new ArrayList<Long>();
        ids.add(99999999L);
        ids.add(36L);
        type.setExcludePeopleIds(ids);
        request.setExcludePeople(type);

        BaseResponse<PlaceHolderResult> result = groupConfigService.setExcludePeople(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors().size(), is(1));
        assertThat(result.getData(), nullValue());

        assertThat(result.getOptions().getSuccess(), is(0));
        assertThat(result.getOptions().getTotal(), is(1));

    }

    /**
     * 添加排除人群
     */
    @Test
    public void testAddExcludePeople() {
        // 先设置
        // testSetExcludePeople();

        // 添加
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        AddExcludePeopleRequest request = new AddExcludePeopleRequest();
        List<GroupExcludePeopleType> excludePeoples = new ArrayList<GroupExcludePeopleType>();

        GroupExcludePeopleType type1 = new GroupExcludePeopleType();
        type1.setGroupId(1649492L);
        type1.setExcludePeopleId(119L);

        GroupExcludePeopleType type2 = new GroupExcludePeopleType();
        type2.setGroupId(1649492L);
        type2.setExcludePeopleId(118L);

        GroupExcludePeopleType type3 = new GroupExcludePeopleType();
        type3.setGroupId(1649492L);
        type3.setExcludePeopleId(120L);

        excludePeoples.add(type1);
        excludePeoples.add(type2);
        excludePeoples.add(type3);
        request.setExcludePeoples(excludePeoples);

        BaseResponse<PlaceHolderResult> result = groupConfigService.addExcludePeople(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(3));
        assertThat(result.getOptions().getTotal(), is(3));

        // 验证结果
        GetExcludePeopleRequest request2 = new GetExcludePeopleRequest();
        request2.setGroupIds(new long[] { 1649492L });

        BaseResponse<ExcludePeopleType> result2 = groupConfigService.getExcludePeople(user, request2, options);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().length, is(1));

        assertThat(((ExcludePeopleType) result2.getData()[0]).getGroupId(), is(1649492L));
        assertThat(((ExcludePeopleType) result2.getData()[0]).getExcludePeopleIds().get(0), is(isIn(new Long[] { 118L,
                119L, 120L })));

        assertThat(result2.getOptions().getSuccess(), is(1));
        assertThat(result2.getOptions().getTotal(), is(1));

    }

    /**
     * 添加重复排除人群
     */
    @Test
    public void testAddExcludePeopleNegativeDuplicated() {
        // 先设置
        testSetExcludePeople();

        // 添加
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        AddExcludePeopleRequest request = new AddExcludePeopleRequest();
        List<GroupExcludePeopleType> excludePeoples = new ArrayList<GroupExcludePeopleType>();

        GroupExcludePeopleType type1 = new GroupExcludePeopleType();
        type1.setGroupId(1649492L);
        type1.setExcludePeopleId(15L);

        GroupExcludePeopleType type2 = new GroupExcludePeopleType();
        type2.setGroupId(1649492L);
        type2.setExcludePeopleId(16L);

        GroupExcludePeopleType type3 = new GroupExcludePeopleType();
        type3.setGroupId(1649492L);
        type3.setExcludePeopleId(15L);

        GroupExcludePeopleType type4 = new GroupExcludePeopleType();
        type4.setGroupId(1649492L);
        type4.setExcludePeopleId(19L);

        GroupExcludePeopleType type5 = new GroupExcludePeopleType();
        type5.setGroupId(1649492L);
        type5.setExcludePeopleId(15L);

        excludePeoples.add(type1);
        excludePeoples.add(type2);
        excludePeoples.add(type3);
        excludePeoples.add(type4);
        excludePeoples.add(type5);
        request.setExcludePeoples(excludePeoples);

        BaseResponse<PlaceHolderResult> result = groupConfigService.addExcludePeople(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors().size(), is(5));
        assertThat(result.getData(), nullValue());

    }

    /**
     * 添加不存在人群
     */
    @Test
    public void testAddExcludePeopleNegativePeopleNotExist() {
        // 先设置
        testSetExcludePeople();

        // 添加
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        AddExcludePeopleRequest request = new AddExcludePeopleRequest();
        List<GroupExcludePeopleType> excludePeoples = new ArrayList<GroupExcludePeopleType>();

        GroupExcludePeopleType type1 = new GroupExcludePeopleType();
        type1.setGroupId(1649492L);
        type1.setExcludePeopleId(118L);

        GroupExcludePeopleType type2 = new GroupExcludePeopleType();
        type2.setGroupId(1649492L);
        type2.setExcludePeopleId(120L);

        GroupExcludePeopleType type3 = new GroupExcludePeopleType();
        type3.setGroupId(1649492L);
        type3.setExcludePeopleId(66666666L);

        GroupExcludePeopleType type4 = new GroupExcludePeopleType();
        type4.setGroupId(1649492L);
        type4.setExcludePeopleId(119L);

        GroupExcludePeopleType type5 = new GroupExcludePeopleType();
        type5.setGroupId(1649492L);
        type5.setExcludePeopleId(118L);

        excludePeoples.add(type1);
        excludePeoples.add(type2);
        excludePeoples.add(type3);
        excludePeoples.add(type4);
        excludePeoples.add(type5);
        request.setExcludePeoples(excludePeoples);

        BaseResponse<PlaceHolderResult> result = groupConfigService.addExcludePeople(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getData(), nullValue());

    }

    /**
     * 删除排除人群
     */
    @Test
    public void testDeleteExcludePeople() {
        // 先设置
        testAddExcludePeople();

        // 再删除
        final int userId = 8;

        BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(userId, userId);
        BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();

        DeleteExcludePeopleRequest request = new DeleteExcludePeopleRequest();
        List<GroupExcludePeopleType> excludePeoples = new ArrayList<GroupExcludePeopleType>();

        GroupExcludePeopleType type1 = new GroupExcludePeopleType();
        type1.setGroupId(1649492L);
        type1.setExcludePeopleId(118L);

        excludePeoples.add(type1);
        request.setExcludePeoples(excludePeoples);

        BaseResponse<PlaceHolderResult> result = groupConfigService.deleteExcludePeople(user, request, options);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().length, is(1));

        assertThat(result.getOptions().getSuccess(), is(1));
        assertThat(result.getOptions().getTotal(), is(1));

    }

}
