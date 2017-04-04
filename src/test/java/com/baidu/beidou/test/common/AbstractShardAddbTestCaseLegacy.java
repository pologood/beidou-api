/**
 * beidou-core-535#com.baidu.beidou.test.common.BaseTestCase.java
 * 下午2:58:26 created by Darwin(Tianxin)
 */
package com.baidu.beidou.test.common;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.unbiz.daounit.util.DbUnitSchemaHelper;

/**
 * 测试广告库的多数据源的testcase的基类
 * @author Darwin(Tianxin)
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:applicationContext-extend-test.xml" })
@TransactionConfiguration(transactionManager="addbTransactionManager")
public abstract class AbstractShardAddbTestCaseLegacy  extends AbstractTransactionalJUnit4SpringContextTests {
	
	//系统初始化操作在第0个切片上
	static {
	    System.setProperty("running.mode", "h2.test");
		ThreadContext.putUserId(1);
	}
	
	@Autowired       
	@Qualifier("addbMultiDataSource")       
	DataSource dataSource;
	
	@Resource
	protected UserMgr userMgr;
	
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }
	
	@BeforeTransaction
	public void go2Shard(){
		int shard = getShard();
		if(shard < 8){
			shard = (shard << 6) + 2;
		}
		ThreadContext.putUserId(shard);
	}
	
	@AfterTransaction
	public void clearShard(){
		ThreadContext.putUserId(null);
	}

	/**
	 * 本case在哪个切片上面跑。
	 * @return	返回值小于8，认为是切片号，否则认为是userId
	 * 下午3:07:08 created by Darwin(Tianxin)
	 */
	public abstract int getShard();
	
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	   /**
     * load sql file to h2 db
     * 
     * @param scriptFiles
     */
    protected void prepareDataFromClasspathScript(String[] scriptFiles) {
        if (scriptFiles == null || scriptFiles.length == 0) {
            return;
        }

        for (String script : scriptFiles) {
            DbUnitSchemaHelper.executeSqlScript(simpleJdbcTemplate, "classpath:" + script);
        }
    }
	
}
