<?php
$host = "localhost";
$user = "root"; // default laragon
$pass = "";     // default laragon kosong
$db   = "projek_map";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die("Koneksi gagal: " . $conn->connect_error);
}
?>
