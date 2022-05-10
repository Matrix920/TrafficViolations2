<?php

session_start();

global $violationLogID;
global $result;

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}

if(isset($_REQUEST['pnid']) && ! isset($_REQUEST['crossout'])){
   $plugedNumber=$_REQUEST['pnid'];
   
   require_once 'include/VehiclesLog.php';
   $vehiclesLog=new VehiclesLog();
   $result=$vehiclesLog->getVehicleInfo($plugedNumber);
   
}else if(isset ($_REQUEST['crossout'])){
   $plugedNumber=$_REQUEST['pnid'];
   
   require_once 'include/VehiclesLog.php';
   $vehiclesLog=new VehiclesLog();
   $result1=$vehiclesLog->crossOutVehicle($plugedNumber);
   $result=$vehiclesLog->getVehicleInfo($plugedNumber);
}
else{
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
            echo '<tr><td><em>Production Date:</em></td> <td>'.$result['ProductionDate'].'</td></tr>';
            echo '<tr><td><em>Registeration Date:</em></td> <td>'.$result['RegisterationDate'].'</td></tr>';
            echo '<tr><td><em>Type:</em></td> <td>'.$result['Type'].'</td></tr>';
            echo '<tr><td><em>Category:</em></td> <td>'.$result['Category'].'</td></tr>';  
            if($result['IsCrossOut']==0){
                echo '<tr><td><em>Is cross Out ? </em></td> <td>No</td></tr>';  
            }else{
                echo '<tr><td><em>Is cross Out ? </em></td> <td>Yes</td></tr>';  
            }
            
            echo '</table>';
        }
        ?>
    </p>
    <p align="center">
        <anchor title="Register">Cross out
        <go href="<?=$_SERVER['PHP_SELF']?>?crossout=1" method="post">
            <postfield name="pnid" value="<?=$result['PlugedNumber']?>"/>
        </go>
        </anchor>
    </p>
    <br/>
    <?php
    if(isset($_REQUEST['crossout'])){
        echo 'car has been crossed out successfully';
    }
    ?>
    <br/>
    <do type="prev" label="back">
        <prev/>
    </do>
   
  
    </card>
</wml>