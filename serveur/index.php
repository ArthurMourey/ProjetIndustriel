<?php

//Connection à la base de données
try
{
    $sql = new PDO("mysql:host=madpumpkzwadmin.mysql.db;dbname=madpumpkzwadmin","madpumpkzwadmin","M4d6um9k1n");
}
catch (Exception $e)
{
    echo json_encode("Erreur : ". $e->getMessage());
}

//La réponse qui sera renvoyée suite à la demande client
$response = array();

//Vérifications des logins
if(isset($_POST["login"]) && isset($_POST["password"])){
	$query = $sql->prepare("SELECT COUNT(*) FROM Utilisateur WHERE Pseudo = :pseudo AND MotDePasse = :mdp");
        $query->bindParam(':pseudo', $_POST["login"], PDO::PARAM_STR);
        $query->bindParam(':mdp',$_POST["password"], PDO::PARAM_STR);
        $query->execute();
        $res = $query->fetch();
        if($res["COUNT(*)"]==1){
		$response = "reponse:SUCCESS";
	}
	else{
		$response = "reponse:FAILURE";
	}
}

//Renvois de la réponse
echo json_encode($response);


?>

				