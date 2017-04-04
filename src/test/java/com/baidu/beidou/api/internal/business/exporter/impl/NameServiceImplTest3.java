package com.baidu.beidou.api.internal.business.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.api.internal.business.exporter.NameService;
import com.baidu.beidou.api.internal.business.vo.GroupResult;
import com.baidu.beidou.api.internal.business.vo.PlanResult;
import com.baidu.beidou.test.common.BasicTestCaseLegacy;

/**
 * NameService 相关case
 * 
 * @author caichao
 * 
 */
public class NameServiceImplTest3 extends BasicTestCaseLegacy {

    private static int userId = 8;

    @Resource
    private NameService nameService;

    /**
     * inid data from file
     */
    @Before
    public void setUp() {
        super.prepareDataFromClasspathScript(new String[] 
                { "com/baidu/beidou/api/internal/business/exporter/impl/NameServiceImplTest_data.sql" });
    }

    /**
     * getEffectivePlansByUserid test cases
     */
    @Test
    public void testGetEffectivePlansByUserid() {
        PlanResult result = nameService.getEffectivePlansByUserid(userId);
        assertThat(result.getStatus(), is(0));
        assertThat(result.getPlanid2Name().size(), is(1));
        assertThat(result.getPlanid2Name().get(32).getName(), is("u88联盟2830224a"));
        assertThat(result.getPlanid2Name().get(32).getIsDeleted(), is(0));
    }

    /**
     * GetEffectivePlansByUserid_Negative
     */
    @Test
    public void testGetEffectivePlansByUseridNegative() {
        int userid = 8;
        PlanResult result = nameService.getEffectivePlansByUserid(userid);
        assertThat(result.getStatus(), is(0));
        assertThat(result.getPlanid2Name().size(), is(1));
    }

    /**
     * getEffectiveGroupsByUseridAndPlanid test cases
     */
    @Test
    public void testGetEffectiveGroupsByUseridAndPlanidMulti() {
        int planid = 32;
        GroupResult result = nameService.getEffectiveGroupsByUseridAndPlanid(userId, planid);
        assertThat(result.getStatus(), is(0));
        assertThat(result.getGroupid2Name().size(), is(1));
        assertThat(result.getGroupid2Name().get(1649492).getName(), is("我的推广组-1"));
        assertThat(result.getGroupid2Name().get(1649492).getIsDeleted(), is(0));
    }

    /**
     * testGetEffectiveGroupsByUseridAndPlanid_Negative
     */
    @Test
    public void testGetEffectiveGroupsByUseridAndPlanidNegative() {
        int userid = 8;
        int planid = 32;
        GroupResult result = nameService.getEffectiveGroupsByUseridAndPlanid(userid, planid);
        assertThat(result.getStatus(), is(0));
        assertThat(result.getGroupid2Name().size(), is(1));
    }

}
