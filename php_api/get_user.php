<?php
header("Content-Type: application/json");
include "db.php";

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['id'])) {
        $id = $_POST['id']; // id user dikirim dari Android

        // Gunakan prepared statement biar aman dari SQL Injection
        $stmt = $conn->prepare("SELECT id, nama, email, telepon FROM users WHERE id = ?");
        $stmt->bind_param("s", $id);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result && $result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $response['success'] = true;
            $response['user'] = $row;
        } else {
            $response['success'] = false;
            $response['message'] = "User tidak ditemukan";
        }

        $stmt->close();
    } else {
        $response['success'] = false;
        $response['message'] = "Parameter ID tidak ada";
    }
} else {
    $response['success'] = false;
    $response['message'] = "Request tidak valid";
}

echo json_encode($response);
?>
