// DummyUserData.kt
package com.example.projek_map.data

data class User(
    val kodePegawai: String,
    val email: String,
    val password: String,
    val nama: String,
    val telepon: String,
    val statusKeanggotaan: String
)

data class Pinjaman(
    val id: Int,
    val kodePegawai: String,
    val jumlah: Int,
    val tenor: Int,
    val status: String
)

data class Simpanan(
    val kodePegawai: String,
    val simpananPokok: Double,
    val simpananWajib: Double,
    val simpananSukarela: Double
)

object DummyUserData {

    // 🔹 Ubah dari listOf() ➜ mutableListOf()
    val users = mutableListOf(
        User(
            kodePegawai = "EMP001",
            email = "pegawai1@koperasi.com",
            password = "1234",
            nama = "Budi Santoso",
            telepon = "08123456789",
            statusKeanggotaan = "Anggota Aktif"
        ),
        User(
            kodePegawai = "EMP002",
            email = "pegawai2@koperasi.com",
            password = "1234",
            nama = "Siti Aminah",
            telepon = "08234567890",
            statusKeanggotaan = "Anggota Tidak Aktif"
        )
    )

    // 🔹 Data simpanan dummy
    val simpananList = listOf(
        Simpanan("EMP001", 500000.0, 200000.0, 300000.0),
        Simpanan("EMP002", 500000.0, 250000.0, 150000.0)
    )

    // 🔹 Data pinjaman dummy
    val pinjamanList = mutableListOf(
        Pinjaman(1, "EMP001", 500000, 12, "Disetujui"),
        Pinjaman(2, "EMP002", 800000, 8, "Lunas"),
        Pinjaman(3, "EMP003", 1200000, 3, "Proses")
    )
}
