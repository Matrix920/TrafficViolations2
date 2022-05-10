<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Users
 *
 * @author Matrix
 */
class VehiclesLog {
    
    private $conn;
    public function __construct() {
        require_once 'Db_connect.php';
        $db=new Db_connect();
        $this->conn=$db->connect();
    }
    //put your code here
    function signup($plugedNumber,$driver,$type,$category,$productionDate,$registerationDate,$isCrossOut) {
        $response=array();
        $response["error"]=true;
        if(! $this->plugedNumberExist($plugedNumber)){
            
            $stmt= $this->conn->prepare("insert into vehicles_log(PlugedNumber,Driver,Type,Category,ProductionDate,RegisterationDate,IsCrossOut) values(?,?,?,?,?,?,?)");
            $stmt->bind_param("isssssi",$plugedNumber,$driver,$type,$category,$productionDate,$registerationDate,$isCrossOut);
            if($stmt->execute()){
                $response["error"]=false;
            }
            
            $stmt->close();
        }
        return $response;
    }
    
    function plugedNumberExist($plugedNumber){
        $stmt= $this->conn->prepare("select * from vehicles_log where plugedNumber=?");
        $stmt->bind_param("s",$plugedNumber);
        $stmt->execute();
        $stmt->store_result();
        if($stmt->num_rows>0){
            $stmt->close();
            return true;
        }
         $stmt->close();
        return FALSE;
    }
    
    function login($driver,$plugedNumber){
        $result=array();
        $result["error"]=true;
        $result["isAdmin"]=FALSE;
        
        //check for admin login
        if($driver=="administrator" && $plugedNumber==123456){
           $result["error"]=false; 
           $result["isAdmin"]=TRUE; 
           
           return $result;
        }
        
        //check for dirver login
        $stmt= $this->conn->prepare("select PlugedNumber,Driver,Type,Category,ProductionDate,RegisterationDate,IsCrossOut from vehicles_log where PlugedNumber=? and Driver=?");
        $stmt->bind_param("is",$plugedNumber,$driver);
        
        $stmt->execute();
        $stmt->store_result();
        if($stmt->num_rows>0){
            $stmt->bind_result($plugedNumber,$driver,$type,$category,$productionDate,$registerationDate,$isCrossOut);
            $stmt->fetch();
            
            $result["error"]=FALSE;
            $result["PlugedNumber"]=$plugedNumber;
            $result["Driver"]=$driver;
            $result["Type"]=$type;
            $result["Category"]=$category;
            $result["ProductionDate"]=$productionDate;
            $result["RegisterationDate"]=$registerationDate;
            $result["IsCrossOut"]=$isCrossOut;
            
        }
        $stmt->close();
        return $result;
    }
    
    function getVehicles(){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("select * from vehicles_log");
        $stmt->execute();
        $res=$stmt->get_result();
        if($res->num_rows>0){
            $response["error"]=false;
            $response["vehicles"]=array();
            while ($row = $res->fetch_assoc()) {
                $vehicle=array();
                $vehicle["PlugedNumber"]=$row["PlugedNumber"];
                $vehicle["Driver"]=$row["Driver"];
                array_push($response["vehicles"],$vehicle);
            }
        }
        
        $stmt->close();
        
        return $response;
    }
    
    function getVehicleInfo($plugedNumber) {
        $stmt= $this->conn->prepare("select * from vehicles_log where PlugedNumber=?");
        $stmt->bind_param("i",$plugedNumber);
        $response=array();
        $response["error"]=true;
        $stmt->execute();
        $res=$stmt->get_result();
        $stmt->close();
        if($res->num_rows>0){
            $response["error"]=false;
            $result=$res->fetch_assoc();
            $response["PlugedNumber"]=$result["PlugedNumber"];
            $response["Driver"]=$result["Driver"];
            $response["RegisterationDate"]=$result["RegisterationDate"];
            $response["ProductionDate"]=$result["ProductionDate"];
            $response["Type"]=$result["Type"];
            $response["Category"]=$result["Category"];
            $response["IsCrossOut"]=$result["IsCrossOut"];
        }
        
        return $response;
    }
    
    function crossOutVehicle($plugedNumber){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("update vehicles_log set IsCrossOut=1 where PlugedNumber=?");
        $stmt->bind_param("i",$plugedNumber);
        if($stmt->execute()){
            $response["error"]=false;
        }
        return $response;
    }
    


}
