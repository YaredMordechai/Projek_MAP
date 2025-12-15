<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$raw = file_get_contents("php://input");
$body = json_decode($raw, true);

if (!is_array($body)) {
    echo json_encode(["success"=>false,"message"=>"Body JSON tidak valid","data"=>null]);
    exit;
}

$judul = trim($body["judul"] ?? "");
$isi   = trim($body["isi"] ?? "");

if ($judul === "" || $isi === "") {
    echo json_encode(["success"=>false,"message"=>"judul/isi wajib diisi","data"=>null]);
    exit;
}

try {
    $stmt = $conn->prepare("INSERT INTO pengumuman (judul, isi, tanggal) VALUES (?, ?, CURDATE())");
    $stmt->bind_param("ss", $judul, $isi);
    $ok = $stmt->execute();

    if (!$ok) {
        echo json_encode(["success"=>false,"message"=>"Gagal insert","data"=>null]);
        exit;
    }

    $newId = $conn->insert_id;

    $stmt2 = $conn->prepare("SELECT id, judul, isi, DATE_FORMAT(tanggal, '%Y-%m-%d') AS tanggal FROM pengumuman WHERE id=?");
    $stmt2->bind_param("i", $newId);
    $stmt2->execute();
    $data = $stmt2->get_result()->fetch_assoc();

    echo json_encode(["success"=>true,"message"=>"Berhasil","data"=>$data]);
} catch (Throwable $e) {
    echo json_encode(["success"=>false,"message"=>$e->getMessage(),"data"=>null]);
}
