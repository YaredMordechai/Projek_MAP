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
    private var pendingCameraUri: Uri? = null

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

        val r = calcRincianAnuitas(pinjaman.jumlah, pinjaman.tenor, pinjaman.bunga)
        val bungaPersenTahun = (pinjaman.bunga * 100).toInt()

        val message = """
            Pokok Pinjaman  : Rp ${formatRupiah(pinjaman.jumlah.toDouble())}
            Bunga ($bungaPersenTahun%) : (anuitas)
            Total Cicilan   : Rp ${formatRupiah(r.totalBayar.toDouble())}
            Tenor           : ${pinjaman.tenor} bulan
            Angsuran/Bulan  : Rp ${formatRupiah(r.cicilanPerBulan.toDouble())}
            Status          : ${pinjaman.status}
        """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle("Rincian Pinjaman #${pinjaman.id}")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .setNeutralButton("Lihat Riwayat Angsuran") { _, _ -> showHistoriPembayaranFromApi(pinjaman.id) }
            .setNegativeButton("Unggah Bukti") { _, _ ->
                currentPinjamanForUpload = pinjaman.id
                showUploadChooser()
            }
            .show()
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

        lifecycleScope.launch {
            val res = repo.addHistoriBukti(
                kodePegawai = kodePegawai,
                pinjamanId = pinjamanId,
                jumlah = 0,
                status = "Bukti Diupload",
                buktiUri = uri.toString()
            )
            if (res.success) toast("Bukti pembayaran tersimpan.")
            else toast(res.message ?: "Gagal menyimpan bukti")
        }
    }

    // ====== Kalkulator anuitas sederhana ======
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
