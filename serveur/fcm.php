<?php

if(isset($_POST["Token"])){
   $token = $_POST["Token"];
   $pseudo = $_POST["Pseudo"];
   
   //Connection à la base de données
   try
   {
       $sql = new PDO();
   }
   catch (Exception $e)
   {
       echo json_encode("Erreur : ". $e->getMessage());
   }

   //Update dans la base de données
   $query = $sql->prepare("UPDATE Utilisateur SET Token= :token WHERE Pseudo= :pseudo");
   $query->bindParam(':token', $_POST["Token"], PDO::PARAM_STR);
   $query->bindParam(':pseudo', $_POST["Pseudo"], PDO::PARAM_STR);
   $query->execute();
   $response = "reponse:SUCCESS";

   echo json_encode($response);

   $dbh = null;

}			