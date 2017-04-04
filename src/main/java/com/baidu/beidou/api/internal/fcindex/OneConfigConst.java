/**
 * 
 */
package com.baidu.beidou.api.internal.fcindex;

/**
 * @author zhuzhenxing@baidu.com
 *
 */
public class OneConfigConst {
    
    private boolean cacheEnable = false;
    public static String TaxCachePrefix = "TAX";
    
	// 2 hours
    private int expireTime = 2 * 60 * 60;
    
    public boolean isCacheEnable() {
        return cacheEnable;
    }
    public void setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }
    public int getExpireTime() {
        return expireTime;
    }
    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }
    public String getTaxCachePrefix() {
		return TaxCachePrefix;
	}
	public void setTaxCachePrefix(String taxCachePrefix) {
		TaxCachePrefix = taxCachePrefix;
	}
}
