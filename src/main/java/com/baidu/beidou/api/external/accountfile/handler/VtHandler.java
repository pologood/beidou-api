package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.VtVo;
import com.baidu.beidou.cprogroup.bo.CproGroupVT;
import com.baidu.beidou.cprogroup.service.CproGroupVTMgr;
import com.baidu.beidou.util.BeanMapperProxy;

/**
 * 
 * ClassName: VtHandler <br>
 * Function: 回头客到访定向人群输出VO的handler
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class VtHandler extends Handler {

	//private static final Log log = LogFactory.getLog(VtHandler.class);

	private CproGroupVTMgr cproGroupVTMgr;

	/**
	 * 生成回头客到访定向人群VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		List<CproGroupVT> vtList = new ArrayList<CproGroupVT>();

		for (Integer groupId : groupIds) {
			List<CproGroupVT> vt = cproGroupVTMgr
					.findCompleteVTRelationByGroup(groupId, userId);
			vtList.addAll(vt);
		}

		if (vtList != null) {
			Mapper mapper = BeanMapperProxy.getMapper();
			for (CproGroupVT vt : vtList) {
				VtVo vtVo = mapper.map(vt, VtVo.class);
				list.add(vtVo);
			}
		}


		return list;
	}

	public CproGroupVTMgr getCproGroupVTMgr() {
		return cproGroupVTMgr;
	}

	public void setCproGroupVTMgr(CproGroupVTMgr cproGroupVTMgr) {
		this.cproGroupVTMgr = cproGroupVTMgr;
	}

}
