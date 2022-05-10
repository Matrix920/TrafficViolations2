<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matrix
 */
class ViolationsLog {
    
    private $conn;
    public function __construct() {
        require_once 'Db_connect.php';
        $db=new Db_connect();
        $this->conn=$db->connect();
    }
    
    
    function addViolation($violationID,$plugedNumber,$date,$location,$isPaid){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("insert into violations_log(ViolationID,PlugedNumber,Date,Location,IsPaid) values(?,?,?,?,?)");
        $stmt->bind_param("iissi",$violationID,$plugedNumber,$date,$location,$isPaid);
        if($stmt->execute()){
            $response["error"]=false;
        }
        return $response;
    }
    
    function updateViolation($violationLogID,$violationID,$plugedNumber,$date,$location,$isPaid){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("update violations_log set ViolationID=?,PlugedNumber=?,Date=?,Location=?,IsPaid=? where ViolationLogID=?");
        $stmt->bind_param("iissii",$violationID,$plugedNumber,$date,$location,$isPaid,$violationLogID);
        if($stmt->execute()){
            $response["error"]=false;
        }
        return $response;
    }
    
    function pay($violationLogID){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("update violations_log set IsPaid=1 where ViolationLogID=?");
        $stmt->bind_param("i",$violationLogID);
        if($stmt->execute()){
            $response["error"]=false;
        }
        return $response;
    }
    
    function deleteViolation($violationLogID){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("delete from violations_log where ViolationLogID=?");
        $stmt->bind_param("i",$violationLogID);
        if($stmt->execute()){
            $response["error"]=false;
        }
        return $response;
    }
    
    /*
     * return violations occured by a driver
     * @plugedNumber
     * outputs Violations with @ViolationLogID and @Date
     */
    function getViolationsByPlugedNumber($plugedNumber){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("select * from violations_log as vl inner join violations as v on vl.ViolationID=v.ViolationID where PlugedNumber=? and IsPaid=0");
        $stmt->bind_param("i",$plugedNumber);
        $stmt->execute();
        $res=$stmt->get_result();
        if($res->num_rows>0){
            $response["error"]=false;
            $response["violations"]=array();
            while ($row = $res->fetch_assoc()) {
                $violation=array();
                $violation["ViolationLogID"]=$row["ViolationLogID"];
                $violation["Date"]=$row["Date"];
                $violation["Location"]=$row["Location"];
				$violation["Tax"]=$row["Tax"];
				$violation["ViolationType"]=$row["ViolationType"];
                array_push($response["violations"],$violation);
            }
        }
        
        $stmt->close();
        
        return $response;
    }
    
    /*
     * get violation info for the driver
     * input @violationLogID
     * ouput @Date @Tax @Location
     */
    function getViolationInfo($violationLogID) {
        $stmt= $this->conn->prepare("select vl.ViolationLogID as ViolationLogID,vl.PlugedNumber as PlugedNumber,vh.Driver as Driver,vl.IsPaid as IsPaid,vl.Date as Date,vl.Location as Location,v.Tax as Tax,v.ViolationType as ViolationType from violations_log as vl inner join violations as v on vl.ViolationID=v.ViolationID inner join vehicles_log as vh on vl.PlugedNumber=vh.PlugedNumber where vl.ViolationLogID=?");
        $stmt->bind_param("i",$violationLogID);
        $response=array();
        $response["error"]=true;
        $stmt->execute();
        $res=$stmt->get_result();
        $stmt->close();
        if($res->num_rows>0){
            $response["error"]=false;
            $result=$res->fetch_assoc();
            $response["Date"]=$result["Date"];
            $response["Location"]=$result["Location"];
            $response["Tax"]=$result["Tax"];
            $response["IsPaid"]=$result["IsPaid"];
            $response["ViolationType"]=$result["ViolationType"];
            $response["Driver"]=$result["Driver"];
            $response["PlugedNumber"]=$result["PlugedNumber"];
            $response["ViolationLogID"]=$result["ViolationLogID"];
        }
        
        return $response;
    }
    //_______________________________________________________
    
    /*
     * get violations by driver
     */
    function getViolationsByDriver($driver){
        $response=array();
        $response["error"]=true;
        $driver="%$driver%";
        $stmt= $this->conn->prepare("select vl.ViolationLogID as ViolationLogID,vhl.Driver as Driver,v.Tax as Tax from violations_log as vl inner join vehicles_log as vhl on vl.PlugedNumber=vhl.PlugedNumber inner join violations  as v on vl.ViolationID=v.ViolationID where vhl.Driver Like ?");
        $stmt->bind_param("s",$driver);
        $stmt->execute();
        $res=$stmt->get_result();
        if($res->num_rows>0){
            $response["error"]=false;
            $response["violations"]=array();
            $tax=0;
            while ($row = $res->fetch_assoc()) {
				$violation= $this->getViolationInfo($row["ViolationLogID"]);
                //$violation=array();
                //$violation["ViolationLogID"]=$row["ViolationLogID"];
                //$violation["Driver"]=$row["Driver"];
                $tax=$tax+$row["Tax"];
                array_push($response["violations"],$violation);
            }
            $response["Tax"]=$tax;
        }
        
        $stmt->close();
        
        return $response;
    }
    
