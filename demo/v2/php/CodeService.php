<?php
require_once 'soapclientcore.php';

class CodeService extends Baidu_Api_Client_Core {
	public function __construct() {
		parent::__construct('CodeService');
	}
}

$service = new CodeService();
$output_headers = array();

// Show service definition. 
print('----------service types-----------');
print_r($service->getTypes());
print('----------service functions-----------');
print_r($service->getFunctions());
print("----------service end-----------");

// Call getAllCategory function
$arguments = array('getAllCategoryRequest' => array());
print_r($arguments);

$output_response = $service->soapCall('getAllCategory', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

// Call getAllRegion function
$arguments = array('getAllRegionRequest' => array());
print_r($arguments);

$output_response = $service->soapCall('getAllRegion', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);