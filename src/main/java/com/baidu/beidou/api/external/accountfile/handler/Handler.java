package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.exception.InternalException;


public abstract class Handler {

	private static final Log log = LogFactory.getLog(Handler.class);
	
	/**
	 * 用于账户信息生成文件 <br>
	 * 
	 * 子类必须覆盖.
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return 
	 * 
	 */
	public abstract List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds);
	
	/**
	 * 用于账户信息生成文件
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 */
	public List<String[]> getDetails(int userId, List<Integer> planIds, List<Integer> groupIds) throws InternalException{
		List<String[]> ret = new ArrayList<String[]>();
		List<AbstractVo> voList = new ArrayList<AbstractVo>();
		
		try{
			StopWatch sw = new StopWatch();
			sw.start();
			voList = getVo(userId, planIds, groupIds);
			sw.stop();
			log.info(this.getClass().getName() + " query VO using " + sw.getTime() + " milliseconds");
		} catch (Exception e) {
			log.error(this.getClass().getName() + " process failed. "
					+ e.getMessage(), e);
			throw new InternalException(this.getClass().getName()
					+ " process failed. ");
		}
		
		for(AbstractVo vo: voList){
			ret.add(vo.toStringArray());
		}
		return ret;
	}
	
}
