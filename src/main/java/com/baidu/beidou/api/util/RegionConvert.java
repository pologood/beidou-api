/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.util;

import com.baidu.beidou.api.external.cprogroup.vo.GroupRegionType;
import com.baidu.beidou.api.external.cprogroup.vo.RegionItemType;
import com.baidu.beidou.cprogroup.util.RegionIdConverter;

/**
 * 北斗、SYS地域ID互转
 * 
 * @author Wang Yu
 * 
 */
public class RegionConvert {
    /**
     * 北斗地域ID转换为SYS地域ID
     * 
     * @param groupRegionTypes 包含北斗地域ID的对象
     * @return 包含SYS地域ID的对象
     */
    public static GroupRegionType[] bdToSys(GroupRegionType[] groupRegionTypes) {
        GroupRegionType[] result = new GroupRegionType[groupRegionTypes.length];
        System.arraycopy(groupRegionTypes, 0, result, 0, groupRegionTypes.length);
        for (GroupRegionType region : result) {
            Integer sysRegId = RegionIdConverter.getSysRegIdFromBeidou(region.getRegionId());
            if (sysRegId == null) {
                region.setRegionId(0);
            } else {
                region.setRegionId(sysRegId);
            }
        }

        return result;
    }

    /**
     * 北斗地域ID转换为SYS地域ID
     * 
     * @param regionItemTypes 包含北斗地域ID的对象
     * @return 包含SYS地域ID的对象
     */
    public static RegionItemType[] bdToSys(RegionItemType[] regionItemTypes) {
        RegionItemType[] result = new RegionItemType[regionItemTypes.length];
        System.arraycopy(regionItemTypes, 0, result, 0, regionItemTypes.length);
        for (RegionItemType region : result) {
            if (region.getRegionId() <= 0) {
                continue;
            }
            Integer sysRegId = RegionIdConverter.getSysRegIdFromBeidou(region.getRegionId());
            if (sysRegId == null) {
                region.setRegionId(0);
            } else {
                region.setRegionId(sysRegId);
            }

        }

        return result;
    }
}
