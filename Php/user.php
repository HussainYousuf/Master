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

    <title>User Panel</title>
</head>

<body>

    <nav class="navbar navbar-expand-sm sticky-top navbar-light bg-light mb-3">
        <div class="container">
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

    <section id="home-admin" class="py-5">
        <div class="container">
            <div class="row">
                <div class="col-sm my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="fas fa-clipboard-list fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="view_post.php">view posts</a>
                        </div>
                    </div>
                </div>
                <div class="col-sm my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="far fa-handshake fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="sub.php">subscribe to community</a>
                        </div>
                    </div>
                </div>
            </div>   
            <div class="row">
                <div class="col-sm my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="fas fa-users fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="view_member.php">other members</a>
                        </div>
                    </div>
                </div>
                <div class="col-sm my-3">
                    <div class="card">
                        <div class="card-body text-center">
                            <i class="fas fa-exclamation fa-6x"></i>
                        </div>
                        <div class="card-footer text-center">
                            <a href="unsub.php">unsubscribe from community</a>
                        </div>
                    </div>
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


