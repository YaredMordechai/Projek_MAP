<?php
require 'config.php';
header('Content-Type: application/json; charset=UTF-8');

$raw = file_get_contents("php://input");
$body = json_decode($raw, true);

if (!is_array($body)) {
  send_json(["success"=>false,"message"=>"Body JSON tidak valid"], 200);
}

$kodePegawai = trim($body["kodePegawai"] ?? "");
$jenisInput  = trim($body["jenisInput"] ?? "Simpanan Sukarela");
$jumlah      = (float)($body["jumlah"] ?? 0);
$keterangan  = trim($body["keterangan"] ?? "-");

$buktiBase64 = $body["buktiBase64"] ?? null;   // baru
$buktiExt    = $body["buktiExt"] ?? "jpg";     // baru

if ($kodePegawai === "" || $jumlah == 0) {
  send_json(["success"=>false,"message"=>"Data tidak lengkap"], 200);
}

function save_base64_image($base64, $ext) {
  $ext = strtolower(trim($ext));
  if (!in_array($ext, ["jpg","jpeg","png"])) $ext = "jpg";

  if (strpos($base64, "base64,") !== false) {
    $base64 = substr($base64, strpos($base64, "base64,") + 7);
  }

  $bin = base64_decode($base64);
  if ($bin === false) return null;

  if (!is_dir("uploads")) mkdir("uploads", 0777, true);

  $name = "simpanan_" . date("Ymd_His") . "_" . bin2hex(random_bytes(4)) . "." . $ext;
  $path = "uploads/" . $name;

  $ok = file_put_contents($path, $bin);
  if ($ok === false) return null;

  $scheme = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? "https" : "http";
  $host = $_SERVER['HTTP_HOST'] ?? "localhost";
  $base = rtrim(dirname($_SERVER['SCRIPT_NAME']), "/\\");
  return $scheme . "://" . $host . $base . "/" . $path;
}

try {
  $conn->begin_transaction();

  // =========
  // RULE:
  // - Jika SETOR (+) dari anggota → WAJIB bukti → masuk simpanan_pending (Menunggu Verifikasi)
  // - Jika TARIK (-) → tetap proses seperti biasa (tanpa bukti)
  // =========

  if ($jumlah > 0) {
    if (empty($buktiBase64)) {
      $conn->rollback();
      send_json(["success"=>false,"message"=>"Setoran simpanan wajib upload bukti pembayaran"], 200);
    }

    $url = save_base64_image($buktiBase64, $buktiExt);
    if (!$url) {
      $conn->rollback();
      send_json(["success"=>false,"message"=>"Gagal simpan file bukti ke server"], 200);
    }

    $stmt = $conn->prepare("
      INSERT INTO simpanan_pending (kodePegawai, jenisInput, jumlah, keterangan, tanggal, statusVerifikasi, buktiUrl)
      VALUES (?, ?, ?, ?, CURDATE(), 'Menunggu Verifikasi', ?)
    ");
    $stmt->bind_param("ssdss", $kodePegawai, $jenisInput, $jumlah, $keterangan, $url);
    $stmt->execute();

    $conn->commit();

    // NOTE: saldo belum berubah sampai diverifikasi admin
    send_json([
      "success"=>true,
      "message"=>"Setoran dikirim. Menunggu verifikasi admin.",
      "data"=>null
    ], 200);
  }

  // ===== TARIK (jumlah < 0) tetap cara lama =====
  $col = "simpananSukarela";
  $label = "Penarikan Sukarela";

  if (stripos($jenisInput, "Pokok") !== false) { $col = "simpananPokok"; $label = "Penarikan Pokok"; }
  else if (stripos($jenisInput, "Wajib") !== false) { $col = "simpananWajib"; $label = "Penarikan Wajib"; }

  $stmt = $conn->prepare("SELECT $col FROM simpanan WHERE kodePegawai = ? FOR UPDATE");
  $stmt->bind_param("s", $kodePegawai);
  $stmt->execute();
  $saldo = 0.0;
  $res = $stmt->get_result()->fetch_assoc();
  if ($res) $saldo = (float)$res[$col];

  if (($saldo + $jumlah) < 0) {
    $conn->rollback();
    send_json(["success"=>false,"message"=>"Saldo tidak cukup untuk penarikan"], 200);
  }

  $stmt = $conn->prepare("UPDATE simpanan SET $col = $col + ? WHERE kodePegawai = ?");
  $stmt->bind_param("ds", $jumlah, $kodePegawai);
  $stmt->execute();

  $stmt = $conn->prepare("
    INSERT INTO histori_simpanan (kodePegawai, tanggal, jenis, jumlah, keterangan)
    VALUES (?, CURDATE(), ?, ?, ?)
  ");
  $stmt->bind_param("ssds", $kodePegawai, $label, $jumlah, $keterangan);
  $stmt->execute();

  $stmt = $conn->prepare("SELECT * FROM simpanan WHERE kodePegawai = ? LIMIT 1");
  $stmt->bind_param("s", $kodePegawai);
  $stmt->execute();
  $row = $stmt->get_result()->fetch_assoc();

  $conn->commit();

  send_json(["success"=>true,"message"=>"Penarikan simpanan berhasil","data"=>$row], 200);

} catch (Exception $e) {
  $conn->rollback();
  send_json(["success"=>false,"message"=>"Gagal transaksi simpanan: ".$e->getMessage()], 200);
}
