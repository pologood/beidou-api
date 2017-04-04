using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.FCService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class FCServiceExamples
    {
        public static void getFCCampaign()
        {
            FCServiceClient service = new FCServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            FCCampaignType[] fcCampaigns = null;

            ResHeader resHeader = service.getFCCampaign(authHeader, out fcCampaigns);

            Console.WriteLine("FCService.getFCCampaign(): ");
            ObjectDumper.WriteResponse(resHeader, fcCampaigns);
        }

        public static void getFCCampaignId()
        {
            FCServiceClient service = new FCServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] fcCampaignIds = null;

            ResHeader resHeader = service.getFCCampaignId(authHeader, out fcCampaignIds);

            Console.WriteLine("FCService.getFCCampaignId(): ");
            ObjectDumper.WriteResponse(resHeader, fcCampaignIds);
        }

        public static void getFCCampaignByFCCampaignId()
        {
            FCServiceClient service = new FCServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] fcCampaignIds = new long[] { 415916L };
            FCCampaignType[] fcCampaigns = null;

            ResHeader resHeader = service.getFCCampaignByFCCampaignId(authHeader, fcCampaignIds, out fcCampaigns);

            Console.WriteLine("FCService.getFCCampaignByFCCampaignId(): ");
            ObjectDumper.WriteResponse(resHeader, fcCampaigns);
        }

        public static void getFCUnitByFCCampaignId()
        {
            FCServiceClient service = new FCServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long fcCampaignId = 415916L;
            FCUnitType[] fcUnits = null;

            ResHeader resHeader = service.getFCUnitByFCCampaignId(authHeader, fcCampaignId, out fcUnits);

            Console.WriteLine("FCService.getFCUnitByFCCampaignId(): ");
            ObjectDumper.WriteResponse(resHeader, fcUnits);
        }

        public static void getFCUnitIdByFCCampaignId()
        {
            FCServiceClient service = new FCServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long fcCampaignId = 415916L;
            long[] fcUnitIds = null;

            ResHeader resHeader = service.getFCUnitIdByFCCampaignId(authHeader, fcCampaignId, out fcUnitIds);

            Console.WriteLine("FCService.getFCUnitIdByFCCampaignId(): ");
            ObjectDumper.WriteResponse(resHeader, fcUnitIds);
        }

        public static void getFCUnitByFCUnitId()
        {
            FCServiceClient service = new FCServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] fcUnitIds = new long[] { 54572791L };
            FCUnitType[] fcUnits = null;

            ResHeader resHeader = service.getFCUnitByFCUnitId(authHeader, fcUnitIds, out fcUnits);

            Console.WriteLine("FCService.getFCUnitByFCUnitId(): ");
            ObjectDumper.WriteResponse(resHeader, fcUnits);
        }
    }
}
