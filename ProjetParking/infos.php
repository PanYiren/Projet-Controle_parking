<?php
include 'chekLogin.php';
if (isset($_POST['idImma'])) {
    $result = "";
    $immatriculation = $_POST['idImma'];
    $stmt = $conn->query("SELECT * from liste WHERE idImma = '$immatriculation'")->fetchAll();
    foreach ($stmt as $row) {
    	     $idImma = $row['idImma'];
             $isResident = $row['IsResident'];
             $isVisiteur = $row['IsVisiteur'];
            
    }
	if ($stmt != NULL) {
		echo json_encode(array("idImma" => $idImma,
		                       "Resident" => $isResident,
		                       "Visiteur" => $isVisiteur));
	}
	else{
		echo json_encode(array("status" => "error", "message" => "User not found"));
	}
}
?>