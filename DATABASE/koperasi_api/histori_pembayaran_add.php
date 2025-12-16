<?php
header("Content-Type: application/json; charset=UTF-8");
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require_once "config.php";
if (!isset($pdo) || !($pdo instanceof PDO)) {
  http_response_code(500);
  echo json_encode(["success"=>false,"message"=>"PDO tidak terbentuk (cek config.php)"]);
  exit;
}

$raw = file_get_contents("php://input");
$data = json_decode($raw, true);

if (!is_array($data)) {
  echo json_encode(["success"=>false, "message"=>"Body JSON tidak valid"]);
  exit;
}

$kodePegawai = trim($data["kodePegawai"] ?? "");
$pinjamanId  = intval($data["pinjamanId"] ?? 0);
$jumlah      = intval($data["jumlah"] ?? 0);
$status      = trim($data["status"] ?? "Dibayar (Admin)");

$tanggalBody = trim($data["tanggal"] ?? "");
$useTanggal  = ($tanggalBody !== "") ? $tanggalBody : null;

$buktiUri    = $data["buktiPembayaranUri"] ?? null;   // fallback lama
$buktiBase64 = $data["buktiBase64"] ?? null;          // baru
$buktiExt    = $data["buktiExt"] ?? "jpg";            // baru (jpg/png)

if ($kodePegawai === "" || $pinjamanId <= 0 || $jumlah <= 0) {
  echo json_encode(["success"=>false, "message"=>"Data tidak lengkap"]);
  exit;
}

function save_base64_image($base64, $ext) {
  $ext = strtolower(trim($ext));
  if (!in_array($ext, ["jpg","jpeg","png"])) $ext = "jpg";

  // buang prefix "data:image/..;base64," kalau ada
  if (strpos($base64, "base64,") !== false) {
    $base64 = substr($base64, strpos($base64, "base64,") + 7);
  }

  $bin = base64_decode($base64);
  if ($bin === false) return null;

  if (!is_dir("uploads")) mkdir("uploads", 0777, true);

  $name = "pinjaman_" . date("Ymd_His") . "_" . bin2hex(random_bytes(4)) . "." . $ext;
  $path = "uploads/" . $name;

  $ok = file_put_contents($path, $bin);
  if ($ok === false) return null;

  // URL publik (sesuaikan host kalau perlu)
  $scheme = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? "https" : "http";
  $host = $_SERVER['HTTP_HOST'] ?? "localhost";
  $base = rtrim(dirname($_SERVER['SCRIPT_NAME']), "/\\");
  return $scheme . "://" . $host . $base . "/" . $path;
}

try {
  $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

  // kalau ada base64 → simpan file → pakai URL itu
  if (!empty($buktiBase64)) {
    $url = save_base64_image($buktiBase64, $buktiExt);
    if ($url) $buktiUri = $url;
  }

  $pdo->beginTransaction();

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

  if ($useTanggal) {
    $stmt = $pdo->prepare("
      INSERT INTO histori_pembayaran (kodePegawai, pinjamanId, tanggal, jumlah, status, buktiPembayaranUri)
      VALUES (?, ?, ?, ?, ?, ?)
    ");
    $stmt->execute([$kodePegawai, $pinjamanId, $useTanggal, $jumlah, $status, $buktiUri]);
  } else {
    $stmt = $pdo->prepare("
      INSERT INTO histori_pembayaran (kodePegawai, pinjamanId, tanggal, jumlah, status, buktiPembayaranUri)
      VALUES (?, ?, CURDATE(), ?, ?, ?)
    ");
    $stmt->execute([$kodePegawai, $pinjamanId, $jumlah, $status, $buktiUri]);
  }

  $newTerbayar = intval($pinj["angsuranTerbayar"]) + $jumlah;

  $pokok = intval($pinj["jumlah"]);
  $bunga = floatval($pinj["bunga"]);
  $totalTagihan = (int) round($pokok + ($pokok * $bunga));

  $newStatusPinjaman = $pinj["status"];
  if ($newTerbayar >= $totalTagihan) $newStatusPinjaman = "Lunas";

  $stmt = $pdo->prepare("UPDATE pinjaman SET angsuranTerbayar=?, status=? WHERE id=?");
  $stmt->execute([$newTerbayar, $newStatusPinjaman, $pinjamanId]);

  $pdo->commit();
  echo json_encode(["success"=>true, "message"=>"Pembayaran tersimpan", "data"=>true]);
  exit;

} catch (Throwable $e) {
  if ($pdo->inTransaction()) $pdo->rollBack();
  http_response_code(500);
  echo json_encode(["success"=>false, "message"=>$e->getMessage()]);
  exit;
}
