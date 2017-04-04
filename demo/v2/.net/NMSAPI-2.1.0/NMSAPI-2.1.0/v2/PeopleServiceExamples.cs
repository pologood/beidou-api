using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.PeopleService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class PeopleServiceExamples
    {
        public static void getPeople()
        {
            PeopleServiceClient service = new PeopleServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] peopleIds = { 90000321 };

            PeopleType[] peoples = null;

            ResHeader resHeader = service.getPeople(authHeader, peopleIds, out peoples);

            Console.WriteLine("PeopleService.getPeople(): ");
            ObjectDumper.WriteResponse(resHeader, peoples);
        }

        public static void getAllPeople()
        {
            PeopleServiceClient service = new PeopleServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            PeopleType[] peoples = null;

            ResHeader resHeader = service.getAllPeople(authHeader, out peoples);

            Console.WriteLine("PeopleService.getAllPeople(): ");
            ObjectDumper.WriteResponse(resHeader, peoples);
        }
    }
}
