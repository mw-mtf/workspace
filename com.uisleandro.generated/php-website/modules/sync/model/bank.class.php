<?php
namespace Sync\Model;

class Bank {

	private $db;

	public function __construct(){
		$this->db = \db::getInstance();
	}

	//SYNC PROTOCOL: insert-on-client step 1,2
	public function get_for_client_insert($fk_bank, $page_number, $page_size){
		$query = "SELECT \"id\" as \"server_id\", \"last_update\"".
		",\"code\"".
		",\"name\"".
		" FROM \"simpledb\".\"bank\"".
		" WHERE \"id\" > ". \db::number($fk_bank).
		" LIMIT $page_size OFFSET " . ($page_size * $page_number) .";";

		$stmt = $this->db->query($query);
		$res = $stmt->fetchAll(\PDO::FETCH_OBJ);


		return $res;
	}


	//SYNC PROTOCOL: insert-on-server step 2,2
	public function insert($last_update,$code,$name){
		$last_update_time = \db::timestamp();

		$query = "INSERT INTO \"simpledb\".\"bank\"(".
		"\"last_update\"".
		",\"code\"".
		",\"name\"".") VALUES (".
		\db::string($last_update) .
		"," .\db::string($code) .
		"," .\db::string($name) .");";

		$this->db->query($query);
		$query = "SELECT currval(pg_get_serial_sequence('simpledb.bank','id'));";
		$stmt = $this->db->query($query);
		$id = $stmt->fetch(\PDO::FETCH_NUM)[0];

		$res = new \stdClass();
		$res->server_id = $id;
		$res->last_update_time = $last_update_time;

		return $res;
	}


	//SYNC PROTOCOL: update-on-server step 4,3
	public function update($id, $last_update,$code,$name){

		$last_update_time = \db::timestamp();

		$query = "UPDATE \"simpledb\".\"bank\" SET ".
		"\"last_update\" = ".\db::string($last_update) .
		",\"code\" = ".\db::string($code) .
		",\"name\" = ".\db::string($name) .
		" WHERE id = ". $id.";";

		$this->db->query($query);

		$res = new \stdClass();

		$res->server_id = $id;

		return $res;
	}


	function get_for_client_update($last_update_time, $page_number, $page_size){

		$query = "SELECT \"id\" as \"server_id\", \"last_update\", \"code\", \"name\" FROM \"simpledb\".\"bank\" WHERE last_update_time > " . $last_update_time;

		if(0 < $page_size){
			$query .= " LIMIT " . $page_size . " OFFSET " . ($page_number * $page_size);
		}

		$query .= ";";

		try{
			$stmt = $this->db->query($query);
		}catch(\Exception $ex){
			echo $query . "\n";
		}

		$res = $stmt->fetchAll(\PDO::FETCH_OBJ);

		return $res;

	}

}
