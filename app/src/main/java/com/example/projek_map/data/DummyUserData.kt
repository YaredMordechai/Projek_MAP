package com.example.projek_map.data

data class User(
    val kodePegawai: String,
    var email: String,
    var password: String,
    var nama: String,
    var telepon: String,
    var statusKeanggotaan: String
)

data class Admin(
    val kodePegawai: String,
    val email: String,
    val password: String,
    val nama: String,
    val role: String = "Admin"
)

data class Pinjaman(
    val id: Int,
    val kodePegawai: String,
    val jumlah: Int,
    val tenor: Int,
    val status: String,
    val bunga: Double = 0.1,
    val angsuranTerbayar: Int = 0,
)

data class Simpanan(
    val kodePegawai: String,
    val simpananPokok: Double,
    val simpananWajib: Double,
    val simpananSukarela: Double
)

// 🔹 Tambahkan data class histori di bawah sini
data class HistoriPembayaran(
    val id: Int,
    val kodePegawai: String,
    val pinjamanId: Int,
    val tanggal: String,
    val jumlah: Int,
    val status: String,
    val buktiPembayaranUri: String? = null // 📷 optional URI ke gambar bukti
)

data class HistoriSimpanan(
    val id: Int,
    val kodePegawai: String,
    val tanggal: String,
    val jenis: String, // Setoran atau Penarikan
    val jumlah: Double,
    val keterangan: String
)

object DummyUserData {

    // 🔹 User dummy
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

    // 🔹 Simpanan dummy
    val simpananList = mutableListOf(
        Simpanan("EMP001", 500000.0, 200000.0, 300000.0),
        Simpanan("EMP002", 500000.0, 250000.0, 150000.0)
    )

    // 🔹 Pinjaman dummy (mutableList biar bisa ditambah dari fragment)
    val pinjamanList = mutableListOf(
        Pinjaman(1, "EMP001", 2000000, 12, "Disetujui"),
        Pinjaman(2, "EMP002", 1500000, 6, "Proses")
    )

    // 🔹 Histori pembayaran dummy
    val historiPembayaranList = mutableListOf(
        HistoriPembayaran(1, "EMP001",1, "2025-07-10", 200000, "Lunas"),
        HistoriPembayaran(2, "EMP002",1, "2025-08-10", 200000, "Lunas"),
        HistoriPembayaran(3, "EMP001",1, "2025-09-10", 200000, "Belum Lunas"),
        HistoriPembayaran(4, "EMP002",2, "2025-07-05", 250000, "Belum Lunas")
    )

    val historiSimpananList = mutableListOf(
        HistoriSimpanan(1, "EMP001", "2025-07-01", "Setoran Wajib", 200000.0, "Setoran rutin Juli"),
        HistoriSimpanan(2, "EMP001", "2025-08-01", "Setoran Sukarela", 300000.0, "Setoran tambahan Agustus"),
        HistoriSimpanan(3, "EMP002", "2025-09-01", "Penarikan Sukarela", -150000.0, "Penarikan untuk keperluan pribadi")
    )

    val admins = listOf(
        Admin(
            kodePegawai = "ADM001",
            email = "admin@koperasi.com",
            password = "admin123",
            nama = "Admin Utama"

        ),
        Admin(
            kodePegawai = "ADM002",
            email = "supervisor@koperasi.com",
            password = "admin123",
            nama = "Supervisor Koperasi"
        )
    )

    // 🔹 Fungsi helper buat ambil histori per pinjaman
    fun getHistoriPembayaran(pinjamanId: Int): List<HistoriPembayaran> {
        return historiPembayaranList.filter { it.pinjamanId == pinjamanId }
    }
    fun getHistoriSimpanan(kodePegawai: String): List<HistoriSimpanan> {
        return historiSimpananList.filter { it.kodePegawai == kodePegawai }
    }


    // 🔹 Hitung total simpanan untuk user tertentu
    fun getTotalSimpanan(kodePegawai: String): Double {
        val simpanan = simpananList.find { it.kodePegawai == kodePegawai }
        return if (simpanan != null) {
            simpanan.simpananPokok + simpanan.simpananWajib + simpanan.simpananSukarela
        } else 0.0
    }

    // 🔹 Hitung total pinjaman aktif (status != "Lunas")
    fun getTotalPinjamanAktif(kodePegawai: String): Double {
        return pinjamanList
            .filter { it.kodePegawai == kodePegawai && it.status != "Lunas" }
            .sumOf { it.jumlah.toDouble() }
    }
    fun tambahSimpanan(kodePegawai: String, jenis: String, jumlah: Double, keterangan: String) {
        val idBaru = (historiSimpananList.maxOfOrNull { it.id } ?: 0) + 1
        val tanggal = java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())
        historiSimpananList.add(
            HistoriSimpanan(idBaru, kodePegawai, tanggal, jenis, jumlah, keterangan)
        )

        // Update saldo total (khusus simpanan sukarela)
        val simpanan = simpananList.find { it.kodePegawai == kodePegawai }
        if (simpanan != null) {
            val index = simpananList.indexOf(simpanan)
            simpananList[index] = simpanan.copy(
                simpananSukarela = simpanan.simpananSukarela + jumlah
            )
        }
    }

    // 📷 Tambah helper untuk menyimpan histori pembayaran dengan bukti (dummy)
    fun addHistoriPembayaranWithBukti(
        kodePegawai: String,
        pinjamanId: Int,
        jumlah: Int,
        status: String,
        buktiUri: String
    ) {
        val idBaru = (historiPembayaranList.maxOfOrNull { it.id } ?: 0) + 1
        val tanggal = java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())
        historiPembayaranList.add(
            HistoriPembayaran(idBaru, kodePegawai, pinjamanId, tanggal, jumlah, status, buktiUri)
        )
    }

    // 🔹 Hitung total angsuran dibayar untuk bulan tertentu
    fun getTotalAngsuranBulanan(kodePegawai: String, bulan: Int): Double {
        return historiPembayaranList
            .filter { it.kodePegawai == kodePegawai && it.tanggal.contains("-${bulan.toString().padStart(2, '0')}-") }
            .sumOf { it.jumlah.toDouble() }
    }

}
