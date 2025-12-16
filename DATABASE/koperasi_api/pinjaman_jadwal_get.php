<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$pinjamanId = isset($_GET["pinjamanId"]) ? intval($_GET["pinjamanId"]) : 0;
if ($pinjamanId <= 0) {
  send_json(["success"=>false, "message"=>"pinjamanId wajib"], 200);
}

$stmt = $conn->prepare("
  SELECT id, pinjamanId, periode,
         DATE_FORMAT(dueDate, '%Y-%m-%d') AS dueDate,
         pokok, bunga, total, sisaPokok,
         isPaid,
         DATE_FORMAT(paidAt, '%Y-%m-%d') AS paidAt
  FROM pinjaman_angsuran_jadwal
  WHERE pinjamanId = ?
  ORDER BY periode ASC
");
$stmt->bind_param("i", $pinjamanId);
$stmt->execute();
$res = $stmt->get_result();

$rows = [];
while ($r = $res->fetch_assoc()) $rows[] = $r;

send_json(["success"=>true, "message"=>"OK", "data"=>$rows], 200);
