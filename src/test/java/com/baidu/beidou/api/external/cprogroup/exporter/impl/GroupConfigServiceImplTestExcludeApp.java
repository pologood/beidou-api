package com.baidu.beidou.api.external.cprogroup.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeAppType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeAppRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.vo.AppInfo;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;

/**
 * GroupConfigServiceImplTest_ExcludeApp
 * @author work
 *
 */
public class GroupConfigServiceImplTestExcludeApp extends AbstractShardAddbTestCaseLegacy {

    private static int userId = 499;

    @Override
    public int getShard() {
        return userId;
    }

    @Resource
    private GroupConfigService groupConfigService;

    /**
     * DB中的所有App信息
     * 
     * @author wangchongjie
     */
    @Test
    public void testLoad() {
        List<AppInfo> appInfoList = new ArrayList<AppInfo>(0);
        AppInfo app1 = new AppInfo();
        app1.setSid(123L);
        app1.setName("愤怒的小鸟");
        app1.setId(1L);
        appInfoList.add(app1);

        AppInfo app2 = new AppInfo();
        app2.setSid(456L);
        app2.setName("12580");
        app2.setId(2L);
        appInfoList.add(app2);

        AppInfo app3 = new AppInfo();
        app3.setSid(789L);
        app3.setName("WIFI");
        app3.setId(3L);
        appInfoList.add(app3);

        /**
         * 
         * 应用App信息倒排索引，通过appId定位该站点在appList中的序号， 即key为appId，value为appList的index
         * 
         * @author wangchongjie
         */
        Map<Long, Integer> reverseIndexByAppId = new HashMap<Long, Integer>(0);
        reverseIndexByAppId.put(123L, 0);
        reverseIndexByAppId.put(456L, 1);
        reverseIndexByAppId.put(789L, 2);

        /**
         * 应用App信息倒排索引，通过站点url定位该站点在siteInfoList中的序号， 即key为app名字，value为appList的index
         * 
         * @author wangchongjie
         */
        Map<String, Integer> reverseIndexByAppName = new HashMap<String, Integer>(0);
        reverseIndexByAppName.put("愤怒的小鸟", 0);
        reverseIndexByAppName.put("12580", 1);
        reverseIndexByAppName.put("WIFI", 2);

        UnionSiteCache.appCache.setAppInfoList(appInfoList);
        UnionSiteCache.appCache.setReverseIndexByAppId(reverseIndexByAppId);
        UnionSiteCache.appCache.setReverseIndexByAppName(reverseIndexByAppName);

    }

    /**
     * 添加排除app
     */
    @Test
    public void testAddExcludeApp() {
        int groupId = 228;
        int groupId1 = 244538;
        DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
        ApiOption apiOption = DarwinApiHelper.getApiOptions();
        DarwinApiHelper.setVisitorSession(userId);
        DarwinApiHelper.setUserSession(userId);

        AddExcludeAppRequest request = new AddExcludeAppRequest();
        List<GroupExcludeAppType> excludeAppList = new ArrayList<GroupExcludeAppType>();
        request.setExcludeApps(excludeAppList);

        GroupExcludeAppType excludeApp0 = new GroupExcludeAppType();
        excludeApp0.setGroupId(groupId);
        List<Long> appFilters = new ArrayList<Long>();
        appFilters.add(123L);
        appFilters.add(456L);
        appFilters.add(789L);
        excludeApp0.setExcludeApp(appFilters);
        excludeAppList.add(excludeApp0);

        GroupExcludeAppType excludeApp1 = new GroupExcludeAppType();
        excludeApp1.setGroupId(groupId1);
        List<Long> appFilters1 = new ArrayList<Long>();
        appFilters1.add(456L);
        appFilters1.add(321L);
        appFilters1.add(987L);
        excludeApp1.setExcludeApp(appFilters1);
        excludeAppList.add(excludeApp1);

        GroupExcludeAppType excludeApp2 = new GroupExcludeAppType();
        excludeApp2.setGroupId(groupId1);
        List<Long> appFilters2 = new ArrayList<Long>();
        appFilters2.add(1111L);
        appFilters2.add(2222L);
        appFilters2.add(3333L);
        excludeApp2.setExcludeApp(appFilters2);
        excludeAppList.add(excludeApp2);

        GroupExcludeAppType excludeApp3 = new GroupExcludeAppType();
        excludeApp3.setGroupId(groupId1);
        List<Long> appFilters3 = new ArrayList<Long>();
        appFilters3.add(12580L);
        excludeApp3.setExcludeApp(appFilters3);
        excludeAppList.add(excludeApp3);

        ApiResult<Object> result = groupConfigService.addExcludeApp(dataUser, request, apiOption);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors().size(), is(4));

        sleep(1);

        // 验证结果
        GetExcludeAppRequest request2 = new GetExcludeAppRequest();
        request2.setGroupIds(new long[] { groupId });

