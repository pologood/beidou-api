package com.baidu.beidou.api.external.site.vo;

/**
 * 
 * ClassName: SiteVo  <br>
 * Function: 联盟站点数据 <br>
 * 
 */
public class SiteVo {
	
	private String siteUrl;

	private int firstTradeId;

	private int secondTradeId;
	
	private String siteName;
	
	/**
	 * 支持的物料类型，按位或取值，位意义：1：文字；2：文字/图片; 3:仅图片
	 */
	private byte wuliaoType;  
	
	/**
	 * 支持的图片物料展现形式，按位或取值，位意义：1：固定，2、悬浮，4、贴片
	 */
	private byte displayType;  
	
	/**
	 * 网站支持的图片类型id，每一位为一个值，当前有24种，最大值为2^24-1=1|2|4|8...|2^23。图片类型id字典见北斗cap库的sitesize表。
	 */
	private long adSize;  
	
	/**
	 * 有展现的广告尺寸id对应的展现量，从数据库中取出的值为0|0|0|0|0|2|0|0|0|2|1|0|0|1|0|0|0|0|0|0|0|0|0|0个数为支持的Size个数（当前为24），以“|”分割，
     * 
     * 各个尺寸的等级取值范围为【0-8】 
     * 1： "0-1万",
     * 2："1-5万", 
     * 3："5-10万", 
     * 4："10万以上", 
     * 5："0-50万", 
     * 6："50-100万", 
     * 7："100-1000万", 
     * 8："1000万以上" 
	 */
	private String adThruput; 
	
	/**
	 * 推广日展现量，取值范围：1：0-10万 ，2：10-100万 ，3： 100-1000万 4： 1000万以上
	 */
	private int srchPv;  
	
	/**
	 * 推广覆盖独立IP访问量，取值范围：1：0-1万，2：1-5万，3：5-10万，4：10万以上
	 */
	private int uv; 
	
	/**
	 * 站点来源
	 * 整数，按位表示：1代表百度联盟站点，2代表来源于google流量
	 */
	private int siteSource;

	public String[] toStringArray() {
		String[] str = new String[11];
		str[0] = String.valueOf(this.getSiteUrl());
		str[1] = String.valueOf(this.getSiteName());
		str[2] = String.valueOf(this.getFirstTradeId());
		str[3] = String.valueOf(this.getSecondTradeId());
		str[4] = String.valueOf(this.getWuliaoType());
		str[5] = String.valueOf(this.getDisplayType());
		str[6] = String.valueOf(this.getAdSize());
		str[7] = String.valueOf(this.getAdThruput());
		str[8] = String.valueOf(this.getSrchPv());
		str[9] = String.valueOf(this.getUv());
		str[10] = String.valueOf(this.getSiteSource());
		return str;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public int getFirstTradeId() {
		return firstTradeId;
	}

	public void setFirstTradeId(int firstTradeId) {
		this.firstTradeId = firstTradeId;
	}

	public int getSecondTradeId() {
		return secondTradeId;
	}

	public void setSecondTradeId(int secondTradeId) {
		this.secondTradeId = secondTradeId;
	}

	public byte getWuliaoType() {
		return wuliaoType;
	}

	public void setWuliaoType(byte wuliaoType) {
		this.wuliaoType = wuliaoType;
	}

	public byte getDisplayType() {
		return displayType;
	}

	public void setDisplayType(byte displayType) {
		this.displayType = displayType;
	}

	public long getAdSize() {
		return adSize;
	}

	public void setAdSize(long adSize) {
		this.adSize = adSize;
	}

	public String getAdThruput() {
		return adThruput;
	}

	public void setAdThruput(String adThruput) {
		this.adThruput = adThruput;
	}

	public int getSrchPv() {
		return srchPv;
	}

	public void setSrchPv(int srchPv) {
		this.srchPv = srchPv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public int getSiteSource() {
		return siteSource;
	}

	public void setSiteSource(int siteSource) {
		this.siteSource = siteSource;
	}
}
