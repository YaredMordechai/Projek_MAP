<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

try {

  // 1) Jumlah anggota
  $jumlahAnggota = 0;
  $res = $conn->query("SELECT COUNT(*) AS c FROM users");
  if (!$res) send_json(["success"=>false, "message"=>"Query users count gagal: ".$conn->error], 500);
  if ($row = $res->fetch_assoc()) $jumlahAnggota = intval($row["c"]);
  $res->free();

  // 2) Total simpanan seluruh anggota
  $totalSimpananAll = 0.0;
  $res = $conn->query("SELECT COALESCE(SUM(simpananPokok + simpananWajib + simpananSukarela),0) AS total FROM simpanan");
  if (!$res) send_json(["success"=>false, "message"=>"Query total simpanan gagal: ".$conn->error], 500);
  if ($row = $res->fetch_assoc()) $totalSimpananAll = floatval($row["total"]);
  $res->free();

  // 3) Total pinjaman aktif semua anggota (status != Lunas)
  $totalPinjamanAll = 0.0;
  $res = $conn->query("SELECT COALESCE(SUM(jumlah),0) AS total FROM pinjaman WHERE LOWER(status) <> 'lunas'");
  if (!$res) send_json(["success"=>false, "message"=>"Query total pinjaman gagal: ".$conn->error], 500);
  if ($row = $res->fetch_assoc()) $totalPinjamanAll = floatval($row["total"]);
  $res->free();

  // 4) Saldo kas (kas_transaksi: Masuk - Keluar)
  $saldoKas = 0.0;
  $res = $conn->query("
    SELECT COALESCE(SUM(CASE WHEN jenis='Masuk' THEN jumlah ELSE -jumlah END),0) AS saldo
    FROM kas_transaksi
  ");
  if (!$res) send_json(["success"=>false, "message"=>"Query saldo kas gagal: ".$conn->error], 500);
  if ($row = $res->fetch_assoc()) $saldoKas = floatval($row["saldo"]);
  $res->free();

  // 5) List users + total simpanan & total pinjaman aktif per user
  $sql = "
  SELECT 
    u.kodePegawai,
    u.email,
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
  if (!$res) send_json(["success"=>false, "message"=>"Query list laporan gagal: ".$conn->error], 500);

  $users = [];
  while ($r = $res->fetch_assoc()) {
    $users[] = [
      "kodePegawai" => $r["kodePegawai"],
      "email" => $r["email"],
      "password" => "", // jangan kirim password
      "nama" => $r["nama"],
      "statusKeanggotaan" => $r["statusKeanggotaan"],
      "totalSimpanan" => floatval($r["totalSimpanan"]),
      "totalPinjamanAktif" => floatval($r["totalPinjamanAktif"])
    ];
  }
  $res->free();

  $data = [
    "jumlahAnggota" => $jumlahAnggota,
    "totalSimpananAll" => $totalSimpananAll,
    "totalPinjamanAll" => $totalPinjamanAll,
    "saldoKas" => $saldoKas,
    "users" => $users
  ];

  send_json(["success" => true, "message" => "OK", "data" => $data], 200);

} catch (Exception $e) {
  send_json(["success"=>false, "message"=>$e->getMessage()], 500);
}
