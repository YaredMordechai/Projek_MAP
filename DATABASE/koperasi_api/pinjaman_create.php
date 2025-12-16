<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$raw = file_get_contents("php://input");
$data = json_decode($raw, true);

if (!$data) {
  echo json_encode(["success"=>false, "message"=>"Body JSON tidak valid"]);
  exit;
}

$kodePegawai = $data["kodePegawai"] ?? "";
$jumlah = intval($data["jumlah"] ?? 0);
$tenor  = intval($data["tenor"] ?? 0);

if ($kodePegawai=="" || $jumlah<=0 || $tenor<=0) {
  echo json_encode(["success"=>false, "message"=>"Data tidak lengkap"]);
  exit;
}

try {
  // status awal proses, bunga default 0.1 sudah di table schema
  $stmt = $conn->prepare("INSERT INTO pinjaman (kodePegawai, jumlah, tenor, status, bunga, angsuranTerbayar)
                          VALUES (?, ?, ?, 'Proses', 0.10, 0)");
  $stmt->bind_param("sii", $kodePegawai, $jumlah, $tenor);

  if (!$stmt->execute()) {
    echo json_encode(["success"=>false, "message"=>"Gagal insert pinjaman: ".$stmt->error]);
    exit;
  }

  $newId = $stmt->insert_id;

  // ambil row yang baru dibuat
  $stmt2 = $conn->prepare("SELECT id, kodePegawai, jumlah, tenor, status, bunga, angsuranTerbayar
                           FROM pinjaman WHERE id=?");
  $stmt2->bind_param("i", $newId);
  $stmt2->execute();
  $res = $stmt2->get_result();
  $row = $res->fetch_assoc();

  echo json_encode(["success"=>true, "message"=>"Pinjaman berhasil diajukan", "data"=>$row]);

} catch (Exception $e) {
  echo json_encode(["success"=>false, "message"=>$e->getMessage()]);
}
