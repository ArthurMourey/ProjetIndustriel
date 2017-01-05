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

//Vérification de l'inscription
if(isset($_POST["login_inscription"]) && isset($_POST["nom_inscription"]) && isset($_POST["prenom_inscription"]) && isset($_POST["mail_inscription"]) && isset($_POST["mdp_inscription"])){
        $query = $sql->prepare("SELECT COUNT(*) FROM Utilisateur WHERE Pseudo = :pseudo");
        $query->bindParam(':pseudo', $_POST["login_inscription"], PDO::PARAM_STR);
        $query->execute();
        $res = $query->fetch();
        if($res["COUNT(*)"]>=1){
                $response = "reponse:FAILURE";
        }
        else{
                $query = $sql->prepare("INSERT INTO Utilisateur(Pseudo, Nom, Prenom, MotDePasse, Email) VALUES (:pseudo, :nom, :prenom, :mdp, :mail)");
                $query->execute(array("pseudo" => $_POST["login_inscription"],"nom" => $_POST["nom_inscription"],"prenom" => $_POST["prenom_inscription"],"mdp" => $_POST["mdp_inscription"], "mail" => $_POST["mail_inscription"]));
                $response = "reponse:SUCCESS";
        }
}


//Renvois de la réponse en json
echo json_encode($response);

?>

							