<?php
require_once 'soapclientcore.php';

class GroupConfigService extends Baidu_Api_Client_Core {
	public function __construct() {
		parent::__construct('GroupConfigService');
	}
}

$service = new GroupConfigService();
$output_headers = array();

// Show service definition. 
print('----------service types-----------');
print_r($service->getTypes());
print('----------service functions-----------');
print_r($service->getFunctions());
print("----------service end-----------");

// Call setTargetInfo function
$arguments = array('setTargetInfoRequest' => array(
					'targetInfo' => array(
						'type' => 2,
						'groupId' => 228,
						'ktItem' => array(
							'targetType' => 2,
							'aliveDays' => 30,
							'ktWordList' => array(
							array(
								'pattern' => 0,
								'keyword' => 'word1'
							),
							array(
								'pattern' => 0,
								'keyword' => 'word2'
							))
						)
					)
				)
			);
print_r($arguments);

$output_response = $service->soapCall('setTargetInfo', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);


// Call getTargetInfo function
$arguments = array('getTargetInfoRequest' => array(
					'groupIds' => array(
						228,
						757447
					)
				)
			);
print_r($arguments);

$output_response = $service->soapCall('getTargetInfo', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);



