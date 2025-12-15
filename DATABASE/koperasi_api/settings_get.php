<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

try {
  // Pake mysqli dari config.php
  $sql = "SELECT bungaPersen, dendaPersenPerHari FROM settings WHERE id=1 LIMIT 1";
  $res = $conn->query($sql);

  if (!$res) {
    echo json_encode(["success"=>false, "message"=>"Query gagal: ".$conn->error]);
    exit;
  }

  $row = $res->fetch_assoc();
  if (!$row) {
    echo json_encode(["success"=>true, "data"=>["bungaPersen"=>10, "dendaPersenPerHari"=>1]]);
    exit;
  }

  echo json_encode([
    "success" => true,
    "data" => [
      "bungaPersen" => (double)$row["bungaPersen"],
      "dendaPersenPerHari" => (double)$row["dendaPersenPerHari"]
    ]
  ]);
} catch (Exception $e) {
  echo json_encode(["success"=>false, "message"=>$e->getMessage()]);
}
