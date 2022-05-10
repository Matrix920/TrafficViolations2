<?php
error_reporting(-1);



ini_set('display_erors', 'On');

ini_set('url_rewriter.tags', '');


session_start();

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}




if(isset($_REQUEST['PlugedNumber']) && isset($_REQUEST['Date']) && isset($_REQUEST['ViolationID']) && isset($_REQUEST['Location']) && isset($_REQUEST['IsPaid'])){
    
    require_once 'include/ViolationsLog.php';
    
    $violationsLog=new ViolationsLog();
    $result=$violationsLog->addViolation($_REQUEST['ViolationID'], $_REQUEST['PlugedNumber'], $_REQUEST['Date'], $_REQUEST['Location'], $_REQUEST['IsPaid']);
        if(! $result["error"]){
           $added=true;
        }else{
            $added=false;
        }
}

require_once 'include/Violations.php';
require_once 'include/VehiclesLog.php';
require_once 'include/ViolationsLog.php';

$vehiclesLog=new VehiclesLog();
$violations=new Violations();
$violationsLog=new ViolationsLog();


$vehicles_result=$vehiclesLog->getVehicles();
$vehicles=$vehicles_result["vehicles"];
$violations_result=$violations->getVioloations();
$violations_types=$violations_result["violations"];


header("Content-Type: text/vnd.wap.wml");
?>
<wml>
    <header><title>New Violation</title></header>
    
    <card>
    
    <p>
      <i>Violation</i><br/>
      <select name="ViolationID">
          <?php
          foreach ($violations_types as $violation_type) {
                    echo '<option value="'.$violation_type['ViolationID'].'">'.$violation_type['ViolationType'].'</option>';
                }
                ?>
      </select>
    </p>
    
    Date format(yyyy-mm-dd)<br/>
    <input type="text"  name="Date" /><br/>
    
    Location<br/>
    <input type="text"  name="Location" /><br/>
    
    <p>
      <i>Driver</i><br/>
      <select name="PlugedNumber">
          <?php
          foreach ($vehicles as $vehicle) {
                    echo '<option value="'.$vehicle['PlugedNumber'].'">'.$vehicle['PlugedNumber'].'</option>';
                }
                ?>
      </select>
    </p>
    
    <p>
        <i>Is Paid?</i><br/>
      <select name="IsPaid">
        <option value="0">No</option>
        <option value="1">Yes</option>
      </select>
    </p>
    
    <p align="center">
    <anchor title="Add">Add
    <go href="<?=$_SERVER['PHP_SELF']?>" method="post">
        <postfield name="Date" value="$(Date)"/>
        <postfield name="Location" value="$(Location)"/>
        <postfield name="PlugedNumber" value="$(PlugedNumber)"/>
        <postfield name="IsPaid" value="$(IsPaid)"/>
        <postfield name="ViolationID" value="$(ViolationID)"/>
    </go>
</anchor>

    <?php
        if(isset($added)){
    if($added){
    echo '<br/><b>added successfully</b>';
    }else{
        echo '<br/><b>error data</b>';
    }
}
        ?>
    <br/>
      <do type="accept" label="back"><prev/></do>
   
    
    <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>
      
      <do type="accept" label="Violations">
       <go href="violations_log.php"/>
    </do>
</p>

    </card>
</wml>