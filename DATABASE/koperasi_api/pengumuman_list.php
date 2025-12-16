<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

try {
    $sql = "SELECT id, judul, isi, DATE_FORMAT(tanggal, '%Y-%m-%d') AS tanggal
            FROM pengumuman
            ORDER BY tanggal DESC, id DESC";
    $res = $conn->query($sql);

    $rows = [];
    if ($res) {
        while ($r = $res->fetch_assoc()) $rows[] = $r;
    }

    echo json_encode([
        "success" => true,
        "message" => "OK",
        "data" => $rows
    ]);
} catch (Throwable $e) {
    echo json_encode([
        "success" => false,
        "message" => $e->getMessage(),
        "data" => null
    ]);
}
