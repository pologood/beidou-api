package com.baidu.api.client.examples.groupconfig;

import java.rmi.RemoteException;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.GroupConfigService;
import com.baidu.api.sem.nms.v2.KeywordType;
import com.baidu.api.sem.nms.v2.KtItemType;
import com.baidu.api.sem.nms.v2.RtItemType;
import com.baidu.api.sem.nms.v2.RtRelationType;
import com.baidu.api.sem.nms.v2.SetTargetInfoRequest;
import com.baidu.api.sem.nms.v2.SetTargetInfoResponse;
import com.baidu.api.sem.nms.v2.TargetInfoType;
import com.baidu.api.sem.nms.v2.VtItemType;

/**
 * 
 * ClassName: SetTargetInfoExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Feb 9, 2012
 */
public class SetTargetInfoExample {

	private GroupConfigService service;

	public SetTargetInfoExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(GroupConfigService.class);
	}

	/**
	 * 修改推广组为关键词定向
	 */
	public SetTargetInfoResponse setTargetInfo2KT() {
		// Prepare your parameters.
		SetTargetInfoRequest parameters = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		//取值范围：
		//1表示回头客定向，
		//2表示关键词定向，
		//3表示不启用定向投放
		//4 表示到访定向
		targetInfo.setType(2);
		targetInfo.setGroupId(228);
		KtItemType ktItem = new KtItemType();
		//设置关键词
		ktItem.setTargetType(7);
		ktItem.setAliveDays(30);
		KeywordType keyword1 = new KeywordType();
		keyword1.setKeyword("百度456");
		keyword1.setPattern(1);
		ktItem.getKtWordList().add(keyword1);
		KeywordType keyword2 = new KeywordType();
		keyword2.setKeyword("百度123");
		keyword2.setPattern(1);
		ktItem.getKtWordList().add(keyword2);
		targetInfo.setKtItem(ktItem);
		parameters.setTargetInfo(targetInfo);
		// Invoke the method.
		SetTargetInfoResponse ret = service.setTargetInfo(parameters);
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
	 * 修改推广组为回头客定向
	 */
	public SetTargetInfoResponse setTargetInfo2RT() {
		// Prepare your parameters.
		SetTargetInfoRequest parameters = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		//取值范围：
		//0表示主题词定向，
		//1表示回头客定向，
		//2表示搜客定向，
		//3表示不启用定向投放
		//4 表示到访定向
		targetInfo.setType(1);
		targetInfo.setGroupId(2166164);
		RtItemType rtItem = new RtItemType();
		rtItem.setAliveDays(30);
		RtRelationType rtRelation = new RtRelationType();
		rtRelation.setFcPlanId(1088l);
		rtRelation.setFcPlanName("FCPlan001");
		rtRelation.setFcUnitId(186324l);
		rtRelation.setFcUnitName("FCUnit001");
		rtRelation.setRelationType(0);
		rtItem.getRtRelationList().add(rtRelation);
		targetInfo.setRtItem(rtItem);
		parameters.setTargetInfo(targetInfo);
		// Invoke the method.
		SetTargetInfoResponse ret = service.setTargetInfo(parameters);
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
	 * 修改推广组为不启用任何定向
	 */
	public SetTargetInfoResponse setTargetInfo2None() {
		// Prepare your parameters.
		SetTargetInfoRequest parameters = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		//取值范围：
		//0表示主题词定向，
		//1表示回头客定向，
		//2表示搜客定向，
		//3表示不启用定向投放
		//4 表示到访定向
		targetInfo.setType(3);
		targetInfo.setGroupId(2166164);
		parameters.setTargetInfo(targetInfo);
		// Invoke the method.
		SetTargetInfoResponse ret = service.setTargetInfo(parameters);
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
	 * 修改推广组为到访定向
	 */
	public SetTargetInfoResponse setTargetInfo2VT() {
		// Prepare your parameters.
		SetTargetInfoRequest parameters = new SetTargetInfoRequest();
		TargetInfoType targetInfo = new TargetInfoType();
		//取值范围：
		//0表示主题词定向，
		//1表示回头客定向，
		//2表示搜客定向，
		//3表示不启用定向投放
		//4 表示到访定向
		targetInfo.setType(4);
		targetInfo.setGroupId(2166164);
		VtItemType vtItem = new VtItemType();
		//设置搜客关键词
		vtItem.getRelatedPeopleIds().add(13l);
		vtItem.getRelatedPeopleIds().add(14l);
		vtItem.getUnRelatePeopleIds().add(15l);
		targetInfo.setVtItem(vtItem);
		parameters.setTargetInfo(targetInfo);
		// Invoke the method.
		SetTargetInfoResponse ret = service.setTargetInfo(parameters);
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
		SetTargetInfoExample example = new SetTargetInfoExample();
		example.setTargetInfo2KT();
		//example.setTargetInfo2CT();
		//example.setTargetInfo2RT();
		//example.setTargetInfo2None();
		//example.setTargetInfo2VT();
	}

}
