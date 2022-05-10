<?php
error_reporting(-1);



ini_set('display_erors', 'On');

ini_set('url_rewriter.tags', '');

session_start();

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}


if(isset($_REQUEST['vlid'])){
   $violationLogID=$_REQUEST['vlid'];
   
   require_once 'include/ViolationsLog.php';
   $violationsLog=new ViolationsLog();
   $result=$violationsLog->getViolationInfo($violationLogID);
   
}else if(isset($_REQUEST['ViolationsLogID']) && isset($_REQUEST['PlugedNumber']) && isset($_REQUEST['Date']) && isset($_REQUEST['ViolationID']) && isset($_REQUEST['Location']) && isset($_REQUEST['IsPaid'])){
    
    require_once 'include/ViolationsLog.php';
    $violationsLog=new ViolationsLog();
    
    $result=$violationsLog->updateViolation($_REQUEST['ViolationsLogID'],$_REQUEST['ViolationID'], $_REQUEST['PlugedNumber'], $_REQUEST['Date'], $_REQUEST['Location'], $_REQUEST['IsPaid']);
    
        if(! $result["error"]){
           $added=true;
           header("Location: violation_info_admin.php?vlid=".$_REQUEST['ViolationsLogID']);
        }else{
            $added=false;
        }
}
else{
     header("Location: home.php");
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
<wml><header><title>Violation Information</title></header>
    <card>
    <onevent type="onenterforward">
        <refresh>
            <setvar name="ViolationType" value=""/>
            <setvar name="Tax"  value=""/>
        </refresh>
    </onevent>
    <p>
      <i>Violation</i> <b>(<?=$result["ViolationType"]?>)</b><br/>
      <select name="ViolationID">
          <?php
          foreach ($violations_types as $violation_type) {
                    echo '<option value="'.$violation_type['ViolationID'].'">'.$violation_type['ViolationType'].'</option>';
                }
                ?>
      </select>
    </p>
    
    Date<br/>
    <input type="text"  name="Date" value="<?=$result["Date"]?>" /><br/>
    
    Location<br/>
    <input type="text"  name="Location"  value="<?=$result["Location"]?>"/><br/>
    
    <p>
        <i>PlugedNumber</i> <b>(<?=$result["PlugedNumber"]?>)</b><br/>
      <select name="PlugedNumber">
          <?php
          foreach ($vehicles as $vehicle) {
                    echo '<option value="'.$vehicle['PlugedNumber'].'">'.$vehicle['PlugedNumber'].'</option>';
                }
                ?>
      </select>
    </p>
    
    <p>
        <i>Is Paid?</i> <b><?php if($result["IsPaid"]==1){echo 'Yes';}else{echo 'No';}?></b><br/>
      <select name="IsPaid">
        <option value="0">No</option>
        <option value="1">Yes</option>
      </select>
    </p>
    
    <anchor title="Update">Update
    <go href="<?=$_SERVER['PHP_SELF']?>" method="post">
        <postfield name="Date" value="$(Date)"/>
        <postfield name="PlugedNumber" value="$(PlugedNumber)"/>
        <postfield name="Location" value="$(Location)"/>
        <postfield name="IsPaid" value="$(IsPaid)"/>
        <postfield name="ViolationID" value="$(ViolationID)"/>
        <postfield name="ViolationsLogID" value="<?=$violationLogID?>"/>
    </go>
</anchor>

    <?php
        if(isset($added)){
    if($added){
        echo '<br/><b>registered successfully</b>';
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
    
    <do type="accept" label="Violations">
       <go href="violations_types.php"/>
    </do>
    </card>
</wml>