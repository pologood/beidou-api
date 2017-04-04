package com.baidu.beidou.api.external.accountfile.dao.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.dao.ApiAccountFileTaskDao;
import com.baidu.beidou.test.common.AbstractShardXdbTestCaseLegacy;
@Ignore
public class ApiAccountFileTaskDaoTest extends AbstractShardXdbTestCaseLegacy {

	private static int userId = 9999;

	@Resource
	private ApiAccountFileTaskDao apiAccountFileTaskDao;

	private static final String FILEID = "12345678abcd";
	private static final String MACHINEID = "tc-et-cpro01.tc";
	
	@Test
	@Rollback(true)
	public void testAdd() {
		add();
	}

	@Test
	@Rollback(true)
	public void testFindByUserId() {
		add();
		List<ApiAccountFileTask> list = apiAccountFileTaskDao.findByUserId(userId);
		assertThat(list.size(), is(1));
		for(ApiAccountFileTask t: list){
			System.out.println(t);
		}
		ApiAccountFileTask t = list.get(0);
		assertThat(t.getFileid(), is(FILEID));
	}
	
	@Test
	@Rollback(true)
	public void testFindByFileId() {
		add();
		List<ApiAccountFileTask> list = apiAccountFileTaskDao.findByFileId(FILEID);
		assertThat(list.size(), is(1));
		for(ApiAccountFileTask t: list){
			System.out.println(t);
		}
		ApiAccountFileTask t = list.get(0);
		assertThat(t.getFileid(), is(FILEID));
	}
	
	@Test
	@Rollback(true)
	public void testFindByUserIdAndFileId() {
		add();
		List<ApiAccountFileTask> list = apiAccountFileTaskDao.findByUserIdAndFileId(userId, FILEID);
		assertThat(list.size(), is(1));
		for(ApiAccountFileTask t: list){
			System.out.println(t);
		}
		ApiAccountFileTask t = list.get(0);
		assertThat(t.getFileid(), is(FILEID));
	}
	
	@Test
	@Rollback(true)
	public void testUpdateStatus() {
		add();
		sleep(10);
		int ret = apiAccountFileTaskDao.updateStatus(userId, FILEID, 3);
		assertThat(ret, is(1));
		
//		sleep(10);
//		List<ApiAccountFileTask> list = apiAccountFileTaskDao.findByUserIdAndFileId(userId, FILEID);
//		assertThat(list.size(), is(1));
//		for(ApiAccountFileTask t: list){
//			System.out.println(t);
//		}
//		ApiAccountFileTask t = list.get(0);
//		assertThat(t.getFileid(), is(FILEID));
//		assertThat(t.getStatus(), is(3));
	}
	
	public void add() {
		ApiAccountFileTask task = new ApiAccountFileTask();
		task.setUserid(userId);
		task.setPlanids("999,888,777");
		task.setStatus(1);
		task.setFileid(FILEID);
		task.setMd5("");
		task.setMachineid(MACHINEID);
		task.setFormat(1);
		task.setOpuser(19);
		Date now = new Date();
		task.setAddtime(now);
		task.setModtime(now);
		apiAccountFileTaskDao.makePersistent(task);
		List<ApiAccountFileTask> list = apiAccountFileTaskDao.findByUserId(userId);
		assertThat(list.size(), is(1));
		
		for(ApiAccountFileTask t: list){
			System.out.println(t);
		}
		ApiAccountFileTask t = list.get(0);
		assertThat(t.getFileid(), is(FILEID));
	}
	
}
