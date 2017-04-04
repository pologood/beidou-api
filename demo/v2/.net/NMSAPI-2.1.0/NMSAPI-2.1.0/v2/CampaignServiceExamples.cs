using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.CampaignService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class CampaignServiceExamples
    {
        public static void addCampaign()
        {
            CampaignServiceClient client = new CampaignServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            CampaignType[] campaigns = new CampaignType[1];
            CampaignType campaign = new CampaignType();
            campaign.budget = 1000;
            campaign.budgetSpecified = true;
            campaign.campaignName = "Demo_" + DateTime.Now.Ticks % 1000000;
            campaign.status = 1;
            campaign.statusSpecified = true;
            ScheduleType[] schedule = new ScheduleType[2];
            schedule[0] = new ScheduleType();
            schedule[0].weekDay = 1;
            schedule[0].startTime = 0;
            schedule[0].endTime = 24;

            schedule[1] = new ScheduleType();
            schedule[1].weekDay = 7;
            schedule[1].startTime = 0;
            schedule[1].endTime = 1;

            campaign.schedule = schedule;
            campaign.startDate = DateTime.Now.AddMonths(1);
            campaigns[0] = campaign;

            ResHeader resHeader = client.addCampaign(authHeader, ref campaigns);

            Console.WriteLine("CampaignService.addCampaign(): ");
            ObjectDumper.WriteResponse(resHeader, campaigns);
        }

        public static void getCampaign()
        {
            CampaignServiceClient client = new CampaignServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            CampaignType[] result = null;
            ResHeader resHeader = client.getCampaign(authHeader, out result);

            Console.WriteLine("CampaignService.getCampaign(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getCampaignId()
        {
            CampaignServiceClient client = new CampaignServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] result = null;
            ResHeader resHeader = client.getCampaignId(authHeader, out result);

            Console.WriteLine("CampaignService.getCampaignId(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getCampaignByCampaignId()
        {
            CampaignServiceClient client = new CampaignServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] parameters = new long[1];
            parameters[0] = 984751L;

            CampaignType[] result = null;
            ResHeader resHeader = client.getCampaignByCampaignId(authHeader, parameters, out result);

            Console.WriteLine("CampaignService.getCampaignByCampaignId(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void updateCampaign()
        {
            CampaignServiceClient client = new CampaignServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            CampaignType[] parameters = new CampaignType[1];
            parameters[0] = new CampaignType();
            parameters[0].campaignId = 984751L;
            parameters[0].campaignIdSpecified = true;
            parameters[0].campaignName = "Demo_" + DateTime.Now.Ticks % 1000000;
            parameters[0].status = 1;
            parameters[0].statusSpecified = true;

            long[] result = null;
            ResHeader resHeader = client.updateCampaign(authHeader, ref parameters);

            Console.WriteLine("CampaignService.updateCampaign(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }
    }
}
