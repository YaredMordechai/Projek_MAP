<?php
require_once "config.php";

$data = read_json_body();
if (!is_array($data)) send_json(["success"=>false,"message"=>"Body JSON tidak valid"], 400);

$kodePegawai = trim($data["kodePegawai"] ?? "");
$status      = trim($data["statusKeanggotaan"] ?? "");

if ($kodePegawai === "" || $status === "") {
    send_json(["success"=>false,"message"=>"kodePegawai dan statusKeanggotaan wajib diisi"], 400);
}

// biar konsisten dengan data kamu di DB
$allowed = ["Anggota Aktif", "Anggota Tidak Aktif", "Nonaktif"];
if (!in_array($status, $allowed, true)) {
    send_json(["success"=>false,"message"=>"statusKeanggotaan tidak valid"], 400);
}

$stmt = $conn->prepare("UPDATE users SET statusKeanggotaan = ? WHERE kodePegawai = ?");
if (!$stmt) send_json(["success"=>false,"message"=>$conn->error], 500);

$stmt->bind_param("ss", $status, $kodePegawai);
$ok = $stmt->execute();
if (!$ok) send_json(["success"=>false,"message"=>$conn->error], 500);

$stmt->close();

send_json(["success"=>true, "data"=>true], 200);
