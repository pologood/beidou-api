using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.AccountFileService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class AccountFileServiceExamples
    {
        public static void getAccountFileId()
        {
            AccountFileServiceClient client = new AccountFileServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            AccountFileRequestType type = new AccountFileRequestType();
            long[] campaignIds = new long[1];
            campaignIds[0] = 3281L;
            type.campaignIds = campaignIds;
            type.format = 0;

            string fileId = null;
            ResHeader resHeader = client.getAccountFileId(authHeader, type, out fileId);

            Console.WriteLine("AccountFileService.getAccountFileId(): ");
            ObjectDumper.WriteResponse(resHeader, fileId);
        }

        public static void getAccountFileState()
        {
            AccountFileServiceClient client = new AccountFileServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            string fileId = "498ef6ca696d25d59a624f64105fae04";

            // 1:waiting, 2:handling, 3: success, 4: failed
            int isGenerated = -1;
            ResHeader resHeader = client.getAccountFileState(authHeader, fileId, out isGenerated);

            Console.WriteLine("AccountFileService.getAccountFileState(): ");
            ObjectDumper.WriteResponse(resHeader, isGenerated);
        }

        public static void getAccountFileUrl()
        {
            AccountFileServiceClient client = new AccountFileServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            string fileId = "498ef6ca696d25d59a624f64105fae04";

            string filePath = null;
            string md5 = null;
            ResHeader resHeader = client.getAccountFileUrl(authHeader, fileId, out filePath, out md5);

            Console.WriteLine("AccountFileService.getAccountFileUrl(): ");
            ObjectDumper.WriteResponse(resHeader, filePath);
        }
    }
}
