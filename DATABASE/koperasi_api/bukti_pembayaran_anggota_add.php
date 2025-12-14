<?php
// bukti_pembayaran_anggota_add.php
require 'config.php';

if (!function_exists('read_json_body')) {
    function read_json_body() {
        $raw = file_get_contents('php://input');
        if ($raw === false || trim($raw) === '') return null;
        $data = json_decode($raw, true);
        if (json_last_error() !== JSON_ERROR_NONE) return null;
        return $data;
    }
}

$body = read_json_body();
if (!is_array($body)) {
    send_json(["success"=>false, "message"=>"Body JSON tidak valid"], 200);
}

$kodePegawai = trim($body["kodePegawai"] ?? "");
$uri = trim($body["uri"] ?? "");

if ($kodePegawai === "" || $uri === "") {
    send_json(["success"=>false, "message"=>"kodePegawai dan uri wajib"], 200);
}

$stmt = $conn->prepare("INSERT INTO bukti_pembayaran_anggota (kodePegawai, uri, tanggal)
                        VALUES (?, ?, CURDATE())");
$stmt->bind_param("ss", $kodePegawai, $uri);

if ($stmt->execute()) {
    send_json(["success"=>true, "data"=>true], 200);
} else {
    send_json(["success"=>false, "message"=>"Gagal simpan bukti: ".$conn->error], 200);
}
?>
