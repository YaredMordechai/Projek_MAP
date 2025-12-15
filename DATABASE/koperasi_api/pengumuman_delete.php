<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$raw = file_get_contents("php://input");
$body = json_decode($raw, true);

if (!is_array($body)) {
    echo json_encode(["success"=>false,"message"=>"Body JSON tidak valid","data"=>null]);
    exit;
}

$id = intval($body["id"] ?? 0);
if ($id <= 0) {
    echo json_encode(["success"=>false,"message"=>"id tidak valid","data"=>null]);
    exit;
}

try {
    $stmt = $conn->prepare("DELETE FROM pengumuman WHERE id=?");
    $stmt->bind_param("i", $id);
    $ok = $stmt->execute();

    echo json_encode([
        "success" => $ok ? true : false,
        "message" => $ok ? "Berhasil" : "Gagal delete",
        "data" => $ok ? true : null
    ]);
} catch (Throwable $e) {
    echo json_encode(["success"=>false,"message"=>$e->getMessage(),"data"=>null]);
}
