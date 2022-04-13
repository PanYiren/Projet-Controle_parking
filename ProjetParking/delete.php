<?php
include 'chekLogin.php';
if (isset($_POST['idImma'])) {
    $result = "";
	$idImma = $_POST['idImma'];
	$stmt1 = $conn->query("SELECT * from liste WHERE idImma = '$idImma'")->fetchAll();
    foreach ($stmt1 as $row) {
    	     $id = $row['idImma'];
            
    }
    $sql1 = "DELETE FROM captureimmatriculation WHERE idImma = '$idImma'";
    $stmt2 = $conn->prepare($sql1);
    $stmt2->execute();
    $sql = "DELETE FROM liste WHERE idImma = '$idImma'";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
    if ($stmt->rowCount() || $stmt2->rowCount()) {
		$result="true";
		$stmt->closeCursor();
		$stmt2->closeCursor();
	}
	elseif (!$stmt->rowCount()) {
		$result = "false";
	}
	echo "$result";
	exit();
}
?>