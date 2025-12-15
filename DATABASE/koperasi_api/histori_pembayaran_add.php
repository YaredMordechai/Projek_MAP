<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "config.php";

$raw = file_get_contents("php://input");
$data = json_decode($raw, true);

if (!is_array($data)) {
  echo json_encode(["success"=>false, "message"=>"Body JSON tidak valid"]);
  exit;
}

$kodePegawai = trim($data["kodePegawai"] ?? "");
$pinjamanId  = intval($data["pinjamanId"] ?? 0);
$jumlah      = intval($data["jumlah"] ?? 0);

// ✅ status histori (bukan status pinjaman)
$status      = trim($data["status"] ?? "Dibayar (Admin)");
$buktiUri    = $data["buktiPembayaranUri"] ?? null;

// ✅ optional tanggal dari body, kalau tidak ada pakai CURDATE()
$tanggalBody = trim($data["tanggal"] ?? "");
$useTanggal  = ($tanggalBody !== "") ? $tanggalBody : null;

if ($kodePegawai === "" || $pinjamanId <= 0 || $jumlah <= 0) {
  echo json_encode(["success"=>false, "message"=>"Data tidak lengkap"]);
  exit;
}

try {
  $pdo->beginTransaction();

  // cek pinjaman
  $stmt = $pdo->prepare("SELECT id, kodePegawai, jumlah, bunga, angsuranTerbayar, status FROM pinjaman WHERE id=?");
  $stmt->execute([$pinjamanId]);
  $pinj = $stmt->fetch(PDO::FETCH_ASSOC);

  if (!$pinj) {
    $pdo->rollBack();
    echo json_encode(["success"=>false, "message"=>"Pinjaman tidak ditemukan"]);
    exit;
  }
  if (strcasecmp($pinj["kodePegawai"], $kodePegawai) !== 0) {
    $pdo->rollBack();
    echo json_encode(["success"=>false, "message"=>"Pinjaman bukan milik kodePegawai tsb"]);
    exit;
  }

  // insert histori
  if ($useTanggal) {
    $stmt = $pdo->prepare("INSERT INTO histori_pembayaran (kodePegawai, pinjamanId, tanggal, jumlah, status, buktiPembayaranUri)
                           VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->execute([$kodePegawai, $pinjamanId, $useTanggal, $jumlah, $status, $buktiUri]);
  } else {
    $stmt = $pdo->prepare("INSERT INTO histori_pembayaran (kodePegawai, pinjamanId, tanggal, jumlah, status, buktiPembayaranUri)
                           VALUES (?, ?, CURDATE(), ?, ?, ?)");
    $stmt->execute([$kodePegawai, $pinjamanId, $jumlah, $status, $buktiUri]);
  }

  // update angsuranTerbayar
  $newTerbayar = intval($pinj["angsuranTerbayar"]) + $jumlah;

  // total tagihan (simple): pokok + (pokok * bunga)
  $pokok = intval($pinj["jumlah"]);
  $bunga = floatval($pinj["bunga"]);
  $totalTagihan = (int) round($pokok + ($pokok * $bunga));

  $newStatusPinjaman = $pinj["status"];
  if ($newTerbayar >= $totalTagihan) {
    $newStatusPinjaman = "Lunas";
  } else {
    // opsional: kalau pinjaman sebelumnya "Disetujui" tetap, kalau mau set "Aktif" juga bisa di sini
    // $newStatusPinjaman = $pinj["status"];
  }

  $stmt = $pdo->prepare("UPDATE pinjaman SET angsuranTerbayar=?, status=? WHERE id=?");
  $stmt->execute([$newTerbayar, $newStatusPinjaman, $pinjamanId]);

  $pdo->commit();
  echo json_encode(["success"=>true, "message"=>"Pembayaran tersimpan", "data"=>true]);
} catch (Exception $e) {
  if ($pdo->inTransaction()) $pdo->rollBack();
  echo json_encode(["success"=>false, "message"=>$e->getMessage()]);
}
