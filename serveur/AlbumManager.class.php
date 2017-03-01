<?php

class AlbumManager {

	private $_db; //instance de PDO

	public function __construct($db){
		$this->setDB($db);
	}

	public function setDB($db){
		$this->_db = $db;
	}
	

	public function get($id){
		/*Executer une requete de type SELECT avec where
		retourne un objet Album*/
		$id = (int)$id;
		$requeste = $this->_db->prepare('SELECT * FROM Album WHERE ID = :id');
		$requeste->bindValue(':id',$id,PDO::PARAM_INT);
		$donnees = $requeste->fetch(PDO::FETCH_ASSOC);

		return  new Album($donnees);
	}

	/**
		Retourne le nombre de sons d'un album
	**/
	public function getNbSons($id){
		$id = (int)$id;
		$requeste = $this->_db->prepare('SELECT count(AlbumID) FROM Musique WHERE AlbumID = :albumId');
		$requeste->bindValue(':albumId',$id,PDO::PARAM_INT);
		$requeste->execute();
		
		return  $requeste->fetchColumn();
	}

	public function getListAlbum(){
		//Retourne la liste de tous les Albums

		$album = [];
		$requeste = $this->_db->query('SELECT * FROM Album ORDER BY ID');
		while($donnees = $requeste->fetch(PDO::FETCH_ASSOC)){
			$album[] = new Album($donnees);
		}
		
		return $album;
	}
	
}