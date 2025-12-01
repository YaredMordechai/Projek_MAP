package com.example.projek_map.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.BuktiPembayaranAnggota
import com.example.projek_map.data.HistoriSimpanan
import com.example.projek_map.data.KoperasiDatabase
import com.example.projek_map.data.Simpanan
import com.example.projek_map.data.DummyUserData   // hanya dipakai untuk konstanta nominal default
import com.example.projek_map.ui.adapters.HistoriSimpananAdapter
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SimpananFragment : Fragment() {

    private lateinit var tvTotalSaldo: TextView
    private lateinit var rvHistori: RecyclerView
    private lateinit var btnSetor: MaterialButton
    private lateinit var btnTarik: MaterialButton
    private lateinit var btnUploadBukti: MaterialButton
    private lateinit var btnAmbilFoto: MaterialButton
    private lateinit var ivBuktiPreview: ImageView

    private lateinit var inputJumlah: TextInputEditText
    private lateinit var dropdownJenis: MaterialAutoCompleteTextView

    private lateinit var prefManager: PrefManager
    private lateinit var adapterHistori: HistoriSimpananAdapter
    private val dataHistori = mutableListOf<HistoriSimpanan>()

    private val jenisList = listOf("Simpanan Pokok", "Simpanan Wajib", "Simpanan Sukarela")

    // nominal default masih mengikuti DummyUserData (hanya konstanta, bukan list data)
    private val nilaiPokok = DummyUserData.DEFAULT_POKOK
    private val nilaiWajib = DummyUserData.DEFAULT_WAJIB

    private var buktiUri: Uri? = null
    private var tempPhotoUri: Uri? = null

    // Galeri
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) onBuktiSelected(uri, "Galeri")
    }

    // Kamera
    private val takePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && tempPhotoUri != null) onBuktiSelected(tempPhotoUri!!, "Kamera")
        else Toast.makeText(requireContext(), "Gagal mengambil foto", Toast.LENGTH_SHORT).show()
    }

    // Izin kamera
    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) launchCamera()
        else Toast.makeText(requireContext(), "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_simpanan, container, false)

        prefManager = PrefManager(requireContext())

        tvTotalSaldo = view.findViewById(R.id.tvTotalSaldoSimpanan)
        rvHistori = view.findViewById(R.id.rvHistoriSimpanan)
        btnSetor = view.findViewById(R.id.btnSetor)
        btnTarik = view.findViewById(R.id.btnTarik)
        btnUploadBukti = view.findViewById(R.id.btnUploadBukti)
        btnAmbilFoto = view.findViewById(R.id.btnAmbilFoto)
        ivBuktiPreview = view.findViewById(R.id.ivBuktiPreview)

        inputJumlah = view.findViewById(R.id.inputJumlahSimpanan)
        dropdownJenis = view.findViewById(R.id.dropdownJenisSimpanan)

        rvHistori.layoutManager = LinearLayoutManager(requireContext())
        adapterHistori = HistoriSimpananAdapter(dataHistori)
        rvHistori.adapter = adapterHistori

        val adapterJenis = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            jenisList
        )
        dropdownJenis.setAdapter(adapterJenis)
        dropdownJenis.setOnItemClickListener { _, _, position, _ ->
            when (jenisList[position]) {
                "Simpanan Pokok" -> {
                    inputJumlah.setText(nilaiPokok.toInt().toString())
                    inputJumlah.isEnabled = false
                }
                "Simpanan Wajib" -> {
                    inputJumlah.setText(nilaiWajib.toInt().toString())
                    inputJumlah.isEnabled = false
                }
                else -> {
                    inputJumlah.setText("")
                    inputJumlah.isEnabled = true
                }
            }
        }

        loadHistoriSimpanan()

        btnSetor.setOnClickListener {
            val jenis = dropdownJenis.text.toString().trim()
            val jumlah = inputJumlah.text?.toString()?.toDoubleOrNull()
            val kodePegawai = prefManager.getKodePegawai() ?: "EMP001"

            if (jenis.isEmpty()) {
                Toast.makeText(requireContext(), "Pilih jenis simpanan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (jumlah == null || jumlah <= 0) {
                Toast.makeText(requireContext(), "Masukkan jumlah valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan ke database sebagai setoran (+jumlah)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                tambahSimpananDiDb(kodePegawai, jenis, jumlah, "-")
                withContext(Dispatchers.Main) {
                    loadHistoriSimpanan()
                    resetForm()
                    showPopup(
                        "Setoran Berhasil",
                        "Setoran $jenis sebesar Rp ${
                            String.format("%,.0f", jumlah).replace(',', '.')
                        } tercatat."
                    )
                }
            }
        }

        btnTarik.setOnClickListener {
            val jenis = dropdownJenis.text.toString().trim()
            val jumlah = inputJumlah.text?.toString()?.toDoubleOrNull()
            val kodePegawai = prefManager.getKodePegawai() ?: "EMP001"

            if (jenis.isEmpty()) {
                Toast.makeText(requireContext(), "Pilih jenis simpanan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (jumlah == null || jumlah <= 0) {
                Toast.makeText(requireContext(), "Masukkan jumlah valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan ke database sebagai penarikan (-jumlah)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                tambahSimpananDiDb(kodePegawai, jenis, -jumlah, "-")
                withContext(Dispatchers.Main) {
                    loadHistoriSimpanan()
                    resetForm()
                    showPopup(
                        "Penarikan Berhasil",
                        "Penarikan $jenis sebesar Rp ${
                            String.format("%,.0f", jumlah).replace(',', '.')
                        } tercatat."
                    )
                }
            }
        }

        btnUploadBukti.setOnClickListener { pickImage.launch("image/*") }

        btnAmbilFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            } else launchCamera()
        }

        return view
    }

    // ===========================
    // DB Helper: Simpanan & Histori
    // ===========================

    private suspend fun tambahSimpananDiDb(
        kodePegawai: String,
        jenisInput: String,
        jumlah: Double,
        keterangan: String
    ) {
        val appContext = requireContext().applicationContext
        val db = KoperasiDatabase.getInstance(appContext)
        val simpananDao = db.simpananDao()
        val historiDao = db.historiSimpananDao()

        val today = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID")).format(Date())
        val jenisBersih = jenisInput.trim()

        val isSetoran = jumlah >= 0.0
        val labelJenis = when {
            jenisBersih.contains("Pokok", ignoreCase = true) ->
                if (isSetoran) "Setoran Pokok" else "Penarikan Pokok"

            jenisBersih.contains("Wajib", ignoreCase = true) ->
                if (isSetoran) "Setoran Wajib" else "Penarikan Wajib"

            else ->
                if (isSetoran) "Setoran Sukarela" else "Penarikan Sukarela"
        }

        // Tambah ke histori_simpanan
        val maxId = historiDao.getMaxId() ?: 0
        val idBaru = maxId + 1
        val histori = HistoriSimpanan(
            id = idBaru,
            kodePegawai = kodePegawai,
            tanggal = today,
            jenis = labelJenis,
            jumlah = jumlah,
            keterangan = keterangan
        )
        historiDao.insertHistori(histori)

        // Update tabel simpanan (saldo per jenis)
        val s = simpananDao.getSimpananByKode(kodePegawai)
        if (s == null) {
            val (pokok, wajib, sukarela) = when {
                labelJenis.contains("Pokok", true) -> Triple(jumlah, 0.0, 0.0)
                labelJenis.contains("Wajib", true) -> Triple(0.0, jumlah, 0.0)
                else -> Triple(0.0, 0.0, jumlah)
            }
            simpananDao.insertAllSimpanan(listOf(Simpanan(kodePegawai, pokok, wajib, sukarela)))
        } else {
            val newPokok =
                if (labelJenis.contains("Pokok", true)) s.simpananPokok + jumlah else s.simpananPokok
            val newWajib =
                if (labelJenis.contains("Wajib", true)) s.simpananWajib + jumlah else s.simpananWajib
            val newSukarela =
                if (labelJenis.contains("Sukarela", true)) s.simpananSukarela + jumlah else s.simpananSukarela

            val updated = s.copy(
                simpananPokok = newPokok,
                simpananWajib = newWajib,
                simpananSukarela = newSukarela
            )
            simpananDao.insertAllSimpanan(listOf(updated))
        }
    }

    private fun loadHistoriSimpanan() {
        val kodePegawai = prefManager.getKodePegawai() ?: "EMP001"
        val appContext = requireContext().applicationContext

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val db = KoperasiDatabase.getInstance(appContext)
            val historiDao = db.historiSimpananDao()
            val simpananDao = db.simpananDao()

            val histori = historiDao.getHistoriForUser(kodePegawai)
            val simpanan = simpananDao.getSimpananByKode(kodePegawai)

            val total = if (simpanan != null) {
                simpanan.simpananPokok + simpanan.simpananWajib + simpanan.simpananSukarela
            } else 0.0

            withContext(Dispatchers.Main) {
                dataHistori.clear()
                dataHistori.addAll(histori)
                adapterHistori.notifyDataSetChanged()

                tvTotalSaldo.text =
                    "Total Saldo Simpanan: Rp ${
                        String.format("%,.0f", total).replace(',', '.')
                    }"
            }
        }
    }

    // ===========================
    // Bukti Pembayaran → DB
    // ===========================

    private fun launchCamera() {
        val imgDir = requireContext().cacheDir.resolve("images").apply { mkdirs() }
        val imgFile = File.createTempFile("bukti_", ".jpg", imgDir)
        tempPhotoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imgFile
        )
        takePicture.launch(tempPhotoUri)
    }

    private fun onBuktiSelected(uri: Uri, source: String) {
        buktiUri = uri
        ivBuktiPreview.visibility = View.VISIBLE
        ivBuktiPreview.setImageURI(uri)

        val kodePegawai = prefManager.getKodePegawai() ?: "EMP001"

        // Simpan bukti ke tabel bukti_pembayaran_anggota
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val appContext = requireContext().applicationContext
            val db = KoperasiDatabase.getInstance(appContext)
            val buktiDao = db.buktiPembayaranAnggotaDao()

            val maxId = buktiDao.getMaxId() ?: 0
            val idBaru = maxId + 1
            val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID")).format(Date())

            val bukti = BuktiPembayaranAnggota(
                id = idBaru,
                kodePegawai = kodePegawai,
                uri = uri.toString(),
                tanggal = tanggal
            )
            buktiDao.insertBukti(bukti)

            withContext(Dispatchers.Main) {
                showPopup(
                    "Bukti Pembayaran",
                    "Bukti dari $source berhasil diunggah.\nStatus: Menunggu verifikasi admin."
                )
            }
        }
    }

    // ===========================
    // UI Helpers
    // ===========================

    private fun resetForm() {
        inputJumlah.setText("")
        inputJumlah.isEnabled = true
        dropdownJenis.setText("", false)
        inputJumlah.clearFocus()
        dropdownJenis.clearFocus()

        view?.let { v ->
            val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    private fun showPopup(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
