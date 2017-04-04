using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.CodeService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class CodeServiceExamples
    {
        public static void getAllRegion()
        {
            CodeServiceClient client = new CodeServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            RegionType[] result = null;
            ResHeader resHeader = client.getAllRegion(authHeader, out result);

            Console.WriteLine("CodeService.getAllRegion(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getAllCategory()
        {
            CodeServiceClient client = new CodeServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            CategoryType[] result = null;
            ResHeader resHeader = client.getAllCategory(authHeader, out result);

            Console.WriteLine("CodeService.getAllCategory(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }
    }
}
