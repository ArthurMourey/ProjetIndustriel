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

//Vérifications des logins
$response = array();
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
else{
   $response = "response:FAILURE";
}

echo json_encode($response);


?>