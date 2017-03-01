<?php

	header('Content-type:application/json;charset=utf-8');

			
		function chargerClass($classe){
			require_once $classe . '.class.php';
		}
			
		spl_autoload_register('chargerClass');

		if(isset($_GET['albumId'])) {
			$albumId = $_GET['albumId'];
		
			if(!empty($albumId)){

				$db = new PDOConnexion();
				$musiqueManager = new MusiqueManager($db->connexion());
				$listMusiques = $musiqueManager->getMusiqueByAlbumId($albumId);

				$result["musiques"] = array();

				if($listMusiques != null){
					foreach ($listMusiques as $musique) {
						$ligne = array();
				        $ligne["id"] = $musique->getID();
				        $ligne["albumId"] = $musique->getAlbumId();
						$ligne["titre"] = $musique->getTitre();
						$ligne["duree"] = $musique->getDuree();
						$ligne["coverImage"] = $musique->getCoverImage();
						$ligne["url"] = $musique->getUrl();
						// push la ligne dans l'array final
						array_push($result["musiques"] , $ligne);
					}
					$result["success"] = 1;
					
				}else {
				    $result["success"] = 0;
				    $result["message"] = "No data found";
				}
				
				//JSON_UNESCAPED_SLASHES : pour supprimer les caractères \/ dans l'url de l'image
				$result = json_encode(array("result"=>$result), JSON_UNESCAPED_SLASHES);
				echo $result;

			}
		}
			
			
?>