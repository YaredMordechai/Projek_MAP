package com.example.projek_map.ui.dialogs

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R

@Suppress("DEPRECATION")
class   HistoriPembayaranDialog : DialogFragment() {

    private lateinit var rvHistori: RecyclerView
    private lateinit var btnUpload: Button
    private var selectedImageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1001

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_histori_pembayaran, null)

        rvHistori = view.findViewById(R.id.rvHistoriPembayaran)
        btnUpload = view.findViewById(R.id.btnUploadBukti)

        btnUpload.setOnClickListener {
            openGallery()
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Histori Pembayaran")
            .setView(view)
            .setPositiveButton("Tutup", null)
            .create()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let {
                Toast.makeText(requireContext(), "Bukti pembayaran berhasil dipilih ✅", Toast.LENGTH_SHORT).show()

                // Simulasikan upload bukti ke server / penyimpanan lokal
                uploadBuktiPembayaran(it)
            }
        }
    }

    private fun uploadBuktiPembayaran(uri: Uri) {
        // Di tahap UTS: cukup simulasi upload
        // Jika nanti disambungkan ke API, bisa kirim multipart di sini
        Toast.makeText(requireContext(), "Bukti pembayaran tersimpan: $uri", Toast.LENGTH_LONG).show()
    }
}
