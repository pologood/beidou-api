package com.baidu.beidou.api.internal.dmp.exporter;

import java.util.List;

import com.baidu.beidou.api.internal.dmp.vo.DmpPlanResult;
import com.baidu.beidou.api.internal.dmp.vo.VtCodeResult;

/**
 * 支持DMP服务
 * 
 * @author caichao
 * 
 */
public interface DmpSupportService {
    /**
     * 获取计划列表
     * 
     * @param userid 用户ID
     * @return DmpPlanResult 接口返回对象
     */
    DmpPlanResult getPlanList(int userid);
    
    /**
     * 获取用户的主域列表
     * 
     * @param userId 用户ID
     * @return 主域列表
     */
    List<String> getUserDomain(int userId);
    
    /**
     * 获取用户人群代码接口
     * 
     * @param userId 用户ID
     * @param isAllSite 代码类型
     * @return 人群代码
     */
    List<VtCodeResult> getVtCodeList(int userId, int isAllSite);
}
