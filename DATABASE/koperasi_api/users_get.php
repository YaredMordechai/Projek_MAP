<?php
require_once "config.php";

$kodePegawai = $_GET["kodePegawai"] ?? "";

if ($kodePegawai !== "") {
    $stmt = $conn->prepare("SELECT kodePegawai, email, nama, statusKeanggotaan FROM users WHERE kodePegawai = ?");
    if (!$stmt) send_json(["success"=>false,"message"=>$conn->error], 500);

    $stmt->bind_param("s", $kodePegawai);
    $stmt->execute();
    $res = $stmt->get_result();
    $row = $res->fetch_assoc();
    $stmt->close();

    send_json(["success"=>true, "data"=>$row ?: null], 200);
} else {
    $sql = "SELECT kodePegawai, email, nama, statusKeanggotaan FROM users ORDER BY kodePegawai ASC";
    $res = $conn->query($sql);
    if (!$res) send_json(["success"=>false,"message"=>$conn->error], 500);

    $rows = [];
    while ($r = $res->fetch_assoc()) $rows[] = $r;

    send_json(["success"=>true, "data"=>$rows], 200);
}