    function getViolationsByDate($fromDate,$toDate){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("select vl.ViolationLogID as ViolationLogID,vhl.Driver as Driver,v.Tax as Tax from violations_log as vl inner join vehicles_log as vhl on vl.PlugedNumber=vhl.PlugedNumber inner join violations  as v on vl.ViolationID=v.ViolationID  where vl.Date between ? and ?");
        $stmt->bind_param("ss",$fromDate,$toDate);
        $stmt->execute();
        $res=$stmt->get_result();
        if($res->num_rows>0){
            $response["error"]=false;
            $response["violations"]=array();
            $tax=0;
            while ($row = $res->fetch_assoc()) {
				$violation= $this->getViolationInfo($row["ViolationLogID"]);
                //$violation=array();
                //$violation["ViolationLogID"]=$row["ViolationLogID"];
                //$violation["Driver"]=$row["Driver"];
                $tax=$tax+$row["Tax"];
                array_push($response["violations"],$violation);
            }
            $response["Tax"]=$tax;
        }
        
        $stmt->close();
        
        return $response;
    }
    
    function getViolationsByLocation($location){
        $response=array();
        $response["error"]=true;
        $location="%$location%";
        $stmt= $this->conn->prepare("select vl.ViolationLogID as ViolationLogID,vhl.Driver as Driver,v.Tax as Tax from violations_log as vl inner join vehicles_log as vhl on vl.PlugedNumber=vhl.PlugedNumber inner join violations  as v on vl.ViolationID=v.ViolationID  where vl.Location like ?");
        $stmt->bind_param("s",$location);
        $stmt->execute();
        $res=$stmt->get_result();
        if($res->num_rows>0){
            $response["error"]=false;
            $response["violations"]=array();
            $tax=0;
            while ($row = $res->fetch_assoc()) {
				$violation= $this->getViolationInfo($row["ViolationLogID"]);
                //$violation=array();
                //$violation["ViolationLogID"]=$row["ViolationLogID"];
                //$violation["Driver"]=$row["Driver"];
                $tax=$tax+$row["Tax"];
                array_push($response["violations"],$violation);
            }
            $response["Tax"]=$tax;
        }
        
        $stmt->close();
        
        return $response;
    }
    
    function getViolationsByPlugedNumberForAdmin($plugedNumber){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("select vl.ViolationLogID as ViolationLogID,vhl.Driver as Driver,v.Tax as Tax from violations_log as vl inner join vehicles_log as vhl on vl.PlugedNumber=vhl.PlugedNumber inner join violations  as v on vl.ViolationID=v.ViolationID  where vl.PlugedNumber = ?");
        $stmt->bind_param("i",$plugedNumber);
        $stmt->execute();
        $res=$stmt->get_result();
        if($res->num_rows>0){
            $response["error"]=false;
            $response["violations"]=array();
            $tax=0;
            while ($row = $res->fetch_assoc()) {
                $violation= $this->getViolationInfo($row["ViolationLogID"]);
                //$violation["ViolationLogID"]=$row["ViolationLogID"];
                //$violation["Driver"]=$row["Driver"];
                $tax=$tax+$row["Tax"];
                array_push($response["violations"],$violation);
            }
            $response["Tax"]=$tax;
        }
        
        $stmt->close();
        
        return $response;
    }
    
    function getViolations(){
        $response=array();
        $response["error"]=true;
        $stmt= $this->conn->prepare("select vl.ViolationLogID as ViolationLogID,vhl.Driver as Driver,v.Tax as Tax from violations_log as vl inner join vehicles_log as vhl on vl.PlugedNumber=vhl.PlugedNumber inner join violations  as v on vl.ViolationID=v.ViolationID ");
        $stmt->execute();
        $res=$stmt->get_result();
        if($res->num_rows>0){
            $response["error"]=false;
            $response["violations"]=array();
            $tax=0;
            while ($row = $res->fetch_assoc()) {
                $violation= $this->getViolationInfo($row["ViolationLogID"]);
           
                $tax=$tax+$row["Tax"];
                array_push($response["violations"],$violation);
            }
            $response["Tax"]=$tax;
        }
        
        $stmt->close();
        
        return $response;
    }
//____________________________________________________
    
    
   
    
   
}
