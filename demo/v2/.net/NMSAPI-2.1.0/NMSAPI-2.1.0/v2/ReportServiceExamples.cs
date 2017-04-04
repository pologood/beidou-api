using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using com.baidu.api.sem.nms.v2.ReportService;
using com.baidu.api.samples.lib;

namespace com.baidu.api.samples.v2
{
    class ReportServiceExamples
    {
        public static void getReportId()
        {
            ReportServiceClient client = new ReportServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            ReportRequestType request = new ReportRequestType();

            //report type: 2 means campaign report
            request.reportType = 2;

            //statRange: 2 means statRange is campaign
            request.statRange = 2;

            DateTime startTime = DateTime.Now.AddYears(-1);
            request.startDate = startTime;
            DateTime endTime = DateTime.Now.AddDays(-1);
            request.endDate = endTime;

            request.statIds = new long[] { 3281, 984745, 984752 };

            request.performanceData = new string[] { "srch", "click", "cost", "ctr", "cpm", "acp" };

            //idOnly is not required, default value is false which means you can get literal info of material in returned result.
            //When literal info is not that useful for you, we appreciate you set true to this field, which could provide you with higher performance
            request.idOnly = false;

            //format is not required, default value is 0, means zip. you can specify 1 to csv
            request.format = 0;

            string reportId = null;
            ResHeader resHeader = client.getReportId(authHeader, request, out reportId);

            Console.WriteLine("ReportService.getReportId(): ");
            ObjectDumper.WriteResponse(resHeader, reportId);
        }

        public static void getReportState()
        {
            ReportServiceClient client = new ReportServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            string reportId = "0c6e5f13593e282d014397b72d63e04d";

            // 1:waiting, 2:handling, 3: success, 4: failed
            int isGenerated = -1;
            ResHeader resHeader = client.getReportState(authHeader, reportId, out isGenerated);

            Console.WriteLine("ReportService.getReportState(): ");
            ObjectDumper.WriteResponse(resHeader, isGenerated);
        }

        public static void getReportFileUrl()
        {
            ReportServiceClient client = new ReportServiceClient();

            AuthHeader authHeader = new AuthHeader();
            HeaderUtil.loadHeader(authHeader);

            string reportId = "0c6e5f13593e282d014397b72d63e04d";

            string reportFilePath = null;
            ResHeader resHeader = client.getReportFileUrl(authHeader, reportId, out reportFilePath);

            Console.WriteLine("ReportService.getReportFileUrl(): ");
            ObjectDumper.WriteResponse(resHeader, reportFilePath);
        }
    }
}
