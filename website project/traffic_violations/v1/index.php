<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

error_reporting(-1);
ini_set('display_erors', 'On');

require '../libs/Slim/Slim.php';

require_once '../include/VehiclesLog.php';
require_once '../include/Violations.php';
require_once '../include/ViolationsLog.php';

\Slim\Slim::registerAutoloader();

$app=  new Slim\Slim();

//______________________________________

//Customer Operations

/*
 * Login
 */


$app->get('/checkconnection',function(){
    require_once '../include/Config.php';
    try {
        
        $conn=new mysqli(DB_HOST, 'DB_USERNAMEFF', DB_PASSWORD,DB_NAME);
        $response['msg']='success';
        echo_response(200,$response ) ;
    } catch (Exception $exc) {
        $response['msg']='error: '.$exc->getMessage();
        echo_response(500,$response ) ;
    }

    
});

/*
 * Login Driver
 */
$app->post('/login',function() use($app){
    
    $required_fields=array('Driver','PlugedNumber');
    verify_required_params($required_fields);
    
    $app= \Slim\Slim::getInstance();
    
    $driver=$app->request->post('Driver');
    $plugedNumber=$app->request->post('PlugedNumber');
    
    $response=array();
    $vehiclesLog=new VehiclesLog();
    $result=$vehiclesLog->login($driver, $plugedNumber);
        if(! $result['error']){       
                echo_response(200, $result);
        }else{
            $response['error']=true;
            
            echo_response(401, $result);
        }
});

/*
 * Sign up new driver
 */
$app->post('/register',function() use($app){
    
    $required_fields=array('PlugedNumber','Driver','Type','Category','ProductionDate','RegisterationDate','IsCrossOut');
    verify_required_params($required_fields);
    
    $app= \Slim\Slim::getInstance();
    
    $driver=$app->request->post('Driver');
    $plugedNumber=$app->request->post('PlugedNumber');
    $type=$app->request->post('Type');
    $category=$app->request->post('Category');
    $productionDate=$app->request->post('ProductionDate');
    $registerationDate=$app->request->post('RegisterationDate');
    $isCrossOut=$app->request->post('IsCrossOut');
    
    $vehiclesLog=new VehiclesLog();
    $result=$vehiclesLog->signup($plugedNumber,$driver,$type,$category,$productionDate,$registerationDate,$isCrossOut);
    if($result['error']){
        echo_response(401, $result);
    } else {
        echo_response(200, $result);
    }
    
});

/*
 * get all vehivles
 */
