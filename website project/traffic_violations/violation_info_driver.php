<?php

session_start();

global $violationLogID;
global $result;

if(! isset($_SESSION['PlugedNumber'])){
    header("Location: index.php");
}

if(isset($_REQUEST['vlid']) &&!isset ($_REQUEST['pay'])){
   $violationLogID=$_REQUEST['vlid'];
   
   require_once 'include/ViolationsLog.php';
   $violationsLog=new ViolationsLog();
   $result=$violationsLog->getViolationInfo($violationLogID);
   
}else if(isset($_REQUEST['pay'])){
   $violationLogID=$_REQUEST['vlid'];
   
   require_once 'include/ViolationsLog.php';
   $violationsLog=new ViolationsLog();
   $result1=$violationsLog->pay($violationLogID);
   $result=$violationsLog->getViolationInfo($violationLogID);
}else{
     header("Location: index.php");
}


header("Content-Type: text/vnd.wap.wml");
?>
<wml><header><title>Violation Information</title></header>
    <card>
        <?php
        if(! $result['error']){
            echo '<table align="LL" columns="2">';
            echo '<tr><td><em>Date:</em></td> <td>'.$result['Date'].'</td></tr>';
            echo '<tr><td><em>Location:</em></td> <td>'.$result['Location'].'</td></tr>';
            echo '<tr><td><em>Tax:</em></td> <td>'.$result['Tax'].'</td></tr>';
            echo '<tr><td><em>Paid?</em></td> <td>';
                    if($result['IsPaid']==1)
                    {echo 'Yes'; 
                    }else{
                        echo 'No';
                    }
            echo '</td></tr>';
            echo '</table>';
        }
        ?>
    
    <p align="center">
        <anchor title="pay">Pay
        <go href="<?=$_SERVER['PHP_SELF']?>?pay=1" method="post">
            <postfield name="vlid" value="<?=$result['ViolationLogID']?>"/>
        </go>
        </anchor>
    </p>
    <do type="prev" label="back">
        <prev/>
    </do>
    <do type="accept" label="Home">
       <go href="home_driver.php"/>
    </do>
    </card>
</wml>