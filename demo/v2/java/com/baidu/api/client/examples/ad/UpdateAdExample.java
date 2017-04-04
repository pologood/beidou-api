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
import com.baidu.api.sem.nms.v2.UpdateAdRequest;
import com.baidu.api.sem.nms.v2.UpdateAdResponse;

public class UpdateAdExample {
	private AdService service;

	public UpdateAdExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(AdService.class);
	}

	public UpdateAdResponse updateAdToTextAd() {
		// Prepare your parameters.
		UpdateAdRequest parameters = new UpdateAdRequest();
		AdType ad = new AdType();
		// adid: required
		ad.setAdId(9283682L);
		ad.setDescription1("加入百度推广");
		ad.setDescription2("生意兴隆财源滚滚");
		ad.setDestinationUrl("http://baidu.com/text");
		ad.setDisplayUrl("baidu.com");
		ad.setGroupId(2166167L);
		ad.setLocalId(123456789L);
		ad.setTitle("这里是标题text");
		// 1: text, 2: image, 3: flash, 5: icon
		ad.setType(1);
		// status: meaningless, status will be always 3(auditing) after modified
		ad.setStatus(0);
		parameters.getAdTypes().add(ad);
		
		// Invoke the method.
		UpdateAdResponse ret = service.updateAd(parameters);
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
	
	public UpdateAdResponse updateAdToImageAd() {
		// Prepare your parameters.
		UpdateAdRequest parameters = new UpdateAdRequest();
		AdType ad = new AdType();
		// adid: required
		ad.setAdId(9283682L);
		//ad.setDescription1("加入百度推广");
		//ad.setDescription2("生意兴隆财源滚滚");
		ad.setDestinationUrl("http://baidu.com/image");
		ad.setDisplayUrl("baidu.com");
		ad.setGroupId(2166167L);
		ad.setLocalId(123456789L);
		ad.setTitle("这里是标题image");
		ad.setImageData(ImageUtil.GetImageByte("c:\\160x600.jpg"));
		ad.setHeight(600);
		ad.setWidth(160);
		// 1: text, 2: image, 3: flash, 5: icon
		ad.setType(2);
		// status: meaningless, status will be always 3(auditing) after modified
		ad.setStatus(0);
		parameters.getAdTypes().add(ad);
		
		// Invoke the method.
		UpdateAdResponse ret = service.updateAd(parameters);
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
	
	public UpdateAdResponse updateAdToFlashAd() {
		// Prepare your parameters.
		UpdateAdRequest parameters = new UpdateAdRequest();
		AdType ad = new AdType();
		// adid: required
		ad.setAdId(9283682L);
		//ad.setDescription1("加入百度推广");
		//ad.setDescription2("生意兴隆财源滚滚");
		ad.setDestinationUrl("http://baidu.com/flash");
		ad.setDisplayUrl("baidu.com");
		ad.setGroupId(2166167L);
		ad.setLocalId(123456789L);
		ad.setTitle("这里是标题flash");
		ad.setImageData(ImageUtil.GetImageByte("c:\\960_90.swf"));
		ad.setHeight(90);
		ad.setWidth(960);
		// 1: text, 2: image, 3: flash, 5: icon
		ad.setType(3);
		// status: meaningless, status will be always 3(auditing) after modified
		ad.setStatus(0);
		parameters.getAdTypes().add(ad);
		
		// Invoke the method.
		UpdateAdResponse ret = service.updateAd(parameters);
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
	
	public UpdateAdResponse updateAdToIconAd() {
		// Prepare your parameters.
		UpdateAdRequest parameters = new UpdateAdRequest();
		AdType ad = new AdType();
		// adid: required
		ad.setAdId(9283682L);
		ad.setDescription1("加入百度推广");
		ad.setDescription2("生意兴隆财源滚滚");
		ad.setDestinationUrl("http://baidu.com/icon");
		ad.setDisplayUrl("baidu.com");
		ad.setGroupId(2166167L);
		ad.setLocalId(123456789L);
		ad.setTitle("这里是标题icon");
		ad.setImageData(ImageUtil.GetImageByte("c:\\60_60.jpg"));
		ad.setHeight(60);
		ad.setWidth(60);
		// 1: text, 2: image, 3: flash, 5: icon
		ad.setType(5);
		// status: meaningless, status will be always 3(auditing) after modified
		ad.setStatus(0);
		parameters.getAdTypes().add(ad);
		
		// Invoke the method.
		UpdateAdResponse ret = service.updateAd(parameters);
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
		UpdateAdExample example = new UpdateAdExample();
//		example.updateAdToTextAd();
//		example.updateAdToImageAd();
//		example.updateAdToFlashAd();
		example.updateAdToIconAd();
	}
}
