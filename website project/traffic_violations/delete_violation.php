<?php
/*
error_reporting(-1);


ini_set('display_erors', 'On');

ini_set('url_rewriter.tags', '');
*/
session_start();

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}

if(isset($_REQUEST['vlid'])){
   $violationLogID=$_REQUEST['vlid'];
   
   require_once 'include/ViolationsLog.php';
   $violationsLog=new ViolationsLog();
   $result=$violationsLog->deleteViolation($violationLogID);
   
}else{
     header("Location: homex.php");
}



header("Content-Type: text/vnd.wap.wml");
?>
<wml>
    <header><title>Delete Violation </title></header>
    
    <card>
    
    <em>Deleted Successfully</em><br/>
    <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>
    
    <do type="accept" label="Violations Log">
       <go href="violations_log.php"/>
    </do>
    </card>
</wml>