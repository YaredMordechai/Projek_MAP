<?php
// users_get.php
require 'config.php';

// Jika ada ?kodePegawai=EMP001 -> ambil 1 user
if (isset($_GET['kodePegawai']) && trim($_GET['kodePegawai']) !== '') {
    $kode = trim($_GET['kodePegawai']);

    $stmt = $conn->prepare("SELECT kodePegawai, email, nama, statusKeanggotaan FROM users WHERE kodePegawai = ? LIMIT 1");
    $stmt->bind_param("s", $kode);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($row = $result->fetch_assoc()) {
        send_json([
            "success" => true,
            "data" => $row
        ], 200);
    } else {
        send_json([
            "success" => false,
            "message" => "User tidak ditemukan"
        ], 200);
    }
} else {
    // Kalau tanpa parameter -> ambil semua user
    $result = $conn->query("SELECT kodePegawai, email, nama, statusKeanggotaan FROM users ORDER BY kodePegawai ASC");
    $users = [];
    while ($row = $result->fetch_assoc()) {
        $users[] = $row;
    }

    send_json([
        "success" => true,
        "data" => $users
    ], 200);
}
?>