        ApiResult<GroupExcludeAppType> result2 = groupConfigService.getExcludeApp(dataUser, request2, apiOption);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().size(), is(1));

        assertThat(((GroupExcludeAppType) result2.getData().get(0)).getGroupId(), is(228L));
        assertThat(((GroupExcludeAppType) result2.getData().get(0)).getExcludeApp().size(), is(0));

        assertThat(result2.getPayment().getTotal(), is(1));
        assertThat(result2.getPayment().getSuccess(), is(1));

    }

    /**
     * 删除排除app
     */
    @Test
    public void testDeleteExcludeApp() {
        int groupId = 228;
        int groupId1 = 244538;
        DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
        ApiOption apiOption = DarwinApiHelper.getApiOptions();
        DarwinApiHelper.setVisitorSession(userId);
        DarwinApiHelper.setUserSession(userId);

        AddExcludeAppRequest request = new AddExcludeAppRequest();
        List<GroupExcludeAppType> excludeAppList = new ArrayList<GroupExcludeAppType>();
        request.setExcludeApps(excludeAppList);

        GroupExcludeAppType excludeApp0 = new GroupExcludeAppType();
        excludeApp0.setGroupId(groupId);
        List<Long> appFilters = new ArrayList<Long>();
        appFilters.add(123L);
        appFilters.add(456L);
        appFilters.add(789L);
        excludeApp0.setExcludeApp(appFilters);
        excludeAppList.add(excludeApp0);

        GroupExcludeAppType excludeApp1 = new GroupExcludeAppType();
        excludeApp1.setGroupId(groupId1);
        List<Long> appFilters1 = new ArrayList<Long>();
        appFilters1.add(456L);
        appFilters1.add(321L);
        appFilters1.add(987L);
        excludeApp1.setExcludeApp(appFilters1);
        excludeAppList.add(excludeApp1);

        GroupExcludeAppType excludeApp2 = new GroupExcludeAppType();
        excludeApp2.setGroupId(groupId1);
        List<Long> appFilters2 = new ArrayList<Long>();
        appFilters2.add(1111L);
        appFilters2.add(2222L);
        appFilters2.add(3333L);
        excludeApp2.setExcludeApp(appFilters2);
        excludeAppList.add(excludeApp2);

        GroupExcludeAppType excludeApp3 = new GroupExcludeAppType();
        excludeApp3.setGroupId(groupId1);
        List<Long> appFilters3 = new ArrayList<Long>();
        appFilters3.add(12580L);
        excludeApp3.setExcludeApp(appFilters3);
        excludeAppList.add(excludeApp3);

        ApiResult<Object> result = groupConfigService.addExcludeApp(dataUser, request, apiOption);

        // 打印返回
        System.out.println(result);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors().size(), is(4));
        assertThat(result.getPayment().getTotal(), is(4));

        sleep(1);

        GetExcludeAppRequest request2 = new GetExcludeAppRequest();
        request2.setGroupIds(new long[] { groupId });

        ApiResult<GroupExcludeAppType> result2 = groupConfigService.getExcludeApp(dataUser, request2, apiOption);

        // 打印返回
        System.out.println(result2);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().size(), is(1));

        assertThat(((GroupExcludeAppType) result2.getData().get(0)).getGroupId(), is(228L));
        assertThat(((GroupExcludeAppType) result2.getData().get(0)).getExcludeApp().size(), is(0));

        assertThat(result2.getPayment().getTotal(), is(1));
        assertThat(result2.getPayment().getSuccess(), is(1));

        DeleteExcludeAppRequest request3 = new DeleteExcludeAppRequest();
        List<GroupExcludeAppType> delExcludeAppList = new ArrayList<GroupExcludeAppType>();
        GroupExcludeAppType delExcludeApp0 = new GroupExcludeAppType();
        delExcludeApp0.setGroupId(groupId);
        List<Long> delAppFilters = new ArrayList<Long>();
        delAppFilters.add(123212L);
        delExcludeApp0.setExcludeApp(delAppFilters);
        delExcludeAppList.add(delExcludeApp0);
        request3.setExcludeApps(delExcludeAppList);

        ApiResult<Object> result3 = groupConfigService.deleteExcludeApp(dataUser, request3, apiOption);

        // 打印返回
        System.out.println(result3);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result2.getErrors(), nullValue());
        assertThat(result2.getData().size(), is(1));

        ApiResult<GroupExcludeAppType> result4 = groupConfigService.getExcludeApp(dataUser, request2, apiOption);

        // 打印返回
        System.out.println(result4);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result4.getErrors(), nullValue());
        assertThat(result4.getData().size(), is(1));

        assertThat(((GroupExcludeAppType) result4.getData().get(0)).getGroupId(), is(228L));
        assertThat(((GroupExcludeAppType) result4.getData().get(0)).getExcludeApp().size(), is(0));

        assertThat(result4.getPayment().getTotal(), is(1));
        assertThat(result4.getPayment().getSuccess(), is(1));

    }

}
