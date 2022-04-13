<?php
include 'chekLogin.php';
if(isset($_POST['idImma']) && isset($_POST['IsResident']) && isset($_POST['IsVisiteur'])){
	$result = "";
	$idImma = $_POST['idImma'];
	$isResident = $_POST['IsResident'];
	$isVisiteur = $_POST['IsVisiteur'];
    $sql = "INSERT INTO liste (idImma, IsResident, IsVisiteur) VALUES ('$idImma', '$isResident', '$isVisiteur')";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	if ($stmt->rowCount()) {
		$result="true";
		$stmt->closeCursor();
	}
	elseif (!$stmt->rowCount()) {
		$result = "false";
	}
	echo "$result";
	exit();
}
?>