<?php
include 'chekLogin.php';
if(isset($_POST['login']) && isset($_POST['password'])){
	$result = "";
	$login = $_POST['login'] ;
	$password = $_POST['password'];
	$sql = "SELECT * FROM utilisateur WHERE login = :login AND password = :password";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(":login", $login, PDO::PARAM_STR);
	$stmt->bindParam(":password", $password, PDO::PARAM_STR);
	$stmt->execute();
	$stmt1 = $conn->query("SELECT * from utilisateur WHERE login = '$login' AND password = '$password' ")->fetchAll();
    foreach ($stmt1 as $row) {
    	     $isGardien = $row['IsGardien'];
    	     $isPolice = $row['IsPolice'];
            
    }
	if ($stmt->rowCount()) {
		if ($isGardien == "1") {
		    $result="true";
		    $stmt->closeCursor();
		}
		else{
			$result = "false";
		}
		
	}
	elseif (!$stmt->rowCount()) {
		$result = "false";
		$stmt->closeCursor();
	}
	echo "$result";
	exit();
	}
?>