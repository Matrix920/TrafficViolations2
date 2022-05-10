<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Db_connect
 *
 * @author Matrix
 */
class Db_connect {
    //put your code here
    private $conn;
    public function __construct() {
        
    }
    
    public function connect(){
        require_once 'Config.php';
        $this->conn=new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
        
        if(mysqli_connect_errno()){
            echo 'error to connect to database' . mysqli_connect_error();
        }
        return $this->conn;
    }
}
