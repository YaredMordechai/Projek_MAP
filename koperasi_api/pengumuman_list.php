<?php
// pengumuman_list.php
require 'config.php';

$result = $conn->query("SELECT * FROM pengumuman ORDER BY tanggal DESC, id DESC");
$list = [];

while ($row = $result->fetch_assoc()) {
    $list[] = $row;
}

send_json([
    "success" => true,
    "data" => $list
], 200);
?>
