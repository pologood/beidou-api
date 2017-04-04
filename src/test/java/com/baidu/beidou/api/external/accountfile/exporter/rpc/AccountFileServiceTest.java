package com.baidu.beidou.api.external.accountfile.exporter.rpc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.api.external.accountfile.exporter.AccountFileService;
import com.baidu.beidou.api.external.accountfile.vo.AccountFileRequestType;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileIdRequest;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileStateRequest;
import com.baidu.beidou.api.external.accountfile.vo.request.GetAccountFileUrlRequest;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileIdResponse;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileStateResponse;
import com.baidu.beidou.api.external.accountfile.vo.response.GetAccountFileUrlResponse;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;

/**
 * 整账户报告测试类
 *
 * @author cuilei05
 */
public class AccountFileServiceTest extends ApiBaseRPCTest<AccountFileService> {
    private Logger logger = LoggerFactory.getLogger(AccountFileServiceTest.class);
    private AccountFileService exporter;

    @Before
    public void before() {
        try {
            exporter = getServiceProxy(AccountFileService.class, ApiExternalConstant
                    .ACCOUNTFILE_SERVICE_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void testGetAccountFileId() throws Exception {
        String accountFileId = getAccountFileId();
        Assert.assertTrue("成功获取accountFileId", accountFileId != null && accountFileId.length() > 0);
    }

    @Test
    @Ignore
    public void testGetAccountFileState() throws Exception {
        String accountFileId = getAccountFileId();
        boolean generated = isAccountFileGenerated(accountFileId, 1);
        logger.info("generated: {}", generated);
    }

    @Test
    @Ignore
    public void testGetAccountFileUrl() throws Exception {
        String accountFileId = getAccountFileId();
        int queryCount = 0;
        boolean success = false;

        while (!success && queryCount++ < 10) {
            success = isAccountFileGenerated(accountFileId, 5);
        }

        if (!success) {
            Assert.fail("无法获取文件URL");
        }

        GetAccountFileUrlRequest request = new GetAccountFileUrlRequest();
        request.setFileId(accountFileId);
        ApiResult<GetAccountFileUrlResponse> result = exporter.getAccountFileUrl(dataUser, request, apiOption);
        logger.info("getAccountFileUrl: {}", result);
        Assert.assertTrue("文件URL有效", result.getData().get(0).getFilePath() != null);
    }

    public void testGetAccountFile() throws Exception {
        String accountFileId = getAccountFileId();

    }

    /**
     * 获取一个文件ID
     *
     * @return
     */
    private String getAccountFileId() {
        GetAccountFileIdRequest request = new GetAccountFileIdRequest();
        AccountFileRequestType type = new AccountFileRequestType();
        type.setCampaignIds(new long[] {108});
        type.setFormat(0);
        request.setAccountFileRequestType(type);
        request.setVersion("1.1");

        ApiResult<GetAccountFileIdResponse> result = exporter.getAccountFileId(dataUser, request, apiOption);

        logger.info("getAccountFileId: {}", result);
        return result.getData().get(0).getFileId();
    }

    /**
     * 查询文件是否生成成功
     *
     * @param accountFileId 文件ID
     * @param retry         重试次数,超过这个次数还没成功则为失败
     *
     * @return 在指定次数内是否生成文件
     */
    private boolean isAccountFileGenerated(String accountFileId, int retry) {
        GetAccountFileStateRequest request = new GetAccountFileStateRequest();
        request.setFileId(accountFileId);
        for (int i = 0; i < retry; i++) {
            ApiResult<GetAccountFileStateResponse> result = exporter.getAccountFileState(dataUser, request, apiOption);
            logger.info("getAccountFileState: {}", result);
            if (result.getData().get(0).getIsGenerated() == 3) {
                return true;
            }
            sleep(500L);
        }
        return false;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
