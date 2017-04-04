using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.AccountService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class SandboxAccountServiceExamples
    {
        public static void execute()
        {
            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);
            AccountServiceClient client = new AccountServiceClient("AccountService", "https://api.baidu.com/sem/nms/v2/AccountService");
            AccountInfoType account = getAccountInfo(client, authHeader);
        }

        public static AccountInfoType getAccountInfo(AccountServiceClient client, AuthHeader authHeader)
        {
            AccountInfoType response = null;

            ResHeader resHeader = client.getAccountInfo(authHeader, out response);

            Console.WriteLine("AccountService.getAccountInfo(): ");
            ObjectDumper.WriteResponse(resHeader, response);

            return response;
        }
    }
}
