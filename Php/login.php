<?php
    require_once("dbc.php");
    require_once("functions.php");
    session_start();
    $failed = false;
    if(isset($_POST["Login"])){
        $name = trim($_POST["name"]);
        $password = trim($_POST["password"]);
        $res = mysqli_query($conn,"select type, user_id, name from user where name='$name' and password='$password' and status='granted'") or die(mysqli_error($conn));
        if(mysqli_num_rows($res) > 0){
            $row = mysqli_fetch_assoc($res);
            $type = $row["type"];
            $user_id = $row["user_id"];
            $name = $row["name"];

            $_SESSION["id"] = $user_id;
            $_SESSION["type"] = $type;

            if($type === 'user'){
                redirect("user.php");
            } 
            else if($type === 'admin'){
                redirect("admin.php");
            } 
        }else{
            $failed = true;
        }
    }
?>

<!DOCTYPE html>
<html>


<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="assets/img/logo_r_resumme.png" type="image/x-icon" />
    <title>Log In</title>
    <script src="assets/js/particles.js"></script>
    <script src="assets/js/main.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.5.3/css/bulma.min.css">
    <link rel="stylesheet" href="assets/css/main.css">
</head>

<body>

    <?php 
        if($failed){
            show_msg("Error","Enter proper credentials");
        } 
    ?>


    <div class="columns is-vcentered">
        <div class="login column is-4 ">
            
            <section class="section">
                <div class="has-text-centered">
                    <img class="login-logo" src="assets/img/logo_r_resumme.png">
                </div>
                <form method="post">
                    <div class="field">
                        <label class="label">Username</label>
                        <div class="control has-icons-right">
                            <input class="input" name="name" type="text">
                            <span class="icon is-small is-right">
                                <i class="fa fa-user"></i>
                            </span>
                        </div>
                    </div>

                    <div class="field">
                        <label class="label">Password</label>
                        <div class="control has-icons-right">
                            <input class="input" name="password" type="password">
                            <span class="icon is-small is-right">
                                <i class="fa fa-key"></i>
                            </span>
                        </div>
                    </div>
                    <div class="has-text-centered">
                        <input type="submit" value="Login" name="Login" class="button is-vcentered is-primary is-outlined">
                    </div>
                </form>
                <div class="has-text-centered">
                    <a href="signup.php"> Don't you have an account? Sign up now!</a>
                </div>
            </section>
        </div>
        <div id="particles-js" class="interactive-bg column is-8">
        </div>
    </div>

</body>
</html>