<?php
require_once "config.php";

$data = read_json_body();
if (!is_array($data)) send_json(["success"=>false,"message"=>"Body JSON tidak valid"], 400);

$kodePegawai = trim($data["kodePegawai"] ?? "");
$nama  = trim($data["nama"] ?? "");
$email = trim($data["email"] ?? "");

if ($kodePegawai === "" || $nama === "" || $email === "") {
    send_json(["success"=>false,"message"=>"kodePegawai, nama, email wajib diisi"], 400);
}

$stmt = $conn->prepare("UPDATE users SET nama = ?, email = ? WHERE kodePegawai = ?");
if (!$stmt) send_json(["success"=>false,"message"=>$conn->error], 500);

$stmt->bind_param("sss", $nama, $email, $kodePegawai);
$ok = $stmt->execute();

if (!$ok) {
    if ($conn->errno == 1062) send_json(["success"=>false,"message"=>"Email sudah digunakan"], 400);
    send_json(["success"=>false,"message"=>$conn->error], 500);
}

$stmt->close();

send_json(["success"=>true, "data"=>true], 200);
