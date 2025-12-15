<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$raw = file_get_contents("php://input");
$body = json_decode($raw, true);

if (!is_array($body)) {
    echo json_encode(["success"=>false,"message"=>"Body JSON tidak valid","data"=>null]);
    exit;
}

$id    = intval($body["id"] ?? 0);
$judul = trim($body["judul"] ?? "");
$isi   = trim($body["isi"] ?? "");

if ($id <= 0 || $judul === "" || $isi === "") {
    echo json_encode(["success"=>false,"message"=>"id/judul/isi tidak valid","data"=>null]);
    exit;
}

try {
    $stmt = $conn->prepare("UPDATE pengumuman SET judul=?, isi=? WHERE id=?");
    $stmt->bind_param("ssi", $judul, $isi, $id);
    $ok = $stmt->execute();

    echo json_encode([
        "success" => $ok ? true : false,
        "message" => $ok ? "Berhasil" : "Gagal update",
        "data" => $ok ? true : null
    ]);
} catch (Throwable $e) {
    echo json_encode(["success"=>false,"message"=>$e->getMessage(),"data"=>null]);
}
