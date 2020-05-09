<?php 

    require_once("dbc.php");
    require_once("functions.php");
    session_start();
    $post_id = $_GET["post_id"];
    if(isset($_POST["vote"])){
        $option = $_POST["option"];
        mysqli_query($conn,"insert into options values ($post_id,{$_SESSION['id']},'$option')") or die(mysqli_error($conn));
        redirect("view_post.php");
    }




?>


<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>Document</title>
</head>

<body>
    <div class="container">
    <?php 
    $options = mysqli_fetch_assoc(mysqli_query($conn,"select options from post where post_id = $post_id"))["options"];
    $options = explode(",",$options);
    echo <<<END
    <form method='post'>
    <select name='option'>
END;
    foreach($options as $option){
        echo "<option>" . trim($option) . "</option>";
    }
    echo "</select>";
    echo "<input type='submit' name='vote' value='vote'></form>";

?>
    </div>
</body>

</html>