package com.baidu.beidou.api.external.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.baidu.beidou.tool.constant.OptHistoryConstant;
import com.baidu.beidou.tool.vo.OpTypeVo;

/**
 * 
 * ClassName: OptHistoryTest  <br>
 * Function: 打印历史操作记录项目
 *
 * @author zhangxu
 * @date Jun 8, 2012
 */
@Ignore
@ContextConfiguration(locations = { "classpath:applicationContext-core.xml" })
public class OptHistoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
		super.setDataSource(dataSource);
	}
	
	@Test
    public void testOutput() {
        Map<Integer, OpTypeVo> map = OptHistoryConstant.opType2Vo;
        List<OpTypeVo> list = new ArrayList<OpTypeVo>(map.values());
        Collections.sort(list, new Comparator<OpTypeVo>() {
            public int compare(OpTypeVo o1, OpTypeVo o2) {
                if (o1.getOpLevel() < o2.getOpLevel()) {
                    return -1;
                } else if (o1.getOpLevel() == o2.getOpLevel()) {
                    if (o1.getOpType() < o2.getOpType()) {
                        return -1;
                    } else if (o1.getOpType() == o2.getOpType()) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        });
        for (OpTypeVo vo : list) {
            System.out.println(
            		vo.getOpType() + "\t" + 
            		vo.getOpName() + "\t" + 
            		vo.getOpLevel() + "\t" + 
            		vo.getTypeName());
        }
    }
	
}
