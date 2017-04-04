<?php
require_once 'soapclientcore.php';

class ReportService extends Baidu_Api_Client_Core {
	public function __construct() {
		parent::__construct('ReportService');
	}
}

$service = new ReportService();
$output_headers = array();

// Show service definition. 
print('----------service types-----------');
print_r($service->getTypes());
print('----------service functions-----------');
print_r($service->getFunctions());
print("----------service end-----------");

// Call getReportId function
$arguments = array('getReportIdRequest' => array(
					'reportRequestType' => array (
						'performanceData' => array('srch', 'click', 'cost', 'ctr', 'cpm', 'acp'), 
						'reportType' => 2, 
						'statRange' => 2, 
						'startDate' => '2012-01-01T00:00:00', 
						'endDate' => '2012-01-16T00:00:00', 
						'statIds' => array(757446,757447,757448)
					)
				)
			);
print_r($arguments);

$output_response = $service->soapCall('getReportId', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

$reportId = $output_response->reportId;

// Call getReportState function
$arguments = array('getReportIdRequest' => $output_response);
print_r($arguments);

$retry=1;
$status = $service->soapCall('getReportState', $arguments, $output_headers);
print('----------output body-----------');
print_r($status);
print('----------output header-----------');
print_r($status);
while($status->isGenerated != "3")
{
	if($retry>=5)
		break;
	print('Sleep 10 seconds for next check report state...');
	sleep(10);
	$status = $service->soapCall('getReportState', $arguments, $output_headers);
	print('----------output body-----------');
	print_r($status);
	print('----------output header-----------');
	print_r($status);
	$retry++;
}


// Call getReportFileUrl function
$fileUrl = array('getReportFileUrlRequest' => $output_response);
print_r($arguments);

$fileUrl = $service->soapCall('getReportFileUrl', $arguments, $output_headers);
print('----------output body-----------');
print_r($fileUrl);
print('----------output header-----------');
print_r($fileUrl);

