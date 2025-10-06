package com.example.projek_map

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class EditProfilFragment : Fragment() {

    private lateinit var imgEditProfile: ImageView
    private lateinit var tvNamaUser: TextView
    private lateinit var tvEmailLama: TextView
    private lateinit var tvTeleponLama: TextView
    private lateinit var etEmailBaru: EditText
    private lateinit var etTeleponBaru: EditText
    private lateinit var etPasswordBaru: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSimpan: MaterialButton
    private lateinit var btnBatal: MaterialButton
    private lateinit var btnChangePhoto: MaterialButton

    private var selectedImageUri: Uri? = null

    // ✅ Launcher baru pengganti onActivityResult
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedImageUri = result.data?.data
            imgEditProfile.setImageURI(selectedImageUri)
        } else {
            Toast.makeText(requireContext(), "Batal memilih foto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgEditProfile = view.findViewById(R.id.imgEditProfile)
        tvNamaUser = view.findViewById(R.id.tvNamaUserEdit)
        tvEmailLama = view.findViewById(R.id.tvEmailLama)
        tvTeleponLama = view.findViewById(R.id.tvTeleponLama)
        etEmailBaru = view.findViewById(R.id.etEmailBaru)
        etTeleponBaru = view.findViewById(R.id.etTeleponBaru)
        etPasswordBaru = view.findViewById(R.id.etPasswordBaru)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        btnSimpan = view.findViewById(R.id.btnSimpan)
        btnBatal = view.findViewById(R.id.btnBatal)
        btnChangePhoto = view.findViewById(R.id.btnChangePhoto)

        // Dummy data
        tvNamaUser.text = "Andi Setiawan"
        tvEmailLama.text = "Email lama: andi@gmail.com"
        tvTeleponLama.text = "Nomor lama: 08123456789"

        // Ganti Foto Profil
        btnChangePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }

        // Simpan Perubahan
        btnSimpan.setOnClickListener {
            val emailBaru = etEmailBaru.text.toString()
            val teleponBaru = etTeleponBaru.text.toString()
            val passBaru = etPasswordBaru.text.toString()
            val confirmPass = etConfirmPassword.text.toString()

            if (passBaru != confirmPass) {
                Toast.makeText(requireContext(), "Password tidak sama!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(requireContext(), "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        // Batal
        btnBatal.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