$app->get('/vehicles',function (){
    $vehicles_log=new VehiclesLog();
    
    $result=$vehicles_log->getVehicles();
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

/*
 * get vehivle info
 */
$app->get('/vehiclesinfo/:plugedNumber',function ($plugedNumber){
    $vehicles_log=new VehiclesLog();
    
    $result=$vehicles_log->getVehicleInfo($plugedNumber);
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

$app->get('/violationlog/pay/:violationLogID',function ($violationLogID){
    $violationsLog=new ViolationsLog();
    
    $result=$violationsLog->pay($violationLogID);
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

/*
 * update violation
 */
$app->post('/violations/update',function() use($app){
    $app= \Slim\Slim::getInstance();
    $violoations=new Violations();
    
    $required_fields=array('ViolationID','ViolationType','Tax');
    verify_required_params($required_fields);
    
    $violationID=$app->request->post('ViolationID');
    $violationType=$app->request->post('ViolationType');
    $tax=$app->request->post('Tax');
    
    $result=$violoations->updateViolation($violationID,$violationType,$tax);
    
    if($result["error"]){
        echo_response(400, $result);
    }else
    {
        echo_response(200, $result);
    }
}); 

/*
 * crossout vehicle
 */
$app->post('/vehicles/crossout',function() use($app){
    
    $required_fields=array('PlugedNumber');
    verify_required_params($required_fields);
    
    $app= \Slim\Slim::getInstance();
    
    $plugedNumber=$app->request->post('PlugedNumber');
    
    $vehiclesLog=new VehiclesLog();
    $result=$vehiclesLog->crossOutVehicle($plugedNumber);
    if($result['error']){
        echo_response(401, $result);
    } else {
        echo_response(200, $result);
    }
    
});


//__________________________________

//Product Operation

 
/*
 * Add Violation
 */
$app->post('/violations/add',function() use($app){
    $app= \Slim\Slim::getInstance();
    $violoations=new Violations();
    
    $required_fields=array('ViolationType','Tax');
    verify_required_params($required_fields);
    
    $violationType=$app->request->post('ViolationType');
    $tax=$app->request->post('Tax');
    
    $result=$violoations->addViolation($violationType,$tax);
    
    if($result["error"]){
        echo_response(400, $result);
    }else
    {
        echo_response(200, $result);
    }
}); 

/*
 * update violation
 */
$app->post('/violations/update',function() use($app){
    $app= \Slim\Slim::getInstance();
    $violoations=new Violations();
    
    $required_fields=array('ViolationID','ViolationType','Tax');
    verify_required_params($required_fields);
    
    $violationID=$app->request->post('ViolationID');
    $violationType=$app->request->post('ViolationType');
    $tax=$app->request->post('Tax');
    
    $result=$violoations->updateViolation($violationID,$violationType,$tax);
    
    if($result["error"]){
        echo_response(400, $result);
    }else
    {
        echo_response(200, $result);
    }
}); 

/*
 * get all violations types
 */
$app->get('/violations',function (){
    $violations=new Violations();
    
    $result=$violations->getVioloations();
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

/*
 * Get violation by id
 */
$app->get('/violations/:violationID',function ($violationID){
    $violations=new Violations();
    
    $result=$violations->getViolationInfo($violationID);
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

//__________________________________________

/*
 * Add Log Violation
 */
$app->post('/violationslog/add',function() use($app){
    $app= \Slim\Slim::getInstance();
    $violationLog=new ViolationsLog();
    
    $required_fields=array('PlugedNumber','ViolationID','Date','Location','IsPaid');
    verify_required_params($required_fields);
    
    $plugedNumber=$app->request->post('PlugedNumber');
    $violationID=$app->request->post('ViolationID');
    $date=$app->request->post('Date');
    $location=$app->request->post('Location');
    $isPaid=$app->request->post('IsPaid');
    
    $result=$violationLog->addViolation($violationID, $plugedNumber, $date, $location, $isPaid);
    
    if($result["error"]){
        echo_response(400, $result);
    }else
    {
        echo_response(200, $result);
    }
}); 

/*
 * get all violations log
 */
$app->get('/violationslog',function (){
    $violationsLog=new ViolationsLog();
    
    $result=$violationsLog->getViolations();
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

/*
 * Update violation Log
 */
$app->post('/violationslog/update',function() use($app){
    $app= \Slim\Slim::getInstance();
    $violationLog=new ViolationsLog();
    
    $required_fields=array('ViolationLogID','PlugedNumber','ViolationID','Date','Location','IsPaid');
    verify_required_params($required_fields);
    
    $plugedNumber=$app->request->post('PlugedNumber');
    $violationID=$app->request->post('ViolationID');
    $date=$app->request->post('Date');
    $location=$app->request->post('Location');
    $isPaid=$app->request->post('IsPaid');
    $violationLogID=$app->request->post('ViolationLogID');
    
    $result=$violationLog->updateViolation($violationLogID, $violationID, $plugedNumber, $date, $location, $isPaid);
    
    if($result["error"]){
        echo_response(400, $result);
    }else
    {
        echo_response(200, $result);
    }
}); 

/*
 * Delete Violation Log
 */
$app->post('/violationslog/delete',function() use($app){
    $app= \Slim\Slim::getInstance();
    $violationLog=new ViolationsLog();
    
    $required_fields=array('ViolationLogID');
    verify_required_params($required_fields);
    
    $violationLogID=$app->request->post('ViolationLogID');
    
    $result=$violationLog->deleteViolation($violationLogID);
    
    if($result["error"]){
        echo_response(400, $result);
    }else
    {
        echo_response(200, $result);
    }
}); 



/*
 * Get violations by driver
 */
$app->get('/violationslog/driver/:driver',function ($driver){
    $violationsLog=new ViolationsLog();
    
    $result=$violationsLog->getViolationsByDriver($driver);
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

/*
 * Get Violations by location
 */
$app->get('/violationslog/location/:location',function ($location){
    $violationsLog=new ViolationsLog();
    
    $result=$violationsLog->getViolationsByLocation($location);
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

/*
 * Get Violations by date
 */
$app->post('/violationslog/date/',function (){
    $app= \Slim\Slim::getInstance();
    $violationLog=new ViolationsLog();
    
    $required_fields=array('FromDate','ToDate');
    verify_required_params($required_fields);
    
    $fromDate=$app->request->post('FromDate');
	$toDate=$app->request->post('ToDate');
	
	$result=$violationLog->getViolationsByDate($fromDate,$toDate);
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

/*
 * Get violations by plugedNumber for driver
 */
$app->get('/violationslog/plugednumberdriver/:plugedNumber',function ($plugedNumber){
    $violationsLog=new ViolationsLog();
    
    $result=$violationsLog->getViolationsByPlugedNumber($plugedNumber);
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

/*
 * get violation Log info
 */
$app->get('/violationslog/violationinfo/:violationLogID',function ($violationLogID){
    $violationsLog=new ViolationsLog();
    
    $result=$violationsLog->getViolationInfo($violationLogID);
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});

/*
 * get viologions by plugedNumber for admin
 */
$app->get('/violationslog/plugednumber/:plugedNumber',function ($plugedNumber){
    $violationsLog=new ViolationsLog();
    
    $result=$violationsLog->getViolationsByPlugedNumberForAdmin($plugedNumber);
    
    if(! $result['error']){
        echo_response(200, $result);
    }else{
        echo_response(400, $result);
    }
});



//________________________________________

//additional operations

/*
 * Verify required parameters
 */

function verify_required_params($required_fields){
    $error=FALSE;
    $error_fields="";
    $request_param=array();
    $request_param=$_REQUEST;
    //Handling PUT request params
    if($_SERVER['REQUEST_METHOD']=='PUT'){
        $pp= \Slim\Slim::getInstance();
        parse_str($pp->request()->getBody(), $request_param);
    }
    foreach ($required_fields as $field) {
        if(!isset($request_param[$field]) || strlen(trim($request_param[$field])) <= 0){
            $error=TRUE;
            $error_fields .= $field . ',';
        }
    }
    
    if($error){
        //required filed(s) are missing or empty
        //echo error json and stop the app
        $response= array();
        $app= \Slim\Slim::getInstance();
        $response["error"]=true;
        $response['message']='Required field(s) '. $error_fields . ' is missing or empty';
        echo_response(400,$response);
        $app->stop();
    }
}



function echo_response($status,$response){
    $app= \Slim\Slim::getInstance();
    
    $app->contentType('application/json');
    
    $app->status($status);
    
    echo json_encode($response);
}


$app->run();

?>
