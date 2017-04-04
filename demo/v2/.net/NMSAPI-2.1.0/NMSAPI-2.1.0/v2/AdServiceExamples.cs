using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.AdService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class AdServiceExamples
    {
        public static void addAd()
        {
            AdServiceClient service = new AdServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            AdType[] parameters = new AdType[2];
            parameters[0] = new AdType();
            parameters[0].localId = 1000001L;
            parameters[0].groupId = 2562221L;
            parameters[0].groupIdSpecified = true;
            parameters[0].description1 = "描述1的文字内容";
            parameters[0].description2 = "描述2的文字内容";
            parameters[0].displayUrl = "www.baidu.com";
            parameters[0].destinationUrl = "http://www.baidu.com";
            parameters[0].title = "创意标题内容";
            parameters[0].type = 1;
            parameters[0].typeSpecified = true;
            parameters[0].status = 0;

            parameters[1] = new AdType();
            parameters[1].localId = 1000002L;
            parameters[1].groupId = 2562221L;
            parameters[1].groupIdSpecified = true;
            parameters[1].displayUrl = "www.baidu.com";
            parameters[1].destinationUrl = "http://www.baidu.com/targeturl";
            parameters[1].title = "picture_name";
            parameters[1].type = 2;
            parameters[1].typeSpecified = true;
            parameters[1].status = 0;
            parameters[1].imageData = ImageUtils.GetImageDataFromFile("../../data/160_600.jpg");
            parameters[1].width = 160;
            parameters[1].widthSpecified = true;
            parameters[1].height = 600;
            parameters[1].heightSpecified = true;

            ResHeader resHeader = service.addAd(authHeader, ref parameters);

            Console.WriteLine("AdService.addAd(): ");
            ObjectDumper.WriteResponse(resHeader, parameters);
        }

        public static void copyAd()
        {
            AdServiceClient service = new AdServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] adIds = new long[] { 26876764L, 26874052L };
            long[] groupIds = new long[] { 2562221L, 2199683L };

            string result = null;
            ResHeader resHeader = service.copyAd(authHeader, groupIds, adIds, out result);

            Console.WriteLine("AdService.copyAd(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void deleteAd()
        {
            AdServiceClient client = new AdServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] adids = new long[] { 1055186 };
            string result = null;

            ResHeader resHeader = client.deleteAd(authHeader, adids, out result);

            Console.WriteLine("AdService.deleteAd(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getAdByAdId()
        {
            AdServiceClient client = new AdServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            AdType[] result = null;

            ResHeader resHeader = client.getAdByAdId(authHeader, new long[] { 27108656L, 27108657L }, out result);

            Console.WriteLine("AdService.getAdByAdId(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getAdByGroupId()
        {
            AdServiceClient client = new AdServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long groupId = 2562221L;

            AdType[] result = null;

            ResHeader resHeader = client.getAdByGroupId(authHeader, groupId, out result);

            Console.WriteLine("AdService.getAdByGroupId(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void getAdIdByGroupId()
        {
            AdServiceClient client = new AdServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            int groupId = 2562221;

            long[] result = null;

            ResHeader resHeader = client.getAdIdByGroupId(authHeader, groupId, out result);

            Console.WriteLine("AdService.getAdIdByGroupId(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void replaceAd()
        {
            AdServiceClient service = new AdServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            long[] adIds = new long[] { 26876764L, 26874052L };
            long adId = 27108657L;

            string result = null;
            ResHeader resHeader = service.replaceAd(authHeader, adIds, adId, out result);

            Console.WriteLine("AdService.replaceAd(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void setAdStatus()
        {
            AdServiceClient service = new AdServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            StatusType[] status = new StatusType[1];
            status[0] = new StatusType();
            status[0].adId = 27108657L;
            status[0].status = 0;

            string result = null;
            ResHeader resHeader = service.setAdStatus(authHeader, status, out result);

            Console.WriteLine("AdService.setAdStatus(): ");
            ObjectDumper.WriteResponse(resHeader, result);
        }

        public static void updateAd()
        {
            AdServiceClient service = new AdServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            AdType[] parameters = new AdType[1];
            parameters[0] = new AdType();
            parameters[0].adId = 27108657L;
            parameters[0].adIdSpecified = true;
            parameters[0].description1 = "描述1的文字内容-图文";
            parameters[0].description2 = "描述2的文字内容-图文";
            parameters[0].displayUrl = "www.baidu.com";
            parameters[0].destinationUrl = "http://www.baidu.com";
            parameters[0].imageData = ImageUtils.GetImageDataFromFile("../../data/60_60.jpg");
            parameters[0].width = 60;
            parameters[0].widthSpecified = true;
            parameters[0].height = 60;
            parameters[0].heightSpecified = true;
            parameters[0].title = "创意标题内容-图文";
            parameters[0].type = 5;
            parameters[0].typeSpecified = true;

            ResHeader resHeader = service.updateAd(authHeader, ref parameters);

            Console.WriteLine("AdService.updateAd(): ");
            ObjectDumper.WriteResponse(resHeader, parameters);
        }
    }
}
