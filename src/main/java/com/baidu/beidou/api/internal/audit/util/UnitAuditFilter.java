package com.baidu.beidou.api.internal.audit.util;

import static com.baidu.beidou.util.FilterUtils.addInCondition;
import static com.baidu.beidou.util.FilterUtils.addInParameter;
import static com.baidu.beidou.util.FilterUtils.addIntCondition;
import static com.baidu.beidou.util.FilterUtils.addIntParameter;
import static com.baidu.beidou.util.FilterUtils.addLongCondition;
import static com.baidu.beidou.util.FilterUtils.addLongParameter;
import static com.baidu.beidou.util.FilterUtils.addStringCondition;
import static com.baidu.beidou.util.FilterUtils.addStringEqualParameter;
import static com.baidu.beidou.util.FilterUtils.addStringLikeParameter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.baidu.beidou.api.internal.audit.constant.AuditConstant;
import com.baidu.beidou.api.internal.audit.constant.QueryConstant;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitAudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.util.StringUtils;

/**
 * ClassName: 审核管理员的获得待审核推广单元的过滤器
 * 必须先调用getFilter（），然后调用getParameters()
 * Function: TODO ADD FUNCTION
 *
 * @author genglei
 * @version cpweb-567
 * @date Jun 22, 2013
 */
public class UnitAuditFilter {
	
	private static final Log LOG = LogFactory.getLog(UnitAuditFilter.class);
	
	private static final String COND_USERID = ".user.userid = ? ";
	private static final String COND_PLANSTATE = ".plan.planState = ? ";
	private static final String COND_GROUPSTATE = ".group.groupState = ? ";
	private static final String COND_UNITSTATE = ".state = ? ";
	
	private static final String COND_REFUSE_REASON = ".auditing.refuseReason = ? ";
	
	private static final String COND_UNITID_IN = ".material.id ";
	private static final String COND_UNITID_EQUAL = ".material.id=? ";
	
	private static final String COND_TITLE_LIKE = ".material.title ";
	private static final String COND_DESCRIPTON1_LIKE = ".material.description1 ";
	private static final String COND_DESCRIPTON2_LIKE = ".material.description2 ";
	
	private static final String COND_WULIAOTYPE_EQUAL = ".material.wuliaoType=?";
    private static final String COND_SMART_UNIT_EQUAL = ".material.isSmart=?";
	private static final String COND_WULIAOTYPE_IN = ".material.wuliaoType";
	
	
	private static final String COND_SHOWURL_LIKE = ".material.showUrl LIKE ? ";
	private static final String COND_TARGETURL_LIKE = ".material.targetUrl LIKE ? ";

	private static final String COND_ACCURACY_TYPE_EQUAL = ".material.confidenceLevel=?";
	private static final String COND_BEAUTY_TYPE_EQUAL = ".material.beautyLevel=?";
	private static final String COND_VULGAR_TYPE_EQUAL = ".material.vulgarLevel=?";
	private static final String COND_CHEAT_TYPE_EQUAL = ".material.cheatLevel=?";
	
	public static UnitAuditSql getUnitAuditFilter(final String unit, QueryUnitAudit query) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder result = new StringBuilder();
		
		// 用户id过滤条件
		Integer userId = query.getUserId();
		addIntCondition(result, unit + COND_USERID, userId);
		addIntParameter(params, userId);

		// 推广计划状态过滤条件
		Integer planState = query.getPlanState();
		addIntCondition(result, unit + COND_PLANSTATE, planState);
		addIntParameter(params, planState);
		
		if (query.getQueryType() != null && !StringUtils.isEmpty(query.getQuery())) {
			// 推广计划名称过滤条件
			addPlanNameCondition(result, query.getQueryType(), query.getQuery(), unit, params);
			
			// 推广组名称过滤条件
			addGroupNameCondition(result, query.getQueryType(), query.getQuery(), unit, params);
			
			// 推广单元内容（标题、描述1、描述2）过滤条件
			addUnitTitleAndDescCondition(result, query.getQueryType(), query.getQuery(), unit, params);
			
			// 推广单元显示URL过滤条件
			addShowUrlCondition(result, query.getQueryType(), query.getQuery(), unit, params);

			// 推广单元点击URL过滤条件
			addTargetUrlCondition(result, query.getQueryType(), query.getQuery(), unit, params);
		}

		// 推广组状态过滤条件
		addGroupViewStateCondition(result, query.getGroupState(), unit, params);

