package com.example.projek_map.data

data class User(
    val kodePegawai: String,
    val email: String,
    val password: String,
    val nama: String
)

// Dummy daftar user yang bisa login
object DummyUserData {
    val users = mutableListOf(
        User("EMP001", "pegawai1@example.com", "12345", "Budi Santoso"),
        User("EMP002", "pegawai2@example.com", "password", "Siti Aisyah")
    )
}
