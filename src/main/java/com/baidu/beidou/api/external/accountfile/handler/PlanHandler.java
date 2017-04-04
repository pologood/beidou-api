package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dozer.Mapper;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.PlanVo;
import com.baidu.beidou.api.external.accountfile.vo.ScheduleType;
import com.baidu.beidou.api.external.cproplan2.util.DeviceOsUtil;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.cproplan.vo.PlanOffTimeVo;
import com.baidu.beidou.stat.util.DateUtil;
import com.baidu.beidou.util.BeanMapperProxy;
import com.baidu.beidou.util.CollectionsUtil;

/**
 * 
 * ClassName: PlanHandler <br>
 * Function: 推广计划文件输出VO的handler
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class PlanHandler extends Handler {

	//private static final Log log = LogFactory.getLog(PlanHandler.class);

	private CproPlanMgr cproPlanMgr = null;

	/**
	 * 生成推广计划VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		List<CproPlan> cproPlanList = new ArrayList<CproPlan>();
		cproPlanList = cproPlanMgr.findCproPlanByUserId(userId);
		//cproPlanList = cproPlanMgr.findCproPlanByPlanIds(planIds);
		
		Set<Integer> planIdSet = new HashSet<Integer>(planIds);
		
		List<PlanOffTimeVo> offtimePlans = cproPlanMgr.findCproPlanOfftime(planIds);
		Map<Integer, Date> offPlanMap = new HashMap<Integer, Date>();
		for(PlanOffTimeVo vo: offtimePlans){
			offPlanMap.put(vo.getPlanId(), vo.getOfftime());
		}
		
		Date today = new Date();
		Mapper mapper = BeanMapperProxy.getMapper();
		if (cproPlanList != null) {
			for (CproPlan cproPlan : cproPlanList) {
				
				//临时方案过滤atleft计划 add by caichao
				if (cproPlan.getPlanType() == 2) {
					continue;
				}

				// 过滤已删除的推广计划
				//if(cproPlan.getPlanState() == CproPlanConstant.PLAN_STATE_DELETE){
				//	continue;
				//}
				if(!planIdSet.contains(cproPlan.getPlanId())){
					continue;
				}
				
				PlanVo planVo = mapper.map(cproPlan, PlanVo.class);
				// 处理日程
				List<ScheduleType> scheduleList = new ArrayList<ScheduleType>();

				decSchedual(scheduleList, cproPlan.getMondayScheme(), 0);
				decSchedual(scheduleList, cproPlan.getTuesdayScheme(), 1);
				decSchedual(scheduleList, cproPlan.getWednesdayScheme(), 2);
				decSchedual(scheduleList, cproPlan.getThursdayScheme(), 3);
				decSchedual(scheduleList, cproPlan.getFridayScheme(), 4);
				decSchedual(scheduleList, cproPlan.getSaturdayScheme(), 5);
				decSchedual(scheduleList, cproPlan.getSundayScheme(), 6);

				ScheduleType[] scheduleArray = new ScheduleType[scheduleList
						.size()];
				for (int i = 0; i < scheduleList.size(); i++) {
					scheduleArray[i] = scheduleList.get(i);
				}
				planVo.setSchedule(scheduleArray);
				
				long deviceOperaSystem = cproPlan.getDeviceOperaSystem();
				//plan.setIsDeviceEnabled(DeviceOsUtil.isAllDevice(deviceOperaSystem) == PlanConstant.ALL_DEVICE ? false : true);
				//plan.setIsOsEnabled(DeviceOsUtil.isAllOs(deviceOperaSystem) == PlanConstant.ALL_OS ? false : true);
				planVo.setDevice(new Long(DeviceOsUtil.getDeviceValue(deviceOperaSystem)).intValue());
				planVo.setOs(CollectionsUtil.tranformLongListToLongArray(DeviceOsUtil.getOSValue(deviceOperaSystem)));
				
				// 加入推广计划下线时间
				if(offPlanMap.containsKey(cproPlan.getPlanId())){
					if (DateUtil.isSameDay(today, offPlanMap.get(cproPlan.getPlanId()))) {
						planVo.setStatus(CproPlanConstant.PLAN_VIEW_STATE_OFFLINE);
					}
				}
				
				//add by wangxiongjie for atright
				planVo.setPlanType(cproPlan.getPlanType());
				
				list.add(planVo);
			}
		}

		return list;
	}
	
	private void decSchedual(List<ScheduleType> scheduleList, Integer scheme,
			Integer dayofWeek) {
		if (scheme != null && scheme != 0) {
			int j = 0;
			ScheduleType schedule = null;
			for (int i = scheme; i > 0; i = i / 2) {
				if (i % 2 == 1) {
					if (schedule == null) {
						schedule = new ScheduleType();
						schedule.setWeekDay(dayofWeek + 1);
						schedule.setStartTime(j);
						scheduleList.add(schedule);
					}
					schedule.setEndTime(j + 1);
				} else {
					schedule = null;
				}
				j++;
			}
		}
	}

	public CproPlanMgr getCproPlanMgr() {
		return cproPlanMgr;
	}

	public void setCproPlanMgr(CproPlanMgr cproPlanMgr) {
		this.cproPlanMgr = cproPlanMgr;
	}

}
