<?php
// admin_login.php
require 'config.php';

// Wajib POST JSON
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    send_json([
        "success" => false,
        "message" => "Method tidak valid. Gunakan POST"
    ], 200);
}

$body = read_json_body();
if ($body === null) {
    send_json([
        "success" => false,
        "message" => "Body JSON tidak valid",
        "data" => null
    ], 200);
}

$input = trim($body['email'] ?? '');      // dari Android: field-nya "email" (kadang isinya ADM001)
$pass  = trim($body['password'] ?? '');

if ($input === '' || $pass === '') {
    send_json([
        "success" => false,
        "message" => "Email/Kode Pegawai dan password wajib diisi",
        "data" => null
    ], 200);
}

// Cari admin berdasarkan kodePegawai ATAU email
$stmt = $conn->prepare("SELECT kodePegawai, email, password, nama, role FROM admins WHERE kodePegawai = ? OR email = ? LIMIT 1");
$stmt->bind_param("ss", $input, $input);
$stmt->execute();
$res = $stmt->get_result();

if (!$row = $res->fetch_assoc()) {
    send_json([
        "success" => false,
        "message" => "Admin tidak ditemukan",
        "data" => null
    ], 200);
}

// Cocokkan password (sesuai dump SQL kamu: password admin masih plain, contoh admin123)
if ($row['password'] !== $pass) {
    send_json([
        "success" => false,
        "message" => "Password salah",
        "data" => null
    ], 200);
}

// Jangan kirim password ke app
unset($row['password']);

send_json([
    "success" => true,
    "message" => "Login admin berhasil",
    "data" => $row
], 200);
