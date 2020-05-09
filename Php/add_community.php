<?php  
    session_start();
    require_once("dbc.php");
    if(isset($_POST["add_community"])){
        mysqli_query($conn,"insert into community(name,details,date,community_id) values ('{$_POST['name']}','{$_POST['details']}',curdate(),null)") or die(mysqli_error($conn));
        echo "community created successfully";
    }

?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Community</title>
</head>
<body>
    <a href='admin.php'>home</a><br>
    <form method="POST">
        <p><input type="text" name="name" placeholder="name"></p>
        <p><input type="text" name="details" placeholder="details"></p>
        <p><input type='submit' value="add community" name="add_community"></p>
    </form>
</body>
</html>