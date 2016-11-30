<?php

//$con=mysql_connect("mysql.hostinger.my","u650032727_lgr","5j5unjie0602","   u650032727_final");
//$statement= mysqli_prepare($con,"INSERT INTO user(name,username,password) VALUES (?,?,?)");
//mysqli_stmt_bind_param($statement,"sss",$name,$username,$password);
//mysqli_stmt_execute($statement);

$response = array();
echo json_encode($_POST);

if(isset($_POST["login"]) && isset($_POST["password"])){
   $response = ["SUCCESS"];
}
else{
   $response = ["FAILURE"];
}


echo json_encode($response);

?>