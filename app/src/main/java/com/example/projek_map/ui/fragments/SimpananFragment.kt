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
import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.SimpananTransaksiRequest
import com.example.projek_map.api.HistoriSimpanan
import com.example.projek_map.ui.adapters.HistoriSimpananAdapter
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.io.File

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

    private val nilaiPokok = 100_000.0
    private val nilaiWajib = 50_000.0

    private var buktiUri: Uri? = null
    private var tempPhotoUri: Uri? = null

    private var buktiBase64: String? = null
    private var buktiExt: String = "jpg"

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) onBuktiSelected(uri, "Galeri")
    }

    private val takePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && tempPhotoUri != null) onBuktiSelected(tempPhotoUri!!, "Kamera")
        else Toast.makeText(requireContext(), "Gagal mengambil foto", Toast.LENGTH_SHORT).show()
    }

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

            // ✅ WAJIB bukti untuk setor
            if (buktiBase64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Setor simpanan wajib upload bukti pembayaran", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val api = ApiClient.apiService

                    // ✅ potongan req yang tadi nyasar → sekarang diletakkan di tempat yang benar
                    val req = SimpananTransaksiRequest(
                        kodePegawai = kodePegawai,
                        jenisInput = jenis,
                        jumlah = jumlah,
                        keterangan = "-",
                        buktiBase64 = buktiBase64,
                        buktiExt = buktiExt
                    )
                    val resp = api.simpananTransaksi(req)

                    val body = resp.body()
                    if (resp.isSuccessful && body?.success == true) {
                        loadHistoriSimpanan()
                        resetForm()
                        resetBukti()

                        showPopup(
                            "Setoran Dikirim",
                            "Setoran $jenis sebesar Rp ${String.format("%,.0f", jumlah).replace(',', '.')} dikirim dan MENUNGGU verifikasi admin."
                        )
                    } else {
                        Toast.makeText(requireContext(), body?.message ?: "Gagal setor simpanan", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message ?: "Gagal konek ke server", Toast.LENGTH_SHORT).show()
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

            lifecycleScope.launch {
                try {
                    val api = ApiClient.apiService
                    val resp = api.simpananTransaksi(
                        SimpananTransaksiRequest(
                            kodePegawai = kodePegawai,
                            jenisInput = jenis,
                            jumlah = -jumlah,
                            keterangan = "-"
                            // tarik tidak wajib bukti
                        )
                    )

                    val body = resp.body()
                    if (resp.isSuccessful && body?.success == true) {
                        loadHistoriSimpanan()
                        resetForm()
                        showPopup(
                            "Penarikan Berhasil",
                            "Penarikan $jenis sebesar Rp ${String.format("%,.0f", jumlah).replace(',', '.')} tercatat."
                        )
                    } else {
                        Snackbar.make(view, body?.message ?: "Gagal tarik simpanan", Snackbar.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Snackbar.make(view, e.message ?: "Gagal konek ke server", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        btnUploadBukti.setOnClickListener { pickImage.launch("image/*") }

        btnAmbilFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            } else {
                launchCamera()
            }
        }

        return view
    }

    private fun launchCamera() {
        val imgFile = File.createTempFile(
            "bukti_", ".jpg",
            requireContext().cacheDir.resolve("images").apply { mkdirs() }
        )
        tempPhotoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imgFile
        )
        takePicture.launch(tempPhotoUri)
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val input = requireContext().contentResolver.openInputStream(uri) ?: return null
            val bytes = input.readBytes()
            input.close()
            android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
        } catch (e: Exception) { null }
    }

    private fun onBuktiSelected(uri: Uri, source: String) {
        buktiUri = uri
        ivBuktiPreview.visibility = View.VISIBLE
        ivBuktiPreview.setImageURI(uri)

        // ✅ PERUBAHAN: hanya simpan base64 (tidak hit endpoint terpisah)
        buktiBase64 = uriToBase64(uri)
        buktiExt = "jpg"

        if (buktiBase64.isNullOrBlank()) {
            showPopup("Bukti Pembayaran", "Gagal membaca gambar dari $source. Coba ulangi.")
            resetBukti()
            return
        }

        showPopup("Bukti Pembayaran", "Bukti dipilih dari $source. Silakan tekan SETOR untuk mengirim.")
    }

    private fun resetBukti() {
        buktiUri = null
        tempPhotoUri = null
        buktiBase64 = null
        buktiExt = "jpg"
        ivBuktiPreview.setImageDrawable(null)
        ivBuktiPreview.visibility = View.GONE
    }

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

    private fun loadHistoriSimpanan() {
        val kodePegawai = prefManager.getKodePegawai() ?: "EMP001"

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val api = ApiClient.apiService

                val simpananResp = api.getSimpanan(kodePegawai)
                val simpananBody = simpananResp.body()

                val historiResp = api.getHistoriSimpananByKodePegawai(kodePegawai)
                val historiBody = historiResp.body()

                dataHistori.clear()
                if (historiResp.isSuccessful && historiBody?.success == true) {
                    dataHistori.addAll(historiBody.data.orEmpty())
                }
                adapterHistori.notifyDataSetChanged()

                val s = if (simpananResp.isSuccessful && simpananBody?.success == true) simpananBody.data else null
                val total = if (s != null) (s.simpananPokok + s.simpananWajib + s.simpananSukarela) else 0.0

                tvTotalSaldo.text = "Total Saldo Simpanan: Rp ${
                    String.format("%,.0f", total).replace(',', '.')
                }"
            } catch (e: Exception) {
                tvTotalSaldo.text = "Total Saldo Simpanan: Rp 0"
                Toast.makeText(requireContext(), e.message ?: "Gagal ambil data simpanan", Toast.LENGTH_SHORT).show()
            }
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
