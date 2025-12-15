<?php
require_once "config.php";

$data = read_json_body();
if (!is_array($data)) send_json(["success"=>false,"message"=>"Body JSON tidak valid"], 400);

$id = intval($data["id"] ?? 0);
if ($id <= 0) send_json(["success"=>false,"message"=>"id tidak valid"], 400);

$stmt = $conn->prepare("DELETE FROM kas_transaksi WHERE id=?");
if (!$stmt) send_json(["success"=>false,"message"=>$conn->error], 500);

$stmt->bind_param("i", $id);
$ok = $stmt->execute();
if (!$ok) send_json(["success"=>false,"message"=>$stmt->error], 500);

$stmt->close();

send_json(["success"=>true,"data"=>true], 200);
