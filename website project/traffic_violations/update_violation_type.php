<?php
error_reporting(-1);


ini_set('display_erors', 'On');

ini_set('url_rewriter.tags', '');

session_start();

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}

global $isAdded;

if(isset($_REQUEST['vid'])){
   $violationID=$_REQUEST['vid'];
   
   require_once 'include/Violations.php';
   $violations=new Violations();
   $violationInfo=$violations->getViolationInfo($violationID);
   
}else if(isset($_REQUEST['ViolationID']) && isset($_REQUEST['ViolationType']) && isset($_REQUEST['Tax'])){
    
    require_once 'include/Violations.php';
   $violations=new Violations();
   
    $result=$violations->updateViolation($_REQUEST['ViolationID'], $_REQUEST['ViolationType'], $_REQUEST['Tax']);
    
        if(! $result["error"]){
           $isAdded=true;
           header("Location: violation_type_info.php?vid=".$_REQUEST['ViolationID']);
        }else{
            $isAdded=false;
        }
}
else{
     header("Location: home.php");
}

if(isset($_REQUEST['ViolationID']) && isset($_REQUEST['ViolationType']) && isset($_REQUEST['Tax'])){
    
    $result=$violations->updateViolation($_REQUEST['ViolationID'], $_REQUEST['ViolationType'], $_REQUEST['Tax']);
    
        if(! $result["error"]){
           $isAdded=true;
        }else{
            $isAdded=false;
        }
}

header("Content-Type: text/vnd.wap.wml");
?>
<wml>
    <header><title>Register</title></header>
    
    <card>
<p align="center">
    Violation Type<br/>
    <input type="text"  name="ViolationType" value="<?=$violationInfo['ViolationType']?>"/><br/>
    Tax<br/>
    <input type="text"  name="Tax" value="<?=$violationInfo['Tax']?>"/><br/>
    
    <anchor title="update">Update
    <go href="<?=$_SERVER['PHP_SELF']?>" method="post">
        <postfield name="ViolationType" value="$(ViolationType)"/>
        <postfield name="Tax" value="$(Tax)"/>
        <postfield name="ViolationID" value="<?=$violationID?>"/>
    </go>
</anchor>

    <?php
if(isset($isAdded)){
    if($isAdded){
    echo '<br/><b>Added successfully</b>';
    }
}
?>
<br/>
<do type="prev" label="back">
        <prev/>
    </do>
    <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>
    
    <do type="accept" label="Violations Log">
       <go href="violations_log.php"/>
    </do>



</p>
    </card>
</wml>