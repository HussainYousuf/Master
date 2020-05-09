<?php 
require_once("dbc.php");
require_once("functions.php");
session_start();
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css"
        integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
        integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous">
    <link rel="stylesheet" href="css/style.css">

    <title>Admin Panel</title>
</head>

<body>

    <nav class="navbar navbar-expand-sm sticky-top navbar-light bg-light mb-3">
        <div class="container">
            <a class="navbar-brand">Admin Panel</a>
            <button class="navbar-toggler" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ml-auto align-items-center">
                    <li class="nav-item m-2">
                        <div><i class="fa fa-user m-2"></i>Admin</div>
                    </li>
                    <li class="nav-item m-2">
                        <a href="logout.php" class="btn btn-dark">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <section id="home-admin" class="py-5">
        <div class="container">
            <div class="row">
                <div class="col-sm-4 my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="fas fa-user-friends fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="admin_view_users.php">view all users</a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="far fa-clipboard fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="" data-toggle="modal" data-target="#vision2">Add Post</a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="far fa-handshake fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="sub.php">view_community</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">

                <div class="col-sm-4 my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="far fa-calendar-check fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="delete_post.php">view/delete post</a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="fas fa-check fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="grant_access.php">grant access</a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="far fa-laugh-beam fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="view_report.php">view report</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="fas fa-book fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="" data-toggle="modal" data-target="#vision">Add community</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>



    <?php  
        if(isset($_POST["add_community"])){
            mysqli_query($conn,"insert into community(name,details,date,community_id) values ('{$_POST['name']}','{$_POST['details']}',curdate(),null)") or die(mysqli_error($conn));
            toast("Community created successfully","success");
        }
    ?>
    <div class="modal" id="vision">
        <div class="modal-dialog">
            <div class="modal-content">

                <div class="modal-header">
                    <h4 class="modal-title">Add Community</h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>

                <div class="modal-body">
                    <form action="" method="POST">
                        <div class="form-group">
                            <input type="text" name="name" placeholder="Enter community name" class="form-control">
                        </div>
                        <div class="form-group">
                            <textarea rows="10" name="details" class="form-control"
                                placeholder="Enter details"></textarea>
                        </div>
                        <div class="form-group mt-4">
                            <button type="Submit" class="btn btn-dark form-control" name="add_community">Add</button>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </div>


    <?php
    if(isset($_POST['add_post'])){
        $title = $_POST["title"];
        $content = $_POST["content"];
        $id = $_POST["list"];
        $options = trim($_POST["options"]);
        if(!empty($options)) mysqli_query($conn,"insert into post values ('$title','$content',{$_SESSION['id']},curdate(),'$options',$id,null)" ) or die(mysqli_error($conn));
        else mysqli_query($conn,"insert into post values ('$title','$content',{$_SESSION['id']},curdate(),null,$id,null)") or die(mysqli_error($conn));
        toast("Post added","success");
    }


    $res2 = mysqli_query($conn,"select name,community_id from community") or die(mysqli_error($conn));
    if(mysqli_num_rows($res2) > 0){
        while($row2=mysqli_fetch_assoc($res2)){
            $names[] = $row2["name"];
            $ids[] = $row2["community_id"];
        }
    }
    
?>   

    <div class="modal" id="vision2">
        <div class="modal-dialog">
            <div class="modal-content">

                <div class="modal-header">
                    <h4 class="modal-title">Add Post</h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>

                <div class="modal-body">
                    <form action="" method="POST">
                        <div class="form-group">
                            <input type="text" name="title" placeholder="Enter title" class="form-control">
                        </div>
                        <div class="form-group">
                            <select name="list" class="browser-default custom-select">
                            <?php 
                                for ($i=0; $i < count($names); $i++) { 
                                    echo "<option value='$ids[$i]'>$names[$i]</option>";
                                } 
                            ?>
                            </select>
                        </div>
                        <div class="form-group">
                            <textarea rows=2 name="options"
                                placeholder="Enter comma seperated list of voting categories (leave blank if not voting)"
                                class="form-control"></textarea>
                        </div>
                        <div class="form-group">
                            <textarea rows="10" name="content" class="form-control"
                                placeholder="Enter content"></textarea>
                        </div>
                        <div class="form-group mt-4">
                            <button type="Submit" class="btn btn-dark form-control" name="add_post">Add</button>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </div>




    <script src="http://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous">
    </script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"
        integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous">
    </script>

</body>

</html>