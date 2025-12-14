<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php';

$raw = file_get_contents("php://input");
$body = json_decode($raw, true);

$ids = $body["ids"] ?? null;
if (!is_array($ids) || count($ids) === 0) {
  send_json(["success" => false, "message" => "ids array is required"]);
  exit;
}

$ids = array_map('intval', $ids);
$placeholders = implode(",", array_fill(0, count($ids), "?"));
$types = str_repeat("i", count($ids));

$sql = "UPDATE decision_notifications SET is_read = 1 WHERE id IN ($placeholders)";
$stmt = $conn->prepare($sql);
$stmt->bind_param($types, ...$ids);
$stmt->execute();

send_json([
  "success" => true,
  "data" => true,
  "message" => "marked as read"
]);
