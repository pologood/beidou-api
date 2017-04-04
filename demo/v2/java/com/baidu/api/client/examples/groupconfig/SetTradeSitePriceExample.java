package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.SetTradeSitePriceRequest;
import com.baidu.api.sem.nms.v2.SetTradeSitePriceResponse;
import com.baidu.api.sem.nms.v2.SitePriceType;
import com.baidu.api.sem.nms.v2.TradePriceType;
import com.baidu.api.sem.nms.v2.TradeSitePriceType;

/**
 * ClassName: SetTradeSitePriceExample
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date 2012-2-13
 */
public class SetTradeSitePriceExample {
	private GroupConfigService service;

	public SetTradeSitePriceExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	public SetTradeSitePriceResponse setTradeSitePrice() {
		// Prepare your parameters.
		SetTradeSitePriceRequest parameters = new SetTradeSitePriceRequest();
		TradeSitePriceType tradeSitePriceConfig = new TradeSitePriceType();
		// Set your group id.
		tradeSitePriceConfig.setGroupId(2166147);
		// Set price for your trade.
		TradePriceType tradePrice = new TradePriceType();
		tradePrice.setTradeId(9);
		tradePrice.setPrice(2.0f);
		tradeSitePriceConfig.getTradePriceList().add(tradePrice);

		SitePriceType sitePrice = new SitePriceType();
		sitePrice.setSite("7k7k.com");
		sitePrice.setPrice(2.0f);
		tradeSitePriceConfig.getSitePriceList().add(sitePrice);
		parameters.setTradeSitePrice(tradeSitePriceConfig);

		// Invoke the method.
		SetTradeSitePriceResponse ret = service.setTradeSitePrice(parameters);
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
		SetTradeSitePriceExample example = new SetTradeSitePriceExample();
		example.setTradeSitePrice();
	}
}
