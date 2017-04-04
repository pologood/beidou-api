<?php
require_once 'soapclientcore.php';

class AccountService extends Baidu_Api_Client_Core {
	public function __construct() {
		parent::__construct('AccountService');
	}
}

$service = new AccountService();
$output_headers = array();

// Show service definition. 
print('----------service types-----------');
print_r($service->getTypes());
print('----------service functions-----------');
print_r($service->getFunctions());
print("----------service end-----------");

// Call getAccountInfo function
$arguments = array('getAccountInfoRequest' => array());
print_r($arguments);

$output_response = $service->soapCall('getAccountInfo', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);