package com.example.projek_map.ui.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.HistoriPembayaran
import com.example.projek_map.network.ApiClient
import com.example.projek_map.ui.adapters.HistoriPembayaranAdapter
import com.example.projek_map.utils.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class HistoriPembayaranDialog : DialogFragment() {

    private lateinit var rvHistori: RecyclerView
    private lateinit var btnUpload: Button
    private var selectedImageUri: Uri? = null
    private lateinit var adapter: HistoriPembayaranAdapter
    private val listHistori = mutableListOf<HistoriPembayaran>()

    private val PICK_IMAGE_REQUEST = 1001

    private val pinjamanId: Int by lazy {
        arguments?.getInt(ARG_PINJAMAN_ID) ?: 0
    }

    private val jumlahAngsuran: Int by lazy {
        arguments?.getInt(ARG_JUMLAH_ANGSURAN) ?: 0
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialog_histori_pembayaran, null)

        rvHistori = view.findViewById(R.id.rvHistoriPembayaran)
        btnUpload = view.findViewById(R.id.btnUploadBukti)

        rvHistori.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoriPembayaranAdapter(listHistori)
        rvHistori.adapter = adapter

        btnUpload.setOnClickListener { openGallery() }

        loadHistoriPembayaran()

        return AlertDialog.Builder(requireContext())
            .setTitle("Histori Pembayaran")
            .setView(view)
            .setPositiveButton("Tutup", null)
            .create()
    }

    private fun loadHistoriPembayaran() {
        if (pinjamanId == 0) return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = ApiClient.instance.getHistoriPembayaran(pinjamanId)
                withContext(Dispatchers.Main) {
                    if (res.isSuccessful && res.body()?.success == true) {
                        listHistori.clear()
                        listHistori.addAll(res.body()?.histori ?: emptyList())
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            res.body()?.message ?: "Gagal memuat histori pembayaran",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error koneksi: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let {
                uploadBuktiPembayaran(it)
            }
        }
    }

    private fun uploadBuktiPembayaran(uri: Uri) {
        val context = requireContext()
        val pref = PrefManager(context)
        val kodePegawai = pref.getKodePegawai() ?: return

        // convert Uri → File
        val path = getRealPathFromUri(uri) ?: run {
            Toast.makeText(context, "Gagal membaca file bukti", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(path)
        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("bukti", file.name, reqFile)

        val kodeBody = RequestBody.create("text/plain".toMediaTypeOrNull(), kodePegawai)
        val pinjamanBody = RequestBody.create("text/plain".toMediaTypeOrNull(), pinjamanId.toString())
        val jumlahBody = RequestBody.create("text/plain".toMediaTypeOrNull(), jumlahAngsuran.toString())
        val statusBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "Bukti pembayaran diupload")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = ApiClient.instance.uploadBuktiPembayaran(
                    kodeBody,
                    pinjamanBody,
                    jumlahBody,
                    statusBody,
                    body
                )
                withContext(Dispatchers.Main) {
                    if (res.isSuccessful && res.body()?.success == true) {
                        Toast.makeText(
                            context,
                            "Bukti pembayaran berhasil diupload ✅",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadHistoriPembayaran()
                    } else {
                        Toast.makeText(
                            context,
                            res.body()?.message ?: "Gagal upload bukti",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Error koneksi: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        // Implementasi simple: gunakan documentId dan MediaStore
        val context = requireContext()
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return null
    }

    companion object {
        private const val ARG_PINJAMAN_ID = "pinjaman_id"
        private const val ARG_JUMLAH_ANGSURAN = "jumlah_angsuran"

        fun newInstance(pinjamanId: Int, jumlahAngsuran: Int): HistoriPembayaranDialog {
            val args = Bundle().apply {
                putInt(ARG_PINJAMAN_ID, pinjamanId)
                putInt(ARG_JUMLAH_ANGSURAN, jumlahAngsuran)
            }
            return HistoriPembayaranDialog().apply { arguments = args }
        }
    }
}
