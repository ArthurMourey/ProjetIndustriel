<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	</head>
	<body>
		<form action="sendNotifications.php" method="POST">
			<input id="notifContent" name="notifContent" type="text" placeholder="Contenu de la notification"/>
			<input type="submit" value="Envoyer notification"/>
		</form>
                </br>
	</body>

</html>

<?php 
function send_notification ($tokens, $message){
	$url = 'https://fcm.googleapis.com/fcm/send';
	$fields = array(
		'registration_ids' => $tokens,
                'data' => $message,
                'notification' => array('sound' => 'default', 'body' => $message['message'], 'title' => 'Musy')
		);
	$headers = array(
		'Authorization:key = AIzaSyD-db1ZDc5bRUVGAeioznd-lJNDEgi6yLk',
		'Content-Type: application/json'
		);

	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);  
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
	$result = curl_exec($ch);           
	if ($result === FALSE) {
		die('Curl failed: ' . curl_error($ch));
	}
	curl_close($ch);
	return $result;
}

if(isset($_POST["notifContent"])){
	//Connection à la base de données
	try
	{
	    $sql = new PDO();
	}
	catch (Exception $e)
	{
	    echo json_encode("Erreur : ". $e->getMessage());
	    die();
	}

	$statement=$sql->prepare("SELECT Token FROM Utilisateur");
	$statement->execute();
	$results=$statement->fetchAll(PDO::FETCH_ASSOC);

	$message = array("message" => $_POST["notifContent"]);
	foreach ($results as $token) {
	    $message_status = send_notification(array($token["Token"]), $message);
	    echo $message_status;
	}
	$sql =null;
        echo "</br></br>Message envoyé !";
}

?>								