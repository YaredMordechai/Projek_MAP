<?php
include 'db.php';

$nama = isset($_POST['nama']) ? mysqli_real_escape_string($conn, $_POST['nama']) : '';
$email = isset($_POST['email']) ? mysqli_real_escape_string($conn, $_POST['email']) : '';
$password = isset($_POST['password']) ? $_POST['password'] : '';

if (empty($nama) || empty($email) || empty($password)) {
    echo json_encode(["success" => false, "message" => "Semua field wajib diisi"]);
    exit;
}

// cek email sudah ada?
$check = mysqli_query($conn, "SELECT id FROM users WHERE email='$email'");
if (mysqli_num_rows($check) > 0) {
    echo json_encode(["success" => false, "message" => "Email sudah terdaftar"]);
    exit;
}

// hash password
$hash = password_hash($password, PASSWORD_BCRYPT);

$query = "INSERT INTO users (nama, email, password) VALUES ('$nama', '$email', '$hash')";
if (mysqli_query($conn, $query)) {
    echo json_encode(["success" => true, "message" => "Registrasi berhasil"]);
} else {
    echo json_encode(["success" => false, "message" => "Gagal registrasi: " . mysqli_error($conn)]);
}
?>
