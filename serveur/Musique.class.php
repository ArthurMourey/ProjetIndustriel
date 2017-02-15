<?php
	
	class Musique{

		private $_ID;
		private $_albumId;
		private $_titre;
		private $_duree;
		private $_coverImage;
		private $_url;


		public function __construct(array $donnees){
			$this->hydrater($donnees);
		}

		//Getters
		public function getID(){
			return $this->_ID ;
		}

		public function getAlbumId(){
			return $this->_albumId ;
		}

		public function getTitre(){
			return $this->_titre ;
		}

		public function getDuree(){
			return $this->_duree ;
		}

		public function getCoverImage(){
			return $this->_coverImage ;
		}

		public function getUrl(){
			return $this->_url ;
		}

		//Setters

		public function setID($id){
			$id = (int)$id;
			if($id > 0){
				$this->_ID = $id;
			}	
		}

		public function setAlbumId($id){
			$id = (int)$id;
			if($id > 0){
				$this->_albumId = $id;
			}	
		}
		
		public function setTitre($titre){
			if(is_string($titre)){
				$this->_titre =  $titre;
			}
		}

		public function setDuree($duree){
			if(is_string($duree)){
				$this->_duree =  $duree;
			}
		}

		public function setCoverImage($coverImage){
		
			if(is_string($coverImage)){
				$this->_coverImage =  $coverImage;
			}
		}

		public function setUrl($url){
		
			$this->_url =  $url;
		}


		public function afficher(){
			echo '<strong>INFOS Album </strong><br/>
				  ID : <b>'. $this->getID() .': </b> <br> 
				  AlbumID : <b>' . $this->getAlbumId() .' </b><br/>
				  Titre : <b>' . $this->getTitre() .' </b><br/>
				  Duree : <b>' . $this->getDuree() .' </b><br/>
				  CoverPhoto  : <b>' . $this->getCoverImage() .' </b><br/>
				  Url : <b>' . $this->getUrl() .' </b><br/>';
				  
			echo '<hr/>';
		}

		public function __toString(){
			return $this->getID() . ' ' . $this->getAlbumId() . ' ' .
				   $this->getTitre() . ' ' . $this->getDuree()  . ' ' .
				   $this->getCoverImage() . ' ' . $this->getUrl() ;
		}


		/*Pour assigner dynamiquement les données de la base
		aux objets avec les setters*/
		public function hydrater(array $donnees){

			foreach ($donnees as $key => $value) {

				$setter = 'set'.ucfirst($key);
				if(method_exists('Musique', $setter)){
					$this->$setter($value);
				}
			}
		}
}