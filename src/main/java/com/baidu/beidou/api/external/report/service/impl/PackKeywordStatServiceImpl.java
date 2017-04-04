package com.baidu.beidou.api.external.report.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import com.baidu.beidou.api.external.report.comparator.AbstractViewItemComparator;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.service.vo.KeywordItemKey;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.KeywordStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.vo.PackKeywordVo;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.atomdriver.AtomUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.DateUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.GroupKtViewItem;

public class PackKeywordStatServiceImpl extends GenericStatServiceImpl<KeywordStatViewItem> implements
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
        logger.info("PackKeywordStatServiceImpl ：request=[{}] got {} result from Olap", request, allData.size());

        // 关键词报告比复杂 只对Olap查询的数据做限制
        if (allData.size() > size) {
            logger.warn(dataSizeTooLarge);
            allData = null;
            throw new StorageServiceException(dataSizeTooLarge, null);
        }

        // 如果Olap查询成功，生成所有数据返回
        List<KeywordStatViewItem> result = getViewItem(allData);

        // 按照时间排序
        Collections.sort(result, new AbstractViewItemComparator());

        return result;
    }

    public List<KeywordStatViewItem> getViewItem(List<GroupKtViewItem> statDatas) throws Exception {
        List<KeywordStatViewItem> result = CollectionUtil.createArrayList();

        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("GroupKeywordStatServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        // 3、组装数据并且过滤
        User user = new User();
        user.setUserid(userId);
        if (CollectionUtil.isNotEmpty(statDatas)) {
            List<PackKeywordVo> keywords = CollectionUtil.createArrayList();
            if (CollectionUtil.isEmpty(groupIds)) {
                if (CollectionUtil.isEmpty(planIds)) {
                    keywords = groupPackFacade.findKeywordByLevel(userId, null, null);
                } else {
                    for (Integer planId : planIds) {
                        List<PackKeywordVo> tmpKeywords = groupPackFacade.findKeywordByLevel(userId, planId, null);
                        keywords.addAll(tmpKeywords);
                    }
                }
            } else {
                for (Integer groupId : groupIds) {
                    List<PackKeywordVo> tmpKeywords = groupPackFacade.findKeywordByLevel(userId, null, groupId);
                    keywords.addAll(tmpKeywords);
                }
            }

            // if( CollectionUtils.isEmpty(keywords)) {
            // //如果没有关键词则直接返回
            // return result;
            // }

            // 获用户所有推广组ID，用于过滤Olap数据，防止出现错误数据，此处应该缩小查询范围。
            Set<Integer> ktGroupIds = CollectionUtil.createHashSet(cproGroupMgr.findGroupIdsByUserId(userId));

            // 生成Map有利于数据合并
            Map<KeywordItemKey, PackKeywordVo> mapView = CollectionUtil.createHashMap(keywords.size());
            Set<Integer> planIdSet = CollectionUtil.createHashSet();
            Set<Integer> groupIdSet = CollectionUtil.createHashSet();
            for (PackKeywordVo keyword : keywords) {
                KeywordItemKey newKey =
                        new KeywordItemKey(keyword.getGroupId(), keyword.getGpId(), keyword.getWordId());

                mapView.put(newKey, keyword);
                planIdSet.add(keyword.getPlanId());
                groupIdSet.add(keyword.getGroupId());
            }
            for (GroupKtViewItem item : statDatas) {
                planIdSet.add(item.getPlanId());
                groupIdSet.add(item.getGroupId());
            }

            Map<Integer, String> planIdNameMapping = cproPlanMgr.findPlanNameByPlanIds(planIdSet);
            Map<Integer, String> groupIdNameMapping = cproGroupMgr.findGroupNameByGroupIds(groupIdSet);

            // 如果有统计数据则进行后续的数据合并操作；
            Long statWordId;
            Integer statPlanId;
            Integer statGroupId;
            Long statGpId;
            Integer statRefPackId;
            Date statDate;
            PackKeywordVo ktKeyword;

            // 存放已删除但有统计信息的关键词统计信息，key为keywordId，value为统计信息
            // Map<Integer, Map<String, Object>> deletedKeywordMap = new
            // HashMap<Integer, Map<String, Object>>();
            Set<Long> deletedWordIds = CollectionUtil.createHashSet();
            Set<Integer> deletedRefPackIds = CollectionUtil.createHashSet();
            Map<KeywordItemKey, GroupKtViewItem> deleteGroupWordStatMap = CollectionUtil.createHashMap();
            for (GroupKtViewItem item : statDatas) {

                // try {
                statWordId = item.getWordId();
                statGroupId = item.getGroupId();
                statGpId = item.getGpId();
                statRefPackId = item.getRefPackId();
                statPlanId = item.getPlanId();
                statDate = DateUtils.olapStrToDate(item.getShowDate());
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
                    deleteGroupWordStatMap.put(key, item);

                    // 如果关键词已經被刪除，则记录之，以备后续查atom；
                    deletedWordIds.add(statWordId);
                    if (statRefPackId != 0) {
                        // 受众组合名称要依据refPackId来查询
                        deletedRefPackIds.add(statRefPackId);
                    }
                    continue;
                }

                KeywordStatViewItem view = new KeywordStatViewItem();
                view.fillStatRecord(item);
                view.setUserid(userId);
                view.setUsername(username);
                view.setPlanid(statPlanId);
                if (planIdNameMapping.get(statPlanId) == null) {
                    throw new InternalException("Error occurred when contructing keyword view item, keywordId=["
                            + statWordId + "] planName cannot be found, planId=[" + statPlanId + "]");
                }
                view.setPlanname(planIdNameMapping.get(statPlanId));
                view.setGroupid(statGroupId);
                if (groupIdNameMapping.get(statGroupId) == null) {
                    throw new InternalException("Error occurred when contructing keyword view item, keywordId=["
                            + statWordId + "] groupName cannot be found, groupId=[" + statGroupId + "]");
                }
                view.setGroupname(groupIdNameMapping.get(statGroupId));
                view.setWordId(statWordId);
                view.setKeyword(ktKeyword.getKeyword());
                view.setDay(DateUtil.formatDate(statDate));
                // view.setDay(dateFormat.format(statDate));

                // 设置受众信息
                view.setGpId(statGpId);
                view.setPackName(ktKeyword.getPackName());

                result.add(view);
            }

            // 如果有Olap查出来但是db里没有词，则需要查询Atom词的明文，追加到结果中
            if (!deleteGroupWordStatMap.isEmpty()) {
                Map<Long, String> atomIdKeywordMapping = AtomUtils.getWordById(deletedWordIds);

                // 查询受众组合名称
                List<Integer> refPackIdList = CollectionUtil.createArrayList(deletedRefPackIds);
                Map<Integer, String> packNameMapping = CollectionUtil.createHashMap();
                Map<Integer, Map<String, Object>> packInfoMap =
                        groupPackFacade.getPackNameByRefPackId(userId, refPackIdList);
                for (Integer id : packInfoMap.keySet()) {
                    Map<String, Object> packInfo = packInfoMap.get(id);
                    if (packInfo != null) {
                        packNameMapping.put(id, (String) packInfo.get("name"));
                    }
                }

                for (Entry<KeywordItemKey, GroupKtViewItem> entry : deleteGroupWordStatMap.entrySet()) {
                    KeywordItemKey preKey = entry.getKey();
                    GroupKtViewItem item = entry.getValue();

                    try {
                        statWordId = item.getWordId();
                        statGroupId = item.getGroupId();
                        statGpId = item.getGpId();
                        statRefPackId = item.getRefPackId();
                        statPlanId = item.getPlanId();
                        statDate = DateUtils.olapStrToDate(item.getShowDate());

                        // 如果推广组ID不在推广组ID的集合中，直接continue
                        if (!ktGroupIds.contains(statGroupId)) {
                            continue;
                        }
                    } catch (Exception e) {
                        logger.error("error to digest Olap data[" + item + "]", e);
                        continue;
                    }

                    KeywordItemKey key = new KeywordItemKey(statGroupId, statGpId, statWordId);
                    if (preKey.equals(key)) {
                        KeywordStatViewItem view = new KeywordStatViewItem();
                        view.fillStatRecord(item);
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

                        // 设置受众信息
                        view.setGpId(statGpId);
                        view.setPackName(packNameMapping.get(statRefPackId));

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

        PackKeywordStatServiceImpl service;
        String beanName = "packKeywordStatService";
        service = (PackKeywordStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20120606");
        Date endDate = DateUtils.strToDate("20120927");
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
        List<Integer> planIds = CollectionUtil.createArrayList();
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
