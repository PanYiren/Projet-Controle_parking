<?php
include 'chekLogin.php';
if (isset($_POST['Profile'])) {
    $result = "";
    $profile = $_POST['Profile'];
    
    $stmt = $conn->query("SELECT idImma FROM `liste_blanc` WHERE Profile = '$profile'")->fetchAll();
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