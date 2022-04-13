<?php
include 'chekLogin.php';
if (isset($_POST['isResident']) && isset($_POST['isVisiteur'])) {
    $result = "";
    $isResident = $_POST['isResident'];
    $isVisiteur = $_POST['isVisiteur'];
    
    $stmt = $conn->query("SELECT idImma FROM `liste` WHERE IsResident = '$isResident' AND IsVisiteur = '$isVisiteur'")->fetchAll();
    foreach ($stmt as $row) {
    	     $idImma[] = array('idImma' => $row['idImma'] );
            
    }
	if ($stmt != NULL) {
		echo json_encode(array("idImma" => $idImma));
	}
	else{
		echo json_encode(array("status" => "Aucune immatriculation"));
	}
}
?>