package com.baidu.beidou.api.external.report.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.baidu.beidou.api.external.report.comparator.AbstractViewItemComparator;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.PackStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.vo.GroupPackVo;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.DateUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.GroupPackViewItem;

/**
 * ClassName: PackageStatServiceImpl Function: 获取受众组合报告数据
 * 
 * @author genglei
 * @version beidou-api 3 plus
 * @date 2012-9-26
 */
public class PackageStatServiceImpl extends GenericStatServiceImpl<PackStatViewItem> implements
        GenericStatService<PackStatViewItem> {

    @Resource
    private ReportLiteService reportLiteService;

    /**
     * queryStat: 获取受众组合报告数据，查询有展现的受众组合
     * 
     * @version beidou-api 3 plus
     * @author genglei01
     * @date 2012-9-21
     * 
     * @param request 查询参数
     * @return List<PackStatViewItem> 受众组合报告数据列表
     */
    public List<PackStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<GroupPackViewItem> allData = reportLiteService.queryPackLiteReport(buildQueryParam(request));
        logger.info("PackageStatServiceImpl ：request=[{}] got {} result from Olap", request, allData.size());
        if (allData.size() > size) {
            logger.warn(dataSizeTooLarge);
            allData = null;
            throw new StorageServiceException(dataSizeTooLarge, null);
        }
        // 如果Olap查询成功，生成所有数据返回
        List<PackStatViewItem> result = getViewItem(allData);

        // 按照时间排序
        Collections.sort(result, new AbstractViewItemComparator());
        return result;
    }

    public List<PackStatViewItem> getViewItem(List<GroupPackViewItem> allData) throws Exception {
        List<PackStatViewItem> result = CollectionUtil.createArrayList();

        if (CollectionUtil.isEmpty(allData)) {
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

        // 获取当前层级的所有受众组合列表
        List<GroupPackVo> groupPackList = CollectionUtil.createArrayList();
        if (CollectionUtil.isEmpty(groupIds)) {
            if (CollectionUtil.isEmpty(planIds)) {
                groupPackList = groupPackFacade.findGroupPackVoByLevel(userId, null, null);
            } else {
                for (Integer planId : planIds) {
                    List<GroupPackVo> tmpGroupPackList = groupPackFacade.findGroupPackVoByLevel(userId, planId, null);
                    groupPackList.addAll(tmpGroupPackList);
                }
            }
        } else {
            for (Integer groupId : groupIds) {
                List<GroupPackVo> tmpGroupPackList = groupPackFacade.findGroupPackVoByLevel(userId, null, groupId);
                groupPackList.addAll(tmpGroupPackList);
            }
        }
        Map<Long, GroupPackVo> groupPackMap = GroupPackVo.list2Map(groupPackList);

        // 获用户所有推广组ID，用于过滤Olap数据，防止出现错误数据，此处应该缩小查询范围。
        Set<Integer> packGroupIds = CollectionUtil.createHashSet(cproGroupMgr.findGroupIdsByUserId(userId));

        // 根据groupIds和planIds查找对应的推广组和推广计划名称
        Set<Integer> planIds = CollectionUtil.createHashSet();
        Set<Integer> groupIds = CollectionUtil.createHashSet();
        for (GroupPackVo pack : groupPackList) {
            planIds.add(pack.getPlanId());
            groupIds.add(pack.getGroupId());
        }
        for (GroupPackViewItem item : allData) {
            planIds.add(item.getPlanId());
            groupIds.add(item.getGroupId());
        }
        Map<Integer, String> planIdNameMapping = cproPlanMgr.findPlanNameByPlanIds(planIds);
        Map<Integer, String> groupIdNameMapping = cproGroupMgr.findGroupNameByGroupIds(groupIds);

        // 组装数据并且过滤，如果有统计数据则进行后续的数据合并操作；
        Integer statPlanId;
        Integer statGroupId;
        Long statGpId;
        Integer statRefPackId;
        Date statDate;
        GroupPackVo groupPack;
        List<Long> packOptimizedGpIdList = CollectionUtil.createArrayList();

        // 存放已删除但有统计信息的受众统计信息，key为gpId，value为统计信息
        Map<Long, GroupPackViewItem> deletedPackMap = CollectionUtil.createHashMap();
        Set<Integer> deletedRefpackIds = CollectionUtil.createHashSet();

        // 遍历统计数据，如果相应受众数据库中不存在则放入删除Map，待后续处理
        for (GroupPackViewItem item : allData) {
            statPlanId = item.getPlanId();
            statGroupId = item.getGroupId();
            statGpId = item.getGpId();
            statRefPackId = item.getRefPackId();
            statDate = DateUtil.parseDate(item.getShowDate(), DateUtil.CHINESE_PATTERN);

            // 如果推广组ID不在推广组ID的集合中，直接continue
            if (!packGroupIds.contains(statGroupId)) {
                throw new InternalException("Error occurred when contructing pack view item, gpId=[" + statGpId
                        + "] not belong to groupId=[" + statGroupId + "]");
            }

            groupPack = groupPackMap.remove(statGpId);
            if (groupPack == null) {
                // 如果受众组合关系已被刪除，则记录之，以备后续查数据库
                // 删除的受众组合不做已优化的判断
                deletedPackMap.put(statGpId, item);
                deletedRefpackIds.add(statRefPackId);
            } else {
                // 没有删除则生成待显示的VO对象
                PackStatViewItem view = new PackStatViewItem();
                view.fillStatRecord(item);
                view.setUserId(userId);
                view.setUserName(userName);
                view.setPlanId(statPlanId);
                if (planIdNameMapping.get(statPlanId) == null) {
                    throw new InternalException("Error occurred when contructing pack view item, gpId=[" + statGpId
                            + "] planName cannot be found, planId=[" + statPlanId + "]");
                }
                view.setPlanName(planIdNameMapping.get(statPlanId));
                view.setGroupId(statGroupId);
                if (groupIdNameMapping.get(statGroupId) == null) {
                    throw new InternalException("Error occurred when contructing pack view item, gpId=[" + statGpId
                            + "] groupName cannot be found, groupId=[" + statGroupId + "]");
                }
                view.setGroupName(groupIdNameMapping.get(statGroupId));
                view.setGpId(statGpId);
                view.setPackName(groupPack.getName());
                view.setDay(dateFormat.format(statDate));
                result.add(view);

                // 记录gpId，以便获取是否为已优化
                packOptimizedGpIdList.add(statGpId);
            }
        }

        // 处理已删除受众组合
        if (!deletedPackMap.isEmpty()) {
            List<Integer> refPackIdList = CollectionUtil.createArrayList(deletedRefpackIds);
            Map<Integer, Map<String, Object>> packInfoMap =
                    groupPackFacade.getPackNameByRefPackId(userId, refPackIdList);

            for (GroupPackViewItem item : deletedPackMap.values()) {
                statPlanId = item.getPlanId();
                statGroupId = item.getGroupId();
                statGpId = item.getGpId();
                statRefPackId = item.getRefPackId();
                statDate = DateUtils.olapStrToDate(item.getShowDate());

                // 没有删除则生成待显示的VO对象
                PackStatViewItem view = new PackStatViewItem();
                view.fillStatRecord(item);
                view.setUserId(userId);
                view.setUserName(userName);
                view.setPlanId(statPlanId);
                if (planIdNameMapping.get(statPlanId) == null) {
                    throw new InternalException("Error occurred when contructing pack view item, gpId=[" + statGpId
                            + "] planName cannot be found, planId=[" + statPlanId + "]");
                }
                view.setPlanName(planIdNameMapping.get(statPlanId));
                view.setGroupId(statGroupId);
                if (groupIdNameMapping.get(statGroupId) == null) {
                    throw new InternalException("Error occurred when contructing pack view item, gpId=[" + statGpId
                            + "] groupName cannot be found, groupId=[" + statGroupId + "]");
                }
                view.setGroupName(groupIdNameMapping.get(statGroupId));
                view.setGpId(statGpId);
                Map<String, Object> packInfo = packInfoMap.get(statRefPackId);
                if (packInfo != null) {
                    view.setPackName((String) packInfo.get("name") + ReportWebConstants.HAS_BEEN_DELETE_SUFFIX);
                }
                view.setDay(dateFormat.format(statDate));

                result.add(view);
            }
        }

        // 填充是否已优化标记
        this.fillPackOptimized(result, packOptimizedGpIdList);

        return result;
    }

    /**
     * fillPackOptimized: 填充是否已优化标记
     * 
     * @version beidou-api 3 plus
     * @author genglei01
     * @date 2012-9-21
     */
    private void fillPackOptimized(List<PackStatViewItem> infoData, List<Long> packOptimizedGpIdList) {
        Map<Long, Boolean> packOptimizedMap = groupPackFacade.checkPackOptimizedByGpIds(packOptimizedGpIdList);

        for (PackStatViewItem item : infoData) {
            Boolean flag = packOptimizedMap.get(item.getGpId());

            if (flag != null && flag) {
                item.setPackName(item.getPackName() + ReportWebConstants.HAS_BEEN_MODIFIED_SUFFIX);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        PackageStatServiceImpl service = null;
        String beanName = "packageStatService";
        service = (PackageStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20120806");
        Date endDate = DateUtils.strToDate("20120923");
        request.setUserid(18);
        request.setReportType(ReportWebConstants.REPORT_TYPE.PACK);
        request.setStatRange(ReportWebConstants.REPORT_RANGE.ACCOUNT);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        List<PackStatViewItem> result = service.queryStat(request);
        for (PackStatViewItem item : result) {
            System.out.println(item);
        }
    }
}
