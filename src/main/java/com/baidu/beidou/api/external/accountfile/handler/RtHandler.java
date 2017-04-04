package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.List;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;

/**
 * 
 * ClassName: RtHandler <br>
 * Function: 回头客点击定向搜索推广关联文件输出VO的handler
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Mar 31, 2012
 */
@Deprecated
public class RtHandler extends Handler {

	//private static final Log log = LogFactory.getLog(RtHandler.class);

	/**
	 * 生成回头客点击定向搜索推广关联VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		return new ArrayList<AbstractVo>();
	}

}
