<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

// 1) Jumlah anggota
$jumlahAnggota = 0;
$res = $conn->query("SELECT COUNT(*) AS c FROM users");
if ($row = $res->fetch_assoc()) $jumlahAnggota = intval($row["c"]);

// 2) Total simpanan seluruh anggota
$totalSimpananAll = 0.0;
$res = $conn->query("SELECT COALESCE(SUM(simpananPokok + simpananWajib + simpananSukarela),0) AS total FROM simpanan");
if ($row = $res->fetch_assoc()) $totalSimpananAll = floatval($row["total"]);

// 3) Total pinjaman aktif semua anggota (status != Lunas)
$totalPinjamanAll = 0.0;
$res = $conn->query("SELECT COALESCE(SUM(jumlah),0) AS total FROM pinjaman WHERE LOWER(status) <> 'lunas'");
if ($row = $res->fetch_assoc()) $totalPinjamanAll = floatval($row["total"]);

// 4) Saldo kas (kas_transaksi: Masuk - Keluar)
$saldoKas = 0.0;
$res = $conn->query("
  SELECT COALESCE(SUM(CASE WHEN jenis='Masuk' THEN jumlah ELSE -jumlah END),0) AS saldo
  FROM kas_transaksi
");
if ($row = $res->fetch_assoc()) $saldoKas = floatval($row["saldo"]);

// 5) List users + total simpanan & total pinjaman aktif per user
$sql = "
SELECT 
  u.kodePegawai,
  u.email,
  u.password,
  u.nama,
  u.statusKeanggotaan,
  COALESCE(s.simpananPokok + s.simpananWajib + s.simpananSukarela,0) AS totalSimpanan,
  COALESCE(p.totalPinjamanAktif,0) AS totalPinjamanAktif
FROM users u
LEFT JOIN simpanan s ON s.kodePegawai = u.kodePegawai
LEFT JOIN (
  SELECT kodePegawai, COALESCE(SUM(jumlah),0) AS totalPinjamanAktif
  FROM pinjaman
  WHERE LOWER(status) <> 'lunas'
  GROUP BY kodePegawai
) p ON p.kodePegawai = u.kodePegawai
ORDER BY u.nama ASC
";
$res = $conn->query($sql);

$users = [];
while ($r = $res->fetch_assoc()) {
  $users[] = [
    "kodePegawai" => $r["kodePegawai"],
    "email" => $r["email"],
    "password" => $r["password"],
    "nama" => $r["nama"],
    "statusKeanggotaan" => $r["statusKeanggotaan"],
    "totalSimpanan" => floatval($r["totalSimpanan"]),
    "totalPinjamanAktif" => floatval($r["totalPinjamanAktif"])
  ];
}

$data = [
  "jumlahAnggota" => $jumlahAnggota,
  "totalSimpananAll" => $totalSimpananAll,
  "totalPinjamanAll" => $totalPinjamanAll,
  "saldoKas" => $saldoKas,
  "users" => $users
];

echo json_encode(["success" => true, "message" => "OK", "data" => $data]);
