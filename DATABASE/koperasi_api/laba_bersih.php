<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

// total pemasukan
$totalMasuk = 0.0;
$res = $conn->query("
    SELECT COALESCE(SUM(jumlah),0) AS total
    FROM kas_transaksi
    WHERE LOWER(jenis) = 'masuk'
");
if ($row = $res->fetch_assoc()) {
    $totalMasuk = floatval($row["total"]);
}

// total pengeluaran
$totalKeluar = 0.0;
$res = $conn->query("
    SELECT COALESCE(SUM(jumlah),0) AS total
    FROM kas_transaksi
    WHERE LOWER(jenis) = 'keluar'
");
if ($row = $res->fetch_assoc()) {
    $totalKeluar = floatval($row["total"]);
}

$labaBersih = $totalMasuk - $totalKeluar;

$data = [
    "totalMasuk" => $totalMasuk,
    "totalKeluar" => $totalKeluar,
    "labaBersih" => $labaBersih
];

echo json_encode([
    "success" => true,
    "message" => "OK",
    "data" => $data
]);
