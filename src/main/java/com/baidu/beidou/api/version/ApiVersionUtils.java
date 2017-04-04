/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.version;

import static com.baidu.beidou.api.version.ApiVersionConstant.DEFAULT_VERSION;

import com.baidu.beidou.api.external.util.vo.ApiOption;

/**
 * API版本解析工具类
 * 
 * @author Wang Yu
 * 
 */
public class ApiVersionUtils {
    public static double getApiVersion(ApiOption apiOption) {
        String strVersion = apiOption.getOptions().get("version");
        if (strVersion != null) {
            return Double.parseDouble(strVersion);
        }
        return DEFAULT_VERSION;
    }
}
