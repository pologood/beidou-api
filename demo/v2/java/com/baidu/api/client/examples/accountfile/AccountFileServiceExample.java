package com.baidu.api.client.examples.accountfile;

import com.baidu.api.client.core.ClientBusinessException;
import com.baidu.api.client.core.ObjToStringUtil;
import com.baidu.api.client.core.ReportUtil;
import com.baidu.api.client.core.ResHeaderUtil;
import com.baidu.api.client.core.ServiceFactory;
import com.baidu.api.client.core.VersionService;
import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.AccountFileRequestType;
import com.baidu.api.sem.nms.v2.AccountFileService;
import com.baidu.api.sem.nms.v2.GetAccountFileIdRequest;
import com.baidu.api.sem.nms.v2.GetAccountFileIdResponse;
import com.baidu.api.sem.nms.v2.GetAccountFileStateRequest;
import com.baidu.api.sem.nms.v2.GetAccountFileStateResponse;
import com.baidu.api.sem.nms.v2.GetAccountFileUrlRequest;
import com.baidu.api.sem.nms.v2.GetAccountFileUrlResponse;
import com.baidu.api.sem.nms.v2.GetReportFileUrlResponse;
import com.baidu.api.sem.nms.v2.GetReportIdResponse;

/**
 * 
 * ClassName: AccountFileServiceExample  <br>
 * Function: TODO ADD FUNCTION
 *
 * @author Baidu API Team
 * @date Apr 20, 2012
 */
public class AccountFileServiceExample {

	private AccountFileService service;

	public AccountFileServiceExample() {
		// Get service factory. Your authentication information will be popped up automatically from
		// baidu-api.properties
		VersionService factory = ServiceFactory.getInstance();
		// Get service stub by given the Service interface.
		// Please see the bean-api.tar.gz to get more details about all the service interfaces.
		this.service = factory.getService(AccountFileService.class);
	}

	public GetAccountFileIdResponse getAccountFileId() {
		// Prepare your parameters.
		GetAccountFileIdRequest parameter = new GetAccountFileIdRequest();
		// get request
		AccountFileRequestType request = new AccountFileRequestType();
		
		// set format
		request.setFormat(0);
		
		// set campaignIds
		long[] campaignIds = new long[] { 108, 1351 };
		for (long campaignId : campaignIds) {
			request.getCampaignIds().add(campaignId);
		}

		parameter.setAccountFileRequestType(request);
		
		// Invoke the method.
		GetAccountFileIdResponse ret = service.getAccountFileId(parameter);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("result\n" + ObjToStringUtil.objToString(ret));
			return ret;
		} else {
			throw new ClientBusinessException(rheader, ret);
		}
	}

	public GetAccountFileStateResponse getAccountFileState(String fileId) {
		// This is the request
		GetAccountFileStateRequest parameters = new GetAccountFileStateRequest();
		parameters.setFileId(fileId);
		// Invoke the method.
		GetAccountFileStateResponse ret = service.getAccountFileState(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("getReportState.result\n" + ObjToStringUtil.objToString(ret));
			return ret;
		} else {
			throw new ClientBusinessException(rheader, ret);
		}
	}

	public GetAccountFileUrlResponse getAccountFileUrl(String fileId) {
		// This is the request
		GetAccountFileUrlRequest parameters = new GetAccountFileUrlRequest();
		parameters.setFileId(fileId);
		// Invoke the method.
		GetAccountFileUrlResponse ret = service.getAccountFileUrl(parameters);
		// Deal with the response header, the second parameter controls whether to print the response header to console
		// or not.
		ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
		// If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
		if (rheader.getStatus() == 0) {
			System.out.println("getReportFileUrl.result\n" + ObjToStringUtil.objToString(ret));
			return ret;
		} else {
			throw new ClientBusinessException(rheader, ret);
		}
	}

	public static void main(String[] args) throws Exception {
		AccountFileServiceExample example = new AccountFileServiceExample();
		GetAccountFileIdResponse id = example.getAccountFileId();

		GetAccountFileUrlResponse urlRes = ReportUtil.getAccountFileUrl(
				example.service, id.getFileId(), 60);
	}

}
