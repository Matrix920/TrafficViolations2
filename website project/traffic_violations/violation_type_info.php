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
   $result=$violations->getViolationInfo($violationID);
   
}else{
     header("Location: homex.php");
}



header("Content-Type: text/vnd.wap.wml");
?>
<wml>
    <header><title>Violation Information</title></header>
    
    <card>
    
    <?php
        if(! $result['error']){
            echo '<table align="LL" columns="2">';
            echo '<tr><td><em>Type:</em></td> <td>'.$result['ViolationType'].'</td></tr>';
            echo '<tr><td><em>Tax:</em></td> <td>'.$result['Tax'].'</td></tr></table>';
        }
        ?>
    
    <p align="center">
        <a href="delete_violation_type.php?vid=<?=$violationID?>">Delete Violation</a><br/>
        
        <a href="update_violation_type.php?vid=<?=$violationID?>">Update Violation</a>
    </p>

    <?php
    if(isset($isAdded)){
        if($isAdded){
            echo '<br/><b>Added successfully</b>';
        }
    }
    ?>
    <do type="prev" label="back">
        <prev/>
    </do>
    <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>
    
    <do type="accept" label="Violations">
       <go href="violations_types.php"/>
    </do>
    </card>
</wml>