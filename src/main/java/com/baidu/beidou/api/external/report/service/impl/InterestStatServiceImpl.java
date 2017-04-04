package com.baidu.beidou.api.external.report.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.report.comparator.AbstractViewItemComparator;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.InterestAssistant;
import com.baidu.beidou.api.external.report.vo.InterestStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.constant.InterestConstant;
import com.baidu.beidou.cprogroup.vo.GroupPackInterestKey;
import com.baidu.beidou.cprogroup.vo.InterestVo4Report;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.DateUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.InterestViewItem;

/**
 * ClassName: InterestStatServiceImpl Function: 获取兴趣报告数据
 * 
 * @author genglei
 * @version beidou-api 3 plus
 * @date 2012-9-25
 */
public class InterestStatServiceImpl extends GenericStatServiceImpl<InterestStatViewItem> implements
        GenericStatService<InterestStatViewItem> {
    
    @Resource
    private ReportLiteService reportLiteService;

    /**
     * queryStat: 获取兴趣报告数据，查询有展现的兴趣
     * 
     * @version beidou-api 3 plus
     * @author genglei01
     * @date 2012-9-25
     * 
     * @param request 查询参数
     * @return List<InterestStatViewItem> 兴趣报告数据列表
     */
    public List<InterestStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        // FIXME
        List<InterestViewItem> allData = reportLiteService.queryInterestLiteReport(buildQueryParam(request));

        List<InterestAssistant> infoData = CollectionUtil.createArrayList();
        // 将doris数据按天切割
        Map<String, List<InterestViewItem>> allDataByDate = CollectionUtil.createHashMap();
        if (CollectionUtil.isNotEmpty(allData)) {
            for (InterestViewItem item : allData) {
                Date date = DateUtils.olapStrToDate(item.getShowDate());
                String key = dateFormat.format(date);
                List<InterestViewItem> temp = allDataByDate.get(key);
                if (temp == null) {
                    temp = CollectionUtil.createArrayList();
                }
                temp.add(item);
                allDataByDate.put(key, temp);
            }
        }
        // 按天处理Olap数据
        for (String key : allDataByDate.keySet()) {
            // 如果Olap查询成功，生成所有数据返回
            List<InterestViewItem> data = allDataByDate.get(key);
            List<InterestAssistant> list = getViewItem(data);
            if (CollectionUtils.isNotEmpty(list)) {
                infoData.addAll(list);
            }
        }

        List<InterestStatViewItem> result = this.pagerInterestViewItemList(infoData);
        logger.info("InterestStatServiceImpl ：request=[{}] got {} result from Olap", request, result.size());
        if (result.size() > size) {
            result = null;
            throw new StorageServiceException();
        }

        // 按照时间排序
        Collections.sort(result, new AbstractViewItemComparator());
        return result;
    }

    public List<InterestAssistant> getViewItem(List<InterestViewItem> allData) throws Exception {
        List<InterestAssistant> result = CollectionUtil.createArrayList();
        try {
            if (CollectionUtils.isEmpty(allData)) {
                // 没有统计数据，则直接返回空
                return result;
            }

            // 获取用户名
            Map<Integer, User> accountInfoData = findAccountInfo(userId);
            if (accountInfoData.get(userId) == null) {
                logger.error("PackStatServiceImpl got empty user info result from db");
                return result;
            }
            String userName = accountInfoData.get(userId).getUsername();

            // 1、将兴趣点和兴趣组合记录放入map中，供过滤使用，并获取List<grouid,List<iid>>，待传给ITFacade
            Map<GroupPackInterestKey, Map<Integer, InterestViewItem>> iidsMap = CollectionUtil.createHashMap();
            Map<GroupPackInterestKey, List<Integer>> shownInterestMap = CollectionUtil.createHashMap();
            List<Integer> refPackIdList = CollectionUtil.createArrayList();

            for (InterestViewItem stat : allData) {
                Integer iid = stat.getInterestId();
                Long gpId = stat.getGpId();
                Integer groupId = stat.getGroupId();
                Integer refpackId = stat.getRefpackId();
                GroupPackInterestKey groupPackKey = new GroupPackInterestKey(groupId, gpId);

                // 记录gpId，以及refpackId
                if (gpId != 0 && refpackId != 0) {
                    refPackIdList.add(refpackId);
                }

                // 将doris每条记录存入map，供过滤使用
                Map<Integer, InterestViewItem> groupInteretsMap = iidsMap.get(groupPackKey);
                if (groupInteretsMap == null) {
                    groupInteretsMap = CollectionUtil.createHashMap();
                    iidsMap.put(groupPackKey, groupInteretsMap);
                }
                groupInteretsMap.put(iid, stat);

                // 将doris每条记录存入List，待传给ITFacade
                List<Integer> list = shownInterestMap.get(groupPackKey);
                if (list == null) {
                    list = CollectionUtil.createArrayList();
                    shownInterestMap.put(groupPackKey, list);
                }
                list.add(iid);
            }

            Map<Integer, String> packNameMapping = CollectionUtil.createHashMap();
            try {
                Map<Integer, Map<String, Object>> packInfoMap =
                        groupPackFacade.getPackNameByRefPackId(userId, refPackIdList);
                for (Integer id : packInfoMap.keySet()) {
                    Map<String, Object> packInfo = packInfoMap.get(id);
                    if (packInfo != null) {
                        if (packInfo.get("name") != null) {
                            packNameMapping.put(id, (String) packInfo.get("name"));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Find pack name by ref pack id failed. " + e.getMessage(), e);
            }

            logger.info("Get pack name successfully");

            // 通过CproItFacade接口，获得具有父子关系的有展现列表的兴趣树
            Map<GroupPackInterestKey, List<InterestVo4Report>> interestVoMap =
                    cproITFacade.querySrchsInterest(shownInterestMap, userId);

            // 遍历CproItFacade返回的兴趣树列表，构造report显示VOList并过滤
            if (!interestVoMap.isEmpty()) {
                InterestViewItem stat;
                for (Map.Entry<GroupPackInterestKey, List<InterestVo4Report>> entry : interestVoMap.entrySet()) {
                    GroupPackInterestKey groupPackId = entry.getKey();
                    List<InterestVo4Report> interestVoList = entry.getValue();

                    for (InterestVo4Report interestVo : interestVoList) {
                        InterestAssistant interestAssistant = null;
                        InterestStatViewItem viewItem = new InterestStatViewItem();
                        stat = iidsMap.get(groupPackId).get(interestVo.getId());
                        viewItem.fillStatRecord(stat);
                        viewItem.setUserId(userId);
                        viewItem.setUserName(userName);
                        viewItem.setPlanId(interestVo.getPlanId());
                        viewItem.setPlanName(interestVo.getPlanName());
                        viewItem.setGroupId(interestVo.getGroupId());
                        viewItem.setGroupName(interestVo.getGroupName());
                        viewItem.setGpId(interestVo.getGpId());
                        viewItem.setInterestId(interestVo.getId());
                        viewItem.setInterestName(interestVo.getName());

                        interestAssistant = new InterestAssistant((Integer) interestVo.getId());
                        boolean isInterest = false;
                        if (interestVo.getId() <= InterestConstant.MAX_INTEREST_ID) {
                            viewItem.setInterestType(ReportWebConstants.INTEREST_TYPE_FIRST);
                            isInterest = true;
                        } else {
                            viewItem.setInterestType(ReportWebConstants.INTEREST_TYPE_CUSTOM);
                        }
                        if (interestVo.isDeleted()) {
                            viewItem.setInterestName(viewItem.getInterestName()
                                    + ReportWebConstants.HAS_BEEN_DELETE_SUFFIX);
                        }
                        interestAssistant.setSelfInterest(viewItem);

                        Integer refpackId = 0;
                        String packName = null;
                        // 构造子兴趣节点
                        Date statDate = null;
                        if (interestVo.getChildren() != null) {
                            for (InterestVo4Report childInterest : interestVo.getChildren()) {
                                InterestStatViewItem childViewItem = new InterestStatViewItem();
                                stat = iidsMap.get(groupPackId).get(childInterest.getId());
                                childViewItem.fillStatRecord(stat);
                                childViewItem.setUserId(userId);
                                childViewItem.setUserName(userName);
                                childViewItem.setPlanId(childInterest.getPlanId());
                                childViewItem.setPlanName(childInterest.getPlanName());
                                childViewItem.setGroupId(childInterest.getGroupId());
                                childViewItem.setGroupName(childInterest.getGroupName());
                                childViewItem.setGpId(childInterest.getGpId());
                                childViewItem.setInterestId(childInterest.getId());
                                childViewItem.setInterestName(childInterest.getName());
                                childViewItem.setInterestType(ReportWebConstants.INTEREST_TYPE_SECOND);
                                // FIXME
                                if (stat == null) {
                                    continue;
                                }

                                // 设置日期
                                if (statDate == null) {
                                    statDate = DateUtil.parseDate(stat.getShowDate(), DateUtil.CHINESE_PATTERN);
                                }
                                childViewItem.setDay(dateFormat.format(statDate));

                                if (childInterest.isDeleted()) {
                                    viewItem.setInterestName(viewItem.getInterestName()
                                            + ReportWebConstants.HAS_BEEN_DELETE_SUFFIX);
                                }

                                // 设置受众组合名称
                                // 获取refPackId，如果未曾获取到refPackId（为null），则获取一次并设置；packName也是同样的
                                if (refpackId == 0 && stat != null && stat.getRefpackId() != null) {
                                    refpackId = stat.getRefpackId();
                                }
                                if (packName == null) {
                                    packName = packNameMapping.get(refpackId);
                                }

                                if (refpackId == 0 || packName == null) {
                                    childViewItem.setPackName(ReportWebConstants.INTEREST_NO_PACKNAME);
                                } else {
                                    childViewItem.setPackName(packName);
                                }
                                interestAssistant.addSecondInterest(childViewItem);
                            }
                        }
                        // FIXME
                        if (stat == null) {
                            continue;
                        }

                        // 设置日期
                        if (statDate == null) {
                            statDate = DateUtil.parseDate(stat.getShowDate(), DateUtil.CHINESE_PATTERN);
                        }
                        viewItem.setDay(dateFormat.format(statDate));

                        // 设置受众组合名称
                        // 存在如下情况：统计数据存在二级兴趣点数据，而返回前端数据需要已经兴趣点数据，此时refPackId使用其二级兴趣点的
                        if (refpackId == 0 && stat != null && stat.getRefpackId() != null) {
                            refpackId = stat.getRefpackId();
                        }
                        if (packName == null) {
                            packName = packNameMapping.get(refpackId);
                        }
                        if (isInterest) {
                            if (refpackId == 0 || packName == null) {
                                viewItem.setPackName(ReportWebConstants.INTEREST_NO_PACKNAME);
                            } else {
                                viewItem.setPackName(packName);
                            }
                        } else {
                            if (refpackId == 0 || packName == null) {
                                viewItem.setPackName(viewItem.getInterestName());
                            } else {
                                viewItem.setPackName(packName);
                            }
                        }

                        interestAssistant.ensureStatFields();
                        result.add(interestAssistant);
                    }
                }
            }
        } catch (NullPointerException e) {
            logger.error("Query interest report failed " + e.getMessage(), e);
            throw e;
        }

        return result;
    }

    /**
     * 获取前端显示VO的List方法
     * 
     * @param infoData
     * @return
     */
    private List<InterestStatViewItem> pagerInterestViewItemList(List<InterestAssistant> infoData) {
        List<InterestStatViewItem> result = CollectionUtil.createArrayList();
        for (InterestAssistant ia : infoData) {
            result.addAll(ia.getInterestViewItemList());
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        InterestStatServiceImpl service = null;
        String beanName = "interestStatService";
        service = (InterestStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20130106");
        Date endDate = DateUtils.strToDate("20130110");
        request.setUserid(18);
        List<Integer> groupIds = CollectionUtil.createArrayList();
        groupIds.add(4302878);
        request.setGroupIds(groupIds);

        request.setReportType(ReportWebConstants.REPORT_TYPE.INTEREST_SHOWN);
        request.setStatRange(ReportWebConstants.REPORT_RANGE.GROUP);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        List<InterestStatViewItem> result = service.queryStat(request);
        for (InterestStatViewItem item : result) {
            System.out.println(item);
        }
        System.out.println("===========");
    }
}
