package com.baidu.beidou.api.external.util.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.baidu.beidou.api.external.util.dao.ApiBlacklistDao;
import com.baidu.beidou.util.dao.GenericDaoImpl;
import com.baidu.beidou.util.dao.GenericRowMapping;

/**
 * 
 * ClassName: ApiBlacklistDaoImpl  <br>
 * Function: API黑名单DAO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 19, 2012
 */
public class ApiBlacklistDaoImpl extends GenericDaoImpl<Object, Integer> implements ApiBlacklistDao {

	/**
	 * 获取所有黑名单用户id
	 * @return List<Integer>
	 */
	public List<Integer> getAllBlacklistUserids(){
		String sql = "select userid from beidoucap.api_blacklist";
        return (List<Integer>)super.findBySql(new GenericRowMapping<Integer>() {
            public Integer mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getInt(1);
            }
        
        }, sql, new Object[] {}, new int[] {});
	}
}
