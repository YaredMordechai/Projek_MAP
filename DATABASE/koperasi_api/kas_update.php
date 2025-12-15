<?php
require_once "config.php";

$data = read_json_body();
if (!is_array($data)) send_json(["success"=>false,"message"=>"Body JSON tidak valid"], 400);

$id        = intval($data["id"] ?? 0);
$tanggal   = trim($data["tanggal"] ?? "");
$jenis     = trim($data["jenis"] ?? "");
$kategori  = trim($data["kategori"] ?? "");
$deskripsi = trim($data["deskripsi"] ?? "");
$jumlah    = $data["jumlah"] ?? null;

if ($id <= 0 || $tanggal==="" || $jenis==="" || $kategori==="" || $jumlah===null) {
    send_json(["success"=>false,"message"=>"Data tidak lengkap"], 400);
}
if ($jenis !== "Masuk" && $jenis !== "Keluar") {
    send_json(["success"=>false,"message"=>"Jenis harus Masuk/Keluar"], 400);
}
$jumlah = floatval($jumlah);
if ($jumlah <= 0) {
    send_json(["success"=>false,"message"=>"Jumlah harus > 0"], 400);
}

$stmt = $conn->prepare("UPDATE kas_transaksi
                        SET tanggal=?, jenis=?, kategori=?, deskripsi=?, jumlah=?
                        WHERE id=?");
if (!$stmt) send_json(["success"=>false,"message"=>$conn->error], 500);

$stmt->bind_param("ssssdi", $tanggal, $jenis, $kategori, $deskripsi, $jumlah, $id);
$ok = $stmt->execute();
if (!$ok) send_json(["success"=>false,"message"=>$stmt->error], 500);

$stmt->close();

send_json(["success"=>true,"data"=>true], 200);
