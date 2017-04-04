package com.baidu.beidou.api.external.accountfile.service.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.service.ApiAccountFileTaskMgr;
import com.baidu.beidou.test.common.AbstractShardXdbTestCaseLegacy;

public class ApiAccountFileTaskMgrTest extends AbstractShardXdbTestCaseLegacy {

    private static int userId = 8;

    @Resource
    public ApiAccountFileTaskMgr apiAccountFileTaskMgr;

    private static final String FILEID = "ef7e28e8fd9f9cb153831212647b658d";
    private static final String MACHINEID = "qa2013-080";

    /**
     * init test case sqldata
     */
    @Before
    public void setUp() {
        super.prepareDataFromClasspathScript(new String[] 
                { "com/baidu/beidou/api/external/accountfile/service/impl/ApiAccountFileTaskMgrTest_data.sql" });
    }

    /**
     * 根据userid查找
     */
    @Test
    public void testFindByUserId() {
        List<ApiAccountFileTask> list = apiAccountFileTaskMgr.findByUserId(userId);
        assertThat(list.size(), is(1));
        for (ApiAccountFileTask t : list) {
            System.out.println(t);
        }
        ApiAccountFileTask t = list.get(0);
        assertThat(t.getFileid(), is(FILEID));
    }

    /**
     * 根据fileid 查找
     */
    @Test
    public void testFindByFileId() {
        List<ApiAccountFileTask> list = apiAccountFileTaskMgr.findByFileId(FILEID);
        assertThat(list.size(), is(1));
        for (ApiAccountFileTask t : list) {
            System.out.println(t);
        }
        ApiAccountFileTask t = list.get(0);
        assertThat(t.getFileid(), is(FILEID));
    }

    /**
     * 根据userid fileid 查询
     */
    @Test
    public void testFindByUserIdAndFileId() {
        List<ApiAccountFileTask> list = apiAccountFileTaskMgr.findByUserIdAndFileId(userId, FILEID);
        assertThat(list.size(), is(1));
        for (ApiAccountFileTask t : list) {
            System.out.println(t);
        }
        ApiAccountFileTask t = list.get(0);
        assertThat(t.getFileid(), is(FILEID));
    }

    /**
     * 更新状态
     */
    @Test
    public void testUpdateStatus() {
        int ret = apiAccountFileTaskMgr.updateStatus(userId, FILEID, 3);
        assertThat(ret, is(1));
    }

    /**
     * 添加 test
     */
    public void add() {
        ApiAccountFileTask task = new ApiAccountFileTask();
        task.setUserid(userId);
        task.setPlanids("100,200,300,400");
        task.setStatus(1);
        task.setFileid(FILEID);
        task.setMd5("");
        task.setMachineid(MACHINEID);
        task.setFormat(1);
        task.setOpuser(8);
        Date now = new Date();
        task.setAddtime(now);
        task.setModtime(now);
        apiAccountFileTaskMgr.addAccountFileTask(task);
        List<ApiAccountFileTask> list = apiAccountFileTaskMgr.findByUserId(userId);
        assertThat(list.size(), is(2));

        for (ApiAccountFileTask t : list) {
            System.out.println(t);
        }
        ApiAccountFileTask t = list.get(1);
        assertThat(t.getFileid(), is(FILEID));
    }

    /**
     * 新增任务
     */
    @Test
    public void testAdd() {
        add();
    }

}
