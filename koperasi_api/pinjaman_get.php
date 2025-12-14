<?php
// pinjaman_get.php
require 'config.php';

// Optional: ?kodePegawai=EMP001
$kode = $_GET['kodePegawai'] ?? null;

if ($kode) {
    $stmt = $conn->prepare("SELECT * FROM pinjaman WHERE kodePegawai = ? ORDER BY id DESC");
    $stmt->bind_param("s", $kode);
    $stmt->execute();
    $result = $stmt->get_result();
} else {
    // tanpa parameter -> semua (admin)
    $result = $conn->query("SELECT * FROM pinjaman ORDER BY id DESC");
}

$pinjaman = [];
while ($row = $result->fetch_assoc()) {
    $pinjaman[] = $row;
}

send_json([
    "success" => true,
    "data" => $pinjaman
]);
