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
    <onevent type="onenterforward">
        <refresh>
            <setvar name="fromDate" value="1800-01-01"/>
            <setvar name="toDate" value="2222-01-01"/>
            <setvar name="Location" value="empty"/>
        </refresh>
    </onevent>
         <p>
        <i>Search By</i>
      <select name="searchBy">
        <option value="0">Driver</option>
        <option value="1">Location</option>
        <option value="2">Date</option>
        <option value="3">Pluged Number</option>
      </select>
        
        <input type="text" name="search"/><br/>
         <p>
        <em>Search By Date  </em>(yyyy-mm-dd)
         <table>
             <tr><td>From Date</td><td><input type="text" format="NNNN-NN-NN" name="fromDate"/></td></tr>
             <tr><td>To Date</td><td><input type="text" format="NNNN-NN-NN"  name="toDate"/></td></tr>
         </table>
         </p>
    </p>
    
    <anchor title="Search">Search
            <go href="violations_log" type="post">
                <postfield name="searchBy" value="$(searchBy)"/>
                <postfield name="search" value="$(search)"/>
                <postfield name="fromDate" value="$(fromDate)"/>
                <postfield name="toDate" value="$(toDate)"/>
            </go>
        </anchor><br/>

    
    <do type="accept" label="back"><prev/></do>
    
    <do type="accept" label="logout">
        <go href="index.php?logout=1"/>
    </do>
    
     <do type="accept" label="Home">
       <go href="home_admin.php"/>
    </do>
    
    </card>
</wml>