<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json; charset=utf-8');

$host = "localhost";
$user = "root";
$pass = "";
$db   = "projek_map";

$conn = mysqli_connect($host, $user, $pass, $db);

if (!$conn) {
    echo json_encode(["success" => false, "message" => "Koneksi gagal: " . mysqli_connect_error()]);
    exit;
}
?>
