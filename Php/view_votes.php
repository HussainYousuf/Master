<?php
// todo 
	require_once("dbc.php");
	session_start();
	$dataPoints = [];
	$title = $_GET["title"];
	$post_id = $_GET["post_id"];
	$res = mysqli_query($conn,"select content,count(*) as count from options where post_id = $post_id group by content;") or die(mysqli_error($conn));
	if(mysqli_num_rows($res) > 0){ 
		while($row=mysqli_fetch_assoc($res)){
			
			$option  = $row["content"];
			$count =  $row["count"];
			$dataPoints[] = array("label"=> $option, "y"=> $count);
		}
	}
	
?>
<!DOCTYPE HTML>
<html>

<head>


    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
        integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">

    <title>Hello, world!</title>
    <script>
    window.onload = function() {

        var chart = new CanvasJS.Chart("chartContainer", {
            animationEnabled: true,
            exportEnabled: true,
            title: {
                text: "<?php echo $title; ?>"
            },
            subtitles: [{
                text: ""
            }],
            data: [{
                type: "pie",
                showInLegend: "true",
                legendText: "{label}",
                indexLabelFontSize: 16,
                indexLabel: "{label} - #percent%",
                //yValueFormatString: "฿#,##0",
                dataPoints: <?php echo json_encode($dataPoints, JSON_NUMERIC_CHECK); ?>
            }]
        });
        chart.render();

    }
    </script>
</head>

<body>
    <nav class="navbar navbar-expand-sm sticky-top navbar-light bg-light mb-3">
        <div class="container">
            <a href="view_votes_of_post.php" class="mr-4"><i class="fas fa-arrow-left fa-2x text-dark"></i></a>
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
    <div id="chartContainer" style="height: 50vh; width: 100%;"></div>
    <script src="canvasjs.min.js"></script>
</body>

</html>