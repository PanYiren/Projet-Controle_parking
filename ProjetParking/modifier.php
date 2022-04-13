<?php
include 'chekLogin.php';
if (isset($_POST['Imma']) && isset($_POST['idImma']) && isset($_POST['IsResident']) && isset($_POST['IsVisiteur'])) {
$result = "";
$idImma = $_POST['Imma'];
$immatriculation = $_POST['idImma'];
$isres = $_POST['IsResident'];
$isvis = $_POST['IsVisiteur'];
$sql = "UPDATE liste SET idImma = '$immatriculation', IsResident = '$isres', IsVisiteur = '$isvis' WHERE idImma = '$idImma'";
$stmt = $conn->prepare($sql);
$stmt->execute();
if ($stmt->rowCount()) {
	$result="true";
	$stmt->closeCursor();
}
elseif (!$stmt->rowCount()) {
	$result = "false";
	$stmt->closeCursor();
}
	echo "$result";
	exit();
}
?>