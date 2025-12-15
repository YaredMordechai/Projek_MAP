<?php
require_once "config.php";

$data = read_json_body();
if (!is_array($data)) send_json(["success"=>false,"message"=>"Body JSON tidak valid"], 400);

$nama  = trim($data["nama"] ?? "");
$email = trim($data["email"] ?? "");
$pass  = (string)($data["password"] ?? "1234");

if ($nama === "" || $email === "") {
    send_json(["success"=>false,"message"=>"nama dan email wajib diisi"], 400);
}

try {
    // Ambil kode terakhir
    $res = $conn->query("SELECT kodePegawai FROM users ORDER BY kodePegawai DESC LIMIT 1");
    if (!$res) send_json(["success"=>false,"message"=>$conn->error], 500);

    $last = $res->fetch_assoc();
    $lastKode = $last["kodePegawai"] ?? "EMP000";
    $num = intval(substr($lastKode, 3)) + 1;
    $kodePegawai = "EMP" . str_pad((string)$num, 3, "0", STR_PAD_LEFT);

    $status = "Anggota Aktif";

    $stmt = $conn->prepare("INSERT INTO users (kodePegawai, email, password, nama, statusKeanggotaan) VALUES (?, ?, ?, ?, ?)");
    if (!$stmt) send_json(["success"=>false,"message"=>$conn->error], 500);

    $stmt->bind_param("sssss", $kodePegawai, $email, $pass, $nama, $status);
    $ok = $stmt->execute();

    if (!$ok) {
        // duplikat email
        if ($conn->errno == 1062) {
            send_json(["success"=>false,"message"=>"Email sudah digunakan"], 400);
        }
        send_json(["success"=>false,"message"=>$conn->error], 500);
    }

    $stmt->close();

    send_json([
        "success" => true,
        "data" => [
            "kodePegawai" => $kodePegawai,
            "email" => $email,
            "nama" => $nama,
            "statusKeanggotaan" => $status
        ]
    ], 200);

} catch (Throwable $e) {
    send_json(["success"=>false,"message"=>$e->getMessage()], 500);
}
