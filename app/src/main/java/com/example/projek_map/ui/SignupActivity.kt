package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.User
import com.example.projek_map.databinding.ActivitySignupBinding
import com.example.projek_map.utils.PrefManager
import com.example.projek_map.ui.MainActivity
import com.google.android.material.button.MaterialButton

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = PrefManager(this)

        binding.btnSignup.setOnClickListener {
            val kodePegawai = binding.etKodePegawai.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val nama = binding.etNama.text.toString().trim()

            if (kodePegawai.isEmpty() || email.isEmpty() || password.isEmpty() || nama.isEmpty()) {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek apakah user sudah terdaftar
            val existingUser = DummyUserData.users.find {
                it.email.equals(email, true) || it.kodePegawai.equals(kodePegawai, true)
            }

            if (existingUser != null) {
                Toast.makeText(this, "User sudah terdaftar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tambah user baru ke dummy list
            val newUser = User(
                kodePegawai = kodePegawai,
                email = email,
                password = password,
                nama = nama,
                telepon = "-",
                statusKeanggotaan = "Anggota Baru"
            )
            // This line will now work correctly without the wrong import
            DummyUserData.users.add(newUser)

            // Simpan data login ke PrefManager
            pref.saveLogin(nama, email, kodePegawai)

            Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()

            // Pindah ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
