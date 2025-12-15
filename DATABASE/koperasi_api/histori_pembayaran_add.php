<?php
header("Content-Type: application/json; charset=UTF-8");

// tampilkan error (dev only)
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// tangkap fatal error yang tidak masuk try/catch
register_shutdown_function(function () {
    $err = error_get_last();
    if ($err && in_array($err['type'], [E_ERROR, E_PARSE, E_CORE_ERROR, E_COMPILE_ERROR])) {
        if (ob_get_length()) { @ob_clean(); }
        http_response_code(500);
        echo json_encode([
            "success" => false,
            "message" => "FATAL: {$err['message']} in {$err['file']}:{$err['line']}"
        ]);
        exit;
    }
});

require_once "config.php"; // pastikan $pdo benar-benar ada
if (!isset($pdo)) { http_response_code(500); echo json_encode(["success"=>false,"message"=>"PDO tidak terbentuk (cek config.php)"]); exit; }
if (!($pdo instanceof PDO)) { http_response_code(500); echo json_encode(["success"=>false,"message"=>"pdo bukan instance PDO"]); exit; }
  

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
$buktiUri    = $data["buktiPembayaranUri"] ?? null;

$tanggalBody = trim($data["tanggal"] ?? "");
$useTanggal  = ($tanggalBody !== "") ? $tanggalBody : null;

if ($kodePegawai === "" || $pinjamanId <= 0 || $jumlah <= 0) {
    echo json_encode(["success"=>false, "message"=>"Data tidak lengkap"]);
    exit;
}

try {
    // pastikan PDO lempar exception kalau query error
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

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

    // update angsuranTerbayar
    $newTerbayar = intval($pinj["angsuranTerbayar"]) + $jumlah;

    $pokok = intval($pinj["jumlah"]);
    $bunga = floatval($pinj["bunga"]);
    $totalTagihan = (int) round($pokok + ($pokok * $bunga));

    $newStatusPinjaman = $pinj["status"];
    if ($newTerbayar >= $totalTagihan) {
        $newStatusPinjaman = "Lunas";
    }

    $stmt = $pdo->prepare("UPDATE pinjaman SET angsuranTerbayar=?, status=? WHERE id=?");
    $stmt->execute([$newTerbayar, $newStatusPinjaman, $pinjamanId]);

    $pdo->commit();

    echo json_encode(["success"=>true, "message"=>"Pembayaran tersimpan", "data"=>true]);
    exit;

} catch (Throwable $e) {
    if (isset($pdo) && $pdo->inTransaction()) $pdo->rollBack();
    http_response_code(500);
    echo json_encode(["success"=>false, "message"=>$e->getMessage()]);
    exit;
}
