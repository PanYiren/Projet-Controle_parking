<?php
include 'chekLogin.php';
    $stmt1 = $conn->query("SELECT * FROM liste")->fetchAll();
    foreach ($stmt1 as $row) {
    	$array[] = array("idImma" => $row['idImma']);
    }
    if ($stmt1 != NULL) {
		echo json_encode(
		array("Infos" => $array));
		}
	exit();
?>