<?php
header('Content-Type: application/json; charset=utf-8');

require 'config.php';

// ✅ Paksa MySQLi lempar error jadi keliatan
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

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
$today = date("Y-m-d"); // ✅ aman untuk kolom DATE

try {
  // ✅ cek DB aktif (biar tau connect ke DB mana)
  $dbRes = $conn->query("SELECT DATABASE() AS db");
  $dbRow = $dbRes->fetch_assoc();
  $activeDb = $dbRow["db"] ?? "(unknown)";

  // ambil data pinjaman (buat notif)
  $check = $conn->prepare("SELECT id, kodePegawai, jumlah, status FROM pinjaman WHERE id = ?");
  $check->bind_param("i", $pinjamanId);
  $check->execute();
  $res = $check->get_result();

  if ($res->num_rows === 0) {
    echo json_encode(["success" => false, "message" => "Pinjaman tidak ditemukan", "db" => $activeDb]);
    exit;
  }

  $row = $res->fetch_assoc();
  $kodePegawaiUser = $row["kodePegawai"];
  $jumlahPinjaman  = intval($row["jumlah"]);
  $statusLama      = $row["status"];

  // update status + audit (tetap dilakukan)
  $stmt = $conn->prepare("
    UPDATE pinjaman
    SET status = ?, approved_by = ?, approved_at = ?
    WHERE id = ?
  ");
  $stmt->bind_param("sssi", $newStatus, $adminKode, $now, $pinjamanId);
  $stmt->execute();

  // ✅ JANGAN hentikan proses hanya karena affected_rows=0
  // karena kalau status sama, insert notif sebelumnya jadi tidak jalan.
  // Kalau mau mencegah spam, kita cek: hanya insert jika status berubah.
  $shouldInsert = ($statusLama !== $newStatus);

  $insertedId = null;
  if ($shouldInsert) {
    $ins = $conn->prepare("
      INSERT INTO decision_notifications (kodePegawai, pinjamanId, decision, jumlah, tanggal, is_read)
      VALUES (?, ?, ?, ?, ?, 0)
    ");
    $ins->bind_param("sisis", $kodePegawaiUser, $pinjamanId, $newStatus, $jumlahPinjaman, $today);
    $ins->execute();
    $insertedId = $conn->insert_id;
  }

  echo json_encode([
    "success" => true,
    "message" => $shouldInsert ? "Keputusan pinjaman berhasil + notif dibuat" : "Status sudah sama, notif tidak dibuat",
    "data" => true,
    "debug" => [
      "db" => $activeDb,
      "pinjamanId" => $pinjamanId,
      "kodePegawaiUser" => $kodePegawaiUser,
      "statusLama" => $statusLama,
      "statusBaru" => $newStatus,
      "shouldInsert" => $shouldInsert,
      "insertedId" => $insertedId
    ]
  ]);

} catch (Throwable $e) {
  echo json_encode([
    "success" => false,
    "message" => "Server error: " . $e->getMessage()
  ]);
}
