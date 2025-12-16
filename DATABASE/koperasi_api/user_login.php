<?php
// user_login.php
require 'config.php';

// Kalau dibuka via browser (GET), kasih info jelas
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    send_json([
        "success" => false,
        "message" => "Gunakan POST dengan JSON body: {\"email\":\"EMP001 atau email\",\"password\":\"1234\"}"
    ], 200);
}

$data = read_json_body();
if (!$data) {
    send_json([
        "success" => false,
        "message" => "Body JSON tidak valid"
    ], 200);
}

// Field input di Android kamu: "Kode Pegawai / Email" -> kita pakai key 'email' sesuai Retrofit
$input    = trim($data['email'] ?? '');
$password = trim($data['password'] ?? '');

if ($input === '' || $password === '') {
    send_json([
        "success" => false,
        "message" => "Email/Kode Pegawai dan password wajib diisi"
    ], 200);
}

// Login bisa pakai email ATAU kodePegawai
$stmt = $conn->prepare("
    SELECT kodePegawai, email, nama, statusKeanggotaan
    FROM users
    WHERE (email = ? OR kodePegawai = ?) AND password = ?
    LIMIT 1
");
$stmt->bind_param("sss", $input, $input, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($user = $result->fetch_assoc()) {
    send_json([
        "success" => true,
        "message" => "Login berhasil",
        "data" => $user
    ], 200);
} else {
    // Jangan pakai 401 biar app gampang handle
    send_json([
        "success" => false,
        "message" => "Email/Kode Pegawai atau password salah"
    ], 200);
}
?>
