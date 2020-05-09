
<!-- <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>

</head>
<body>
    <a href="index.html">go back</a>    
    <form method="POST">
        <p><input type="text" name="name" placeholder="name" ></p>
        <p><input type="text" name="email" placeholder="email" ></p> 
        <p><input type="text" name="mobile" placeholder="mobile" ></p>
        <p><input type="password" name="password" placeholder="password" ></p>
        <p><input type="radio" name="type" value="admin" > Admin </p>
        <p><input type="radio" name="type" value="user" checked> User</p>
        <p><input type="submit" name="signup" value="Sign Up"></p>
    </form>
</body>
</html> -->




<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=0">
    <link rel="icon" href="assets/img/logo_r_resumme.png" type="image/x-icon" />
    <title>Sign Up</title>
    <script src="assets/js/particles.js"></script>
    <script src="assets/js/main.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.5.3/css/bulma.min.css">
    <link rel="stylesheet" href="assets/css/main.css">

</head>

<body>


<?php
    require_once("dbc.php");
    require_once("functions.php");
    if(isset($_POST["signup"])){
        $name = trim($_POST["name"]);
        $email = trim($_POST["email"]);
        $mobile = trim($_POST["mobile"]);
        $password = trim($_POST["password"]);
        $type = $_POST["type"];
        if(!empty($name) && !empty($email) && !empty($mobile) && !empty($password)){
            mysqli_query($conn,"insert into user(name,email,mobile,password,type,status,date,user_id) values ('$name','$email','$mobile','$password','$type','pending',null,null)") or die(mysqli_error($conn));
            show_msg("info","user created wait for admin to authenticate");
        }else{
            show_msg("error","enter proper credentails");
        }
    }

?>

    <div class="columns is-vcentered">
        <div class="login column is-4 ">
            <section class="section">
                
            <form method='post'>
                <div class="field">
                    <label class="label">Name</label>
                    <div class="control has-icons-right">
                        <input class="input" name="name" type="text">
                        <span class="icon is-small is-right">
                            <i class="fa fa-user"></i>
                        </span>
                    </div>
                </div>

                <div class="field">
                    <label class="label">Email</label>
                    <div class="control has-icons-right">
                        <input class="input" name="email" type="text">
                        <span class="icon is-small is-right">
                            <i class="fa fa-envelope"></i>
                        </span>
                    </div>
                </div>

                <div class="field">
                    <label class="label">Mobile</label>
                    <div class="control has-icons-right">
                        <input class="input" name="mobile" type="text">
                        <span class="icon is-small is-right">
                            <i class="fa fa-mobile"></i>
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

                <div class="control">
                    <label class="label">Type</label>
                    <label class="radio">
                        <input type="radio" name="type" value="user" checked>
                        User
                    </label>
                    <label class="radio">
                        <input type="radio" value="admin" name="type">
                        Admin
                    </label>
                </div>





                <div class="has-text-centered">
                    <input type="submit" value="Sign Up" name="signup" class="button is-vcentered is-primary is-outlined">
                </div>

            </form>
                <div class="has-text-centered">
                    <a href="login.php"> Already have an account? Log in now !</a>
                </div>
            </section>
        </div>
        <div id="particles-js" class="interactive-bg column is-8">

        </div>
    </div>

</body>

</html>