		// 创意ID查询
		addUnitIdCondition(result, query.getQueryType(), query.getQuery(), query.getUnitIds(), unit, params);
		
		// 推广单元物料类型过滤条件
		addUnitTypeCondition(result, query.getUnitType(), unit, params);
		
		// 创意标注过滤，准确度过滤
		addAccuracyTypeCondition(result, query.getAccuracyType(), unit, params);
		
		// 创意标注过滤，美观度过滤
		addBeautyTypeCondition(result, query.getBeautyType(), unit, params);
		
		// 创意标注过滤，低俗过滤
		addVulgarTypeCondition(result, query.getVulgarType(), unit, params);
		
		// 创意标注过滤，欺诈过滤
		addCheatTypeCondition(result, query.getCheatType(), unit, params);
		
		return new UnitAuditSql(result.toString(), params);
	}
	
	public static UnitAuditSql getUnitReauditFilter(final String unit, QueryUnitReaudit query) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder result = new StringBuilder();
		
		// 用户id过滤条件
		Integer userId = query.getUserId();
		addIntCondition(result, unit + COND_USERID, userId);
		addIntParameter(params, userId);

		// 推广计划状态过滤条件
		Integer planState = query.getPlanState();
		addIntCondition(result, unit + COND_PLANSTATE, planState);
		addIntParameter(params, planState);
		
		if (query.getQueryType() != null && !StringUtils.isEmpty(query.getQuery())) {
			// 推广计划名称过滤条件
			addPlanNameCondition(result, query.getQueryType(), query.getQuery(), unit, params);
			
			// 推广组名称过滤条件
			addGroupNameCondition(result, query.getQueryType(), query.getQuery(), unit, params);
			
			// 推广单元内容（标题、描述1、描述2）过滤条件
			addUnitTitleAndDescCondition(result, query.getQueryType(), query.getQuery(), unit, params);

			// 推广单元显示URL过滤条件
			addShowUrlCondition(result, query.getQueryType(), query.getQuery(), unit, params);

			// 推广单元点击URL过滤条件
			addTargetUrlCondition(result, query.getQueryType(), query.getQuery(), unit, params);
		}

		// 推广组状态过滤条件
		addGroupViewStateCondition(result, query.getGroupState(), unit, params);

		// 创意ID查询
		addUnitIdConditionForReaudit(result, query.getQueryType(), query.getQuery(), unit, params);
		
		// 创意状态过滤
		addUnitStateCondition(result, query.getUnitState(), query.getRefuseReasonId(), unit, params);
		
		// 推广单元物料类型过滤条件
		addUnitTypeCondition(result, query.getUnitType(), unit, params);
		
		// 创意标注过滤，准确度过滤
		addAccuracyTypeCondition(result, query.getAccuracyType(), unit, params);
		
		// 创意标注过滤，美观度过滤
		addBeautyTypeCondition(result, query.getBeautyType(), unit, params);
		
		// 创意标注过滤，低俗过滤
		addVulgarTypeCondition(result, query.getVulgarType(), unit, params);
		
		// 创意标注过滤，欺诈过滤
		addCheatTypeCondition(result, query.getCheatType(), unit, params);
		
		return new UnitAuditSql(result.toString(), params);
	}
	
	public static UnitAuditSql getUnitReauditFilter(final String unit, String unitIds) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder result = new StringBuilder();
		
		addUnitIdsCondition(result, unitIds, unit, params);
		return new UnitAuditSql(result.toString(), params);
	}
	
	private static void addPlanNameCondition(final StringBuilder result,
			Integer queryType, String query, String unit, List<Object> params) {
		if (queryType == QueryConstant.QueryUnitAudit.planName
				&& !StringUtils.isEmpty(query)) {
			String planName = query.toLowerCase();
			addStringCondition(result, "lower(" + unit + ".plan.planName) like ? ",	planName);
			addStringLikeParameter(params, planName);
		}
	}
	
	private static void addGroupNameCondition(final StringBuilder result,
			Integer queryType, String query, String unit, List<Object> params) {
		if (queryType == QueryConstant.QueryUnitAudit.groupName
				&& !StringUtils.isEmpty(query)) {
			String groupName = query.toLowerCase();
			addStringCondition(result, "lower(" + unit + ".group.groupName) like ? ", groupName);
			addStringLikeParameter(params, groupName);
		}
	}
	
	private static void addUnitIdCondition(final StringBuilder result,
			Integer queryType, String query, List<Long> unitIds, String unit, List<Object> params) {
		if (queryType == null || queryType != QueryConstant.QueryUnitAudit.unitId
				|| (queryType == QueryConstant.QueryUnitAudit.unitId && StringUtils.isEmpty(query))) {
			if (CollectionUtils.isEmpty(unitIds)) {
				result.append(" and 1=0 ");
			} else {
				Long[] unitIdArray = unitIds.toArray(new Long[0]);
				addInCondition(result, unit + COND_UNITID_IN, unitIdArray);
				addInParameter(params, unitIdArray);
			}
		} else {
			String unitIdStr = query;
			if (unitIdStr.indexOf(",") <= 0) {
				try {
					Long unitId = Long.valueOf(unitIdStr);
					if (unitIds.contains(unitId)) {
						addLongCondition(result, unit + COND_UNITID_EQUAL, unitId);
						addLongParameter(params, unitId);
					} else {
						result.append(" and 1=0 ");
					}
					
				} catch (Exception e) {
					LOG.error("unitId is not a long value", e);
				}
			}
		}
	}
	
	private static void addUnitIdConditionForReaudit(final StringBuilder result,
			Integer queryType, String query, String unit, List<Object> params) {
		if (queryType == null) {
			return;
		}
		
		if (queryType == QueryConstant.QueryUnitAudit.unitId
				&& !StringUtils.isEmpty(query)) {
			String unitIdStr = query;
			
			if (unitIdStr.indexOf(",") <= 0) {
				try {
					Long unitId = Long.valueOf(unitIdStr);
					addLongCondition(result, unit + COND_UNITID_EQUAL, unitId);
					addLongParameter(params, unitId);
				} catch (Exception e) {
					LOG.error("unitId is not a long value", e);
				}
			}
		}
	}
	
	private static void addUnitIdsCondition(final StringBuilder result,
			String unitIds, String unit, List<Object> params) {
		try {
			String[] strArray = unitIds.split(",");
			if (ArrayUtils.isEmpty(strArray)) {
				result.append(" and 1=0");
			}
			Long[] unitIdArray = new Long[strArray.length];
			for (int index = 0; index < strArray.length; index ++) {
				unitIdArray[index] = Long.valueOf(strArray[index]);
			}
			addInCondition(result, unit + COND_UNITID_IN, unitIdArray);
			addInParameter(params, unitIdArray);
		} catch (Exception e) {
			LOG.error("generate sql for listReauditByUnitIds failed, unitIds-" + unitIds, e);
			result.append(" and 1=0");
		}
	}
	
	private static void addUnitTitleAndDescCondition(final StringBuilder result,
			Integer queryType, String query, String unit, List<Object> params) {
		if (queryType == QueryConstant.QueryUnitAudit.unitContent
				&& !StringUtils.isEmpty(query)) {
			addStringCondition(result, "concat(" + unit + COND_TITLE_LIKE + ", "
					+ getSqlIfStatement(unit + COND_DESCRIPTON1_LIKE) + ", "
					+ getSqlIfStatement(unit + COND_DESCRIPTON2_LIKE) + ") like ? ", query);
			addStringLikeParameter(params, query);
		}
	}
	
	private static String getSqlIfStatement(String arg) {
		StringBuilder sb = new StringBuilder();
		sb.append(" IFNULL(" + arg +	", '') ");
		
		return sb.toString();
	}
	
	private static void addTargetUrlCondition(final StringBuilder result,
			Integer queryType, String query, String unit, List<Object> params) {
		if (queryType == QueryConstant.QueryUnitAudit.targetUrl
				&& !StringUtils.isEmpty(query)) {
			String targetUrl = query.toLowerCase();
			addStringCondition(result, unit + COND_TARGETURL_LIKE, targetUrl);
			addStringLikeParameter(params, targetUrl);
		}
	}
	
	private static void addShowUrlCondition(final StringBuilder result,
			Integer queryType, String query, String unit, List<Object> params) {
		if (queryType == QueryConstant.QueryUnitAudit.showUrl
				&& !StringUtils.isEmpty(query)) {
			String showUrl = query.toLowerCase();
			addStringCondition(result, unit + COND_SHOWURL_LIKE, showUrl);
			addStringLikeParameter(params, showUrl);
		}
	}

    private static void addUnitTypeCondition(final StringBuilder result, Integer unitType, String unit,
            List<Object> params) {
        if (unitType == null || unitType < 0) {
            return;
        } else if (unitType == QueryConstant.QueryUnitType.text) {
            Integer wuliaoType = CproUnitConstant.MATERIAL_TYPE_LITERAL;
            addIntCondition(result, unit + COND_WULIAOTYPE_EQUAL, wuliaoType);
            addIntParameter(params, wuliaoType);
            
            // 过滤掉智能文本创意
            Integer isSmart = CproUnitConstant.IS_SMART_FALSE;
            addIntCondition(result, unit + COND_SMART_UNIT_EQUAL, isSmart);
            addIntParameter(params, isSmart);
        } else if (unitType == QueryConstant.QueryUnitType.icon) {
            Integer wuliaoType = CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON;
            addIntCondition(result, unit + COND_WULIAOTYPE_EQUAL, wuliaoType);
            addIntParameter(params, wuliaoType);
            
            // 过滤掉智能文本创意
            Integer isSmart = CproUnitConstant.IS_SMART_FALSE;
            addIntCondition(result, unit + COND_SMART_UNIT_EQUAL, isSmart);
            addIntParameter(params, isSmart);
        } else if (unitType == QueryConstant.QueryUnitType.image) {
            Integer[] wuliaoTypeArray = new Integer[] { CproUnitConstant.MATERIAL_TYPE_PICTURE, 
                    CproUnitConstant.MATERIAL_TYPE_FLASH };
            addInCondition(result, unit + COND_WULIAOTYPE_IN, wuliaoTypeArray);
            addInParameter(params, wuliaoTypeArray);
        } else if (unitType == QueryConstant.QueryUnitType.smart) {
            Integer isSmart = CproUnitConstant.IS_SMART_TRUE;
            addIntCondition(result, unit + COND_SMART_UNIT_EQUAL, isSmart);
            addIntParameter(params, isSmart);
        } else if (unitType == QueryConstant.QueryUnitType.allNotSmart) {
            Integer isSmart = CproUnitConstant.IS_SMART_FALSE;
            addIntCondition(result, unit + COND_SMART_UNIT_EQUAL, isSmart);
            addIntParameter(params, isSmart);
        }
    }
	
	private static void addUnitStateCondition(final StringBuilder result,
			Integer unitState, Integer refuseReasonId, String unit, List<Object> params) {
		if (unitState != null && unitState == AuditConstant.AUDIT_RESULT_PASS) {
		} else {
			if (refuseReasonId != null && refuseReasonId > 0) {
				addStringCondition(result, unit + COND_REFUSE_REASON, String.valueOf(refuseReasonId));
				addStringEqualParameter(params, String.valueOf(refuseReasonId));
			}
		}
		
		if (unitState != null && unitState >= 0) {
			int state = 0;
			if (unitState == AuditConstant.AUDIT_RESULT_PASS) {
				state = CproUnitConstant.UNIT_STATE_NORMAL;
			} else if (unitState == AuditConstant.AUDIT_RESULT_REFUSE) {
				state = CproUnitConstant.UNIT_STATE_REFUSE;
			} else if (unitState == AuditConstant.AUDIT_RESULT_PAUSE) {
				state = CproUnitConstant.UNIT_STATE_PAUSE;
			} else {
				return;
			}
			addIntCondition(result, unit + COND_UNITSTATE, state);
			addIntParameter(params, state);
		}
	}
	
	private static void addAccuracyTypeCondition(final StringBuilder result, 
			Integer accuracyType, String unit, List<Object> params) {
		if (accuracyType != null && accuracyType >= QueryConstant.QueryModifyTag.levelMinValue
				&& accuracyType <= QueryConstant.QueryModifyTag.confidenceLevelMaxValue) {
			addIntCondition(result, unit + COND_ACCURACY_TYPE_EQUAL, accuracyType);
			addIntParameter(params, accuracyType);
		}
	}
	
	private static void addBeautyTypeCondition(final StringBuilder result, 
			Integer beautyType, String unit, List<Object> params) {
		if (beautyType != null && beautyType >= QueryConstant.QueryModifyTag.levelMinValue
				&& beautyType <= QueryConstant.QueryModifyTag.beautyLevelMaxValue) {
			addIntCondition(result, unit + COND_BEAUTY_TYPE_EQUAL, beautyType);
			addIntParameter(params, beautyType);
		}
	}
	
	private static void addVulgarTypeCondition(final StringBuilder result, 
			Integer vulgarType, String unit, List<Object> params) {
		if (vulgarType != null && vulgarType >= QueryConstant.QueryModifyTag.levelMinValue
				&& vulgarType <= QueryConstant.QueryModifyTag.vulgarLevelMaxValue) {
			addIntCondition(result, unit + COND_VULGAR_TYPE_EQUAL, vulgarType);
			addIntParameter(params, vulgarType);
		}
	}
	
	private static void addCheatTypeCondition(final StringBuilder result, 
			Integer cheatType, String unit, List<Object> params) {
		if (cheatType != null && cheatType >= QueryConstant.QueryModifyTag.levelMinValue
				&& cheatType <= QueryConstant.QueryModifyTag.cheatLevelMaxValue) {
			addIntCondition(result, unit + COND_CHEAT_TYPE_EQUAL, cheatType);
			addIntParameter(params, cheatType);
		}
	}
	
	private static void addGroupViewStateCondition(final StringBuilder result,
			Integer groupState, String unit, List<Object> params) {
		switch (groupState) {
		case CproGroupConstant.GROUP_VIEW_STATE_NORMAL:
			// 有效并且推广计划有效
			addIntCondition(result, unit + COND_PLANSTATE, CproPlanConstant.PLAN_STATE_NORMAL);
			addIntParameter(params, CproPlanConstant.PLAN_STATE_NORMAL);
			addIntCondition(result, unit + COND_GROUPSTATE, CproGroupConstant.GROUP_STATE_NORMAL);
			addIntParameter(params, CproGroupConstant.GROUP_STATE_NORMAL);
			break;
		case CproGroupConstant.GROUP_VIEW_STATE_PAUSE:
			addIntCondition(result, unit + COND_GROUPSTATE, CproGroupConstant.GROUP_STATE_PAUSE);
			addIntParameter(params, CproGroupConstant.GROUP_STATE_PAUSE);
			break;
		case CproGroupConstant.GROUP_VIEW_STATE_DELETE:
			addIntCondition(result, unit + COND_GROUPSTATE, CproGroupConstant.GROUP_STATE_DELETE);
			addIntParameter(params, CproGroupConstant.GROUP_STATE_DELETE);
			break;
		case CproGroupConstant.GROUP_VIEW_STATE_PLAN_NOTBEGIN:
			// 有效并且推广计划未开始
			addIntCondition(result, unit + COND_PLANSTATE, CproPlanConstant.PLAN_STATE_NOTBEGIN);
			addIntParameter(params, CproPlanConstant.PLAN_STATE_NOTBEGIN);
			addIntCondition(result, unit + COND_GROUPSTATE, CproGroupConstant.GROUP_STATE_NORMAL);
			addIntParameter(params, CproGroupConstant.GROUP_STATE_NORMAL);
			break;
		case CproGroupConstant.GROUP_VIEW_STATE_PLAN_PAUSE:
			// 有效并且推广计划暂停
			addIntCondition(result, unit + COND_PLANSTATE, CproPlanConstant.PLAN_STATE_PAUSE);
			addIntParameter(params, CproPlanConstant.PLAN_STATE_PAUSE);
			addIntCondition(result, unit + COND_GROUPSTATE, CproGroupConstant.GROUP_STATE_NORMAL);
			addIntParameter(params, CproGroupConstant.GROUP_STATE_NORMAL);
			break;
		case CproGroupConstant.GROUP_VIEW_STATE_PALN_DELETE:
			// 有效并且推广计划删除
			addIntCondition(result, unit + COND_PLANSTATE, CproPlanConstant.PLAN_STATE_DELETE);
			addIntParameter(params, CproPlanConstant.PLAN_STATE_DELETE);
			addIntCondition(result, unit + COND_GROUPSTATE, CproGroupConstant.GROUP_STATE_NORMAL);
			addIntParameter(params, CproGroupConstant.GROUP_STATE_NORMAL);
			break;
		case CproGroupConstant.GROUP_VIEW_STATE_PLAN_END:
			// 有效并且推广计划已结束
			addIntCondition(result, unit + COND_PLANSTATE, CproPlanConstant.PLAN_STATE_END);
			addIntParameter(params, CproPlanConstant.PLAN_STATE_END);
			addIntCondition(result, unit + COND_GROUPSTATE, CproGroupConstant.GROUP_STATE_NORMAL);
			addIntParameter(params, CproGroupConstant.GROUP_STATE_NORMAL);
			break;
		default:
			break;
		}
	}
}
