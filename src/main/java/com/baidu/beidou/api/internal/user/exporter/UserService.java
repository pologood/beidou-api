package com.baidu.beidou.api.internal.user.exporter;

import com.baidu.beidou.api.internal.user.vo.UserServResult;
import com.baidu.beidou.user.bo.Visitor;


public interface UserService {

	/**
	 * 用户opuser是否有对datauser进行操作的权限
	 * @param optype 权限tag。
	 * @param opuser
	 * @param datauser
	 * @return
	 */
	public boolean hasPrivilege(String optype, int opuser, int datauser);
	
    /**
     * 根据用户Id获得用户的基本信息，包括角色（包括UC和MCC）、权限、用户名等信息，封装为<code>Visitor</code>对象返回 <p/>
     * 
     * 被删除和被关闭的用户返回null，判断规则如下：<br/>
     * <code>
     * user.getUstate() == UserConstant.USER_STATE_DELETED || user.getUshifenstatid() == UserConstant.SHIFEN_STATE_CLOSE
     * </code>
     * 
     * <p/>
     * 该接口北斗会访问的第三方系统如下：<br/>
     * 1) UC的Mcpack API服务 <br/>
     * 2) MCC的memcache <br/>
     * 3) 北斗广告库的useraccount表（UC异步调用北斗API写入同步UC表和北斗用户表）检查状态
     * 
     * @param userId
     * @param isMccRole
     * @param mccUserName
     * 
     * @return 用户 Visitor
     * @see com.baidu.beidou.user.bo.Visitor
     */
    UserServResult<Visitor> getUserByUserId(int userId, boolean isMccRole, String mccUserName);
    
}