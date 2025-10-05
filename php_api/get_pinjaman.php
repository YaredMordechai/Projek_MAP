<?php
header("Content-Type: application/json");
include "db.php";

$result = mysqli_query($conn, "SELECT * FROM pinjaman ORDER BY id DESC");

$data = array();
while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode($data);
?>
