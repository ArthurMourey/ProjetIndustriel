<?php

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

//La réponse qui sera renvoyée suite à la demande client
$response = array();

//Vérifications des logins
if(isset($_POST["login"]) && isset($_POST["password"])){
	$query = $sql->prepare("SELECT MotDePasse FROM Utilisateur WHERE Pseudo = :pseudo");
        $query->bindParam(':pseudo', $_POST["login"], PDO::PARAM_STR);
        $query->execute();
        $res = $query->fetch();
        if(password_verify($_POST["password"],$res["MotDePasse"])){
		$response = "reponse:SUCCESS";
	}
	else{
		$response = $res["MotDePasse"];
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
                $hash = password_hash($_POST["mdp_inscription"],PASSWORD_BCRYPT);

                $query = $sql->prepare("INSERT INTO Utilisateur(Pseudo, Nom, Prenom, MotDePasse, Email) VALUES (:pseudo, :nom, :prenom, :mdp, :mail)");
                $query->execute(array("pseudo" => $_POST["login_inscription"],"nom" => $_POST["nom_inscription"],"prenom" => $_POST["prenom_inscription"],"mdp" => $hash, "mail" => $_POST["mail_inscription"]));
                $response = "reponse:SUCCESS";
        }
}

//Renvois de la réponse en json
echo json_encode($response);

$dbh = null;

?>

							