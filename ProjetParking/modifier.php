<?php
include 'chekLogin.php';
if (isset($_POST['Imma']) && isset($_POST['Profile']) && isset($_POST['idImma'])) {
$result = "";
$idImma = $_POST['Imma'];
$immatriculation = $_POST['idImma'];
$profile = $_POST['Profile'];
$sql = "UPDATE liste_blanc SET idImma = '$immatriculation', Profile = '$profile' WHERE idImma = '$idImma'";
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