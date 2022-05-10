<?php

session_start();

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}

global $result;

    
    require_once  'include/VehiclesLog.php';
    $violationsLog=new VehiclesLog();
    
    $result=$violationsLog->getVehicles();


header("Content-Type: text/vnd.wap.wml");
?>
<wml>
     <header><title>Home</title></header>
    <card>
    
<?php
    
        if(! $result["error"]){  
            echo "<table align='LL' columns='2'>";
                $vehicles=$result['vehicles'];
                $i=1;
                foreach ($vehicles as $vehicle) {
                    echo '<tr><td>'.$vehicle['PlugedNumber'].'</td><td><a href="vehicle_info.php?pnid='.$vehicle['PlugedNumber'].'">-'.$vehicle['Driver'].'</a></td></tr>';
                    $i++;
                }
                echo '</table>';
        }else{
            echo 'no violations';
        }

?>
    <p align="center">
    </p>
    
    <do type="accept" label="back"><prev/></do>
    
  
    
     <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>
    
    </card>
</wml>