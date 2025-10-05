<?php
include 'db.php';

$email = isset($_POST['email']) ? mysqli_real_escape_string($conn, $_POST['email']) : '';
$password = isset($_POST['password']) ? $_POST['password'] : '';

if (empty($email) || empty($password)) {
    echo json_encode(["success" => false, "message" => "Email dan password wajib diisi"]);
    exit;
}

$q = mysqli_query($conn, "SELECT id, nama, email, password FROM users WHERE email='$email' LIMIT 1");
if (mysqli_num_rows($q) == 0) {
    echo json_encode(["success" => false, "message" => "Email tidak terdaftar"]);
    exit;
}

$user = mysqli_fetch_assoc($q);
if (password_verify($password, $user['password'])) {
    // hapus field password dari response
    unset($user['password']);
    echo json_encode(["success" => true, "message" => "Login berhasil", "user" => $user]);
} else {
    echo json_encode(["success" => false, "message" => "Password salah"]);
}
?>
