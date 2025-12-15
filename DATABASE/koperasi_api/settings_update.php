<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$data = read_json_body();
if (!is_array($data)) {
  echo json_encode(["success"=>false, "message"=>"Body JSON tidak valid"]);
  exit;
}

$bunga = isset($data["bungaPersen"]) ? (double)$data["bungaPersen"] : null;
$denda = isset($data["dendaPersenPerHari"]) ? (double)$data["dendaPersenPerHari"] : null;

if ($bunga === null || $denda === null) {
  echo json_encode(["success"=>false, "message"=>"Data tidak lengkap"]);
  exit;
}

try {
  // upsert id=1
  $stmt = $conn->prepare("
    INSERT INTO settings (id, bungaPersen, dendaPersenPerHari)
    VALUES (1, ?, ?)
    ON DUPLICATE KEY UPDATE bungaPersen=VALUES(bungaPersen), dendaPersenPerHari=VALUES(dendaPersenPerHari)
  ");
  $stmt->bind_param("dd", $bunga, $denda);

  if (!$stmt->execute()) {
    echo json_encode(["success"=>false, "message"=>"Gagal update: ".$stmt->error]);
    exit;
  }

  echo json_encode(["success"=>true, "message"=>"Pengaturan tersimpan", "data"=>true]);
} catch (Exception $e) {
  echo json_encode(["success"=>false, "message"=>$e->getMessage()]);
}
