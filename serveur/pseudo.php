<?php

if(isset($_POST["Pseudo"])){
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

	$statement=$sql->prepare("SELECT Pseudo FROM Utilisateur WHERE Pseudo != :pseudo");
        $statement->bindParam(':pseudo', $_POST["Pseudo"], PDO::PARAM_STR);
	$statement->execute();
	$results=$statement->fetchAll(PDO::FETCH_ASSOC);

	echo json_encode($results);

	$sql =null;
}

?>