<?php
// user_register.php
require 'config.php';

// hanya terima POST JSON
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    send_json([
        "success" => false,
        "message" => "Gunakan POST JSON: {\"kodePegawai\":\"EMP003\",\"email\":\"...\",\"password\":\"...\",\"nama\":\"...\"}"
    ], 200);
}

$data = read_json_body();
if (!$data) {
    send_json(["success" => false, "message" => "Body JSON tidak valid"], 200);
}

$kodePegawai = trim($data['kodePegawai'] ?? '');
$email       = trim($data['email'] ?? '');
$password    = trim($data['password'] ?? '');
$nama        = trim($data['nama'] ?? '');

if ($kodePegawai === '' || $email === '' || $password === '' || $nama === '') {
    send_json(["success" => false, "message" => "Semua field wajib diisi"], 200);
}

// cek unik: kodePegawai / email
$cek = $conn->prepare("SELECT 1 FROM users WHERE kodePegawai = ? OR email = ? LIMIT 1");
$cek->bind_param("ss", $kodePegawai, $email);
$cek->execute();
$exists = $cek->get_result()->fetch_assoc();

if ($exists) {
    send_json(["success" => false, "message" => "Kode Pegawai atau Email sudah terdaftar"], 200);
}

// insert user baru
$status = "Anggota Aktif";
$ins = $conn->prepare("INSERT INTO users (kodePegawai, email, password, nama, statusKeanggotaan) VALUES (?,?,?,?,?)");
$ins->bind_param("sssss", $kodePegawai, $email, $password, $nama, $status);

if (!$ins->execute()) {
    send_json(["success" => false, "message" => "Gagal daftar: " . $conn->error], 200);
}

// optional: bikin row simpanan awal biar endpoint simpanan_get aman
$conn->query("INSERT IGNORE INTO simpanan (kodePegawai, simpananPokok, simpananWajib, simpananSukarela)
              VALUES ('$kodePegawai', 0, 0, 0)");

send_json([
    "success" => true,
    "message" => "Pendaftaran berhasil",
    "data" => [
        "kodePegawai" => $kodePegawai,
        "email" => $email,
        "nama" => $nama,
        "statusKeanggotaan" => $status
    ]
], 200);
?>
