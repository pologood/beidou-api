/**
 * beidou-core-535#com.baidu.beidou.test.common.BaseTestCase.java
 * 下午2:58:26 created by Darwin(Tianxin)
 */
package com.baidu.beidou.test.common;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baidu.beidou.util.ThreadContext;
import com.baidu.unbiz.daounit.util.DbUnitSchemaHelper;

/**
 * 测试广告库的多数据源的testcase的基类
 * @author Darwin(Tianxin)
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:applicationContext-extend-test.xml" })
@TransactionConfiguration(transactionManager = "xdbTransactionManager")
public abstract class AbstractShardXdbTestCaseLegacy extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    protected JdbcTemplate xdbJdbcTemplate;
    
	//系统初始化操作在第0个切片上
	static {
		ThreadContext.putUserId(1);
	}

	@Autowired
	@Qualifier("xdbMultiDataSource")
	DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	protected void prepareDataFromClasspathScript(String[] scriptFiles) {
        if (scriptFiles == null || scriptFiles.length == 0) {
            return;
        }

        for (String script : scriptFiles) {
            DbUnitSchemaHelper.executeSqlScript(xdbJdbcTemplate, "classpath:" + script);
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
