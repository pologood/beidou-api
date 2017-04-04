package com.baidu.beidou.api.external.cprogroup.vo.request;

/**
 * ClassName: GetRegionConfigRequest
 * Function: 获取投放地域设置，需要输入groupIds
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class GetRegionConfigRequest extends GroupIdsBaseRequest {
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

	private static final long serialVersionUID = 7263333173437126275L;

}
