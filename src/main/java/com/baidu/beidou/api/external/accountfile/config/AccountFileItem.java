package com.baidu.beidou.api.external.accountfile.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.accountfile.handler.Handler;
import com.baidu.beidou.api.external.accountfile.output.CSVWriter;
import com.baidu.beidou.exception.InternalException;

/**
 * 
 * InterfaceName: AccountFileItem  <br>
 * Function: 账户信息结构处理项
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 29, 2012
 */
public abstract class AccountFileItem {
	
	private static final Log LOG = LogFactory.getLog(AccountFileItem.class);

	/**
	 * 获取item id
	 */
	protected abstract int getId();

	/**
	 * 获取item名称
	 */
	protected abstract String getName();

	/**
	 * 获取item生成的文件名
	 */
	protected abstract String getFilename();

	/**
	 * 获取处理item返回对应List<VO>的处理器
	 */
	protected abstract Handler getHandler();

	/**
	 * 获取输出文件的表头
	 */
	protected abstract String[] getHeader();
	
	/**
	 * 根据前端传递过来的userId，planIds来生成对应item的数据文件
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @param tmpPath 临时文件保存路径
	 * @return 
	 */
	public void handle(int userId, List<Integer> planIds, List<Integer> groupIds, String tmpPath) throws InternalException, IOException {
		StringBuffer planSbs = new StringBuffer();
		for(Integer i: planIds){
			planSbs.append(i);
			planSbs.append(",");
		}
		StringBuffer groupSbs = new StringBuffer();
		for(Integer i: groupIds){
			groupSbs.append(i);
			groupSbs.append(",");
		}
		LOG.debug("Start to get vo. itemId=[" + this.getId() + 
				"]. itemName=[" + this.getName() + 
				"]. userId=[" + userId + 
				"]. tmpPath=[" + tmpPath);
		List<String[]> details = this.getHandler().getDetails(userId, planIds, groupIds);
		String filePath = tmpPath + File.separator + this.getFilename();
		//LOG.debug("Start to output to csv file " + filePath);
		CSVWriter.getInstance().write(details, this.getHeader(), filePath);
	}
	
}
