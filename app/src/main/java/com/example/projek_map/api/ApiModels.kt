package com.example.projek_map.api

data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)

data class LoginRequest(val email: String, val password: String)

data class RegisterRequest(
    val kodePegawai: String,
    val email: String,
    val password: String,
    val nama: String
)

/** === PINJAMAN === */
data class CreatePinjamanRequest(
    val kodePegawai: String,
    val jumlah: Int,
    val tenor: Int
)

data class DecidePinjamanRequest(
    val pinjamanId: Int,
    val decision: String, // "approve" / "reject"
    val adminKode: String,
)

/** === HISTORI PEMBAYARAN === */
data class AddHistoriPembayaranRequest(
    val kodePegawai: String,
    val pinjamanId: Int,
    val tanggal: String,
    val jumlah: Int,
    val status: String,
    val buktiPembayaranUri: String? = null,

    // ✅ BARU (untuk upload file)
    val buktiBase64: String? = null,
    val buktiExt: String? = "jpg"
)


data class HistoriPembayaran(
    val id: Int,
    val kodePegawai: String,
    val pinjamanId: Int,
    val tanggal: String,          // "yyyy-MM-dd"
    val jumlah: Int,
    val status: String,
    val buktiPembayaranUri: String? = null
)

// === SIMPANAN TRANSAKSI (SETOR / TARIK) ===
data class SimpananTransaksiRequest(
    val kodePegawai: String,
    val jenisInput: String,
    val jumlah: Double,
    val keterangan: String,

    // ✅ BARU (setoran anggota wajib bukti)
    val buktiBase64: String? = null,
    val buktiExt: String? = "jpg"
)


// === BUKTI PEMBAYARAN ANGGOTA (upload bukti simpanan) ===
data class BuktiPembayaranAnggotaRequest(
    val kodePegawai: String,
    val uri: String
)

// NOTIFIKASI
data class DecisionNotificationDto(
    val id: Int,
    val kodePegawai: String,
    val pinjamanId: Int,
    val decision: String,   // "Disetujui" / "Ditolak"
    val jumlah: Int,
    val tanggal: String,
    val is_read: Int        // 0/1 dari MySQL
)

data class MarkReadRequest(
    val ids: List<Int>
)

data class AdminCatatPembayaranRequest(
    val kodePegawai: String,
    val pinjamanId: Int,
    val tanggal: String, // yyyy-MM-dd
    val jumlah: Int,
    val status: String = "Dibayar (Admin)"
)

data class AddUserRequest(
    val kodePegawai: String,
    val nama: String,
    val email: String,
    val password: String = "1234",
    val statusKeanggotaan: String = "Anggota Aktif"
)

data class UpdateUserRequest(
    val kodePegawai: String,
    val nama: String,
    val email: String
)

data class SetUserStatusRequest(
    val kodePegawai: String,
    val statusKeanggotaan: String // "Anggota Aktif" / "Nonaktif"
)

data class DeleteUserRequest(
    val kodePegawai: String
)

// KAS TRANSAKSI
data class KasTransaksi(
    val id: Int,
    val tanggal: String,   // "yyyy-MM-dd"
    val jenis: String,     // "Masuk" / "Keluar"
    val kategori: String,
    val deskripsi: String,
    val jumlah: Double
)

data class KasTransaksiAddRequest(
    val tanggal: String,
    val jenis: String,
    val kategori: String,
    val deskripsi: String,
    val jumlah: Double
)

data class KasTransaksiUpdateRequest(
    val id: Int,
    val tanggal: String,
    val jenis: String,
    val kategori: String,
    val deskripsi: String,
    val jumlah: Double
)

data class KasDeleteRequest(val id: Int)

data class SettingsData(
    val bungaPersen: Double,
    val dendaPersenPerHari: Double
)

data class SettingsResponse(
    val success: Boolean,
    val message: String? = null,
    val data: SettingsData? = null
)

data class SettingsUpdateRequest(
    val bungaPersen: Double,
    val dendaPersenPerHari: Double
)

/** === PENGUMUMAN === */
data class PengumumanAddRequest(
    val judul: String,
    val isi: String
)

data class PengumumanUpdateRequest(
    val id: Int,
    val judul: String,
    val isi: String
)

data class PengumumanDeleteRequest(
    val id: Int
)

/** === LAPORAN USER === */
data class LaporanUserData(
    val kodePegawai: String,
    val year: Int,
    val bulan: Int,
    val totalSimpanan: Double,
    val totalPinjamanAktif: Double,
    val totalAngsuranBulanan: Double,
    val monthlySimpanan: List<Double>,
    val monthlyAngsuran: List<Double>
)

/** === LAPORAN ADMIN === */
data class LaporanAdminResponse(
    val success: Boolean,
    val message: String? = null,
    val data: LaporanAdminData? = null
)

data class LaporanAdminData(
    val saldoKas: Double,
    val totalSimpananAll: Double,
    val totalPinjamanAll: Double,
    val jumlahAnggota: Int,
    val users: List<LaporanUserRow>
)

data class LaporanUserRow(
    val kodePegawai: String,
    val email: String,
    val password: String = "",
    val nama: String,
    val statusKeanggotaan: String,
    val totalSimpanan: Double,
    val totalPinjamanAktif: Double
)

//LABA
data class LabaBersihData(
    val totalMasuk: Double,
    val totalKeluar: Double,
    val labaBersih: Double
)
data class LabaRugiData(
    val year: Int,
    val bulan: Int,
    val totalPendapatan: Double,
    val totalBeban: Double,
    val labaRugi: Double,
    val isLaba: Boolean
)

data class LabaRugiResponse(
    val success: Boolean,
    val message: String?,
    val data: LabaRugiData?
)

data class SimpananPending(
    val id: Int,
    val kodePegawai: String,
    val jenisInput: String,
    val jumlah: Double,
    val keterangan: String?,
    val tanggal: String?,
    val statusVerifikasi: String?,
    val buktiUrl: String?
)

data class SimpananPendingDecideRequest(
    val id: Int,
    val action: String // "approve" / "reject"
)
