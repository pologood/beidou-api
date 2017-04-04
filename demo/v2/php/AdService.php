<?php
require_once 'soapclientcore.php';

class AdService extends Baidu_Api_Client_Core {
	public function __construct() {
		parent::__construct('AdService');
	}
}

$service = new AdService();
$output_headers = array();

// Show service definition. 
print('----------service types-----------');
print_r($service->getTypes());
print('----------service functions-----------');
print_r($service->getFunctions());
print("----------service end-----------");

// Call addAd function with text
$arguments = array('addAdRequest' => array(
					'adTypes' => array(array(
						'localId' => 123456,
						'groupId' => 2166166,
						'status' => 0,
						'type' => 1,
						'title' => 'TestAd',
						'displayUrl' => 'baidu.com',
						'destinationUrl' => 'http://baidu.com/123',
						'description1' => 'desc1234567',
						'description2' => 'desc2345678')
					)
				)
			);
print_r($arguments);

$output_response = $service->soapCall('addAd', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

// Call addAd function with image
$file = 'e:\phpapi\60_60.jpg';
$image = file_get_contents($file);
$arguments = array('addAdRequest' => array(
					'adTypes' => array(array(
						'localId' => 123456,
						'groupId' => 2166166,
						'status' => 0,
						'type' => 5,
						'title' => 'TestAd',
						'displayUrl' => 'baidu.com',
						'destinationUrl' => 'http://baidu.com/123',
						'description1' => 'desc1234567',
						'description2' => 'desc2345678',
						'imageData' => $image,
						'width' => 60,
						'height' => 60)
					)
				)
			);
print_r($arguments);

$output_response = $service->soapCall('addAd', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

// Call setAdStatus function
$arguments = array('setAdStatusRequest' => array(
				'statusTypes' => array(
						'adId' => 9283659,
						'status' => 1
					)
				)
			);
print_r($arguments);

$output_response = $service->soapCall('setAdStatus', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

// Call getAdByGroupId function
$arguments = array('getAdByGroupIdRequest' => array(
				'groupId' => 2166166
				)
			);
print_r($arguments);

$output_response = $service->soapCall('getAdByGroupId', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

// Call copyAd function
$arguments = array('copyAdRequest' => array(
					groupIds => array(2166165, 2166164),
					adIds => array(9283673, 9283672)
				)
			);
print_r($arguments);

$output_response = $service->soapCall('copyAd', $arguments, $output_headers);
print('----------output body-----------');
print_r($output_response);
print('----------output header-----------');
print_r($output_headers);

