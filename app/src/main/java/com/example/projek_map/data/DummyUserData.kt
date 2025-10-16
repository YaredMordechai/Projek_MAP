package com.example.projek_map.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

// =====================
// Data classes
// =====================

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
    val jumlah: Int,          // pokok pinjaman
    val tenor: Int,           // bulan
    val status: String,       // Proses / Disetujui / Ditolak / Lunas
    val bunga: Double = 0.1,  // 10% (dummy)
    val angsuranTerbayar: Int = 0,
)

data class Simpanan(
    val kodePegawai: String,
    val simpananPokok: Double,
    val simpananWajib: Double,
    val simpananSukarela: Double
)

// 🔹 histori ANGsuran pinjaman
data class HistoriPembayaran(
    val id: Int,
    val kodePegawai: String,
    val pinjamanId: Int,
    val tanggal: String,
    val jumlah: Int,
    val status: String,                   // Lunas / Belum Lunas
    val buktiPembayaranUri: String? = null
)

// 🔹 histori SIMPANAN
data class HistoriSimpanan(
    val id: Int,
    val kodePegawai: String,
    val tanggal: String,
    val jenis: String,   // Contoh: "Setoran Wajib", "Penarikan Sukarela"
    val jumlah: Double,  // +setor, -tarik
    val keterangan: String
)

// ✅ Bukti pembayaran per-anggota (tanpa pinjamanId) — dipakai SimpananFragment
data class BuktiPembayaranAnggota(
    val id: Int,
    val kodePegawai: String,
    val uri: String,
    val tanggal: String
)

// =====================
// Dummy store & helpers
// =====================

object DummyUserData {

    // --- Konstanta nominal default untuk dropdown (sinkron ke UI) ---
    const val DEFAULT_POKOK = 100_000.0
    const val DEFAULT_WAJIB = 50_000.0

