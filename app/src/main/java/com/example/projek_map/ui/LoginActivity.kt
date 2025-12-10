package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projek_map.data.AuthRepository
import com.example.projek_map.databinding.ActivityLoginBinding
import com.example.projek_map.utils.PrefManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: PrefManager
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = PrefManager(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil API lewat repository
            lifecycleScope.launch {
                binding.btnLogin.isEnabled = false

                val result = authRepository.login(email, password)

                binding.btnLogin.isEnabled = true

                if (result?.success == true && result.data != null) {
                    val user = result.data

                    // Simpan ke PrefManager
                    pref.saveLogin(user.nama, user.email, user.kodePegawai)

                    Toast.makeText(
                        this@LoginActivity,
                        "Login berhasil! Selamat datang ${user.nama}",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("userName", user.nama)
                    intent.putExtra("userEmail", user.email)
                    intent.putExtra("userStatusKeanggotaan", user.statusKeanggotaan)
                    intent.putExtra("userKodePegawai", user.kodePegawai)
                    intent.putExtra("isAdmin", false) // sementara semua dari API dianggap user biasa
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        result?.message ?: "Email / password salah atau server error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
