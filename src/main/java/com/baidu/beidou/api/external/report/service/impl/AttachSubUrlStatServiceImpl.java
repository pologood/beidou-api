package com.baidu.beidou.api.external.report.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.AttachSubUrlStatViewItem;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.exception.GroupAttachInfoException;
import com.baidu.beidou.cprogroup.service.GroupAttachInfoMgr;
import com.baidu.beidou.cprogroup.vo.AttachInfoVo;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.AttachViewItem;
import com.baidu.unbiz.soma.module.reportmodule.report.common.vo.QueryParam;
import com.baidu.unbiz.soma.module.reportmodule.report.constant.AttachInfoConstant;
import com.google.common.collect.Lists;

/**
 * ClassName: AttachSubUrlStatServiceImpl <br>
 * Function: 附加信息-子链 service
 *
 * @author wangxujin
 */
public class AttachSubUrlStatServiceImpl extends GenericStatServiceImpl<AttachSubUrlStatViewItem> implements
        GenericStatService<AttachSubUrlStatViewItem> {

    @Resource(name = "reportLiteService")
    private ReportLiteService reportLiteService;

    @Resource(name = "groupAttachInfoMgr")
    private GroupAttachInfoMgr groupAttachInfoMgr;

    /**
     * 查询附加信息-子链接口数据
     *
     * @param request 报告模块查询参数
     *
     * @return
     *
     * @throws Exception
     */
    @Override
    public List<AttachSubUrlStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        QueryParam queryParam = buildQueryParam(request);
        queryParam.setAttachType(AttachInfoConstant.ATTACH_INFO_SUB_URL);
        List<AttachViewItem> allData = reportLiteService.queryAttachInfoLiteReport(queryParam);

        logger.info("AttachSubUrlStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());

        List<AttachSubUrlStatViewItem> result = Lists.newArrayList();
        for (AttachViewItem item : allData) {
            AttachSubUrlStatViewItem statItem = new AttachSubUrlStatViewItem();
            Date date = DateUtils.olapStrToDate(item.getShowDate());
            statItem.setDay(dateFormat.format(date));
            statItem.setUserId(userId);
            statItem.setPlanId(item.getPlanId());
            statItem.setGroupId(item.getGroupId());
            // ubmc
            AttachInfoVo attach;
            try {
                attach = groupAttachInfoMgr.getSubUrlInfoFromUbmc(item.getGroupId());
            } catch (GroupAttachInfoException ex) {
                logger.warn("AttachSubUrlStatServiceImpl ：from ubmc " + ex);
                attach = null;
            }
            if (attach == null || StringUtils.isEmpty(attach.getAttachSubUrlTitle())) {
                statItem.setSubUrlTitle("子链");
            } else {
                statItem.setSubUrlTitle(attach.getAttachSubUrlTitle());
            }
            statItem.fillStatRecord(item);
            result.add(statItem);
        }

        if (!idOnly) {
            result = getEmptyViewItem(result);
        }
        logger.info("AttachSubUrlStatServiceImpl ：request=[{}] contruct {} info data from db", request, result.size());

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
    public List<AttachSubUrlStatViewItem> getEmptyViewItem(List<AttachSubUrlStatViewItem> result) throws Exception {
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

        for (AttachSubUrlStatViewItem stat : result) {
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
