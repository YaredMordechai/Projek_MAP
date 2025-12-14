<?php
// config.php

// === DB Config ===
$host = "localhost";
$user = "root";   // default XAMPP
$pass = "";       // default XAMPP kosong
$db   = "koperasi_db";

// Matikan error HTML ke output (biar JSON gak rusak)
ini_set('display_errors', 0);
error_reporting(E_ALL);

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

// Helper: baca JSON body
function read_json_body() {
    $raw = file_get_contents('php://input');
    if ($raw === false || trim($raw) === '') return null;

    $data = json_decode($raw, true);
    if (json_last_error() !== JSON_ERROR_NONE) return null;

    return $data;
}
?>
