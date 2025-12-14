<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$raw = file_get_contents("php://input");
$data = json_decode($raw, true);

if (!is_array($data) || empty($data["pinjamanId"]) || empty($data["status"])) {
  echo json_encode(["success"=>false, "message"=>"Body JSON tidak valid"]);
  exit;
}

$pinjamanId = intval($data["pinjamanId"]);
$status = trim($data["status"]);

try {
  $stmt = $pdo->prepare("UPDATE pinjaman SET status=? WHERE id=?");
  $stmt->execute([$status, $pinjamanId]);

  if ($stmt->rowCount() <= 0) {
    echo json_encode(["success"=>false, "message"=>"Pinjaman tidak ditemukan / status sama"]);
    exit;
  }

  echo json_encode(["success"=>true, "message"=>"Status pinjaman berhasil diupdate"]);
} catch (Exception $e) {
  echo json_encode(["success"=>false, "message"=>$e->getMessage()]);
}
