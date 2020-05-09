<?php
   require_once("dbc.php");
   $name = mysqli_real_escape_string($conn, $_GET['name']);
   $number = mysqli_real_escape_string($conn, $_GET['no']);
   #$res = mysqli_query($conn, "INSERT INTO unregistered(name,number) VALUES('$name','$number')");
   $Query = "INSERT INTO unregistered(name, mobile) VALUES('$name','$number')";
   $res = mysqli_query($conn, $Query);
   unset($_GET['name']);
   unset($_GET['no']);
?>