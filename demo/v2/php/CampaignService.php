<?php
require_once 'soapclientcore.php';

class CampaignService extends Baidu_Api_Client_Core {
	public function __construct() {
		parent::__construct('CampaignService');
	}
}

$service = new CampaignService();
$output_headers = array();

// Show service definition. 
print('----------service types-----------');
print_r($service->getTypes());
print('----------service functions-----------');
print_r($service->getFunctions());
print("----------service end-----------");

// Call addCampaign function
$arguments = array('addCampaignRequest' => array(
					'campaignTypes' => array(array(
						'campaignName' => 'TestCampaign',
						'budget' => 200,
						'status' => 1,
						'startDate' => '2012-03-20',
						'endDate' => '2012-04-20'
					))
				)
			);
print_r($arguments);

$output_response = $service->soapCall('addCampaign', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

// Call getCampaign function
$arguments = array('getCampaignRequest' => array(
				)
			);
print_r($arguments);

$output_response = $service->soapCall('getCampaign', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

// Call getCampaignByCampaignId function
$arguments = array('getCampaignByCampaignIdRequest' => array(
				757446, 757447)
			);
print_r($arguments);

$output_response = $service->soapCall('getCampaignByCampaignId', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

