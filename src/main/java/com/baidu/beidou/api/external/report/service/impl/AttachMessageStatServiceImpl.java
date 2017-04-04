package com.baidu.beidou.api.external.report.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;

import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.AttachMessageStatViewItem;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.atom.client.AtomDriver;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.AttachViewItem;
import com.baidu.unbiz.soma.module.reportmodule.report.common.vo.QueryParam;
import com.baidu.unbiz.soma.module.reportmodule.report.constant.AttachInfoConstant;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * ClassName: AttachMessageStatServiceImpl <br>
 * Function: 附加信息-短信 service
 *
 * @author wangxujin
 */
public class AttachMessageStatServiceImpl extends GenericStatServiceImpl<AttachMessageStatViewItem> implements
        GenericStatService<AttachMessageStatViewItem> {

    @Resource(name = "reportLiteService")
    private ReportLiteService reportLiteService;

    @Resource(name = "atomDriver")
    private AtomDriver atomDriver;

    /**
     * 查询附加信息-短信接口数据
     *
     * @param request 报告模块查询参数
     *
     * @return
     *
     * @throws Exception
     */
    @Override
    public List<AttachMessageStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        QueryParam queryParam = buildQueryParam(request);
        queryParam.setAttachType(AttachInfoConstant.ATTACH_INFO_MESSAGE);
        List<AttachViewItem> allData = reportLiteService.queryAttachInfoLiteReport(queryParam);

        logger.info("AttachMessageStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());

        List<AttachMessageStatViewItem> result = Lists.newArrayList();
        for (AttachViewItem item : allData) {
            AttachMessageStatViewItem statItem = new AttachMessageStatViewItem();
            Date date = DateUtils.olapStrToDate(item.getShowDate());
            statItem.setDay(dateFormat.format(date));
            statItem.setUserId(userId);
            statItem.setPlanId(item.getPlanId());
            statItem.setGroupId(item.getGroupId());
            statItem.setMessageId(item.getAttachId());
            Map<Long, String> map = atomDriver.getWordNameMap(Sets.newHashSet(item.getAttachId()));
            if (MapUtils.isNotEmpty(map) && map.get(item.getAttachId()) != null) {
                statItem.setMessage(map.get(item.getAttachId()));
            } else {
                statItem.setMessage("未知");
            }
            statItem.fillStatRecord(item);
            result.add(statItem);
        }

        if (!idOnly) {
            result = getEmptyViewItem(result);
        }
        logger.info("AttachMessageStatServiceImpl ：request=[{}] contruct {} info data from db", request, result.size());

        return result;
    }

    /**
     * 获取 展现、点击、消费、ctr、cpm、acp数据的有展现网站信息 <br>
     * Note：非id only
     *
     * @param result 有展现网站数据列表
     *
     * @return
     *
     * @throws Exception
     */
    public List<AttachMessageStatViewItem> getEmptyViewItem(List<AttachMessageStatViewItem> result) throws Exception {
        // Step1.
        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("AttachPhoneStatServiceImpl got empty user info result from db");
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

        for (AttachMessageStatViewItem stat : result) {
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
}
