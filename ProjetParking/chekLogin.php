<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "parking";

try{
	$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
}
catch(PDOException $e){
	die("Connexion à la base échoué");
}

?>