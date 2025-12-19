<?php
require 'config.php';
header('Content-Type: application/json; charset=UTF-8');

$kode = isset($_GET["kodePegawai"]) ? trim($_GET["kodePegawai"]) : "";

try {

  if ($kode !== "") {
    // ✅ Per user + hanya yang masih pending
    $stmt = $conn->prepare("
      SELECT id, kodePegawai, jenisInput, jumlah, keterangan,
             DATE_FORMAT(tanggal, '%Y-%m-%d') AS tanggal,
             statusVerifikasi, buktiUrl
      FROM simpanan_pending
      WHERE kodePegawai = ?
        AND statusVerifikasi = 'Menunggu Verifikasi'
      ORDER BY tanggal DESC, id DESC
    ");
    $stmt->bind_param("s", $kode);
  } else {
    // ✅ Admin (all) + hanya yang masih pending
    $stmt = $conn->prepare("
      SELECT id, kodePegawai, jenisInput, jumlah, keterangan,
             DATE_FORMAT(tanggal, '%Y-%m-%d') AS tanggal,
             statusVerifikasi, buktiUrl
      FROM simpanan_pending
      WHERE statusVerifikasi = 'Menunggu Verifikasi'
      ORDER BY tanggal DESC, id DESC
    ");
  }

  $stmt->execute();
  $res = $stmt->get_result();

  $rows = [];
  while ($r = $res->fetch_assoc()) {
    $r["id"] = (int)$r["id"];
    $r["jumlah"] = (float)$r["jumlah"];
    $rows[] = $r;
  }

  send_json(["success" => true, "message" => "OK", "data" => $rows], 200);

} catch (Exception $e) {
  send_json(["success" => false, "message" => $e->getMessage(), "data" => []], 200);
}
