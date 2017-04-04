/**
 * 2009-8-19 04:20:27
 * @author zengyunfeng
 */
package com.baidu.beidou.api.internal.user.exporter.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.internal.user.exporter.UserService;
import com.baidu.beidou.api.internal.user.vo.UserServResult;
import com.baidu.beidou.user.bo.Visitor;
import com.baidu.beidou.user.exception.UserStateDisableException;
import com.baidu.beidou.user.service.UserMgr;



/**
 * @author zengyunfeng
 *
 */
public class UserServiceImpl implements UserService {
    
    private static final Log LOG = LogFactory.getLog(UserServiceImpl.class);
	
	private UserMgr userMgr = null;
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.baidu.beidou.user.exporter.impl.UserService#hasPrivilege(String, int, int)
	 */
	public boolean hasPrivilege(String opTag, 
			int privilegeUser, int dataUser){
		if(org.apache.commons.lang.StringUtils.isEmpty(opTag)){
			return false;
		}
		
		return userMgr.hasDataPrivilege(opTag, privilegeUser, dataUser);
		
	}
	
	/**
     * (non-Javadoc)
     * 
     * @see com.baidu.beidou.user.exporter.impl.UserService#getUserByUserId(int, boolean, String)
     */
    @Override
    public UserServResult<Visitor> getUserByUserId(int userId, boolean isMccRole, String mccUserName) {
        UserServResult<Visitor> result = UserServResult.create();
        if (userId < 0) {
            return result;
        }
        try {
            result.setData(userMgr.getUserByUserId(userId, isMccRole, mccUserName));
        } catch (UserStateDisableException e) {
            result.setErrCode(UserServResult.RPC_FAIL);
            result.setErrMsg("User state is disabled. " + e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            result.setErrCode(UserServResult.RPC_FAIL);
            result.setErrMsg(e.getMessage());
        }

        return result;
    }
	
	/**
	 * @return the userMgr
	 */
	public UserMgr getUserMgr() {
		return userMgr;
	}

	/**
	 * @param userMgr the userMgr to set
	 */
	public void setUserMgr(UserMgr userMgr) {
		this.userMgr = userMgr;
	}

}
