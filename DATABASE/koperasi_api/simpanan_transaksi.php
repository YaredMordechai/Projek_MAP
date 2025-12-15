<?php
// simpanan_transaksi.php
require 'config.php';


$body = read_json_body();
if (!is_array($body)) {
    send_json(["success"=>false, "message"=>"Body JSON tidak valid"], 200);
}

$kodePegawai = trim($body["kodePegawai"] ?? "");
$jenisInput  = trim($body["jenisInput"] ?? ""); // Simpanan Pokok/Wajib/Sukarela
$jumlah      = (float)($body["jumlah"] ?? 0);
$keterangan  = trim($body["keterangan"] ?? "-");

if ($kodePegawai === "" || $jenisInput === "" || $jumlah == 0.0) {
    send_json(["success"=>false, "message"=>"kodePegawai, jenisInput, jumlah wajib"], 200);
}

// ===== mapping jenis =====
$jenisLower = strtolower($jenisInput);

if (strpos($jenisLower, "pokok") !== false) {
    $col = "simpananPokok";
    $label = ($jumlah >= 0) ? "Setoran Pokok" : "Penarikan Pokok";
} elseif (strpos($jenisLower, "wajib") !== false) {
    $col = "simpananWajib";
    $label = ($jumlah >= 0) ? "Setoran Wajib" : "Penarikan Wajib";
} else {
    $col = "simpananSukarela";
    $label = ($jumlah >= 0) ? "Setoran Sukarela" : "Penarikan Sukarela";
}

$conn->begin_transaction();

try {
    // pastikan row simpanan ada
    $stmt = $conn->prepare("
        INSERT INTO simpanan (kodePegawai, simpananPokok, simpananWajib, simpananSukarela)
        VALUES (?,0,0,0)
        ON DUPLICATE KEY UPDATE kodePegawai = kodePegawai
    ");
    $stmt->bind_param("s", $kodePegawai);
    $stmt->execute();

    // ambil saldo sekarang (LOCK)
    $stmt = $conn->prepare("SELECT $col FROM simpanan WHERE kodePegawai = ? FOR UPDATE");
    $stmt->bind_param("s", $kodePegawai);
    $stmt->execute();
    $saldo = (float)$stmt->get_result()->fetch_assoc()[$col];

    // validasi tarik
    if ($jumlah < 0 && ($saldo + $jumlah) < 0) {
        $conn->rollback();
        send_json([
            "success" => false,
            "message" => "Saldo tidak cukup untuk penarikan"
        ], 200);
    }

    // update saldo
    $stmt = $conn->prepare("UPDATE simpanan SET $col = $col + ? WHERE kodePegawai = ?");
    $stmt->bind_param("ds", $jumlah, $kodePegawai);
    $stmt->execute();

    // insert histori
    $stmt = $conn->prepare("
        INSERT INTO histori_simpanan (kodePegawai, tanggal, jenis, jumlah, keterangan)
        VALUES (?, CURDATE(), ?, ?, ?)
    ");
    $stmt->bind_param("ssds", $kodePegawai, $label, $jumlah, $keterangan);
    $stmt->execute();

    // ambil saldo terbaru
    $stmt = $conn->prepare("SELECT * FROM simpanan WHERE kodePegawai = ? LIMIT 1");
    $stmt->bind_param("s", $kodePegawai);
    $stmt->execute();
    $row = $stmt->get_result()->fetch_assoc();

    $conn->commit();

    send_json([
        "success" => true,
        "message" => "Transaksi simpanan berhasil",
        "data" => $row
    ], 200);

} catch (Exception $e) {
    $conn->rollback();
    send_json([
        "success" => false,
        "message" => "Gagal transaksi simpanan: ".$e->getMessage()
    ], 200);
}
