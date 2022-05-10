<?php

session_start();

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}

header("Content-Type: text/vnd.wap.wml");

    require_once 'include/Violations.php';

    $violations=new Violations();
    $result=$violations->getVioloations();

?>
<wml>
     <header><title>Violations Types</title></header>
    <card>
   
    
    
    
<?php

    
        if(! $result["error"]){  
            echo "<table align='LL' columns='2'>";
                $violations=$result['violations'];
                $i=1;
                foreach ($violations as $violation) {
                    echo '<tr><td>-</td><td><a href="violation_type_info.php?vid='.$violation['ViolationID'].'">'.$violation['ViolationType'].'</a></td></tr>';
                    $i++;
                }
                echo '</table>';
        }else{
            echo 'no violations';
        }

?>
    <p align="center">
    <em><a href="add_violation_type.php">New Violation</a></em>
    </p>
    
    <do type="accept" label="back"><prev/></do>
    
    <do type="accept" label="logout">
        <go href="index.php?logout=1"/>
    </do>
    
    <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>
    
    </card>
</wml>