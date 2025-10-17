package com.example.projek_map.data

import android.os.Build
import androidx.annotation.RequiresApi
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
    var status: String,       // Proses / Disetujui / Ditolak / Lunas
    val bunga: Double = 0.1,  // 10% (dummy)
    val angsuranTerbayar: Int = 0,
)

data class Simpanan(
    val kodePegawai: String,
    var simpananPokok: Double,
    var simpananWajib: Double,
    var simpananSukarela: Double
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

data class AngsuranItem(
    val periode: Int,          // 1..tenor
    val pokok: Int,            // porsi pokok bulan ini
    val bunga: Int,            // porsi bunga bulan ini
    val total: Int,            // cicilan bulan ini
    val sisaPokok: Int         // sisa pokok setelah bayar bulan ini
)

data class RincianPinjaman(
    val cicilanPerBulan: Int,
    val totalBunga: Int,
    val totalBayar: Int,
    val totalPokok: Int,
    val terbayar: Int,
    val sisaBayar: Int,
    val sisaPokok: Int,
    val angsuranDibayar: Int   // sudah berapa kali angsuran (pembulatan ke bawah)
)

data class DueReminder(
    val pinjamanId: Int,
    val kodePegawai: String,
    val dueDate: String,  // yyyy-MM-dd
    val nominalCicilan: Int
)

data class Pengumuman(
    val id: Int,
    val judul: String,
    val isi: String,
    val tanggal: String  // yyyy-MM-dd
)

data class TransaksiSimpanan(
    val id: Int,
    val kodePegawai: String,
    val jenis: String,      // "Pokok", "Wajib", atau "Sukarela"
    val jumlah: Double,
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
        Pinjaman(1, "EMP001", 2_000_000, 12, "Proses"),
        Pinjaman(2, "EMP002", 1_500_000, 6, "Proses"),
        Pinjaman(1, "EMP001", 3_000_000, 12, "Disetujui"),
        Pinjaman(2, "EMP002", 5_500_000, 6, "Disetujui")
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

    val transaksiSimpananList = mutableListOf<TransaksiSimpanan>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun tambahTransaksiSimpanan(kodePegawai: String, jenis: String, jumlah: Double) {
        val simpanan = simpananList.find { it.kodePegawai == kodePegawai }
        if (simpanan != null) {
            when (jenis.lowercase()) {
                "pokok" -> simpanan.simpananPokok += jumlah
                "wajib" -> simpanan.simpananWajib += jumlah
                "sukarela" -> simpanan.simpananSukarela += jumlah
            }
        }

        transaksiSimpananList.add(
            TransaksiSimpanan(
                id = transaksiSimpananList.size + 1,
                kodePegawai = kodePegawai,
                jenis = jenis,
                jumlah = jumlah,
                tanggal = java.time.LocalDate.now().toString()
            )
        )
    }

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
        val today = getTodayDate()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = format.parse(today)

        // cari pembayaran terakhir utk pinjaman ini
        val lastPayment = historiPembayaranList
            .filter { it.pinjamanId == pinjamanId }
            .maxByOrNull { format.parse(it.tanggal)?.time ?: 0L }

        var finalStatus = status
        var denda = 0

        if (lastPayment != null) {
            val lastDue = format.parse(lastPayment.tanggal)
            if (lastDue != null && lastDue.before(todayDate)) {
                val diffDays = ((todayDate.time - lastDue.time) / (1000 * 60 * 60 * 24)).toInt()
                if (diffDays > 0) {
                    // misal 1% per hari dari jumlah cicilan terakhir
                    val jumlahDenda = (lastPayment.jumlah * 0.01 * diffDays).toInt()
                    denda = jumlahDenda
                    finalStatus = "$status - Terlambat $diffDays hari (Denda: Rp ${String.format("%,d", denda).replace(',', '.')})"
                }
            }

        }


        historiPembayaranList.add(
            HistoriPembayaran(
                id = historiPembayaranList.size + 1,
                kodePegawai = kodePegawai,
                pinjamanId = pinjamanId,
                tanggal = today,
                jumlah = jumlah,
                status = finalStatus,
                buktiPembayaranUri = buktiUri
            )
        )
    }

    private fun getTodayDate(): String {
        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return format.format(java.util.Date())
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

    fun approvePinjaman(id: Int) {
        var pinjaman = pinjamanList.find { it.id == id }
        pinjaman?.let {
            it.status = "Disetujui"
            println("Pinjaman ${it.kodePegawai} disetujui oleh admin.")
        }
    }

    fun rejectPinjaman(id: Int) {
        var pinjaman = pinjamanList.find { it.id == id }
        pinjaman?.let {
            it.status = "Ditolak"
            println("Pinjaman ${it.kodePegawai} ditolak oleh admin.")
        }
    }

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
    // Perhitungan bunga & angsuran
    // =====================

    private fun monthlyRate(p: Pinjaman): Double {
        // Asumsi: p.bunga adalah bunga TAHUNAN (mis. 0.12 = 12% per tahun)
        // Jika kamu menyimpan bunga bulanan, ubah ke return p.bunga
        return p.bunga / 12.0
    }

    /** Rumus anuitas:
     * A = P * i / (1 - (1+i)^-n)
     * P = pokok pinjaman, i = bunga per bulan, n = tenor (bulan)
     */
    fun hitungCicilanPerBulanAnuitas(pinjamanId: Int): Int {
        val p = pinjamanList.find { it.id == pinjamanId } ?: return 0
        val P = p.jumlah.toDouble()
        val n = p.tenor
        if (n <= 0) return 0

        val i = monthlyRate(p)
        if (i == 0.0) return (P / n).toInt()

        val faktor = i / (1 - Math.pow(1 + i, -n.toDouble()))
        return (P * faktor).toInt()
    }

    /** Buat jadwal angsuran (anuitas). */
    fun generateJadwalAnuitas(pinjamanId: Int): List<AngsuranItem> {
        val p = pinjamanList.find { it.id == pinjamanId } ?: return emptyList()
        val n = p.tenor
        if (n <= 0) return emptyList()

        val i = monthlyRate(p)
        val cicilan = hitungCicilanPerBulanAnuitas(pinjamanId)
        var sisa = p.jumlah.toDouble()

        val items = mutableListOf<AngsuranItem>()
        for (k in 1..n) {
            val bunga = (sisa * i).toInt()
            val pokok = (cicilan - bunga).coerceAtLeast(0)
            sisa -= pokok
            items.add(
                AngsuranItem(
                    periode = k,
                    pokok = pokok,
                    bunga = bunga,
                    total = cicilan,
                    sisaPokok = sisa.toInt().coerceAtLeast(0)
                )
            )
        }
        // Koreksi rounding: pastikan sisa pokok terakhir 0
        if (items.isNotEmpty()) {
            val last = items.last()
            if (last.sisaPokok != 0) {
                val delta = last.sisaPokok
                val fixedLast = last.copy(pokok = last.pokok + delta, total = last.total + delta, sisaPokok = 0)
                items[items.lastIndex] = fixedLast
            }
        }
        return items
    }

    /** Ringkasan pinjaman (anuitas) + membaca pembayaran dari histori. */
    fun getRincianPinjamanAnuitas(pinjamanId: Int): RincianPinjaman {
        val p = pinjamanList.find { it.id == pinjamanId }
            ?: return RincianPinjaman(0,0,0,0,0,0,0,0)

        val jadwal = generateJadwalAnuitas(pinjamanId)
        val cicilan = hitungCicilanPerBulanAnuitas(pinjamanId)
        val totalPokok = jadwal.sumOf { it.pokok }
        val totalBunga = jadwal.sumOf { it.bunga }
        val totalBayar = jadwal.sumOf { it.total }

        val terbayar = historiPembayaranList
            .filter { it.pinjamanId == pinjamanId }
            .sumOf { it.jumlah }

        val sisaBayar = (totalBayar - terbayar).coerceAtLeast(0)

        // Estimasi angsuran yang sudah dibayar (jika pembayaran sesuai cicilan)
        val angsuranDibayar = if (cicilan > 0) (terbayar / cicilan) else 0

        // Hitung sisa pokok: total pokok - pokok yang “diasumsikan” sudah dibayar
        val pokokTerbayar = jadwal.take(angsuranDibayar).sumOf { it.pokok }
        val sisaPokok = (totalPokok - pokokTerbayar).coerceAtLeast(0)

        return RincianPinjaman(
            cicilanPerBulan = cicilan,
            totalBunga = totalBunga,
            totalBayar = totalBayar,
            totalPokok = totalPokok,
            terbayar = terbayar,
            sisaBayar = sisaBayar,
            sisaPokok = sisaPokok,
            angsuranDibayar = angsuranDibayar
        )
    }

    // ====== Versi FLAT (opsional, untuk perbandingan / fallback) ======

    /** Cicilan per bulan (flat): (pokok/tenor) + (pokok * bunga_tahunan/12) */
    fun hitungCicilanPerBulanFlat(pinjamanId: Int): Int {
        val p = pinjamanList.find { it.id == pinjamanId } ?: return 0
        val P = p.jumlah.toDouble()
        val n = p.tenor
        if (n <= 0) return 0

        val i = monthlyRate(p)
        val pokokPerBulan = P / n
        val bungaPerBulan = P * i
        return (pokokPerBulan + bungaPerBulan).toInt()
    }

    fun generateJadwalFlat(pinjamanId: Int): List<AngsuranItem> {
        val p = pinjamanList.find { it.id == pinjamanId } ?: return emptyList()
        val n = p.tenor
        if (n <= 0) return emptyList()

        val i = monthlyRate(p)
        val P = p.jumlah.toDouble()
        val pokokPerBulan = (P / n).toInt()
        val bungaPerBulan = (P * i).toInt()
        val totalPerBulan = pokokPerBulan + bungaPerBulan

        var sisa = P.toInt()
        val items = mutableListOf<AngsuranItem>()
        for (k in 1..n) {
            val pokok = if (k < n) pokokPerBulan else sisa // koreksi rounding di bulan terakhir
            val bunga = bungaPerBulan
            sisa -= pokok
            items.add(
                AngsuranItem(
                    periode = k,
                    pokok = pokok,
                    bunga = bunga,
                    total = pokok + bunga,
                    sisaPokok = sisa.coerceAtLeast(0)
                )
            )
        }
        return items
    }

    fun getRincianPinjamanFlat(pinjamanId: Int): RincianPinjaman {
        val jadwal = generateJadwalFlat(pinjamanId)
        val cicilan = hitungCicilanPerBulanFlat(pinjamanId)
        val totalPokok = jadwal.sumOf { it.pokok }
        val totalBunga = jadwal.sumOf { it.bunga }
        val totalBayar = jadwal.sumOf { it.total }

        val terbayar = historiPembayaranList
            .filter { it.pinjamanId == pinjamanId }
            .sumOf { it.jumlah }

        val sisaBayar = (totalBayar - terbayar).coerceAtLeast(0)
        val angsuranDibayar = if (cicilan > 0) (terbayar / cicilan) else 0
        val pokokTerbayar = jadwal.take(angsuranDibayar).sumOf { it.pokok }
        val sisaPokok = (totalPokok - pokokTerbayar).coerceAtLeast(0)

        return RincianPinjaman(
            cicilanPerBulan = cicilan,
            totalBunga = totalBunga,
            totalBayar = totalBayar,
            totalPokok = totalPokok,
            terbayar = terbayar,
            sisaBayar = sisaBayar,
            sisaPokok = sisaPokok,
            angsuranDibayar = angsuranDibayar
        )
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

    // =====================
    // NOTIFIKASI JATUH TEMPO
    // =====================

    fun getUpcomingDues(kodePegawai: String, daysAhead: Int = 3): List<DueReminder> {
        val todayCal = java.util.Calendar.getInstance()
        val y = todayCal.get(java.util.Calendar.YEAR)
        val m = todayCal.get(java.util.Calendar.MONTH) // 0..11
        val d = todayCal.get(java.util.Calendar.DAY_OF_MONTH)

        // tanggal jatuh tempo default = tanggal 10
        val DUE_DAY = 10

        val list = mutableListOf<DueReminder>()

        pinjamanList
            .filter { it.kodePegawai == kodePegawai && it.status.equals("Disetujui", true) || it.status.equals("Aktif", true) }
            .forEach { p ->
                // hitung cicilan per bulan (pakai helper yang sudah ada; kalau kamu punya anuitas gunakan yg itu)
                val cicilan = hitungCicilanPerBulan(p.id).coerceAtLeast(0)

                // due di bulan ini:
                val dueCal = java.util.Calendar.getInstance().apply {
                    set(java.util.Calendar.YEAR, y)
                    set(java.util.Calendar.MONTH, m)
                    set(java.util.Calendar.DAY_OF_MONTH, DUE_DAY)
                    set(java.util.Calendar.HOUR_OF_DAY, 0)
                    set(java.util.Calendar.MINUTE, 0)
                    set(java.util.Calendar.SECOND, 0)
                    set(java.util.Calendar.MILLISECOND, 0)
                }

                // jika sudah lewat tanggal 10 bulan ini, geser ke bulan depan
                if (d > DUE_DAY) {
                    dueCal.add(java.util.Calendar.MONTH, 1)
                }

                // selisih hari
                val diffMs = dueCal.timeInMillis - todayCal.timeInMillis
                val diffDays = kotlin.math.ceil(diffMs / (1000.0 * 60 * 60 * 24)).toInt()

                if (diffDays in 0..daysAhead) {
                    val dueStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale("id","ID"))
                        .format(dueCal.time)
                    list.add(DueReminder(p.id, p.kodePegawai, dueStr, cicilan))
                }
            }

        return list
    }

    // =====================
    // Pengumuman Koperasi
    // =====================

    val pengumumanList = mutableListOf(
        Pengumuman(1, "Rapat Anggota Tahunan", "RAT akan dilaksanakan di aula kantor pukul 09.00 WIB.", "2025-10-18"),
        Pengumuman(2, "Update Suku Bunga", "Suku bunga pinjaman bulanan ditetapkan 1.2% flat efektif.", "2025-10-10"),
        Pengumuman(3, "Libur Operasional", "Koperasi libur tanggal 28-29 Okt untuk maintenance sistem.", "2025-10-28")
    )

    fun addPengumuman(judul: String, isi: String): Pengumuman {
        val idBaru = (pengumumanList.maxOfOrNull { it.id } ?: 0) + 1
        val tanggal = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale("id","ID"))
            .format(java.util.Date())

        val p = Pengumuman(
            id = idBaru,
            judul = judul,
            isi = isi,
            tanggal = tanggal
        )
        // taruh di awal agar langsung tampil paling atas
        pengumumanList.add(0, p)
        return p
    }


    // =====================
    // 🔔 Queue notifikasi keputusan pinjaman (untuk dikirim saat user login)
    // =====================

    data class DecisionNotification(
        val id: Int,
        val kodePegawai: String,
        val pinjamanId: Int,
        val decision: String,  // "disetujui" / "ditolak"
        val jumlah: Int,
        val tanggal: String
    )

    private val decisionQueue = mutableListOf<DecisionNotification>()

    fun enqueueDecisionNotification(
        kodePegawai: String,
        pinjamanId: Int,
        decision: String,
        jumlah: Int
    ) {
        val idBaru = (decisionQueue.maxOfOrNull { it.id } ?: 0) + 1
        val tanggal = dateFmt.format(Date())
        decisionQueue.add(
            DecisionNotification(
                id = idBaru,
                kodePegawai = kodePegawai,
                pinjamanId = pinjamanId,
                decision = decision,
                jumlah = jumlah,
                tanggal = tanggal
            )
        )
    }

    fun drainDecisionNotificationsFor(kodePegawai: String): List<DecisionNotification> {
        val list = decisionQueue.filter { it.kodePegawai == kodePegawai }
        if (list.isNotEmpty()) {
            decisionQueue.removeAll(list.toSet())
        }
        return list
    }

    // =====================
// 🔧 Pengaturan Pengurus Koperasi
// =====================

    var sukuBungaPinjamanGlobal: Double = 0.1      // default 10%
    var dendaKeterlambatanHarian: Double = 0.01    // default 1% per hari

    fun setSukuBungaBaru(persen: Double) {
        sukuBungaPinjamanGlobal = persen / 100.0
        pinjamanList.forEach { p ->
            if (p.status.equals("Proses", true) || p.status.equals("Disetujui", true)) {
                val idx = pinjamanList.indexOf(p)
                pinjamanList[idx] = p.copy(bunga = sukuBungaPinjamanGlobal)
            }
        }
    }

    fun setDendaKeterlambatanBaru(persenPerHari: Double) {
        dendaKeterlambatanHarian = persenPerHari / 100.0
    }

    // 🔹 Fungsi untuk mencatat pembayaran manual (oleh pengurus)
    fun catatPembayaranAngsuranAdmin(
        kodePegawai: String,
        pinjamanId: Int,
        jumlahBayar: Int,
        tanggal: String = getTodayDate()
    ) {
        historiPembayaranList.add(
            HistoriPembayaran(
                id = (historiPembayaranList.maxOfOrNull { it.id } ?: 0) + 1,
                kodePegawai = kodePegawai,
                pinjamanId = pinjamanId,
                tanggal = tanggal,
                jumlah = jumlahBayar,
                status = "Dibayar (Admin)"
            )
        )
    }

    // 🔹 Dapatkan daftar pinjaman aktif
    fun getDaftarPinjamanAktif(): List<Pinjaman> {
        return pinjamanList.filter { it.status.equals("Disetujui", true) || it.status.equals("Aktif", true) }
    }

    // 🔹 Dapatkan riwayat pelunasan (status Lunas)
    fun getDaftarPinjamanLunas(): List<Pinjaman> {
        return pinjamanList.filter { it.status.equals("Lunas", true) }
    }

}
