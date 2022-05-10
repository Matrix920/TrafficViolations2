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

if(isset($_REQUEST['vid'])){
   $violationID=$_REQUEST['vid'];
   
   require_once 'include/Violations.php';
   $violations=new Violations();
   $result=$violations->delete_violation($violationID);
   
}else{
     header("Location: homex.php");
}



header("Content-Type: text/vnd.wap.wml");
?>
<wml>
    <header><title>Delete Violation Type</title></header>
    
    <card>
    
    <em>Deleted Successfully</em><br/>
    <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>
    
    <do type="accept" label="Violations">
       <go href="violations_types.php"/>
    </do>
    </card>
</wml>