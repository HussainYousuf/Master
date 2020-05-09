<?php
    require_once("dbc.php");
    require_once("functions.php");
    session_start();
?>

<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
        integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">

    <title>Hello, world!</title>
</head>

<body>



    <nav class="navbar navbar-expand-sm sticky-top navbar-light bg-light mb-3">
        <div class="container">
            <a href="user.php" class="mr-4"><i class="fas fa-arrow-left fa-2x text-dark"></i></a>
            <a class="navbar-brand">User Panel</a>
            <button class="navbar-toggler" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ml-auto align-items-center">
                    <li class="nav-item m-2">
                        <div><i class="fa fa-user m-2"></i>User</div>
                    </li>
                    <li class="nav-item m-2">
                        <a href="logout.php" class="btn btn-dark">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>


    <div class="container">

        <div class="list-group">

            <?php 

        $res = mysqli_query($conn,"select community_id from user_community where user_id = {$_SESSION['id']}");
        if(mysqli_num_rows($res) > 0){
            while($row=mysqli_fetch_assoc($res)){
                $community_id = $row["community_id"];
                $res2 = mysqli_query($conn,"select title,content,user_id,date,post_id,options from post where community_id = $community_id");
                $community_name = mysqli_fetch_assoc(mysqli_query($conn,"select name from community where community_id = $community_id"))['name'];
                if(mysqli_num_rows($res2) > 0){
                    while($row2=mysqli_fetch_assoc($res2)){

                        $author = mysqli_fetch_assoc(mysqli_query($conn,"select name from user where user_id = {$row2['user_id']}"))["name"];
                        $title = $row2["title"];
                        $date = $row2["date"];
                        $content = $row2["content"];
                        $post_id = $row2["post_id"];
                        $options = $row2["options"];

                        $res3 = mysqli_query($conn,"select * from options where post_id = $post_id and user_id = {$_SESSION['id']}") or die("went wrong");
                        if(!empty($options) && mysqli_num_rows($res3) < 1){ 
                            $msg="<form method='post' action='vote.php?post_id=$post_id'>";
                            $msg .="<input type='submit' class= 'btn btn-primary' value='please vote' name='submit'></form>"; 
                        }else $msg="" ; 

                        echo <<<END
                        <br>
                        <a href="add_comment.php?id=$post_id" class="list-group-item list-group-item-action flex-column align-items-start">
                            <div class="d-flex w-100 justify-content-between">
                                <h4>$title</h4>
                                <small class="text-muted">$msg</small>
                                <small class="text-muted">$date</small>
                            </div>
                            <hr>
                            <p class="mb-1">$content</p>
                            <hr>
                            <div class="d-flex w-100 justify-content-between">
                                <small class="text-muted">$author</small>
                                <small class="text-muted">$community_name</small>
                            </div>
                        </a>
                        
END;
                    }
                }
            }
        }

        ?>

        </div>

    </div>



    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous">
    </script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous">
    </script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous">
    </script>
</body>

</html>