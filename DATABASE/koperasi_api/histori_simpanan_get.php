<?php
// histori_simpanan_get.php
require 'config.php';

header('Content-Type: application/json; charset=UTF-8');

$kode = isset($_GET['kodePegawai']) ? trim($_GET['kodePegawai']) : '';

if ($kode !== '') {
    $stmt = $conn->prepare("
        SELECT id, kodePegawai, tanggal, jenis, jumlah, keterangan
        FROM histori_simpanan
        WHERE kodePegawai = ?
        ORDER BY tanggal DESC, id DESC
    ");
    if (!$stmt) {
        send_json(["success" => false, "message" => "Prepare failed: " . $conn->error], 500);
        exit;
    }
    $stmt->bind_param("s", $kode);
} else {
    $stmt = $conn->prepare("
        SELECT id, kodePegawai, tanggal, jenis, jumlah, keterangan
        FROM histori_simpanan
        ORDER BY tanggal DESC, id DESC
    ");
    if (!$stmt) {
        send_json(["success" => false, "message" => "Prepare failed: " . $conn->error], 500);
        exit;
    }
}

if (!$stmt->execute()) {
    send_json(["success" => false, "message" => "Execute failed: " . $stmt->error], 500);
    exit;
}

$result = $stmt->get_result();

$data = [];
while ($row = $result->fetch_assoc()) {
    $row["id"] = (int)$row["id"];
    $row["jumlah"] = (float)$row["jumlah"];
    $data[] = $row;
}

$stmt->close();

send_json([
    "success" => true,
    "data" => $data
], 200);
exit;
