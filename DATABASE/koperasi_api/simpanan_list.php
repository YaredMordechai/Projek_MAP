<?php
// simpanan_list.php
require 'config.php';

// optional filter: kalau ada kodePegawai, kembalikan 1 row saja
$kode = isset($_GET['kodePegawai']) ? trim($_GET['kodePegawai']) : '';

if ($kode !== '') {
    $stmt = $conn->prepare("SELECT kodePegawai, simpananPokok, simpananWajib, simpananSukarela
                            FROM simpanan
                            WHERE kodePegawai = ?
                            LIMIT 1");
    $stmt->bind_param("s", $kode);
} else {
    $stmt = $conn->prepare("SELECT kodePegawai, simpananPokok, simpananWajib, simpananSukarela
                            FROM simpanan
                            ORDER BY kodePegawai ASC");
}

$stmt->execute();
$result = $stmt->get_result();

$data = [];
while ($row = $result->fetch_assoc()) {
    $row["simpananPokok"] = (float)$row["simpananPokok"];
    $row["simpananWajib"] = (float)$row["simpananWajib"];
    $row["simpananSukarela"] = (float)$row["simpananSukarela"];
    $data[] = $row;
}

send_json([
    "success" => true,
    "data" => $kode !== '' ? ($data[0] ?? null) : $data
], 200);
?>
