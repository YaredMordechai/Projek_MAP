<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$raw = file_get_contents("php://input");
$body = json_decode($raw, true);

$id = intval($body["id"] ?? 0);
$action = strtolower(trim($body["action"] ?? "")); // approve|reject

if ($id <= 0 || !in_array($action, ["approve","reject"])) {
  echo json_encode(["success"=>false, "message"=>"Invalid fields"]);
  exit;
}

try {
  $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
  $pdo->beginTransaction();

  // ambil histori
  $st = $pdo->prepare("SELECT * FROM histori_pembayaran WHERE id=? FOR UPDATE");
  $st->execute([$id]);
  $h = $st->fetch(PDO::FETCH_ASSOC);

  if (!$h) { $pdo->rollBack(); echo json_encode(["success"=>false,"message"=>"Histori tidak ditemukan"]); exit; }

  $statusNow = strtolower(trim($h["status"] ?? ""));
  if (strpos($statusNow, "menunggu") === false) {
    // sudah diproses sebelumnya
    $pdo->rollBack();
    echo json_encode(["success"=>false,"message"=>"Histori sudah diproses"]);
    exit;
  }

  if ($action === "reject") {
    $st = $pdo->prepare("UPDATE histori_pembayaran SET status='Ditolak' WHERE id=?");
    $st->execute([$id]);
    $pdo->commit();
    echo json_encode(["success"=>true,"message"=>"Pembayaran ditolak","data"=>true]);
    exit;
  }

  // approve:
  $pinjamanId = intval($h["pinjamanId"]);
  $kodePegawai = $h["kodePegawai"];
  $jumlahBayar = intval($h["jumlah"]);

  // update status histori dulu
  $st = $pdo->prepare("UPDATE histori_pembayaran SET status='Dibayar (Verified)' WHERE id=?");
  $st->execute([$id]);

  // ambil pinjaman
  $st = $pdo->prepare("SELECT id, kodePegawai, jumlah, bunga, angsuranTerbayar, status FROM pinjaman WHERE id=? FOR UPDATE");
  $st->execute([$pinjamanId]);
  $p = $st->fetch(PDO::FETCH_ASSOC);
  if (!$p) { $pdo->rollBack(); echo json_encode(["success"=>false,"message"=>"Pinjaman tidak ditemukan"]); exit; }
  if (strcasecmp($p["kodePegawai"], $kodePegawai) !== 0) { $pdo->rollBack(); echo json_encode(["success"=>false,"message"=>"Kode pegawai tidak cocok"]); exit; }

  $newTerbayar = intval($p["angsuranTerbayar"]) + $jumlahBayar;

  $pokok = intval($p["jumlah"]);
  $bunga = floatval($p["bunga"]);
  $totalTagihan = (int) round($pokok + ($pokok * $bunga));

  $newStatusPinjaman = $p["status"];
  if ($newTerbayar >= $totalTagihan) $newStatusPinjaman = "Lunas";

  $st = $pdo->prepare("UPDATE pinjaman SET angsuranTerbayar=?, status=? WHERE id=?");
  $st->execute([$newTerbayar, $newStatusPinjaman, $pinjamanId]);

  $pdo->commit();
  echo json_encode(["success"=>true,"message"=>"Pembayaran disetujui","data"=>true]);

} catch (Throwable $e) {
  if ($pdo->inTransaction()) $pdo->rollBack();
  http_response_code(500);
  echo json_encode(["success"=>false,"message"=>$e->getMessage()]);
}
