package com.baidu.beidou.api.external.accountfile.exporter.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.accountfile.exporter.AccountFileService;
import com.baidu.beidou.api.external.accountfile.vo.AccountFileRequestType;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileIdRequest;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileStateRequest;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileUrlRequest;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileIdResponse;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileStateResponse;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileUrlResponse;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.test.common.AbstractShardXdbTestCaseLegacy;
@Ignore
public class AccountFileServiceImplTest extends AbstractShardXdbTestCaseLegacy {
	
	@Resource
	private AccountFileService accountFileService;

	@Before
	public void beforeEach() {
		// 如果下载文件夹不存在，则新建一个
		File fileDownload = new File(AccountFileWebConstants.DOWNLOAD_FILE_SAVE_PATH);
		if(!fileDownload.exists()){
			fileDownload.mkdir();
		}
		File fileTmp = new File(AccountFileWebConstants.TMP_SAVE_PATH);
		if(!fileTmp.exists()){
			fileTmp.mkdir();
		}
	}
	
	@Test
	@Rollback(true)
	public void testGetAccountFileIdNoCampaignIds() throws Exception{
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetAccountFileIdRequest request = new GetAccountFileIdRequest();
		AccountFileRequestType type = new AccountFileRequestType();
		type.setFormat(0);
		//type.setCampaignIds(new long[]{108});
		request.setAccountFileRequestType(type);
		request.setVersion("1.1");
		
		ApiResult<GetAccountFileIdResponse> result = accountFileService.getAccountFileId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getData().get(0).getFileId().length(), is(32));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		Thread.sleep(10000);
		
	}
	
	@Test
	@Rollback(true)
	public void testGetAccountFileId() throws Exception{
		final int userId = 19;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetAccountFileIdRequest request = new GetAccountFileIdRequest();
		AccountFileRequestType type = new AccountFileRequestType();
		type.setFormat(1);
		type.setCampaignIds(new long[]{108,104569});
		request.setAccountFileRequestType(type);
		request.setVersion("1.1");
		
		ApiResult<GetAccountFileIdResponse> result = accountFileService.getAccountFileId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getData().get(0).getFileId().length(), is(32));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		Thread.sleep(10000);
		
	}
	
	@Test
	@Rollback(true)
	public void testGetAccountFileState() throws Exception {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetAccountFileIdRequest request = new GetAccountFileIdRequest();
		AccountFileRequestType type = new AccountFileRequestType();
		type.setFormat(1);
		type.setCampaignIds(new long[]{108, 104569});
		request.setAccountFileRequestType(type);
		request.setVersion("1.1");
		
		ApiResult<GetAccountFileIdResponse> result = accountFileService.getAccountFileId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getData().get(0).getFileId().length(), is(32));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		String fileId = result.getData().get(0).getFileId();
		
		GetAccountFileStateRequest request2 = new GetAccountFileStateRequest();
		request2.setFileId(fileId);
		int max = 0;
		ApiResult<GetAccountFileStateResponse> result2 = accountFileService.getAccountFileState(dataUser, request2, apiOption);
		while(result2.getData().get(0).getIsGenerated() != 3){
			System.out.println(result2);
			System.out.println("not gen yet!!!! so sleep a little while");
			if(max++ > 5){
				break;
			}
			Thread.sleep(3000);
			result2 = accountFileService.getAccountFileState(dataUser, request2, apiOption);
		}
		
		// 打印返回
		System.out.println(result2);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result2.getErrors(), nullValue());
		assertThat(result2.getData().size(), is(1));
		assertThat(result2.getData().get(0).getIsGenerated(), greaterThan(0));
		assertThat(result2.getData().get(0).getIsGenerated(), lessThan(4));
		assertThat(result2.getPayment().getTotal(), is(1));
		assertThat(result2.getPayment().getSuccess(), is(1));
		
		Thread.sleep(10000);
	}
	
	//@Test
	//@Rollback(false)
	public void testGetAccountFileUrl() throws Exception {
		final int userId = 499;
		DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
		ApiOption apiOption = DarwinApiHelper.getApiOptions();
		DarwinApiHelper.setVisitorSession(userId);
		DarwinApiHelper.setUserSession(userId);
		
		GetAccountFileIdRequest request = new GetAccountFileIdRequest();
		AccountFileRequestType type = new AccountFileRequestType();
		type.setFormat(0);
		type.setCampaignIds(new long[]{108,104569});
		request.setAccountFileRequestType(type);
		request.setVersion("1.1");
		
		ApiResult<GetAccountFileIdResponse> result = accountFileService.getAccountFileId(dataUser, request, apiOption);
		
		// 打印返回
		System.out.println(result);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result.getErrors(), nullValue());
		assertThat(result.getData().size(), is(1));
		assertThat(result.getData().get(0).getFileId().length(), is(32));
		assertThat(result.getPayment().getTotal(), is(1));
		assertThat(result.getPayment().getSuccess(), is(1));
		
		String fileId = result.getData().get(0).getFileId();
		
		GetAccountFileStateRequest request2 = new GetAccountFileStateRequest();
		request2.setFileId(fileId);
		int max = 0;
		ApiResult<GetAccountFileStateResponse> result2 = accountFileService.getAccountFileState(dataUser, request2, apiOption);
		while(result2.getData().get(0).getIsGenerated() != 3){
			System.out.println(result2);
			System.out.println("not gen yet!!!! so sleep a little while");
			if(max++ > 10){
				break;
			}
			Thread.sleep(3000);
			result2 = accountFileService.getAccountFileState(dataUser, request2, apiOption);
		}
		
		
		GetAccountFileUrlRequest request3 = new GetAccountFileUrlRequest();
		request3.setFileId(fileId);
		
		ApiResult<GetAccountFileUrlResponse> result3 = accountFileService.getAccountFileUrl(dataUser, request3, apiOption);
		
		// 打印返回
		System.out.println(result3);
		
		// 成功后没有error，并且返回该用户的信息
		assertThat(result3.getErrors(), nullValue());
		assertThat(result3.getData().size(), is(1));
		assertThat(result3.getData().get(0).getFilePath().length(), greaterThan(4));
		assertThat(result3.getData().get(0).getFilePath().length(), greaterThan(4));
		assertThat(result3.getData().get(0).getMd5().length(), is(32));
		assertThat(result3.getPayment().getTotal(), is(1));
		assertThat(result3.getPayment().getSuccess(), is(1));
		
	}
	
}
