<?php
// simpanan_get.php
require 'config.php';

if (!isset($_GET['kodePegawai']) || trim($_GET['kodePegawai']) === '') {
    send_json([
        "success" => false,
        "message" => "Parameter kodePegawai wajib diisi"
    ], 200);
}

$kode = trim($_GET['kodePegawai']);

$stmt = $conn->prepare("SELECT * FROM simpanan WHERE kodePegawai = ? LIMIT 1");
$stmt->bind_param("s", $kode);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    send_json([
        "success" => true,
        "data" => $row
    ], 200);
} else {
    send_json([
        "success" => true,
        "data" => null
    ], 200);
}
