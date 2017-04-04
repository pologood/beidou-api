package com.baidu.beidou.api.external.accountfile.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.accountfile.constant.ApiAccountFileTaskConstant;
import com.baidu.beidou.api.external.accountfile.facade.AccountFileOutputFacade;
import com.baidu.beidou.api.external.accountfile.service.AccountFileMgr;
import com.baidu.beidou.api.external.accountfile.service.ApiAccountFileTaskMgr;
import com.baidu.beidou.api.external.accountfile.util.AccountFileUtil;
import com.baidu.beidou.api.external.util.GZipper;
import com.baidu.beidou.api.external.util.Zipper;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.MD5;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.beidou.util.memcache.BeidouCacheInstance;

/**
 * 
 * ClassName: AccountFileMgr  <br>
 * Function: 处理queue中监听账户信息请求
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class AccountFileMgrImpl implements AccountFileMgr{
	
	private static final Log log = LogFactory.getLog(AccountFileMgrImpl.class);

	private UserMgr userMgr;
	
	private CproPlanMgr cproPlanMgr;
	
	private CproGroupMgr cproGroupMgr;
	
	private ApiAccountFileTaskMgr apiAccountFileTaskMgr;
	
	private AccountFileOutputFacade accountFileOutputFacade;

	/**
	 * 处理jms插入的获取账户信息请求
	 * @param task 账户信息数据任务
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(ApiAccountFileTask task){
		if(task == null){
			log.fatal("task can not be null!");
			return;
		}
		
		long id = task.getId();
		int userid = task.getUserid();
		
		// 异步处理逻辑，需设置userId到ThreadContext里面
		ThreadContext.putUserId(userid);
		
		String planids = task.getPlanids();
		String fileId = task.getFileid();
		int format = task.getFormat();
		try{
			StopWatch sw = new StopWatch();
			sw.start();
			
			// step1. 设置哈希锁，将本次请求的userid-planids做成字符串的key，fileId做成value，放入memcache中，
			// 如果前端再有新的请求过来，查询memcache中是否有在处理的相同请求，有则直接返回fileId，没有则放入queue中，
			// 为了降低负载，同时也提供更好的用户体验。
			// 关于为什么在这里设置哈希锁，而不再exporter请求中，假象严酷的条件下，用户的请求都阻塞在来queue中，同时会有大量的
			// 请求由mgrImpl处理，那么哈希锁就失效了。这种请求由前端的loadControlInterceptor控制，同一个用户同时最NEW/DOING
			// 的请求总数作一个限制，这样即使出现来阻塞情况，也可以在源头节流，不会对系统造成大负载。
			setHashLock(task);
			
			// step2. 更新任务状态为处理中
			apiAccountFileTaskMgr.updateStatusAndHandleTime(userid, fileId, ApiAccountFileTaskConstant.TASK_STATUS_DOING);
			log.info("update status to DOING. task=[" + task + "]");
			
			// step3. 解析出必要的参数
			// 如果DB中存储来planids，说明需要查询某个推广计划的数据，因此从DB读取planids；
			// 如果DB中无存储，表明查询全部非删除状态的推广计划数据
			//log.info("got new task [" + task + "]");
			List<Integer> planIds = new ArrayList<Integer>();
			if(StringUtils.isNotEmpty(planids)){
				String[] planIdsArray = StringUtils.split(planids, AccountFileWebConstants.ACCOUNTFILE_PLANID_SEPERATOR);
				for(String planId: planIdsArray){
					planIds.add(Integer.parseInt(planId));
				}
			} else {
				planIds = cproPlanMgr.findNonDeletedPlanIdsByUserId(userid);
			}
			Set<Integer> planIdSet = new HashSet<Integer>(planIds);
			
			// 先查找非删除的推广组id列表
			List<Integer> nonDelGroupIds = new ArrayList<Integer>();
			nonDelGroupIds = cproGroupMgr.findNonDeletedGroupIdsByUserId(userid);
			Set<Integer> groupIdSet = new HashSet<Integer>(nonDelGroupIds);
			
			// 查找Group list
			List<Integer> groupIds = new ArrayList<Integer>();
			Map<Integer, Integer> groupIdPlanIdMap = cproGroupMgr.findGroupIdPlanIdsByUserId(userid);
			Iterator iter = groupIdPlanIdMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>)iter.next();
				// 如果推广计划在查询范围内，则加入planId到列表中
				if(planIdSet.contains(entry.getValue()) && groupIdSet.contains(entry.getKey())){
					groupIds.add(entry.getKey());
				}
			}
			
			// step4. 检查参数合法性
			checkParameters(task);
			//log.info("check parameters ok. taskid=[" + id + "]");
			
			// step5. 构建临时文件输出路径
			String tmpPath = makeTmpPath(fileId);
			//log.info("make tmp path " + tmpPath + " for taskid=[" + id + "] done");

			// step6. 查询数据库生成账户信息数据文件，也就是各个item
			
			accountFileOutputFacade.output(userid, planIds, groupIds, tmpPath);
			log.info("output to csv done. taskid=[" + id + "]. ");
			
			// step7. 压缩文件，默认为zip格式
			String finalFilePath = "";
			if(format == AccountFileWebConstants.FORMAT_ZIP){
				finalFilePath = AccountFileUtil.getSavedFilePath(task.getAddtime(), fileId, AccountFileWebConstants.ACCOUNTFILE_ZIP_SUFFIX);
				Zipper zipper = new Zipper();
				zipper.zipDir(finalFilePath, tmpPath);
				//log.info("zip done. taskid=[" + id + "]");
			} else {
				String gzipTmpPath = makeGZipTmpPath(fileId);
				finalFilePath = AccountFileUtil.getSavedFilePath(task.getAddtime(), fileId,  AccountFileWebConstants.ACCOUNTFILE_GZIP_SUFFIX);
				GZipper gzipper = new GZipper();
				gzipper.gzipDir(finalFilePath, gzipTmpPath, tmpPath);
				//log.info("gzip done. taskid=[" + id + "]");
			}
			
			// step8. 将文件做一个MD5值
			String finalFileMd5 = MD5.getFileMD5(new File(finalFilePath));
			
			// step9. 更新任务状态为处理成功，并更新md5
			apiAccountFileTaskMgr.updateStatusAndMd5(userid, fileId, ApiAccountFileTaskConstant.TASK_STATUS_OK, finalFileMd5);
			log.info("update status to OK. taskid=[" + id + "]");
			
			sw.stop();
			log.info("process task [" + task + "] done. Totallly using " + sw.getTime() + " milliseconds.");
			
		} catch (IllegalArgumentException e){
			log.error("fail to process task=[" + task + "]" + e.getMessage(),e); 
			try{
				apiAccountFileTaskMgr.updateStatus(userid, fileId, ApiAccountFileTaskConstant.TASK_STATUS_FAIL);
			} catch (Exception ex){
				log.error(ex.getMessage(),ex); 
			}
		} catch (InternalException e){
			log.error("fail to process task=[" + task + "]" + e.getMessage(),e); 
			try{
				apiAccountFileTaskMgr.updateStatus(userid, fileId, ApiAccountFileTaskConstant.TASK_STATUS_FAIL);
			} catch (Exception ex){
				log.error(ex.getMessage(),ex); 
			}
		} catch (IOException e){
			log.error("fail to process task=[" + task + "]" + e.getMessage(),e); 
			try{
				apiAccountFileTaskMgr.updateStatus(userid, fileId, ApiAccountFileTaskConstant.TASK_STATUS_FAIL);
			} catch (Exception ex){
				log.error(ex.getMessage(),ex); 
			}
		} catch (Exception e){
			log.error("fail to process task=[" + task + "]" + e.getMessage(),e); 
			try{
				apiAccountFileTaskMgr.updateStatus(userid, fileId, ApiAccountFileTaskConstant.TASK_STATUS_FAIL);
			} catch (Exception ex){
				log.error(ex.getMessage(),ex); 
			}
		} finally{
			removeHashLock(task);
			log.info("deal task done. taskid=[" + task.getId() + "]");
		}

	}
	
	/**
	 * 校验参数
	 */
	private void checkParameters(ApiAccountFileTask task) throws IllegalArgumentException{
		try{
			if(task.getUserid() < 1){
				throw new IllegalArgumentException("userId invalid");
			}
			String planids = task.getPlanids();
			if(StringUtils.isNotEmpty(planids)){
				String[] planIdsArray = StringUtils.split(planids, AccountFileWebConstants.ACCOUNTFILE_PLANID_SEPERATOR);
				for(String planId: planIdsArray){
					Integer.parseInt(planId);
				}
			} 
		}catch(NumberFormatException e){
			log.error("invalid arguments found " + task + "]");
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 此方法验证如果开启了哈希锁，并且任务已处理，则缓存结果fileId到memcache中
	 * @param task
	 * @return
	 */
	protected void setHashLock(ApiAccountFileTask task) {
		if(AccountFileWebConstants.ENABLE_HASH_LOCK){
			try{
				long id = task.getId();
				String keyStr = getTaskHashStr(task);
				String keyMd5 = MD5.getMd5(keyStr);
				String value = task.getFileid();
				BeidouCacheInstance.getInstance().memcacheSet(keyMd5, value, AccountFileWebConstants.EXPIRE_TIME_OF_HASH_LOCK);
				log.info("set hash lock for taskid=[" + id + "], queryParam=[" + keyStr + "], md5=[" + keyMd5  + "] fileId=[" + value + "]");
			} catch (Exception e){
				log.error("fail set hash cache for task=[" + task + "]" + e.getMessage(),e); 
			} 
		}
	}
	
	/**
	 * 此方法验证如果开启了哈希锁，并且任务已处理完毕，则将memcache中的哈希锁清除
	 * @param task
	 * @return
	 */
	protected void removeHashLock(ApiAccountFileTask task) {
		if(AccountFileWebConstants.ENABLE_HASH_LOCK){
			try{
				long id = task.getId();
				String keyStr = getTaskHashStr(task);
				String keyMd5 = MD5.getMd5(keyStr);
				String value = task.getFileid();
				BeidouCacheInstance.getInstance().memcacheRandomDelete(keyMd5);
				log.info("remove hash lock for taskid=[" + id + "], queryParam=[" + keyStr + "], md5=[" + keyMd5  + "] fileId=[" + value + "]");
			} catch (Exception e){
				log.error("fail remove hash lock for task=[" + task + "]" + e.getMessage(),e); 
			} 
		}
	}
	
	/**
	 * 根据task获取其放在memcache中的原始key，未作md5
	 * @param task
	 * @return String 
	 */
	private String getTaskHashStr(ApiAccountFileTask task){
		int userId = task.getUserid();
		String planIds = task.getPlanids();
		int format = task.getFormat();
		// memcache key = "userId-planIds-format"
		String keyStr = Integer.toString(userId) + AccountFileWebConstants.ACCOUNTFILE_HASH_CACHE_KEY_SEPERATOR + planIds + AccountFileWebConstants.ACCOUNTFILE_HASH_CACHE_KEY_SEPERATOR + Integer.toString(format);
		return keyStr;
	}
	
	public CproPlanMgr getCproPlanMgr() {
		return cproPlanMgr;
	}

	public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
		this.cproPlanMgr = cproPlanMgr;
	}

	public UserMgr getUserMgr() {
		return userMgr;
	}

	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}
	

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	private String makeTmpPath(String fileId) throws IOException{
		String tmpPath = AccountFileWebConstants.TMP_SAVE_PATH + File.separator + fileId;
		File f = new File(tmpPath);
		f.mkdir();
		return tmpPath;
	}
	
	private String makeGZipTmpPath(String fileId) throws IOException{
		String path = AccountFileWebConstants.TMP_SAVE_PATH + File.separator + fileId + AccountFileWebConstants.ACCOUNTFILE_TAR_SUFFIX;
		return path;
	}
	
	public ApiAccountFileTaskMgr getApiAccountFileTaskMgr() {
		return apiAccountFileTaskMgr;
	}

	public void setApiAccountFileTaskMgr(ApiAccountFileTaskMgr apiAccountFileTaskMgr) {
		this.apiAccountFileTaskMgr = apiAccountFileTaskMgr;
	}

	public AccountFileOutputFacade getAccountFileOutputFacade() {
		return accountFileOutputFacade;
	}

	public void setAccountFileOutputFacade(
			AccountFileOutputFacade accountFileOutputFacade) {
		this.accountFileOutputFacade = accountFileOutputFacade;
	}
	

	
}
