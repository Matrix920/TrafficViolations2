<?php

session_start();

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}

global $result;

if(isset($_REQUEST['searchBy'])){
    $searchBy=$_REQUEST['searchBy'];
if(isset($_REQUEST['search']) && $searchBy==3){
    
    require_once  'include/ViolationsLog.php';
    $violationsLog=new ViolationsLog();
        
    $plugedNumber=$_REQUEST['search'];
    $result=$violationsLog->getViolationsByPlugedNumberForAdmin($plugedNumber);
    
}else if(isset($_REQUEST['fromDate']) && isset($_REQUEST['toDate'])  && $searchBy==2){
    
    require_once  'include/ViolationsLog.php';
    $violationsLog=new ViolationsLog();
    
    $fromDate=$_REQUEST['fromDate'];
    $toDate=$_REQUEST['toDate'];
            
    $result=$violationsLog->getViolationsByDate($fromDate, $toDate);
    
}else if(isset($_REQUEST['search']) && $searchBy==0){
    
    require_once  'include/ViolationsLog.php';
    $violationsLog=new ViolationsLog();
    
    $driver=$_REQUEST['search'];
    
    $result=$violationsLog->getViolationsByDriver($driver);
    
}else if(isset($_REQUEST['search']) && $searchBy==1){
    
    require_once  'include/ViolationsLog.php';
    $violationsLog=new ViolationsLog();
    
    $location=$_REQUEST['search'];
    
    $result=$violationsLog->getViolationsByLocation($location);
    
}}else{
    
    require_once  'include/ViolationsLog.php';
    $violationsLog=new ViolationsLog();
    
    $result=$violationsLog->getViolations();
}

header("Content-Type: text/vnd.wap.wml");
?>
<wml>
     <header><title>Home</title></header>
    <card>
    <a href="search_violations_log.php">Search</a><br/>
    
<?php
    
        if(! $result["error"]){  
            echo "<table align='LL' columns='2'>";
                $violations=$result['violations'];
                $i=1;
                foreach ($violations as $violation) {
                    echo '<tr><td>(  '.$i.'  )</td><td><a href="violation_info_admin.php?vlid='.$violation['ViolationLogID'].'">'.$violation['Driver'].'</a></td></tr>';
                    $i++;
                }
                echo '</table>';
        }else{
            echo 'no violations';
        }
        
        echo '<br/>';
        
        if(isset($_REQUEST['searchBy'])){
            echo 'Total tax is '.$result['Tax'];
        }
?>
    <br/>
    <p align="center">
    <em><a href="add_violation.php">New Violation</a></em>
    </p>
    
    <do type="accept" label="back"><prev/></do>
    
    <do type="accept" label="logout">
        <go href="index.php?logout=1"/>
    </do>
    
     <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>
    
    </card>
</wml>