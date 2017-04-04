package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.RegionConfigType;
import com.baidu.api.sem.nms.v2.RegionItemType;
import com.baidu.api.sem.nms.v2.SetRegionConfigRequest;
import com.baidu.api.sem.nms.v2.SetRegionConfigResponse;

/**
 * ClassName: SetRegionConfigExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-13
 */
public class SetRegionConfigExample {
	private GroupConfigService service;

	public SetRegionConfigExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public SetRegionConfigResponse setRegionConfig() {
		// Prepare your parameters.
		SetRegionConfigRequest parameters = new SetRegionConfigRequest();
		RegionConfigType regionConfig = new RegionConfigType();
		// Set your group id.
		regionConfig.setGroupId(2166147);
		// Set if not all region.
		regionConfig.setAllRegion(false);
		// Set region type, 1000: Beijing, 2000: Shanghai
		RegionItemType regionType = new RegionItemType();
		regionType.setType(1);
		regionType.setRegionId(1000);
		regionConfig.getRegionList().add(regionType);
		regionType = new RegionItemType();
		regionType.setType(1);
		regionType.setRegionId(2000);
		regionConfig.getRegionList().add(regionType);
		parameters.setRegionConfig(regionConfig);

		// Invoke the method.
		SetRegionConfigResponse ret = service.setRegionConfig(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getResponse()));
			return ret;
		} else {
			throw new ClientBusinessException(rheader, ret);
		}
	}

	/**
	 * @param args
	 * @throws Throwable 
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws Throwable {
		SetRegionConfigExample example = new SetRegionConfigExample();
		example.setRegionConfig();
	}
}
