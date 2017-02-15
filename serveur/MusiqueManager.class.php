<?php

class MusiqueManager {

	private $_db; //instance de PDO

	public function __construct($db){
		$this->setDB($db);
	}

	public function setDB($db){
		$this->_db = $db;
	}
	

	public function get($id){
		/*Executer une requete de type SELECT avec where
		retourne un objet Musique*/
		$id = (int)$id;
		$requeste = $this->_db->prepare('SELECT * FROM Musique WHERE ID = :id');
		$requeste->bindValue(':id',$id,PDO::PARAM_INT);
		$donnees = $requeste->fetch(PDO::FETCH_ASSOC);

		return  new Musique($donnees);
	}

	
	public function getMusiqueByAlbumId($albumId){
		//Retourne la liste de toutes les Musique d'un album donnée

		$albumId = (int)$albumId;
		$musiques = [];
		$requeste = $this->_db->prepare('SELECT * FROM Musique m JOIN Album a ON m.AlbumID = a.ID WHERE AlbumID = :albumId ORDER BY a.ID');
		$requeste->bindValue(':albumId',$albumId,PDO::PARAM_INT);
		$requeste->execute();
		$musiques = [];
		while($donnees = $requeste->fetch(PDO::FETCH_ASSOC)){
			$musiques[] = new Musique($donnees);
		}
		
		return $musiques;
	}
	
}