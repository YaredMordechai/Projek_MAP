<?php
require_once "config.php";

$data = read_json_body();
if (!is_array($data)) send_json(["success"=>false,"message"=>"Body JSON tidak valid"], 400);

$tanggal   = trim($data["tanggal"] ?? "");
$jenis     = trim($data["jenis"] ?? "");
$kategori  = trim($data["kategori"] ?? "");
$deskripsi = trim($data["deskripsi"] ?? "");
$jumlah    = $data["jumlah"] ?? null;

if ($tanggal==="" || $jenis==="" || $kategori==="" || $jumlah===null) {
    send_json(["success"=>false,"message"=>"Data tidak lengkap"], 400);
}
if ($jenis !== "Masuk" && $jenis !== "Keluar") {
    send_json(["success"=>false,"message"=>"Jenis harus Masuk/Keluar"], 400);
}
$jumlah = floatval($jumlah);
if ($jumlah <= 0) {
    send_json(["success"=>false,"message"=>"Jumlah harus > 0"], 400);
}

$stmt = $conn->prepare("INSERT INTO kas_transaksi (tanggal, jenis, kategori, deskripsi, jumlah)
                        VALUES (?, ?, ?, ?, ?)");
if (!$stmt) send_json(["success"=>false,"message"=>$conn->error], 500);

$stmt->bind_param("ssssd", $tanggal, $jenis, $kategori, $deskripsi, $jumlah);
$ok = $stmt->execute();
if (!$ok) send_json(["success"=>false,"message"=>$stmt->error], 500);

$newId = $stmt->insert_id;
$stmt->close();

send_json(["success"=>true,"data"=>["id"=>$newId]], 200);
