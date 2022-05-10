<?php

session_start();

if(isset($_REQUEST['Driver']) && isset($_REQUEST['PlugedNumber'])){
    
    //check for driver login
    require_once 'include/VehiclesLog.php';
    $vehiclesLog=new VehiclesLog();
    $result=$vehiclesLog->login($_REQUEST['Driver'],$_REQUEST['PlugedNumber']);
    
        if(! $result["error"]){    
            if(! $result["isAdmin"]){
                $_SESSION['PlugedNumber']=$_REQUEST['PlugedNumber'];
                $_SESSION['Driver']=$result['Driver'];
                header("Location: home_driver.php");
                exit();
            }
            else{
                $_SESSION["admin"]="admin";
                header("Location: home_admin.php");
                exit();
            }
        }else{
            header("Location: ".$_SERVER['PHP_SELF']."?error=error");
        }
}

if(isset($_REQUEST['logout'])){
    session_unset();
}

header("Content-Type: text/vnd.wap.wml");
?>
<wml>
    <header><title>Login</title></header>
    <card>
    <!--
    <onevent type="onenterforward">
        <refresh>
            <setvar name="login" value=""/>
            <setvar name="password" value=""/>
        </refresh>
    </onevent>
    -->
<p align="center">
    Driver<br/>
    <input type="text" format="M*M" name="Driver"/>
    <br/>
    Pluged Number
    <br/>
    <input type="password" format="N*N"  name="PlugedNumber"/>
    <br/>
    
<anchor title="Login">Login
    <go href="<?=$_SERVER['PHP_SELF']?>" method="post">
        <postfield name="Driver" value="$(Driver)"/>
        <postfield name="PlugedNumber" value="$(PlugedNumber)"/>
    </go>
    
</anchor>
<?php
if(isset($_REQUEST['error'])){
    echo '<br/><b>login failed</b>';
}
?>
<br/>
<a href="register.php">Register</a>


</p>
    </card>
</wml>