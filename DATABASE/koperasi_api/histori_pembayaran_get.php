<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

if (!isset($pdo) || !($pdo instanceof PDO)) {
  echo json_encode(["success"=>false, "message"=>"PDO tidak terbentuk", "data"=>[]]);
  exit;
}

$pinjamanId  = isset($_GET["pinjamanId"]) ? intval($_GET["pinjamanId"]) : null;
$kodePegawai = isset($_GET["kodePegawai"]) ? trim($_GET["kodePegawai"]) : null;

try {
  $sql = "
    SELECT
      id,
      kodePegawai,
      pinjamanId,
      DATE_FORMAT(tanggal, '%Y-%m-%d') AS tanggal,
      jumlah,
      status,
      buktiPembayaranUri
    FROM histori_pembayaran
    WHERE 1=1
  ";

  $params = [];

  if ($pinjamanId !== null && $pinjamanId > 0) {
    $sql .= " AND pinjamanId = ?";
    $params[] = $pinjamanId;
  }
  if ($kodePegawai !== null && $kodePegawai !== "") {
    $sql .= " AND kodePegawai = ?";
    $params[] = $kodePegawai;
  }

  $sql .= " ORDER BY tanggal DESC, id DESC";

  $stmt = $pdo->prepare($sql);
  $stmt->execute($params);
  $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

  echo json_encode(["success" => true, "message" => "OK", "data" => $rows]);
} catch (Exception $e) {
  echo json_encode(["success" => false, "message" => $e->getMessage(), "data" => []]);
}
