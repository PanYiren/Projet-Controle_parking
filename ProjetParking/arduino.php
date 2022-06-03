<?php
include 'chekLogin.php';
if (isset($_GET['immatriculation'])) {
    $immatriculation = $_GET['immatriculation'];
    $stmt1 = $conn->query("SELECT * FROM liste_blanc")->fetchAll();
    foreach ($stmt1 as $row) {
      if ($row['idImma'] == $immatriculation) {
         echo "true";
         break;
      }
      else {
         echo "false";
      }
    }
    exit();
}
?>