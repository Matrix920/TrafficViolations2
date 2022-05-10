<?php
error_reporting(-1);


ini_set('display_erors', 'On');

ini_set('url_rewriter.tags', '');

session_start();

global $isregistered;

if(isset($_REQUEST['PlugedNumber']) && isset($_REQUEST['Driver']) && isset($_REQUEST['Type']) && isset($_REQUEST['Category']) && isset($_REQUEST['ProductionDate']) && isset($_REQUEST['RegisterationDate']) && isset($_REQUEST['IsCrossOut'])){
    require_once 'include/VehiclesLog.php';
    $vehiclesLog=new VehiclesLog();
    $result=$vehiclesLog->signup($_REQUEST['PlugedNumber'], $_REQUEST['Driver'], $_REQUEST['Type'], $_REQUEST['Category'], $_REQUEST['ProductionDate'], $_REQUEST['RegisterationDate'], $_REQUEST['IsCrossOut']);
    
        if(! $result["error"]){
           $isregistered=true;
        }else{
            $isregistered=false;
        }
}

header("Content-Type: text/vnd.wap.wml");
?>
<wml>
    <header><title>Register</title></header>
    
    <card>
<p align="center">
    Pluged Number<br/>
    <input type="text"  name="PlugedNumber" /><br/>
    Driver<br/>
    <input type="text" name="Driver"/><br/>
    Type<br/>
    <input type="text"  name="Type"/><br/>
    Category<br/>
    <input type="text"  name="Category"/><br/>
    Production Date<br/>
    <input type="text" name="ProductionDate"/><br/>
    Registeration Date<br/>
    <input type="text"  name="RegisterationDate"/><br/>
    <p>
        <i>Is Cross Out?</i><br/>
      <select name="IsCrossOut">
        <option value="0">No</option>
        <option value="1">Yes</option>
      </select>
    </p>
    
    <anchor title="Register">Register
    <go href="<?=$_SERVER['PHP_SELF']?>" method="post">
        <postfield name="PlugedNumber" value="$(PlugedNumber)"/>
        <postfield name="Driver" value="$(Driver)"/>
        <postfield name="Type" value="$(Type)"/>
        <postfield name="Category" value="$(Category)"/>
        <postfield name="ProductionDate" value="$(ProductionDate)"/>
        <postfield name="RegisterationDate" value="$(RegisterationDate)"/>
        <postfield name="IsCrossOut" value="$(IsCrossOut)"/>
    </go>
</anchor>

    <?php
if(isset($isregistered)){
    if($isregistered){
    echo '<br/><b>registered successfully</b>';
    }else{
        echo '<br/><b>user login already exist</b>';
    }
}
?>
<br/>
<a href="index.php">Login</a>



</p>
    </card>
</wml>