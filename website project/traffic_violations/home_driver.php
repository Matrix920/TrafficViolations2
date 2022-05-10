<?php

session_start();

if(! isset($_SESSION['PlugedNumber'])){
    header("Location: index.php");
}

header("Content-Type: text/vnd.wap.wml");
?>
<wml>
     <header><title>Home</title></header>
    <card>
    <?php
        if(isset($_SESSION['Driver'])){
            echo 'Welcome <b>'.$_SESSION['Driver'].'</b><br/>';
        }
        ?>
    
    
    
<?php
if(isset($_SESSION['PlugedNumber'])){
    $plugedNumber=$_SESSION["PlugedNumber"];
    require_once 'include/ViolationsLog.php';
    $violationLog=new ViolationsLog();
    $result=$violationLog->getViolationsByPlugedNumber($plugedNumber);
    
        if(! $result["error"]){  
            echo "<table align='LL' columns='3'>";
                $violations=$result['violations'];
                $i=1;
                foreach ($violations as $violation) {
                    echo '<tr><td>(  '.$i.'  )</td><td><a href="violation_info_driver.php?vlid='.$violation['ViolationLogID'].'">'.$violation['Location'].'</a></td><td>'.$violation['Date'].'</td></tr>';
                    $i++;
                }
                echo '</table>';
        }else{
            echo 'no violations';
        }
}else{
   header("Location: index.php");
}
?>
    <br/>
    <do type="accept" label="logout">
        <go href="index.php?logout=1"/>
    </do>
    
    </card>
</wml>