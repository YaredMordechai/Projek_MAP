<?php
header('Content-Type: application/json; charset=utf-8');

require 'config.php';

$raw = file_get_contents("php://input");
$body = json_decode($raw, true);

if (!$body) {
  echo json_encode(["success" => false, "message" => "Invalid JSON body"]);
  exit;
}

$pinjamanId = isset($body["pinjamanId"]) ? intval($body["pinjamanId"]) : 0;
$decision   = isset($body["decision"]) ? strtolower(trim($body["decision"])) : "";
$adminKode  = isset($body["adminKode"]) ? trim($body["adminKode"]) : "";

if ($pinjamanId <= 0 || $adminKode === "" || !in_array($decision, ["approve", "reject"])) {
  echo json_encode(["success" => false, "message" => "Missing/invalid fields"]);
  exit;
}

$newStatus = ($decision === "approve") ? "Disetujui" : "Ditolak";
$now = date("Y-m-d H:i:s");

try {
  // ambil data pinjaman (buat notif)
  $check = $conn->prepare("SELECT id, kodePegawai, jumlah, status FROM pinjaman WHERE id = ?");
  $check->bind_param("i", $pinjamanId);
  $check->execute();
  $res = $check->get_result();

  if ($res->num_rows === 0) {
    echo json_encode(["success" => false, "message" => "Pinjaman tidak ditemukan"]);
    exit;
  }

  $row = $res->fetch_assoc();
  $kodePegawaiUser = $row["kodePegawai"];
  $jumlahPinjaman  = intval($row["jumlah"]);

  // update status + audit
  $stmt = $conn->prepare("
    UPDATE pinjaman
    SET status = ?, approved_by = ?, approved_at = ?
    WHERE id = ?
  ");
  $stmt->bind_param("sssi", $newStatus, $adminKode, $now, $pinjamanId);
  $stmt->execute();

  if ($stmt->affected_rows <= 0) {
    echo json_encode(["success" => false, "message" => "Tidak ada perubahan"]);
    exit;
  }

  // insert notifikasi keputusan
  $ins = $conn->prepare("
    INSERT INTO decision_notifications (kodePegawai, pinjamanId, decision, jumlah, tanggal, is_read)
    VALUES (?, ?, ?, ?, ?, 0)
  ");
  // types: s (kodePegawai), i (pinjamanId), s (decision), i (jumlah), s (tanggal)
  $ins->bind_param("sisis", $kodePegawaiUser, $pinjamanId, $newStatus, $jumlahPinjaman, $now);
  $ins->execute();

  echo json_encode([
    "success" => true,
    "message" => "Keputusan pinjaman berhasil",
    "data" => true
  ]);

} catch (Exception $e) {
  echo json_encode([
    "success" => false,
    "message" => "Server error: " . $e->getMessage()
  ]);
}
