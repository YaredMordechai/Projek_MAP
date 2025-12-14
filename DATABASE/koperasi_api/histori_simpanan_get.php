<?php
// histori_simpanan_get.php
require 'config.php';

// optional filter
$kode = isset($_GET['kodePegawai']) ? trim($_GET['kodePegawai']) : '';

if ($kode !== '') {
    $stmt = $conn->prepare("SELECT id, kodePegawai, tanggal, jenis, jumlah, keterangan
                            FROM histori_simpanan
                            WHERE kodePegawai = ?
                            ORDER BY tanggal DESC, id DESC");
    $stmt->bind_param("s", $kode);
} else {
    $stmt = $conn->prepare("SELECT id, kodePegawai, tanggal, jenis, jumlah, keterangan
                            FROM histori_simpanan
                            ORDER BY tanggal DESC, id DESC");
}

$stmt->execute();
$result = $stmt->get_result();

$data = [];
while ($row = $result->fetch_assoc()) {
    $row["id"] = (int)$row["id"];
    $row["jumlah"] = (float)$row["jumlah"];
    $data[] = $row;
}

send_json([
    "success" => true,
    "data" => $data
], 200);
?>
