package com.baidu.beidou.api.external.report.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baidu.beidou.api.external.report.comparator.AbstractViewItemComparator;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.TradeAssistant;
import com.baidu.beidou.api.external.report.vo.TradeStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproGroupInfo;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.cprogroup.vo.TradeInfo;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.report.ReportConstants;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.StringUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.TradeViewItem;

public class ChosenTradeStatServiceImpl extends GenericStatServiceImpl<TradeStatViewItem> implements
        GenericStatService<TradeStatViewItem> {
    
    @Resource
    private ReportLiteService reportLiteService;

    /**
     * 获取有展现网站报告数据
     * 
     * @param request 查询参数
     * @return List<ShownSiteStatServiceImpl> 有展现网站报告数据列表
     */
    public List<TradeStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);

        // 获取所有推广组
        this.groupIds = findGroupIds(userId, planIds, groupIds);
        List<CproGroupInfo> groupInfoList = cproGroupMgr.findCproGroupInfoByKeys(groupIds, userId);
        if (CollectionUtil.isEmpty(groupInfoList)) {
            return CollectionUtil.createArrayList();
        }
        // 填充doris数据
        // 如果时间在group_mainsite视图生成之后则通过group_mainsite查询
        List<TradeStatViewItem> result = getQueryDataByTrade(groupInfoList, request);

        logger.info("ChosenTradeStatServiceImpl ：request=[{}] got {} result from OLAP", request, result.size());
        // 按照时间排序
        Collections.sort(result, new AbstractViewItemComparator());

        if (!idOnly) {
            result = getEmptyViewItem(result);
        }

        return result;
    }

    /**
     * 获取行业粒度的数据
     * 
     * @param result
     * @param groupInfoList
     */
    private List<TradeStatViewItem> getQueryDataByTrade(List<CproGroupInfo> groupInfoList,
            ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<TradeViewItem> allData = reportLiteService.queryTradeLiteReport(buildQueryParam(request));

        // 将doris数据按天切割
        Map<String, List<TradeViewItem>> allDataByDate = CollectionUtil.createHashMap();
        if (CollectionUtil.isNotEmpty(allData)) {
            for (TradeViewItem item : allData) {
                Date date = DateUtils.olapStrToDate(item.getShowDate());
                String key = dateFormat.format(date);
                List<TradeViewItem> temp = allDataByDate.get(key);
                if (temp == null) {
                    temp = CollectionUtil.createArrayList();
                }
                temp.add(item);
                allDataByDate.put(key, temp);
            }
        }
        List<TradeStatViewItem> result = CollectionUtil.createArrayList();
        // 按天处理doris数据
        for (Map.Entry<String, List<TradeViewItem>> entry : allDataByDate.entrySet()) {
            String key = entry.getKey();
            List<TradeViewItem> data = entry.getValue();
            // 行业辅助VO类的Mapping即Map<GroupID,<TradeId,TradeAssistant>>
            Map<Integer, Map<Integer, TradeAssistant>> mapping = CollectionUtil.createHashMap();
            initTradeAssistantMap(groupInfoList, mapping);
            // 行业添加doris数据处理
            if (CollectionUtil.isNotEmpty(data)) {
                for (TradeViewItem item : data) {
                    Integer firstTradeId = item.getFirstTradeId();
                    Integer secondTradeId = item.getSecondTradeId();
                    Integer planId = item.getPlanId();
                    Integer groupId = item.getGroupId();
                    Map<Integer, TradeAssistant> map = mapping.get(groupId);
                    if (map == null) {
                        // 如果没有此group，不需要处理
                        continue;
                    }

                    // 处理一级行业
                    TradeAssistant firstTrade = map.get(firstTradeId);
                    if (firstTrade != null) {
                        if (firstTrade.getSelfTrade() != null) {
                            firstTrade.getSelfTrade().setWithData(true);// 设置是否有统计数据的标志
                            firstTrade.getSelfTrade().setDay(key);
                            firstTrade.getSelfTrade().setUserId(userId);
                            firstTrade.getSelfTrade().setPlanId(planId);
                            firstTrade.getSelfTrade().setGroupId(groupId);
                            if (secondTradeId == ReportConstants.sumFlagInDoris) {
                                firstTrade.getSelfTrade().fillUvAndTrans(item);
                            }
                            firstTrade.getSelfTrade().mergeBasicField(item);// 填充数据(累加)
                            TradeStatViewItem secondTradeViewItem = firstTrade.getSecondTrade(secondTradeId);
                            if (secondTradeViewItem != null) {
                                secondTradeViewItem.fillStatRecord(item);// 填充数据
                                secondTradeViewItem.setWithData(true);// 设置是否有统计数据的标志
                                secondTradeViewItem.setDay(key);
                                secondTradeViewItem.setUserId(userId);
                                secondTradeViewItem.setPlanId(planId);
                                secondTradeViewItem.setGroupId(groupId);
                            }
                        }
                    }
                    // 处理二级行业
                    TradeAssistant secondTrade = map.get(secondTradeId);
                    if (secondTrade != null) {
                        secondTrade.getSelfTrade().fillStatRecord(item);// 填充数据
                        secondTrade.getSelfTrade().setWithData(true);// 设置是否有统计数据的标志
                        secondTrade.getSelfTrade().setDay(key);
                        secondTrade.getSelfTrade().setUserId(userId);
                        secondTrade.getSelfTrade().setPlanId(planId);
                        secondTrade.getSelfTrade().setGroupId(groupId);
                    }

                }
            }
            // 获取mapping中有数据的TradeAssistant,转换成TradeStatViewItem
            getViewItem(mapping, result);
        }

        return result;

    }

    /**
     * 转换tradeList
     * 
     * @param tradeListStr
     * @return
     */
    private static List<Integer> unConvertTradeList(String tradeListStr) {
        List<Integer> tradeIdList = CollectionUtil.createArrayList();
        if (StringUtils.isEmpty(tradeListStr)) {
            return tradeIdList;
        }
        String[] tradeList = tradeListStr.split("\\" + CproGroupConstant.FIELD_SEPERATOR);
        if (tradeList != null) {
            for (String tradeIdStr : tradeList) {
                tradeIdList.add(Integer.valueOf(tradeIdStr));
            }
        }
        return tradeIdList;
    }

    private void appendTradeInfo(TradeInfo firstTrade, TradeInfo secondTrade, TradeStatViewItem vo, int level) {
        vo.setLevel(level);
        if (firstTrade != null) {
            vo.setFirstTrade(firstTrade.getTradename());
            vo.setFirstTradeId(firstTrade.getTradeid());
        }
        if (secondTrade != null) {
            // 有secondTrade的时候，firstTrade一定非空
            vo.setSecondTrade(secondTrade.getTradename());
            vo.setSecondTradeId(secondTrade.getTradeid());
        }
    }

    /**
     * 初始化自选行业
     * 
     * @param groupInfoList
     * @param mapping
     */
    private void initTradeAssistantMap(List<CproGroupInfo> groupInfoList,
            Map<Integer, Map<Integer, TradeAssistant>> mapping) {
        // 遍历当前层级的推广组
        for (CproGroupInfo groupInfo : groupInfoList) {
            List<Integer> tradeIdList = unConvertTradeList(groupInfo.getSiteTradeListStr());
            if (groupInfo.getIsAllSite() == CproGroupConstant.GROUP_ALLSITE || CollectionUtil.isEmpty(tradeIdList)) {
                // 没有自选行业的不需要关心
                continue;
            }
            Integer groupId = groupInfo.getGroupId();
            Map<Integer, TradeAssistant> map = mapping.get(groupId);
            if (map == null) {
                map = CollectionUtil.createHashMap();
                mapping.put(groupInfo.getGroupId(), map);
            }
            // 4.1遍历推广组的自选行业

            for (Integer tradeId : tradeIdList) {
                TradeInfo tradeInfo = UnionSiteCache.tradeInfoCache.getTradeInfoById(tradeId);// 从Cache中获取行业信息
                if (tradeInfo == null || tradeInfo.getViewstat() == CproGroupConstant.TRADE_OTHER) {
                    // 不存在的trade或者“其他”都不可见
                    continue;
                }
                if (tradeInfo.getParentid() == 0) {// 如果为一级行业
                    TradeAssistant tradeAssistant = new TradeAssistant(tradeInfo.getTradeid(), true);
                    // 如果选中了一级行业，就需要为其append全部二级行业
                    List<Integer> subTradeList =
                            UnionSiteCache.tradeInfoCache.getFirstSiteTradeChildren().get(tradeInfo.getTradeid());
                    // 只要一级行业选中，就需要为其append全部二级行业
                    if (CollectionUtil.isNotEmpty(subTradeList)) {
                        for (Integer subTrade : subTradeList) {
                            TradeInfo subTradeInfo = UnionSiteCache.tradeInfoCache.getTradeInfoById(subTrade);
                            TradeStatViewItem vo = new TradeStatViewItem();
                            vo.setSelfTradeName(subTradeInfo.getTradename());
                            vo.setSelfTradeId(subTradeInfo.getTradeid());
                            appendTradeInfo(tradeInfo, subTradeInfo, vo, 1);
                            tradeAssistant.addSecondTrade(vo);
                        }
                    }

                    TradeStatViewItem vo = new TradeStatViewItem();
                    vo.setSelfTradeName(tradeInfo.getTradename());
                    vo.setSelfTradeId(tradeInfo.getTradeid());
                    appendTradeInfo(tradeInfo, null, vo, 0);
                    vo.setGroupId(groupId);
                    tradeAssistant.setSelfTrade(vo);
                    map.put(tradeInfo.getTradeid(), tradeAssistant);
                } else {
                    // 如果选中了二级行业，不需要为其append一级行业，两者互斥
                    TradeAssistant tradeAssistant = new TradeAssistant(tradeInfo.getTradeid(), false);
                    TradeStatViewItem vo = new TradeStatViewItem();
                    vo.setSelfTradeName(tradeInfo.getTradename());
                    vo.setSelfTradeId(tradeInfo.getTradeid());
                    vo.setGroupId(groupId);
                    TradeInfo firstTradeInfo = UnionSiteCache.tradeInfoCache.getTradeInfoById(tradeInfo.getParentid());
                    appendTradeInfo(firstTradeInfo, tradeInfo, vo, 0);
                    tradeAssistant.setSelfTrade(vo);
                    map.put(tradeInfo.getTradeid(), tradeAssistant);
                }
            }
        }
    }

    /**
     * 获取mapping中有数据的TradeAssistant,转换成TradeStatViewItem
     * 
     * @param mapping
     * @param result
     */
    private void getViewItem(Map<Integer, Map<Integer, TradeAssistant>> mapping, List<TradeStatViewItem> result) {
        for (Integer groupId : mapping.keySet()) {
            Map<Integer, TradeAssistant> map = mapping.get(groupId);
            for (Integer tradeId : map.keySet()) {
                TradeAssistant tradeAssistant = map.get(tradeId);
                // 获取有统计数据的TradeStatViewItem
                List<TradeStatViewItem> temp = tradeAssistant.getTradeViewItemWithData();
                if (CollectionUtil.isNotEmpty(temp)) {
                    result.addAll(temp);
                }
            }
        }
    }

    /**
     * 填充用户名、计划名、推广组名
     * 
     * @param result
     * @return
     * @throws Exception
     */
    public List<TradeStatViewItem> getEmptyViewItem(List<TradeStatViewItem> result) throws Exception {
        // Step1.
        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("GroupStatServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        // Step2.
        Map<Integer, CproGroup> groupInfoData = findGroupInfo(userId, planIds, groupIds);
        if (CollectionUtil.isEmpty(groupIds)) {
            groupIds = CollectionUtil.createArrayList();
            groupIds.addAll(groupInfoData.keySet());
        }

        // Step3.
        Map<Integer, CproPlan> planInfoData = findPlanInfo(userId, groupInfoData);

        for (TradeStatViewItem stat : result) {
            try {
                stat.setUserName(username);
                if (planInfoData.get(stat.getPlanId()) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get plan id=["
                            + stat.getPlanId() + "]");
                }
                stat.setPlanName(planInfoData.get(stat.getPlanId()).getPlanName());
                if (groupInfoData.get(stat.getGroupId()) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get group id=["
                            + stat.getGroupId() + "]");
                }
                stat.setGroupName(groupInfoData.get(stat.getGroupId()).getGroupName());
            } catch (Exception e) {
                logger.error("error occured when contructing view item", e);
                continue;
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        CproGroupConstantMgr cproGroupConstantMgr =
                (CproGroupConstantMgr) ServiceLocator.getInstance().getBeanByName("cproGroupConstantMgr");
        cproGroupConstantMgr.loadSystemConf();

        ChosenTradeStatServiceImpl service;
        String beanName = "chosenTradeStatService";
        service = (ChosenTradeStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20120130");
        Date endDate = DateUtils.strToDate("20130611");
        request.setUserid(480787);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setIdOnly(false);

        List<Integer> planIds = CollectionUtil.createArrayList();
        // planIds.add(757446);
        // planIds.add(1363476);
        // planIds.add(740918);// not me
        request.setPlanIds(planIds);

        List<Integer> groupIds = CollectionUtil.createArrayList();
        // groupIds.add(4302877);
        // groupIds.add(2166147);
        // groupIds.add(2166148);
        // groupIds.add(970599);// not me
        request.setGroupIds(groupIds);

        List<TradeStatViewItem> result = service.queryStat(request);
        for (TradeStatViewItem item : result) {
            System.out.println(item);
        }
        System.out.println("==========");
    }
}