    private val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale("id","ID"))
    private val monthFmt = SimpleDateFormat("MM", Locale("id","ID"))

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
        Simpanan("EMP001", 500_000.0, 200_000.0, 300_000.0),
        Simpanan("EMP002", 500_000.0, 250_000.0, 150_000.0)
    )

    // 🔹 Pinjaman dummy (mutableList biar bisa ditambah dari fragment)
    val pinjamanList = mutableListOf(
        Pinjaman(1, "EMP001", 2_000_000, 12, "Disetujui"),
        Pinjaman(2, "EMP002", 1_500_000, 6, "Proses")
    )

    // 🔹 Histori pembayaran dummy
    val historiPembayaranList = mutableListOf(
        HistoriPembayaran(1, "EMP001", 1, "2025-07-10", 200_000, "Lunas"),
        HistoriPembayaran(2, "EMP002", 1, "2025-08-10", 200_000, "Lunas"),
        HistoriPembayaran(3, "EMP001", 1, "2025-09-10", 200_000, "Belum Lunas"),
        HistoriPembayaran(4, "EMP002", 2, "2025-07-05", 250_000, "Belum Lunas")
    )

    // 🔹 Histori simpanan dummy
    val historiSimpananList = mutableListOf(
        HistoriSimpanan(1, "EMP001", "2025-07-01", "Setoran Wajib", 200_000.0, "Setoran rutin Juli"),
        HistoriSimpanan(2, "EMP001", "2025-08-01", "Setoran Sukarela", 300_000.0, "Setoran tambahan Agustus"),
        HistoriSimpanan(3, "EMP002", "2025-09-01", "Penarikan Sukarela", -150_000.0, "Penarikan untuk keperluan pribadi")
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

    // ====== BUKTI PEMBAYARAN (tanpa pinjamanId) untuk UI SimpananFragment ======
    private val buktiAnggotaList = mutableListOf<BuktiPembayaranAnggota>()

    /** Dipanggil dari SimpananFragment saat user memilih gambar bukti (dummy). */
    fun simpanBuktiPembayaran(kodePegawai: String, uri: String) {
        val idBaru = (buktiAnggotaList.maxOfOrNull { it.id } ?: 0) + 1
        val tanggal = dateFmt.format(Date())
        buktiAnggotaList.add(
            BuktiPembayaranAnggota(
                id = idBaru,
                kodePegawai = kodePegawai,
                uri = uri,
                tanggal = tanggal
            )
        )
    }

    /** (Opsional) Ambil daftar bukti yang pernah diunggah oleh anggota. */
    fun getBuktiPembayaran(kodePegawai: String): List<BuktiPembayaranAnggota> {
        return buktiAnggotaList.filter { it.kodePegawai == kodePegawai }
    }

    // =====================
    // Query helpers
    // =====================

    fun findUser(kodePegawai: String): User? = users.find { it.kodePegawai == kodePegawai }

    fun getHistoriPembayaran(pinjamanId: Int): List<HistoriPembayaran> {
        return historiPembayaranList.filter { it.pinjamanId == pinjamanId }
    }

    fun getHistoriSimpanan(kodePegawai: String): List<HistoriSimpanan> {
        return historiSimpananList.filter { it.kodePegawai == kodePegawai }
    }

    fun getBuktiPembayaranByAnggota(kodePegawai: String): List<HistoriPembayaran> {
        return historiPembayaranList.filter { it.kodePegawai == kodePegawai && it.buktiPembayaranUri != null }
    }

    // =====================
    // Hitung saldo & ringkasan
    // =====================

    fun getSaldoSimpananPokok(kodePegawai: String): Double {
        return simpananList.find { it.kodePegawai == kodePegawai }?.simpananPokok ?: 0.0
    }

    fun getSaldoSimpananWajib(kodePegawai: String): Double {
        return simpananList.find { it.kodePegawai == kodePegawai }?.simpananWajib ?: 0.0
    }

    fun getSaldoSimpananSukarela(kodePegawai: String): Double {
        return simpananList.find { it.kodePegawai == kodePegawai }?.simpananSukarela ?: 0.0
    }

    // 🔹 Total simpanan untuk user tertentu
    fun getTotalSimpanan(kodePegawai: String): Double {
        val s = simpananList.find { it.kodePegawai == kodePegawai }
        return if (s != null) s.simpananPokok + s.simpananWajib + s.simpananSukarela else 0.0
    }

    // 🔹 Total pinjaman aktif (status != "Lunas")
    fun getTotalPinjamanAktif(kodePegawai: String): Double {
        return pinjamanList
            .filter { it.kodePegawai == kodePegawai && it.status != "Lunas" }
            .sumOf { it.jumlah.toDouble() }
    }

    // =====================
    // Mutators (Simpanan)
    // =====================

    /**
     * Tambah histori simpanan (setoran/penarikan) dan update saldo per jenis.
     * @param jenisInput contoh dari UI: "Simpanan Pokok" / "Simpanan Wajib" / "Simpanan Sukarela"
     * @param jumlah bernilai + (setoran) atau - (penarikan)
     *
     * Catatan: histori.jenis akan otomatis menjadi "Setoran X" atau "Penarikan X".
     */
    fun tambahSimpanan(kodePegawai: String, jenisInput: String, jumlah: Double, keterangan: String) {
        val today = dateFmt.format(Date())
        val jenisBersih = jenisInput.trim()

        // Tentukan label histori
        val isSetoran = jumlah >= 0.0
        val labelJenis = when {
            jenisBersih.contains("Pokok", ignoreCase = true) ->
                if (isSetoran) "Setoran Pokok" else "Penarikan Pokok"
            jenisBersih.contains("Wajib", ignoreCase = true) ->
                if (isSetoran) "Setoran Wajib" else "Penarikan Wajib"
            else ->
                if (isSetoran) "Setoran Sukarela" else "Penarikan Sukarela"
        }

        // Tambah ke histori
        val idBaru = (historiSimpananList.maxOfOrNull { it.id } ?: 0) + 1
        historiSimpananList.add(
            HistoriSimpanan(idBaru, kodePegawai, today, labelJenis, jumlah, keterangan)
        )

        // Update saldo per jenis
        val s = simpananList.find { it.kodePegawai == kodePegawai }
        if (s == null) {
            val (pokok, wajib, sukarela) = when {
                labelJenis.contains("Pokok", true) -> Triple(jumlah, 0.0, 0.0)
                labelJenis.contains("Wajib", true) -> Triple(0.0, jumlah, 0.0)
                else -> Triple(0.0, 0.0, jumlah)
            }
            simpananList.add(Simpanan(kodePegawai, pokok, wajib, sukarela))
        } else {
            val idx = simpananList.indexOf(s)
            val newPokok    = if (labelJenis.contains("Pokok", true))    s.simpananPokok + jumlah else s.simpananPokok
            val newWajib    = if (labelJenis.contains("Wajib", true))    s.simpananWajib + jumlah else s.simpananWajib
            val newSukarela = if (labelJenis.contains("Sukarela", true)) s.simpananSukarela + jumlah else s.simpananSukarela
            simpananList[idx] = s.copy(
                simpananPokok = newPokok,
                simpananWajib = newWajib,
                simpananSukarela = newSukarela
            )
        }
    }

    // =====================
    // Mutators (Bukti pembayaran pinjaman)
    // =====================

    fun addHistoriPembayaranWithBukti(
        kodePegawai: String,
        pinjamanId: Int,
        jumlah: Int,
        status: String,
        buktiUri: String
    ) {
        val idBaru = (historiPembayaranList.maxOfOrNull { it.id } ?: 0) + 1
        val tanggal = dateFmt.format(Date())
        historiPembayaranList.add(
            HistoriPembayaran(idBaru, kodePegawai, pinjamanId, tanggal, jumlah, status, buktiUri)
        )
    }

    fun uploadBuktiSaja(kodePegawai: String, pinjamanId: Int, buktiUri: String) {
        val idBaru = (historiPembayaranList.maxOfOrNull { it.id } ?: 0) + 1
        val tanggal = dateFmt.format(Date())
        historiPembayaranList.add(
            HistoriPembayaran(idBaru, kodePegawai, pinjamanId, tanggal, 0, "Belum Lunas", buktiUri)
        )
    }

    // =====================
    // Pinjaman helpers (dummy)
    // =====================

    fun getNextPinjamanId(): Int = (pinjamanList.maxOfOrNull { it.id } ?: 0) + 1

    fun ajukanPinjaman(kodePegawai: String, jumlah: Int, tenor: Int): Pinjaman {
        val p = Pinjaman(
            id = getNextPinjamanId(),
            kodePegawai = kodePegawai,
            jumlah = jumlah,
            tenor = tenor,
            status = "Proses"
        )
        pinjamanList.add(p)
        return p
    }

    fun setStatusPinjaman(pinjamanId: Int, statusBaru: String) {
        val i = pinjamanList.indexOfFirst { it.id == pinjamanId }
        if (i >= 0) {
            val p = pinjamanList[i]
            pinjamanList[i] = p.copy(status = statusBaru)
        }
    }

    fun hitungCicilanPerBulan(pinjamanId: Int): Int {
        val p = pinjamanList.find { it.id == pinjamanId } ?: return 0
        val total = (p.jumlah * (1.0 + p.bunga)).toInt()
        return if (p.tenor > 0) total / p.tenor else total
    }

    fun getTotalAngsuranDibayar(pinjamanId: Int): Int {
        return historiPembayaranList
            .filter { it.pinjamanId == pinjamanId }
            .sumOf { it.jumlah }
    }

    fun getSisaAngsuran(pinjamanId: Int): Int {
        val p = pinjamanList.find { it.id == pinjamanId } ?: return 0
        val total = (p.jumlah * (1.0 + p.bunga)).toInt()
        val terbayar = getTotalAngsuranDibayar(pinjamanId)
        return max(total - terbayar, 0)
    }

    // =====================
    // Laporan & ringkasan
    // =====================

    fun getTotalAngsuranBulanan(kodePegawai: String, bulan: Int): Double {
        val bulanStr = "-${bulan.toString().padStart(2, '0')}-"
        return historiPembayaranList
            .filter { it.kodePegawai == kodePegawai && it.tanggal.contains(bulanStr) }
            .sumOf { it.jumlah.toDouble() }
    }
}
