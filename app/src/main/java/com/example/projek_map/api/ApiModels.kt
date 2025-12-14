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
    val tanggal: String,          // "yyyy-MM-dd"
    val jumlah: Int,
    val status: String,
    val buktiPembayaranUri: String? = null
)

// === SIMPANAN TRANSAKSI (SETOR / TARIK) ===
data class SimpananTransaksiRequest(
    val kodePegawai: String,
    val jenisInput: String,     // "Simpanan Pokok" / "Simpanan Wajib" / "Simpanan Sukarela"
    val jumlah: Double,         // +setor, -tarik
    val keterangan: String = "-"
)

// === BUKTI PEMBAYARAN ANGGOTA (upload bukti simpanan) ===
data class BuktiPembayaranAnggotaRequest(
    val kodePegawai: String,
    val uri: String
)

//NOTIFIKASI
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
