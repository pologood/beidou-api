package com.baidu.api.client.examples.ad;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ImageUtil;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.AdService;
import com.baidu.api.sem.nms.v2.AdType;
import com.baidu.api.sem.nms.v2.AddAdRequest;
import com.baidu.api.sem.nms.v2.AddAdResponse;

/**
 * 
 * ClassName: AddAdExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Feb 9, 2012
 */
public class AddAdExample {

	private AdService service;

	/** 
	 * -----------------------------------------------------
	 * Note: Sample Ad images can not found under com\baidu\api\client\image directory
	 * ------------------------------------------------------
	 */

	public AddAdExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(AdService.class);
	}

	public AddAdResponse addTextAd() {
		// Prepare your parameters.
		AddAdRequest parameters = new AddAdRequest();
		AdType ad = new AdType();
		ad.setDescription1("加入百度推广");
		ad.setDescription2("生意兴隆财源滚滚");
		ad.setDestinationUrl("http://baidu.com/123");
		ad.setDisplayUrl("baidu.com");
		ad.setGroupId(2166165l);
		ad.setLocalId(123456789l);
		ad.setTitle("这里是标题");
		// 1 means text
		ad.setType(1);
		ad.setStatus(1);
		parameters.getAdTypes().add(ad);
		// Invoke the method.
		AddAdResponse ret = service.addAd(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getAdTypes()));
			return ret;
		} else {
			throw new ClientBusinessException(rheader, ret);
		}
	}

	public AddAdResponse addImageAd() {
		// Prepare your parameters.
		AddAdRequest parameters = new AddAdRequest();
		AdType ad = new AdType();
		//ad.setDescription1("加入百度推广");
		//ad.setDescription2("生意兴隆财源滚滚");
		ad.setDestinationUrl("http://baidu.com/123");
		ad.setDisplayUrl("baidu.com");
		ad.setGroupId(2166165l);
		ad.setLocalId(123456789l);
		ad.setTitle("这里是标题");
		ad.setImageData(ImageUtil.GetImageByte("c:\\160x600.jpg"));
		ad.setHeight(600);
		ad.setWidth(160);
		// 2 means image
		ad.setType(2);
		ad.setStatus(1);
		parameters.getAdTypes().add(ad);
		// Invoke the method.
		AddAdResponse ret = service.addAd(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getAdTypes()));
			return ret;
		} else {
			throw new ClientBusinessException(rheader, ret);
		}
	}

	public AddAdResponse addFlashAd() {
		// Prepare your parameters.
		AddAdRequest parameters = new AddAdRequest();
		AdType ad = new AdType();
		//ad.setDescription1("加入百度推广");
		//ad.setDescription2("生意兴隆财源滚滚");
		ad.setDestinationUrl("http://baidu.com/123");
		ad.setDisplayUrl("baidu.com");
		ad.setGroupId(2166165l);
		ad.setLocalId(123456789l);
		ad.setTitle("这里是标题");
		ad.setImageData(ImageUtil.GetImageByte("c:\\960_90.swf"));
		ad.setHeight(90);
		ad.setWidth(960);
		// 3 means flash
		ad.setType(3);
		ad.setStatus(1);
		parameters.getAdTypes().add(ad);
		// Invoke the method.
		AddAdResponse ret = service.addAd(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getAdTypes()));
			return ret;
		} else {
			throw new ClientBusinessException(rheader, ret);
		}
	}

	public AddAdResponse addTextWithIconAd() {
		// Prepare your parameters.
		AddAdRequest parameters = new AddAdRequest();
		AdType ad = new AdType();
		ad.setDescription1("加入百度推广");
		ad.setDescription2("生意兴隆财源滚滚");
		ad.setDestinationUrl("http://baidu.com/123");
		ad.setDisplayUrl("baidu.com");
		ad.setGroupId(2166165l);
		ad.setLocalId(123456789l);
		ad.setTitle("这里是标题");
		ad.setImageData(ImageUtil.GetImageByte("c:\\60_60.jpg"));
		ad.setHeight(60);
		ad.setWidth(60);
		// 3 means text with icon
		ad.setType(5);
		ad.setStatus(1);
		parameters.getAdTypes().add(ad);
		// Invoke the method.
		AddAdResponse ret = service.addAd(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret.getAdTypes()));
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
		AddAdExample example = new AddAdExample();
		//example.addTextAd();
		//example.addImageAd();
		//example.addFlashAd();
		example.addTextWithIconAd();
	}
}
