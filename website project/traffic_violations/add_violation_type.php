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


global $isAdded;

if(isset($_REQUEST['ViolationType']) && isset($_REQUEST['Tax'])){
    require_once 'include/Violations.php';
    $violaitons=new Violations();
    $result=$violaitons->addViolation($_REQUEST['ViolationType'], $_REQUEST['Tax']);
    
        if(! $result["error"]){
           $isAdded=true;
        }else{
            $isAdded=false;
        }
}

header("Content-Type: text/vnd.wap.wml");
?>
<wml>
    <header><title>New Violation Type</title></header>
    
    <card>
    
    <onevent type="onenterforward">
        <refresh>
            <setvar name="ViolationType" value=""/>
            <setvar name="Tax"  value=""/>
        </refresh>
    </onevent>
    
<p align="center">
    Violation Type<br/>
    <input type="text"  name="ViolationType" format="" /><br/>
    Tax<br/>
    <input type="text"  name="Tax" format="" /><br/>
    
    <anchor title="Register">Add
    <go href="<?=$_SERVER['PHP_SELF']?>" method="post">
        <postfield name="ViolationType" value="$(ViolationType)"/>
        <postfield name="Tax" value="$(Tax)"/>
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

<do type="accept" label="back"><prev/></do>
    
  
    <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>

<do type="accept" label="Violations">
       <go href="violations_types.php"/>
    </do>
</p>
    </card>
</wml>