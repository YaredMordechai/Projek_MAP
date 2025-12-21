<?php
// config.php

// === DB Config ===
$host = "localhost";
$user = "kopa2939_koperasi_user";   // default XAMPP
$pass = "Abl00abl";       // default XAMPP kosong
$db   = "kopa2939_koperasi_db";

// Matikan error HTML ke output (biar JSON gak rusak)
ini_set('display_errors', 0);
error_reporting(E_ALL);

// ✅ Tambahan: port & PDO config (tanpa ganggu yang lain)
$port = 3306; // kalau MySQL kamu bukan 3306, ganti sesuai Laragon/XAMPP
$pdo = null;

// Pastikan header JSON (boleh dipanggil berulang)
function send_json($data, $code = 200) {
    http_response_code($code);
    header('Content-Type: application/json; charset=utf-8');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Headers: Content-Type, Authorization');
    header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
    echo json_encode($data);
    exit;
}

// Handle preflight OPTIONS (umumnya tidak perlu untuk Android, tapi aman)
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    send_json(["success" => true, "message" => "OK"], 200);
}

// Koneksi DB
$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    send_json([
        "success" => false,
        "message" => "Koneksi database gagal: " . $conn->connect_error
    ], 500);
}

mysqli_set_charset($conn, "utf8mb4");

// ✅ Tambahan: Bentuk PDO juga (karena histori_pembayaran_add/get pakai $pdo->prepare)
try {
    $dsn = "mysql:host={$host};port={$port};dbname={$db};charset=utf8mb4";
    $pdo = new PDO($dsn, $user, $pass, [
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES => false,
    ]);
} catch (Exception $e) {
    // biar gak silent: endpoint yang butuh PDO akan dapat error jelas
    // (kalau kamu mau tetap lanjut pakai mysqli-only di endpoint lain, biarkan $pdo null)
    // send_json(["success"=>false,"message"=>"PDO tidak terbentuk: ".$e->getMessage()], 500);
    $pdo = null;
}

// Helper: baca JSON body
function read_json_body() {
    $raw = file_get_contents('php://input');
    if ($raw === false || trim($raw) === '') return null;

    $data = json_decode($raw, true);
    if (json_last_error() !== JSON_ERROR_NONE) return null;

    return $data;
}
?>
