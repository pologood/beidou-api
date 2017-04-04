using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.GroupService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class GroupServiceExamples
    {
        public static void addGroup()
        {
            GroupServiceClient client = new GroupServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupType[] groups = new GroupType[1];
            GroupType group = new GroupType();
            group.campaignId = 984757L;
            group.campaignIdSpecified = true;
            group.groupName = "Demo_" + DateTime.Now.Ticks % 100000;
            group.price = 1000;
            group.priceSpecified = true;
            group.status = 0;
            group.type = 0;
            groups[0] = group;

            ResHeader resHeader = client.addGroup(authHeader, ref groups);

            Console.WriteLine("GroupService.addGroup(): ");
            ObjectDumper.WriteResponse(resHeader, groups);
        }

        public static void getGroupByCampaignId()
        {
            GroupServiceClient client = new GroupServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupType[] result = null;
            ResHeader resHeader = client.getGroupByCampaignId(authHeader, 984757L, out result);

            Console.WriteLine("GroupService.getGroupByCampaignId(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getGroupIdByCampaignId()
        {
            GroupServiceClient client = new GroupServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] result = null;
            ResHeader resHeader = client.getGroupIdByCampaignId(authHeader, 984757L, out result);

            Console.WriteLine("GroupService.getGroupIdByCampaignId(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getGroupByGroupId()
        {
            GroupServiceClient client = new GroupServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupType[] result = null;

            ResHeader resHeader = client.getGroupByGroupId(authHeader, new long[] { 3066118, 3066103 }, out result);

            Console.WriteLine("GroupService.getGroupByGroupId(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void updateGroup()
        {
            GroupServiceClient client = new GroupServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            GroupType[] parameters = new GroupType[1];
            GroupType type = new GroupType();
            parameters[0] = type;
            type.groupId = 3066118L;
            type.groupIdSpecified = true;
            type.status = 0;
            type.price = 10;
            type.statusSpecified = true;
            type.groupName = "Demo_" + DateTime.Now.Ticks % 100000;

            ResHeader resHeader = client.updateGroup(authHeader, ref parameters);

            Console.WriteLine("GroupService.updateGroup(): ");
            ObjectDumper.WriteResponse(resHeader, parameters);
        }
    }
}
