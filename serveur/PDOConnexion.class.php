<?php
	class PDOConnexion{
		
		
		
		public function connexion(){
			try {
				return  new PDO(,array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
			} catch (Exception $e) {
				die('ERROR : '.$e->getMessage());
			}
		}
	}
