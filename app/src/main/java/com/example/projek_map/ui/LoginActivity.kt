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

            lifecycleScope.launch {
                binding.btnLogin.isEnabled = false

                // =========================
                // 1️⃣ COBA LOGIN ADMIN DULU
                // =========================
                val adminResult = authRepository.loginAdmin(email, password)
                if (adminResult?.success == true && adminResult.data != null) {
                    val admin = adminResult.data

                    pref.logout() // 🔒 PENTING: bersihkan session lama
                    pref.saveLogin(admin.nama, admin.email, admin.kodePegawai)
                    pref.setIsAdmin(true)

                    Toast.makeText(
                        this@LoginActivity,
                        "Login admin berhasil! Selamat datang ${admin.nama}",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                    return@launch
                }

                // =========================
                // 2️⃣ BARU COBA LOGIN USER
                // =========================
                val userResult = authRepository.login(email, password)
                if (userResult?.success == true && userResult.data != null) {
                    val user = userResult.data

                    pref.logout() // 🔒 bersihkan session lama
                    pref.saveLogin(user.nama, user.email, user.kodePegawai)
                    pref.setIsAdmin(false)

                    Toast.makeText(
                        this@LoginActivity,
                        "Login berhasil! Selamat datang ${user.nama}",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                    return@launch
                }

                binding.btnLogin.isEnabled = true
                Toast.makeText(
                    this@LoginActivity,
                    adminResult?.message
                        ?: userResult?.message
                        ?: "Email / password salah",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
