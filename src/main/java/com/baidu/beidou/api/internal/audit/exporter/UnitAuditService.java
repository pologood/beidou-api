package com.baidu.beidou.api.internal.audit.exporter;

import java.util.List;

import com.baidu.beidou.api.internal.audit.vo.AuditOpt;
import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.audit.vo.ProductView;
import com.baidu.beidou.api.internal.audit.vo.UnitAuditInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitMaterialInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitReauditInfo;
import com.baidu.beidou.api.internal.audit.vo.request.AuditResultUnit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryProduct;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitAudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitTagAndTrade;

/**
 * ClassName: UnitAuditService
 * Function: 北斗内部API，创意相关Service
 *
 * @author genglei
 * @version cpweb-567
 * @date Jun 23, 2013
 */
public interface UnitAuditService {
	
	/**
	 * getUnitAuditList: 在待审核创意页面，获取待审核创意信息
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 22, 2013
	 */
	public AuditResult<UnitAuditInfo> getUnitAuditList(Integer userId, QueryUnitAudit query);
	
	/**
	 * getUnitAuditNum: 在待审核创意页面，获取待审核创意数量
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 22, 2013
	 */
	public AuditResult<Object> getUnitAuditNum(Integer userId, QueryUnitAudit query);
	
	/**
	 * getUnitReauditList：在重审页面的全部创意Tab下，通过unitIds，获取重新创意信息 
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 22, 2013
	 */
	public AuditResult<UnitReauditInfo> getUnitReauditListByUnitIds(String unitIds);
	
	/**
	 * getUnitReauditList:  在重审创意页面，获取重审创意信息
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 22, 2013
	 */
	public AuditResult<UnitReauditInfo> getUnitReauditList(Integer userId, QueryUnitReaudit query);
	
	/**
	 * pass: 审核通过
	 * userId为操作用户
	 * 如果为初审，AuditResultUnit中unitId/userId/tradeId/tradeModified/accuracyType/beautyType/vulgarType/cheatType要有值。
	 * 如果为复审，AuditResultUnit中没有tradeId/tradeModified/accuracyType/beautyType/vulgarType/cheatType
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 23, 2013
	 */
	public AuditResult<AuditOpt> pass(List<AuditResultUnit> auditList, Integer auditorId);
	
	/**
	 * refuse: 审核拒绝
	 * userIds为操作用户
	 * refuseReasonIds为多个不同拒绝理由，所有创意均因为这些理由拒绝，该值是必需的
	 * 如果为初审，AuditResultUnit中unitId/userId/tradeId/tradeModified/accuracyType/beautyType/vulgarType/cheatType要有值。
	 * 如果为复审，AuditResultUnit中没有tradeId/tradeModified/accuracyType/beautyType/vulgarType/cheatType
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 23, 2013
	 */
	public AuditResult<AuditOpt> refuse(List<AuditResultUnit> auditList, 
			List<Integer> refuseReasonIds, Integer auditorId);
	
	/**
	 * modifyTag: 重审页面，修改创意标签Tag
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 23, 2013
	 */
	public AuditResult<Object> modifyTag(List<Integer> userIds, List<Long> unitIds, 
			Integer tagType, Integer tagValue, Integer auditorId);
	
	/**
	 * modifyTrade: 重审页面，修改创意行业分类
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 23, 2013
	 */
	public AuditResult<Object> modifyTrade(List<Integer> userIds, List<Long> unitIds, 
			Integer tradeId, Integer auditorId);
	
	/**
	 * modifyTagAndTrade: 重审页面，修改创意标签Tag和行业分类
	 * @version 1.0.0
	 * @author zhangzhenhua02
	 * @date Nov 20, 2014
	 * @param queryList 请求列表
	 * @param auditorId 审核员id
	 * @return 审核结果
	 */
	public AuditResult<Object> modifyTagAndTrade(List<QueryUnitTagAndTrade> queryList, Integer auditorId);
	
	/**
	 * modifyRefuseReason: 重审页面，修改创意拒绝理由（处于拒绝中状态才可修改）
	 * @version cpweb-567
	 * @author genglei01
	 * @date Jun 23, 2013
	 */
	public AuditResult<Object> modifyRefuseReason(Integer userId, Long unitId, 
			List<Integer> refuseReasonIds, Integer auditorId);
	
	/**
	 * findAuditingUnitIds: 获取待审核创意ID，用于分配审核员时，过滤已审核的创意
	 * @version cpweb-567
	 * @author genglei01
	 * @date Aug 7, 2013
	 */
	public AuditResult<Long> findAuditingUnitIds(Integer userId, List<Long> unitIds);
	
	/**
	 * getProductViewList: 根据unitId等信息，获取该创意可拼接生成的产品预览列表，抽查
	 * @version cpweb-699
	 * @author genglei01
	 * @date Mar 5, 2014
	 */
	public AuditResult<ProductView> getProductViewList(Integer userId, QueryProduct query);
	
	/**
	 * refuseProduct: 拒绝产品
	 * @version cpweb-699
	 * @author genglei01
	 * @date Mar 5, 2014
	 */
	public AuditResult<Object> refuseProduct(Integer userId, List<Long> productIds);
	
    /**
     * Function: 获取创意物料信息，包含图片或者flash的二进制文件
     * 
     * @author genglei01
     * @param userId userId
     * @param unitId unitId
     * @return UnitMaterialInfo
     */
    public AuditResult<UnitMaterialInfo> getUnitMaterailInfo(Integer userId, Long unitId);
	
}
