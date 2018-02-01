<?php
namespace Sync\Model;

class User {

	private $db;

	public function __construct(){
		$this->db = \db::getInstance();
	}

	//SYNC PROTOCOL: insert-on-client step 1,2
	public function get_for_client_insert($fk_user, $page_number, $page_size){
		$query = "SELECT \"id\" as \"server_id\", \"last_update\"".
		",\"fk_system\"".
		",\"fk_role\"".
		",\"username\"".
		",\"password\"".
		",\"name\"".
		",\"email\"".
		",\"last_use_time\"".
		",\"last_error_time\"".
		",\"error_count\"".
		",\"active\"".
		" FROM \"simpledb\".\"user\"".
		" WHERE \"id\" > ". \db::number($fk_user).
		" LIMIT $page_size OFFSET " . ($page_size * $page_number) .";";

		$stmt = $this->db->query($query);
		$res = $stmt->fetchAll(\PDO::FETCH_OBJ);


		return $res;
	}


	//SYNC PROTOCOL: insert-on-server step 2,2
	public function insert($last_update,$fk_system,$fk_role,$username,$password,$name,$email,$last_use_time,$last_error_time,$error_count,$active){
		$last_update_time = \db::timestamp();

		$query = "INSERT INTO \"simpledb\".\"user\"(".
		"\"last_update\"".
		",\"fk_system\"".
		",\"fk_role\"".
		",\"username\"".
		",\"password\"".
		",\"name\"".
		",\"email\"".
		",\"last_use_time\"".
		",\"last_error_time\"".
		",\"error_count\"".
		",\"active\"".") VALUES (".
		\db::string($last_update) .
		"," .\db::number($fk_system) .
		"," .\db::number($fk_role) .
		"," .\db::string($username) .
		"," .\db::string($password) .
		"," .\db::string($name) .
		"," .\db::string($email) .
		"," .\db::string($last_use_time) .
		"," .\db::string($last_error_time) .
		"," .\db::string($error_count) .
		"," .\db::string($active) .");";

		$this->db->query($query);
		$query = "SELECT currval(pg_get_serial_sequence('simpledb.user','id'));";
		$stmt = $this->db->query($query);
		$id = $stmt->fetch(\PDO::FETCH_NUM)[0];

		$res = new \stdClass();
		$res->server_id = $id;
		$res->last_update_time = $last_update_time;

		return $res;
	}


	//SYNC PROTOCOL: update-on-server step 4,3
	public function update($id, $last_update,$fk_system,$fk_role,$username,$password,$name,$email,$last_use_time,$last_error_time,$error_count,$active){

		$last_update_time = \db::timestamp();

		$query = "UPDATE \"simpledb\".\"user\" SET ".
		"\"last_update\" = ".\db::string($last_update) .
		",\"fk_system\" = ".\db::number($fk_system) .
		",\"fk_role\" = ".\db::number($fk_role) .
		",\"username\" = ".\db::string($username) .
		",\"password\" = ".\db::string($password) .
		",\"name\" = ".\db::string($name) .
		",\"email\" = ".\db::string($email) .
		",\"last_use_time\" = ".\db::string($last_use_time) .
		",\"last_error_time\" = ".\db::string($last_error_time) .
		",\"error_count\" = ".\db::string($error_count) .
		",\"active\" = ".\db::string($active) .
		" WHERE id = ". $id.";";

		$this->db->query($query);

		$res = new \stdClass();

		$res->server_id = $id;

		return $res;
	}


	function get_for_client_update($last_update_time, $page_number, $page_size){

		$query = "SELECT \"id\" as \"server_id\", \"last_update\", \"fk_system\", \"fk_role\", \"username\", \"password\", \"name\", \"email\", \"last_use_time\", \"last_error_time\", \"error_count\", \"active\" FROM \"simpledb\".\"user\" WHERE last_update_time > " . $last_update_time;

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
