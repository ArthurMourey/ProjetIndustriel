<?php
	
	class Album{

		private $_ID;
		private $_nom;
		private $_coverImage;
		private $_dateSortie;


		public function __construct(array $donnees){
			$this->hydrater($donnees);
		}

		//Getters
		public function getID(){
			return $this->_ID ;
		}

		public function getNom(){
			return $this->_nom ;
		}

		public function getCoverImage(){
			return $this->_coverImage ;
		}

		public function getDateSortie(){
			return $this->_dateSortie ;
		}

		//Setters

		public function setID($id){
			$id = (int)$id;
			if($id > 0){
				$this->_ID = $id;
			}	
		}
		
		public function setNom($nom){
		
			if(is_string($nom)){
				$this->_nom =  $nom;
			}
		}

		public function setCoverImage($coverImage){
		
			if(is_string($coverImage)){
				$this->_coverImage =  $coverImage;
			}
		}

		public function setDateSortie($dateSortie){
		
			$this->_dateSortie =  $dateSortie;
		}


		public function afficher(){
			echo '<strong>INFOS Album </strong><br/>
				  ID : <b>'. $this->getID() .': </b> <br> 
				  Nom : <b>' . $this->getNom() .' </b><br/>
				  CoverImage  : <b>' . $this->getCoverImage() .' </b><br/>
				  Date Sortie : <b>' . $this->getDateSortie() .' </b><br/>';
				  
			echo '<hr/>';
		}

		public function __toString(){
			return $this->getID() . ' ' . $this->getNom() . ' ' .
				   $this->getCoverImage() . ' ' . $this->getDateSortie() ;
		}


		/*Pour assigner dynamiquement les données de la base
		aux objets avec les setters*/
		public function hydrater(array $donnees){

			foreach ($donnees as $key => $value) {

				$setter = 'set'.ucfirst($key);
				if(method_exists('Album', $setter)){
					
					$this->$setter($value);
				}
			}
		}
}