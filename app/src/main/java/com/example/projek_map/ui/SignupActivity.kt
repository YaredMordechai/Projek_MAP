package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.User
import com.example.projek_map.databinding.ActivitySignupBinding
import com.example.projek_map.utils.PrefManager

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)

        binding.btnSignup.setOnClickListener {
            val kodePegawai = binding.etKodePegawai.text.toString().trim()
            val nama = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (kodePegawai.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek apakah user sudah terdaftar
            val exists = DummyUserData.users.any {
                it.email.equals(email, ignoreCase = true) || it.kodePegawai == kodePegawai
            }

            if (exists) {
                Toast.makeText(this, "User sudah terdaftar!", Toast.LENGTH_SHORT).show()
            } else {
                // Buat user baru (dengan nilai default)
                val newUser = User(
                    kodePegawai = kodePegawai,
                    email = email,
                    password = password,
                    nama = nama,
                    telepon = "-",
                    statusKeanggotaan = "Aktif"
                )

                // Tambahkan user ke data dummy
                DummyUserData.users.add(newUser)

                // Simpan ke PrefManager (sesi login)
                prefManager.saveLogin(
                    userName = nama,
                    email = email,
                    kodePegawai = kodePegawai
                )

                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()

                // Arahkan ke halaman login
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
