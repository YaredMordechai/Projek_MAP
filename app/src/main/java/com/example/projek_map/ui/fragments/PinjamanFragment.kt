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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.ui.adapters.HistoriPembayaranAdapter
import com.example.projek_map.ui.adapters.PinjamanAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
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

    // ==== Upload Bukti ====
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestCameraPermission: ActivityResultLauncher<String>
    private var currentPinjamanForUpload: Int? = null
    private var pendingCameraUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pinjaman, container, false)

        // --- Launchers ---
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) onBuktiSelected(uri) else toast("Tidak ada gambar terpilih.")
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val uri = pendingCameraUri
            if (success && uri != null) {
                onBuktiSelected(uri)
            } else {
                toast("Gagal mengambil foto.")
            }
            pendingCameraUri = null
        }

        requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                launchCamera()
            } else {
                toast("Izin kamera ditolak.")
            }
        }

        // Ambil flag admin dari arguments
        isAdmin = arguments?.getBoolean("isAdmin", false) ?: false

        // Inisialisasi view
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

        loadDummyPinjaman()
        updateStatusCard()

        btnAjukan.setOnClickListener {
            val nominalText = inputNominal.text?.toString()?.trim()
            val tenorText = inputTenor.text?.toString()?.trim()

            if (nominalText.isNullOrEmpty() || tenorText.isNullOrEmpty()) {
                toast("Isi semua kolom dulu"); return@setOnClickListener
            }

            val nominal = nominalText.toIntOrNull()
            val tenor = tenorText.toIntOrNull()
            if (nominal == null || tenor == null) {
                toast("Nominal dan Tenor harus berupa angka"); return@setOnClickListener
            }

            val newPinjaman = Pinjaman(
                id = DummyUserData.pinjamanList.size + 1,
                kodePegawai = "EMP001",
                jumlah = nominal,
                tenor = tenor,
                bunga = 0.10,
                angsuranTerbayar = 0,
                status = "Proses"
            )

            DummyUserData.pinjamanList.add(0, newPinjaman)
            dataPending.add(0, newPinjaman)
            adapterPending.notifyItemInserted(0)
            rvPending.scrollToPosition(0)

            inputNominal.setText("")
            inputTenor.setText("")
            toast("Pinjaman diajukan!")

            // 🔹 Refresh data & update total semua pinjaman (aktif + pending)
            loadDummyPinjaman()
            updateStatusCard()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        if (!isAdmin) refreshAllLists()
        showDuePopupIfAny()
    }

    private fun showDuePopupIfAny() {
        val kodePegawai = "EMP001"
        val upcoming = DummyUserData.getUpcomingDues(kodePegawai, daysAhead = 3)
        if (upcoming.isEmpty()) return

        val text = StringBuilder().apply {
            appendLine("Ada cicilan yang akan jatuh tempo dalam 3 hari:")
            appendLine()
            upcoming.forEach { r ->
                appendLine("• Pinjaman #${r.pinjamanId} | Jatuh tempo: ${r.dueDate}")
                appendLine("  Angsuran: Rp ${formatRupiah(r.nominalCicilan.toDouble())}")
            }
        }.toString()

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Pengingat Jatuh Tempo")
            .setMessage(text)
            .setPositiveButton("Oke", null)
            .show()
    }

    private fun refreshAllLists() {
        loadDummyPinjaman()
        adapterPending.notifyDataSetChanged()
        adapterAktif.notifyDataSetChanged()
        adapterSelesai.notifyDataSetChanged()
        updateStatusCard()
    }

    private fun loadDummyPinjaman() {
        dataPending.clear(); dataAktif.clear(); dataSelesai.clear()
        DummyUserData.pinjamanList.forEach { p ->
            when (p.status.lowercase()) {
                "proses", "menunggu" -> dataPending.add(p)
                "disetujui", "aktif" -> dataAktif.add(p)
                "selesai", "lunas" -> dataSelesai.add(p)
            }
        }
    }

    // ✅ Sudah diperbarui: menghitung total semua pinjaman (aktif + pending)
    private fun updateStatusCard() {
        val semuaPinjaman = dataAktif + dataPending // 🔹 gabungkan dua list

        if (semuaPinjaman.isEmpty()) {
            txtPinjamanAktif.text = "Belum ada pinjaman aktif atau pengajuan."
            return
        }

        val totalPinjaman = semuaPinjaman.sumOf { it.jumlah.toDouble() }
        val totalTenor = semuaPinjaman.sumOf { it.tenor }
        val contoh = semuaPinjaman.first()

        txtPinjamanAktif.text = buildString {
            append("Total Pinjaman Diajukan: Rp ${formatRupiah(totalPinjaman)}\n")
            append("\n")
            append("Jumlah Pengajuan: ${semuaPinjaman.size} pinjaman\n")
            append("\n")
            append("Disetujui: Rp ${formatRupiah(contoh.jumlah.toDouble())} (${contoh.tenor} bulan)")
        }
    }

    private fun showPinjamanDetailDialog(pinjaman: Pinjaman) {
        val r = DummyUserData.getRincianPinjamanAnuitas(pinjaman.id)
        val bungaPersenTahun = (pinjaman.bunga * 100).toInt()

        val message = """
        Pokok Pinjaman  : Rp ${formatRupiah(pinjaman.jumlah.toDouble())}
        Bunga ($bungaPersenTahun%) : (anuitas)
        Total Cicilan   : Rp ${formatRupiah(r.totalBayar.toDouble())}
        Tenor           : ${pinjaman.tenor} bulan
        Angsuran/Bulan  : Rp ${formatRupiah(r.cicilanPerBulan.toDouble())}
        Terbayar        : Rp ${formatRupiah(r.terbayar.toDouble())}
        Sisa Cicilan    : Rp ${formatRupiah(r.sisaBayar.toDouble())}
        Sisa Pokok      : Rp ${formatRupiah(r.sisaPokok.toDouble())}
        Status          : ${pinjaman.status}
        """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle("Rincian Pinjaman #${pinjaman.id}")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .setNeutralButton("Lihat Riwayat Angsuran") { _, _ ->
                showHistoriPembayaran(pinjaman.id)
            }
            .setNegativeButton("Unggah Bukti") { _, _ ->
                currentPinjamanForUpload = pinjaman.id
                showUploadChooser()
            }
            .show()
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
        val granted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            launchCamera()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCamera() {
        val uri = createImageUri() ?: run {
            toast("Gagal menyiapkan file foto"); return
        }
        pendingCameraUri = uri
        takePictureLauncher.launch(uri)
    }

    private fun createImageUri(): Uri? {
        return try {
            val dir = requireContext().getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
            val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val imageFile = File(dir, "IMG_BUKTI_${time}.jpg")
            FileProvider.getUriForFile(
                requireContext(),
                "com.example.projek_map.fileprovider",
                imageFile
            )
        } catch (e: Exception) { null }
    }

    private fun onBuktiSelected(uri: Uri) {
        val pinjamanId = currentPinjamanForUpload
        if (pinjamanId == null) {
            toast("Pinjaman tidak diketahui."); return
        }
        DummyUserData.addHistoriPembayaranWithBukti(
            kodePegawai = "EMP001",
            pinjamanId = pinjamanId,
            jumlah = 0,
            status = "Bukti Diupload",
            buktiUri = uri.toString()
        )
        toast("Bukti pembayaran tersimpan (dummy).")
        refreshAllLists()
    }

    private fun showHistoriPembayaran(pinjamanId: Int) {
        val histori = DummyUserData.getHistoriPembayaran(pinjamanId)
        val view = layoutInflater.inflate(R.layout.dialog_histori_pembayaran, null)
        val rvHistori = view.findViewById<RecyclerView>(R.id.rvHistoriPembayaran)
        rvHistori.layoutManager = LinearLayoutManager(requireContext())
        rvHistori.adapter = HistoriPembayaranAdapter(histori)

        AlertDialog.Builder(requireContext())
            .setTitle("Histori Pembayaran")
            .setView(view)
            .setPositiveButton("Tutup", null)
            .show()
    }

    private fun tampilkanLaporanBulanan() {
        val kodePegawai = "EMP001"
        val bulanSekarang = Calendar.getInstance().get(Calendar.MONTH) + 1
        val totalSimpanan = DummyUserData.getTotalSimpanan(kodePegawai)
        val totalPinjaman = DummyUserData.getTotalPinjamanAktif(kodePegawai)
        val totalAngsuran = DummyUserData.getTotalAngsuranBulanan(kodePegawai, bulanSekarang)
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        view?.findViewById<TextView>(R.id.txtTotalSimpanan)?.text =
            "Total Simpanan: ${formatter.format(totalSimpanan)}"
        view?.findViewById<TextView>(R.id.txtTotalPinjaman)?.text =
            "Total Pinjaman Aktif: ${formatter.format(totalPinjaman)}"
        view?.findViewById<TextView>(R.id.txtTotalAngsuran)?.text =
            "Total Angsuran Bulan Ini: ${formatter.format(totalAngsuran)}"
    }

    private fun formatRupiah(value: Double): String =
        String.format("%,.0f", value).replace(',', '.')

    private fun toast(msg: String) =
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}
