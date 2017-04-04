<?php
require_once 'soapclientcore.php';

class PeopleService extends Baidu_Api_Client_Core {
	public function __construct() {
		parent::__construct('PeopleService');
	}
}

$service = new PeopleService();
$output_headers = array();

// Show service definition. 
print('----------service types-----------');
print_r($service->getTypes());
print('----------service functions-----------');
print_r($service->getFunctions());
print("----------service end-----------");

// Call getPeople function
$arguments = array('getPeopleRequest' => array(13,14,15));
print_r($arguments);

$output_response = $service->soapCall('getPeople', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

// Call getAllPeople function
$arguments = array('getAllPeopleRequest' => array());
print_r($arguments);

$output_response = $service->soapCall('getAllPeople', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);