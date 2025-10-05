<?php
header("Content-Type: application/json");
include "db.php";
session_start();

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $nama   = isset($_SESSION['nama']) ? $_SESSION['nama'] : 'User';
    $jumlah = isset($_POST['jumlah']) ? $_POST['jumlah'] : '';
    $tenor  = isset($_POST['tenor']) ? $_POST['tenor'] : '';

    if (!empty($jumlah) && !empty($tenor)) {
        // Simpan ke DB (pastikan tabel punya kolom tenor)
        $query = "INSERT INTO pinjaman (nama, jumlah, tenor, tanggal, status) 
                  VALUES ('$nama', '$jumlah', '$tenor', CURDATE(), 'Pending')";
        $result = mysqli_query($conn, $query);

        if ($result) {
            $response['success'] = true;
            $response['message'] = "Pinjaman berhasil diajukan";
        } else {
            $response['success'] = false;
            $response['message'] = "Gagal menyimpan: " . mysqli_error($conn);
        }
    } else {
        $response['success'] = false;
        $response['message'] = "Data tidak lengkap (jumlah atau tenor kosong)";
    }
} else {
    $response['success'] = false;
    $response['message'] = "Metode request tidak valid";
}

echo json_encode($response);
?>
