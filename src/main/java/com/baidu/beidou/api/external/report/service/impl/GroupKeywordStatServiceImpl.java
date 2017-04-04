package com.baidu.beidou.api.external.report.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.report.comparator.AbstractViewItemComparator;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.service.vo.KeywordItemKey;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.KeywordStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.atomdriver.AtomUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.DateUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.GroupKtViewItem;

public class GroupKeywordStatServiceImpl extends GenericStatServiceImpl<KeywordStatViewItem> implements
        GenericStatService<KeywordStatViewItem> {
    
    @Resource
    private ReportLiteService reportLiteService;

    /**
     * 获取关键词报告数据,查询有展现的关键词
     * 
     * @param request 查询参数
     * @return List<KeywordStatViewItem> 关键词报告数据列表
     */
    public List<KeywordStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<GroupKtViewItem> allData = reportLiteService.queryKeywordLiteReport(buildQueryParam(request));
        logger.info("GroupKeywordStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());
        // 关键词报告比复杂 只对doris查询的数据做限制
        if (allData.size() > size) {
            logger.info("PackKeywordStatServiceImpl : request data size too large");
            allData = null;
            throw new StorageServiceException();
        }

        // 如果doris查询成功，生成所有数据返回
        List<KeywordStatViewItem> result = getViewItem(allData);

        // 按照时间排序
        Collections.sort(result, new AbstractViewItemComparator());

        return result;
    }

    public List<KeywordStatViewItem> getViewItem(List<GroupKtViewItem> statDatas) throws Exception {
        List<KeywordStatViewItem> result = new ArrayList<KeywordStatViewItem>();

        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("GroupKeywordStatServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        // 3、组装数据并且过滤
        User user = new User();
        user.setUserid(userId);
        if (!CollectionUtils.isEmpty(statDatas)) {
            List<CproKeyword> keywords = new ArrayList<CproKeyword>();
            if (CollectionUtils.isEmpty(groupIds)) {
                if (CollectionUtils.isEmpty(planIds)) {
                    keywords = cproKeywordMgr.findByLevelId(null, null, user);
                } else {
                    for (Integer planId : planIds) {
                        List<CproKeyword> tmpKeywords = cproKeywordMgr.findByLevelId(null, planId, user);
                        keywords.addAll(tmpKeywords);
                    }
                }
            } else {
                for (Integer groupId : groupIds) {
                    List<CproKeyword> tmpKeywords = cproKeywordMgr.findByLevelId(groupId, null, user);
                    keywords.addAll(tmpKeywords);
                }
            }

            // if( CollectionUtils.isEmpty(keywords)) {
            // //如果没有关键词则直接返回
            // return result;
            // }

            // 获用户所有推广组ID，用于过滤doris数据，防止出现错误数据，此处应该缩小查询范围。
            Set<Integer> ktGroupIds = CollectionUtil.createHashSet(cproGroupMgr.findGroupIdsByUserId(userId));

            // 生成Map有利于数据合并
            Map<KeywordItemKey, CproKeyword> mapView = new HashMap<KeywordItemKey, CproKeyword>(keywords.size());
            Set<Integer> planIdSet = new HashSet<Integer>();
            Set<Integer> groupIdSet = new HashSet<Integer>();
            for (CproKeyword keyword : keywords) {
                KeywordItemKey newKey = new KeywordItemKey(keyword.getGroupId(), 0L, keyword.getWordId());
                mapView.put(newKey, keyword);
                planIdSet.add(keyword.getPlanId());
                groupIdSet.add(keyword.getGroupId());
            }
            for (GroupKtViewItem stat : statDatas) {
                planIdSet.add(stat.getPlanId());
                groupIdSet.add(stat.getGroupId());
            }

            Map<Integer, String> planIdNameMapping = cproPlanMgr.findPlanNameByPlanIds(planIdSet);
            Map<Integer, String> groupIdNameMapping = cproGroupMgr.findGroupNameByGroupIds(groupIdSet);

            // 如果有统计数据则进行后续的数据合并操作；
            Long statWordId;
            Integer statPlanId;
            Integer statGroupId;
            Long statGpId;
            Date statDate;
            CproKeyword ktKeyword;

            // 存放已删除但有统计信息的关键词统计信息，key为keywordId，value为统计信息
            // Map<Integer, Map<String, Object>> deletedKeywordMap = new
            // HashMap<Integer, Map<String, Object>>();
            Set<Long> deletedWordIds = new HashSet<Long>();
            Map<KeywordItemKey, GroupKtViewItem> deleteGroupWordStatMap = CollectionUtil.createHashMap();
            for (GroupKtViewItem stat : statDatas) {

                // try {
                statWordId = stat.getWordId();
                statGroupId = stat.getGroupId();
                statGpId = stat.getGpId();
                statPlanId = stat.getPlanId();
                statDate = DateUtils.olapStrToDate(stat.getShowDate());

                // 如果推广组ID不在推广组ID的集合中，直接continue
                if (!ktGroupIds.contains(statGroupId)) {
                    throw new InternalException("Error occurred when contructing keyword view item, wordId=["
                            + statWordId + "] not belong to groupId=[" + statGroupId + "]");
                }
                // } catch (Exception e) {
                // log.error("error to digest doris data[" + stat + "]",e);
                // continue;
                // }

                KeywordItemKey key = new KeywordItemKey(statGroupId, statGpId, statWordId);
                ktKeyword = mapView.get(key);
                if (ktKeyword == null) {
                    // 删除词记录下来，待后续统一处理
                    deleteGroupWordStatMap.put(key, stat);

                    // 如果关键词已經被刪除，则记录之，以备后续查atom；
                    deletedWordIds.add(statWordId);
                    continue;
                }

                KeywordStatViewItem view = new KeywordStatViewItem();
                view.fillStatRecord(stat);
                view.setUserid(userId);
                view.setUsername(username);
                view.setPlanid(statPlanId);
                if (planIdNameMapping.get(statPlanId) == null) {
                    throw new InternalException("Error occurred when contructing keyword view item, wordId=["
                            + statWordId + "] planName cannot be found, planId=[" + statPlanId + "]");
                }
                view.setPlanname(planIdNameMapping.get(statPlanId));
                view.setGroupid(statGroupId);
                if (groupIdNameMapping.get(statGroupId) == null) {
                    throw new InternalException("Error occurred when contructing keyword view item, wordId=["
                            + statWordId + "] groupName cannot be found, groupId=[" + statGroupId + "]");
                }
                view.setGroupname(groupIdNameMapping.get(statGroupId));
                view.setWordId(statWordId);
                view.setKeyword(mapView.get(key).getKeyword());
                view.setDay(dateFormat.format(statDate));
                result.add(view);
            }

            // 如果有doris查出来但是db里没有词，则需要查询Atom词的明文，追加到结果中
            if (!deleteGroupWordStatMap.isEmpty()) {
                // 访问atom，通过wordId查询字面
                Map<Long, String> atomIdKeywordMapping = AtomUtils.getWordById(deletedWordIds);

                for (Entry<KeywordItemKey, GroupKtViewItem> entry : deleteGroupWordStatMap.entrySet()) {
                    KeywordItemKey preKey = entry.getKey();
                    GroupKtViewItem stat = entry.getValue();
                    try {

                        statWordId = stat.getWordId();
                        statGroupId = stat.getGroupId();
                        statGpId = stat.getGpId();

                        statPlanId = stat.getPlanId();
                        statDate = DateUtil.parseDate(stat.getShowDate(), DateUtil.CHINESE_PATTERN);

                        // 如果推广组ID不在推广组ID的集合中，直接continue
                        if (!ktGroupIds.contains(statGroupId)) {
                            continue;
                        }
                    } catch (Exception e) {
                        logger.error("error to digest doris data[{}]", e, stat);
                        continue;
                    }

                    KeywordItemKey key = new KeywordItemKey(statGroupId, statGpId, statWordId);
                    if (preKey.equals(key)) {
                        KeywordStatViewItem view = new KeywordStatViewItem();
                        view.fillStatRecord(stat);
                        view.setUserid(userId);
                        view.setUsername(username);
                        view.setPlanid(statPlanId);
                        if (planIdNameMapping.get(statPlanId) == null) {
                            throw new InternalException("Error occurred when contructing keyword view item, wordId=["
                                    + statWordId + "] planName cannot be found, planId=[" + statPlanId + "]");
                        }
                        view.setPlanname(planIdNameMapping.get(statPlanId));
                        view.setGroupid(statGroupId);
                        if (groupIdNameMapping.get(statGroupId) == null) {
                            throw new InternalException("Error occurred when contructing keyword view item, wordId=["
                                    + statWordId + "] groupName cannot be found, groupId=[" + statGroupId + "]");
                        }
                        view.setGroupname(groupIdNameMapping.get(statGroupId));
                        view.setWordId(statWordId);
                        view.setKeyword(atomIdKeywordMapping.get(statWordId)
                                + ReportWebConstants.HAS_BEEN_DELETE_SUFFIX);
                        view.setDay(dateFormat.format(statDate));
                        result.add(view);
                    }
                }
            }
        }

        return result;
    }

    /**
     * main
     */
    public static void main(String[] args) throws Exception {

        GroupKeywordStatServiceImpl service;
        String beanName = "groupKeywordStatService";
        service = (GroupKeywordStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20120606");
        Date endDate = DateUtils.strToDate("20120609");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        // List<Integer> planIds = new ArrayList<Integer>();
        // planIds.add(771017);
        // planIds.add(771014);
        // planIds.add(771001);
        // planIds.add(771005);
        // planIds.add(328475);
        // request.setPlanIds(planIds);
        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(757446);
        planIds.add(757447);
        planIds.add(740918);// not me
        request.setPlanIds(planIds);
        List<KeywordStatViewItem> result = service.queryStat(request);
        for (KeywordStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
