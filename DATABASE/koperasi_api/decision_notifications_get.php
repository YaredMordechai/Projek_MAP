<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php';

$kode = $_GET['kodePegawai'] ?? null;
$onlyUnread = $_GET['onlyUnread'] ?? "1"; // default 1

if (!$kode) {
  send_json(["success" => false, "message" => "kodePegawai is required"]);
  exit;
}

if ($onlyUnread === "1") {
  $stmt = $conn->prepare("
    SELECT id, kodePegawai, pinjamanId, decision, jumlah, tanggal, is_read
    FROM decision_notifications
    WHERE kodePegawai = ? AND is_read = 0
    ORDER BY id DESC
  ");
  $stmt->bind_param("s", $kode);
} else {
  $stmt = $conn->prepare("
    SELECT id, kodePegawai, pinjamanId, decision, jumlah, tanggal, is_read
    FROM decision_notifications
    WHERE kodePegawai = ?
    ORDER BY id DESC
  ");
  $stmt->bind_param("s", $kode);
}

$stmt->execute();
$result = $stmt->get_result();

$list = [];
while ($row = $result->fetch_assoc()) {
  $list[] = $row;
}

send_json([
  "success" => true,
  "data" => $list
]);
