


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Add Post</title>
</head>
<body>
    <a href='admin.php'>home</a><br>

    <form method="POST">
        <p><input name="title" type="text" placeholder="Enter title"></p>
        <select name="list">
            <?php 
                for ($i=0; $i < count($names); $i++) { 
                    echo "<option value='$ids[$i]'>$names[$i]</option>";
                } 
            ?>
        </select>
        <br>
        <p><input name="options" type="text" size='70' placeholder="Enter comma seperated list of options you want the user to show"></p>
        <p><textarea name="content" placeholder="Enter your post"></textarea></p>
        <input type="submit" name="submit" value="submit">
        <?php echo $msg; ?>
    </form>
</body>
</html>