<?php
require_once 'soapclientcore.php';

class GroupService extends Baidu_Api_Client_Core {
	public function __construct() {
		parent::__construct('GroupService');
	}
}

$service = new GroupService();
$output_headers = array();

// Show service definition. 
print('----------service types-----------');
print_r($service->getTypes());
print('----------service functions-----------');
print_r($service->getFunctions());
print("----------service end-----------");

// Call addGroup function
$arguments = array('addGroupRequest' => array(
					'groupTypes' => array(array(
						'campaignId' => 757457,
						'groupName' => 'TestGroup',
						'price' => 200,
						'status' => 1,
						'type' => 0,
					))
				)
			);
print_r($arguments);

$output_response = $service->soapCall('addGroup', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

// Call getGroupByCampaignId function
$arguments = array('getGroupByCampaignIdRequest' => array(
				'campaignId' => 757457)
			);
print_r($arguments);

$output_response = $service->soapCall('getGroupByCampaignId', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);


