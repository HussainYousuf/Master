<?php 
    require_once("dbc.php");
    session_start();
?>



<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
        integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
        integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous">
    <link rel="stylesheet" href="css/style.css">
    <title>Restaurant</title>
</head>

<body>
    <nav class="navbar navbar-expand-sm sticky-top navbar-light bg-light mb-3">
        <div class="container">
            <?php 
                echo <<<END
                <a href="user.php" class="mr-4"><i class="fas fa-arrow-left fa-2x text-dark"></i></a>
                <a class="navbar-brand" >User Panel</a>
END;
            ?>
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

    <section id="teaching-staff">
        <div class="container">
            <h1 class="h1 text-center py-5">Members</h1>
            <div class="responsive-table">
                <table class="table table-striped text-center" id="teacher-table">
                    <thead>
                        <tr>
                            <th>name</th>
                            <th>email</th>
                            <th>mobile</th>
                            <th>date</th>
                        </tr>
                    </thead>
                    <tbody>
                    <?php 
                    $res = mysqli_query($conn,"select distinct user_id from user_community where user_id != {$_SESSION['id']} and community_id in (select community_id from user_community where user_id = {$_SESSION['id']})") or die(mysqli_error($conn));
                    if(mysqli_num_rows($res) > 0){
                        while($row=mysqli_fetch_assoc($res)){
                            $res2 = mysqli_query($conn,"select name,email,mobile,date from user where user_id = {$row['user_id']}") or die(mysqli_error($conn));
                            $row = mysqli_fetch_assoc($res2);
                            $name = $row['name'];
                            $email = $row['email'];
                            $mobile = $row["mobile"];
                            $date = $row["date"];

                            echo <<<END
                            <tr>
                                <td>$name</td>
                                <td>$email</td>
                                <td>$mobile</td>     
                                <td>$date</td>                           
END;
                            echo "<tr>";
                        }
                    }
                    
                    ?>

                    </tbody>

                </table>
                <div class="form-group mt-4">
                </div>
            </div>
        </div>
    </section>


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