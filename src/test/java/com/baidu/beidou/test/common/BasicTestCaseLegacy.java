package com.baidu.beidou.test.common;

import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baidu.beidou.util.ThreadContext;
import com.baidu.unbiz.daounit.util.DbUnitSchemaHelper;
import com.baidu.unbiz.daounit.util.MemcachedStub;
import com.baidu.unbiz.daounit.util.MemcachedStubStart;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:applicationContext-extend-test.xml" })
@TransactionConfiguration(transactionManager = "addbTransactionManager")
public abstract class BasicTestCaseLegacy extends AbstractTransactionalJUnit4SpringContextTests {
    private static MemcachedStub mcStub;
    
	static {
		ThreadContext.putUserId(8);
	}


	/**
	 * 注入数据源
	 * @param dataSource dataSource
	 */
    @Autowired
    public void setDataSource(@Qualifier("addbMultiDataSource") DataSource dataSource) {
        super.setDataSource(dataSource);
    }
    
    /**
     * 为测试类准备memcached stub
     */
    protected void prepareMemcachedStub() {
        if (mcStub == null) {
            mcStub = MemcachedStubStart.start(this.getClass());
        }
    }

    /**
     * 释放memcached stub
     */
    protected void releaseMemcachedStub() {
        if (mcStub != null) {
            mcStub.release();
        }
    }
    
	/**
	 * load sql file to h2 db
	 * @param scriptFiles scriptFiles
	 */
    protected void prepareDataFromClasspathScript(String[] scriptFiles) {
        if (scriptFiles == null || scriptFiles.length == 0) {
            return;
        }

        for (String script : scriptFiles) {
            DbUnitSchemaHelper.executeSqlScript(simpleJdbcTemplate, "classpath:" + script);
        }
    }
    
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
