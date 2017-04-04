package com.baidu.beidou.api.internal.util.exporter.impl;

import com.baidu.beidou.api.internal.util.exporter.CacheRefreshService;
import com.baidu.beidou.cprogroup.service.UnionSiteMgr;

/**
 * localcache刷新服务
 *
 * @author wangyu45
 */
public class CacheRefreshServiceImpl implements CacheRefreshService {

    private UnionSiteMgr unionSiteMgr;

    @Override
    public synchronized void refreshBeidouToSysMap() {
        unionSiteMgr.loadBeidouToSysMap();
    }

    @Override
    public synchronized void reloadRegionCache() {
        unionSiteMgr.loadRegInfo();
    }

    @Override
    public synchronized void reloadSiteTradeCache() {
        unionSiteMgr.loadSiteTradeInfo();
    }

    public void setUnionSiteMgr(UnionSiteMgr unionSiteMgr) {
        this.unionSiteMgr = unionSiteMgr;
    }
}
