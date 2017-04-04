package com.baidu.beidou.api.internal.util.exporter;

/**
 * localcache 刷新服务
 *
 * @author Administrator
 */
public interface CacheRefreshService {
    /**
     * 刷新北斗-SYS地域映射缓存
     */
    public void refreshBeidouToSysMap();

    /**
     * 重新加载地域缓存
     */
    public void reloadRegionCache();

    /**
     * 重新加载网站行业缓存
     */
    public void reloadSiteTradeCache();
}
