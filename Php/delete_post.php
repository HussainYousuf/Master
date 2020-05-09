<?php 

    require_once("dbc.php");
    session_start();
    if(isset($_POST['submit'])){
        $ids = $_POST['delete'];
        foreach($ids as $id){
            mysqli_query($conn,"delete from post where post_id = $id") or die(mysqli_error($conn));
            mysqli_query($conn,"delete from comment where post_id = $id") or die(mysqli_error($conn));
            mysqli_query($conn,"delete from options where post_id = $id") or die(mysqli_error($conn));
            
        }
    }
    $res = mysqli_query($conn,"select title,content,date,post_id from post where user_id = {$_SESSION['id']}") or die(mysqli_error($conn));

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
    <title>Admin</title>
</head>

<body>
    <nav class="navbar navbar-expand-sm sticky-top navbar-light bg-light mb-3">
        <div class="container">
            <?php 
                echo <<<END
                <a href="admin.php" class="mr-4"><i class="fas fa-arrow-left fa-2x text-dark"></i></a>
                <a class="navbar-brand" >Admin Panel</a>
END;
            
            ?>
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

    <section id="teaching-staff">
        <div class="container">
            <h1 class="h1 text-center py-5">Posts</h1>
            <div class="responsive-table">
                <table class="table table-striped text-center" id="teacher-table">
                    <thead>
                        <tr>
                            <th>title</th>
                            <th>content</th>
                            <th>date</th>
                            <th>view comment</th>
                            <th>delete</th>
                        </tr>
                    </thead>
                    <tbody>
                    <form method='post'>
                    <?php 
                    
                    if(mysqli_num_rows($res) > 0){
                        while($row=mysqli_fetch_assoc($res)){
                            $title = $row['title'];
                            $content = $row['content'];
                            $date = $row["date"];
                            $post_id = $row["post_id"];

                            echo <<<END
                            <tr>
                                <td>$title</td>
                                <td>$content</td>
                                <td>$date</td>
                                <td><a class="btn btn-primary" href="view_comment.php?post_id=$post_id" role="button">Comments</a>
                                <td><input type='checkbox' name='delete[]' value="$post_id"><td>  
                            <tr>    

END;

                        }
                    }
                    
                    ?>

                    </tbody>

                </table>
                <div class="form-group mt-4">
                    <input type='submit' class='btn btn-dark form-control' name='submit'>
                    </form>
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