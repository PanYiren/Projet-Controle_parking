<?php
include 'chekLogin.php';
if (isset($_POST['idImma'])) {
    $result = "";
	$idImma = $_POST['idImma'];
	/*$stmt1 = $conn->query("SELECT * from liste_blanc WHERE idImma = '$idImma'")->fetchAll();
    foreach ($stmt1 as $row) {
    	     $id = $row['idImma'];
            
    }*/
    $sql = "DELETE FROM liste_blanc WHERE idImma = '$idImma'";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
    if ($stmt->rowCount()) {
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