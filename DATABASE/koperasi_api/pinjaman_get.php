<?php
// pinjaman_get.php
require 'config.php';

$kode      = $_GET['kodePegawai'] ?? null;  // optional
$status    = $_GET['status'] ?? null;       // optional
$pinjamanId= $_GET['pinjamanId'] ?? null;   // optional

$sql = "
  SELECT 
    p.id, p.kodePegawai, p.jumlah, p.tenor, p.status, p.approved_by, p.approved_at, p.reject_reason, p.bunga, p.angsuranTerbayar,
    r.cicilanPerBulan, r.totalBunga, r.totalBayar, r.totalPokok, r.terbayar, r.sisaBayar, r.sisaPokok, r.angsuranDibayar
  FROM pinjaman p
  LEFT JOIN pinjaman_rincian r ON r.pinjamanId = p.id
";

$params = [];
$types = "";
$where = [];

if ($pinjamanId !== null && trim($pinjamanId) !== "") {
  $where[] = "p.id = ?";
  $params[] = intval($pinjamanId);
  $types .= "i";
}

if ($kode !== null && $kode !== "") {
  $where[] = "p.kodePegawai = ?";
  $params[] = $kode;
  $types .= "s";
}

if ($status !== null && $status !== "") {
  $where[] = "p.status = ?";
  $params[] = $status;
  $types .= "s";
}

if (!empty($where)) {
  $sql .= " WHERE " . implode(" AND ", $where);
}

$sql .= " ORDER BY p.id DESC";

if (!empty($params)) {
  $stmt = $conn->prepare($sql);
  $stmt->bind_param($types, ...$params);
  $stmt->execute();
  $result = $stmt->get_result();
} else {
  $result = $conn->query($sql);
}

$list = [];
while ($row = $result->fetch_assoc()) {
  $list[] = $row;
}

send_json([
  "success" => true,
  "data" => ($pinjamanId !== null && trim($pinjamanId) !== "") ? ($list[0] ?? null) : $list
], 200);
