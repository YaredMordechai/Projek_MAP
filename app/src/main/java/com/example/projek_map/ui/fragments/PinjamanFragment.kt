package com.example.projek_map.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.Pinjaman
import com.example.projek_map.ui.adapters.PinjamanAdapter
import com.example.projek_map.ui.viewmodels.PinjamanViewModel
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.NumberFormat
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

    // ✅ untuk dialog histori + info sisa bayar
    private var lastHistoriPinjamanId: Int? = null
    private var lastPinjamanForHistori: Pinjaman? = null

    private val dataPending = mutableListOf<Pinjaman>()
    private val dataAktif = mutableListOf<Pinjaman>()
    private val dataSelesai = mutableListOf<Pinjaman>()

    private var isAdmin: Boolean = false

    private val vm: PinjamanViewModel by viewModels()

    // ==== Upload Bukti ====
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestCameraPermission: ActivityResultLauncher<String>
    private var currentPinjamanForUpload: Int? = null
    private var pendingJumlahForUpload: Int? = null
    private var pendingStatusForUpload: String = "Menunggu Verifikasi"
    private var pendingCameraUri: Uri? = null

    // base64 yang akan dikirim ke backend
    private var buktiBase64: String? = null
    private var buktiExt: String = "jpg"

    // ====== Notifikasi keputusan pinjaman (anggota) ======
    private val statusCachePrefsName = "pinjaman_status_cache"
    private val notifChannelId = "pinjaman_decision_channel"
    private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        // ===== Observers (MVVM) =====
        vm.pending.observe(viewLifecycleOwner) { list ->
            dataPending.clear()
            dataPending.addAll(list)
            adapterPending.notifyDataSetChanged()
            updateStatusCard()
        }
        vm.aktif.observe(viewLifecycleOwner) { list ->
            dataAktif.clear()
            dataAktif.addAll(list)
            adapterAktif.notifyDataSetChanged()
            updateStatusCard()
        }
        vm.selesai.observe(viewLifecycleOwner) { list ->
            dataSelesai.clear()
            dataSelesai.addAll(list)
            adapterSelesai.notifyDataSetChanged()
            updateStatusCard()
        }

        // cek perubahan status untuk notifikasi keputusan
        vm.allPinjaman.observe(viewLifecycleOwner) { all ->
            if (!isAdmin) handleDecisionNotifications(all)
        }

        vm.toast.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) toast(msg)
        }

        // ✅ Dialog histori + info sisa bayar
        vm.historiDialogText.observe(viewLifecycleOwner) { text ->
            if (!text.isNullOrBlank()) {
                val id = lastHistoriPinjamanId ?: return@observe
                val pinjaman = lastPinjamanForHistori

                val infoSisa = if (pinjaman != null) {
                    val pokok = pinjaman.jumlah
                    val bunga = pinjaman.bunga
                    val totalTagihan = (pokok + (pokok * bunga)).toInt()
                    val terbayar = pinjaman.angsuranTerbayar
                    val sisa = (totalTagihan - terbayar).coerceAtLeast(0)

                    fun rupiahInt(x: Int) =
                        "Rp " + String.format("%,d", x).replace(',', '.')

                    "\n\n---\n" +
                            "Total Tagihan : ${rupiahInt(totalTagihan)}\n" +
                            "Sudah Dibayar : ${rupiahInt(terbayar)}\n" +
                            "Sisa Bayar    : ${rupiahInt(sisa)}"
                } else ""

                AlertDialog.Builder(requireContext())
                    .setTitle("Histori Pembayaran #$id")
                    .setMessage(text + infoSisa)
                    .setPositiveButton("Tutup", null)
                    .show()

                lastHistoriPinjamanId = null
                lastPinjamanForHistori = null
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        if (!isAdmin) refreshAllListsFromApi()
    }

    // ====== Notifikasi keputusan pinjaman (anggota) ======
    private fun handleDecisionNotifications(all: List<Pinjaman>) {
        if (all.isEmpty()) return

        val prefs = requireContext().getSharedPreferences(statusCachePrefsName, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val decidedNow = mutableListOf<String>()

        for (p in all) {
            val id = p.id
            val newStatus = p.status.orEmpty().trim()
            val newLower = newStatus.lowercase()

            val key = "status_$id"
            val oldStatus = prefs.getString(key, null)?.trim()
            val oldLower = oldStatus?.lowercase()

            val oldIsPending = oldLower == "proses" || oldLower == "menunggu"
            val newIsDecision = newLower == "disetujui" || newLower == "ditolak"

            if (oldStatus != null && oldIsPending && newIsDecision) {
                val jumlahText = try { rupiah.format(p.jumlah) } catch (_: Exception) { "Rp ${p.jumlah}" }
                decidedNow.add("Pinjaman #$id ($jumlahText) $newStatus")
            }

            editor.putString(key, newStatus)
        }

        editor.apply()

        if (decidedNow.isNotEmpty()) {
            showLocalNotification(
                title = "Keputusan Pinjaman",
                message = "Ada ${decidedNow.size} keputusan baru. Buka menu Pinjaman untuk detail."
            )

            AlertDialog.Builder(requireContext())
                .setTitle("Update Keputusan Pinjaman")
                .setMessage(decidedNow.joinToString("\n\n"))
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun showLocalNotification(title: String, message: String) {
        val nm = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                notifChannelId,
                "Notifikasi Pinjaman",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            nm.createNotificationChannel(ch)
        }

        val notif = NotificationCompat.Builder(requireContext(), notifChannelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .build()

        nm.notify((System.currentTimeMillis() % 100000).toInt(), notif)
    }

    // ✅ versi yang benar (pakai Pinjaman, bukan Int)
    private fun showHistoriPembayaranFromApi(pinjaman: Pinjaman) {
        lastHistoriPinjamanId = pinjaman.id
        lastPinjamanForHistori = pinjaman
        vm.loadHistoriPembayaranText(pinjaman.id)
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

        btnAjukan.isEnabled = false
        vm.ajukanPinjaman(kodePegawai, nominal, tenor)
        btnAjukan.isEnabled = true

        inputNominal.setText("")
        inputTenor.setText("")
    }

    private fun refreshAllListsFromApi() {
        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai().orEmpty()
        if (kodePegawai.isBlank()) {
            txtPinjamanAktif.text = "Kode pegawai kosong."
            return
        }
        vm.refreshAllListsFromApi(kodePegawai)
    }

    private fun updateStatusCard() {
        val semuaPinjaman = dataAktif + dataPending
        if (semuaPinjaman.isEmpty()) {
            txtPinjamanAktif.text = "Belum ada pinjaman aktif atau pengajuan."
            return
        }

        val p = semuaPinjaman.firstOrNull()
        txtPinjamanAktif.text = if (p != null) {
            "Pinjaman Terbaru: #${p.id} - ${p.status}"
        } else {
            "Pinjaman Terbaru: -"
        }
    }

    // ✅ Hapus tombol "Lihat Rincian" (sesuai request kamu)
    private fun showPinjamanDetailDialog(pinjaman: Pinjaman) {
        val metode = if ((pinjaman.metode ?: "").equals("flat", true)) "flat" else "anuitas"

        AlertDialog.Builder(requireContext())
            .setTitle("Detail Pinjaman #${pinjaman.id}")
            .setMessage(
                "Status: ${pinjaman.status}\n" +
                        "Jumlah: Rp ${pinjaman.jumlah}\n" +
                        "Tenor: ${pinjaman.tenor} bulan\n" +
                        "Metode: $metode"
            )
            .setNeutralButton("Histori Pembayaran") { _, _ ->
                showHistoriPembayaranFromApi(pinjaman)
            }
            .setPositiveButton("Bayar Angsuran") { _, _ ->
                showBayarDialog(pinjaman)
            }
            .setNegativeButton("Tutup", null)
            .show()
    }

    // ✅ bayar wajib upload bukti
    private fun showBayarDialog(pinjaman: Pinjaman) {
        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai().orEmpty()
        if (kodePegawai.isBlank()) {
            toast("Kode pegawai kosong. Silakan login ulang."); return
        }

        val defaultJumlah = pinjaman.cicilanPerBulan ?: run {
            val t = if (pinjaman.tenor <= 0) 1 else pinjaman.tenor
            pinjaman.jumlah / t
        }

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 0)
        }

        val tv = TextView(requireContext()).apply {
            text = "Isi nominal pembayaran (Rp):"
        }

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
            .setPositiveButton("Pilih Bukti & Kirim") { _, _ ->
                val jumlah = edtJumlah.text?.toString()?.trim()
                    ?.replace(".", "")?.replace(",", "")?.toIntOrNull()
                if (jumlah == null || jumlah <= 0) {
                    toast("Nominal tidak valid.")
                    return@setPositiveButton
                }

                currentPinjamanForUpload = pinjaman.id
                pendingJumlahForUpload = jumlah
                pendingStatusForUpload = "Menunggu Verifikasi"
                showUploadChooser()
            }
            .setNegativeButton("Batal", null)
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
        } catch (_: Exception) {
            null
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val input = requireContext().contentResolver.openInputStream(uri) ?: return null
            val bytes = input.readBytes()
            input.close()
            android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
        } catch (_: Exception) {
            null
        }
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
            toast("Nominal pembayaran belum diisi.")
            return
        }

        buktiBase64 = uriToBase64(uri)
        buktiExt = "jpg"

        if (buktiBase64.isNullOrBlank()) {
            toast("Gagal membaca gambar. Coba ulangi.")
            return
        }

        val status = pendingStatusForUpload

        // reset state
        currentPinjamanForUpload = null
        pendingJumlahForUpload = null
        pendingStatusForUpload = "Menunggu Verifikasi"

        // submit
        vm.submitPembayaranAngsuran(
            kodePegawai = kodePegawai,
            pinjamanId = pinjamanId,
            jumlah = jumlah,
            status = status,
            buktiBase64 = buktiBase64,
            buktiExt = buktiExt
        )

        buktiBase64 = null
        buktiExt = "jpg"
    }

    private fun toast(msg: String) {
        android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show()
    }
}
