package com.example.projek_map.data

data class User(
    val kodePegawai: String,
    val email: String,
    val password: String,
    val nama: String,
    val telepon: String,
    val statusKeanggotaan: String
)

// Dummy daftar user yang bisa login
object DummyUserData {
    val users = mutableListOf(
        User(
            "EMP001",
            "pegawai1@example.com",
            "12345",
            "Budi Santoso",
            "08123456789",
            "Anggota Aktif"
        ),
        User(
            "EMP002",
            "pegawai2@example.com",
            "password",
            "Siti Aisyah",
            "08234567890",
            "Anggota Tidak Aktif"
        )
    )
}
