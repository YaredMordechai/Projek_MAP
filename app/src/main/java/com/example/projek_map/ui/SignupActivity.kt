package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.databinding.ActivitySignupBinding
import com.example.projek_map.utils.PrefManager
import com.example.projek_map.viewmodel.KoperasiViewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var pref: PrefManager

    private val viewModel: KoperasiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = PrefManager(this)

        // observe hasil register
        viewModel.registerResult.observe(this) { user ->
            if (user != null) {
                // simpan login
                pref.saveLogin(user.nama, user.email, user.kodePegawai)

                Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()

                // langsung masuk MainActivity dengan extras lengkap
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("userName", user.nama)
                    putExtra("userEmail", user.email)
                    putExtra("userStatusKeanggotaan", user.statusKeanggotaan)
                    putExtra("userKodePegawai", user.kodePegawai)
                    putExtra("isAdmin", false)
                }
                startActivity(intent)
                finish()
            }
        }

        viewModel.registerError.observe(this) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignup.setOnClickListener {
            val kodePegawai = binding.etKodePegawai.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val nama = binding.etNama.text.toString().trim()

            if (kodePegawai.isEmpty() || email.isEmpty() || password.isEmpty() || nama.isEmpty()) {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(kodePegawai, email, password, nama)
        }
    }
}
