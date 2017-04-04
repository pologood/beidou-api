package com.baidu.beidou.api.external.report.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.constant.ApiReportTaskConstant;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.exception.JsonConverterException;
import com.baidu.beidou.api.external.report.output.impl.ApiReportCSVWriter;
import com.baidu.beidou.api.external.report.output.impl.Zipper;
import com.baidu.beidou.api.external.report.service.ApiReportMgr;
import com.baidu.beidou.api.external.report.service.ApiReportTaskMgr;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.util.JsonConverter;
import com.baidu.beidou.api.external.report.vo.AbstractApiReportCsvVo;
import com.baidu.beidou.api.external.report.vo.AbstractStatViewItem;
import com.baidu.beidou.api.external.report.vo.AccountReportCsvVo;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.AppReportCsvVo;
import com.baidu.beidou.api.external.report.vo.AttachConsultReportCsvVo;
import com.baidu.beidou.api.external.report.vo.AttachMessageReportCsvVo;
import com.baidu.beidou.api.external.report.vo.AttachPhoneReportCsvVo;
import com.baidu.beidou.api.external.report.vo.AttachSubUrlReportCsvVo;
import com.baidu.beidou.api.external.report.vo.ChosenTradeReportCsvVo;
import com.baidu.beidou.api.external.report.vo.DeviceReportCsvVo;
import com.baidu.beidou.api.external.report.vo.GenderReportCsvVo;
import com.baidu.beidou.api.external.report.vo.GroupReportCsvVo;
import com.baidu.beidou.api.external.report.vo.InterestShownReportCsvVo;
import com.baidu.beidou.api.external.report.vo.KeywordReportCsvVo;
import com.baidu.beidou.api.external.report.vo.PackKeywordReportCsvVo;
import com.baidu.beidou.api.external.report.vo.PackReportCsvVo;
import com.baidu.beidou.api.external.report.vo.PlanReportCsvVo;
import com.baidu.beidou.api.external.report.vo.RegionReportCsvVo;
import com.baidu.beidou.api.external.report.vo.ShownSiteReportCsvVo;
import com.baidu.beidou.api.external.report.vo.UnitReportCsvVo;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.MD5;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.beidou.util.memcache.BeidouCacheInstance;

