<?php
$servername = "192.168.10.20";
$username = "admin";
$password = "admin";
$dbname = "parking";

try{
	$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
}
catch(PDOException $e){
	die("Connexion à la base échoué");
}

?>