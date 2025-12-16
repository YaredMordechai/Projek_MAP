<?php
// simpanan_get_all.php
require 'config.php';

$stmt = $conn->prepare("SELECT kodePegawai, simpananPokok, simpananWajib, simpananSukarela FROM simpanan ORDER BY kodePegawai ASC");
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
    "data" => $data
], 200);
?>
