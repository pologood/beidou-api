using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.InterestService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class InterestServiceExamples
    {
        public static void getInterest()
        {
            InterestServiceClient service = new InterestServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            InterestType[] interestTypes = null;

            ResHeader resHeader = service.getInterest(authHeader, out interestTypes);

            Console.WriteLine("InterestService.getInterest(): ");
            ObjectDumper.WriteResponse(resHeader, interestTypes);
        }

        public static void getAllCustomInterest()
        {
            InterestServiceClient service = new InterestServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            CustomInterestType[] interests = null;

            ResHeader resHeader = service.getAllCustomInterest(authHeader, out interests);

            Console.WriteLine("InterestService.getAllCustomInterest(): ");
            ObjectDumper.WriteResponse(resHeader, interests);
        }

        public static void getCustomInterest()
        {
            InterestServiceClient service = new InterestServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] interestIds = { 90000321 };

            CustomInterestType[] interests = null;

            ResHeader resHeader = service.getCustomInterest(authHeader, interestIds, out interests);

            Console.WriteLine("InterestService.getCustomInterest(): ");
            ObjectDumper.WriteResponse(resHeader, interests);
        }
    }
}