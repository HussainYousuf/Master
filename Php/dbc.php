<?php
$host = "localhost";
$user = "id9766754_user1";
$password = "password1";
$database = "id9766754_database1";

$conn = mysqli_connect($host,$user,$password) or die("could not connect");

$db_selected = mysqli_select_db($conn,$database);

if (!$db_selected) {
    mysqli_query($conn,"create database if not exists $database") or die("database creation failed");
    mysqli_select_db($conn,$database);
    $sql = file_get_contents("$database".".sql");
    mysqli_multi_query($conn,$sql) or die("query failed");
}


?>