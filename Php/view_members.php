<?php 
    require_once("dbc.php");
    session_start();
    $msg = <<<END
    <table>
        <tr>
            <th>name</th>
            <th>email</th>
            <th>mobile</th>
        </tr>
END;

    $res = mysqli_query($conn,"select user_id from user_community where user_id != {$_SESSION['id']} and community_id in (select community_id from user_community where user_id = {$_SESSION['id']})") or die(mysqli_error($conn));
    if(mysqli_num_rows($res) > 0){
        while($row=mysqli_fetch_assoc($res)){

            $res2 = mysqli_query($conn,"select name,email,mobile from user where user_id = {$row['user_id']}") or die(mysqli_error($conn));
            $row2 = mysqli_fetch_assoc($res2);
            $name = $row2["name"]; 
            $email = $row2["email"];
            $mobile = $row2["mobile"];

            $msg .= <<<END
            <tr>
                <td>$name</td>
                <td>$email</td>
                <td>$mobile</td>
            </tr>
END;
        }
    }

    $msg .= "</table>"

?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
    <a href='user.php'>home</a><br>
    <?php echo $msg; ?>
</body>
</html>