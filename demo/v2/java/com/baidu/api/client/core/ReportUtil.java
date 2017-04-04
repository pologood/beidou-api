/**
 * ReportUtil.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.baidu.api.client.core;

import com.baidu.api.sem.common.v2.ResHeader;
import com.baidu.api.sem.nms.v2.AccountFileService;
import com.baidu.api.sem.nms.v2.GetAccountFileStateRequest;
import com.baidu.api.sem.nms.v2.GetAccountFileStateResponse;
import com.baidu.api.sem.nms.v2.GetAccountFileUrlRequest;
import com.baidu.api.sem.nms.v2.GetAccountFileUrlResponse;
import com.baidu.api.sem.nms.v2.GetReportFileUrlRequest;
import com.baidu.api.sem.nms.v2.GetReportFileUrlResponse;
import com.baidu.api.sem.nms.v2.GetReportStateRequest;
import com.baidu.api.sem.nms.v2.GetReportStateResponse;
import com.baidu.api.sem.nms.v2.ReportService;



/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-6-30$
 * 
 */
public abstract class ReportUtil {

    /**
     * Get the report file Url by report Id. We will check the file status internally.
     * 
     * @param service
     *            The instance of ReportService
     * @param reportId
     *            The report Id.
     * @param retryNum
     *            Retry times, we will check the file status every 30 seconds.
     * @return
     */
    public static final GetReportFileUrlResponse getReportFileUrl(ReportService service, String reportId, int retryNum) {
        System.out.println("We will check the file status every 30 seconds, please wait...");
        // This is the request
        GetReportStateRequest parameters = new GetReportStateRequest();
        parameters.setReportId(reportId);
        int lastStatus = -1;
        while (retryNum-- > 0) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Invoke the method.
            GetReportStateResponse ret = service.getReportState(parameters);
            // Deal with the response header, the second parameter controls whether to print the response header to
            // console
            // or not.
            ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
            // If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
            if (rheader.getStatus() == 0) {
                System.out.println("getReportState.result\n" + ObjToStringUtil.objToString(ret));
                if (ret.getIsGenerated() == 3) {
                    lastStatus = 3;
                    break;
                }
            } else {
                throw new ClientBusinessException(rheader, ret);
            }
        }
        if (lastStatus == 3) {
            // This is the request
            GetReportFileUrlRequest parameters2 = new GetReportFileUrlRequest();
            parameters2.setReportId(reportId);
            // Invoke the method.
            GetReportFileUrlResponse ret = service.getReportFileUrl(parameters2);
            // Deal with the response header, the second parameter controls whether to print the response header to
            // console
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
        throw new ClientInternalException("We tried to get file for " + retryNum / 2
                + " minites, but file still not ready!");
    }
    
    /**
     * Get the account file Url by file Id. We will check the file status internally.
     * 
     * @param service
     *            The instance of AccountFileService
     * @param reportId
     *            The file Id.
     * @param retryNum
     *            Retry times, we will check the file status every 30 seconds.
     * @return
     */
    public static final GetAccountFileUrlResponse getAccountFileUrl(AccountFileService service, String fileId, int retryNum) {
        System.out.println("We will check the file status every 30 seconds, please wait...");
        // This is the request
        GetAccountFileStateRequest parameters = new GetAccountFileStateRequest();
        parameters.setFileId(fileId);
        int lastStatus = -1;
        while (retryNum-- > 0) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Invoke the method.
            GetAccountFileStateResponse ret = service.getAccountFileState(parameters);
            // Deal with the response header, the second parameter controls whether to print the response header to
            // console
            // or not.
            ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
            // If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
            if (rheader.getStatus() == 0) {
                System.out.println("getAccountFileState.result\n" + ObjToStringUtil.objToString(ret));
                if (ret.getIsGenerated() == 3) {
                    lastStatus = 3;
                    break;
                }
            } else {
                throw new ClientBusinessException(rheader, ret);
            }
        }
        if (lastStatus == 3) {
            // This is the request
        	GetAccountFileUrlRequest parameters2 = new GetAccountFileUrlRequest();
            parameters2.setFileId(fileId);
            // Invoke the method.
            GetAccountFileUrlResponse ret = service.getAccountFileUrl(parameters2);
            // Deal with the response header, the second parameter controls whether to print the response header to
            // console
            // or not.
            ResHeader rheader = ResHeaderUtil.getResHeader(service, true);
            // If status equals zero, there is no error. Otherwise, you need to check the errors in the response header.
            if (rheader.getStatus() == 0) {
                System.out.println("getAccountFileUrl.result\n" + ObjToStringUtil.objToString(ret));
                return ret;
            } else {
                throw new ClientBusinessException(rheader, ret);
            }
        }
        throw new ClientInternalException("We tried to get file for " + retryNum / 2
                + " minites, but file still not ready!");
    }

    /**
     * Download the file from server and decode it as GBK.
     * 
     * @param url
     *            The file url
     * @return
     */
    public static final String getFileContent(String url) {
        return getFileContent(url, null);
    }

    /**
     * Download the file from server and uncompress it.
     * 
     * @param url
     *            The file url
     * @param format
     *            The compressing format
     * @return The character file content
     */
    public static final String getFileContent(String url, String format) {
        byte[] fileContent = DownloadUtil.downloadFile(url);

        if (format == null || format.equals("")) {
            try {
                return new String(fileContent, "gbk");
            } catch (Exception e) {
                throw new ClientInternalException(e);
            }
        }
        if (format.equalsIgnoreCase("zip")) {
            try {
                fileContent = GZipUtil.unzip(fileContent);
                return new String(fileContent, "gbk");
            } catch (Exception e) {
                throw new ClientInternalException(e);
            }
        }
        throw new ClientInternalException("We only support GZIP now, please try to use GZIP.");
    }
}
