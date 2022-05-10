<?php

session_start();

global $violationLogID;
global $result;

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}

if(isset($_REQUEST['vlid'])){
   $violationLogID=$_REQUEST['vlid'];
   
   require_once 'include/ViolationsLog.php';
   $violationsLog=new ViolationsLog();
   $result=$violationsLog->getViolationInfo($violationLogID);
   
}else{
     header("Location: home.php");
}


header("Content-Type: text/vnd.wap.wml");
?>
<wml><header><title>Violation Information</title></header>
    <card>
    <p align="center">
        <?php
        if(! $result['error']){
            
            echo '<table align="LL" columns="2">';
            echo '<tr><td><em>Pluged Number:</em></td> <td>'.$result['PlugedNumber'].'</td></tr>';
            echo '<tr><td><em>Driver:</em></td> <td>'.$result['Driver'].'</td></tr>';
            echo '<tr><td><em>Violation Type:</em></td> <td>'.$result['ViolationType'].'</td></tr>';
            echo '<tr><td><em>Date:</em></td> <td>'.$result['Date'].'</td></tr>';
            echo '<tr><td><em>Location:</em></td> <td>'.$result['Location'].'</td></tr>';
            echo '<tr><td><em>Tax:</em></td> <td>'.$result['Tax'].'</td></tr>';      
            echo '</table>';
            echo '<p align="center">';
            if($result['IsPaid']==1){
                echo '<i>Paid</i>';
            }else{
                echo '<i>Not Paid</i>';
            }
            echo '</p>';
            
            
           
        }
        ?>
    </p>
    <p align="center">
        <a href="delete_violation.php?vlid=<?=$violationLogID?>">Delete Violation</a>
        <a href="update_violation.php?vlid=<?=$violationLogID?>">Update Violation</a>
    </p>
    
    <do type="prev" label="back">
        <prev/>
    </do>
    <do type="accept" label="home">
       <go href="home_admin.php"/>
    </do>
    
    <do type="accept" label="Violations Log">
       <go href="violations_log.php"/>
    </do>
    </card>
</wml>