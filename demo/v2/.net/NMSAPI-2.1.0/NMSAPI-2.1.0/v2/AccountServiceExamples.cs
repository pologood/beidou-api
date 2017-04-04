using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.AccountService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class AccountServiceExamples
    {
        public static void getAccountInfo()
        {
            AccountServiceClient service = new AccountServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            AccountInfoType accountInfo = null;

            ResHeader resHeader = service.getAccountInfo(authHeader, out accountInfo);

            Console.WriteLine("AccountService.getAccountInfo(): ");
            ObjectDumper.WriteResponse(resHeader, accountInfo);
        }
    }
}
