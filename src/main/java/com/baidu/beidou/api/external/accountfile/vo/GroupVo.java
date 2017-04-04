package com.baidu.beidou.api.external.accountfile.vo;


/**
 * 
 * ClassName: GroupVo  <br>
 * Function: 推广组数据 <br>
 * 
 * 属性包括：GroupId,GroupName,CampaignId,推广组类型,推广组出价,状态,投放网络方式,定向方式,投放地域,过滤网站,过滤IP
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class GroupVo implements AbstractVo{
	
	private long groupId; // 在新增接口中，没有意义；升级接口中，必须有
	
	private long campaignId; // 新增接口中，必须含有campaignId
	
	private String groupName; //新增的推广组名
	
	private int price; //点击价格, 单位为分
	
	private Integer status; //状态，0：生效；1: 搁置；2：删除。对新增接口，只可以设置生效或搁置.

	private Integer type; //推广组类型	0:固定；1：悬浮；2：贴片
	
	private int isAllSite;
	
	private String sites;
	
	private String trades;
	
	private int targetType;
	
	private int isAllRegion;
	
	private String regionList;
	
	private String excludSites;
	
	private String exculdeIps;
	
	private int aliveDays;
	
	private int genderInfo;
	
	private int ktTargetType;
	
	private int isITEnabled;
	
	private int isSmart;
	
    private String phone = "-";

    private int phoneStatus;

    private int consult;

    private int consultStatus;

    private String messagePhone = "-";

    private String message = "-";

    private int messageStatus;
	
	public String[] toStringArray(){
		String[] str = new String[26];
		str[0] = String.valueOf(this.getGroupId());
		str[1] = String.valueOf(this.getGroupName());
		str[2] = String.valueOf(this.getCampaignId());
		str[3] = String.valueOf(this.getType());
		str[4] = String.valueOf(this.getPrice());
		str[5] = String.valueOf(this.getStatus());
		str[6] = String.valueOf(this.getIsAllSite());
		str[7] = String.valueOf(this.getSites());
		str[8] = String.valueOf(this.getTrades());
		str[9] = String.valueOf(this.getTargetType());
		str[10] = String.valueOf(this.getIsAllRegion());
		str[11] = String.valueOf(this.getRegionList());
		str[12] = String.valueOf(this.getExcludSites());
		str[13] = String.valueOf(this.getExculdeIps());
		str[14] = String.valueOf(this.getAliveDays());
		str[15] = String.valueOf(this.getGenderInfo());
		str[16] = String.valueOf(this.getKtTargetType());
		str[17] = String.valueOf(this.getIsITEnabled());
		str[18] = String.valueOf(this.getIsSmart());
		str[19] = String.valueOf(this.getPhone());
		str[20] = String.valueOf(this.getPhoneStatus());
		str[21] = String.valueOf(this.getConsult());
		str[22] = String.valueOf(this.getConsultStatus());
		str[23] = String.valueOf(this.getMessagePhone());
		str[24] = String.valueOf(this.getMessage());
		str[25] = String.valueOf(this.getMessageStatus());
		return str;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public int getIsAllSite() {
		return isAllSite;
	}

	public void setIsAllSite(int isAllSite) {
		this.isAllSite = isAllSite;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getIsAllRegion() {
		return isAllRegion;
	}

	public void setIsAllRegion(int isAllRegion) {
		this.isAllRegion = isAllRegion;
	}

	public String getExcludSites() {
		return excludSites;
	}

	public void setExcludSites(String excludSites) {
		this.excludSites = excludSites;
	}

	public String getExculdeIps() {
		return exculdeIps;
	}

	public void setExculdeIps(String exculdeIps) {
		this.exculdeIps = exculdeIps;
	}

	public String getRegionList() {
		return regionList;
	}

	public void setRegionList(String regionList) {
		this.regionList = regionList;
	}

	public int getAliveDays() {
		return aliveDays;
	}

	public void setAliveDays(int aliveDays) {
		this.aliveDays = aliveDays;
	}

	public String getSites() {
		return sites;
	}

	public void setSites(String sites) {
		this.sites = sites;
	}

	public String getTrades() {
		return trades;
	}

	public void setTrades(String trades) {
		this.trades = trades;
	}

	public int getGenderInfo() {
		return genderInfo;
	}

	public void setGenderInfo(int genderInfo) {
		this.genderInfo = genderInfo;
	}

	public int getKtTargetType() {
		return ktTargetType;
	}

	public void setKtTargetType(int ktTargetType) {
		this.ktTargetType = ktTargetType;
	}

	public int getIsITEnabled() {
		return isITEnabled;
	}

	public void setIsITEnabled(int isITEnabled) {
		this.isITEnabled = isITEnabled;
	}

	public int getIsSmart() {
		return isSmart;
	}

	public void setIsSmart(int isSmart) {
		this.isSmart = isSmart;
	}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(int phoneStatus) {
        this.phoneStatus = phoneStatus;
    }

    public int getConsult() {
        return consult;
    }

    public void setConsult(int consult) {
        this.consult = consult;
    }

    public int getConsultStatus() {
        return consultStatus;
    }

    public void setConsultStatus(int consultStatus) {
        this.consultStatus = consultStatus;
    }

    public String getMessagePhone() {
        return messagePhone;
    }

    public void setMessagePhone(String messagePhone) {
        this.messagePhone = messagePhone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }
}
