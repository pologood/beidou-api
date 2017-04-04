package com.baidu.beidou.api.internal.audit.dao;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import com.baidu.beidou.api.internal.audit.vo.request.QueryBase;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;

//@Ignore
public class UnitAuditDaoImplTest extends AbstractShardAddbTestCaseLegacy {

    private static int userId = 499;

    @Override
    public int getShard() {
        return userId;
    }

    @Resource
    private UnitAuditDao UnitAuditDao;

    @Test
    public void testGetReauditUserList() throws Exception{
        QueryBase query = new QueryBase();
        query.setQueryType(1);
        query.setQuery("cprotest2");
        query.setPage(1);
        query.setPageSize(10);
        
        List<Integer> result = UnitAuditDao.findAllUserIds(query);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        System.out.println(result.get(0));
    }
}
