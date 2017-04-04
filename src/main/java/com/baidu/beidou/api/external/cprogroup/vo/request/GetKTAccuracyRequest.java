package com.baidu.beidou.api.external.cprogroup.vo.request;

import com.baidu.beidou.api.external.util.request.ApiRequest;
import org.apache.commons.lang.ArrayUtils;

import java.io.Serializable;

/**
 * 获取优质流量设置API请求Model
 *
 * @author huangjinkun.
 * @date 16/2/19
 * @time 下午3:20
 */
public class GetKTAccuracyRequest implements Serializable, ApiRequest {

    private static final long serialVersionUID = 2554788069464803960L;

    private int[] groupIds;

    @Override
    public int getDataSize() {
        int result = 0;

        if (!ArrayUtils.isEmpty(groupIds)) {
            result = groupIds.length;
        }

        return result;
    }

    public int[] getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(int[] groupIds) {
        this.groupIds = groupIds;
    }
}
