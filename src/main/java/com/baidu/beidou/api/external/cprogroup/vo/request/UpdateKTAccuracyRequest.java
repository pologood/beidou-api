package com.baidu.beidou.api.external.cprogroup.vo.request;

import com.baidu.beidou.api.external.cprogroup.vo.GroupKTAccuracyType;
import com.baidu.beidou.api.external.util.request.ApiRequest;
import com.baidu.unbiz.common.CollectionUtil;

import java.util.List;

/**
 * 修改优质流量设置API请求Model
 *
 * @author huangjinkun.
 * @date 16/2/19
 * @time 下午3:45
 */
public class UpdateKTAccuracyRequest implements ApiRequest {
    private List<GroupKTAccuracyType> ktAccuracies;

    public List<GroupKTAccuracyType> getKtAccuracies() {
        return ktAccuracies;
    }

    public void setKtAccuracies(List<GroupKTAccuracyType> ktAccuracies) {
        this.ktAccuracies = ktAccuracies;
    }

    @Override
    public int getDataSize() {
        if (CollectionUtil.isEmpty(ktAccuracies)) {
            return 0;
        }
        return ktAccuracies.size();
    }
}
