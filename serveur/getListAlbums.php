<?php

	header('Content-type:application/json;charset=utf-8');

			
			function chargerClass($classe){
				require_once $classe . '.class.php';
			}
			
			spl_autoload_register('chargerClass');

			$db = new PDOConnexion();
			$albumManager = new AlbumManager($db->connexion());
			$listAlbums = $albumManager->getListAlbum();

			
			$result["albums"] = array();

			if($listAlbums != null){
				foreach ($listAlbums as $album) {
					$ligne = array();
			        $ligne["id"] = $album->getID();
			        $ligne["nom"] = $album->getNom();
					$ligne["coverImage"] = $album->getCoverImage();
					$ligne["dateSortie"] = $album->getDateSortie();
					$ligne["nbSons"] = $albumManager->getNbSons($album->getID());
					// push la ligne dans l'array final
					array_push($result["albums"] , $ligne);
				}
				$result["success"] = 1;
				
			}else {
			    $result["success"] = 0;
			    $result["message"] = "No data found";
			}
			//JSON_UNESCAPED_SLASHES : pour supprimer les caractères \/ dans l'url de l'image
			$result = json_encode(array("result"=>$result), JSON_UNESCAPED_SLASHES);
			echo $result;
			
			
?>