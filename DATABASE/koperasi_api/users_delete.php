<?php
require_once "config.php";

$data = read_json_body();
if (!is_array($data)) send_json(["success"=>false,"message"=>"Body JSON tidak valid"], 400);

$kodePegawai = trim($data["kodePegawai"] ?? "");
if ($kodePegawai === "") send_json(["success"=>false,"message"=>"kodePegawai wajib diisi"], 400);

$stmt = $conn->prepare("DELETE FROM users WHERE kodePegawai = ?");
if (!$stmt) send_json(["success"=>false,"message"=>$conn->error], 500);

$stmt->bind_param("s", $kodePegawai);
$ok = $stmt->execute();
if (!$ok) send_json(["success"=>false,"message"=>$conn->error], 500);

$stmt->close();

send_json(["success"=>true, "data"=>true], 200);