/**
 * 
 * ClassName: ApiReportMgrImpl  <br>
 * Function: 处理报告任务的核心服务类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public abstract class ApiReportMgrImpl implements ApiReportMgr{
	
	private static final Log log = LogFactory.getLog(ApiReportMgrImpl.class);
	
	private ApiReportTaskMgr apiReportTaskMgr;
	
	/**
	 * 处理报告任务
	 * @version 2.0.0
	 * @author zhangxu
	 * @date Jan 7, 2012
	 */
	public void process(ApiReportTask task){
		long id;
		String reportid;
		int reportType;
		boolean isZip;
		int performanceData;
		
		// 异步处理逻辑，需设置userId到ThreadContext里面
		Integer userId = task.getUserid();
		ThreadContext.putUserId(userId);
		
		try{
			// step1. 解析出query param
			log.info("got new task [" + task + "]");
			ApiReportQueryParameter request = JsonConverter.fromJson(task.getQueryparam());
			log.info("serialize query param ok. param=[" + request + "]");
			
			// step2. 检查参数合法性
			checkParameters(task, request);
			id = task.getId();
			reportid = task.getReportid();
			reportType = request.getReportType();
			isZip = (task.getIszip() == ReportWebConstants.FORMAT_ZIP) ? true : false;
			performanceData = task.getPerformancedata();
			//log.info("check parameters ok. taskid=[" + id + "]");
			
			// step3. 更新任务状态为处理中
			apiReportTaskMgr.updateTaskStatusDoing(task);
			log.info("update status to DOING. taskid=[" + id + "]");

			// step4. 查询DB和olap得出统计数据
			//排查线上问题，调试代码
			GenericStatService<AbstractStatViewItem> statService = null;
			List<AbstractStatViewItem> response = null;
			StopWatch sw = new StopWatch();
			sw.start();
			statService = getStatService(reportType);
			response = statService.queryStat(request);
			sw.stop();
			
			//调试代码结束
			log.info("query olap and merge with db done. taskid=[" + id + "]. using " + sw.getTime() + " milliseconds.");
			if(log.isDebugEnabled()){
				for(AbstractStatViewItem item: response){
					log.debug(item);
				}
			}
			
			// step5. 写入文件
			String srcFile = ReportWebConstants.DOWNLOAD_FILE_SAVE_PATH + File.separator + reportid;
			String zipFile = ReportWebConstants.DOWNLOAD_FILE_SAVE_PATH + File.separator + reportid + ReportWebConstants.REPORT_ZIP_SUFFIX;
			AbstractApiReportCsvVo vo = getReportCsvVo(request, response, performanceData);
			ApiReportCSVWriter.getInstance().write(vo, srcFile);
			log.info("output to csv done. taskid=[" + id + "]");
			
			// step6. 如果需要则压缩文件
			if(isZip){
				Zipper zipper = new Zipper();
				zipper.zipFile(zipFile, srcFile);
				//log.info("zip done. taskid=[" + id + "]");
			}
			
			// 设置为null，释放内存空间
			statService = null;
			
			// step7. 更新任务状态为处理成功
			apiReportTaskMgr.updateTaskStatusOk(task);
			log.info("update status to OK. taskid=[" + id + "]");
			
			// step8. 如果任务状态为成功，如果开启来哈希缓存就将reportId缓存在memcache中
			postSetHashCache(task);
			
		} 
//		catch (StorageServiceException e) {
//			log.error("fail to process task1=[" + task + "]" + e.getMessage(),e); 
//			try{
//				apiReportTaskMgr.updateTaskStatusQueryTooLargeFail(task);
//			} catch (InternalException ex){
//				log.error(e.getMessage(),e); 
//			}
//		} 
		catch (JsonConverterException e){
			log.error("fail to process task2=[" + task + "]" + e.getMessage(),e); 
			try{
				apiReportTaskMgr.updateTaskStatusFail(task);
			} catch (InternalException ex){
				log.error(e.getMessage(),e); 
			}
		} catch (IllegalArgumentException e){
			log.error("fail to process task3=[" + task + "]" + e.getMessage(),e); 
			try{
				apiReportTaskMgr.updateTaskStatusFail(task);
			} catch (InternalException ex){
				log.error(e.getMessage(),e); 
			}
		} catch (InternalException e){
			log.error("fail to process task4=[" + task + "]" + e.getMessage(),e); 
			try{
				apiReportTaskMgr.updateTaskStatusFailIncrementRetry(task);
			} catch (InternalException ex){
				log.error(e.getMessage(),e); 
			}
		} catch (IOException e){
			log.error("fail to process task5=[" + task + "]" + e.getMessage(),e); 
			try{
				apiReportTaskMgr.updateTaskStatusFailIncrementRetry(task);
			} catch (InternalException ex){
				log.error(e.getMessage(),e); 
			}
		} catch (Exception e){
			log.error("fail to process task6=[" + task + "]" + e,e); 
			try{
				apiReportTaskMgr.updateTaskStatusFailIncrementRetry(task);
			} catch (InternalException ex){
				log.error(e.getMessage(),e); 
			}
		} finally{
			log.info("deal task done [" + task + "]");
		}
	}
	
	/**
	 * 此方法验证如果开启了哈希缓存，并且任务状态为成功，则缓存结果reportId到memcache中
	 * @param task
	 * @return
	 */
	protected void postSetHashCache(ApiReportTask task) {
		if(ReportWebConstants.ENABLE_HASH_CACHE && task.getStatus() == ApiReportTaskConstant.TASK_STATUS_OK){
			try{
				long id = task.getId();
				int performanceData = task.getPerformancedata(); // performanceData与queryParam与isZip结合作为key
				int isZip = task.getIszip();
				String queryParamStr = task.getQueryparam();
				String key = MD5.getMd5(queryParamStr + performanceData + isZip);
				String value = task.getReportid();
				BeidouCacheInstance.getInstance().memcacheSet(key, value, ReportWebConstants.EXPIRE_TIME_OF_HASH_CACHE);
				log.info("set hash cache for taskid=[" + id + "], queryparamMD5=[" + key + "], reportid=[" + value + "]");
			} catch (Exception e){
				log.error("fail set hash cache for task=[" + task + "]" + e.getMessage(),e); 
			} 
		}
	}
	
	
	/**
	 * 返回填充了数据的准备写入CSV文件的VO
	 */
	public AbstractApiReportCsvVo getReportCsvVo(ApiReportQueryParameter request, List response, int performanceData){
		int reportType = request.getReportType();
		boolean idOnly = request.isIdOnly();
		AbstractApiReportCsvVo vo = null;
		switch(reportType){
		case ReportWebConstants.REPORT_TYPE.ACCOUNT:
			vo = new AccountReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.PLAN:
			vo = new PlanReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.GROUP:
			vo = new GroupReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.UNIT:
			vo = new UnitReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL:      //原CT报告，返回3.0之后的Keyword报告格式
			vo = new KeywordReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL_COMPATIBLE:      //原QT报告，返回3.0之后的Keyword报告格式
			vo = new KeywordReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.SITE_SHOWN:
			vo = new ShownSiteReportCsvVo(idOnly, performanceData); 
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.INTEREST_SHOWN:
			vo = new InterestShownReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.PACK:
			vo = new PackReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.KEYWORD_PACK:
			vo = new PackKeywordReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.REGION:
			vo = new RegionReportCsvVo(idOnly, performanceData); 
			vo.setDetails(response); 
			break;
		case ReportWebConstants.REPORT_TYPE.GENDER:
			vo = new GenderReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.INTEREST_CHOSEN:
			vo = new InterestShownReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.SITE_CHOSEN:
			vo = new ShownSiteReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.TRADE_CHOSEN:
			vo = new ChosenTradeReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.APP:
			vo = new AppReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
		case ReportWebConstants.REPORT_TYPE.DEVICE:
			vo = new DeviceReportCsvVo(idOnly, performanceData);
			vo.setDetails(response);
			break;
			case ReportWebConstants.REPORT_TYPE.ATTACH_PHONE:
				vo = new AttachPhoneReportCsvVo(idOnly, performanceData);
				vo.setDetails(response);
				break;
			case ReportWebConstants.REPORT_TYPE.ATTACH_MESSAGE:
				vo = new AttachMessageReportCsvVo(idOnly, performanceData);
				vo.setDetails(response);
				break;
			case ReportWebConstants.REPORT_TYPE.ATTACH_CONSULT:
				vo = new AttachConsultReportCsvVo(idOnly, performanceData);
				vo.setDetails(response);
				break;
			case ReportWebConstants.REPORT_TYPE.ATTACH_SUB_URL:
				vo = new AttachSubUrlReportCsvVo(idOnly, performanceData);
				vo.setDetails(response);
				break;
		default:
			throw new IllegalArgumentException("can not build stat vo for reportType=[" + reportType + "]");
		}
		return vo;
	}
	
	/**
	 * 根据报告类型利用多态获取对应的查询服务类
	 * @param reportType
	 * @return GenericStatService
	 */
	protected GenericStatService getStatService(int reportType) throws InternalException{
		switch(reportType){
		case ReportWebConstants.REPORT_TYPE.ACCOUNT:
			return getAccountStatService();
		case ReportWebConstants.REPORT_TYPE.PLAN:
			return getPlanStatService();
		case ReportWebConstants.REPORT_TYPE.GROUP:
			return getGroupStatService();
		case ReportWebConstants.REPORT_TYPE.UNIT:
			return getUnitStatService();
		case ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL:   //原CT报告查询3.0之后统一的Keyword报告
			return getGroupKeywordStatService();
		case ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL_COMPATIBLE:   //原QT报告查询3.0之后统一的Keyword报告
			return getGroupKeywordStatService();
		case ReportWebConstants.REPORT_TYPE.SITE_SHOWN:
			return getShownSiteStatService();
		case ReportWebConstants.REPORT_TYPE.INTEREST_SHOWN:
			return getInterestStatService();
		case ReportWebConstants.REPORT_TYPE.PACK:
			return getPackageStatService(); 
		case ReportWebConstants.REPORT_TYPE.KEYWORD_PACK:
			return getPackKeywordStatService();
		case ReportWebConstants.REPORT_TYPE.REGION:
			return getRegionStatService(); 
		case ReportWebConstants.REPORT_TYPE.GENDER:
			return getGenderStatService(); 
		case ReportWebConstants.REPORT_TYPE.INTEREST_CHOSEN:
			return getChosenInterestStatService();
		case ReportWebConstants.REPORT_TYPE.SITE_CHOSEN:
			return getChosenSiteStatService();
		case ReportWebConstants.REPORT_TYPE.TRADE_CHOSEN:
			return getChosenTradeStatService();
		case ReportWebConstants.REPORT_TYPE.APP:
			return getAppStatService();
		case ReportWebConstants.REPORT_TYPE.DEVICE:
			return getDeviceStatService();
			case ReportWebConstants.REPORT_TYPE.ATTACH_PHONE:
				return getAttachPhoneStatService();
			case ReportWebConstants.REPORT_TYPE.ATTACH_MESSAGE:
				return getAttachMessageStatService();
			case ReportWebConstants.REPORT_TYPE.ATTACH_CONSULT:
				return getAttachConsultStatService();
			case ReportWebConstants.REPORT_TYPE.ATTACH_SUB_URL:
				return getAttachSubUrlStatService();
		}
		throw new InternalException("can not found proper stat service for reportType=[" + reportType + "]");
	}
	
	/*
	 * 通过Lookup方法注入来实现在单例中注入多例的bean
	 */
	public abstract GenericStatService getAccountStatService();
	public abstract GenericStatService getPlanStatService();
	public abstract GenericStatService getGroupStatService();
	public abstract GenericStatService getUnitStatService();
	public abstract GenericStatService getGroupKeywordStatService();
	public abstract GenericStatService getShownSiteStatService();
	public abstract GenericStatService getPackageStatService();
	public abstract GenericStatService getPackKeywordStatService();
	public abstract GenericStatService getRegionStatService();
	public abstract GenericStatService getGenderStatService();
	public abstract GenericStatService getInterestStatService();
	public abstract GenericStatService getChosenInterestStatService();
	public abstract GenericStatService getChosenSiteStatService();
	public abstract GenericStatService getChosenTradeStatService();
	public abstract GenericStatService getDeviceStatService();
	public abstract GenericStatService getAppStatService();
	public abstract GenericStatService getAttachPhoneStatService();
	public abstract GenericStatService getAttachMessageStatService();
	public abstract GenericStatService getAttachConsultStatService();
	public abstract GenericStatService getAttachSubUrlStatService();
	
	
	
	/**
	 * 校验参数
	 */
	public void checkParameters(ApiReportTask view, ApiReportQueryParameter request) throws IllegalArgumentException{
		long id = view.getId();
		String reportid = view.getReportid();
		int reportType = request.getReportType();
		boolean idOnly = request.isIdOnly();
		boolean isZip = (view.getIszip() == ReportWebConstants.FORMAT_ZIP) ? true : false;
		
		// id主键必须大于0
		if(id <= 0l){
			throw new IllegalArgumentException("invalid id value should be greater than 0 ");
		}
		
		// 报告类型值合法
		if(reportType != ReportWebConstants.REPORT_TYPE.ACCOUNT && 
				reportType != ReportWebConstants.REPORT_TYPE.PLAN &&
				reportType != ReportWebConstants.REPORT_TYPE.GROUP &&
				reportType != ReportWebConstants.REPORT_TYPE.UNIT &&
				reportType != ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL &&
				reportType != ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL_COMPATIBLE && 
				reportType != ReportWebConstants.REPORT_TYPE.SITE_SHOWN &&
				reportType != ReportWebConstants.REPORT_TYPE.INTEREST_SHOWN && 
				reportType != ReportWebConstants.REPORT_TYPE.PACK &&
				reportType != ReportWebConstants.REPORT_TYPE.KEYWORD_PACK &&
				reportType != ReportWebConstants.REPORT_TYPE.REGION && 
				reportType != ReportWebConstants.REPORT_TYPE.GENDER&&
				reportType != ReportWebConstants.REPORT_TYPE.INTEREST_CHOSEN && 
				reportType != ReportWebConstants.REPORT_TYPE.SITE_CHOSEN&&
				reportType != ReportWebConstants.REPORT_TYPE.TRADE_CHOSEN &&
				reportType != ReportWebConstants.REPORT_TYPE.APP &&
				reportType != ReportWebConstants.REPORT_TYPE.DEVICE
				&& reportType != ReportWebConstants.REPORT_TYPE.ATTACH_PHONE
				&& reportType != ReportWebConstants.REPORT_TYPE.ATTACH_MESSAGE
				&& reportType != ReportWebConstants.REPORT_TYPE.ATTACH_CONSULT
				&& reportType != ReportWebConstants.REPORT_TYPE.ATTACH_SUB_URL) {
			throw new IllegalArgumentException("invalid reportType value");
		}
		
		// reportid为md5之后到32位字符串
		if(StringUtils.isEmpty(reportid) || reportid.length() != ReportWebConstants.MD5_STRING_LENGTH){
			throw new IllegalArgumentException("invalid reportid value");
		}
		
		// userid大于0
		if(request.getUserid() <= 0){
			throw new IllegalArgumentException("invalid userid value");
		}
		
		// 起始、终止时间不为空
		if(request.getStartDate() == null){
			throw new IllegalArgumentException("null startDate value");
		}
		if(request.getEndDate() == null){
			throw new IllegalArgumentException("null endDate value");
		}
		
		// 如果endDate大于今日，endDate设置为今天，这里是唯一改变来request对象的地方
		Date tody23 = DateUtils.getCurDateFloor().getTime();
		if(request.getEndDate().after(tody23)){
			request.setEndDate(DateUtils.getCurDateCeil().getTime());
		}
		
		// 报告范围为空，则不允许有statIds参数
		if(request.getStatRange() == 0 ){
			if(!CollectionUtils.isEmpty(request.getPlanIds()) ||
					!CollectionUtils.isEmpty(request.getGroupIds()) ||
					!CollectionUtils.isEmpty(request.getUnitIds())){
				throw new IllegalArgumentException("statRange is null but have statIds");
			}
		}
		
		// 报告范围，必须有各种statIds之一
		if(request.getStatRange() == ReportWebConstants.REPORT_RANGE.ACCOUNT ){
			if(!CollectionUtils.isEmpty(request.getPlanIds()) ||
					!CollectionUtils.isEmpty(request.getGroupIds()) ||
					!CollectionUtils.isEmpty(request.getUnitIds())){
				throw new IllegalArgumentException("statRange is account but have other statIds");
			}
		} else if(request.getStatRange() == ReportWebConstants.REPORT_RANGE.PLAN ){
//			if(CollectionUtils.isEmpty(request.getPlanIds())){
//				throw new IllegalArgumentException("statRange is plan but do not have statIds");
//			}
			if(!CollectionUtils.isEmpty(request.getGroupIds()) ||
					!CollectionUtils.isEmpty(request.getUnitIds())){
				throw new IllegalArgumentException("statRange is plan but have other statIds");
			}
		} else if(request.getStatRange() == ReportWebConstants.REPORT_RANGE.GROUP ){
//			if(CollectionUtils.isEmpty(request.getGroupIds())){
//				throw new IllegalArgumentException("statRange is group but do not have statIds");
//			}
			if(!CollectionUtils.isEmpty(request.getPlanIds()) ||
					!CollectionUtils.isEmpty(request.getUnitIds())){
				throw new IllegalArgumentException("statRange is group but have other statIds");
			}
		} else if(request.getStatRange() == ReportWebConstants.REPORT_RANGE.UNIT ){
//			if(CollectionUtils.isEmpty(request.getUnitIds())){
//				throw new IllegalArgumentException("statRange is unit but do not have statIds");
//			}
			if(!CollectionUtils.isEmpty(request.getPlanIds()) ||
					!CollectionUtils.isEmpty(request.getGroupIds())){
				throw new IllegalArgumentException("statRange is unit but have other statIds");
			}
		}
		
		// 统计范围下可以查询该报告
		if(!ReportWebConstants.REPORT_RANGE_ALLOWED_TYPES_MAP.get(request.getStatRange()).contains(reportType)){
			throw new IllegalArgumentException("reportType=[" + reportType + "] cannot be queried under statRange=[" + request.getStatRange() + "]");
		}
	}
	

	protected List<Integer> fromLongToInteger(List<Long> list){
		List<Integer> result = new ArrayList<Integer>();
		if(list == null){
			return result;
		}
		for(Long i: list){
			result.add(i.intValue());
		}
		return result;
	}
	
	public ApiReportTaskMgr getApiReportTaskMgr() {
		return apiReportTaskMgr;
	}

	public void setApiReportTaskMgr(ApiReportTaskMgr apiReportTaskMgr) {
		this.apiReportTaskMgr = apiReportTaskMgr;
	}


}
