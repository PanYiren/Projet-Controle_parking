<?php
include 'chekLogin.php';
if(isset($_POST['idImma']) && isset($_POST['Profile'])){
	$result = "";
	$idImma = $_POST['idImma'];
	$profile = $_POST['Profile'];
    $sql = "INSERT INTO liste_blanc (idImma, Profile) VALUES ('$idImma', '$profile')";
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