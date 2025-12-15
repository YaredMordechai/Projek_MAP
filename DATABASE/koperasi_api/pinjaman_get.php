<?php
// pinjaman_get.php
require 'config.php';

$kode   = $_GET['kodePegawai'] ?? null; // optional
$status = $_GET['status'] ?? null;      // optional (Proses/Disetujui/Ditolak/Lunas/Aktif)

$sql = "SELECT * FROM pinjaman";
$params = [];
$types = "";
$where = [];

if ($kode !== null && $kode !== "") {
    $where[] = "kodePegawai = ?";
    $params[] = $kode;
    $types .= "s";
}

if ($status !== null && $status !== "") {
    $where[] = "status = ?";
    $params[] = $status;
    $types .= "s";
}

if (!empty($where)) {
    $sql .= " WHERE " . implode(" AND ", $where);
}

$sql .= " ORDER BY id DESC";

if (!empty($params)) {
    $stmt = $conn->prepare($sql);
    $stmt->bind_param($types, ...$params);
    $stmt->execute();
    $result = $stmt->get_result();
} else {
    $result = $conn->query($sql);
}

$pinjaman = [];
while ($row = $result->fetch_assoc()) {
    $pinjaman[] = $row;
}

send_json([
    "success" => true,
    "data" => $pinjaman
]);
