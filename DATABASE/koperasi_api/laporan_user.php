<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$kodePegawai = isset($_GET["kodePegawai"]) ? trim($_GET["kodePegawai"]) : "";
$year = isset($_GET["year"]) ? intval($_GET["year"]) : intval(date("Y"));
$bulan = isset($_GET["bulan"]) ? intval($_GET["bulan"]) : intval(date("n")); // 1..12

if ($kodePegawai === "") {
  echo json_encode(["success" => false, "message" => "kodePegawai wajib", "data" => null]);
  exit;
}
if ($bulan < 1 || $bulan > 12) $bulan = intval(date("n"));

// 1) Total simpanan (simpanan table)
$totalSimpanan = 0.0;
$stmt = $conn->prepare("SELECT simpananPokok, simpananWajib, simpananSukarela FROM simpanan WHERE kodePegawai = ?");
$stmt->bind_param("s", $kodePegawai);
$stmt->execute();
$res = $stmt->get_result();
if ($row = $res->fetch_assoc()) {
  $totalSimpanan = floatval($row["simpananPokok"]) + floatval($row["simpananWajib"]) + floatval($row["simpananSukarela"]);
}
$stmt->close();

// 2) Total pinjaman aktif (status != Lunas)
$totalPinjamanAktif = 0.0;
$stmt = $conn->prepare("SELECT COALESCE(SUM(jumlah),0) AS total FROM pinjaman WHERE kodePegawai = ? AND LOWER(status) <> 'lunas'");
$stmt->bind_param("s", $kodePegawai);
$stmt->execute();
$res = $stmt->get_result();
if ($row = $res->fetch_assoc()) {
  $totalPinjamanAktif = floatval($row["total"]);
}
$stmt->close();

// 3) Total angsuran bulanan (histori_pembayaran, month & year)
$totalAngsuranBulanan = 0.0;
$stmt = $conn->prepare("SELECT COALESCE(SUM(jumlah),0) AS total FROM histori_pembayaran WHERE kodePegawai = ? AND YEAR(tanggal)=? AND MONTH(tanggal)=?");
$stmt->bind_param("sii", $kodePegawai, $year, $bulan);
$stmt->execute();
$res = $stmt->get_result();
if ($row = $res->fetch_assoc()) {
  $totalAngsuranBulanan = floatval($row["total"]);
}
$stmt->close();

// 4) Rekap bulanan simpanan (histori_simpanan)
$monthlySimpanan = array_fill(0, 12, 0.0);
$stmt = $conn->prepare("
  SELECT MONTH(tanggal) AS m, COALESCE(SUM(jumlah),0) AS total
  FROM histori_simpanan
  WHERE kodePegawai = ? AND YEAR(tanggal) = ?
  GROUP BY MONTH(tanggal)
");
$stmt->bind_param("si", $kodePegawai, $year);
$stmt->execute();
$res = $stmt->get_result();
while ($r = $res->fetch_assoc()) {
  $m = intval($r["m"]); // 1..12
  if ($m >= 1 && $m <= 12) $monthlySimpanan[$m - 1] = floatval($r["total"]);
}
$stmt->close();

// 5) Rekap bulanan angsuran (histori_pembayaran)
$monthlyAngsuran = array_fill(0, 12, 0.0);
$stmt = $conn->prepare("
  SELECT MONTH(tanggal) AS m, COALESCE(SUM(jumlah),0) AS total
  FROM histori_pembayaran
  WHERE kodePegawai = ? AND YEAR(tanggal) = ?
  GROUP BY MONTH(tanggal)
");
$stmt->bind_param("si", $kodePegawai, $year);
$stmt->execute();
$res = $stmt->get_result();
while ($r = $res->fetch_assoc()) {
  $m = intval($r["m"]);
  if ($m >= 1 && $m <= 12) $monthlyAngsuran[$m - 1] = floatval($r["total"]);
}
$stmt->close();

$data = [
  "kodePegawai" => $kodePegawai,
  "year" => $year,
  "bulan" => $bulan,
  "totalSimpanan" => $totalSimpanan,
  "totalPinjamanAktif" => $totalPinjamanAktif,
  "totalAngsuranBulanan" => $totalAngsuranBulanan,
  "monthlySimpanan" => $monthlySimpanan,   // index 0..11
  "monthlyAngsuran" => $monthlyAngsuran    // index 0..11
];

echo json_encode(["success" => true, "message" => "OK", "data" => $data]);
