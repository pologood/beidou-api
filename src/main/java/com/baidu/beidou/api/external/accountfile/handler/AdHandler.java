package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dozer.Mapper;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.AdVo;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.SeniorUnitMgrFilter;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.util.BeanMapperProxy;

/**
 * 
 * ClassName: AdHandler <br>
 * Function: 创意文件输出VO的handler
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class AdHandler extends Handler {

	//private static final Log log = LogFactory.getLog(AdHandler.class);

	private CproUnitMgr cproUnitMgr;

	/**
	 * 生成创意VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		List<UnitInfoView> unitList = new ArrayList<UnitInfoView>();

		SeniorUnitMgrFilter filter = new SeniorUnitMgrFilter();
		filter.setUserid(userId);
		filter.setPlanId(planIds);
		
		List<Long> unitIds = cproUnitMgr.findUnitIdsByPlanIds(userId, planIds);

		unitList = cproUnitMgr.findUnitInfoViewByIdsBatch(userId, unitIds);
		
		Set<Integer> groupIdSet = new HashSet<Integer>(groupIds);

		if (unitList != null) {
			Mapper mapper = BeanMapperProxy.getMapper();
			for (UnitInfoView unitInfo : unitList) {
				//不返回删除状态的创意
				if(unitInfo.getStateView().getViewState() == CproUnitConstant.UNIT_STATE_DELETE){
					continue; 
				}
				
				//不返回删除状态的推广组下面的创意
				if(!groupIdSet.contains(unitInfo.getGroupid().intValue())){
					continue; 
				}
				AdVo adVo = mapper.map(unitInfo, AdVo.class);
				list.add(adVo);
			}
		}

		return list;
	}

	public CproUnitMgr getCproUnitMgr() {
		return cproUnitMgr;
	}

	public void setCproUnitMgr(CproUnitMgr cproUnitMgr) {
		this.cproUnitMgr = cproUnitMgr;
	}

}