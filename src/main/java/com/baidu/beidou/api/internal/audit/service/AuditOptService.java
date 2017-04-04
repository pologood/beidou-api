package com.baidu.beidou.api.internal.audit.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baidu.beidou.api.internal.audit.vo.AuditOpt;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.bo.Visitor;

public interface AuditOptService {
	
	public Unit auditPass(Integer userId, Long unitId, Integer tradeId, Integer tradeModified,
			Integer accuracyType, Integer beautyType, Integer vulgarType, Integer cheatType, 
			Integer dangerType, List<OptContent> optContents);
	
	public Unit reauditPass(Integer userId, Long unitId, List<OptContent> optContents);
	
	public Unit auditRefuse(Integer userId, Long unitId, Integer tradeId, Integer tradeModified,
			Integer accuracyType, Integer beautyType, Integer vulgarType, Integer cheatType,
			Integer dangerType, List<Integer> refuseReasonIds, List<OptContent> optContents);
	
	public Unit reauditRefuse(Integer userId, Long unitId, 
			List<Integer> refuseReasonIds, List<OptContent> optContents);
	
	public List<AuditOpt> generateAuditOpt(List<Unit> unit, Visitor visitor, boolean isRefused);
	
	public void modifyTag(Integer userId, Long unitId, Integer tagType, Integer tagValue);
	
	public void modifyTrade(Integer userId, Long unitId, Integer tradeId, 
			List<OptContent> optContents);
	/**
	 * 更新创意标注
	 * @param userId userId
	 * @param unitId 创意id
	 * @param tradeId 行业id
	 * @param tagTypeValueMap  tag以及tagValue的map
	 * @param modTime 更新时间
	 * @param optContents  操作历史
	 */
	public void modifyTagAndTrade(Integer userId, Long unitId, Integer tradeId, Map<Integer,
			Integer> tagTypeValueMap, Date modTime, List<OptContent> optContents);
	
	public void modifyRefuseReason(Integer userId, Long unitId, List<Integer> refuseReasonIds);

    /**
     * Function: 审核操作
     * 
     * @author genglei01
     * @param userId userId
     * @param unitId unitId
     * @param mcId    mcId
     * @param versionId    versionId
     * @param newTradeId 三级行业ID
     * @param tradeModified 行业是否被修改，1表示被修改
     * @param unitTag 创意标注，目前支持5个标注
     * @param unitTagModified   创意标注是否修改
     * @param auditType 审核操作类型，1代表一审，2代表复审
     * @param auditResult 审核操作结果，0代表有效，4代表拒绝
     * @param auditResultModified   审核操作结果是否修改
     * @param refuseReasonIds 拒绝理由ID列表
     * @param optContents 历史操作记录
     * @return  创意信息Unit
     */
    public Unit auditResult(Integer userId, Long unitId, Long mcId, Integer versionId, Integer newTradeId, 
            boolean tradeModified, Integer unitTag, boolean unitTagModified, Integer auditType, Integer auditResult, 
            boolean auditResultModified, List<Integer> refuseReasonIds, List<OptContent> optContents);
    
    /**
     * Function: 记录审核日志，推送审核结果给风控团队
     *      1. 记录创意审核物料版本，以便后续进行清理
     *      2. 推送审核结果给风控团队
     * 
     * @author genglei01
     * @param unit 创意信息
     * @param auditResult 审核操作结果，0代表有效，4代表拒绝
     * @param refuseReasonIds   拒绝理由ID列表
     * @param auditorId   审核员ID
     * @return boolean 是否成功
     */
    public boolean pushAuditLog(Unit unit, Integer auditResult, List<Integer> refuseReasonIds, Integer auditorId);

}
