<?php

namespace Sync\Controller;

use Sync\Model\Reseller as ResellerModel;

class Reseller {

	public function __construct(){
		if(empty($GLOBALS["ENABLE_SYNC_MODULE"])){
			die();
		}
	}

	//SYNC PROTOCOL: insert-on-client step 1,2
	public function get_for_client_insert(){
		$error_msg = "{\"status\":\"error\", \"usage\":\"{ \\\"server_id\\\" : (number), \\\"page_number\\\" : (number), \\\"page_size\\\" : (number)}\" }";
		if(isset($_REQUEST["json"])){
			$obj = json_decode($_REQUEST["json"]);
			if(NULL == $obj){
				echo $error_msg;
				die();
			}
			$reseller = new ResellerModel();
			$result = $reseller->get_for_client_insert($obj->server_id, $obj->page_number, $obj->page_size);
			echo json_encode($result, JSON_NUMERIC_CHECK);
		} else {
			echo $error_msg;
			die();
		}
	}

	//SYNC PROTOCOL: insert-on-server step 2,2
	function bash_insert(){
		$error_msg = "{\"status\":\"error\", \"usage\":\"[{\\\"client_id\\\" : (long),\\\"last_update\\\" : (datetime),\\\"system_amount\\\" : (number),\\\"name\\\" : (mediumtext),\\\"address\\\" : (mediumtext),\\\"neighborhood\\\" : (mediumtext),\\\"city\\\" : (mediumtext),\\\"state\\\" : (smalltext),\\\"zip_code\\\" : (smalltext)}]\"}";
		if(isset($_REQUEST["json"])){
			$rows = json_decode($_REQUEST["json"]);
			if(NULL == $rows || empty($rows)){
				echo $error_msg;
				die();
			}
			$reseller = new ResellerModel();
			$result = "[";
			for($i = 0; $i < sizeof($rows); $i++){
				$ret = $reseller->insert($rows[$i]->last_update,$rows[$i]->system_amount,$rows[$i]->name,$rows[$i]->address,$rows[$i]->neighborhood,$rows[$i]->city,$rows[$i]->state,$rows[$i]->zip_code);
				$ret->client_id = $rows[$i]->client_id;
				if($i > 0){
					$result .= ",";
				}
				$result .= json_encode($ret, JSON_NUMERIC_CHECK);
			}
			$result .= "]";
			echo $result;
		} else {
			echo $error_msg;
		}
	}



	//SYNC PROTOCOL: update-on-server step 4,3
	function bash_update(){
		$error_msg = "{ \"status\" : \"error\" , \"usage\" : \"[{\\\"client_id\\\" : (long),\\\"server_id\\\" : (long),\\\"last_update\\\" : (datetime),\\\"system_amount\\\" : (number),\\\"name\\\" : (mediumtext),\\\"address\\\" : (mediumtext),\\\"neighborhood\\\" : (mediumtext),\\\"city\\\" : (mediumtext),\\\"state\\\" : (smalltext),\\\"zip_code\\\" : (smalltext)}]\"}"; 
		if(isset($_REQUEST["json"])){
			$rows = json_decode($_REQUEST["json"]);
			if(NULL == $rows || empty($rows)){
				echo $error_msg;
				die();
			}
			$reseller = new ResellerModel();
			$result = "[";
			for($i = 0; $i < sizeof($rows); $i++){
				$ret = $reseller->update($rows[$i]->server_id,$rows[$i]->last_update,$rows[$i]->system_amount,$rows[$i]->name,$rows[$i]->address,$rows[$i]->neighborhood,$rows[$i]->city,$rows[$i]->state,$rows[$i]->zip_code);
				$ret->client_id = $rows[$i]->client_id;
				if($i > 0){
					$result .= ",";
				}
				$result .= json_encode($ret, JSON_NUMERIC_CHECK);
			}
			$result .= "]";
			echo $result;
		} else {
			echo $error_msg;
		}
	}


	function get_for_client_update(){
		$error_msg = "{ \"status\" : \"error\" , \"usage\" : \"{ \"last_update_time\": (number), \"page_number\":  (number), \"page_size\": (number) }\"}"; 
		if(isset($_REQUEST["json"])){
			$obj = json_decode($_REQUEST["json"]);
			if(NULL == $obj){
				echo $error_msg;
				return;
			}
			if(0 == $obj->last_update_time){
					echo "[]";
					return;
			}
			$reseller = new ResellerModel();
			$result = $reseller->get_for_client_update($obj->last_update_time, $obj->page_number, $obj->page_size);
			echo json_encode($ret, JSON_NUMERIC_CHECK);
		} else {
			echo $error_msg;
		}
	}

}
