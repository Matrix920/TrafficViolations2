<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Violations
 *
 * @author Matrix
 */
class Violations {
    private $conn;
    public function __construct() {
        require_once 'Db_connect.php';
        $db=new Db_connect();
        $this->conn=$db->connect();
    }
    //put your code here
    function addViolation($violationType,$tax) {
        $response=array();
        $response["error"]=true;
            
        $stmt= $this->conn->prepare("insert into violations(ViolationType,Tax) values(?,?)");
        $stmt->bind_param("sd",$violationType,$tax);
        $stmt->execute();
        $stmt->close();
        $response["error"]=false;
        return $response;
    }
    
  function delete_violation($violationID) {
        $response=array();
        $response["error"]=true;
            
        $stmt= $this->conn->prepare("delete from violations where ViolationID=?");
        $stmt->bind_param("i",$violationID);
        $stmt->execute();
        $stmt->close();
        $response["error"]=false;
        return $response;
    }
    
    function getVioloations(){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("select * from violations");
        $stmt->execute();
        $res=$stmt->get_result();
        if($res->num_rows>0){
            $response["error"]=false;
            $response["violations"]=array();
            while ($row = $res->fetch_assoc()) {
                $violation=array();
                $violation["ViolationID"]=$row["ViolationID"];
                $violation["ViolationType"]=$row["ViolationType"];
                $violation["Tax"]=$row["Tax"];
                array_push($response["violations"],$violation);
            }
        }
        
        $stmt->close();
        
        return $response;
    }
    
     function getViolationInfo($violationID) {
        $stmt= $this->conn->prepare("select * from violations where ViolationID=?");
        $stmt->bind_param("i",$violationID);
        $response=array();
        $response["error"]=true;
        $stmt->execute();
        $res=$stmt->get_result();
        $stmt->close();
        if($res->num_rows>0){
            $response["error"]=false;
            $result=$res->fetch_assoc();
            $response["Tax"]=$result["Tax"];
            $response["ViolationType"]=$result["ViolationType"];
            $response["ViolationID"]=$violationID;
        }
        
        return $response;
    }
    
    function updateViolation($violationID,$violationType,$tax){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("update violations set ViolationType=?,Tax=? where ViolationID=?");
        $stmt->bind_param("sdi",$violationType,$tax,$violationID);
        if($stmt->execute()){
            $response["error"]=false;
        }
        return $response;
    }
    
}
