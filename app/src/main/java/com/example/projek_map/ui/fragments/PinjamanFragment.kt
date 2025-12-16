package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.EditText
import android.widget.LinearLayout
import android.text.InputType
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.HistoriPembayaran
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.data.PinjamanRepository
import com.example.projek_map.ui.adapters.PinjamanAdapter
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PinjamanFragment : Fragment() {

    private lateinit var rvPending: RecyclerView
    private lateinit var rvPinjamanAktif: RecyclerView
    private lateinit var rvSelesai: RecyclerView
    private lateinit var btnAjukan: MaterialButton
    private lateinit var inputNominal: TextInputEditText
    private lateinit var inputTenor: TextInputEditText
    private lateinit var txtPinjamanAktif: TextView

    private lateinit var adapterPending: PinjamanAdapter
    private lateinit var adapterAktif: PinjamanAdapter
    private lateinit var adapterSelesai: PinjamanAdapter

    private val dataPending = mutableListOf<Pinjaman>()
    private val dataAktif = mutableListOf<Pinjaman>()
    private val dataSelesai = mutableListOf<Pinjaman>()

    private var isAdmin: Boolean = false
    private val repo = PinjamanRepository()

    // ==== Upload Bukti ====
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestCameraPermission: ActivityResultLauncher<String>
    private var currentPinjamanForUpload: Int? = null
    private var pendingJumlahForUpload: Int? = null
    private var pendingStatusForUpload: String = "Menunggu Verifikasi"
    private var pendingCameraUri: Uri? = null

    // ✅ baru: base64 yang akan dikirim ke backend
    private var buktiBase64: String? = null
    private var buktiExt: String = "jpg"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_pinjaman, container, false)

        // Launchers
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) onBuktiSelected(uri) else toast("Tidak ada gambar terpilih.")
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val uri = pendingCameraUri
            if (success && uri != null) onBuktiSelected(uri) else toast("Gagal mengambil foto.")
            pendingCameraUri = null
        }

        requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) launchCamera() else toast("Izin kamera ditolak.")
        }

        isAdmin = arguments?.getBoolean("isAdmin", false) ?: false

        // View init
        txtPinjamanAktif = view.findViewById(R.id.txtPinjamanAktif)
        inputNominal = view.findViewById(R.id.inputNominalPinjaman)
        inputTenor = view.findViewById(R.id.inputTenor)
        btnAjukan = view.findViewById(R.id.btnAjukanPinjaman)
        rvPending = view.findViewById(R.id.rvPendingPinjaman)
        rvPinjamanAktif = view.findViewById(R.id.rvRiwayatPinjaman)
        rvSelesai = view.findViewById(R.id.rvRiwayatSelesai)

        if (isAdmin) {
            txtPinjamanAktif.text = "Admin tidak memiliki data pinjaman"
            inputNominal.visibility = View.GONE
            inputTenor.visibility = View.GONE
            btnAjukan.visibility = View.GONE
            rvPending.visibility = View.GONE
            rvPinjamanAktif.visibility = View.GONE
            rvSelesai.visibility = View.GONE
            return view
        }

        rvPending.layoutManager = LinearLayoutManager(requireContext())
        rvPinjamanAktif.layoutManager = LinearLayoutManager(requireContext())
        rvSelesai.layoutManager = LinearLayoutManager(requireContext())

        adapterPending = PinjamanAdapter(dataPending) { showPinjamanDetailDialog(it) }
        adapterAktif = PinjamanAdapter(dataAktif) { showPinjamanDetailDialog(it) }
        adapterSelesai = PinjamanAdapter(dataSelesai) { showPinjamanDetailDialog(it) }

        rvPending.adapter = adapterPending
        rvPinjamanAktif.adapter = adapterAktif
        rvSelesai.adapter = adapterSelesai

        btnAjukan.setOnClickListener { ajukanPinjaman() }

        return view
    }

    override fun onResume() {
        super.onResume()
        if (!isAdmin) refreshAllListsFromApi()
    }

    private fun ajukanPinjaman() {
        val nominalText = inputNominal.text?.toString()?.trim()
        val tenorText = inputTenor.text?.toString()?.trim()

        if (nominalText.isNullOrEmpty() || tenorText.isNullOrEmpty()) {
            toast("Isi semua kolom dulu"); return
        }

        val nominal = nominalText.toIntOrNull()
        val tenor = tenorText.toIntOrNull()
        if (nominal == null || tenor == null) {
            toast("Nominal dan Tenor harus berupa angka"); return
        }

        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai().orEmpty()
        if (kodePegawai.isBlank()) {
            toast("Kode pegawai kosong. Silakan login ulang."); return
        }

        lifecycleScope.launch {
            btnAjukan.isEnabled = false
            val res = repo.createPinjaman(kodePegawai, nominal, tenor)
            btnAjukan.isEnabled = true

            if (res.success) {
                toast("Pinjaman diajukan!")
                inputNominal.setText("")
                inputTenor.setText("")
                refreshAllListsFromApi()
            } else {
                toast(res.message ?: "Gagal mengajukan pinjaman")
            }
        }
    }

    private fun refreshAllListsFromApi() {
        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai().orEmpty()
        if (kodePegawai.isBlank()) {
            txtPinjamanAktif.text = "Kode pegawai kosong."
            return
        }

        lifecycleScope.launch {
            val res = repo.getPinjamanUser(kodePegawai)
            if (!res.success) {
                toast(res.message ?: "Gagal memuat pinjaman")
                return@launch
            }

            val list = res.data ?: emptyList()
            dataPending.clear(); dataAktif.clear(); dataSelesai.clear()

            list.forEach { p ->
                when (p.status.lowercase()) {
                    "proses", "menunggu" -> dataPending.add(p)
                    "disetujui", "aktif" -> dataAktif.add(p)
                    "selesai", "lunas" -> dataSelesai.add(p)
                    else -> dataPending.add(p)
                }
            }

            adapterPending.notifyDataSetChanged()
            adapterAktif.notifyDataSetChanged()
            adapterSelesai.notifyDataSetChanged()

            updateStatusCard()
        }
    }

    private fun updateStatusCard() {
        val semuaPinjaman = dataAktif + dataPending
        if (semuaPinjaman.isEmpty()) {
            txtPinjamanAktif.text = "Belum ada pinjaman aktif atau pengajuan."
            return
        }

        val totalPinjaman = semuaPinjaman.sumOf { it.jumlah.toDouble() }
        val contoh = semuaPinjaman.first()

        txtPinjamanAktif.text = buildString {
            append("Total Pinjaman Diajukan: Rp ${formatRupiah(totalPinjaman)}\n\n")
            append("Jumlah Pengajuan: ${semuaPinjaman.size} pinjaman\n\n")
            append("Contoh: Rp ${formatRupiah(contoh.jumlah.toDouble())} (${contoh.tenor} bulan) - ${contoh.status}")
        }
    }

    private fun showPinjamanDetailDialog(pinjaman: Pinjaman) {
        lifecycleScope.launch {
            val rincResp = repo.getRincianPinjamanDb(pinjaman.id, "anuitas")
            val rinc = if (rincResp.success) rincResp.data else null

            val fallback = calcRincianAnuitas(pinjaman.jumlah, pinjaman.tenor, pinjaman.bunga)

            val cicilan = rinc?.cicilanPerBulan ?: fallback.cicilanPerBulan
            val totalBayar = rinc?.totalBayar ?: fallback.totalBayar
            val terbayar = rinc?.terbayar ?: 0
            val sisaBayar = rinc?.sisaBayar ?: (totalBayar - terbayar).coerceAtLeast(0)
            val sisaAngsuran = if (cicilan > 0) (sisaBayar / cicilan) else 0

            val bungaPersenTahun = (pinjaman.bunga * 100).toInt()

            val message = """
                Pokok Pinjaman     : Rp ${formatRupiah(pinjaman.jumlah.toDouble())}
                Metode             : ANUITAS
                Bunga Tahunan      : $bungaPersenTahun%
                Tenor              : ${pinjaman.tenor} bulan

                Angsuran / Bulan   : Rp ${formatRupiah(cicilan.toDouble())}
                Total Cicilan      : Rp ${formatRupiah(totalBayar.toDouble())}

                Terbayar           : Rp ${formatRupiah(terbayar.toDouble())}
                Sisa Bayar         : Rp ${formatRupiah(sisaBayar.toDouble())}
                Est. Sisa Angsuran : ${sisaAngsuran} bulan

                Status             : ${pinjaman.status}
            """.trimIndent()

            AlertDialog.Builder(requireContext())
                .setTitle("Rincian Pinjaman #${pinjaman.id}")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setNeutralButton("Lihat Riwayat Angsuran") { _, _ ->
                    showHistoriPembayaranFromApi(pinjaman.id)
                }
                .setNegativeButton("Bayar Angsuran") { _, _ ->
                    showBayarAngsuranDialog(pinjaman)
                }
                .show()
        }
    }

    private fun showBayarAngsuranDialog(pinjaman: Pinjaman) {
        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai().orEmpty()
        if (kodePegawai.isBlank()) {
            toast("Kode pegawai kosong. Silakan login ulang."); return
        }

        lifecycleScope.launch {
            val rincResp = repo.getRincianPinjamanDb(pinjaman.id, "anuitas")
            val rinc = if (rincResp.success) rincResp.data else null

            val fallback = calcRincianAnuitas(pinjaman.jumlah, pinjaman.tenor, pinjaman.bunga)
            val defaultJumlah = (rinc?.cicilanPerBulan ?: fallback.cicilanPerBulan).coerceAtLeast(0)

            val info = buildString {
                append("Rekomendasi cicilan/bulan: Rp ${formatRupiah(defaultJumlah.toDouble())}\n")
                if (rinc != null) append("Sisa bayar: Rp ${formatRupiah(rinc.sisaBayar.toDouble())}\n")
                append("\nIsi nominal pembayaran (Rp):")
            }

            val container = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(48, 24, 48, 0)
            }

            val tv = TextView(requireContext()).apply { text = info }

            val edtJumlah = EditText(requireContext()).apply {
                inputType = InputType.TYPE_CLASS_NUMBER
                hint = "Contoh: $defaultJumlah"
                setText(defaultJumlah.toString())
            }

            container.addView(tv)
            container.addView(edtJumlah)

            AlertDialog.Builder(requireContext())
                .setTitle("Bayar Angsuran #${pinjaman.id}")
                .setView(container)
                // Bayar tanpa bukti
                .setPositiveButton("Bayar") { _, _ ->
                    val jumlah = edtJumlah.text?.toString()?.trim()
                        ?.replace(".", "")?.replace(",", "")?.toIntOrNull()
                    if (jumlah == null || jumlah <= 0) {
                        toast("Nominal tidak valid.")
                        return@setPositiveButton
                    }
                    buktiBase64 = null
                    buktiExt = "jpg"
                    submitPembayaranAngsuran(
                        kodePegawai = kodePegawai,
                        pinjamanId = pinjaman.id,
                        jumlah = jumlah
                    )
                }
                // Pilih bukti + bayar
                .setNeutralButton("Pilih Bukti & Bayar") { _, _ ->
                    val jumlah = edtJumlah.text?.toString()?.trim()
                        ?.replace(".", "")?.replace(",", "")?.toIntOrNull()
                    if (jumlah == null || jumlah <= 0) {
                        toast("Nominal tidak valid.")
                        return@setNeutralButton
                    }
                    currentPinjamanForUpload = pinjaman.id
                    pendingJumlahForUpload = jumlah
                    pendingStatusForUpload = "Menunggu Verifikasi"
                    showUploadChooser()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val input = requireContext().contentResolver.openInputStream(uri) ?: return null
            val bytes = input.readBytes()
            input.close()
            android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
        } catch (e: Exception) { null }
    }

    private fun submitPembayaranAngsuran(
        kodePegawai: String,
        pinjamanId: Int,
        jumlah: Int
    ) {
        lifecycleScope.launch {
            val status = if (buktiBase64.isNullOrBlank()) "Dibayar (User)" else pendingStatusForUpload

            // ✅ PERUBAHAN: kirim base64 ke backend (bukan uri lokal)
            val res = repo.addHistoriBukti(
                kodePegawai = kodePegawai,
                pinjamanId = pinjamanId,
                jumlah = jumlah,
                status = status,
                buktiBase64 = buktiBase64,
                buktiExt = buktiExt
            )

            // reset state upload/payment
            currentPinjamanForUpload = null
            pendingJumlahForUpload = null
            pendingStatusForUpload = "Menunggu Verifikasi"
            buktiBase64 = null
            buktiExt = "jpg"

            if (res.success) {
                toast("Pembayaran tercatat.")
                refreshAllListsFromApi()
            } else {
                toast(res.message ?: "Gagal mencatat pembayaran")
            }
        }
    }

    private fun showHistoriPembayaranFromApi(pinjamanId: Int) {
        lifecycleScope.launch {
            try {
                val resp = com.example.projek_map.api.ApiClient.apiService.getHistoriPembayaran(pinjamanId)
                val body = resp.body()
                if (!resp.isSuccessful || body?.success != true) {
                    toast(body?.message ?: "Gagal ambil histori")
                    return@launch
                }

                val list: List<HistoriPembayaran> = body.data ?: emptyList()

                val text = if (list.isEmpty()) {
                    "Belum ada pembayaran untuk pinjaman ini."
                } else {
                    list.joinToString("\n\n") { h ->
                        val bukti = if (h.buktiPembayaranUri.isNullOrEmpty()) "-" else "✓"
                        "Tanggal: ${h.tanggal}\nJumlah: Rp ${h.jumlah}\nStatus: ${h.status}\nBukti: $bukti"
                    }
                }

                AlertDialog.Builder(requireContext())
                    .setTitle("Histori Pembayaran #$pinjamanId")
                    .setMessage(text)
                    .setPositiveButton("Tutup", null)
                    .show()

            } catch (e: Exception) {
                toast("Error: ${e.message}")
            }
        }
    }

    private fun showUploadChooser() {
        val items = arrayOf("Ambil Foto", "Pilih dari Galeri")
        AlertDialog.Builder(requireContext())
            .setTitle("Unggah Bukti Pembayaran")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> checkAndOpenCamera()
                    1 -> pickImageLauncher.launch("image/*")
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun checkAndOpenCamera() {
        val granted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        if (granted) launchCamera() else requestCameraPermission.launch(Manifest.permission.CAMERA)
    }

    private fun launchCamera() {
        val uri = createImageUri() ?: run { toast("Gagal menyiapkan file foto"); return }
        pendingCameraUri = uri
        takePictureLauncher.launch(uri)
    }

    private fun createImageUri(): Uri? {
        return try {
            val dir = requireContext().getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
            val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val imageFile = File(dir, "IMG_BUKTI_${time}.jpg")
            FileProvider.getUriForFile(requireContext(), "com.example.projek_map.fileprovider", imageFile)
        } catch (_: Exception) { null }
    }

    private fun onBuktiSelected(uri: Uri) {
        val pinjamanId = currentPinjamanForUpload ?: run {
            toast("Pinjaman tidak diketahui."); return
        }

        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai().orEmpty()
        if (kodePegawai.isBlank()) {
            toast("Kode pegawai kosong. Silakan login ulang."); return
        }

        val jumlah = pendingJumlahForUpload
        if (jumlah == null || jumlah <= 0) {
            toast("Nominal pembayaran belum diisi. Silakan ulangi dari tombol 'Bayar Angsuran'.")
            return
        }

        // ✅ PERUBAHAN: ubah uri → base64, lalu submit
        buktiBase64 = uriToBase64(uri)
        buktiExt = "jpg"

        if (buktiBase64.isNullOrBlank()) {
            toast("Gagal membaca file bukti. Coba ulangi."); return
        }

        submitPembayaranAngsuran(
            kodePegawai = kodePegawai,
            pinjamanId = pinjamanId,
            jumlah = jumlah
        )
    }

    private data class Rincian(val cicilanPerBulan: Int, val totalBayar: Int)

    private fun calcRincianAnuitas(pokok: Int, tenorBulan: Int, bungaTahunan: Double): Rincian {
        val r = bungaTahunan / 12.0
        if (tenorBulan <= 0) return Rincian(0, 0)
        if (r == 0.0) {
            val cicilan = pokok / tenorBulan
            return Rincian(cicilan, cicilan * tenorBulan)
        }
        val p = pokok.toDouble()
        val n = tenorBulan.toDouble()
        val cicilan = (p * r) / (1 - Math.pow(1 + r, -n))
        val cicilanInt = cicilan.toInt()
        return Rincian(cicilanInt, cicilanInt * tenorBulan)
    }

    private fun formatRupiah(value: Double): String =
        String.format("%,.0f", value).replace(',', '.')

    private fun toast(msg: String) =
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}
