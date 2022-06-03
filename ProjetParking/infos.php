<?php
include 'chekLogin.php';
if (isset($_POST['idImma'])) {
    $result = "";
    $immatriculation = $_POST['idImma'];
    $stmt = $conn->query("SELECT * from liste_blanc WHERE idImma = '$immatriculation'")->fetchAll();
    foreach ($stmt as $row) {
    	     $idImma = $row['idImma'];
             $profile = $row['Profile'];
            
    }
	if ($stmt != NULL) {
		echo json_encode(array("idImma" => $idImma,
		                       "Profile" => $profile));
	}
	else{
		echo json_encode(array("status" => "error", "message" => "User not found"));
	}
}
?>