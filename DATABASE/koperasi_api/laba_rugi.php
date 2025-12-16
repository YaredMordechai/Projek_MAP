<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$year  = isset($_GET["year"])  ? intval($_GET["year"])  : intval(date("Y"));
$bulan = isset($_GET["bulan"]) ? intval($_GET["bulan"]) : 0; // 0 = semua bulan

$where = [];
$types = "";
$params = [];

if ($year > 0) {
  $where[] = "YEAR(tanggal)=?";
  $types .= "i";
  $params[] = $year;
}

if ($bulan >= 1 && $bulan <= 12) {
  $where[] = "MONTH(tanggal)=?";
  $types .= "i";
  $params[] = $bulan;
}

$whereSql = "";
if (!empty($where)) $whereSql = " AND " . implode(" AND ", $where);

// pendapatan = masuk
$totalPendapatan = 0.0;
$stmt = $conn->prepare("
  SELECT COALESCE(SUM(jumlah),0) AS total
  FROM kas_transaksi
  WHERE LOWER(jenis)='masuk' $whereSql
");
if ($types !== "") $stmt->bind_param($types, ...$params);
$stmt->execute();
$res = $stmt->get_result();
if ($r = $res->fetch_assoc()) $totalPendapatan = floatval($r["total"]);
$stmt->close();

// beban = keluar
$totalBeban = 0.0;
$stmt = $conn->prepare("
  SELECT COALESCE(SUM(jumlah),0) AS total
  FROM kas_transaksi
  WHERE LOWER(jenis)='keluar' $whereSql
");
if ($types !== "") $stmt->bind_param($types, ...$params);
$stmt->execute();
$res = $stmt->get_result();
if ($r = $res->fetch_assoc()) $totalBeban = floatval($r["total"]);
$stmt->close();

$labaRugi = $totalPendapatan - $totalBeban;

$data = [
  "year" => $year,
  "bulan" => $bulan,
  "totalPendapatan" => $totalPendapatan,
  "totalBeban" => $totalBeban,
  "labaRugi" => $labaRugi,
  "isLaba" => ($labaRugi >= 0)
];

echo json_encode(["success" => true, "message" => "OK", "data" => $data]);
