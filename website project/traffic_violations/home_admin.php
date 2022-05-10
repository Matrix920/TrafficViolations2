<?php

session_start();

if(! isset($_SESSION['admin'])){
    header("Location: index.php");
}

header("Content-Type: text/vnd.wap.wml");
?>
<wml>
     <header><title>Home</title></header>
<card>
<p align="center">
<em><a href="violations_types.php">Violations Types</a></em><br/>
<em><a href="violations_log.php">Violations Log</a></em><br/>
<em><a href="vehicles_log.php">Vehicles Log</a></em><br/>

    <do type="accept" label="back"><prev/></do>
   
    <do type="accept" label="logout">
        <go href="index.php?logout=1"/>
    </do>
</p>
    </card>
</wml>